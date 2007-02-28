package backend.global;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;


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

/**
 * avoCADo's main colors for use by all graphical elements
 */
public class AvoColors {

	/**
	 * Background color of Menuet in various modes.
	 */
	public static final Color COLOR_MENUET_MAIN   = new Color(Display.getCurrent(),  230,  230,  230);
	public static final Color COLOR_MENUET_2D     = new Color(Display.getCurrent(),  180,  240,  180);
	public static final Color COLOR_MENUET_2Dto3D = new Color(Display.getCurrent(),  180,  180,  240);
	public static final Color COLOR_MENUET_3D     = new Color(Display.getCurrent(),  220,  220,  180);
	
	/**
	 * colors for special menuet buttons
	 */
	public static final Color COLOR_MENUET_DONE_MO = new Color(Display.getCurrent(),  200,  255,  200);
	public static final Color COLOR_MENUET_DONE_US = new Color(Display.getCurrent(),  230,  255,  230);
	public static final Color COLOR_MENUET_CNCL_MO = new Color(Display.getCurrent(),  255,  200,  200);
	public static final Color COLOR_MENUET_CNCL_US = new Color(Display.getCurrent(),  255,  230,  230);	
	public static final Color COLOR_MENUET_TLBX_MO = new Color(Display.getCurrent(),  200,  200,  255);
	public static final Color COLOR_MENUET_TLBX_US = new Color(Display.getCurrent(),  220,  220,  255);
	
	/**
	 * colors for parameter dialog
	 */
	public static final Color COLOR_PARAM_BG       = new Color(Display.getCurrent(),  220,  210,  240);
	public static final Color COLOR_PARAM_DERIVED  = new Color(Display.getCurrent(),  220,  220,  240);
	public static final Color COLOR_PARAM_SEL_SAT  = new Color(Display.getCurrent(),  160,  240,  160);
	public static final Color COLOR_PARAM_SEL_UNSAT= new Color(Display.getCurrent(),  240,  160,  160);
	
	/**
	 * colors for QuickSettings bar
	 */
	public static final Color COLOR_QSET_BG = new Color(Display.getCurrent(),  220,  220,  240);
	
	/**
	 * colors for GLView
	 */
	public static final float[] GL_COLOR4_BACKGND    = new float[] {0.95f, 0.95f, 1.0f, 1.0f};
	public static final float[] GL_COLOR4_GRID_DARK  = new float[] {0.6f, 0.6f, 0.6f, 1.0f}; 
	public static final float[] GL_COLOR4_GRID_LIGHT = new float[] {0.8f, 0.8f, 0.8f, 1.0f}; 
	public static final float[] GL_COLOR4_2D_NONACT  = new float[] {0.2f, 0.4f, 0.9f, 1.0f}; 
	public static final float[] GL_COLOR4_2D_ACTIVE  = new float[] {1.0f, 0.5f, 0.0f, 1.0f}; 
	public static final float[] GL_COLOR4_2D_X_AXIS  = new float[] {1.0f, 0.5f, 0.5f, 1.0f}; // red
	public static final float[] GL_COLOR4_2D_Y_AXIS  = new float[] {0.5f, 1.0f, 0.5f, 1.0f}; // green
	public static final float[] GL_COLOR4_2D_Z_AXIS  = new float[] {0.5f, 0.5f, 1.0f, 1.0f}; // blue
	
}
