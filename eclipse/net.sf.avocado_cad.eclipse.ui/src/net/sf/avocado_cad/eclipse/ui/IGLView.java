package net.sf.avocado_cad.eclipse.ui;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

public interface IGLView {

	public void setViewIso();

	public void setViewSketch();
	
	public void setViewLeft();
	
	public void setViewFront();
	
	public void setViewTop();

	public void setBounds(Rectangle bounds);
	
	public void setup(Composite comp);
}
