package tasks;

import gui.FileTableModel;

import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Describes a download manager
 */
public class DownloadManager {
	
	private static ExecutorService threadPool;
	private static int ID = 0;
	private Hashtable<Integer, DownloadTask> downloadTasks = new Hashtable<Integer, DownloadTask>();
	
	public DownloadManager() {}
	
	/**
	 * Creates a new DownloadManager object
	 * @param threadCount The number of worker threads
	 */
	public DownloadManager(int threadCount) {
		createThreadPool(threadCount);
	}
	
	/**
	 * Creates a thread pool given the number of worker threads
	 * @param threadCount The number of worker threads
	 */
	public void createThreadPool(int threadCount) {
		threadPool = Executors.newFixedThreadPool(threadCount);
	}
	
	/**
	 * Checks if the download queue is empty
	 * @return
	 */
	public boolean isEmpty() {
		return downloadTasks.isEmpty();
	}
	
	/**
	 * Adds a download task
	 * @param url The URL
	 * @param directory The directory where to save the downloaded file
	 * @return
	 */
	public DownloadTask addDownloadTask(String url, String directory) {
		DownloadTask task = new DownloadTask(url, directory);
		addDownloadTask(task);
		return task;
	}
	
	/**
	 * Adds a download task
	 * @param downloadTask The download task object
	 */
	public void addDownloadTask(DownloadTask downloadTask) {
		downloadTasks.put(ID++, downloadTask);
	}
	
	/**
	 * Gets a download task object
	 * @param index The download task index
	 * @return
	 */
	public DownloadTask getTask(int index) {
		return downloadTasks.get(index);
	}
	
	/**
	 * Cancels a specific download task
	 * @param taskID The download task index
	 * @param defer Whether to defer the removal from the internal HashTable
	 */
	public void cancelTask(int taskID, boolean defer) {
		if (downloadTasks.containsKey(taskID)) {
			DownloadTask task = defer ? downloadTasks.get(taskID) : downloadTasks.remove(taskID);
			task.cancel();
		}
	}
	
	/**
	 * Cancels all the download tasks
	 */
	public void cancellAllTasks() {
		for (int taskID : downloadTasks.keySet()) {
			cancelTask(taskID, true);
		}
		
		downloadTasks.clear();
	}
	
	/**
	 * Starts the download process
	 * @param model
	 */
	public void start(FileTableModel model) {
		for (DownloadTask task : downloadTasks.values()) {
			task.startTask(threadPool, model);
		}
	}
	
	/**
	 * Shuts down the download manager
	 */
	public void shutdown() {
		threadPool.shutdownNow();
	}
}