package ui.paramdialog;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import ui.animator.Animator;
import ui.menuet.Menuet;
import ui.utilities.SWTUtils;
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

	public static Shell paramShell;
	public static Shell mainShell;
	
	public static final int TAB_HEIGHT     = 20;
	public static final int TAB_WIDTH      = 150;
	public static final int TAB_TEXT_WIDTH = 100;
	public static final int BORDER_WIDTH   = 3;

	private static int shellBodyHeight = 81;
	private static int shellBodyWidth  = 450;
	
	private float lastPercentComplete = 0.0f;
	
	ShellAnim animator = new ShellAnim();
	
	Label tabLabel;
	Composite pComp;
	
	ParamSet pSet;
	
	/**
	 * Dynamic parameter dialog.
	 * This shell slides into view when
	 * the properties of a feature are being
	 * modified (creation and editing) to
	 * allow for display and entry of all
	 * relevant parameters.
	 */
	public DynParamDialog(Shell mainAppShell){
		paramShell = new Shell(mainShell, SWT.NONE | SWT.NO_TRIM | SWT.ON_TOP | SWT.RESIZE);
		paramShell.setText("Feature Parameters");		
		paramShell.setBounds(-100,-100,100,100); // offscreen...
		paramShell.setBackground(AvoGlobal.COLOR_PARAM_BG);
		
		mainShell = mainAppShell;
		// if main shell is moved, move the paramDialog too!
		mainShell.addControlListener(new ControlListener(){
			public void controlMoved(ControlEvent e) {
				animator.animatorTransition(lastPercentComplete);			
			}
			public void controlResized(ControlEvent e) {
				animator.animatorTransition(lastPercentComplete);			
			}			
		});
		
		ScrolledComposite spComp = new ScrolledComposite(paramShell,SWT.NONE);
		spComp.setBounds(BORDER_WIDTH,TAB_HEIGHT+BORDER_WIDTH,shellBodyWidth-2*BORDER_WIDTH,shellBodyHeight-2*BORDER_WIDTH);
		spComp.setLayout(new FillLayout());
		pComp = new Composite(spComp, SWT.NONE);
		
		tabLabel = new Label(paramShell, SWT.NONE);
		tabLabel.setText("Circle");
		tabLabel.setBounds(BORDER_WIDTH,BORDER_WIDTH,TAB_TEXT_WIDTH-2*BORDER_WIDTH,50);
		tabLabel.setBackground(AvoGlobal.COLOR_PARAM_BG);
		
		int bWidth = (TAB_WIDTH-TAB_TEXT_WIDTH-2*BORDER_WIDTH)/2;
		Button bOK = new Button(paramShell, SWT.PUSH);
		bOK.setBounds(TAB_TEXT_WIDTH,BORDER_WIDTH,bWidth,TAB_HEIGHT-BORDER_WIDTH);
		bOK.setBackground(AvoGlobal.COLOR_PARAM_BG);
		bOK.setToolTipText("OK");
		bOK.addPaintListener(new PaintListener(){
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				gc.drawImage(ImageUtils.getIcon("./paramDialog/OK.png", 12,12), 5, 3);
			}			
		});
		bOK.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e) {			
			}
			public void mouseDown(MouseEvent e) {
				// TODO Update and keep param dialog appropriately!	
				animator.animateBackwards(0);
			}
			public void mouseUp(MouseEvent e) {				
			}			
		});
		
		Button bCancel = new Button(paramShell, SWT.PUSH);
		bCancel.setBounds(TAB_TEXT_WIDTH+bWidth+BORDER_WIDTH,BORDER_WIDTH,bWidth,TAB_HEIGHT-BORDER_WIDTH);
		bCancel.setBackground(AvoGlobal.COLOR_PARAM_BG);
		bCancel.setToolTipText("Cancel");
		bCancel.addPaintListener(new PaintListener(){
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				gc.drawImage(ImageUtils.getIcon("./paramDialog/Cancel.png", 12,12), 5, 3);
			}			
		});
		bCancel.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e) {			
			}
			public void mouseDown(MouseEvent e) {
				// TODO Cancel param dialog appropriately!	
				animator.animateBackwards(0);
			}
			public void mouseUp(MouseEvent e) {				
			}			
		});
		
		// TODO: remove auto animate on startup
		animator.animateForwards(250);
	}
	
	
	class ShellAnim extends Animator{
		public void animatorTransition(float percentComplete) {		
			lastPercentComplete = percentComplete;
			if(paramShell != null && !paramShell.isDisposed() && !mainShell.isDisposed()){
				Rectangle b = mainShell.getBounds();
				
				int totalHeight = (int)((TAB_HEIGHT+shellBodyHeight)*percentComplete);
				
				// region is specified relative to zero
				Region region = new Region();
				region.add(new int[]{	0, 									0, 
										TAB_WIDTH,							0,
										TAB_WIDTH,							(int)(TAB_HEIGHT*percentComplete),
										0,									(int)(TAB_HEIGHT*percentComplete),
										0,									0});
				region.add(new int[]{
										0,									(int)(TAB_HEIGHT*percentComplete),
										shellBodyWidth,						(int)(TAB_HEIGHT*percentComplete),
										shellBodyWidth,						totalHeight,
										0,									totalHeight,
										0,									0});

				paramShell.setRegion(region);
				// shell location/size are set in global screen coordinates.  :o(
				int left =  b.x+Menuet.MENUET_WIDTH + SWTUtils.getShellLeftEdgeWidth(mainShell);
				//TODO: put in quicksetting height dynamically from class
				int quicksettingsHeight = 25;
				int top  =  b.y+b.height-SWTUtils.getShellBottomEdgeHeight(mainShell)-quicksettingsHeight-totalHeight;
				paramShell.setLocation(left,top);
				paramShell.setSize(region.getBounds().width, region.getBounds().height);
				
				//toolTip.setBounds(b.x+Menuet.this.getBounds().width + (b.width-c.width)/2, b.y+22, (int)(200*percentComplete), b.height-30);
			}else{
				//initializeToolTip();
			}
			//System.out.println("transistioning: " + percentComplete);
			if(percentComplete > 0.0f){
				paramShell.setVisible(true);
			}else{
				paramShell.setVisible(false);
			}
		}		
	}
	
	/**
	 * update the parameters being shown
	 * in the paramDialog.  This should be
	 * called when a new item is selected
	 * or being drawn.
	 */
	public void updateParams(Feature f){
		tabLabel.setText(AvoGlobal.currentTool.mElement.meLabel);
		animator.animateForwards(250);
		//TODO: update pComp.
		buildParamComposite(f);
	}
	
	private void buildParamComposite(Feature f){
		// make sure feature is not null
		if(AvoGlobal.workingFeature == null){
			return;
		}
		
		/*
		// remove all of the old children from the composite.
		Control[] cList = pComp.getChildren();
		for(int i=0; i<cList.length; i++){
			Control c = cList[i];
			c.dispose();
		}
		*/
		
		//
		// WIERD BEHAVIOR.. items should wrap!!!
		// TODO !!
		// TODO !!
		// TODO !!
		
		//pComp.setLayout(new FillLayout());
		//Composite c = new Composite(pComp, SWT.NONE);
		RowLayout r1 = new RowLayout();
		r1.wrap = true;
		pComp.setLayout(r1);
		pComp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		
		pSet = AvoGlobal.workingFeature.paramSet;
		Iterator iter = pSet.getIterator();
		while(iter.hasNext()){
			Param p = (Param)iter.next();
			Label l = new Label(pComp, SWT.NONE);
			l.setText(p.getLabel());
			System.out.println("added param:" + p.getLabel() + " ## " + l.getBounds());
		}
		pComp.pack();
	}
	
}
