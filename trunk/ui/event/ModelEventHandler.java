package ui.event;


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
public class ModelEventHandler {
	public final static int ELEMENT_ADDED          = 753826;
	public final static int ELEMENT_REMOVED        = 734221;
	public final static int ACTIVE_ELEMENT_CHANGED = 718291;
	
	
	protected CustObservable observable;
	
	public ModelEventHandler(){
		observable = new CustObservable();
	}
	
	
	public void notifyElementAdded(){
		observable.notifyObservers(ELEMENT_ADDED);
		observable.setChanged();
	}
	
	public void notifyElementRemoved(){
		observable.notifyObservers(ELEMENT_REMOVED);
		observable.setChanged();
	}
	
	public void notifyActiveElementChanged(){
		observable.notifyObservers(ACTIVE_ELEMENT_CHANGED);
		observable.setChanged();
	}
	
	
	
	public void addModelListener(ModelListener g){
		if(g != null){
			observable.addObserver(g);
			observable.setChanged(); // update the model since a new listener has been added
		}else{
			System.out.println("you can only add non-NULL ModelListeners!");
		}
	}
	
	public void removeModelListener(ModelListener g){
		if(g != null){
			observable.deleteObserver(g);
		}else{
			System.out.println("Model listener could not be removed because it was NULL!");
		}
	}
}
