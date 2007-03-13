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
	
	private final double TOL = 1e-10; // double tollerance  
	
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
	
	public void addPolygon(CSG_Polygon polygon){
		polygons.add(polygon);
		bounds.includeBounds(polygon.getBounds());
	}
	
	public CSG_Bounds getBounds(){
		return bounds.deepCopy();
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
	
	public void removePolygon(CSG_Polygon poly){
		if(polygons.contains(poly)){
			polygons.remove(poly);
		}else{
			System.out.println("CSG_Face(removePolygon): Tried to remove a polygon that was not in the list of polygons!");
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
	
	/**
	 * get the distance from a CSG_Vertex to the plane
	 * defined by this Face.
	 * @param v the CSG_Vertex to test
	 * @return distance from vertex to plane (true "zero" if within tollerance)
	 */
	public double distFromVertexToFacePlane(CSG_Vertex v){
		double dotProd = v.getX()*facePlane.getNormal().getX() + v.getY()*facePlane.getNormal().getY() + v.getZ()*facePlane.getNormal().getZ();
		double dist = dotProd + facePlane.getOffset();
		if(dist > -TOL && dist < TOL){
			return 0.0;
		}
		return dist;
	}
	
}
