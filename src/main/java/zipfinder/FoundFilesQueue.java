package zipfinder;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;

public class FoundFilesQueue extends ArrayBlockingQueue<File> {
	private static final int INITIAL_CAPACITY = 40;
	private static final long serialVersionUID = -2320589118723383907L;
	private boolean done = false;

	public FoundFilesQueue() {
		super(FoundFilesQueue.INITIAL_CAPACITY);
	}

	public void done() {
		done = true;
	}

	public boolean isDone() {
		return done;
	}
}
