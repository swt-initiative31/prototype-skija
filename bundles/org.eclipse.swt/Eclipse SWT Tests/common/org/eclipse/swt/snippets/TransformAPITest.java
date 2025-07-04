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

public class TransformAPITest {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Transform Test Cases");
        shell.setSize(800, 600);

        shell.addListener(SWT.Paint, event -> {
			GC gc = Drawing.createGraphicsContext(event.gc, shell);
			// GC gc = event.gc;

            // Test Case 1: Multiple consecutive transforms
            testConsecutiveTransforms(gc, display, 10, 10);

            // Test Case 2: Scale + Rotation + Translation
            testScaleRotateTranslate(gc, display, 200, 10);

            // Test Case 3: Nested transforms with save/restore
            testNestedTransforms(gc, display, 400, 10);

            // Test Case 4: Transform with text rendering
            testTransformWithText(gc, display, 10, 150);

            // Test Case 5: Transform with paths
            testTransformWithPaths(gc, display, 300, 150);

            // Test Case 6: Transform with images (if available)
            testTransformWithShapes(gc, display, 10, 300);

            // Test Case 7: Inverse transforms
            testInverseTransforms(gc, display, 400, 300);

            // Test Case 8: Transform chaining
            testTransformChaining(gc, display, 200, 450);

			gc.commit();
        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    // Test Case 1: Multiple consecutive transforms
    private static void testConsecutiveTransforms(GC gc, Display display, int x, int y) {
        // Reference rectangle
        gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
        gc.fillRectangle(x, y, 80, 40);
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Reference", x + 5, y + 5, true);

        // First transform: rotate 30 degrees
        Transform t1 = new Transform(display);
        t1.translate(x + 40, y + 70);
        t1.rotate(30);
        t1.translate(-40, -20);
        gc.setTransform(t1);

        gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
        gc.fillRectangle(0, 0, 80, 40);
        gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        gc.drawString("Rotate 30°", 5, 5, true);

        // Second transform: additional rotation
        Transform t2 = new Transform(display);
        t2.translate(40, 20);
        t2.rotate(15); // Additional 15 degrees
        t2.translate(-40, -20);
        gc.setTransform(t2);

        gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
        gc.fillRectangle(0, 0, 80, 40);
        gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        gc.drawString("+ 15°", 5, 5, true);

        gc.setTransform(null);
        t1.dispose();
        t2.dispose();
    }

    // Test Case 2: Scale + Rotation + Translation
    private static void testScaleRotateTranslate(GC gc, Display display, int x, int y) {
        // Reference
        gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
        gc.fillRectangle(x, y, 50, 30);
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Original", x + 2, y + 5, true);

        Transform transform = new Transform(display);
        transform.translate(x + 75, y + 50); // Move to center
        transform.scale(1.5f, 2.0f);         // Scale 1.5x horizontally, 2x vertically
        transform.rotate(45);                // Rotate 45 degrees
        transform.translate(-25, -15);       // Center the rectangle

        gc.setTransform(transform);
        gc.setBackground(display.getSystemColor(SWT.COLOR_MAGENTA));
        gc.fillRectangle(0, 0, 50, 30);
        gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        gc.drawString("Scaled+Rotated", 2, 5, true);

        gc.setTransform(null);
        transform.dispose();
    }

    // Test Case 3: Nested transforms (simulating save/restore)
    private static void testNestedTransforms(GC gc, Display display, int x, int y) {
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Nested Transforms:", x, y - 15, true);

        // Outer transform
        Transform outer = new Transform(display);
        outer.translate(x + 50, y + 50);
        outer.rotate(30);

        gc.setTransform(outer);
        gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
        gc.fillRectangle(-25, -25, 50, 50);
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Outer", -20, -5, true);

        // Get current transform for nesting
        Transform current = new Transform(display);
        gc.getTransform(current);

        // Apply additional transform
        Transform additional = new Transform(display);
        additional.translate(30, 0);
        additional.rotate(45);
        additional.scale(0.7f, 0.7f);

        // Combine transforms
        current.multiply(additional);
        gc.setTransform(current);

        gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
        gc.fillRectangle(-15, -15, 30, 30);
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Inner", -10, -5, true);

        gc.setTransform(null);
        outer.dispose();
        current.dispose();
        additional.dispose();
    }

    // Test Case 4: Transform with text rendering
    private static void testTransformWithText(GC gc, Display display, int x, int y) {
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Text Transform Tests:", x, y - 15, true);

        String[] texts = {"Normal", "Rotated", "Scaled", "Skewed"};
        Color[] colors = {
            display.getSystemColor(SWT.COLOR_BLACK),
            display.getSystemColor(SWT.COLOR_RED),
            display.getSystemColor(SWT.COLOR_GREEN),
            display.getSystemColor(SWT.COLOR_BLUE)
        };

        for (int i = 0; i < texts.length; i++) {
            Transform t = new Transform(display);

            switch (i) {
                case 0: // Normal
                    t.translate(x, y + i * 25);
                    break;
                case 1: // Rotated
                    t.translate(x + 80, y + i * 25);
                    t.rotate(30);
                    break;
                case 2: // Scaled
                    t.translate(x + 160, y + i * 25);
                    t.scale(1.5f, 1.2f);
                    break;
                case 3: // Skewed (shear)
                    t.translate(x + 240, y + i * 25);
                    t.setElements(1, 0, 0.3f, 1, 0, 0); // Manual shear
                    break;
            }

            gc.setTransform(t);
            gc.setForeground(colors[i]);
            gc.drawString(texts[i], 0, 0, true);

            t.dispose();
        }

        gc.setTransform(null);
    }

    // Test Case 5: Transform with paths
    private static void testTransformWithPaths(GC gc, Display display, int x, int y) {
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Path Transforms:", x, y - 15, true);

        // Create a star path
        Path starPath = new Path(display);
        float[] starPoints = createStarPoints(0, 0, 20, 10, 5);
        starPath.moveTo(starPoints[0], starPoints[1]);
        for (int i = 2; i < starPoints.length; i += 2) {
            starPath.lineTo(starPoints[i], starPoints[i + 1]);
        }
        starPath.close();

        // Draw star with different transforms
        Transform[] transforms = new Transform[4];
        for (int i = 0; i < 4; i++) {
            transforms[i] = new Transform(display);
            transforms[i].translate(x + 30 + i * 60, y + 30);
            transforms[i].rotate(i * 45);
            transforms[i].scale(1 + i * 0.3f, 1 + i * 0.3f);

            gc.setTransform(transforms[i]);
            gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW + i));
            gc.fillPath(starPath);
            gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
            gc.drawPath(starPath);
        }

        gc.setTransform(null);
        starPath.dispose();
        for (Transform t : transforms) {
            t.dispose();
        }
    }

