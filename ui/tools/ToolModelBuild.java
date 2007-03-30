package ui.tools;

import javax.media.opengl.GL;

import backend.adt.ParamSet;
import backend.model.Feature2D3D;
import backend.model.CSG.BoolOp;
import backend.model.CSG.CSG_Solid;


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
public interface ToolModelBuild extends ToolModel{

	// TODO: should not actually do drawing! (I think?!?)
	abstract public void draw3DFeature(GL gl, Feature2D3D feat2D3D);
	
	/**
	 * Construct a CSG_Solid from the feature2D3D and return it.
	 * @param feat2D3D 
	 * @return the CSG_Solid (water-tight)
	 */
	abstract public CSG_Solid getBuiltSolid(Feature2D3D feat2D3D);
	
	/**
	 * @return the Boolean operation to peform on 
	 * the solid this tool creates.
	 */
	abstract public BoolOp getBooleanOperation(ParamSet pSet);
}
