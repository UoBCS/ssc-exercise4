package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class FileTableModel extends AbstractTableModel {
	
	private ArrayList<RowData> rows;
	private Map<String, RowData> mapLookup;
	
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
                value = rowData.getFormattedStatus(); // getStatus
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
            case 2:
                if (aValue instanceof Float) {
                    rowData.setProgress((float) aValue);
                }
                break;
        }
    }

	public ArrayList<RowData> getRows() {
		return rows;
	}
	
	public void addFile(String file) {
        RowData rowData = new RowData(file);
        mapLookup.put(file, rowData);
        rows.add(rowData);
        fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
    }

    public void updateStatus(String file, int progress) {
        RowData rowData = mapLookup.get(file);
        
        if (rowData != null) {
            int row = rows.indexOf(rowData);
            float p = (float) progress / 100f;
            setValueAt(p, row, 2);
            fireTableCellUpdated(row, 2);
        }
    }
	
}
