package backend.model;

import java.util.LinkedList;


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
public class Group {

	/**
	 * List of all of the parts contained in the group.
	 */
	protected LinkedList<Part> partList = new LinkedList<Part>();
	
	protected int activePart = -1;
	
	public Group(){
	}
	
	/**
	 * add a part to the end of the list of parts.
	 * @param part non-null Part to be added
	 * @return the index of the newly added part, or -1 if part was null.
	 */
	public int add(Part part){
		if(part != null){
			partList.add(part);
			return partList.size()-1;
		}
		return -1;
	}
	
	/**
	 * get the part at a given index.
	 * @param i index
	 * @return the Part, or null if index was invalid.
	 */
	public Part getAtIndex(int i){
		if(i < 0 || i >= partList.size()){
			// index is not valid!
			return null;
		}
		return partList.get(i);
	}
	
	/**
	 * @return the size() of the list of parts.
	 */
	public int getPartListSize(){
		return partList.size();
	}
	
	
	/**
	 * set the index of the part that should be set to Active.
	 * @param i index
	 */
	public void setActivePart(int i){
		if(i < 0 || i >= partList.size()){
			// index is not valid!
			return;
		}
		activePart = i;
	}
	
	/**
	 * set the active part to none
	 */
	public void setActiveToNone(){
		activePart = -1;
	}
	
	/**
	 * get the currently active part
	 * @return the active part, or null if no part is active
	 */
	public Part getActivePart(){
		return this.getAtIndex(activePart);
	}
	
	
}
