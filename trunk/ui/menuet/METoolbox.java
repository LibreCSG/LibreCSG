package ui.menuet;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import ui.utilities.ColorUtils;
import backend.data.utilities.ImageUtils;
import backend.global.AvoColors;

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
public class METoolbox{

	MEButton toolboxButton;
	MEButton lastToolButton;
	
	public METoolbox(Menuet menuet, int mode){		
		toolboxButton  = new MEButton(menuet,mode);		
		toolboxButton.meLabel = "Toolbox";
		toolboxButton.meIcon  = ImageUtils.getIcon("menuet/Toolbox.png", 24, 24);
		toolboxButton.mePreferredHeight = 50;
		toolboxButton.setToolTipText("Toolbox of all possible tools that \n" +
				       				 "can be used in the current mode.");
		toolboxButton.meColorMouseOver  = AvoColors.COLOR_MENUET_TLBX_MO;
		toolboxButton.meColorUnselected = AvoColors.COLOR_MENUET_TLBX_US;
		toolboxButton.meColorBackground = ColorUtils.getModeBGColorByMode(mode);		
		toolboxButton.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e) {		
			}
			public void mouseDown(MouseEvent e) {
				// TODO: show the toolbox of all the current mode's tools
				System.out.println("Open the toolbox! (not implemented yet)  :[  ");			
			}
			public void mouseUp(MouseEvent e) {				
			}			
		});
		
		lastToolButton = new MEButton(menuet,mode);
		lastToolButton.meLabel = "";
		lastToolButton.meIcon  = ImageUtils.getIcon("menuet/Toolbox.png", 24, 24);
		lastToolButton.mePreferredHeight = 50;
		lastToolButton.meDispOptions = MenuetElement.ME_TEXT_ONLY;
		lastToolButton.mePriority = 6; // start out not showing lastTool button
		lastToolButton.meColorMouseOver  = AvoColors.COLOR_MENUET_TLBX_MO;
		lastToolButton.meColorUnselected = AvoColors.COLOR_MENUET_TLBX_US;
		lastToolButton.meColorBackground = ColorUtils.getModeBGColorByMode(mode);
	}
	


}
