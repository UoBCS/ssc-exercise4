package tasks;

import gui.FileTableModel;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadManager {
	
	private static ExecutorService threadPool;
	private static int ID = 0;
	private Hashtable<Integer, DownloadTask> downloadTasks = new Hashtable<Integer, DownloadTask>();
	
	public DownloadManager() {}
	
	public DownloadManager(int threadCount) {
		createThreadPool(threadCount);
	}
	
	public void createThreadPool(int threadCount) {
		threadPool = Executors.newFixedThreadPool(threadCount);
	}
	
	public boolean isEmpty() {
		return downloadTasks.isEmpty();
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
	
	public void cancelTask(int taskID, boolean defer) {
		if (downloadTasks.containsKey(taskID)) {
			DownloadTask task = defer ? downloadTasks.get(taskID) : downloadTasks.remove(taskID);
			task.cancel();
		}
	}
	
	public void cancellAllTasks() {
		for (int taskID : downloadTasks.keySet()) {
			cancelTask(taskID, true);
		}
		
		downloadTasks.clear();
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
