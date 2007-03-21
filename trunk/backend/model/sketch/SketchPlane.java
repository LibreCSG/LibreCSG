package backend.model.sketch;

import javax.media.opengl.GL;

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
	private CSG_Vertex var1Axis;
	private CSG_Vertex var2Axis;
	
	public SketchPlane(CSG_Vertex origin, CSG_Vertex normal, CSG_Vertex var1Axis){
		this.origin   = origin;
		this.normal   = normal.getUnitLength();
		this.var1Axis = var1Axis.getUnitLength();
		this.var2Axis = this.normal.getVectCrossProduct(this.var1Axis);
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
		return var1Axis.deepCopy();
	}
	
	public void drawPlanForDebug(GL gl){
		// 3x5 rectangle (aligned along var 1);
		CSG_Vertex a = origin.addToVertex(var1Axis.getScaledCopy( 2.5)).addToVertex(var2Axis.getScaledCopy( 1.5));
		CSG_Vertex b = origin.addToVertex(var1Axis.getScaledCopy(-2.5)).addToVertex(var2Axis.getScaledCopy( 1.5));
		CSG_Vertex c = origin.addToVertex(var1Axis.getScaledCopy(-2.5)).addToVertex(var2Axis.getScaledCopy(-1.5));
		CSG_Vertex d = origin.addToVertex(var1Axis.getScaledCopy( 2.5)).addToVertex(var2Axis.getScaledCopy(-1.5));
		gl.glColor3f(0.5f, 1.0f, 1.0f);
		gl.glBegin(GL.GL_LINE_LOOP);
			gl.glVertex3dv(a.getXYZ(), 0);
			gl.glVertex3dv(b.getXYZ(), 0);
			gl.glVertex3dv(c.getXYZ(), 0);
			gl.glVertex3dv(d.getXYZ(), 0);
		gl.glEnd();
	}
	
}
