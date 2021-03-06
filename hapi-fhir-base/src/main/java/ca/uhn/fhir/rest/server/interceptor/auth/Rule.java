package ca.uhn.fhir.rest.server.interceptor.auth;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.BundleUtil.BundleEntryParts;
import ca.uhn.fhir.util.FhirTerser;

class Rule implements IAuthRule {

	private AppliesTypeEnum myAppliesTo;
	private Set<?> myAppliesToTypes;
	private String myClassifierCompartmentName;
	private Collection<? extends IIdType> myClassifierCompartmentOwners;
	private ClassifierTypeEnum myClassifierType;
	private PolicyEnum myMode;
	private String myName;
	private RuleOpEnum myOp;
	private TransactionAppliesToEnum myTransactionAppliesToOp;

	public Rule(String theRuleName) {
		myName = theRuleName;
	}

	@Override
	public Verdict applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IBaseResource theOutputResource, IRuleApplier theRuleApplier) {
		FhirContext ctx = theRequestDetails.getServer().getFhirContext();

		IBaseResource appliesTo;
		switch (myOp) {
		case READ:
			if (theOutputResource == null) {
				return null;
			}
			appliesTo = theOutputResource;
			break;
		case WRITE:
			if (theInputResource == null) {
				return null;
			}
			appliesTo = theInputResource;
			break;
		case DELETE:
			if (theOperation == RestOperationTypeEnum.DELETE) {
				if (theInputResource == null) {
					return new Verdict(myMode, this);
				} else {
					appliesTo = theInputResource;
				}
			} else {
				return null;
			}
			break;
		case BATCH:
		case TRANSACTION:
			if (theInputResource != null && requestAppliesToTransaction(ctx, myOp, theInputResource)) {
				if (myMode == PolicyEnum.DENY) {
					return new Verdict(PolicyEnum.DENY, this);
				} else  {
					List<BundleEntryParts> inputResources = BundleUtil.toListOfEntries(ctx, (IBaseBundle) theInputResource);
					Verdict verdict = null;
					for (BundleEntryParts nextPart : inputResources) {
						
						IBaseResource inputResource = nextPart.getResource();
						RestOperationTypeEnum operation = null;
						if (nextPart.getRequestType() == RequestTypeEnum.GET) {
							continue;
						}
						if (nextPart.getRequestType() == RequestTypeEnum.POST) {
							operation = RestOperationTypeEnum.CREATE;
						} else if (nextPart.getRequestType() == RequestTypeEnum.PUT) {
							operation = RestOperationTypeEnum.UPDATE;
						} else {
							throw new InvalidRequestException("Can not handle transaction with operation of type " + nextPart.getRequestType());
						}

						/*
						 * This is basically just being conservative - Be careful of transactions containing
						 * nested operations and nested transactions. We block the by default. At some point
						 * it would be nice to be more nuanced here. 
						 */
						RuntimeResourceDefinition resourceDef = ctx.getResourceDefinition(nextPart.getResource());
						if ("Parameters".equals(resourceDef.getName()) || "Bundle".equals(resourceDef.getName())) {
							throw new InvalidRequestException("Can not handle transaction with nested resource of type " + resourceDef.getName());							
						}
						
						Verdict newVerdict = theRuleApplier.applyRulesAndReturnDecision(operation, theRequestDetails, inputResource, null);
						if (newVerdict == null) {
							continue;
						} else if (verdict == null) {
							verdict = newVerdict;
						} else if (verdict.getDecision() == PolicyEnum.ALLOW && newVerdict.getDecision() == PolicyEnum.DENY) {
							verdict = newVerdict;
						}
					}
					return verdict;
				}
			} else if (theOutputResource != null) {
				List<BundleEntryParts> inputResources = BundleUtil.toListOfEntries(ctx, (IBaseBundle) theInputResource);
				Verdict verdict = null;
				for (BundleEntryParts nextPart : inputResources) {
					if (nextPart.getResource() == null) {
						continue;
					}
					Verdict newVerdict = theRuleApplier.applyRulesAndReturnDecision(RestOperationTypeEnum.READ, theRequestDetails, null, nextPart.getResource());
					if (newVerdict == null) {
						continue;
					} else if (verdict == null) {
						verdict = newVerdict;
					} else if (verdict.getDecision() == PolicyEnum.ALLOW && newVerdict.getDecision() == PolicyEnum.DENY) {
						verdict = newVerdict;
					}
				}
				return verdict;
			} else {
				return null;
			}
		case ALLOW_ALL:
			return new Verdict(PolicyEnum.ALLOW, this);
		case DENY_ALL:
			return new Verdict(PolicyEnum.DENY, this);
		case METADATA:
			if (theOperation == RestOperationTypeEnum.METADATA) {
				return new Verdict(myMode, this);
			} else {
				return null;
			}
		default:
			// Should not happen
			throw new IllegalStateException("Unable to apply security to event of type " + theOperation);
		}

		switch (myAppliesTo) {
		case ALL_RESOURCES:
			break;
		case TYPES:
			if (myAppliesToTypes.contains(appliesTo.getClass()) == false) {
				return null;
			}
			break;
		default:
			throw new IllegalStateException("Unable to apply security to event of applies to type " + myAppliesTo);
		}

		switch (myClassifierType) {
		case ANY_ID:
			break;
		case IN_COMPARTMENT:
			FhirTerser t = ctx.newTerser();
			boolean foundMatch = false;
			for (IIdType next : myClassifierCompartmentOwners) {
				if (t.isSourceInCompartmentForTarget(myClassifierCompartmentName, appliesTo, next)) {
					foundMatch = true;
					break;
				}
			}
			if (!foundMatch) {
				return null;
			}
			break;
		default:
			throw new IllegalStateException("Unable to apply security to event of applies to type " + myAppliesTo);
		}

		return new Verdict(myMode, this);
	}

	private boolean requestAppliesToTransaction(FhirContext theContext, RuleOpEnum theOp, IBaseResource theInputResource) {
		if (!"Bundle".equals(theContext.getResourceDefinition(theInputResource).getName())) {
			return false;
		}
		
		IBaseBundle request = (IBaseBundle) theInputResource;
		String bundleType = BundleUtil.getBundleType(theContext, request);
		switch (theOp) {
		case TRANSACTION:
			return "transaction".equals(bundleType);
		case BATCH:
			return "batch".equals(bundleType);
		default:
			return false;
		}
	}

	@Override
	public String getName() {
		return myName;
	}

	public TransactionAppliesToEnum getTransactionAppliesToOp() {
		return myTransactionAppliesToOp;
	}

	public void setAppliesTo(AppliesTypeEnum theAppliesTo) {
		myAppliesTo = theAppliesTo;
	}

	public void setAppliesToTypes(Set<?> theAppliesToTypes) {
		myAppliesToTypes = theAppliesToTypes;
	}

	public void setClassifierCompartmentName(String theClassifierCompartmentName) {
		myClassifierCompartmentName = theClassifierCompartmentName;
	}

	public void setClassifierCompartmentOwners(Collection<? extends IIdType> theInCompartmentOwners) {
		myClassifierCompartmentOwners = theInCompartmentOwners;
	}

	public void setClassifierType(ClassifierTypeEnum theClassifierType) {
		myClassifierType = theClassifierType;
	}

	public void setMode(PolicyEnum theRuleMode) {
		myMode = theRuleMode;
	}

	public Rule setOp(RuleOpEnum theRuleOp) {
		myOp = theRuleOp;
		return this;
	}

	public void setTransactionAppliesToOp(TransactionAppliesToEnum theOp) {
		myTransactionAppliesToOp = theOp;
	}
}
