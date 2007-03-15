package backend.model.CSG;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


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
 * Constructive Solid Geometry :: Face<br/><br/>
 * 
 * A CSG_Solid is a water-tight volume in 3D space:<br/>
 * 1. Composed of CSG_Faces<br/>
 * 2. No Dangling Faces (all connect to others exactly once at an edge<br/>
 * 
 * Algorithms and structures from:<br/>
 * - Laidlaw, Trumbore, and Hughes <br/>
 * - "Constructive Solid Geometry for Polyhedral Objects"<br/>
 * - SIGGRAPH 1986, Volume 20, Number 4, pp.161-170
 */
public class CSG_Solid {

	private List<CSG_Face>   faces    = new LinkedList<CSG_Face>();
	private CSG_Bounds bounds = new CSG_Bounds();
	
	/**
	 * Construct a new CSG_Solid with no Faces.  Faces should be added via addFace();
	 */
	public CSG_Solid(){
	}
	
	/**
	 * Construct a new CSG_Solid that includes the given Face.
	 * @param firstFace CSG_Face from which to start the CSG_Solid.
	 */
	public CSG_Solid(CSG_Face firstFace){
		faces.add(firstFace);
		bounds = firstFace.getBounds();
	}
	
	/**
	 * add a CSG_Face to this CSG_Solid
	 * @param f the CSG_Face to add.
	 */
	public void addFace(CSG_Face f){
		faces.add(f);
		bounds.includeBounds(f.getBounds());
	}
	
	/**
	 * @return the iterator over all faces in this CSG_Solid.
	 */
	public Iterator<CSG_Face> getFacesIter(){
		return faces.iterator();
	}
	
	public CSG_Bounds getBounds(){
		return bounds.deepCopy();
	}
	
	public CSG_Solid deepCopy(){
		CSG_Solid clone = new CSG_Solid();
		clone.bounds = bounds.deepCopy();
		for(CSG_Face f : faces){
			clone.faces.add(f.deepCopy());
		}
		return clone;
	}
	
	public void setAllPolygonsToUnknownType(){
		for(CSG_Face face : faces){
			Iterator<CSG_Polygon> polyIter = face.getPolygonIterator();
			while(polyIter.hasNext()){
				polyIter.next().type = CSG_Polygon.POLY_TYPE.POLY_UNKNOWN;
			}
		}
	}
}