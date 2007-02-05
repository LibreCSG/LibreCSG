package ui.tools;

import javax.media.opengl.GL;

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
	 * @param x
	 * @param y
	 * @param z
	 * @param mouseX
	 * @param mouseY
	 */
	abstract public void glMouseDown(double x, double y, double z, int mouseX, int mouseY);
	
	/**
	 * glView calls this when it recieved a <em>mousemove</em> event.
	 * Both mouse (screen) coordinates and absolute x,y,z 
	 * coordinates are provided.
	 * @param x
	 * @param y
	 * @param z
	 * @param mouseX
	 * @param mouseY
	 */
	abstract public void glMouseDrag(double x, double y, double z, int mouseX, int mouseY);
	
	/**
	 * glView calls this when it recieved a <em>mouseup</em> event.
	 * Both mouse (screen) coordinates and absolute x,y,z 
	 * coordinates are provided.
	 * @param x
	 * @param y
	 * @param z
	 * @param mouseX
	 * @param mouseY
	 */
	abstract public void glMouseUp(double x, double y, double z, int mouseX, int mouseY);
	
	/**
	 * draw the feature on the glView given a set of
	 * parameters that define the feature.  Only DynamicPrimitives
	 * should be used for drawing.
	 * @param gl
	 * @param p
	 */
	public void glDrawFeature(GL gl, ParamSet p);
	
}
