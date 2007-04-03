package backend.model.ref;

import backend.model.Feature2D3D;
import backend.model.Feature3D3D;
import backend.model.Part;
import backend.model.sketch.SketchPlane;

public class ModRef_Plane extends ModelReference{

	private final int uniqueSubPartID;
	private final int uniqueFaceID;
	
	// planes are built with reference to a particular part, feat2D3D, and faceID within that feature.
	
	/**
	 * a plane reference is build upon an existing SubPart 
	 * that has a unique face associated with it.
	 * @param uniqueSubPartID the unique ID of the SubPart to use
	 * @param uniqueFaceID the unique ID of the face created by the SubPart.
	 */
	public ModRef_Plane(int uniqueSubPartID, int uniqueFaceID){
		super(ModRefType.Plane);
		this.uniqueSubPartID = uniqueSubPartID;
		this.uniqueFaceID    = uniqueFaceID;
	}
	
	public SketchPlane getSketchPlane(Part part){
		Feature2D3D feat2D3D = part.getFeat2D3DByID(uniqueSubPartID);
		if(feat2D3D != null){
			// get the sketch plane from the feature2D3D
			return feat2D3D.getPlaneByFaceID(uniqueFaceID);
		}else{
			Feature3D3D feat3D3D = part.getFeat3D3DByID(uniqueSubPartID);
			if(feat3D3D != null){
				// get the sketch plane from the feature3D3D
				return feat3D3D.getPlaneByFaceID(uniqueFaceID);
			}
		}
		System.out.println("ModRef_Plane(getSketchPlane): Could not get feat2D3D/feat3D3D for sketch plane! FIX THIS NOW!");
		return null;
	}
	
	@Override
	public String getStringReferenceInfo() {
		return "SubPartID:" + uniqueSubPartID + ", FaceID:" + uniqueFaceID;
	}
	
}
