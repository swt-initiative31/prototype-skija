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

public abstract class FontMetricsHandle {
	public abstract int getAscent();
	public abstract int getDescent();
	public abstract int getHeight();
	public abstract int getLeading();
	public abstract double getAverageCharacterWidth();
	/**
	 * Returns the average character width, measured in points,
	 * of the font described by the receiver.
	 *
	 * @return the average character width of the font
	 * @deprecated Use getAverageCharacterWidth() instead
	 */
	@Deprecated
	public abstract int getAverageCharWidth();
}
