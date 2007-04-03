package backend.model.ref;

import backend.model.Group;
import backend.model.Project;

public class ModRef_Group extends ModelReference{

	private final int uniqueGroupID;
	
	public ModRef_Group(int uniqueGroupID){
		super(ModRefType.Group);
		this.uniqueGroupID = uniqueGroupID;
	}
	
	@Override
	public String getStringReferenceInfo() {
		return "GroupID:" + uniqueGroupID;
	}

	public Group getGroup(Project project){
		return project.getGroupByUniqueID(uniqueGroupID);
	}
	
}
