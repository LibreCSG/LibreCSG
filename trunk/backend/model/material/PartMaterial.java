package backend.model.material;

import javax.media.opengl.GL;

public class PartMaterial {

	double r = 0.5;
	double g = 0.5;
	double b = 0.5;
	double a = 0.5;
	
	public PartMaterial(double r, double g, double b, double a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public void loadMaterial(GL gl){
		gl.glColor4d(r, g, b, a);
	}
	
	public void disposeMaterial(GL gl){
		
	}
	
	
	
}
