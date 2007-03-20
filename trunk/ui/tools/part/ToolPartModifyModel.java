package ui.tools.part;

import backend.adt.ParamSet;
import ui.tools.ToolModelPart;

public class ToolPartModifyModel implements ToolModelPart{

	public ParamSet constructNewParamSet() {
		return null;
	}

	public void finalize(ParamSet paramSet) {
	}

	public boolean paramSetIsValid(ParamSet paramSet) {
		return false;
	}

	public void updateDerivedParams(ParamSet paramSet) {
	}

}
