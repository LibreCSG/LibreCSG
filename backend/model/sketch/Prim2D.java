package backend.model.sketch;

import javax.media.opengl.GL;

import backend.adt.Point2D;


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
public abstract class Prim2D {
 	
	/**
	 * end points of curve.
	 */
	public Point2D ptA = new Point2D(0.0,0.0);
	public Point2D ptB = new Point2D(0.0,0.0);
	
	/**
	 * Each prim2D can only be a part of two regions.
	 * Regions consume the prim2D either in the AB
	 * or BA directions.  This flag indicates if it
	 * has been consumed in the <bold>A-->B</bold> direction.
	 */
	public boolean consumedAB = false;
	
	/**
	 * Each prim2D can only be a part of two regions.
	 * Regions consume the prim2D either in the AB
	 * or BA directions.  This flag indicates if it
	 * has been consumed in the <bold>B-->A</bold> direction.
	 */
	public boolean consumedBA = false;
	
	abstract public void glDraw(GL gl);
	
	/**
	 * return the point at which "this" prim2D intersects
	 * the given prim2D at any point other than the givn prim2D's end points.  
	 * @param anyPrim2D the given prim2D to check for intersection
	 * @return the point of intersection, or null if no intersection occurs.
	 */
	abstract public Point2D intersect(Prim2D anyPrim2D);
	
	abstract public Point2D intersectsLine(Prim2DLine ln);
	
	abstract public Point2D intersectsArc(Prim2DArc arc);
	
	abstract public double distFromPrim(Point2D pt);	
	
	abstract public PrimPair2D splitPrimAtPoint(Point2D pt);	
	
	abstract public double getPrimLength();
	
	/**
	 * Test to see if the Prim2D ends at the given point.
	 * If it does, return the <em>other</em> end point.
	 * If not, returns null.
	 * @param ptC non-null point to test.
	 * @return the other end point, or null if given point is not an end point.
	 */
	public Point2D hasPtGetOther(Point2D ptC){
		if(ptA.equalsPt(ptC)){
			return ptB;
		}
		if(ptB.equalsPt(ptC)){
			return ptA;
		}
		return null;
	}
	
}