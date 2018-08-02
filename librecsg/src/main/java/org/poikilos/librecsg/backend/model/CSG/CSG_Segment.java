package org.poikilos.librecsg.backend.model.CSG;

import java.util.Iterator;
import java.util.List;

import com.jogamp.opengl.GL2;


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
 * Constructive Solid Geometry :: Segment<br/><br/>
 *
 * A line segment in 3D space.<br/><br/>
 *
 * The CSG_Segment also stores descriptor information
 * about the starting, middle, and ending points.
 * Additionally, neighboring vertices and distance
 * information along a plane intersection line is also
 * kept. <br/><br/>
 *
 * Algorithms and structures from:<br/>
 * - Laidlaw, Trumbore, and Hughes <br/>
 * - "Constructive Solid Geometry for Polyhedral Objects"<br/>
 * - SIGGRAPH 1986, Volume 20, Number 4, pp.161-170 *
 */
public class CSG_Segment {

	// Data structure from Fig. 5.1
    private double distStartFromP;
    private double distEndFromP;
	private CSG_Ray ray;
	private CSG_Vertex startVert;
	private CSG_Vertex endVert;
	public enum VERTEX_DESC { VERTEX, EDGE, FACE, UNKNOWN };
	private VERTEX_DESC descStart = VERTEX_DESC.UNKNOWN;
	private VERTEX_DESC descMid   = VERTEX_DESC.UNKNOWN;
	private VERTEX_DESC descEnd   = VERTEX_DESC.UNKNOWN;
	private int vertNearStartPt = 0;
	private int vertNearEndPt   = 0;

	private final double TOL = 1e-10;

