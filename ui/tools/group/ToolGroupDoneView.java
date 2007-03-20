package ui.tools.group;

import ui.menuet.MEButtonDone;
import ui.menuet.Menuet;
import ui.tools.ToolViewGroup;

public class ToolGroupDoneView extends ToolViewGroup{

	public ToolGroupDoneView(Menuet menuet){
		// initialize GUI elements
		mElement = new MEButtonDone(menuet, this.getToolMode(), this);
		mElement.setToolTipText("Finish working in the Group mode.");
		
		this.applyToolGroupSettings();	// APPLY SKETCH GROUP SETTINGS
	}
	
	@Override
	public void toolSelected() {
		changeMenuetToolMode(Menuet.MENUET_MODE_PROJECT, new ToolGroupDoneCtrl());
	}

}
