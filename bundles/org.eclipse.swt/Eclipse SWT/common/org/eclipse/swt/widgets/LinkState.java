package org.eclipse.swt.widgets;

/**
 * @author Thomas Singer
 */
final class LinkState extends ControlState {
	private final Link link;

	public LinkState(Link link) {
		this.link = link;
	}

	@Override
	protected Control getControl() {
		return link;
	}
}
