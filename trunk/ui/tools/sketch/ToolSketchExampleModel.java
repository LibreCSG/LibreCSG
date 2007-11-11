package ui.tools.sketch;

import ui.tools.ToolModelSketch;
import backend.adt.ParamType;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.global.AvoGlobal;
import backend.model.Sketch;
import backend.model.sketch.Prim2DArc;
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
//along with AvoCADo; if not, write to the Free Software
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

/*
* @author  Adam Kumpf
* @created Feb. 2007
*/
public class ToolSketchExampleModel implements ToolModelSketch{

	public Prim2DList buildPrim2DList(ParamSet paramSet) {
		try{
			Point2D ptC  = paramSet.getParam("c").getDataPoint2D();
			double size = paramSet.getParam("s").getDataDouble();
			int triangles = paramSet.getParam("t").getDataInteger();
			if(triangles >= 2){
				Prim2DList primList = new Prim2DList();
				Point2D arm = new Point2D(size,0.0);
				double degPerTri = 360.0 / (double)(2*triangles);
				for(int i=0; i< triangles; i++){
					primList.add(new Prim2DLine(ptC,arm.getNewRotatedPt(2*i*degPerTri).addPt(ptC)));
					primList.add(new Prim2DLine(arm.getNewRotatedPt((2*i+1)*degPerTri).addPt(ptC),arm.getNewRotatedPt(2*i*degPerTri).addPt(ptC)));
					primList.add(new Prim2DLine(ptC,arm.getNewRotatedPt((2*i+1)*degPerTri).addPt(ptC)));
				}
				primList.add(new Prim2DArc(ptC, 1.2*size, 0.0, 360.0));
				primList.add(new Prim2DArc(ptC, 1.25*size, 0.0, 360.0));
				return primList;
			}
		}catch(Exception ex){
			System.out.println(ex.getClass());
		}
		return null;
	}

	public boolean paramSetIsValid(ParamSet paramSet) {
		//		 ParamSet:  "Rectangle"
		// --------------------------------
		// # "c"  ->  "Center"    <Point2D>
		// # "s"  ->  "Size"      <Double>
		// # "t"  ->  "Triangles" <Integer>
		// --------------------------------		
		boolean isValid = (	paramSet != null &&
							paramSet.label == "Example" &&
							paramSet.hasParam("c", ParamType.Point2D) &&
							paramSet.hasParam("s", ParamType.Double) &&
							paramSet.hasParam("t", ParamType.Integer));
		return isValid;
	}

	public void updateDerivedParams(ParamSet paramSet) {		
	}

	public void finalize(ParamSet paramSet) {
		Sketch sketch = AvoGlobal.project.getActiveSketch();
		if(sketch != null){
			sketch.deselectAllFeat2D();
		}
	}

	public ParamSet constructNewParamSet() {
		ParamSet pSet = new ParamSet("Example", new ToolSketchExampleModel());
		pSet.addParam("c", new Param("Center", new Point2D(0.0,0.0)));
		pSet.addParam("s", new Param("Size", 4.0));
		pSet.addParam("t", new Param("Triangles", 9));
		return pSet;
	}
	
	public boolean isWorthKeeping(ParamSet paramSet) {
		// TODO: check for isWorthKeeping! 
		return true;
	}
}
