package org.poikilos.librecsg.backend.model.sketch;

import java.util.LinkedList;


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
public class Prim2DList extends LinkedList<Prim2D>{

	private static final long serialVersionUID = 1000L;

	/**
	 * set all prim2D to not be consumed in either the
	 * <bold>A-->B</bold> or <bold>B-->A</bold> directions.
	 */
	public void unconsumeAll(){
		for(Prim2D prim : this){
			prim.consumedAB = false;
			prim.consumedBA = false;
		}
	}

	/**
	 * get a list of all unique points in this Prim2DList.
	 * @return the list of points.
	 */
	public Point2DList getPointList(){
		Point2DList pList = new Point2DList();
		for(Prim2D prim : this){
			if(!pList.contains(prim.ptA)){
				pList.add(prim.ptA);
			}
			if(!pList.contains(prim.ptB)){
				pList.add(prim.ptB);
			}
		}
		return pList;
	}

	public Prim2DList deepCopy(){
		return this.deepCopy();
	}


}
