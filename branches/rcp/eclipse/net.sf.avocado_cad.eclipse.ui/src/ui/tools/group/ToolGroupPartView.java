package ui.tools.group;

import net.sf.avocado_cad.eclipse.ui.AvoGlobal;
import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.ToolViewGroup;
import ui.utilities.ImageUtils;
import backend.model.Group;


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
* @created Mar. 2007
*/
public class ToolGroupPartView extends ToolViewGroup{

	public ToolGroupPartView(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode(), this, false);
		mElement.mePreferredHeight = 100;
		mElement.meLabel = "Part";
		mElement.meIcon = ImageUtils.getIcon("menuet/Group_Part_Add.png", 24, 24);
		mElement.setToolTipText("Add a new Part to the group.");
		mElement.meDispOptions = MenuetElement.ME_TRY_TEXT;
		
		this.applyToolGroupSettings();	// APPLY MAIN GROUP SETTINGS
	}
	
	@Override
	public void toolSelected() {
		// Add a new Part to the Group
		Group group = (Group) AvoGlobal.project.getActiveGroup();
		if(group != null){
			group.addNewPart();
			changeMenuetToolMode(Menuet.MENUET_MODE_PART);
		}else{
			System.out.println("ToolGroupPartCtrl(menuetElementSelected): No group was active? Cannot add new Part!");
		}			
	}

}