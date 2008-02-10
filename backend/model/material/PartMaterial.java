package backend.model.material;

import javax.media.opengl.GL;

public class PartMaterial {

	double r = 0.5;
	double g = 0.5;
	double b = 0.5;
	double a = 0.5;
	
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
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	/**
	 * setup the GL drawing configuration to correctly apply material in subsequent operations.
	 * @param gl
	 */
	public void loadMaterial(GL gl){
		gl.glColor4d(r, g, b, a);
	}
	
	/**
	 * dispose of any resources that may have been claimed from the loading of the material.
	 * This should be called after the drawing of the part is complete.
	 * @param gl
	 */
	public void disposeMaterial(GL gl){
		// TODO; 
	}
	
	
	
}
