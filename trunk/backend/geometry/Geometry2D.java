package backend.geometry;

import backend.adt.Point2D;

public class Geometry2D {

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
	
}
