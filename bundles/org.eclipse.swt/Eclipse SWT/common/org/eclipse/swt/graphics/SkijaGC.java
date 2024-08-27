package org.eclipse.swt.graphics;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;

import io.github.humbleui.skija.*;
import io.github.humbleui.skija.Font;
import io.github.humbleui.types.*;

public class SkijaGC implements IGraphicsContext {
	public final static float CONVERSION_RATIO_OS_TO_SKIJA = SWT.getPlatform().equals("win32") ? 1.35f : 1.0f;

	private Surface surface;
	private Rectangle r;
	private GC innerGC;

	private int lineWidth;

	private Font font;


	public SkijaGC(Drawable drawable, int style) {
		innerGC = new GC(drawable, style);
		init();
	}

	public SkijaGC(GC gc) {
		innerGC = gc;
		init();
	}

	private void init() {
		r = DPIUtil.autoScaleUp(innerGC.getClipping());
		int width = r.width == 0 ? 1 : r.width;
		int height = r.height == 0 ? 1 : r.height;
		surface = Surface.makeRaster(ImageInfo.makeN32Premul(width, height));
		org.eclipse.swt.graphics.Font originalFont = innerGC.getFont();
		if (originalFont == null || originalFont.isDisposed()) {
			originalFont = innerGC.getDevice().getSystemFont();
		}
		setFont(originalFont);
	}

	@Override
	public void dispose() {
		this.innerGC.dispose();

	}

	@Override
	public Color getBackground() {
		return innerGC.getBackground();
	}

	private String[] splitString(String text) {
		String[] lines = new String[1];
		int start = 0, pos;
		do {
			pos = text.indexOf('\n', start);
			if (pos == -1) {
				lines[lines.length - 1] = text.substring(start);
			} else {
				boolean crlf = (pos > 0) && (text.charAt(pos - 1) == '\r');
				lines[lines.length - 1] = text.substring(start, pos - (crlf ? 1 : 0));
				start = pos + 1;
				String[] newLines = new String[lines.length + 1];
				System.arraycopy(lines, 0, newLines, 0, lines.length);
				lines = newLines;
			}
		} while (pos != -1);
		return lines;
	}

	@Override
	public void commit() {
//		if (this.background != null) {
//
//			Surface bgSurface = Surface
//					.makeRaster(ImageInfo.makeN32Premul(r.width, r.height));
//			//
//			//
//			Canvas bgc = bgSurface.getCanvas();
//			Paint bgPaint = new Paint().setColor(convertSWTColorToSkijaColor(getBackground()));
//			bgc.drawRect(Rect.makeWH(r.width, r.height), bgPaint);
//
//			bgSurface.draw(surface.getCanvas(), 0, 0, null);
//			bgPaint.close();
//
//			// byte[] imageBytes = EncoderPNG.encode(im).getBytes();
//			//
//			// Image i = new Image(getDevice(), new
//			// ByteArrayInputStream(imageBytes));
//			// super.drawImage(i, 0, 0);
//
//			bgSurface = surface;
//
//		}

		io.github.humbleui.skija.Image im = surface.makeImageSnapshot();
		byte[] imageBytes = EncoderPNG.encode(im).getBytes();

		Image i = new Image(innerGC.getDevice(), new ByteArrayInputStream(imageBytes));

		Rectangle clipping = innerGC.getClipping();
		Rectangle scaledClipping = DPIUtil.autoScaleUp(clipping);
		innerGC.drawImage(i, 0, 0, scaledClipping.width, scaledClipping.height, 0, 0, clipping.width, clipping.height);
		i.dispose();
	}

	public Point textExtent(String string) {
		return textExtent(string, SWT.NONE);
	}

	@Override
	public void setBackground(Color color) {
		innerGC.setBackground(color);
	}

	@Override
	public void setForeground(Color color) {
		innerGC.setForeground(color);
	}

	@Override
	public void fillRectangle(Rectangle rect) {
		fillRectangle(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void drawImage(Image image, int x, int y) {
		Canvas canvas = surface.getCanvas();
		canvas.drawImage(convertSWTImageToSkijaImage(image), DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y));
	}

	@Override
	public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY,
			int destWidth, int destHeight) {
		Canvas canvas = surface.getCanvas();
		canvas.drawImageRect(
				convertSWTImageToSkijaImage(image), new Rect(DPIUtil.autoScaleUp(srcX), DPIUtil.autoScaleUp(srcY),
						DPIUtil.autoScaleUp(srcX + srcWidth), DPIUtil.autoScaleUp(srcY + srcHeight)),
				new Rect(DPIUtil.autoScaleUp(destX), DPIUtil.autoScaleUp(destY), DPIUtil.autoScaleUp(destX + destWidth),
						DPIUtil.autoScaleUp(destY + destHeight)));

	}

