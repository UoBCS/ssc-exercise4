package gui;


public class RowData {
	
	public static final int READY = 1;
	public static final int DOWNLOADING = 2;
	public static final int FINISHED = 3;
	
    private String file;
    private int status;
    private float progress;

    public RowData(String file) {
        this.file = file;
        this.status = READY;
        this.progress = 0f;
    }

    public static String formatStatus(int status) {
		switch (status) {
			case 1: return "Not started";
			case 2: return "Downloading";
			case 3: return "Finished";
			default: return "";
		}
	}
    
    public String getFile() {
        return file;
    }

    public int getStatus() {
    	return status;
    }
    
    public String getFormattedStatus() {
    	return formatStatus(status);
    }
    
    public void setStatus(int status) {
    	this.status = status;
    }
    
    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
	
}
