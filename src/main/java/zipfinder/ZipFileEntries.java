package zipfinder;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * Extracts zip file entries from file
 * 
 * @author bjorn
 *
 */
public class ZipFileEntries {
	private final File file;

	/**
	 * Used for mocking
	 */
	public ZipFileEntries() {
		file = null;
	}

	public ZipFileEntries(final File file) {
		this.file = file;
	}

	public Enumeration<? extends ZipEntry> getEntries() throws ZipException, IOException {
		ZipFile zipFile = new ZipFile(file);
		return zipFile.entries();
	}

	public File getFile() {
		return file;
	}

}
