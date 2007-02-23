package ui.paramdialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import ui.event.ParamListener;
import backend.adt.PType;
import backend.adt.Param;
import backend.adt.ParamNotCorrectTypeException;
import backend.adt.ParamSet;
import backend.adt.SelectionList;
import backend.global.AvoGlobal;

public class PCompSelectionList extends ParamComp{
	
	private List lSL;	
	private Composite selComp;
	
	public PCompSelectionList(Composite parent, int style, Param p, ParamSet paramSet){
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
		if(p.getType() != PType.SelectionList){
			System.out.println("trying to display a non-SelectionList in a PCompSelectionList (paramDialog)");
			return;
		}
		
		SelectionList selectionList = getParamData();
		
		//
		// Param label display
		//
		Label l = new Label(this, SWT.NONE);
		l.setText(p.getLabel() + ": ");
		
		//
		// Create font to use for text boxes
		//
		FontData fd = new FontData();
		fd.setHeight(10);
		fd.setName("courier");
		Font textF = new Font(this.getDisplay(), fd);	
		
		selComp = new Composite(this, SWT.NONE);
		
		GridLayout gl = new GridLayout();
		gl.marginHeight = 2;
		gl.marginWidth = 2;
		selComp.setLayout(gl);
		
		lSL = new List(selComp, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		lSL.setItems(selectionList.getStringArray());
		lSL.setFont(textF);
		
		GridData gd = new GridData();
		gd.heightHint = 50;
		gd.minimumHeight = 50;
		gd.minimumWidth = 50;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		lSL.setLayoutData(gd);
		
		setBGColor();
		
		lSL.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if(e.character == SWT.DEL){
					// handle delete key pressed... 
					if(lSL.getSelectionIndex() != -1){
						lSL.remove(lSL.getSelectionIndex());
						SelectionList selList = getParamData();
						selList.clearList();
						for(String s : lSL.getItems()){
							selList.add(s);
						}
						updateParamViaToolInterface();
						AvoGlobal.glView.updateGLView = true;
					}
				}
			}
			public void keyReleased(KeyEvent e) {
			}			
		});
		
		lSL.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {
				SelectionList sl = getParamData();
				System.out.println(param.getLabel() + " just got focus!");
				sl.hasFocus = true;
			}
			public void focusLost(FocusEvent e) {
				SelectionList sl = getParamData();
				System.out.println(param.getLabel() + " just lost focus!");
				sl.hasFocus = false;
			}			
		});
		
		//
		// Add param listener!
		//		
		paramListener = new ParamListener(){
			public void paramModified() {
				lSL.setItems(getParamData().getStringArray());
				setBGColor();
			}
			public void paramSwitched() {
			}			
		};	
		AvoGlobal.paramEventHandler.addParamListener(paramListener);
		
	}
	
	SelectionList getParamData(){
		try{
			SelectionList data = param.getDataSelectionList();
			return data;
		}catch(ParamNotCorrectTypeException e){
			System.out.println(" *** WARNING *** PCompSelectionList :: param was not of type SelectionList!");
			return new SelectionList();
		}
	}
	
	void setBGColor(){		
		SelectionList sl = getParamData();		
		if(sl.isSatisfied){
			selComp.setBackground(AvoGlobal.COLOR_PARAM_SEL_SAT);
		}else{
			selComp.setBackground(AvoGlobal.COLOR_PARAM_SEL_UNSAT);
		}		
	}
}
