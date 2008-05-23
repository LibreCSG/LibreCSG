package net.sf.avocado_cad.model.api.sketch;

import net.sf.avocado_cad.model.api.adt.IPoint2D;

public interface IPrim2DArc extends IPrim2D {

	public IPoint2D getArcCenterPoint();

	public double getArcRadius();

	public double getStartAngle();

	public double getArcAngle();

}
