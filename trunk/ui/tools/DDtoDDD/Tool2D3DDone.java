package ui.tools.DDtoDDD;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.Tool2D3D;
import backend.data.utilities.ImageUtils;
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
public class Tool2D3DDone extends Tool2D3D{

	public Tool2D3DDone(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode());
		mElement.mePreferredHeight = 100;
		mElement.meColorMouseOver  = AvoGlobal.COLOR_MENUET_DONE_MO;
		mElement.meColorUnselected = AvoGlobal.COLOR_MENUET_DONE_US; 
		mElement.meLabel = "Done";
		mElement.meIcon = ImageUtils.getIcon("menuet/Done.png", 24, 24);
		mElement.setToolTipText("Finish working in the 2Dto3D mode,\nkeeping any changes that have been made.");
		mElement.mePriority = 0; 	// 0 = always show element, >5 = never show element
		mElement.meDispOptions = MenuetElement.ME_TRY_ICON;
		
		this.applyToolGroupSettings();	// APPLY 2D GROUP SETTINGS
		
		toolInterface = new Tool2D3DDoneInt();
	}

	@Override
	public void toolSelected() {
		AvoGlobal.paramDialog.finalizeCurrentParams();
		AvoGlobal.menuet.disableAllTools();
		AvoGlobal.menuet.setCurrentToolMode(Menuet.MENUET_MODE_MAIN);
		AvoGlobal.paramDialog.setParamSet(null);
		AvoGlobal.menuet.currentTool = null;			
		AvoGlobal.menuet.updateToolModeDisplayed();
		AvoGlobal.glView.updateGLView = true;				
		
		// TODO: if the feat2D3D isn't complete, then (1) let the user know, and then (2) delete it if user desires.
	}
	
}
