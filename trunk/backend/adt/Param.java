package backend.adt;


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

	public enum PType {
		Boolean, Int, Double, String, Point2D, Point3D
	}
	
	protected PType  ptype;
	protected String plabel;
	protected Object pdata;

	/**
	 * A parameter encapsulates data with both
	 * a label and a type so that it can be
	 * used for storage and display.
	 * @param data
	 * @param label
	 */
	public Param(Point2D data, String label){
		pdata  = data;
		plabel = label;
		ptype =  PType.Point2D;
	}
	
	public Param(boolean data, String label){
		pdata  = data;
		plabel = label;
		ptype =  PType.Boolean;	
	}
	
	public Param(int data, String label){
		pdata  = data;
		plabel = label;
		ptype =  PType.Int;	
	}
	
	public Param(double data, String label){
		pdata  = data;
		plabel = label;
		ptype =  PType.Double;	
	}
	
	public Param(String data, String label){
		pdata  = data;
		plabel = label;
		ptype =  PType.String;	
	}
	
}
