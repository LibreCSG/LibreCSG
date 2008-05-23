package ui.opengl;

import java.util.Iterator;

import javax.media.opengl.GL;

import net.sf.avocado_cad.eclipse.ui.AvoGlobal;
import net.sf.avocado_cad.model.api.IPart;
import net.sf.avocado_cad.model.api.IPartMaterial;
import net.sf.avocado_cad.model.api.adt.IPoint2D;
import net.sf.avocado_cad.model.api.csg.ICSGFace;
import net.sf.avocado_cad.model.api.csg.ICSGPlane;
import net.sf.avocado_cad.model.api.csg.ICSGPolygon;
import net.sf.avocado_cad.model.api.csg.ICSGRay;
import net.sf.avocado_cad.model.api.csg.ICSGSegment;
import net.sf.avocado_cad.model.api.csg.ICSGSolid;
import net.sf.avocado_cad.model.api.csg.ICSGVertex;
import net.sf.avocado_cad.model.api.csg.POLY_TYPE;
import net.sf.avocado_cad.model.api.sketch.IPrim2D;
import net.sf.avocado_cad.model.api.sketch.IPrim2DArc;
import net.sf.avocado_cad.model.api.sketch.IPrim2DLine;
import net.sf.avocado_cad.model.api.sketch.IRegion2D;
import net.sf.avocado_cad.model.api.sketch.ISketchPlane;
import ui.utilities.AvoColors;

public class GLDraw {

	public static void glDraw(GL gl, IPrim2DLine line) {
		if(line.isSelected()){
			float[] color = AvoColors.GL_COLOR4_2D_ACTIVE;
			gl.glColor4f(color[0], color[1], color[2], color[3]);
		}else{
			float[] color = AvoColors.GL_COLOR4_2D_NONACT;
			gl.glColor4f(color[0], color[1], color[2], color[3]);
		}
		GLDynPrim.line2D(gl, line.getPtA(), line.getPtB(), 0.0);		
	}

	/**
	 * draw the region. the state of selection, mouseover, 
	 * etc. will be handled automatically.
	 * @param gl
	 */
	public static void glDrawRegion(GL gl, IRegion2D region){
		if(region.isSelected()){
			// selected
			if(region.isMousedOver()){
				float[] color = AvoColors.GL_COLOR4_2D_REG_SELMO;
				gl.glColor4f(color[0], color[1], color[2], color[3]);
			}else{
				float[] color = AvoColors.GL_COLOR4_2D_REG_SEL;
				gl.glColor4f(color[0], color[1], color[2], color[3]);
			}
		}else{
			// not selected
			if(region.isMousedOver()){
				float[] color = AvoColors.GL_COLOR4_2D_REG_UNSELMO;
				gl.glColor4f(color[0], color[1], color[2], color[3]);
			}else{
				float[] color = AvoColors.GL_COLOR4_2D_REG_UNSEL;
				gl.glColor4f(color[0], color[1], color[2], color[3]);
			}
		}
		if(region.getCSGFace() != null){
			for(ICSGPolygon poly : region.getCSGFace().getPolygons()) {
				glDrawPolygon(gl, poly);
			}
		}		
	}
	
	public static void glDrawSolid(GL gl, IPart part){
		loadMaterial(gl,part.getPartMaterial());
		glDrawSolid(gl, part.getSolid());
		disposeMaterial(gl,part.getPartMaterial());
		
	}
	
	public static void glDrawSelectedElements(GL gl, IPart part){
		gl.glColor4d(0.5, 0.5, 0.9, 0.5);
		glDrawSelectedElements(gl, part.getSolid());		
	}
	
	public static void glDrawImportantEdges(GL gl, IPart part){
		gl.glColor4d(0.5, 0.5, 0.5, 0.5);
		glDrawImportantEdges(gl, part.getSolid());
	}


