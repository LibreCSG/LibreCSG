package backend.adt;

import java.util.Iterator;
import java.util.LinkedHashMap;

import ui.tools.ToolModel;
import ui.tools.ToolModel2D;
import ui.tools.ToolModel2D3D;
import ui.tools.ToolModel3D3D;


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

	public String label;
	protected ToolModel  toolModel;
	protected LinkedHashMap <String,Param>paramSet = new LinkedHashMap<String,Param>();
	
	/**
	 * construct a new parameter set.
	 */
	public ParamSet(String label, ToolModel toolModel){
		this.label = label;
		if(toolModel == null){
			System.out.println(" *** WARNING *** ParamSet was given a NULL ToolInterface.  This is a bad idea!");
		}
		this.toolModel = toolModel;
	}
	
	/**
	 * get the tool model used to build
	 * and manipulate the parameter set.
	 * @return the tool model
	 */
	public ToolModel getToolModel(){
		return toolModel;
	}
	
	/**
	 * check to see if the tool model associated with this 
	 * ParamSet is a ToolModel2D, and if so return it.  
	 * otherwise, return NULL. 
	 * @return the ToolModel2D, or NULL
	 */
	public ToolModel2D getToolModel2D(){
		// TODO: get rid of cast?!
		if(toolModel != null && toolModel instanceof ToolModel2D){
			return (ToolModel2D)toolModel;
		}
		return null;
	}
	
	/**
	 * try to finalize the ParamSet by calling finalize from
	 * the tool model, if the tool model is not NULL.
	 */
	public void tryToFinalize(){
		if(toolModel != null){
			toolModel.finalize(this);
		}
	}
	
	/**
	 * check to see if the tool model associated with this 
	 * ParamSet is a ToolModel2D3D, and if so return it.  
	 * otherwise, return NULL. 
	 * @return the ToolModel2D3D, or NULL
	 */
	public ToolModel2D3D getToolModel2D3D(){
		// TODO: get rid of cast?!
		if(toolModel != null && toolModel instanceof ToolModel2D3D){
			return (ToolModel2D3D)toolModel;
		}
		return null;
	}
	
	/**
	 * check to see if the tool model associated with this 
	 * ParamSet is a ToolModel3D3D, and if so return it.  
	 * otherwise, return NULL. 
	 * @return the ToolModel3D3D, or NULL
	 */
	public ToolModel3D3D getToolModel3D3D(){
		// TODO: get rid of cast?!
		if(toolModel != null && toolModel instanceof ToolModel3D3D){
			return (ToolModel3D3D)toolModel;
		}
		return null;
	}
	
	/**
	 * update all of the Derived Parameters in the ParamSet
	 * by requesting the toolInterface to do the update.
	 * If an exception occurs, a warning is printed to the
	 * console to notify that code sould be fixed. (probably 
	 * a typo or incorrect variable type is to blame)
	 */
	public void updateDerivedParams(){
		if(toolModel != null){			
			if(toolModel.paramSetIsValid(this)){
				toolModel.updateDerivedParams(this);
			}else{
				System.out.println(" *** PARAM SET *** param set was not valid! not performing derived parameter update.");
			}	
		}
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
	 * @throws ParamNotFoundException
	 */
	public void removeParam(String name) throws ParamNotFoundException{
		if(paramSet.get(name) != null){
			paramSet.remove(name);
		}else{
			System.out.println("Tried to remove a parameter that did not exist in the set!");
			System.out.println("  --> Name: " + name);
			throw new ParamNotFoundException();
		}
	}
	
	/**
	 * Get a parameter from the set by name.
	 * @param name
	 * @return
	 * @throws ParamNotFoundException
	 */
	public Param getParam(String name) throws ParamNotFoundException{
		Param p = paramSet.get(name);
		if(p != null){
			return p;
		}
		throw new ParamNotFoundException();
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
	 * @throws ParamNotFoundException
	 */
	public void changeParam(String name, Object data) throws ParamNotFoundException{
		if(paramSet.get(name) != null){
			paramSet.get(name).change(data);
		}else{
			System.out.println("Tried to change a parameter that did not exist in the set!");
			System.out.println("  --> Name: " + name);
			throw new ParamNotFoundException();
		}		
	}

	
	/**
	 * Check to see if parameter set contains a parameter
	 * with the given name and type.
	 * @param name
	 * @param type
	 * @return
	 */
	public boolean hasParam(String name, PType type){
		if(paramSet.get(name) != null){
			if(paramSet.get(name).getType() == type){
				return true;
			}else{
				// found param with that name, but type
				// does not match...
				System.out.println("ParamSet.hasParam: found param, but type does not match. " +
						paramSet.get(name).getType() + " != " + type );
				return false;
			}
		}else{
			// param with that name was not in the set
			System.out.println("ParamSet.hasParam: no param found in set with the name: " + name);
			return false;
		}		
	}
}
