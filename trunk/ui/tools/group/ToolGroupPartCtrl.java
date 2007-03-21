package ui.tools.group;

import org.eclipse.swt.events.MouseEvent;

import ui.tools.ToolCtrlGroup;
import backend.global.AvoGlobal;
import backend.model.Group;

public class ToolGroupPartCtrl implements ToolCtrlGroup{

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
		// Add a new Part to the Group
		Group group = AvoGlobal.project.getActiveGroup();
		if(group != null){
			group.addNewPart();
		}else{
			System.out.println("ToolGroupPartCtrl(menuetElementSelected): No group was active? Cannot add new Part!");
		}
	}

}
