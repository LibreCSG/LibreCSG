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
 * Constructive Solid Geometry :: Boolean Operators<br/><br/>
 * 
 * Algorithms and structures from:<br/>
 * - Laidlaw, Trumbore, and Hughes <br/>
 * - "Constructive Solid Geometry for Polyhedral Objects"<br/>
 * - SIGGRAPH 1986, Volume 20, Number 4, pp.161-170
 * 
 */
public class CSG_BooleanOperator {

	/**
	 * find the intersection of solidA and solidB and return it.
	 * @param solidA the "A" 3D CSG_Solid
	 * @param solidB the "B" 3D CSG_Solid
	 * @return the new CSG_Solid representing (A intersect B) or NULL if no intersection.
	 */
	public static CSG_Solid Intersection(CSG_Solid solidA, CSG_Solid solidB){
		// TODO: CSG Intersection
		return null;
	}
	
	/**
	 * find the union of solidA and solidB and return it.
	 * @param solidA the "A" 3D CSG_Solid
	 * @param solidB the "B" 3D CSG_Solid
	 * @return the new CSG_Solid representing (A union B) or NULL if no union.
	 */
	public static CSG_Solid Union(CSG_Solid solidA, CSG_Solid solidB){
		// TODO: CSG Union
		return null;
	}
	
	/**
	 * find the difference of solidA and solidB and return it.
	 * @param solidA the "A" 3D CSG_Solid
	 * @param solidB the "B" 3D CSG_Solid
	 * @return the new CSG_Solid representing (A - B) or NULL if result is no solid.
	 */
	public static CSG_Solid Subtraction(CSG_Solid solidA, CSG_Solid solidB){
		// TODO: CSG Subtraction
		return null;
	}
	
}
