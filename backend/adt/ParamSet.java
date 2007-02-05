package backend.adt;

import java.util.Iterator;
import java.util.LinkedHashMap;


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
public class ParamSet {

	protected LinkedHashMap <String,Param>paramSet = new LinkedHashMap<String,Param>();
	
	/**
	 * construct a new parameter set.
	 */
	public ParamSet(){
	}
	
	/**
	 * Add a parameter to the set.
	 * @param p
	 * @param name
	 */
	public void addParam(String name, Param p){
		paramSet.put(name, p);
	}
	
	/**
	 * Remove a parameter to the set.
	 * @param p
	 * @param name
	 */
	public void removeParam(String name){
		if(paramSet.get(name) != null){
			paramSet.remove(name);
		}else{
			System.out.println("Tried to remove a parameter that did not exist in the set!");
			System.out.println("  --> Name: " + name);
		}
	}
	
	/**
	 * Get a parameter from the set by name.
	 * @param name
	 * @return
	 */
	public Param getParam(String name){
		return paramSet.get(name);
	}
	
	/**
	 * get an iterator over all of the 
	 * parameters in the set.
	 * @return
	 */
	public Iterator<Param> getIterator(){
		return paramSet.values().iterator();
	}
	
	/**
	 * change a parameter's value.  The type
	 * of the parameter must be the same as it
	 * was originally defined to be when constructed
	 * or the operation will be aborted and no 
	 * change will be made.
	 * @param name
	 * @param p
	 */
	public void changeParam(String name, Object data){
		if(paramSet.get(name) != null){
			paramSet.get(name).change(data);
		}else{
			System.out.println("Tried to change a parameter that did not exist in the set!");
			System.out.println("  --> Name: " + name);
		}		
	}
	
}
