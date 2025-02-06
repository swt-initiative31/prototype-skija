package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

/**
 * @author Thomas Singer
 */
public interface IMeasureContext {
	Point textExtent(String text, int drawFlags);
}
