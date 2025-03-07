/*******************************************************************************
 * Copyright (c) 2025 Syntevo GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Singer (Syntevo) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.toolbar.ToolBarRenderer;

public class DefaultColorProvider implements ColorProvider {

	public static ColorProvider createLightInstance() {
		final float baseHue = 208;
		final Color selection = hsb(baseHue, 1, 0.72f);

		final Color black = gray(0);
		final Color gray128 = gray(128);
		final Color gray160 = gray(160);
		final Color gray192 = gray(192);
		final Color gray204 = gray(204);
		final Color gray227 = gray(227);
		final Color white = gray(255);

		final Color background = gray227;
		final Color foreground = black;
		final Color disabled = gray160;

		final DefaultColorProvider p = new DefaultColorProvider();
		p.put(Composite.KEY_BACKGROUND, background);

		p.put(CSimpleText.KEY_BACKGROUND, white);
		p.put(CSimpleText.KEY_BACKGROUND_READONLY, gray192);
		p.put(CSimpleText.KEY_FOREGROUND, foreground);
		p.put(CSimpleText.KEY_DISABLED, disabled);
		p.put(CSimpleText.KEY_SELECTION_BACKGROUND, selection);
		p.put(CSimpleText.KEY_SELECTION_FOREGROUND, white);
		p.put(CSimpleText.KEY_BORDER, gray128);

		p.put(Button.KEY_HOVER, hsb(baseHue, 0.1f, 1));
		p.put(Button.KEY_TOGGLE, hsb(baseHue, 0.2f, 0.96f));
		p.put(Button.KEY_SELECTION, selection);
		p.put(Button.KEY_TEXT, foreground);
		p.put(Button.KEY_DISABLE, disabled);
		p.put(Button.KEY_OUTLINE, gray160);
		p.put(Button.KEY_OUTLINE_DISABLED, gray192);
		p.put(Button.KEY_GRAYED, gray192);
		p.put(Button.KEY_PUSH, white);

		p.put(Label.KEY_BACKGROUND, background);
		p.put(Label.KEY_FOREGROUND, foreground);
		p.put(Label.KEY_DISABLED, disabled);
		p.put(Label.KEY_SHADOW_IN_1, gray160);
		p.put(Label.KEY_SHADOW_IN_2, white);
		p.put(Label.KEY_SHADOW_OUT_1, white);
		p.put(Label.KEY_SHADOW_OUT_2, gray160);

		p.put(Link.KEY_DISABLED, disabled);
		p.put(Link.KEY_LINK, hsb(baseHue, 1, 0.6f));
		p.put(Link.KEY_FOREGROUND, foreground);

		p.put(ScaleRenderer.KEY_HANDLE_IDLE, selection);
		p.put(ScaleRenderer.KEY_HANDLE_HOVER, black);
		p.put(ScaleRenderer.KEY_HANDLE_DRAG, gray204);
		p.put(ScaleRenderer.KEY_HANDLE_OUTLINE, gray160);
		p.put(ScaleRenderer.KEY_NOTCH, gray160);
		p.put(ScaleRenderer.KEY_DISABLED, disabled);

		p.put(ToolBarRenderer.KEY_SEPARATOR, gray160);
		p.put(ToolBarRenderer.KEY_SHADOW_OUT, gray160);
		p.put(ToolBarRenderer.KEY_HOVER_BACKGROUND, hsb(baseHue, 0.2f, 1));
		p.put(ToolBarRenderer.KEY_HOVER_BORDER, hsb(baseHue, 0.3f, 1));
		p.put(ToolBarRenderer.KEY_SELECTION_BACKGROUND, hsb(baseHue, 0.3f, 1));
		p.put(ToolBarRenderer.KEY_SELECTION_BORDER, hsb(baseHue, 0.5f, 1));

		return p;
	}

	public static ColorProvider createDarkInstance() {
		final float baseHue = 208;
		final Color selection = hsb(baseHue, 0.5f, 0.72f);

		final Color black = gray(0);
		final Color gray30 = gray(30);
		final Color gray64 = gray(64);
		final Color gray80 = gray(80);
		final Color gray128 = gray(128);
		final Color gray160 = gray(160);
		final Color gray204 = gray(204);
		final Color white = gray(255);

		final Color background = gray30;
		final Color foreground = white;
		final Color disabled = gray160;

		final DefaultColorProvider p = new DefaultColorProvider();
		p.put(Composite.KEY_BACKGROUND, gray30);

		p.put(CSimpleText.KEY_BACKGROUND, gray64);
		p.put(CSimpleText.KEY_BACKGROUND_READONLY, background);
		p.put(CSimpleText.KEY_FOREGROUND, foreground);
		p.put(CSimpleText.KEY_DISABLED, disabled);
		p.put(CSimpleText.KEY_SELECTION_BACKGROUND, selection);
		p.put(CSimpleText.KEY_SELECTION_FOREGROUND, white);
		p.put(CSimpleText.KEY_BORDER, gray128);

		p.put(Button.KEY_PUSH, gray64);
		p.put(Button.KEY_HOVER, gray80);
		p.put(Button.KEY_TOGGLE, hsb(baseHue, 0.2f, 0.96f));
		p.put(Button.KEY_SELECTION, selection);
		p.put(Button.KEY_GRAYED, gray128);
		p.put(Button.KEY_OUTLINE, gray160);
		p.put(Button.KEY_OUTLINE_DISABLED, gray128);
		p.put(Button.KEY_TEXT, foreground);
		p.put(Button.KEY_DISABLE, disabled);

		p.put(Label.KEY_BACKGROUND, background);
		p.put(Label.KEY_FOREGROUND, foreground);
		p.put(Label.KEY_DISABLED, disabled);
		p.put(Label.KEY_SHADOW_IN_1, black);
		p.put(Label.KEY_SHADOW_IN_2, gray80);
		p.put(Label.KEY_SHADOW_OUT_1, gray80);
		p.put(Label.KEY_SHADOW_OUT_2, black);

		p.put(Link.KEY_DISABLED, disabled);
		p.put(Link.KEY_LINK, hsb(baseHue, 1, 0.8f));
		p.put(Link.KEY_FOREGROUND, foreground);

		p.put(ScaleRenderer.KEY_HANDLE_IDLE, selection);
		p.put(ScaleRenderer.KEY_HANDLE_HOVER, gray204);
		p.put(ScaleRenderer.KEY_HANDLE_DRAG, gray128);
		p.put(ScaleRenderer.KEY_HANDLE_OUTLINE, gray128);
		p.put(ScaleRenderer.KEY_NOTCH, gray128);
		p.put(ScaleRenderer.KEY_DISABLED, disabled);

		p.put(ToolBarRenderer.KEY_SEPARATOR, gray160);
		p.put(ToolBarRenderer.KEY_SHADOW_OUT, gray160);
		p.put(ToolBarRenderer.KEY_HOVER_BACKGROUND, hsb(baseHue, 0.2f, 1));
		p.put(ToolBarRenderer.KEY_HOVER_BORDER, hsb(baseHue, 0.3f, 1));
		p.put(ToolBarRenderer.KEY_SELECTION_BACKGROUND, hsb(baseHue, 0.3f, 1));
		p.put(ToolBarRenderer.KEY_SELECTION_BORDER, hsb(baseHue, 0.5f, 1));
		return p;
	}

	private final Map<String, Color> map = new HashMap<>();

	public DefaultColorProvider() {
	}

	@Override
	public Color getColor(String key) {
		if (key == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		final Color color = map.get(key);
		if (color == null) {
			SWT.error(SWT.ERROR_UNSPECIFIED);
		}
		return color;
	}

	private void put(String keyBorder, Color color) {
		map.put(keyBorder, color);
	}

	public static Color gray(int value) {
		return new Color(value, value, value);
	}

	public static Color hsb(float baseHue, float saturation, float brightness) {
		return new Color(new RGB(baseHue, saturation, brightness));
	}

	public static Color blend(Color color1, Color color2, float factor) {
		return new Color(blend(color1.getRed(), color2.getRed(), factor),
				blend(color1.getGreen(), color2.getGreen(), factor),
				blend(color1.getBlue(), color2.getBlue(), factor));
	}

	private static int blend(int value1, int value2, float factor) {
		int value = Math.round(value1 - (value1 - value2) * factor);
		return Math.max(0, Math.min(value, 255));
	}
}