	public static void glDrawSolid(GL gl, ICSGSolid solid){
		for(ICSGFace f : solid.getFaces()) {
			glDrawFace(gl, f);
		//	f.drawFaceForDebug(gl);
		//	f.drawFaceLinesForDebug(gl);
		//	f.drawFaceNormalsForDebug(gl);
		}	
	}
	
	public static void glDrawWireframe(GL gl, ICSGSolid solid){
		for(ICSGFace f : solid.getFaces()) {
			drawFaceWireframe(gl,f);
		}
	}

	public static void glDrawImportantEdges(GL gl, ICSGSolid solid){
		for(ICSGFace f : solid.getFaces()) {
			glDrawImportantEdges(gl,f);
		}	
	}
	public static void glDrawSelectedElements(GL gl, ICSGSolid solid){
		for(ICSGFace f : solid.getFaces()) {
			if(f.isSelected()){
				glDrawFace( gl, f);
			}
		}	
	}
	

	/**
	 * setup the GL drawing configuration to correctly apply material in subsequent operations.
	 * @param gl
	 */
	public static void loadMaterial(GL gl, IPartMaterial material){
		if(GLGlobal.glView.OPENGL_FRAGMENT_SHADER_GLSL_ENABLED){
			// TODO: setup material in GLSL
			gl.glColor4d(material.getR(), material.getG(), material.getB(), material.getA());
		}else{
			if(GLGlobal.glView.OPENGL_FRAGMENT_PROGRAM_ARB_ENABLED){
				// TODO: setup material in ARB 
				gl.glColor4d(material.getR(), material.getG(), material.getB(), material.getA());
				
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
				gl.glColor4d(material.getR(), material.getG(), material.getB(), material.getA());
			}
		}		
	}
	
	/**
	 * dispose of any resources that may have been claimed from the loading of the material.
	 * This should be called after the drawing of the part is complete.
	 * @param gl
	 */
	public static void disposeMaterial(GL gl, IPartMaterial material){
		if(GLGlobal.glView.OPENGL_FRAGMENT_SHADER_GLSL_ENABLED){
			// TODO: dispose material in GLSL
		}else{
			if(GLGlobal.glView.OPENGL_FRAGMENT_PROGRAM_ARB_ENABLED){
				// TODO: dispose material in ARB 
				gl.glDisable(GL.GL_FRAGMENT_PROGRAM_ARB);
			}else{
				// dispose r,g,b,a for material.. (no action really needed here)
				gl.glColor4d(0.5, 0.5, 0.5, 1.0);
			}
		}	
	}
	
	
	private static void setGLPolyColor(GL gl, POLY_TYPE type) {
		switch(type) {
		case POLY_INSIDE:
			gl.glColor3d(0.5, 0.2, 0.2);    // red
			break;
		case POLY_OUTSIDE:
			gl.glColor3d(0.2, 0.5, 0.2); 	// green
			break;
		case POLY_OPPOSITE:
			gl.glColor3d(0.2, 0.2, 0.5);	// blue
			break;
		case POLY_SAME:
			gl.glColor3d(0.5, 0.2, 0.5);	// purple
			break;
		case POLY_UNKNOWN:
			gl.glColor3d(0.25, 0.25, 0.25);	// gray
			break;
		}
	}
	public static void drawFaceForDebug(GL gl, ICSGFace face){		
		for(ICSGPolygon poly : face.getPolygons()){
			setGLPolyColor(gl,poly.getType());
			for(ICSGVertex v : poly.getVertices()) {
				gl.glVertex3dv(v.getXYZ(), 0);
			}
			gl.glEnd();
		}
	}
	
	public static  void drawFaceWireframe(GL gl, ICSGFace face){
		for(ICSGPolygon poly : face.getPolygons()){	
			gl.glBegin(GL.GL_LINE_LOOP);
			for(ICSGVertex v : poly.getVertices()) {
				gl.glVertex3dv(v.getXYZ(), 0);
			}
			gl.glEnd();
		}
	}
	
