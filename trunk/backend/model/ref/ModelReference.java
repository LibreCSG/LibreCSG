package backend.model.ref;


/**
 * Base class for all model refernces.
 *
 */
public abstract class ModelReference {

	private ModRefType modelReferenceType; 
	
	public ModelReference(ModRefType refType){
		if(refType != null){
			modelReferenceType = refType;
		}else{
			modelReferenceType = ModRefType.Invalid;
		}
	}
	
	/**
	 * String representation of this model reference.
	 */
	public String toString(){
		return "ModelReference(" + modelReferenceType + "):{" + getStringReferenceInfo() + "}";
	}
	
	/**
	 * @return a string representing the <b>unique information about this ModelReference</b>.
	 * This information will be placed after a preface string in the toString() method
	 * of the <i>abstract class ModelReference</i> to which this model reference subclasses.
	 */
	public abstract String getStringReferenceInfo();
	
	
}
