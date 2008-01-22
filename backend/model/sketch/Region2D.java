package backend.model.sketch;

import java.util.Iterator;
import java.util.LinkedList;

import javax.media.opengl.GL;

import backend.adt.Point2D;
import backend.geometry.Geometry2D;
import backend.model.CSG.CSG_Face;
import backend.model.CSG.CSG_Polygon;
import backend.model.CSG.CSG_Vertex;



//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the 
//GNU General Public License (GPL).

//This file is part of avoCADo.

//AvoCADo is free software; you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation; either version 2 of the License, or
//(at your option) any later version.

//AvoCADo is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

//You should have received a copy of the GNU General Public License
//along with AvoCADo; if not, write to the Free Software
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA


/*
 * @author  Adam Kumpf
 * @created Feb. 2007
 */
public class Region2D implements Comparable{

	private Prim2DCycle prim2DCycle = new Prim2DCycle();
	private CSG_Face csgFace = null;
	private LinkedList<Region2D> regionsToSubtract = new LinkedList<Region2D>();

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

	/**
	 * check to see if the given point is inside this Region.
	 * This is done by checking if the vertex is contained in
	 * any of the derived CSG_Face's convex polygons. vertices that 
	 * fall exactly on the edge are <em>not</em> considered to 
	 * be <em>inside</em> the Face.
	 * @param vert the CSG_Vertex to check
	 * @return true iff vertex is inside the face.
	 */
	public boolean regionContainsPoint2D(Point2D pt){
		if(!this.csgFace.vertexIsInsideFace(new CSG_Vertex(pt, 0.0))){
			return false;
		}
		// check if inside edges from cut operations.
		Iterator<Region2D> iterReg = regionsToSubtract.iterator();
		while(iterReg.hasNext()){
			// check each subtracted region
			Region2D subtractedRegion = iterReg.next();
			if(subtractedRegion.regionContainsPoint2D(pt)){
				//System.out.println("found a point in the subtrated region! therefore I cannot contain the region. :)");
				return false; // point was inside a subtracted region.
			}
		}	
		//System.out.println("got to end of for loop without any continues... pt=" + pt);
		return true; // point was inside face and not inside a cut region.. point is contained! :)
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

	public boolean regionHasBeenCut(Region2D cutRegion){
		for(Region2D subReg : regionsToSubtract){
			if(subReg.equals(cutRegion)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * tests to see if regionB is entirely within the 
	 * perimeter of this Region2D.  Edge points, in 
	 * this care, as still considered within.  There is
	 * no checking regarding subtractedRegions, so check
	 * for those separately if you care about that.
	 * @param regionB the region to test.
	 * @return true iff regionB is within this region.
	 */
	public boolean containsRegion(Region2D regionB){
		if(this.equals(regionB)){
			return true; // same region.
		}		
		// now do a rigorous check...
		LinkedList<Point2D> pointsToTest = regionB.prim2DCycle.getPointList();
		for(Point2D pt : pointsToTest){
			if(this.regionContainsPoint2D(pt)){
				continue; // point was inside one of the face's polygons.. :)
			}			
			// not within region, but could be the same as an edge point, so check all of those.
			// outside edges...
			boolean ok = false; // this is a hack that allows a "break" to push out more than one level... 
			Iterator<Point2D> iter = this.prim2DCycle.getPointList().iterator();
			while(iter.hasNext()){
				Point2D edgePoint = iter.next();
				if(edgePoint.equalsPt(pt)){
					ok = true;
					break; // go to next point, this one is okay. :)
				}
			}					
			if(ok){
				//System.out.println("caught last chance OK continue. :)");
				continue;
			}
			//System.out.println("got to end of for loop without any continues... pt=" + pt);
			return false; // check both inside polygons and edge vertices, but no luck.
		}
		return true;
	}

	/**
	 * test to see if this region and the region passed in share any common end points.
	 * @param regionB the region to compare this region with.
	 * @return true iff there is at least one end point in common.
	 */
	public boolean sharesAtLeastOneCommonPrimEndPoint(Region2D regionB){
		for(Prim2D primA : prim2DCycle){
			for(Prim2D primB : regionB.prim2DCycle){
				if(primA.ptA.equalsPt(primB.ptA) || primA.ptA.equalsPt(primB.ptB) ||
						primA.ptB.equalsPt(primB.ptA) || primA.ptB.equalsPt(primB.ptB)){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * test to see if this region has the same prim2D points
	 * as the given region.  This can be used to quickly find
	 * duplicate regions (perhaps with just reversed or offset
	 * prim2D).
	 * @param regionB the region to compare to.
	 * @return true iff this region has the same defining points as the given region.
	 */
	public boolean hasSamePointsAsRegion(Region2D regionB){
		if(this.prim2DCycle.size() != regionB.prim2DCycle.size()){
			return false; // cycles are of different size.
		}
		for(Prim2D prim : prim2DCycle){
			if(	!regionB.prim2DCycle.containsPt(prim.ptA) ||
					!regionB.prim2DCycle.containsPt(prim.ptB) ||
					!regionB.prim2DCycle.containsPt(prim.getCenterPtAlongPrim())){
				return false; // one of the points could not be found. the cycles are different.
			}
		}
		return true; // every end and mid point is contained.
	}

	/**
	 * alter this region to have a hole cut in it that is
	 * defined by the given region.
	 * @param regionB the region2D to cut from this region.
	 */
	public void cutRegionFromRegion(Region2D regionB){
		if(regionB != null){
			regionsToSubtract.add(new Region2D(regionB.prim2DCycle));
			this.csgFace = createCSG_Face();
		}else{
			System.out.println(" ### cutRegionFromRegion was passed a null region (not doing anything). ");
		}
	}

	/** 
	 * find the closest distance from the vertices of this region2D
	 * to any edge/vertex of the given region.
	 * @param regionB the region to find the distance to.
	 * @return the closest distance.
	 */
	public double distanceFromVerticiesToRegion(Region2D regionB){
		double distance = Double.MAX_VALUE;
		if(regionB == null){
			System.out.println("** BAD NEWS! Null region passed to 'distanceFromVerticiesToRegion' **");
		}
		for(Point2D pt : this.prim2DCycle.getPointList()){
			double d = regionB.prim2DCycle.getClosestDistanceToPoint(pt);
			if(d < distance){
				distance = d;
			}
		}
		return distance;
	}


	private CSG_Face createCSG_Face(){
		CSG_Face face = null;

		// create a list of 2D points.
		LinkedList<Point2D> pointList = getPeremeterPointList();

		//System.out.println("face size: " + pointList.size());

		if(pointList.size() < 3){
			System.out.println("Region2D(getCSG_Face): Invalid cycle.. Not enough points in list!  size=" + pointList.size());
			System.out.println("point0=" + pointList.get(0) + ", point1=" + pointList.get(1));
			return null;
		}

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
		//
		//

		// CHECK FOR Duplicate adjacent points and remove them if they exist!!
		Point2D lastLastPt = pointList.get(0);
		Point2D lastPt = pointList.get(1);
		int sub = 0;
		for(int i=2; (i-sub)<pointList.size(); i++){
			Point2D nextPt = pointList.get(i-sub);
			if(lastPt.equalsPt(nextPt)){
				System.out.println("DUPLIACTE! {HACK!!}  -- i=" + i + ", " + nextPt);
				pointList.remove(i-sub);
				sub++;
				lastPt = pointList.get(i-sub-1);
				nextPt = pointList.get(i-sub);
			}else{
				if(lastLastPt.equalsPt(nextPt)){
					System.out.println("ZERO REGION! {HACK!!}  -- i=" + i + ", " + nextPt);
					pointList.remove(i-sub);
					pointList.remove(i-sub-1);
					sub++;
					sub++;
					lastPt = pointList.get(i-sub-1);
					nextPt = pointList.get(i-sub);
				}
			}
			lastLastPt = lastPt;
			lastPt = nextPt;
		}

		double TOL = 1e-10;
		LinkedList<Point2D> polyPoints = new LinkedList<Point2D>();
		int index = 0;
		while(pointList.size() >= 3 && index < pointList.size()){
			polyPoints.clear();
			//System.out.println("index: " + index);
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
				//	System.out.println("trying... Poly:" + poly + ", angle="  + angle);
				//	System.out.println("pointList.size() = " + pointList.size());
				if(!polygonContainsPoints(poly, pointList, polyPoints)){
					//		System.out.println("YES");
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
							if(!polygonContainsPoints(polyCopy, pointList, polyPoints)){
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
						//System.out.println("ok(1st): " + poly);
						face = new CSG_Face(poly);
						LinkedList<CSG_Vertex> perimVerts = new LinkedList<CSG_Vertex>();
						for(Point2D point : pointList){
							perimVerts.add(new CSG_Vertex(point, 0.0));
						}
					}else{
						//System.out.println("ok: " + poly);
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

	// TODO: use point2DList instead of a linkedList<Point2D>
	private boolean polygonContainsPoints(CSG_Polygon poly, LinkedList<Point2D> pointList, LinkedList<Point2D> invalidPoints){
		boolean polyOverlapsOtherPoints = false;
		for(Point2D point : pointList){
			boolean doNotTestPoint = false;
			for(Point2D ptInvalid : invalidPoints){
				if(point.equalsPt(ptInvalid)){
					doNotTestPoint = true;
					break;
				}
			}
			if(!doNotTestPoint && poly.vertexIsInsideOrOnEdgeOfPolygon(new CSG_Vertex(point, 0.0))){
				//System.out.println("Point was inside! " + point);
				polyOverlapsOtherPoints = true;
			}
		}
		return polyOverlapsOtherPoints;
	}

	public void glDrawUnselected(GL gl, boolean stencilCutRegions){
		// TODO: put this in a GL lib of somekind..
		gl.glColor4d(0.95, 0.5, 0.5, 0.5);
		//
		// -------------------------
		//		
		if(stencilCutRegions && regionsToSubtract.size() > 0){
			// stencil out "subtracted" regions

			//Disable color and depth buffers
			gl.glColorMask(false, false, false, false);             //Disable writing in color buffer
			gl.glDepthMask(false);                                  //Disable writing in depth buffer

			gl.glEnable(GL.GL_STENCIL_TEST);                        //Enable Stencil test
			gl.glClearStencil(0);
			gl.glClear(GL.GL_STENCIL_BUFFER_BIT);
			gl.glStencilFunc(GL.GL_ALWAYS, 1, 1);                   //Test always success, value written 1
			gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_REPLACE);  //Stencil & Depth test passes => replace existing value

			for(Region2D reg : regionsToSubtract){
				reg.glDrawUnselected(gl, false);
			}

			// turn back on everything.
			gl.glDepthMask(true);
			gl.glColorMask(true, true, true, true); //Enable drawing of r, g, b & a

			gl.glStencilFunc(GL.GL_NOTEQUAL, 1, 1);                //Draw only where stencil buffer is NOT 1
			gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);    //Stencil buffer read only
			gl.glColor4d(0.5, 0.95, 0.5, 0.5);
		}


		//
		// -------------------------
		//
		if(csgFace != null){
			Iterator<CSG_Polygon> polyIter = csgFace.getPolygonIterator();
			while(polyIter.hasNext()){
				CSG_Polygon poly = polyIter.next();
				poly.glDrawPolygon(gl);
			}
		}

		//
		//
		if(regionsToSubtract.size() > 0){
			gl.glDisable(GL.GL_STENCIL_TEST);                        //Disable Stencil test
		}
	}

	public void glDrawSelected(GL gl){
//		TODO: put this in a GL lib of somekind..
		gl.glColor4d(0.7, 0.7, 0.9, 1.0);
	}

	public String toString(){
		double subArea = 0.0;
		for(Region2D subReg : regionsToSubtract){
			subArea += subReg.getRegionArea();
		}
		return "Region2D: area=" + (getRegionArea()-subArea) + ", regions to cut: " + regionsToSubtract.size();
	}

}
