package ui.shells;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ui.utilities.ImageUtils;



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
* @created Mar. 2007
*/
public class AboutAvoCADoGPLShell {

	public static Shell shell;
	
	/**
	 * create the startup splash shell and display it
	 * @param display
	 */
	public AboutAvoCADoGPLShell(Display display){
		shell = new Shell(display, SWT.PRIMARY_MODAL);
		
		setupShell(); 				// place components in the avoCADo license shell
		
		shell.setText("avoCADo GPLv2");
		shell.setSize(583, 350);	//TODO: set initial size to last known size
		Rectangle b = display.getBounds();
		int xPos = Math.max(0, (b.width-583)/2);
		int yPos = Math.max(0, (b.height-350)/2);
		shell.setLocation(xPos, yPos);
		shell.open();
		
		// handle events while the shell is not disposed
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
	/**
	 * setup the shell 
	 */
	void setupShell(){
		// --populate the shell--
	
		shell.setBackgroundImage(ImageUtils.getIcon("./avoCADo-license-bg.jpg", 583, 350));
		
		GridLayout gl = new GridLayout();
		shell.setLayout(gl);
		gl.numColumns = 1;
		gl.marginWidth = 10;		
		gl.marginHeight = 10;
		gl.verticalSpacing = 10;
		gl.horizontalSpacing = 10;
		
		Label title = new Label(shell, SWT.NONE);
		title.setText(" avoCADo: Open Source 3D CAD under GPLv2   :) ");
		title.setBackground(new Color(shell.getDisplay(), 255, 255, 255));
		GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
		gd2.horizontalAlignment = SWT.CENTER;
		title.setLayoutData(gd2);
		
		Text text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		text.setEditable(false);
		text.setText(	"Copyright (C) 2007 avoCADo (Adam Kumpf creator)\n" +
						"This code is distributed under the terms of the\n" + 
						"GNU General Public License (GPL).\n\n" +
						"AvoCADo is free software; you can redistribute it and/or modify\n" +
						"it under the terms of the GNU General Public License as published by\n" +
						"the Free Software Foundation; either version 2 of the License, or\n" +
						"(at your option) any later version.\n\n" + 
						"AvoCADo is distributed in the hope that it will be useful,\n" +
						"but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
						"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
						"GNU General Public License for more details.\n\n" + 
						"You should have received a copy of the GNU General Public License\n" +
						"along with AvoCADo; if not, write to the Free Software\n" +
						"Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA");
		
		GridData gd0 = new GridData(GridData.FILL_BOTH);
		gd0.grabExcessVerticalSpace = true;
		gd0.grabExcessHorizontalSpace = true;
		text.setLayoutData(gd0);
		
		Button okButton = new Button(shell, SWT.PUSH);
		okButton.setText("  OK  ");
		okButton.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e) {
			}
			public void mouseDown(MouseEvent e) {
				shell.dispose();
			}
			public void mouseUp(MouseEvent e) {
			}			
		});
		GridData gd1 = new GridData(GridData.FILL_HORIZONTAL);
		gd1.horizontalAlignment = SWT.CENTER;
		okButton.setLayoutData(gd1);
	}
	
}
