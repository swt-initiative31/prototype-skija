package org.eclipse.swt.tests.skija;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.NativeGC;
import org.eclipse.swt.graphics.SkijaGC;
import org.eclipse.swt.widgets.Display;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Test_org_eclipse_swt_graphics_SkijaGC {

    static final int WIDTH = 5;
    static final int HEIGHT = 5;

    Color white, red, blue, green;

    @Before
    public void setUp() {
	display = Display.getDefault();
	white = display.getSystemColor(SWT.COLOR_WHITE);
	red = display.getSystemColor(SWT.COLOR_RED);
	green = display.getSystemColor(SWT.COLOR_GREEN);
	blue = display.getSystemColor(SWT.COLOR_BLUE);
    }

    final static String BEFORE_FILE = "before.png";
    final static String AFTER_FILE = "after.png";

    final static String[] filenames = new String[] { BEFORE_FILE, AFTER_FILE };

    private Image createImage(Color c, int width, int height) {

	Image img = new Image(display, 5, 5);
	NativeGC ngc = new NativeGC(img);
	ngc.setBackground(c);
	ngc.fillRectangle(0, 0, width, height);

	ngc.dispose();

	return img;

    }

    @Test
    public void test_drawImage() throws IOException {

	comparedrawImage_coloredImage(red);
	comparedrawImage_coloredImage(green);
	comparedrawImage_coloredImage(blue);
	comparedrawImage_coloredImage(white);

	Color c = new Color(150, 0, 0, 0);
	comparedrawImage_coloredImage(c);
	c.dispose();

	c = new Color(0, 150, 0, 50);
	comparedrawImage_coloredImage(c);
	c.dispose();

	c = new Color(0, 150, 0, 255);
	comparedrawImage_coloredImage(c);
	c.dispose();

	Image info = display.getSystemImage(SWT.ICON_INFORMATION);
	comparedrawImage_systemImage(info);
	info.dispose();

    }

    private void comparedrawImage_systemImage(Image input) throws IOException {

	deleteFiles();

	try {

	    {

		Image image = new Image(display, 300, 200);
		GC gc = new GC(image);
		gc.drawImage(input, 0, 0);
		gc.dispose();

		ImageLoader s = new ImageLoader();
		s.data = new ImageData[] { image.getImageData() };
		s.save(BEFORE_FILE,  SWT.IMAGE_PNG);



	    }

	    {

		Image image = new Image(display, 300, 200);
		GC gc = new GC(image);
		SkijaGC sgc = new SkijaGC((NativeGC)gc.innerGC, null);

		sgc.drawImage(input, 0, 0);
		sgc.commit();

		sgc.dispose();
		gc.dispose();


		ImageLoader s = new ImageLoader();
		s.data = new ImageData[] { image.getImageData() };
		s.save(AFTER_FILE,  SWT.IMAGE_PNG);

	    }

	    assertFilesEqual(BEFORE_FILE, AFTER_FILE);

	} finally {
	    deleteFiles();
	}

    }

    private void comparedrawImage_coloredImage(Color c) throws IOException {

	deleteFiles();

	try {

	    ImageLoader saver = new ImageLoader();

	    {
		var redImg = createImage(c, WIDTH, HEIGHT);
		saver.data = new ImageData[] { redImg.getImageData() };
		saver.save(BEFORE_FILE, SWT.IMAGE_PNG);
		redImg.dispose();
	    }

	    // -------------------------
	    {
		Image redImg = createImage(c, WIDTH, HEIGHT);
		Image baseImg = createImage(white, WIDTH, HEIGHT);
		NativeGC nativeGC = new NativeGC(baseImg);

		SkijaGC sgc = new SkijaGC(nativeGC, null);
		sgc.drawImage(redImg, 0, 0);
		sgc.commit();

		saver.data = new ImageData[] { baseImg.getImageData() };
		saver.save(AFTER_FILE, SWT.IMAGE_PNG);
	    }

	    assertFilesEqual(BEFORE_FILE, AFTER_FILE);

	} finally {
	    deleteFiles();
	}

    }

    private static void deleteFiles() {
	for (var s : filenames) {
	    File f = new File(s);
	    if (f.exists()) {
		f.delete();
	    }
	}
    }

    private void assertFilesEqual(String beforeFile, String afterFile) throws IOException {

	byte[] b1 = Files.readAllBytes(Path.of(beforeFile));
	byte[] b2 = Files.readAllBytes(Path.of(afterFile));

	Assert.assertArrayEquals(b1, b2);

    }

    /* custom */
    Display display;
}
