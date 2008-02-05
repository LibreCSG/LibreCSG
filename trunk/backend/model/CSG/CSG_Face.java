package backend.model.CSG;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;

import backend.adt.Rotation3D;
import backend.adt.Translation3D;
import backend.model.ref.ModRef_Cylinder;
import backend.model.ref.ModRef_Plane;


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
 * A CSG_Face is a set of polygons that must:<br/>
 * 1. Be planar and Convex<br/>
 * 2. Have no 3 vertices that are collinear<br/>
 * 3. Have every vertex be unique<br/>
 * 4. Always be viewed as Clockwise from outside<br/><br/>
 * 
 * Algorithms and structures from:<br/>
 * - Laidlaw, Trumbore, and Hughes <br/>
 * - "Constructive Solid Geometry for Polyhedral Objects"<br/>
 * - SIGGRAPH 1986, Volume 20, Number 4, pp.161-170
 */
public class CSG_Face {
	
	private CSG_Bounds bounds;
	private CSG_Plane facePlane;	
	private List<CSG_Polygon> polygons = new LinkedList<CSG_Polygon>();
	private boolean selectable = false;
	private boolean isSelected = false;
	
	private ModRef_Plane relativePlane = null;
	private ModRef_Cylinder cylinderReference = null;
	
	private boolean perimNeedsUpdated = true;
	private LinkedList<CSG_Vertex> perimVertices = null;
	
	/**
	 * create a new Face (convex, planar, noncollinear polygons)<br/><br/>
	 * <b>More polygons may be added to this face, but they must remain coplanar!</b><br/>
	 * 
	 * @param polygon CSG_Polygon defining the face (or a part of it)
	 */
	public CSG_Face(CSG_Polygon polygon){
		polygons.add(polygon);
		facePlane = polygon.getPlane();
		bounds = polygon.getBounds();
		perimNeedsUpdated = true;
	}
	
	public Object[] getPolygonArray(){
		return polygons.toArray();
	}
	
	public void addPolygon(CSG_Polygon polygon){
		polygons.add(polygon);
		bounds.includeBounds(polygon.getBounds());
		perimNeedsUpdated = true;
	}
	
	public CSG_Bounds getBounds(){
		return bounds.deepCopy();
	}
	
	/**
	 * get the total area of all of the polygons 
	 * that make up this CSG_Face
	 * @return the area.
	 */
	public double getArea(){
		double area = 0.0;
		for(CSG_Polygon poly : polygons){
			area += poly.getArea();
		}
		return area;
	}
	
