package backend.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.avocado_cad.model.api.IPart;
import net.sf.avocado_cad.model.api.IPartMaterial;
import net.sf.avocado_cad.model.api.ISubPart;
import net.sf.avocado_cad.model.api.csg.ICSGVertex;
import net.sf.avocado_cad.model.api.event.BackendGlobal;
import net.sf.avocado_cad.model.api.sketch.ISketchPlane;
import backend.model.CSG.BoolOp;
import backend.model.CSG.CSG_BooleanOperator;
import backend.model.CSG.CSG_Plane;
import backend.model.CSG.CSG_Solid;
import backend.model.CSG.CSG_Vertex;
import backend.model.material.PartMaterial;
import backend.model.ref.ModRef_Plane;
import backend.model.ref.ModRef_PlaneFixed;
import backend.model.sketch.SketchPlane;


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
* @created Feb. 2007
*/
public class Part implements IPart {

	private List<SubPart> subPartList = new LinkedList<SubPart>();
	
	protected int activeSubPart = -1;
	
	protected Group group;
	public final int ID;
	private int subPartCounter = 1;
	
	private ICSGVertex origin = new CSG_Vertex(0.0, 0.0, 0.0);
	private CSG_Vertex xAxis  = new CSG_Vertex(1.0, 0.0, 0.0);
	private CSG_Vertex yAxis  = new CSG_Vertex(0.0, 1.0, 0.0);
	private CSG_Vertex zAxis  = new CSG_Vertex(0.0, 0.0, 1.0);
	public final ISketchPlane sketchPlaneXY = new SketchPlane(new CSG_Plane(zAxis, 0.0));
	public final ISketchPlane sketchPlaneYZ = new SketchPlane(new CSG_Plane(xAxis, 0.0));
	public final ISketchPlane sketchPlaneZX = new SketchPlane(new CSG_Plane(yAxis, 0.0));

	public final ModRef_Plane planeXY = new ModRef_PlaneFixed(sketchPlaneXY);
	public final ModRef_Plane planeYZ = new ModRef_PlaneFixed(sketchPlaneYZ);
	public final ModRef_Plane planeZX = new ModRef_PlaneFixed(sketchPlaneZX);
	
	private ModRef_Plane selectedPlane = null;
	
	private IPartMaterial partMaterial = new PartMaterial(0.6, 0.9, 0.6, 1.0);
	
	private CSG_Solid partSolid = new CSG_Solid(); 
	
	public Part(Group group, int ID){
		this.group = group;
		this.ID = ID;
	}
	
	public Group getParentGroup(){
		return this.group;		
	}
	
	public void setSelectedPlane(ModRef_Plane selectedPlane){
		this.selectedPlane = selectedPlane;
	}
	
	public ModRef_Plane getSelectedPlane(){
		return selectedPlane;
	}
	
	public Iterator<SubPart> getSubPartIterator(){
		return subPartList.iterator();
	}
	public Collection<SubPart> getSubParts() {
		return subPartList;
	}
	public int addNewSketchOnSelectedPlane(){
		if(selectedPlane == null){
			// default plane if nothing is selected: XY plane
			selectedPlane = planeXY;
		}
		subPartList.add(new Sketch(this, subPartCounter++, selectedPlane));
		BackendGlobal.modelEventHandler.notifyElementAdded();
		int newIndex = subPartList.size()-1;
		activeSubPart = newIndex;
		return newIndex;
	}
	
	/**
	 * add a new feature2D3D (build) to the part by referencing the ID of the sketch to use.
	 * @param sketchID
	 * @return
	 */
	public int addNewFeat2D3D(int sketchID){
		if(sketchID > 0 && sketchID < subPartCounter && getSketchByID(sketchID) != null){
			subPartList.add(new Build(this, sketchID, subPartCounter++));
			BackendGlobal.modelEventHandler.notifyElementAdded();
			return subPartList.size()-1;
		}else{
			System.out.println(" *** PART *** could not add a New Feature2D3D since sketchID was invalid! sketchID=" + sketchID);
			return -1;
		}
	}
	
	public int addNewFeat3D3D(){
		subPartList.add(new Modify(this, null, subPartCounter++));
		BackendGlobal.modelEventHandler.notifyElementAdded();
		return subPartList.size()-1;
	}
	
	/**
	 * get the SubPart at a give index
	 * @param i index
	 * @return the SubPart, or null if index was invalid.
	 */
	public SubPart getAtIndex(int i){
		// TODO: don't get items by index!!
		if(i < 0 || i >= subPartList.size()){
			// index is not valid!
			return null;
		}
		return subPartList.get(i);
	}
	
	/**
	 * @return the size() of the list of SubParts.
	 */
	public int getSubPartListSize(){
		return subPartList.size();
	}
	
