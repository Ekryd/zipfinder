package zipfinder;

import zipfinder.gui.SwingController;

/*
 * * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * 
 * @author not attributable
 * 
 * @version 1.0
 */
public abstract class Main {
	private static final ZipFinderPreferences PREFERENCES = new ZipFinderPreferences();

	private Main() {
	}

	public static void main(final String[] args) {
		switch (args.length) {
			case 0:
				new SwingController().start();
				break;
			case 1:
				PREFERENCES.addDirectory(args[0]);
				new SwingController().start();
				break;
			case 2:
				PREFERENCES.addDirectory(args[0]);
				PREFERENCES.addStringToFind(args[1]);
				new HeadLessController(args[0], args[1]).start();
				break;
			default:
				System.out.println("Usage: directory stringToFind");
				System.exit(1);
		}
	}
}
