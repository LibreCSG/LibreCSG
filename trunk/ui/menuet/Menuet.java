package ui.menuet;

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Composite;

import ui.tools.ToolView;
import ui.utilities.ColorUtils;
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
public class Menuet extends Composite{

	public static final int MENUET_WIDTH =  65;
	
	/**
	 * Tool mode identifiers 
	 * (used as index in Menuet's LinkedList, so keep them sequential)
	 */
	public static final int MENUET_MODE_MAIN   = 0;
	public static final int MENUET_MODE_2D     = 1;
	public static final int MENUET_MODE_2Dto3D = 2;
	public static final int MENUET_MODE_3D     = 3;	
	public static final int MENUET_TOTAL_MODES = 4; // always the highest mode number + 1
	
	/**
	 * Current tool mode being used (2D, 2Dto3D, 3D, etc.) 
	 * This determines which mode the menuet displays.
	 */
	protected int  currentToolMode = MENUET_MODE_MAIN;	
	
	/**
	 * The current tool being used.  this is much more than
	 * just the pretty User-Interface component.. this provides
	 * the actual functionality of the tool that is currently
	 * selected.
	 */
	public ToolView currentTool;
	
	
	/**
	 * Array of LinkedLists, each containing all of 
	 * the button/label/etc. for a particular mode.
	 */
	LinkedList <MenuetElement>menuetElements[] = new LinkedList[MENUET_TOTAL_MODES];

	/**
	 * Construct a new <b>menuet</b>: Mode based menu system
	 */
	public Menuet(Composite c, int type){
		super(c,type);
		
		// initialize LinkedList array
		for(int i=0; i<MENUET_TOTAL_MODES; i++){
			menuetElements[i] = new LinkedList<MenuetElement>();
		}
		
		this.addControlListener(new ControlListener(){
			public void controlMoved(ControlEvent e) {			
			}
			public void controlResized(ControlEvent e) {
				// position menuet elements appropriately
				respositionMenuetElements(Menuet.this.getBounds().height,Menuet.this.getBounds().width);		
			}			
		});
		
	}	
	
	/**
	 * set the current tool mode
	 * @param toolMode must be in the range of 0 - (MENUET_TOTAL_MODES-1)
	 */
	public void setCurrentToolMode(int toolMode){
		if(toolMode < 0 || toolMode >= MENUET_TOTAL_MODES){
			// invalid toolMode
			System.out.println("invalid tool mode specified.. ignoring setCurrentToolMode call");
			return;
		}
		currentToolMode = toolMode;
	}
	
	/**
	 * @return the current toolMode which is an int between 0 - (MENUET_TOTAL_MODES-1).
	 */
	public int getCurrentToolMode(){
		int toolModeCopy = currentToolMode;
		return toolModeCopy;
	}
	
	/**
	 * Let the menuet know about the addition of a new
	 * element.  This is necessary so that the menuet 
	 * can manage layout and sizing of the elements.
	 * @param mElement
	 */
	public void addMenuetElement(MenuetElement mElement, int mode){
		//  store/manage MenuetElements.
		if(mode<0 || mode >= MENUET_TOTAL_MODES){
			System.out.println("Attempted to add menuet item of invalid mode. Aborted!");
			System.out.println("  --> Label:" + mElement.meLabel + " mode:" + mode);
		}else{
			menuetElements[mode].add(mElement);
			//System.out.println("Added " + mElement.meLabel + " to mode " + mode);
		}
	}
	
	public void updateToolModeDisplayed(){
		respositionMenuetElements(Menuet.this.getBounds().height,Menuet.this.getBounds().width);
	}
	
	public void disableAllTools(){
		for(int i=0; i<MENUET_TOTAL_MODES; i++){
			Iterator iter = menuetElements[i].iterator();
			while(iter.hasNext()){
				MenuetElement mElement = (MenuetElement)iter.next();
				mElement.setShown(false);
				mElement.setVisible(false);
				mElement.isSelected = false;
			}
		}
	}
	
	/**
	 * de-select all buttons in the current mode except
	 * for the element passed in to <em>selectButton</em>.
	 * @param me
	 */
	public void selectButton(MenuetElement me){
		Iterator iter = menuetElements[currentToolMode].iterator();
		while(iter.hasNext()){
			MenuetElement mElement = (MenuetElement)iter.next();
			if(mElement.equals(me)){
				mElement.isSelected = true;
				mElement.redraw();
			}else{
				mElement.isSelected = false;
				mElement.redraw();
			}
		}
		this.redraw();
	}	
	