    // Test Case 6: Transform with various shapes
    private static void testTransformWithShapes(GC gc, Display display, int x, int y) {
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Shape Transforms:", x, y - 15, true);

        Transform transform = new Transform(display);
        transform.translate(x + 50, y + 80);
        transform.rotate(25);

        gc.setTransform(transform);

        // Draw various shapes
        gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
        gc.fillOval(-30, -30, 60, 40);

        gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
        gc.fillRoundRectangle(40, -20, 60, 40, 10, 10);

        gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
        gc.fillArc(110, -30, 60, 60, 45, 180);

        // Draw polygon
        int[] polygon = {200, 0, 220, -20, 240, 0, 220, 20};
        gc.setBackground(display.getSystemColor(SWT.COLOR_MAGENTA));
        gc.fillPolygon(polygon);

        gc.setTransform(null);
        transform.dispose();
    }

    // Test Case 7: Inverse transforms
    private static void testInverseTransforms(GC gc, Display display, int x, int y) {
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Inverse Transform Test:", x, y - 15, true);

        // Original rectangle
        gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
        gc.fillRectangle(x, y, 50, 30);
        gc.drawString("Original", x + 2, y + 5, true);

        // Apply transform
        Transform forward = new Transform(display);
        forward.translate(x + 100, y + 50);
        forward.rotate(45);
        forward.scale(1.5f, 1.5f);
        forward.translate(-25, -15);

        gc.setTransform(forward);
        gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
        gc.fillRectangle(0, 0, 50, 30);
        gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        gc.drawString("Transformed", 2, 5, true);

        // Apply inverse
        Transform inverse = new Transform(display);
		forward.invert();
        gc.setTransform(inverse);

        gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
        gc.fillRectangle(x + 200, y, 50, 30);
        gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        gc.drawString("Inverse", x + 202, y + 5, true);

        gc.setTransform(null);
        forward.dispose();
        inverse.dispose();
    }

    // Test Case 8: Transform chaining
    private static void testTransformChaining(GC gc, Display display, int x, int y) {
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawString("Transform Chaining:", x, y - 15, true);

        Transform base = new Transform(display);
        base.translate(x + 50, y + 50);

        for (int i = 0; i < 5; i++) {
            Transform step = new Transform(display);
            step.translate(i * 30, 0);
            step.rotate(i * 15);
            step.scale(1 - i * 0.1f, 1 - i * 0.1f);

            // Chain the transforms
            Transform combined = new Transform(display);
            combined.multiply(base);
            combined.multiply(step);

            gc.setTransform(combined);
            gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW + (i % 6)));
            gc.fillRectangle(-10, -10, 20, 20);
            gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
            gc.drawString(String.valueOf(i + 1), -5, -5, true);

            step.dispose();
            combined.dispose();
        }

        gc.setTransform(null);
        base.dispose();
    }

    // Helper method to create star points
    private static float[] createStarPoints(float centerX, float centerY, float outerRadius, float innerRadius, int points) {
        float[] coords = new float[points * 4];
        double angleStep = Math.PI / points;

        for (int i = 0; i < points; i++) {
            // Outer point
            double outerAngle = i * 2 * angleStep - Math.PI / 2;
            coords[i * 4] = centerX + (float) (outerRadius * Math.cos(outerAngle));
            coords[i * 4 + 1] = centerY + (float) (outerRadius * Math.sin(outerAngle));

            // Inner point
            double innerAngle = (i * 2 + 1) * angleStep - Math.PI / 2;
            coords[i * 4 + 2] = centerX + (float) (innerRadius * Math.cos(innerAngle));
            coords[i * 4 + 3] = centerY + (float) (innerRadius * Math.sin(innerAngle));
        }

        return coords;
    }
}