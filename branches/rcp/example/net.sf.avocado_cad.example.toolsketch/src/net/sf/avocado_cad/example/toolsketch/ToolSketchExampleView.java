package net.sf.avocado_cad.example.toolsketch;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.ToolViewSketch;
import ui.tools.sketch.ISketchToolView;
import ui.utilities.ImageUtils;


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
public class ToolSketchExampleView extends ToolViewSketch implements ISketchToolView {

	public ToolSketchExampleView(){
		
	}
	
	public void setup(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode(), this, true);
		mElement.mePreferredHeight = 50;
		mElement.meLabel = "Example";
		mElement.meIcon = ImageUtils.getIcon("menuet/2D_Example.png", 24, 24);
		mElement.setToolTipText("This is an example tool\nmeant to show how something a\nbit more complex than just\na line or a circle can be formed.");
		mElement.meDispOptions = MenuetElement.ME_TRY_TEXT;

		
		this.applyToolGroupSettings();	// APPLY 2D GROUP SETTINGS
	}

	@Override
	public void toolSelected() {
		changeMenuetTool(mElement, new ToolSketchExampleCtrl());
	}

	
}