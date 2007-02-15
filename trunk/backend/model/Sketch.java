package backend.model;

import java.util.LinkedList;

import backend.adt.Param;
import backend.adt.Parameterized;
import backend.adt.Point3D;
import backend.adt.Rotation3D;


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
public class Sketch extends Parameterized{	
	
	/**
	 * List of all the Feature2Ds contained in the sketch
	 */
	private LinkedList<Feature2D> feat2DList = new LinkedList<Feature2D>();
	
	
	
	public Sketch(){
		paramSet.addParam("o", new Param("Offset", new Point3D(0.0, 0.0, 0.0)));
		paramSet.addParam("r", new Param("Rotation", new Rotation3D(0.0, 0.0, 0.0)));
		paramSet.label = "Sketch";
	}
	
	/**
	 * add a Feature2D to the end of the list of Feature2Ds
	 * @param f2D non-null Feature2D to be added
	 * @return the index of the newly added Feature2D, or -1 if Feature2D was null.
	 */
	public int add(Feature2D f2D){
		if(f2D != null){
			feat2DList.add(f2D);
			return feat2DList.size();
		}
		return -1;
	}
	
	/**
	 * get the Feauture2D at a given index.
	 * @param i index
	 * @return the Feature, or null if index was invalid.
	 */
	public Feature2D getAtIndex(int i){
		if(i < 0 || i >= feat2DList.size()){
			// index is not valid!
			return null;
		}
		return feat2DList.get(i);
	}
	
	/**
	 * @return the size() of the list of Feature2Ds.
	 */
	public int getFeat2DListSize(){
		return feat2DList.size();
	}
	
}
