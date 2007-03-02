package backend.model;

import ui.tools.ToolModel2D;
import backend.adt.ParamSet;
import backend.model.sketch.Prim2DList;


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
public class Feature2D {

	public boolean		      isSelected = true;
	public ParamSet           paramSet   = null;
	
	/**
	 * This is made public to speed access to the prim2D elements.
	 * Ideally, it would be nicely hidden, but will add significant
	 * cost when drawing objects with many primatives.
	 */
	public Prim2DList prim2DList = new Prim2DList();
	
	protected Sketch sketch;
	
	//	 TODO: Feature 2D should not take in model?!?! only in param set?!?!
	public Feature2D(Sketch sketch, ToolModel2D toolMod, ParamSet paramSet){
		this.sketch = sketch;
		this.paramSet  = paramSet;
	}
	
	public Sketch getParentSketch(){
		return this.sketch;
	}
	
	public void buildPrim2DList(){
		if(paramSet != null){
			ToolModel2D toolModel2D = paramSet.getToolModel2D();
			if(toolModel2D != null){
				prim2DList = toolModel2D.buildPrim2DList(paramSet);
			}
		}
	}
	
	public ToolModel2D getToolInterface2D(){
		if(paramSet != null){
			return paramSet.getToolModel2D();
		}		
		return null;
	}
	
}
