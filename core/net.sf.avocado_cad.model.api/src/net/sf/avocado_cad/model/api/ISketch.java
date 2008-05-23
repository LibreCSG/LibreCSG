package net.sf.avocado_cad.model.api;

import java.util.Collection;
import java.util.Iterator;

import net.sf.avocado_cad.model.api.sketch.IFeature2D;
import net.sf.avocado_cad.model.api.sketch.IRegion2D;
import net.sf.avocado_cad.model.api.sketch.ISketchPlane;


public interface ISketch {

	/**
	 * @return the actual plane on which to sketch.
	 */
	public ISketchPlane getSketchPlane();

	public IPart getParentPart();

	public IBuild getBuild();

	public boolean isConsumed();

	public Collection<? extends IFeature2D> getFeature2Ds();

	public Collection<? extends IRegion2D> getRegion2Ds();
}
