package ui.tools.sketch;

import ui.tools.ToolModelSketch;
import backend.adt.ParamType;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.global.AvoGlobal;
import backend.model.Sketch;
import backend.model.sketch.Prim2DArc;
import backend.model.sketch.Prim2DList;


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
//along with AvoCADo; if not, write to the Free Softwares
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

/*
* @author  Adam Kumpf
* @created Feb. 2007
*/
public class Tool2DCircleModel implements ToolModelSketch {

	public Prim2DList buildPrim2DList(ParamSet paramSet) {
		try{
			Point2D ptCenter = paramSet.getParam("c").getDataPoint2D();
			double  radius   = paramSet.getParam("r").getDataDouble();
			Prim2DList primList = new Prim2DList();
			primList.add(new Prim2DArc(ptCenter,radius,0.0,360.0));
			return primList;
		}catch(Exception ex){
			System.out.println(ex.getClass());
		}
		return null;
	}

	public boolean paramSetIsValid(ParamSet paramSet) {
		//		 ParamSet:  "Circle"
		// --------------------------------
		// # "c"  ->  "Center"    <Point2D>
		// # "r"  ->  "Radius"    <Double>
		// --------------------------------		
		boolean isValid = (	paramSet != null &&
							paramSet.label == "Circle" &&
							paramSet.hasParam("c", ParamType.Point2D) &&
							paramSet.hasParam("r", ParamType.Double));
		return isValid;
	}


	public void updateDerivedParams(ParamSet paramSet) {	
		// no derived params for this feature.
	}


	public void finalize(ParamSet paramSet) {
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null){
			sketch.deselectAllFeat2D();
		}
	}

	public ParamSet constructNewParamSet() {
		ParamSet pSet = new ParamSet("Circle", this);
		pSet.addParam("c", new Param("Center", new Point2D(0.0,0.0)));
		pSet.addParam("r", new Param("Radius", 0.0));
		return pSet;
	}

}
