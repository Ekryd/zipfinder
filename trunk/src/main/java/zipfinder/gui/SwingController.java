package zipfinder.gui;

import java.util.LinkedList;

import javax.swing.SwingUtilities;

import zipfinder.FileFinder;
import zipfinder.FoundFilesQueue;
import zipfinder.ZipFinderPreferences;
import zipfinder.ZipSearcherRunner;
import zipfinder.gui.SwingGui.SearchButtonListener;
import zipfinder.logger.StatusLogger;

public class SwingController implements StatusLogger, SearchButtonListener {
	private final SwingGui swingGui;
	private final StatusLogger statusLogger;
	private final ZipFinderPreferences preferences;
	private FileFinder fileFinder = null;

	public SwingController() {
		preferences = new ZipFinderPreferences();
		final LinkedList<String> recentDirectories = preferences.getRecentDirectories();
		final LinkedList<String> recentStringsToFind = preferences.getRecentStringsToFind();
		swingGui = new SwingGui(recentDirectories.toArray(new String[recentDirectories.size()]), recentStringsToFind
				.toArray(new String[recentStringsToFind.size()]));
		swingGui.setSearchButtonListener(this);
		statusLogger = this;
	}

	public void logError(final String msg) {
		swingGui.addToConsole(msg);
	}

	public void logFilesFound(final String msg) {
		swingGui.addToConsole(msg);
	}

	public void logFoundZipFile() {
		swingGui.showWorking();
	}

	public void performSearch() {
		final FoundFilesQueue foundFilesQueue = new FoundFilesQueue();
		final String directory = swingGui.getDirectory();
		final String stringToFind = swingGui.getStringToFind();
		final Thread fileFinderThread = createFileFinderThread(directory, foundFilesQueue);
		final Thread zipSearcherThread = createZipSearcherThread(stringToFind, foundFilesQueue);
		preferences.addDirectory(directory);
		preferences.addStringToFind(stringToFind);
		fileFinderThread.start();
		zipSearcherThread.start();
		new Thread(new Runnable() {
			public void run() {
				try {
					fileFinderThread.join();
				} catch (InterruptedException e) {
					statusLogger.logError(e.getMessage());
				}
				try {
					zipSearcherThread.join();
				} catch (InterruptedException e) {
					statusLogger.logError(e.getMessage());
				}
				swingGui.showDoneWorking();
				fileFinder = null;
			}
		}).start();
	}

	public void performStopSearch() {
		fileFinder.stop();
	}

	public void start() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				swingGui.createComponents();
				swingGui.addListeners();
				swingGui.showFrame();
			}
		});
	}

	private Thread createFileFinderThread(final String directory, final FoundFilesQueue foundFilesQueue) {
		fileFinder = new FileFinder(directory, foundFilesQueue, statusLogger);
		final Thread fileFinderThread = new Thread(fileFinder);
		return fileFinderThread;
	}

	private Thread createZipSearcherThread(final String stringToFind, final FoundFilesQueue foundFilesQueue) {
		final ZipSearcherRunner zipSearcherRunner = new ZipSearcherRunner(foundFilesQueue, stringToFind);
		zipSearcherRunner.setStatusLogger(statusLogger);
		final Thread zipSearcherThread = new Thread(zipSearcherRunner);
		return zipSearcherThread;
	}
}
