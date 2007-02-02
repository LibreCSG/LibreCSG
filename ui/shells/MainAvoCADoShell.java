package ui.shells;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class MainAvoCADoShell extends Shell{

	/**
	 * create the main avoCADo shell
	 * @param display
	 */
	public MainAvoCADoShell(Display display){
		super(display);
		setupShell();
		this.setText("avoCADo");
		this.setSize(800, 600);		//TODO: set intial size to last known size
		this.open();
	}
	
	/**
	 * yes, subclassing a shell is not so swell...
	 * But it offers a nice level of abstraction
	 * as long as routines within this class 
	 * make an effort to "play nicely."
	 */
	protected void checkSubclass(){
	}

	
	/**
	 * setup the shell with each of its key components
	 * (menubar, menuet, treeviewer, glview, quicksettings, etc.)
	 */
	void setupShell(){
		//TODO: populate the main shell!
		
	}
	
}
