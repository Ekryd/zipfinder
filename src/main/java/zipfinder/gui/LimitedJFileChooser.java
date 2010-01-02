package zipfinder.gui;

import java.awt.Component;
import java.awt.Container;

import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class LimitedJFileChooser extends JFileChooser {
	private static final long serialVersionUID = 2169816824568081017L;

	public void customize() {
		setControlButtonsAreShown(false);
		setFileHidingEnabled(false);
		removeExcessButtons(this, getComponents());
		removeExcessFields(this, getComponents());
	}

	private void removeExcessButtons(final Container parent, final Component[] components) {
		boolean first = true;
		for (Component component : components) {
			if (component instanceof AbstractButton) {
				if (first) {
					first = false;
				} else {
					AbstractButton button = (AbstractButton) component;
					parent.remove(button);
				}
			}
			if (component instanceof Container) {
				Container container = (Container) component;
				removeExcessButtons(container, container.getComponents());
			}
		}
	}

	private void removeExcessFields(final Container parent, final Component[] components) {
		final Component component = components[components.length - 1];
		if (component instanceof JPanel) {
			parent.remove(component);
		} else {
			throw new IllegalStateException("Layout is not as expected");
		}
	}
}
