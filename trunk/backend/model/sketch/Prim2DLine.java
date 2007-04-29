package backend.model.sketch;

import java.util.LinkedList;

import javax.media.opengl.GL;

import ui.opengl.GLDynPrim;
import backend.adt.Point2D;
import backend.geometry.Geometry2D;


//
//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the 
//GNU General Public License (GPL).
//
//This file is part of avoCADo.
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
public class Prim2DLine extends Prim2D{
	
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
		// Intersection code of arc with line		
		Point2D ptC = arc.center;		
		double r = arc.radius;
		double phiX   = ptB.getX() - ptA.getX();
		double phiY   = ptB.getY() - ptA.getY();
		double betaX  = ptA.getX() - ptC.getX();
		double betaY  = ptA.getY() - ptC.getY();
		double alphaX = ptA.getX()*ptA.getX() + ptC.getX()*ptC.getX() - 2.0*ptA.getX()*ptC.getX();
		double alphaY = ptA.getY()*ptA.getY() + ptC.getY()*ptC.getY() - 2.0*ptA.getY()*ptC.getY();
		
		double A = phiX*phiX + phiY*phiY;
		double B = 2.0*phiX*betaX + 2.0*phiY*betaY;
		double C = alphaX + alphaY - r*r;
				
		double sqrtNum = B*B - 4*A*C;
		if(sqrtNum < 0.0 || (A < Geometry2D.epsilon && A > -Geometry2D.epsilon)){
			return null;  // No intersection, roots are imaginary or infinite.
		}
		// quadratic formula...
		double sLine1 = (-B + Math.sqrt(sqrtNum))/(2*A);
		double sLine2 = (-B - Math.sqrt(sqrtNum))/(2*A);
		
		double sLine = sLine1;
		// the epsilons "shorten" the acceptable window for the primative that will be modified
		// and the epsilons "widen" the window for "this" primative to ensure checking if very close.
		if(sLine < -Geometry2D.epsilon || sLine > (1.0+Geometry2D.epsilon)){
			// sLine1 not within (0,1) range, try sLine2
			sLine = sLine2;
			if(sLine < -Geometry2D.epsilon || sLine > (1.0+Geometry2D.epsilon)){
				// sLine2 not within (0,1) range -- failed to get a good s for the line
				return null;
			}
		}
		
		// sLine now contains a (0,1) parameter along the line segment.		
		
		double acosNum = (sLine*phiX+ptA.getX()-ptC.getX()) / r;
		double asinNum = (sLine*phiY+ptA.getY()-ptC.getY()) / r;
		if(acosNum > 1.0 || acosNum < -1.0 || asinNum > 1.0 || asinNum < -1.0){
			System.out.println("Invalid arcsin,arccos numbers; asinNum:" + asinNum + ", acosNum:" + acosNum);
			return null;
		}
		double sCircCos = Math.toDegrees(Math.acos(acosNum));
		double sCircSin = Math.toDegrees(Math.asin(asinNum));
		double sCircAngle = sCircCos;
		if(sCircSin < 0.0){
			sCircAngle = 360.0 - sCircCos;
		}
		
		double posAngleDiff = (sCircAngle - arc.startAngle + 360.0)%360.0;
		
		// the epsilons "shorten" the acceptable window for the primative that will be modified
		// and the epsilons "widen" the window for "this" primative to ensure checking if very close.
		if(posAngleDiff <= Geometry2D.epsilon || posAngleDiff >= (arc.arcAngle - Geometry2D.epsilon)){
			//System.out.println("close, but not an intersectoin!  :)");
			return null;
		}
				
		// intersection with line segment.. (same as arc)
		double x = (1.0-sLine)*ptA.getX() + sLine*ptB.getX();
		double y = (1.0-sLine)*ptA.getY() + sLine*ptB.getY();
		return new Point2D(x,y);
	
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
		
		
		// check to see if end point lies on the line
		if(!ptC.equalsPt(ptA) && !ptD.equalsPt(ptA)){
			// test to see if A intersect segment CD
			if(ln.distFromPrim(ptA) == 0.0){
				//System.out.println("ptA intersected line CD");
				return ptA;
			}					
		}
		if(!ptC.equalsPt(ptB) && !ptD.equalsPt(ptB)){
			// test to see if B intersects segment CD
			if(ln.distFromPrim(ptB) == 0.0){
				//System.out.println("ptB ntersected line CD");
				return ptB;
			}
		}

		if(denom == 0.0){
			// lines are parallel...
			// the only intersection worth noting is if the
			// end point lies on the line, and that is already
			// checked for.  these lines
			if(numA == 0.0 && numB == 0.0){
				// Parallel lines are coincident!
				// but still, nothing to be done...				
			}			
		}
		else{
			double uA = numA / denom;
			double uB = numB / denom;
			if(uA > (0.0+Geometry2D.epsilon) && uA < (1.0-Geometry2D.epsilon) && uB > (0.0+Geometry2D.epsilon) && uB < (1.0-Geometry2D.epsilon)){				
				double iX = ptC.getX()+uB*(ptD.getX()-ptC.getX());
				double iY = ptC.getY()+uB*(ptD.getY()-ptC.getY());
				Point2D iPoint = new Point2D(iX, iY);
				//System.out.println("INTERSECT: Lines segments intersect!! -- " + iPoint + " uA,uB:" + uA + "," + uB);
				//  return point of intersection...
				return iPoint;
			}else{
				//System.out.println("INTERSECT: Lines DO NOT intersect... uA,uB:" + uA + "," + uB);
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

	public PrimPair2D splitPrimAtPoint(Point2D pt) {	
		//System.out.println("splitting line...");
		return new PrimPair2D(new Prim2DLine(ptA, pt), new Prim2DLine(pt, ptB));
	}

	public double getPrimLength() {
		return ptA.computeDist(ptB);
	}

	@Override
	public Prim2DLine getSwappedEndPtPrim2D() {
		return new Prim2DLine(ptB, ptA);
	}

	@Override
	public Point2D getCenterPtAlongPrim() {
		return ptA.addPt(ptB).getScaledPt(0.5);
	}

	@Override
	public LinkedList<Point2D> getVertexList(int maxVerts) {
		LinkedList<Point2D> points = new LinkedList<Point2D>();
		points.add(ptA);
		points.add(ptB);
		return points;
	}

	
}
