package backend.model.CSG;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;

import backend.adt.Rotation3D;
import backend.adt.Translation3D;
import backend.model.ref.ModRef_Plane;


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
	}
	
	public Object[] getPolygonArray(){
		return polygons.toArray();
	}
	
	public void addPolygon(CSG_Polygon polygon){
		polygons.add(polygon);
		bounds.includeBounds(polygon.getBounds());
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
	 * any of the face's convex polygons.
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
	 * @return the face's barycenter (the average of all vertices that define it);
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
	
	public void cleanupMarkedForDeletionPolygons(){
		for(int i=polygons.size()-1; i>= 0; i--){
			CSG_Polygon poly = polygons.get(i);
			if(poly.isMarkedForDeletion()){
				polygons.remove(i);
			}
		}
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
	 * by the user for creating subsequent sketchs.
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
				gl.glColor3d(0.9, 0.4, 0.4);
			}
			if(poly.type == CSG_Polygon.POLY_TYPE.POLY_OUTSIDE){ 	// green
				gl.glColor3d(0.4, 0.9, 0.4);
			}
			if(poly.type == CSG_Polygon.POLY_TYPE.POLY_OPPOSITE){	// blue
				gl.glColor3d(0.4, 0.4, 0.9);
			}
			if(poly.type == CSG_Polygon.POLY_TYPE.POLY_SAME){		// purple
				gl.glColor3d(0.8, 0.4, 0.8);
			}
			if(poly.type == CSG_Polygon.POLY_TYPE.POLY_UNKNOWN){	// gray
				gl.glColor3d(0.5, 0.5, 0.5);
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
			//if(selectable){
				// draw perimeter line
				gl.glColor3d(0.25, 0.25, 0.25);
				// TODO: don't have perimeter, just draw polygon outlines
				iterV = poly.getVertexIterator();
				gl.glBegin(GL.GL_LINE_LOOP);
				while(iterV.hasNext()){
					gl.glVertex3dv(iterV.next().getXYZ(), 0);
				}
				gl.glEnd();
			//}
		}
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
	
}
