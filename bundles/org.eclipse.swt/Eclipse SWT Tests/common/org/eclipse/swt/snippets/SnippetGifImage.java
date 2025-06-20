package org.eclipse.swt.snippets;

import java.io.*;

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
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetGifImage {

	static int ZOOM = 200;

	static Image[] images = null;
	static Display display;

	static final int[] imageTypes = { SWT.ICON, SWT.ICON, SWT.ICON };

	static final String[] imageLocations = { "closedFolder.gif", //$NON-NLS-1$
			"openFolder.gif", //$NON-NLS-1$
			"target.gif" //$NON-NLS-1$
	};


	public static void main(String[] args) {
		display = new Display();

		initResources();

		Image image1 = display.getSystemImage(SWT.ICON_INFORMATION);

		ImageData img = image1.getImageData();

		Shell shell = new Shell(display);
		shell.setText("SnippetButton");
		shell.setLayout(new GridLayout());

		// button.addSelectionListener(widgetSelectedAdapter(e ->
		// System.out.println("Received evt: " + e )));
		// button.addSelectionListener(widgetSelectedAdapter(__ ->
		// System.out.println("Another click")));

		// When the shell is active and the user presses ENTER, the button is
		// pressed
		// shell.setDefaultButton(button);
		for(int i = 0; i < 3 ; i++)
		{
			var button = new Button(shell, SWT.PUSH);
			button.addListener(SWT.Selection, event -> System.out.println("Click!!!"));
			button.setText("Button Question Icon");
			button.setImage(images[0]);
			button.setSize(133, 150);
			button.setLocation(200, 200);
		}

		shell.setSize(300, 500);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	static void initResources() {
		final Class<SnippetGifImage> clazz = SnippetGifImage.class;
		try {
			if (images == null) {
				images = new Image[imageLocations.length];

				for (int i = 0; i < imageLocations.length; ++i) {
					try (InputStream sourceStream = clazz.getResourceAsStream(imageLocations[i])) {
						ImageData source = new ImageData(sourceStream);
						if (imageTypes[i] == SWT.ICON) {
							ImageData mask = source.getTransparencyMask();
							images[i] = new Image(display, source, mask);
						} else {
							images[i] = new Image(display, source);
						}
					}
				}
			}
			return;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}


}
