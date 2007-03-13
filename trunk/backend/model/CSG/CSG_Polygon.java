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

	private List<CSG_Vertex> vertices = new LinkedList<CSG_Vertex>();
	private CSG_Bounds bounds;
	
	private final double TOL = 1e-10; // double tollerance 

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
	}
	
	/**
	 * add a CSG_Vertex to the end of the vertex list
	 * @param newVertex the CSG_Vertex to be added.
	 */
	public void addVertex(CSG_Vertex newVertex){
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
	 * @return the number of vertices that define this CSG_Polygon.
	 */
	public int getNumberVertices(){
		return vertices.size();
	}
	
	public void markVertexInPolyByIndex(int i, CSG_Vertex.VERT_TYPE type){
		if(i < 0 || i >= vertices.size()){
			System.out.println("CSG_Polygon(markVertexInPolygonByIndex): Invalid index! i=" + i + " vertices=" + vertices.size());
			return;
		}
		vertices.get(i).setVertType(type);
	}
	
	/**
	 * @return the iterator over all of the Polygon's vertices
	 */
	public Iterator<CSG_Vertex> getVertexIterator(){
		return vertices.iterator();
	}
	
	/**
	 * Get the plane defined by this planar convex polygon.
	 * @return the CSG_Plane, or NULL if polygon was malformed.
	 */
	public CSG_Plane getPlane(){
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
		double D =  ( v1.getX()*(v2.getY()*v3.getZ()-v3.getY()*v2.getZ())
						+ v2.getX()*(v3.getY()*v1.getZ()-v1.getY()*v3.getZ())
						+ v3.getX()*(v1.getY()*v2.getZ()-v2.getY()*v1.getZ()));
		double unitDivider= 1.0;
		if((A < TOL && A > -TOL) && (B < TOL && B > -TOL) && (C < TOL && C > -TOL)){
			// vertices are collinear!!!
			System.out.println("CSG_Polygon(getPlane) Normal Was ZERO! Vertices must have been collinear!");
		}else{
			unitDivider = Math.sqrt(A*A + B*B + C*C);
		}
		
		CSG_Vertex normal = new CSG_Vertex(A/unitDivider, B/unitDivider, C/unitDivider);
		double normOffset = D/unitDivider;
		return new CSG_Plane(normal, normOffset);
	}
}
