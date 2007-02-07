package ui.utilities;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

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
public class ColorUtils {

	/**
	 * get the background color for the specified
	 * tool mode.
	 * @param mode
	 * @return
	 */
	public static Color getModeBGColorByMode(int mode){
		Color clr;
		switch(mode){
			case(AvoGlobal.MENUET_MODE_MAIN):{
				clr = AvoGlobal.COLOR_MENUET_MAIN;
				break;
			}			
			case(AvoGlobal.MENUET_MODE_2D):{
				clr = AvoGlobal.COLOR_MENUET_2D;
				break;
			}
			case(AvoGlobal.MENUET_MODE_2Dto3D):{
				clr = AvoGlobal.COLOR_MENUET_2Dto3D;
				break;
			}
			case(AvoGlobal.MENUET_MODE_3D):{
				clr = AvoGlobal.COLOR_MENUET_3D;
				break;
			}
			default:{
				clr = new Color(Display.getCurrent(),0,0,0);
				break;
			}
		}
		return clr;
	}
	
	
	/**
	 * get the background color for the
	 * current tool mode being used.
	 * @return
	 */
	public static Color getModeBGColor(){
		return getModeBGColorByMode(AvoGlobal.currentToolMode);
	}
	
}
