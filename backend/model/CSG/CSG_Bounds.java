package backend.model.CSG;


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
 * Constructive Solid Geometry :: Bounds<br/><br/>
 * 
 * Simple max/min bounding box to speed up intersection computation.
 */
public class CSG_Bounds{

	private double minX, minY, minZ, maxX, maxY, maxZ;
	
	private final double TOL = 1e-10; // double tollerance  
	
	
	public CSG_Bounds(){
		minX = Double.MAX_VALUE;
		minY = Double.MAX_VALUE;
		minZ = Double.MAX_VALUE;
		maxX = Double.MIN_VALUE;
		maxY = Double.MIN_VALUE;
		maxZ = Double.MIN_VALUE;
	}
	
	public CSG_Bounds(CSG_Vertex firstVertex){
		minX = firstVertex.getX();
		maxX = firstVertex.getX();
		minY = firstVertex.getY();
		maxY = firstVertex.getY();
		minZ = firstVertex.getZ();
		maxZ = firstVertex.getZ();
	}
	
	/**
	 * update the bounds min/max to include a CSG_Vertex
	 * @param v the new CSG_Vertex to include
	 */
	public void includeVertex(CSG_Vertex v){
		minX = Math.min(minX, v.getX());
		maxX = Math.max(maxX, v.getX());		
		minY = Math.min(minY, v.getY());
		maxY = Math.max(maxY, v.getY());		
		minZ = Math.min(minZ, v.getZ());
		maxZ = Math.max(maxZ, v.getZ());
	}
	
	/**
	 * update the bounds min/max to include another CSG_Bounds
	 * @param bounds CSG_Bounds to include
	 */
	public void includeBounds(CSG_Bounds bounds){
		includeVertex(new CSG_Vertex(bounds.minX, bounds.minY, bounds.minZ));
		includeVertex(new CSG_Vertex(bounds.maxX, bounds.maxY, bounds.maxZ));
	}
	
	public String toString(){
		return "CSG_Bounds:{(" + minX + "," + maxX + ")(" + minY + "," + maxY + ")(" + minZ + "," + maxZ + ")}";
	}
	
	/**
	 * @return a Deep Copy of the instance of this class.
	 */
	public CSG_Bounds deepCopy(){
		CSG_Bounds clone = new CSG_Bounds(new CSG_Vertex(minX, minY, minZ));
		clone.includeVertex(new CSG_Vertex(maxX, maxY, maxZ));
		return clone;
	}
	
	public CSG_Vertex getBoundCenter(){
		return new CSG_Vertex((minX+maxX)/2.0, (minY+maxY)/2.0, (minZ+maxZ)/2.0);
	}
	
	public boolean overlapsBounds(CSG_Bounds b2){
		boolean xOK = (minX > b2.maxX-TOL) || (b2.minX > maxX-TOL);  
		boolean yOK = (minY > b2.maxY-TOL) || (b2.minY > maxY-TOL);
		boolean zOK = (minZ > b2.maxZ-TOL) || (b2.minZ > maxZ-TOL);
		return (!xOK || !yOK || !zOK);
	}
	
}
