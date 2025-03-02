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

public class DefaultColorProvider implements ColorProvider {

	public static ColorProvider createLightInstance() {
		final Color selection = new Color(0, 95, 184);

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

		p.put(Button.KEY_HOVER, new Color(224, 238, 254));
		p.put(Button.KEY_TOGGLE, new Color(204, 228, 247));
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
		p.put(Label.KEY_SHADOW_IN_2, white);
		p.put(Label.KEY_SHADOW_IN_1, gray160);
		p.put(Label.KEY_SHADOW_OUT_1, background);
		p.put(Label.KEY_SHADOW_OUT_2, gray160);

		p.put(Link.KEY_DISABLED, disabled);
		p.put(Link.KEY_LINK, new Color(0, 102, 204));
		p.put(Link.KEY_FOREGROUND, foreground);

		p.put(ScaleRenderer.KEY_HANDLE_IDLE, selection);
		p.put(ScaleRenderer.KEY_HANDLE_HOVER, black);
		p.put(ScaleRenderer.KEY_HANDLE_DRAG, gray204);
		p.put(ScaleRenderer.KEY_HANDLE_OUTLINE, gray160);
		p.put(ScaleRenderer.KEY_NOTCH, gray160);
		p.put(ScaleRenderer.KEY_DISABLED, disabled);

		return p;
	}

	public static ColorProvider createDarkInstance() {
		final Color selection = new Color(91, 139, 184);

		final Color black = gray(0);
		final Color gray30 = gray(30);
		final Color gray64 = gray(64);
		final Color gray128 = gray(128);
		final Color gray160 = gray(160);
		final Color gray192 = gray(192);
		final Color gray204 = gray(204);
		final Color gray207 = gray(207);
		final Color gray227 = gray(227);
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
		p.put(Button.KEY_HOVER, new Color(224, 238, 254));
		p.put(Button.KEY_TOGGLE, new Color(204, 228, 247));
		p.put(Button.KEY_SELECTION, selection);
		p.put(Button.KEY_GRAYED, gray128);
		p.put(Button.KEY_OUTLINE, gray160);
		p.put(Button.KEY_OUTLINE_DISABLED, gray128);
		p.put(Button.KEY_TEXT, foreground);
		p.put(Button.KEY_DISABLE, disabled);

		p.put(Label.KEY_BACKGROUND, background);
		p.put(Label.KEY_FOREGROUND, foreground);
		p.put(Label.KEY_DISABLED, disabled);
		p.put(Label.KEY_SHADOW_IN_2, white);
		p.put(Label.KEY_SHADOW_IN_1, gray160);
		p.put(Label.KEY_SHADOW_OUT_1, gray227);
		p.put(Label.KEY_SHADOW_OUT_2, gray160);

		p.put(Link.KEY_DISABLED, disabled);
		p.put(Link.KEY_LINK, new Color(147, 200, 255));
		p.put(Link.KEY_FOREGROUND, foreground);

		p.put(ScaleRenderer.KEY_HANDLE_IDLE, selection);
		p.put(ScaleRenderer.KEY_HANDLE_HOVER, gray204);
		p.put(ScaleRenderer.KEY_HANDLE_DRAG, gray128);
		p.put(ScaleRenderer.KEY_HANDLE_OUTLINE, gray128);
		p.put(ScaleRenderer.KEY_NOTCH, gray128);
		p.put(ScaleRenderer.KEY_DISABLED, disabled);
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

	private static Color gray(int value) {
		return new Color(value, value, value);
	}
}
