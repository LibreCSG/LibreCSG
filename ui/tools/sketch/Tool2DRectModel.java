package ui.tools.sketch;

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
//along with AvoCADo; if not, write to the Free Software
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

/*
* @author  Adam Kumpf
* @created Feb. 2007
*/
public class Tool2DRectModel implements ToolModelSketch{

	public Prim2DList buildPrim2DList(ParamSet paramSet) {
		try{
			Point2D ptA  = paramSet.getParam("a").getDataPoint2D();
			Point2D ptB  = paramSet.getParam("b").getDataPoint2D();
			Point2D ptAB = new Point2D(ptA.getX(),ptB.getY());
			Point2D ptBA = new Point2D(ptB.getX(),ptA.getY());
			Prim2DList primList = new Prim2DList();
			primList.add(new Prim2DLine(ptA,ptAB));
			primList.add(new Prim2DLine(ptA,ptBA));
			primList.add(new Prim2DLine(ptB,ptAB));
			primList.add(new Prim2DLine(ptB,ptBA));
			return primList;
		}catch(Exception ex){
			System.out.println(ex.getClass());
		}
		return null;
	}

	public boolean paramSetIsValid(ParamSet paramSet) {
		//		 ParamSet:  "Rectangle"
		// --------------------------------
		// # "a"  ->  "Pt.A"    <Point2D>
		// # "b"  ->  "Pt.B"    <Point2D>
		// # "w"  ->  "Width"   <Double> @derived
		// # "h"  ->  "Height"  <Double> @derived
		// --------------------------------		
		boolean isValid = (	paramSet != null &&
							paramSet.label == "Rectangle" &&
							paramSet.hasParam("a", ParamType.Point2D) &&
							paramSet.hasParam("b", ParamType.Point2D) &&
							paramSet.hasParam("w", ParamType.Double) &&
							paramSet.hasParam("h", ParamType.Double));
		return isValid;
	}

	public void updateDerivedParams(ParamSet paramSet) {
		try{
			Point2D ptA = paramSet.getParam("a").getDataPoint2D();
			Point2D ptB = paramSet.getParam("b").getDataPoint2D();
			paramSet.changeParam("w", Math.abs(ptA.getX() - ptB.getX()));
			paramSet.changeParam("h", Math.abs(ptA.getY() - ptB.getY()));
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
		ParamSet pSet = new ParamSet("Rectangle", new Tool2DRectModel());
		pSet.addParam("a", new Param("Pt.A", new Point2D(0.0,0.0)));
		pSet.addParam("b", new Param("Pt.B", new Point2D(0.0,0.0)));
		Param width = new Param("Width", 0.0);
		Param height = new Param("Height", 0.0);
		width.setParamIsDerived(true);
		height.setParamIsDerived(true);
		pSet.addParam("w", width);
		pSet.addParam("h", height);
		return pSet;
	}

}
