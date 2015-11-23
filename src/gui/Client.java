package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import tasks.DownloadManager;
import tasks.ImagesCrawlerWorker;


public class Client {

	private JFrame frmFileDownloader;
	private InputComponent inputComp;
	private JTable table;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frmFileDownloader.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Client() {
		initialize();
	}

	/**
	 * Initialise the contents of the frame.
	 */
	private void initialize() {
		frmFileDownloader = new JFrame();
		frmFileDownloader.getContentPane().setBackground(SystemColor.text);
		frmFileDownloader.setTitle("File Downloader");
		frmFileDownloader.setBounds(100, 100, 800, 600);
		frmFileDownloader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFileDownloader.getContentPane().setLayout(null);
		
		// Input component
		// ---------------
		
		inputComp = new InputComponent(this);
		inputComp.cancelBtn.setBackground(SystemColor.text);
		inputComp.cancelBtn.setLocation(370, 195);
		inputComp.cancelBtn.setSize(100, 25);
		inputComp.clearBtn.setBackground(SystemColor.text);
		inputComp.clearBtn.setLocation(510, 195);
		inputComp.clearBtn.setSize(100, 25);
		inputComp.threadsTxt.setText("2");
		inputComp.downloadBtn.setBackground(SystemColor.text);
		inputComp.getFiles.setBackground(SystemColor.text);
		inputComp.getFiles.setLocation(80, 195);
		inputComp.getFiles.setSize(100, 25);
		inputComp.downloadBtn.setLocation(228, 195);
		inputComp.downloadBtn.setSize(100, 25);
		inputComp.downloadBtn.setEnabled(false);
		inputComp.locationBtn.setBackground(SystemColor.text);
		inputComp.threadsTxt.setBounds(80, 140, 86, 25);
		inputComp.threadsLbl.setBounds(12, 145, 67, 15);
		inputComp.locationBtn.setBounds(515, 98, 125, 25);
		inputComp.locationLbl.setBounds(12, 103, 67, 15);
		inputComp.filterTxt.setBounds(80, 61, 420, 25);
		inputComp.filterLbl.setBounds(12, 66, 67, 15);
		inputComp.urlTxt.setBounds(80, 25, 560, 25);
		inputComp.urlLbl.setBounds(12, 30, 67, 15);
		inputComp.setBounds(12, 12, 776, 246);
		inputComp.setBorder(new TitledBorder(null, "Input", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		inputComp.setLayout(null);
		inputComp.directoryTxt.setBounds(80, 98, 420, 25);
		inputComp.directoryTxt.setColumns(10);
		JLabel filterExampleLbl = new JLabel("example *.doc;*.ppt for Word docs and PP");
		filterExampleLbl.setForeground(Color.ORANGE);
		filterExampleLbl.setBounds(520, 66, 244, 15);
		inputComp.add(filterExampleLbl);
		frmFileDownloader.getContentPane().add(inputComp);
		
		// File list component
		// -------------------
		
		FileTableModel model = new FileTableModel();
		DownloadManager manager = new DownloadManager();
		
		table = new JTable();
		table.setModel(model);
		table.getColumn("Progress").setCellRenderer(new ProgressCellRenderer());
		JScrollPane fileListScrollPnl = new JScrollPane(table);
		frmFileDownloader.getContentPane().add( fileListScrollPnl);
		fileListScrollPnl.setBounds(12, 270, 776, 290);
		
		// Event handlers
		// --------------
		
		inputComp.getFiles.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Sanity checks
				//if (inputComp.urlTxt)
				
				// Start worker thread
				model.deleteData();
				new ImagesCrawlerWorker(inputComp.urlTxt.getText(), inputComp.filterTxt.getText(), model, inputComp).execute();
			}
		});
		
		inputComp.downloadBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					// Long running operation (download)
					manager.createThreadPool(Integer.parseInt(inputComp.threadsTxt.getText()));
					ArrayList<RowData> rows = model.getRows();
					
					if (rows.size() == 0) {
						MessageBox.show("File queue empty", "Warning");
					}
					else {
						for (RowData row : rows) {
							manager.addDownloadTask(row.getFile(), inputComp.directoryTxt.getText());
						}
						
						manager.start(model);
					}
				}
				catch (NumberFormatException ex) {
					System.out.println(ex.getMessage());
				}
				
			}
		});
		
		inputComp.cancelBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				manager.cancellAllTasks();
			}
		});
		
		inputComp.clearBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				model.deleteData();
				table.setModel(model);
			}
		});
	}
}
