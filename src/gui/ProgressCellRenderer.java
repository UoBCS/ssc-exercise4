package gui;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ProgressCellRenderer extends JProgressBar implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int progress = 0;
        if (value instanceof Float) {
            progress = Math.round(((Float) value) * 100f);
        } else if (value instanceof Integer) {
            progress = (int) value;
        }
        setValue(progress);
        return this;
    }
}