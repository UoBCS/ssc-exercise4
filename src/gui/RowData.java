package gui;


/**
 * Describes the data in a table row
 */
public class RowData {
	
	// DOWNLOAD STATUSES
	public static final int READY = 1;
	public static final int DOWNLOADING = 2;
	public static final int FINISHED = 3;
	
    private String file;
    private int status;
    private float progress;

    /**
     * Creates a new RowData object given the URL of a file
     * @param file
     */
    public RowData(String file) {
        this.file = file;
        this.status = READY;
        this.progress = 0f;
    }

    /**
     * Returns the formatted status
     * @param status Status in the integer format
     * @return Status in the string format
     */
    public static String formatStatus(int status) {
		switch (status) {
			case 1: return "Not started";
			case 2: return "Downloading";
			case 3: return "Finished";
			default: return "";
		}
	}
    
    /**
     * Gets the URL of a file
     * @return
     */
    public String getFile() {
        return file;
    }

    /**
     * Gets the current download status
     * @return
     */
    public int getStatus() {
    	return status;
    }
    
    /**
     * Gets the current download formatted status
     * @return
     */
    public String getFormattedStatus() {
    	return formatStatus(status);
    }
    
    /**
     * Sets the status
     * @param status The new status value
     */
    public void setStatus(int status) {
    	this.status = status;
    }
    
    /**
     * Gets the progress value
     * @return
     */
    public float getProgress() {
        return progress;
    }

    /**
     * Sets the progress of the download
     * @param progress The new progress value
     */
    public void setProgress(float progress) {
        this.progress = progress;
    }
	
}
