















package ca.uhn.fhir.model.dstu.composite;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

/**
 * HAPI/FHIR <b>Coding</b> Datatype
 * (A reference to a code defined by a terminology system)
 *
 * <p>
 * <b>Definition:</b>
 * A reference to a code defined by a terminology system
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * References to codes are very common in healthcare models
 * </p> 
 */
@DatatypeDef(name="Coding") 
public class CodingDt 
        extends  BaseElement         implements ICompositeDatatype  , IQueryParameterType {

	/**
	 * Creates a new Coding
	 */
	public CodingDt() {
		// nothing
	}

	/**
	 * Creates a new Coding with the given system and code
	 */
	public CodingDt(String theSystem, String theCode) {
		setSystem(theSystem);
		setCode(theCode);
	}

	@Child(name="system", type=UriDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Identity of the terminology system",
		formalDefinition="The identification of the code system that defines the meaning of the symbol in the code."
	)
	private UriDt mySystem;
	
	@Child(name="version", type=StringDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Version of the system - if relevant",
		formalDefinition="The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and When the meaning is not guaranteed to be consistent, the version SHOULD be exchanged"
	)
	private StringDt myVersion;
	
	@Child(name="code", type=CodeDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Symbol in syntax defined by the system",
		formalDefinition="A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)"
	)
	private CodeDt myCode;
	
	@Child(name="display", type=StringDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="Representation defined by the system",
		formalDefinition="A representation of the meaning of the code in the system, following the rules of the system."
	)
	private StringDt myDisplay;
	
	@Child(name="primary", type=BooleanDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="If this code was chosen directly by the user",
		formalDefinition="Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays)"
	)
	private BooleanDt myPrimary;
	
	@Child(name="valueSet", order=5, min=0, max=1, type={
		ca.uhn.fhir.model.dstu.resource.ValueSet.class,
	})
	@Description(
		shortDefinition="Set this coding was chosen from",
		formalDefinition="The set of possible coded values this coding was chosen from or constrained by"
	)
	private ResourceReferenceDt myValueSet;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  mySystem,  myVersion,  myCode,  myDisplay,  myPrimary,  myValueSet);
	}
	
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(  mySystem,  myVersion,  myCode,  myDisplay,  myPrimary,  myValueSet);
	}
	

	/**
	 * Gets the value(s) for <b>system</b> (Identity of the terminology system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the code system that defines the meaning of the symbol in the code.
     * </p> 
	 */
	public UriDt getSystem() {  
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	/**
	 * Sets the value(s) for <b>system</b> (Identity of the terminology system)
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the code system that defines the meaning of the symbol in the code.
     * </p> 
	 */
	public void setSystem(UriDt theValue) {
		mySystem = theValue;
	}


 	/**
	 * Sets the value for <b>system</b> (Identity of the terminology system)
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the code system that defines the meaning of the symbol in the code.
     * </p> 
	 */
	public void setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
	}

 
	/**
	 * Gets the value(s) for <b>version</b> (Version of the system - if relevant).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and When the meaning is not guaranteed to be consistent, the version SHOULD be exchanged
     * </p> 
	 */
	public StringDt getVersion() {  
		if (myVersion == null) {
			myVersion = new StringDt();
		}
		return myVersion;
	}

	/**
	 * Sets the value(s) for <b>version</b> (Version of the system - if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and When the meaning is not guaranteed to be consistent, the version SHOULD be exchanged
     * </p> 
	 */
	public void setVersion(StringDt theValue) {
		myVersion = theValue;
	}


 	/**
	 * Sets the value for <b>version</b> (Version of the system - if relevant)
	 *
     * <p>
     * <b>Definition:</b>
     * The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and When the meaning is not guaranteed to be consistent, the version SHOULD be exchanged
     * </p> 
	 */
	public void setVersion( String theString) {
		myVersion = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (Symbol in syntax defined by the system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
     * </p> 
	 */
	public CodeDt getCode() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Symbol in syntax defined by the system)
	 *
     * <p>
     * <b>Definition:</b>
     * A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
     * </p> 
	 */
	public void setCode(CodeDt theValue) {
		myCode = theValue;
	}


 	/**
	 * Sets the value for <b>code</b> (Symbol in syntax defined by the system)
	 *
     * <p>
     * <b>Definition:</b>
     * A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
     * </p> 
	 */
	public void setCode( String theCode) {
		myCode = new CodeDt(theCode); 
	}

 
	/**
	 * Gets the value(s) for <b>display</b> (Representation defined by the system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A representation of the meaning of the code in the system, following the rules of the system.
     * </p> 
	 */
	public StringDt getDisplay() {  
		if (myDisplay == null) {
			myDisplay = new StringDt();
		}
		return myDisplay;
	}

	/**
	 * Sets the value(s) for <b>display</b> (Representation defined by the system)
	 *
     * <p>
     * <b>Definition:</b>
     * A representation of the meaning of the code in the system, following the rules of the system.
     * </p> 
	 */
	public void setDisplay(StringDt theValue) {
		myDisplay = theValue;
	}


 	/**
	 * Sets the value for <b>display</b> (Representation defined by the system)
	 *
     * <p>
     * <b>Definition:</b>
     * A representation of the meaning of the code in the system, following the rules of the system.
     * </p> 
	 */
	public void setDisplay( String theString) {
		myDisplay = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>primary</b> (If this code was chosen directly by the user).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays)
     * </p> 
	 */
	public BooleanDt getPrimary() {  
		if (myPrimary == null) {
			myPrimary = new BooleanDt();
		}
		return myPrimary;
	}

	/**
	 * Sets the value(s) for <b>primary</b> (If this code was chosen directly by the user)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays)
     * </p> 
	 */
	public void setPrimary(BooleanDt theValue) {
		myPrimary = theValue;
	}


 	/**
	 * Sets the value for <b>primary</b> (If this code was chosen directly by the user)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays)
     * </p> 
	 */
	public void setPrimary( Boolean theBoolean) {
		myPrimary = new BooleanDt(theBoolean); 
	}

 
	/**
	 * Gets the value(s) for <b>valueSet</b> (Set this coding was chosen from).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The set of possible coded values this coding was chosen from or constrained by
     * </p> 
	 */
	public ResourceReferenceDt getValueSet() {  
		if (myValueSet == null) {
			myValueSet = new ResourceReferenceDt();
		}
		return myValueSet;
	}

	/**
	 * Sets the value(s) for <b>valueSet</b> (Set this coding was chosen from)
	 *
     * <p>
     * <b>Definition:</b>
     * The set of possible coded values this coding was chosen from or constrained by
     * </p> 
	 */
	public void setValueSet(ResourceReferenceDt theValue) {
		myValueSet = theValue;
	}


  

	/**
	 * Returns true if <code>this</code> Coding has the same {@link CodingDt#getCode() Code}
	 * and {@link CodingDt#getSystem() system} (as compared by simple equals comparison).
	 * Does not compare other Codes (e.g. {@link CodingDt#getUse() use}) or any extensions. 
	 */
	public boolean matchesSystemAndCode(CodingDt theCoding) {
		if (theCoding == null) {
			return false;
		}
		return getCode().equals(theCoding.getCode()) && getSystem().equals(theCoding.getSystem());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValueAsQueryToken() {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(getSystem().getValueAsString())) {
			return getSystem().getValueAsString() + '|' + getCode().getValueAsString(); 
		} else {
			return getCode().getValueAsString();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAsQueryToken(String theParameter) {
		int barIndex = theParameter.indexOf('|');
		if (barIndex != -1) {
			setSystem(new UriDt(theParameter.substring(0, barIndex)));
			setCode(theParameter.substring(barIndex + 1));
		} else {
			setCode(theParameter);
		}
	}	


}