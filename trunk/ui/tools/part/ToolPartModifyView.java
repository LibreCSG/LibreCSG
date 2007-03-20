package ui.tools.part;

import backend.data.utilities.ImageUtils;
import backend.global.AvoColors;
import ui.menuet.MEButton;
import ui.menuet.Menuet;
import ui.menuet.MenuetElement;
import ui.tools.ToolViewPart;

public class ToolPartModifyView extends ToolViewPart{

	public ToolPartModifyView(Menuet menuet){	
		
		// initialize GUI elements
		mElement = new MEButton(menuet, this.getToolMode(), this, false);
		mElement.mePreferredHeight = 100;
		mElement.meLabel = "Modify";
		mElement.meColorUnselected = AvoColors.COLOR_MENUET_MODIFY;
		mElement.meColorMouseOver  = AvoColors.COLOR_MENUET_MODIFY_LIGHT;
		mElement.meIcon = ImageUtils.getIcon("menuet/MAIN_2Dto3D.png", 24, 24);
		mElement.setToolTipText("Modify the current 3D shape.");
		mElement.meDispOptions = MenuetElement.ME_TRY_TEXT;
		
		this.applyToolGroupSettings();	// APPLY MAIN GROUP SETTINGS
	}	
	
	@Override
	public void toolSelected() {
		changeMenuetToolMode(Menuet.MENUET_MODE_MODIFY, new ToolPartModifyCtrl());
	}

}
