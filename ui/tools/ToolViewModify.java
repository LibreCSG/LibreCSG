package ui.tools;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import ui.menuet.Menuet;
import backend.global.AvoColors;


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
* @created Mar. 2007
*/
public abstract class ToolViewModify extends ToolView{

	/**
	 * This is applied only after the MenuetElement
	 * has been created to add on functionality
	 * common to all tools in the gorup.
	 */
	public void applyToolGroupSettings() {
		if(mElement != null){
			mElement.meColorBackground = AvoColors.COLOR_MENUET_MODIFY;
			mElement.addMouseListener(new MouseListener(){
				public void mouseDoubleClick(MouseEvent e) {
				}
				public void mouseDown(MouseEvent e) {
					toolSelected();
				}
				public void mouseUp(MouseEvent e) {
				}				
			});
		}
	}

	public int getToolMode() {
		return Menuet.MENUET_MODE_MODIFY;
	}
	
}
