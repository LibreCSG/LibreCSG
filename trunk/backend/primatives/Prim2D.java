package backend.primatives;

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
public interface Prim2D {
 
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
	
}
