package org.poikilos.librecsg.ui.opengl;

import java.util.Iterator;
import java.util.LinkedList;

import javax.media.opengl.GL;

import org.poikilos.librecsg.backend.adt.Point2D;
import org.poikilos.librecsg.backend.adt.Rotation3D;
import org.poikilos.librecsg.backend.adt.Translation3D;
import org.poikilos.librecsg.backend.model.CSG.CSG_BooleanOperator;
import org.poikilos.librecsg.backend.model.CSG.CSG_Face;
import org.poikilos.librecsg.backend.model.CSG.CSG_Plane;
import org.poikilos.librecsg.backend.model.CSG.CSG_Polygon;
import org.poikilos.librecsg.backend.model.CSG.CSG_Solid;
import org.poikilos.librecsg.backend.model.CSG.CSG_Vertex;
import org.poikilos.librecsg.backend.model.sketch.Prim2DCycle;
import org.poikilos.librecsg.backend.model.sketch.Prim2DLine;
import org.poikilos.librecsg.backend.model.sketch.Region2D;
import org.poikilos.librecsg.backend.model.sketch.SketchPlane;


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

/**
 * Backend tests to visualize using openGL.
 *
 */
public class GLTests {

	/**
	 * test the "Convexize polygon" routine.  This should be able
	 * to take an arbitrary region and tesselate the polygon
	 * into a set of convex polygons for rendering in openGL.
	 * @param gl
	 */
	public static void testConvexize(GL gl){
		gl.glLoadIdentity();
		gl.glTranslated(-5.0, 0.0, 0.0);
		Point2D ptA = new Point2D(0.0, 0.0);
		Point2D ptB = new Point2D(2.0, 0.0);
		Point2D ptC = new Point2D(2.0, 2.0);
		Point2D ptD = new Point2D(1.1, 1.0);
		Point2D ptE = new Point2D(2.0, 3.0);
		Point2D ptF = new Point2D(3.0, 2.0);
		Point2D ptG = new Point2D(2.5, 1.0);
		Point2D ptH = new Point2D(2.5, 0.0);
		Point2D ptI = new Point2D(4.0, 1.0);
		Point2D ptJ = new Point2D(3.0, 3.0);
		Point2D ptK = new Point2D(2.0, 4.0);
		Point2D ptL = new Point2D(0.0, 2.0);
		Point2D ptM = new Point2D(-1.0, 1.0);


		Prim2DLine l1  = new Prim2DLine(ptA, ptB);
		Prim2DLine l2  = new Prim2DLine(ptB, ptC);
		Prim2DLine l3  = new Prim2DLine(ptC, ptD);
		Prim2DLine l4  = new Prim2DLine(ptD, ptE);
		Prim2DLine l5  = new Prim2DLine(ptE, ptF);
		Prim2DLine l6  = new Prim2DLine(ptF, ptG);
		Prim2DLine l7  = new Prim2DLine(ptG, ptH);
		Prim2DLine l8  = new Prim2DLine(ptH, ptI);
		Prim2DLine l9  = new Prim2DLine(ptI, ptJ);
		Prim2DLine l10 = new Prim2DLine(ptJ, ptK);
		Prim2DLine l11 = new Prim2DLine(ptK, ptL);
		Prim2DLine l12 = new Prim2DLine(ptL, ptM);
		Prim2DLine l13 = new Prim2DLine(ptM, ptA);
		Prim2DCycle cycle = new Prim2DCycle();
		cycle.add(l1);
		cycle.add(l2);
		cycle.add(l3);
		cycle.add(l4);
		cycle.add(l5);
		cycle.add(l6);
		cycle.add(l7);
		cycle.add(l8);
		cycle.add(l9);
		cycle.add(l10);
		cycle.add(l11);
		cycle.add(l12);
		cycle.add(l13);

		Region2D region = new Region2D(cycle);
		CSG_Face face = region.getCSG_Face();

		face.drawFaceForDebug(gl);
		face.drawFaceLinesForDebug(gl);
		//System.out.println("face area: " + face.getArea());
		gl.glLoadIdentity();
	}

