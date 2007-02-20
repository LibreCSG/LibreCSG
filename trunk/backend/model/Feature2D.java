package backend.model;

import ui.tools.ToolInterface2D;
import backend.adt.ParamSet;
import backend.primatives.Prim2DList;


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

	protected ToolInterface2D toolInt2D; 
	public boolean		      isSelected = true;
	public ParamSet paramSet = null;
	
	/**
	 * This is made public to speed access to the prim2D elements.
	 * Ideally, it would be nicely hidden, but will add significant
	 * cost when drawing objects with many primatives.
	 */
	public Prim2DList prim2DList = new Prim2DList();
	
	public Feature2D(ToolInterface2D toolInt, ParamSet paramSet){
		this.toolInt2D = toolInt;
		this.paramSet  = paramSet;
	}
	
	public void buildPrim2DList(){
		if(toolInt2D != null && paramSet != null){
			prim2DList = this.toolInt2D.buildPrimList(paramSet);
		}
	}
	
	public ToolInterface2D getToolInterface2D(){
		return toolInt2D;
	}
	
	public ParamSet getParamSet(){
		return paramSet;
	}
}
