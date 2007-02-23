package backend.model.sketch;

import backend.adt.Point2D;
import backend.geometry.Geometry2D;


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

	protected Prim2DCycle prim2DCycle = new Prim2DCycle();
	
	public double getRegionArea(){
		// TODO: Big HACK.. only considering 3-sided regions.
		if(prim2DCycle.size() == 3){
			// area of triangle = 0.5*base*height
			// base = prim2DCycle.get(0);
			Prim2D base = prim2DCycle.get(0);
			Point2D otherVert = prim2DCycle.get(1).hasPtGetOther(base.ptB);
			if(otherVert == null){
				System.out.println("error getting triangle area.. ??");
				return 0.0;
			}
			double height = Geometry2D.distFromLineSeg(base.ptA, base.ptB, otherVert);
			return 0.5*base.ptA.computeDist(base.ptB)*height;
		}
		return 0.0;
	}
	
	public boolean regionContainsPoint2D(Point2D pt){
		// TODO: Big HACK.. only considering 3-sided regions
		if(prim2DCycle.size() == 3){
			// consider barycentric coordinates to determine if point is inside triangle.
			Prim2D base = prim2DCycle.get(0);
			Point2D pt1 = base.ptA;
			Point2D pt2 = base.ptB;
			Point2D pt3 = prim2DCycle.get(1).hasPtGetOther(base.ptB);
			
			double A = pt1.getX()-pt3.getX();
			double B = pt2.getX()-pt3.getX();
			double C = pt3.getX()-pt.getX();
			double D = pt1.getY()-pt3.getY();
			double E = pt2.getY()-pt3.getY();
			double F = pt3.getY()-pt.getY();
			double G =  0.0; // pt1.getZ()-pt3.getZ(); // for 3D point
			double H =  0.0; // pt2.getZ()-pt3.getZ(); // for 3D point
			double I =  0.0; // pt3.getZ()-pt.getZ();  // for 3D point
			
			double lambda1 = (B*(F+I) - C*(E+H)) / (A*(E+H) - B*(D+G));
			double lambda2 = (A*(F+I) - C*(D+G)) / (B*(D+G) - A*(E+H));
			double lambda3 = 1.0 - lambda1 - lambda2;
			
			// pt is in Triangle ptA,ptB,ptC iff: lambda1 && lambda2 && lambda3 are all 0 < lambda < 1
			if(lambda1 > 0.0 && lambda1 < 1.0 && lambda2 > 0.0 && lambda2 < 1.0 && lambda3 > 0.0 && lambda3 < 1.0){
				return true;
			}
			
		}		
		return false;
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
		Point2DList p2DList = new Point2DList();
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
		Point2DList p2DList = new Point2DList();
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
	
}
