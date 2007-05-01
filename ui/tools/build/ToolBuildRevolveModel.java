package ui.tools.build;

import javax.media.opengl.GL;

import ui.tools.ToolModelBuild;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.ParamType;
import backend.adt.SelectionList;
import backend.global.AvoGlobal;
import backend.model.Build;
import backend.model.Sketch;
import backend.model.CSG.BoolOp;
import backend.model.CSG.CSG_Solid;
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
public class ToolBuildRevolveModel implements ToolModelBuild{

	public void draw3DFeature(GL gl, Build feat2D3D) {
		// TODO Auto-generated method stub
		
	}

	public void finalize(ParamSet paramSet) {
		// finalize revolve and return to main menu
		Build feat2D3D = AvoGlobal.project.getActiveFeat2D3D();
		if(feat2D3D != null){
			Sketch sketch = feat2D3D.getPrimarySketch();
			if(sketch != null){
				// TODO: only keep feature and consume sketch if selectionLists are all satisfied as well.
				sketch.isConsumed = true;
				AvoGlobal.modelEventHandler.notifyActiveElementChanged();
			}else{
				AvoGlobal.project.getActivePart().removeActiveSubPart();				
			}
			
			// TODO: return to main menuet! (but this should be handled from the controller, not the model)
			
		}else{
			System.out.println("I have no idea what's going on?!?  the active feature2D3D was null!?!");
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
		pSet.addParam("angle", new Param("Angle", 360.0));
		pSet.addParam("offset", new Param("OffsetAngle", 0.0));
		pSet.addParam("regions", new Param("Regions", new SelectionList()));
		pSet.addParam("centerline", new Param("CenterLine", new SelectionList()));		
		return pSet;
	}

	public BoolOp getBooleanOperation(ParamSet pSet) {
		// TODO Auto-generated method stub
		return null;
	}

	public CSG_Solid getBuiltSolid(Build feat2D3D) {
		// TODO Auto-generated method stub
		return null;
	}

	public SketchPlane getSketchPlaneByID(Build feat2D3D, int faceID) {
		// TODO Auto-generated method stub
		return null;
	}

}
