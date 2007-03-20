package ui.menuet;

import backend.data.utilities.ImageUtils;
import backend.global.AvoColors;
import ui.tools.ToolView;



/**
 * standardized Menuet Button for "DONE"
 */
public class MEButtonDone extends MEButton{

	/**
	 * standardized Menuet Button for "DONE"
	 */
	public MEButtonDone(Menuet menuet, int mode, ToolView toolView) {
		super(menuet, mode, toolView, false);
		this.mePreferredHeight = 100;
		this.meColorMouseOver  = AvoColors.COLOR_MENUET_DONE_MO;
		this.meColorUnselected = AvoColors.COLOR_MENUET_DONE_US; 
		this.meLabel = "Done";
		this.meIcon = ImageUtils.getIcon("./menuet/Done.png", 24, 24);
		this.meDispOptions = MenuetElement.ME_TRY_ICON;
	}

}
