package net.sf.avocado_cad.model.api.csg;

import java.util.Collection;
import java.util.List;

public interface ICSGFace {

	public Collection<? extends ICSGPolygon> getPolygons();
	
	public List<? extends ICSGVertex> getMatingEdgeLines();
	
	public boolean isSelected();
}
