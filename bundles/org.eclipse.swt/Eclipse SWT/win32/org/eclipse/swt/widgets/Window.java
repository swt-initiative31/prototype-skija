package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

import io.github.humbleui.skija.*;
import io.github.humbleui.types.*;

public class Window {

	// smooth at 120 FPS, before not really...
	// with vsync use an FPS over 60 and then is is also smooth.
	// vsync reduces the FPS to the monitor refresh rate.
	static final int FPS = 40;
	boolean vsync = true;

	static final int WIDTH = 800;
	static final int HEIGHT = 600;

	// Dont use, was just for testing...
	boolean directColor = false;

	boolean withSkija = true;
	private Display d;
	long hwnd;

	boolean printFrameRate = true;
	long lastFrame;
	int frames;

	// for cirle positioning
	private long startTime;

	public Window() {

		startTime = System.currentTimeMillis();

		d = new Display();
		d.w = this;

	}

	public int exec() {

		hwnd = OS.CreateWindowEx(0, d.windowClass, d.windowClass, OS.WS_OVERLAPPEDWINDOW, OS.CW_USEDEFAULT,
				OS.CW_USEDEFAULT, WIDTH, HEIGHT, 0, 0, d.hInstance, null);

		if (hwnd == 0)
			throw new IllegalStateException("Did not work...");

		OS.ShowWindow(hwnd, OS.SW_SHOW);
		OS.UpdateWindow(hwnd);

		MSG msg = new MSG();
		while (OS.GetMessage(msg, 0, 0, 0)) {
			OS.TranslateMessage(msg);
			OS.DispatchMessage(msg);
		}

		return (int) msg.wParam;

	}

	public static void main(String[] arg) {

		new Window().exec();

	}

	boolean created = false;
	private long memHdc;
	private Surface surface;

	boolean lastColor = true;

	public long windowProc(long hwnd, long msg, long wParam, long lParam) {
		long hBitmap = 0;
		long pBitmapData = 0;

		if (!created) {
			setupMemoryDC();
			created = true;
		}

		switch ((int) msg) {
		case OS.WM_CREATE: {

			System.out.println("Create...");
			break;

		}

		case OS.WM_PAINT: {

			PAINTSTRUCT ps = new PAINTSTRUCT();
			long hdc = OS.BeginPaint(hwnd, ps);

			if (directColor) {
				long hBrush = OS.CreateSolidBrush(convertRGBToInt(new RGB(255, 0, 0))); // Rot
				RECT rect = new RECT(100, 100, 400, 300);
				OS.FillRect(hdc, rect, hBrush);
				OS.DeleteObject(hBrush); // Brush freigeben

			} else {

				if (printFrameRate) {

					if (System.currentTimeMillis() - lastFrame > 1000) {
						System.out.println("Frames: " + frames);
						frames = 0;
						lastFrame = System.currentTimeMillis();
					}
					frames++;
				}

				surface.getCanvas().clear(0xFFFFFFFF);

				long currentPosTime = System.currentTimeMillis() - startTime;

				currentPosTime = currentPosTime % 10000;

				double position = (double) currentPosTime / (double) 10000;

				int colorAsRBG = 0xFF42FFF4;

				try (var paint = new Paint()) {
					paint.setColor(colorAsRBG);

					surface.getCanvas().drawCircle((int) (position * WIDTH), 100, 100, paint);

				}

				// for vsync use DwmFlush in order to wait until the screen drawing is finished.
				if (vsync)
					OS.DwmFlush();
				OS.BitBlt(hdc, 0, 0, WIDTH, HEIGHT, memHdc, 0, 0, OS.SRCCOPY);
			}

			OS.EndPaint(hwnd, ps);

			break;

		}

		case OS.WM_TIMER:
			OS.InvalidateRect(hwnd, null, false);
			break;
		case OS.WM_SIZE:

			break;
		}

		return OS.DefWindowProc(hwnd, (int) msg, wParam, lParam);

	}

	private void setupMemoryDC() {

		if (withSkija) {

			/* Create resources */
			long hdc = OS.GetDC(hwnd);
			memHdc = OS.CreateCompatibleDC(hdc);
			BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
			bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
			bmiHeader.biWidth = WIDTH;
			bmiHeader.biHeight = -HEIGHT;
			bmiHeader.biPlanes = 1;
			bmiHeader.biBitCount = 32;
			bmiHeader.biCompression = OS.BI_RGB;
			byte[] bmi = new byte[BITMAPINFOHEADER.sizeof];
			OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
			long[] pBits = new long[1];
			long hBitmap = OS.CreateDIBSection(hdc, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
			if (hBitmap == 0)
				SWT.error(SWT.ERROR_NO_HANDLES);

			OS.SelectObject(memHdc, hBitmap);

			surface = Surface.makeRasterDirect(ImageInfo.makeN32(WIDTH, HEIGHT, ColorAlphaType.PREMUL), pBits[0],
					4 * WIDTH);

			var canvas = surface.getCanvas();

			Paint paint = new Paint().setColor(0xFF42FFF4);
			canvas.clear(0xFFFFFFFF);
			canvas.drawRect(Rect.makeXYWH(100, 100, 200, 200), paint);

//	      // Gib den HDC zurück
			OS.ReleaseDC(hwnd, hdc);

			// TIMER
			OS.SetTimer(hwnd, 1, 1000 / FPS, 0);

			return;
		}

		/* Create resources */
		long hdc = OS.GetDC(hwnd);
		memHdc = OS.CreateCompatibleDC(hdc);
		BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
		bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
		bmiHeader.biWidth = WIDTH;
		bmiHeader.biHeight = -HEIGHT;
		bmiHeader.biPlanes = 1;
		bmiHeader.biBitCount = 32;
		bmiHeader.biCompression = OS.BI_RGB;
		byte[] bmi = new byte[BITMAPINFOHEADER.sizeof];
		OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
		long[] pBits = new long[1];
		long hBitmap = OS.CreateDIBSection(hdc, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
		if (hBitmap == 0)
			SWT.error(SWT.ERROR_NO_HANDLES);

		// Erstelle ein HDC für das Bitmap
		OS.SelectObject(memHdc, hBitmap);

		long hBrush = OS.CreateSolidBrush(convertRGBToInt(new RGB(255, 0, 0))); // Rot
		RECT rect = new RECT(100, 100, 400, 300);
		OS.FillRect(memHdc, rect, hBrush);
		OS.DeleteObject(hBrush); // Brush freigeben

//      // Gib den HDC zurück
		OS.ReleaseDC(hwnd, hdc);
//
//      // Bild anzeigen (z.B. im Clientbereich des Fensters)
		OS.InvalidateRect(hwnd, null, true);

	}

	public static int convertRGBToInt(RGB rgb) {
		// extract RGB-components
		int red = rgb.red;
		int green = rgb.green;
		int blue = rgb.blue;

		// probably wrong currently...
		int color = (red << 16) | (green << 8) | (blue);

		return color;
	}

}
