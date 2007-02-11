package ui.tools.DD;

import java.util.LinkedList;

import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolInterface2D;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.global.AvoGlobal;
import backend.model.Feature2D;
import backend.primatives.Prim2D;
import backend.primatives.Prim2DLine;


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
public class Tool2DLineInt implements ToolInterface2D {

	/**
	 * All of the tool's main functionality
	 * mouse handling, glView drawing, 
	 * parameter storage, etc.
	 *
	 */
	public Tool2DLineInt(){		
	}
	
	public void glMouseDown(double x, double y, double z,  MouseEvent e) {
		//
		// starting to draw a new feature... deselect all other features.
		//
		AvoGlobal.getFeatureSet().deselectAll();
		
		//
		// Build parameter set for this feature
		//
		ParamSet pSet = new ParamSet();
		pSet.addParam("a", new Param("Pt.A", new Point2D(x,y)));
		pSet.addParam("b", new Param("Pt.B", new Point2D(x,y)));
		Param dist = new Param("Dist", 0.0);
		dist.setParamIsDerived(true);
		pSet.addParam("d", dist);
		
		//
		// add the new feature to the end of the feature set
		//
		AvoGlobal.getFeatureSet().addFeature(new Feature2D(this, pSet, "Line"));
	}

	public void glMouseDrag(double x, double y, double z,  MouseEvent e) {
		//
		// get parameter set
		//
		ParamSet paramSet = AvoGlobal.getFeatureSet().getLastFeature().paramSet;
		
		//
		// update param values
		//
		paramSet.changeParam("b", new Point2D(x,y));
		Point2D ptA = (Point2D)paramSet.getParam("a").getData();
		Point2D ptB = (Point2D)paramSet.getParam("b").getData();
		paramSet.changeParam("d", ptA.computeDist(ptB));
	}

	public void glMouseUp(double x, double y, double z,  MouseEvent e) {
		//
		// get parameter set
		//
		ParamSet paramSet = AvoGlobal.getFeatureSet().getLastFeature().paramSet;
		
		//
		// finalize the feature's formation
		//
		paramSet.changeParam("b", new Point2D(x,y));
		Point2D ptA = (Point2D)paramSet.getParam("a").getData();
		Point2D ptB = (Point2D)paramSet.getParam("b").getData();
		paramSet.changeParam("d", ptA.computeDist(ptB));
		
		// * discard if start point is the same as the end point
		if(ptA.equalsPt(ptB)){
			// end point are the same... discard
			System.out.println("end points of line are the same... discarding feature");
			AvoGlobal.getFeatureSet().removeLastFeature();
		}
	}

	public LinkedList<Prim2D> buildPrimList(ParamSet p) {
		Point2D ptA = (Point2D)p.getParam("a").getData();
		Point2D ptB = (Point2D)p.getParam("b").getData();
		LinkedList<Prim2D> ll = new LinkedList<Prim2D>();
		ll.add(new Prim2DLine(ptA,ptB));
		return ll;
	}


}
