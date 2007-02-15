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
public class Project {

	/**
	 * List of all the groups contained in the project.
	 */
	protected LinkedList<Group> groupList = new LinkedList<Group>();
	
	protected int activeGroup = -1;
	
	
	public Project(){
	}
	
	/**
	 * add a group to the end of the list of groups
	 * @param group non-null Group to be added
	 * @return the index of the newly added group, or -1 if group was null.
	 */
	public int add(Group group){
		if(group != null){
			groupList.add(group);
			int newIndex = groupList.size()-1;
			return newIndex;
		}
		return -1;
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
	}
	
	/**
	 * set the active group to none
	 */
	public void setActiveToNone(){
		activeGroup = -1;
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
	 * try to get the active Feature3D.
	 * @return the active Feature3D, or null if not found
	 */
	public Feature3D getActiveFeat3D(){
		Part p = getActivePart();
		if(p != null){
			 return p.getActiveFeat3D();
		}
		return null;
	}
	
	/**
	 * try to get the active Sketch.
	 * @return the active Sketch, or null if not found
	 */
	public Sketch getActiveSketch(){
		Feature3D f3D = getActiveFeat3D();
		if(f3D != null && f3D instanceof Feature2D3D){
			 return ((Feature2D3D)f3D).getActiveSketch();
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
	
	// TODO: High-level functionality to add a new sketch/feature/etc.
	//       tracing down active elements where possible and creating the rest.
	
	
}
