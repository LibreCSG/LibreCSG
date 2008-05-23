package ui.tools;

import net.sf.avocado_cad.model.api.adt.IParamSet;
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

/**
 * The ToolModel interface defines a large set of data manipulation
 * and validation methods particular to each tool. 
 */
public interface ToolModel {

	/**
	 * Construct a new ParamSet that is initialized to
	 * default values for this tool.  
	 * @return a <b>valid</b> ParamSet for this tool.
	 */
	abstract public IParamSet constructNewParamSet();
	
	/**
	 * Finalize the ParamSet for the given tool.  For example, this will 
	 * be called when the "done" button of the ParamDialog is clicked 
	 * for a particular tool.
	 * @param paramSet the set of parameters to finalize via this ToolModel.
	 */
	abstract public void finalize(ParamSet paramSet);
	
	/**
	 * verify that the parameter data is in fact valid/complete.
	 * This include checks of each expected element in the set
	 * for inclusion in the set and that it is the correct type.
	 * @param paramSet ParamSet to be checked.
	 * @return true <em>iff</em> paramSet is valid for the given ToolModel
	 */
	abstract public boolean paramSetIsValid(IParamSet paramSet);
	
	/**
	 * update all derived params in the ParamSet if any exist via this ToolModel.
	 * @param paramSet a <em>valid</em> paramSet for the given ToolModel.
	 */	
	abstract public void updateDerivedParams(ParamSet paramSet);
	
	
	/**
	 * check to see if the element is worth keeping by inspecting it paramSet 
	 * values.  For example, if a line has its start and end points at the 
	 * same location, the line is not worth keeping and should return false.
	 * @param paramSet a <em>valid</em> paramSet for the given ToolModel.
	 * @return true if the element is worth keeping.
	 */	
	abstract public boolean isWorthKeeping(IParamSet paramSet);
	
}
