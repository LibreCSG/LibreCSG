package org.poikilos.librecsg.backend.model.ref;

import org.poikilos.librecsg.backend.model.Part;
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
 * a plane reference that actually points to a plane
 * that can be used directly.
 */
public class ModRef_PlaneFixed extends ModRef_Plane{

	private final SketchPlane fixedSketchPlane;

	public ModRef_PlaneFixed(SketchPlane fixedSketchPlane) {
		super(-1, -1);
		this.fixedSketchPlane = fixedSketchPlane;
		if(fixedSketchPlane == null){
			System.out.println("ModRef_PlaneRoot(getSketchPlane): rootPlaneType was NULL! whah?!?");
		}
	}

	public SketchPlane getSketchPlane(Part part){
		return fixedSketchPlane;
	}
}
