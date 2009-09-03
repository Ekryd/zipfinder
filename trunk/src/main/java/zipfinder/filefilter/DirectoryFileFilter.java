package zipfinder.filefilter;

import java.io.File;
import java.io.FileFilter;

public class DirectoryFileFilter implements FileFilter {
	/*
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	public boolean accept(final File pathname) {
		return pathname.isDirectory();
	}
}
