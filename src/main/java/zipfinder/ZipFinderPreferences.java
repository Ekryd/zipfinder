package zipfinder;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class ZipFinderPreferences {
	private static final String SEPARATOR = "|";
	private static final String SEPARATOR_REGEX = "\\" + SEPARATOR;
	private static final int NR_OF_ENTRIES = 20;
	private static final String STRINGS_TO_FIND_KEY = "StringsToFind";
	private static final String DIRECTORIES_KEY = "Directories";
	private final Preferences preferences = Preferences.systemNodeForPackage(this.getClass());

	public void addDirectory(final String str) {
		if (isEmptyString(str)) {
			return;
		}
		final LinkedList<String> oldArray = getRecentDirectories();
		LinkedList<String> newArray = createNewStringArray(str, oldArray);
		String newString = createNewString(newArray);
		preferences.put(DIRECTORIES_KEY, newString);
		try {
			preferences.sync();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	public void addStringToFind(final String str) {
		if (isEmptyString(str)) {
			return;
		}
		final LinkedList<String> oldArray = getRecentStringsToFind();
		LinkedList<String> newArray = createNewStringArray(str, oldArray);
		String newString = createNewString(newArray);
		preferences.put(STRINGS_TO_FIND_KEY, newString);
		try {
			preferences.sync();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	public LinkedList<String> getRecentDirectories() {
		return getValueArray(DIRECTORIES_KEY);
	}

	public LinkedList<String> getRecentStringsToFind() {
		return getValueArray(STRINGS_TO_FIND_KEY);
	}

	private String createNewString(final Collection<String> newArray) {
		StringBuffer newString = new StringBuffer();
		boolean first = true;
		for (String string : newArray) {
			if (first) {
				first = false;
			} else {
				newString.append(SEPARATOR);
			}
			newString.append(string);
		}
		return newString.toString();
	}

	private LinkedList<String> createNewStringArray(final String str, final Collection<String> oldCollection) {
		LinkedList<String> returnValue = new LinkedList<String>(oldCollection);
		if (returnValue.contains(str)) {
			returnValue.remove(str);
		}
		returnValue.addFirst(str);
		while (returnValue.size() > NR_OF_ENTRIES) {
			returnValue.removeLast();
		}
		return returnValue;
	}

	private LinkedList<String> getValueArray(final String key) {
		final String value = preferences.get(key, "");
		final String[] valueArray = value.split(SEPARATOR_REGEX);
		final LinkedList<String> returnValue = new LinkedList<String>();
		if (valueArray.length == 1 && valueArray[0].isEmpty()) {
			return returnValue;
		}
		returnValue.addAll(Arrays.asList(valueArray));
		return returnValue;
	}

	private boolean isEmptyString(final String str) {
		return str == null || str.isEmpty();
	}
}