	/**
	 * test Constructive Solid Geometry.  Two cubes are
	 * overlapped and then tested by performing intersection,
	 * union, and subtraction operations.
	 * @param gl
	 */
	public static void testCSG(GL gl){

		System.out.println(" ------  Constructive Solid Geometry Test -------- ");

		gl.glLoadIdentity();
		gl.glLineWidth(2.0f);
		gl.glPointSize(5.0f);

		// solid 1
		CSG_Vertex v1 = new CSG_Vertex(0.0, 0.0, 0.0);
		CSG_Vertex v2 = new CSG_Vertex(1.0, 0.0, 0.0);
		CSG_Vertex v3 = new CSG_Vertex(1.0, 1.0, 0.0);
		CSG_Vertex v4 = new CSG_Vertex(0.0, 1.0, 0.0);
		CSG_Vertex v5 = new CSG_Vertex(0.0, 0.0, 1.0);
		CSG_Vertex v6 = new CSG_Vertex(1.0, 0.0, 1.0);
		CSG_Vertex v7 = new CSG_Vertex(1.0, 1.0, 1.0);
		CSG_Vertex v8 = new CSG_Vertex(0.0, 1.0, 1.0);

		CSG_Face f1 = new CSG_Face(new CSG_Polygon(v1, v2, v3, v4));
		CSG_Face f2 = new CSG_Face(new CSG_Polygon(v1, v5, v6, v2));
		CSG_Face f3 = new CSG_Face(new CSG_Polygon(v2, v6, v7, v3));
		CSG_Face f4 = new CSG_Face(new CSG_Polygon(v3, v7, v8, v4));
		CSG_Face f5 = new CSG_Face(new CSG_Polygon(v4, v8, v5, v1));
		CSG_Face f6 = new CSG_Face(new CSG_Polygon(v8, v7, v6, v5));

		CSG_Solid s1 = new CSG_Solid(f1);
		s1.addFace(f2);
		s1.addFace(f3);
		s1.addFace(f4);
		s1.addFace(f5);
		s1.addFace(f6);

		// solid 2
		CSG_Vertex v1b = new CSG_Vertex(0.75, 0.75, 0.75).addToVertex(new CSG_Vertex(0,0,-0.5));
		CSG_Vertex v2b = new CSG_Vertex(1.75, 0.75, 0.75).addToVertex(new CSG_Vertex(0,0,-0.5));
		CSG_Vertex v3b = new CSG_Vertex(1.75, 1.75, 0.75).addToVertex(new CSG_Vertex(0,0,-0.5));
		CSG_Vertex v4b = new CSG_Vertex(0.75, 1.75, 0.75).addToVertex(new CSG_Vertex(0,0,-0.5));
		CSG_Vertex v5b = new CSG_Vertex(0.75, 0.75, 1.75).addToVertex(new CSG_Vertex(0,0,-0.5));
		CSG_Vertex v6b = new CSG_Vertex(1.75, 0.75, 1.75).addToVertex(new CSG_Vertex(0,0,-0.5));
		CSG_Vertex v7b = new CSG_Vertex(1.75, 1.75, 1.75).addToVertex(new CSG_Vertex(0,0,-0.5));
		CSG_Vertex v8b = new CSG_Vertex(0.75, 1.75, 1.75).addToVertex(new CSG_Vertex(0,0,-0.5));

		CSG_Face f1b = new CSG_Face(new CSG_Polygon(v1b, v2b, v3b, v4b));
		CSG_Face f2b = new CSG_Face(new CSG_Polygon(v1b, v5b, v6b, v2b));
		CSG_Face f3b = new CSG_Face(new CSG_Polygon(v2b, v6b, v7b, v3b));
		CSG_Face f4b = new CSG_Face(new CSG_Polygon(v3b, v7b, v8b, v4b));
		CSG_Face f5b = new CSG_Face(new CSG_Polygon(v4b, v8b, v5b, v1b));
		CSG_Face f6b = new CSG_Face(new CSG_Polygon(v8b, v7b, v6b, v5b));

		CSG_Solid s2 = new CSG_Solid(f1b);
		s2.addFace(f2b);
		s2.addFace(f3b);
		s2.addFace(f4b);
		s2.addFace(f5b);
		s2.addFace(f6b);


		//
		//  2 solids to play with now!
		//
		glDrawSolid(s1, 0.4f, 0.5f, 0.8f, gl);
		glDrawSolid(s2, 0.4f, 0.5f, 0.8f, gl);


		CSG_Solid s3i = CSG_BooleanOperator.Intersection(s1, s2);
		CSG_Solid s3u = CSG_BooleanOperator.Union(s1, s2);
		CSG_Solid s3s = CSG_BooleanOperator.Subtraction(s1, s2);

		if(s3i != null){
			gl.glTranslated(2.0,0.0,0.0);
			glDrawSolid(s3i, 0.4f, 0.8f, 0.4f, gl);
		}
		if(s3u != null){
			gl.glTranslated(2.0,0.0,0.0);
			glDrawSolid(s3u, 0.4f, 0.8f, 0.4f, gl);
		}
		if(s3s != null){
			gl.glTranslated(2.0,0.0,0.0);
			glDrawSolid(s3s, 0.4f, 0.8f, 0.4f, gl);
		}


		gl.glLoadIdentity();

	}


