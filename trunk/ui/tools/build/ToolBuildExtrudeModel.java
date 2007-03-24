package ui.tools.build;

import javax.media.opengl.GL;

import ui.tools.ToolModelBuild;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.ParamType;
import backend.adt.SelectionList;
import backend.global.AvoGlobal;
import backend.model.Feature2D3D;
import backend.model.Sketch;
import backend.model.CSG.CSG_Face;
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
public class ToolBuildExtrudeModel implements ToolModelBuild{

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
							CSG_Face face = includedRegion.getCSG_Face();
							face.drawFaceForDebug(gl);
							face.drawFaceLinesForDebug(gl);
							
							/*
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
							*/
						}
					}
				}
			}catch(Exception ex){
				System.out.println("Extrude(draw): " + ex.getClass().getName());
			}
		}		
	}

	public boolean paramSetIsValid(ParamSet paramSet) {
		//		 ParamSet:  "Extrude"
		// --------------------------------
		// # "h"        ->  "Hegith"    <Double>
		// # "regions"  ->  "Regions"   <SelectionList>
		// --------------------------------		
		boolean isValid = (	paramSet != null &&
							paramSet.label == "Extrude" &&
							paramSet.hasParam("h", ParamType.Double) &&
							paramSet.hasParam("regions", ParamType.SelectionList));
		return isValid;
	}

	public void updateDerivedParams(ParamSet paramSet) {
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

			// TODO: return to main menuet! (but this should be handled from the controller, not the model)
			
		}else{
			System.out.println("I have no idea what's going on?!?  the active feature2D3D was null!?!");
		}
		
	}

	public ParamSet constructNewParamSet() {
		ParamSet pSet = new ParamSet("Extrude", new ToolBuildExtrudeModel());
		pSet.addParam("regions", new Param("Regions", new SelectionList()));
		pSet.addParam("h", new Param("Height", 2*AvoGlobal.gridSize));
		return pSet;
	}

}
