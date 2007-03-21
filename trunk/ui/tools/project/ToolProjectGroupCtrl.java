package ui.tools.project;

import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolCtrlProject;
import backend.global.AvoGlobal;

public class ToolProjectGroupCtrl implements ToolCtrlProject{

	public void glMouseDown(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseDrag(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseMovedUp(double x, double y, double z, MouseEvent e) {
	}

	public void glMouseUp(double x, double y, double z, MouseEvent e) {
	}

	public void menuetElementDeselected() {		
	}

	public void menuetElementSelected() {
		// Add a new Group to the project!
		AvoGlobal.project.addNewGroup();
	}

}
