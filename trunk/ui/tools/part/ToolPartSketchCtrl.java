package ui.tools.part;

import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolCtrlPart;
import backend.global.AvoGlobal;
import backend.model.Part;
import backend.model.Sketch;


//
//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the 
//GNU General Public License (GPL).
//
//This file is part of avoADo.
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
* @created Feb. 2007
*/
public class ToolPartSketchCtrl implements ToolCtrlPart{
	
	public void glMouseDown(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseUp(double x, double y, double z, MouseEvent e) {
	}

	public void menuetElementDeselected() {
	}

	public void menuetElementSelected() {
		// Add a new sketch to the active part
		Part part = AvoGlobal.project.getActivePart();
		if(part != null){
			// TODO: allow for planes other than planeXY!!
			part.addNewSketch(part.planeXY);
			// TODO, force update this way?
			AvoGlobal.glView.updateGLView = true; // force update since grid should now be displayed...
		}else{
			System.out.println("ToolPartSketchCtrl(menuetElementSelected): Active Part was Null! Cannot add a new sketch!");
		}
	}

}
