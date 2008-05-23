package net.sf.avocado_cad.model.api.sketch;

import java.util.List;

import net.sf.avocado_cad.model.api.adt.IParamSet;

public interface IFeature2D {

	IParamSet getParamSet();

	public boolean isSelected();

	public List<? extends IPrim2D> getPrim2DList();
	
	
	// these setters will be removed from the interface.
	@Deprecated
	public void setPrim2DList(IPrim2DList prim2DList);
	
	@Deprecated
	public void setSelected(boolean selected);
}
