package ui.shells;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import ui.menubar.AvoMenuBar;
import ui.menuet.Menuet;
import ui.menuet.MenuetBuilder;
import ui.opengl.GLView;
import ui.paramdialog.DynParamDialog;
import ui.quicksettings.QuickSettings;
import ui.treeviewer.TreeViewer;
import backend.data.utilities.ImageUtils;
import backend.global.AvoColors;
import backend.global.AvoGlobal;

//
//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the 
//GNU General Public License (GPL).
//
// This file is part of avoADo.
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
public class MainAvoCADoShell{

	Shell shell;
	
	SashForm comp2topSash;
	Composite mainViewComp;
	
	/**
	 * create the main avoCADo shell and display it
	 * @param display
	 */
	public MainAvoCADoShell(Display display){
		shell = new Shell(display);
		
		setupShell(); 				// place components in the main avoCADo shell
		
		shell.setText("avoCADo");
		shell.setSize(800, 600);	//TODO: set intial size to last known size
		shell.setMinimumSize(640, 480);
		shell.setImage(ImageUtils.getIcon("./avoCADo.png", 32, 32));
		shell.open();
		
		// handle events while the shell is not disposed
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
	
	
	/**
	 * setup the shell with each of its key components
	 * (menubar, menuet, treeviewer, glview, quicksettings, etc.)
	 */
	void setupShell(){
		// --populate the main shell--	
		
		//
		// give the shell a menubar (top)
		//
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		// populate the menubar
		AvoMenuBar.populateMenuBar(menu);
		
		//
		// break the shell into two pieces (left/right)
		//
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginWidth = 0;		
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.verticalSpacing = 0;
		gl.horizontalSpacing = 0;
		shell.setLayout(gl);
		
		//
		// set the small left piece as the menuet
		//
		AvoGlobal.menuet = new Menuet(shell, SWT.NONE);
		// put all of the elements onto the menuet!	
		MenuetBuilder.buildMenuet(AvoGlobal.menuet); 
		AvoGlobal.menuet.setBackground(AvoColors.COLOR_MENUET_MAIN);
		GridData gd0 = new GridData(GridData.FILL_VERTICAL);
		gd0.grabExcessVerticalSpace = true;
		gd0.widthHint = Menuet.MENUET_WIDTH;
		gd0.minimumWidth = Menuet.MENUET_WIDTH;
		AvoGlobal.menuet.setLayoutData(gd0);
		AvoGlobal.menuet.updateToolModeDisplayed();
		
		
		//
		// break the right piece into a top and bottom
		//
		Composite comp2 = new Composite(shell, SWT.NONE);
		GridData gd1 = new GridData(GridData.FILL_BOTH);
		gd1.grabExcessHorizontalSpace = true;
		gd1.grabExcessVerticalSpace = true;
		comp2.setLayoutData(gd1);
		comp2.setBackground(new Color(shell.getDisplay(), 100, 100, 200));		
		
		GridLayout gl2 = new GridLayout();
		gl2.numColumns = 1;
		gl2.marginWidth = 0;		
		gl2.marginHeight = 0;
		gl2.marginWidth = 0;
		gl2.verticalSpacing = 0;
		gl2.horizontalSpacing = 0;
		comp2.setLayout(gl2);
		
		//
		// break the top piece into two more pieces via a sash
		//
		comp2topSash = new SashForm(comp2, SWT.NONE);
		comp2topSash.setBackground(new Color(shell.getDisplay(), 200, 100, 100));
		comp2topSash.SASH_WIDTH = 5;		
		GridData gd2top = new GridData(GridData.FILL_BOTH);
		gd2top.grabExcessHorizontalSpace = true;
		gd2top.grabExcessVerticalSpace = true;
		comp2topSash.setLayoutData(gd2top);
		
		//
		// mainViewComp fills left side of SashForm
		// and will hold glView and the paramDialog
		// (paramDialog is placed on top of glView)
		//
		mainViewComp = new Composite(comp2topSash,SWT.NONE);
		
		//
		// Add the paramDialog
		//
		AvoGlobal.paramDialog = new DynParamDialog(mainViewComp);
		
		//
		// Add the glView
		//
		AvoGlobal.glView = new GLView(mainViewComp);
		mainViewComp.addControlListener(new ControlListener(){
			public void controlMoved(ControlEvent e) {			
			}
			public void controlResized(ControlEvent e) {
				// resize glView to fill entire mainViewComp
				// (can't use FillLayout since another component,
				//  the paramDialog is placed over the glView).
				AvoGlobal.glView.glCanvas.setBounds(mainViewComp.getBounds());
				AvoGlobal.paramDialog.positionParamDialog();
			}			
		});
		
		
		//
		// right piece is treeviewer
		//
		AvoGlobal.treeViewer = new TreeViewer(comp2topSash, SWT.NONE);
		//Composite comp3right = new Composite(comp2topSash, SWT.NONE);
		//comp3right.setBackground(new Color(shell.getDisplay(), 200, 200, 200));
		
		//
		// now that items have been added to sash, set the weights.
		// TODO: intelligently set sash weights
		comp2topSash.setWeights(new int[] {5, 1});
		
		//
		// remaining little section on the bottom is quicksetting bar
		//
		// TODO: use quicksetting instead of dummy composite
		QuickSettings comp2bot = new QuickSettings(comp2, SWT.NONE);
		//comp2bot.setBackground(new Color(shell.getDisplay(), 150, 50, 150));
		GridData gd2bot = new GridData(GridData.FILL_HORIZONTAL);
		gd2bot.grabExcessHorizontalSpace = true;
		gd2bot.heightHint = 30;
		gd2bot.minimumHeight = 30;
		comp2bot.setLayoutData(gd2bot);
		
	}
	
}
