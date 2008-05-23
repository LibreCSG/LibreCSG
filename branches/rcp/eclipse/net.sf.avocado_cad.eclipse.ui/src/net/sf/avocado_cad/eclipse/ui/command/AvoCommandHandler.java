package net.sf.avocado_cad.eclipse.ui.command;

import net.sf.avocado_cad.eclipse.ui.AvoGlobal;
import net.sf.avocado_cad.model.api.event.BackendGlobal;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;

import backend.model.Project;

public class AvoCommandHandler extends AbstractHandler{

	public static IAction newAction;
	public static IAction quitAction;
	public static IAction helpAction;
	
	

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		try {
			String commandName = arg0.getCommand().getName();
			if("NewCommand".equals(commandName)) {
				AvoGlobal.project = new Project();
				BackendGlobal.modelEventHandler.notifyElementRemoved();					
				AvoGlobal.intializeNewAvoCADoProject();
			} else if ("QuitCommand".equals(commandName)) {
				Display.getCurrent().dispose();
			} else if ("VisitWebsite".equals(commandName)) {
				Program.launch("http://avocado-cad.sourceforge.net/");
			}
		} catch (NotDefinedException e) {
			
		}
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
