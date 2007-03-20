package ui.tools.share;

import backend.adt.ParamSet;
import ui.tools.ToolModelShare;

public class ToolShareDoneModel implements ToolModelShare{

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
