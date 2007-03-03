package ui.menuet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import ui.tools.ToolView;
import backend.data.utilities.ImageUtils;


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
public abstract class MenuetElement extends Canvas{

	public final static int ME_ICON_ONLY  = 43071;
	public final static int ME_TRY_TEXT   = 43072;
	public final static int ME_TRY_ICON   = 43073;
	public final static int ME_TEXT_ONLY  = 43074;	
	
	final int ALIGN_TOP    = 0;
	//final int ALIGN_MIDDLE = 1; // no align middle
	final int ALIGN_BOTTOM = 2;
	
	
	public Image  meIcon     = null;
	public String meLabel    = "???";
	private int   meAlign    = ALIGN_TOP; 	// default alignment
	public  int   mePriority = 0; 			// zero=top priority, >0=less important, >5=don't show
	public int    meDispOptions = ME_TRY_TEXT;
	
	public Color meColorForeground = new Color(this.getDisplay(),   0,   0,   0);
	public Color meColorBackground = new Color(this.getDisplay(), 255, 255, 255);
	public Color meColorSelected   = new Color(this.getDisplay(), 210, 210, 210);
	public Color meColorUnselected = new Color(this.getDisplay(), 235, 235, 235);
	public Color meColorMouseOver  = new Color(this.getDisplay(), 240, 240, 240);	
	public Color meColorOutline    = new Color(this.getDisplay(),  96,  96,  96);
	public Color meColorOutline2   = new Color(this.getDisplay(), 150, 150, 150);
	public Color meColorFocused    = this.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
	
	public int mePreferredHeight = 50; // preferred height of element if possible.
	public boolean isSelected  = false;
	
	private boolean isShown = false;
	public final int toolMode;
	public final ToolView toolView;
	
	public final boolean isStoredInToolbox;
	
	public MenuetElement(Composite parent, int mode, ToolView toolView, boolean isStoredInToolbox) {
		super(parent, SWT.NONE);
		this.toolMode = mode;
		this.isStoredInToolbox = isStoredInToolbox;
		this.addPaintListener(new PaintListener(){
			public void paintControl(PaintEvent e) {
				paintElement(e);
			}			
		});		
		meIcon = ImageUtils.getIcon("", 24, 24); // load default icon
		this.toolView = toolView;
	}

	
	/**
	 * paint the element
	 * @param e
	 */
	abstract void paintElement(PaintEvent e);
	
	
	/**
	 * for a given width, return the lowest possible
	 * height that will still allow for the element
	 * to be displayed.
	 * @param width
	 * @return
	 */
	abstract public int getMinDisplayHeight(int width);
	
	/**
	 * align MenuetElement as close to the
	 * top of the Menuet on which it is placed
	 * as possible.
	 */
	public void meAlignToTop(){
		meAlign = ALIGN_TOP;
	}
	
	/**
	 * align MenuetElement as close to the
	 * middle of the Menuet on which it is placed
	 * as possible.
	 */
//	public void meAlignToMiddle(){
//		meAlign = ALIGN_MIDDLE;
//	}
	
	/**
	 * align MenuetElement as close to the
	 * bottom of the Menuet on which it is placed
	 * as possible.
	 */
	public void meAlignToBottom(){
		meAlign = ALIGN_BOTTOM;
	}	
	
	/**
	 * true if aligned to Top
	 * @return
	 */
	public boolean meIsAlignedTop(){
		return (meAlign == ALIGN_TOP);	
	}
	
	/**
	 * true if aligned to Middle
	 * @return
	 */	
//	public boolean meIsAlignedMiddle(){
//		return (meAlign == ALIGN_MIDDLE);
//	}
	
	/**
	 * true if aligned to Bottom
	 * @return
	 */	
	public boolean meIsAlignedBottom(){
		return (meAlign == ALIGN_BOTTOM);
	}
	
	
	public void setShown(boolean shown){
		isShown = shown;
	}
	
	public boolean meIsShown(){
		return isShown;
	}
	
}
