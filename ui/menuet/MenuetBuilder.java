package ui.menuet;

import ui.tools.DD.Tool2DCancel;
import ui.tools.DD.Tool2DCircle;
import ui.tools.DD.Tool2DDone;
import ui.tools.DD.Tool2DLine;
import ui.tools.DD.Tool2DRect;
import ui.tools.DD.Tool2DSelect;
import ui.tools.main.ToolMain2D;
import ui.tools.main.ToolMain2Dto3D;
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
public class MenuetBuilder {

	/**
	 * Build all of the tools into the menuet.
	 * Nothing fancy, just add them all along 
	 * with appropriate labels.
	 * @param menuet
	 */
	public static void buildMenuet(Menuet menuet){
		
		//
		//  TOOL MODE:  2D
		//
		new Tool2DDone(menuet);
		new Tool2DCancel(menuet);
		
		MELabel label2D = new MELabel(menuet,Menuet.MENUET_MODE_2D);
		label2D.meColorBackground = AvoGlobal.COLOR_MENUET_2D;
		label2D.meLabel = "2D";
		label2D.textIsBold = true;
		
		new Tool2DLine(menuet);
		new Tool2DCircle(menuet);
		new Tool2DRect(menuet);
		
		
		new METoolbox(menuet,Menuet.MENUET_MODE_2D);
		
		new Tool2DSelect(menuet);
		
		
		//
		//  TOOL MODE:  Main
		//
		MELabel labelMAIN = new MELabel(menuet,Menuet.MENUET_MODE_MAIN);
		labelMAIN.meColorBackground = AvoGlobal.COLOR_MENUET_MAIN;
		labelMAIN.meLabel = "Tools";
		labelMAIN.textIsBold = true;
		
		new ToolMain2D(menuet);
		new ToolMain2Dto3D(menuet);
		
		
	}
	
}
