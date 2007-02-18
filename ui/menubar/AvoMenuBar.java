package ui.menubar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import backend.global.AvoGlobal;


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
public class AvoMenuBar {

	static Shell browserShell;
	
	/**
	 * add items to the shell's top menubar.
	 * @param menu
	 */
	public static void populateMenuBar(Menu menu){
		// TODO: fully populate menubar
		MenuItem miFile = new MenuItem(menu, SWT.CASCADE);
		miFile.setText("&File");
		{
			// File Menu
			Menu fileSub = new Menu(menu.getShell(), SWT.DROP_DOWN);
			miFile.setMenu(fileSub);
			MenuItem fsNew = new MenuItem(fileSub, SWT.PUSH);
			fsNew.setText("&New");
			MenuItem fsQuit = new MenuItem(fileSub, SWT.PUSH);
			fsQuit.setText("&Quit");
			fsQuit.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent e) {
				}
				public void widgetSelected(SelectionEvent e) {
					// quit, but not direrctly so that closing
					// tasks can still be performed.
					Display.getCurrent().dispose();
				}				
			});
		}
		  
		
		MenuItem miEdit = new MenuItem(menu, SWT.CASCADE);
		miEdit.setText("&Edit");
		
		MenuItem miView = new MenuItem(menu, SWT.CASCADE);
		miView.setText("&View");
		{
			// View Menu
			Menu viewSub = new Menu(menu.getShell(), SWT.DROP_DOWN);
			miView.setMenu(viewSub);
			MenuItem vsTop = new MenuItem(viewSub, SWT.PUSH);
			vsTop.setText("Top");
			vsTop.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent e) {
				}
				public void widgetSelected(SelectionEvent e) {
					AvoGlobal.glView.setViewTop();
				}				
			});
		}
		
		MenuItem miHelp = new MenuItem(menu, SWT.CASCADE);
		miHelp.setText("&Help");
		{
			// Help Menu
			Menu helpSub = new Menu(menu.getShell(), SWT.DROP_DOWN);
			miHelp.setMenu(helpSub);
			MenuItem hsWeb = new MenuItem(helpSub, SWT.PUSH);
			hsWeb.setText("Visit avoCADo Website");
			hsWeb.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent e) {					
				}
				public void widgetSelected(SelectionEvent e) {
					// TODO: load external browser.				
				}				
			});
		}
	}
	
}
