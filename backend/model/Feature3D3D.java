package backend.model;

import java.util.LinkedList;

import ui.tools.ToolInterface3D3D;
import backend.adt.ParamSet;
import backend.global.AvoGlobal;


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
public class Feature3D3D implements SubPart{

	protected LinkedList<SubPart> subPartList = new LinkedList<SubPart>();
	protected ToolInterface3D3D toolInt3D3D;
	public ParamSet paramSet = null;
	
	public Feature3D3D(ToolInterface3D3D toolInt3D3D, ParamSet paramSet){
		this.paramSet = paramSet;
		this.toolInt3D3D = toolInt3D3D;		
	}
	
	/**
	 * add a SubPart to the end of the list of SubParts.
	 * @param subPart non-null SubPart  to be added
	 * @return the index of the newly added SubPart, or -1 if SubPart was null.
	 */
	public int add(SubPart subPart){
		if(subPart != null){
			subPartList.add(subPart);
			AvoGlobal.modelEventHAndler.notifyElementAdded();
			return subPartList.size()-1;
		}
		return -1;
	}
	
	/**
	 * get the SubPart at a given index
	 * @param i index
	 * @return the SubPart, or null if index was invalid.
	 */
	public SubPart getAtIndex(int i){
		if(i < 0 || i >= subPartList.size()){
			// index was not valid!
			return null;
		}
		return subPartList.get(i);
	}
	
	/**
	 * @return the size() of the list of SubParts.
	 */
	public int getSubPartListSize(){
		return subPartList.size();
	}
	
	/**
	 * Remove the SubPart at the index if present.
	 * @param i index
	 */
	public void removeSubPartAtIndex(int i){
		if(i < 0 || i >= subPartList.size()){
			// index is not valid!
			return;
		}
		subPartList.remove(i);
		AvoGlobal.modelEventHAndler.notifyElementRemoved();
	}

	
	public Feature2D3D getFeature2D3D() {
		return null;
	}

	public Feature3D3D getFeature3D3D() {
		return this;
	}

	public Sketch getSketch() {
		return null;
	}

}
