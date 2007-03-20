package ui.menuet;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;

import ui.tools.ToolView;

public class MESpacer extends MenuetElement{

	public MESpacer(Menuet menuet, int mode, ToolView toolView){
		super(menuet, mode, toolView, false);		
		// let menuet know about the new element
		menuet.addMenuetElement(this, mode);
		this.mePreferredHeight = 30;
	}

	@Override
	public int getMinDisplayHeight(int width) {
		return defaultMinDisplayHeight;
	}

	@Override
	void paintElement(PaintEvent e) {
		GC g = e.gc;
		g.setBackground(this.getBackground());
		int width  = this.getBounds().width;
		int height = this.getBounds().height;
		
		// clear entire canvas where button will be painted
		g.fillRectangle(0, 0, width, height);
	}

}