	/**
	 * construct a new CSG_Segment by finding the part of a ray that
	 * exists within a polygon.
	 * @param poly the polygon that contains the segment.
	 * @param zDists the distance from another polygon's plane to this polygon.
	 * @param ray the ray that will contain the segment.
	 */
	public CSG_Segment(CSG_Polygon poly, List<Double> zDists, CSG_Ray ray){
		this.ray = ray;
		if(poly.getNumberVertices() != zDists.size()){
			System.out.println("** CSG_Segment(constructor): OMG, d00d the number of zDists doesn't match your face.");
			distStartFromP = 0.0;
			distEndFromP = 1.0;
			startVert = ray.getBasePoint();
			endVert = startVert.addToVertex(ray.getDirection());
			return;
		}
		Iterator<CSG_Vertex> fIter = poly.getVertexIterator();
		Iterator<Double> dIter = zDists.iterator();
		double dist = 0.0;
		boolean lastDistWasPos = false;
		boolean lastDistWasNeg = false;
		CSG_Vertex lastVert = null;
		double lastDist = 0.0;
		//
		// check each vertex and pairs of vertices to find segment ends.
		//
		int vertIndex = 0;
		while(fIter.hasNext() && endVert == null){
			dist = dIter.next();
			CSG_Vertex vert = fIter.next();

			if(dist < TOL && dist > -TOL){
				// vertex intersects, add it
				includeVertex(vert, VERTEX_DESC.VERTEX, vertIndex);
				lastDistWasNeg = false;
				lastDistWasPos = false;
			}else{
				if(dist > TOL){
					if(lastDistWasNeg && lastVert != null){
						// do line intersection (ratio of zDists)
						double alpha = Math.abs(lastDist)/(Math.abs(lastDist)+Math.abs(dist));
						CSG_Vertex tempV = vert.getScaledCopy(alpha);
						tempV = tempV.addToVertex(lastVert.getScaledCopy(1.0-alpha));
						includeVertex(tempV, VERTEX_DESC.EDGE, vertIndex);
					}
					lastDistWasNeg = false;
					lastDistWasPos = true;
				}else{
					if(lastDistWasPos && lastVert != null){
						// do line intersection (ratio of zDists)
						double alpha = Math.abs(lastDist)/(Math.abs(lastDist)+Math.abs(dist));
						CSG_Vertex tempV = vert.getScaledCopy(alpha);
						tempV = tempV.addToVertex(lastVert.getScaledCopy(1.0-alpha));
						includeVertex(tempV, VERTEX_DESC.EDGE, vertIndex);
					}
					lastDistWasPos = false;
					lastDistWasNeg = true;
				}
			}
			lastVert = vert;
			lastDist = dist;
			vertIndex++;
		}

		//
		// check last (wrap-around) case (last to first vertices)
		//
		fIter = poly.getVertexIterator();
		dIter = zDists.iterator();
		vertIndex = 0;
		if(fIter.hasNext() && endVert == null){
			dist = dIter.next();
			CSG_Vertex vert = fIter.next();
			if(dist < TOL && dist > -TOL){
				// vertex intersects, add it
				includeVertex(vert, VERTEX_DESC.VERTEX, vertIndex);
			}else{
				if(dist > TOL){
					if(lastDistWasNeg && lastVert != null){
						// do line intersection (ratio of zDists)
						double alpha = Math.abs(lastDist)/(Math.abs(lastDist)+Math.abs(dist));
						CSG_Vertex tempV = vert.getScaledCopy(alpha);
						tempV = tempV.addToVertex(lastVert.getScaledCopy(1.0-alpha));
						includeVertex(tempV, VERTEX_DESC.EDGE, vertIndex);
					}
					lastDistWasNeg = false;
					lastDistWasPos = true;
				}else{
					if(lastDistWasPos && lastVert != null){
						// do line intersection (ratio of zDists)
						double alpha = Math.abs(lastDist)/(Math.abs(lastDist)+Math.abs(dist));
						CSG_Vertex tempV = vert.getScaledCopy(alpha);
						tempV = tempV.addToVertex(lastVert.getScaledCopy(1.0-alpha));
						includeVertex(tempV, VERTEX_DESC.EDGE, vertIndex);
					}
					lastDistWasPos = false;
					lastDistWasNeg = true;
				}
			}
		}

		if(startVert != null && endVert == null){
			// Fig 5.2, for vertex,vertex,vertex case
			includeVertex(endVert, VERTEX_DESC.VERTEX, vertNearStartPt);
		}

		// TODO make sure both vertA and vertB are not null!
		if(startVert == null || endVert == null){
			System.out.println("*** null verts: start=" + startVert + " and end=" + endVert);
			startVert = new CSG_Vertex(0,0,0);
			endVert = new CSG_Vertex(0,0,0);
		}else{
			//System.out.println("got a good pair of verts! :)");
		}
		// startVert and endVert now contain the endpoints!
		this.distStartFromP = this.ray.getDistAlongRay(startVert);
		this.distEndFromP   = this.ray.getDistAlongRay(endVert);

		// update the middle vertex desciption
		if(vertNearStartPt == vertNearEndPt){
			descMid = VERTEX_DESC.VERTEX;
		}else{
			// TODO: BIG PROBLEM -- VEV being falsly classified as VFE
			// this is most likely due to the final vertex being classified
			// as an EDGE instead of a VERTEX, which in turn pushed the middle
			// to FACE instead of EDGE. (perhaps error in zDists calculation?)
			// or perhaps this analysis is bogus, and the problem is somewhere else completely...
			if(descStart == VERTEX_DESC.VERTEX && descEnd == VERTEX_DESC.VERTEX &&
					((vertNearStartPt-vertNearEndPt)%(poly.getNumberVertices()-2) == 1 ||
							(vertNearStartPt-vertNearEndPt)%(poly.getNumberVertices()-2) == -1)){
				// segment joins two neighboring vertices, middle must be a line
				descMid = VERTEX_DESC.EDGE;
			}else{
				descMid = VERTEX_DESC.FACE;
			}
		}

		ensureEndpointOrder();

	}


