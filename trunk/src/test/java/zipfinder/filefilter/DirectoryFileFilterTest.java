package zipfinder.filefilter;

import java.io.File;

import junit.framework.TestCase;

@SuppressWarnings("serial")
public class DirectoryFileFilterTest extends TestCase {
	public final void testAcceptDirectory() {
		final DirectoryFileFilter directoryFileFilter = new DirectoryFileFilter();
		File file = new File("") {
			@Override
			public boolean isDirectory() {
				return true;
			}
		};
		assertEquals(true, directoryFileFilter.accept(file));
	}

	public final void testAcceptNoDirectory() {
		final DirectoryFileFilter directoryFileFilter = new DirectoryFileFilter();
		File file = new File("") {
			@Override
			public boolean isDirectory() {
				return false;
			}
		};
		assertEquals(false, directoryFileFilter.accept(file));
	}
}
