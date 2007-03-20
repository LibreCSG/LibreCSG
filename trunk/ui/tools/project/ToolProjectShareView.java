package ui.tools.project;

import backend.data.utilities.ImageUtils;
import backend.global.AvoColors;
import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.ToolViewProject;

public class ToolProjectShareView extends ToolViewProject{

	public ToolProjectShareView(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode(), this, false);
		mElement.mePreferredHeight = 100;
		mElement.meLabel = "Share";
		mElement.meColorUnselected = AvoColors.COLOR_MENUET_SHARE;
		mElement.meColorMouseOver  = AvoColors.COLOR_MENUET_SHARE_LIGHT;
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
