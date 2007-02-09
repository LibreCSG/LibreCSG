package backend.adt;

import backend.global.AvoGlobal;


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
public class Param {

	protected PType  ptype;
	protected String plabel;
	protected Object pdata;

	/**
	 * A parameter encapsulates data with both
	 * a label and a type so that it can be
	 * used for storage and display.
	 * @param label
	 * @param data
	 */
	public Param(String label, Point2D data){
		pdata  = data;
		plabel = label;
		ptype =  PType.Point2D;
	}
	
	public Param(String label, boolean data){
		pdata  = data;
		plabel = label;
		ptype =  PType.Boolean;	
	}
	
	public Param(String label, int data){
		pdata  = data;
		plabel = label;
		ptype =  PType.Int;	
	}
	
	public Param(String label, double data){
		pdata  = data;
		plabel = label;
		ptype =  PType.Double;	
	}
	
	public Param(String label, String data){
		pdata  = data;
		plabel = label;
		ptype =  PType.String;	
	}
	
	/**
	 * get the parameter type
	 * @return
	 */
	public PType getType(){
		return ptype;
	}
	
	/**
	 * get the parameter's label. 
	 * This should be human-readable.
	 * @return
	 */
	public String getLabel(){
		return plabel;
	}
	
	
	/**
	 * get the parameter's data
	 * @return
	 */
	public Object getData(){
		return pdata;
	}
	
	/**
	 * change the data associated with a parameter.
	 * note that data must be of the same type in
	 * which the parameter was originally constructed.
	 * @param data
	 */
	public void change(Object data){
		if(data.getClass().equals(pdata.getClass())){
			pdata = data;
			AvoGlobal.paramDialog.notifyParamChangeListener();	
		}else{
			System.out.println("No change made in Param.change since data was not same type as originally constructed.");
			System.out.println("  --> " + data.getClass().getName() + " != " + pdata.getClass().getName());
		}
	}
	
}
