package backend.global;

import ui.event.GLViewEventHandler;
import ui.event.ModelEventHandler;
import ui.event.ParamEventHandler;
import ui.menuet.Menuet;
import ui.menuet.MenuetToolboxDialog;
import ui.opengl.GLView;
import ui.opengl.RenderLevel;
import ui.paramdialog.DynParamDialog;
import ui.tools.ToolCtrl;
import ui.treeviewer.TreeViewer;
import backend.adt.ParamSet;
import backend.model.Group;
import backend.model.Part;
import backend.model.Project;


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

/**
 * avoCADo's main global variables.  References to global state
 * should be made sparingly if possible to reduce the amount of
 * cross-linked and dependent code.  however, many methods may
 * require information about other parts of the application and
 * access via this global state is preferred over directly accessing
 * information in generally unrelated classes.
 */
public class AvoGlobal {

	/**
	 * The main interaction menu.
	 * Mode-based and dynamically displayed with
	 * prioritization of elements.
	 */
	public static Menuet menuet;
	//	 TODO: make Menuet private
	
	/**
	 * The main 3D viewport
	 */
	public static GLView glView;
	//	 TODO: make GLView private
	
	/**
	 * The dynamically displayed parameter Dialog
	 */
	public static DynParamDialog paramDialog;
	//	 TODO: make DynParamDialog private
	
	public static void setActiveParamSet(ParamSet paramSet){
		paramDialog.setParamSet(paramSet);
	}
	
	
	public static ToolCtrl activeToolController = null;
	
	/**
	 * The toolbox of all possible tools in the current mode.
	 */
	public static MenuetToolboxDialog toolboxDialog;
	
	
	/**
	 * The tree view of the current Project
	 */
	public static TreeViewer treeViewer;
	//	 TODO: make TreeViewer private	
	
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
	

	// TODO: perhaps move event handler just into their own static selves instead of AvoGlobal?
	public static ParamEventHandler  paramEventHandler  = new ParamEventHandler();
	public static GLViewEventHandler glViewEventHandler = new GLViewEventHandler(); 
	public static ModelEventHandler  modelEventHandler  = new ModelEventHandler();

	
	public static double[] glCursor3DPos = new double[] {0.0, 0.0, 0.0}; 
	
	/**
	 * Setup a newly created project to be user friendly. :) <br/>
	 * build the project all the way to a new sketch and 
	 * then put the menuet in the sketch mode.
	 */
	public static void intializeNewAvoCADoProject(){
		project.addNewGroup();
		Group group = project.getActiveGroup();
		if(group != null){
			group.addNewPart();
			Part part = group.getActivePart();
			if(part != null){
				part.addNewSketchOnSelectedPlane();
				menuet.setCurrentToolMode(Menuet.MENUET_MODE_SKETCH);
				paramDialog.setParamSet(null);
				glView.updateGLView = true;
			}
		}
	}
	
}
