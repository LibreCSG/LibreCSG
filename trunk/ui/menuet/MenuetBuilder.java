package ui.menuet;

import ui.tools.build.ToolBuildDoneView;
import ui.tools.build.ToolBuildExtrudeView;
import ui.tools.build.ToolBuildRevolveView;
import ui.tools.part.ToolPartBuildView;
import ui.tools.part.ToolPartSketchView;
import ui.tools.sketch.ToolSketchCancelView;
import ui.tools.sketch.ToolSketchCircleView;
import ui.tools.sketch.ToolSketchDoneView;
import ui.tools.sketch.ToolSketchExampleView;
import ui.tools.sketch.ToolSketchLineView;
import ui.tools.sketch.ToolSketchRectView;
import ui.tools.sketch.ToolSketchSelectView;
import backend.global.AvoColors;
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
public class MenuetBuilder {

	/**
	 * Build all of the tools into the menuet.
	 * Nothing fancy, just add them all along 
	 * with appropriate labels.
	 * @param menuet
	 */
	public static void buildMenuet(Menuet menuet){
		
		//
		//  TOOL MODE:  Sketch
		//
		new ToolSketchDoneView(menuet);
		new ToolSketchCancelView(menuet);
		
		MELabel label2D = new MELabel(menuet,Menuet.MENUET_MODE_SKETCH, null);
		label2D.meLabel = "Sketch";
		label2D.textIsBold = true;
		
		new ToolSketchLineView(menuet);
		new ToolSketchCircleView(menuet);
		new ToolSketchRectView(menuet);
		new ToolSketchExampleView(menuet);
		
		
		new METoolbox(menuet,Menuet.MENUET_MODE_SKETCH);
		
		new ToolSketchSelectView(menuet);
		
		
		//
		//  TOOL MODE:  Build
		//
		new ToolBuildDoneView(menuet);
		
		MELabel label2D3D = new MELabel(menuet,Menuet.MENUET_MODE_BUILD, null);
		label2D3D.meLabel = "Build";
		label2D3D.textIsBold = true;
		
		new ToolBuildExtrudeView(menuet);
		new ToolBuildRevolveView(menuet);
		
		new METoolbox(menuet,Menuet.MENUET_MODE_BUILD);
		
		//
		//  TOOL MODE:  Part
		//
		MESpacer spacerPartDone = new MESpacer(menuet, Menuet.MENUET_MODE_PART, null);
		spacerPartDone.mePreferredHeight = 100;
		MELabel labelMAIN = new MELabel(menuet,Menuet.MENUET_MODE_PART, null);
		labelMAIN.meLabel = "Part";
		labelMAIN.textIsBold = true;
		
		new ToolPartSketchView(menuet);
		new ToolPartBuildView(menuet);
		AvoGlobal.menuet.currentToolMode = Menuet.MENUET_MODE_PART;		
		
	}
	
}
