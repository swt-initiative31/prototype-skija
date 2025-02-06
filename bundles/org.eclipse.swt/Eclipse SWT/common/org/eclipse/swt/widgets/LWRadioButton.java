package org.eclipse.swt.widgets;

import java.util.*;
import java.util.List;

import org.eclipse.swt.graphics.*;

/**
 * @author Thomas Singer
 */
public class LWRadioButton extends LWAbstractButton {

	private final Group group;

	private LWRadioButtonUI ui;

	public LWRadioButton(String text, Group group, LWContainer container) {
		super(0, container);
		this.group = group;
		group.add(this);
		setText(text);

		ui = new LWRadioButtonUI(this);
	}

	public LWRadioButton(int style, Group group) {
		super(style);
		this.group = Objects.requireNonNull(group);
		group.add(this);

		ui = new LWRadioButtonUI(this);
	}

	@Override
	public String toString() {
		return "RadioButton '" + getText() + "'";
	}

	@Override
	public Point getPreferredSize(IMeasureContext measureContext) {
		return ui.getPreferredSize(measureContext);
	}

	@Override
	public void paint(GC gc, IColorProvider colorProvider) {
		ui.paint(gc, colorProvider);
	}

	@Override
	protected void handleSelection() {
		setSelected();
	}

	@Override
	public String getAccessibleText() {
		final List<LWRadioButton> buttons = group.getRadioButtons();
		final int index = buttons.indexOf(this) + 1;

		final StringBuilder buffer = new StringBuilder();
		buffer.append(getText());
		buffer.append(" radio button checked.\r\n");
		buffer.append(index);
		buffer.append(" of ");
		buffer.append(buttons.size());
		buffer.append(".\r\n ");
		buffer.append("To change the selection press Up or Down Arrow.");
		return buffer.toString();
	}

	public void setSelected() {
		final List<LWRadioButton> buttons = group.getRadioButtons();
		for (LWRadioButton button : buttons) {
			if (button != this) {
				button.setSelected(false);
			}
		}

		setSelected(true);
	}

	public interface Group {
		void add(LWRadioButton radioButton);

		List<LWRadioButton> getRadioButtons();
	}

	public static class DefaultGroup implements Group {
		private final List<LWRadioButton> buttons = new ArrayList<>();

		@Override
		public void add(LWRadioButton button) {
			if (buttons.contains(button)) {
				throw new IllegalArgumentException("duplicate");
			}

			buttons.add(button);
		}

		@Override
		public List<LWRadioButton> getRadioButtons() {
			return Collections.unmodifiableList(buttons);
		}
	}
}
