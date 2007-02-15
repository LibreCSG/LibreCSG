package ui.tools.DD;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolInterface2D;
import backend.adt.ParamSet;
import backend.global.AvoGlobal;
import backend.model.Sketch;
import backend.primatives.Prim2D;


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
public class Tool2DSelectInt implements ToolInterface2D {
	
	boolean shiftIsDown = false;
	
	/**
	 * All of the tool's main functionality
	 * mouse handling, glView drawing, 
	 * parameter storage, etc.
	 *
	 */
	public Tool2DSelectInt(){		
	}
	
	public void glMouseDown(double x, double y, double z,  MouseEvent e) {
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null){		
			
			if((e.stateMask & SWT.SHIFT) != 0){
				shiftIsDown = true;
			}else{
				shiftIsDown = false;
			}
			if(!shiftIsDown){
				
				sketch.deselectAllFeat2D();
			}
	
			// TODO: iterate over all features in the current set to see if they've been clicked

		}
	}

	public void glMouseDrag(double x, double y, double z,  MouseEvent e) {
	}

	public void glMouseUp(double x, double y, double z,  MouseEvent e) {
	}

	public LinkedList<Prim2D> buildPrimList(ParamSet p) {
		return null;
	}

	public void buildDerivedParams(ParamSet pSet) {
	}

}