	private static ColorType getSkijaColorType(ImageData imageData) {
		PaletteData palette = imageData.palette;

		if (palette.isDirect) {
			int redMask = palette.redMask;
			int greenMask = palette.greenMask;
			int blueMask = palette.blueMask;

			String hexString = Integer.toHexString(redMask);
			System.out.println("RED: " + hexString);

			hexString = Integer.toHexString(greenMask);
			System.out.println("GREEN: " + hexString);

			hexString = Integer.toHexString(blueMask);
			System.out.println("BLUE: " + hexString);

			if (redMask == 0xFF0000 && greenMask == 0x00FF00 && blueMask == 0x0000FF) {
				return ColorType.UNKNOWN;
			}

			if (redMask == 0xFF00000 && greenMask == 0x00FF0000 && blueMask == 0x0000FF00) {
				return ColorType.RGBA_8888;
			}

			if (redMask == 0xF800 && greenMask == 0x07E0 && blueMask == 0x001F) {
				return ColorType.RGB_565;
			}

			if (redMask == 0xF000 && greenMask == 0x0F00 && blueMask == 0x00F0) {
				return ColorType.ARGB_4444;
			}

			if (redMask == 0x0000FF00 && greenMask == 0x00FF0000 && blueMask == 0xFF000000) {
				return ColorType.BGRA_8888;
			}

			if (redMask == 0x3FF00000 && greenMask == 0x000FFC00 && blueMask == 0x000003FF) {
				return ColorType.RGBA_1010102;
			}

			if (redMask == 0x000003FF && greenMask == 0x000FFC00 && blueMask == 0x3FF00000) {
				return ColorType.BGRA_1010102;
			}
		} else {
			if (imageData.depth == 8 && palette.colors != null && palette.colors.length <= 256) {
				return ColorType.ALPHA_8;
			}

			if (imageData.depth == 16) {
				// 16-bit indexed images are not directly mappable to common
				// ColorTypes
				return ColorType.ARGB_4444; // Assume for indexed color images
			}

			if (imageData.depth == 24) {
				// Assuming RGB with no alpha channel
				return ColorType.RGB_888X;
			}

			if (imageData.depth == 32) {
				// Assuming 32-bit color with alpha channel
				return ColorType.RGBA_8888;
			}
		}

		// Additional mappings for more complex or less common types
		// You may need additional logic or assumptions here

		// Fallback for unsupported or unknown types
		throw new UnsupportedOperationException("Unsupported ColorType");
	}

	private static io.github.humbleui.skija.Image convertSWTImageToSkijaImage(Image swtImage) {
		ImageData imageData = swtImage.getImageData(DPIUtil.getDeviceZoom());

		int width = imageData.width;
		int height = imageData.height;

		PaletteData palette = imageData.palette;

		palette.getPixel(new RGB(1, 2, 3));

		// ImageInfo imageInfo = new ImageInfo(width, height,
		// ColorType.BGRA_8888,
		// ColorAlphaType.PREMUL);

		ColorType colType = getSkijaColorType(imageData);

		if (colType.equals(ColorType.UNKNOWN)) {
			imageData = convertARGBToRGBA(imageData);
			colType = ColorType.RGBA_8888;
		}

		ImageInfo imageInfo = new ImageInfo(width, height, colType, ColorAlphaType.UNPREMUL);

		return io.github.humbleui.skija.Image.makeRasterFromBytes(imageInfo, imageData.data, imageData.bytesPerLine);
	}

	private static ImageData convertARGBToRGBA(ImageData imageData) {
		byte[] data = imageData.data;
		byte[] convertedData = new byte[data.length];
		for (int i = 0; i < data.length; i += 4) {
			byte alpha = (byte) imageData.alphaData[i / 4];
			byte red = data[i + 1];
			byte green = data[i + 2];
			byte blue = data[i + 3];

			convertedData[i] = red;
			convertedData[i + 1] = green;
			convertedData[i + 2] = blue;
			convertedData[i + 3] = alpha;
		}
		imageData.data = convertedData;
		return imageData;
	}


	// Funktion zur Konvertierung der Farbe
	public static int convertSWTColorToSkijaColor(Color swtColor) {
		// Extrahieren der RGB-Komponenten
		int red = swtColor.getRed();
		int green = swtColor.getGreen();
		int blue = swtColor.getBlue();
		int alpha = swtColor.getAlpha();

		// Erstellen der Skija-Farbe: ARGB 32-Bit-Farbe
		int skijaColor = (alpha << 24) | (red << 16) | (green << 8) | blue;

		return skijaColor;
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		Paint p = new Paint();
		p.setColor(convertSWTColorToSkijaColor(getForeground()));
		p.setAntiAlias(true);
		surface.getCanvas().drawLine(x1, y1, x2, y2, p);
		p.close();
	}

	@Override
	public Color getForeground() {
		return innerGC.getForeground();
	}

	@Override
	public void drawText(String string, int x, int y) {
		drawText(string, x, y, SWT.NONE);
	}

