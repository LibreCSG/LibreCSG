package backend.model.ref;

import backend.model.Feature3D3D;
import backend.model.Part;

public class ModRef_Modify extends ModelReference{

	private final int uniqueSubPartID;
	
	public ModRef_Modify(int uniqueSubPartID){
		super(ModRefType.Modify);
		this.uniqueSubPartID = uniqueSubPartID;
	}
	
	@Override
	public String getStringReferenceInfo() {
		return "SubPartID:" + uniqueSubPartID;
	}
	
	public Feature3D3D getModify(Part part){
		return part.getFeat3D3DByID(uniqueSubPartID);
	}
	

}
