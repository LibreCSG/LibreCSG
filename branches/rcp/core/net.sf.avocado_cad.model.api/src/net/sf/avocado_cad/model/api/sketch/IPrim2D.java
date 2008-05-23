package net.sf.avocado_cad.model.api.sketch;

import java.util.LinkedList;

import net.sf.avocado_cad.model.api.adt.IPoint2D;

public interface IPrim2D {

	/**
	 * return the point at which "this" prim2D intersects
	 * the given prim2D at any point other than the givn prim2D's end points.  
	 * @param anyPrim2D the given prim2D to check for intersection
	 * @return the point of intersection, or null if no intersection occurs.
	 */
	public abstract IPoint2D intersect(IPrim2D anyPrim2D);

	/**
	 * if line and this prim2D intersect past Geometry2D.epsilon inward
	 * from the line's ends, the point of intersection (on the line)
	 * should be returned.. otherwise, return null.
	 * @param ln the prim2DLine to be checked
	 * @return the point of intersection, or NULL if no intersection.
	 */
	public abstract IPoint2D intersectsLine(IPrim2DLine ln);

	/**
	 * if arc and this prim2D intersect past Geometry2D.epsilon inward
	 * from the arc's ends, the point of intersection (on the arc)
	 * should be returned.. otherwise, return null.
	 * @param arc the prim2DArc to be checked
	 * @return the point of intersection, or NULL if no intersection.
	 */
	public abstract IPoint2D intersectsArc(IPrim2DArc arc);

	/**
	 * the distance from the specified point to the closest
	 * point on this prim2D.
	 * @param pt the point to check for distance from the prim2D
	 * @return the distance
	 */
	public abstract double distFromPrim(IPoint2D pt);

	/**
	 * The resulting pair of prim2D when this prim2D is 
	 * split at the specified point.  If the point is determined
	 * to be eroneous or the prim2D can not be split, NULL may
	 * be returned.  Given a "good" split, this should never happen though.
	 * @param pt the point to split the prim2D
	 * @return a pair of prim2Ds
	 */
	public abstract IPrimPair2D splitPrimAtPoint(IPoint2D pt);

	/**
	 * the length of the prim2D is the "string" length
	 * if you were to follow the curve. (line integral)
	 * @return the length
	 */
	public abstract double getPrimLength();

	/**
	 * Get a list of vertices that define the curve/line.
	 * simple prim2D (i.e., the line) may only return the 
	 * two end points.  More complex prim2D should contain a 
	 * list of vertices that define the shape.<br/>
	 * (vertices ordered always <b>from ptA to ptB</b>).
	 * @param maxVerts maximum number of vertices to use
	 * @return the list of vertices that define the prim2D.
	 */
	public abstract LinkedList<? extends IPoint2D> getVertexList(int maxVerts);

	/**
	 * get a new Prim2D that has its endpoints swapped
	 * @return
	 */
	public abstract IPrim2D getSwappedEndPtPrim2D();

	/**
	 * @return the center point along the prim2D. Useful for
	 * testing concavity and CW/CCW lines.
	 */
	public abstract IPoint2D getCenterPtAlongPrim();

	/**
	 * Test to see if the Prim2D ends at the given point.
	 * If it does, return the <em>other</em> end point.
	 * If not, returns null.
	 * @param ptC non-null point to test.
	 * @return the other end point, or null if given point is not an end point.
	 */
	public IPoint2D hasPtGetOther(IPoint2D ptC);

	public IPoint2D getPtA();

	public IPoint2D getPtB();

	public boolean isSelected();

}
