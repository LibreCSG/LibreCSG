package ui.tools.build;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolCtrlBuild;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.adt.SelectionList;
import backend.global.AvoGlobal;
import backend.model.Build;
import backend.model.Sketch;
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
public class ToolBuildExtrudeCtrl implements ToolCtrlBuild{

	boolean shiftIsDown = false;
	
	public void buildDerivedParams(ParamSet pSet) {
	}

	public void glMouseDown(double x, double y, double z, MouseEvent e, ParamSet paramSet) {
		
		Build feat2D3D = AvoGlobal.project.getActiveFeat2D3D();
		if(feat2D3D != null){	
			Sketch sketch = feat2D3D.getPrimarySketch();
			if(sketch != null){
				//ParamSet paramSet = feat2D3D.paramSet;
				if(!(new ToolBuildExtrudeModel()).paramSetIsValid(paramSet)){
					//System.out.println("%% making new extrude paramSet");
					// paramSet is not valid for this feature, create a new one.
					paramSet = (new ToolBuildExtrudeModel()).constructNewParamSet();
					feat2D3D.paramSet = paramSet;
				}
				
				try{
					SelectionList selectionList = paramSet.getParam("regions").getDataSelectionList();
					if((e.stateMask & SWT.SHIFT) != 0){
						// shift is down
					}else{
						// shift is not down
						selectionList.clearList();
					}					
					
					Point2D clickedPoint = new Point2D(x,y);
					//System.out.println("looking at point: " + clickedPoint);
					for(int i=0; i < sketch.getRegion2DListSize(); i++){
						Region2D reg  = sketch.getRegAtIndex(i);
						if(reg.regionContainsPoint2D(clickedPoint) && !selectionList.contains(String.valueOf(i))){
							selectionList.add(String.valueOf(i));							
						}
					}
					if(selectionList.getSelectionSize() > 0){
						selectionList.isSatisfied = true;
					}else{
						selectionList.isSatisfied = false;
					}
					// TODO: shouldn't need to directly indicate param modified??
					AvoGlobal.paramEventHandler.notifyParamModified();
					
				}catch(Exception ex){
					System.out.println("Extrude(mousedown): " + ex.getClass().getName());
				}
				
				AvoGlobal.paramDialog.setParamSet(feat2D3D.paramSet);				
			}
		}
		
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e, ParamSet paramSet) {
	}

	public void glMouseUp(double x, double y, double z, MouseEvent e, ParamSet paramSet) {
		// TODO: hack just to see if label updates...
		AvoGlobal.modelEventHandler.notifyElementAdded();
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e, ParamSet paramSet) {
	}

	public void menuetElementDeselected() {
	}

	public void menuetElementSelected() {
		Build feat2D3D = AvoGlobal.project.getActiveFeat2D3D();
		if(feat2D3D != null){
			feat2D3D.paramSet = (new ToolBuildExtrudeModel()).constructNewParamSet();
			AvoGlobal.glView.updateGLView = true;
		}
	}

	public void glKeyPressed(KeyEvent e, boolean ctrlIsDown, boolean shiftIsDown, ParamSet paramSet) {
		// TODO Auto-generated method stub
	}



}
