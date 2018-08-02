package org.poikilos.librecsg.backend.model.sketch;

import java.util.LinkedList;

import org.poikilos.librecsg.backend.adt.Point2D;
import org.poikilos.librecsg.backend.geometry.Geometry2D;



//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the
//GNU General Public License (GPL).

//This file is part of avoCADo.

//AvoCADo is free software; you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation; either version 2 of the License, or
//(at your option) any later version.

//AvoCADo is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

//You should have received a copy of the GNU General Public License
//along with AvoCADo; if not, write to the Free Software
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA


/*
 * @author  Adam Kumpf
 * @created Feb. 2007
 */
public class Region2DList extends LinkedList<Region2D>{

	private static final long serialVersionUID = 1000L;

	/**
	 * build 2D regions from all of the Prim2D
	 * found in this sketch.  This is a somewhat
	 * expensive operation and should be done
	 * minimally. (e.g., switching to 2D->3D mode
	 * before extruding/revolving/etc).
	 */
	public void buildRegionsFromPrim2D(Prim2DList allPrims){
		this.clear(); // start with a fresh list.

		//
		// STEP INVOLVED TO GET 2D REGIONS! :)
		//
		// 1. find intersections of all prim2D and split them so that none overlap (except at endpoints).
		// 2. only keep unique Prim2D.
		// 3. get rid of all prim2D that do not connect to others (or theirself) at both ends.
		//      these are referred to as "dangling" prim2D.
		//
		// 4. start from each prim2D still remaining, and walk down connecting prim2D, always taking
		//      the left-most turn possible when the elements branch.
		// 5. check to see what the total angle was for the cycle (is it clockwise?).
		// 6. if the cycle was clockwise (!isCCW) then keep it and consume elements in that direction.
		//
		// 7. perform steps 4,5,6 again but starting at the opposite end of the initial prim2D.
		//
		// 8. there is a chance for duplicate cycles, but in opposite direction.. make sure these
		//      duplicates are removed.
		//
		// 9. all cycles are of minimal area and unique!... EXCEPT, fully enclosed regions.
		//     (e.g., a circle within a circle)
		//    check to see if every point of each region is within any other region.
		// 10. cut the larger region to go around the inner region.
		//
		// 11. repeat steps 9,10 until there are no more cuts to be made.
		//

		System.out.println("Region2DList(buildRegionsFromPrim2D): Building 2D regions from the sketch...");


		// STEP 1:
		// Find Intersections Between Prims and keep running until
		// no more intersections are found.  this will give a list
		// of Prim2D that intersect only at endpoints.
		//
		//  TODO: This step is ridiculous! (generates TONS of prims)
		//System.out.println("Initial Prims in list: " + allPrims.size());
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
							if(allPrims.size() < maxPrimSize){ // TODO: HACK, just stop it.. out of control
								PrimPair2D iPair = prim_B.splitPrimAtPoint(iPt);
								if(iPair != null){
									foundIntersection = true;
									allPrims.add(iPair.primA);
									allPrims.add(iPair.primB);
									allPrims.remove(prim_B);
								}
								//System.out.println("Added little prims! size:" + allPrims.size());
							}
						}
					}
				}
			}
		}
		if(allPrims.size() >= maxPrimSize){
			System.out.println("Region2DList(buildRegionsFromPrim2D): *** Hit max prim list size!! size=" + allPrims.size());
		}

		// STEP 2:
		// unique-ify the prims (check for duplicates and discard if found)
		for(int i=allPrims.size()-1; i>=0; i--){
			boolean foundMatch = false;
			Prim2D testPrim = allPrims.get(i);
			for(Prim2D prim : allPrims){
				if(!prim.equals(testPrim) &&
						prim.hasPtGetOther(testPrim.ptA) != null &&
						prim.hasPtGetOther(testPrim.ptB) != null &&
						prim.getCenterPtAlongPrim().equalsPt(testPrim.getCenterPtAlongPrim())){
					foundMatch = true;
					break;
				}
			}
			if(foundMatch){
				allPrims.remove(i);
			}
		}

		//System.out.println("Total Prims now in list: " + allPrims.size());


		// STEP 3:
		// prune elements that don't connect to another at both ends.
		// there could be a string of element wandering off in space, so
		// this needs to be done iteratively until there are no more
		// dangling elements.
		//
		System.out.println("Region2DList(buildRegionsFromPrim2D): Pruning dangling elements...");
		Prim2DList prunedPrims = new Prim2DList();
		for(Prim2D prim : allPrims){ // just to be safe, we'll make our own copy of "allPrims"
			prunedPrims.add(prim);
		}
		// iteratively prune off dangling prims until there are no more!
		int lastPrimListSize = Integer.MAX_VALUE;
		while(lastPrimListSize > prunedPrims.size()){
			//System.out.println("iterating... prunedPrims.size=" + prunedPrims.size());
			lastPrimListSize = prunedPrims.size();
			Prim2DList tempList = new Prim2DList();
			for(Prim2D prim : prunedPrims){
				if(prim.ptA.equalsPt(prim.ptB)){
					// self connecting primitive (e.g., circle)
					tempList.add(prim);
				}else{
					boolean conA = false;
					boolean conB = false;
					for(Prim2D primB : prunedPrims){
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
						tempList.add(prim);
					}
				}
			}
			prunedPrims = tempList;
		}

		//
		// No more dangling prims! :)
		//  --> all prim2D are now connected to another/itself at both ends.
		//

		// STEPS 4,5,6,7 (mainly bundled into the method "smartSearchForValidCycle()"
		// Start at each prim and check for valid cycles!
		//
		for(Prim2D prim : prunedPrims){
			//System.out.println("starting from: " + prim);
			if(!prim.consumedAB){
				smartSearchForValidCycle(prim, prunedPrims, prim);
			}
			if(!prim.consumedBA){
				smartSearchForValidCycle(prim.getSwappedEndPtPrim2D(), prunedPrims, prim);
			}

		}
		System.out.println("Total Cycles Found: " + this.size());


		// STEP 8
		// remove any duplicate cycles (same elements but different starting point/direction).
		System.out.println("Searching for region duplicates...");
		for(int i=this.size()-1; i>= 0; i--){
			Region2D regA = this.get(i);
			boolean duplicateFound = false;
			for(Region2D regB : this){
				if(regA != regB && regA.hasSamePointsAsRegion(regB)){
					duplicateFound = true;
					break;
				}
			}
			if(duplicateFound){
				this.remove(i);
				System.out.println("duplicate found; doing a good thing --> removing");
			}
		}

		// STEP 9,10,11
		// cut out inner 2D regions (holes)!
		//
		System.out.println("Cutting out inner regions from larger regions...");
		LinkedList<LinkedList<Integer>> regionsContained = new LinkedList<LinkedList<Integer>>();

		// find regions contained within regions.
		//int k = 0;
		for(Region2D regA : this){
			LinkedList<Integer> llRegionIndexes = new LinkedList<Integer>();
			for(int i=0; i<this.size(); i++){
				Region2D regB = this.get(i);
				if(regA != regB && !regA.regionHasBeenCut(regB) && regA.containsRegion(regB)){
					llRegionIndexes.add(i);
				}
			}
			regionsContained.add(llRegionIndexes);
			//k++;
		}
		// show the contain info for debug.
		for(int j=0; j<this.size(); j++){
			System.out.println("contained: [" + j + "]=" + regionsContained.get(j).size() );
		}

		//
		// there is some serious thought in this, so think carefully before you go messing with it! :)
		//
		for(int i=0; i<this.size(); i++){
			Region2D outerRegion = this.get(i);
			LinkedList<Integer> myConRegions = regionsContained.get(i);
			LinkedList<Integer> forbiddenRegionsToSubtract = new LinkedList<Integer>();
			if(myConRegions.size() == 0){
				continue; // no contained region, move on to the next one.
			}
			for(int j=this.size(); j>=0; j--){ // loop from most contained regions to least
				for(Integer conIndex : myConRegions){
					int numSubContainedRegions = regionsContained.get(conIndex).size();
					if(numSubContainedRegions == j && !forbiddenRegionsToSubtract.contains(conIndex)){
						outerRegion.cutRegionFromRegion(this.get(conIndex));
						for(Integer subConIndex : regionsContained.get(conIndex)){
							forbiddenRegionsToSubtract.add(subConIndex);
						}
					}
				}
			}
		}

		// just display the region info for debug.
		for(int j=0; j<this.size(); j++){
			System.out.println("region: [" + j + "]=" + this.get(j));
		}
	}


	private void smartSearchForValidCycle(Prim2D startPrim, Prim2DList prunedPrims, Prim2D extraCheck){
		Prim2DCycle newCycle = new Prim2DCycle();
		newCycle.add(startPrim);
		Point2D conPt = startPrim.ptB;
		while(!newCycle.isValidCycle()){
			Prim2D nextPrim = null;
			double maxAngle = -180.0;
			for(Prim2D primB : prunedPrims){
				if(!newCycle.contains(primB) && primB != extraCheck){
					Point2D otherPoint = primB.hasPtGetOther(conPt);
					if(otherPoint != null){
						if(	(primB.ptA.equalsPt(conPt) && !primB.consumedAB) ||
								(primB.ptB.equalsPt(conPt) && !primB.consumedBA)	){
							// prim2D is not consumed in the given direction and
							// does not occur in the list so far. :)

							// check 3-point-angle,
							// if this is the largest angle, keep it!
							double angle = Geometry2D.threePtAngle(	newCycle.getLast().getCenterPtAlongPrim(),
									conPt,
									primB.getCenterPtAlongPrim());
							//System.out.println(" --> angle="  +angle);
							if(angle > maxAngle){
								maxAngle = angle;
								nextPrim = primB;
							}
						}
					}
				}
			}
			if(nextPrim != null){
				if(!nextPrim.hasPtGetOther(conPt).equalsPt(newCycle.getFirst().ptA) &&
						newCycle.containsPt(nextPrim.hasPtGetOther(conPt))){
					// conPt would intersect the cycle somewhere not at the end! discard it!
					break;
				}else{
					newCycle.add(nextPrim);
					conPt = nextPrim.hasPtGetOther(conPt);
				}
			}else{
				// nothing connects?!
				break;
			}
		}
		if(newCycle.isValidCycle() && !newCycle.isCCW()){
			newCycle.consumeCycleDirection();
			this.add(new Region2D(newCycle));
			// System.out.println("********* Added cycle!");
		}else{
			//System.out.println("didn't add cycle: isValid=" + newCycle.isValidCycle() + ", isCCW=" + newCycle.isCCW());
		}
	}
}
