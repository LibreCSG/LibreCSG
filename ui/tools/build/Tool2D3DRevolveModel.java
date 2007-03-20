package ui.tools.build;

import javax.media.opengl.GL;

import ui.tools.ToolModelBuild;
import backend.adt.ParamType;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.SelectionList;
import backend.global.AvoGlobal;
import backend.model.Feature2D3D;
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
public class Tool2D3DRevolveModel implements ToolModelBuild{

	public void draw3DFeature(GL gl, Feature2D3D feat2D3D) {
		// TODO Auto-generated method stub
		
	}

	public void finalize(ParamSet paramSet) {
		// finalize revolve and return to main menu
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
		ParamSet pSet = new ParamSet("Revolve", new Tool2D3DRevolveModel());
		pSet.addParam("angle", new Param("Angle", 360.0));
		pSet.addParam("offset", new Param("OffsetAngle", 0.0));
		pSet.addParam("regions", new Param("Regions", new SelectionList()));
		pSet.addParam("centerline", new Param("CenterLine", new SelectionList()));		
		return pSet;
	}

}
