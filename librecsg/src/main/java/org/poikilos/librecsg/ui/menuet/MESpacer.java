package org.poikilos.librecsg.ui.menuet;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;


//
//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the
//GNU General Public License (GPL).
//
//This file is part of avoCADo.
//
//AvoCADo is free software; you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation; either version 2 of the License, or
//(at your option) any later version.
//
//AvoCADo is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with AvoCADo; if not, write to the Free Software
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

/*
* @author  Adam Kumpf
* @created Mar. 2007
*/
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
