package backend.primatives;

import javax.media.opengl.GL;

import ui.opengl.GLDynPrim;
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
public class Prim2DArc extends Prim2D{

	//TODO: remember to update ptA and ptB -- the arc's end points.
	
	protected Point2D center;
	protected double  radius;
	protected double  startAngle;
	protected double  arcAngle;
	
	public Prim2DArc(Point2D center, double radius, double startAngle, double arcAngle){
		this.center = center;
		this.radius = radius;
		this.startAngle = startAngle;
		this.arcAngle   = arcAngle;
		//TODO: set ptA, and ptB based on center, radius, startAngle, and arcAngle
	}
	
	public void glDraw(GL gl) {
		GLDynPrim.arc2D(gl, center, radius, startAngle, arcAngle, 0.0);
		GLDynPrim.point(gl, center.getX(), center.getY(), 0.0, 3.0);
	}

	public Point2D intersectsArc(Prim2DArc arc) {
		// TODO need Intersection code!
		return null;
	}

	public Point2D intersectsLine(Prim2DLine ln) {		
		// TODO need Intersection code!
		return null;
	}

	public double distFromPrim(Point2D pt) {
		// TODO: compute distance from Arc
		return 10.0;
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
		return null;
	}

	@Override
	public double getPrimLength() {
		// calculate arc length
		return (arcAngle/180.0)*Math.PI*radius;
	}
	
}
