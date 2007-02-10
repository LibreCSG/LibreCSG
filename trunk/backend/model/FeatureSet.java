package backend.model;

import java.util.Iterator;
import java.util.LinkedList;

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
public class FeatureSet {
	
	/**
	 * implementation of the set of features.  this is abstracted away
	 * so that it can be changed "under the cover" at a later time if
	 * speed/memory are a concern.
	 */
	protected LinkedList <Feature>featSet = new LinkedList<Feature>();
	
	/**
	 * construct a new set of features.  All features
	 * should share the same tool mode. 
	 * (e.g., 2D tools, 2D-3D tools, etc.)
	 *
	 */
	public FeatureSet(){
	}
	
	/**
	 * add a new feature to the set
	 * @param newFeature
	 */
	public void addFeature(Feature newFeature){
		featSet.add(newFeature);
		System.out.println("added feature! type:" + newFeature.toolInterface.getClass().getCanonicalName());
		AvoGlobal.paramDialog.updateParams(newFeature);
	}
	
	/**
	 * get the last feature that was added to the set.
	 * The returned feature points to the actual feature 
	 * in the set, so changing it does not require additional
	 * re-placement in the set after an attribute has been modified.
	 * @return
	 */
	public Feature getLastFeature(){
		if(featSet != null && featSet.size() > 0){
			return featSet.getLast();
		}else{
			return null;
		}
	}
	
	/**
	 * returns an iterator over all of the features
	 * in the set.  Useful when access to all of 
	 * the features is required. (i.e., drawing)
	 * @return
	 */
	public Iterator iterator(){
		return featSet.iterator();
	}
	
	/**
	 * deselect all of the features in the feature set.
	 *
	 */
	public void deselectAll(){
		for(Feature f : featSet){
			f.isSelected = false;
		}
	}
	
	/**
	 * select all of the features in the feature set.
	 *
	 */
	public void selectAll(){
		for(Feature f : featSet){
			f.isSelected = true;
		}
	}
	
	/**
	 * remove the last feature from the feature set.
	 *
	 */
	public void removeLastFeature(){
		if(featSet.size() > 0){
			featSet.removeLast();
		}else{
			System.out.println("tried to remove the last feature from the FeatureSet, but there were NO features!!");
		}
	}
	
}
