package zipfinder;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.*;

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
	private final Pattern pattern;
	private StatusLogger statusLogger;

	public ZipSearcher(final String str) {
		String patternString = str.trim().replaceAll("\\W", ".");
		patternString = "\\S*" + patternString + "\\S*";
		pattern = Pattern.compile(patternString);
	}

	public String[] findEntries(final ZipFileEntries entries) {
		Enumeration<? extends ZipEntry> zipEntries;
		try {
			zipEntries = entries.getEntries();
		} catch (ZipException e) {
			statusLogger.logError("ZipFinder Error, ZipFile: " + entries.getFile().getAbsoluteFile() + " "
					+ e.getMessage());
			return new String[0];
		} catch (IOException e) {
			statusLogger.logError("ZipFinder Error, ZipFile: " + entries.getFile().getAbsoluteFile() + " "
					+ e.getMessage());
			return new String[0];
		}
		Collection<String> fileNames = new ArrayList<String>();
		while (zipEntries.hasMoreElements()) {
			ZipEntry entry = zipEntries.nextElement();
			String fileName = entry.getName();
			if (isFileMatch(fileName)) {
				fileNames.add(fileName);
			}
		}
		return fileNames.toArray(new String[fileNames.size()]);
	}

	private boolean isFileMatch(final String fileName) {
		return pattern.matcher(fileName).matches();
	}

	void setStatusLogger(final StatusLogger statusLogger) {
		this.statusLogger = statusLogger;
	}
}
