package zipfinder.gui;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class RecentStringsJTextField extends JTextField {
	private static final long serialVersionUID = -2897510304242074174L;
	private final String[] recentStringsToFind;

	public RecentStringsJTextField(final String[] recentStringsToFind) {
		this.recentStringsToFind = recentStringsToFind;
	}

	@Override
	protected Document createDefaultModel() {
		return new UpperCaseDocument();
	}

	private String getCurrentText() {
		return super.getText();
	}

	class UpperCaseDocument extends PlainDocument {
		private static final long serialVersionUID = -4425263923601520435L;

		@Override
		public void replace(final int offset, final int length, final String text, final AttributeSet attrs)
				throws BadLocationException {
			super.replace(offset, length, text, attrs);
			if (text == null || text.length() == 0) {
				return;
			}
			final String currentText = getCurrentText();
			for (String recentString : recentStringsToFind) {
				if (recentString.startsWith(currentText) && recentString.length() > currentText.length()) {
					super.replace(0, currentText.length(), recentString, attrs);
					selectAll();
					setSelectionStart(offset + 1);
					return;
				}
			}
		}
	}
}
