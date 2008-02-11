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
			gl.glColor4d(r, g, b, a);
		}else{
			if(AvoGlobal.glView.OPENGL_FRAGMENT_PROGRAM_ARB_ENABLED){
				// TODO: setup material in ARB 
				gl.glColor4d(r, g, b, a);
				
				/*
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
						"TEMP t0, t1, t2, t3, t4, t5, t6, tempColor; \n" +
						"ATTRIB texcoord = fragment.texcoord[0]; \n" +
						//"ATTRIB in_color = fragment.color; \n" +
						"MOV tempColor, fragment.color; \n" + 
						//"MOV out_color, fragment.color; \n" + 	// start with original fragment color. 
						//"MOV out_color.r, texcoord.x; \n" +
						//"MOV out_color.g, texcoord.y; \n" +
						//"MOV out_color.b, texcoord.z; \n" +
						//"MUL t0, texcoord.x, 0.2; \n" + 
						//"MUL t1, texcoord.y, 0.25; \n" +
						"MUL t0, texcoord.x, 0.37; \n" +
						"FRC t0, t0; \n" +
						"SUB t0, t0, 0.5; \n" +
						"ABS t0, t0; \n" +
						"MUL t0, t0, 2.0; \n"  +
						"MUL t1, t0, 0.4; \n"  +  // multiplier for Red
						"ADD t1, t1, -0.1; \n" +  // offset for Red
						"ADD tempColor.r, tempColor.r, t1; \n" + 
						"MUL t1, t0, 0.1; \n"  +  // multiplier for Green
						"ADD t1, t1, -0.1; \n" +  // offset for Green
						"ADD tempColor.g, tempColor.g, t1; \n" + 
						"MUL t1, t0, 0.1; \n"  +  // multiplier for Blue
						"ADD t1, t1, -0.1; \n" +  // offset for Blue
						"ADD tempColor.b, tempColor.b, t1; \n" + 
						
						"MUL t0, texcoord.y, 1.59; \n" +
						"FRC t0, t0; \n" +
						"SUB t0, t0, 0.5; \n" +
						"ABS t0, t0; \n" +
						"MUL t0, t0, 2.0; \n"  +
						"MUL t1, t0, 0.3; \n"  +  // multiplier for Red
						"ADD t1, t1, -0.2; \n" +  // offset for Red
						"ADD tempColor.r, tempColor.r, t1; \n" + 
						"MUL t1, t0, 0.2; \n"  +  // multiplier for Green
						"ADD t1, t1, -0.1; \n" +  // offset for Green
						"ADD tempColor.g, tempColor.g, t1; \n" + 
						"MUL t1, t0, 0.2; \n"  +  // multiplier for Blue
						"ADD t1, t1, -0.1; \n" +  // offset for Blue
						"ADD tempColor.b, tempColor.b, t1; \n" +
						
						"MUL t0, texcoord.z, 2.31; \n" +
						"FRC t0, t0; \n" +
						"SUB t0, t0, 0.5; \n" +
						"ABS t0, t0; \n" +
						"MUL t0, t0, 2.0; \n"  +
						"MUL t1, t0, 0.2; \n"  +  // multiplier for Red
						"ADD t1, t1, -0.1; \n" +  // offset for Red
						"ADD tempColor.r, tempColor.r, t1; \n" + 
						"MUL t1, t0, 0.2; \n"  +  // multiplier for Green
						"ADD t1, t1, -0.1; \n" +  // offset for Green
						"ADD tempColor.g, tempColor.g, t1; \n" + 
						"MUL t1, t0, 0.1; \n"  +  // multiplier for Blue
						"ADD t1, t1, 0.1; \n" +  // offset for Blue
						"ADD tempColor.b, tempColor.b, t1; \n" +						

						//"FLR t3, texcoord.x; \n" +
						//"ADD t3, t3, 0.1117; \n" +
						//"ABS t3, t3; \n" +
						//"RSQ t3, t3; \n" +
						//"MUL t3, t3, 10.0; \n" +
						
						//"MUL t3, t3, 1103515245; \n" +
						//"ADD t3, t3, 12345; \n" +
						//"MUL t3, t3, 0.000015; \n" +
						//"MUL t3, t3, 0.000031; \n" + 
						//"MUL t3, t3, 0.1; \n" +
												
						//"FRC tempColor.r, t3; \n" + 
						
						
						"MOV out_color, tempColor; \n" +
						
						//"COS t0, t0; \n" +
						//"COS t3, t1; \n" +
						//"ADD t0, t2, 1.0; \n" +
						//"ADD t1, t3, 1.0; \n" +
						//"FRC out_color.b, t1; \n" +
						//vertex.y = ( sin(wave + (vertex.x / 5.0) ) + sin(wave + (vertex.z / 4.0) ) ) * 2.5;
						
						"END";		
					gl.glProgramStringARB(GL.GL_FRAGMENT_PROGRAM_ARB, GL.GL_PROGRAM_FORMAT_ASCII_ARB, fragmentAsmString.length(), fragmentAsmString);
				}
				gl.glUseProgramObjectARB(arbFragProgID);
				//gl.glDisable(GL.GL_FRAGMENT_PROGRAM_ARB);
				*/
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