	// make sure endpoints are ordered with
	// start < end.
	private void ensureEndpointOrder(){
		if(distStartFromP > distEndFromP){
			// make distStartFromP always smaller...
			// swap start and end points.
			VERTEX_DESC descTemp = descStart;
			CSG_Vertex  vertTemp = startVert;
			int vertNearTemp = vertNearStartPt;
			double distTemp = distStartFromP;
			descStart = descEnd;
			startVert = endVert;
			vertNearStartPt = vertNearEndPt;
			distStartFromP = distEndFromP;
			descEnd = descTemp;
			endVert = vertTemp;
			vertNearEndPt = vertNearTemp;
			distEndFromP = distTemp;
		}
	}

	/**
	 * if vertA == NULL, place vert in vertA; <br/>
	 * else vertB = vert;
	 * @param vert
	 * @param vertA
	 * @param vertB
	 */
	private void includeVertex(CSG_Vertex vert, VERTEX_DESC vertDesc, int neighborIndex){
		//System.out.println("adding vert...");
		if(startVert == null){
			startVert = vert;
			descStart = vertDesc;
			vertNearStartPt = neighborIndex;
		}else{
			endVert = vert;
			descEnd = vertDesc;
			vertNearEndPt = neighborIndex;
		}
	}

	public CSG_Segment(CSG_Vertex startVert, CSG_Vertex endVert){
		this.startVert = startVert;
		this.endVert   = endVert;
		this.ray = new CSG_Ray(startVert,endVert.subFromVertex(startVert));
		this.distStartFromP = 0.0;
		this.distEndFromP = endVert.getDistBetweenVertices(startVert);
		ensureEndpointOrder();
	}


	public void drawSegmentForDebug(GL2 gl){
		gl.glBegin(GL2.GL_LINES);
			gl.glColor3f(0.5f,0.5f,0.5f);
			gl.glVertex3d(startVert.getX(), startVert.getY(), startVert.getZ());
			gl.glVertex3d(endVert.getX(), endVert.getY(), endVert.getZ());
		gl.glEnd();
		gl.glBegin(GL2.GL_POINTS);
			gl.glColor3f(0.0f,1.0f,0.0f);
			gl.glVertex3d(startVert.getX(), startVert.getY(), startVert.getZ());
			gl.glColor3f(1.0f,0.0f,0.0f);
			gl.glVertex3d(endVert.getX(), endVert.getY(), endVert.getZ());
		gl.glEnd();
	}

	/**
	 * @return minimum distance along ray (the start) that segment ends.
	 */
	public double getStartRayDist(){
		if(distStartFromP > distEndFromP){
			System.out.println("** CSG_Segment(getStartRayDist): start dist is not less than end dist!!");
		}
		return distStartFromP;
	}

	/**
	 * @return maximum distance along ray (the end) that segment ends.
	 */
	public double getEndRayDist(){
		if(distStartFromP > distEndFromP){
			System.out.println("** CSG_Segment(getEndRayDist): start dist is not less than end dist!!");
		}
		return distEndFromP;
	}

	public void setStart(double startDist, VERTEX_DESC startDesc){
		distStartFromP = startDist;
		startVert = ray.getVertexAtDist(startDist);
		descStart = startDesc;
		//ensureEndpointOrder();
	}

	public void setEnd(double endDist, VERTEX_DESC endDesc){
		distEndFromP = endDist;
		endVert = ray.getVertexAtDist(endDist);
		descEnd = endDesc;
		//ensureEndpointOrder();
	}

	public CSG_Ray getRay(){
		return ray;
	}

	public VERTEX_DESC getStartDesc(){
		return descStart;
	}

	public VERTEX_DESC getMiddleDesc(){
		return descMid;
	}

	public VERTEX_DESC getEndDesc(){
		return descEnd;
	}

	public int getVertIndexNearStart(){
		return vertNearStartPt;
	}

	public int getVertIndexNearEnd(){
		return vertNearEndPt;
	}

	public CSG_Vertex getVertStart(){
		return startVert;
	}

