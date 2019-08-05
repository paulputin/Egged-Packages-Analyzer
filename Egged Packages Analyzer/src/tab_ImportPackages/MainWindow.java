package tab_ImportPackages;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JLayeredPane;
import javax.swing.JCheckBox;
import javax.swing.JProgressBar;

public class MainWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 640, 555);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 604, 495);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Import Packages", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblFolderWithEgged = new JLabel("Folder with Egged Packages:");
		lblFolderWithEgged.setBounds(10, 11, 291, 14);
		panel.add(lblFolderWithEgged);
		
		JTextPane textPackagesFolder = new JTextPane();
		textPackagesFolder.setEditable(false);
		textPackagesFolder.setBounds(10, 36, 579, 20);
		panel.add(textPackagesFolder);
		
		JButton btnSelectFolder = new JButton("Select Folder with Egged Packages");
		btnSelectFolder.setBounds(10, 67, 579, 23);
		panel.add(btnSelectFolder);
		
		JList list = new JList();
		list.setBounds(10, 145, 579, 212);
		panel.add(list);
		
		JCheckBox chckbxIsExportToExcel = new JCheckBox("Export to Excel");
		chckbxIsExportToExcel.setEnabled(false);
		chckbxIsExportToExcel.setBounds(10, 97, 135, 23);
		panel.add(chckbxIsExportToExcel);
		
		JCheckBox chckbxIsExportToSqlite = new JCheckBox("Export to SQLite");
		chckbxIsExportToSqlite.setSelected(true);
		chckbxIsExportToSqlite.setEnabled(false);
		chckbxIsExportToSqlite.setBounds(454, 97, 135, 23);
		panel.add(chckbxIsExportToSqlite);
		
		JButton btnStartDataExport = new JButton("Start Data Export");
		btnStartDataExport.setEnabled(false);
		btnStartDataExport.setBounds(207, 368, 197, 23);
		panel.add(btnStartDataExport);
		
		JLabel lblProgressLabel = new JLabel("");
		lblProgressLabel.setBounds(10, 417, 579, 14);
		panel.add(lblProgressLabel);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(10, 442, 579, 14);
		panel.add(progressBar);
		
		JButton btnSelectSQLiteFile = new JButton("Select SQLite File");
		btnSelectSQLiteFile.setBounds(392, 7, 197, 23);
		panel.add(btnSelectSQLiteFile);
		
		JLabel labelSelectFilesToExportToExcel = new JLabel("Select Files To Export To Excel");
		labelSelectFilesToExportToExcel.setBounds(10, 127, 291, 14);
		panel.add(labelSelectFilesToExportToExcel);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Validator View", null, panel_1, null);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("TIM View", null, panel_2, null);
		
		
	}
}
