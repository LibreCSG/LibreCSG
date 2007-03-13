package backend.model.CSG;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;


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
 * Constructive Solid Geometry :: Boolean Operators<br/><br/>
 * 
 * Algorithms and structures from:<br/>
 * - Laidlaw, Trumbore, and Hughes <br/>
 * - "Constructive Solid Geometry for Polyhedral Objects"<br/>
 * - SIGGRAPH 1986, Volume 20, Number 4, pp.161-170
 * 
 */
public class CSG_BooleanOperator {

	/**
	 * find the intersection of solidA and solidB and return it.
	 * @param solidA the "A" 3D CSG_Solid
	 * @param solidB the "B" 3D CSG_Solid
	 * @return the new CSG_Solid representing (A intersect B) or NULL if no intersection.
	 */
	public static CSG_Solid Intersection(CSG_Solid solidA, CSG_Solid solidB){
		// TODO: CSG Intersection
		splitSolidABySolidB(solidA, solidB);
		return null;
	}
	
	/**
	 * find the union of solidA and solidB and return it.
	 * @param solidA the "A" 3D CSG_Solid
	 * @param solidB the "B" 3D CSG_Solid
	 * @return the new CSG_Solid representing (A union B) or NULL if no union.
	 */
	public static CSG_Solid Union(CSG_Solid solidA, CSG_Solid solidB){
		// TODO: CSG Union
		return null;
	}
	
	/**
	 * find the difference of solidA and solidB and return it.
	 * @param solidA the "A" 3D CSG_Solid
	 * @param solidB the "B" 3D CSG_Solid
	 * @return the new CSG_Solid representing (A - B) or NULL if result is no solid.
	 */
	public static CSG_Solid Subtraction(CSG_Solid solidA, CSG_Solid solidB){
		// TODO: CSG Subtraction
		return null;
	}
	
	
	private static void splitSolidABySolidB(CSG_Solid sA, CSG_Solid sB){
		// 4.1 :: Line 1
		if(sA.bounds.overlapsBounds(sB.bounds)){
			// 4.1 :: Line 2
			Iterator<CSG_Face> aFaceIter =  sA.getFacesIter();
			while(aFaceIter.hasNext()){
				CSG_Face aFace = aFaceIter.next();
				// 4.1 :: Line 3
				if(aFace.getBounds().overlapsBounds(sB.bounds)){
					// 4.1 :: Line 4
					Iterator<CSG_Face> bFaceIter = sB.getFacesIter();
					while(bFaceIter.hasNext()){
						CSG_Face bFace = bFaceIter.next();
						// TODO: analyze aFace,bFace as in "5. Do Polygons Intersect?"
						CSG_FACE_INFO fInfo = performFaceIntersection(aFace, bFace);
						
					}					
				}				
			}
		}
	}
	
	
	public static CSG_FACE_INFO performFaceIntersection(CSG_Face faceA, CSG_Face faceB){
		// "5. Do Polygons Intersect?"
		// Step 1: get dist from each vertex in faceA to plane of faceB
		boolean gotPositive = false;
		boolean gotNegative = false;
		Iterator<CSG_Vertex> aVerts = faceA.getVertexIterator();
		List<Double> distAsToBPlane = new LinkedList<Double>();
		while(aVerts.hasNext()){
			double dist = faceB.distFromVertexToFacePlane(aVerts.next());
			distAsToBPlane.add(dist);
			if(dist > 0.0){
				gotPositive = true;
			}
			if(dist < 0.0){
				gotNegative = true;
			}
		}
		if(!gotPositive && !gotNegative){
			// all distances from faceA vertices to faceB were zero!
			return CSG_FACE_INFO.FACE_COPLANAR;
		}
		if(gotPositive && !gotNegative){
			// faceA is completely on one side of faceB
			return CSG_FACE_INFO.FACE_NOT_INTERSECT;
		}
		if(!gotPositive && gotNegative){
			// faceA is completely on the other side of faceB
			return CSG_FACE_INFO.FACE_NOT_INTERSECT;
		}		
		
		// Step 2: not quite sure yet, so need to check the other way...
		// get dist from each vertex in faceB to plane of faceA		
		gotPositive = false;
		gotNegative = false;
		Iterator<CSG_Vertex> bVerts = faceB.getVertexIterator();
		List<Double> distBsToAPlane = new LinkedList<Double>();
		while(bVerts.hasNext()){
			double dist = faceA.distFromVertexToFacePlane(bVerts.next());
			distBsToAPlane.add(dist);
			if(dist > 0.0){
				gotPositive = true;
			}
			if(dist < 0.0){
				gotNegative = true;
			}			
		}		
		if(gotPositive && !gotNegative){
			// faceB is completely on one side of faceA
			return CSG_FACE_INFO.FACE_NOT_INTERSECT;
		}
		if(!gotPositive && gotNegative){
			// faceB is completely on the other side of faceA
			return CSG_FACE_INFO.FACE_NOT_INTERSECT;
		}
		
		//
		// now things get a bit more tricky.. need to do line intersection..
		//
		
		CSG_Ray planeIntRay = new CSG_Ray(faceA, faceB);

		// TODO

		// find CSG_Segment in faceA
		CSG_Segment segmentA = new CSG_Segment(faceA, distAsToBPlane, planeIntRay);
		
		// find CSG_Segment in faceB
		CSG_Segment segmentB = new CSG_Segment(faceB, distBsToAPlane, planeIntRay);
		
		GLContext glc = GLContext.getCurrent();
		GL gl = glc.getGL();
		segmentA.drawSegmentForDebug(gl);
		segmentB.drawSegmentForDebug(gl);
		
		// check to see if segments overlap at all...
		double aMax = segmentA.getMaxRayDist();
		double aMin = segmentA.getMinRayDist();
		double bMax = segmentB.getMaxRayDist();
		double bMin = segmentB.getMinRayDist();		
		if((aMin >= bMin && aMin <= bMax) || (aMax >= bMin && aMax <= bMax)){
			//return CSG_FACE_INFO.FACE_INTERSECT;
			// perform Face Intersection!!  the segments overlap!
			System.out.println("should perform face intersection!");
			return CSG_FACE_INFO.FACE_INTERSECT;
		}
		return CSG_FACE_INFO.FACE_NOT_INTERSECT;
	}
	
	enum CSG_FACE_INFO {
		FACE_COPLANAR, FACE_INTERSECT, FACE_NOT_INTERSECT, FACE_UNKNOWN
	}
	
}
