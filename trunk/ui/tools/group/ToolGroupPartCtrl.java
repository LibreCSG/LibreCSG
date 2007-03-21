package ui.tools.group;

import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolCtrlGroup;
import backend.global.AvoGlobal;
import backend.model.Group;


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
public class ToolGroupPartCtrl implements ToolCtrlGroup{

	public void glMouseDown(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseUp(double x, double y, double z, MouseEvent e) {
	}

	public void menuetElementDeselected() {
	}

	public void menuetElementSelected() {
		// Add a new Part to the Group
		Group group = AvoGlobal.project.getActiveGroup();
		if(group != null){
			group.addNewPart();
		}else{
			System.out.println("ToolGroupPartCtrl(menuetElementSelected): No group was active? Cannot add new Part!");
		}
	}

}
