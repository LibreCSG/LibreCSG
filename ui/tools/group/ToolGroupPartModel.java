package ui.tools.group;

import ui.tools.ToolModelGroup;
import backend.adt.ParamSet;

public class ToolGroupPartModel implements ToolModelGroup{

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
