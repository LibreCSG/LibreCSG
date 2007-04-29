package ui.tools.sketch;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import ui.menuet.MEButtonCancel;
import ui.menuet.Menuet;
import ui.tools.ToolViewSketch;
import backend.global.AvoGlobal;
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
public class ToolSketchCancelView extends ToolViewSketch{

	public ToolSketchCancelView(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButtonCancel(menuet, this.getToolMode(), this);
		mElement.setToolTipText("Cancel ALL changes made \nin the Sketch drawing mode.");
		
		this.applyToolGroupSettings();	// APPLY 2D GROUP SETTINGS
	}

	@Override
	public void toolSelected() {		
		MessageBox m = new MessageBox(AvoGlobal.menuet.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		m.setMessage("Are you sure you want to discard ALL changes\nand exit the Sketch drawing mode?");
		m.setText("Discard ALL Changes?");
		if(m.open() == SWT.YES){
			Sketch sketch = AvoGlobal.project.getActiveSketch();
			if(sketch != null){
				sketch.deselectAllFeat2D();
			}
			changeMenuetToolMode(Menuet.MENUET_MODE_PART);
			AvoGlobal.glView.updateGLView = true;
		}

	}
}