	public static void drawFaceLinesForDebug(GL gl, ICSGFace face){
		for(ICSGPolygon poly : face.getPolygons()){		
			setGLPolyColor(gl,poly.getType());
//			if(poly.type == ICSGPolygon.POLY_TYPE.POLY_INSIDE){ 	// red
//				gl.glColor3d(0.5, 0.2, 0.2);
//			}
//			if(poly.type == ICSGPolygon.POLY_TYPE.POLY_OUTSIDE){ 	// green
//				gl.glColor3d(0.2, 0.5, 0.2);
//			}
//			if(poly.type == ICSGPolygon.POLY_TYPE.POLY_OPPOSITE){	// blue
//				gl.glColor3d(0.2, 0.2, 0.5);
//			}
//			if(poly.type == ICSGPolygon.POLY_TYPE.POLY_SAME){		// purple
//				gl.glColor3d(0.5, 0.2, 0.5);
//			}
//			if(poly.type == ICSGPolygon.POLY_TYPE.POLY_UNKNOWN){	// gray
//				gl.glColor3d(0.25, 0.25, 0.25);
//			}
			for(ICSGVertex v : poly.getVertices()) {
				gl.glVertex3dv(v.getXYZ(), 0);
			}
			gl.glEnd();
		}
	}
	
	
	public static void glDrawImportantEdges(GL gl, ICSGFace face){
		/*
		if(this.isSelectable()){
			// draw perimeter if face is selectable.. :)
			//gl.glColor3d(0.25, 0.25, 0.25);
			Iterator<ICSGVertex> iterPerim = this.getPerimeterVertices().iterator();
			gl.glBegin(GL.GL_LINE_LOOP);
				while(iterPerim.hasNext()){
					ICSGVertex v = iterPerim.next();
					gl.glTexCoord3dv(v.getXYZ(), 0); // must call before you place the vertex! :)
					gl.glVertex3dv(v.getXYZ(), 0);						
				}
			gl.glEnd();
		}
		*/
		
		// alternatively, just draw the mating edge lines
		gl.glBegin(GL.GL_LINES);
			for(ICSGVertex v : face.getMatingEdgeLines()){
				gl.glTexCoord3dv(v.getXYZ(), 0); // must call before you place the vertex! :)
				gl.glVertex3dv(v.getXYZ(), 0);			
			}
		gl.glEnd();
	}
	
	
	public static void glDrawFace(GL gl, ICSGFace face){
		if(AvoGlobal.DEBUG_MODE){
			drawFaceForDebug(gl, face);
			drawFaceLinesForDebug(gl, face);
			drawFaceNormalsForDebug(gl, face);
		}else{
			// Main drawing routine.  
			for(ICSGPolygon poly : face.getPolygons()){
				/*
				if(selectable && isSelected){
					gl.glColor3d(0.4, 0.9, 0.7);
				}else{
					 gl.glColor3d(0.4, 0.9, 0.4);
				}
				*/
				gl.glBegin(GL.GL_POLYGON);
				for(ICSGVertex v : poly.getVertices()) {
					gl.glTexCoord3dv(v.getXYZ(), 0); // must call before you place the vertex! :)
					gl.glVertex3dv(v.getXYZ(), 0);					
				}
				gl.glEnd();
			}
			/*
			if(this.isSelectable()){
				// draw perimeter if face is selectable.. :)
				gl.glColor3d(0.25, 0.25, 0.25);
				Iterator<ICSGVertex> iterPerim = this.getPerimeterVertices().iterator();
				gl.glBegin(GL.GL_LINE_LOOP);
					while(iterPerim.hasNext()){
						ICSGVertex v = iterPerim.next();
						gl.glTexCoord3dv(v.getXYZ(), 0); // must call before you place the vertex! :)
						gl.glVertex3dv(v.getXYZ(), 0);						
					}
				gl.glEnd();
			}
			*/
			
		}		
	}

