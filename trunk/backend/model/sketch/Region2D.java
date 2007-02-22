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

	public Prim2DCycle prim2DCycle = new Prim2DCycle();
	
	public boolean isSelected = false;
	
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
	
}
