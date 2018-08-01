package org.poikilos.librecsg.ui.tools;

import javax.media.opengl.GL;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;

import org.poikilos.librecsg.backend.adt.ParamSet;


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
 * All of the tool's main controller functionality:
 * (mouse handling, button clicking, etc.)
 */
public interface ToolCtrl {

	/**
	 * glView calls this when it received a <em>mousedown</em> event.
	 * Both mouse (screen) coordinates and absolute x,y,z
	 * coordinates are provided.
	 * @param x x-position
	 * @param y y-position
	 * @param z z-position
	 * @param e mouseEvent (useful for key state, button pressed, etc.)
	 */
	abstract public void glMouseDown(double x, double y, double z, MouseEvent e, ParamSet paramSet);

	/**
	 * glView calls this when it received a <em>mousemove</em>
	 * event with the mouse button held down.
	 * Both mouse (screen) coordinates and absolute x,y,z
	 * coordinates are provided.
	 * @param x x-position
	 * @param y y-position
	 * @param z z-position
	 * @param e mouseEvent (useful for key state, button pressed, etc.)
	 */
	abstract public void glMouseDrag(double x, double y, double z,  MouseEvent e, ParamSet paramSet);

	/**
	 * glView calls this when it recieved a <em>mousemove</em>
	 * event with the mouse button <em>NOT</em> held down.
	 * Both mouse (screen) coordinates and absolute x,y,z
	 * coordinates are provided.
	 * @param x x-position
	 * @param y y-position
	 * @param z z-position
	 * @param e mouseEvent (useful for key state, button pressed, etc.)
	 */
	abstract public void glMouseMovedUp(double x, double y, double z,  MouseEvent e, ParamSet paramSet);

	/**
	 * glView calls this when it received a <em>mouseup</em> event.
	 * Both mouse (screen) coordinates and absolute x,y,z
	 * coordinates are provided.
	 * @param x x-position
	 * @param y y-position
	 * @param z z-position
	 * @param e mouseEvent (useful for key state, button pressed, etc.)
	 */
	abstract public void glMouseUp(double x, double y, double z,  MouseEvent e, ParamSet paramSet);

	abstract public void glKeyPressed(KeyEvent e, boolean ctrlIsDown, boolean shiftIsDown, ParamSet paramSet);

	/**
	 * This is called by the menuet element (the tool's view)
	 * when the element is clicked or otherwise selected.
	 */
	abstract public void menuetElementSelected();

	/**
	 * This is called by the menuet element (the tool's view) when
	 * the element is deselected (e.g., another tool is clicked).
	 * The tool should finalize the activeParamSet if necessary.
	 */
	abstract public void menuetElementDeselected();


}

