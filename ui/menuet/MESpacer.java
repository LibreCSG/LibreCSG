package ui.menuet;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

public class MESpacer extends MenuetElement{

	private boolean showImage = false;
	
	/**
	 * Menuet Element Spacer.
	 * @param menuet The menuet to add the spacer to
	 * @param mode The mode within the menuet to use
	 */
	public MESpacer(Menuet menuet, int mode){
		super(menuet, mode, null, false);		
		// let menuet know about the new element
		menuet.addMenuetElement(this, mode);
		this.mePreferredHeight = 30;
	}

	/**
	 * Menuet Element Spacer, with image shown
	 * @param menuet The menuet to add the spacer to
	 * @param mode The mode within the menuet to use
	 * @param icon The image to display.
	 */
	public MESpacer(Menuet menuet, int mode, Image icon){
		super(menuet, mode, null, false);		
		// let menuet know about the new element
		menuet.addMenuetElement(this, mode);
		this.mePreferredHeight = 30;
		if(icon != null){
			this.meIcon = icon;
		}
		showImage = true;
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
		
		if(showImage && meIcon != null){
			Rectangle b = meIcon.getBounds();
			int imageX = Math.max((width-b.width)/2, 1);
			int imageY = Math.max((height-b.height)/2, 1);
			g.drawImage(meIcon, imageX, imageY);
		}
	}

}
