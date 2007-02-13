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
import org.eclipse.swt.widgets.Label;

import ui.animator.Animator;
import ui.event.ParamListener;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.data.utilities.ImageUtils;
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
		paramComp.setLayout(new RowLayout(SWT.HORIZONTAL));
		spComp.setContent(paramComp);
	    spComp.setExpandVertical(true);
	    spComp.setExpandHorizontal(true);
		
		
		paramCompTab = new Composite(comp,SWT.NONE);
		paramCompTab.setBounds(10,0,TAB_WIDTH,TAB_HEIGHT);
		paramCompTab.setBackground(AvoGlobal.COLOR_PARAM_BG);	
		paramCompTab.setBounds(1,1,TAB_WIDTH,TAB_HEIGHT);
		tabLabel = new Label(paramCompTab, SWT.NONE);
		tabLabel.setText("");
		tabLabel.setBounds(BORDER_WIDTH,BORDER_WIDTH,TAB_TEXT_WIDTH-2*BORDER_WIDTH,TAB_HEIGHT-BORDER_WIDTH);
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
				animator.animateBackwards(0);
			}			
		});
		
		animator = new ParamAnim(); 
		buildParamComposite();
		
		//
		// Add param listener!
		//		
		AvoGlobal.paramEventHandler.addParamListener(new ParamListener(){
			public void paramModified() {
			}
			public void paramSwitched() {
				updateParams();
			}			
		});
		
	}
	
	/**
	 * move the paramDialog to it's appropriate position
	 * on the screen.. this only should need to be done
	 * if the window is resized.
	 */
	public void positionParamDialog(){
		animator.setToLastValue();
	}
	
	/**
	 * update the parameters being shown
	 * in the paramDialog.  This should be
	 * called when a new item is selected
	 * or being drawn.
	 */
	public void updateParams(){
		buildParamComposite();
		if(AvoGlobal.getActiveParamSet() != null){
			animator.animateForwards(200);
		}
	}
	
	
	private void buildParamComposite(){
		// remove all of the old children from the composite.
		Control[] cList = paramComp.getChildren();
		for(int i=0; i<cList.length; i++){
			Control c = cList[i];
			if(c instanceof ParamComp){
				((ParamComp)c).removeParamListener();
			}
			c.dispose();
		}
		
		// setup the composite for rowLayout.
		paramComp.setLayout(new RowLayout(SWT.HORIZONTAL));
		//paramComp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		
		// make sure ParamSet is not null
		if(AvoGlobal.getActiveParamSet() == null){
			animator.animateBackwards(0);
			tabLabel.setText("null");
			return;
		}
		
		tabLabel.setText(AvoGlobal.getActiveParamSet().label);
		
		//
		// add all parameters from the current feature to 
		// the paramDialog for display/modification
		//
		Iterator iter = AvoGlobal.getActiveParamSet().getIterator();
		while(iter.hasNext()){
			Param p = (Param)iter.next();
			switch(p.getType()){
				case Boolean : {
					// TODO: PCompBoolean
					Label l = new Label(paramComp, SWT.SINGLE);
					l.setText(p.getLabel());
					break;
				}
				case Int : {
					// TODO: PCompInt
					Label l = new Label(paramComp, SWT.SINGLE);
					l.setText(p.getLabel());
					break;
				}
				case Double : {
					new PCompDouble(paramComp, SWT.BORDER, p);
					break;
				}
				case String : {
					// TODO: PCompString
					Label l = new Label(paramComp, SWT.SINGLE);
					l.setText(p.getLabel());
					break;
				}
				case Point2D : {
					new PCompPoint2D(paramComp, SWT.BORDER, p);
					break;
				}
				case Point3D : {
					// TODO: PCompPoint3D
					Label l = new Label(paramComp, SWT.SINGLE);
					l.setText(p.getLabel());
					break;
				}
				default : {
					Label l = new Label(paramComp, SWT.SINGLE);
					l.setText(p.getLabel());
					System.out.println("Parameter was of unknown type! ## " + p.getType() + " ++ " + p.getLabel());
					break;
				}
			}
		}
		
		paramComp.pack();
		
        spComp.setMinSize(paramComp.computeSize(spComp.getClientArea().width-10, SWT.DEFAULT));
		
	}
	
	
	class ParamAnim extends Animator{
		public void animatorTransition(float percentComplete) {				
			paramCompMain.setLocation((int)(percentComplete*bodyWidth)-bodyWidth,parentComp.getBounds().height-bodyHeight);
			paramCompTab.setLocation((int)(percentComplete*bodyWidth)-bodyWidth,parentComp.getBounds().height-bodyHeight-TAB_HEIGHT);
			//System.out.println("transistioned @ " +  percentComplete);			
		}		
	}

	
	// TODO:  Does this really need to be here?!?!
	/**
	 * let parameter display elements know that
	 * values may have changed and therefor should
	 * be check for updating to the screen.
	 */
	public void notifyParamChangeListener(){
		AvoGlobal.paramEventHandler.notifyParamModified();
	}
	
}