package main;

import org.eclipse.swt.widgets.Display;

import ui.shells.MainAvoCADoShell;
import ui.shells.StartupSplashShell;

//
// Copyright (C) 2007 avoCADo (Adam Kumpf creator)
// This code is distributed under the terms of the 
// GNU General Public License (GPL).
//
//    This file is part of avoADo.
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
 * @created Jan. 2007
 */

/**
 * The main avoCADo class..  <br/><br/>
 * 1. Initialize things <br/>
 * 2. Run the Application <br/>
 * 3. Finalize/Safely close things <br/>
 */
public class AvoCADo {

	final Display display;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("** Starting avoCADo **");
		new AvoCADo();
		System.out.println("** Exiting avoCADo  **");
	}
	
	public AvoCADo(){
		display = new Display();
		
		// splash screen.. loads before openGL is needed
		// since that can take a few seconds.  This gives
		// more immediate feedback that the app has started.
		new StartupSplashShell(display);
		
		// create the main shell and display it
		new MainAvoCADoShell(display);
		
		display.dispose();
	}
	

}
