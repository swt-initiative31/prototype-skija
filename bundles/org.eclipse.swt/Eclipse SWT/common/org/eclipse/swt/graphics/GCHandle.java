/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

public abstract class GCHandle extends Resource {

	public void commit() {}

	public abstract Point stringExtent(String string);

	public abstract int getLineCap();
	public abstract void drawLine(int x1, int y1, int x2, int y2);
	public abstract void drawRectangle(int x, int y, int width, int height);
	public abstract void fillRectangle(int x, int y, int width, int height);
	public abstract void drawText(String string, int x, int y);
	public abstract void drawText(String string, int x, int y, boolean isTransparent);
	public abstract void drawImage(Image image, int x, int y);
	public abstract void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle);
	public abstract void drawOval(int x, int y, int width, int height);
	public abstract void fillOval(int x, int y, int width, int height);
	public abstract void drawPolyline(int[] pointArray);
	public abstract void drawPolygon(int[] pointArray);
	public abstract void fillPolygon(int[] pointArray);
	public abstract void drawPoint(int x, int y);
	public abstract void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight);
	public abstract void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight);

	public abstract Color getBackground();
	public abstract void setBackground(Color color);
	public abstract Color getForeground();
	public abstract void setForeground(Color color);
	public abstract Font getFont();
	public abstract void setFont(Font font);
	public abstract Rectangle getClipping();
	public abstract void setClipping(int x, int y, int width, int height);
	public abstract void setTransform(Transform transform);
	public abstract int getAlpha();
	public abstract void setAlpha(int alpha);
	public abstract Point textExtent(String string, int dRAW_FLAGS);
	public abstract Point textExtent(String string);
	public abstract void fillRectangle(Rectangle rect);
	public abstract void fillGradientRectangle(int i, int pos, int width, int gradientHeight,
							   boolean b);
	public abstract void setLineWidth(int i);
	public abstract FontMetrics getFontMetrics();
	public abstract void drawImage(Image img, int i, int j, int width, int imageHeight, int x,
				   int imageY, int width2, int imageHeight2);
	public abstract void drawText(String line, int lineX, int lineY, int dRAW_FLAGS);
	public abstract void drawRectangle(Rectangle rectangle);

	public abstract void setAntialias(int antialias);
	public abstract int getAntialias();

	public abstract void setAdvanced(boolean enable);

	public abstract void setLineStyle(int lineStyle);
	public abstract void drawFocus (int x, int y, int width, int height);
	public abstract int getLineStyle();
	public abstract int getLineWidth();
	public abstract LineAttributes getLineAttributes();


	public abstract void copyArea(Image image, int x, int y);
	public abstract void copyArea(int srcX, int srcY, int width, int height, int destX, int destY);
	public abstract void copyArea(int srcX, int srcY, int width, int height, int destX, int destY, boolean paint);
	public abstract void drawPath(Path path);
	public abstract void drawString(String string, int x, int y, boolean isTransparent);
	public abstract void drawString(String string, int x, int y);

	abstract void checkGC(int mask);

	abstract void fillPath(Path path);

	abstract boolean isClipped();

	abstract int getFillRule();

	abstract void getClipping(Region region);

	abstract void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle);

	protected abstract int getAdvanceWidth(char ch);

	protected abstract boolean getAdvanced();

	protected abstract Pattern getBackgroundPattern();

	protected abstract int getCharWidth(char ch);

	protected abstract Pattern getForegroundPattern();

	abstract GCData getGCData();

	protected abstract int getInterpolation();

	protected abstract int[] getLineDash();

	protected abstract int getLineJoin();

	abstract int getStyle();

	protected abstract int getTextAntialias();

	protected abstract void getTransform(Transform transform);

	protected abstract boolean getXORMode();

	protected abstract void setBackgroundPattern(Pattern pattern);

	protected abstract void setClipping(Path path);

	protected abstract void setClipping(Rectangle rect);

	protected abstract void setClipping(Region region);

	abstract void setFillRule(int rule);

	protected abstract void setForegroundPattern(Pattern pattern);

	protected abstract void setInterpolation(int interpolation);

	protected abstract void setLineAttributes(LineAttributes attributes);

	protected abstract void setLineCap(int cap);

	protected abstract void setLineDash(int[] dashes);

	protected abstract void setLineJoin(int join);

	protected abstract void setXORMode(boolean xor);

	protected abstract void setTextAntialias(int antialias);
}

