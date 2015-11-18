package tasks;

import gui.FileTableModel;

import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadManager {
	
	private static ExecutorService threadPool;
	private static int ID = 0;
	private Hashtable<Integer, DownloadTask> downloadTasks = new Hashtable<Integer, DownloadTask>();
	
	public DownloadManager(int threadCount) {
		threadPool = Executors.newFixedThreadPool(threadCount);
	}
	
	public DownloadTask addDownloadTask(String url, String directory) {
		DownloadTask task = new DownloadTask(url, directory);
		addDownloadTask(task);
		return task;
	}
	
	public void addDownloadTask(DownloadTask downloadTask) {
		downloadTasks.put(ID++, downloadTask);
	}
	
	public DownloadTask getTask(int index) {
		return downloadTasks.get(index);
	}
	
	public Boolean isAllTasksFinished() {
		for (Integer taskID : downloadTasks.keySet()) {
			if (!isTaskFinished(taskID)) {
				return false;
			}
		}
		return true;
	}

	public Boolean isTaskFinished(int taskID) {
		DownloadTask task = downloadTasks.get(taskID);
		return task.isFinished();
	}
	
	public void cancelTask(int taskID) {
		if (downloadTasks.contains(taskID)) {
			DownloadTask task = downloadTasks.remove(taskID);
			task.cancel();
		}
	}
	
	public void cancellAllTasks() {
		for (int taskID : downloadTasks.keySet()) {
			cancelTask(taskID);
		}
	}
	
	public void start(FileTableModel model) {
		for (DownloadTask task : downloadTasks.values()) {
			task.startTask(threadPool, model);
		}
	}
	
	// Implement shutdownSafely
	
	public void shutdown() {
		threadPool.shutdownNow();
	}
	
}
