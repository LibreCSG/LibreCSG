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
import backend.model.Feature2D;
import backend.model.Part;
import backend.model.Sketch;
import backend.model.CSG.BoolOp;
import backend.model.CSG.CSG_BooleanOperator;
import backend.model.CSG.CSG_Face;
import backend.model.CSG.CSG_Polygon;
import backend.model.CSG.CSG_Solid;
import backend.model.CSG.CSG_Vertex;
import backend.model.ref.ModRef_Plane;
import backend.model.sketch.Point2DList;
import backend.model.sketch.Prim2D;
import backend.model.sketch.Prim2DLine;
import backend.model.sketch.Region2D;
import backend.model.sketch.SketchPlane;



//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the 
//GNU General Public License (GPL).

//This file is part of avoCADo.

//AvoCADo is free software; you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation; either version 2 of the License, or
//(at your option) any later version.

//AvoCADo is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

//You should have received a copy of the GNU General Public License
//along with AvoCADo; if not, write to the Free Software
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA


/*
 * @author  Adam Kumpf
 * @created Feb. 2007
 */
public class ToolBuildRevolveModel implements ToolModelBuild{

	public void finalize(ParamSet paramSet) {
		System.out.println("Finalizig Revolve");
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
			System.out.println("ToolBuildRevolveModel(finalize): I have no idea what's going on?!?  the active feature2D3D was null!?!");
		}
	}

	public boolean paramSetIsValid(ParamSet paramSet) {
		//		 ParamSet:  "Revolve"
		// --------------------------------
		// # "regions"     ->  "Regions"     <SelectionList>
		// # "centerline"  ->  "CenterLine"  <SelectionList>
		// # "angle"       ->  "Angle"       <Double>
		// # "offset"      ->  "OffsetAngle" <Double>
		// --------------------------------		
		boolean isValid = (	paramSet != null &&
				paramSet.label == "Revolve" &&
				paramSet.hasParam("regions", ParamType.SelectionList) &&
				paramSet.hasParam("centerline", ParamType.SelectionList) &&
				paramSet.hasParam("angle", ParamType.Double) &&
				paramSet.hasParam("offset", ParamType.Double));
		return isValid;
	}

	public void updateDerivedParams(ParamSet paramSet) {
		// no derive parameters for this feature.	
	}

	public ParamSet constructNewParamSet() {
		ParamSet pSet = new ParamSet("Revolve", new ToolBuildRevolveModel());
		pSet.addParam("angle", new Param("Angle", 180.0));
		pSet.addParam("offset", new Param("OffsetAngle", 0.0));
		pSet.addParam("regions", new Param("Regions", new SelectionList()));
		pSet.addParam("centerline", new Param("CenterLine", new SelectionList()));
		return pSet;
	}

	public BoolOp getBooleanOperation(ParamSet pSet) {
		// TODO Auto-generated method stub
		return BoolOp.Union;
	}

	public CSG_Solid getBuiltSolid(Build feat2D3D) {
		ParamSet paramSet = feat2D3D.paramSet;
		Sketch sketch = feat2D3D.getPrimarySketch();
		CSG_Solid solid = new CSG_Solid();
		if(sketch != null && paramSet != null){					
			try{
				SelectionList selectionList = paramSet.getParam("regions").getDataSelectionList();
				Double angle = paramSet.getParam("angle").getDataDouble();
				Double offset = paramSet.getParam("offset").getDataDouble();
				SelectionList centerLineSel = paramSet.getParam("centerline").getDataSelectionList();
				int faceCounter = 1;
				// TODO: This is a CRAZY amount of nested IFs just to make sure the centerline is correct. 
				//       Perhaps the ParamSet should include some functionality to alleviate this!
				if(selectionList != null && angle != null && offset != null && 
						centerLineSel != null && selectionList.getSelectionSize() > 0 && 
						centerLineSel.getSelectionSize()  > 0){
					String[] regNum = centerLineSel.getStringAtIndex(0).split("\\.");
					if(regNum.length == 2 && sketch.getFeat2DListSize() > Integer.parseInt(regNum[0])){
						Feature2D feat2D = sketch.getAtIndex(Integer.parseInt(regNum[0]));
						if(feat2D != null && feat2D.prim2DList.size() > Integer.parseInt(regNum[1])){
							Prim2D prim2DCLMaybe = feat2D.prim2DList.get(Integer.parseInt(regNum[1]));
							if(prim2DCLMaybe instanceof Prim2DLine){
								Prim2DLine prim2DCenterLine = (Prim2DLine)prim2DCLMaybe;
								for(int i=0; i<selectionList.getSelectionSize(); i++){
									Region2D includedRegion = sketch.getRegAtIndex(Integer.parseInt(selectionList.getStringAtIndex(i)));
									
									if(includedRegion != null){
										CSG_Solid newSolid =  getSolidFromRegion(includedRegion, angle, offset, prim2DCenterLine, feat2D3D.ID, faceCounter);
										solid  = CSG_BooleanOperator.Union(solid, newSolid);
										LinkedList<Region2D> cutRegions = includedRegion.getRegionsToCut();
										for(Region2D cutReg : cutRegions){
											if(cutReg != null){
												CSG_Solid cutSolid = getSolidFromRegion(cutReg, angle, offset, prim2DCenterLine, feat2D3D.ID, faceCounter);
												solid = CSG_BooleanOperator.Subtraction(solid, cutSolid);
											}
										}
									}
								}
							}else{
								System.out.println("ToolBuilRevolveModel(getBuiltSolid): centerline was not a Prim2DLine!! aborting. ");
							}
						}else{
							System.out.println("ToolBuilRevolveModel(getBuiltSolid): centerline prim2DList index out of range!! aborting. ");
						}
					}else{
						System.out.println("ToolBuilRevolveModel(getBuiltSolid): centerline feature2D index out of range!! aborting. ");
					}
				}
			}catch(Exception ex){
				System.out.println("ToolBuilRevolveModel(getBuiltSolid): " + ex.getClass().getName());
				ex.printStackTrace();
			}
		}
		if(!solid.isValidSolid()){
			System.out.println("ToolBuildRevolveModel(getBuiltSolid): Solid was not valid!! returning default empty solid instead.");
			return new CSG_Solid();
		}	

		return solid;
	}

	private CSG_Solid getSolidFromRegion(Region2D region, double angle, double offset, Prim2DLine centerline, int ID, int faceCounter){
		if(angle < 0.5 && angle > -0.5){
			System.out.println("ToolBuildRevolveModel: Angle was too small.. aborting solid creation...");
			return null;
		}
		double radAngle = angle * Math.PI / 180.0;
		int angleSteps = (int)Math.max(Math.abs(angle)/10, 10);
		double angleStep = radAngle/angleSteps;
		CSG_Solid solid = new CSG_Solid();
		double radOffAngle = offset * Math.PI / 180.0;

		// bottom face
//		CSG_Face startFace = region.getCSG_Face();
//		if(angle > 0.0){ // make sure normal points correct way (outward).
//			startFace.flipFaceDirection();										
//		}
//		startFace.setIsSelectable(new ModRef_Plane(ID, faceCounter++));
//		solid.addFace(startFace);

		Point2DList pList = region.getPeremeterPointList();

		if(pList.size() >= 3){
			// assuming orientation relative to x-axis revolve...
			
			// find
			double xdiff = centerline.getPtB().getX() - centerline.getPtA().getX();
			double ydiff = centerline.getPtB().getY() - centerline.getPtA().getY();
			
			double xoffset = 0.0;
			double yoffset = 0.0;
			double zrot = 0.0;
			boolean flipAll = false;
			if(ydiff == 0.0){
				// line is parallel to x-axis, just run a yoffset!
				yoffset = -centerline.getPtA().getY();
			}else{
				// line is not parallel to x-axis.
				double slopeInv = xdiff / ydiff;
				if(slopeInv == 0.0){
					// line is vertical (parallel to y-axis)
					System.out.println("Line is parallel to y-axis!");
					xoffset = -centerline.getPtA().getX();
					zrot = -Math.PI/2;
				}else{
					double clAngle = Math.atan(1/slopeInv);						
					// okay, back to the basics here.. 
					// for a line: y=mx+yintercept
					double m = 1/slopeInv; // okay to do this since we know it is non-zero.
					double yintercept = centerline.getPtA().getY() - m*centerline.getPtA().getX();
					double xintercept = (0.0 - yintercept) / m;									
					xoffset = -xintercept;
					zrot = -clAngle;											
				}
			}			
			
			// now do the math to revolve the shell around the centerline.
			CSG_Vertex preTrans = new CSG_Vertex(xoffset, yoffset, 0.0);
			Rotation3D preRot = new Rotation3D(0.0, 0.0, zrot);
			Translation3D postTrans = new Translation3D(-xoffset, -yoffset, 0.0);
			Rotation3D postRot = new Rotation3D(0.0, 0.0, -zrot);
			Translation3D noTrans3D = new Translation3D(0.0, 0.0, 0.0);
			
			// test vertex to see if the normals need to be flipped.
			CSG_Vertex bary = region.getCSG_Face().getFaceBarycenter();
			bary = bary.addToVertex(preTrans).getTranslatedRotatedCopy(noTrans3D, preRot);
			if(bary.getY() < 0.0){
				flipAll = true;
			}			
			
			// add first face...
			CSG_Face startFace = region.getCSG_Face().getTranslatedCopy(preTrans);
			startFace.applyTranslationRotation(noTrans3D, preRot);
			startFace.applyTranslationRotation(noTrans3D, new Rotation3D(radOffAngle, 0.0, 0.0));
			startFace.applyTranslationRotation(postTrans, postRot);
			startFace.setIsSelectable(new ModRef_Plane(ID, faceCounter++));
			if(angle <= 0.0){ // make sure normal points correct way (outward).
				startFace.flipFaceDirection();										
			}
			startFace.setIsSelectable(new ModRef_Plane(ID, faceCounter++));
			solid.addFace(startFace);
			
			// add all the side faces.
			for(int i=0; i<angleSteps; i++){			
				CSG_Vertex lastVert = new CSG_Vertex(pList.getLast(), 0.0);
				CSG_Vertex curVert = new CSG_Vertex(pList.getLast(), 0.0).addToVertex(preTrans).getTranslatedRotatedCopy(noTrans3D, preRot);
				Iterator<Point2D> pIter = pList.iterator();		
				while(pIter.hasNext()){
					lastVert = curVert;
					curVert = new CSG_Vertex(pIter.next(), 0.0).addToVertex(preTrans).getTranslatedRotatedCopy(noTrans3D, preRot);				
					CSG_Vertex a = lastVert.getTranslatedRotatedCopy(noTrans3D, new Rotation3D(radOffAngle+angleStep*i, 0.0, 0.0)).getTranslatedRotatedCopy(postTrans, postRot);
					CSG_Vertex b = lastVert.getTranslatedRotatedCopy(noTrans3D, new Rotation3D(radOffAngle+angleStep*(i+1), 0.0, 0.0)).getTranslatedRotatedCopy(postTrans, postRot);
					CSG_Vertex c = curVert.getTranslatedRotatedCopy(noTrans3D, new Rotation3D(radOffAngle+angleStep*(i+1), 0.0, 0.0)).getTranslatedRotatedCopy(postTrans, postRot);
					CSG_Vertex d = curVert.getTranslatedRotatedCopy(noTrans3D, new Rotation3D(radOffAngle+angleStep*i, 0.0, 0.0)).getTranslatedRotatedCopy(postTrans, postRot);
					CSG_Polygon newPoly = new CSG_Polygon(a, b, c, d);
					if(angle < 0.0){
						newPoly.reverseVertexOrder();
					}
					CSG_Face newFace = new CSG_Face(newPoly);
					if(newFace.isValidFace()){
						solid.addFace(newFace);
					}
				}
			}		
			
			// close it up! :)
			CSG_Face endFace = region.getCSG_Face().getTranslatedCopy(preTrans);
			endFace.applyTranslationRotation(noTrans3D, preRot);
			endFace.applyTranslationRotation(noTrans3D, new Rotation3D(radOffAngle+radAngle, 0.0, 0.0));
			endFace.applyTranslationRotation(postTrans, postRot);
			endFace.setIsSelectable(new ModRef_Plane(ID, faceCounter++));
			if(angle >= 0.0){ // make sure normal points correct way (outward).
				endFace.flipFaceDirection();
			}
			solid.addFace(endFace);
			
			// check for a "flip all" case where region is on other side of the centerline.
			if(flipAll){
				Iterator<CSG_Face> fIter = solid.getFacesIter();
				while(fIter.hasNext()){
					CSG_Face f = fIter.next();
					f.flipFaceDirection();
				}
			}
			System.out.println("FlipAll: " + flipAll);
		}
		
		

		return solid;
	}

	public boolean isWorthKeeping(ParamSet paramSet) {
		// TODO: check for isWorthKeeping! 
		return true;
	}

}
