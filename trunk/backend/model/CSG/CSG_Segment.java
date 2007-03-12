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
	private enum POINT_DESC { VERTEX, EDGE, FACE, UNKNOWN };
	private POINT_DESC descStart = POINT_DESC.UNKNOWN;
	private POINT_DESC descMid   = POINT_DESC.UNKNOWN;
	private POINT_DESC descEnd   = POINT_DESC.UNKNOWN;
	private CSG_Vertex vertNearStartPt = null;
	private CSG_Vertex vertNearEndPt   = null;
	
	public CSG_Segment(){
		
	}
	
}
