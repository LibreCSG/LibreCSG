package ui.tools.part;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.ToolViewPart;
import backend.data.utilities.ImageUtils;
import backend.global.AvoGlobal;
import backend.model.Part;


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
//along with AvoCADo; if not, write to the Free Softwares
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

/*
* @author  Adam Kumpf
* @created Feb. 2007
*/
public class ToolPartSketchView extends ToolViewPart{

	public ToolPartSketchView(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode(), this, false);
		mElement.mePreferredHeight = 100;
		mElement.meLabel = "Sketch";
		mElement.meIcon = ImageUtils.getIcon("menuet/Part_Sketch.png", 24, 24);
		mElement.setToolTipText("2D Sketch Mode");
		mElement.meDispOptions = MenuetElement.ME_TRY_TEXT;
		
		this.applyToolGroupSettings();	// APPLY MAIN GROUP SETTINGS
	}

	@Override
	public void toolSelected() {
		// create a new sketch and change the menuet to the sketch mode
		Part part = AvoGlobal.project.getActivePart();
		if(part != null){
			part.addNewSketchOnSelectedPlane();
			changeMenuetToolMode(Menuet.MENUET_MODE_SKETCH);
			AvoGlobal.glView.updateGLView = true;
		}else{
			System.out.println("ToolSketkchLineView(toolSelected): What?! there was no active part to build the sketch!");
		}
	}

}
