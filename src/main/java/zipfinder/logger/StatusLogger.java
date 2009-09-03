package zipfinder.logger;

public interface StatusLogger {
	void logError(String msg);

	void logFilesFound(String msg);

	void logFoundZipFile();
}
