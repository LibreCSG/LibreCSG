package ui.tools.DD;

import java.util.Iterator;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.Tool2D;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.data.utilities.ImageUtils;
import backend.global.AvoGlobal;


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
public class Tool2DLine extends Tool2D{

	ParamSet pSet;
	
	public Tool2DLine(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode());
		mElement.mePreferredHieght = 50;
		mElement.meLabel = "Line";
		mElement.meIcon = ImageUtils.getIcon("menuet/2D_Line.png", 24, 24);
		mElement.setToolTipText("Line");
		mElement.mePriority = 0; 	// 0 = always show element, >5 = never show element
		mElement.meDispOptions = MenuetElement.ME_TRY_TEXT;
		
		this.applyToolGroupSettings();	// APPLY 2D GROUP SETTINGS
	}

	@Override
	public void toolSelected() {
		AvoGlobal.menuet.selectButton(mElement);
		AvoGlobal.currentTool = this;
	}

	@Override
	public void glMouseDown(double x, double y, double z, int mouseX, int mouseY) {
		// TODO Auto-generated method stub
		System.out.println("mousedown in line: x,y=" + x + "," + y);
		pSet = new ParamSet();
		pSet.addParam("c", new Param("center", new Point2D(x,y)));
		pSet.addParam("r", new Param("radius", 0.0));
	}

	@Override
	public void glMouseDrag(double x, double y, double z, int mouseX, int mouseY) {
		// TODO Auto-generated method stub	
		//System.out.println("mousemove in line: x,y=" + x + "," + y);
		double dist = ((Point2D)pSet.getParam("c").getData()).computeDist(new Point2D(x,y));
		pSet.changeParam("r", dist);
		System.out.println("Dist: " + dist);
	}

	@Override
	public void glMouseUp(double x, double y, double z, int mouseX, int mouseY) {
		// TODO Auto-generated method stub		
		System.out.println("mouseup in line: x,y=" + x + "," + y);
		Iterator allP = pSet.getIterator();
		while(allP.hasNext()){
			Param p = (Param)allP.next();
			System.out.println("param ** TYPE:" + p.getType() + " \tLABEL:" + p.getLabel() + " \tDATA:" + p.getData().toString());
		}
		
		// * finalize line's formation
		// * store permanently in model
	}
	
	
	
}

