package backend.model;

import net.sf.avocado_cad.model.api.ISketch;
import net.sf.avocado_cad.model.api.adt.IParamSet;
import net.sf.avocado_cad.model.api.sketch.IFeature2D;
import net.sf.avocado_cad.model.api.sketch.IPrim2DList;
import backend.adt.ParamSet;
import backend.model.sketch.Prim2D;
import backend.model.sketch.Prim2DList;


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

/**
 * model componet that represents "Sketch" tools
 */
public class Feature2D implements IFeature2D {

	private boolean		      isSelected = true;
	private ParamSet           paramSet   = null;
	
	/**
	 * This is made public to speed access to the prim2D elements.
	 * Ideally, it would be nicely hidden, but will add significant
	 * cost when drawing objects with many primatives.
	 */
	public Prim2DList prim2DList = new Prim2DList();
	
	protected ISketch sketch;
	
	public Feature2D(ISketch sketch, ParamSet paramSet){
		this.sketch = sketch;
		this.paramSet  = paramSet;
	}

	public IParamSet getParamSet() {
		return this.paramSet;
	}

	public ISketch getParentSketch(){
		return this.sketch;
	}
	
	public void setSelected(boolean isSelected){
		this.isSelected = isSelected;
		for(Prim2D prim : prim2DList){
			prim.isSelected = isSelected;
		}
	}
	
	public boolean isSelected(){
		return this.isSelected;
	}

	public Prim2DList getPrim2DList() {
		return prim2DList;
	}

	public void setPrim2DList(IPrim2DList prim2DList) {
		this.prim2DList = (Prim2DList)prim2DList;
	}
	
//	public void buildPrim2DList(){
//		if(paramSet != null){
//			ToolModelSketch toolModel2D = paramSet.getToolModel2D();
//			if(toolModel2D != null){
//				prim2DList = toolModel2D.buildPrim2DList(paramSet);
//			}
//			setSelected(this.isSelected);
//		}
//	}
	
	
}
