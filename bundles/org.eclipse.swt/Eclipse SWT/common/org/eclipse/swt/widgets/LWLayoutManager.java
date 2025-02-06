package org.eclipse.swt.widgets;

import java.util.List;

import org.eclipse.swt.graphics.*;

/**
 * @author Thomas Singer
 */
public interface LWLayoutManager {
	Point getPreferredSize(List<LWControl> children, IMeasureContext measureContext);

	void layout(List<LWControl> children, Point size, IMeasureContext measureContext);
}
