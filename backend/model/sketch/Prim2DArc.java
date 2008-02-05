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
public class Prim2DArc extends Prim2D{

	//TODO: remember to update ptA and ptB -- the arc's end points.
	
	protected Point2D center;
	protected double  radius;
	protected double  startAngle;
	protected double  arcAngle;
	
	private boolean reversed = false;
	
	/**
	 * Construct a new Prim2DArc sketch primative that handles 
	 * intersections, drawing, and manipulation.
	 * @param center the center of the arc
	 * @param radius the radius of the arc (360.0 = full circle)
	 * @param startAngle in degrees
	 * @param arcAngle in degrees (must be <b>positive</b>)
	 */
	public Prim2DArc(Point2D center, double radius, double startAngle, double arcAngle){
		this.center = center;
		this.radius = radius;
		this.startAngle = (startAngle%360.0+360.0)%360.0; // keep it positive internally
		if(arcAngle <= 0.0){
			System.out.println("Prim2DArc(Constructor): arcAngle was invalid! must be positive! given:" + arcAngle);
			arcAngle = 0.0;
		}
		this.arcAngle   = arcAngle;
		// set ptA, and ptB based on center, radius, startAngle, and arcAngle
		this.ptA = getPtAtAngle(startAngle);
		this.ptB = getPtAtAngle(startAngle+arcAngle);
	}
	
	private Point2D getPtAtAngle(double angle){
		double x = center.getX() + Math.cos(Math.toRadians(angle))*radius;
		double y = center.getY() + Math.sin(Math.toRadians(angle))*radius;
		return new Point2D(x,y);
	}
	
	public Point2D getArcCenterPoint(){
		return this.center.deepCopy();
	}
	
	public double getArcRadius(){
		return this.radius;
	}
	
	public void glDraw(GL gl) {
		GLDynPrim.arc2D(gl, center, radius, startAngle, arcAngle, 0.0);
		GLDynPrim.point(gl, center.getX(), center.getY(), 0.0, 3.0);
	}

	public Point2D intersectsArc(Prim2DArc arc) {
		double dist = this.center.computeDist(arc.center);
		if(dist >= this.radius + arc.radius){
			// arcs are too far apart.. there is no way for them to intersect.
			return null;
		}
		
		double a = (this.radius*this.radius - arc.radius*arc.radius + dist*dist) / (2*dist);
		double h = Math.sqrt(this.radius*this.radius - a*a);
		Point2D mid = this.center.addPt((arc.center.subPt(this.center)).getScaledPt(a/dist));
		double pt1X = mid.getX() + (h/dist)*(arc.center.getY()-this.center.getY());
		double pt1Y = mid.getY() - (h/dist)*(arc.center.getX()-this.center.getX());
		Point2D pt1 = new Point2D(pt1X, pt1Y);
		double pt2X = mid.getX() - (h/dist)*(arc.center.getY()-this.center.getY());
		double pt2Y = mid.getY() + (h/dist)*(arc.center.getX()-this.center.getX());
		Point2D pt2 = new Point2D(pt2X, pt2Y);
		
		// two candidate points for intersection. 
		
		double distTo1 = arc.distFromPrim(pt1);
		if(distTo1 < Geometry2D.epsilon){
			// could be good, just make sure it's not an end point.
			if(!pt1.equalsPt(arc.ptA) && !pt1.equalsPt(arc.ptB)){
				return pt1;
			}
		}
		double distTo2 = arc.distFromPrim(pt2);
		if(distTo2 < Geometry2D.epsilon){
			// could be good, just make sure it's not an end point.
			if(!pt2.equalsPt(arc.ptA) && !pt2.equalsPt(arc.ptB)){
				return pt2;
			}
		}
		return null;
	}

