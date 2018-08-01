package org.poikilos.librecsg.backend.model.sketch;

import java.util.LinkedList;

import org.poikilos.librecsg.backend.adt.Point2D;
import org.poikilos.librecsg.backend.geometry.Geometry2D;


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

/**
 * cycle of Prim2D that only intersect at their endpoints.
 */
public class Prim2DCycle extends Prim2DList implements Comparable{

	private static final long serialVersionUID = 10013L;

	private double cycleArea = 0;

	public double getCycleLength(){
		double length = 0.0;
		for(Prim2D prim : this){
			length += prim.getPrimLength();
		}
		return length;
	}

	public double getCycleArea(){
		return cycleArea;
	}

	public void setCycleArea(double cycleArea){
		this.cycleArea = cycleArea;
	}

	public int compareTo(Object o) {
		if(o instanceof Prim2DCycle){
			Prim2DCycle primB = (Prim2DCycle)o;
			if(primB.getCycleArea() > this.getCycleArea()){
				return -1;
			}
			if(primB.getCycleArea() < this.getCycleArea()){
				return 1;
			}
		}
		return 0;
	}

	/**
	 * test to see if this prim2DCycle contains the specified point at
	 * one of its prim2D ends or centerpoint.
	 * @param pt the given point to test.
	 * @return true iff the given point exists at one of the endpoints
	 * or the centerpoint of any prim2D that this cycle contains.
	 */
	public boolean containsPt(Point2D pt){
		for(Prim2D prim : this){
			if(prim.ptA.equalsPt(pt) || prim.ptB.equalsPt(pt) || prim.getCenterPtAlongPrim().equalsPt(pt)){
				return true;
			}
		}
		return false;
	}

	/**
	 * traverse the cycle and see if the sum of
	 * the angles adds up to +-180 degrees to
	 * determine if cycle is counter-clockwise.
	 * @return true if counter-clockwise (CCW)
	 */
	public boolean isCCW(){
		// TODO: should take into acount initial angle (e.g., arcs) not just ptA to ptB.
		// take into account center point of prim to account for curves.
		// this can be done without more than one additional point because
		// this method specifies that Prim2D in the cycle may not intersect
		// at any place but their end points.
		double angle = 0.0;
		LinkedList<Point2D> pointList = new LinkedList<Point2D>();
		Point2D conPt = this.getFirst().ptA;
		for(Prim2D prim : this){
			pointList.add(conPt);
			pointList.add(prim.getCenterPtAlongPrim());
			conPt = prim.hasPtGetOther(conPt);
		}
		pointList.add(this.getFirst().ptA);
		pointList.add(this.getFirst().getCenterPtAlongPrim());
		for(int i=0; i<pointList.size()-2; i++){
			angle += Geometry2D.threePtAngle(pointList.get(i), pointList.get(i+1), pointList.get(i+2));
		}
		//System.out.println("Angle for complete cycle is: " + angle);
		return (angle < 0.0); // it should be ideally +180.0 or -180.0
	}

	/**
	 * check to see if the cycle can be traced without
	 * conflicting with the consumedDirection indicators.
	 * @return true if cycle can be completely traced without a consume conflict.
	 */
	public boolean cycleDirectionIsUnconsumed(){
		boolean cycleIsGood = true;
		if(isCCW()){
			// cycle is Counter-Clockwise (when first prim2D A-->B)
			Point2D conPt = this.getFirst().ptA;
			for(Prim2D prim : this){
				if(conPt.equalsPt(prim.ptA)){
					// going B-->A
					if(prim.consumedBA){
						cycleIsGood = false;
					}
				}else{
					// going A-->B
					if(prim.consumedAB){
						cycleIsGood = false;
					}
				}
				conPt = prim.hasPtGetOther(conPt);
			}
		}else{
			// cycle is Clockwise (when first prim2D A-->B)
			// go in opposite direction!
			Point2D conPt = this.getFirst().ptA;
			for(Prim2D prim : this){
				if(conPt.equalsPt(prim.ptA)){
					// going B-->A, but mark opposite!
					if(prim.consumedAB){
						cycleIsGood = false;
					}
				}else{
					// going A-->B, but mark opposite!
					if(prim.consumedBA){
						cycleIsGood = false;
					}
				}
				conPt = prim.hasPtGetOther(conPt);
			}
		}
		return cycleIsGood;
	}

