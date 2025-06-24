package org.eclipse.swt.snippets;

import java.awt.event.*;

import javax.swing.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.*;
import com.jogamp.opengl.util.*;

import io.github.humbleui.skija.*;
import io.github.humbleui.skija.Font;

// Should work
public class SkijaSwingDB implements GLEventListener {
	private DirectContext context;
	private Surface skiaSurface;
	private static GLCanvas glCanvas;

	private BackendRenderTarget renderTarget;
	private boolean printFrameRate = true;
	private long lastFrame;
	private long framesToDraw;
	private long frames;
	private long startTime = System.currentTimeMillis();


	final static int RECTANGLES_PER_LINE = 50;

	static class RecDraw{



		public RecDraw(int xPos, int yPos, org.eclipse.swt.graphics.Color c) {
			super();
			this.xPos = xPos;
			this.yPos = yPos;
			this.c = c;
		}
		int xPos ,yPos;
		org.eclipse.swt.graphics.Color c;

	}

	static RecDraw[][] recDraws = new RecDraw[RECTANGLES_PER_LINE][RECTANGLES_PER_LINE];
	int width = 2;


	public static void main(String[] args) throws Throwable {

		for( int x = 0 ; x < RECTANGLES_PER_LINE ; x++ ) {
			for(int y = 0 ; y < RECTANGLES_PER_LINE ; y++) {

				recDraws[x][y] = new RecDraw( x*2,y*2,Display.getDefault().getSystemColor((x+y )% 16 ));

			}
		}



		// Setzen der System-Eigenschaft
		System.setProperty("sun.awt.noerasebackground", "true");

		// Erstellen des GLCanvas
		GLProfile profile = GLProfile.get(GLProfile.GL2);


		GLCapabilities capabilities = new GLCapabilities(profile);

		glCanvas = new GLCanvas(capabilities);

		glCanvas.setAutoSwapBufferMode(true);


		SkijaSwingDB example = new SkijaSwingDB();
		glCanvas.addGLEventListener(example);


		// Frame erstellen und Canvas hinzufügen
		JFrame frame = new JFrame("Skija on Swing Example");

		frame.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {

				System.out.println("MouseMoved...");

			}
		});

		frame.setSize(800, 600);
		frame.add(glCanvas);
		frame.setVisible(true);

		// FPSAnimator erstellen und auf 60 FPS einstellen
		FPSAnimator animator = new FPSAnimator(glCanvas, 800);
		animator.start();

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		try {
			// Erstellen des DirectContext für Skija
			context = DirectContext.makeGL();
			if (context == null) {
				throw new RuntimeException("Failed to create DirectContext");
			}
			System.out.println("DirectContext created: " + context);

			// OpenGL-BackendRenderTarget erstellen
			GL2 gl = drawable.getGL().getGL2();
			gl.setSwapInterval(0);
			int width = drawable.getSurfaceWidth();
			int height = drawable.getSurfaceHeight();
			int samples = 0;
			int stencil = 8;

			renderTarget = BackendRenderTarget.makeGL(width, height, samples, stencil, 0, 0);

			if (renderTarget == null) {
				throw new RuntimeException("Failed to create BackendRenderTarget");
			}
			System.out.println("BackendRenderTarget created: " + renderTarget);

			// // Surface erstellen
			// skiaSurface = Surface.makeFromBackendRenderTarget(context,
			// renderTarget, SurfaceOrigin.BOTTOM_LEFT,
			// SurfaceColorFormat.RGBA_8888, ColorSpace.getSRGB());

			java.awt.Rectangle rect = glCanvas.getBounds();
			renderTarget = BackendRenderTarget.makeGL(rect.width, rect.height, /* samples */0, /* stencil */8,
					/* fbid */0, FramebufferFormat.GR_GL_RGBA8);
			skiaSurface = Surface.makeFromBackendRenderTarget(context, renderTarget, SurfaceOrigin.BOTTOM_LEFT,
					SurfaceColorFormat.RGBA_8888, ColorSpace.getDisplayP3(), new SurfaceProps(PixelGeometry.RGB_H));

			if (skiaSurface == null) {
				throw new RuntimeException("Failed to create Surface");
			}
			System.out.println("Surface created: " + skiaSurface);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Initialization failed", e);
		}
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		if (skiaSurface != null) {

			var canvas = skiaSurface.getCanvas();
			// Zeichnen mit Skija
			canvas.clear(0xFFFFFFFF);

			int width = drawable.getSurfaceWidth();
			int height = drawable.getSurfaceHeight();




			SkijaGC gc = new SkijaGC(skiaSurface);


			long currentPosTime = System.currentTimeMillis() - startTime;

			currentPosTime = currentPosTime % 10000;

			double position = (double) currentPosTime / (double) 10000;

			int shift = (int) (position *skiaSurface.getWidth());
			int shiftDown = 20;

			for( int x = 0 ; x < RECTANGLES_PER_LINE ; x++ ) {
				for(int y = 0 ; y < RECTANGLES_PER_LINE ; y++) {

	                var rec = recDraws[x][y];
	                gc.setForeground(rec.c);
					gc.drawRectangle(shift + rec.xPos ,shiftDown + rec.yPos,  2,2 );

				}
			}



			if (printFrameRate) {

				if (System.currentTimeMillis() - lastFrame > 1000) {
					framesToDraw = frames;


					frames = 0;
					lastFrame = System.currentTimeMillis();
				}
				frames++;

		        // Textstil erstellen
		        Paint paintText = new Paint().setColor(0xFF000000);
		        Font font = new Font(Typeface.makeDefault(), 24);

		        // Text zeichnen
		        canvas.drawString("Frames: " + framesToDraw, 40, 40, font, paintText);
			}


			}


			context.flush();
	}

	private TextBlob buildTextBlob(String text) {
		TextBlobBuilder blobBuilder = new TextBlobBuilder();
		float lineHeight = 30;
		int yOffset = 0;
		Font f = new Font(Typeface.makeFromName("Arial", FontStyle.NORMAL));
		f.setSize(30);

		blobBuilder.appendRun(f, text, 0, yOffset);
		yOffset += lineHeight;

		TextBlob textBlob = blobBuilder.build();
		blobBuilder.close();
		return textBlob;
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		if (skiaSurface != null) {
			skiaSurface.close();
		}
		if (context != null) {
			context.close();
		}
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

		// OpenGL-BackendRenderTarget erstellen
		GL2 gl = drawable.getGL().getGL2();
		gl.setSwapInterval(0);
		width = drawable.getSurfaceWidth();
		height = drawable.getSurfaceHeight();
		int samples = 0;
		int stencil = 8;

		if (skiaSurface != null) {
			skiaSurface.close();
			skiaSurface = null;
		}
		if (renderTarget != null) {
			renderTarget.close();
			renderTarget = null;
		}

		java.awt.Rectangle rect = glCanvas.getBounds();
		renderTarget = BackendRenderTarget.makeGL(rect.width, rect.height, /* samples */0, /* stencil */8, /* fbid */0,
				FramebufferFormat.GR_GL_RGBA8);
		skiaSurface = Surface.makeFromBackendRenderTarget(context, renderTarget, SurfaceOrigin.BOTTOM_LEFT,
				SurfaceColorFormat.RGBA_8888, ColorSpace.getDisplayP3(), new SurfaceProps(PixelGeometry.RGB_H));

		display(drawable);

		// GL2 gl = drawable.getGL().getGL2();
		// gl.glViewport(0, 0, width, height);
		// gl.glMatrixMode(GL2.GL_PROJECTION);
		// gl.glLoadIdentity();
		//
		// // Setze einen orthographischen 2D-Projektionsmodus
		// if (width > height) {
		// float aspect = (float) width / height;
		// gl.glOrtho(-aspect, aspect, -1.0, 1.0, -1.0, 1.0);
		// } else {
		// float aspect = (float) height / width;
		// gl.glOrtho(-1.0, 1.0, -aspect, aspect, -1.0, 1.0);
		// }
		//
		// gl.glMatrixMode(GL2.GL_MODELVIEW);
		// gl.glLoadIdentity();
	}
}
