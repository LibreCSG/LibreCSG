package backend.model.ref;

import backend.model.Part;
import backend.model.Sketch;

public class ModRef_Sketch extends ModelReference{

	private final int uniqueSubPartID;
	
	public ModRef_Sketch(int uniqueSubPartID){
		super(ModRefType.Sketch);
		this.uniqueSubPartID = uniqueSubPartID;
	}
	
	@Override
	public String getStringReferenceInfo() {
		return "SubPartID:" + uniqueSubPartID;
	}
	
	public Sketch getSketch(Part part){
		return part.getSketchByID(uniqueSubPartID);
	}

}
