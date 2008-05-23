package net.sf.avocado_cad.model.api;

import java.util.Collection;

import net.sf.avocado_cad.model.api.csg.ICSGSolid;

public interface IPart {

	public Collection<? extends ISubPart> getSubParts();

	public ICSGSolid getSolid();
	
	public IPartMaterial getPartMaterial();
	
}
