package org.poikilos.librecsg.ui.tools.sketch;

import com.jogamp.opengl.GLContext;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;

import org.poikilos.librecsg.ui.tools.ToolCtrlSketch;
import org.poikilos.librecsg.ui.navigation.*;
import org.poikilos.librecsg.backend.adt.ParamSet;
import org.poikilos.librecsg.backend.adt.Point2D;
import org.poikilos.librecsg.backend.global.AvoGlobal;
import org.poikilos.librecsg.backend.model.Feature2D;
import org.poikilos.librecsg.backend.model.Sketch;
import org.poikilos.librecsg.backend.model.sketch.Prim2D;


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
* @created Feb. 2007
*/
public class ToolSketchSelectCtrl implements ToolCtrlSketch {

	boolean shiftIsDown = false;

	/**
	 * All of the tool's main functionality
	 * mouse handling, glView drawing,
	 * parameter storage, etc.
	 *
	 */
	//protected NavigationToolbar navigationToolbar;
	public ToolSketchSelectCtrl(){
		//navigationToolbar=AvoGlobal.navigationToolbar;
		//navigationToolbar.showMessage("Select element by clicking on it.");
	}

	public void glMouseDown(double x, double y, double z,  MouseEvent e, ParamSet paramSet) {
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null && !sketch.isConsumed){

			if((e.stateMask & SWT.SHIFT) != 0){
				shiftIsDown = true;
			}else{
				shiftIsDown = false;
			}
			if(!shiftIsDown){
				sketch.deselectAllFeat2D();
			}

			//
			// iterate over all features in the current set to see if they've been clicked
			//
			for(int i = 0; i < sketch.getFeat2DListSize(); i++){
				Feature2D f2D = sketch.getAtIndex(i);
				for(Prim2D prim2D : f2D.getPrim2DList()){
					// TODO: HACK do not hardcode the distance!
					if(prim2D.distFromPrim(new Point2D(x,y)) < 0.2){
						f2D.setSelected(true);
						//
						// give paramDialog the paramSet so that it can
						// be displayed to the user for manual parameter
						// input.
						//
						AvoGlobal.paramDialog.setParamSet(f2D.paramSet);
						//navigationToolbar.showMessage("Feature " +  f2D.ID + " selected");
					}
				}
			}
			AvoGlobal.glView.updateGLView = true;
		}
	}

	public void glMouseDrag(double x, double y, double z,  MouseEvent e, ParamSet paramSet) {
	}

	public void glMouseUp(double x, double y, double z,  MouseEvent e, ParamSet paramSet) {
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e, ParamSet paramSet) {
	}

	public void menuetElementDeselected() {
	}

	public void menuetElementSelected() {
	}

	public void glKeyPressed(KeyEvent e, boolean ctrlIsDown, boolean shiftIsDown, ParamSet paramSet) {
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null && !sketch.isConsumed){
			if(e.character == SWT.DEL){
				for(int i = sketch.getFeat2DListSize()-1; i >= 0; i--){
					Feature2D f2D = sketch.getAtIndex(i);
					if(f2D.isSelected()){
						sketch.removeFeat2DAtIndex(i);
					}
				}
			}
			AvoGlobal.glView.updateGLView = true;
		}
	}

}
