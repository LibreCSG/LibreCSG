package ui.menubar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import ui.shells.AboutAvoCADoGPLShell;
import backend.global.AvoGlobal;
import backend.model.Project;


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
public class AvoMenuBar {

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
			fsNew.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent e) {					
				}
				public void widgetSelected(SelectionEvent e) {
					// TODO Create new project, after checking to make sure
					//      current project is either empty or no new changes 
					//      have been made since the last save.
					// TODO: for now, just blindly tossing the old project and starting a new one.
					AvoGlobal.project = new Project();
					AvoGlobal.modelEventHandler.notifyElementRemoved();					
					AvoGlobal.intializeNewAvoCADoProject();
				}				
			});
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
			vsTop.setText("&Top");
			vsTop.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent e) {
				}
				public void widgetSelected(SelectionEvent e) {
					AvoGlobal.glView.setViewTop();
				}				
			});
			MenuItem vsFront = new MenuItem(viewSub, SWT.PUSH);
			vsFront.setText("&Font");
			vsFront.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent e) {
				}
				public void widgetSelected(SelectionEvent e) {
					AvoGlobal.glView.setViewFront();
				}				
			});
			MenuItem vsLeft = new MenuItem(viewSub, SWT.PUSH);
			vsLeft.setText("&Left");
			vsLeft.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent e) {
				}
				public void widgetSelected(SelectionEvent e) {
					AvoGlobal.glView.setViewLeft();
				}				
			});
			MenuItem vsIso = new MenuItem(viewSub, SWT.PUSH);
			vsIso.setText("&Isometric");
			vsIso.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent e) {
				}
				public void widgetSelected(SelectionEvent e) {
					AvoGlobal.glView.setViewIso();
				}				
			});
			MenuItem vsSketch = new MenuItem(viewSub, SWT.PUSH);
			vsSketch.setText("&Sketch");
			vsSketch.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent e) {
				}
				public void widgetSelected(SelectionEvent e) {
					AvoGlobal.glView.setViewSketch();
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
					// launch external browser.	
					Program.launch("http://avocado-cad.sourceforge.net/");
				}				
			});
			MenuItem hsGPL = new MenuItem(helpSub, SWT.PUSH);
			hsGPL.setText("Open Source License (GPL)");
			hsGPL.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent e) {					
				}
				public void widgetSelected(SelectionEvent e) {
					// load dialog with GPLv2
					new AboutAvoCADoGPLShell(Display.getCurrent());
				}				
			});
			
			
		}
	}
	
}
