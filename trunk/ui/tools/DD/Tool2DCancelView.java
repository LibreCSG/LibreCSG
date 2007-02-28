package ui.tools.DD;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.ToolCtrl2D;
import ui.tools.ToolView2D;
import backend.global.AvoColors;
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
public class Tool2DCancelView extends ToolView2D{

	public Tool2DCancelView(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode());
		mElement.mePreferredHeight = 25;
		mElement.meColorMouseOver  = AvoColors.COLOR_MENUET_CNCL_MO;
		mElement.meColorUnselected = AvoColors.COLOR_MENUET_CNCL_US; 
		mElement.meLabel = "Cancel";
		mElement.setToolTipText("Cancel ALL changes made \nin the 2D drawing mode.");
		mElement.mePriority = 0; 	// 0 = always show element, >5 = never show element
		mElement.meDispOptions = MenuetElement.ME_TEXT_ONLY;
		
		this.applyToolGroupSettings();	// APPLY 2D GROUP SETTINGS
	}

	@Override
	public void toolSelected() {
		// 
		// two actions should always be performed:
		// (1) update the state of the menuet to reflect selection changes
		// (2) set the current tool controller (via AvoGlobal) to that which was selected.
		//
		
		//
		// (1) update menuet's state
		//
		
		
		//
		// (2) call to the tool's controller to set it as selected.
		//
		ToolCtrl2D toolCtrl = new Tool2DCancelCtrl();
		toolCtrl.menuetToolSelected();
		
		
		
		AvoGlobal.paramDialog.finalizeCurrentParams();
		MessageBox m = new MessageBox(AvoGlobal.menuet.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		m.setMessage("Are you sure you want to discard ALL changes\nand exit the 2D drawing mode?");
		m.setText("Discard ALL Changes?");
		if(m.open() == SWT.YES){		
			AvoGlobal.menuet.disableAllTools();
			AvoGlobal.menuet.setCurrentToolMode(Menuet.MENUET_MODE_MAIN);
			
			// TODO: remove sketch when Cancel is pushed.
			//       this should also force the TreeViewer to rebuild itself.

			AvoGlobal.setActiveParamSet(null);
			
			AvoGlobal.menuet.currentTool = null;			
			
			AvoGlobal.menuet.updateToolModeDisplayed();
			AvoGlobal.glView.updateGLView = true;
		}
	}
}