	/**
	 * test Constructive Solid Geometry.  Two cubes are
	 * overlapped with flush faces and then tested by performing
	 * intersection, union, and subtraction operations.
	 * @param gl
	 */
	public static void testCSG_Flush(GL gl){

		System.out.println(" ------  Constructive Solid Geometry Test -------- ");

		gl.glLoadIdentity();
		gl.glLineWidth(2.0f);
		gl.glPointSize(5.0f);

		// solid 1
		CSG_Vertex v1 = new CSG_Vertex(0.0, 0.0, 0.0);
		CSG_Vertex v2 = new CSG_Vertex(1.0, 0.0, 0.0);
		CSG_Vertex v3 = new CSG_Vertex(1.0, 1.0, 0.0);
		CSG_Vertex v4 = new CSG_Vertex(0.0, 1.0, 0.0);
		CSG_Vertex v5 = new CSG_Vertex(0.0, 0.0, 1.0);
		CSG_Vertex v6 = new CSG_Vertex(1.0, 0.0, 1.0);
		CSG_Vertex v7 = new CSG_Vertex(1.0, 1.0, 1.0);
		CSG_Vertex v8 = new CSG_Vertex(0.0, 1.0, 1.0);

		CSG_Face f1 = new CSG_Face(new CSG_Polygon(v1, v2, v3, v4));
		CSG_Face f2 = new CSG_Face(new CSG_Polygon(v1, v5, v6, v2));
		CSG_Face f3 = new CSG_Face(new CSG_Polygon(v2, v6, v7, v3));
		CSG_Face f4 = new CSG_Face(new CSG_Polygon(v3, v7, v8, v4));
		CSG_Face f5 = new CSG_Face(new CSG_Polygon(v4, v8, v5, v1));
		CSG_Face f6 = new CSG_Face(new CSG_Polygon(v8, v7, v6, v5));

		CSG_Solid s1 = new CSG_Solid(f1);
		s1.addFace(f2);
		s1.addFace(f3);
		s1.addFace(f4);
		s1.addFace(f5);
		s1.addFace(f6);

		// solid 2
		CSG_Vertex v1b = new CSG_Vertex(0.75, 0.75, 0.75).addToVertex(new CSG_Vertex(0,0,-0.75));
		CSG_Vertex v2b = new CSG_Vertex(1.75, 0.75, 0.75).addToVertex(new CSG_Vertex(0,0,-0.75));
		CSG_Vertex v3b = new CSG_Vertex(1.75, 1.75, 0.75).addToVertex(new CSG_Vertex(0,0,-0.75));
		CSG_Vertex v4b = new CSG_Vertex(0.75, 1.75, 0.75).addToVertex(new CSG_Vertex(0,0,-0.75));
		CSG_Vertex v5b = new CSG_Vertex(0.75, 0.75, 1.75).addToVertex(new CSG_Vertex(0,0,-0.75));
		CSG_Vertex v6b = new CSG_Vertex(1.75, 0.75, 1.75).addToVertex(new CSG_Vertex(0,0,-0.75));
		CSG_Vertex v7b = new CSG_Vertex(1.75, 1.75, 1.75).addToVertex(new CSG_Vertex(0,0,-0.75));
		CSG_Vertex v8b = new CSG_Vertex(0.75, 1.75, 1.75).addToVertex(new CSG_Vertex(0,0,-0.75));

		CSG_Face f1b = new CSG_Face(new CSG_Polygon(v1b, v2b, v3b, v4b));
		CSG_Face f2b = new CSG_Face(new CSG_Polygon(v1b, v5b, v6b, v2b));
		CSG_Face f3b = new CSG_Face(new CSG_Polygon(v2b, v6b, v7b, v3b));
		CSG_Face f4b = new CSG_Face(new CSG_Polygon(v3b, v7b, v8b, v4b));
		CSG_Face f5b = new CSG_Face(new CSG_Polygon(v4b, v8b, v5b, v1b));
		CSG_Face f6b = new CSG_Face(new CSG_Polygon(v8b, v7b, v6b, v5b));

		CSG_Solid s2 = new CSG_Solid(f1b);
		s2.addFace(f2b);
		s2.addFace(f3b);
		s2.addFace(f4b);
		s2.addFace(f5b);
		s2.addFace(f6b);

		//
		//  2 solids to play with now!
		//
		s1.applyTranslationRotation(new Translation3D(0.0, -3.0, 0.0), null);
		s2.applyTranslationRotation(new Translation3D(0.0, -3.0, 0.0), null);

		glDrawSolid(s1, 0.4f, 0.5f, 0.8f, gl);
		glDrawSolid(s2, 0.4f, 0.5f, 0.8f, gl);


		CSG_Solid s3i = CSG_BooleanOperator.Intersection(s1, s2);
		CSG_Solid s3u = CSG_BooleanOperator.Union(s1, s2);
		CSG_Solid s3s = CSG_BooleanOperator.Subtraction(s1, s2);

		if(s3i != null){
			gl.glTranslated(2.0,0.0,0.0);
			glDrawSolid(s3i, 0.4f, 0.8f, 0.4f, gl);
		}
		if(s3u != null){
			gl.glTranslated(2.0,0.0,0.0);
			glDrawSolid(s3u, 0.4f, 0.8f, 0.4f, gl);
		}
		if(s3s != null){
			gl.glTranslated(2.0,0.0,0.0);
			glDrawSolid(s3s, 0.4f, 0.8f, 0.4f, gl);
		}

		gl.glLoadIdentity();
	}


