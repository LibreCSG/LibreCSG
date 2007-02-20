package ui.opengl;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.glu.GLU;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import ui.menuet.Menuet;
import ui.tools.ToolInterface2D3D;
import backend.adt.Point3D;
import backend.global.AvoGlobal;
import backend.model.Feature2D;
import backend.model.Feature2D3D;
import backend.model.Sketch;
import backend.model.SubPart;
import backend.primatives.Prim2D;


//
//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the 
//GNU General Public License (GPL).
//
//This file is part of avoADo.
//
//AvoCADo is free software; you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation; either version 2 of the License, or
//(at your option) any later version.
//
//AvoCADo is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with AvoCADo; if not, write to the Free Software
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

/*
* @author  Adam Kumpf
* @created Feb. 2007
*/
public class GLView {

	static GL  gl;
	static GLU glu;
	public final  GLCanvas  glCanvas;
	final  GLContext glContext;
	
	boolean mouseIsDown = false;
	
	public boolean updateGLView = true;
	
	static float aspect = 0.0f;
	static float rotation_x = 0.0f;
	static float rotation_y = 0.0f;
	static float rotation_z = 0.0f;
	static float translation_x = 0.0f;
	static float translation_y = 0.0f;
	static float dist_from_center = -30.0f;
	static float viewing_angle = 45.0f;
	
	//double lastMousePos3D[] = new double[] {0.0, 0.0, 0.0};
	
	
	static int mouse_down_button 	= -1;
	static int mouse_down_x 		= 0;
	static int mouse_down_y		 	= 0;
	static float rot_init_x = 0.0f;
	static float rot_init_y = 0.0f;
	static float rot_init_z = 0.0f;	
	static float trans_init_x = 0.0f;
	static float trans_init_y = 0.0f;
	
	static int MOUSE_LEFT   = 1;
	static int MOUSE_MIDDLE = 2;
	static int MOUSE_MIDDLE_SHIFT = 21;
	static int MOUSE_MIDDLE_CTRL  = 22; 
	static int MOUSE_RIGHT  = 3;
	
	
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
		
		glCanvas.addControlListener(new ControlListener(){
			public void controlMoved(ControlEvent e) {
				updateGLView = true;				
			}

			public void controlResized(ControlEvent e) {
				updateGLView = true;				
			}			
		});
		
		
		glCanvas.addPaintListener(new PaintListener(){
			public void paintControl(PaintEvent e) {
				updateGLView = true;
			}			
		});
		
