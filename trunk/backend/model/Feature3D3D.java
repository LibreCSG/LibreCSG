package backend.model;

import java.util.LinkedList;

import ui.tools.ToolInterface3D3D;
import backend.adt.ParamSet;


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
public class Feature3D3D extends Feature3D{

	private LinkedList<Feature3D> feat3DList = new LinkedList<Feature3D>();
	protected ToolInterface3D3D toolInt3D3D;
	
	public Feature3D3D(ToolInterface3D3D toolInt3D3D, ParamSet paramSet){
		this.paramSet = paramSet;
		this.toolInt3D3D = toolInt3D3D;		
	}
	
	/**
	 * add a Feature3D to the end of the list of Feature3Ds.
	 * @param f3D non-null Feature3D to be added
	 * @return the index of the newly added Feature3D, or -1 if Feature3D was null.
	 */
	public int add(Feature3D f3D){
		if(f3D != null){
			feat3DList.add(f3D);
			return feat3DList.size()-1;
		}
		return -1;
	}
	
	/**
	 * get the Feature3D at a given index
	 * @param i index
	 * @return the Feature3D, or null if index was invalid.
	 */
	public Feature3D getAtIndex(int i){
		if(i < 0 || i >= feat3DList.size()){
			// index was not valid!
			return null;
		}
		return feat3DList.get(i);
	}
	
	/**
	 * @return the size() of the list of Feature3Ds.
	 */
	public int getFeat3DListSize(){
		return feat3DList.size();
	}
	
}
