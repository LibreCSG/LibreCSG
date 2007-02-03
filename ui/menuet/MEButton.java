package ui.menuet;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
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
public class MEButton extends MenuetElement{

	int MEB_ARC_RADIUS  = 20;
	int MEB_SIDE_SPACE  = 8;	// unconsumed space to right of button
	boolean mouseIsOver = false;
	
	public MEButton(Menuet menuet, int mode){
		super(menuet);
		
		// let menuet know about the new element
		menuet.addMenuetElement(this, mode); 
		
		this.addMouseTrackListener(new MouseTrackListener(){
			public void mouseEnter(MouseEvent e) {
				mouseIsOver = true;
				redraw();			
			}
			public void mouseExit(MouseEvent e) {
				mouseIsOver = false;
				redraw();
			}
			public void mouseHover(MouseEvent e) {
			}			
		});
		
	}

	void paintElement(PaintEvent e) {
		GC g = e.gc;
		g.setBackground(this.meColorBackground);
		int width  = this.getBounds().width;
		int height = this.getBounds().height;
		
		// clear entire canvas where button will be painted
		g.fillRectangle(0, 0, width, height);

		// draw button background
		int selShift = 0;
		if(mouseIsOver && !isSelected){
			g.setBackground(this.meColorMouseOver);
		}else{
			g.setBackground(this.meColorUnselected);
		}
		if(isSelected){
			selShift = MEB_SIDE_SPACE-1;
			g.setBackground(this.meColorSelected);
		}
		g.fillRoundRectangle(-MEB_ARC_RADIUS,0,width-MEB_SIDE_SPACE+selShift+MEB_ARC_RADIUS, height-1, MEB_ARC_RADIUS, MEB_ARC_RADIUS);
		
		// draw button outline
		g.setLineWidth(1);
		if(mouseIsOver){ // || isFocused, but not currently used
			g.setForeground(this.meColorFocused);
		}else{
			g.setForeground(this.meColorOutline2);
		}
		g.drawRoundRectangle(-(MEB_ARC_RADIUS-1),1,width-MEB_SIDE_SPACE+selShift+(MEB_ARC_RADIUS-1), height-3, MEB_ARC_RADIUS-1, MEB_ARC_RADIUS-1);
		g.drawRoundRectangle(-(MEB_ARC_RADIUS-1),1,width-MEB_SIDE_SPACE+selShift+(MEB_ARC_RADIUS-1)-1, height-3, MEB_ARC_RADIUS-1, MEB_ARC_RADIUS-1);
		g.drawRoundRectangle(-(MEB_ARC_RADIUS-1),1,width-MEB_SIDE_SPACE+selShift+(MEB_ARC_RADIUS-1)-2, height-3, MEB_ARC_RADIUS-1, MEB_ARC_RADIUS-1);
		g.setForeground(this.meColorOutline);
		g.setLineWidth(1);
		g.drawRoundRectangle(-MEB_ARC_RADIUS,0,width-MEB_SIDE_SPACE+selShift+MEB_ARC_RADIUS, height-1, MEB_ARC_RADIUS, MEB_ARC_RADIUS);
		
		//
		// handle position of Text/Icon within MEButton
		//
		int usableHeight = height-4;
		// draw button text
		g.setForeground(this.meColorForeground);
		FontData fd = new FontData();
		fd.setHeight(10);
		g.setFont(new Font(this.getDisplay(), fd));
		Point textPt = g.textExtent(this.meLabel);
		switch(this.meDispOptions){
			case(MenuetElement.ME_ICON_ONLY): 	{
				g.drawImage(this.meIcon, (width-MEB_SIDE_SPACE-this.meIcon.getBounds().width-1)/2, (height-this.meIcon.getBounds().height)/2);
				break;
			}
			case(MenuetElement.ME_TEXT_ONLY): 	{
				g.drawText(this.meLabel, (width-textPt.x-MEB_SIDE_SPACE-1)/2, (height-textPt.y)/2);
				break;
			}
			case(MenuetElement.ME_TRY_ICON): 	{
				if(usableHeight >= (textPt.y+this.meIcon.getBounds().height+1)){
					g.drawImage(this.meIcon, (width-MEB_SIDE_SPACE-this.meIcon.getBounds().width-1)/2, (height-(this.meIcon.getBounds().height+1+textPt.y))/2);
					g.drawText(this.meLabel, (width-textPt.x-MEB_SIDE_SPACE-1)/2, (height-(this.meIcon.getBounds().height+1+textPt.y))/2 + this.meIcon.getBounds().height+1);
				}else{
					g.drawText(this.meLabel, (width-textPt.x-MEB_SIDE_SPACE-1)/2, (height-textPt.y)/2);
				}
				break;
			}
			case(MenuetElement.ME_TRY_TEXT):	{
				if(usableHeight >= (textPt.y+this.meIcon.getBounds().height+1)){
					g.drawImage(this.meIcon, (width-MEB_SIDE_SPACE-this.meIcon.getBounds().width-1)/2, (height-(this.meIcon.getBounds().height+1+textPt.y))/2);
					g.drawText(this.meLabel, (width-textPt.x-MEB_SIDE_SPACE-1)/2, (height-(this.meIcon.getBounds().height+1+textPt.y))/2 + this.meIcon.getBounds().height+1);
				}else{
					g.drawImage(this.meIcon, (width-MEB_SIDE_SPACE-this.meIcon.getBounds().width-1)/2, (height-this.meIcon.getBounds().height)/2);
				}
				break;
			}
			default:{
				g.drawText(this.meLabel, (width-textPt.x-MEB_SIDE_SPACE-1)/2, (height-textPt.y)/2);
				break;
			}		
		}		
	}

	@Override
	public int getMinDisplayHeight(int width) {
		return 28;
	}
	
	
	
}
