package ui.tools.main;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.ToolMain;
import backend.data.utilities.ImageUtils;
import backend.global.AvoGlobal;
import backend.model.Feature2D3D;
import backend.model.Group;
import backend.model.Part;
import backend.model.Sketch;

public class ToolMainNone extends ToolMain{

	
	//
//	Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//	This code is distributed under the terms of the 
//	GNU General Public License (GPL).
	//
//	This file is part of avoADo.
	//
//	AvoCADo is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
	//
//	AvoCADo is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
	//
//	You should have received a copy of the GNU General Public License
//	along with AvoCADo; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
	//

	/*
	* @author  Adam Kumpf
	* @created Feb. 2007
	*/
public ToolMainNone(Menuet menuet){	
		
		// initialize GUI elements
		mElement = null; // NO GUI ELEMENT
		
		this.applyToolGroupSettings();	// APPLY MAIN GROUP SETTINGS
		
		toolInterface = new ToolMainNoneInt();
	}

	@Override
	public void toolSelected() {
		AvoGlobal.menuet.disableAllTools();
		AvoGlobal.menuet.setCurrentToolMode(Menuet.MENUET_MODE_MAIN);
		AvoGlobal.menuet.updateToolModeDisplayed();
	}
	
}
