package org.poikilos.librecsg.ui.tools.build;

import javax.media.opengl.GL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;

import org.poikilos.librecsg.ui.tools.ToolCtrlBuild;
import org.poikilos.librecsg.backend.adt.ParamSet;
import org.poikilos.librecsg.backend.adt.Point2D;
import org.poikilos.librecsg.backend.adt.SelectionList;
import org.poikilos.librecsg.backend.global.AvoGlobal;
import org.poikilos.librecsg.backend.model.Feature2D;
import org.poikilos.librecsg.backend.model.Build;
import org.poikilos.librecsg.backend.model.Sketch;
import org.poikilos.librecsg.backend.model.sketch.Prim2D;
import org.poikilos.librecsg.backend.model.sketch.Prim2DLine;
import org.poikilos.librecsg.backend.model.sketch.Region2D;


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
public class ToolBuildRevolveCtrl implements ToolCtrlBuild{

	public void glMouseDown(double x, double y, double z, MouseEvent e, ParamSet paramSet) {
		Build build = AvoGlobal.project.getActiveFeat2D3D();
		if(build != null){
			Sketch sketch = build.getPrimarySketch();
			if(sketch != null){
				//ParamSet paramSet = feat2D3D.paramSet;
				if(!(new ToolBuildRevolveModel()).paramSetIsValid(paramSet)){
					// paramSet is not valid for this feature, create a new one.
					paramSet = (new ToolBuildRevolveModel()).constructNewParamSet();
					build.paramSet = paramSet;
				}

				try{
					SelectionList regions    = paramSet.getParam("regions").getDataSelectionList();
					SelectionList centerline = paramSet.getParam("centerline").getDataSelectionList();
					if(centerline.hasFocus){
						System.out.println("looking for centerline.");
						// try to select a 2D line for the centerline if clicked...
						for(int i = 0; i < sketch.getFeat2DListSize(); i++){
							Feature2D f2D = sketch.getAtIndex(i);
							for(int j=0; j< f2D.getPrim2DList().size(); j++){
								Prim2D prim2D = f2D.getPrim2DList().get(j);
								// TODO: hack (hardcoded distance from line for selection.
								if(prim2D instanceof Prim2DLine && prim2D.distFromPrim(new Point2D(x,y)) < 0.2){
									centerline.clearList();
									centerline.add(String.valueOf(i) + "." + String.valueOf(j));
									centerline.isSatisfied = true;
								}
							}
						}
						if(centerline.getSelectionSize() == 1){
							centerline.isSatisfied = true;
						}else{
							centerline.isSatisfied = false;
						}
					}else{
						System.out.println("looking for region.");
						// selecting regions...
						if((e.stateMask & SWT.SHIFT) != 0){
							// shift is down
						}else{
							// shift is not down
							regions.clearList();
						}

						Point2D clickedPoint = new Point2D(x,y);
						for(int i=0; i < sketch.getRegion2DListSize(); i++){
							Region2D reg  = sketch.getRegAtIndex(i);
							reg.setSelected(false);
							if(reg.regionContainsPoint2D(clickedPoint) && !regions.contains(String.valueOf(i))){
								regions.add(String.valueOf(i));
							}
							if(regions.contains(String.valueOf(i))){
								reg.setSelected(true);	// set region as selected
							}
						}
						if(regions.getSelectionSize() > 0){
							regions.isSatisfied = true;
						}else{
							regions.isSatisfied = false;
						}

						// TODO: shouldn't need to directly indicate param modified??
						AvoGlobal.paramEventHandler.notifyParamModified();
					}
				}catch(Exception ex){
					System.out.println("Revolve(mousedown): " + ex.getClass().getName());
				}

				AvoGlobal.paramDialog.setParamSet(build.paramSet);
			}
		}
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e, ParamSet paramSet) {
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e, ParamSet paramSet) {
		Build build = AvoGlobal.project.getActiveFeat2D3D();
		if(build != null){
			Sketch sketch = build.getPrimarySketch();
			if(sketch != null){
				try{
					SelectionList regions    = paramSet.getParam("regions").getDataSelectionList();
					SelectionList centerline = paramSet.getParam("centerline").getDataSelectionList();
					Point2D clickedPoint = new Point2D(x,y);
					if(centerline.hasFocus){
						// centerline has focus
						for(int i=0; i<sketch.getFeat2DListSize(); i++){
							Feature2D feat2D = sketch.getAtIndex(i);
							for(int j=0; j< feat2D.getPrim2DList().size(); j++){
								Prim2D prim2D = feat2D.getPrim2DList().get(j);
								prim2D.isSelected = false;
								// TODO: hack (hardcoded distance from line for selection.
								if(prim2D instanceof Prim2DLine && prim2D.distFromPrim(new Point2D(x,y)) < 0.2){
									prim2D.isSelected = true;
								}
							}
						}

					}else{
						// region has focus
						for(int i=0; i < sketch.getRegion2DListSize(); i++){
							Region2D reg  = sketch.getRegAtIndex(i);
							reg.setMousedOver(false);
						}
						for(int i=0; i < sketch.getRegion2DListSize(); i++){
							Region2D reg  = sketch.getRegAtIndex(i);
							if(reg.regionContainsPoint2D(clickedPoint)){
								reg.setMousedOver(true);
							}
						}
					}
				}catch(Exception ex){
					System.out.println("Revolve(mousemovedup): " + ex.getClass().getName());
				}
			}
		}
	}

	public void glMouseUp(double x, double y, double z, MouseEvent e, ParamSet paramSet) {
	}

	public void menuetElementDeselected() {
	}

	public void menuetElementSelected() {
		Build build = AvoGlobal.project.getActiveFeat2D3D();
		if(build != null){
			System.out.println("constructing new param set!");
			build.paramSet = (new ToolBuildRevolveModel()).constructNewParamSet();
			AvoGlobal.paramDialog.setParamSet(build.paramSet);
			AvoGlobal.glView.updateGLView = true;
		}
	}

	public void glKeyPressed(KeyEvent e, boolean ctrlIsDown, boolean shiftIsDown, ParamSet paramSet) {
		// TODO Auto-generated method stub
	}

}
