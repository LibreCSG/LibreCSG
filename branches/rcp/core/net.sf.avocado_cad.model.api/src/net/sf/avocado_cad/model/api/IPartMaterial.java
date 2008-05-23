package net.sf.avocado_cad.model.api;

public interface IPartMaterial {

	public double getR();

	public double getG();

	public double getB();

	public double getA();

	// The following methods are deprecated and will be removed.
	@Deprecated
	public void setR(double r);

	@Deprecated
	public void setG(double g);

	@Deprecated
	public void setB(double b);

	@Deprecated
	public void setA(double a);

}
