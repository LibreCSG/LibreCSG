package ui.tools.sketch;

import net.sf.avocado_cad.eclipse.ui.AvoGlobal;
import net.sf.avocado_cad.model.api.adt.IParamSet;
import net.sf.avocado_cad.model.api.adt.ParamType;
import ui.tools.ToolModelSketch;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.model.Sketch;
import backend.model.sketch.Prim2DLine;
import backend.model.sketch.Prim2DList;


//
//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the 
//GNU General Public License (GPL).
//
//This file is part of avoCADo.
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
public class ToolSketchLineModel implements ToolModelSketch{

	public Prim2DList buildPrim2DList(IParamSet paramSet) {
		try{
			Point2D ptA = (Point2D) paramSet.getParam("a").getDataPoint2D();
			Point2D ptB = (Point2D) paramSet.getParam("b").getDataPoint2D();
			Prim2DList primList = new Prim2DList();
			primList.add(new Prim2DLine(ptA,ptB));
			return primList;
		}catch(Exception ex){
			System.out.println(ex.getClass());
		}
		return null;
	}

	public boolean paramSetIsValid(IParamSet paramSet) {
		//		 ParamSet:  "Line"
		// --------------------------------
		// # "a"  ->  "Pt.A"     <Point2D>
		// # "b"  ->  "Pt.B"     <Point2D>
		// # "l"  ->  "Length"   <Double> @derived
		// --------------------------------		
		boolean isValid = (	paramSet != null &&
							"Line".equals(paramSet.getLabel()) &&
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
			Point2D ptA = (Point2D) paramSet.getParam("a").getDataPoint2D();
			Point2D ptB = (Point2D) paramSet.getParam("b").getDataPoint2D();
			paramSet.changeParam("l", ptA.computeDist(ptB));
		}catch(Exception ex){
			System.out.println(ex.getClass());
		}
		
	}

	public void finalize(ParamSet paramSet) {
		Sketch sketch = (Sketch) AvoGlobal.project.getActiveSketch();
		if(sketch != null){
			sketch.deselectAllFeat2D();
		}
	}

	public ParamSet constructNewParamSet() {
		ParamSet pSet = new ParamSet("Line");
		pSet.addParam("a", new Param("Pt.A", new Point2D(0.0,0.0)));
		pSet.addParam("b", new Param("Pt.B", new Point2D(0.0,0.0)));
		Param dist = new Param("Length", 0.0);
		dist.setParamIsDerived(true);
		pSet.addParam("l", dist);
		AvoGlobal.paramSetToolModels.put(pSet, new ToolSketchLineModel());
		return pSet;
	}

	public boolean isWorthKeeping(IParamSet paramSet) {
		// TODO: check for isWorthKeeping! 
		return true;
	}
}
