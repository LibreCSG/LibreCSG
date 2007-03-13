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

	final static double TOL = 1e-10;
	
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
		//
		// Pseudo-code.. very similar to Fig. 4.1 in 1986 SIGGRAPH: CSG
		//               but modified to handle faces (each of which is 
		//               made of possibly multiple polygons)
		//
		// ___Spitting SolidA by SolidB___
		//
		//( 1) if(extent of solidA overlaps solidB)
		//( 2)   for each faceA in solidA
		//( 3)     if(extent of faceA overlaps solidB)
		//( 4)       for each polygonA in faceA
		//( 5)         if(extent of polygonA overlaps solidB)
		//( 6)           for each faceB in solidB
		//( 7)             if(extent of polygonA overlaps faceB)
		//( 8)              for each polygonB in faceB
		//( 9)                 if(extent of polygonA overlaps polygonB)
		//(10)                   ** analize them as in "5. Do Two Polygons Intersect"
		//(11)                   if(INTERSECT)
		//(12)                     ** subdivide polygonA as in "6. Subdividing non-coplanar polygons"
		//(13)                   else
		//(14)                     ** do nothing with polygonA (it was COPLANAR or NOT_INTERSECT)
		//
		
		
		// ( 1) if(extent of solidA overlaps solidB)
		if(sA.bounds.overlapsBounds(sB.bounds)){
			// ( 2) for each faceA in solidA
			Iterator<CSG_Face> faceIterA =  sA.getFacesIter();
			while(faceIterA.hasNext()){
				CSG_Face faceA = faceIterA.next();
				// ( 3) if(extent of faceA overlaps solidB)
				if(faceA.getBounds().overlapsBounds(sB.bounds)){
					// ( 4) for each polygonA in faceA
					Iterator<CSG_Polygon> polyIterA = faceA.getPolygonIterator();
					while(polyIterA.hasNext()){
						CSG_Polygon polyA = polyIterA.next();
						// ( 5) if(extent of polygonA overlaps solidB)
						if(polyA.getBounds().overlapsBounds(sB.bounds)){
							// ( 6) for each faceB in solidB
							Iterator<CSG_Face> faceIterB = sB.getFacesIter();
							while(faceIterB.hasNext()){
								CSG_Face faceB = faceIterB.next();
								// ( 7) if(extent of polygonA overlaps faceB)
								if(polyA.getBounds().overlapsBounds(faceB.getBounds())){
									// ( 8) for each polygonB in faceB
									Iterator<CSG_Polygon> polyIterB = faceB.getPolygonIterator();
									while(polyIterB.hasNext()){
										CSG_Polygon polyB = polyIterB.next();
										// ( 9) if(extent of polygonA overlaps polygonB)
										if(polyA.getBounds().overlapsBounds(polyB.getBounds())){
											// (10) ** analize them as in "5. Do Two Polygons Intersect"
											CSG_FACE_INFO fInfo = performPolyIntersection(polyA, faceA, polyB, faceB);
										}
									}									
								}								
							}							
						}						
					}			
				}				
			}
		}
	}
	
	
	public static CSG_FACE_INFO performPolyIntersection(CSG_Polygon polyA, CSG_Face faceA, CSG_Polygon polyB, CSG_Face faceB){
		// "5. Do Polygons Intersect?"
		// Step 1: get dist from each vertex in faceA to plane of faceB
		boolean gotPositive = false;
		boolean gotNegative = false;
		Iterator<CSG_Vertex> aVerts = polyA.getVertexIterator();
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
		Iterator<CSG_Vertex> bVerts = polyB.getVertexIterator();
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

		// find CSG_Segment in faceA
		CSG_Segment segmentA = new CSG_Segment(polyA, distAsToBPlane, planeIntRay);
		
		// find CSG_Segment in faceB
		CSG_Segment segmentB = new CSG_Segment(polyB, distBsToAPlane, planeIntRay);
		

		
		// check to see if segments overlap at all...
		double aMax = segmentA.getEndRayDist();
		double aMin = segmentA.getStartRayDist();
		double bMax = segmentB.getEndRayDist();
		double bMin = segmentB.getStartRayDist();		
		if((aMin >= bMin && aMin <= bMax) || (aMax >= bMin && aMax <= bMax)){
			//return CSG_FACE_INFO.FACE_INTERSECT;
			// perform Face Intersection!!  the segments overlap!
			// TODO
			//System.out.println("should perform face intersection!");
			subdivideFaceA(polyA, faceA, segmentA, segmentB);			
			return CSG_FACE_INFO.FACE_INTERSECT;
		}
		return CSG_FACE_INFO.FACE_NOT_INTERSECT;
	}
	
	private static void subdivideFaceA(CSG_Polygon polyA, CSG_Face faceA, CSG_Segment segmentA, CSG_Segment segmentB){
		// section 6.  find section of segmentB that overlaps segmentA...
		
		// save original values for segmentA to save work later.
		double origStartA = segmentA.getStartRayDist();
		double origEndA   = segmentA.getEndRayDist();
		CSG_Segment.VERTEX_DESC origStartDescA = segmentA.getStartDesc();
		CSG_Segment.VERTEX_DESC origEndDescA = segmentA.getEndDesc();
		
		double startInA = Math.max(segmentA.getStartRayDist(), segmentB.getStartRayDist());
		double endInA   = Math.min(segmentA.getEndRayDist(), segmentB.getEndRayDist());
		// check to see if endpoints of segmentA changed...
		
		if(startInA < segmentA.getStartRayDist()+TOL && startInA > segmentA.getStartRayDist()-TOL){
			// startpoint was not changed.
		}else{
			segmentA.setStart(startInA, segmentA.getMiddleDesc());
		}
		if(endInA < segmentA.getEndRayDist()+TOL && endInA > segmentA.getEndRayDist()-TOL){
			// endpoint was not changed.
		}else{
			segmentA.setEnd(endInA, segmentA.getMiddleDesc());
		}
		
		//
		// Handle all VERTEX_DESC possibilities.. craziness! :)
		//
		if(segmentA.VERT_DESC_is_VVV()){
			// subdividing -- none			
			// marking
			polyA.markVertexInPolyByIndex(segmentA.getVertIndexNearStart(),CSG_Vertex.VERT_TYPE.VERT_BOUNDARY);
		}
		if(segmentA.VERT_DESC_is_VEV()){
			// subdividing -- none			
			// marking
			polyA.markVertexInPolyByIndex(segmentA.getVertIndexNearStart(),CSG_Vertex.VERT_TYPE.VERT_BOUNDARY);	
			polyA.markVertexInPolyByIndex(segmentA.getVertIndexNearEnd(),CSG_Vertex.VERT_TYPE.VERT_BOUNDARY);			
		}
		if(segmentA.VERT_DESC_is_VEE()){
			
		}
		if(segmentA.VERT_DESC_is_VFV()){
			
		}
		if(segmentA.VERT_DESC_is_VFE()){
			
		}
		if(segmentA.VERT_DESC_is_EEV()){ 
			// Symmetric to VEE			
			
		}
		if(segmentA.VERT_DESC_is_EEE()){
			
		}
		if(segmentA.VERT_DESC_is_EFV()){
			// Symmetric to VFE
			
		}
		if(segmentA.VERT_DESC_is_EFE()){
			
		}
		if(segmentA.VERT_DESC_is_EFF()){
			// subdividing -- 	
			// TODO: for now just adding new polygons, not removing the old...
			if(segmentA.getStartDesc() == CSG_Segment.VERTEX_DESC.EDGE){
				// start is on the edge
			}else{
				// end is on the edge
			}
			
			// marking		
		}
		if(segmentA.VERT_DESC_is_FFV()){
			// Symmetric to VFF
			
		}
		if(segmentA.VERT_DESC_is_FFE()){
			// Symmetric to EFF
			
		}
		if(segmentA.VERT_DESC_is_FFF()){
			
		}
		
		
		
		System.out.println("subdividing: SegmentA is {" + segmentA.getStartDesc() + "-" + 
				segmentA.getMiddleDesc() + "-" + segmentA.getEndDesc() + "}");
				
		GLContext glc = GLContext.getCurrent();
		GL gl = glc.getGL();
		segmentA.drawSegmentForDebug(gl);
		//segmentB.drawSegmentForDebug(gl);
		
	}
	
	enum CSG_FACE_INFO {
		FACE_COPLANAR, FACE_INTERSECT, FACE_NOT_INTERSECT, FACE_UNKNOWN
	}
	
}
