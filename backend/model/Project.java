package backend.model;

import java.util.LinkedList;
import java.util.List;

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
public class Project {

	/**
	 * List of all the groups contained in the project.
	 */
	protected List<Group> groupList = new LinkedList<Group>();
	
	protected int activeGroup = -1;
	
	protected int groupCounter = 1;
	
	public Project(){		
	}
	
	/**
	 * add a group to the end of the list of groups
	 * @param group non-null Group to be added
	 * @return the index of the newly added group, or -1 if group was null.
	 */
	public int addNewGroup(){
		groupList.add(new Group(this, groupCounter++));
		AvoGlobal.modelEventHandler.notifyElementAdded();
		int newIndex = groupList.size()-1;
		activeGroup = newIndex;
		return newIndex;
	}
	
	/**
	 * get the group at a given index
	 * @param i index
	 * @return the Group, or null if index was invalid.
	 */
	public Group getAtIndex(int i){
		if(i < 0 || i >= groupList.size()){
			// index is not valid!
			return null;
		}
		return groupList.get(i);
	}
	
	/**
	 * @return the size() of the list of groups.
	 */
	public int getGroupListSize(){
		return groupList.size();
	}
	
	/**
	 * set the index of the group that should be set to Active.
	 * @param i index
	 */
	public void setActiveGroup(int i){
		if(i < 0 || i >= groupList.size()){
			// index is not valid!
			return;
		}
		activeGroup = i;
		AvoGlobal.modelEventHandler.notifyActiveElementChanged();
	}
	
	/**
	 * set the active group to none
	 */
	public void setActiveToNone(){
		activeGroup = -1;
		AvoGlobal.modelEventHandler.notifyActiveElementChanged();
	}
	
	/**
	 * get the currently active group
	 * @return the active group, or null if no group is active
	 */
	public Group getActiveGroup(){
		return this.getAtIndex(activeGroup);
	}
	
	/**
	 * try to get the active part.
	 * @return the active part, or null if not found
	 */
	public Part getActivePart(){
		Group g = getActiveGroup();
		if(g != null){
			return g.getActivePart();
		}
		return null;
	}
	
	/**
	 * try to get the active SubPart.
	 * @return the active SubPart, or null if not found
	 */
	public SubPart getActiveSubPart(){
		Part p = getActivePart();
		if(p != null){
			 return p.getActiveSubPart();
		}
		return null;
	}
	
	/**
	 * get the active Sketch if it exists.
	 * @return
	 */
	public Sketch getActiveSketch(){
		SubPart subPart = getActiveSubPart();
		if(subPart != null){
			return subPart.getSketch();
		}
		return null;
	}
	
	/**
	 * get the active Feature2D3D if it exists.
	 * @return
	 */
	public Feature2D3D getActiveFeat2D3D(){
		SubPart subPart = getActiveSubPart();
		if(subPart != null){
			return subPart.getFeature2D3D();
		}
		return null;
	}
	
	/**
	 * get the active Feature3D3D if it exists.
	 * @return
	 */
	public Feature3D3D getActiveFeat3D3D(){
		SubPart subPart = getActiveSubPart();
		if(subPart != null){
			return subPart.getFeature3D3D();
		}
		return null;
	}
	
	
	/**
	 * try to get the active Feature2D.
	 * @return the active Feature2D, or null if not found.
	 */
	public Feature2D getActiveFeat2D(){
		Sketch sketch = getActiveSketch();
		if(sketch != null){
			return sketch.getActiveFeat2D();
		}
		return null;
	}
	
	/**
	 * Remove the Group at the index if present.
	 * @param i index
	 */
	public void removeGroupAtIndex(int i){
		if(i < 0 || i >= groupList.size()){
			// index is not valid!
			return;
		}
		groupList.remove(i);
		AvoGlobal.modelEventHandler.notifyElementRemoved();
	}
	
	/**
	 * remove the active Group from the list.
	 */
	public void removeActiveGroup(){
		removeGroupAtIndex(activeGroup);
	}
	
	// TODO: High-level functionality to add a new sketch/feature/etc.
	//       tracing down active elements where possible and creating the rest.
	
	
}
