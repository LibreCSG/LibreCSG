package backend.adt;

public class Rotation3D {
	protected double x = 0.0;
	protected double y = 0.0;
	protected double z = 0.0;
	
	/**
	 * Abstract Data Type: Rotation 3D
	 * rotate about the x, y, and z axis
	 * in X->Y->Z order.  angles are given
	 * in degrees.
	 * @param x
	 * @param y
	 * @param z
	 */
	public Rotation3D(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	/**
	 * get the X rotation value
	 * @return
	 */
	public double getXRot(){
		return x;
	}
	
	/**
	 * get the Y rotation value
	 * @return
	 */
	public double getYRot(){
		return y;
	}
	
	/**
	 * get the Z rotation value
	 * @return
	 */
	public double getZRot(){
		return z;
	}
	
	/**
	 * set the X rotation value
	 * @param newX
	 */
	public void setXRot(double newX){
		x = newX;
	}

	/**
	 * set the Y rotation value
	 * @param newY
	 */
	public void setYRot(double newY){
		y = newY;
	}
	
	/**
	 * set the Z rotation value
	 * @param newY
	 */
	public void setZRot(double newZ){
		z = newZ;
	}
	
	
}
