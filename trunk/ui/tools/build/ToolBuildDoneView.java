package ui.tools.build;

import ui.menuet.MEButtonDone;
import ui.menuet.Menuet;
import ui.tools.ToolModelBuild;
import ui.tools.ToolViewBuild;
import backend.global.AvoGlobal;
import backend.model.Build;


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
public class ToolBuildDoneView extends ToolViewBuild{

	public ToolBuildDoneView(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButtonDone(menuet, this.getToolMode(), this);
		mElement.setToolTipText("Finish working in the 2Dto3D mode,\nkeeping any changes that have been made.");
		
		this.applyToolGroupSettings();	// APPLY 2D GROUP SETTINGS
	}

	@Override
	public void toolSelected() {
		Build feat2D3D = AvoGlobal.project.getActiveFeat2D3D();
		if(feat2D3D != null){
			if(feat2D3D.paramSet == null){
				AvoGlobal.project.getActivePart().removeActiveSubPart();
			}else{
				AvoGlobal.paramDialog.finalizeCurrentParams();
			}
		}else{
			System.out.println("ToolBuildDoneView(toolSelected): Hmm??! No active feature2D3D?");
		}
		changeMenuetToolMode(Menuet.MENUET_MODE_PART);
	}
	
}
