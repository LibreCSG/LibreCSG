package ui.tools.build;

import java.util.Iterator;
import java.util.LinkedList;

import ui.tools.ToolModelBuild;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.ParamType;
import backend.adt.Point2D;
import backend.adt.Rotation3D;
import backend.adt.SelectionList;
import backend.adt.Translation3D;
import backend.global.AvoGlobal;
import backend.model.Build;
import backend.model.Part;
import backend.model.Sketch;
import backend.model.CSG.BoolOp;
import backend.model.CSG.CSG_BooleanOperator;
import backend.model.CSG.CSG_Face;
import backend.model.CSG.CSG_Polygon;
import backend.model.CSG.CSG_Solid;
import backend.model.CSG.CSG_Vertex;
import backend.model.ref.ModRef_Cylinder;
import backend.model.ref.ModRef_Plane;
import backend.model.sketch.Point2DList;
import backend.model.sketch.Prim2D;
import backend.model.sketch.Prim2DArc;
import backend.model.sketch.Prim2DLine;
import backend.model.sketch.Region2D;
import backend.model.sketch.SketchPlane;


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
public class ToolBuildExtrudeModel implements ToolModelBuild{

	public boolean paramSetIsValid(ParamSet paramSet) {
		//		 ParamSet:  "Extrude"
		// --------------------------------
		// # "h"        ->  "Height"    <Double>
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
		Build feat2D3D = AvoGlobal.project.getActiveFeat2D3D();
		if(feat2D3D != null){
			Sketch sketch = feat2D3D.getPrimarySketch();
			if(sketch != null){
				// TODO: only keep feature and consume sketch if selectionLists are all satisfied as well.
				sketch.isConsumed = true;
				Part part = sketch.getParentPart();
				
				// Translate/rotate the part to be position on the sketch plane.
				SketchPlane sp = sketch.getSketchPlane();
				Rotation3D rotation = new Rotation3D(sp.getRotationX(), sp.getRotationY(), sp.getRotationZ());
				Translation3D trans = new Translation3D(sp.getOrigin().getX(), sp.getOrigin().getY(), sp.getOrigin().getZ());
				CSG_Solid solid = getBuiltSolid(feat2D3D);
				solid.applyTranslationRotation(trans, rotation);
				
				part.updateSolid(solid,	getBooleanOperation(paramSet));
				
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

	public CSG_Solid getBuiltSolid(Build feat2D3D) {
		ParamSet paramSet = feat2D3D.paramSet;
		Sketch sketch = feat2D3D.getPrimarySketch();
		CSG_Solid solid = new CSG_Solid();
		if(sketch != null && paramSet != null){					
			try{
				SelectionList selectionList = paramSet.getParam("regions").getDataSelectionList();
				Double height = paramSet.getParam("h").getDataDouble();
				int faceCounter = 1;
				if(selectionList != null && height != null){
					//System.out.println("drawing extrude at height=" + height + " and selection: " + selectionList.toString());
					for(int i=0; i<selectionList.getSelectionSize(); i++){
						Region2D includedRegion = sketch.getRegAtIndex(Integer.parseInt(selectionList.getStringAtIndex(i)));
						if(includedRegion != null){
							CSG_Solid newSolid =  getSolidFromRegion(includedRegion, height, feat2D3D.ID, faceCounter);
							solid  = CSG_BooleanOperator.Union(solid, newSolid);
							LinkedList<Region2D> cutRegions = includedRegion.getRegionsToCut();
							for(Region2D cutReg : cutRegions){
								if(cutReg != null){
									CSG_Solid cutSolid = getSolidFromRegion(cutReg, height, feat2D3D.ID, faceCounter);
									solid = CSG_BooleanOperator.Subtraction(solid, cutSolid);
									
								}
							}
						}
					}
				}
			}catch(Exception ex){
				System.out.println("ToolBuildExtrudeModel(getBuiltSolid): " + ex.getClass().getName());
				ex.printStackTrace();
			}
		}
		if(!solid.isValidSolid()){
			System.out.println("ToolBuildExtrudeModel(getBuiltSolid): Solid was not valid!! returning default empty solid instead.");
			return new CSG_Solid();
		}	
		
		return solid;
	}

	public boolean isWorthKeeping(ParamSet paramSet) {
		// TODO: check for isWorthKeeping! 
		
		// if there is at least on region selected and the height != zero, then it is worth keeping.
		return true;
	}
	
	private CSG_Solid getSolidFromRegion(Region2D region, double height, int ID, int faceCounter){
		CSG_Solid solid = new CSG_Solid();
		// top Face
		CSG_Face topFace = region.getCSG_Face().getTranslatedCopy(new CSG_Vertex(0.0, 0.0, height));
		if(height >= 0.0){ // make sure normal points correct way (outward).
			topFace.flipFaceDirection();
		}
		topFace.setIsSelectable(new ModRef_Plane(ID, faceCounter++, new SketchPlane(topFace.getPlane())));
		solid.addFace(topFace);
		
		// bottom face
		CSG_Face botFace = region.getCSG_Face();
		if(height < 0.0){ // make sure normal points correct way (outward).
			botFace.flipFaceDirection();										
		}
		botFace.setIsSelectable(new ModRef_Plane(ID, faceCounter++, new SketchPlane(botFace.getPlane())));
		solid.addFace(botFace);
									
		region.getPrims().orientCycle();
		Iterator<Prim2D> iter = region.getPrims().iterator();
		while(iter.hasNext()){
			Prim2D prim2D = iter.next();
			if(prim2D instanceof Prim2DArc){
				Prim2DArc arc = (Prim2DArc)prim2D;
				CSG_Vertex arcCenterPt = new CSG_Vertex(arc.getArcCenterPoint(), 0.0);
				ModRef_Cylinder cylRef = new ModRef_Cylinder(ID, faceCounter++, arcCenterPt, topFace.getPlaneNormal(), arc.getArcRadius());
				addSimilarFacesByPointList(arc.getVertexList(25), null, cylRef, height, solid);
			}else{
				if(prim2D instanceof Prim2DLine){
					Prim2DLine line = (Prim2DLine)prim2D;
					CSG_Polygon poly = new CSG_Polygon(new CSG_Vertex(line.getPtA(), 0.0), new CSG_Vertex(line.getPtA(), height), 
														new CSG_Vertex(line.getPtB(), height), new CSG_Vertex(line.getPtB(), 0.0));
					CSG_Face newFace = new CSG_Face(poly);
					if(height < 0.0){
						newFace.flipFaceDirection();
					}
					newFace.setIsSelectable(new ModRef_Plane(ID, faceCounter++, new SketchPlane(newFace.getPlane())));
					solid.addFace(newFace);
				}else{
					addSimilarFacesByPointList(prim2D.getVertexList(25), null, null, height, solid);
				}
			}
			
		}
		return solid;
	}
	

	private void addSimilarFacesByPointList(LinkedList<Point2D> pointList, ModRef_Plane planeRef, ModRef_Cylinder cylRef, double height, CSG_Solid solid){
		CSG_Vertex lastVert        = null;
		CSG_Vertex lastVertExtrude = null;
		for(Point2D pt : pointList){
			if(lastVert == null){
				lastVert        = new CSG_Vertex(pt, 0.0);
				lastVertExtrude = new CSG_Vertex(pt, height);
			}else{
				CSG_Vertex newVert        = new CSG_Vertex(pt, 0.0);
				CSG_Vertex newVertExtrude = new CSG_Vertex(pt, height);
				CSG_Polygon poly = new CSG_Polygon(newVert, lastVert, lastVertExtrude, newVertExtrude);
				CSG_Face newFace = new CSG_Face(poly);
				if(height < 0.0){
					newFace.flipFaceDirection();
				}
				if(cylRef != null){
					newFace.setCylindricalReference(cylRef);
				}
				if(planeRef != null){
					newFace.setIsSelectable(planeRef);
				}
				solid.addFace(newFace);
				lastVert        = newVert;
				lastVertExtrude = newVertExtrude;
			}
		}
	}
	
	
}
