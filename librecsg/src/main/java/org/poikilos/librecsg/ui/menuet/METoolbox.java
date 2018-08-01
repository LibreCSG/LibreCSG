package org.poikilos.librecsg.ui.menuet;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import org.poikilos.librecsg.backend.data.utilities.ImageUtils;
import org.poikilos.librecsg.backend.global.AvoColors;
import org.poikilos.librecsg.backend.global.AvoGlobal;

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
public class METoolbox {

	MEButton toolboxButton;
	final public static String meToolboxLabel = "Toolbox";
	final public static int numLastTools = 3;

	public METoolbox(Menuet menuet, int mode){
		toolboxButton  = new MEButton(menuet, mode, null, false);
		toolboxButton.meLabel = meToolboxLabel;
		toolboxButton.meIcon  = ImageUtils.getIcon("menuet/Toolbox.png", 24, 24);
		toolboxButton.mePreferredHeight = 50;
		toolboxButton.setToolTipText("Toolbox of all possible tools that \n" +
				       				 "can be used in the current mode.");
		toolboxButton.meColorMouseOver  = AvoColors.COLOR_MENUET_TLBX_MO;
		toolboxButton.meColorUnselected = AvoColors.COLOR_MENUET_TLBX_US;
		toolboxButton.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e) {
			}
			public void mouseDown(MouseEvent e) {
				AvoGlobal.toolboxDialog.loadToolBoxForCurrentMode();
			}
			public void mouseUp(MouseEvent e) {
			}
		});

		for(int i=0; i<numLastTools; i++){
			MEButton lastToolButton = new MEButton(menuet, mode, null, false);
			lastToolButton.meLabel = "??";
			lastToolButton.mePreferredHeight = 50;
			lastToolButton.meDispOptions = MenuetElement.ME_TEXT_ONLY;
			lastToolButton.meSetShown(false);
		}
	}
}
