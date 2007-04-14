package backend.model.ref;

import backend.model.Feature3D3D;
import backend.model.Part;


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
* @created Apr. 2007
*/

public class ModRef_Modify extends ModelReference{

	private final int uniqueSubPartID;
	
	public ModRef_Modify(int uniqueSubPartID){
		super(ModRefType.Modify);
		this.uniqueSubPartID = uniqueSubPartID;
	}
	
	@Override
	public String getStringReferenceInfo() {
		return "SubPartID:" + uniqueSubPartID;
	}
	
	public Feature3D3D getModify(Part part){
		return part.getFeat3D3DByID(uniqueSubPartID);
	}
	

}
