package ui.tools.part;

import ui.menuet.MEButtonDone;
import ui.menuet.Menuet;
import ui.tools.ToolViewPart;

public class ToolPartDoneView extends ToolViewPart{

	public ToolPartDoneView(Menuet menuet){
		// initialize GUI elements
		mElement = new MEButtonDone(menuet, this.getToolMode(), this);
		mElement.setToolTipText("Finish working in the Part mode.");
		
		this.applyToolGroupSettings();	// APPLY SKETCH GROUP SETTINGS
	}
	
	@Override
	public void toolSelected() {
		changeMenuetToolMode(Menuet.MENUET_MODE_GROUP, new ToolPartDoneCtrl());		
	}

}
