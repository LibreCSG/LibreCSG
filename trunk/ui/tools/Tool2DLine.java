package ui.tools;

import org.eclipse.swt.widgets.Composite;

import ui.menuet.MEButton;
import ui.menuet.MenuetElement;


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
public class Tool2DLine extends Tool2D{

	public Tool2DLine(Composite menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, MenuetElement.ME_TRY_TEXT);
		mElement.mePreferredHieght = 50;
		mElement.meLabel = "Circle";
		mElement.setIcon("menuet/2D_Circle.png", 24, 24);
		mElement.setToolTipText("Center point circle");
		mElement.mePriority = 0; 	// 0 = always show element, >5 = never show element
		mElement.setBounds(0,0,65,75);
		
		this.applyToolGroupSettings();	// APPLY 2D GROUP SETTINGS
	}
	
	
	
	
	
	
	
	
	
}

