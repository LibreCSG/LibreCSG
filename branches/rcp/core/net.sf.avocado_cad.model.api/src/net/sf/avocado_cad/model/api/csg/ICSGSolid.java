package net.sf.avocado_cad.model.api.csg;

import java.util.Collection;

public interface ICSGSolid {

	public Collection<? extends ICSGFace> getFaces();

	public ICSGBounds getBounds();

	/**
	 * check to make sure solid is valid. (all faces vaild and solid is water-tight)
	 * @return true if solid is valid
	 */
	public boolean isValidSolid();

}
