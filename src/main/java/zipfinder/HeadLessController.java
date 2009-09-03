package zipfinder;

import zipfinder.logger.StandardStatusLogger;
import zipfinder.logger.StatusLogger;

public class HeadLessController {
	private final String directory;
	private final String stringToFind;
	private StatusLogger statusLogger;
	private final FoundFilesQueue foundFilesQueue = new FoundFilesQueue();

	public HeadLessController(final String directory, final String stringToFind) {
		this.directory = directory;
		this.stringToFind = stringToFind;
	}

	public void start() {
		setStatusLogger(new StandardStatusLogger());
		findClassesInArchives();
	}

	private Thread createFileFinderThread() {
		final FileFinder fileFinder = new FileFinder(directory, foundFilesQueue, statusLogger);
		final Thread fileFinderThread = new Thread(fileFinder);
		return fileFinderThread;
	}

	private Thread createZipSearcherThread() {
		final ZipSearcherRunner zipSearcherRunner = new ZipSearcherRunner(foundFilesQueue, stringToFind);
		zipSearcherRunner.setStatusLogger(statusLogger);
		final Thread zipSearcherThread = new Thread(zipSearcherRunner);
		return zipSearcherThread;
	}

	private void findClassesInArchives() {
		final Thread fileFinderThread = createFileFinderThread();
		final Thread zipSearcherThread = createZipSearcherThread();
		fileFinderThread.start();
		zipSearcherThread.start();
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
	}

	private void setStatusLogger(final StatusLogger statusLogger) {
		this.statusLogger = statusLogger;
	}
}