	/**
	 * draw solid with coloring and normals for debugging.
	 * @param s
	 * @param r
	 * @param g
	 * @param b
	 * @param gl
	 */
	private static void glDrawSolid(CSG_Solid s, float r, float g, float b, GL gl){
		Iterator<CSG_Face> iter = s.getFacesIter();
		while(iter.hasNext()){
			CSG_Face f = iter.next();
			f.drawFaceForDebug(gl);
			f.drawFaceLinesForDebug(gl);
			f.drawFaceNormalsForDebug(gl);
		}
	}

	/**
	 * test the sketch plane rotation and CSG_Vertex rotation code.
	 * If things are working correctly, every line should end at
	 * a point of the same color. (purple lines -> purple points;
	 * orange lines -> orange/green points).S
	 * @param gl
	 */
	public static void testRotation(GL gl){

		System.out.println(" ------  GL Rotation Test -------- ");

		gl.glLoadIdentity();
		gl.glLineWidth(2.0f);
		gl.glPointSize(5.0f);

		CSG_Vertex v1 = new CSG_Vertex(1.0, 1.0, 0.0);
		CSG_Vertex v2 = new CSG_Vertex(2.0, 1.0, 0.0);
		CSG_Vertex v3 = new CSG_Vertex(2.0, 2.0, 0.0);
		CSG_Vertex v4 = new CSG_Vertex(1.0, 2.0, 0.0);

		CSG_Vertex vX = new CSG_Vertex(1.0, 0.0, 0.0);
		CSG_Vertex vY = new CSG_Vertex(0.0, 1.0, 0.0);
		CSG_Vertex vZ = new CSG_Vertex(0.0, 0.0, 1.0);

		vX.drawPointForDebug(gl);
		vY.drawPointForDebug(gl);
		vZ.drawPointForDebug(gl);

		for(double yInc= -0.25; yInc<0.26; yInc+= 0.1){
			for(double rot=-3.10; rot<3.14; rot+= 0.1){

				CSG_Vertex normal = new CSG_Vertex(Math.sin(rot), yInc, Math.cos(rot)).getUnitLength();
				//CSG_Vertex normal = new CSG_Vertex(1.0, 0.0, 0.0);
				CSG_Plane plane = new CSG_Plane(normal, 0.0);
				SketchPlane sP  = new SketchPlane(plane);

				//System.out.println("--> normal: " + normal);
				Translation3D translation = new Translation3D(0.0, 0.0, 0.0);
				Rotation3D rotation    = new Rotation3D(sP.getRotationX(), sP.getRotationY(), sP.getRotationZ());
				CSG_Face f1 = new CSG_Face(new CSG_Polygon(v1, v2, v3, v4));
				CSG_Face f2 = f1.getTranslatedCopy(new CSG_Vertex(-3.0,  0.0, 0.0));
				CSG_Face f3 = f1.getTranslatedCopy(new CSG_Vertex(-3.0, -3.0, 0.0));
				CSG_Face f4 = f1.getTranslatedCopy(new CSG_Vertex( 0.0, -3.0, 0.0));
				gl.glColor3d(rot, 0.5, 0.0);
				gl.glPointSize(7.0f);
				//vX.getTranslatedRotatedCopy(translation, rotation).glDrawVertex(gl);
				//vY.getTranslatedRotatedCopy(translation, rotation).glDrawVertex(gl);
				vZ.getTranslatedRotatedCopy(translation, rotation).glDrawVertex(gl);
				f1.applyTranslationRotation(translation, rotation);
				f2.applyTranslationRotation(translation, rotation);
				f3.applyTranslationRotation(translation, rotation);
				f4.applyTranslationRotation(translation, rotation);
				//f1.glDrawFace(gl); //
				//f2.glDrawFace(gl); // uncomment these faces to see how
				//f3.glDrawFace(gl); // planar regions are rotated.
				//f4.glDrawFace(gl); //

				gl.glColor3d(0.8, 0.0, 0.8);
				sP.getVar1Axis().glDrawVertex(gl);

				plane.drawNormalFromOriginForDegug(gl);
				gl.glRotatef((float)(rotation.getXRot()*180.0/Math.PI), 1.0f, 0.0f, 0.0f);
			    gl.glRotatef((float)(rotation.getYRot()*180.0/Math.PI), 0.0f, 1.0f, 0.0f);
			    gl.glRotatef((float)(rotation.getZRot()*180.0/Math.PI), 0.0f, 0.0f, 1.0f);
			    gl.glColor3d(0.9, 0.7, 0.3);
			    gl.glPointSize(5.0f);
			    //vZ.glDrawVertex(gl);
			    gl.glBegin(GL.GL_LINES);
			    	gl.glColor3d(0.9, 0.7, 0.3); // normal
			    	gl.glVertex3d(0.0, 0.0, 0.0);
			    	gl.glVertex3d(vZ.getX(), vZ.getY(), vZ.getZ());
			    	gl.glColor3d(0.9, 0.3, 0.9); // x-axis
			    	gl.glVertex3d(0.0, 0.0, 0.0);
			    	gl.glVertex3d(1.0, 0.0, 0.0);
			    gl.glEnd();
			    gl.glLoadIdentity();
			    gl.glColor3d(0.7, 0.7, 0.7);
			}
		}
	}