	public CSG_Vertex getVertEnd(){
		return endVert;
	}

	/** Segment type: Vertex, Vertex, Vertex */
	public boolean VERT_DESC_is_VVV(){
		return descStart == VERTEX_DESC.VERTEX && descMid == VERTEX_DESC.VERTEX && descEnd == VERTEX_DESC.VERTEX;
	}

	/** Segment type: Vertex, Edge, Vertex */
	public boolean VERT_DESC_is_VEV(){
		return descStart == VERTEX_DESC.VERTEX && descMid == VERTEX_DESC.EDGE && descEnd == VERTEX_DESC.VERTEX;
	}

	/** Segment type: Vertex, Edge, Edge */
	public boolean VERT_DESC_is_VEE(){
		return descStart == VERTEX_DESC.VERTEX && descMid == VERTEX_DESC.EDGE && descEnd == VERTEX_DESC.EDGE;
	}

	/** Segment type: Vertex, Face, Vertex */
	public boolean VERT_DESC_is_VFV(){
		return descStart == VERTEX_DESC.VERTEX && descMid == VERTEX_DESC.FACE && descEnd == VERTEX_DESC.VERTEX;
	}

	/** Segment type: Vertex, Face, Edge */
	public boolean VERT_DESC_is_VFE(){
		return descStart == VERTEX_DESC.VERTEX && descMid == VERTEX_DESC.FACE && descEnd == VERTEX_DESC.EDGE;
	}

	/** Segment type: Vertex, Face, Face */
	public boolean VERT_DESC_is_VFF(){
		return descStart == VERTEX_DESC.VERTEX && descMid == VERTEX_DESC.FACE && descEnd == VERTEX_DESC.FACE;
	}

	/** Segment type: Edge, Edge, Vertex */
	public boolean VERT_DESC_is_EEV(){
		return descStart == VERTEX_DESC.EDGE && descMid == VERTEX_DESC.EDGE && descEnd == VERTEX_DESC.VERTEX;
	}

	/** Segment type: Edge, Edge, Edge */
	public boolean VERT_DESC_is_EEE(){
		return descStart == VERTEX_DESC.EDGE && descMid == VERTEX_DESC.EDGE && descEnd == VERTEX_DESC.EDGE;
	}

	/** Segment type: Edge, Face, Vertex */
	public boolean VERT_DESC_is_EFV(){
		return descStart == VERTEX_DESC.EDGE && descMid == VERTEX_DESC.FACE && descEnd == VERTEX_DESC.VERTEX;
	}

	/** Segment type: Edge, Face, Edge */
	public boolean VERT_DESC_is_EFE(){
		return descStart == VERTEX_DESC.EDGE && descMid == VERTEX_DESC.FACE && descEnd == VERTEX_DESC.EDGE;
	}

	/** Segment type: Edge, Face, Face */
	public boolean VERT_DESC_is_EFF(){
		return descStart == VERTEX_DESC.EDGE && descMid == VERTEX_DESC.FACE && descEnd == VERTEX_DESC.FACE;
	}

	/** Segment type: Face, Face, Vertex */
	public boolean VERT_DESC_is_FFV(){
		return descStart == VERTEX_DESC.FACE && descMid == VERTEX_DESC.FACE && descEnd == VERTEX_DESC.VERTEX;
	}

	/** Segment type: Face, Face, Edge */
	public boolean VERT_DESC_is_FFE(){
		return descStart == VERTEX_DESC.FACE && descMid == VERTEX_DESC.FACE && descEnd == VERTEX_DESC.EDGE;
	}

	/** Segment type: Face, Face, Face */
	public boolean VERT_DESC_is_FFF(){
		return descStart == VERTEX_DESC.FACE && descMid == VERTEX_DESC.FACE && descEnd == VERTEX_DESC.FACE;
	}

	public String toString(){
		return "CSG_Segment{" + startVert + "(" + descStart + ")," + endVert + "(" + descEnd + ")}";
	}

}
