package zipfinder;

import java.io.IOException;
import java.util.*;
import java.util.zip.*;

import junit.framework.TestCase;
import cheesymock.*;

public class ZipSearcherTest extends TestCase {

	private ZipFileEntries entries;
	private Recorder recorder;

	protected void setUp() throws Exception {
		entries = Cheesy.mock(ZipFileEntries.class, new Object() {
			@SuppressWarnings("unused")
			public Enumeration<? extends ZipEntry> getEntries() {
				Vector<ZipEntry> returnValue = new Vector<ZipEntry>();
				returnValue.add(new ZipEntry("java/lang/String.class"));
				return returnValue.elements();
			}
		});
		recorder = new Recorder();
	}

	public void testMatchNormal() throws ZipException, IOException {
		// setup
		ZipSearcher search = new ZipSearcher("String");
		// expect
		recorder.expect(entries).getEntries();
		// test
		String[] fileNames = search.findEntries(entries);
		recorder.check();
		// assert
		assertEquals(1, fileNames.length);
		assertEquals("java/lang/String.class", fileNames[0]);
	}

	public void testMatchRegEx() throws ZipException, IOException {
		// setup
		ZipSearcher search = new ZipSearcher("java.lang.String");
		// expect
		recorder.expect(entries).getEntries();
		// test
		String[] fileNames = search.findEntries(entries);
		recorder.check();
		// assert
		assertEquals(1, fileNames.length);
		assertEquals("java/lang/String.class", fileNames[0]);
	}

	public void testNoMatchNormal() throws ZipException, IOException {
		// setup
		ZipSearcher search = new ZipSearcher("Long");
		// expect
		recorder.expect(entries).getEntries();
		// test
		String[] fileNames = search.findEntries(entries);
		recorder.check();
		// assert
		assertEquals(0, fileNames.length);
	}

	public void testNoMatchRegExl() throws ZipException, IOException {
		// setup
		ZipSearcher search = new ZipSearcher("java.String");
		// expect
		recorder.expect(entries).getEntries();
		// test
		String[] fileNames = search.findEntries(entries);
		recorder.check();
		// assert
		assertEquals(0, fileNames.length);
	}
}
