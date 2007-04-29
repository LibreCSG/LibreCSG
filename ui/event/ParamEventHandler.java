package ui.event;



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
public class ParamEventHandler {
	public final static int PARAM_MODIFIED = 423376;
	public final static int PARAM_SWITCHED = 429832;
	
	protected CustObservable observable;
	
	public ParamEventHandler(){
		observable = new CustObservable();
	}
	
	/**
	 * Notify listeners that the data within the
	 * currently active ParamSet has been modified
	 * and should be updated on the dispaly accordingly.
	 */
	public void notifyParamModified(){
		observable.notifyObservers(PARAM_MODIFIED);
		observable.setChanged();
	}
	
	/**
	 * Notify listeners that the ParamSet has been 
	 * switched (i.e., changed to a new tool or null).
	 */
	public void notifyParamSwitched(){
		observable.notifyObservers(PARAM_SWITCHED);
		observable.setChanged();
	}
	
	
	
	public void addParamListener(ParamListener p){
		if(p != null){
			observable.addObserver(p);
			observable.setChanged(); // update the model since a new listener has been added
		}else{
			System.out.println("you can only add non-NULL ParamListeners!");
		}
	}
	
	public void removeParamListener(ParamListener p){
		if(p != null){
			observable.deleteObserver(p);
		}else{
			System.out.println("param listener could not be removed because it was NULL!");
		}
	}
}
