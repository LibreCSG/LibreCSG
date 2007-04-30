package backend.model.sketch;

import javax.media.opengl.GL;

import backend.adt.Rotation3D;
import backend.adt.Translation3D;
import backend.model.CSG.CSG_Plane;
import backend.model.CSG.CSG_Ray;
import backend.model.CSG.CSG_Vertex;


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
 * The "sketch plane" is a 2D plane in a 3D space.  
 * In other words, it lets purely 2D shapes be placed on a plane that could be at 
 * any angle/orientation in 3D space.  The sketch plane allows for calculations 
 * and interactions to remain much simpler when sketching than they would be if 
 * every line, arc, curve, etc. was defined directly in 3D.  In particular, there 
 * is rotation information contained in the SketchPlane that allows openGL and 3D 
 * CSG intersection code to orient to the 2D space where the sketch is located.
 */
public class SketchPlane {

	private CSG_Vertex origin;
	private CSG_Vertex normal;
	private CSG_Vertex xAxis;
	private CSG_Vertex yAxis;
	
	private double TOL = 1e-10;
	
	/*
	public SketchPlane(CSG_Vertex origin, CSG_Vertex normal, CSG_Vertex xAxis){
		System.out.println("SketchPlane(constructor): old constructor is being used.. this should be fixed soon.");
		double dotProd = normal.getDotProduct(xAxis);
		if(dotProd > TOL || dotProd < -TOL){
			System.out.println("SketchPlane(Constructor): normal and var1Axis were not orthogonal! dotProd=" + dotProd);
		}
		this.origin   = origin;
		this.normal   = normal.getUnitLength();
		this.xAxis = xAxis.getUnitLength();
		this.yAxis = this.normal.getVectCrossProduct(this.xAxis).getUnitLength();
	}
	*/
	
	/**
	 * construct sketchPlane from a CSG_Plane
	 * @param csgPlane the plane to place the sketch on
	 */
	public SketchPlane(CSG_Plane csgPlane){
		if(csgPlane == null){
			System.out.println("SketchPlane(constructor): a NULL CSG_Plane was given.  u betta check y'self.");
			return;
		}
		this.normal = csgPlane.getNormal().getUnitLength();
		this.xAxis  = getXAxisFromNormal(this.normal);
		this.origin = csgPlane.getRayIntersection(new CSG_Ray(new CSG_Vertex(0.0, 0.0, 0.0),this.normal));
		this.yAxis = this.normal.getCrossProduct(this.xAxis).getUnitLength();
		//System.out.println("sketch plane constructed with origin: " + origin + ", and xAxis: " + xAxis);
	}
	
	/** 
	 * given a unit normal vector of a plane through the origin,
	 * derive a suitable (and sensible) "x-axis" for the plane.
	 * the 2D "x-axis" should not be confused with the actual
	 * X,Y,Z coordinates in 3D space.
	 * 
	 * This took a while to get something reasonable... 
	 * there may be bugs, but really think before you act here. :)
	 * 
	 * @param normal the plane's normal vector
	 * @return the unit length x-axis vector. 
	 */
	private CSG_Vertex getXAxisFromNormal(CSG_Vertex normal){
		double nx = normal.getX();
		double ny = normal.getY();
		double nz = normal.getZ();
		double nxAbs = Math.abs(nx);
		double nyAbs = Math.abs(ny);
		double nzAbs = Math.abs(nz);
		double xx = 1.0;
		double xy = 1.0;
		double xz = 1.0;
		
		// CASE: Z NORM GREATEST
		if (nzAbs >= nxAbs && nzAbs >= nyAbs){	
			if(nxAbs >= nyAbs){
				xx = (1-nxAbs);
				xy = (1-xx)*(Math.sqrt(2)-1);
			}else{
				xx = (1-nyAbs);
				xy = (1-xx)*(Math.sqrt(2)-1)*(-Math.signum(ny)*Math.signum(nz));
			}
			xz = -(nx*xx + ny*xy)/nz;	// dot product of normal with point on plane = 0
		}

		// CASE: X NORM GREATEST
		if (nxAbs >= nyAbs && nxAbs >= nzAbs){
			if(nyAbs >= nzAbs){
				xy = (1-nyAbs);
				xz = (1-xy)*(Math.sqrt(2)-1);
			}else{
				xy = (1-nzAbs);
				xz = (1-xy)*(Math.sqrt(2)-1)*(-Math.signum(nz)*Math.signum(nx));
			}
			xx = -(ny*xy + nz*xz)/nx;	// dot product of normal with point on plane = 0
		}

		// CASE: Y NORM GREATEST
		if (nyAbs >= nzAbs && nyAbs >= nxAbs){
			if(nzAbs > nxAbs){
				xz = (1-nzAbs);
				xx = (1-xz)*(Math.sqrt(2)-1);
			}else{
				xz = (1-nxAbs);
				xx = (1-xz)*(Math.sqrt(2)-1)*(-Math.signum(nx)*Math.signum(ny));
			}
			xy = -(nx*xx + nz*xz)/ny;	// dot product of normal with point on plane = 0
		}

		// NORMALIZE VECTOR to unit length
		// divider should be something nice (i.e. between 0.1 and 1.0)	
		double divider = Math.sqrt(xx*xx + xy*xy + xz*xz); 
		xx = xx/divider;
		xy = xy/divider;
		xz = xz/divider;
	
		return new CSG_Vertex(xx, xy, xz);
	}
	
	
	/**
	 * @return the CSG_Vertex of the plane's origin
	 */
	public CSG_Vertex getOrigin(){
		return origin.deepCopy();
	}
	
