package ui.tools.DD;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.Tool2D;
import backend.data.utilities.ImageUtils;
import backend.global.AvoGlobal;
import backend.model.Feature2D;
import backend.model.Sketch;
import backend.primatives.Prim2D;
import backend.primatives.Prim2DArc;
import backend.primatives.Prim2DLine;


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
public class Tool2DDone extends Tool2D{
	
	public Tool2DDone(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode());
		mElement.mePreferredHeight = 100;
		mElement.meColorMouseOver  = AvoGlobal.COLOR_MENUET_DONE_MO;
		mElement.meColorUnselected = AvoGlobal.COLOR_MENUET_DONE_US; 
		mElement.meLabel = "Done";
		mElement.meIcon = ImageUtils.getIcon("menuet/Done.png", 24, 24);
		mElement.setToolTipText("Finish working in the 2D mode.");
		mElement.mePriority = 0; 	// 0 = always show element, >5 = never show element
		mElement.meDispOptions = MenuetElement.ME_TRY_ICON;
		
		this.applyToolGroupSettings();	// APPLY 2D GROUP SETTINGS
		
		toolInterface = new Tool2DDoneInt();
	}

	@Override
	public void toolSelected() {
		// TODO: Push all new items into main backend.model
		AvoGlobal.menuet.disableAllTools();
		AvoGlobal.menuet.setCurrentToolMode(Menuet.MENUET_MODE_MAIN);
		// TODO: deselect all 2D features
		AvoGlobal.paramDialog.setParamSet(null);
		AvoGlobal.menuet.currentTool = null;			
		AvoGlobal.menuet.updateToolModeDisplayed();
		AvoGlobal.glView.updateGLView = true;
		
		// TODO: HACK just to test line intersection code!
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null){
			for(int i=0; i < sketch.getFeat2DListSize(); i++){
				Feature2D f2D_A = sketch.getAtIndex(i);
				for(Prim2D prim_A : f2D_A.prim2DList){
					for(int j=i+1; j < sketch.getFeat2DListSize(); j++){
						Feature2D f2D_B = sketch.getAtIndex(j);
						for(Prim2D prim_B : f2D_B.prim2DList){
							prim_A.intersect(prim_B);
						}
					}
				}
			}
		}
		
	}
}
