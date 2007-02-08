package ui.tools.DD;

import javax.media.opengl.GL;

import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.global.AvoGlobal;
import backend.model.Feature;
import ui.opengl.GLDynPrim;
import ui.tools.ToolInterface;


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
public class Tool2DCircleInt implements ToolInterface  {

	/**
	 * All of the tool's main functionality
	 * mouse handling, glView drawing, 
	 * parameter storage, etc.
	 *
	 */
	public Tool2DCircleInt(){
	}
	
	
	public void glMouseDown(double x, double y, double z, int mouseX, int mouseY) {
		//
		// Build parameter set for this feature
		//
		ParamSet pSet = new ParamSet();
		pSet.addParam("c", new Param("center", new Point2D(x,y)));
		pSet.addParam("r", new Param("radius", 0.0));
		
		//
		// set the workingFeature to this feature
		//
		AvoGlobal.setWorkingFeature(new Feature(this, pSet));
	}

	public void glMouseDrag(double x, double y, double z, int mouseX, int mouseY) {
		Point2D ptC = (Point2D)AvoGlobal.getWorkingFeature().paramSet.getParam("c").getData();
		AvoGlobal.getWorkingFeature().paramSet.changeParam("r", ptC.computeDist(new Point2D(x,y)));		
	}

	public void glMouseUp(double x, double y, double z, int mouseX, int mouseY) {		
		// * finalize feature's formation
		Point2D ptC = (Point2D)AvoGlobal.getWorkingFeature().paramSet.getParam("c").getData();
		AvoGlobal.getWorkingFeature().paramSet.changeParam("r", ptC.computeDist(new Point2D(x,y)));	
		
		// * store permanently in model
		double radius = (Double)AvoGlobal.getWorkingFeature().paramSet.getParam("r").getData();
		if(!(radius == 0.0)){
			// the beginning and end of the segment are different... store the line.
			// store the line more permenently (paramSet and type)
			AvoGlobal.pushWorkFeatToSet();
		}else{
			System.out.println("radius was zero?!?!");
		}
	}

	public void glDrawFeature(GL gl, ParamSet p) {
		GLDynPrim.circle2D(gl, (Point2D)p.getParam("c").getData(), (Double)p.getParam("r").getData(), 0.0);
	}
	
}
