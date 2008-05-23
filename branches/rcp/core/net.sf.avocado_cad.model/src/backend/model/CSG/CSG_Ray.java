package backend.model.CSG;

import net.sf.avocado_cad.model.api.csg.ICSGRay;
import net.sf.avocado_cad.model.api.csg.ICSGVertex;



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
public class CSG_Ray implements ICSGRay {

	private CSG_Vertex basePoint;
	private CSG_Vertex direction;
	
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
		direction = faceA.getPlaneNormal().getCrossProduct(faceB.getPlaneNormal()).getUnitLength();
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
				// x can be set to zero
				// (1) normA.y*y + normA.z*z - dA = 0 
				// (2) normB.y*y + normB.z*z - dB = 0
				// --> 
				double div = faceB.getPlaneNormal().getZ()*faceA.getPlaneNormal().getY() - 
				faceB.getPlaneNormal().getY()*faceA.getPlaneNormal().getZ();
				x = 0.0;
				y = (dB*faceA.getPlaneNormal().getZ() - dA*faceB.getPlaneNormal().getZ())/div;
				z = (dA*faceB.getPlaneNormal().getY() - dB*faceA.getPlaneNormal().getY())/div;
			}else{
				if(Math.abs(direction.getY()) > TOL){
					// y can be set to zero
					double div = faceB.getPlaneNormal().getX()*faceA.getPlaneNormal().getZ() - 
									faceB.getPlaneNormal().getZ()*faceA.getPlaneNormal().getX();
					x = (dA*faceB.getPlaneNormal().getZ() - dB*faceA.getPlaneNormal().getZ())/div;
					y = 0.0;
					z = (dB*faceA.getPlaneNormal().getX() - dA*faceB.getPlaneNormal().getX())/div;
				}else{
					if(Math.abs(direction.getZ()) > TOL){
						// z can be set to zero						
						double div = faceB.getPlaneNormal().getY()*faceA.getPlaneNormal().getX() - 
										faceB.getPlaneNormal().getX()*faceA.getPlaneNormal().getY();
						x = (dB*faceA.getPlaneNormal().getY() - dA*faceB.getPlaneNormal().getY())/div;
						y = (dA*faceB.getPlaneNormal().getX() - dB*faceA.getPlaneNormal().getX())/div;
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
		this.direction = direction.getUnitLength();
	}
	
	/**
	 * @return a copy of the ray's direction.
	 */
	public CSG_Vertex getDirection(){
		return direction.deepCopy();
	}
	
	/**
	 * @return a copy of the ray's basePoint.
	 */
	public CSG_Vertex getBasePoint(){
		return basePoint.deepCopy();
	}
	
	public CSG_Ray deepCopy(){
		return new CSG_Ray(basePoint.deepCopy(), direction.deepCopy());
	}
	
	public String toString(){
		return "CSG_Ray: base=" + basePoint + " direction=" + direction;
	}
	
	
	
	/**
	 * Get the vertex that is found at a distance along the ray.
	 * @param dist the distance along the ray to travel
	 * @return the CSG_Vertex of the point on the ray
	 */
	public CSG_Vertex getVertexAtDist(double dist){
		return basePoint.addToVertex(direction.getScaledCopy(dist));
	}
	
	public double getDistAlongRay(CSG_Vertex vert){
		double dist = basePoint.getDistBetweenVertices(vert);
		ICSGVertex originRay = vert.subFromVertex(basePoint);
		double dotProd = originRay.getDotProduct(direction);
		if(dotProd >= 0.0){
			return dist;
		}else{
			return -dist;
		}
	}
	
	/**
	 * get a ray that has been altered just slightly to
	 * hopefully get rid of any mathematical singularities.
	 * @return
	 */
	public CSG_Ray getPerturbedRay(){
		double pert = 1e-7;
		double pertX = pert*Math.random() - pert/2.0;
		double pertY = pert*Math.random() - pert/2.0;
		double pertZ = pert*Math.random() - pert/2.0;
		CSG_Vertex newDir = direction.addToVertex(new CSG_Vertex(pertX, pertY, pertZ));
		return new CSG_Ray(basePoint, newDir);
	}
	
}