	/**
	 * @return the CSG_Vertex representing the normal of the plane
	 */
	public CSG_Vertex getNormal(){
		return normal.deepCopy();
	}
	
	/**
	 * @return the CSG_Vertex corresponding to the 
	 * direction of the 1st variables axis (plane orientation).
	 */
	public CSG_Vertex getVar1Axis(){
		return xAxis.deepCopy();
	}
	
	public void drawPlaneForDebug(GL gl){
		// 3x5 rectangle (aligned along var 1);
		CSG_Vertex a = origin.addToVertex(xAxis.getScaledCopy( 2.5)).addToVertex(yAxis.getScaledCopy( 1.5));
		CSG_Vertex b = origin.addToVertex(xAxis.getScaledCopy(-2.5)).addToVertex(yAxis.getScaledCopy( 1.5));
		CSG_Vertex c = origin.addToVertex(xAxis.getScaledCopy(-2.5)).addToVertex(yAxis.getScaledCopy(-1.5));
		CSG_Vertex d = origin.addToVertex(xAxis.getScaledCopy( 2.5)).addToVertex(yAxis.getScaledCopy(-1.5));
		gl.glColor3f(0.5f, 1.0f, 1.0f);
		gl.glBegin(GL.GL_LINE_LOOP);
			gl.glVertex3dv(a.getXYZ(), 0);
			gl.glVertex3dv(b.getXYZ(), 0);
			gl.glVertex3dv(c.getXYZ(), 0);
			gl.glVertex3dv(d.getXYZ(), 0);
		gl.glEnd();
	}
	
	/**
	 * orient the scene to the sketch plane.  
	 * @param gl
	 */
	public void glOrientToPlane(GL gl){
		gl.glLoadIdentity();
		// align to origin
		gl.glTranslated(origin.getX(), origin.getY(), origin.getZ());
		
		// rotate to align planes
		double xRot = getRotationX();
		double yRot = getRotationY();
		gl.glRotated(xRot*180.0/Math.PI, 1.0, 0.0, 0.0);
		gl.glRotated(yRot*180.0/Math.PI, 0.0, 1.0, 0.0);
		
		// rotate around z-axis to align 2D grid
		double zRot = getRotationZ();
		gl.glRotated(zRot*180.0/Math.PI, 0.0, 0.0, 1.0);
		//System.out.println("xRot:" + xRot + " yRot:" + yRot + " zRot:" + zRot);
	}
	
