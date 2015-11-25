package tasks;

import gui.FileTableModel;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Describes an individual download task
 */
public class DownloadTask {
	
	private String url;
	private String targetFile;
	private ExecutorService threadPoolRef;
	private int taskID = TASK_COUNTER++;
	private Future<?> result;
	
	private static int TASK_COUNTER = 0;
	
	/**
	 * Creates a new download task
	 * @param url The file URL
	 * @param directory The directory where to save the file
	 */
	public DownloadTask(String url, String directory) {
		this.url = url;
		setTargetFile(directory);
	}

	/**
	 * Given a directory and the file's URL constructs the target file
	 * @param directory
	 */
	private void setTargetFile(String directory) {
		String[] parts = url.split("/");
		
		Path path = Paths.get(directory, parts[parts.length - 1]);
		targetFile = path.toString();
	}
	
	/**
	 * Gets the task ID
	 * @return
	 */
	public int getTaskID() {
		return taskID;
	}
	
	/**
	 * Submits the current task to the thread pool
	 * @param threadPool The executor service object
	 * @param model The table's model
	 */
	public void startTask(ExecutorService threadPool, FileTableModel model) {
		threadPoolRef = threadPool;
		
		DownloadRunnable runnable = new DownloadRunnable(model, url, targetFile, this);
		result = threadPoolRef.submit(runnable);
	}
	
	/**
	 * Cancels the current download task
	 */
	public void cancel() {
		if (!result.isDone() && !result.isCancelled()) {
			result.cancel(true);
		}
	}
}