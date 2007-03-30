package backend.model.sketch;

import java.util.Iterator;
import java.util.LinkedList;

import javax.media.opengl.GL;

import backend.adt.Point2D;
import backend.geometry.Geometry2D;
import backend.model.CSG.CSG_Face;
import backend.model.CSG.CSG_Polygon;
import backend.model.CSG.CSG_Vertex;


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
* @created Feb. 2007
*/
public class Region2D implements Comparable{

	private Prim2DCycle prim2DCycle = new Prim2DCycle();
	private CSG_Face csgFace = null;
	
	public Region2D(Prim2DCycle cycle){
		if(cycle != null && cycle.isValidCycle()){
			this.prim2DCycle = cycle;
			this.csgFace = createCSG_Face();
		}else{
			System.out.println("Region2D(Constructor): Tried to construct a Region2D with an invalid cycle!");
			
		}		
	}
	
	public double getRegionArea(){
		if(csgFace == null){
			return Double.MAX_VALUE;
		}
		return csgFace.getArea();
	}
	
	public boolean regionContainsPoint2D(Point2D pt){
		if(csgFace != null){
			return csgFace.vertexIsInsideFace(new CSG_Vertex(pt, 0.0));
		}else{
			return false;
		}
	}

	
	public int compareTo(Object o) {
		if(o instanceof Region2D){
			Region2D regionB = (Region2D)o;
			if(regionB.getRegionArea() > this.getRegionArea()){
				return -1;
			}
			if(regionB.getRegionArea() < this.getRegionArea()){
				return 1;
			}
		}
		return 0;
	}
	
	/**
	 * generate and return a list of triangles that can be 
	 * used to fill the region when drawing or used otherwise
	 * to compute the total area of the region.
	 * @return a list of verticies (3*n) specifying triangles that comprise the region2D.
	 */
	public Point2DList getPoint2DListTriangles(){
		// TODO: cache this, perhaps?
		Point2DList p2DList = getPoint2DListEdges();
		// TODO: HACK, just for triangular regions...
		if(prim2DCycle.size() == 3){
			Point2D ptA = prim2DCycle.get(0).ptA;
			Point2D ptB = prim2DCycle.get(0).ptB;
			Point2D ptC = prim2DCycle.get(1).hasPtGetOther(ptB);
			if(prim2DCycle.isCCW()){
				p2DList.add(new Point2D(ptA.getX(), ptA.getY()));
				p2DList.add(new Point2D(ptB.getX(), ptB.getY()));
				p2DList.add(new Point2D(ptC.getX(), ptC.getY()));
			}else{
				p2DList.add(new Point2D(ptC.getX(), ptC.getY()));				
				p2DList.add(new Point2D(ptB.getX(), ptB.getY()));
				p2DList.add(new Point2D(ptA.getX(), ptA.getY()));
			}	
		}
		return p2DList;
	}
	
	/**
	 * generate and return a list of point2D pairs that specify
	 * the region's outline.
	 * @return a point2DList of the regions defining points, in pairs.
	 */
	public Point2DList getPoint2DListEdges(){
		// TODO: HACK, doesn't take into account faces that may be on the inside of an object (drilled hole)
		Point2DList p2DList = new Point2DList();
		Point2D conPt = new Point2D(0.0, 0.0);
		if(prim2DCycle.size() > 0){
			conPt = prim2DCycle.getFirst().ptA;
		}
		for(Prim2D prim : prim2DCycle){
			p2DList.add(conPt);
			conPt = prim.hasPtGetOther(conPt);
			p2DList.add(conPt);
		}		
		return p2DList;
	}
	
	/**
	 * generate and return a list of point2D quads that specify
	 * the region's outline. (a,b,b,a) for each line segment.
	 * @return a point2DList of the regions defining points, in quads.
	 */
	public Point2DList getPoint2DListEdgeQuad(){
		// TODO: HACK, doesn't take into account faces that may be on the inside of an object (drilled hole)
		Point2DList p2DList = getPoint2DListEdges();
		Point2D conPt = new Point2D(0.0, 0.0);
		if(prim2DCycle.size() > 0){
			conPt = prim2DCycle.getFirst().ptA;
		}
		for(Prim2D prim : prim2DCycle){
			Point2D lastPt = conPt;
			p2DList.add(conPt);
			conPt = prim.hasPtGetOther(conPt);
			p2DList.add(conPt);
			p2DList.add(conPt);
			p2DList.add(lastPt);
		}		
		return p2DList;
	}
	
	public CSG_Face getCSG_Face(){
		return csgFace;
	}
	
	public Point2DList getPeremeterPointList(){
		Point2DList pointList = new Point2DList();
		prim2DCycle.orientCycle();
		for(Prim2D prim : prim2DCycle){
			pointList.addAll(prim.getVertexList(25));
			pointList.removeLast(); // since it overlaps with the next element	
		}
		return pointList;
	}
	
