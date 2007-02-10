package backend.geometry;

import backend.adt.Point2D;

public class Geometry2D {

	/**
	 * determines whether a point is on a line defined by its two endpoints.
	 * @param ptA line end 
	 * @param ptB line end
	 * @param ptC point
	 * @param err allowable error where point is still considered on the line
	 * @return
	 */
	public static boolean pointOnLine(Point2D ptA, Point2D ptB, Point2D ptC, double err){
		double segLength = ptA.computeDist(ptB);
		double distA = ptA.computeDist(ptC);
		double distB = ptB.computeDist(ptC);
		
		// just making triangles here.. .
		
		// check first to see if the point is past the 
		// ends of the line segment.
		if(distA > (segLength+err) || (distB > segLength+err)){
			return false;
		}
		
		// now compute distance from line...
		double alpha = (distA*distA - distB*distB + segLength*segLength) / (2.0*segLength);
		double distFromLine = distA*distA - alpha*alpha;
		if(distFromLine <= err){
			return true;
		}else{
			return false;
		}		
	}
	
}
