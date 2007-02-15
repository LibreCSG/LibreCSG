package ui.tools.DD;

import java.util.LinkedList;

import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolInterface2D;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.global.AvoGlobal;
import backend.model.Feature2D;
import backend.model.Sketch;
import backend.primatives.Prim2D;
import backend.primatives.Prim2DArc;


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
public class Tool2DCircleInt implements ToolInterface2D  {

	/**
	 * All of the tool's main functionality
	 * mouse handling, glView drawing, 
	 * parameter storage, etc.
	 *
	 */
	public Tool2DCircleInt(){
	}
	
	
	public void glMouseDown(double x, double y, double z,  MouseEvent e) {
		//
		//TODO starting to draw a new feature... deselect all other features.
		//
		
		//
		// Build parameter set for this feature
		//
		ParamSet pSet = new ParamSet("Circle");
		pSet.addParam("c", new Param("center", new Point2D(x,y)));
		pSet.addParam("r", new Param("radius", 0.0));
		
		//
		// add the new feature to the end of the feature set
		//
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null){
			sketch.add(new Feature2D(this, pSet));
			AvoGlobal.paramDialog.setParamSet(pSet);
		}
	}

	public void glMouseDrag(double x, double y, double z,  MouseEvent e) {
		//
		// get parameter set
		//
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null){
			ParamSet paramSet = sketch.getAtIndex(sketch.getFeat2DListSize()-1).paramSet;
			
			//
			// update param values
			//
			Point2D ptC = (Point2D)paramSet.getParam("c").getData();
			paramSet.changeParam("r", ptC.computeDist(new Point2D(x,y)));
		}
	}

	public void glMouseUp(double x, double y, double z,  MouseEvent e) {	
		//
		// get parameter set
		//
		ParamSet paramSet = AvoGlobal.assembly.partList.getLast().feat3DList.getLast().feat2DList.getLast().paramSet;
		
		//
		// finalize the feature's formation
		//
		Point2D ptC = (Point2D)paramSet.getParam("c").getData();
		paramSet.changeParam("r", ptC.computeDist(new Point2D(x,y)));	
		
		// * store permanently in model
		double radius = (Double)paramSet.getParam("r").getData();
		if(radius == 0.0){
			System.out.println("radius was zero... feature discarded");
			AvoGlobal.assembly.partList.getLast().feat3DList.getLast().feat2DList.removeLast();
			AvoGlobal.setActiveParamSet(null);
		}
	}

	public LinkedList<Prim2D> buildPrimList(ParamSet p) {
		Point2D ptC = (Point2D)p.getParam("c").getData();
		double    r = (Double)p.getParam("r").getData();
		LinkedList<Prim2D> ll = new LinkedList<Prim2D>();
		ll.add(new Prim2DArc(ptC,r,0.0,360.0));
		return ll;
	}
}
