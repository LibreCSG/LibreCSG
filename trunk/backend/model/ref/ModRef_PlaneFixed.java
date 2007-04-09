package backend.model.ref;

import backend.model.Part;
import backend.model.sketch.SketchPlane;

public class ModRef_PlaneFixed extends ModRef_Plane{

	private final SketchPlane fixedSketchPlane;
	
	public ModRef_PlaneFixed(SketchPlane fixedSketchPlane) {
		super(-1, -1);
		this.fixedSketchPlane = fixedSketchPlane;
	}
	
	public SketchPlane getSketchPlane(Part part){
		if(fixedSketchPlane != null){
			return fixedSketchPlane;
		}else{
			System.out.println("ModRef_PlaneRoot(getSketchPlane): rootPlaneType was NULL! whah?!?");
			return null;
		}
	}

	
}
