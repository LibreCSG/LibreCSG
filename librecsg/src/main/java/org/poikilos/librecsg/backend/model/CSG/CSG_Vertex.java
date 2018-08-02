package org.poikilos.librecsg.backend.model.CSG;

import java.util.LinkedList;
import java.util.List;

import com.jogamp.opengl.GL2;
import org.poikilos.librecsg.backend.adt.Point2D;
import org.poikilos.librecsg.backend.adt.Point3D;
import org.poikilos.librecsg.backend.adt.Rotation3D;
import org.poikilos.librecsg.backend.adt.Translation3D;


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
* @created Mar. 2007
*/

/**
 * Constructive Solid Geometry :: Vertex<br/><br/>
 *
 * A 3D point in space used as a stand-alone point,
 * part of a line, or a node on a polygon/face.
 * Adjacent vertices are also stored in a list.<br/><br/>
 *
 * The CSG_Vertex also stores status information about
 * being on the inside/outside/boundary of another solid.
 * Initially this status is set to unknown.<br/><br/>
 *
 * Algorithms and structures from:<br/>
 * - Laidlaw, Trumbore, and Hughes <br/>
 * - "Constructive Solid Geometry for Polyhedral Objects"<br/>
 * - SIGGRAPH 1986, Volume 20, Number 4, pp.161-170
 */
public class CSG_Vertex {

	final double TOL = 1e-10;

	private final double x,y,z;
	List<CSG_Vertex> adjacentVertices = new LinkedList<CSG_Vertex>();

	public CSG_Vertex(double x, double y, double z){
		this.x = cleanDouble(x);
		this.y = cleanDouble(y);
		this.z = cleanDouble(z);
	}

	public CSG_Vertex(Point2D p2D, double z){
		this.x = p2D.getX();
		this.y = p2D.getY();
		this.z = z;
	}

	public CSG_Vertex(Point3D p3D){
		this.x = p3D.getX();
		this.y = p3D.getY();
		this.z = p3D.getZ();
	}

	// just to clean up values around zero (seeing -0.0 is annoying)
	private double cleanDouble(double input){
		if(input < TOL && input > -TOL){
			return 0.0;
		}
		return input;
	}

	public double getX(){
		return x;
	}

	public double getY(){
		return y;
	}

	public double getZ(){
		return z;
	}

	public double[] getXYZ(){
		return new double[] {x, y, z};
	}

	public CSG_Vertex deepCopy(){
		CSG_Vertex clone = new CSG_Vertex(x,y,z);
		return clone;
	}

	public CSG_Vertex getScaledCopy(double scale){
		return new CSG_Vertex(scale*x, scale*y, scale*z);
	}

	public double getDistFromOrigin(){
		return Math.sqrt(x*x + y*y + z*z);
	}

	public double getDistBetweenVertices(CSG_Vertex vertB){
		return this.subFromVertex(vertB).getDistFromOrigin();
	}

	/**
	 * computes the cross-product of the two vertices.
	 * @param vertB the given vector to compute (this)x(vertB)
	 * @return the CSG_Vextor representing the cross-product
	 *   of this and vertB.
	 */
	public CSG_Vertex getCrossProduct(CSG_Vertex vertB){
		double cx = y*vertB.z - z*vertB.y;
		double cy = z*vertB.x - x*vertB.z;
		double cz = x*vertB.y - y*vertB.x;
		return new CSG_Vertex(cx,cy,cz);
	}

	public double getDotProduct(CSG_Vertex vertB){
		return x*vertB.x + y*vertB.y + z*vertB.z;
	}

	public CSG_Vertex addToVertex(CSG_Vertex vertB){
		return new CSG_Vertex(x+vertB.x, y+vertB.y, z+vertB.z);
	}

	/**
	 * @param vertB CSG_Vertex to subtract from this
	 * @return this - vertB
	 */
	public CSG_Vertex subFromVertex(CSG_Vertex vertB){
		return new CSG_Vertex(x-vertB.x, y-vertB.y, z-vertB.z);
	}

	public String toString(){
		return "CSG_Vert(" + x + "," + y + "," + z + ")";
	}

	public CSG_Vertex getUnitLength(){
		double dist = getDistFromOrigin();
		if(dist > TOL){
			return getScaledCopy(1.0/dist);
		}else{
			System.out.println("could not get unit Length Vertex from origin!");
			return this;
		}
	}

	public void drawPointForDebug(GL2 gl){
		gl.glColor3d(0.0, 1.0, 0.0);
		gl.glBegin(GL2.GL_POINTS);
			gl.glVertex3d(x, y, z);
		gl.glEnd();
	}

	public void glDrawVertex(GL2 gl){
		gl.glBegin(GL2.GL_POINTS);
			gl.glVertex3d(x, y, z);
		gl.glEnd();
	}

	public CSG_Vertex getFlippedVertex(){
		return new CSG_Vertex(-x, -y, -z);
	}

	/**
	 * apply rotation in X, Y, Z order, then translation.
	 * @param translation 3D translation
	 * @param rotation 3D rotation
	 * @return a copy of the vertex with the translation/rotation applied.
	 */
	public CSG_Vertex getTranslatedRotatedCopy(Translation3D translation, Rotation3D rotation){
		double newX = x;
		double newY = y;
		double newZ = z;

		//System.out.println("Rotation: (" + rotation.getX() + "," + rotation.getY() + "," + rotation.getZ() + ")");
		if(rotation != null){
			double rotX = rotation.getXRot();
			double rotY = rotation.getYRot();
			double rotZ = rotation.getZRot();

			// rotate about Z axis
			double newX2 = newX*Math.cos(rotZ) - newY*Math.sin(rotZ);
			double newY2 = newX*Math.sin(rotZ) + newY*Math.cos(rotZ);
			double newZ2 = newZ;

			// rotate about Y axis
			double newZ3 = newZ2*Math.cos(rotY) - newX2*Math.sin(rotY);
			double newX3 = newZ2*Math.sin(rotY) + newX2*Math.cos(rotY);
			double newY3 = newY2;

			// rotate about X axis
			double newY4 = newY3*Math.cos(rotX) - newZ3*Math.sin(rotX);
			double newZ4 = newY3*Math.sin(rotX) + newZ3*Math.cos(rotX);
			double newX4 = newX3;

			newX = newX4;
			newY = newY4;
			newZ = newZ4;
		}
		if(translation != null){
			return new CSG_Vertex(	newX + translation.getXTrans(),
									newY + translation.getYTrans(),
									newZ + translation.getZTrans());
		}else{
			return new CSG_Vertex(newX, newY, newZ);
		}
	}

	public boolean equalsVertex(CSG_Vertex v2){
		double dX = this.x-v2.x;
		double dY = this.y-v2.y;
		double dZ = this.z-v2.z;
		if(dX <= TOL && dX >= -TOL && dY <= TOL && dY >= -TOL && dZ <= TOL && dZ >= -TOL){
			return true;
		}
		return false;
	}


	public float[] toFloatArray(){
		float[] floatArray=new float[]{(float)this.x,(float)this.y,(float)this.z};
		return floatArray;
	}

	public double[] toDoubleArray(){
		double[] doubleArray = new double[]{(double)this.x,(double)this.y,(double)this.z};
		return doubleArray;
	}

}
