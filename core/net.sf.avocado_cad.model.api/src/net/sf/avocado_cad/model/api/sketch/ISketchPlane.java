package net.sf.avocado_cad.model.api.sketch;

import net.sf.avocado_cad.model.api.csg.ICSGVertex;

public interface ISketchPlane {

	/**
	 * @return the CSG_Vertex of the plane's origin
	 */
	public ICSGVertex getOrigin();

	/**
	 * @return the CSG_Vertex representing the normal of the plane
	 */
	public ICSGVertex getNormal();

	/**
	 * rotation about X axis, in radians.
	 * @return
	 */
	public double getRotationX();

	/**
	 * rotation about Y axis, in radians.
	 * @return
	 */
	public double getRotationY();

	/**
	 * rotation about Z axis in radians.
	 */
	public double getRotationZ();

	public ICSGVertex getXAxis();

	public ICSGVertex getYAxis();

}
