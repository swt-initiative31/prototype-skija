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

public class DefaultColorProvider implements IColorProvider {
	private final Map<String, Color> map = new HashMap<>();
	protected final Display display;

	public DefaultColorProvider(Display display) {
		this.display = Objects.requireNonNull(display);

		final Color selection = new Color(0, 95, 184);
		final Color disabled = new Color(128, 128, 128);

		map.put(Label.KEY_DISABLED, disabled);
		map.put(Label.KEY_SHADOW_IN_LIGHT, new Color(255, 255, 255));
		map.put(Label.KEY_SHADOW_IN_DARK, new Color(160, 160, 160));
		map.put(Label.KEY_SHADOW_OUT_LIGHT, new Color(227, 227, 227));
		map.put(Label.KEY_SHADOW_OUT_DARK, new Color(160, 160, 160));

		map.put(Button.KEY_BUTTON, new Color(255, 255, 255));
		map.put(Button.KEY_HOVER, new Color(224, 238, 254));
		map.put(Button.KEY_TOGGLE, new Color(204, 228, 247));
		map.put(Button.KEY_SELECTION, selection);
		map.put(Button.KEY_TRISTATE, new Color(128, 128, 128));
		map.put(Button.KEY_OUTLINE, new Color(160, 160, 160));
		map.put(Button.KEY_TEXT, new Color(0, 0, 0));
		map.put(Button.KEY_DISABLE, disabled);
		map.put(Button.KEY_ARROW, new Color(128, 128, 128));

		map.put(ScaleRenderer.KEY_HANDLE_IDLE, selection);
		map.put(ScaleRenderer.KEY_HANDLE_HOVER, new Color(0, 0, 0));
		map.put(ScaleRenderer.KEY_HANDLE_DRAG, new Color(204, 204, 204));
		map.put(ScaleRenderer.KEY_HANDLE_OUTLINE, new Color(160, 160, 160));
		map.put(ScaleRenderer.KEY_NOTCH, new Color(160, 160, 160));
	}

	@Override
	public Color getColor(String key) {
		final Color color = map.get(Objects.requireNonNull(key));
		if (color == null) {
			SWT.error(SWT.ERROR_UNSPECIFIED);
		}
		return color;
	}
}
