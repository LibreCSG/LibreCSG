package backend.adt;



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
public class Point2D {
	protected double x = 0.0;
	protected double y = 0.0;
	
	/**
	 * Abstract Data Type: Point 2D
	 * Specifies a point in 2D space.
	 * @param px
	 * @param py
	 */
	public Point2D(double px, double py){
		x = px;
		y = py;
	}
	
	/**
	 * compute the distance from the 
	 * instance of this point to another
	 * point.
	 * @param p2
	 * @return
	 */
	public double computeDist(Point2D p2){
		double dx = x-p2.x;
		double dy = y-p2.y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	/**
	 * return a new point that is the sum
	 * of the current point and another point.
	 * @param p2
	 * @return
	 */
	public Point2D addPt(Point2D p2){
		return new Point2D(x+p2.x, y+p2.y);
	}
	
	/**
	 * return the difference between the current
	 * point and another point. 
	 * result = current - otherPoint
	 * @param p2
	 * @return
	 */
	public Point2D subPt(Point2D p2){
		return new Point2D(x-p2.x, y-p2.y);
	}
	
	/**
	 * return a new point that is the current
	 * point multiplied by a scalar.
	 * @param scaleFactor
	 * @return
	 */
	public Point2D getScaledPt(double scaleFactor){
		return new Point2D(x*scaleFactor, y*scaleFactor);
	}
	
	/**
	 * get the X value
	 * @return
	 */
	public double getX(){
		return x;
	}
	
	/**
	 * get the Y value
	 * @return
	 */
	public double getY(){
		return y;
	}
	
	/**
	 * set the X value
	 * @param newX
	 */
	public void setX(double newX){
		x = newX;
	}

	/**
	 * set the Y value
	 * @param newY
	 */
	public void setY(double newY){
		y = newY;
	}
	
}
