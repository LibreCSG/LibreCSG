package ui.paramdialog;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import ui.animator.Animator;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.data.utilities.ImageUtils;
import backend.global.AvoGlobal;
import backend.model.Feature;


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
public class DynParamDialog {

	public static Composite paramCompMain;
	public static Composite paramCompTab;
	public static Composite parentComp;
	
	private static Label tabLabel;
	
	private ScrolledComposite spComp;
	private Composite paramComp;
	private ParamSet pSet;
	
	private static ParamAnim animator;
	
	public static final int TAB_HEIGHT     = 20;
	public static final int TAB_WIDTH      = 150;
	public static final int TAB_TEXT_WIDTH = 100;
	public static final int BORDER_WIDTH   = 3;

	private static int bodyHeight = 81;
	private static int bodyWidth  = 450;
	
	public DynParamDialog(Composite comp){
		parentComp = comp;
		
		paramCompMain = new Composite(comp,SWT.NONE);
		paramCompMain.setBounds(10,10,bodyWidth,bodyHeight);
		paramCompMain.setBackground(AvoGlobal.COLOR_PARAM_BG);
		paramCompMain.setBounds(1,1,bodyWidth,bodyHeight);
		spComp = new ScrolledComposite(paramCompMain, SWT.V_SCROLL);
		spComp.setBounds(BORDER_WIDTH,BORDER_WIDTH,bodyWidth-2*BORDER_WIDTH,bodyHeight-2*BORDER_WIDTH);
		paramComp = new Composite(spComp, SWT.NONE);
		spComp.setContent(paramComp);
		RowLayout rl = new RowLayout();
		rl.wrap = true;
		paramComp.setLayout(rl);
		
		
		
		paramCompTab = new Composite(comp,SWT.NONE);
		paramCompTab.setBounds(10,0,TAB_WIDTH,TAB_HEIGHT);
		paramCompTab.setBackground(AvoGlobal.COLOR_PARAM_BG);	
		paramCompTab.setBounds(1,1,TAB_WIDTH,TAB_HEIGHT);
		tabLabel = new Label(paramCompTab, SWT.NONE);
		tabLabel.setText("");
		tabLabel.setBounds(BORDER_WIDTH,BORDER_WIDTH,TAB_TEXT_WIDTH-2*BORDER_WIDTH,TAB_HEIGHT-2*BORDER_WIDTH);
		tabLabel.setBackground(AvoGlobal.COLOR_PARAM_BG);
		
		int bWidth = TAB_WIDTH-TAB_TEXT_WIDTH-BORDER_WIDTH;
		Button bOK = new Button(paramCompTab, SWT.PUSH);
		bOK.setBounds(TAB_TEXT_WIDTH,BORDER_WIDTH,bWidth,TAB_HEIGHT-BORDER_WIDTH);
		bOK.setBackground(AvoGlobal.COLOR_PARAM_BG);
		bOK.setToolTipText("OK");
		bOK.addPaintListener(new PaintListener(){
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				gc.drawImage(ImageUtils.getIcon("./paramDialog/OK.png", 12,12), 15, 3);
			}			
		});
		bOK.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e) {			
			}
			public void mouseDown(MouseEvent e) {
				// handle "OK" click in paramDialog
				if(AvoGlobal.getWorkingFeature() != null){
					AvoGlobal.pushWorkFeatToSet();
					AvoGlobal.setWorkingFeature(null);
				}
				animator.animateBackwards(0);
			}
			public void mouseUp(MouseEvent e) {				
			}			
		});
		bOK.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				// handle "OK" click in paramDialog
				if(AvoGlobal.getWorkingFeature() != null){
					AvoGlobal.pushWorkFeatToSet();
					AvoGlobal.setWorkingFeature(null);
				}
				animator.animateBackwards(0);
			}			
		});
		
		animator = new ParamAnim(); 
		buildParamComposite(null);
	}
	
	public void positionParamDialog(){
		animator.setToLastValue();
	}
	
	/**
	 * update the parameters being shown
	 * in the paramDialog.  This should be
	 * called when a new item is selected
	 * or being drawn.
	 */
	public void updateParams(Feature f){
		buildParamComposite(f);
		if(f != null){
			animator.animateForwards(200);
		}
	}
	
	
	private void buildParamComposite(Feature f){
		// remove all of the old children from the composite.
		Control[] cList = paramComp.getChildren();
		for(int i=0; i<cList.length; i++){
			Control c = cList[i];
			c.dispose();
		}
		
		// setup the composite for rowLayout.
		spComp.setContent(paramComp);
		RowLayout rl = new RowLayout();
		rl.wrap = true;
		paramComp.setLayout(rl);
		paramComp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		
		// make sure feature is not null
		Feature workingFeature = AvoGlobal.getWorkingFeature();
		if(workingFeature == null){
			// feature was null, hide the paramDialog and return.
			animator.animateBackwards(0);
			tabLabel.setText("null");
			return;
		}
		
		
		tabLabel.setText(f.label);
		
		//
		// add all parameters from the current feature to 
		// the paramDialog for display/modification
		//
		pSet = workingFeature.paramSet;
		Iterator iter = pSet.getIterator();
		while(iter.hasNext()){
			Param p = (Param)iter.next();
			switch(p.getType()){
				case Boolean : {
					break;
				}
				case Int : {
					break;
				}
				case Double : {
					break;
				}
				case String : {
					break;
				}
				case Point2D : {
					new PCompPoint2D(paramComp, SWT.BORDER, p);
					break;
				}
				case Point3D : {
					break;
				}
				default : {
					Label l = new Label(paramComp, SWT.SINGLE);
					l.setText(p.getLabel());
					System.out.println("Parameter was of unknown type! ## " + p.getType());
					break;
				}
			}
		}
		paramComp.pack();
		
	}
	
	
	class ParamAnim extends Animator{
		public void animatorTransition(float percentComplete) {				
			paramCompMain.setLocation((int)(percentComplete*bodyWidth)-bodyWidth,parentComp.getBounds().height-bodyHeight);
			paramCompTab.setLocation((int)(percentComplete*bodyWidth)-bodyWidth,parentComp.getBounds().height-bodyHeight-TAB_HEIGHT);
			//System.out.println("transistioned @ " +  percentComplete);			
		}		
	}
	
}
