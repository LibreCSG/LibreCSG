package backend.model.CSG;

import javax.media.opengl.GL;


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

/**
* Constructive Solid Geometry :: Ray<br/><br/>
* 
* A line originating from a base point in a given direction.
* <br/><br/> 
* 
* Algorithms and structures from:<br/>
* - Laidlaw, Trumbore, and Hughes <br/>
* - "Constructive Solid Geometry for Polyhedral Objects"<br/>
* - SIGGRAPH 1986, Volume 20, Number 4, pp.161-170
*/
public class CSG_Ray {

	CSG_Vertex basePoint;
	CSG_Vertex direction;
	
	final double TOL = 1e-10;
	
	/**
	 * construct a new CSG_Ray at the intersection of 
	 * the planes derived from two CSG_Faces.  If the 
	 * planes are parallel (i.e. no intersection) then
	 * both the basePoint and the direction will be
	 * set to (0,0,0);
	 * @param faceA the 1st CSG_Face
	 * @param faceB the 2nd CSG_Face
	 */
	public CSG_Ray(CSG_Face faceA, CSG_Face faceB){
		direction = faceA.getPlaneNormal().getVectCrossProduct(faceB.getPlaneNormal());
		if(direction.getDistFromOrigin() < TOL){
			System.out.println("planes are parallel!  They will not intersect at a line. ");
			basePoint = new CSG_Vertex(0.0, 0.0, 0.0); // dummy base Point
		}else{
			// need an arbitrarily chosen basePoint that sits 
			// on the Line where the planes intersect.
			// -- common solution; just find a part of the
			//    direction vector that is non-zero and then
			//    move accordingly.
			// 
			double dA = faceA.getPlaneOffset();
			double dB = faceB.getPlaneOffset();
			double x = 0.0;
			double y = 0.0;
			double z = 0.0;
			if(Math.abs(direction.getX()) > TOL){
				// x was equivalently zero
				x = 0.0;
				y = (dB*faceA.getPlaneNormal().getZ() - dA*faceB.getPlaneNormal().getZ())/direction.getX();
				z = (dA*faceB.getPlaneNormal().getY() - dB*faceA.getPlaneNormal().getY())/direction.getX();
			}else{
				if(Math.abs(direction.getY()) > TOL){
					// y was equivalently zero
					x = (dA*faceB.getPlaneNormal().getZ() - dB*faceA.getPlaneNormal().getZ())/direction.getY();
					y = 0.0;
					z = (dB*faceA.getPlaneNormal().getX() - dA*faceB.getPlaneNormal().getX())/direction.getY();
				}else{
					if(Math.abs(direction.getZ()) > TOL){
						// z was equivalently zero
						x = (dB*faceA.getPlaneNormal().getY() - dA*faceB.getPlaneNormal().getY())/direction.getZ();
						y = (dA*faceB.getPlaneNormal().getX() - dB*faceA.getPlaneNormal().getX())/direction.getZ();
						z = 0.0;
					}		
				}
			}
			basePoint = new CSG_Vertex(x,y,z);
		}
	}
	
	/**
	 * construct a new CSG_Ray specified by a basePoint and a direction.
	 * @param basePoint the basePoint
	 * @param direction the direction
	 */
	public CSG_Ray(CSG_Vertex basePoint, CSG_Vertex direction){
		this.basePoint = basePoint;
		this.direction = direction;
	}
	
	public String toString(){
		return "CSG_Ray: base=" + basePoint + " direction=" + direction;
	}
	
	/**
	 * just for debug purposes... 
	 * a simple way to visualize the ray in 3D space. 
	 * @param gl the GL context on which to render
	 * @param length the total length of the line
	 */
	public void drawLineForDebug(GL gl, double length){
		CSG_Vertex begin = basePoint;
		CSG_Vertex end   = basePoint.addToVertex(direction.getScaledCopy(length));
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3d(begin.getX(), begin.getY(), begin.getZ());
			gl.glVertex3d(end.getX(), end.getY(), end.getZ());			
		gl.glEnd();
		gl.glBegin(GL.GL_POINTS);
			gl.glVertex3d(basePoint.getX(), basePoint.getY(), basePoint.getZ());
		gl.glEnd();
	}
	
}
