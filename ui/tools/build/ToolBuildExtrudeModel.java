package ui.tools.build;

import java.util.Iterator;

import javax.media.opengl.GL;

import ui.tools.ToolModelBuild;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.ParamType;
import backend.adt.Point2D;
import backend.adt.SelectionList;
import backend.global.AvoGlobal;
import backend.model.Feature2D3D;
import backend.model.Part;
import backend.model.Sketch;
import backend.model.CSG.BoolOp;
import backend.model.CSG.CSG_Face;
import backend.model.CSG.CSG_Polygon;
import backend.model.CSG.CSG_Solid;
import backend.model.CSG.CSG_Vertex;
import backend.model.ref.ModRef_Plane;
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
public class ToolBuildExtrudeModel implements ToolModelBuild{

	public void draw3DFeature(GL gl, Feature2D3D feat2D3D) {
		// if sketch is not consumed... just draw face to be extruded
		//System.out.println("trying to draw extrude");		
		
		Sketch sketch = feat2D3D.getPrimarySketch();
		if(sketch != null){
			Iterator<Region2D> regIter = sketch.getRegion2DIterator();
			while(regIter.hasNext()){
				Region2D region = regIter.next();
				// TODO: draw on the sketch plane!
				region.glDrawUnselected(gl);
			}
		}
		
		getBuiltSolid(feat2D3D).glDrawSolid(gl);
	
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
							paramSet.hasParam("cut", ParamType.Boolean) &&
							paramSet.hasParam("regions", ParamType.SelectionList));
		return isValid;
	}

	public void updateDerivedParams(ParamSet paramSet) {
	}

	public void finalize(ParamSet paramSet) {
		System.out.println("Finalizig extrude");
		// finalize extrude and return to main menu
		// TODO: get feat2D3D directly as input to method (not active feature!)
		Feature2D3D feat2D3D = AvoGlobal.project.getActiveFeat2D3D();
		if(feat2D3D != null){
			Sketch sketch = feat2D3D.getPrimarySketch();
			if(sketch != null){
				// TODO: only keep feature and consume sketch if selectionLists are all satisfied as well.
				sketch.isConsumed = true;
				Part part = sketch.getParentPart();
				part.updateSolid(getBuiltSolid(feat2D3D), getBooleanOperation(paramSet));
				
				AvoGlobal.modelEventHandler.notifyActiveElementChanged();
			}else{
				AvoGlobal.project.getActivePart().removeActiveSubPart();				
			}			
		}else{
			System.out.println("ToolBuildExtrudeModel(finalize): I have no idea what's going on?!?  the active feature2D3D was null!?!");
		}
		
	}

	public ParamSet constructNewParamSet() {
		ParamSet pSet = new ParamSet("Extrude", new ToolBuildExtrudeModel());
		pSet.addParam("regions", new Param("Regions", new SelectionList()));
		pSet.addParam("h", new Param("Height", 2*AvoGlobal.gridSize));
		pSet.addParam("cut", new Param("Cut?", false));
		return pSet;
	}

	public BoolOp getBooleanOperation(ParamSet pSet) {
		if(paramSetIsValid(pSet)){
			try{
				Boolean isCut = pSet.getParam("cut").getDataBoolean();
				if(isCut){
					return BoolOp.Subtracted;
				}else{
					return BoolOp.Union;
				}
			}catch(Exception ex){
				System.out.println("Extrude(getBooleanOperation): " + ex.getClass().getName());
			}			
		}
		return BoolOp.Union;
	}

	public CSG_Solid getBuiltSolid(Feature2D3D feat2D3D) {
		ParamSet paramSet = feat2D3D.paramSet;
		Sketch sketch = feat2D3D.getPrimarySketch();
		CSG_Solid solid = new CSG_Solid();
		if(sketch != null && paramSet != null){					
			try{
				SelectionList selectionList = paramSet.getParam("regions").getDataSelectionList();
				Double height = paramSet.getParam("h").getDataDouble();
				if(selectionList != null && height != null){
					//System.out.println("drawing extrude at height=" + height + " and selection: " + selectionList.toString());
					for(int i=0; i<selectionList.getSelectionSize(); i++){
						Region2D includedRegion = sketch.getRegAtIndex(Integer.parseInt(selectionList.getStringAtIndex(i)));
						if(includedRegion != null){
							
							// faceID = 1 --> top Face
							// faceID = 2 --> bottom face
							solid.addFace(getFaceByID(feat2D3D, 1));
							solid.addFace(getFaceByID(feat2D3D, 2));
							
							Point2DList ptList = includedRegion.getPeremeterPointList();
							
							CSG_Vertex lastVert        = null;
							CSG_Vertex lastVertExtrude = null;
							for(Point2D pt : ptList){
								if(lastVert == null){
									lastVert        = new CSG_Vertex(pt, 0.0);
									lastVertExtrude = new CSG_Vertex(pt, height);
								}else{
									CSG_Vertex newVert        = new CSG_Vertex(pt, 0.0);
									CSG_Vertex newVertExtrude = new CSG_Vertex(pt, height);
									CSG_Polygon poly = new CSG_Polygon(newVert, lastVert, lastVertExtrude, newVertExtrude);
									CSG_Face newFace = new CSG_Face(poly);
									solid.addFace(newFace);
									lastVert        = newVert;
									lastVertExtrude = newVertExtrude;
								}
							}
							CSG_Vertex newVert        = new CSG_Vertex(ptList.getFirst(), 0.0);
							CSG_Vertex newVertExtrude = new CSG_Vertex(ptList.getFirst(), height);
							CSG_Polygon poly = new CSG_Polygon(newVert, lastVert, lastVertExtrude, newVertExtrude);
							CSG_Face newFace = new CSG_Face(poly);
							solid.addFace(newFace);				
						}
					}
				}
			}catch(Exception ex){
				System.out.println("ToolBuildExtrudeModel(getBuiltSolid): " + ex.getClass().getName());
			}
		}
		if(!solid.isValidSolid()){
			System.out.println("ToolBuildExtrudeModel(getBuiltSolid): Solid was not valid!! returning default empty solid instead.");
			return new CSG_Solid();
		}	
		return solid;
	}

	public CSG_Face getFaceByID(Feature2D3D feat2D3D, int faceID) {
		ParamSet paramSet = feat2D3D.paramSet;
		Sketch sketch = feat2D3D.getPrimarySketch();
		if(sketch != null && paramSet != null){					
			try{
				SelectionList selectionList = paramSet.getParam("regions").getDataSelectionList();
				Double height = paramSet.getParam("h").getDataDouble();
				if(selectionList != null && height != null){
					Region2D includedRegion = sketch.getRegAtIndex(Integer.parseInt(selectionList.getStringAtIndex(0)));
					if(includedRegion != null){
						// CHECK FOR FACE ID  :)
						switch(faceID){
							case 1: {	// Top Face
										CSG_Face topFace = includedRegion.getCSG_Face().getTranslatedCopy(new CSG_Vertex(0.0, 0.0, height));
										if(height >= 0.0){
											// make sure normal points correct way
											topFace.flipFaceDirection();
										}
										topFace.setIsSelectable(new ModRef_Plane(feat2D3D.ID, 1));
										return topFace;
									}
							case 2: {	// Bottom Face
										CSG_Face botFace = includedRegion.getCSG_Face();
										if(height < 0.0){
											// make sure normal points correct way.
											botFace.flipFaceDirection();										
										}
										botFace.setIsSelectable(new ModRef_Plane(feat2D3D.ID, 2));
										return botFace;
									}
							default:{
										System.out.println("ToolBuildExtrudeModel(getFaceByID): no face with ID=" + faceID + " !!!");
										return null;
									}
						}						
					}
				}
			}catch(Exception ex){
				System.out.println("ToolBuildExtrudeModel(getFaceByID): " + ex.getClass().getName());
			}
		}
		return null;
	}


}
