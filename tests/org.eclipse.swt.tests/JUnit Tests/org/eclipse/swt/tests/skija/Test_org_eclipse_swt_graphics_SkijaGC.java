package org.eclipse.swt.tests.skija;

import static org.junit.Assert.assertEquals;

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
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.SkijaGC;
import org.eclipse.swt.widgets.Display;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Test_org_eclipse_swt_graphics_SkijaGC {

    static final int WIDTH = 5;
    static final int HEIGHT = 5;

    static final int ERROR_RANGE = 2;

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
	if (info != null) {
	    comparedrawImage_systemImage(info);
	    info.dispose();
	}

    }

    private void comparedrawImage_systemImage(Image input) throws IOException {

	deleteFiles();

	ImageLoader saver = new ImageLoader();

	var inputData = input.getImageData();

	var width = inputData.width;
	var height = inputData.height;

	try{

	{

	    Image image = new Image(display, width, height);
	    GC gc = new GC(image);
	    gc.drawImage(input, 0, 0);
	    gc.dispose();

	    saver.data = new ImageData[]{image.getImageData()};
	    saver.save(BEFORE_FILE, SWT.IMAGE_PNG);
	    image.dispose();

	}

	{

	    Image image = new Image(display, width, height);
	    GC gc = new GC(image);
	    SkijaGC sgc = new SkijaGC((NativeGC) gc.innerGC, null);

	    sgc.drawImage(input, 0, 0);
	    sgc.commit();
	    sgc.dispose();
	    gc.dispose();

	    saver.data = new ImageData[]{image.getImageData()};
	    saver.save(AFTER_FILE, SWT.IMAGE_PNG);
	    image.dispose();

	}

	assertFilesSimilar(BEFORE_FILE, AFTER_FILE );

	}finally {
	    deleteFiles();
	}

    }

    private void assertFilesSimilar(String beforeFile, String afterFile) {

	Image i1 = new Image(display, beforeFile);
	Image i2 = new Image(display, afterFile);

	compareImageData(i1.getImageData(), i2.getImageData());

	i1.dispose();
	i2.dispose();


    }

    private static boolean comparePaletteData(PaletteData palette1, PaletteData palette2) {
        if (palette1 == palette2) {
            return true;
        }

        if (palette1 == null || palette2 == null) {
            return false;
        }

        if (palette1.isDirect != palette2.isDirect) {
            return false;
        }

        if (palette1.isDirect) {
            return palette1.redMask == palette2.redMask &&
                   palette1.greenMask == palette2.greenMask &&
                   palette1.blueMask == palette2.blueMask;
        } else {
            RGB[] colors1 = palette1.getRGBs();
            RGB[] colors2 = palette2.getRGBs();

            if (colors1.length != colors2.length) {
                return false;
            }

            for (int i = 0; i < colors1.length; i++) {
                if (!colors1[i].equals(colors2[i])) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean compareImageData(ImageData img1, ImageData img2) {
        if (img1 == img2) {
            return true;
        }

        if (img1 == null || img2 == null) {
            return false;
        }

        if (img1.width != img2.width || img1.height != img2.height ||
            img1.depth != img2.depth || img1.bytesPerLine != img2.bytesPerLine) {
            return false;
        }

        for (int y = 0; y < img1.height; y++) {
            for (int x = 0; x < img1.width; x++) {
                if (img1.getPixel(x, y) != img2.getPixel(x, y)) {
                    return false;
                }
                if (img1.getAlpha(x, y) != img2.getAlpha(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void validateImageData(ImageData d1, ImageData d2) {

	if(comparePaletteData(d1.palette,d2.palette))
	    validateArrays(d1.data,d2.data);

	else
	    throw new IllegalStateException("Invalid compare types");

    }

    private void validateArrays(byte[] bytes1, byte[] bytes2) {

	assertEquals(bytes1.length, bytes2.length);

	for (int r = 0; r < bytes1.length; r++) {

	    if (Math.abs(bytes1[r] - bytes2[r]) > ERROR_RANGE) {
		if (Math.abs(bytes1[r] + bytes2[r]) > ERROR_RANGE)
		    Assert.fail("Index: " + r + " Left: " + bytes1[r] + " Right: " + bytes2[r]);
	    }

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

	    assertFilesSimilar(BEFORE_FILE, AFTER_FILE);

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
