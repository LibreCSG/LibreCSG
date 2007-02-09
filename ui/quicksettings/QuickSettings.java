package ui.quicksettings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

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
public class QuickSettings extends Composite{

	static Combo cSnap;
	
	public QuickSettings(Composite parent, int style){
		super(parent, style);
		
		this.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		new XYZDisplayComp(this, SWT.BORDER);
		
		
		FontData fd = new FontData();
		fd.setHeight(8);
		fd.setName("courier");
		Font f = new Font(this.getDisplay(), fd);	
		
		Combo cUnits = new Combo(this, SWT.READ_ONLY);
		cUnits.setItems(new String[] {"inches", "mil", "meters", "mm"});
		cUnits.setFont(f);
		cUnits.select(2);
		cUnits.setBackground(this.getBackground());
		
		
		Label lSnap = new Label(this, SWT.BOLD);
		lSnap.setText("Snap:");
		cSnap = new Combo(this, SWT.READ_ONLY);
		cSnap.setItems(new String[] {"off", "0.25x", "0.5x", "1.0x"});
		cSnap.setFont(f);
		cSnap.select(3);
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
		
		
		
	}
	
	

	
}