	public Point2D intersectsLine(Prim2DLine ln) {		
		// Intersection code of line with arc	
		Point2D ptA = ln.ptA;
		Point2D ptB = ln.ptB;
		Point2D ptC = center;		
		double r = radius;
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
		if(sLine < Geometry2D.epsilon || sLine > (1.0-Geometry2D.epsilon)){
			// sLine1 not within (0,1) range, try sLine2
			sLine = sLine2;
			if(sLine < Geometry2D.epsilon || sLine > (1.0-Geometry2D.epsilon)){
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
		
		double posAngleDiff = (sCircAngle - startAngle + 360.0)%360.0;
		
		// the epsilons "shorten" the acceptable window for the primative that will be modified
		// and the epsilons "widen" the window for "this" primative to ensure checking if very close.
		if(posAngleDiff <= -Geometry2D.epsilon || posAngleDiff >= (arcAngle + Geometry2D.epsilon)){
			//System.out.println("close, but not an intersectoin!  :)");
			return null;
		}
				
		// intersection with line segment.. (same as arc)
		double x = (1.0-sLine)*ptA.getX() + sLine*ptB.getX();
		double y = (1.0-sLine)*ptA.getY() + sLine*ptB.getY();
		return new Point2D(x,y);
	}

	public double distFromPrim(Point2D pt) {
		double distFromCircle = Geometry2D.distFromCircle(center, radius, pt);
		double angleToPt = center.getCartesianAngle(pt);
		// if Arc covers the angle, then return the distance, 
		// else return closer of distances to end points
		double posAngleToPt = (angleToPt%360.0+360.0)%360.0; // gaurantee to be positive
		//double arcBeginAngle = (startAngle%360.0 + 360.0)%360.0;
		double posAngleDiff = (posAngleToPt - startAngle + 360.0)%360.0;
		if(posAngleDiff <= arcAngle){
			return distFromCircle;
		}
		// closest point is arc endpoint, return the closest one.
		return Math.min(ptA.computeDist(pt), ptB.computeDist(pt));
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
		// simple check to make sure the point is actually pretty close to the arc.
		double distFromArc = this.distFromPrim(pt);
		if(distFromArc > 0.001){
			System.out.println("Prim2DArc(splitPrimAtPoint): ** trying to split prim at a point that is not near the arc!! - aborting");
			System.out.println(" --> distance from arc was: " + distFromArc);
			return null;
		}
		//System.out.println("point was good.. trying arc split");
		double ptAngle = new Point2D(0.0, 0.0).getCartesianAngle(pt.subPt(center));
		double posAngleToPt = (ptAngle+360.0)%360.0;
		double arcEndAngle   = ((startAngle+arcAngle)%360.0 + 360.0)%360.0;
		double posAngleDiff = (posAngleToPt - startAngle + 360.0)%360.0;
		if((posAngleToPt < startAngle+Geometry2D.epsilon && posAngleToPt > startAngle-Geometry2D.epsilon) ||
				(posAngleToPt < arcEndAngle+Geometry2D.epsilon && posAngleToPt > arcEndAngle-Geometry2D.epsilon)){
			// would create an arc with angle zero.. don't split it.
			System.out.println("chose not to split arc.. this really shouldn't happen if the intersection code is working..");
			System.out.println(" --> posAngleToPt:" + posAngleToPt + ", startAngle:" + startAngle + ", arcAngle:" + arcAngle);
			return null;
		}
		//System.out.println(this);
		//System.out.println("ArcSplit: ptAngle:" + ptAngle + ", arcBeginAngle:"  + arcBeginAngle + ", posAngleDiff:" + posAngleDiff);
		
		if(arcAngle > 0.0){
			Prim2DArc arc1 = new Prim2DArc(center, radius, startAngle, posAngleDiff);
			Prim2DArc arc2 = new Prim2DArc(center, radius, posAngleToPt, arcAngle-posAngleDiff);
			//System.out.println("-> arc1: " + arc1);
			//System.out.println("-> arc2: " + arc2);
			return new PrimPair2D(arc1, arc2);
		}else{
			//double negAngleDiff = 360.0 - posAngleDiff;
			//Prim2DArc arc1 = new Prim2DArc(center, radius, startAngle, negAngleDiff);
			//Prim2DArc arc2 = new Prim2DArc(center, radius, ptAngle, arcAngle-negAngleDiff);
			//System.out.println("-> arc1: " + arc1);
			//System.out.println("-> arc2: " + arc2);
			//return new PrimPair2D(arc1, arc2);
			return null;
		}
	}

	@Override
	public double getPrimLength() {
		// calculate arc length
		return (arcAngle/180.0)*Math.PI*radius;
	}

	@Override
	public Prim2DArc getSwappedEndPtPrim2D() {
		Prim2DArc revArc = new Prim2DArc(center, radius, startAngle, arcAngle);
		revArc.reversed = true;
		Point2D tempA = revArc.ptA;
		revArc.ptA = revArc.ptB;
		revArc.ptB = tempA;
		return revArc;
	}

	@Override
	public Point2D getCenterPtAlongPrim() {
		return getPtAtAngle(startAngle + arcAngle/2.0);
	}
	
	public String toString(){
		return "Prim2DArc{center:" + center + ", radius:" + radius + ", startAngle:" + startAngle + ", arcAngle:" + arcAngle;
	}

	@Override
	public LinkedList<Point2D> getVertexList(int maxVerts) {
		LinkedList<Point2D> points = new LinkedList<Point2D>();
		maxVerts = Math.max(2, maxVerts);
		int totalPts = Math.min((int)arcAngle+2, maxVerts);
		double deltaAngle = arcAngle/(totalPts-1);
		if(!reversed){
			for(int i=0; i<totalPts; i++){
				points.add(getPtAtAngle(startAngle + (double)i*deltaAngle));
			}
		}else{
			for(int i=totalPts-1; i >= 0; i--){
				points.add(getPtAtAngle(startAngle + (double)i*deltaAngle));
			}
		}
		return points;
	}
}
