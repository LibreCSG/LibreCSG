package ui.tools.part;

import java.util.Iterator;

import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolCtrlPart;
import backend.global.AvoGlobal;
import backend.model.Part;
import backend.model.CSG.CSG_Face;
import backend.model.CSG.CSG_Vertex;

/**
 * default controller to use when in the "Part" menuet mode.
 * the basic functionality includes selecting of CSG_Faces
 * on which a new sketch can be started.
 */
public class ToolPartDefaultCtrl implements ToolCtrlPart{

	private final double MIN_DIST_FROM_FACE = 0.1;
	
	public void glMouseDown(double x, double y, double z, MouseEvent e) {
		Part part = AvoGlobal.project.getActivePart();
		if(part != null){
			CSG_Vertex clickedVert = new CSG_Vertex(x, y, z);
			Iterator<CSG_Face> faceIter = part.getSolid().getFacesIter();
			while(faceIter.hasNext()){
				CSG_Face face = faceIter.next();
				if(face.isSelectable() && 
						Math.abs(face.distFromVertexToFacePlane(clickedVert)) < MIN_DIST_FROM_FACE &&
						face.vertexIsInsideFace(clickedVert)){
					// a selectable face was clicked!
					System.out.println("You selected a selectable face! " + face.getModRefPlane());
					face.setSelected(true);
					part.setSelectedPlane(face.getModRefPlane());
				}else{
					face.setSelected(false);
				}
			}
			AvoGlobal.glView.updateGLView = true;			
		}
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void glMouseUp(double x, double y, double z, MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void menuetElementDeselected() {
		// TODO Auto-generated method stub
		
	}

	public void menuetElementSelected() {
		// TODO Auto-generated method stub
		
	}

}
