package backend.model;

import java.util.LinkedList;
import java.util.List;

import backend.global.AvoGlobal;


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
public class Group {

	/**
	 * List of all of the parts contained in the group.
	 */
	protected List<Part> partList = new LinkedList<Part>();
	
	protected int activePart = -1;
	
	protected Project project;
	public final int ID;
	private int partCounter = 1;
		
	public Group(Project project, int ID){
		this.project = project;
		this.ID = ID;
	}
	
	public Project getParentProject(){
		return this.project;
	}
	
	/**
	 * add a part to the end of the list of parts.
	 * @param part non-null Part to be added
	 * @return the index of the newly added part, or -1 if part was null.
	 */
	public int addNewPart(){
		partList.add(new Part(this, partCounter++));
		AvoGlobal.modelEventHandler.notifyElementAdded();
		int newIndex = partList.size()-1;
		activePart = newIndex;
		return newIndex;
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
		AvoGlobal.modelEventHandler.notifyActiveElementChanged();
	}
	
	/**
	 * set the active part to none
	 */
	public void setActiveToNone(){
		activePart = -1;
		AvoGlobal.modelEventHandler.notifyActiveElementChanged();
	}
	
	/**
	 * get the currently active part
	 * @return the active part, or null if no part is active
	 */
	public Part getActivePart(){
		return this.getAtIndex(activePart);
	}
	
	/**
	 * Remove the Part at the index if present.
	 * @param i index
	 */
	public void removePartAtIndex(int i){
		if(i < 0 || i >= partList.size()){
			// index is not valid!
			return;
		}
		partList.remove(i);
		AvoGlobal.modelEventHandler.notifyElementRemoved();
	}
	
	/**
	 * remove the active Part from the list.
	 */
	public void removeActivePart(){
		removePartAtIndex(activePart);
	}
	
	
	public Part getPartByUniqueID(int uniqueID){
		for(Part part : partList){
			if(part.ID == uniqueID){
				return part;
			}
		}
		System.out.println("Group(getPartByUniqueID): No Part with the specified uniqueID was found! ID=" + uniqueID +", FIX THIS STAT!");
		return null;
	}
}
