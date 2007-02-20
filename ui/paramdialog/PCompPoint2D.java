package ui.paramdialog;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ui.event.ParamListener;
import ui.utilities.NumUtils;
import backend.adt.PType;
import backend.adt.Param;
import backend.adt.ParamNotCorrectTypeException;
import backend.adt.ParamSet;
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
public class PCompPoint2D extends ParamComp{

	private Text tx;
	private Text ty;	
	
	public PCompPoint2D(Composite parent, int style, Param p, ParamSet paramSet){
		super(parent,style, paramSet);
		param = p;
		
		//
		// Setup the component's layout
		//
		RowLayout rl = new RowLayout();
		rl.wrap = false;
		this.setLayout(rl);
		
		//
		// check to make sure param is of correct type
		//
		if(p.getType() != PType.Point2D){
			System.out.println("trying to display a non-point2D in a PCompPoint2D (paramDialog)");
			return;
		}
		
		Point2D pt = getParamData();
		
		
		//
		// Create font to use for text boxes
		//
		FontData fd = new FontData();
		fd.setHeight(10);
		fd.setName("courier");
		Font textF = new Font(this.getDisplay(), fd);
		
		//
		// Param label display
		//
		Label l = new Label(this, SWT.NONE);
		l.setText(p.getLabel() + ": ");
		
		//
		// X Coor:
		//
		Label lx = new Label(this, SWT.NONE);
		lx.setText("x");
		tx = new Text(this, SWT.BORDER);
		tx.setText(NumUtils.doubleToFixedString(pt.getX(),8));
		tx.setFont(textF);
		
		//
		// X Coor: handle key presses
		//
		tx.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if(e.character == '\r'){
					// check to see if string is a valid number
					if(NumUtils.stringIsNumber(tx.getText())){
						Point2D pt = getParamData();
						pt.setX(Double.parseDouble(tx.getText()));
						param.change(pt);
						updateParamViaToolInterface();
						AvoGlobal.glView.updateGLView = true;
					}else{
						tx.setText(NumUtils.doubleToFixedString(getParamData().getX(),8));
					}
				}
			}
			public void keyReleased(KeyEvent e) {		
			}			
		});
		
		//
		// X Coor: perform field validation
		//
		tx.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {			
			}
			public void focusLost(FocusEvent e) {
				// check to see if string is a valid number
				if(NumUtils.stringIsNumber(tx.getText())){
					Point2D pt = getParamData();
					pt.setX(Double.parseDouble(tx.getText()));
					param.change(pt);
					updateParamViaToolInterface();
					AvoGlobal.glView.updateGLView = true;
				}else{
					tx.setText(NumUtils.doubleToFixedString(getParamData().getX(),8));
				}
			}			
		});

		//
		// Y Coor:
		//
		Label ly = new Label(this, SWT.NONE);
		ly.setText("y");		
		ty = new Text(this, SWT.BORDER);
		ty.setText(NumUtils.doubleToFixedString(pt.getY(),8));	
		ty.setFont(textF);
		
		//
		// handle key presses
		//
		ty.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if(e.character == '\r'){
					// check to see if string is a valid number
					if(NumUtils.stringIsNumber(ty.getText())){
						Point2D pt = getParamData();
						pt.setY(Double.parseDouble(ty.getText()));
						param.change(pt);
						updateParamViaToolInterface();
						AvoGlobal.glView.updateGLView = true;
					}else{
						ty.setText(NumUtils.doubleToFixedString(getParamData().getY(),8));
					}
				}
			}
			public void keyReleased(KeyEvent e) {		
			}			
		});
		
		//
		// perform field validation
		//
		ty.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {			
			}
			public void focusLost(FocusEvent e) {
				// check to see if string is a valid number
				if(NumUtils.stringIsNumber(ty.getText())){
					Point2D pt = getParamData();
					pt.setY(Double.parseDouble(ty.getText()));
					param.change(pt);
					updateParamViaToolInterface();
					AvoGlobal.glView.updateGLView = true;
				}else{
					ty.setText(NumUtils.doubleToFixedString(getParamData().getY(),8));
				}
			}			
		});
		
		//
		// Disable user alteration if the parameter is derived
		//
		if(p.isDerivedParam()){
			tx.setEditable(false);
			tx.setBackground(AvoGlobal.COLOR_PARAM_DERIVED);	
			ty.setEditable(false);
			ty.setBackground(AvoGlobal.COLOR_PARAM_DERIVED);
		}
		
		//
		// Add param listener!
		//		
		paramListener = new ParamListener(){
			public void paramModified() {
				Point2D pt = getParamData();
				tx.setText(NumUtils.doubleToFixedString(pt.getX(),8));
				ty.setText(NumUtils.doubleToFixedString(pt.getY(),8));
			}
			public void paramSwitched() {
			}			
		};	
		AvoGlobal.paramEventHandler.addParamListener(paramListener);
		
	}
	

	Point2D getParamData(){
		try{
			Point2D data = param.getDataPoint2D();
			return data;
		}catch(ParamNotCorrectTypeException e){
			System.out.println(" *** WARNING *** PCompPoint2D :: param was not of type Point2D!");
			return new Point2D(0.0, 0.0);
		}
	}

	
}
