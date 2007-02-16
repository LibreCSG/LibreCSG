package backend.primatives;

import javax.media.opengl.GL;

import ui.opengl.GLDynPrim;
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
public class Prim2DLine implements Prim2D{

	Point2D ptA;
	Point2D ptB;
	
	public Prim2DLine(Point2D ptA, Point2D ptB){
		if(ptA == null || ptB == null){
			ptA = new Point2D(0.0, 0.0);
			ptB = new Point2D(0.0, 0.0);
			System.out.println("prim2DLine constructed with a null end point!");
		}
		this.ptA = ptA;
		this.ptB = ptB;
	}
	
	public void glDraw(GL gl) {
		GLDynPrim.line2D(gl, ptA, ptB, 0.0);		
	}

	public Point2D intersectsArc(Prim2DArc arc) {
		// TODO need Intersection code!
		return null;
	}

	public Point2D intersectsLine(Prim2DLine ln) {
		Point2D ptC = ln.ptA;
		Point2D ptD = ln.ptB;
		double denom = 	(ptD.getX()-ptC.getX())*(ptB.getY()-ptA.getY()) - 
						(ptD.getY()-ptC.getY())*(ptB.getX()-ptA.getX());

		double numA  =  (ptA.getX()-ptC.getX())*(ptD.getY()-ptC.getY())-
						(ptD.getX()-ptC.getX())*(ptA.getY()-ptC.getY());
		
		double numB  =  (ptC.getY()-ptA.getY())*(ptB.getX()-ptA.getX()) - 
						(ptC.getX()-ptA.getX())*(ptB.getY()-ptA.getY());
		
		if(denom == 0.0){
			// handle special case when lines are parallel...
			if(numA == 0.0 && numB == 0.0){
				// Parallel lines are coincident!
				if(!ptC.equalsPt(ptA) && !ptD.equalsPt(ptA)){
					// test to see if A intersect segment CD
					if(ln.distFromPrim(ptA) == 0.0){
						// ptA was on parallel line segment
						return ptA;
					}					
				}
				if(!ptC.equalsPt(ptB) && !ptD.equalsPt(ptB)){
					// test to see if B intersects segment CD
					if(ln.distFromPrim(ptB) == 0.0){
						// ptB was on parallel line segment
						return ptB;
					}
				}
			}
			
		}
		else{
			double uA = numA / denom;
			double uB = numB / denom;
			if(uA >= (0.0-Geometry2D.epsilon) && uA <= (1.0+Geometry2D.epsilon) && uB > 0.0 && uB < 1.0){				
				double iX = ptA.getX()+uA*(ptB.getX()-ptA.getX());
				double iY = ptA.getY()+uA*(ptB.getY()-ptA.getY());
				Point2D iPoint = new Point2D(iX, iY);
				System.out.println("INTERSECT: Lines segments intersect!! -- " + iPoint);
				// TODO: return point of intersection...
			}else{
				System.out.println("INTERSECT: Lines DO NOT intersect... uA,uB:" + uA + "," + uB);
			}
		}
		return null;
	}

	public double distFromPrim(Point2D pt) {
		return Geometry2D.distFromLineSeg(ptA, ptB, pt);
	}

	public Point2D intersect(Prim2D anyPrim2D) {
		if(anyPrim2D instanceof Prim2DLine){
			return this.intersectsLine((Prim2DLine)anyPrim2D);
		}
		if(anyPrim2D instanceof Prim2DArc){
			return this.intersectsArc((Prim2DArc)anyPrim2D);
		}
		System.out.println("Prim2D was of unsupported type!!");
		return null;
	}
	
}
