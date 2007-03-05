package ui.tools.tool2Dto3D;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolCtrl2D3D;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.adt.SelectionList;
import backend.global.AvoGlobal;
import backend.model.Feature2D3D;
import backend.model.Sketch;
import backend.model.sketch.Region2D;


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
public class Tool2D3DExtrudeCtrl implements ToolCtrl2D3D{

	boolean shiftIsDown = false;
	
	public void buildDerivedParams(ParamSet pSet) {
	}

	public void glMouseDown(double x, double y, double z, MouseEvent e) {
		
		Feature2D3D feat2D3D = AvoGlobal.project.getActiveFeat2D3D();
		if(feat2D3D != null){	
			Sketch sketch = feat2D3D.getPrimarySketch();
			if(sketch != null){
				ParamSet paramSet = feat2D3D.paramSet;
				if(!(new Tool2D3DExtrudeModel()).paramSetIsValid(paramSet)){
					//System.out.println("%% making new extrude paramSet");
					// paramSet is not valid for this feature, create a new one.
					paramSet = (new Tool2D3DExtrudeModel()).constructNewParamSet();
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

	public void glMouseDrag(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseUp(double x, double y, double z, MouseEvent e) {
		// TODO: hack just to see if label updates...
		AvoGlobal.modelEventHandler.notifyElementAdded();
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e) {
	}

	public void menuetElementDeselected() {
	}

	public void menuetElementSelected() {
	}



}
