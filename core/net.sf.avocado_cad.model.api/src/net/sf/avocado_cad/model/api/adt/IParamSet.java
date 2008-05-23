package net.sf.avocado_cad.model.api.adt;

import java.util.Map;

public interface IParamSet {

	/**
	 * Get a parameter from the set by name.
	 * @param name
	 * @return
	 * @throws ParamNotFoundException
	 */
	public IParam getParam(String name) throws ParamNotFoundException;

	/**
	 * Check to see if parameter set contains a parameter
	 * with the given name and type.
	 * @param name
	 * @param type
	 * @return
	 */
	public boolean hasParam(String name, ParamType type);

	
	public Map<String, ? extends IParam> getAllParams();

	public String getLabel();

	/**
	 * @deprecated will be removed - IParamSet should be a read-only interface  
	 * change a parameter's value.  The type
	 * of the parameter must be the same as it
	 * was originally defined to be when constructed
	 * or the operation will be aborted and no 
	 * change will be made.
	 * @param name
	 * @param p
	 * @throws ParamNotFoundException
	 */
	@Deprecated
	public void changeParam(String name, Object data)
			throws ParamNotFoundException;
}
