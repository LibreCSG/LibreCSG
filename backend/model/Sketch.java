package backend.model;

import java.util.LinkedList;

import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.Parameterized;
import backend.adt.Point2D;
import backend.adt.Point3D;
import backend.adt.Rotation3D;
import backend.global.AvoGlobal;
import backend.primatives.Prim2D;
import backend.primatives.PrimPair2D;


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
public class Sketch extends Parameterized{	
	
	/**
	 * List of all the Feature2Ds contained in the sketch
	 */
	private LinkedList<Feature2D> feat2DList = new LinkedList<Feature2D>();
	
	protected int activeFeat2D = -1;
	
	public Sketch(){
		paramSet = new ParamSet("Sketch");
		paramSet.addParam("o", new Param("Offset", new Point3D(0.0, 0.0, 0.0)));
		paramSet.addParam("r", new Param("Rotation", new Rotation3D(0.0, 0.0, 0.0)));
		paramSet.label = "Sketch";
	}
	
	/**
	 * add a Feature2D to the end of the list of Feature2Ds
	 * @param f2D non-null Feature2D to be added
	 * @return the index of the newly added Feature2D, or -1 if Feature2D was null.
	 */
	public int add(Feature2D f2D){
		if(f2D != null){
			feat2DList.add(f2D);
			AvoGlobal.modelEventHAndler.notifyElementAdded();
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
	 * set the index of the Feature2D that should be set to Active.
	 * @param i index
	 */
	public void setActiveFeat2D(int i){
		if(i < 0 || i >= feat2DList.size()){
			// index is not valid!
			return;
		}		
		activeFeat2D = i;
		AvoGlobal.modelEventHAndler.notifyActiveElementChanged();
	}
	
	/**
	 * set the active Feature2D to none
	 */
	public void setActiveToNone(){		
		activeFeat2D = -1;
		AvoGlobal.modelEventHAndler.notifyActiveElementChanged();
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
		if(i < 0 || i >= feat2DList.size()){
			// index is not valid!
			return;
		}
		feat2DList.remove(i);
		AvoGlobal.modelEventHAndler.notifyElementRemoved();
	}
	
	/**
	 * remove the active Feature2D from the list.
	 */
	public void removeActiveFeat2D(){
		removeFeat2DAtIndex(activeFeat2D);
	}
	
	
	public void buildRegionsFromPrim2D(){
		//
		// Put all Prim2D into one big list.
		//
		LinkedList<Prim2D> allPrims = new LinkedList<Prim2D>();
		for(int i=0; i < this.getFeat2DListSize(); i++){
			Feature2D f2D_A = this.getAtIndex(i);
			for(Prim2D prim : f2D_A.prim2DList){
				allPrims.add(prim);
			}
		}
		
		//
		// find intersections between prims and keep running until
		// no more intersections are found.  this will give a list
		// of Prim2D that intersect only at endpoints.
		//
		boolean foundIntersection = true;
		int maxPrimSize = 1000; // TODO: HACK just for debug
		while(foundIntersection){
			foundIntersection = false;
			for(int i=0; i < allPrims.size(); i++){
				Prim2D prim_A = allPrims.get(i);
				for(int j=0; j < allPrims.size(); j++){
					Prim2D prim_B = allPrims.get(j);
					if(!prim_A.equals(prim_B)){
						// comparing two different Prim2Ds.
						// check for intersection...
						Point2D iPt = prim_A.intersect(prim_B);
						if(iPt != null){								
							if(allPrims.size() < maxPrimSize){ // HACK, just stop it.. out of control
								foundIntersection = true;
								PrimPair2D iPair = prim_B.splitPrimAtPoint(iPt);
								allPrims.add(iPair.primA);
								allPrims.add(iPair.primB);
								allPrims.remove(prim_B);
								//System.out.println("Added little prims! size:" + allPrims.size());
							}
							
						}							
					}
				}
			}
		}
		
		//
		// prune elements that don't connect to another at both ends.
		// this isn't a requirement for the next step, but since the
		// depth-first search is pretty intense, this can substantially
		// improve it by cutting out a large number of iterations in
		// some cases.
		//
		LinkedList<Prim2D> prunedPrims = new LinkedList<Prim2D>();
		for(Prim2D prim : allPrims){
			boolean conA = false;
			boolean conB = false;
			for(Prim2D primB : allPrims){
				if(!prim.equals(primB)){
					if(prim.ptA.equalsPt(primB.ptA) || prim.ptA.equalsPt(primB.ptB)){
						conA = true;
					}
					if(prim.ptB.equalsPt(primB.ptA) || prim.ptB.equalsPt(primB.ptB)){
						conB = true;
					}
				}						
			}				
			if(conA && conB){
				prunedPrims.add(prim);
				//System.out.println("added pruned prim! size:" + prunedPrims.size());
			}
		}
	
		//for(Prim2D prim : prunedPrims){
		//	System.out.println("  -PP-> " + prim.ptA + " :: " +  prim.ptB);
		//}
		
		// complete cycles.  
		LinkedList<LinkedList<Prim2D>> allCycles = new LinkedList<LinkedList<Prim2D>>();
		
		//
		// find cycles... Recursion-free depth first search
		//
		for(Prim2D prim : prunedPrims){
			RFDFSearch(allCycles, prunedPrims, prim);
		}
					
		System.out.println("Total Cycles found: " + allCycles.size());
		//for(LinkedList<Prim2D> llP2D : allCycles){
		//	System.out.println("CYCLE:");
		//	for(Prim2D prim : llP2D){
		//		System.out.println("  --> " + prim.ptA + " :: " +  prim.ptB);
		//	}
		//}
		
	}
	
	
	LinkedList<Prim2D> copyLL(LinkedList<Prim2D> origList){
		LinkedList<Prim2D> newList = new LinkedList<Prim2D>();
		for(Prim2D prim : origList){
			newList.add(prim);
		}
		return newList;
	}
	
	void RFDFSearch(LinkedList<LinkedList<Prim2D>> allCycles, LinkedList<Prim2D> allPrims, Prim2D primStart){
		Point2D endPt = primStart.ptA; // end point (where the cycles should eventually end)
		Point2D conPt = primStart.ptB; // connection point (where next prim2D must connect)
		
		// all of the prim2D used so far at a given level
		LinkedList<LinkedList<Prim2D>> usedAtLevel = new LinkedList<LinkedList<Prim2D>>();
		int level = 1;
		
		// the path taken so far
		LinkedList<Prim2D> pathSoFar = new LinkedList<Prim2D>();
		pathSoFar.add(primStart); // path always starts with primStart.
		
		while(level > 0){
			//System.out.println("*     while");
			if(usedAtLevel.size() < level){
				// this is a new level; add a new list.
				usedAtLevel.add(new LinkedList<Prim2D>());
				//System.out.println("**    added new level:" + level);
			}
			for(int i=0; i<allPrims.size(); i++){
				Prim2D prim = allPrims.get(i);
				if(!pathSoFar.contains(prim) && !usedAtLevel.get(level-1).contains(prim)){
					// the prim is not in the current path and has not been used yet, try it.
					//System.out.println("***   considering new prim2D; i=" + i);
					
					// add it to the set of used prim2D so we don't check it again later.
					usedAtLevel.get(level-1).add(prim); 
					
					// check is conPt is an end point of the prim2D. if so, get the other point.
					Point2D nextPt = prim.hasPtGetOther(conPt);
					if(nextPt != null){
						pathSoFar.add(prim);
						if(nextPt.equalsPt(endPt)){
							// cycle has been completed. 
							// add it and continue checking others.
							allCycles.add(copyLL(pathSoFar)); // use a copy since pathSoFar will change!
							//System.out.println("****  added cycle");
							pathSoFar.removeLast();
						}else{
							// not the end yet. 
							// update the conPt and continue trying at the next level.
							conPt = nextPt;							
							level++;
							//System.out.println("****  going to next level; newLevel=" + level);
							break;
						}
					}
				}
				if(i == (allPrims.size()-1)){
					// just checked last Prim2D in the set.					
					level--;
					//System.out.println("***** going down a level; newLevel=" + level);
					conPt = pathSoFar.getLast().hasPtGetOther(conPt);
					pathSoFar.removeLast();
					usedAtLevel.removeLast();
				}
				
			}
			
		}
		
	}
	
}
