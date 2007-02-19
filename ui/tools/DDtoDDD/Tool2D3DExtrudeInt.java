package ui.tools.DDtoDDD;

import javax.media.opengl.GL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolInterface2D3D;
import backend.adt.Param;
import backend.adt.ParamNotFoundException;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.global.AvoGlobal;
import backend.model.Feature2D3D;
import backend.model.Region2D;
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
public class Tool2D3DExtrudeInt implements ToolInterface2D3D{

	boolean shiftIsDown = false;
	
	public void buildDerivedParams(ParamSet pSet) {
	}

	public void glMouseDown(double x, double y, double z, MouseEvent e) {
		//System.out.println("got extrude mouse down!");
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null){
			
			Feature2D3D f2D3D = (Feature2D3D)AvoGlobal.project.getActiveFeat3D();
			if(f2D3D.toolInt2D3D == null){
				//
				// Build parameter set for this feature
				//
				ParamSet pSet = new ParamSet("Extrude", this);
				pSet.addParam("h", new Param("Height", 2*AvoGlobal.gridSize));
				
				f2D3D.toolInt2D3D = this;
				f2D3D.paramSet = pSet;
				
				//
				// give paramDialog the paramSet so that it can
				// be displayed to the user for manual parameter
				// input.
				//
				AvoGlobal.paramDialog.setParamSet(pSet);
			}
			
			
			if((e.stateMask & SWT.SHIFT) != 0){
				shiftIsDown = true;
			}else{
				shiftIsDown = false;
			}
			if(!shiftIsDown){			
				// only deselect other regions if SHIFT key is not down.
				sketch.deselectAllRegions();
			}
			
			sketch.selectRegionsThatContainsPoint(new Point2D(x,y));
			
		}
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseUp(double x, double y, double z, MouseEvent e) {
	}

