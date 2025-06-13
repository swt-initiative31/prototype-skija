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

	protected abstract int getAlpha();
	protected abstract void setAlpha(int alpha);
	protected abstract Color getBackground();
	protected abstract void setBackground(Color color);
	protected abstract Pattern getBackgroundPattern();
	protected abstract void setBackgroundPattern(Pattern pattern);
	protected abstract Color getForeground();
	protected abstract void setForeground(Color color);
	protected abstract Pattern getForegroundPattern();
	protected abstract void setForegroundPattern(Pattern pattern);
	protected abstract Font getFont();
	protected abstract void setFont(Font font);

	protected abstract boolean getAdvanced();
	protected abstract void setAdvanced(boolean enable);
	protected abstract int getAntialias();
	protected abstract void setAntialias(int antialias);
	protected abstract int getFillRule();
	protected abstract void setFillRule(int rule);
	protected abstract int getInterpolation();
	protected abstract void setInterpolation(int interpolation);
	protected abstract LineAttributes getLineAttributes();
	protected abstract void setLineAttributes(LineAttributes attributes);
	protected abstract int getLineCap();
	protected abstract void setLineCap(int cap);
	protected abstract int[] getLineDash();
	protected abstract void setLineDash(int[] dashes);
	protected abstract int getLineJoin();
	protected abstract void setLineJoin(int join);
	protected abstract int getLineStyle();
	protected abstract void setLineStyle(int lineStyle);
	protected abstract int getLineWidth();
	protected abstract void setLineWidth(int i);
	protected abstract int getTextAntialias();
	protected abstract void setTextAntialias(int antialias);
	protected abstract boolean getXORMode();
	protected abstract void setXORMode(boolean xor);

	protected abstract void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle);
	protected abstract void drawFocus(int x, int y, int width, int height);
	protected abstract void drawImage(Image image, int x, int y);
	protected abstract void drawImage(Image img, int i, int j, int width, int imageHeight, int x,
			int imageY, int width2, int imageHeight2);
	protected abstract void drawLine(int x1, int y1, int x2, int y2);
	protected abstract void drawOval(int x, int y, int width, int height);
	protected abstract void drawPath(Path path);
	protected abstract void drawPoint(int x, int y);
	protected abstract void drawPolygon(int[] pointArray);
	protected abstract void drawPolyline(int[] pointArray);
	protected abstract void drawRectangle(int x, int y, int width, int height);
	protected abstract void drawRectangle(Rectangle rectangle);
	protected abstract void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight);
	protected abstract void drawText(String line, int lineX, int lineY, int dRAW_FLAGS);

	protected abstract void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle);
	protected abstract void fillOval(int x, int y, int width, int height);
	protected abstract void fillGradientRectangle(int i, int pos, int width, int gradientHeight,
			boolean b);
	protected abstract void fillPath(Path path);
	protected abstract void fillPolygon(int[] pointArray);
	protected abstract void fillRectangle(int x, int y, int width, int height);
	protected abstract void fillRectangle(Rectangle rect);
	protected abstract void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight);

	protected abstract FontMetrics getFontMetrics();
	protected abstract int getAdvanceWidth(char ch);
	protected abstract int getCharWidth(char ch);
	protected abstract Point stringExtent(String string);
	protected abstract Point textExtent(String string, int dRAW_FLAGS);
	protected abstract Point textExtent(String string);
	protected abstract void drawString(String string, int x, int y);
	protected abstract void drawString(String string, int x, int y, boolean isTransparent);
	protected abstract void drawText(String string, int x, int y);
	protected abstract void drawText(String string, int x, int y, boolean isTransparent);

	protected abstract boolean isClipped();
	protected abstract Rectangle getClipping();
	protected abstract void getClipping(Region region);
	protected abstract void setClipping(int x, int y, int width, int height);
	protected abstract void setClipping(Path path);
	protected abstract void setClipping(Rectangle rect);
	protected abstract void setClipping(Region region);

	protected abstract void getTransform(Transform transform);
	protected abstract void setTransform(Transform transform);

	protected abstract void copyArea(Image image, int x, int y);
	protected abstract void copyArea(int srcX, int srcY, int width, int height, int destX, int destY);
	protected abstract void copyArea(int srcX, int srcY, int width, int height, int destX, int destY, boolean paint);

	abstract void checkGC(int mask);

	abstract GCData getGCData();

	abstract int getStyle();

	protected void commit() {
	}
}

