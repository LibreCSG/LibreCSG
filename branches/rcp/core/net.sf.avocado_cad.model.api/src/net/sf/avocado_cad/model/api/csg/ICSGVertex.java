package net.sf.avocado_cad.model.api.csg;

import net.sf.avocado_cad.model.api.adt.IRotation3D;
import net.sf.avocado_cad.model.api.adt.ITranslation3D;


public interface ICSGVertex {

	public double getX();

	public double getY();

	public double getZ();

	public double[] getXYZ();

	public double getDistFromOrigin();

	public double getDistBetweenVertices(ICSGVertex vertB);

	/**
	 * computes the cross-product of the two vertices.
	 * @param vertB the given vector to compute (this)x(vertB)
	 * @return the CSG_Vextor representing the cross-product
	 *   of this and vertB. 
	 */
	public ICSGVertex getCrossProduct(ICSGVertex vertB);

	public double getDotProduct(ICSGVertex vertB);

	public ICSGVertex getFlippedVertex();

	/**
	 * apply rotation in X, Y, Z order, then translation.
	 * @param translation 3D translation
	 * @param rotation 3D rotation
	 * @return a copy of the vertex with the translation/rotation applied.
	 */
	public ICSGVertex getTranslatedRotatedCopy(ITranslation3D translation, IRotation3D rotation);

	public boolean equalsVertex(ICSGVertex v2);

	public ICSGVertex getScaledCopy(double scale);

	
	public ICSGVertex addToVertex(ICSGVertex scaledCopy);

}