	private CSG_Face createCSG_Face(){
		CSG_Face face = null;
		
		// create a list of 2D points.
		LinkedList<Point2D> pointList = getPeremeterPointList();
		
		//System.out.println("face size: " + pointList.size());
		
		if(pointList.size() < 3){
			System.out.println("Region2D(getCSG_Face): Invalid cycle.. Not enough points in list!");
			return null;
		}
		
		//pointList.removeLast(); // last point is a repeat of the first.
		
		//
		// pseudo-code for "convexize polygon" method
		// 
		// while(at least 3 points to consider and index < numberOfPoints)
		//   ptA,ptB,ptC starting at index%numberOfPoints
		//   if(angle{ABC} > 0)
		//     construct potential polygon(A,B,C)
		//     if(poly doesn't overlap/contain any other point)
		//       for(every remaining pointD)
		//         if(angle{poly.LastLastPt, poly.LastPt, pointD} > 0 && 
		//            angle{poly.LastPt, pointD, poly.firstPoint} > 0 &&
		//            angle{pointD, poly.firstPoint, poly.secPoint} > 0)
		//           if(poly doesn't contains any other point)
		//             poly.add(ptD);
		//           else
		//             break;
		//         else
		//           break;
		//       face.addPoly(poly);
		//       remove all of poly's middle points from pointList
		//       index = 0;
		//     else
		//       index++
		//   else
		//     index++;
		//   
		
		
		double TOL = 1e-10;
		LinkedList<Point2D> polyPoints = new LinkedList<Point2D>();
		int index = 0;
		while(pointList.size() >= 3 && index < pointList.size()){
			polyPoints.clear();
			
			int listSize = pointList.size();
			Point2D ptA = pointList.get(index%listSize);
			Point2D ptB = pointList.get((index+1)%listSize);
			Point2D ptC = pointList.get((index+2)%listSize);
			polyPoints.add(ptA);
			polyPoints.add(ptB);
			polyPoints.add(ptC);
			
			double angle = Geometry2D.threePtAngle(ptA, ptB, ptC);
			if(angle > TOL){ // points going clockwise				
				CSG_Polygon poly = new CSG_Polygon(new CSG_Vertex(ptA, 0.0), new CSG_Vertex(ptB, 0.0), new CSG_Vertex(ptC, 0.0));
				if(!polygonContainPoints(poly, pointList, polyPoints)){
					int indexStart = index;
					for(int i=index; i-indexStart < (listSize-3); i++){
						Point2D ptD = pointList.get((i+3)%listSize);
						CSG_Vertex vertLastLast = poly.getVertAtModIndex(poly.getNumberVertices()-2);
						CSG_Vertex vertLast = poly.getVertAtModIndex(poly.getNumberVertices()-1);
						Point2D ptLastLast = new Point2D(vertLastLast.getX(), vertLastLast.getY());
						Point2D ptLast = new Point2D(vertLast.getX(), vertLast.getY());
						double angleA = Geometry2D.threePtAngle(ptLastLast, ptLast, ptD);
						double angleB = Geometry2D.threePtAngle(ptLast, ptD, ptA);
						double angleC = Geometry2D.threePtAngle(ptD, ptA, ptB);
						if(angleA > TOL && angleB > TOL && angleC > TOL){			
							CSG_Polygon polyCopy = poly.deepCopy();
							polyCopy.addVertex(new CSG_Vertex(ptD, 0.0));
							polyPoints.add(ptD);
							if(!polygonContainPoints(polyCopy, pointList, polyPoints)){
								poly.addVertex(new CSG_Vertex(ptD, 0.0));
							}else{
								polyPoints.removeLast();
								// Polygon contained another vertex in the list!
								break;
							}							
						}else{
							// an angle was negative (non-convex)
							break;
						}
					}
					//System.out.println("adding polygon: " + poly);
					if(face == null){
						face = new CSG_Face(poly);
						LinkedList<CSG_Vertex> perimVerts = new LinkedList<CSG_Vertex>();
						for(Point2D point : pointList){
							perimVerts.add(new CSG_Vertex(point, 0.0));
						}
					}else{
						face.addPolygon(poly);
					}
					//  remove all polygon's midpoints from the pointList
					polyPoints.removeLast();
					polyPoints.removeFirst();
					for(Point2D p : polyPoints){
						pointList.remove(p);
					}
					index = 0;
				}else{
					index++;
				}
			}else{
				index++;
			}			
		} // end while loop
		
		return face;
	}
	
	private boolean polygonContainPoints(CSG_Polygon poly, LinkedList<Point2D> pointList, LinkedList<Point2D> invalidPoints){
		boolean polyOverlapsOtherPoints = false;
		for(Point2D point : pointList){
			if(!invalidPoints.contains(point) && poly.vertexIsInsideOrOnEdgeOfPolygon(new CSG_Vertex(point, 0.0))){
				polyOverlapsOtherPoints = true;
			}
		}
		return polyOverlapsOtherPoints;
	}

	public void glDrawUnselected(GL gl){
		// TODO: put this in a GL lib of somekind..
		gl.glColor4d(0.5, 0.75, 0.5, 0.25);
		if(csgFace != null){
			Iterator<CSG_Polygon> polyIter = csgFace.getPolygonIterator();
			while(polyIter.hasNext()){
				CSG_Polygon poly = polyIter.next();
				poly.glDrawPolygon(gl);
			}
		}
	}
	
	public void glDrawSelected(GL gl){
//		 TODO: put this in a GL lib of somekind..
		gl.glColor4d(0.7, 0.7, 0.9, 1.0);
	}
	
}
