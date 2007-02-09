package ui.paramdialog;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ui.utilities.NumUtils;
import backend.adt.PType;
import backend.adt.Param;
import backend.adt.Point2D;
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
public class PCompPoint2D extends Composite{

	private static Text tx;
	private static Text ty;
	private Param param;
	
	public PCompPoint2D(Composite parent, int style, Param p){
		super(parent,style);
		param = p;
		
		RowLayout rl = new RowLayout();
		rl.wrap = false;
		this.setLayout(rl);
		
		if(p.getType() != PType.Point2D){
			System.out.println("trying to display a non-point2D in a PCompPoint2D (paramDialog)");
			return;
		}
		
		Point2D pt = (Point2D)param.getData();
		
		Label l = new Label(this, SWT.NONE);
		l.setText(p.getLabel() + ": ");
		
		Label lx = new Label(this, SWT.NONE);
		lx.setText("x");
		tx = new Text(this, SWT.BORDER);
		tx.setText(NumUtils.doubleToFixedString(pt.getX(),6));
		
		// handle key presses
		tx.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if(e.character == '\r'){
					// check to see if string is a valid number
					if(NumUtils.stringIsNumber(tx.getText())){
						Point2D pt = (Point2D)param.getData();
						pt.setX(Double.parseDouble(tx.getText()));
						param.change(pt);
						AvoGlobal.glViewNeedsUpdated = true;
					}else{
						tx.setText(NumUtils.doubleToFixedString(((Point2D)param.getData()).getX(),6));
					}
				}
			}
			public void keyReleased(KeyEvent e) {		
			}			
		});
		// perform field validation
		tx.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {			
			}
			public void focusLost(FocusEvent e) {
				// check to see if string is a valid number
				if(NumUtils.stringIsNumber(tx.getText())){
					Point2D pt = (Point2D)param.getData();
					pt.setX(Double.parseDouble(tx.getText()));
					param.change(pt);
					AvoGlobal.glViewNeedsUpdated = true;
				}else{
					tx.setText(NumUtils.doubleToFixedString(((Point2D)param.getData()).getX(),6));
				}
			}			
		});
		
		Label ly = new Label(this, SWT.NONE);
		ly.setText("y");		
		ty = new Text(this, SWT.BORDER);
		ty.setText(NumUtils.doubleToFixedString(pt.getY(),6));	
		
		// handle key presses
		ty.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if(e.character == '\r'){
					// check to see if string is a valid number
					if(NumUtils.stringIsNumber(ty.getText())){
						Point2D pt = (Point2D)param.getData();
						pt.setY(Double.parseDouble(ty.getText()));
						param.change(pt);
						AvoGlobal.glViewNeedsUpdated = true;
					}else{
						ty.setText(NumUtils.doubleToFixedString(((Point2D)param.getData()).getY(),6));
					}
				}
			}
			public void keyReleased(KeyEvent e) {		
			}			
		});
		// perform field validation
		ty.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {			
			}
			public void focusLost(FocusEvent e) {
				// check to see if string is a valid number
				if(NumUtils.stringIsNumber(ty.getText())){
					Point2D pt = (Point2D)param.getData();
					pt.setY(Double.parseDouble(ty.getText()));
					param.change(pt);
					AvoGlobal.glViewNeedsUpdated = true;
				}else{
					ty.setText(NumUtils.doubleToFixedString(((Point2D)param.getData()).getY(),6));
				}
			}			
		});
		
	}
	

	
}