	public static void drawFaceNormalsForDebug(GL gl, ICSGFace face){
		for(ICSGPolygon poly : face.getPolygons()){			
			drawPolygonNormalsForDebug(gl, poly);
		}
	}
	public static void glDrawPolygon(GL gl, ICSGPolygon polygon){
		// TODO: put this in a GL lib of somekind..
		gl.glBegin(GL.GL_POLYGON);
		for(ICSGVertex v : polygon.getVertices()){
			gl.glVertex3dv(v.getXYZ(), 0);
		}
		gl.glEnd();
	}
	public static void drawPolygonForDebug(GL gl, ICSGPolygon polygon){
		gl.glColor3f(0.5f, 0.7f, 0.7f);
		gl.glBegin(GL.GL_POLYGON);
		for(ICSGVertex v : polygon.getVertices()){
			gl.glVertex3dv(v.getXYZ(), 0);
		}
		gl.glEnd();
	}
	
	public static void drawPolygonNormalsForDebug(GL gl, ICSGPolygon polygon){
		gl.glColor3d(1.0, 0.0, 0.0);
		ICSGVertex fCenter = polygon.getBarycenterVertex();
		ICSGVertex norm = polygon.getPlane().getNormal();
		double scale = 0.10;
		fCenter.addToVertex(norm.getScaledCopy(scale));
		ICSGVertex nShifted = fCenter.addToVertex(norm.getScaledCopy(scale));
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3dv(fCenter.getXYZ(), 0);
			gl.glVertex3dv(nShifted.getXYZ(), 0);
		gl.glEnd();
	}
	public static void glDraw(GL gl, IPrim2DArc arc) {
		if(arc.isSelected()){
			float[] color = AvoColors.GL_COLOR4_2D_ACTIVE;
			gl.glColor4f(color[0], color[1], color[2], color[3]);
		}else{
			float[] color = AvoColors.GL_COLOR4_2D_NONACT;
			gl.glColor4f(color[0], color[1], color[2], color[3]);
		}
		IPoint2D center = arc.getArcCenterPoint();
		GLDynPrim.arc2D(gl, center, arc.getArcRadius(), arc.getStartAngle(), arc.getArcAngle(), 0.0);
		GLDynPrim.point(gl, center.getX(), center.getY(), 0.0, 3.0);
	}
	public static void glDraw(GL gl, IPrim2D prim) {
		if(prim instanceof IPrim2DArc) {
			glDraw(gl,(IPrim2DArc)prim);
		} else if(prim instanceof IPrim2DLine) {
			glDraw(gl,(IPrim2DLine)prim);
		} else {
			throw new IllegalStateException();
		}
	}
	

	public static void drawNormalFromOriginForDegug(GL gl, ICSGPlane plane){
		gl.glColor3d(1.0, 0.0, 0.0);
		double scale = 1.0;
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3dv(plane.getNormal().getScaledCopy(scale).getXYZ(), 0);
			gl.glVertex3d(0.0, 0.0, 0.0);
		gl.glEnd();
	}
	
	/**
	 * just for debug purposes... 
	 * a simple way to visualize the ray in 3D space. 
	 * @param gl the GL context on which to render
	 * @param length the total length of the line
	 */
	public static void drawLineForDebug(GL gl, ICSGRay ray, double length){
		ICSGVertex basePoint = ray.getBasePoint();
		ICSGVertex begin = basePoint;
		ICSGVertex end   = basePoint.addToVertex(ray.getDirection().getScaledCopy(length));
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3d(begin.getX(), begin.getY(), begin.getZ());
			gl.glVertex3d(end.getX(), end.getY(), end.getZ());			
		gl.glEnd();
		gl.glBegin(GL.GL_POINTS);
			gl.glVertex3d(basePoint.getX(), basePoint.getY(), basePoint.getZ());
		gl.glEnd();
	}
	

