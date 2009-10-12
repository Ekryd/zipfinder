package zipfinder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import zipfinder.logger.StatusLogger;

/*
 * * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * 
 * @author not attributable
 * 
 * @version 1.0
 */
public class ZipSearcher {
	private final String str;
	private StatusLogger statusLogger;

	public ZipSearcher(final String str) {
		this.str = str;
	}

	public String[] findEntries(final File zfile) {
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(zfile);
		} catch (ZipException e) {
			statusLogger.logError("ZipFinder Error, ZipFile: " + zfile.getAbsoluteFile() + " " + e.getMessage());
			return new String[0];
		} catch (IOException e) {
			statusLogger.logError("ZipFinder Error, ZipFile: " + zfile.getAbsoluteFile() + " " + e.getMessage());
			return new String[0];
		}
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		Collection<String> fileNames = new ArrayList<String>();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			String fileName = entry.getName();
			if (fileName.indexOf(str) != -1) {
				fileNames.add(fileName);
			}
		}
		return fileNames.toArray(new String[fileNames.size()]);
	}

	void setStatusLogger(final StatusLogger statusLogger) {
		this.statusLogger = statusLogger;
	}
}