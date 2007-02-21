package backend.adt;

import java.util.LinkedList;
import java.util.List;


//
//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the 
//GNU General Public License (GPL).
//
//This file is part of avoADo.
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
public class SelectionList {

	List<String> selectionList = new LinkedList<String>();
	
	/**
	 * specifies that the ParamSelection has been satisfied.
	 * For example, if two parts must be selected, the
	 * isSatisfied flag should only be set to true iff two
	 * have been chosen.
	 */
	public boolean isSatisfied = false;
	
	/**
	 * indicates that the this ParamSelection has focus in
	 * the userInterface.  The ToolInterface code can check
	 * various ParamSelections for this flag to determine
	 * if the user is wanting to alter a particular selection.
	 */
	public boolean hasFocus = false;
	
	/**
	 * A list of selected items, used for construction
	 * and display of features that require references to
	 * others. (sketchs, lines, regions, etc.)<br/><br/>
	 * 
	 * There are 4 main parts to the SelectionList.<br/>
	 * (1) String for each selection (human-readable)<br/>
	 * (2) index of the selected item (so when a user clicks, you know which one)<br/>
	 * (3) boolean hasFocus -- indicator that the selection has focus in the UI.<br/>
	 * (4) boolean isSatisfied -- indicates whether or not the current selection is sufficient.
	 */
	public SelectionList(){		
	}
	
	
	public void add(String string){
		if(string != null){
			selectionList.add(string);
		}else{
			System.out.println(" -- could not add element to selectionList since String was null!");
		}
	}
	
	public String getStringAtIndex(int i){
		if(i >= 0 && i < selectionList.size()){
			return selectionList.get(i);
		}else{
			System.out.println(" -- could not get element because index was invalid! index:" + i + ", listSize:" + selectionList.size());
			return null;
		}
	}
	
	public void removeAtIndex(int i){
		if(i >= 0 && i < selectionList.size()){
			selectionList.remove(i);
		}else{
			System.out.println(" -- could not remove element because index was invalid! index:" + i + ", listSize:" + selectionList.size());
		}
	}
	
	public void clearList(){
		selectionList.clear();
	}
	
	public String toString(){
		String allElements = "";
		for(String string : selectionList){
			allElements += string + ", ";
		}
		return "SelectionList: size(" + selectionList.size() + ") -- " + allElements;
	}
	
	
	
}
