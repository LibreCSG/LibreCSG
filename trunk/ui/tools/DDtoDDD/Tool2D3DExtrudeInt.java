package ui.tools.DDtoDDD;

import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolInterface2D3D;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.global.AvoGlobal;
import backend.model.Sketch;


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
public class Tool2D3DExtrudeInt implements ToolInterface2D3D{

	public void buildDerivedParams(ParamSet pSet) {
	}

	public void glMouseDown(double x, double y, double z, MouseEvent e) {
		System.out.println("got extrude mouse down!");
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null){
			sketch.selectRegionsThatContainsPoint(new Point2D(x,y));
		}
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseUp(double x, double y, double z, MouseEvent e) {
	}

}
