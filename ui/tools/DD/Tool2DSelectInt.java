package ui.tools.DD;

import java.util.Iterator;

import javax.media.opengl.GL;

import ui.tools.ToolInterface;
import backend.adt.ParamSet;
import backend.global.AvoGlobal;
import backend.model.Feature;


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
public class Tool2DSelectInt implements ToolInterface {
	
	/**
	 * All of the tool's main functionality
	 * mouse handling, glView drawing, 
	 * parameter storage, etc.
	 *
	 */
	public Tool2DSelectInt(){		
	}
	
	public void glMouseDown(double x, double y, double z, int mouseX, int mouseY) {
		// iterate over all features in the current set to see if they've been clicked
		Iterator allFeats = AvoGlobal.getFeatureSet().iterator();
		while(allFeats.hasNext()){
			Feature f = (Feature)allFeats.next();
			if(f.toolInterface.mouseIsOver(f.paramSet, x, y, z, mouseX, mouseY, 0.1)){
				f.isSelected = true;
			}else{
				//System.out.println("not over featere.  go fish.");
			}
		}
	}

	public void glMouseDrag(double x, double y, double z, int mouseX, int mouseY) {
	}

	public void glMouseUp(double x, double y, double z, int mouseX, int mouseY) {
	}

	public void glDrawFeature(GL gl, ParamSet p) {
	}

	public boolean mouseIsOver(ParamSet p, double x, double y, double z, int mouseX, int mouseY, double err) {
		return false;
	}
}
