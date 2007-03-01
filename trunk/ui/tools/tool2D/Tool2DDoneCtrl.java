package ui.tools.tool2D;

import org.eclipse.swt.events.MouseEvent;

import backend.global.AvoGlobal;
import backend.model.Sketch;

import ui.tools.ToolCtrl2D;

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
public class Tool2DDoneCtrl implements ToolCtrl2D {
	/**
	 * All of the tool's main functionality mouse handling, glView drawing,
	 * parameter storage, etc.
	 * 
	 */
	public Tool2DDoneCtrl() {
	}

	public void glMouseDown(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseUp(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e) {
	}

	public void menuetElementDeselected() {
	}

	public void menuetElementSelected() {
		Sketch sketch = AvoGlobal.project.getActiveSketch();

		if(sketch != null){
			if(sketch.getFeat2DListSize() == 0){
				// if sketch has no Feature2D, then discard it.
				AvoGlobal.project.getActivePart().removeActiveSubPart();
			}else{
				// deselect Feat2D and keep the sketch.
				sketch.deselectAllFeat2D();			
			}
		}
	}



}
