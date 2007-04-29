package backend.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import backend.global.AvoGlobal;
import backend.model.ref.ModRef_Plane;
import backend.model.sketch.Prim2D;
import backend.model.sketch.Prim2DList;
import backend.model.sketch.Region2D;
import backend.model.sketch.Region2DList;
import backend.model.sketch.SketchPlane;


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
 * model componet that represents the "Sketch" 
 * which contains a set of Feature2D.
 */
public class Sketch implements SubPart{	
	
	/**
	 * List of all the Feature2Ds contained in the sketch
	 */
	protected List<Feature2D> feat2DList = new LinkedList<Feature2D>();
	protected Region2DList    regionList = new Region2DList();
	
	private ModRef_Plane sketchPlaneRef;
	
	protected int activeFeat2D = -1;
	
	/**
	 * a consumed sketch is one that has been used to construct
	 * another feature and therefore cannot be modified directly
	 * without first supressing the feature.
	 */
	public boolean isConsumed = false;
	
	protected Part part;
	public final int ID;
	
	public Sketch(Part part, int ID, ModRef_Plane sketchPlaneRef){
		this.part = part;
		this.ID = ID;
		this.sketchPlaneRef = sketchPlaneRef;
	}
	
	public Iterator<Region2D> getRegion2DIterator(){
		return regionList.iterator();
	}
	
	/**
	 * @return the actual plane on which to sketch.
	 */
	public SketchPlane getSketchPlane(){
		return sketchPlaneRef.getSketchPlane(part);
	}
	
	/**
	 * @return a reference to the sketch plane.
	 */
	public ModRef_Plane getSketchPlaneRef(){
		return sketchPlaneRef;
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

	
	public Build getBuild() {
		return null;
	}

	public Modify getModify() {
		return null;
	}

	public Sketch getSketch() {
		return this;
	}
	
}
