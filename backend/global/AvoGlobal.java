package backend.global;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import ui.menuet.Menuet;
import ui.tools.Tool;


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
public class AvoGlobal {

	/**
	 * Background color of Menuet in various modes.
	 */
	public static final Color COLOR_MENUET_MAIN = new Color(Display.getCurrent(),  230,  230,  230);
	public static final Color COLOR_MENUET_2D = new Color(Display.getCurrent(),  150,  220,  150);
	public static final Color COLOR_MENUET_2Dto3D = new Color(Display.getCurrent(),  150,  150,  220);
	public static final Color COLOR_MENUET_3D = new Color(Display.getCurrent(),  220,  220,  150);
	
	/**
	 * Tool mode identifiers 
	 * (used as index in Menuet's LinkedList, so keep them sequential)
	 */
	public static final int MENUET_MODE_MAIN   = 0;
	public static final int MENUET_MODE_2D     = 1;
	public static final int MENUET_MODE_2Dto3D = 2;
	public static final int MENUET_MODE_3D     = 3;
	
	public static final int MENUET_TOTAL_MODES = 4; // always the highest mode number + 1
	
	/**
	 * Current tool mode being used (2D, 2Dto3D, 3D, etc.) 
	 */
	public static int currentToolMode = MENUET_MODE_MAIN; 
	
	public static Tool currentTool    = null;
	
	/**
	 * The main interaction menu.
	 * Mode-based and dynamically displayed with
	 * prioritization of elements.
	 */
	public static Menuet menuet;
	
	// TODO: use glView instead of dummy composite
	public static Composite glView;
	
	
}
