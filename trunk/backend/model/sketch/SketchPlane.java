package backend.model.sketch;

import javax.media.opengl.GL;

import backend.model.CSG.CSG_Plane;
import backend.model.CSG.CSG_Ray;
import backend.model.CSG.CSG_Vertex;


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
* @created Mar. 2007
*/
public class SketchPlane {

	private CSG_Vertex origin;
	private CSG_Vertex normal;
	private CSG_Vertex xAxis;
	private CSG_Vertex yAxis;
	
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
		this.yAxis = this.normal.getVectCrossProduct(this.xAxis).getUnitLength();
		System.out.println("sketch plane constructed with origin: " + origin + ", and xAxis: " + xAxis);
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
		return -Math.asin(normal.getY());		
	}
	
	/**
	 * rotation about Y axis, in radians.
	 * @return
	 */
	public double getRotationY(){
		return -Math.asin(normal.getX());		
	}
	
	/**
	 * rotation about Z axis in radians.
	 */
	public double getRotationZ(){
		double dotX = xAxis.getDotProduct(new CSG_Vertex(1.0, 0.0, 0.0));
		double dotY = xAxis.getDotProduct(new CSG_Vertex(0.0, 1.0, 0.0));
		double zRot = 0.0;
		if(dotX > dotY){
			// use dotX
			zRot = Math.acos(dotX);
		}else{
			// use dotY
			zRot = Math.acos(dotY)-Math.PI/2.0;
		}
		return zRot;
	}
}
