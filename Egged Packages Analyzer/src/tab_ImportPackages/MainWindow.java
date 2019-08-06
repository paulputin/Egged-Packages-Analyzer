package tab_ImportPackages;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JTable;

public class MainWindow {

	private JFrame frmEggedPackagesAnalyzer;
	
	JFileChooser PackagesFolderChooser;
	//private JTable table;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmEggedPackagesAnalyzer.setVisible(true);
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
		frmEggedPackagesAnalyzer = new JFrame();
		frmEggedPackagesAnalyzer.setResizable(false);
		frmEggedPackagesAnalyzer.setTitle("Egged Packages Analyzer v. 1.0.0");
		frmEggedPackagesAnalyzer.setBounds(100, 100, 640, 555);
		frmEggedPackagesAnalyzer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEggedPackagesAnalyzer.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 604, 495);
		frmEggedPackagesAnalyzer.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Read Packages", null, panel, null);
		panel.setLayout(null);
		
		JButton btnSelectFolder = new JButton("Select Folder with Egged Packages");
		btnSelectFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Folder Selector
				String FolderChooserTitle = "Select the Folder with the Egged Validator Zipped Packages...";
				PackagesFolderChooser = new JFileChooser();
				PackagesFolderChooser.setCurrentDirectory(new java.io.File("."));
				PackagesFolderChooser.setDialogTitle(FolderChooserTitle);
				PackagesFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				PackagesFolderChooser.setAcceptAllFileFilterUsed(false);
				
				if (PackagesFolderChooser.showOpenDialog(PackagesFolderChooser) == JFileChooser.APPROVE_OPTION) {
					//System.out.println("getCurrentDirectory(): " +  PackagesFolderChooser.getCurrentDirectory());
					//System.out.println("getSelectedFile() : " +  PackagesFolderChooser.getSelectedFile());
					
					String SelectedFolder = PackagesFolderChooser.getSelectedFile().toString();
					File fPricesPackage = new File(SelectedFolder+"\\Prices.zip");
					File fLineStopsPackage = new File(SelectedFolder+"\\LineStop.zip");
					File fPubPackage = new File(SelectedFolder+"\\Pub8000.zip");
					File fDnmPackage = new File(SelectedFolder+"\\Dnm8000.zip");
					
					
					//Prices, LineStop, Pub, Dnm Packages exist in the Chosen Folder....
					if (
							(fPricesPackage.exists() && fPricesPackage.isFile()) 
							&& (fLineStopsPackage.exists() 	&& fLineStopsPackage.isFile())
							&& (fPubPackage.exists() 	&& fPubPackage.isFile())
							&& (fDnmPackage.exists() 	&& fDnmPackage.isFile())
							)
					{
						
						//TODO
						// 3. Unzip Packages

						
						
						//TODO
						// 4. Read Versions Files
						
						
						
						//TODO
						// 5. Read All Files Headers
						
						
						
						//TODO
						// 6. Display Packages and Files Info for Selecting
						
						
	
						
						
						//MyUnziper.PricePackageSetter(fPricesPackage);
						//MyUnziper.LineStopsPackageSetter(fLineStopsPackage);
						//MyUnziper.PackagesFolderSetter(SelectedFolder);
						
						//MyPackageFileReader.SetPackageFolder(SelectedFolder);
						//MyPackageFileReader.SetCurrentFolder(SelectedFolder);
						//MyPackageFileReader.SetlblProgressStaus(lblProgressStaus);
						
						//MySQL.DbConnect(SelectedFolder);
						
						
					} 
						//Packages do not exist in the Chosen Folder...
						else {
							//isPackageFolderSelected = false;
							//btnNewButton_2.setEnabled(false);
							//textPackagesFolder.setText("");
							JOptionPane.showMessageDialog(null, "The Selected Folder Doesn't Contain: \nPrices.zip\nLineStop.zip\nPub800.zip\nDnm800.zip\n\nPlease Choose Another Folder...");
						}
					}
					//No Folder was Selected...
					else {
						//isPackageFolderSelected = false;
						//textPackagesFolder.setText("");
						//btnNewButton_2.setEnabled(false);
						System.out.println("No Packages Folder Selection ");
					}
			

				
				
				
				
				

				

				
				
				
			// End Button Click	
			} //Action Performed
		});
		btnSelectFolder.setBounds(10, 7, 372, 23);
		panel.add(btnSelectFolder);
		
		JCheckBox chckbxIsExportToExcel = new JCheckBox("Export to Excel");
		chckbxIsExportToExcel.setEnabled(false);
		chckbxIsExportToExcel.setBounds(10, 40, 135, 23);
		panel.add(chckbxIsExportToExcel);
		
		JCheckBox chckbxIsExportToSqlite = new JCheckBox("Export to SQLite");
		chckbxIsExportToSqlite.setEnabled(false);
		chckbxIsExportToSqlite.setBounds(247, 40, 135, 23);
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
		btnSelectSQLiteFile.setIcon(null);
		btnSelectSQLiteFile.setBounds(392, 7, 197, 23);
		panel.add(btnSelectSQLiteFile);
		
		JLabel labelSelectFilesToExportToExcel = new JLabel("Select Files To Export To Excel");
		labelSelectFilesToExportToExcel.setBounds(10, 70, 291, 14);
		panel.add(labelSelectFilesToExportToExcel);
		
		JTable table = new JTable();
		table.setEnabled(false);
		table.setBounds(10, 94, 579, 264);
		//panel.add(table);
		
		JScrollPane scroll = new JScrollPane();
		scroll.setBounds(10, 94, 579, 246);
		scroll.setViewportView(table);
		panel.add(scroll);
		
		DefaultTableModel model = new DefaultTableModel() {
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0: return Boolean.class;
				case 1: return String.class;
				case 2: return String.class;
				case 3: return String.class;
				case 4: return String.class;
				case 5: return String.class;
					default: return String.class;
				}
			}
		}; // DefaultTableModel
		
		table.setModel(model);
		
		model.addColumn("Export");
		model.addColumn("Package");
		model.addColumn("File Name");
		model.addColumn("Data Ver");
		model.addColumn("Start Date");
		model.addColumn("End Date");
		
		/*
		for (int i = 0; i < 50; i++) {
			model.addRow(new Object[0]);
			model.setValueAt(false, i, 0);
			model.setValueAt("Our Row " + (i+1), i, 1);
			model.setValueAt("Our Column 2", i, 2);
			model.setValueAt("Our Column 3", i, 3);
			model.setValueAt("Our Column 4", i, 4);
			model.setValueAt("Our Column 5", i, 5);
		}
		*/
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Validator View", null, panel_1, null);
		tabbedPane.setEnabledAt(1, false);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("TIM View", null, panel_2, null);
		tabbedPane.setEnabledAt(2, false);
		
		
	}
}
