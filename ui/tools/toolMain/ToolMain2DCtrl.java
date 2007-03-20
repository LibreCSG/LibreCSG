package ui.tools.toolMain;

import org.eclipse.swt.events.MouseEvent;

import backend.global.AvoGlobal;
import backend.model.Sketch;

import ui.tools.ToolCtrlPart;


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
public class ToolMain2DCtrl implements ToolCtrlPart{
	
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
		//
		// create new Feature3D to use for subsequent 2D sketches
		//
		if(AvoGlobal.project.getActiveGroup() == null){
			int i = AvoGlobal.project.addNewGroup();
			AvoGlobal.project.setActiveGroup(i);
		}
		if(AvoGlobal.project.getActivePart() == null){
			int i = AvoGlobal.project.getActiveGroup().addNewPart();
			AvoGlobal.project.getActiveGroup().setActivePart(i);
		}
		
		int i = AvoGlobal.project.getActivePart().addNewSketch();
		AvoGlobal.project.getActivePart().setActiveSubPart(i);
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null){
			AvoGlobal.paramDialog.setParamSet(sketch.paramSet);
		}
		AvoGlobal.glView.updateGLView = true; // force update since grid should now be displayed...
	}

}
