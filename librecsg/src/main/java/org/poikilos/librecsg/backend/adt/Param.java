package org.poikilos.librecsg.backend.adt;

import org.poikilos.librecsg.backend.global.AvoGlobal;


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
public class Param {

	protected final ParamType  ptype;
	protected String plabel;
	protected Object pdata;
	protected boolean derivedParam = false;

	/**
	 * A parameter encapsulates data with both
	 * a label and a type so that it can be
	 * used for storage and display.
	 * @param label the parameter's short description
	 * @param data the data to store, <type> dependent.
	 */
	public Param(String label, Point2D data){
		pdata  = data;
		plabel = label;
		ptype  = ParamType.Point2D;
	}

	public Param(String label, Point3D data){
		pdata  = data;
		plabel = label;
		ptype  = ParamType.Point3D;
	}

	public Param(String label, Rotation3D data){
		pdata  = data;
		plabel = label;
		ptype  = ParamType.Rotation3D;
	}

	public Param(String label, Boolean data){
		pdata  = data;
		plabel = label;
		ptype  = ParamType.Boolean;
	}

	public Param(String label, Integer data){
		pdata  = data;
		plabel = label;
		ptype  = ParamType.Integer;
	}

	public Param(String label, Double data){
		pdata  = data;
		plabel = label;
		ptype  = ParamType.Double;
	}

	public Param(String label, String data){
		pdata  = data;
		plabel = label;
		ptype  = ParamType.String;
	}

	public Param(String label, SelectionList data){
		pdata  = data;
		plabel = label;
		ptype  = ParamType.SelectionList;
	}


	/**
	 * get the parameter type
	 * @return
	 */
	public ParamType getType(){
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

	public Point2D getDataPoint2D() throws ParamNotCorrectTypeException{
		if(ptype == ParamType.Point2D){
			return (Point2D)pdata;
		}
		throw new ParamNotCorrectTypeException();
	}

	public Point3D getDataPoint3D() throws ParamNotCorrectTypeException{
		if(ptype == ParamType.Point3D){
			return (Point3D)pdata;
		}
		throw new ParamNotCorrectTypeException();
	}

	public Rotation3D getDataRotation3D() throws ParamNotCorrectTypeException{
		if(ptype == ParamType.Rotation3D){
			return (Rotation3D)pdata;
		}
		throw new ParamNotCorrectTypeException();
	}

	public Boolean getDataBoolean() throws ParamNotCorrectTypeException{
		if(ptype == ParamType.Boolean){
			return (Boolean)pdata;
		}
		throw new ParamNotCorrectTypeException();
	}

	public Integer getDataInteger() throws ParamNotCorrectTypeException{
		if(ptype == ParamType.Integer){
			return (Integer)pdata;
		}
		throw new ParamNotCorrectTypeException();
	}

	public Double getDataDouble() throws ParamNotCorrectTypeException{
		if(ptype == ParamType.Double){
			return (Double)pdata;
		}
		throw new ParamNotCorrectTypeException();
	}

	public String getDataString() throws ParamNotCorrectTypeException{
		if(ptype == ParamType.String){
			return (String)pdata;
		}
		throw new ParamNotCorrectTypeException();
	}

	public SelectionList getDataSelectionList() throws ParamNotCorrectTypeException{
		if(ptype == ParamType.SelectionList){
			return (SelectionList)pdata;
		}
		throw new ParamNotCorrectTypeException();
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
			AvoGlobal.paramEventHandler.notifyParamModified();
		}else{
			System.out.println("No change made in Param.change since data was not same type as originally constructed.");
			System.out.println("  --> " + data.getClass().getName() + " != " + pdata.getClass().getName());
		}
	}

	/**
	 * test to see if the parameter is derived from
	 * others and therefore should not be altered
	 * directly by the user.
	 * @return
	 */
	public boolean isDerivedParam(){
		return derivedParam;
	}

	/**
	 * specify if the parameter is derived frrom
	 * others and therefore should not be altered
	 * directly by the user.
	 * @param isDerived
	 */
	public void setParamIsDerived(boolean isDerived){
		derivedParam = isDerived;
	}

	public String toString(){
		String string = "";
		try{
		switch(this.ptype){
			case Boolean: {
				string = getDataBoolean().toString();
				break;
			}
			case Integer: {
				string = getDataInteger().toString();
				break;
			}
			case Double: {
				string = getDataDouble().toString();
				break;
			}
			case String: {
				string = getDataString().toString();
				break;
			}
			case Point2D: {
				string = getDataPoint2D().toString();
				break;
			}
			case Point3D: {
				string = getDataPoint3D().toString();
				break;
			}
			case Rotation3D: {
				string = getDataRotation3D().toString();
				break;
			}
			case SelectionList: {
				string = getDataSelectionList().toString();
				break;
			}
			default: {
				string = "unknown type!! update Param.toString!";
				break;
			}
		}
		}catch(Exception ex){
			System.out.println(this.getClass().getCanonicalName() + " :toString: " + ex.getClass());
		}
		return "(" + this.plabel + "," + string + ")";
	}

}
