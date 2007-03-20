package ui.tools.project;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.ToolViewProject;
import backend.data.utilities.ImageUtils;
import backend.global.AvoColors;

public class ToolProjectGroupView extends ToolViewProject{

	public ToolProjectGroupView(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode(), this, false);
		mElement.mePreferredHeight = 100;
		mElement.meLabel = "Group";
		mElement.meColorUnselected = AvoColors.COLOR_MENUET_GROUP;
		mElement.meColorMouseOver  = AvoColors.COLOR_MENUET_GROUP_LIGHT;
		mElement.meIcon = ImageUtils.getIcon("menuet/MAIN_2Dto3D.png", 24, 24);
		mElement.setToolTipText("Add a new Group to the project.");
		mElement.meDispOptions = MenuetElement.ME_TRY_TEXT;
		
		this.applyToolGroupSettings();	// APPLY MAIN GROUP SETTINGS
	}
	
	@Override
	public void toolSelected() {
		changeMenuetToolMode(Menuet.MENUET_MODE_GROUP, new ToolProjectGroupCtrl());		
	}

}
