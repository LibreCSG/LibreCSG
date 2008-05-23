package ui.tools.sketch;

import net.sf.avocado_cad.eclipse.ui.AvoGlobal;
import net.sf.avocado_cad.model.api.adt.IParamSet;
import ui.tools.ToolModelSketch;
import backend.adt.ParamSet;
import backend.model.Sketch;
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
public class ToolSketchSelectModel implements ToolModelSketch{

	public Prim2DList buildPrim2DList(IParamSet paramSet) {
		return null;
	}

	public boolean paramSetIsValid(IParamSet paramSet) {
		return false;
	}

	public void updateDerivedParams(ParamSet paramSet) {
		// no derived params for this feature.
	}

	public void finalize(ParamSet paramSet) {
		Sketch sketch = (Sketch) AvoGlobal.project.getActiveSketch();
		if(sketch != null){
			sketch.deselectAllFeat2D();
		}
	}

	public IParamSet constructNewParamSet() {
		return null;
	}
	
	public boolean isWorthKeeping(IParamSet paramSet) {
		// TODO: check for isWorthKeeping! 
		return true;
	}

}
