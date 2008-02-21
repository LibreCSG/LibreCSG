package ui.tools.build;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolCtrlBuild;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.adt.SelectionList;
import backend.global.AvoGlobal;
import backend.model.Feature2D;
import backend.model.Build;
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

	public void glMouseDown(double x, double y, double z, MouseEvent e, ParamSet paramSet) {
		Build feat2D3D = AvoGlobal.project.getActiveFeat2D3D();
		if(feat2D3D != null){	
			Sketch sketch = feat2D3D.getPrimarySketch();
			if(sketch != null){
				//ParamSet paramSet = feat2D3D.paramSet;
				if(!(new ToolBuildRevolveModel()).paramSetIsValid(paramSet)){
					// paramSet is not valid for this feature, create a new one.
					paramSet = (new ToolBuildRevolveModel()).constructNewParamSet();
					feat2D3D.paramSet = paramSet;
				}
				
				try{
					SelectionList regions    = paramSet.getParam("regions").getDataSelectionList();
					SelectionList centerline = paramSet.getParam("centerline").getDataSelectionList();
					if(centerline.hasFocus){
						System.out.println("looking for centerline.");
						// try to select a 2D line for the centerline if clicked...
						for(int i = 0; i < sketch.getFeat2DListSize(); i++){
							Feature2D f2D = sketch.getAtIndex(i);
							for(int j=0; j< f2D.prim2DList.size(); j++){
								Prim2D prim2D = f2D.prim2DList.get(j);
							//for(Prim2D prim2D : f2D.prim2DList){
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
							if(reg.regionContainsPoint2D(clickedPoint) && !regions.contains(String.valueOf(i))){
								regions.add(String.valueOf(i));
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
				
				AvoGlobal.paramDialog.setParamSet(feat2D3D.paramSet);
			}
		}		
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e, ParamSet paramSet) {
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e, ParamSet paramSet) {
	}

	public void glMouseUp(double x, double y, double z, MouseEvent e, ParamSet paramSet) {
	}
	
	public void menuetElementDeselected() {
	}

	public void menuetElementSelected() {
		Build feat2D3D = AvoGlobal.project.getActiveFeat2D3D();
		if(feat2D3D != null){
			feat2D3D.paramSet = (new ToolBuildRevolveModel()).constructNewParamSet();
			AvoGlobal.glView.updateGLView = true;
		}
	}

	public void glKeyPressed(KeyEvent e, boolean ctrlIsDown, boolean shiftIsDown, ParamSet paramSet) {
		// TODO Auto-generated method stub
	}
	
}
