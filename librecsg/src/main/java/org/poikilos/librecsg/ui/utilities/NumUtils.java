package org.poikilos.librecsg.ui.utilities;

import java.text.DecimalFormat;
import java.text.NumberFormat;


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
public class NumUtils {

	/**
	 * return a string that is at least a certain
	 * number of characters in length.  This is helpful
	 * when populating text boxes with number data that
	 * may otherwise be short and packed too tightly
	 * by the GUI components.
	 * @param d
	 * @param length
	 * @return
	 */
	public static String doubleToAtLeastString(double d, int length){
		//String s = String.valueOf(d);
		NumberFormat formatter = new DecimalFormat ( "0.000" ) ;
	    String s = formatter.format ( d ) ;
		for(int i=s.length(); i<length; i++){
			s += "0";
		}
		return s;
	}


	public static String doubleToFixedString(double d, int length){
		String s = doubleToAtLeastString(d, length);
		if(s.charAt(0) != '-'){
			return " " + s.substring(0,length-1);
		}
		return s.substring(0,length);
	}


	public static boolean stringIsNumber(String s){
		try{
			Double.parseDouble(s);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}

}
