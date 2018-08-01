package org.poikilos.librecsg.backend.adt;


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
* @created Apr. 2007
*/
public class Translation3D {
	protected double x = 0.0;
	protected double y = 0.0;
	protected double z = 0.0;

	/**
	 * Abstract Data Type: Translation 3D
	 * translate in the x, y, and z directions
	 * @param x
	 * @param y
	 * @param z
	 */
	public Translation3D(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}


	/**
	 * get the X translation value
	 * @return
	 */
	public double getXTrans(){
		return x;
	}

	/**
	 * get the Y translation value
	 * @return
	 */
	public double getYTrans(){
		return y;
	}

	/**
	 * get the Z translation value
	 * @return
	 */
	public double getZTrans(){
		return z;
	}

	/**
	 * set the X translation value
	 * @param newX
	 */
	public void setXTrans(double newX){
		x = newX;
	}

	/**
	 * set the Y translation value
	 * @param newY
	 */
	public void setYTrans(double newY){
		y = newY;
	}

	/**
	 * set the Z translation value
	 * @param newY
	 */
	public void setZTrans(double newZ){
		z = newZ;
	}

	public String toString(){
		return "Translation3D: (" + Double.toString(x) + "," + Double.toString(y) + "," + Double.toString(z) + ")";
	}
}
