package ui.tools.group;

import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.ToolViewGroup;
import backend.data.utilities.ImageUtils;
import backend.global.AvoColors;

public class ToolGroupPartView extends ToolViewGroup{

	public ToolGroupPartView(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode(), this, false);
		mElement.mePreferredHeight = 100;
		mElement.meLabel = "Part";
		mElement.meColorUnselected = AvoColors.COLOR_MENUET_PART;
		mElement.meColorMouseOver  = AvoColors.COLOR_MENUET_PART_LIGHT;
		mElement.meIcon = ImageUtils.getIcon("menuet/MAIN_2Dto3D.png", 24, 24);
		mElement.setToolTipText("Add a new Part to the group.");
		mElement.meDispOptions = MenuetElement.ME_TRY_TEXT;
		
		this.applyToolGroupSettings();	// APPLY MAIN GROUP SETTINGS
	}
	
	@Override
	public void toolSelected() {
		changeMenuetToolMode(Menuet.MENUET_MODE_PART, new ToolGroupPartCtrl());	
	}

}
