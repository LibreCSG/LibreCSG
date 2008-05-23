package ui.tools.sketch;

import net.sf.avocado_cad.eclipse.ui.AvoGlobal;
import net.sf.avocado_cad.model.api.adt.IParamSet;
import net.sf.avocado_cad.model.api.sketch.IFeature2D;
import net.sf.avocado_cad.model.api.sketch.IPrim2D;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolCtrlSketch;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.model.Feature2D;
import backend.model.Sketch;


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
	public ToolSketchSelectCtrl(){		
	}
	
	public void glMouseDown(double x, double y, double z,  MouseEvent e, IParamSet paramSet) {
		Sketch sketch = (Sketch) AvoGlobal.project.getActiveSketch();
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
				for(IPrim2D prim2D : f2D.prim2DList){
					// TODO: HACK do not hardcode the distance!
					if(prim2D.distFromPrim(new Point2D(x,y)) < 0.2){
						f2D.setSelected(true);
						//
						// give paramDialog the paramSet so that it can
						// be displayed to the user for manual parameter
						// input.
						//
						AvoGlobal.paramDialog.setParamSet((ParamSet)f2D.getParamSet());
					}
				}
			}
			AvoGlobal.updateGLView = true;			
		}
	}

	public void glMouseDrag(double x, double y, double z,  MouseEvent e, IParamSet paramSet) {
	}

	public void glMouseUp(double x, double y, double z,  MouseEvent e, IParamSet paramSet) {
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e, IParamSet paramSet) {
	}

	public void menuetElementDeselected() {
	}

	public void menuetElementSelected() {
	}

	public void glKeyPressed(KeyEvent e, boolean ctrlIsDown, boolean shiftIsDown, IParamSet paramSet) {
		Sketch sketch = (Sketch) AvoGlobal.project.getActiveSketch();
		if(sketch != null && !sketch.isConsumed){
			if(e.character == SWT.DEL){
				for(int i = sketch.getFeat2DListSize()-1; i >= 0; i--){
					IFeature2D f2D = sketch.getAtIndex(i);
					if(f2D.isSelected()){
						sketch.removeFeat2DAtIndex(i);
					}
				}
			}
			AvoGlobal.updateGLView = true;
		}
	}

}
