package tasks;

import gui.FileTableModel;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DownloadTask {

	public static final int READY = 1;
	public static final int DOWNLOADING = 2;
	public static final int FINISHED = 3;
	
	private String url;
	private String targetFile;
	private ExecutorService threadPoolRef;
	private int taskID = TASK_COUNTER++;
	private boolean isFinished = false;
	private int status = READY;
	private Future<?> result;
	
	private static int TASK_COUNTER = 0;
	
	public DownloadTask(String url, String directory) {
		this.url = url;
		setTargetFile(directory);
	}

	private void setTargetFile(String directory) {
		String[] parts = url.split("/");
		
		Path path = Paths.get(directory, parts[parts.length - 1]);
		targetFile = path.toString();
	}
	
	private void setDownloadStatus(int status) {
		if (status == FINISHED) {
			isFinished = true;
		}
		this.status = status;
	}
	
	public int getTaskID() {
		return taskID;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void startTask(ExecutorService threadPool, FileTableModel model) {
		setDownloadStatus(DOWNLOADING);
		threadPoolRef = threadPool;
		
		DownloadRunnable runnable = new DownloadRunnable(model, url, targetFile);
		result = threadPoolRef.submit(runnable);
	}
	
	public void cancel() {
		if (!result.isDone() && !result.isCancelled()) {
			result.cancel(true);
		}
	}
	
	public boolean isFinished() {
		return isFinished;
	}

}
