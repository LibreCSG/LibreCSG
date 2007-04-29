package backend.geometry;

import backend.adt.Point2D;


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
public class Geometry2D {

	/**
	 * allowance for errors in double calculations/equalities.
	 */
	public static final double epsilon = 0.000000001;
	
	/**
	 * calculates the distance between a line segment (defined by
	 * its two end points) and a point.
	 * @param ptA line end 
	 * @param ptB line end
	 * @param ptC point
	 * @return the distance
	 */
	public static double distFromLineSeg(Point2D ptA, Point2D ptB, Point2D ptC){
		double segLength = ptA.computeDist(ptB);
		double distA = ptA.computeDist(ptC);
		double distB = ptB.computeDist(ptC);		
		
		// check first to see if the point is past the 
		// ends of the line segment.
		if(distA > segLength || distB > segLength){
			return Math.max(distA,distB);
		}		
		
		// check to see if point is on the line itself
		if((distA+distB) >= (segLength-epsilon) && (distA+distB) <= (segLength+epsilon)){
			return 0.0;
		}
		
		// just making triangles here...
		// now compute distance from line...
		double alpha = (distA*distA - distB*distB + segLength*segLength) / (2.0*segLength);
		double distFromLine = Math.sqrt(distA*distA - alpha*alpha);		
		return distFromLine;
	}
	
	/**
	 * calculates the distance between a circle (defined by
	 * its center and radius) and a point.
	 * @param c center point
	 * @param radius
	 * @param pt point 
	 * @return the distance
	 */
	public static double distFromCircle(Point2D ptC, double radius, Point2D ptX){
		// calculate distance between ptC and ptX...
		// since all points on a circle are equidistant from the center,
		// the distance from the closest part of the circle will
		// be: abs((distance ptC to ptX) - radius)
		return Math.abs(ptC.computeDist(ptX) - radius);
	}
	
	/**
	 * determine how far ptC is from being colinear with ptA and ptB.
	 * @param ptA
	 * @param ptB
	 * @param ptC
	 * @return angle (degrees) difference between AB and BC
	 */
	public static double threePtAngle(Point2D ptA, Point2D ptB, Point2D ptC){
		// angle AB
		double angleAB = getAnglePtToPt(ptA, ptB);
		double angleBC = getAnglePtToPt(ptB, ptC);
		//System.out.println("A:" + ptA + " B:" + ptB + " C:" + ptC);
		//System.out.println("AB:" + angleAB + " BC:" + angleBC);
		double angle3Pt = (angleBC - angleAB);
		if(angle3Pt < 180.0){
			angle3Pt += 360.0;
		}
		if(angle3Pt > 180.0){
			angle3Pt -= 360.0;
		}
		return angle3Pt%180.0;
	}
	
	/**
	 * get the cartesian angle between ptA and ptB
	 * @param ptA
	 * @param ptB
	 * @return the angle (in degrees)
	 */
	public static double getAnglePtToPt(Point2D ptA, Point2D ptB){
		double x = ptB.getX() - ptA.getX();
		double y = ptB.getY() - ptA.getY();
		double radAngle = 0.0;
		if(x > 0.0){
			radAngle = Math.atan(y/x);
		}
		if(x < 0.0){
			if(y >= 0.0){
				radAngle = Math.atan(y/x) + Math.PI;
			}else{
				radAngle = Math.atan(y/x) - Math.PI;
			}			
		}
		if(x == 0.0){
			if(y > 0.0){
				radAngle = Math.PI/2.0;
			}
			if(y < 0.0){
				radAngle = -Math.PI/2.0;
			}
		}
		return radAngle*180.0/Math.PI;
	}

}
