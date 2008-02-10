package backend.model.material;

import javax.media.opengl.GL;

import backend.global.AvoGlobal;

public class PartMaterial {

	double r = 0.5;
	double g = 0.5;
	double b = 0.5;
	double a = 0.5;
	
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
		if(AvoGlobal.glView.OPENGL_FRAGMENT_SHADER_GLSL_ENABLED){
			// TODO: setup material in GLSL
		}else{
			if(AvoGlobal.glView.OPENGL_FRAGMENT_PROGRAM_ARB_ENABLED){
				// TODO: setup material in ARB 
				gl.glEnable(GL.GL_FRAGMENT_PROGRAM_ARB);
				if(arbFragProgID == -1){
				//	int[] shaderArray = {0};
				//	gl.glGenProgramsARB(1, shaderArray, 0);
				//	arbFragProgID = shaderArray[0];

					arbFragProgID = gl.glCreateProgramObjectARB();
					System.out.println("# " + arbFragProgID);
					
					//int fragProg = gl.glCreateProgramObjectARB();
					gl.glBindProgramARB(GL.GL_FRAGMENT_PROGRAM_ARB, arbFragProgID);
					
					//
					// super simple Fragment Shader.  
					// just place RGB based on XYZ of texture coordinate. :)
					//
					String fragmentAsmString = "!!ARBfp1.0\n\n" + 
						"OUTPUT out_color = result.color; \n" +
						"TEMP t0, t1, t2, t3; \n" +
						"ATTRIB texcoord = fragment.texcoord[0]; \n" +
						"MOV out_color, fragment.color; \n" + 
						"MOV out_color.r, texcoord.x; \n" +
						"MOV out_color.g, texcoord.y; \n" +
						"MOV out_color.b, texcoord.z; \n" +
						"END";		
					gl.glProgramStringARB(GL.GL_FRAGMENT_PROGRAM_ARB, GL.GL_PROGRAM_FORMAT_ASCII_ARB, fragmentAsmString.length(), fragmentAsmString);
				}
				gl.glUseProgramObjectARB(arbFragProgID);
				//gl.glDisable(GL.GL_FRAGMENT_PROGRAM_ARB);
			}else{
				// just use r,g,b,a for material.. boo. :(
				gl.glColor4d(r, g, b, a);
			}
		}		
	}
	
	/**
	 * dispose of any resources that may have been claimed from the loading of the material.
	 * This should be called after the drawing of the part is complete.
	 * @param gl
	 */
	public void disposeMaterial(GL gl){
		if(AvoGlobal.glView.OPENGL_FRAGMENT_SHADER_GLSL_ENABLED){
			// TODO: dispose material in GLSL
		}else{
			if(AvoGlobal.glView.OPENGL_FRAGMENT_PROGRAM_ARB_ENABLED){
				// TODO: dispose material in ARB 
				gl.glDisable(GL.GL_FRAGMENT_PROGRAM_ARB);
			}else{
				// dispose r,g,b,a for material.. (no action really needed here)
				gl.glColor4d(0.5, 0.5, 0.5, 1.0);
			}
		}	
	}
	
	
	
}
