package backend.model;

import java.util.LinkedList;
import java.util.List;

import ui.tools.main.ToolMain2DModel;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.Point3D;
import backend.adt.Rotation3D;
import backend.global.AvoGlobal;
import backend.model.sketch.Prim2D;
import backend.model.sketch.Prim2DList;
import backend.model.sketch.Region2D;
import backend.model.sketch.Region2DList;


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
public class Sketch implements SubPart{	
	
	/**
	 * List of all the Feature2Ds contained in the sketch
	 */
	protected List<Feature2D> feat2DList = new LinkedList<Feature2D>();
	protected Region2DList    regionList = new Region2DList();
	public ParamSet             paramSet = null;
	
	protected int activeFeat2D = -1;
	
	/**
	 * a consumed sketch is one that has been used to construct
	 * another feature and therefore cannot be modified directly
	 * without first supressing the feature.
	 */
	public boolean isConsumed = false;
	
	protected Part part;
	protected int ID;
	
	public Sketch(Part part, int ID){
		this.part = part;
		this.ID = ID;
		// TODO: need sketch tool interface -- stange way to do this? (sketch tool)? !!
		paramSet = new ParamSet("Sketch", new ToolMain2DModel());
		paramSet.addParam("o", new Param("Offset", new Point3D(0.0, 0.0, 0.0)));
		paramSet.addParam("r", new Param("Rotation", new Rotation3D(0.0, 0.0, 0.0)));
		paramSet.label = "Sketch";
	}
	
	public Part getParentPart(){
		return this.part;
	}
	
	public int getID(){
		int newInt = ID;
		return newInt;
	}
	
	/**
	 * add a Feature2D to the end of the list of Feature2Ds
	 * @param f2D non-null Feature2D to be added
	 * @return the index of the newly added Feature2D, or -1 if Feature2D was null.
	 */
	public int add(Feature2D f2D){
		if(isConsumed){
			System.out.println("Cannot Add Feature2D to sketch!  The Sketch is Consumed!");
			return -1;
		}
		if(f2D != null){
			feat2DList.add(f2D);
			AvoGlobal.modelEventHandler.notifyElementAdded();
			return feat2DList.size()-1;
		}
		return -1;
	}
	
	/**
	 * get the Feauture2D at a given index.
	 * @param i index
	 * @return the Feature, or null if index was invalid.
	 */
	public Feature2D getAtIndex(int i){
		if(i < 0 || i >= feat2DList.size()){
			// index is not valid!
			return null;
		}
		return feat2DList.get(i);
	}
	
	/**
	 * @return the size() of the list of Feature2Ds.
	 */
	public int getFeat2DListSize(){
		return feat2DList.size();
	}
	
	/**
	 * @return the size() of the list of Region2Ds.
	 */
	public int getRegion2DListSize(){
		return regionList.size();
	}
	
	/**
	 * get the Region2D at a given index.
	 * @param i index
	 * @return the Region, or null if index was invalid.
	 */
	public Region2D getRegAtIndex(int i){
		if(i < 0 || i >= regionList.size()){
			// index is not valid!
			return null;
		}
		return regionList.get(i);
	}
	
	/**
	 * set the index of the Feature2D that should be set to Active.
	 * @param i index
	 */
	public void setActiveFeat2D(int i){
		if(isConsumed){
			System.out.println("Cannot Set Active Feature2D in sketch!  it is Consumed!");
			return;
		}
		if(i < 0 || i >= feat2DList.size()){
			// index is not valid!
			return;
		}		
		activeFeat2D = i;
		AvoGlobal.modelEventHandler.notifyActiveElementChanged();
	}
	
	/**
	 * set the active Feature2D to none
	 */
	public void setActiveToNone(){		
		activeFeat2D = -1;
		AvoGlobal.modelEventHandler.notifyActiveElementChanged();
	}
	
	/**
	 * get the currently active Feature2D
	 * @return the active Feature2D, or null if no Feature2D is active
	 */
	public Feature2D getActiveFeat2D(){
		return this.getAtIndex(activeFeat2D);
	}
	
	
	/**
	 * Deselect all of the Feature2D components
	 * within the given sketch.
	 */
	public void deselectAllFeat2D(){
		if(feat2DList.size() > 0){
			for(Feature2D f2D : feat2DList){
				f2D.isSelected = false;
			}
		}
	}
	
	/**
	 * Remove the Feature2D at the index if present.
	 * @param i index
	 */
	public void removeFeat2DAtIndex(int i){
		if(isConsumed){
			System.out.println("Cannot Remove Feature2D from sketch!  it is Consumed!");
			return;
		}
		if(i < 0 || i >= feat2DList.size()){
			// index is not valid!
			return;
		}
		feat2DList.remove(i);
		AvoGlobal.modelEventHandler.notifyElementRemoved();
	}
	
	/**
	 * remove the active Feature2D from the list.
	 */
	public void removeActiveFeat2D(){
		removeFeat2DAtIndex(activeFeat2D);
	}
	
	
	public void buildRegions(){
		//
		// Put all Prim2D into one big list.
		//
		Prim2DList allPrims = new Prim2DList();
		for(int i=0; i < this.getFeat2DListSize(); i++){
			Feature2D f2D_A = this.getAtIndex(i);
			for(Prim2D prim : f2D_A.prim2DList){
				allPrims.add(prim);
			}
		}
		//
		// build the regions from the list of all Prim2D!
		//
		regionList.buildRegionsFromPrim2D(allPrims);
	}

	
	public Feature2D3D getFeature2D3D() {
		return null;
	}

	public Feature3D3D getFeature3D3D() {
		return null;
	}

	public Sketch getSketch() {
		return this;
	}
	
}
