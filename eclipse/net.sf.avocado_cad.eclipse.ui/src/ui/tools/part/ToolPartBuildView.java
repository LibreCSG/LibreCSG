package ui.tools.part;

import net.sf.avocado_cad.eclipse.ui.AvoGlobal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.ToolViewPart;
import ui.utilities.ImageUtils;
import backend.model.Part;
import backend.model.Sketch;


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
public class ToolPartBuildView extends ToolViewPart{

	public ToolPartBuildView(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode(), this, false);
		mElement.mePreferredHeight = 100;
		mElement.meLabel = "Build";
		mElement.meIcon = ImageUtils.getIcon("menuet/Part_Build.png", 24, 24);
		mElement.setToolTipText("Transform 2D sketches\ninto various 3D shapes.");
		mElement.meDispOptions = MenuetElement.ME_TRY_TEXT;
		
		this.applyToolGroupSettings();	// APPLY MAIN GROUP SETTINGS
	}

	@Override
	public void toolSelected() {
		Sketch sketch = (Sketch) AvoGlobal.project.getActiveSketch();
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
			
			changeMenuetToolMode(Menuet.MENUET_MODE_BUILD);
			
			// TODO: Building should not be done in the view!!
			sketch.buildRegions();
			
			int i = ((Part)AvoGlobal.project.getActivePart()).addNewFeat2D3D(sketch.getUniqueID());
			((Part)AvoGlobal.project.getActivePart()).setActiveSubPart(i);
			
		}
	}
	
}
