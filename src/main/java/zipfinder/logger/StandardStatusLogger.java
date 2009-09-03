package zipfinder.logger;


public class StandardStatusLogger implements StatusLogger {
	public void logError(final String msg) {
		System.err.println(msg);
	}

	public void logFilesFound(final String msg) {
		System.out.println(msg);
	}

	public void logFoundZipFile() {
	}
}
