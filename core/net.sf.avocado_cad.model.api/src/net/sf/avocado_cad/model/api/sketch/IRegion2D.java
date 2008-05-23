package net.sf.avocado_cad.model.api.sketch;

import net.sf.avocado_cad.model.api.csg.ICSGFace;

public interface IRegion2D {

	public boolean isSelected();

	public boolean isMousedOver();

	public ICSGFace getCSGFace();
}
