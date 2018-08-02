package org.poikilos.librecsg.ui.opengl;

import com.jogamp.opengl.GL2;
import org.poikilos.librecsg.backend.adt.Point2D;
import org.poikilos.librecsg.backend.adt.Point3D;
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
public class GLDynPrim {


	public static void line3D(GL2 gl, Point3D ptA, Point3D ptB){
		gl.glBegin(GL2.GL_LINES);
			gl.glVertex3d(ptA.getX(), ptA.getY(), ptA.getZ());
			gl.glVertex3d(ptB.getX(), ptB.getY(), ptB.getZ());
		gl.glEnd();
	}

	public static void line2D(GL2 gl, Point2D ptA, Point2D ptB, double zOffset){
		gl.glBegin(GL2.GL_LINES);
			gl.glVertex3d(ptA.getX(), ptA.getY(), zOffset);
			gl.glVertex3d(ptB.getX(), ptB.getY(), zOffset);
		gl.glEnd();
	}

	private static void circle2D(GL2 gl, Point2D center, double radius, double zOffset){
		gl.glPushMatrix();
			gl.glTranslatef((float)center.getX(), (float)center.getY(), (float)zOffset);
			float w = (float)Math.cos(Math.PI / 4);
			cad_NURBS(gl, 	radius,0.0,0.0,1.0,
						radius,radius,0.0,w,
						0.0,radius,0.0,1.0);
			cad_NURBS(gl,	-radius,0.0,0.0,1.0,
						-radius,radius,0.0,w,
						0.0,radius,0.0,1.0);
			cad_NURBS(gl,	-radius,0.0,0.0,1.0,
						-radius,-radius,0.0,w,
						0.0,-radius,0.0,1.0);
			cad_NURBS(gl,	radius,0.0,0.0,1.0,
						radius,-radius,0.0,w,
						0.0,-radius,0.0,1.0);
		gl.glPopMatrix();
	}


	public static void arc2D(GL2 gl, Point2D center, double radius, double startAngle, double arcAngle, double zOffset){
		if(arcAngle == 360.0){
			circle2D(gl, center, radius, zOffset);
		}else{
			gl.glPushMatrix();
			gl.glTranslatef((float)center.getX(), (float)center.getY(), (float)zOffset);
			gl.glRotated(startAngle, 0.0, 0.0, 1.0);
			arcAngle = arcAngle % 360.0; // kill redundant drawing
			while(arcAngle > 0.0 || arcAngle < 0.0){
				if(arcAngle >= 90.0 || arcAngle <= -90.0){
					double sign = Math.signum(arcAngle);
					double w    = Math.cos(Math.PI / 4);
					cad_NURBS(gl,	radius,0.0f,0.0f,1.0f,
									radius,sign*radius,0.0f,w,
									0.0f,sign*radius,0.0f,1.0f);
					arcAngle -= sign*90.0;
					gl.glRotated(sign*90.0, 0.0, 0.0, 1.0);
				}else{
					double arcRadians = arcAngle / 360.0 *  2 * Math.PI;
					double finalx     = Math.cos(arcRadians)*radius;
					double finaly     = Math.sin(arcRadians)*radius;

					double midy = Math.tan(arcRadians/2)*radius;
					double w    = Math.cos(arcRadians/2);
					cad_NURBS(gl,	radius,0.0f,0.0f,1.0f,
									radius,midy,0.0f,w,
									finalx,finaly,0.0f,1.0f);
					arcAngle = 0.0;
				}
			}
			gl.glPopMatrix();
		}
	}


	public static void cad_NURBS(GL2 gl,	double x1, double y1, double z1, double w1,
										double x2, double y2, double z2, double w2,
										double x3, double y3, double z3, double w3){

		//
		// Dynamically choose how many segments to use for
		// approximation of curve.
		//
		int segments;
		switch(AvoGlobal.renderLevel){
			case PhotoReal :{
				segments = 100;
				break;
			}
			case High :{
				segments = 25;
				break;
			}
			case Medium :{
				segments = 10;
				break;
			}
			case Low :{
				segments = 5;
				break;
			}
			case Fastest :{
				segments = 3;
				break;
			}
			default:{
				segments = 3;
				break;
			}
		}
		double [] ctrlPoints = {x1*w1,y1*w1,z1*w1,w1,
								x2*w2,y2*w2,z2*w2,w2,
								x3*w3,y3*w3,z3*w3,w3 };
		gl.glEnable(GL2.GL_MAP1_VERTEX_4);
		gl.glEnable(GL2.GL_AUTO_NORMAL);
		gl.glMap1d(GL2.GL_MAP1_VERTEX_4, 0.0, 1.0, 4, 3, ctrlPoints, 0);
		gl.glMapGrid1d(segments, 0.0, 1.0);
		gl.glEvalMesh1(GL2.GL_LINE, 0, segments);

	}

	public static void mesh(GL2 gl, double xstart, double xend, double ystart, double yend, int xsteps, int ysteps){
		double grid[] =  new double[] {xstart, ystart, 0.0, xstart, yend, 0.0, xend, ystart, 0.0, xend, yend, 0.0};

		gl.glEnable(GL2.GL_MAP2_VERTEX_3);
		gl.glLineWidth(1.0f);
		gl.glMap2d(GL2.GL_MAP2_VERTEX_3,
				0.0, 1.0,  /* U ranges 0..1 */
				3,         /* U stride, 3 floats per coord */
				2,         /* U is 2nd order, ie. linear */
				0.0, 1.0,  /* V ranges 0..1 */
				2 * 3,     /* V stride, row is 2 coords, 3 floats per coord */
				2,         /* V is 2nd order, ie linear */
				grid,0);  /* control points */

		gl.glMapGrid2d(
			    ysteps, 0.0, 1.0,
			    xsteps, 0.0, 1.0);

		gl.glEvalMesh2(GL2.GL_LINE, 0, ysteps, 0, xsteps);
	}

	public static void point(GL2 gl, double x, double y, double z, double size){
		gl.glPointSize((float)size);
		gl.glBegin(GL2.GL_POINTS);
			gl.glVertex3d(x, y, z);
		gl.glEnd();
	}

}
