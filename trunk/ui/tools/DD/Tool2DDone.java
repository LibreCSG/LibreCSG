package ui.tools.DD;

import java.util.LinkedList;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.Tool2D;
import backend.adt.Point2D;
import backend.data.utilities.ImageUtils;
import backend.global.AvoGlobal;
import backend.model.Feature2D;
import backend.model.Sketch;
import backend.primatives.Prim2D;
import backend.primatives.Prim2DLine;
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
public class Tool2DDone extends Tool2D{
	
	public Tool2DDone(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode());
		mElement.mePreferredHeight = 100;
		mElement.meColorMouseOver  = AvoGlobal.COLOR_MENUET_DONE_MO;
		mElement.meColorUnselected = AvoGlobal.COLOR_MENUET_DONE_US; 
		mElement.meLabel = "Done";
		mElement.meIcon = ImageUtils.getIcon("menuet/Done.png", 24, 24);
		mElement.setToolTipText("Finish working in the 2D mode.");
		mElement.mePriority = 0; 	// 0 = always show element, >5 = never show element
		mElement.meDispOptions = MenuetElement.ME_TRY_ICON;
		
		this.applyToolGroupSettings();	// APPLY 2D GROUP SETTINGS
		
		toolInterface = new Tool2DDoneInt();
	}

	@Override
	public void toolSelected() {
		// TODO: Push all new items into main backend.model
		AvoGlobal.menuet.disableAllTools();
		AvoGlobal.menuet.setCurrentToolMode(Menuet.MENUET_MODE_MAIN);
		// TODO: deselect all 2D features
		AvoGlobal.paramDialog.setParamSet(null);
		AvoGlobal.menuet.currentTool = null;			
		AvoGlobal.menuet.updateToolModeDisplayed();
		AvoGlobal.glView.updateGLView = true;
		
		// TODO: HACK just to test line intersection code!
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null){
			
			LinkedList<Prim2D> allPrims = new LinkedList<Prim2D>();
			// Put all prims into one list.
			for(int i=0; i < sketch.getFeat2DListSize(); i++){
				Feature2D f2D_A = sketch.getAtIndex(i);
				for(Prim2D prim : f2D_A.prim2DList){
					allPrims.add(prim);
				}
			}

			// find intersections between prims and keep running until
			// no more intersections are found.
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
									System.out.println("Added little prims! size:" + allPrims.size());
								}
								
							}							
						}
					}
				}
			}
			
			// 
			// should now have a list of Prim2D that intersect only at endpoints
			//
			
			//
			// prune elements that don't connect to another at both ends.
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
					System.out.println("added pruned prim! size:" + prunedPrims.size());
				}
			}
			
			
			for(Prim2D prim : prunedPrims){
				System.out.println("  -PP-> " + prim.ptA + " :: " +  prim.ptB);
			}
			
			//TODO: somehow not getting all of the cycles...
			//
			// find cycles... depth first search
			//
			LinkedList<LinkedList> allCycles = new LinkedList<LinkedList>();
			for(Prim2D prim : prunedPrims){
				//
				// start a new cycle
				//
				LinkedList<Prim2D> primUsed = new LinkedList<Prim2D>();
				primUsed.add(prim); // first element is starting prim2D
				
				Point2D endPt = prim.ptB;
				Point2D conPt = prim.ptA; // the point that must connect next.
				
				//
				// stop condition: endPt equals starting point OR no more elements to add
				//
				boolean foundCon = true;
				while(foundCon){
					foundCon = false; // up to the for loop to prove this wrong...
					for(Prim2D primB : prunedPrims){
						// check to make sure element has not yet been used
						if(!primUsed.contains(primB)){
							if(conPt.equalsPt(primB.ptA)){
								foundCon = true;
								primUsed.add(primB);
								conPt = primB.ptB;
							}else{
								if(conPt.equalsPt(primB.ptB)){
									foundCon = true;
									primUsed.add(primB);
									conPt = primB.ptA;
								}
							}
						}
						if(conPt.equalsPt(endPt)){
							System.out.println("cycle complete..");
							// cycle is complete!
							allCycles.add(primUsed);
							foundCon = false;
							break;
						}
					}
				}				
			}
			
			System.out.println("Total Cycles found: " + allCycles.size());
			for(LinkedList<Prim2D> llP2D : allCycles){
				System.out.println("CYCLE:");
				for(Prim2D prim : llP2D){
					System.out.println("  --> " + prim.ptA + " :: " +  prim.ptB);
				}
			}
			
			
			
		}
		
	}
}
