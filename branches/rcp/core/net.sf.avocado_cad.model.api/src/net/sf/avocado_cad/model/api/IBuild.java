package net.sf.avocado_cad.model.api;

import net.sf.avocado_cad.model.api.adt.IParamSet;

public interface IBuild {

	IParamSet getParamSet();

	ISketch getPrimarySketch();

	public int getID();
}
