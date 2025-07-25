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
 *     Syntevo GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;

abstract class ControlCommon extends Widget {

	private boolean disposed;

	protected ControlCommon() {
	}

	protected ControlCommon(Composite parent, int style) {
		super(parent, style);
	}

	protected final ColorProvider getColorProvider() {
		return display.getColorProvider();
	}

}
