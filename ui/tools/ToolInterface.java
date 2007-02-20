package ui.tools;

import org.eclipse.swt.events.MouseEvent;

import backend.adt.ParamSet;


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
public interface ToolInterface {
	
	/**
	 * glView calls this when it recieved a <em>mousedown</em> event.
	 * Both mouse (screen) coordinates and absolute x,y,z 
	 * coordinates are provided.
	 * @param x x-position
	 * @param y y-position
	 * @param z z-position
	 * @param e mouseEvent (useful for key state, button pressed, etc.)
	 */
	abstract public void glMouseDown(double x, double y, double z, MouseEvent e);
	
	/**
	 * glView calls this when it recieved a <em>mousemove</em> 
	 * event with the mouse button held down.
	 * Both mouse (screen) coordinates and absolute x,y,z 
	 * coordinates are provided.
	 * @param x x-position
	 * @param y y-position
	 * @param z z-position
	 * @param e mouseEvent (useful for key state, button pressed, etc.)
	 */
	abstract public void glMouseDrag(double x, double y, double z,  MouseEvent e);
	
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
	abstract public void glMouseMovedUp(double x, double y, double z,  MouseEvent e);
	
	/**
	 * glView calls this when it recieved a <em>mouseup</em> event.
	 * Both mouse (screen) coordinates and absolute x,y,z 
	 * coordinates are provided.
	 * @param x x-position
	 * @param y y-position
	 * @param z z-position
	 * @param e mouseEvent (useful for key state, button pressed, etc.)
	 */
	abstract public void glMouseUp(double x, double y, double z,  MouseEvent e);
	
	/**
	 * verify that the parameter data is in fact valid/complete.
	 * This include checks of each expected element in the set
	 * for inclusion in the set and that it is the correct type.
	 * @param paramSet ParamSet to be checked.
	 * @return true iff paramSet is valid for the given ToolInterface
	 */
	abstract public boolean paramSetIsValid(ParamSet paramSet);
	
	/**
	 * update all derived params in the ParamSet if any exist via this ToolInterface.
	 * @param paramSet a <em>valid</em> paramSet for the given ToolInterface.
	 */	
	abstract public void updateDerivedParams(ParamSet paramSet);
	
}

