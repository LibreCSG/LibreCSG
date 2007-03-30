package backend.model.CSG;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;


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
* Constructive Solid Geometry :: Polygon<br/><br/>
* 
* A convex planar non-collinear polygon in 3D space.<br/><br/> 
* 
* Algorithms and structures from:<br/>
* - Laidlaw, Trumbore, and Hughes <br/>
* - "Constructive Solid Geometry for Polyhedral Objects"<br/>
* - SIGGRAPH 1986, Volume 20, Number 4, pp.161-170 * 
*/
public class CSG_Polygon {

	private LinkedList<CSG_Vertex> vertices = new LinkedList<CSG_Vertex>();
	private CSG_Bounds bounds;
	public enum POLY_TYPE {POLY_INSIDE, POLY_OUTSIDE, POLY_SAME, POLY_OPPOSITE, POLY_UNKNOWN};
	public POLY_TYPE type = POLY_TYPE.POLY_UNKNOWN;
	private final double TOL = 1e-10; // double tollerance 
	private CSG_Plane polygonPlane;

	private boolean markedForDeletion = false;
	
	/**
	 * create a new Polygon (convex, planar, noncollinear)<br/><br/>
	 * <b>Vertices should be added in Clockwise order looking from outside of polygon</b><br/>
	 * Polygons must have at least 3 vertices, but they can have
	 * more, as long as the polygon remain convex, planar, and noncollinear.<br/>
	 * 
	 * @param v1 1st CSG_Vertex
	 * @param v2 2nd CSG_Vertex
	 * @param v3 3rd CSG_Vertex
	 * @param v4 4th CSG_Vertex
	 */
	public CSG_Polygon(CSG_Vertex v1, CSG_Vertex v2, CSG_Vertex v3, CSG_Vertex v4){
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);
		vertices.add(v4);
		bounds = new CSG_Bounds(v1);
		bounds.includeVertex(v2);
		bounds.includeVertex(v3);
		bounds.includeVertex(v4);
		polygonPlane = computePlane();
	}
	
	/**
	 * create a new Polygon (convex, planar, noncollinear)<br/><br/>
	 * <b>Vertices should be added in Clockwise order looking from outside of polygon</b><br/>
	 * Polygons must have at least 3 vertices, but they can have
	 * more, as long as the polygon remain convex, planar, and noncollinear.<br/>
	 * 
	 * @param v1 1st CSG_Vertex
	 * @param v2 2nd CSG_Vertex
	 * @param v3 3rd CSG_Vertex
	 */
	public CSG_Polygon(CSG_Vertex v1, CSG_Vertex v2, CSG_Vertex v3){
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);
		bounds = new CSG_Bounds(v1);
		bounds.includeVertex(v2);
		bounds.includeVertex(v3);
		polygonPlane = computePlane();
	}
	
	/**
	 * add a CSG_Vertex to the end of the vertex list
	 * @param newVertex the CSG_Vertex to be added.
	 */
	public void addVertex(CSG_Vertex newVertex){
		// TODO: check to make sure added point is coplanar and noncollinear
		vertices.add(newVertex);
		bounds.includeVertex(newVertex);
	}
	
	/**
	 * @return a copy of the rectangular bounds of this  CSG_Polygon.
	 */
	public CSG_Bounds getBounds(){
		return bounds.deepCopy();
	}
	
	/**
	 * get the vertex at the index mod the list size.
	 * @param i the index (can be negative up to the size of the list)
	 * @return CSG_Vertex at that index
	 */
	public CSG_Vertex getVertAtModIndex(int i){
		if(vertices.size() <= 0){
			System.out.println("CSG_Polygon(getVertAtIndex): vertices list has size Zero!");
			return null;
		}
		return vertices.get((i+vertices.size())%vertices.size());
	}
	
	public boolean indexIsSameModSize(int i, int j){
		if(vertices.size() <= 0){
			System.out.println("CSG_Polygon(indexIsSameModSize): vertices list has size Zero!");
			return false;
		}
		return ((i+vertices.size())%vertices.size()) == ((j+vertices.size())%vertices.size());
	}
	
	/**
	 * @return the CSG_Vertex defining this polygon's 
	 * barycenter (the average of all the vertices).
	 */
	public CSG_Vertex getBarycenterVertex(){
		int verts = vertices.size();
		double xSum = 0.0;
		double ySum = 0.0;
		double zSum = 0.0;
		for(CSG_Vertex v : vertices){
			xSum += v.getX();
			ySum += v.getY();
			zSum += v.getZ();
		}
		return new CSG_Vertex(xSum/(double)verts, ySum/(double)verts, zSum/(double)verts);
	}
	
	/**
	 * get the area defined by this convex polygon
	 * @return the area
	 */
	public double getArea(){
		if(vertices.size() < 3){
			// must be at least 3 vertices. this shouldn't happen.
			System.out.println("CSG_Polygon(getArea): Less than 3 vertices in polygon.. whaah?");
			return 0.0;
		}
		CSG_Vertex barycenter = getBarycenterVertex();
		// calculate area of each triangle (from barycenter to each edge)
		double area = 0.0;
		CSG_Vertex lastVert = vertices.getLast();
		for(CSG_Vertex vert : vertices){
			// using Heron's formula to calculate the area of each triangle
			double a = barycenter.getDistBetweenVertices(lastVert);
			double b = barycenter.getDistBetweenVertices(vert);
			double c = vert.getDistBetweenVertices(lastVert);
			double s = 0.5*(a+b+c); // the semiperimeter
			//System.out.println("abc: (" + a + "," + b + "," + c + ")");
			area += Math.sqrt(s*(s-a)*(s-b)*(s-c));
			lastVert = vert;
		}
		return area;
	}
	
	/**
	 * @return the number of vertices that define this CSG_Polygon.
	 */
	public int getNumberVertices(){
		return vertices.size();
	}
	
	/**
	 * @return the iterator over all of the Polygon's vertices
	 */
	public Iterator<CSG_Vertex> getVertexIterator(){
		return vertices.iterator();
	}
	
	public void markForDeletion(){
		markedForDeletion = true;
	}
	
	public boolean isMarkedForDeletion(){
		return markedForDeletion;
	}
	
	/**
	 * Get the plane defined by this planar convex polygon.
	 * @return the CSG_Plane, or NULL if polygon was malformed.
	 */
	public CSG_Plane getPlane(){
		return polygonPlane;
	}
	
	private CSG_Plane computePlane(){
		if(!(vertices.size() >= 3)){
			System.out.println("CSG_Polygon(getPlane): not enough vertices in polygon to constuct a plane!");
			return null;
		}
		CSG_Vertex v1 = vertices.get(0);
		CSG_Vertex v2 = vertices.get(1);
		CSG_Vertex v3 = vertices.get(2);
		
		//
		// compute the normal and offset(d) of the first 3 points.
		//
		// A = - [ y1 (z2 - z3) + y2 (z3 - z1) + y3 (z1 - z2) ]
		// B = - [ z1 (x2 - x3) + z2 (x3 - x1) + z3 (x1 - x2) ]
		// C = - [ x1 (y2 - y3) + x2 (y3 - y1) + x3 (y1 - y2) ]
		// D = x1 (y2 z3 - y3 z2) + x2 (y3 z1 - y1 z3) + x3 (y1 z2 - y2 z1)
		//
		// dist_from_plane = norm(.)vect + D
		double A = -(v1.getY()*(v2.getZ()-v3.getZ()) + v2.getY()*(v3.getZ()-v1.getZ()) + v3.getY()*(v1.getZ()-v2.getZ())); 
		double B = -(v1.getZ()*(v2.getX()-v3.getX()) + v2.getZ()*(v3.getX()-v1.getX()) + v3.getZ()*(v1.getX()-v2.getX()));
		double C = -(v1.getX()*(v2.getY()-v3.getY()) + v2.getX()*(v3.getY()-v1.getY()) + v3.getX()*(v1.getY()-v2.getY()));
		//double D =  ( v1.getX()*(v2.getY()*v3.getZ()-v3.getY()*v2.getZ())
		//				+ v2.getX()*(v3.getY()*v1.getZ()-v1.getY()*v3.getZ())
		//				+ v3.getX()*(v1.getY()*v2.getZ()-v2.getY()*v1.getZ()));
		//double unitDivider= 1.0;
		if((A < TOL && A > -TOL) && (B < TOL && B > -TOL) && (C < TOL && C > -TOL)){
			// vertices are collinear!!!
			System.out.println("CSG_Polygon(getPlane) Normal Was ZERO! Vertices must have been collinear!");
		}
		//else{
		//	unitDivider = Math.sqrt(A*A + B*B + C*C);
		//}
		
		CSG_Vertex normal = new CSG_Vertex(A, B, C).getUnitLength();
		double normOffset = -v1.getDotProduct(normal);
		return new CSG_Plane(normal, normOffset);
	}
	
	public String toString(){
		String vertString = "";
		for(CSG_Vertex v : vertices){
			vertString += v.toString();
		}
		return "CSG_Polygon:{" + vertString + "}";
	}
	
	/**
	 * get the ray intersection vertex with this polygon's plane.
	 * @return the CSG_Vertex where the ray intersects the plane, 
	 *   or NULL if ray is parallel to the plane
	 */
	public CSG_Vertex getRayIntersectionWithPlane(CSG_Ray ray){
		return polygonPlane.getRayIntersection(ray);
	}
	
	/**
	 * test to see if the given vertex is within this polygon.
	 * vertices that fall exactly on the edge are <em>not</em>
	 * considered to be <em>inside</em> the polygon.
	 * @param testVert the CSG_Vertex to test
	 * @return true if vertex is <em>inside</em> the polygon.
	 */
	public boolean vertexIsInsidePolygon(CSG_Vertex testVert){
		//return vertexIsInsideOrOnEdgeOfPolygon(testVert);
		
		CSG_Vertex lastVert = vertices.get(vertices.size()-1);
		for(CSG_Vertex vert : vertices){
			CSG_Vertex vertDiff = vert.subFromVertex(lastVert);
			CSG_Vertex normalInsidePoly = vertDiff.getVectCrossProduct(getPlane().getNormal());			
			// -- to visualize calculated normals for determining "inside" the polygon
			//	GL gl = GLContext.getCurrent().getGL();
			//	gl.glColor3f(0.0f, 0.0f, 1.0f);
			//	vert.drawPointForDebug(gl);
			//	gl.glColor3f(0.0f, 0.7f, 0.7f);
			//	normalInsidePoly.getScaledCopy(0.125).addToVertex(vert).drawPointForDebug(gl);			
			CSG_Plane testPlane = new CSG_Plane(normalInsidePoly, -normalInsidePoly.getDotProduct(vert));
			if(testPlane.distFromVertex(testVert) < TOL){
				return false;  // point is outside (or on top of) an edge!
			}
			lastVert = vert;
		}
		return true;
		 
	}
	
	/**
	 * test to see if the given vertex is within this polygon.
	 * vertices that fall on the edge are also considered to 
	 * be inside.
	 * @param testVert the CSG_Vertex to test
	 * @return true if vertex is <em>inside or on the edge of</em> the polygon.
	 */
	public boolean vertexIsInsideOrOnEdgeOfPolygon(CSG_Vertex testVert){
		CSG_Vertex lastVert = vertices.get(vertices.size()-1);
		for(CSG_Vertex vert : vertices){
			CSG_Vertex vertDiff = vert.subFromVertex(lastVert);
			CSG_Vertex normalInsidePoly = vertDiff.getVectCrossProduct(getPlane().getNormal());			
			// -- to visualize calculated normals for determining "inside" the polygon
			//	GL gl = GLContext.getCurrent().getGL();
			//	gl.glColor3f(0.0f, 0.0f, 1.0f);
			//	vert.drawPointForDebug(gl);
			//	gl.glColor3f(0.0f, 0.7f, 0.7f);
			//	normalInsidePoly.getScaledCopy(0.125).addToVertex(vert).drawPointForDebug(gl);			
			CSG_Plane testPlane = new CSG_Plane(normalInsidePoly, -normalInsidePoly.getDotProduct(vert));
			if(testPlane.distFromVertex(testVert) < -TOL){
				return false;  // point is outside (or on top of) an edge!
			}
			lastVert = vert;
		}
		return true; 
	}
	
	public void glDrawPolygon(GL gl){
		// TODO: put this in a GL lib of somekind..
		gl.glBegin(GL.GL_POLYGON);
		for(CSG_Vertex v : vertices){
			gl.glVertex3dv(v.getXYZ(), 0);
		}
		gl.glEnd();
	}
	
	public void drawPolygonForDebug(GL gl){
		gl.glColor3f(0.5f, 0.7f, 0.7f);
		gl.glBegin(GL.GL_POLYGON);
		for(CSG_Vertex v : vertices){
			gl.glVertex3dv(v.getXYZ(), 0);
		}
		gl.glEnd();
	}
	
	public void drawPolygonNormalsForDebug(GL gl){
		gl.glColor3d(1.0, 0.0, 0.0);
		CSG_Vertex fCenter = getBarycenterVertex();
		CSG_Vertex norm = getPlane().getNormal();
		double scale = 0.10;
		fCenter.addToVertex(norm.getScaledCopy(scale));
		CSG_Vertex nShifted = fCenter.addToVertex(norm.getScaledCopy(scale));
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3dv(fCenter.getXYZ(), 0);
			gl.glVertex3dv(nShifted.getXYZ(), 0);
		gl.glEnd();
	}
	
	public CSG_Polygon deepCopy(){
		CSG_Polygon clone = new CSG_Polygon(vertices.get(0).deepCopy(), vertices.get(1).deepCopy(), vertices.get(2).deepCopy());
		for(int i=3; i<vertices.size(); i++){
			clone.vertices.add(vertices.get(i).deepCopy());
		}
		clone.type = type;
		return clone;
	}
	
	public void reverseVertexOrder(){
		LinkedList<CSG_Vertex> newList = new LinkedList<CSG_Vertex>();
		for(int i=vertices.size()-1; i>=0; i--){
			newList.add(vertices.get(i));
		}
		vertices = newList;
		polygonPlane = computePlane();
	}
	
	/**
	 * check to make sure polygon is valid: (coplanar, convex, noncollinear)
	 * @return true if polygon is valid
	 */
	public boolean isValidPolygon(){
		// TODO: check to make sure vertices are coplanar, convex, and non-collinear
		return true;
	}
	
	public CSG_Polygon getTranslatedCopy(CSG_Vertex translation){		
		CSG_Polygon tPoly = this.deepCopy();
		for(int i=0; i < tPoly.vertices.size(); i++){
			tPoly.vertices.set(i, tPoly.vertices.get(i).addToVertex(translation));
		}
		return tPoly;
	}
	
}
