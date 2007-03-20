package ui.tools.group;

import backend.adt.ParamSet;
import ui.tools.ToolModelGroup;

public class ToolGroupDoneModel implements ToolModelGroup{

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
