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
import backend.global.AvoColors;
import backend.global.AvoGlobal;

public class PCompInteger extends ParamComp{

	private Text tD;	
	
	public PCompInteger(Composite parent, int style, Param p, ParamSet paramSet){
		super(parent, style, paramSet);
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
		if(p.getType() != PType.Integer){
			System.out.println("trying to display a non-Integer in a PCompInteger (paramDialog)");
			return;
		}
		
		Integer pD = getParamData();
		
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
		
		//
		// Value:
		//
		tD = new Text(this, SWT.BORDER);		
		tD.setFont(textF);
		tD.setText(Integer.toString(pD));
		
		//
		// Value: handle key presses
		//
		tD.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {
				if(e.character == '\r'){
					// check to see if string is a valid number
					if(NumUtils.stringIsNumber(tD.getText())){
						Integer pD = getParamData();
						pD = Integer.parseInt(tD.getText());
						param.change(pD);
						updateParamViaToolInterface();
						AvoGlobal.glView.updateGLView = true;
					}else{
						tD.setText(Integer.toString(getParamData()));
					}
				}
			}
			public void keyReleased(KeyEvent e) {		
			}			
		});
		
		//
		// perform field validation
		//
		tD.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {			
			}
			public void focusLost(FocusEvent e) {
				// check to see if string is a valid number
				if(NumUtils.stringIsNumber(tD.getText())){
					Integer pD = getParamData();
					pD = Integer.parseInt(tD.getText());
					param.change(pD);
					updateParamViaToolInterface();
					AvoGlobal.glView.updateGLView = true;
				}else{
					tD.setText(Integer.toString(getParamData()));
				}
			}			
		});
		
		//
		// Disable user alteration if the parameter is derived
		//
		if(p.isDerivedParam()){
			tD.setEditable(false);
			tD.setBackground(AvoColors.COLOR_PARAM_DERIVED);			
		}		
		
		//
		// Add param listener!
		//		
		paramListener = new ParamListener(){
			public void paramModified() {
				tD.setText(Integer.toString(getParamData()));
			}
			public void paramSwitched() {
			}			
		};	
		AvoGlobal.paramEventHandler.addParamListener(paramListener);
		
	}
	
	Integer getParamData(){
		try{
			Integer data = param.getDataInteger();
			return data;
		}catch(ParamNotCorrectTypeException e){
			System.out.println(" *** WARNING *** PCompDouble :: param was not of type Integer!");
			return 0;
		}
	}
	
}
