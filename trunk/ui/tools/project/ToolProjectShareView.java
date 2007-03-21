package ui.tools.project;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.ToolViewProject;
import backend.data.utilities.ImageUtils;

public class ToolProjectShareView extends ToolViewProject{

	public ToolProjectShareView(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode(), this, false);
		mElement.mePreferredHeight = 100;
		mElement.meLabel = "Share";
		mElement.meIcon = ImageUtils.getIcon("menuet/MAIN_2Dto3D.png", 24, 24);
		mElement.setToolTipText("Share the project with others.");
		mElement.meDispOptions = MenuetElement.ME_TRY_TEXT;
		
		this.applyToolGroupSettings();	// APPLY MAIN GROUP SETTINGS
	}
	
	@Override
	public void toolSelected() {
		changeMenuetToolMode(Menuet.MENUET_MODE_SHARE, new ToolProjectShareCtrl());		
	}
	
}
