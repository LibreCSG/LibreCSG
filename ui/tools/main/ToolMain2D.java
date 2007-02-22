package ui.tools.main;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.ToolMain;
import backend.data.utilities.ImageUtils;
import backend.global.AvoGlobal;
import backend.model.Sketch;


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
public class ToolMain2D extends ToolMain{

	public ToolMain2D(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode());
		mElement.mePreferredHeight = 100;
		mElement.meLabel = "2D";
		mElement.meIcon = ImageUtils.getIcon("menuet/MAIN_2D.png", 24, 24);
		mElement.setToolTipText("2D Sketch Mode");
		mElement.mePriority = 0; 	// 0 = always show element, >5 = never show element
		mElement.meDispOptions = MenuetElement.ME_TRY_TEXT;
		
		this.applyToolGroupSettings();	// APPLY MAIN GROUP SETTINGS
		
		toolInterface = new ToolMain2DInt();
	}

	@Override
	public void toolSelected() {
		AvoGlobal.menuet.disableAllTools();
		AvoGlobal.menuet.setCurrentToolMode(Menuet.MENUET_MODE_2D);
		AvoGlobal.menuet.updateToolModeDisplayed();
		//
		// create new Feature3D to use for subsequent 2D sketches
		//
		if(AvoGlobal.project.getActiveGroup() == null){
			int i = AvoGlobal.project.addNewGroup();
			AvoGlobal.project.setActiveGroup(i);
		}
		if(AvoGlobal.project.getActivePart() == null){
			int i = AvoGlobal.project.getActiveGroup().addNewPart();
			AvoGlobal.project.getActiveGroup().setActivePart(i);
		}
		
		int i = AvoGlobal.project.getActivePart().addNewSketch();
		AvoGlobal.project.getActivePart().setActiveSubPart(i);
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null){
			AvoGlobal.paramDialog.setParamSet(sketch.paramSet);
		}
	}

}