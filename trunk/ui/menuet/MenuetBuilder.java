package ui.menuet;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;

import ui.tools.build.ToolBuildDoneView;
import ui.tools.build.ToolBuildExtrudeView;
import ui.tools.build.ToolBuildRevolveView;
import ui.tools.group.ToolGroupDoneView;
import ui.tools.group.ToolGroupPartView;
import ui.tools.modify.ToolModifyDoneView;
import ui.tools.part.ToolPartBuildView;
import ui.tools.part.ToolPartDoneView;
import ui.tools.part.ToolPartModifyView;
import ui.tools.part.ToolPartSketchView;
import ui.tools.project.ToolProjectGroupView;
import ui.tools.project.ToolProjectShareView;
import ui.tools.share.ToolShareDoneView;
import ui.tools.sketch.ToolSketchCancelView;
import ui.tools.sketch.ToolSketchCircleView;
import ui.tools.sketch.ToolSketchDoneView;
import ui.tools.sketch.ToolSketchExampleView;
import ui.tools.sketch.ToolSketchLineView;
import ui.tools.sketch.ToolSketchRectView;
import ui.tools.sketch.ToolSketchSelectView;
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
		
		MELabel labelSketch = new MELabel(menuet,Menuet.MENUET_MODE_SKETCH);
		labelSketch.meLabel = "Sketch";
		labelSketch.textIsBold = true;
		
		new ToolSketchLineView(menuet);
		new ToolSketchCircleView(menuet);
		new ToolSketchRectView(menuet);
		new ToolSketchExampleView(menuet);
		
		new METoolbox(menuet,Menuet.MENUET_MODE_SKETCH);
		
		new ToolSketchSelectView(menuet);
		menuet.setDefaultTool(3, Menuet.MENUET_MODE_SKETCH);
		
		//
		//  TOOL MODE:  Build
		//
		new ToolBuildDoneView(menuet);
		
		MESpacer spacerBuildCancel = new MESpacer(menuet, Menuet.MENUET_MODE_BUILD);
		spacerBuildCancel.mePreferredHeight = MEButtonCancel.preferredHeight;
		MELabel labelBuild = new MELabel(menuet,Menuet.MENUET_MODE_BUILD);
		labelBuild.meLabel = "Build";
		labelBuild.textIsBold = true;
		
		new ToolBuildExtrudeView(menuet);
		new ToolBuildRevolveView(menuet);
		
		new METoolbox(menuet,Menuet.MENUET_MODE_BUILD);
		
		
		//
		// TOOL MODE: Modify
		//
		new ToolModifyDoneView(menuet);
		MESpacer spacerModifyCancel = new MESpacer(menuet, Menuet.MENUET_MODE_MODIFY);
		spacerModifyCancel.mePreferredHeight = MEButtonCancel.preferredHeight;
		MELabel labelModify = new MELabel(menuet,Menuet.MENUET_MODE_MODIFY);
		labelModify.meLabel = "Modify";
		labelModify.textIsBold = true;
		
		new METoolbox(menuet,Menuet.MENUET_MODE_MODIFY);
		
		//
		//  TOOL MODE:  Part
		//
		new ToolPartDoneView(menuet);
		
		MESpacer spacerPartCancel = new MESpacer(menuet, Menuet.MENUET_MODE_PART);
		spacerPartCancel.mePreferredHeight = MEButtonCancel.preferredHeight;
		MELabel labelPart = new MELabel(menuet,Menuet.MENUET_MODE_PART);
		labelPart.meLabel = "Part";
		labelPart.textIsBold = true;
		
		new ToolPartSketchView(menuet);
		new ToolPartBuildView(menuet);
		new ToolPartModifyView(menuet);
		
		//
		//  TOOL MODE: Project
		//
		
		MESpacerAvoCADo spacerAvoCADo = new MESpacerAvoCADo(menuet, Menuet.MENUET_MODE_PROJECT); 
		spacerAvoCADo.spacer.mePreferredHeight = MEButtonDone.preferredHeight;
		MESpacer spacerProjectCancel = new MESpacer(menuet, Menuet.MENUET_MODE_PROJECT);
		spacerProjectCancel.mePreferredHeight = MEButtonCancel.preferredHeight;
		MELabel labelProject = new MELabel(menuet,Menuet.MENUET_MODE_PROJECT);
		labelProject.meLabel = "Project";
		labelProject.textIsBold = true;
		
		new ToolProjectGroupView(menuet);
		new ToolProjectShareView(menuet);
		
		//
		//  TOOL MODE: Group
		//
		new ToolGroupDoneView(menuet);

		MESpacer spacerGroupCancel = new MESpacer(menuet, Menuet.MENUET_MODE_GROUP);
		spacerGroupCancel.mePreferredHeight = MEButtonCancel.preferredHeight;
		MELabel labelGroup = new MELabel(menuet,Menuet.MENUET_MODE_GROUP);
		labelGroup.meLabel = "Group";
		labelGroup.textIsBold = true;
		
		new ToolGroupPartView(menuet);
		
		
		//
		//  TOOL MODE: Share
		//
		new ToolShareDoneView(menuet);
		
		MESpacer spacerShareCancel = new MESpacer(menuet, Menuet.MENUET_MODE_SHARE);
		spacerShareCancel.mePreferredHeight = MEButtonCancel.preferredHeight;
		MELabel labelShare = new MELabel(menuet,Menuet.MENUET_MODE_SHARE);
		labelShare.meLabel = "Share";
		labelShare.textIsBold = true;
				
		
		//
		// Setup Menuet for initial viewing...
		//
		AvoGlobal.menuet.currentToolMode = Menuet.MENUET_MODE_PROJECT;
	}
	
}
