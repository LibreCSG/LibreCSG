package backend.model;

import java.util.LinkedList;
import java.util.List;

import ui.tools.ToolInterface2D3D;
import backend.adt.ParamSet;
import backend.global.AvoGlobal;


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
	
	public Feature2D3D(Part part, ToolInterface2D3D toolInt2D3D, ParamSet paramSet){
		this.part = part;
		this.paramSet    = paramSet;
		this.toolInt2D3D = toolInt2D3D;		
	}	
	
	public Part getParentPart(){
		return this.part;
	}
	
	/**
	 * add a Sketch fot the end of the list of Sketches.
	 * @param sketch non-null Sketch to be added
	 * @return the index of the newly added Sketch, or -1 if Sketch was null.
	 */
	public int add(Sketch sketch){
		if(sketch != null){
			sketchList.add(sketch);
			AvoGlobal.modelEventHandler.notifyElementAdded();
			return sketchList.size()-1;
		}
		return -1;
	}
	
	/**
	 * get the Sketch at a given index.
	 * @param i index
	 * @return the Sketch, or null if the index was invalid.
	 */
	public Sketch getAtIndex(int i){
		if(i < 0 || i >= sketchList.size()){
			// index was not valid!
			return null;
		}
		return sketchList.get(i);
	}
	
	/**
	 * @return the size() of the list of Sketches.
	 */
	public int getSketchListSize(){
		return sketchList.size();
	}
	
	
	/**
	 * set the index of the sketch that should be set to Active.
	 * @param i index
	 */
	public void setActiveSketch(int i){
		if(i < 0 || i >= sketchList.size()){
			// index is not valid!
			return;
		}
		activeSketch = i;
		AvoGlobal.modelEventHandler.notifyActiveElementChanged();
	}
	
	/**
	 * set the active sketch to none
	 */
	public void setActiveToNone(){
		activeSketch = -1;
		AvoGlobal.modelEventHandler.notifyActiveElementChanged();
	}
	
	/**
	 * get the currently active sketch
	 * @return the active sketch, or null if no sketch is active
	 */
	public Sketch getActiveSketch(){
		return this.getAtIndex(activeSketch);
	}
	
	
	/**
	 * Remove the Sketch at the index if present.
	 * @param i index
	 */
	public void removeSketchAtIndex(int i){
		if(i < 0 || i >= sketchList.size()){
			// index is not valid!
			return;
		}
		sketchList.remove(i);
		AvoGlobal.modelEventHandler.notifyElementRemoved();
	}
	
	/**
	 * remove the active Sketch from the list.
	 */
	public void removeActiveSketch(){
		removeSketchAtIndex(activeSketch);
	}



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
