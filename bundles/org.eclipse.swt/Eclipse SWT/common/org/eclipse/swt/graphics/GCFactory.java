package org.eclipse.swt.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class GCFactory {

	public static GC getGraphicsContext(Control control) {

		if (SWT.USE_SKIJA) {
			return new SkijaGC(control);
		}

		return new GC(control);

	}

	public static GC createChildGC(GC parentGC, Control c) {

		if (SWT.USE_SKIJA) {

			if (parentGC instanceof SkijaGC s)
				return new SkijaGC(s, c);

			throw new IllegalArgumentException("Expecting SkijaGC not gc of type: " + parentGC.getClass());
		}

		return new GC(c);

	}

}
