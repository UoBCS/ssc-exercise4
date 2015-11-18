package tasks;

import gui.FileTableModel;

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
		
		/*URL urlObj;
		InputStream is;
		OutputStream os;
		
		try {
			urlObj = new URL(url);
			is = urlObj.openStream();
			os = new FileOutputStream(targetFile);
			
			byte[] b = new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}

			is.close();
			os.close();
			
			
		}
		catch (IOException e) {
			
		}*/
		
		try {

            URL urlObj = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) (urlObj.openConnection());
            long completeFileSize = httpConnection.getContentLength();

            java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(targetFile);
            java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;

                // calculate progress
                final int currentProgress = (int) ((((double)downloadedFileSize) / ((double)completeFileSize)) * 100);

                // update progress bar
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                    	model.updateStatus(url, currentProgress);
                    	// fileListModel.setProgress(currentProgress, 
                    	//jProgressBar.setValue(currentProgress);
                    }
                });

                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
        }
		catch (FileNotFoundException e) {
			
        }
		catch (IOException e) {
			
        }
		
	}
	

}