	/**
	 * consume the cycle in the direction it must be
	 * traced to determine conflicts with other cycles.
	 */
	public void consumeCycleDirection(){
		if(isCCW()){
			// cycle is Counter-Clockwise (when first prim2D A-->B)
			Point2D conPt = this.getFirst().ptA;
			for(Prim2D prim : this){
				if(conPt.equalsPt(prim.ptA)){
					// going B-->A
					prim.consumedBA = true;
				}else{
					// going A-->B
					prim.consumedAB = true;
				}
				conPt = prim.hasPtGetOther(conPt);
			}
		}else{
			// cycle is Clockwise (when first prim2D A-->B)
			// go in opposite direction!
			Point2D conPt = this.getFirst().ptA;
			for(Prim2D prim : this){
				if(conPt.equalsPt(prim.ptA)){
					// going B-->A, but mark opposite!
					prim.consumedAB = true;
				}else{
					// going A-->B, but mark opposite!
					prim.consumedBA = true;
				}
				conPt = prim.hasPtGetOther(conPt);
			}
		}
	}

	/**
	 * reverse the order of primitaves in the cycle,
	 * thus changing a CCW ordered cycle to a CW cycle.
	 * (or vice versa).
	 */
	public void reverseCycleOrder(){
		// reverse cycle order
		Prim2DCycle newCycle = new Prim2DCycle();
		newCycle.add(this.getFirst().getSwappedEndPtPrim2D());
		this.removeFirst();
		for(int i=this.size()-1; i>= 0; i--){
			newCycle.add(this.get(i));
			this.remove(i);
		}
		this.clear();
		for(Prim2D prim : newCycle){
			this.add(prim);
		}
	}

	/**
	 * check to see if the cycle is valid.
	 * That is, it must be connected and
	 * start and end at the same point.
	 * @return
	 */
	public boolean isValidCycle(){
		if(this.size() < 1){
			return false;
		}
		if(this.size() == 1){
			Prim2D prim = this.getFirst();
			if(prim.ptA.equalsPt(prim.ptB)){
				return true;
			}else{
				//System.out.println("Prim2DCycle(isValid): Single element cycle, and ptA != ptB..  bogus.");
				return false;
			}
		}else{
			// traverse cycle, add points to check for duplicates along the way.
			Point2D conPt = this.getFirst().ptA;
			for(Prim2D prim : this){
				Point2D nextPt = prim.hasPtGetOther(conPt);
				if(nextPt == null){
					//System.out.println("Prim2DCycle(isValid): Cycle was not valid; could not find a connected point!");
					return false;
				}else{
					conPt = nextPt;
				}
			}
			if(conPt.equalsPt(this.getFirst().ptA)){
				return true;
			}else{
				//System.out.println("Prim2DCycle(isValid): Cycle was not valid; end point was not the same as the start point!");
				return false;
			}
		}
	}

	/**
	 * make sure all prim2D in the cycle are
	 * oriented from ptA to ptB.
	 */
	public void orientCycle(){
		if(!isValidCycle()){
			System.out.println("Prim2DCycle(orientCycle): trying to orient an invalid cycle! aborting!");
			return;
		}
		Point2D conPt = this.getFirst().ptA;
		for(int i=0; i<this.size(); i++){
			Prim2D prim = this.get(i);
			if(!prim.ptA.equalsPt(conPt)){
				this.set(i, prim.getSwappedEndPtPrim2D());
				conPt = prim.ptA;
			}else{
				conPt = prim.ptB;
			}
		}
	}

	/**
	 * get the closest euclidean distance from the point to
	 * each of the prim2D that make up this prim2DCycle.
	 * @param pt the point to test
	 * @return the closest distance to the given point.
	 */
	public double getClosestDistanceToPoint(Point2D pt){
		double distance = Double.MAX_VALUE;
		for(Prim2D prim : this){
			double d = prim.distFromPrim(pt);
			if(d < distance){
				distance = d;
			}
		}
		return distance;
	}

	public Prim2DCycle deepCopy(){
		return this.deepCopy();
	}

}
