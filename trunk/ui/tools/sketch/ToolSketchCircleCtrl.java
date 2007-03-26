package ui.tools.sketch;

import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolCtrlSketch;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.global.AvoGlobal;
import backend.model.Feature2D;
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
public class ToolSketchCircleCtrl implements ToolCtrlSketch  {

	/**
	 * All of the tool's main functionality
	 * mouse handling, glView drawing, 
	 * parameter storage, etc.
	 *
	 */
	public ToolSketchCircleCtrl(){
	}
	
	
	public void glMouseDown(double x, double y, double z,  MouseEvent e) {
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null && !sketch.isConsumed){
			//
			// starting to draw a new feature... deselect all other features.
			//
			sketch.deselectAllFeat2D();
			
			//
			// Build parameter set for this feature
			//
			ParamSet pSet = (new ToolSketchCircleModel()).constructNewParamSet();
			try{
				pSet.changeParam("c", new Point2D(x,y));
			}catch(Exception ex){
				System.out.println("Tool2DCircle(mousedown): " + ex.getClass());
			}
			//
			// add the new feature to the end of the feature set
			// and set it as the active feature2D.		
			int indx = sketch.add(new Feature2D(sketch, pSet));
			sketch.setActiveFeat2D(indx);
			
			//
			// give paramDialog the paramSet so that it can
			// be displayed to the user for manual parameter
			// input.
			//
			AvoGlobal.paramDialog.setParamSet(pSet);
		}
	}

	public void glMouseDrag(double x, double y, double z,  MouseEvent e) {
		Feature2D feat2D = AvoGlobal.project.getActiveFeat2D();
		if(feat2D != null){
			//
			// get parameter set
			//
			ParamSet paramSet = feat2D.paramSet;
		
			//
			// update param values
			//
			try{
				Point2D ptCenter = paramSet.getParam("c").getDataPoint2D();
				paramSet.changeParam("r", ptCenter.computeDist(new Point2D(x,y)));
			}catch(Exception ex){
				System.out.println("Tool2DCircle(mousedrag): " + ex.getClass());
			}
		}
	}

	public void glMouseUp(double x, double y, double z,  MouseEvent e) {
		Feature2D feat2D = AvoGlobal.project.getActiveFeat2D();
		if(feat2D != null){
			//
			// get parameter set
			//
			ParamSet paramSet = feat2D.paramSet;
			
			//
			// finalize the feature's formation
			//
			try{
				Point2D ptCenter = paramSet.getParam("c").getDataPoint2D();
				paramSet.changeParam("r", ptCenter.computeDist(new Point2D(x,y)));
				
				// * discard if radius == 0.0
				Double radius = paramSet.getParam("r").getDataDouble();
				if(radius == 0.0){
					System.out.println("radius was zero... feature discarded");
					// remove feature2D from the set!
					AvoGlobal.project.getActiveSketch().removeActiveFeat2D();
					AvoGlobal.paramDialog.setParamSet(null);
				}
			}catch(Exception ex){
				System.out.println("Tool2DCircle(mouseup): " + ex.getClass());
			}						
		}
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e) {
	}


	public void menuetElementDeselected() {		
	}


	public void menuetElementSelected() {
	}



	
}
