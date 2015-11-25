package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Represents the input section
 */
public class InputComponent extends JPanel {
	
	public JLabel urlLbl;
	public JTextField urlTxt;
	
	public JLabel filterLbl;
	public JTextField filterTxt;
	
	public JLabel locationLbl;
	public JButton locationBtn;
	public JTextField directoryTxt;
	
	public JLabel threadsLbl;
	public JTextField threadsTxt;
	
	public JButton getFiles;
	public JButton downloadBtn;
	public JButton cancelBtn;
	public JButton clearBtn;
	
	/**
	 * Creates a new input section object
	 * @param client The JFrame which will contain this object
	 */
	public InputComponent(Client client) {
		super();
		
		// Store reference to use inside anonymous classes
		final InputComponent self = this;
		
		// URL
		urlLbl = new JLabel("Website:");
		urlTxt = new JTextField();
		
		add(urlLbl);
		add(urlTxt);
		
		// Filter
		filterLbl = new JLabel("Filter:");
		filterTxt = new JTextField();
		
		add(filterLbl);
		add(filterTxt);
		
		// Location
		locationLbl = new JLabel("Save To:");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(false);
		locationBtn = new JButton("Choose location");
		
		locationBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.showOpenDialog(self);
				directoryTxt.setText(fileChooser.getSelectedFile().toString());
			}
		});
		
		add(locationLbl);
		add(locationBtn);
		
		directoryTxt = new JTextField();
		add(directoryTxt);
		
		// Threads
		threadsLbl = new JLabel("Threads:");
		threadsTxt = new JTextField();
	
		add(threadsLbl);
		add(threadsTxt);
		
		// Get files
		getFiles = new JButton("Get Files");
		add(getFiles);
		
		// Download
		downloadBtn = new JButton("Download");
		add(downloadBtn);
		
		// Cancel
		cancelBtn = new JButton("Cancel");
		add(cancelBtn);
		
		// Clear
		clearBtn = new JButton("Clear");
		add(clearBtn);
		
	}
	
}
