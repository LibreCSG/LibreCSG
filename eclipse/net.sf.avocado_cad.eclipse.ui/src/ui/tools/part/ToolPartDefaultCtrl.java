package ui.tools.part;

import java.util.Iterator;

import net.sf.avocado_cad.eclipse.ui.AvoGlobal;
import net.sf.avocado_cad.model.api.adt.IParamSet;
import net.sf.avocado_cad.model.api.csg.ICSGVertex;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolCtrlPart;
import backend.adt.ParamSet;
import backend.model.Part;
import backend.model.CSG.CSG_Face;
import backend.model.CSG.CSG_Vertex;


//
//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the 
//GNU General Public License (GPL).
//
//This file is part of avoCADo.
//
//AvoCADo is free software; you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation; either version 2 of the License, or
//(at your option) any later version.
//
//AvoCADo is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with AvoCADo; if not, write to the Free Software
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

/*
* @author  Adam Kumpf
* @created Apr. 2007
*/

/**
 * default controller to use when in the "Part" menuet mode.
 * the basic functionality includes selecting of CSG_Faces
 * on which a new sketch can be started.
 */
public class ToolPartDefaultCtrl implements ToolCtrlPart{

	private final double MAX_DIST_FROM_FACE = 0.1;
	
	public void glMouseDown(double x, double y, double z, MouseEvent e, IParamSet paramSet) {
		long startTime = System.nanoTime();
		Part part = (Part) AvoGlobal.project.getActivePart();
		if(part != null){
			ICSGVertex clickedVert = new CSG_Vertex(x, y, z);
			double closestDistSoFar = MAX_DIST_FROM_FACE;
			CSG_Face faceToSelect = null;
			for(CSG_Face face : part.getSolid().getFaces()) {
				face.setSelected(false);
				if(face.isSelectable() && 
						Math.abs(face.distFromVertexToFacePlane(clickedVert)) < closestDistSoFar &&
						face.vertexIsInsideFace(clickedVert)){
					// a selectable face was clicked!
					faceToSelect = face;					
					closestDistSoFar = Math.abs(face.distFromVertexToFacePlane(clickedVert));
				}else{
					face.setSelected(false);
				}
			}
			if(faceToSelect != null){
				System.out.println("You selected a selectable face! " + faceToSelect.getModRefPlane());
				faceToSelect.setSelected(true);
				part.setSelectedPlane(faceToSelect.getModRefPlane());
			}
			
			// TODO: this belong in constraint/mating code, not here...
			// -------- TEST
			closestDistSoFar = MAX_DIST_FROM_FACE;
			CSG_Face faceWithArc = null;
			for(CSG_Face face : part.getSolid().getFaces()) {
				if(face.getModRefCylinder() != null && 
						Math.abs(face.distFromVertexToFacePlane(clickedVert)) < closestDistSoFar &&
						face.vertexIsInsideFace(clickedVert)){
					// a selectable face was clicked!
					System.out.println("*");
					faceWithArc = face;					
					closestDistSoFar = Math.abs(face.distFromVertexToFacePlane(clickedVert));
				}
			}
			if(faceWithArc != null){
				System.out.println("You selected a selectable ARC face! " + faceWithArc.getModRefCylinder());
			}
			// -----
			
			AvoGlobal.updateGLView = true;			
		}
		long endTime = System.nanoTime();
		//System.out.println("Time to search for clicked face: " + (endTime-startTime)/1e6 + "mSec");
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e, IParamSet paramSet) {
		// TODO Auto-generated method stub
		
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e, IParamSet paramSet) {
		// TODO Auto-generated method stub
		
	}

	public void glMouseUp(double x, double y, double z, MouseEvent e, IParamSet paramSet) {
		// TODO Auto-generated method stub
		
	}

	public void menuetElementDeselected() {
		// TODO Auto-generated method stub
		
	}

	public void menuetElementSelected() {
		// TODO Auto-generated method stub
		
	}

	public void glKeyPressed(KeyEvent e, boolean ctrlIsDown, boolean shiftIsDown, IParamSet paramSet) {
		// TODO Auto-generated method stub
	}

}
