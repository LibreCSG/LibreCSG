package ui.tools.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.ToolMain;
import backend.data.utilities.ImageUtils;
import backend.global.AvoGlobal;
import backend.model.Feature2D3D;
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
public class ToolMain2D3D extends ToolMain{

	public ToolMain2D3D(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode());
		mElement.mePreferredHeight = 100;
		mElement.meLabel = "2Dto3D";
		mElement.meIcon = ImageUtils.getIcon("menuet/MAIN_2Dto3D.png", 24, 24);
		mElement.setToolTipText("Transform 2D sketches\ninto various 3D shapes.");
		mElement.mePriority = 0; 	// 0 = always show element, >5 = never show element
		mElement.meDispOptions = MenuetElement.ME_TRY_TEXT;
		
		this.applyToolGroupSettings();	// APPLY MAIN GROUP SETTINGS
		
		toolInterface = new ToolMain2D3DInt();
	}

	@Override
	public void toolSelected() {
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch == null || sketch.isConsumed){
			MessageBox m = new MessageBox(AvoGlobal.menuet.getShell(), SWT.ICON_QUESTION | SWT.OK);
			m.setMessage(	"You must select an unconsumed sketch before\n" +
							"any 2Dto3D operations can be performed.\n\n" +
							"Please create a new sketch or select one\n" +
							"by double-clicking on it in the project's\n" +
							"list of elements.");
			m.setText("Please select a sketch");
			m.open();
		}else{
			// there is a sketch active!
			AvoGlobal.menuet.disableAllTools();
			AvoGlobal.menuet.setCurrentToolMode(Menuet.MENUET_MODE_2Dto3D);
			AvoGlobal.menuet.updateToolModeDisplayed();	
			sketch.buildRegionsFromPrim2D();
			
			Feature2D3D newFeat2D3D = new Feature2D3D(AvoGlobal.project.getActivePart(), null, null);
			int j = newFeat2D3D.add(sketch);
			newFeat2D3D.setActiveSketch(j);
			sketch.isConsumed = true;
			int i = AvoGlobal.project.getActivePart().add(newFeat2D3D);
			AvoGlobal.project.getActivePart().setActiveSubPart(i);
			
		}
	}
	
}
