package zipfinder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import zipfinder.logger.StatusLogger;

public class ZipSearcherRunner implements Runnable {
	private static final int TIMEOUT = 100;
	private int nrOfFiles = 0;
	private final FoundFilesQueue foundFilesQueue;
	private final String stringToFind;
	private StatusLogger statusLogger;

	public ZipSearcherRunner(final FoundFilesQueue foundFilesQueue, final String stringToFind) {
		this.foundFilesQueue = foundFilesQueue;
		this.stringToFind = stringToFind;
	}

	private void printFileInfo(final File zipFile, final String[] names) {
		statusLogger.logFilesFound(zipFile.getAbsolutePath());
		for (String name : names) {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("  ").append(name);
			statusLogger.logFilesFound(stringBuffer.toString());
		}
	}

	public void run() {
		while (!(foundFilesQueue.isDone() && foundFilesQueue.isEmpty())) {
			ZipSearcher searcher = new ZipSearcher(stringToFind);
			searcher.setStatusLogger(statusLogger);
			File zipFile = null;
			try {
				zipFile = foundFilesQueue.poll(ZipSearcherRunner.TIMEOUT, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// Do nothing
			}
			if (zipFile != null) {
				nrOfFiles++;
				String[] names = searcher.findEntries(new ZipFileEntries(zipFile));
				if (names.length != 0) {
					printFileInfo(zipFile, names);
				}
			}
		}
		statusLogger.logFilesFound("Found " + nrOfFiles + " compressed files");
	}

	public void setStatusLogger(final StatusLogger statusLogger) {
		this.statusLogger = statusLogger;
	}
}
