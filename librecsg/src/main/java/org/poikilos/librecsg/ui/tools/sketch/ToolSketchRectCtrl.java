package org.poikilos.librecsg.ui.tools.sketch;

import javax.media.opengl.GL;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;

import org.poikilos.librecsg.ui.tools.ToolCtrlSketch;
import org.poikilos.librecsg.ui.navigation.*;
import org.poikilos.librecsg.backend.adt.ParamSet;
import org.poikilos.librecsg.backend.adt.Point2D;
import org.poikilos.librecsg.backend.global.AvoGlobal;
import org.poikilos.librecsg.backend.model.Feature2D;
import org.poikilos.librecsg.backend.model.Sketch;


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
public class ToolSketchRectCtrl implements ToolCtrlSketch {

	/**
	 * All of the tool's main functionality
	 * mouse handling, glView drawing,
	 * parameter storage, etc.
	 *
	 */
	//protected NavigationToolbar navigationToolbar;

	public ToolSketchRectCtrl(){
		//navigationToolbar = AvoGlobal.navigationToolbar;
		//navigationToolbar.showMessage("Left click on sketch (hold) to set first corner. Drag to set size.");
	}

	public void glMouseDown(double x, double y, double z,  MouseEvent e, ParamSet paramSet) {
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null && !sketch.isConsumed){
			//
			// starting to draw a new feature... deselect all other features.
			//
			sketch.deselectAllFeat2D();

			//
			// Build parameter set for this feature
			//
			ParamSet pSet = (new ToolSketchRectModel()).constructNewParamSet();
			try{
				pSet.changeParam("a", new Point2D(x,y));
				pSet.changeParam("b", new Point2D(x,y));
			}catch(Exception ex){
				System.out.println(this.getClass().getCanonicalName() + " :: " + ex.getClass());
			}
			//
			// add the new feature to the end of the feature set
			// and set it as the active feature2D.
			Feature2D f2D = new Feature2D(sketch, pSet,"Rectangle" + sketch.getFeat2DListSize());
			f2D.setDescriptor("Rectangle");
			int indx = sketch.add(f2D);
			sketch.setActiveFeat2D(indx);

			//
			// give paramDialog the paramSet so that it can
			// be displayed to the user for manual parameter
			// input.
			//
			AvoGlobal.paramDialog.setParamSet(pSet);
			//navigationToolbar.showMessage("Drag to set size.");
		}
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e, ParamSet paramSet) {
		Feature2D feat2D = AvoGlobal.project.getActiveFeat2D();
		if(feat2D != null){
			//
			// get parameter set
			//
			//ParamSet paramSet = feat2D.paramSet;


			//
			// update param values
			//
			try{
				paramSet.changeParam("b", new Point2D(x,y));
				(new ToolSketchRectModel()).updateDerivedParams(paramSet);
				//navigationToolbar.showMessage("Release to finish rectangle.");
			}catch(Exception ex){
				System.out.println(ex.getClass());
			}
		}
	}

	public void glMouseUp(double x, double y, double z,  MouseEvent e, ParamSet paramSet) {
		Feature2D feat2D = AvoGlobal.project.getActiveFeat2D();
		if(feat2D != null){
			//
			// get parameter set
			//
			//ParamSet paramSet = feat2D.paramSet;

			//
			// finalize the feature's formation
			//
			try{
				paramSet.changeParam("b", new Point2D(x,y));
				(new ToolSketchRectModel()).updateDerivedParams(paramSet);

				Point2D ptA = paramSet.getParam("a").getDataPoint2D();
				Point2D ptB = paramSet.getParam("b").getDataPoint2D();
				// * discard if startX == endX or startY == endY  (rectangle has 0 Area)
				if(ptA.getX() == ptB.getX() || ptA.getY() == ptB.getY()){
					// end point are the same... discard
					System.out.println("Reactangle has zero area... discarding feature");
					// remove feature2D from the set
					AvoGlobal.project.getActiveSketch().removeActiveFeat2D();
					AvoGlobal.paramDialog.setParamSet(null);
					//navigationToolbar.showMessage("Left click on sketch (hold) to set first corner. Drag to set size.");
				}else{
					//navigationToolbar.showMessage("Rectangle done. Left click on sketch to deselect it.");
				}

			}catch(Exception ex){
				System.out.println(ex.getClass());
			}
		}
	}



	public void glMouseMovedUp(double x, double y, double z, MouseEvent e, ParamSet paramSet) {
	}

	public void menuetElementDeselected() {
	}

	public void menuetElementSelected() {
	}

	public void glKeyPressed(KeyEvent e, boolean ctrlIsDown, boolean shiftIsDown, ParamSet paramSet) {
		// TODO Auto-generated method stub
	}

}
