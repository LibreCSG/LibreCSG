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
		CSG_Solid solidAClone = solidA.deepCopy();
		splitSolidABySolidB(solidA, solidB);
		splitSolidABySolidB(solidB, solidAClone);
		splitSolidABySolidB(solidA, solidB);
		classifySolidAPolysInSolidB(solidA, solidB);
		classifySolidAPolysInSolidB(solidB, solidA);
		
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
		//               also: swapped order of solidA, solidB to allow
		//                     for newly split polygons (from A) to be 
		//                     considered by subsequent B polygons. 
		//
		// ___Spitting SolidA by SolidB___
		//
		//( 1) if(extent of solidB overlaps solidA)
		//( 2)   for each faceB in solidB
		//( 3)     if(extent of faceB overlaps solidA)
		//( 4)       for each polygonB in faceB
		//( 5)         if(extent of polygonB overlaps solidA)
		//( 6)           for each faceA in solidA
		//( 7)             if(extent of polygonB overlaps faceA)
		//( 8)              for each polygonA in faceA (array access to allow for adding of polygons)
		//( 9)                 if(extent of polygonA overlaps polygonB)
		//(10)                   ** analize them as in "5. Do Two Polygons Intersect"
		//(11)                   if(INTERSECT)
		//(12)                     ** subdivide polygonA as in "6. Subdividing non-coplanar polygons"
		//(13)                   else
		//(14)                     ** do nothing with polygonA (it was COPLANAR or NOT_INTERSECT)
		//(15)             clean up markedForDeletion Polygons in faceA
		

		System.out.println("Splitting Solids");
		// ( 1) if(extent of solidB overlaps solidA)
		if(sB.getBounds().overlapsBounds(sA.getBounds())){
			// ( 2) for each faceB in solidB
			Iterator<CSG_Face> faceIterB =  sB.getFacesIter();
			while(faceIterB.hasNext()){
				CSG_Face faceB = faceIterB.next();
				// ( 3) if(extent of faceB overlaps solidA)
				if(faceB.getBounds().overlapsBounds(sA.getBounds())){
					// ( 4) for each polygonB in faceB
					Iterator<CSG_Polygon> polyIterB = faceB.getPolygonIterator();
					while(polyIterB.hasNext()){
						CSG_Polygon polyB = polyIterB.next();
						// ( 5) if(extent of polygonB overlaps solidA)
						if(polyB.getBounds().overlapsBounds(sA.getBounds())){
							// ( 6) for each faceA in solidA
							Iterator<CSG_Face> faceIterA = sA.getFacesIter();
							while(faceIterA.hasNext()){
								CSG_Face faceA = faceIterA.next();
								// ( 7) if(extent of polygonB overlaps faceA)
								if(polyB.getBounds().overlapsBounds(faceA.getBounds())){
									// ( 8) for each polygonA in faceA (array access to allow for adding of polygons)
									for(int iPolyA=0; iPolyA < faceA.getPolygonListSize(); iPolyA++){
										//System.out.println("iPolyA:" + iPolyA + ", totalListSize:" + faceA.getPolygonListSize());
										CSG_Polygon polyA = faceA.getPolygonAtIndex(iPolyA);
										// ( 9) if(extent of polygonA overlaps polygonB)
										if(polyA.getBounds().overlapsBounds(polyB.getBounds())){
											// (10) ** analize them as in "5. Do Two Polygons Intersect"
											//System.out.println("considering polyA: " + polyA);
											// performPolyIntersection handles lines (11-14)
											CSG_FACE_INFO info = performPolyIntersection(polyA, faceA, polyB, faceB);
										}
									}
								}
								// (15) clean up markedForDeletion Polygons in faceA
								faceA.cleanupMarkedForDeletionPolygons();
							}							
						}						
					}
				}					
			}
		}
	}
	
	
	// returns the new version of polyA
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
		if(!(aMin > bMax-TOL || aMax < bMin+TOL)){
			//return CSG_FACE_INFO.FACE_INTERSECT;
			// perform Face Intersection!!  the segments overlap!
			//System.out.println("should perform face intersection!");			
			subdivideFaceA(polyA, faceA, segmentA, segmentB);	
			return CSG_FACE_INFO.FACE_INTERSECT;
		}
		//System.out.println("Faces were close, but no intersection.  here's the details.. :)  a(" + aMin + "," + aMax + " )  b(" + bMin + "," + bMax + ")");
		return CSG_FACE_INFO.FACE_NOT_INTERSECT;
	}
	
	// return new version of polyA
	private static void subdivideFaceA(CSG_Polygon polyA, CSG_Face faceA, CSG_Segment segmentA, CSG_Segment segmentB){
		// section 6.  find section of segmentB that overlaps segmentA...
		
		// save original values for segmentA to save work later.
		CSG_Vertex origStartVert = segmentA.getVertStart();					// start before segment trim
		CSG_Vertex origEndVert   = segmentA.getVertEnd();					// end before segment trim
		CSG_Segment.VERTEX_DESC origStartDescA = segmentA.getStartDesc();	// startDesc before segment trim
		CSG_Segment.VERTEX_DESC origEndDescA = segmentA.getEndDesc();		// endDesc before segment trim

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
		
		int startNextI = segmentA.getVertIndexNearStart();					// index of vertex after startVert or the
																			//   start vertex if origStartDescA = VERTEX
		int startPrevI = segmentA.getVertIndexNearStart()-1;				// index of vertex before startVert
		CSG_Vertex startNextVert = polyA.getVertAtModIndex(startNextI);		// vertex after startVert
		CSG_Vertex startVert = segmentA.getVertStart();						// startVert
		CSG_Vertex startPrevVert = polyA.getVertAtModIndex(startPrevI);		// vertex before startVert
		int endNextI = segmentA.getVertIndexNearEnd();						// index of vertex after endVert or the
																			//   end vertex if origEndDescA = VERTEX
		int endPrevI = segmentA.getVertIndexNearEnd()-1;					// index of vertex before endVert
		CSG_Vertex endNextVert = polyA.getVertAtModIndex(endNextI);			// vertex after endVert
		CSG_Vertex endVert = segmentA.getVertEnd();							// endVert
		CSG_Vertex endPrevVert = polyA.getVertAtModIndex(endPrevI);			// vertex before endVert		
		
		System.out.println("subdividing: SegmentA is {" + segmentA.getStartDesc() + "-" + 
				segmentA.getMiddleDesc() + "-" + segmentA.getEndDesc() + "}");
		
		//
		// Handle all VERTEX_DESC possibilities.. craziness! :)
		//
		if(segmentA.VERT_DESC_is_VVV()){
			// subdividing -- none			
		}
		if(segmentA.VERT_DESC_is_VEV()){
			// subdividing -- none						
		}
		if(segmentA.VERT_DESC_is_VEE()){
			// subdividing -- 	
			// Fig 6.3, (c) case of split to 2 polygons	
			int numVerts = polyA.getNumberVertices();
			if(startNextI%numVerts == (endNextI+1)%numVerts){
				// end is before start, going clockwise
				int startNextNextI = startNextI + 1;
				CSG_Vertex startNextNextVert = polyA.getVertAtModIndex(startNextNextI);
				CSG_Polygon newPoly1 = new CSG_Polygon(endVert, startVert, startNextNextVert);
				CSG_Polygon newPoly2 = new CSG_Polygon(endNextVert, endVert, startNextNextVert);
				for(int i = startNextNextI+1; !polyA.indexIsSameModSize(i, endNextI); i++){
					newPoly2.addVertex(polyA.getVertAtModIndex(i));
				}
				faceA.addPolygon(newPoly1);
				faceA.addPolygon(newPoly2);	
				polyA.markForDeletion();
			}else{
				// end is after start, going clockwise
				CSG_Polygon newPoly1 = new CSG_Polygon(startPrevVert, startVert, endVert);
				CSG_Polygon newPoly2 = new CSG_Polygon(startPrevVert, endVert, endNextVert);
				for(int i = endNextI+1; !polyA.indexIsSameModSize(i, startPrevI); i++){
					newPoly2.addVertex(polyA.getVertAtModIndex(i));
				}
				faceA.addPolygon(newPoly1);
				faceA.addPolygon(newPoly2);	
				polyA.markForDeletion();
			}		
			
		}
		if(segmentA.VERT_DESC_is_VFV()){
			// subdividing -- 	
			// Fig 6.3, (e) case of split to 2 polygons	
			CSG_Polygon newPoly1 = new CSG_Polygon(endPrevVert, endVert, startNextVert);
			for(int i = startNextI+1; !polyA.indexIsSameModSize(i, endPrevI); i++){
				newPoly1.addVertex(polyA.getVertAtModIndex(i));
			}
			CSG_Polygon newPoly2 = new CSG_Polygon(startPrevVert, startVert, endNextVert);
			for(int i = endNextI+1; !polyA.indexIsSameModSize(i, startPrevI); i++){
				newPoly2.addVertex(polyA.getVertAtModIndex(i));
			}
			faceA.addPolygon(newPoly1);
			faceA.addPolygon(newPoly2);
			polyA.markForDeletion();
			
		}
		if(segmentA.VERT_DESC_is_VFE()){
			// subdividing -- 	
			// Fig 6.3, (f) case of split to 2 polygons	
			CSG_Polygon newPoly1 = new CSG_Polygon(endPrevVert, endVert, startNextVert);
			for(int i = startNextI+1; !polyA.indexIsSameModSize(i, endPrevI); i++){
				newPoly1.addVertex(polyA.getVertAtModIndex(i));
			}
			CSG_Polygon newPoly2 = new CSG_Polygon(startNextVert, endVert, endNextVert);
			for(int i = endNextI+1; !polyA.indexIsSameModSize(i, startNextI); i++){
				newPoly2.addVertex(polyA.getVertAtModIndex(i));
			}
			faceA.addPolygon(newPoly1);
			faceA.addPolygon(newPoly2);
			polyA.markForDeletion();
			
		}
		if(segmentA.VERT_DESC_is_VFF()){
			// subdividing -- 	
			// Fig 6.3, (g) case of split to 4 polygons	(assume all 4 needed)
			// TODO handle subdivide special cases seperately
			CSG_Polygon newPoly1 = new CSG_Polygon(endVert, origEndVert, endNextVert);
			CSG_Polygon newPoly2 = new CSG_Polygon(endVert, endPrevVert, origEndVert);
			CSG_Polygon newPoly3 = new CSG_Polygon(startVert, endVert, endNextVert);
			for(int i = endNextI+1; !polyA.indexIsSameModSize(i, startNextI); i++){
				newPoly3.addVertex(polyA.getVertAtModIndex(i));
			}
			CSG_Polygon newPoly4 = new CSG_Polygon(endPrevVert, endVert, startNextVert);
			for(int i = startNextI+1; !polyA.indexIsSameModSize(i, endPrevI); i++){
				newPoly4.addVertex(polyA.getVertAtModIndex(i));
			}
			faceA.addPolygon(newPoly1);
			faceA.addPolygon(newPoly2);
			faceA.addPolygon(newPoly3);
			faceA.addPolygon(newPoly4);		
			polyA.markForDeletion();
			
		}		
		if(segmentA.VERT_DESC_is_EEV()){ 
			// Symmetric to VEE			
			// subdividing -- 	
			// Fig 6.3, (c) case of split to 2 polygons	
			CSG_Polygon newPoly1 = new CSG_Polygon(startVert, endVert, endNextVert);
			CSG_Polygon newPoly2 = new CSG_Polygon(startPrevVert, startVert, endNextVert);
			for(int i = endNextI+1; !polyA.indexIsSameModSize(i, startPrevI); i++){
				newPoly2.addVertex(polyA.getVertAtModIndex(i));
			}
			faceA.addPolygon(newPoly1);
			faceA.addPolygon(newPoly2);	
			polyA.markForDeletion();
			
		}
		if(segmentA.VERT_DESC_is_EEE()){
			// subdividing --
			// Fig 6.3, (i) case of split to 3 polygons
			// TODO handle subdivide special cases seperately
			int startNextNextI = startNextI + 1;
			CSG_Vertex startNextNextVert = polyA.getVertAtModIndex(startNextNextI);
			CSG_Polygon newPoly1 = new CSG_Polygon(startVert, startNextVert, startNextNextVert);
			CSG_Polygon newPoly2 = new CSG_Polygon(endVert, startVert, startNextNextVert);
			CSG_Polygon newPoly3 = new CSG_Polygon(endPrevVert, endVert, startNextNextVert);
			for(int i = startNextNextI+1; !polyA.indexIsSameModSize(i, endPrevI); i++){
				newPoly3.addVertex(polyA.getVertAtModIndex(i));
			}
			faceA.addPolygon(newPoly1);
			faceA.addPolygon(newPoly2);
			faceA.addPolygon(newPoly3);	
			polyA.markForDeletion();
			
		}
		if(segmentA.VERT_DESC_is_EFV()){
			// Symmetric to VFE
			// subdividing -- 	
			// Fig 6.3, (f) case of split to 2 polygons	
			CSG_Polygon newPoly1 = new CSG_Polygon(startPrevVert, startVert, endNextVert);
			for(int i = endNextI+1; !polyA.indexIsSameModSize(i, startPrevI); i++){
				newPoly1.addVertex(polyA.getVertAtModIndex(i));
			}
			CSG_Polygon newPoly2 = new CSG_Polygon(endNextVert, startVert, startNextVert);
			for(int i = startNextI+1; !polyA.indexIsSameModSize(i, endNextI); i++){
				newPoly2.addVertex(polyA.getVertAtModIndex(i));
			}
			faceA.addPolygon(newPoly1);
			faceA.addPolygon(newPoly2);
			polyA.markForDeletion();
			
		}
		if(segmentA.VERT_DESC_is_EFE()){
			// subdividing -- 	
			// Fig 6.3, (k) case of split to 2 polygons	
			CSG_Polygon newPoly1 = new CSG_Polygon(endPrevVert, endVert, startVert);
			for(int i = startNextI; !polyA.indexIsSameModSize(i, endPrevI); i++){
				newPoly1.addVertex(polyA.getVertAtModIndex(i));
			}
			CSG_Polygon newPoly2 = new CSG_Polygon(startPrevVert, startVert, endVert);
			for(int i = endNextI; !polyA.indexIsSameModSize(i, startPrevI); i++){
				newPoly2.addVertex(polyA.getVertAtModIndex(i));
			}
			faceA.addPolygon(newPoly1);
			faceA.addPolygon(newPoly2);
			polyA.markForDeletion();
			
		}
		if(segmentA.VERT_DESC_is_EFF()){			
			// subdividing -- 	
			// for simplicity, always do the Fig 6.3, (l) case of split to 4 polygons
			// TODO handle subdivide special cases seperately			
			CSG_Polygon newPoly1 = new CSG_Polygon(endVert, origEndVert, endNextVert);
			CSG_Polygon newPoly2 = new CSG_Polygon(endVert, endPrevVert, origEndVert);
			CSG_Polygon newPoly3 = new CSG_Polygon(startVert, endVert, endNextVert);
			for(int i = endNextI+1; !polyA.indexIsSameModSize(i, startNextI); i++){
				newPoly3.addVertex(polyA.getVertAtModIndex(i));
			}
			CSG_Polygon newPoly4 = new CSG_Polygon(endPrevVert, endVert, startVert);
			for(int i = startNextI; !polyA.indexIsSameModSize(i, endPrevI); i++){
				newPoly4.addVertex(polyA.getVertAtModIndex(i));
			}			
			faceA.addPolygon(newPoly1);
			faceA.addPolygon(newPoly2);
			faceA.addPolygon(newPoly3);
			faceA.addPolygon(newPoly4);
			polyA.markForDeletion();
			
		}
		if(segmentA.VERT_DESC_is_FFV()){
			// Symmetric to VFF
			// subdividing -- 	
			// Fig 6.3, (g) case of split to 4 polygons	(assume all 4 needed)
			CSG_Polygon newPoly1 = new CSG_Polygon(startVert, origStartVert, startNextVert);
			CSG_Polygon newPoly2 = new CSG_Polygon(startVert, startPrevVert, origStartVert);
			CSG_Polygon newPoly3 = new CSG_Polygon(endVert, startVert, startNextVert);
			for(int i = startNextI+1; !polyA.indexIsSameModSize(i, endNextI); i++){
				newPoly3.addVertex(polyA.getVertAtModIndex(i));
			}
			CSG_Polygon newPoly4 = new CSG_Polygon(startPrevVert, startVert, endNextVert);
			for(int i = endNextI+1; !polyA.indexIsSameModSize(i, startPrevI); i++){
				newPoly4.addVertex(polyA.getVertAtModIndex(i));
			}
			faceA.addPolygon(newPoly1);
			faceA.addPolygon(newPoly2);
			faceA.addPolygon(newPoly3);
			faceA.addPolygon(newPoly4);		
			polyA.markForDeletion();
			
		}
		if(segmentA.VERT_DESC_is_FFE()){
			// Symmetric to EFF			
			// subdividing -- 	
			// for simplicity, always do the Fig 6.3, (l) case of split to 4 polygons
			CSG_Polygon newPoly1 = new CSG_Polygon(startVert, origStartVert, startNextVert);
			CSG_Polygon newPoly2 = new CSG_Polygon(startVert, startPrevVert, origStartVert);
			CSG_Polygon newPoly3 = new CSG_Polygon(endVert, startVert, startNextVert);
			for(int i = startNextI+1; !polyA.indexIsSameModSize(i, endNextI); i++){
				newPoly3.addVertex(polyA.getVertAtModIndex(i));
			}
			CSG_Polygon newPoly4 = new CSG_Polygon(startPrevVert, startVert, endVert);
			for(int i = endNextI; !polyA.indexIsSameModSize(i, startPrevI); i++){
				newPoly4.addVertex(polyA.getVertAtModIndex(i));
			}			
			faceA.addPolygon(newPoly1);
			faceA.addPolygon(newPoly2);
			faceA.addPolygon(newPoly3);
			faceA.addPolygon(newPoly4);
			polyA.markForDeletion();
			
			// marking	
			
		}
		if(segmentA.VERT_DESC_is_FFF()){
			// subdividing -- 	
			// for simplicity, always do the Fig 6.3, (n) case of split to 6 polygons
			// TODO handle subdivide special cases seperately			
			CSG_Polygon newPoly1 = new CSG_Polygon(startVert, origStartVert, startNextVert);
			CSG_Polygon newPoly2 = new CSG_Polygon(startVert, startPrevVert, origStartVert);
			CSG_Polygon newPoly3 = new CSG_Polygon(endVert, endPrevVert, origEndVert);
			CSG_Polygon newPoly4 = new CSG_Polygon(endVert, origEndVert, endNextVert);				
			CSG_Polygon newPoly5 = new CSG_Polygon(endPrevVert, endVert, startVert, startNextVert);
			for(int i = startNextI+1; !polyA.indexIsSameModSize(i, endPrevI); i++){
				newPoly5.addVertex(polyA.getVertAtModIndex(i));
			}		
			CSG_Polygon newPoly6 = new CSG_Polygon(startPrevVert, startVert, endVert, endNextVert);
			for(int i = endNextI+1; !polyA.indexIsSameModSize(i, startPrevI); i++){
				newPoly6.addVertex(polyA.getVertAtModIndex(i));
			}			
			faceA.addPolygon(newPoly1);
			faceA.addPolygon(newPoly2);
			faceA.addPolygon(newPoly3);
			faceA.addPolygon(newPoly4);
			faceA.addPolygon(newPoly5);
			faceA.addPolygon(newPoly6);
			polyA.markForDeletion();
		}
		
		
		
		
				
		GLContext glc = GLContext.getCurrent();
		GL gl = glc.getGL();
		segmentA.drawSegmentForDebug(gl);
		//segmentB.drawSegmentForDebug(gl);
	
	}
	
	
	private static void classifySolidAPolysInSolidB(CSG_Solid sA, CSG_Solid sB){
		Iterator<CSG_Face> faceIterA = sA.getFacesIter();
		while(faceIterA.hasNext()){
			CSG_Face faceA = faceIterA.next();
			Iterator<CSG_Polygon> polyIterA = faceA.getPolygonIterator();
			while(polyIterA.hasNext()){
				CSG_Polygon polyA = polyIterA.next();
				classifyPolygonAInSolidB(polyA, sB);
			}
		}
	}
	
	private static void classifyPolygonAInSolidB(CSG_Polygon polyA, CSG_Solid solidB){
		// See Fig 7.2, Polygon Classification Routine
		CSG_Vertex barycenterA = polyA.getBarycenterVertex();
		CSG_Vertex normalA = polyA.getPlane().getNormal();
		// start with perturbed ray to reduce liklihood of unsuccessful cast
		CSG_Ray ray = new CSG_Ray(barycenterA, normalA).getPerturbedRay();
		//System.out.println(polyA);
		//System.out.println("poly-- Barycenter: " + barycenterA + ", rayBase: " + ray.getBasePoint());
		boolean castWasSuccessful = false;
		CSG_Polygon closestPolyB = null;
		double closestDist = Double.MAX_VALUE;

		while(!castWasSuccessful){
			castWasSuccessful = true; // assumed true unless found to be faulty			
			Iterator<CSG_Face> faceIterB = solidB.getFacesIter();
			while(faceIterB.hasNext() && castWasSuccessful){
				CSG_Face faceB = faceIterB.next();
				Iterator<CSG_Polygon> polyIterB = faceB.getPolygonIterator();
				while(polyIterB.hasNext() && castWasSuccessful){
					CSG_Polygon polyB = polyIterB.next();
					double dotProduct = polyB.getPlane().getNormal().getDotProduct(ray.getDirection());
					CSG_Vertex intersectPolyBVert = polyB.getRayIntersectionWithPlane(ray);
					if(intersectPolyBVert == null){
						// no intersection, do nothing... just continue checking
						continue; // use continue to bypass things that may reference this NULL CSG_Vertex
					}
					double distance = ray.getDistAlongRay(intersectPolyBVert);
					boolean dotProductIsZero = (dotProduct < TOL && dotProduct > -TOL);
					boolean distanceIsZero = (distance < TOL && distance > -TOL);					
				
					//System.out.println("dot:" + dotProduct + ", dist:" + distance + ", intPt" + intersectPolyBVert);
					
					if(dotProductIsZero && distanceIsZero){
						// dotProduct = 0.0 && distance = 0.0
						// cast was unsuccessful, try again with a perturbed ray
						ray = ray.getPerturbedRay();
						castWasSuccessful = false;
					}else{
						if(dotProductIsZero && distance > TOL){
							// dotProduct = 0.0 && distance > 0.0
							// no intersection, do nothing... just continue checking
						}else{
							if(!dotProductIsZero && distanceIsZero){
								// dotProduct != 0.0, distance = 0.0
								if(polyB.vertexIsInsidePolygon(intersectPolyBVert)){
									// ray passes through polygon (closest possible!)
									closestPolyB = polyB;
									closestDist  = 0.0;
								}else{
									// no intersection, do nothing... just continue checking
								}
							}else{
								if(!dotProductIsZero && distance > TOL){
									// dotProduct != 0.0 && distance > 0.0
									if(distance < closestDist){
										// this is closer than the polyB currently held.. check it further
										if(polyB.vertexIsInsidePolygon(intersectPolyBVert)){
											// ray passes through polygon, and it's closer that what we have..
											// keep this polyB.
											// TODO: handle ray intersect polygon edge...
											closestPolyB = polyB;
											closestDist = distance;
										}
									}else{
										// no intersection, do nothing... just continue checking
									}
								}else{
									// no intersection, do nothing... just continue checking
								}
							}
						}
					}
				}				
			}
			if(!castWasSuccessful){
				System.out.println("unsuccessful cast.. trying again.");
			}
		} // end the main while(!castWasSuccessful) loop
		
		if(closestPolyB == null){
			// no intersection.... polygon is outside
			polyA.type = CSG_Polygon.POLY_TYPE.POLY_OUTSIDE;
		}else{
			double dotProduct = closestPolyB.getPlane().getNormal().getDotProduct(ray.getDirection());
			boolean closestDistIsZero = (closestDist < TOL && closestDist > -TOL);
			if(closestDistIsZero){
				if(dotProduct > TOL){
					polyA.type = CSG_Polygon.POLY_TYPE.POLY_SAME;
				}else{
					if(dotProduct < -TOL){
						polyA.type = CSG_Polygon.POLY_TYPE.POLY_OPPOSITE;
					}
				}
			}else{
				if(dotProduct > TOL){
					polyA.type = CSG_Polygon.POLY_TYPE.POLY_INSIDE;
				}else{
					if(dotProduct < -TOL){
						polyA.type = CSG_Polygon.POLY_TYPE.POLY_OUTSIDE;
					}
				}
			}
		}
		
	}
	
	enum CSG_FACE_INFO {
		FACE_COPLANAR, FACE_INTERSECT, FACE_NOT_INTERSECT, FACE_UNKNOWN
	}
	
}
