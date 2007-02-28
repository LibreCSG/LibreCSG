package ui.tools.DDtoDDD;

import javax.media.opengl.GL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;

import ui.menuet.Menuet;
import ui.tools.ToolCtrl2D3D;
import backend.adt.PType;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.adt.SelectionList;
import backend.global.AvoGlobal;
import backend.model.Feature2D3D;
import backend.model.Sketch;
import backend.model.sketch.Point2DList;
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
public class Tool2D3DExtrudeInt implements ToolCtrl2D3D{

	boolean shiftIsDown = false;
	
	public void buildDerivedParams(ParamSet pSet) {
	}

	public void glMouseDown(double x, double y, double z, MouseEvent e) {
		
		Feature2D3D feat2D3D = AvoGlobal.project.getActiveFeat2D3D();
		if(feat2D3D != null){	
			Sketch sketch = feat2D3D.getPrimarySketch();
			if(sketch != null){
				ParamSet paramSet = feat2D3D.paramSet;
				if(!paramSetIsValid(paramSet)){
					//System.out.println("%% making new extrude paramSet");
					// paramSet is not valid for this feature, create a new one.
					paramSet = new ParamSet("Extrude", this);
					paramSet.addParam("regions", new Param("Regions", new SelectionList()));
					paramSet.addParam("h", new Param("Height", 2*AvoGlobal.gridSize));
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

	public void draw3DFeature(GL gl, Feature2D3D feat2D3D) {
		// if sketch is not consumed... just draw face to be extruded
		//System.out.println("trying to draw extrude");
		
		// TODO: using selected regions from the sketch... this should be done with a SelectionList!
		
		ParamSet paramSet = feat2D3D.paramSet;
		Sketch sketch = feat2D3D.getPrimarySketch();
		if(sketch != null && paramSet != null){		
			try{
				SelectionList selectionList = paramSet.getParam("regions").getDataSelectionList();
				Double height = paramSet.getParam("h").getDataDouble();
				if(selectionList != null && height != null){
					//System.out.println("drawing extrude at height=" + height + " and selection: " + selectionList.toString());
					for(int i=0; i<selectionList.getSelectionSize(); i++){
						Region2D includedRegion = sketch.getRegAtIndex(Integer.parseInt(selectionList.getStringAtIndex(i)));
						if(includedRegion != null){
							Point2DList ptList = includedRegion.getPoint2DListTriangles();
							gl.glColor4f(0.7f, 0.85f, 0.85f, 0.6f);
							gl.glBegin(GL.GL_TRIANGLES);
								for(int j=ptList.size()-1; j >= 0; j--){
									Point2D p = ptList.get(j);
									gl.glVertex3d(p.getX(), p.getY(), 0.0);
								}
								for(Point2D p : ptList){
									gl.glVertex3d(p.getX(), p.getY(), height);
								}
							gl.glEnd();
							
							Point2DList qList = includedRegion.getPoint2DListEdgeQuad();
							gl.glBegin(GL.GL_QUADS);
								int q = 0;
								for(Point2D p : qList){
									if(q%4 == 0 || q%4 == 1){
										gl.glVertex3d(p.getX(), p.getY(), 0.0);
									}else{
										gl.glVertex3d(p.getX(), p.getY(), height);
									}
									q++;
								}
							gl.glEnd();
							
							Point2DList lnList = includedRegion.getPoint2DListEdges();
							gl.glColor4f(0.6f, 0.75f, 0.75f, 1.0f);
							gl.glBegin(GL.GL_LINES);
								for(Point2D p : lnList){
									gl.glVertex3d(p.getX(), p.getY(), 0.0);
								}
								for(Point2D p : lnList){
									gl.glVertex3d(p.getX(), p.getY(), height);
								}
								for(Point2D p : lnList){
									gl.glVertex3d(p.getX(), p.getY(), 0.0);
									gl.glVertex3d(p.getX(), p.getY(), height);
								}
							gl.glEnd();							
						}
					}
				}
			}catch(Exception ex){
				System.out.println("Extrude(draw): " + ex.getClass().getName());
			}
		}		
	}
	
	public void glMouseMovedUp(double x, double y, double z, MouseEvent e) {
	}

	public boolean paramSetIsValid(ParamSet paramSet) {
		//		 ParamSet:  "Extrude"
		// --------------------------------
		// # "h"        ->  "Hegith"    <Double>
		// # "regions"  ->  "Regions"   <SelectionList>
		// --------------------------------		
		boolean isValid = (	paramSet != null &&
							paramSet.label == "Extrude" &&
							paramSet.hasParam("h", PType.Double) &&
							paramSet.hasParam("regions", PType.SelectionList));
		return isValid;
	}

	public void updateDerivedParams(ParamSet paramSet) {
		// TODO Auto-generated method stub		
	}

	public void finalize(ParamSet paramSet) {
		System.out.println("Finalizig extrude");
		// finalize extrude and return to main menu
		Feature2D3D feat2D3D = AvoGlobal.project.getActiveFeat2D3D();
		if(feat2D3D != null){
			Sketch sketch = feat2D3D.getPrimarySketch();
			if(sketch != null){
				// TODO: only keep feature and consume sketch if selectionLists are all satisfied as well.
				sketch.isConsumed = true;
			}else{
				AvoGlobal.project.getActivePart().removeActiveSubPart();				
			}
			
			AvoGlobal.menuet.disableAllTools();
			AvoGlobal.menuet.setCurrentToolMode(Menuet.MENUET_MODE_MAIN);
			AvoGlobal.paramDialog.setParamSet(null);
			AvoGlobal.menuet.currentTool = null;			
			AvoGlobal.menuet.updateToolModeDisplayed();
			AvoGlobal.glView.updateGLView = true;		
			
		}else{
			System.out.println("I have no idea what's going on?!?  the active feature2D3D was null!?!");
		}
		
	}

}
