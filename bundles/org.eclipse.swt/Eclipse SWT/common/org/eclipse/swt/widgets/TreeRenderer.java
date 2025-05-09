package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

public abstract class TreeRenderer extends ControlRenderer {

	protected final Tree tree;

	protected TreeRenderer(Tree tree) {
		super(tree);
		this.tree = tree;
	}


}
