/*******************************************************************************
 * Copyright (c) 2025 ETAS GmbH and others, all rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ETAS GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class TransformAPIEdgeCasesTest {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Transform Edge Cases & Performance Test");
        shell.setSize(900, 700);

        shell.addListener(SWT.Paint, event -> {
			GC gc = Drawing.createGraphicsContext(event.gc, shell);
			// GC gc = event.gc;
            // Edge Case 1: Identity transforms
            testIdentityTransforms(gc, display, 10, 10);

            // Edge Case 2: Zero and negative scaling
            testScalingEdgeCases(gc, display, 200, 10);

            // Edge Case 3: Large rotation angles
            testLargeRotations(gc, display, 400, 10);

            // Edge Case 4: Very small scaling factors
            testMicroScaling(gc, display, 600, 10);

            // Edge Case 5: Transform with clipping
            testTransformWithClipping(gc, display, 10, 150);

            // Edge Case 6: Rapid transform changes
            testRapidTransformChanges(gc, display, 300, 150);

            // Edge Case 7: Matrix precision test
            testMatrixPrecision(gc, display, 10, 300);

            // Edge Case 8: Transform state management
            testTransformStateManagement(gc, display, 400, 300);

            // Performance Test: Many small transforms
            performanceTestManyTransforms(gc, display, 10, 450);

            gc.commit();
        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    // Test identity and null transforms
    private static void testIdentityTransforms(GC gc, Display display, int x, int y) {
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Identity Transform Tests:", x, y - 15, true);

        // Test 1: Identity transform should have no effect
        Transform identity = new Transform(display);
        gc.setTransform(identity);
        gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
        gc.fillRectangle(x, y, 50, 30);
        gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        gc.drawString("Identity", x + 2, y + 5, true);

        // Test 2: Null transform (reset)
        gc.setTransform(null);
        gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
        gc.fillRectangle(x + 60, y, 50, 30);
        gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        gc.drawString("Null Reset", x + 62, y + 5, true);

        // Test 3: Multiple identity operations
        Transform t = new Transform(display);
        t.translate(0, 0);  // No translation
        t.rotate(0);        // No rotation
        t.scale(1, 1);      // No scaling
        gc.setTransform(t);
        gc.setBackground(display.getSystemColor(SWT.COLOR_MAGENTA));
        gc.fillRectangle(x, y + 40, 50, 30);
        gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        gc.drawString("Multi-Id", x + 2, y + 45, true);

        gc.setTransform(null);
        identity.dispose();
        t.dispose();
    }

    // Test scaling edge cases
    private static void testScalingEdgeCases(GC gc, Display display, int x, int y) {
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Scaling Edge Cases:", x, y - 15, true);

        // Reference rectangle
        gc.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
        gc.fillRectangle(x, y, 30, 20);
        gc.drawString("Ref", x + 2, y + 2, true);

        // Test very small positive scaling
        Transform smallScale = new Transform(display);
        smallScale.translate(x + 40, y + 10);
        smallScale.scale(0.1f, 0.1f);
        smallScale.translate(-15, -10);
        gc.setTransform(smallScale);
        gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
        gc.fillRectangle(0, 0, 30, 20);
        gc.setTransform(null);
        gc.drawString("0.1x", x + 40, y + 25, true);

        // Test negative scaling (flip)
        Transform negScale = new Transform(display);
        negScale.translate(x + 80, y + 10);
        negScale.scale(-1, 1);  // Horizontal flip
        negScale.translate(-15, -10);
        gc.setTransform(negScale);
        gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
        gc.fillRectangle(0, 0, 30, 20);
        gc.setTransform(null);
        gc.drawString("Flip-X", x + 80, y + 25, true);

        // Test very large scaling
        Transform largeScale = new Transform(display);
        largeScale.translate(x + 120, y + 10);
        largeScale.scale(3.0f, 0.5f);
        largeScale.translate(-5, -10);
        gc.setTransform(largeScale);
        gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
        gc.fillRectangle(0, 0, 10, 20);
        gc.setTransform(null);
        gc.drawString("3x,0.5x", x + 120, y + 25, true);

        smallScale.dispose();
        negScale.dispose();
        largeScale.dispose();
    }

    // Test large rotation angles
    private static void testLargeRotations(GC gc, Display display, int x, int y) {
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Large Rotation Tests:", x, y - 15, true);

        float[] angles = {90, 180, 270, 360, 450, 720}; // Including > 360°
        Color[] colors = {
            display.getSystemColor(SWT.COLOR_RED),
            display.getSystemColor(SWT.COLOR_GREEN),
            display.getSystemColor(SWT.COLOR_BLUE),
            display.getSystemColor(SWT.COLOR_YELLOW),
            display.getSystemColor(SWT.COLOR_MAGENTA),
            display.getSystemColor(SWT.COLOR_CYAN)
        };

        for (int i = 0; i < angles.length; i++) {
            Transform rot = new Transform(display);
            rot.translate(x + 30 + (i % 3) * 60, y + 30 + (i / 3) * 60);
            rot.rotate(angles[i]);
            rot.translate(-15, -10);

            gc.setTransform(rot);
            gc.setBackground(colors[i]);
            gc.fillRectangle(0, 0, 30, 20);
            gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
            gc.drawString(String.valueOf((int)angles[i]) + "°", 2, 2, true);

            rot.dispose();
        }

        gc.setTransform(null);
    }

    // Test very small scaling factors
    private static void testMicroScaling(GC gc, Display display, int x, int y) {
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Micro Scaling:", x, y - 15, true);

        float[] scales = {0.01f, 0.001f, 10.0f, 100.0f};

        for (int i = 0; i < scales.length; i++) {
            Transform micro = new Transform(display);
            micro.translate(x + 20 + i * 40, y + 20);
            micro.scale(scales[i], scales[i]);
            micro.translate(-50, -25);

            gc.setTransform(micro);
            gc.setBackground(display.getSystemColor(SWT.COLOR_RED + i));
            gc.fillRectangle(0, 0, 100, 50);

            micro.dispose();
        }

        gc.setTransform(null);

        // Labels
        String[] labels = {"0.01x", "0.001x", "10x", "100x"};
        for (int i = 0; i < labels.length; i++) {
            gc.drawString(labels[i], x + i * 40, y + 50, true);
        }
    }

    // Test transform with clipping
    private static void testTransformWithClipping(GC gc, Display display, int x, int y) {
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Transform + Clipping:", x, y - 15, true);

        // Set clipping region
        Rectangle clipRect = new Rectangle(x + 20, y + 20, 80, 60);
        gc.setClipping(clipRect);

        // Draw clipping boundary
        gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
        gc.drawRectangle(clipRect);
        gc.drawString("Clip Region", x + 25, y + 5, true);

        // Apply transform and draw shapes that extend beyond clip
        Transform t = new Transform(display);
        t.translate(x + 60, y + 50);
        t.rotate(45);
        t.translate(-40, -30);

        gc.setTransform(t);
        gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
        gc.fillRectangle(0, 0, 80, 60);
        gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        gc.drawString("Clipped Transform", 5, 5, true);

        // Reset
        gc.setTransform(null);
        gc.setClipping((Rectangle)null);
        t.dispose();
    }

    // Test rapid transform changes
    private static void testRapidTransformChanges(GC gc, Display display, int x, int y) {
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Rapid Transform Changes:", x, y - 15, true);

        // Simulate rapid animation-like transform changes
        for (int frame = 0; frame < 20; frame++) {
            Transform t = new Transform(display);
            float angle = frame * 18; // 18 degrees per frame
            float scale = 0.5f + (frame % 10) * 0.1f;

            t.translate(x + 50 + frame * 3, y + 30);
            t.rotate(angle);
            t.scale(scale, scale);
            t.translate(-5, -5);

            gc.setTransform(t);

            Color color = display.getSystemColor(SWT.COLOR_RED + (frame % 6));
            gc.setBackground(color);
            gc.fillRectangle(0, 0, 10, 10);

            t.dispose();
        }

        gc.setTransform(null);
    }

    // Test matrix precision
    private static void testMatrixPrecision(GC gc, Display display, int x, int y) {
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Matrix Precision Test:", x, y - 15, true);

        // Test accumulating small transformations
        Transform accumulated = new Transform(display);
        accumulated.translate(x + 50, y + 50);

        // Apply many small rotations
        for (int i = 0; i < 36; i++) {
            Transform smallRot = new Transform(display);
            smallRot.rotate(1); // 1 degree
            accumulated.multiply(smallRot);
            smallRot.dispose();
        }

        gc.setTransform(accumulated);
        gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
        gc.fillRectangle(-20, -10, 40, 20);
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("36x1°", -15, -5, true);

        // Compare with single 36-degree rotation
        gc.setTransform(null);
        Transform single = new Transform(display);
        single.translate(x + 150, y + 50);
        single.rotate(36);
        gc.setTransform(single);
        gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
        gc.fillRectangle(-20, -10, 40, 20);
        gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        gc.drawString("1x36°", -15, -5, true);

        gc.setTransform(null);
        accumulated.dispose();
        single.dispose();
    }

    // Test transform state management
    private static void testTransformStateManagement(GC gc, Display display, int x, int y) {
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Transform State Management:", x, y - 15, true);

        // Test setting same transform multiple times
        Transform t1 = new Transform(display);
        t1.translate(x + 30, y + 30);
        t1.rotate(30);

		for (int i = 0; i < 8; i++) {
            gc.setTransform(t1); // Set same transform multiple times
            gc.setBackground(display.getSystemColor(SWT.COLOR_RED + i));
            gc.fillRectangle(i * 20, 0, 15, 15);
        }

        // Test getting current transform
        Transform current = new Transform(display);
        gc.getTransform(current);

        // Verify we can use the retrieved transform
        gc.setTransform(current);
        gc.setBackground(display.getSystemColor(SWT.COLOR_MAGENTA));
        gc.fillRectangle(60, 0, 15, 15);

        gc.setTransform(null);
        t1.dispose();
        current.dispose();
    }

    // Performance test with many transforms
    private static void performanceTestManyTransforms(GC gc, Display display, int x, int y) {
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Performance Test (1000 transforms):", x, y - 15, true);

        long startTime = System.currentTimeMillis();

        // Create and apply 1000 small transforms
        for (int i = 0; i < 1000; i++) {
            if (i % 100 == 0) { // Only draw every 100th for visibility
                Transform t = new Transform(display);
                t.translate(x + (i / 100) * 30, y + 20);
                t.rotate(i * 0.36f); // Small incremental rotation
                t.scale(0.5f + (i % 50) * 0.01f, 0.5f + (i % 50) * 0.01f);

                gc.setTransform(t);
                gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE + (i % 6)));
                gc.fillRectangle(-5, -5, 10, 10);

                t.dispose();
            }
        }

        long endTime = System.currentTimeMillis();
        gc.setTransform(null);
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Time: " + (endTime - startTime) + "ms", x, y + 60, true);
    }
}