	/**
	 * rotation about X axis, in radians.
	 * @return
	 */
	public double getRotationX(){
		
		double normY = normal.getY();
		double normZ = normal.getZ();
		double d = normY*normY + normZ*normZ;
		if(d > TOL){
			// flatten to 2D yz-plane and renormalize to 1.0 for easy trig math.
			d = Math.sqrt(d);
			normY = normY/d;
			normZ = normZ/d;
		}
		
		if(normY >= 0.0){
			return -( Math.PI/2.0 - Math.asin(normZ));
		}else{
			return -(-Math.PI/2.0 + Math.asin(normZ));
		}	
	}
	
	/**
	 * rotation about Y axis, in radians.
	 * @return
	 */
	public double getRotationY(){

		double rotX = getRotationX();
		Rotation3D rotation       = new Rotation3D(rotX, 0.0, 0.0);
		Translation3D translation = new Translation3D(0.0, 0.0, 0.0); 
		CSG_Vertex newZAxis    = new CSG_Vertex(0.0, 0.0, 1.0).getTranslatedRotatedCopy(translation, rotation);
		CSG_Vertex newXAxis    = new CSG_Vertex(1.0, 0.0, 0.0).getTranslatedRotatedCopy(translation, rotation);
		CSG_Vertex newNormal   = normal.deepCopy();
		
		double dotProdZ = newZAxis.getDotProduct(newNormal);
		double dotProdX = newXAxis.getDotProduct(newNormal);
		
		// find trig values, making sure to keep input to [-1.0, 1.0]
		double angleFromZ = Math.acos(Math.max(-1.0, Math.min(1.0, dotProdZ)));
		double angleFromX = Math.acos(Math.max(-1.0, Math.min(1.0, dotProdX)));
		//System.out.println("Calc Y: newNormal=" + newNormal + ", newZAxis=" + newZAxis);
		//System.out.println("Calc Y: AngleFromZ=" + angleFromZ + ", AngleFromX=" + angleFromX);
		double rotY = 0.0;
		if(angleFromZ > Math.PI/2.0){
			if(angleFromX > Math.PI/2.0){
				// Z(hi),X(hi)	
				rotY = -(Math.PI - angleFromZ);
			}else{
				// Z(hi),X(low)	
				rotY = Math.PI - angleFromZ;
			}
		}else{
			if(angleFromX > Math.PI/2.0){
				// Z(low),X(hi)	
				rotY = -angleFromZ;
			}else{
				// Z(low),X(low)
				rotY = angleFromZ;
			}
		}				
		//System.out.println("Calc Y: rotX=" + rotX + ", rotY=" + rotY);
		return rotY;
	}
	
	/**
	 * rotation about Z axis in radians.
	 */
	public double getRotationZ(){

		double rotX = getRotationX();
		double rotY = getRotationY();
		Rotation3D rotation       = new Rotation3D(rotX, rotY, 0.0);
		Translation3D translation = new Translation3D(0.0, 0.0, 0.0);
		CSG_Vertex newXAxis    = new CSG_Vertex(1.0, 0.0, 0.0).getTranslatedRotatedCopy(translation, rotation);
		CSG_Vertex newYAxis    = new CSG_Vertex(0.0, 1.0, 0.0).getTranslatedRotatedCopy(translation, rotation);
		
		double dotProdX = newXAxis.getDotProduct(xAxis);
		double dotProdY = newYAxis.getDotProduct(xAxis);
		
		// find trig values, making sure to keep input to [-1.0, 1.0]
		double angleFromX = Math.acos(Math.max(-1.0, Math.min(1.0, dotProdX)));
		double angleFromY = Math.acos(Math.max(-1.0, Math.min(1.0, dotProdY)));
		
		//System.out.println("Calc Z: AngleFromX=" + angleFromX + ", AngleFromY=" + angleFromY);
		
		double rotZ = 0.0;
		if(angleFromX > Math.PI/2.0){
			if(angleFromY > Math.PI/2.0){
				// X(hi),Y(hi)	
				rotZ = -angleFromX;
			}else{
				// X(hi),Y(low)	
				rotZ =  angleFromX;
			}
		}else{
			if(angleFromY > Math.PI/2.0){
				// X(low),Y(hi)	
				rotZ = -angleFromX;
			}else{
				// X(low),Y(low)
				rotZ = angleFromX;
			}
		}				
		//System.out.println("Calc Y: rotY=" + rotY);
		return rotZ;

	}
}
