package backend.model.ref;

import backend.model.CSG.CSG_Vertex;

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
* @created Feb. 2008
*/

public class ModRef_Cylinder extends ModelReference{

	private final int uniqueSubPartID;
	private final int uniqueFaceID;
	private final CSG_Vertex centerPoint;
	private final CSG_Vertex normal;
	private final double radius;
	
	// planes are built with reference to a particular part, feat2D3D, and faceID within that feature.
	
	/**
	 * a cylinder reference is built upon an existing SubPart 
	 * that has a unique face associated with it.
	 * @param uniqueSubPartID the unique ID of the SubPart to use
	 * @param uniqueFaceID the unique ID of the face created by the SubPart.
	 * @param centerPoint the center of the circle/cylinder
	 * @param normal the normal along which the axis of the cylinder is situated
	 * @param radius the radius of the cylinder
	 */
	public ModRef_Cylinder(int uniqueSubPartID, int uniqueFaceID, CSG_Vertex centerPoint, CSG_Vertex normal, double radius){
		super(ModRefType.Plane);
		this.uniqueSubPartID = uniqueSubPartID;
		this.uniqueFaceID    = uniqueFaceID;
		this.centerPoint = centerPoint;
		this.normal = normal;
		this.radius = radius;
	}
	
	public CSG_Vertex getCenterPoint(){
		return centerPoint;
	}
	
	public CSG_Vertex getNormal(){
		return normal;
	}
	
	public double getRadius(){
		return radius;
	}
	
	public int getUniqueFaceID(){
		return uniqueFaceID;
	}
	
	@Override
	public String getStringReferenceInfo() {
		return "SubPartID:" + uniqueSubPartID + ", FaceID:" + uniqueFaceID;
	}

}
