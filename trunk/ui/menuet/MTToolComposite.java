package ui.menuet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class MTToolComposite extends Composite{

	MTToolComposite(Composite parent, int type, MenuetElement mElement) {
		super(parent, type);
		
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
			t1.setText(mElement.getToolTipText());
		}else{
			t1.setText("null tooltip info...");
		}
		t1.setEditable(false); 
		t1.setEnabled(false);
	}
	
}
