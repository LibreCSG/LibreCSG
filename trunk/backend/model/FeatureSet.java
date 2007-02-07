package backend.model;

import java.util.Iterator;
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
public class FeatureSet {
	
	protected LinkedList <Feature>featSet = new LinkedList<Feature>();
	
	/**
	 * construct a new set of features.  All features
	 * should share the same tool mode. 
	 * (e.g., 2D tools, 2D-3D tools, etc.)
	 *
	 */
	public FeatureSet(){
	}
	
	
	public void addFeature(Feature newFeature){
		featSet.add(newFeature);
		System.out.println("added feature! type:" + newFeature.toolInterface.getClass().getCanonicalName());
	}
	
	public Feature getLastFeature(){
		return featSet.getLast();
	}
	
	public Iterator iterator(){
		return featSet.iterator();
	}
	
}