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
 * A CSG_Face is a special type of polygon that must:<br/>
 * 1. Be planar and Convex<br/>
 * 2. Have no 3 vertices that are collinear<br/>
 * 3. Have every vertex be unique<br/>
 * 4. Always be viewed as Clockwise <br/><br/>
 * 
 * Algorithms and structures from:<br/>
 * - Laidlaw, Trumbore, and Hughes <br/>
 * - "Constructive Solid Geometry for Polyhedral Objects"<br/>
 * - SIGGRAPH 1986, Volume 20, Number 4, pp.161-170
 */
public class CSG_Face {
	
	private List<CSG_Vertex> vertices = new LinkedList<CSG_Vertex>();
	private CSG_Bounds bounds;
	private CSG_Vertex normal;
	private double normOffset;
	
	private final double TOL = 1e-10; // double tollerance  
	
	/**
	 * create a new Face (convex planar polygon)<br/><br/>
	 * <b>Vertices should be added in Clockwise order looking from outside of face</b><br/>
	 * Faces must have at least 3 vertices, but they can have
	 * more, as long as they remain convex and planar.<br/>
	 * 
	 * @param v1 1st CSG_Vertex
	 * @param v2 2nd CSG_Vertex
	 * @param v3 3rd CSG_Vertex
	 * @param v4 4th CSG_Vertex
	 */
	public CSG_Face(CSG_Vertex v1, CSG_Vertex v2, CSG_Vertex v3, CSG_Vertex v4){
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);
		vertices.add(v4);
		bounds = new CSG_Bounds(v1);
		bounds.includeVertex(v2);
		bounds.includeVertex(v3);
		bounds.includeVertex(v4);
		computeNormal(v1, v2, v3);
	}
	
	/**
	 * create a new Face (convex planar polygon)<br/><br/>
	 * <b>Vertices should be added in Clockwise order looking from outside of face</b><br/>
	 * Faces must have at least 3 vertices, but they can have
	 * more, as long as they remain convex and planar.<br/>
	 * 
	 * @param v1 1st CSG_Vertex
	 * @param v2 2nd CSG_Vertex
	 * @param v3 3rd CSG_Vertex
	 */
	public CSG_Face(CSG_Vertex v1, CSG_Vertex v2, CSG_Vertex v3){
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);
		bounds = new CSG_Bounds(v1);
		bounds.includeVertex(v2);
		bounds.includeVertex(v3);		
		computeNormal(v1, v2, v3);
	}
	
	private void computeNormal(CSG_Vertex v1, CSG_Vertex v2, CSG_Vertex v3){
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
			System.out.println("CSG_Face :: Normal Was ZERO! Vertices must have been collinear!");
		}else{
			unitDivider = Math.sqrt(A*A + B*B + C*C);
		}
		
		normal = new CSG_Vertex(A/unitDivider, B/unitDivider, C/unitDivider);
		normOffset = D/unitDivider;
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
	 * @return a copy of the rectangular bounds of the Face.
	 */
	public CSG_Bounds getBounds(){
		return bounds.deepCopy();
	}
	
	/**
	 * @return a copy of the normal defined by the Face's plane.
	 */
	public CSG_Vertex getPlaneNormal(){
		return normal.deepCopy();
	}
	
	/**
	 * @return the offset of the plane the contains this Face.
	 */
	public double getPlaneOffset(){
		return normOffset;
	}
	
	/**
	 * @return the average of all the vertices.
	 */
	public CSG_Vertex getAverageVertex(){
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
	 * @return the iterator over all of the Face's vertices
	 */
	public Iterator<CSG_Vertex> getVertexIterator(){
		return vertices.iterator();
	}
	
	/**
	 * get the distance from a CSG_Vertex to the plane
	 * defined by this Face.
	 * @param v the CSG_Vertex to test
	 * @return distance from vertex to plane (true "zero" if within tollerance)
	 */
	public double distFromVertexToFacePlane(CSG_Vertex v){
		double dotProd = v.getX()*normal.getX() + v.getY()*normal.getY() + v.getZ()*normal.getZ();
		double dist = dotProd + normOffset;
		if(dist > -TOL && dist < TOL){
			return 0.0;
		}
		return dist;
	}
	
	/**
	 * @return the number of vertices that define this face.
	 */
	public int getNumberVertices(){
		return vertices.size();
	}
	
}
