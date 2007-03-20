package ui.tools.modify;

import backend.adt.ParamSet;
import ui.tools.ToolModelModify;

public class ToolModifyDoneModel implements ToolModelModify {

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
