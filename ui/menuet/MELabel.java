package ui.menuet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

//
//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the 
//GNU General Public License (GPL).
//
//This file is part of avoADo.
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
* @created Feb. 2007
*/
public class MELabel extends MenuetElement{

	
	public boolean textIsBold = false;
	
	public MELabel(Menuet menuet, int mode){
		super(menuet);		
		// let menuet know about the new element
		menuet.addMenuetElement(this, mode);
		this.mePreferredHeight = 30;
	}
	
	
	@Override
	void paintElement(PaintEvent e) {
		GC g = e.gc;
		g.setBackground(this.meColorBackground);
		int width  = this.getBounds().width;
		int height = this.getBounds().height;
		
		// clear entire canvas where button will be painted
		g.fillRectangle(0, 0, width, height);
		
		// draw text
		g.setForeground(this.meColorForeground);
		FontData fd = new FontData();
		fd.setHeight(10);
		if(textIsBold){
			fd.setStyle(SWT.BOLD);
		}else{
			fd.setStyle(SWT.NORMAL);
		}
		g.setFont(new Font(this.getDisplay(), fd));
		Point textPt = g.textExtent(this.meLabel);			
		g.drawText(this.meLabel, (width-textPt.x)/2, (height-textPt.y)/2);
	}

	@Override
	public int getMinDisplayHeight(int width) {
		return 20;
	}
	
	
}
