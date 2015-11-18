package tasks;

import gui.FileTableModel;
import gui.RowData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.SwingUtilities;

public class DownloadRunnable implements Runnable {

	private String url;
	private String targetFile;
	private int downloaded;
	private int size;
	private FileTableModel model;

	public DownloadRunnable(FileTableModel model, String url, String targetFile) {
		this.url = url;
		this.targetFile = targetFile;
		this.model = model;
	}
	
	public int getSize() {
	    return size;
	}
	
	public float getProgress() {
	    return ((float) downloaded / size) * 100;
	}
	
	@Override
	public void run() {
		
		try {
			
			// Set downloading status
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					model.updateStatus(url, RowData.DOWNLOADING);
				}
			});
			

            URL urlObj = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) (urlObj.openConnection());
            long completeFileSize = httpConnection.getContentLength();

            BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
            FileOutputStream fos = new FileOutputStream(targetFile);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;

                // Calculate progress
                final int currentProgress = (int) ((((double)downloadedFileSize) / ((double)completeFileSize)) * 100);

                // Update progress bar
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
            SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					model.updateStatus(url, RowData.FINISHED);
				}
			});
        }
		catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
        }
		catch (IOException e) {
			System.out.println(e.getMessage());
        }
		
	}
	

}
