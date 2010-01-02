package zipfinder.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

/*
 * * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class SwingGui {
	private static final int TEXT_ROWS = 10;
	private static final int INSET = 5;
	private static final int MAX_LINES = 500;
	private static final String SEARCH_LABEL = "Search";
	private static final long serialVersionUID = -6412553081078284251L;
	private JFrame frame;
	private JTextArea console;
	private RecentStringsJTextField textToFindField;
	private JPanel lowerPanel;
	private JPanel upperPanel;
	private JSplitPane jSplitPane1;
	private JComboBox directoryComboBox;
	private LimitedJFileChooser directoryTree;
	private final String[] recentDirectories;
	private final String[] recentStringsToFind;
	private final Insets insets = new Insets(INSET, INSET, INSET, INSET);
	private SearchButtonListener searchButtonListener;
	private JButton searchButton;
	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param recentDirectories
	 * @param recentStringsToFind
	 */
	public SwingGui(final String[] recentDirectories, final String[] recentStringsToFind) {
		this.recentDirectories = recentDirectories;
		this.recentStringsToFind = recentStringsToFind;
	}

	public void addListeners() {
		directoryTree.addPropertyChangeListener(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY,
				new PropertyChangeListener() {
					public void propertyChange(final PropertyChangeEvent evt) {
						final String oldValue = directoryComboBox.getSelectedItem() == null ? "" : directoryComboBox
								.getSelectedItem().toString();
						if (!oldValue.equals(directoryTree.getSelectedFile())) {
							directoryComboBox.setSelectedItem(directoryTree.getSelectedFile());
						}
					}
				});
		directoryComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				final File file = new File(directoryComboBox.getSelectedItem().toString());
				if (file.canRead()) {
					directoryTree.setSelectedFile(file);
				}
			}
		});
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (searchButton.getText().equals(SEARCH_LABEL)) {
					searchButtonListener.performSearch();
				} else {
					searchButtonListener.performStopSearch();
				}
			}
		});
	}

	public void addToConsole(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				while (console.getLineCount() > SwingGui.MAX_LINES) {
					try {
						int offset = console.getLineEndOffset(0);
						console.getDocument().remove(0, offset);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
				console.append(text + "\n");
			}
		});
	}

	public void createComponents() {
		// frame
		frame = new JFrame("ZipFinder");
		BorderLayout thisLayout = new BorderLayout();
		frame.getContentPane().setLayout(thisLayout);
		// jSplitPanel
		jSplitPane1 = new JSplitPane();
		frame.getContentPane().add(jSplitPane1, BorderLayout.CENTER);
		jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		jSplitPane1.setResizeWeight(1.0);
		jSplitPane1.setRequestFocusEnabled(false);
		// upperPanel
		upperPanel = new JPanel();
		jSplitPane1.add(upperPanel, JSplitPane.TOP);
		GridBagLayout upperPanelLayout = new GridBagLayout();
		upperPanelLayout.columnWidths = new int[] { INSET, INSET };
		upperPanelLayout.rowHeights = new int[] { INSET, INSET };
		upperPanelLayout.columnWeights = new double[] { 1, 0 };
		upperPanelLayout.rowWeights = new double[] { 0, 1 };
		upperPanel.setLayout(upperPanelLayout);
		// directoryComboBox
		ComboBoxModel directoryComboBoxModel = new DefaultComboBoxModel(recentDirectories);
		directoryComboBox = new JComboBox();
		upperPanel.add(directoryComboBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, insets, 0, 0));
		directoryComboBox.setModel(directoryComboBoxModel);
		directoryComboBox.setEditable(true);
		// directoryTree
		directoryTree = new LimitedJFileChooser();
		upperPanel.add(directoryTree, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, insets, 0, 0));
		directoryTree.customize();
		directoryTree.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (directoryComboBox.getSelectedItem() != null)
			directoryTree.setSelectedFile(new File(directoryComboBox.getSelectedItem().toString()));
		// lowerPanel
		lowerPanel = new JPanel();
		GridBagLayout lowerPanelLayout = new GridBagLayout();
		lowerPanelLayout.columnWidths = new int[] { INSET, INSET };
		lowerPanelLayout.rowHeights = new int[] { INSET, INSET };
		lowerPanelLayout.columnWeights = new double[] { 1, 0 };
		lowerPanelLayout.rowWeights = new double[] { 0, 1 };
		jSplitPane1.add(lowerPanel, JSplitPane.BOTTOM);
		lowerPanel.setLayout(lowerPanelLayout);
		// textToFindField
		textToFindField = new RecentStringsJTextField(recentStringsToFind);
		lowerPanel.add(textToFindField, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, insets, 0, 0));
		textToFindField.setText("");
		searchButton = new JButton();
		lowerPanel.add(searchButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, insets, 0, 0));
		searchButton.setText(SwingGui.SEARCH_LABEL);
		// console
		console = new JTextArea();
		lowerPanel.add(new JScrollPane(console), new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));
		console.setText("");
		console.setRows(TEXT_ROWS);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public String getDirectory() {
		return directoryComboBox.getSelectedItem().toString();
	}

	public String getStringToFind() {
		return textToFindField.getText();
	}

	public void setSearchButtonListener(final SearchButtonListener searchButtonListener) {
		this.searchButtonListener = searchButtonListener;
	}

	public void showDoneWorking() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				searchButton.setText(SwingGui.SEARCH_LABEL);
				// searchButton.setEnabled(true);
			}
		});
	}

	public void showFrame() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void showWorking() {
		// searchButton.setEnabled(false);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (searchButton.getText().equals("working.   ")) {
					searchButton.setText("working..  ");
				} else if (searchButton.getText().equals("working..  ")) {
					searchButton.setText("working... ");
				} else if (searchButton.getText().equals("working... ")) {
					searchButton.setText("working....");
				} else {
					searchButton.setText("working.   ");
				}
			}
		});
	}

	public interface SearchButtonListener {
		void performSearch();

		void performStopSearch();
	}
}
