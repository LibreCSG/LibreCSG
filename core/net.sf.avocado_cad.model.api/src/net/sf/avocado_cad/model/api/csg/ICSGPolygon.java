package net.sf.avocado_cad.model.api.csg;

import java.util.List;

public interface ICSGPolygon {

	public List<? extends ICSGVertex> getVertices();

	public ICSGVertex getBarycenterVertex();

	public ICSGPlane getPlane();
	
	public POLY_TYPE getType();
}
