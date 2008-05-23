package net.sf.avocado_cad.model.api.adt;





public interface IPoint2D {

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
	 * compute the distance from the 
	 * instance of this point to another
	 * point.
	 * @param p2
	 * @return
	 */
	public double computeDist(IPoint2D p2);

	/**
	 * returns <code>true</code> if the current
	 * point is equal in value to the point
	 * given as an argument to the method.
	 * @param p2
	 * @return
	 */
	public boolean equalsPt(IPoint2D p2);

	/**
	 * return the difference between the current
	 * point and another point. 
	 * result = current - otherPoint
	 * @param p2
	 * @return
	 */
	public IPoint2D subPt(IPoint2D p2);

	/**
	 * return a new point that is the current
	 * point multiplied by a scalar.
	 * @param scaleFactor
	 * @return
	 */
	public IPoint2D getScaledPt(double scaleFactor);

	/**
	 * return a new point that is the sum
	 * of the current point and another point.
	 * @param p2
	 * @return
	 */
	public IPoint2D addPt(IPoint2D p2);

}
