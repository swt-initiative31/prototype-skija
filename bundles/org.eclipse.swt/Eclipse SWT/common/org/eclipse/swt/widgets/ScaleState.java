package org.eclipse.swt.widgets;

public final class ScaleState extends ControlState {

	private final Scale scale;

	public ScaleState(Scale scale) {
		this.scale = scale;
	}

	@Override
	protected Control getControl() {
		return scale;
	}
}
