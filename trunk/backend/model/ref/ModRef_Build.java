package backend.model.ref;

import backend.model.Feature2D3D;
import backend.model.Part;

public class ModRef_Build extends ModelReference{

	private final int uniqueSubPartID;
	
	public ModRef_Build(int uniqueSubPartID){
		super(ModRefType.Build);
		this.uniqueSubPartID = uniqueSubPartID;
	}
	
	@Override
	public String getStringReferenceInfo() {
		return "SubPartID:" + uniqueSubPartID;
	}

	public Feature2D3D getBuild(Part part){
		return part.getFeat2D3DByID(uniqueSubPartID);
	}
	
}
