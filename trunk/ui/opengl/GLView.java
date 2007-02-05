package ui.opengl;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.glu.GLU;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import backend.global.AvoGlobal;

public class GLView {

	static GL  gl;
	static GLU glu;
	final  GLCanvas  glCanvas;
	final  GLContext glContext;
	
	boolean mouseIsDown = false;
	
	boolean updateGLView = true;
	
	static float aspect = 0.0f;
	static float rotation_x = 0.0f;
	static float rotation_y = 0.0f;
	static float rotation_z = 0.0f;
	static float translation_x = 0.0f;
	static float translation_y = 0.0f;
	static float dist_from_center = -30.0f;
	static float viewing_angle = 45.0f;
	
	double lastMousePos3D[] = new double[] {0.0, 0.0, 0.0};
	
	public GLView(Composite comp){
		GLData data = new GLData ();
		data.doubleBuffer = true;
		data.depthSize = 8;
		glCanvas = new GLCanvas(comp, SWT.NONE, data);
		
		GLCapabilities glc = new GLCapabilities();
		System.out.println("DepthBits:" + glc.getDepthBits() + 
				"; DoubleBuff:" + glc.getDoubleBuffered() + 
				"; HWAccel:" + glc.getHardwareAccelerated() + 
				"; StencilBits:" + glc.getStencilBits() +
				"; FloatPBuf:" + glc.getPbufferFloatingPointBuffers());
		glCanvas.setCurrent();
		glContext = GLDrawableFactory.getFactory().createExternalGLContext();
		
		//
		// Mouse events get sent to the active tool, along with
		// information about where the view was clicked.
		//
		glCanvas.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e) {
			}
			public void mouseDown(MouseEvent e) {
				if(AvoGlobal.currentTool != null && AvoGlobal.currentTool.toolInterface != null){
					double[] coor = getWorldCoorFromMouse(e.x,e.y);
					AvoGlobal.currentTool.toolInterface.glMouseDown(coor[0], coor[1], coor[2], e.x, e.y);
					mouseIsDown = true;
				}
			}
			public void mouseUp(MouseEvent e) {
				if(mouseIsDown && glCanvas.getBounds().contains(e.x,e.y) && (AvoGlobal.currentTool != null && AvoGlobal.currentTool.toolInterface != null)){
					double[] coor = getWorldCoorFromMouse(e.x,e.y);
					AvoGlobal.currentTool.toolInterface.glMouseUp(coor[0], coor[1], coor[2], e.x, e.y);
				}
				mouseIsDown = false;
			}			
		});
		
		glCanvas.addMouseMoveListener(new MouseMoveListener(){
			public void mouseMove(MouseEvent e) {
				if(glCanvas.getBounds().contains(e.x,e.y)){
					double[] coor = getWorldCoorFromMouse(e.x,e.y);
					if(AvoGlobal.currentTool != null && AvoGlobal.currentTool.toolInterface != null){
						if(mouseIsDown){
							AvoGlobal.currentTool.toolInterface.glMouseDrag(coor[0], coor[1], coor[2], e.x, e.y);
						}
					}
				}
			}			
		});
		
	
		glInit();
		initLights(gl);
		
		new Runnable() {
	    	//int rot = 0;
	        public void run() {	  
				if (glCanvas.isDisposed()) return;
				else{
					if(updateGLView){
						Long startTime = System.nanoTime();
						glCanvas.setCurrent();
						glContext.makeCurrent();
						gl = glContext.getGL ();
						//-------------------------------						
						int width = glCanvas.getParent().getBounds().width;
						int height= glCanvas.getBounds().height;
					    gl.glViewport(0, 0, width, height);
					    aspect = (float) width / (float) height;
					    // -------------------------------							
					    gl.glMatrixMode(GL.GL_PROJECTION);
					    gl.glLoadIdentity();
					    glu.gluPerspective(viewing_angle, aspect, 1.0f, 1000.0f); // Perspective view

					    gl.glTranslatef(translation_x, translation_y, dist_from_center);	    
					    gl.glRotatef(rotation_x, 1.0f, 0.0f, 0.0f);
					    gl.glRotatef(rotation_y, 0.0f, 1.0f, 0.0f);
					    gl.glRotatef(rotation_z, 0.0f, 0.0f, 1.0f);
					    // -------------------------------						    
					    gl.glMatrixMode(GL.GL_MODELVIEW);						
						gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
						gl.glLoadIdentity();
						gl.glPolygonMode(GL.GL_FRONT, GL.GL_FILL);
						gl.glColor4f(0.7f, 0.7f, 0.7f, 1.0f);
						gl.glLineWidth(2.0f);

						gl.glColor3f(1.0f,0.0f,0.0f);
						cad_3DX(0.0f,0.0f,0.0f,0.25f);
						
						
					    //updateGLView = false;

						
					    gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
						if(AvoGlobal.workingFeature != null){
							AvoGlobal.workingFeature.toolInterface.glDrawFeature(gl, AvoGlobal.workingFeature.paramSet);
						}
						
						
						// invisible plane to catch mouse events at depth ~0.0
						// this should be the last thing drawn in the 2D mode
						gl.glColor4f(1.0f,0.0f,0.0f, 0.0f);
						gl.glBegin(GL.GL_QUADS);
							gl.glVertex3f(-100.0f, 100.0f, 0.0f);
							gl.glVertex3f( 100.0f, 100.0f, 0.0f);
							gl.glVertex3f( 100.0f,-100.0f, 0.0f);
							gl.glVertex3f(-100.0f,-100.0f, 0.0f);
						gl.glEnd();
						
						drawToolEndPos();
						
						glCanvas.swapBuffers(); // double buffering excitement!
						glContext.release();	// go ahead, you can have it back.
						
						Long timeDiff = System.nanoTime() - startTime;
						// TODO: dynamically change RenderLevel based on time to render!
						//System.out.println("Time to render: " + timeDiff);
					}
					Display.getCurrent().timerExec(50, this); // run "this" again in 50mSec.
		        }				
			}
	    }.run();
		
	}
	
	private void drawToolEndPos(){
		gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
		cad_3DX((float)lastMousePos3D[0], (float)lastMousePos3D[1], (float)lastMousePos3D[2], 0.125f);
	}
	
	public void cad_3DX(float x, float y, float z, float size){
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(x+size, y+size, z+size);
			gl.glVertex3f(x-size, y-size, z-size);
			gl.glVertex3f(x-size, y+size, z+size);
			gl.glVertex3f(x+size, y-size, z-size);			
			gl.glVertex3f(x+size, y-size, z+size);
			gl.glVertex3f(x-size, y+size, z-size);
			gl.glVertex3f(x+size, y+size, z-size);
			gl.glVertex3f(x-size, y-size, z+size);			
		gl.glEnd();		
	}

	private void glInit(){
		glContext.makeCurrent();
		gl = glContext.getGL ();
		glu = new GLU();
		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL.GL_BLEND);
		gl.glEnable(GL.GL_AUTO_NORMAL);
		gl.glColorMaterial(GL.GL_FRONT, GL.GL_DIFFUSE);
		gl.glEnable(GL.GL_COLOR_MATERIAL);	// override material properties, makes coloring easier & faster
		gl.glEnable(GL.GL_LINE_SMOOTH);
		gl.glClearDepth(1.0);
		gl.glLineWidth(2.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_SHADE_MODEL);
		gl.glShadeModel(GL.GL_SMOOTH);
		
		glContext.release();
	
		Device.DEBUG = true;
	}
	
	
	private void initLights(GL gl)
	  {
    	gl.glDisable(GL.GL_LIGHTING);
	    gl.glDisable(GL.GL_LIGHT0);
	  }
	
	public double[] getWorldCoorFromMouse(int mouseX, int mouseY){
		// get mouse coordinates and project them onto the 3D space
		int[] viewport = new int[4];
		double[] modelview = new double[16];
		double[] projection = new double[16];
		float winX, winY;
		FloatBuffer winZ = FloatBuffer.allocate(4);
		double wcoord[] = new double[4];// wx, wy, wz;// returned xyz coords

		gl.glGetDoublev( GL.GL_MODELVIEW_MATRIX, modelview, 0 );
		gl.glGetDoublev( GL.GL_PROJECTION_MATRIX, projection, 0 );
		gl.glGetIntegerv( GL.GL_VIEWPORT, viewport, 0 );

		
		winX = (float)mouseX;
		winY = (float)viewport[3] - (float)mouseY;		

		gl.glReadPixels( mouseX, (int)winY, 1, 1, GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, winZ );
		glu.gluUnProject((double)winX, (double)winY, (double)(winZ.get()), modelview, 0, projection, 0, viewport, 0, wcoord, 0);
		
        //System.out.println("World coords: (" + wcoord[0] + ", " + wcoord[1] + ", " + wcoord[2] + ")");
		
		//
		// Map coord's to snap if snap is enabled
		//
		if(AvoGlobal.snapEnabled){
			wcoord[0] = Math.floor((wcoord[0]+AvoGlobal.snapSize/2.0)/AvoGlobal.snapSize)*AvoGlobal.snapSize;
			wcoord[1] = Math.floor((wcoord[1]+AvoGlobal.snapSize/2.0)/AvoGlobal.snapSize)*AvoGlobal.snapSize;
			wcoord[2] = Math.floor((wcoord[2]+AvoGlobal.snapSize/2.0)/AvoGlobal.snapSize)*AvoGlobal.snapSize;
		}
		
        lastMousePos3D = wcoord;
		return wcoord;
	}
}
