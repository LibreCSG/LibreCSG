package backend.adt;

import java.util.Observable;


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
public class ParamEvents extends Observable{
	
	public final static int PARAM_CHANGED = 423376;
	
	public ParamEvents(){
		super();
	}
	
	
	public void notifyParamChanged(){
		notifyObservers(PARAM_CHANGED);
		setChanged();
	}
	
	public void addParamListener(ParamListener p){
		if(p != null){
			addObserver(p);
			setChanged(); // update the model since a new listener has been added
		}else{
			System.out.println("you can only add non-NULL ParamListeners!");
		}
	}
	
	public void removeParamListener(ParamListener p){
		if(p != null){
			deleteObserver(p);
		}else{
			System.out.println("param listener could not be removed because it was NULL!");
		}
	}
}
