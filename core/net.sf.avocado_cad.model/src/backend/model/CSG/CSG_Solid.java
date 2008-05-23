package backend.model.CSG;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.avocado_cad.model.api.adt.IRotation3D;
import net.sf.avocado_cad.model.api.adt.ITranslation3D;
import net.sf.avocado_cad.model.api.csg.ICSGSolid;
import net.sf.avocado_cad.model.api.csg.POLY_TYPE;


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
public class CSG_Solid implements ICSGSolid {

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
	
	
	public Collection<CSG_Face> getFaces() {
		return faces;
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
				polyIter.next().type = POLY_TYPE.POLY_UNKNOWN;
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
	
	/**
	 * find boundaries between faces.  store vertices in a list for rendering later.
	 */
	public void computeAllMatingEdgeLines(){
		// TODO: this does not find edges that are split on one side 
		// (i.e., one face is of a box is a rect. while another face 
		//  is split in many triangles.)
		Iterator<CSG_Face> fIter = faces.iterator();
		while(fIter.hasNext()){
			CSG_Face f = fIter.next();
			f.matingEdgeLines = new LinkedList<CSG_Vertex>(); // start with empty list.
			Iterator<CSG_Polygon> pIter = f.getPolygonIterator();
			while(pIter.hasNext()){
				CSG_Polygon p = pIter.next();
				// now check for each pair of points in the polygon.
				Iterator<CSG_Vertex> vIter = p.getVertexIterator();
				CSG_Vertex v1 = null;
				CSG_Vertex v2 = p.getVertAtModIndex(p.getNumberVertices()-1);
				while(vIter.hasNext()){
					v1 = vIter.next();
					Iterator<CSG_Face> fIter2 = faces.iterator();
					while(fIter2.hasNext()){
						CSG_Face f2 = fIter2.next();
						boolean hasSamePlane = false; //f.isSelectable() && f2.isSelectable() && f.getModRefPlane() == f2.getModRefPlane();
						boolean hasSameCyl = false; //f.getModRefCylinder() != null && f.getModRefCylinder() == f2.getModRefCylinder();
						if(f2 != f && !hasSamePlane && !hasSameCyl && f2.containsAdjacentVertexPair(v1, v2)){
							f.matingEdgeLines.add(v1);
							f.matingEdgeLines.add(v2);
							//System.out.println("Mating Edge Hit!");
							break; // HIT! no need to keep checking other faces.
						}
					}					
					v2 = v1; 
				}
			}
		}
	
	}
	
	
	public void printAllPolygonClassification(){
		for(CSG_Face f : this.getFaces()) {
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
	public void applyTranslationRotation(ITranslation3D translation, IRotation3D rotation){
		Iterator<CSG_Face> facesIter = faces.iterator();
		while(facesIter.hasNext()){
			facesIter.next().applyTranslationRotation(translation, rotation);
		}
	}
	
}