		//
		//  TODO: do rotation better!
		//
		glCanvas.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e) {
			}
			public void mouseDown(MouseEvent e) {
				mouse_down_button = e.button;
				if(((e.stateMask & SWT.SHIFT) != 0) && e.button == MOUSE_MIDDLE){
					System.out.println("Shift + Middle click");
					mouse_down_button = MOUSE_MIDDLE_SHIFT;
				}
				if(((e.stateMask & SWT.CTRL) != 0) && e.button == MOUSE_MIDDLE){
					System.out.println("Control + Middle click");
					mouse_down_button = MOUSE_MIDDLE_CTRL;
				}
				mouse_down_x = e.x;
				mouse_down_y = e.y;
				rot_init_x = rotation_x;
				rot_init_y = rotation_y;
				rot_init_z = rotation_z;
				trans_init_x = translation_x;
				trans_init_y = translation_y;
				//System.out.println("mouse button: " + e.button);
				
				//
				// let current tool know that the left mouse has been clicked
				//
				if(mouse_down_button == MOUSE_LEFT && 
						AvoGlobal.menuet.currentTool != null && 
						AvoGlobal.menuet.currentTool.toolInterface != null){
					double[] coor = getWorldCoorFromMouse(e.x,e.y);
					AvoGlobal.menuet.currentTool.toolInterface.glMouseDown(coor[0], coor[1], coor[2], e);
					updateGLView = true;
				}				
			}
			public void mouseUp(MouseEvent e) {
				if(mouse_down_button == MOUSE_LEFT && 
						glCanvas.getBounds().contains(e.x,e.y) && 
						(AvoGlobal.menuet.currentTool != null && AvoGlobal.menuet.currentTool.toolInterface != null)){
					double[] coor = getWorldCoorFromMouse(e.x,e.y);
					AvoGlobal.menuet.currentTool.toolInterface.glMouseUp(coor[0], coor[1], coor[2], e);					
				}
				mouse_down_button = -1;
				updateGLView = true;
			}			
		});
		glCanvas.addMouseMoveListener(new MouseMoveListener(){
			public void mouseMove(MouseEvent e) {
				float sensitivity = viewing_angle / 22.5f * 0.25f;				
				if(mouse_down_button == MOUSE_LEFT){					
				}
				if(mouse_down_button == MOUSE_MIDDLE){
					//System.out.println("Rotating view");
					int xdif = e.x - mouse_down_x;
					int ydif = e.y - mouse_down_y;					
					//rotation_x = sensitivity*ydif/2 + rot_init_x;
					//rotation_y = sensitivity*xdif/2 + rot_init_y;
					rotation_x = ydif/2 + rot_init_x;
					rotation_y = xdif/2 + rot_init_y;
					rotation_z =   0.0f + rot_init_z;
				}
				if(mouse_down_button == MOUSE_MIDDLE_SHIFT){
					//System.out.println("Panning view");
					int xdif = e.x - mouse_down_x;
					int ydif = e.y - mouse_down_y;
					translation_x = sensitivity*xdif/13.60f + trans_init_x;
					translation_y = -sensitivity*ydif/13.60f + trans_init_y;
				}	
				if(mouse_down_button == MOUSE_MIDDLE_CTRL){
					//System.out.println("Rotating view");
					int xdif = e.x - mouse_down_x;
					int ydif = e.y - mouse_down_y;					
					rotation_x = rot_init_x;
					rotation_y = rot_init_y;
					rotation_z =  sensitivity*ydif + sensitivity*xdif + rot_init_z;
				}
				if(mouse_down_button == MOUSE_RIGHT){					
				}		
				
				//
				// if mouse is inside glView, send the mouseMove event
				// the the currently active tool
				//
				if(glCanvas.getBounds().contains(e.x,e.y)){
					updateGLView = true;
					double[] coor = getWorldCoorFromMouse(e.x,e.y);
					if(AvoGlobal.menuet.currentTool != null && AvoGlobal.menuet.currentTool.toolInterface != null){
						if(mouse_down_button == MOUSE_LEFT){
							AvoGlobal.menuet.currentTool.toolInterface.glMouseDrag(coor[0], coor[1], coor[2], e);
						}else{
							if(mouse_down_button == -1){
								AvoGlobal.menuet.currentTool.toolInterface.glMouseMovedUp(coor[0], coor[1], coor[2], e);
							}
						}
					}
				}
				
			}			
		});
		glCanvas.addListener(SWT.MouseWheel, new Listener(){
			public void handleEvent(Event event) {
				System.out.println("Got mouse wheel: " + event.count);
				float zoom_smoothness = 1.50f; // closer to 1.0 is smoother
				if(event.count > 0){
					viewing_angle = Math.max(0.025f, viewing_angle / zoom_smoothness);
				}else{
					viewing_angle = Math.min(135.0f, viewing_angle * zoom_smoothness);
				}
				
				System.out.println("Viewing angle now:" + viewing_angle);
				updateGLView = true;
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
						
						// if there is an active sketch, show the gridlines.
						if(AvoGlobal.project.getActiveSketch() != null){
							// disable depth test so that overlapped items at
							// the same depth (in particular, 0.0) still get drawn.
							gl.glDisable(GL.GL_DEPTH_TEST);
							// set grid color						
							gl.glColor4f(AvoGlobal.GL_COLOR4_GRID_DARK[0], AvoGlobal.GL_COLOR4_GRID_DARK[1],
							  		AvoGlobal.GL_COLOR4_GRID_DARK[2], AvoGlobal.GL_COLOR4_GRID_DARK[3]);
							gl.glColor4f(AvoGlobal.GL_COLOR4_GRID_LIGHT[0], AvoGlobal.GL_COLOR4_GRID_LIGHT[1],
						  			AvoGlobal.GL_COLOR4_GRID_LIGHT[2], AvoGlobal.GL_COLOR4_GRID_LIGHT[3]);
							// draw grid
							gl.glDisable(GL.GL_LINE_SMOOTH);
							GLDynPrim.mesh(gl, -10.0, 10.0, -10.0, 10.0, 20, 20);
							gl.glEnable(GL.GL_LINE_SMOOTH);
							gl.glEnable(GL.GL_DEPTH_TEST);
						}
						
						gl.glColor4f(0.7f, 0.7f, 0.7f, 1.0f);
						gl.glLineWidth(2.5f);

						gl.glColor3f(1.0f,0.0f,0.0f);
						cad_3DX(0.0f,0.0f,0.0f,0.25f);
						
						if(AvoGlobal.project.getActivePart() != null){
							for(int q=0; q < AvoGlobal.project.getActivePart().getSubPartListSize(); q++){
								SubPart subPart = AvoGlobal.project.getActivePart().getAtIndex(q);
								
								// TODO: HACK for now to just show the active 2D sketch
								Sketch sketch = subPart.getSketch();
								if(sketch != null){
									gl.glPushMatrix();
									// TODO: handle sketch offset/rotation
									for(int i=0; i < sketch.getFeat2DListSize(); i++){
										Feature2D f2D = sketch.getAtIndex(i);
										if(f2D.isSelected){
								    		gl.glColor4f(	AvoGlobal.GL_COLOR4_2D_ACTIVE[0], AvoGlobal.GL_COLOR4_2D_ACTIVE[1],
						  									AvoGlobal.GL_COLOR4_2D_ACTIVE[2], AvoGlobal.GL_COLOR4_2D_ACTIVE[3]);
								    		// TODO: HACK, don't build primatives here.. build when created/modified!
								    		f2D.buildPrim2DList();
								    	}else{
								    		gl.glColor4f(	AvoGlobal.GL_COLOR4_2D_NONACT[0], AvoGlobal.GL_COLOR4_2D_NONACT[1],
								  							AvoGlobal.GL_COLOR4_2D_NONACT[2], AvoGlobal.GL_COLOR4_2D_NONACT[3]);
								    	}
										for(Prim2D prim : f2D.prim2DList){
								    		prim.glDraw(gl);
								    	}
									}
									
									gl.glPopMatrix();
								}
								
								Feature2D3D feat2D3D = subPart.getFeature2D3D();
								if(feat2D3D != null && feat2D3D.toolInt2D3D != null){
									feat2D3D.toolInt2D3D.draw3DFeature(gl, feat2D3D.paramSet, feat2D3D.getActiveSketch());
								}
							}
						}

						if(mouse_down_button != MOUSE_MIDDLE && 
								mouse_down_button != MOUSE_MIDDLE_SHIFT && 
								mouse_down_button != MOUSE_MIDDLE_CTRL &&
								AvoGlobal.menuet.currentTool != null){
							drawToolEndPos();
						}

						//
						// invisible plane to catch mouse events at depth ~0.0
						// this should be the last thing drawn in the 2D mode
						// TODO: HACK, this should be sized according to the grid, which should also be dynamic!
						//
						if(AvoGlobal.menuet.getCurrentToolMode() == Menuet.MENUET_MODE_2D ||
								AvoGlobal.menuet.getCurrentToolMode() == Menuet.MENUET_MODE_2Dto3D){
							gl.glColor4f(1.0f,0.0f,0.0f, 0.0f);
							gl.glBegin(GL.GL_QUADS);
								gl.glVertex3f(-100.0f, 100.0f, 0.0f);
								gl.glVertex3f( 100.0f, 100.0f, 0.0f);
								gl.glVertex3f( 100.0f,-100.0f, 0.0f);
								gl.glVertex3f(-100.0f,-100.0f, 0.0f);
							gl.glEnd();
						}
						
						glCanvas.swapBuffers(); // double buffering excitement!
						glContext.release();	// go ahead, you can have it back.
						
						Long timeDiff = System.nanoTime() - startTime;
						// TODO: dynamically change RenderLevel based on time to render!
						//System.out.println("Time to render: " + timeDiff);
						
						updateGLView = false;
					}
					Display.getCurrent().timerExec(50, this); // run "this" again in 50mSec.
		        }				
			}
	    }.run();
		
	}
	
	private void drawToolEndPos(){
		gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
		float size = (float)(AvoGlobal.gridSize / 4.0);
		cad_2DCross((float)AvoGlobal.glCursor3DPos[0], (float)AvoGlobal.glCursor3DPos[1], (float)AvoGlobal.glCursor3DPos[2], size);
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
	
	public void cad_2DCross(float x, float y, float z, float size){
		float sizeB = size*0.7f;
		gl.glLineWidth(1.0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(x+sizeB, y+sizeB, z);
			gl.glVertex3f(x-sizeB, y-sizeB, z);
			gl.glVertex3f(x-sizeB, y+sizeB, z);
			gl.glVertex3f(x+sizeB, y-sizeB, z);			
		gl.glEnd();	
		gl.glLineWidth(2.25f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(x, y+size, z);
			gl.glVertex3f(x, y-size, z);
			gl.glVertex3f(x+size, y, z);
			gl.glVertex3f(x-size, y, z);			
		gl.glEnd();	
	}

	private void glInit(){
		glContext.makeCurrent();
		gl = glContext.getGL ();
		glu = new GLU();
		
		gl.glClearColor(AvoGlobal.GL_COLOR4_BACKGND[0],AvoGlobal.GL_COLOR4_BACKGND[1],
						AvoGlobal.GL_COLOR4_BACKGND[2],AvoGlobal.GL_COLOR4_BACKGND[3]);
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL.GL_BLEND);
		gl.glEnable(GL.GL_AUTO_NORMAL);
		gl.glColorMaterial(GL.GL_FRONT, GL.GL_DIFFUSE);
		gl.glEnable(GL.GL_COLOR_MATERIAL);	// override material properties, makes coloring easier & faster
		gl.glEnable(GL.GL_LINE_SMOOTH); // smooth rendering of lines
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

		//TODO: more precision in XYZ lookup by using doubles instead of floats?
		
		winX = (float)mouseX;
		winY = (float)viewport[3] - (float)mouseY;		

		gl.glReadPixels( mouseX, (int)winY, 1, 1, GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, winZ );
		glu.gluUnProject((double)winX, (double)winY, (double)(winZ.get()), modelview, 0, projection, 0, viewport, 0, wcoord, 0);
		
        //System.out.println("World coords: (" + wcoord[0] + ", " + wcoord[1] + ", " + wcoord[2] + ")");
		
		//
		// Map coord's to snap if snap is enabled
		//
		if(AvoGlobal.snapEnabled && AvoGlobal.menuet.getCurrentToolMode() == Menuet.MENUET_MODE_2D){
			wcoord[0] = Math.floor((wcoord[0]+AvoGlobal.snapSize/2.0)/AvoGlobal.snapSize)*AvoGlobal.snapSize;
			wcoord[1] = Math.floor((wcoord[1]+AvoGlobal.snapSize/2.0)/AvoGlobal.snapSize)*AvoGlobal.snapSize;
			wcoord[2] = Math.floor((wcoord[2]+AvoGlobal.snapSize/2.0)/AvoGlobal.snapSize)*AvoGlobal.snapSize;
		}
		
		if(AvoGlobal.menuet.getCurrentToolMode() == Menuet.MENUET_MODE_2D && wcoord[2] < 0.25 && wcoord[2] > -0.25){
			wcoord[2] = 0.0; // tie z-value to zero if close enough to located on the drawing plane.
		}
		
        AvoGlobal.glCursor3DPos = wcoord;
        AvoGlobal.glViewEventHandler.notifyCursorMoved();
		return wcoord;
	}
	
	
	public void setViewTop(){
		rotation_x = 0.0f;
		rotation_y = 0.0f;
		rotation_z = 0.0f;
		translation_x = 0.0f;
		translation_y = 0.0f;
	}
}
