package backend.model;

import java.util.LinkedList;
import java.util.List;

import net.sf.avocado_cad.model.api.IModify;
import net.sf.avocado_cad.model.api.IPart;
import net.sf.avocado_cad.model.api.ISubPart;
import net.sf.avocado_cad.model.api.adt.IParamSet;
import net.sf.avocado_cad.model.api.event.BackendGlobal;
import net.sf.avocado_cad.model.api.sketch.ISketchPlane;


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

/**
 * model componet that represents "Modify" tools
 */
public class Modify implements SubPart, IModify{

	protected List<ISubPart> subPartList = new LinkedList<ISubPart>();
	
	protected IPart part;
	public final int ID;
	
	public Modify(IPart part, IParamSet paramSet, int ID){
		this.part = part;
		this.ID = ID;
	}
	
	public IPart getParentPart(){
		return this.part;
	}
	
	/**
	 * add a SubPart to the end of the list of SubParts.
	 * @param subPart non-null SubPart  to be added
	 * @return the index of the newly added SubPart, or -1 if SubPart was null.
	 */
	public int add(ISubPart subPart){
		if(subPart != null){
			subPartList.add(subPart);
			BackendGlobal.modelEventHandler.notifyElementAdded();
			return subPartList.size()-1;
		}
		return -1;
	}
	
	/**
	 * get the SubPart at a given index
	 * @param i index
	 * @return the SubPart, or null if index was invalid.
	 */
	public ISubPart getAtIndex(int i){
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
		BackendGlobal.modelEventHandler.notifyElementRemoved();
	}

	
	public Build getBuild() {
		return null;
	}

	public Modify getModify() {
		return this;
	}

	public Sketch getSketch() {
		return null;
	}
	
	public int getUniqueID(){
		return ID;
	}

	// TODO: implement ability to get sketch plane!
	public ISketchPlane getPlaneByFaceID(int uniqueFaceID){
		return null;
	}
	
}
