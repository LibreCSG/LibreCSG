package ui.tools;

import ui.menuet.MenuetElement;
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

/**
 * The abstract Tool class represents a set of 
 * functionality capable of handling user-interaction
 * and appropriately performing a given action.
 * 
 * Classes should extend Tool to form groups of
 * related functionality. (e.g., Tool2D for 2D
 * drawing tools like lines, circles, curves, etc.)
 * 
 * The main functionality of a tool includes:
 *   - User interface element (MenuetElement)
 *   - Parameter definitions with good defaults
 *   - handling of tool-related events in the GLview
 *   - construction of elements via GL primatives
 */
public abstract class ToolView{

	public MenuetElement mElement = null;
	
	/**
	 * once the MenuetElement and other
	 * major components have been initialized,
	 * this should be called to apply colors,
	 * patterns, etc. to elements specific
	 * to the tool gorup.
	 */
	abstract public void applyToolGroupSettings();
	
	/**
	 * Defined by each tool group.  This
	 * determines which mode the menuetElement
	 * will be placed under for display.
	 * @return
	 */
	abstract public int  getToolMode();
	
	/**
	 * <b>Setup the application to operate via a different tool / mode.</b><br>
	 * Typically the majority of the functionality can be easily accomplished 
	 * by making a call to either changeMenuetTool() or changeMenuetToolMode().  
	 */
	abstract public void toolSelected();
	
	/**
	 * changes the menuet tool by: <br>
	 *   - (1) call menuetElementDeselected() (via the active controller in AvoGlobal) if != NULL.<br>
	 *   - (2) update the state of the menuet to reflect selection changes (via selectButton)<br>
	 *   - (3) set the active tool controller (via AvoGlobal) to match the menuet's mode.<br>
	 *   - (4) call menuetElementSelected() for this tool<br><br>	  
	 * 
	 * @param mElement MenuetElement to set as selected
	 * @param toolCtrl ToolController to use for mouse/keyboard/etc. interfaces
	 */
	protected void changeMenuetTool(MenuetElement mElement, ToolCtrl toolCtrl){
		// (1) call menuetElementDeselected() (via the active controller in AvoGlobal) if != NULL.
		if(AvoGlobal.activeToolController != null){
			AvoGlobal.activeToolController.menuetElementDeselected();
		}
		
		// (2) update the state of the menuet to reflect selection changes (via selectButton)
		AvoGlobal.menuet.selectButton(mElement);
		
		// (3) set the active tool controller (via AvoGlobal) to match the menuet's mode.
		AvoGlobal.activeToolController = toolCtrl;
		
		// (4) call menuetToolSelected() for this tool
		if(toolCtrl != null){
			toolCtrl.menuetElementSelected();		
		}
	}
	
	/**
	 * changes the menuet tool by: <br>
	 *   - (1) call menuetElementDeselected() (via the active controller in AvoGlobal) if != NULL.<br>
	 *   - (2) update the state of the menuet to reflect the desired mode (via setCurrentToolMode)<br>
	 *   - (3) set the active tool controller (via AvoGlobal) to match the menuet's mode.<br>
	 *   - (4) call menuetElementSelected() for this tool<br><br>	  
	 * 
	 * @param mode Menuet tool mode to select (Menuet.MENUET_MODE_???)
	 * @param toolCtrl ToolController to use for mouse/keyboard/etc. interfaces
	 */
	protected void changeMenuetToolMode(int mode, ToolCtrl toolCtrl){
		// (1) call menuetElementDeselected() (via the active controller in AvoGlobal) if != NULL.
		if(AvoGlobal.activeToolController != null){
			AvoGlobal.activeToolController.menuetElementDeselected();
		}

		// (2) update the state of the menuet to reflect the desired mode (via setCurrentToolMode)
		AvoGlobal.menuet.setCurrentToolMode(mode);
		
		// (3) set the active tool controller (via AvoGlobal) to match the menuet's mode.
		AvoGlobal.activeToolController = toolCtrl;
		
		// (4) call menuetToolSelected() for this tool
		if(toolCtrl != null){
			toolCtrl.menuetElementSelected();
		}
		
	}
	
}
