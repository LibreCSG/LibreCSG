package ui.menuet;

import ui.tools.DD.Tool2DCancel;
import ui.tools.DD.Tool2DCircle;
import ui.tools.DD.Tool2DDone;
import ui.tools.DD.Tool2DLine;
import ui.tools.main.ToolMain2D;
import backend.global.AvoGlobal;

public class MenuetBuilder {

	/**
	 * Build all of the tools into the menuet.
	 * Nothing fancy, just add them all along 
	 * with appropriate labels.
	 * @param menuet
	 */
	public static void buildMenuet(Menuet menuet){
		
		//
		//  TOOL MODE:  2D
		//
		new Tool2DDone(menuet);
		new Tool2DCancel(menuet);
		
		MELabel l1 = new MELabel(menuet,AvoGlobal.MENUET_MODE_2D);
		l1.meColorBackground = AvoGlobal.COLOR_MENUET_2D;
		l1.meLabel = "2D";
		l1.textIsBold = true;
		
		new Tool2DLine(menuet);
		new Tool2DCircle(menuet);
		
		//
		//  TOOL MODE:  Main
		//
		new ToolMain2D(menuet);
		
		
	}
	
}
