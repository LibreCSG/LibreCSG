package backend.model.ref;

import backend.model.Group;
import backend.model.Project;


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
* @created Apr. 2007
*/

public class ModRef_Group extends ModelReference{

	private final int uniqueGroupID;
	
	public ModRef_Group(int uniqueGroupID){
		super(ModRefType.Group);
		this.uniqueGroupID = uniqueGroupID;
	}
	
	@Override
	public String getStringReferenceInfo() {
		return "GroupID:" + uniqueGroupID;
	}

	public Group getGroup(Project project){
		return project.getGroupByUniqueID(uniqueGroupID);
	}
	
}
