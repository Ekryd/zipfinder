package zipfinder.filefilter;

import java.io.File;
import java.io.FileFilter;

public class ZipFileFilter implements FileFilter {
	/* 
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	public boolean accept(final File pathname) {
		if (!pathname.isFile()) {
			return false;
		}
		String name = pathname.getName().toLowerCase();
		return name.endsWith(".zip") || name.endsWith(".jar");
	}
}
