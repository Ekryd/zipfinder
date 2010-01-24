package zipfinder;

import java.io.File;

import zipfinder.filefilter.DirectoryFileFilter;
import zipfinder.filefilter.ZipFileFilter;
import zipfinder.logger.StatusLogger;

public class FileFinder implements Runnable {
	private static final DirectoryFileFilter DIRECTORY_FILE_FILTER = new DirectoryFileFilter();
	private static final ZipFileFilter ZIP_FILE_FILTER = new ZipFileFilter();
	private final File startDirectory;
	private final FoundFilesQueue foundFilesQueue;
	private StatusLogger statusLogger;
	private boolean stop = false;

	public FileFinder(final String directory, final FoundFilesQueue foundFilesQueue) {
		startDirectory = new File(directory);
		this.foundFilesQueue = foundFilesQueue;
	}

	/**
	 * @param startDirectory
	 * @param foundFilesQueue
	 * @param statusLogger
	 */
	public FileFinder(final String directory, final FoundFilesQueue foundFilesQueue, final StatusLogger statusLogger) {
		this.startDirectory = new File(directory);
		this.foundFilesQueue = foundFilesQueue;
		this.statusLogger = statusLogger;
	}

	public void run() {
		try {
			if (statusLogger == null) {
				throw new IllegalStateException("StatusLogger is not instansiated");
			}
			if (!startDirectory.isDirectory()) {
				statusLogger.logError(startDirectory + " is not a directory");
			} else {
				searchForFiles(startDirectory);
			}
		} finally {
			foundFilesQueue.done();
		}
	}

	public void stop() {
		stop = true;
	}

	private void searchForFiles(final File directory) {
		if (stop) {
			return;
		}
		File[] zipFiles = directory.listFiles(ZIP_FILE_FILTER);
		if (zipFiles == null) {
			statusLogger.logError("Kunde inte söka i folder " + directory);
			return;
		}
		for (File file : zipFiles) {
			if (stop) {
				return;
			}
			statusLogger.logFoundZipFile();
			try {
				foundFilesQueue.put(file);
			} catch (InterruptedException e) {
				statusLogger.logError(e.getMessage());
			}
		}
		File[] directories = directory.listFiles(DIRECTORY_FILE_FILTER);
		for (int i = 0; i < directories.length; i++) {
			if (stop) {
				return;
			}
			searchForFiles(directories[i]);
		}
	}
}
