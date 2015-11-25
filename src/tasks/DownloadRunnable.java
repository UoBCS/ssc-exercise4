package tasks;

import gui.FileTableModel;
import gui.RowData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.SwingUtilities;

/**
 * Describes a download action
 */
public class DownloadRunnable implements Runnable {

	private String url;
	private String targetFile;
	private long downloaded;
	private long size;
	private FileTableModel model;
	private DownloadTask downloadTask;

	/**
	 * Creates a new DownloadRunnable object
	 * @param model The table's model
	 * @param url The URL
	 * @param targetFile The target file (on the local machine)
	 * @param downloadTask 
	 */
	public DownloadRunnable(FileTableModel model, String url, String targetFile, DownloadTask downloadTask) {
		this.url = url;
		this.targetFile = targetFile;
		this.model = model;
		this.downloadTask = downloadTask;
	}
	
	/**
	 * Gets the file's size
	 * @return
	 */
	public long getSize() {
	    return size;
	}
	
	/**
	 * Gets the current progress
	 * @return
	 */
	public float getProgress() {
	    return ((float) downloaded / size) * 100;
	}
	
	@Override
	public void run() {
		
		try {
			
			// Set downloading status
			// ----------------------
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					model.updateStatus(url, RowData.DOWNLOADING);
				}
			});
			
			// Open connection and get file size
			// --------------------------------
            URL urlObj = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) (urlObj.openConnection());
            long completeFileSize = httpConnection.getContentLength();
            size = completeFileSize;

            // Setup buffers
            // -------------
            BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
            FileOutputStream fos = new FileOutputStream(targetFile);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            
            // Actual download of data
            // -----------------------
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;
                downloaded = downloadedFileSize;

                // Calculate and update progress
                // -----------------------------
                final int currentProgress = (int) ((((double)downloadedFileSize) / ((double)completeFileSize)) * 100);
                
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                    	model.updateProgress(url, currentProgress);
                    }
                });

                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
            
            // Set finished status
            // ------------------
            SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					model.updateStatus(url, RowData.FINISHED);
				}
			});
        }
		catch (FileNotFoundException e) {
			downloadTask.cancel();
        }
		catch (IOException e) {
			downloadTask.cancel();
        }
		
	}
}