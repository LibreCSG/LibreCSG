package backend.model.ref;

import backend.model.Part;
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
* @created Apr. 2007
*/

public class ModRef_Plane extends ModelReference{

	private final int uniqueSubPartID;
	private final int uniqueFaceID;
	private final SketchPlane sketchPlane;
	
	// planes are built with reference to a particular part, feat2D3D, and faceID within that feature.
	
	/**
	 * a plane reference is build upon an existing SubPart 
	 * that has a unique face associated with it.
	 * @param uniqueSubPartID the unique ID of the SubPart to use
	 * @param uniqueFaceID the unique ID of the face created by the SubPart.
	 */
	public ModRef_Plane(int uniqueSubPartID, int uniqueFaceID, SketchPlane sketchPlane){
		super(ModRefType.Plane);
		this.uniqueSubPartID = uniqueSubPartID;
		this.uniqueFaceID    = uniqueFaceID;
		this.sketchPlane = sketchPlane;
		if(sketchPlane == null){
			System.out.println("You passed in a NULL sketchplane to ModRef_Plane.. BAD IDEA!!");
		}
	}
	
	public SketchPlane getSketchPlane(Part part){
		return sketchPlane;
	}
	
	public int getUniqueFaceID(){
		return uniqueFaceID;
	}
	
	@Override
	public String getStringReferenceInfo() {
		return "SubPartID:" + uniqueSubPartID + ", FaceID:" + uniqueFaceID;
	}
	
}
