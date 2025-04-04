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

public interface RendererFactory {
	ButtonRenderer createCheckboxRenderer(CustomButton button);

	ButtonRenderer createRadioButtonRenderer(CustomButton button);

	ButtonRenderer createArrowButtonRenderer(CustomButton button);

	ButtonRenderer createPushToggleButtonRenderer(CustomButton button);

	LabelRenderer createLabelRenderer(CustomLabel label);

	ScaleRenderer createScaleRenderer(CustomScale scale);

	SliderRenderer createSliderRenderer(CustomSlider slider);

	LinkRenderer createLinkRenderer(CustomLink link);
}
