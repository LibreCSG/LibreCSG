package ui.quicksettings;

import net.sf.avocado_cad.eclipse.ui.AvoGlobal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import ui.event.GLViewListener;
import ui.utilities.AvoColors;
import ui.utilities.NumUtils;


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
public class XYZDisplayComp extends Composite{

	
	protected static Label lX;
	protected static Label lY;
	protected static Label lZ;
	
	public XYZDisplayComp(Composite parent, int style){
		super(parent, style);
		
		this.setLayout(new RowLayout(SWT.HORIZONTAL));
		this.setBackground(AvoColors.COLOR_QSET_BG);
		
		FontData fd = new FontData();
		fd.setHeight(10);
		fd.setStyle(SWT.BOLD);
		fd.setName("courier");
		Font f = new Font(this.getDisplay(), fd);		
		
		Label llx = new Label(this, SWT.NO_BACKGROUND);
		llx.setFont(f);
		llx.setText("X:");
		llx.setBackground(AvoColors.COLOR_QSET_BG);
		lX = new Label(this, SWT.NONE);	
		lX.setAlignment(SWT.RIGHT);
		lX.setFont(f);
		lX.setBackground(AvoColors.COLOR_QSET_BG);
		
		Label lly = new Label(this, SWT.NONE);
		lly.setFont(f);
		lly.setText(" Y:");
		lly.setBackground(AvoColors.COLOR_QSET_BG);
		lY = new Label(this, SWT.NONE);
		lY.setAlignment(SWT.RIGHT);
		lY.setFont(f);
		lY.setBackground(AvoColors.COLOR_QSET_BG);
		
		Label llz = new Label(this, SWT.NONE);
		llz.setFont(f);
		llz.setText(" Z:");
		llz.setBackground(AvoColors.COLOR_QSET_BG);
		lZ = new Label(this, SWT.NONE);
		lZ.setAlignment(SWT.RIGHT);
		lZ.setFont(f);
		lZ.setBackground(AvoColors.COLOR_QSET_BG);
		
		updateXYZ();
		
		AvoGlobal.glViewEventHandler.addGLViewListener(new GLViewListener(){
			@Override
			public void cursorMoved() {
				updateXYZ();
			}			
		});
	}
	
	private void updateXYZ(){
		lX.setText(NumUtils.doubleToFixedString(AvoGlobal.glCursor3DPos[0], 7));
		lY.setText(NumUtils.doubleToFixedString(AvoGlobal.glCursor3DPos[1], 7));
		lZ.setText(NumUtils.doubleToFixedString(AvoGlobal.glCursor3DPos[2], 7));
	}
}
