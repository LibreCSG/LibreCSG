package org.poikilos.librecsg.ui.quicksettings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.poikilos.librecsg.ui.navigation.XYZDisplayComp;
import org.poikilos.librecsg.ui.opengl.GLView;
import org.poikilos.librecsg.backend.global.AvoGlobal;


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
public class QuickSettings extends Composite{

	static Combo cSnap;
	static Combo dSnap;

	public QuickSettings(Composite parent, int style){
		super(parent, style);

		this.setLayout(new RowLayout(SWT.HORIZONTAL));

		FontData fd = new FontData();
		fd.setHeight(8);
		fd.setName("Verdana");
		Font f = new Font(this.getDisplay(), fd);

		Label lUnits = new Label(this, SWT.BOLD);
		lUnits.setFont(f);
		lUnits.setText(" Units:");
		Combo cUnits = new Combo(this, SWT.READ_ONLY);
		cUnits.setItems(new String[] {"inches", "mils", "meters", "mm"});
		cUnits.setFont(f);
		cUnits.select(2);
		cUnits.setBackground(this.getBackground());


		Label lSnap = new Label(this, SWT.BOLD);
		lSnap.setFont(f);
		lSnap.setText(" Snap:");
		cSnap = new Combo(this, SWT.READ_ONLY);
		cSnap.setItems(new String[] {"Off", "0.25x", "0.5x", "1.0x"});
		cSnap.setFont(f);
		cSnap.select(2);
		cSnap.setBackground(this.getBackground());
		cSnap.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				int selIndex = cSnap.getSelectionIndex();
				switch(selIndex){
					case(0):{
						AvoGlobal.snapEnabled = false;
						break;
					}
					case(1):{
						AvoGlobal.snapEnabled = true;
						AvoGlobal.snapSize = 0.25;
						break;
					}
					case(2):{
						AvoGlobal.snapEnabled = true;
						AvoGlobal.snapSize = 0.5;
						break;
					}
					case(3):{
						AvoGlobal.snapEnabled = true;
						AvoGlobal.snapSize = 1.0;
						break;
					}
					default:{
						// do nothing. no snap has been selected.
						System.out.println("no snap selected in quickSettings?? ignoring change.");
						break;
					}
				}
			}
		});


		Label lDebug = new Label(this, SWT.BOLD);
		lDebug.setFont(f);
		lDebug.setText(" View:");
		dSnap = new Combo(this, SWT.READ_ONLY);
		dSnap.setItems(new String[] {"Normal", "Debug"});
		dSnap.setFont(f);
		dSnap.select(0);
		dSnap.setBackground(this.getBackground());
		dSnap.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				int selIndex = dSnap.getSelectionIndex();
				switch(selIndex){
					case(0):{
						AvoGlobal.DEBUG_MODE = false;
						AvoGlobal.glView.updateGLView = true;
						break;
					}
					case(1):{
						AvoGlobal.DEBUG_MODE = true;
						AvoGlobal.glView.updateGLView = true;
						break;
					}
					default:{
						// do nothing. no snap has been selected.
						System.out.println("no view selected in quickSettings?? ignoring change.");
						break;
					}
				}
			}
		});


	}




}
