package ui.tools.main;

import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolInterfaceMain;
import backend.adt.PType;
import backend.adt.Param;
import backend.adt.ParamNotFoundException;
import backend.adt.ParamSet;
import backend.adt.Point3D;
import backend.adt.Rotation3D;


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
public class ToolMain2DInt implements ToolInterfaceMain{

	// The Sketch Creation/Param Management interface	
	Point3D    sketchOrigin;
	Rotation3D sketchRotation;
	
	public void glMouseDown(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseUp(double x, double y, double z, MouseEvent e) {
	}

	public void loadParamsAndUpdateState(ParamSet pSet) throws ParamNotFoundException {
		// ParamSet:  "Sketch"
		//
		// # "o"  ->  "Offset"    <Point3D>
		// # "r"  ->  "Rotation"  <Rotation3D>
		
		//
		//  Verify that param set is valid and load it into local state.
		//
		if(pSet != null){
			if(pSet.label != "Sketch"){
				throw new ParamNotFoundException();
			}
			
			Param paramO = pSet.getParam("o");
			if(paramO.getType() != PType.Point3D){
				throw new ParamNotFoundException();
			}
			sketchOrigin = (Point3D)paramO.getData();
			
			Param paramR = pSet.getParam("r");
			if(paramR.getType() != PType.Rotation3D){
				throw new ParamNotFoundException();
			}
			sketchRotation = (Rotation3D)paramR.getData();
		}	
	}

	public void modifyParamsFromState(ParamSet pSet) throws ParamNotFoundException {
		if(pSet != null){
			if(pSet.label != "Sketch"){
				throw new ParamNotFoundException();
			}
			
			Param paramO = pSet.getParam("o");
			if(paramO.getType() != PType.Point3D){
				throw new ParamNotFoundException();
			}
			paramO.change(sketchOrigin);
			
			Param paramR = pSet.getParam("r");
			if(paramR.getType() != PType.Rotation3D){
				throw new ParamNotFoundException();
			}
			paramR.change(sketchRotation);
		}		
	}

}
