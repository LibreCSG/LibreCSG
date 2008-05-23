package backend.model.material;

import net.sf.avocado_cad.model.api.IPartMaterial;



public class PartMaterial implements IPartMaterial {

	public double r = 0.5;
	public double g = 0.5;
	public double b = 0.5;
	public double a = 0.5;
	
	private int arbFragProgID = -1;
	
	// TODO: Until openGL implementation catch up to openGL2.0, I suggest we have 3 rendering levels:
	// TODO: Render Level 1: r,g,b,a
	// TODO: Render Level 2: ARB (assembly instruction) fragment program
	// TODO: Render Level 3: openGL 2.0 GLSL perlin noise based procedural material shader.
	
	/**
	 * The material out of which the part is made.  This should be 
	 * dynamic enough to cover a full spectrum of real and creative 
	 * material types. (wood, metal, glass, lava, zebra, leopard, etc.)
	 */
	public PartMaterial(double r, double g, double b, double a){
		this.r = Math.min(Math.max(0.0, r), 1.0);
		this.g = Math.min(Math.max(0.0, g), 1.0);
		this.b = Math.min(Math.max(0.0, b), 1.0);
		this.a = Math.min(Math.max(0.0, a), 1.0);
	}
	
	public double getR(){
		return r;
	}
	
	public double getG(){
		return g;
	}
	
	public double getB(){
		return b;
	}
	
	public double getA(){
		return a;
	}
	
	public void setR(double r){
		this.r = Math.min(Math.max(0.0, r), 1.0);
	}
	
	public void setG(double g){
		this.g = Math.min(Math.max(0.0, g), 1.0);
	}
	
	public void setB(double b){
		this.b = Math.min(Math.max(0.0, b), 1.0);
	}
	
	public void setA(double a){
		this.a = Math.min(Math.max(0.0, a), 1.0);
	}
	
	
	
	
	
}
