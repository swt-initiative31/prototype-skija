/*******************************************************************************
 * Copyright (c) 2024 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.graphics;

public class ImageUtils {

	public static void setImageDataProvider(Image image, ImageDataProvider imgDataProv) {
		image.setImageDataProvider(imgDataProv);
	}

	public static Image createGenericImage(ImageDataProvider imgDataProv) {
		return new Image(imgDataProv);
	}
}
