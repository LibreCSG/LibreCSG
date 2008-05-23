package net.sf.avocado_cad.model.api;

import net.sf.avocado_cad.model.api.sketch.IFeature2D;

public interface IProject {

	/**
	 * add a group to the end of the list of groups
	 * @param group non-null Group to be added
	 * @return the index of the newly added group, or -1 if group was null.
	 */
	public int addNewGroup();

	/**
	 * get the group at a given index
	 * @param i index
	 * @return the Group, or null if index was invalid.
	 */
	public IGroup getAtIndex(int i);

	/**
	 * @return the size() of the list of groups.
	 */
	public int getGroupListSize();

	/**
	 * set the index of the group that should be set to Active.
	 * @param i index
	 */
	public void setActiveGroup(int i);

	/**
	 * set the active group to none
	 */
	public void setActiveToNone();

	/**
	 * get the currently active group
	 * @return the active group, or null if no group is active
	 */
	public IGroup getActiveGroup();

	/**
	 * try to get the active part.
	 * @return the active part, or null if not found
	 */
	public IPart getActivePart();

	/**
	 * try to get the active SubPart.
	 * @return the active SubPart, or null if not found
	 */
	public ISubPart getActiveSubPart();

	/**
	 * get the active Sketch if it exists.
	 * @return
	 */
	public ISketch getActiveSketch();

	/**
	 * get the active Feature2D3D if it exists.
	 * @return
	 */
	public IBuild getActiveFeat2D3D();

	/**
	 * get the active Feature3D3D if it exists.
	 * @return
	 */
	public IModify getActiveFeat3D3D();

	/**
	 * try to get the active Feature2D.
	 * @return the active Feature2D, or null if not found.
	 */
	public IFeature2D getActiveFeat2D();

	/**
	 * Remove the Group at the index if present.
	 * @param i index
	 */
	public void removeGroupAtIndex(int i);

	/**
	 * remove the active Group from the list.
	 */
	public void removeActiveGroup();

	public IGroup getGroupByUniqueID(int uniqueID);

}