	/**
	 * @return true if a CSG_Solid exists yet for the part.
	 */
	public boolean solidExists(){
		if(partSolid.getNumberOfFaces() > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * Modify/Build on this part's current solid representation.
	 * @param solid the CSG_Solid to add/subtract/intersect with this part's existing solid
	 * @param boolop the boolean operation to apply. 
	 */
	public void updateSolid(CSG_Solid solid, BoolOp boolop){
		switch(boolop){
			case Union:{
				partSolid = CSG_BooleanOperator.Union(partSolid, solid);
				break;
			}
			case Intersection:{
				partSolid = CSG_BooleanOperator.Intersection(partSolid, solid);
				break;
			}
			case Subtracted:{
				partSolid = CSG_BooleanOperator.Subtraction(partSolid, solid);
				break;
			}
			case SubtractFrom:{
				partSolid = CSG_BooleanOperator.Subtraction(solid, partSolid);
				break;
			}
			default:{
				System.out.println("Part(updateSolid): Unknown boolean operation, aborting solid update.");
				break;
			}
		}
	}
	
	
	// TODO: Hack!, don't actually pass the solid (but it's big so copying may be problematic...)
	public CSG_Solid getSolid(){
		return partSolid;
	}
	
	/**
	 * set the index of the SubPart that should be set to Active.
	 * @param i index
	 */
	public void setActiveSubPart(int i){
		// TODO: set active by ID, not index! (abstract away the way things are stored)
		if(i < 0 || i >= subPartList.size()){
			// index is not valid!
			return;
		}
		activeSubPart = i;
		BackendGlobal.modelEventHandler.notifyActiveElementChanged();
	}
	
	public IPartMaterial getPartMaterial(){
		return partMaterial;
	}
	
	public void setPartMaterial(IPartMaterial newPartMaterial){
		partMaterial = newPartMaterial;
	}
	
	/**
	 * set the active SubPart to none
	 */
	public void setActiveToNone(){
		activeSubPart = -1;
		BackendGlobal.modelEventHandler.notifyActiveElementChanged();
	}
	
	/**
	 * get the currently active SubPart
	 * @return the active SubPart, or null if no SubPart is active
	 */
	public SubPart getActiveSubPart(){
		return this.getAtIndex(activeSubPart);
	}
	
	/**
	 * Remove the SubPart at the index if present.
	 * @param i index
	 */
	public void removeSubPartAtIndex(int i){
		// TODO: don't remove things by index!
		if(i < 0 || i >= subPartList.size()){
			// index is not valid!
			return;
		}
		subPartList.remove(i);
		BackendGlobal.modelEventHandler.notifyElementRemoved();
	}
	
	public ISubPart getSubpartByUniqueID(int ID){
		for(ISubPart sp : subPartList){
			if(sp.getUniqueID() == ID){
				return sp;
			}
		}
		System.out.println("tried to get a subpart with a bogus unique ID!! requested: " + ID);
		return null; // no SubPart was found with that ID!
	}
	
	/**
	 * remove the active SubPart from the list.
	 */
	public void removeActiveSubPart(){
		// TODO: only remove by ID (not whatever happens to be active)
		removeSubPartAtIndex(activeSubPart);
		BackendGlobal.modelEventHandler.notifyElementRemoved();
	}
	
	/**
	 * Get a sketch by its unique ID.
	 * @param id the ID of the sketch to retreive
	 * @return the sketch, if it exists, or null otherwise.
	 */
	public Sketch getSketchByID(int id){
		for(SubPart subPart : subPartList){
			Sketch sketch = subPart.getSketch();
			if(sketch != null && sketch.getUniqueID() == id){
				return sketch;
			}
		}
		System.out.println("Part(getSketchByID): there was no Sketch at that ID! ID=" + id + ", FIX THIS NOW!");
		return null;
	}
	
	/**
	 * Get a Feature2D3D by its unique ID.
	 * @param id the ID of the Feature2D3D to retreive
	 * @return the Feature2D3D, if it exists, or null otherwise.
	 */
	public Build getFeat2D3DByID(int id){
		for(SubPart subPart : subPartList){
			Build feat2D3D = subPart.getBuild();
			if(feat2D3D != null && feat2D3D.ID == id){
				return feat2D3D;
			}
		}
		System.out.println("Part(getFeat2D3DByID): there was no Feat2D3D at that ID! ID=" + id + ", FIX THIS NOW!");
		return null;
	}
	
	/**
	 * Get a Feature3D3D by its unique ID.
	 * @param id the ID of the Feature3D3D to retreive
	 * @return the Feature3D3D, if it exists, or null otherwise.
	 */
	public Modify getFeat3D3DByID(int id){
		for(SubPart subPart : subPartList){
			Modify feat3D3D = subPart.getModify();
			if(feat3D3D != null && feat3D3D.ID == id){
				return feat3D3D;
			}
		}
		System.out.println("Part(getFeat3D3DByID): there was no Feat3D3D at that ID! ID=" + id + ", FIX THIS NOW!");
		return null;
	}
	
}
