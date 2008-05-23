package net.sf.avocado_cad.model.api.adt;


public interface IParam {

	/**
	 * get the parameter type
	 * @return
	 */
	public ParamType getType();

	/**
	 * get the parameter's label. 
	 * This should be human-readable.
	 * @return
	 */
	public String getLabel();

	public IPoint2D getDataPoint2D() throws ParamNotCorrectTypeException;

	public IPoint3D getDataPoint3D() throws ParamNotCorrectTypeException;

	public IRotation3D getDataRotation3D() throws ParamNotCorrectTypeException;

	public Boolean getDataBoolean() throws ParamNotCorrectTypeException;

	public Integer getDataInteger() throws ParamNotCorrectTypeException;

	public Double getDataDouble() throws ParamNotCorrectTypeException;

	public String getDataString() throws ParamNotCorrectTypeException;

	public ISelectionList getDataSelectionList() throws ParamNotCorrectTypeException;

	/**
	 * change the data associated with a parameter.
	 * note that data must be of the same type in
	 * which the parameter was originally constructed.
	 * @param data
	 */
	public void change(Object data);

	/**
	 * test to see if the parameter is derived from
	 * others and therefore should not be altered
	 * directly by the user.
	 * @return
	 */
	public boolean isDerivedParam();

}