	@Override
	public void drawText(String string, int x, int y, boolean isTransparent) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void drawText(String text, int x, int y, int flags) {

		Canvas c = surface.getCanvas();

		Paint p = new Paint();
		p.setColor(convertSWTColorToSkijaColor(getForeground()));
		p.setAntiAlias(true);

		// Erstellen eines TextBlob für 2 Zeilen
		TextBlobBuilder blobBuilder = new TextBlobBuilder();
		String[] lines = text == null ? null : splitString(text);

		float lineHeight = font.getMetrics().getHeight();

		// we actually have to set the first height to the text hight half. This
		// is kind of irritating...
		float fy = textExtent(lines[0]).y;

		if (lines != null)
			for (String line : lines) {
				blobBuilder.appendRun(font, line, 0, fy);
				fy = fy + lineHeight;

			}

		// Erstellen des TextBlob
		TextBlob textBlob = blobBuilder.build();
		blobBuilder.close();

		// Zeichnen des TextBlobs auf dem Canvas

		c.drawTextBlob(textBlob, DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y), p);

		p.close();

	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	public void drawFocus(int x, int y, int width, int height) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	void drawIcon(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth,
			int destHeight, boolean simple) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	void drawBitmap(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY,
			int destWidth, int destHeight, boolean simple) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	void drawBitmapAlpha(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY,
			int destWidth, int destHeight, boolean simple) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	void drawBitmapTransparentByClipping(long srcHdc, long maskHdc, int srcX, int srcY, int srcWidth, int srcHeight,
			int destX, int destY, int destWidth, int destHeight, boolean simple, int imgWidth, int imgHeight) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void drawOval(int x, int y, int width, int height) {
		Paint p = new Paint();
		p.setColor(convertSWTColorToSkijaColor(getForeground()));
		p.setMode(PaintMode.STROKE);
		p.setAntiAlias(true);
		surface.getCanvas().drawOval(new Rect(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y),
				DPIUtil.autoScaleUp(x + width), DPIUtil.autoScaleUp(y + height)), p);
		p.close();
	}

	public void drawPath(Path path) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void drawPoint(int x, int y) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void drawPolygon(int[] pointArray) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	public void drawPolyline(int[] pointArray) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void drawRectangle(int x, int y, int width, int height) {
		Paint p = new Paint();
		p.setColor(convertSWTColorToSkijaColor(getForeground()));
		p.setMode(PaintMode.STROKE);
		p.setAntiAlias(true);
		surface.getCanvas().drawRect(new Rect(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y),
				DPIUtil.autoScaleUp(x + width), DPIUtil.autoScaleUp(y + height)), p);
		p.close();
	}

	@Override
	public void drawRectangle(Rectangle rect) {
		drawRectangle(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		drawRectangle(x, y, width, height);
	}

	public void drawString(String string, int x, int y) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	public void drawString(String string, int x, int y, boolean isTransparent) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void fillGradientRectangle(int x, int y, int width, int height, boolean vertical) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		Paint p = new Paint();
		p.setColor(convertSWTColorToSkijaColor(getBackground()));
		p.setMode(PaintMode.FILL);
		p.setAntiAlias(true);
		surface.getCanvas().drawOval(new Rect(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y),
				DPIUtil.autoScaleUp(x + width), DPIUtil.autoScaleUp(y + height)), p);
		p.close();
	}

	public void fillPath(Path path) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void fillPolygon(int[] pointArray) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void fillRectangle(int x, int y, int width, int height) {
		Paint p = new Paint();
		p.setColor(convertSWTColorToSkijaColor(getBackground()));
		p.setMode(PaintMode.FILL);
		p.setAntiAlias(true);
		surface.getCanvas().drawRect(new Rect(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y),
				DPIUtil.autoScaleUp(x + width), DPIUtil.autoScaleUp(y + height)), p);
		p.close();
	}

	@Override
	public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		fillRectangle(x, y, width, height);
	}

	@Override
	public Point textExtent(String string, int flags) {
		Rect textExtent = this.font.measureText(string);
		return DPIUtil.autoScaleDown(new Point(Math.round(textExtent.getWidth()), Math.round(textExtent.getHeight())));
	}

	@Override
	public void setFont(org.eclipse.swt.graphics.Font font) {
		innerGC.setFont(font);
		FontData fontData = font.getFontData()[0];
		this.font = new Font(Typeface.makeFromName(fontData.getName(), FontStyle.NORMAL),
				DPIUtil.autoScaleUp(fontData.getHeight()) * CONVERSION_RATIO_OS_TO_SKIJA);
	}

	@Override
	public org.eclipse.swt.graphics.Font getFont() {
		return innerGC.getFont();
	}

	@Override
	public void setClipping(int x, int y, int width, int height) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	@Override
	public void setTransform(Transform transform) {

		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	@Override
	public void setAlpha(int alpha) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	@Override
	public int getAlpha() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	@Override
	public void setLineWidth(int i) {
		this.lineWidth = i;

	}

	@Override
	public IFontMetrics getFontMetrics() {
		IFontMetrics fm = new SkijaFontMetrics(font.getMetrics());
		return fm;
	}

	@Override
	public int getAntialias() {
		return innerGC.getAntialias();
	}

	@Override
	public void setAntialias(int antialias) {
		innerGC.setAntialias(antialias);
	}

	@Override
	public void setAdvanced(boolean enable) {
		innerGC.setAdvanced(enable);
	}

}
