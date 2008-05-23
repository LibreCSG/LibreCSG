package net.sf.avocado_cad.model.api.event;


public class BackendGlobal {

	// TODO: perhaps move event handler just into their own static selves instead of AvoGlobal?
	public static ParamEventHandler  paramEventHandler  = new ParamEventHandler();
	public static ModelEventHandler  modelEventHandler  = new ModelEventHandler();

}