	/**
	 * test the perimeter formation routine.
	 * This should be able to take any CSG_Face composed of
	 * many convex polygons and return just the points
	 * along hte face's perimeter for edge drawing
	 * purposes in open GL.
	 * @param gl
	 */
	public static void testPerimeterFormation(GL gl){
		gl.glLoadIdentity();
		gl.glTranslated(-5.0, 0.0, 0.0);
		Point2D ptA = new Point2D(0.0, 0.0);
		Point2D ptB = new Point2D(2.0, 0.0);
		Point2D ptC = new Point2D(2.0, 2.0);
		Point2D ptD = new Point2D(1.1, 1.0);
		Point2D ptE = new Point2D(2.0, 3.0);
		Point2D ptF = new Point2D(3.0, 2.0);
		Point2D ptG = new Point2D(2.5, 1.0);
		Point2D ptH = new Point2D(2.5, 0.0);
		Point2D ptI = new Point2D(4.0, 1.0);
		Point2D ptJ = new Point2D(3.0, 3.0);
		Point2D ptK = new Point2D(2.0, 4.0);
		Point2D ptL = new Point2D(0.0, 2.0);
		Point2D ptM = new Point2D(-1.0, 1.0);


		Prim2DLine l1  = new Prim2DLine(ptA, ptB);
		Prim2DLine l2  = new Prim2DLine(ptB, ptC);
		Prim2DLine l3  = new Prim2DLine(ptC, ptD);
		Prim2DLine l4  = new Prim2DLine(ptD, ptE);
		Prim2DLine l5  = new Prim2DLine(ptE, ptF);
		Prim2DLine l6  = new Prim2DLine(ptF, ptG);
		Prim2DLine l7  = new Prim2DLine(ptG, ptH);
		Prim2DLine l8  = new Prim2DLine(ptH, ptI);
		Prim2DLine l9  = new Prim2DLine(ptI, ptJ);
		Prim2DLine l10 = new Prim2DLine(ptJ, ptK);
		Prim2DLine l11 = new Prim2DLine(ptK, ptL);
		Prim2DLine l12 = new Prim2DLine(ptL, ptM);
		Prim2DLine l13 = new Prim2DLine(ptM, ptA);
		Prim2DCycle cycle = new Prim2DCycle();
		cycle.add(l1);
		cycle.add(l2);
		cycle.add(l3);
		cycle.add(l4);
		cycle.add(l5);
		cycle.add(l6);
		cycle.add(l7);
		cycle.add(l8);
		cycle.add(l9);
		cycle.add(l10);
		cycle.add(l11);
		cycle.add(l12);
		cycle.add(l13);

		Region2D region = new Region2D(cycle);
		CSG_Face face = region.getCSG_Face();

		face.drawFaceForDebug(gl);
		face.drawFaceLinesForDebug(gl);

		gl.glTranslated(5.0, 0.0, 0.0);
		// now find the perimeter and draw that...
		gl.glLineWidth(3.0f);
		gl.glPointSize(5.0f);
		gl.glColor3d(0.9, 0.7, 0.3);
		LinkedList<CSG_Vertex> perim = face.getPerimeterVertices();
		for(int i=0; i<perim.size(); i++){
			CSG_Vertex vA = perim.get(i);
			CSG_Vertex vB = perim.get((i+1)%perim.size());
			gl.glBegin(GL.GL_LINE_LOOP);
				gl.glVertex3d(vA.getX(), vA.getY(), vA.getZ());
				gl.glVertex3d(vB.getX(), vB.getY(), vB.getZ());
			gl.glEnd();
		}

		//System.out.println("face area: " + face.getArea());
		gl.glLoadIdentity();
	}

}
