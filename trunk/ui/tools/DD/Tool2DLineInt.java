package ui.tools.DD;

import java.util.Iterator;

import javax.media.opengl.GL;

import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.global.AvoGlobal;
import backend.model.Feature;
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
public class Tool2DLineInt implements ToolInterface {

	/**
	 * All of the tool's main functionality
	 * mouse handling, glView drawing, 
	 * parameter storage, etc.
	 *
	 */
	public Tool2DLineInt(){		
	}
	
	public void glMouseDown(double x, double y, double z, int mouseX, int mouseY) {
		System.out.println("mousedown in line: x,y=" + x + "," + y);
		
		//
		// Build parameter set for 2D Line
		//
		ParamSet pSet = new ParamSet();
		pSet.addParam("a", new Param("Pt.A", new Point2D(x,y)));
		pSet.addParam("b", new Param("Pt.B", new Point2D(x,y)));
		
		//
		// set the workingFeature to the 2D Line
		//
		AvoGlobal.workingFeature = new Feature(this, pSet);
	}

	public void glMouseDrag(double x, double y, double z, int mouseX, int mouseY) {
		//System.out.println("mousemove in line: x,y=" + x + "," + y);
		AvoGlobal.workingFeature.paramSet.changeParam("b", new Point2D(x,y));
		
	}

	public void glMouseUp(double x, double y, double z, int mouseX, int mouseY) {
		System.out.println("mouseup in line: x,y=" + x + "," + y);
		Iterator allP = AvoGlobal.workingFeature.paramSet.getIterator();
		while(allP.hasNext()){
			Param p = (Param)allP.next();
			System.out.println("param ** TYPE:" + p.getType() + " \tLABEL:" + p.getLabel() + " \tDATA:" + p.getData().toString());
		}
		
		// * finalize line's formation
		AvoGlobal.workingFeature.paramSet.changeParam("b", new Point2D(x,y));
		
		// * store permanently in model
		Point2D ptA = (Point2D)AvoGlobal.workingFeature.paramSet.getParam("a").getData();
		Point2D ptB = (Point2D)AvoGlobal.workingFeature.paramSet.getParam("b").getData();
		if(! ptA.equals(ptB)){
			// the beginning and end of the segment are different... store the line.
			// TODO: store the line more permenently (paramSet and type)
			
		}
	}

	public void glDrawFeature(GL gl, ParamSet p) {
		gl.glColor4f(1.0f,0.5f,0.0f, 1.0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f((float)((Point2D)p.getParam("a").getData()).getX(), (float)((Point2D)p.getParam("a").getData()).getY(), 0.0f);
			gl.glVertex3f((float)((Point2D)p.getParam("b").getData()).getX(), (float)((Point2D)p.getParam("b").getData()).getY(), 0.0f);
		gl.glEnd();
	}
}
