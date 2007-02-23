package ui.tools.DDtoDDD;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.Tool2D3D;
import backend.data.utilities.ImageUtils;
import backend.global.AvoGlobal;
import backend.model.Feature2D3D;



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
public class Tool2D3DRevolve extends Tool2D3D{

	public Tool2D3DRevolve(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode());
		mElement.mePreferredHeight = 50;
		mElement.meLabel = "Revolve";
		mElement.meIcon = ImageUtils.getIcon("menuet/2D3D_Revolve.png", 24, 24);
		mElement.setToolTipText("Revolvle (spin) a region around a line.");
		mElement.mePriority = 0; 	// 0 = always show element, >5 = never show element
		mElement.meDispOptions = MenuetElement.ME_TRY_TEXT;
		
		this.applyToolGroupSettings();	// APPLY 2D GROUP SETTINGS
		
		toolInterface = new Tool2D3DRevolveInt();
	}

	public void toolSelected() {
		AvoGlobal.paramDialog.finalizeCurrentParams();
		AvoGlobal.menuet.selectButton(mElement);
		AvoGlobal.menuet.currentTool = this;
		
		//
		// Set tool Interface to this feature
		//
		Feature2D3D feat2D3D = AvoGlobal.project.getActiveFeat2D3D();
		if(feat2D3D != null){
			feat2D3D.toolInt2D3D = new Tool2D3DRevolveInt();
		}
	}
	
}
