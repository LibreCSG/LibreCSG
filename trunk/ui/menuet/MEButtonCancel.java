package ui.menuet;

import backend.data.utilities.ImageUtils;
import backend.global.AvoColors;
import ui.tools.ToolView;



/**
 * standardized Menuet Button for "CANCEL"
 */
public class MEButtonCancel extends MEButton{

	public final static int preferredHeight = 25;
	
	/**
	 * standardized Menuet Button for "CANCEL"
	 */
	public MEButtonCancel(Menuet menuet, int mode, ToolView toolView) {
		super(menuet, mode, toolView, false);
		this.mePreferredHeight = preferredHeight;
		this.meColorMouseOver  = AvoColors.COLOR_MENUET_CNCL_MO;
		this.meColorUnselected = AvoColors.COLOR_MENUET_CNCL_US; 
		this.meLabel = "Cancel";
		this.meIcon = ImageUtils.getIcon("./menuet/Done.png", 24, 24);
		this.meDispOptions = MenuetElement.ME_TEXT_ONLY;;
	}

}
