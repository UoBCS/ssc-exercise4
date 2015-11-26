package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

/**
 * Table model for list of files to download
 */
public class FileTableModel extends AbstractTableModel {
	
	private ArrayList<RowData> rows;
	private Map<String, RowData> mapLookup;
	
	/**
	 * Creates a new FileTableModel object
	 */
	public FileTableModel() {
		rows = new ArrayList<RowData>();
		mapLookup = new HashMap<String, RowData>();
	}
	
	@Override
	public int getRowCount() {
		return rows.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
    public String getColumnName(int column) {
        String name = "??";
        
        switch (column) {
            case 0:
                name = "File";
                break;
            case 1:
                name = "Status";
                break;
            case 2:
                name = "Progress";
                break;
        }
        
        return name;
    }
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		RowData rowData = rows.get(rowIndex);
        Object value = null;
        switch (columnIndex) {
            case 0:
                value = rowData.getFile();
                break;
            case 1:
                value = rowData.getFormattedStatus();
                break;
            case 2:
                value = rowData.getProgress();
                break;
        }
        return value;
	}
	
	@Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        RowData rowData = rows.get(rowIndex);
        
        switch (columnIndex) {
        	case 1:
        		if (aValue instanceof Integer) {
        			rowData.setStatus((Integer) aValue);
        		}
        		break;
            case 2:
                if (aValue instanceof Float) {
                    rowData.setProgress((Float) aValue);
                }
                break;
        }
    }
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
	    return true;
	}
	
	/**
	 * Delete all data inside the model
	 */
	public void deleteData() {
        int _rows = getRowCount();
        if (_rows == 0) {
            return;
        }
        rows.clear();
        mapLookup.clear();
        fireTableRowsDeleted(0, _rows - 1);
    }
	
	/**
	 * Get the model rows
	 * @return
	 */
	public ArrayList<RowData> getRows() {
		return rows;
	}
	
	/**
	 * Adds a file to the model
	 * @param file URL of the file
	 */
	public void addFile(String file) {
        RowData rowData = new RowData(file);
        mapLookup.put(file, rowData);
        rows.add(rowData);
        fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
    }

	/**
	 * Updates the progress bar
	 * @param file The URL of the file
	 * @param progress The new progress value
	 */
    public void updateProgress(String file, int progress) {
        RowData rowData = mapLookup.get(file);
        
        if (rowData != null) {
            int row = rows.indexOf(rowData);
            float p = (float) progress / 100f;
            setValueAt(p, row, 2);
            fireTableCellUpdated(row, 2);
        }
    }
    
    /**
     * Updates the status of a download task
     * @param file The URL of the file
     * @param status The new status
     */
    public void updateStatus(String file, int status) {
    	RowData rowData = mapLookup.get(file);
        
        if (rowData != null) {
            int row = rows.indexOf(rowData);
            setValueAt(status, row, 1);
            fireTableCellUpdated(row, 1);
        }
    }
	
}