	public static void drawSegmentForDebug(GL gl, ICSGSegment seg){
		ICSGVertex startVert = seg.getVertStart();
		ICSGVertex endVert = seg.getVertEnd();
		gl.glBegin(GL.GL_LINES);
			gl.glColor3f(0.5f,0.5f,0.5f);
			gl.glVertex3d(startVert.getX(), startVert.getY(), startVert.getZ());
			gl.glVertex3d(endVert.getX(), endVert.getY(), endVert.getZ());			
		gl.glEnd();
		gl.glBegin(GL.GL_POINTS);
			gl.glColor3f(0.0f,1.0f,0.0f);
			gl.glVertex3d(startVert.getX(), startVert.getY(), startVert.getZ());
			gl.glColor3f(1.0f,0.0f,0.0f);
			gl.glVertex3d(endVert.getX(), endVert.getY(), endVert.getZ());	
		gl.glEnd();
	}
	

	public static void drawPointForDebug(GL gl, ICSGVertex vertex){
		gl.glColor3d(0.0, 1.0, 0.0);
		gl.glBegin(GL.GL_POINTS);
			gl.glVertex3d( vertex.getX(), vertex.getY(), vertex.getZ());
		gl.glEnd();
	}
	
	public static void glDrawVertex(GL gl, ICSGVertex vertex){
		gl.glBegin(GL.GL_POINTS);
			gl.glVertex3d(vertex.getX(), vertex.getY(), vertex.getZ());
		gl.glEnd();
	}
	

	public static void drawPlaneForDebug(GL gl, ISketchPlane plane){
		ICSGVertex origin = plane.getOrigin();
		ICSGVertex xAxis = plane.getXAxis();
		ICSGVertex yAxis = plane.getYAxis();
		// 3x5 rectangle (aligned along var 1);
		ICSGVertex a = origin.addToVertex(xAxis.getScaledCopy( 2.5)).addToVertex(yAxis.getScaledCopy( 1.5));
		ICSGVertex b = origin.addToVertex(xAxis.getScaledCopy(-2.5)).addToVertex(yAxis.getScaledCopy( 1.5));
		ICSGVertex c = origin.addToVertex(xAxis.getScaledCopy(-2.5)).addToVertex(yAxis.getScaledCopy(-1.5));
		ICSGVertex d = origin.addToVertex(xAxis.getScaledCopy( 2.5)).addToVertex(yAxis.getScaledCopy(-1.5));
		gl.glColor3f(0.5f, 1.0f, 1.0f);
		gl.glBegin(GL.GL_LINE_LOOP);
			gl.glVertex3dv(a.getXYZ(), 0);
			gl.glVertex3dv(b.getXYZ(), 0);
			gl.glVertex3dv(c.getXYZ(), 0);
			gl.glVertex3dv(d.getXYZ(), 0);
		gl.glEnd();
	}
	
	/**
	 * orient the scene to the sketch plane.  
	 * @param gl
	 */
	public static void glOrientToPlane(GL gl, ISketchPlane plane){
		//gl.glLoadIdentity();
		// align to origin
		ICSGVertex origin = plane.getOrigin();
		
		gl.glTranslated(origin.getX(), origin.getY(), origin.getZ());
		
		// rotate to align planes
		double xRot = plane.getRotationX();
		double yRot = plane.getRotationY();
		gl.glRotated(xRot*180.0/Math.PI, 1.0, 0.0, 0.0);
		gl.glRotated(yRot*180.0/Math.PI, 0.0, 1.0, 0.0);
		
		// rotate around z-axis to align 2D grid
		double zRot = plane.getRotationZ();
		gl.glRotated(zRot*180.0/Math.PI, 0.0, 0.0, 1.0);
		//System.out.println("xRot:" + xRot + " yRot:" + yRot + " zRot:" + zRot);
	}
}
