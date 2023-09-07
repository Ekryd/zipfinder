package zipfinder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;import java.util.prefs.Preferences;
import junit.framework.TestCase;
import zipfinder.testutil.ReflectionHelper;

@SuppressWarnings("unused")
public class ZipFinderPreferencesTest extends TestCase {
	private ZipFinderPreferences zipFinderPreferences = new ZipFinderPreferences();
	private Preferences preferencesMock = mock(Preferences.class);
	private String store;

	public final void testAddDirectoryEmpty() throws Exception {
			when(preferencesMock.get("Directories", "")).thenReturn("");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);
		zipFinderPreferences.addDirectory("pelle");

		verify(preferencesMock).get("Directories", "");
		verify(preferencesMock).put("Directories", "pelle");
		verify(preferencesMock).sync();
	}

	public final void testAddDirectoryExistingEntry() throws Exception {
			when(preferencesMock.get("Directories", "")).thenReturn("anna|stina|olle");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);

		zipFinderPreferences.addDirectory("stina");
		verify(preferencesMock).get("Directories", "");
		verify(preferencesMock).put("Directories", "stina|anna|olle");
		verify(preferencesMock).sync();
	}

	public final void testAddDirectoryOneEntry() throws Exception {
			when(preferencesMock.get("Directories", "")).thenReturn("anna");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);

		zipFinderPreferences.addDirectory("pelle");
		verify(preferencesMock).get("Directories", "");
		verify(preferencesMock).put("Directories", "pelle|anna");
		verify(preferencesMock).sync();
	}

	public final void testAddDirectoryThreeEntry() throws Exception {
			when(preferencesMock.get("Directories", "")).thenReturn("anna|C:\\Documents and Settings\\Bjorn\\.saverbeans|olle");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);

		zipFinderPreferences.addDirectory("C:\\Documents and Settings");
		verify(preferencesMock).get("Directories", "");
		verify(preferencesMock).put("Directories",
				"C:\\Documents and Settings|anna|C:\\Documents and Settings\\Bjorn\\.saverbeans|olle");
		verify(preferencesMock).sync();
	}

	public final void testAddDirectoryTooManyEntryEntry() throws Exception {
			when(preferencesMock.get("Directories", "")).thenReturn("1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);

		zipFinderPreferences.addDirectory("stina");
		verify(preferencesMock).get("Directories", "");
		verify(preferencesMock).put("Directories",
				"stina|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19");
		verify(preferencesMock).sync();
	}

	public final void testAddStringToFindEmpty() throws Exception {
			when(preferencesMock.get("StringsToFind", "")).thenReturn("");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);
		zipFinderPreferences.addStringToFind("pelle");

		verify(preferencesMock).get("StringsToFind", "");
		verify(preferencesMock).put("StringsToFind", "pelle");
		verify(preferencesMock).sync();
	}

	public final void testAddStringToFindExistingEntry() throws Exception {
			when(preferencesMock.get("StringsToFind", "")).thenReturn("anna|stina|olle");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);

		zipFinderPreferences.addStringToFind("stina");
		verify(preferencesMock).get("StringsToFind", "");
		verify(preferencesMock).put("StringsToFind", "stina|anna|olle");
		verify(preferencesMock).sync();
	}

	public final void testAddStringToFindOneEntry() throws Exception {
			when(preferencesMock.get("StringsToFind", "")).thenReturn("pelle|anna");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);

		zipFinderPreferences.addStringToFind("pelle");
		verify(preferencesMock).get("StringsToFind", "");
		verify(preferencesMock).put("StringsToFind", "pelle|anna");
		verify(preferencesMock).sync();
	}

	public final void testAddStringToFindThreeEntry() throws Exception {
			when(preferencesMock.get("StringsToFind", "")).thenReturn("anna|C:\\Documents and Settings\\Bjorn\\.saverbeans|olle");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);

		zipFinderPreferences.addStringToFind("C:\\Documents and Settings");
		verify(preferencesMock).get("StringsToFind", "");
		verify(preferencesMock).put("StringsToFind",
				"C:\\Documents and Settings|anna|C:\\Documents and Settings\\Bjorn\\.saverbeans|olle");
		verify(preferencesMock).sync();
	}

	public final void testAddStringToFindTooManyEntryEntry() throws Exception {
			when(preferencesMock.get("StringsToFind", "")).thenReturn("1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);

		zipFinderPreferences.addStringToFind("stina");
		verify(preferencesMock).get("StringsToFind", "");
		verify(preferencesMock).put("StringsToFind",
				"stina|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19");
		verify(preferencesMock).sync();
	}

	public final void testGetRecentDirectoriesEmpty()throws IllegalAccessException {
			when(preferencesMock.get("Directories", "")).thenReturn("");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);

		LinkedList<String> recentDirectories = zipFinderPreferences.getRecentDirectories();
		assertEquals(0, recentDirectories.size());
	}

	public final void testGetRecentDirectoriesManyEntry()throws IllegalAccessException {
			when(preferencesMock.get("Directories", "")).thenReturn("pelle|C:\\Documents and Settings\\Bjorn\\.saverbeans|stina");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);

		LinkedList<String> recentDirectories = zipFinderPreferences.getRecentDirectories();

		verify(preferencesMock).get("Directories", "");
		assertEquals(3, recentDirectories.size());
		assertEquals("pelle", recentDirectories.get(0));
		assertEquals("C:\\Documents and Settings\\Bjorn\\.saverbeans", recentDirectories.get(1));
		assertEquals("stina", recentDirectories.get(2));
	}

	public final void testGetRecentDirectoriesOneEntry()throws IllegalAccessException {
			when(preferencesMock.get("Directories", "")).thenReturn("pelle");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);

		LinkedList<String> recentDirectories = zipFinderPreferences.getRecentDirectories();

		verify(preferencesMock).get("Directories", "");
		assertEquals(1, recentDirectories.size());
		assertEquals("pelle", recentDirectories.get(0));
	}

	public final void testGetRecentStringsToFindEmpty()throws IllegalAccessException {
			when(preferencesMock.get("StringsToFind", "")).thenReturn("");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);

		LinkedList<String> recentDirectories = zipFinderPreferences.getRecentStringsToFind();
		verify(preferencesMock).get("StringsToFind", "");
		assertEquals(0, recentDirectories.size());
	}

	public final void testGetRecentStringsToFindManyEntry()throws IllegalAccessException {
			when(preferencesMock.get("StringsToFind", "")).thenReturn("pelle|anna|stina");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);

		LinkedList<String> recentDirectories = zipFinderPreferences.getRecentStringsToFind();
		verify(preferencesMock).get("StringsToFind", "");
		assertEquals(3, recentDirectories.size());
		assertEquals("pelle", recentDirectories.get(0));
		assertEquals("anna", recentDirectories.get(1));
		assertEquals("stina", recentDirectories.get(2));
	}

	public final void testGetRecentStringsToFindOneEntry()throws IllegalAccessException {
			when(preferencesMock.get("StringsToFind", "")).thenReturn("pelle");
		new ReflectionHelper(zipFinderPreferences).setField(preferencesMock);

		LinkedList<String> recentDirectories = zipFinderPreferences.getRecentStringsToFind();
		verify(preferencesMock).get("StringsToFind", "");
		assertEquals(1, recentDirectories.size());
		assertEquals("pelle", recentDirectories.get(0));
	}

}
