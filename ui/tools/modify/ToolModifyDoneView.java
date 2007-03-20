package ui.tools.modify;

import ui.menuet.MEButtonDone;
import ui.menuet.Menuet;
import ui.tools.ToolViewModify;

public class ToolModifyDoneView extends ToolViewModify{

	public ToolModifyDoneView(Menuet menuet){
		// initialize GUI elements
		mElement = new MEButtonDone(menuet, this.getToolMode(), this);
		mElement.setToolTipText("Finish working in the Modify mode.");
		
		this.applyToolGroupSettings();	// APPLY SKETCH GROUP SETTINGS
	}	
	
	@Override
	public void toolSelected() {
		changeMenuetToolMode(Menuet.MENUET_MODE_PART, new ToolModifyDoneCtrl());	
	}

}
