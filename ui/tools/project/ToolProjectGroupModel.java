package ui.tools.project;

import backend.adt.ParamSet;
import ui.tools.ToolModelProject;

public class ToolProjectGroupModel implements ToolModelProject{

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
