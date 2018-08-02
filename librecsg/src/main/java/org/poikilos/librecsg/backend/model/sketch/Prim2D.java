package org.poikilos.librecsg.backend.model.sketch;

import java.util.LinkedList;

import com.jogamp.opengl.GL2;
import org.poikilos.librecsg.backend.adt.Point2D;


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
public abstract class Prim2D {

	/**
	 * end points of curve.
	 */
	protected Point2D ptA;
	protected Point2D ptB;
	protected String descriptor;
	protected String id;

	public boolean isSelected = false;

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

	/**
	 * draw the Prim2D the screen
	 * @param gl
	 */
	abstract public void glDraw(GL2 gl);

	/**
	 * return the point at which "this" prim2D intersects
	 * the given prim2D at any point other than the givn prim2D's end points.
	 * @param anyPrim2D the given prim2D to check for intersection
	 * @return the point of intersection, or null if no intersection occurs.
	 */
	abstract public Point2D intersect(Prim2D anyPrim2D);

	/**
	 * if line and this prim2D intersect past Geometry2D.epsilon inward
	 * from the line's ends, the point of intersection (on the line)
	 * should be returned.. otherwise, return null.
	 * @param ln the prim2DLine to be checked
	 * @return the point of intersection, or NULL if no intersection.
	 */
	abstract public Point2D intersectsLine(Prim2DLine ln);

	/**
	 * if arc and this prim2D intersect past Geometry2D.epsilon inward
	 * from the arc's ends, the point of intersection (on the arc)
	 * should be returned.. otherwise, return null.
	 * @param arc the prim2DArc to be checked
	 * @return the point of intersection, or NULL if no intersection.
	 */
	abstract public Point2D intersectsArc(Prim2DArc arc);

	/**
	 * the distance from the specified point to the closest
	 * point on this prim2D.
	 * @param pt the point to check for distance from the prim2D
	 * @return the distance
	 */
	abstract public double distFromPrim(Point2D pt);

	/**
	 * The resulting pair of prim2D when this prim2D is
	 * split at the specified point.  If the point is determined
	 * to be eroneous or the prim2D can not be split, NULL may
	 * be returned.  Given a "good" split, this should never happen though.
	 * @param pt the point to split the prim2D
	 * @return a pair of prim2Ds
	 */
	abstract public PrimPair2D splitPrimAtPoint(Point2D pt);

	/**
	 * the length of the prim2D is the "string" length
	 * if you were to follow the curve. (line integral)
	 * @return the length
	 */
	abstract public double getPrimLength();

	/**
	 * Get a list of vertices that define the curve/line.
	 * simple prim2D (i.e., the line) may only return the
	 * two end points.  More complex prim2D should contain a
	 * list of vertices that define the shape.<br/>
	 * (vertices ordered always <b>from ptA to ptB</b>).
	 * @param maxVerts maximum number of vertices to use
	 * @return the list of vertices that define the prim2D.
	 */
	abstract public LinkedList<Point2D> getVertexList(int maxVerts);

	/**
	 * get a new Prim2D that has its endpoints swapped
	 * @return
	 */
	abstract public Prim2D getSwappedEndPtPrim2D();

	/**
	 * @return the center point along the prim2D. Useful for
	 * testing concavity and CW/CCW lines.
	 */
	abstract public Point2D getCenterPtAlongPrim();

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

	public Point2D getPtA(){
		return ptA;
	}

	public Point2D getPtB(){
		return ptB;
	}

	public String getDescriptor(){
		return descriptor;
	}

	public String getID(){
		return id;
	}

	public String setID(String newID){
		return id=newID;
	}

	public String toString(){
		return "Prim2D (ID=" + this.id + "): ptA=" + ptA + ", ptB=" + ptB;
	}

}
