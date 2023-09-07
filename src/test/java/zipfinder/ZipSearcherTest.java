package zipfinder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.*;
import java.util.zip.*;
import junit.framework.TestCase;

public class ZipSearcherTest extends TestCase {

  private ZipFileEntries entries = mock(ZipFileEntries.class);

  protected void setUp() throws Exception {
    Vector<ZipEntry> returnValue = new Vector<ZipEntry>();
    returnValue.add(new ZipEntry("java/lang/String.class"));

    when(entries.getEntries()).thenReturn(returnValue.elements());
  }

  public void testMatchNormal() throws ZipException, IOException {
    ZipSearcher search = new ZipSearcher("String");
    String[] fileNames = search.findEntries(entries);

    verify(entries).getEntries();
    assertEquals(1, fileNames.length);
    assertEquals("java/lang/String.class", fileNames[0]);
  }

  public void testMatchRegEx() throws ZipException, IOException {
    ZipSearcher search = new ZipSearcher("java.lang.String");
    String[] fileNames = search.findEntries(entries);

    verify(entries).getEntries();
    assertEquals(1, fileNames.length);
    assertEquals("java/lang/String.class", fileNames[0]);
  }

  public void testNoMatchNormal() throws ZipException, IOException {
    ZipSearcher search = new ZipSearcher("Long");
    String[] fileNames = search.findEntries(entries);

    verify(entries).getEntries();
    assertEquals(0, fileNames.length);
  }

  public void testNoMatchRegExl() throws ZipException, IOException {
    ZipSearcher search = new ZipSearcher("java.String");
    String[] fileNames = search.findEntries(entries);

    verify(entries).getEntries();
    assertEquals(0, fileNames.length);
  }
}
