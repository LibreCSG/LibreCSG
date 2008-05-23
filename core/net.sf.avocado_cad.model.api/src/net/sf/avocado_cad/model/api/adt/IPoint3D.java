package net.sf.avocado_cad.model.api.adt;


public interface IPoint3D {

	/**
	 * returns <code>true</code> if the current
	 * point is equal in value to the point
	 * given as an argument to the method.
	 * @param p2
	 * @return
	 */
	public boolean equalsPt(IPoint3D p2);

	/**
	 * get the X value
	 * @return
	 */
	public double getX();

	/**
	 * get the Y value
	 * @return
	 */
	public double getY();

	/**
	 * get the Z value
	 * @return
	 */
	public double getZ();

}
