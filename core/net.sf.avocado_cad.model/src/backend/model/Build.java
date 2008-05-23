package backend.model;

import net.sf.avocado_cad.model.api.IBuild;
import net.sf.avocado_cad.model.api.IPart;
import net.sf.avocado_cad.model.api.ISubPart;
import backend.adt.ParamSet;



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
 * model componet that represents "Build" tools.
 */
public class Build implements SubPart, IBuild {
	
	public    ParamSet          paramSet    = null;

	// TODO: this should be a ModRef_Sketch!
	protected int primarySketchID;
	
	protected Part part;
	public final int ID;
	
	public Build(Part part, int primarySketchID, int ID){
		this.part = part;
		this.primarySketchID = primarySketchID;
		this.ID = ID;
		if(part.getSketchByID(primarySketchID) == null){
			System.out.println("Tried to create a BUILD on an invalid primarySketch ID!!! :(");
		}
	}	
	
	public IPart getParentPart(){
		return this.part;
	}
	
	public Sketch getPrimarySketch(){
		return part.getSketchByID(primarySketchID);
	}
	
	public int getUniqueID(){
		return ID;
	}
	
	//TODO: handle how sketches are linked to the Feature2D3D (sketch by number?)
	
	public Build getBuild() {
		return this;
	}

	public Modify getModify() {
		return null;
	}

	public Sketch getSketch() {
		return null;
	}

	public ParamSet getParamSet() {
		return paramSet;
	}

	public int getID() {
		return ID;
	}
	
}
