package backend.model;

import java.util.LinkedList;
import java.util.List;

import ui.tools.ToolInterface2D3D;
import backend.adt.ParamSet;


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
public class Feature2D3D implements SubPart{
	
	protected List<Sketch>      sketchList  = new LinkedList<Sketch>();
	public    ToolInterface2D3D toolInt2D3D = null;
	public    ParamSet          paramSet    = null;
	
	protected int activeSketch = -1;
	
	protected Part part;
	protected int ID;
	
	public Feature2D3D(Part part, ToolInterface2D3D toolInt2D3D, ParamSet paramSet, int ID){
		this.part = part;
		this.paramSet    = paramSet;
		this.toolInt2D3D = toolInt2D3D;		
		this.ID = ID;
	}	
	
	public Part getParentPart(){
		return this.part;
	}
	
	public int getID(){
		int newInt = ID;
		return newInt;
	}
	
	//TODO: handle how sketches are linked to the Feature2D3D (sketch by number?)
	
	public Feature2D3D getFeature2D3D() {
		return this;
	}

	public Feature3D3D getFeature3D3D() {
		return null;
	}

	public Sketch getSketch() {
		return null;
	}
	
	
}
