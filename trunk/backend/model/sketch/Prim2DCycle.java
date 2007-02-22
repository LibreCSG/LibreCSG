package backend.model.sketch;

import java.util.LinkedList;

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
public class Prim2DCycle extends Prim2DList implements Comparable{

	private static final long serialVersionUID = 10013L;

	public double getCycleLength(){
		double length = 0.0;
		for(Prim2D prim : this){
			length += prim.getPrimLength();
		}
		return length;
	}

	public int compareTo(Object o) {
		if(o instanceof Prim2DCycle){
			Prim2DCycle primB = (Prim2DCycle)o;
			if(primB.getCycleLength() > this.getCycleLength()){
				return -1;
			}
			if(primB.getCycleLength() < this.getCycleLength()){
				return 1;
			}
		}
		return 0; 
	}
	
	public boolean containsPt(Point2D pt){
		for(Prim2D prim : this){
			if(prim.ptA.equalsPt(pt) || prim.ptB.equalsPt(pt)){
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
		double angle = 0.0;
		LinkedList<Point2D> pointList = new LinkedList<Point2D>();
		Point2D conPt = this.getFirst().ptA;
		for(Prim2D prim : this){
			pointList.add(conPt);
			conPt = prim.hasPtGetOther(conPt);
		}
		pointList.add(this.getFirst().ptA);
		pointList.add(this.getFirst().ptB);
		for(int i=0; i<pointList.size()-2; i++){
			angle += Geometry2D.threePtAngle(pointList.get(i), pointList.get(i+1), pointList.get(i+2));
		}
		//System.out.println("Angle for complete cycle is: " + angle);
		return (angle > 0.0); // it should be ideally +180.0 or -180.0
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
	
}