	void respositionMenuetElements(int totalHeight, int totalWidth){
		// Step 1: calculate height of MenuetElements, hiding low priority elements if needed
		// Step 2: place in appropriate location according to alignment		
		
		// System.out.println("repositioning elements...");
		
		int mode = currentToolMode;
		
		int totalMinHeight = 0;
		Iterator iter = menuetElements[mode].iterator();
		while(iter.hasNext()){ // get height of all elements
			MenuetElement mElement = (MenuetElement)iter.next();
			totalMinHeight += mElement.getMinDisplayHeight(totalWidth);
			if(mElement.mePriority > 5){
				mElement.setShown(false);
				mElement.setVisible(false);
			}else{
				mElement.setShown(true);
				mElement.setVisible(true);
			}
		}	

		int priority = 5;
		int visibleElements = menuetElements[mode].size();
				
		// check to see if items fit...
		// take away items until they can (in order of priority)		
		while(totalMinHeight > totalHeight && priority >= 0 ){
			// trim off items until they fit
			iter = menuetElements[mode].iterator();
			while(iter.hasNext()){ 
				MenuetElement mElement = (MenuetElement)iter.next();
				if(mElement.meIsShown() && mElement.mePriority > priority){
					mElement.setShown(false);
					totalMinHeight -= mElement.getMinDisplayHeight(totalWidth);
					visibleElements--;
				}
				if(totalMinHeight <= totalHeight){
					break; 	// no need to remove all items of a certain priority
							// if only a few need to go.
				}
			}
			priority--;
		}
		
		
		
		// the correct elements to be displayed are now selected...
		// now they need to be resized to fill the space as much as possible
		//
		// Three iterations.. 
		// 1. linearly fill in space, noting constraints  
		// 2. redistribute remaining space
		// 3. redistribute again to reduce jumpiness
		
		
		int freePixels = totalHeight - totalMinHeight;
		float unusedPixels = 0.0f;
		int constrainedElements = 0; // height of element is pinned to a min/max value
		// iteration (1)
		iter = menuetElements[mode].iterator();
		while(iter.hasNext()){ 
			MenuetElement mElement = (MenuetElement)iter.next();
			if(mElement.meIsShown()){
				float newHeightF = (float)mElement.getMinDisplayHeight(totalWidth) + (float)freePixels/(float)visibleElements;
				int newHeight = (int)Math.floor(newHeightF);
				int newHeightB = Math.max(mElement.getMinDisplayHeight(totalWidth), Math.min(newHeight, mElement.mePreferredHeight));
				mElement.setBounds(0,0,0,newHeightB);
				unusedPixels += newHeightF - (float)newHeightB;
				if(newHeightB == mElement.mePreferredHeight){
					constrainedElements++;
				}
			}
		}		
		
		int unconstrainedElements = visibleElements - constrainedElements;
		constrainedElements = 0;
		float unusedPixels2 = 0.0f;
		// iteration (2)
		if(unusedPixels >= 1.0f && unconstrainedElements > 0){
			iter = menuetElements[mode].iterator();
			while(iter.hasNext()){ 
				MenuetElement mElement = (MenuetElement)iter.next();
				if(mElement.meIsShown() && mElement.getBounds().height != mElement.mePreferredHeight){
					float newHeightF = (float)mElement.getBounds().height + (float)unusedPixels/(float)unconstrainedElements;
					int newHeight = (int)Math.floor(newHeightF);
					int newHeightB = Math.max(mElement.getMinDisplayHeight(totalWidth), Math.min(newHeight, mElement.mePreferredHeight));
					mElement.setBounds(0,0,0,newHeightB);
					unusedPixels2 += newHeight - (float)newHeightB;
					if(newHeightB == mElement.mePreferredHeight){
						constrainedElements++;
					}					
				}
			}				
		}
		
		unconstrainedElements -= constrainedElements;
		// iteration (3)
		if(unusedPixels2 >= 1.0f && unconstrainedElements > 0){
			iter = menuetElements[mode].iterator();
			while(iter.hasNext()){ 
				MenuetElement mElement = (MenuetElement)iter.next();
				if(mElement.meIsShown() && mElement.getBounds().height != mElement.mePreferredHeight){
					float newHeightF = (float)mElement.getBounds().height + (float)unusedPixels2/(float)unconstrainedElements;
					int newHeight = (int)Math.floor(newHeightF);
					int newHeightB = Math.max(mElement.getMinDisplayHeight(totalWidth), Math.min(newHeight, mElement.mePreferredHeight));
					mElement.setBounds(0,0,0,newHeightB);				
				}
			}				
		}	

		// each MenuetElement is now appropriately sized and hidden/shown
		
		//
		// BACKGROUND
		//
		this.setBackground(ColorUtils.getModeBGColor());		
		
		//
		// TOP
		//
		int heightMarker = 0;		
		iter = menuetElements[mode].iterator();
		while(iter.hasNext()){ // place TOP
			MenuetElement mElement = (MenuetElement)iter.next();
			if(mElement.meIsShown() && mElement.meIsAlignedTop()){
				mElement.setBounds(0, heightMarker, totalWidth, mElement.getBounds().height);
				heightMarker += mElement.getBounds().height;
			}
		}		
		
		
		// TODO: Allow for MIDDLE aligned MenuetElements
		
		//
		// BOTTOM
		//
		int bottomSize = 0;
		iter = menuetElements[mode].iterator();
		while(iter.hasNext()){ // calculate BOTTOM
			MenuetElement mElement = (MenuetElement)iter.next();
			if(mElement.meIsShown() && mElement.meIsAlignedBottom()){
				bottomSize += mElement.getBounds().height;	
			}
		}
		heightMarker = totalHeight-bottomSize;
		iter = menuetElements[mode].iterator();
		while(iter.hasNext()){ // place BOTTOM
			MenuetElement mElement = (MenuetElement)iter.next();
			if(mElement.meIsShown() && mElement.meIsAlignedBottom()){				
				mElement.setBounds(0, heightMarker, totalWidth, mElement.getBounds().height);	
				heightMarker += mElement.getBounds().height;
			}
		}		
		
		
	}
	
	
}
