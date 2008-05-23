package ui.paramdialog;


import net.sf.avocado_cad.eclipse.ui.AvoGlobal;
import net.sf.avocado_cad.model.api.adt.IParam;
import net.sf.avocado_cad.model.api.event.BackendGlobal;
import net.sf.avocado_cad.model.api.event.ParamListener;

import org.eclipse.swt.widgets.Composite;

import ui.tools.ToolModel;
import backend.adt.ParamSet;


//
//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the 
//GNU General Public License (GPL).
//
//This file is part of avoCADo.
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
public class ParamComp extends Composite{

	protected IParam    param;
	protected ParamSet paramSet;
	
	public ParamComp(Composite parent, int style, ParamSet paramSet){
		super(parent, style);
		this.paramSet = paramSet;		
	}
	
	public void updateParamViaToolInterface(){
		ToolModel toolModel = AvoGlobal.paramSetToolModels.get(paramSet);
		if(toolModel != null){			
		if(toolModel.paramSetIsValid(paramSet)){
			toolModel.updateDerivedParams(paramSet);
		}else{
			System.out.println(" *** PARAM SET *** param set was not valid! not performing derived parameter update.");
		}	
	}
	}
	
	public ParamListener paramListener;
	
	public void removeParamListener(){
		BackendGlobal.paramEventHandler.removeParamListener(paramListener);
	}
}
