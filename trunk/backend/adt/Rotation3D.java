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
public class Rotation3D {
	protected double x = 0.0;
	protected double y = 0.0;
	protected double z = 0.0;
	
	/**
	 * Abstract Data Type: Rotation 3D
	 * rotate about the x, y, and z axis
	 * in X->Y->Z order.  angles are given
	 * in degrees.
	 * @param x
	 * @param y
	 * @param z
	 */
	public Rotation3D(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	/**
	 * get the X rotation value
	 * @return
	 */
	public double getXRot(){
		return x;
	}
	
	/**
	 * get the Y rotation value
	 * @return
	 */
	public double getYRot(){
		return y;
	}
	
	/**
	 * get the Z rotation value
	 * @return
	 */
	public double getZRot(){
		return z;
	}
	
	/**
	 * set the X rotation value
	 * @param newX
	 */
	public void setXRot(double newX){
		x = newX;
	}

	/**
	 * set the Y rotation value
	 * @param newY
	 */
	public void setYRot(double newY){
		y = newY;
	}
	
	/**
	 * set the Z rotation value
	 * @param newY
	 */
	public void setZRot(double newZ){
		z = newZ;
	}
	
	
}
