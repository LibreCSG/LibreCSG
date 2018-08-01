package org.poikilos.librecsg.ui.utilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;


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
* @created Feb. 2007
*/
public class SWTUtils {

	/**
	 * Set Cursor to Normal (Arrow)
	 * @param c
	 */
	public static void setCursorNormal(Composite c){
		c.setCursor(null);
	}

	/**
	 * Set Cursor to Hand
	 * @param c
	 */
	public static void setCursorHand(Composite c){
		c.setCursor(new Cursor(c.getDisplay(),SWT.CURSOR_HAND));
	}

	/**
	 * Set Cursor to Wait
	 * @param c
	 */
	public static void setCursorWait(Composite c){
		c.setCursor(new Cursor(c.getDisplay(),SWT.CURSOR_WAIT));
	}

	/**
	 * Set Cursor to Crosshair
	 * @param c
	 */
	public static void setCursorCrosshair(Composite c){
		c.setCursor(new Cursor(c.getDisplay(),SWT.CURSOR_CROSS));
	}

	/**
	 * Set Cursor to Help
	 * @param c
	 */
	public static void setCursorHelp(Composite c){
		c.setCursor(new Cursor(c.getDisplay(),SWT.CURSOR_HELP));
	}

	/**
	 * Set Cursor to I-Beam
	 * @param c
	 */
	public static void setCursorIBeam(Composite c){
		c.setCursor(new Cursor(c.getDisplay(),SWT.CURSOR_IBEAM));
	}

	/**
	 * Get height of the shell's titlebar
	 * @param s
	 * @return
	 */
	public static int getShellTitlebarHeight(Shell s){
		Rectangle b = s.getBounds();
		Rectangle t = s.computeTrim(b.x, b.y, b.width, b.height);
		int tbarHeight = (b.y-t.y);
		return tbarHeight;
	}

	/**
	 * Get the width of the shell's left edge
	 * @param s
	 * @return
	 */
	public static int getShellLeftEdgeWidth(Shell s){
		Rectangle b = s.getBounds();
		Rectangle t = s.computeTrim(b.x, b.y, b.width, b.height);
		int leftEW = (b.x-t.x);
		if(s.getMaximized()){
			return 0;
		}else{
			return leftEW;
		}
	}

	/**
	 * Get the width of the shell's right edge
	 * @param s
	 * @return
	 */
	public static int getShellRightEdgeWidth(Shell s){
		Rectangle b = s.getBounds();
		Rectangle t = s.computeTrim(b.x, b.y, b.width, b.height);
		int rightEW = ((t.width-b.width)-(b.x-t.x));
		if(s.getMaximized()){
			return 0;
		}else{
			return rightEW;
		}
	}

	/**
	 * Get the height of the shell's bottom edge
	 * @param s
	 * @return
	 */
	public static int getShellBottomEdgeHeight(Shell s){
		Rectangle b = s.getBounds();
		Rectangle t = s.computeTrim(b.x, b.y, b.width, b.height);
		int bottomEH = ((t.height-b.height)-(b.y-t.y));
		if(s.getMaximized()){
			return 0;
		}else{
			return bottomEH;
		}
	}
}
