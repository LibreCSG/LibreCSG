package ui.tools.share;

import ui.menuet.MEButtonDone;
import ui.menuet.Menuet;
import ui.tools.ToolViewShare;

public class ToolShareDoneView extends ToolViewShare{

	public ToolShareDoneView(Menuet menuet){
		// initialize GUI elements
		mElement = new MEButtonDone(menuet, this.getToolMode(), this);
		mElement.setToolTipText("Finish working in the Share mode.");
		
		this.applyToolGroupSettings();	// APPLY SKETCH GROUP SETTINGS
	}
	
	@Override
	public void toolSelected() {
		changeMenuetToolMode(Menuet.MENUET_MODE_PROJECT, new ToolShareDoneCtrl());
	}

}
