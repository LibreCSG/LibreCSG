package backend.global;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import ui.event.GLViewEventHandler;
import ui.event.ModelEventHandler;
import ui.event.ParamEventHandler;
import ui.menuet.Menuet;
import ui.opengl.GLView;
import ui.opengl.RenderLevel;
import ui.paramdialog.DynParamDialog;
import ui.treeviewer.TreeViewer;
import backend.model.Project;


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

	
	// TODO: move all colors to a new class -- AvoColors
	/**
	 * Background color of Menuet in various modes.
	 */
	public static final Color COLOR_MENUET_MAIN   = new Color(Display.getCurrent(),  230,  230,  230);
	public static final Color COLOR_MENUET_2D     = new Color(Display.getCurrent(),  150,  220,  150);
	public static final Color COLOR_MENUET_2Dto3D = new Color(Display.getCurrent(),  150,  150,  220);
	public static final Color COLOR_MENUET_3D     = new Color(Display.getCurrent(),  220,  220,  150);
	
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
	public static final Color COLOR_PARAM_BG       = new Color(Display.getCurrent(),  220,  190,  190);
	public static final Color COLOR_PARAM_DERIVED  = new Color(Display.getCurrent(),  220,  220,  240);
	
	/**
	 * colors for QuickSettings bar
	 */
	public static final Color COLOR_QSET_BG = new Color(Display.getCurrent(),  220,  220,  240);
	

	/**
	 * The main interaction menu.
	 * Mode-based and dynamically displayed with
	 * prioritization of elements.
	 */
	public static Menuet menuet;
	
	/**
	 * The main 3D viewport
	 */
	public static GLView glView;
	
	/**
	 * The dynamically displayed parameter Dialog
	 */
	public static DynParamDialog paramDialog;
	
	/**
	 * The tree view of the current Project
	 */
	public static TreeViewer treeViewer;
	
	

	//public static Tool currentTool     = null;
	
	/**
	 * The main assembly of parts in the workspace!
	 */
	public static Project project = new Project();
	
	/**
	 * precision of rendering... higher levels take longs,
	 * but more accurately follow shapes, cureves, etc.
	 */
	public static RenderLevel renderLevel = RenderLevel.Medium;
	
	/**
	 * mouse snaps to positions in multiples of the <code>snapSize</code> 
	 * when <code>snapEnabled</code> is set to <code>true</code>.
	 */
	public static double  gridSize    = 1.0;  
	public static double  snapSize    = 0.5;
	public static boolean snapEnabled = true;
	
	//
	//  glView Colors
	//
	public static final float[] GL_COLOR4_BACKGND    = new float[] {0.95f, 0.95f, 1.0f, 1.0f};
	public static final float[] GL_COLOR4_GRID_DARK  = new float[] {0.6f, 0.6f, 0.6f, 1.0f}; 
	public static final float[] GL_COLOR4_GRID_LIGHT = new float[] {0.8f, 0.8f, 0.8f, 1.0f}; 
	public static final float[] GL_COLOR4_2D_NONACT  = new float[] {0.2f, 0.4f, 0.9f, 1.0f}; 
	public static final float[] GL_COLOR4_2D_ACTIVE  = new float[] {1.0f, 0.5f, 0.0f, 1.0f}; 
	

	public static ParamEventHandler  paramEventHandler  = new ParamEventHandler();
	public static GLViewEventHandler glViewEventHandler = new GLViewEventHandler(); 
	public static ModelEventHandler  modelEventHAndler  = new ModelEventHandler();

	
	public static double[] glCursor3DPos = new double[] {0.0, 0.0, 0.0}; 
	
	
	
}
