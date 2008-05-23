package ui.menuet;

import java.util.LinkedList;

import net.sf.avocado_cad.eclipse.ui.AvoGlobal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;




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
public class MTToolComposite extends Composite{

	MenuetElement mElement; // the element to display in the composite
	
	public final static int numLastTools = 1;
	
	MTToolComposite(Composite parent, int type, MenuetElement mElement) {
		super(parent, type);
		this.mElement = mElement;
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.marginWidth = 2;		
		gl.marginHeight = 2;
		gl.verticalSpacing = 0;
		gl.horizontalSpacing = 0;
		this.setLayout(gl);
		
		//
		// Top portion with icon/button and tool's name
		//
		Composite compTop = new Composite(this, SWT.NONE);
		GridData gd0 = new GridData();
		gd0.grabExcessHorizontalSpace = true;
		gd0.widthHint = 300;
		compTop.setLayoutData(gd0);
		
		GridLayout gl2 = new GridLayout();
		gl2.numColumns = 2;
		gl2.marginWidth = 0;		
		gl2.marginHeight = 0;
		gl2.verticalSpacing = 0;
		gl2.horizontalSpacing = 0;
		compTop.setLayout(gl2);
		
		Button b = new Button(compTop, SWT.NONE);
		GridData gd11 = new GridData();
		gd11.grabExcessHorizontalSpace = false;
		b.setLayoutData(gd11);
		b.setImage(mElement.meIcon);
		b.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {	
			}
			public void widgetSelected(SelectionEvent e) {
				//
				// here's the deal. 
				//   get the current tool mode, 
				//   look through all tools to find toolboxLastUsed, (always index toolbox + 1)
				//   set toolBoxLastUsed to the newly selected mElement
				//   mimic a mousedown to the newly selected mElement.
				//
				LinkedList<MenuetElement> mElements = AvoGlobal.menuet.menuetElements[AvoGlobal.menuet.currentToolMode];
				int lastToolI = -1;
				for(int i=0; i<mElements.size(); i++){
					if(mElements.get(i).meLabel.equals(METoolbox.meToolboxLabel)){
						lastToolI = i+1;
					}
				}
				if(lastToolI != -1){					
					MenuetElement newToolSel = MTToolComposite.this.mElement;	
					if(newToolSel.isStoredInToolbox){
						int originalIndexOfNewTool = mElements.indexOf(newToolSel);
						mElements.add(lastToolI, newToolSel);
						newToolSel.meSetShown(true);
						MenuetElement oldestLastTool = mElements.get(lastToolI + METoolbox.numLastTools);
						oldestLastTool.meSetShown(false);
						if(originalIndexOfNewTool < lastToolI){
							mElements.remove(originalIndexOfNewTool);
						}else{
							mElements.remove(originalIndexOfNewTool+1);
						}						
						newToolSel.notifyListeners(SWT.MouseDown, new Event());
					}else{
						System.out.println("trying to select an element in the toolbox that does not belong!!");
					}
				}
				AvoGlobal.toolboxDialog.closeToolBox();
				AvoGlobal.menuet.updateToolModeDisplayed();			
			}			
		});
		
		
		Label l = new Label(compTop, SWT.CENTER);
		GridData gd12 = new GridData();
		gd12.grabExcessHorizontalSpace = true;
		l.setLayoutData(gd12);
		l.setText(mElement.meLabel);
		
		//
		// Bottom portion with description from element's ToolTipText
		//
		Composite compBot = new Composite(this, SWT.NONE);
		GridData gd21 = new GridData();
		gd21.grabExcessHorizontalSpace = true;
		gd21.widthHint = 300;
		compBot.setLayoutData(gd21);		
		compBot.setLayout(new FillLayout());
		
		Text t1 = new Text(compBot, SWT.MULTI | SWT.WRAP);
		if(mElement.getToolTipText() != null){
			// remove "\n" that may exist in the toolTipText
			t1.setText(mElement.getToolTipText().replace('\n', ' '));
		}else{
			t1.setText("null tooltip info...");
		}
		t1.setEditable(false); 
		t1.setEnabled(false);
	}
	
}
