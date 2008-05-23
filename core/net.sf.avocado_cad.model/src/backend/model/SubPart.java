package backend.model;

import net.sf.avocado_cad.model.api.ISubPart;

public interface SubPart extends ISubPart {

	Sketch getSketch();
	
	Build getBuild();
	
	Modify getModify();
}
