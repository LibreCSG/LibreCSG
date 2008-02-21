package backend.model.CSG;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;

import backend.adt.Rotation3D;
import backend.adt.Translation3D;


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
* @created Mar. 2007
*/

/**
 * Constructive Solid Geometry :: Face<br/><br/>
 * 
 * A CSG_Solid is a water-tight volume in 3D space:<br/>
 * 1. Composed of CSG_Faces<br/>
 * 2. No Dangling Faces (all connect to others exactly once at an edge (water-tight)<br/>
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
	 * Get the number of faces on this CSG_Solid. 
	 * @return the total number of faces on the solid. 
	 */
	public int getNumberOfFaces(){
		return faces.size();
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
	
	/**
	 * cleanup the solid.  This may be kind of computationally expensive...
	 */
	public void clean(){
		Iterator<CSG_Face> fIter = faces.iterator();
		while(fIter.hasNext()){
			CSG_Face f = fIter.next();
			f.cleanupMarkedForDeletionPolygons();
			f.cleanupBogusPolygons();
		}
		for(int i=faces.size()-1; i >= 0; i--){
			if(!faces.get(i).isValidFace()){
				System.out.println("CSG_Solid: Clean() is removing a face because it is not valid.");
				faces.remove(i);
			}
		}
	}
	
	public void setAllPolygonsToUnknownType(){
		for(CSG_Face face : faces){
			Iterator<CSG_Polygon> polyIter = face.getPolygonIterator();
			while(polyIter.hasNext()){
				polyIter.next().type = CSG_Polygon.POLY_TYPE.POLY_UNKNOWN;
			}
		}
	}
	
	/**
	 * check to make sure solid is valid. (all faces vaild and solid is water-tight)
	 * @return true if solid is valid
	 */
	public boolean isValidSolid(){
		// TODO: check to make sure the solid is water-tight and that each face is valid! 
		return true;
	}
	
	public void glDrawSolid(GL gl){
		Iterator<CSG_Face> iter = faces.iterator();
		while(iter.hasNext()){
			CSG_Face f = iter.next();
			f.glDrawFace(gl);
		//	f.drawFaceForDebug(gl);
		//	f.drawFaceLinesForDebug(gl);
		//	f.drawFaceNormalsForDebug(gl);
		}	
	}
	
	public void glDrawSelectedElements(GL gl){
		Iterator<CSG_Face> iter = faces.iterator();
		while(iter.hasNext()){
			CSG_Face f = iter.next();
			if(f.isSelected()){
				f.glDrawFace(gl);
			}
		}	
	}
	
	public void glDrawImportantEdges(GL gl){
		Iterator<CSG_Face> iter = faces.iterator();
		while(iter.hasNext()){
			CSG_Face f = iter.next();
			f.glDrawImportantEdges(gl);
		}	
	}
	
	public void printAllPolygonClassification(){
		Iterator<CSG_Face> fIter = this.getFacesIter();
		while(fIter.hasNext()){
			CSG_Face f = fIter.next();
			System.out.println(f + " with area " + f.getArea());
			Iterator<CSG_Polygon> pIter = f.getPolygonIterator();
			while(pIter.hasNext()){
				CSG_Polygon p = pIter.next();
				System.out.println("  " + p.type + " -- " + p);
			}
		}
	}
	
	/**
	 * apply rotation in X, Y, Z order, then translation.
	 * @param translation 3D translation
	 * @param rotation 3D rotation
	 */
	public void applyTranslationRotation(Translation3D translation, Rotation3D rotation){
		Iterator<CSG_Face> facesIter = faces.iterator();
		while(facesIter.hasNext()){
			facesIter.next().applyTranslationRotation(translation, rotation);
		}
	}
	
}
