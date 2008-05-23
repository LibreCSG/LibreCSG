package ui.tools.build;

import net.sf.avocado_cad.eclipse.ui.AvoGlobal;
import net.sf.avocado_cad.model.api.adt.IParamSet;
import net.sf.avocado_cad.model.api.adt.IPoint2D;
import net.sf.avocado_cad.model.api.adt.ISelectionList;
import net.sf.avocado_cad.model.api.event.BackendGlobal;
import net.sf.avocado_cad.model.api.sketch.IPrim2D;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolCtrlBuild;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.adt.SelectionList;
import backend.model.Build;
import backend.model.Feature2D;
import backend.model.Sketch;
import backend.model.sketch.Prim2D;
import backend.model.sketch.Prim2DLine;
import backend.model.sketch.Region2D;


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

	public void glMouseDown(double x, double y, double z, MouseEvent e, IParamSet paramSet) {
		Build build = (Build) AvoGlobal.project.getActiveFeat2D3D();
		if(build != null){	
			Sketch sketch = build.getPrimarySketch();
			if(sketch != null){
				//ParamSet paramSet = feat2D3D.paramSet;
				if(!(new ToolBuildRevolveModel()).paramSetIsValid(paramSet)){
					// paramSet is not valid for this feature, create a new one.
					ParamSet ps2 = (new ToolBuildRevolveModel()).constructNewParamSet();
					build.paramSet = ps2;
				}
				
				try{
					ISelectionList regions    = paramSet.getParam("regions").getDataSelectionList();
					ISelectionList centerline = paramSet.getParam("centerline").getDataSelectionList();
					if(centerline.hasFocus()){
						System.out.println("looking for centerline.");
						// try to select a 2D line for the centerline if clicked...
						for(int i = 0; i < sketch.getFeat2DListSize(); i++){
							Feature2D f2D = sketch.getAtIndex(i);
							for(int j=0; j< f2D.prim2DList.size(); j++){
								IPrim2D prim2D = f2D.prim2DList.get(j);
								// TODO: hack (hardcoded distance from line for selection.
								if(prim2D instanceof Prim2DLine && prim2D.distFromPrim(new Point2D(x,y)) < 0.2){
									centerline.clearList();
									centerline.add(String.valueOf(i) + "." + String.valueOf(j));
									centerline.setSatisfied(true);
								}
							}
						}
						if(centerline.getSelectionSize() == 1){
							centerline.setSatisfied(true);
						}else{
							centerline.setSatisfied(false);
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
						
						IPoint2D clickedPoint = new Point2D(x,y);
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
							regions.setSatisfied(true);
						}else{
							regions.setSatisfied(false);
						}
						
						// TODO: shouldn't need to directly indicate param modified??
						BackendGlobal.paramEventHandler.notifyParamModified();
					}
				}catch(Exception ex){
					System.out.println("Revolve(mousedown): " + ex.getClass().getName());
				}
				
				AvoGlobal.paramDialog.setParamSet(build.paramSet);
			}
		}		
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e, IParamSet paramSet) {
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e, IParamSet paramSet) {
		Build build = (Build) AvoGlobal.project.getActiveFeat2D3D();
		if(build != null){	
			Sketch sketch = build.getPrimarySketch();
			if(sketch != null){			
				try{
					ISelectionList regions    = paramSet.getParam("regions").getDataSelectionList();
					ISelectionList centerline = paramSet.getParam("centerline").getDataSelectionList();
					IPoint2D clickedPoint = new Point2D(x,y);
					if(centerline.hasFocus()){
						// centerline has focus
						for(int i=0; i<sketch.getFeat2DListSize(); i++){
							Feature2D feat2D = sketch.getAtIndex(i);
							for(int j=0; j< feat2D.prim2DList.size(); j++){
								Prim2D prim2D = feat2D.prim2DList.get(j);
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

	public void glMouseUp(double x, double y, double z, MouseEvent e, IParamSet paramSet) {
	}
	
	public void menuetElementDeselected() {
	}

	public void menuetElementSelected() {
		Build build = (Build) AvoGlobal.project.getActiveFeat2D3D();
		if(build != null){
			System.out.println("constructing new param set!");
			build.paramSet = (new ToolBuildRevolveModel()).constructNewParamSet();
			AvoGlobal.paramDialog.setParamSet(build.paramSet);
			AvoGlobal.updateGLView = true;
		}
	}

	public void glKeyPressed(KeyEvent e, boolean ctrlIsDown, boolean shiftIsDown, IParamSet paramSet) {
		// TODO Auto-generated method stub
	}
	
}
