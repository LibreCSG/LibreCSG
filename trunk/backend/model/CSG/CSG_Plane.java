package backend.model.CSG;


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
* @created Mar. 2007
*/

/**
* Constructive Solid Geometry :: Plane<br/><br/>
* 
* Defines a plane in 3D space via a normal and an offset.<br/><br/> 
* 
* Algorithms and structures from:<br/>
* - Laidlaw, Trumbore, and Hughes <br/>
* - "Constructive Solid Geometry for Polyhedral Objects"<br/>
* - SIGGRAPH 1986, Volume 20, Number 4, pp.161-170 * 
*/
public class CSG_Plane {

	private CSG_Vertex normal;
	private double offset;
	
	private final double TOL = 1e-10; // double tollerance 
	
	/**
	 * Construct a CSG_plane defined by a normal vector and an offset.<br/>
	 * The plane's equation is: <em>normal(dot)vertex + offset = 0</em>
	 * @param normal CSG_Vertex describing the plane's normal vector
	 * @param offset the plane's offset
	 */
	public CSG_Plane(CSG_Vertex normal, double offset){
		if(normal.getDistFromOrigin() < TOL){
			System.out.println("CSG_Plane: invalid normal for plane construction!");
			normal = new CSG_Vertex(1.0, 0.0, 0.0);
		}
		this.normal = normal;
		this.offset = offset;
	}
	
	/**
	 * @return the CSG_Vertex describing the plane's normal vector
	 */
	public CSG_Vertex getNormal(){
		return normal.deepCopy();
	}
	
	/**
	 * @return the plane's <em>offset</em> in the plane equation:<br/>
	 * <em>Ax + By + Cz + offset = 0</em>
	 */
	public double getOffset(){
		return offset;
	}
	
}
