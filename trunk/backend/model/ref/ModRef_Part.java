package backend.model.ref;

import backend.model.Group;
import backend.model.Part;

public class ModRef_Part extends ModelReference{

	private final int uniquePartID;
	
	public ModRef_Part(int uniquePartID){
		super(ModRefType.Part);
		this.uniquePartID = uniquePartID;
	}
	
	@Override
	public String getStringReferenceInfo() {
		return "PartID:" + uniquePartID;
	}
	
	public Part getPart(Group group){
		return group.getPartByUniqueID(uniquePartID);
	}
	
}