	/**
	 * check to see if the given vertex is inside this CSG_Face.
	 * This is done by checking if the vertex is contained in
	 * any of the face's convex polygons. vertices that fall 
	 * exactly on the edge are <em>not</em> considered to 
	 * be <em>inside</em> the Face.
	 * @param vert the CSG_Vertex to check
	 * @return true iff vertex is inside the face.
	 */
	public boolean vertexIsInsideFace(CSG_Vertex vert){
		for(CSG_Polygon poly : polygons){
			if(poly.vertexIsInsidePolygon(vert)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return the iterator over all of the Face's polygons.
	 */
	public Iterator<CSG_Polygon> getPolygonIterator(){
		return polygons.iterator();
	}
	
	/**
	 * @return the face's barycenter (the average of all vertices that define it, 
	 * which <b> could be located outside the perimeter of a convex face</b>).
	 * see "getFirstPolygonBarycenter()" if you require a vertex within the face.
	 */
	public CSG_Vertex getFaceBarycenter(){
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		int totalVertices = 0;
		for(CSG_Polygon polygon : polygons){
			Iterator<CSG_Vertex> iterV = polygon.getVertexIterator();
			while(iterV.hasNext()){
				CSG_Vertex v = iterV.next();
				totalVertices++;
				x += v.getX();
				y += v.getY();
				z += v.getZ();
			}
		}
		if(totalVertices > 0){
			return new CSG_Vertex(x/(double)totalVertices, y/(double)totalVertices, z/(double)totalVertices);
		}else{
			System.out.println("CSG_Face(getFaceBarycenter): totalVertices = 0!! ");
			return new CSG_Vertex(0.0, 0.0, 0.0);
		}
	}
	
	/**
	 * @return the first polygon's barycenter (the average of all vertices that define it) 
	 * which <b> is gauranteed to be located within the face.</b>).
	 */
	public CSG_Vertex getFirstPolygonBarycenter(){
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		int totalVertices = 0;
		Iterator<CSG_Vertex> iterV = polygons.get(0).getVertexIterator();
		while(iterV.hasNext()){
			CSG_Vertex v = iterV.next();
			totalVertices++;
			x += v.getX();
			y += v.getY();
			z += v.getZ();
		}		
		if(totalVertices > 0){
			return new CSG_Vertex(x/(double)totalVertices, y/(double)totalVertices, z/(double)totalVertices);
		}else{
			System.out.println("CSG_Face(getFirstPolygonBarycenter): totalVertices = 0!! ");
			return new CSG_Vertex(0.0, 0.0, 0.0);
		}
	}
	
	public void cleanupMarkedForDeletionPolygons(){
		for(int i=polygons.size()-1; i>= 0; i--){
			CSG_Polygon poly = polygons.get(i);
			if(poly.isMarkedForDeletion()){
				polygons.remove(i);
			}
		}
		perimNeedsUpdated = true;
	}
	
	/**
	 * @return a copy of the normal defined by the Face's plane.
	 */
	public CSG_Vertex getPlaneNormal(){
		return facePlane.getNormal();
	}
	
	/**
	 * @return the offset of the plane the contains this Face.
	 */
	public double getPlaneOffset(){
		return facePlane.getOffset();
	}
	
	public CSG_Plane getPlane(){
		return facePlane;
	}
	
	/**
	 * get the distance from a CSG_Vertex to the plane
	 * defined by this Face.
	 * @param v the CSG_Vertex to test
	 * @return distance from vertex to plane (true "zero" if within tollerance)
	 */
	public double distFromVertexToFacePlane(CSG_Vertex v){
		return facePlane.distFromVertex(v);
	}
	
	public CSG_Polygon getPolygonAtIndex(int i){
		if(i < 0 || i >= polygons.size()){
			System.out.println("CSG_Face(getPolygonAtIndex): index was invalid! i=" + i + ", size()=" + polygons.size());
			return null;
		}else{
			return polygons.get(i);
		}
	}
	
	public int getPolygonListSize(){
		return polygons.size();
	}
	
	public CSG_Face deepCopy(){
		CSG_Face clone = null;
		for(CSG_Polygon poly : polygons){
			if(clone == null){
				clone = new CSG_Face(poly.deepCopy());
			}else{
				clone.addPolygon(poly.deepCopy());
			}
		}
		clone.bounds = bounds.deepCopy();
		clone.selectable = this.selectable;
		clone.isSelected = this.isSelected;
		clone.relativePlane = this.relativePlane;
		clone.cylinderReference = this.cylinderReference;
		return clone;
	}
	
	/**
	 * @return true if this face is intended to be selectable
	 */
	public boolean isSelectable(){
		return selectable;
	}
	
	/**
	 * Set whether or not this CSG_Face should be selectable
	 * by the user for creating subsequent sketches.
	 * @param planeReference A ModRef_Plane specifying the relative
	 * plane upon which a subsequent sketch could be built.
	 */
	public void setIsSelectable(ModRef_Plane planeReference){
		this.selectable = true;
		if(planeReference != null){
			this.relativePlane = planeReference;
		}else{
			System.out.println("CSG_Face(setIsSelectable): a NULL ModRef_Plane was used! FIX THIS!");
		}
	}
	
	public void setCylindricalReference(ModRef_Cylinder cylinderReference){
		this.cylinderReference = cylinderReference;
	}
	
	public ModRef_Cylinder getModRefCylinder(){
		return this.cylinderReference;
	}
	
	public ModRef_Plane getModRefPlane(){
		if(!selectable){
			System.out.println("CSG_Face(getModRefPlane): what are you doing? the face is not even selectable!!");
			return null;
		}
		if(relativePlane != null){
			return relativePlane;
		}else{
			System.out.println("CSG_Face(getModRefPlane): the plane was null, even though it was selectable.. bad news :(");
			return null;
		}		
	}
	
	
	/**
	 * make this face NOT selectable.
	 */
	public void setIsNotSelectable(){
		this.selectable = false;
		this.relativePlane = null;
	}
	
	
	public boolean isSelected(){
		return isSelected;
	}
	
	public void setSelected(boolean isSelected){
		this.isSelected = isSelected;
	}
	
	public void drawFaceForDebug(GL gl){		
		for(CSG_Polygon poly : polygons){
			if(poly.type == CSG_Polygon.POLY_TYPE.POLY_INSIDE){ 	// red
				gl.glColor4d(0.9, 0.4, 0.4, 0.5);
			}
			if(poly.type == CSG_Polygon.POLY_TYPE.POLY_OUTSIDE){ 	// green
				gl.glColor4d(0.4, 0.9, 0.4, 0.5);
			}
			if(poly.type == CSG_Polygon.POLY_TYPE.POLY_OPPOSITE){	// blue
				gl.glColor4d(0.4, 0.4, 0.9, 0.5);
			}
			if(poly.type == CSG_Polygon.POLY_TYPE.POLY_SAME){		// purple
				gl.glColor4d(0.8, 0.4, 0.8, 0.5);
			}
			if(poly.type == CSG_Polygon.POLY_TYPE.POLY_UNKNOWN){	// gray
				gl.glColor4d(0.5, 0.5, 0.5, 0.5);
			}
			Iterator<CSG_Vertex> iterV = poly.getVertexIterator();
			gl.glBegin(GL.GL_POLYGON);
			while(iterV.hasNext()){
				gl.glVertex3dv(iterV.next().getXYZ(), 0);
			}
			gl.glEnd();
		}
	}
	
	public void drawFaceLinesForDebug(GL gl){
		for(CSG_Polygon poly : polygons){			
			if(poly.type == CSG_Polygon.POLY_TYPE.POLY_INSIDE){ 	// red
				gl.glColor3d(0.5, 0.2, 0.2);
			}
			if(poly.type == CSG_Polygon.POLY_TYPE.POLY_OUTSIDE){ 	// green
				gl.glColor3d(0.2, 0.5, 0.2);
			}
			if(poly.type == CSG_Polygon.POLY_TYPE.POLY_OPPOSITE){	// blue
				gl.glColor3d(0.2, 0.2, 0.5);
			}
			if(poly.type == CSG_Polygon.POLY_TYPE.POLY_SAME){		// purple
				gl.glColor3d(0.5, 0.2, 0.5);
			}
			if(poly.type == CSG_Polygon.POLY_TYPE.POLY_UNKNOWN){	// gray
				gl.glColor3d(0.25, 0.25, 0.25);
			}
			Iterator<CSG_Vertex> iterV = poly.getVertexIterator();
			gl.glBegin(GL.GL_LINE_LOOP);
			while(iterV.hasNext()){
				gl.glVertex3dv(iterV.next().getXYZ(), 0);
			}
			gl.glEnd();
		}
	}
	
	
	
	public void glDrawFace(GL gl){
		for(CSG_Polygon poly : polygons){
			if(selectable && isSelected){
				gl.glColor3d(0.4, 0.9, 0.7);
			}else{
				gl.glColor3d(0.4, 0.9, 0.4);
			}
			Iterator<CSG_Vertex> iterV = poly.getVertexIterator();
			gl.glBegin(GL.GL_POLYGON);
			while(iterV.hasNext()){
				gl.glVertex3dv(iterV.next().getXYZ(), 0);
			}
			gl.glEnd();
		}
		// draw perimeter.. :)
		gl.glColor3d(0.25, 0.25, 0.25);
		Iterator<CSG_Vertex> iterPerim = this.getPerimeterVertices().iterator();
		gl.glBegin(GL.GL_LINE_LOOP);
			while(iterPerim.hasNext()){
				gl.glVertex3dv(iterPerim.next().getXYZ(), 0);
			}
		gl.glEnd();
	}
	
	
	public LinkedList<CSG_Vertex> getPerimeterVertices(){
		if(perimVertices != null && !perimNeedsUpdated){
			return perimVertices;
		}else{
			perimVertices = computePerimeterVertices();
			perimNeedsUpdated = false;
			return perimVertices;
		}
	}
	
	private LinkedList<CSG_Vertex> computePerimeterVertices(){
		LinkedList<CSG_Vertex>  perim = new LinkedList<CSG_Vertex>();
		
		// make a copy of the list so we can add/remove items freely.
		LinkedList<CSG_Polygon> polys = new LinkedList<CSG_Polygon>();
		for(CSG_Polygon poly : polygons){
			polys.add(poly);
		}
		
		boolean isFirstTime = true;
		int lastSize = Integer.MAX_VALUE;
		while(lastSize > polys.size()){
			lastSize = polys.size();
			for(int n=0; n<polys.size(); n++){
				CSG_Polygon poly = polys.get(n);
				if(isFirstTime){
					// first time, just add the entire polygon.
					Iterator<CSG_Vertex> iter = poly.getVertexIterator();
					while(iter.hasNext()){
						perim.add(iter.next());
					}
					polys.removeFirst();
					isFirstTime = false;
				}else{				
					// look for a shared edge.. 
					// if it exists, squish the two polygons together.
					double TOL = 1e-12;
					boolean gotoNext = false;
					int polyVerts = poly.getNumberVertices();
					for(int i=0; i<polyVerts; i++){
						CSG_Vertex pVertA = poly.getVertAtModIndex(i);
						for(int j=0; j<perim.size(); j++){
							CSG_Vertex ptA = perim.get(j);
							if(pVertA.getDistBetweenVertices(ptA) < TOL){
								// points matched.. check the next one
								CSG_Vertex pVertB = poly.getVertAtModIndex(i+1);
								CSG_Vertex ptB = perim.get((j-1+perim.size())%perim.size());
								if(pVertB.getDistBetweenVertices(ptB) < TOL){
									// edge matched!
									for(int q=i; q>i-poly.getNumberVertices()+1; q--){
										perim.add(j, poly.getVertAtModIndex(q));
									}
									polys.remove(n);
									gotoNext = true;
								}
							}
							if(gotoNext){
								break;
							}
						}
						if(gotoNext){
							break;
						}
					}
					
				}
			}
		}
		return perim;
	}
	
	
	public void drawFaceNormalsForDebug(GL gl){
		for(CSG_Polygon poly : polygons){			
			poly.drawPolygonNormalsForDebug(gl);
		}
	}
	
	/**
	 * check to make sure face is valid (all polygons valid and in same plane)
	 * @return true is face is valid
	 */
	public boolean isValidFace(){
		// TODO: check to see if this is a valid face! 
		// (all polygons same plane and all polygons valid)
		return true;
	}
	
	public CSG_Face getTranslatedCopy(CSG_Vertex translation){
		CSG_Face tFace = this.deepCopy();
		for(int i=0; i < tFace.polygons.size(); i++){
			tFace.polygons.set(i, tFace.polygons.get(i).getTranslatedCopy(translation));
		}
		return tFace;
	}
	
	public void flipFaceDirection(){
		for(CSG_Polygon poly : polygons){
			poly.reverseVertexOrder();
			
		}
		facePlane = polygons.get(0).getPlane();
	}
	
	/**
	 * apply rotation in X, Y, Z order, then translation.
	 * @param translation 3D translation
	 * @param rotation 3D rotation
	 */
	public void applyTranslationRotation(Translation3D translation, Rotation3D rotation){
		Iterator<CSG_Polygon> polyIter = polygons.iterator();
		while(polyIter.hasNext()){
			polyIter.next().applyTranslationRotation(translation, rotation);
		}
	}
	
	/**
	 * get the closest distance from this face to another.
	 * @param face2 the other face.
	 * @return the distance.
	 */
	public double getDistanceToFace(CSG_Face face2){
		double distance = Double.MAX_VALUE;
		Iterator<CSG_Polygon> polyIter = polygons.iterator();
		while(polyIter.hasNext()){
			CSG_Polygon poly = polyIter.next();
			Iterator<CSG_Polygon> poly2Iter = face2.polygons.iterator();
			while(poly2Iter.hasNext()){
				CSG_Polygon poly2 = poly2Iter.next();
				double distCheck = poly.getClosestDistanceToPoly(poly2);
				if(distCheck < distance){
					distance = distCheck;
				}
			}
		}
		return distance;
	}
	
	/**
	 * combine this face with the face given as the argument.  
	 * The faces will be joined via their closest vertices.
	 * @param face2 the face to combine this one with.
	 */
	/*
	public void combineWithFace(CSG_Face face2){
		double distance = Double.MAX_VALUE;
		CSG_Polygon joiningPolygon = null;
		Iterator<CSG_Polygon> polyIter = polygons.iterator();
		while(polyIter.hasNext()){
			CSG_Polygon poly = polyIter.next();
			Iterator<CSG_Polygon> poly2Iter = face2.polygons.iterator();
			while(poly2Iter.hasNext()){
				CSG_Polygon poly2 = poly2Iter.next();
				double distCheck = poly.getClosestDistanceToPoly(poly2);
				if(distCheck < distance){
					distance = distCheck;
					joiningPolygon = poly.getClosestJoiningPolygon(poly2);
				}
			}
		}
		if(joiningPolygon == null){
			System.out.println("Tried to Join CSG_Faces with a NULL polygon.. abort. :(");
			return;
		}
		polygons.add(joiningPolygon);	
		for(CSG_Polygon polyToAdd : face2.polygons){
			polygons.add(polyToAdd);
		}
	}
	//*/
	
}
