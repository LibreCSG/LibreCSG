package ui.tools.tool2D;

import ui.tools.ToolModelSketch;
import backend.adt.ParamType;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.global.AvoGlobal;
import backend.model.Sketch;
import backend.model.sketch.Prim2DLine;
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
public class Tool2DLineModel implements ToolModelSketch{

	public Prim2DList buildPrim2DList(ParamSet paramSet) {
		try{
			Point2D ptA = paramSet.getParam("a").getDataPoint2D();
			Point2D ptB = paramSet.getParam("b").getDataPoint2D();
			Prim2DList primList = new Prim2DList();
			primList.add(new Prim2DLine(ptA,ptB));
			return primList;
		}catch(Exception ex){
			System.out.println(ex.getClass());
		}
		return null;
	}

	public boolean paramSetIsValid(ParamSet paramSet) {
		//		 ParamSet:  "Line"
		// --------------------------------
		// # "a"  ->  "Pt.A"     <Point2D>
		// # "b"  ->  "Pt.B"     <Point2D>
		// # "l"  ->  "Length"   <Double> @derived
		// --------------------------------		
		boolean isValid = (	paramSet != null &&
							paramSet.label == "Line" &&
							paramSet.hasParam("a", ParamType.Point2D) &&
							paramSet.hasParam("b", ParamType.Point2D) &&
							paramSet.hasParam("l", ParamType.Double));
		return isValid;
	}

	public void updateDerivedParams(ParamSet paramSet) {
		//
		// Build all derived parameters
		//
		try{
			Point2D ptA = paramSet.getParam("a").getDataPoint2D();
			Point2D ptB = paramSet.getParam("b").getDataPoint2D();
			paramSet.changeParam("l", ptA.computeDist(ptB));
		}catch(Exception ex){
			System.out.println(ex.getClass());
		}
		
	}

	public void finalize(ParamSet paramSet) {
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null){
			sketch.deselectAllFeat2D();
		}
	}

	public ParamSet constructNewParamSet() {
		ParamSet pSet = new ParamSet("Line", new Tool2DLineModel());
		pSet.addParam("a", new Param("Pt.A", new Point2D(0.0,0.0)));
		pSet.addParam("b", new Param("Pt.B", new Point2D(0.0,0.0)));
		Param dist = new Param("Length", 0.0);
		dist.setParamIsDerived(true);
		pSet.addParam("l", dist);
		return pSet;
	}

}
