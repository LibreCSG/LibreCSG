package ui.tools;

import backend.model.Build;
import backend.model.CSG.CSG_Face;


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
public interface ToolModelModify extends ToolModel{

	/**
	 * get the CSG_Face defined by this feature's unique faceID and
	 * the corresponding parameter set.
	 * @param pSet the ParamSet used to indicate how the feature should be constructed.
	 * @param faceID the unique ID of the face to retreive
	 * @return the CSG_Face specified by the faceID, or NULL if no face exists at that ID.
	 */
	abstract public CSG_Face getFaceByID(Build feat3D3D, int faceID);
	
}