	public void draw3DFeature(GL gl, ParamSet paramSet, Sketch sketch) {
		// if sketch is not consumed... just draw face to be extruded
		//System.out.println("trying to draw extrude");
		
		for(int i=0; i<sketch.getRegion2DListSize(); i++){
			Region2D reg = sketch.getRegAtIndex(i);
			if(reg.isSelected){
				// region is selected -- fill it in.
				// TODO: HACK, only drawing if 3 sided.
				if(reg.prim2DCycle.size() == 3){
					Point2D ptA = reg.prim2DCycle.get(0).ptA;
					Point2D ptB = reg.prim2DCycle.get(0).ptB;
					Point2D ptC = reg.prim2DCycle.get(1).hasPtGetOther(ptB);
					gl.glColor4f(1.0f, 0.7f, 0.85f, 0.5f);
					gl.glBegin(GL.GL_TRIANGLES);
					//System.out.println("A:" + ptA + " B:" + ptB + " C:" + ptC);
						if(reg.prim2DCycle.isCCW()){
							gl.glVertex3d(ptA.getX(), ptA.getY(), 0.0);
							gl.glVertex3d(ptB.getX(), ptB.getY(), 0.0);
							gl.glVertex3d(ptC.getX(), ptC.getY(), 0.0);
						}else{
							gl.glVertex3d(ptC.getX(), ptC.getY(), 0.0);												
							gl.glVertex3d(ptB.getX(), ptB.getY(), 0.0);
							gl.glVertex3d(ptA.getX(), ptA.getY(), 0.0);
						}											
					gl.glEnd();
					
					
					if(paramSet != null && paramSet.getParam("h") != null){
						double height = (Double)paramSet.getParam("h").getData();
						
						// draw top
						gl.glColor4f(1.0f, 0.7f, 0.85f, 0.5f);
						gl.glBegin(GL.GL_TRIANGLES);
						//System.out.println("A:" + ptA + " B:" + ptB + " C:" + ptC);
							if(reg.prim2DCycle.isCCW()){
								gl.glVertex3d(ptA.getX(), ptA.getY(), height);
								gl.glVertex3d(ptB.getX(), ptB.getY(), height);
								gl.glVertex3d(ptC.getX(), ptC.getY(), height);
							}else{
								gl.glVertex3d(ptC.getX(), ptC.getY(), height);												
								gl.glVertex3d(ptB.getX(), ptB.getY(), height);
								gl.glVertex3d(ptA.getX(), ptA.getY(), height);
							}											
						gl.glEnd();
						
						// draw defining lines
						gl.glColor4f(0.7f, 0.5f, 0.6f, 1.0f);
						gl.glBegin(GL.GL_LINES);
							gl.glVertex3d(ptA.getX(), ptA.getY(), height);
							gl.glVertex3d(ptB.getX(), ptB.getY(), height);
							gl.glVertex3d(ptB.getX(), ptB.getY(), height);
							gl.glVertex3d(ptC.getX(), ptC.getY(), height);
							gl.glVertex3d(ptC.getX(), ptC.getY(), height);
							gl.glVertex3d(ptA.getX(), ptA.getY(), height);	
							
							gl.glVertex3d(ptA.getX(), ptA.getY(), 0.0);
							gl.glVertex3d(ptA.getX(), ptA.getY(), height);
							gl.glVertex3d(ptB.getX(), ptB.getY(), 0.0);
							gl.glVertex3d(ptB.getX(), ptB.getY(), height);
							gl.glVertex3d(ptC.getX(), ptC.getY(), 0.0);
							gl.glVertex3d(ptC.getX(), ptC.getY(), height);
							
						gl.glEnd();						
						
						// draw sides
						gl.glColor4f(1.0f, 0.7f, 0.85f, 0.5f);
						gl.glBegin(GL.GL_QUADS);
							if(reg.prim2DCycle.isCCW()){
								gl.glVertex3d(ptA.getX(), ptA.getY(), 0.0);
								gl.glVertex3d(ptB.getX(), ptB.getY(), 0.0);
								gl.glVertex3d(ptB.getX(), ptB.getY(), height);
								gl.glVertex3d(ptA.getX(), ptA.getY(), height);
								
								gl.glVertex3d(ptB.getX(), ptB.getY(), 0.0);
								gl.glVertex3d(ptC.getX(), ptC.getY(), 0.0);
								gl.glVertex3d(ptC.getX(), ptC.getY(), height);
								gl.glVertex3d(ptB.getX(), ptB.getY(), height);
								
								gl.glVertex3d(ptC.getX(), ptC.getY(), 0.0);
								gl.glVertex3d(ptA.getX(), ptA.getY(), 0.0);
								gl.glVertex3d(ptA.getX(), ptA.getY(), height);
								gl.glVertex3d(ptC.getX(), ptC.getY(), height);
								
							}else{
								gl.glVertex3d(ptA.getX(), ptA.getY(), height);
								gl.glVertex3d(ptB.getX(), ptB.getY(), height);
								gl.glVertex3d(ptB.getX(), ptB.getY(), 0.0);
								gl.glVertex3d(ptA.getX(), ptA.getY(), 0.0);
								
								gl.glVertex3d(ptB.getX(), ptB.getY(), height);
								gl.glVertex3d(ptC.getX(), ptC.getY(), height);
								gl.glVertex3d(ptC.getX(), ptC.getY(), 0.0);
								gl.glVertex3d(ptB.getX(), ptB.getY(), 0.0);
								
								gl.glVertex3d(ptC.getX(), ptC.getY(), height);
								gl.glVertex3d(ptA.getX(), ptA.getY(), height);
								gl.glVertex3d(ptA.getX(), ptA.getY(), 0.0);
								gl.glVertex3d(ptC.getX(), ptC.getY(), 0.0);
							}				
						gl.glEnd();						
						
					}
				}
			}
		}
		
	}
	
	public void glMouseMovedUp(double x, double y, double z, MouseEvent e) {
	}


	public void loadParamsAndUpdateState(ParamSet pSet) throws ParamNotFoundException {
		// TODO Auto-generated method stub		
	}


	public void modifyParamsFromState(ParamSet pSet) throws ParamNotFoundException {
		// TODO Auto-generated method stub		
	}

}
