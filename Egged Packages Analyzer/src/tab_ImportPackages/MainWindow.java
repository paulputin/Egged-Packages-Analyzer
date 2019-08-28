package tab_ImportPackages;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JRadioButton;
import javax.swing.JInternalFrame;
import javax.swing.JToolBar;
import javax.swing.JDesktopPane;
import java.awt.Panel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class MainWindow {

	private JFrame frmEggedPackagesAnalyzer;
	
	DefaultTableModel PackagesTableModel = new DefaultTableModel() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Class<?> getColumnClass(int column) {
			switch (column) {
			case 0: return String.class;
			case 1: return String.class;
				default: return String.class;
			}
		}
	}; // DefaultTableModel
	
	DefaultTableModel FilesTableModel = new DefaultTableModel() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

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

	//Declare user Controls suitable for access
	JFileChooser PackagesFolderChooser;
	//PackagesFolderChooser = new JFileChooser();	
	
	
	JTable FilesTable = new JTable();
	JTable PackagesTable = new JTable();
	JButton btnStartDataExport = new JButton("Start Data Export");
	JCheckBox chckbxIsExportToExcel = new JCheckBox("Export to Excel");
	JCheckBox chckbxIsExportToSqlite = new JCheckBox("Export to SQLite");
	JRadioButton rbtnAutoFilesSelector_ValidFiles = new JRadioButton("Select Valid Files", true);
	JRadioButton rbtnAutoFilesSelector_CurrentFiles = new JRadioButton("Select Current Files", false);
	JRadioButton rbtnAutoFilesSelector_NewFiles = new JRadioButton("Select New Files", false);
	JRadioButton rbtnAutoFilesSelector_AllFiles = new JRadioButton("Select All Files", false);

	
	//declare EggedPackages and whether to use them
	HashMap<String, Boolean> EggedExistingPackages = new HashMap<String, Boolean>();
	List<List<String>> EggedActivePackages = new ArrayList<>();
	boolean isPackagesReadCorrectly = false;
	EggedData MyEggedData = new EggedData();
	
	

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
		
		DeclareUsableEggedPackages();
		
		
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
				String FolderChooserTitle = "Select the Folder with the Egged Zipped Packages...";
				PackagesFolderChooser = new JFileChooser();
				PackagesFolderChooser.setCurrentDirectory(new java.io.File("."));
				PackagesFolderChooser.setDialogTitle(FolderChooserTitle);
				PackagesFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				PackagesFolderChooser.setAcceptAllFileFilterUsed(false);
				
				if (PackagesFolderChooser.showOpenDialog(PackagesFolderChooser) == JFileChooser.APPROVE_OPTION) {
					//System.out.println("getCurrentDirectory(): " +  PackagesFolderChooser.getCurrentDirectory());
					//System.out.println("getSelectedFile() : " +  PackagesFolderChooser.getSelectedFile());
					
					String SelectedFolder = PackagesFolderChooser.getSelectedFile().toString();
					
					FormActiveEggedPackages(SelectedFolder);

					// check if Packages Exist
					// Unzip Packages
					// Read Version files
					// Read Data Files Headers
					// Fill in the Table with File Names data
					
					if (AllActiveEggedPackagesExist()) {
						if (AllFilesunzippedSuccessfully()) {
							if (VersionFilesReadSuccessfully()){
								if (DataFilesHeadersReadSuccessfully()) {
									if (FilledTablesWithDataSuccessfully()) {
										isPackagesReadCorrectly = true;
									}else {
										//btnStartDataExport.setEnabled = false;
										JOptionPane.showMessageDialog(null, "The Table was not filled with Data successfully");
										ClearTheTables();
									}
									
								} else {
									JOptionPane.showMessageDialog(null, "Unexpected error reading data file(s)");
									ClearTheTables();
								}
							} else {
								JOptionPane.showMessageDialog(null, "Unexpected error reading version file(s)");
								ClearTheTables();
							}
						} else {
							JOptionPane.showMessageDialog(null, "Enexpeced error while unzipping package(s)");
							ClearTheTables();
						}
					} else 
					{
						String FileNames = FormActivePackageNames();
						JOptionPane.showMessageDialog(null, "Error:\nNot all packages exist in the selected folder\nThe expected packages are:\n" + FileNames);
						ClearTheTables();
					}
					
						
						//TODO
						// 6. Display Packages and Files Info for Selecting
						// display all files in the table

					}
			
					//No Folder was Selected...
					else {
						//isPackageFolderSelected = false;
						//textPackagesFolder.setText("");
						//btnNewButton_2.setEnabled(false);
						System.out.println("No Packages Folder Selection ");
						ClearTheTables();
					}
			
			// End Button Click	
			} //Action Performed
		});
		btnSelectFolder.setBounds(10, 7, 261, 23);
		panel.add(btnSelectFolder);
		

		
		
		//JButton btnStartDataExport = new JButton("Start Data Export");
		btnStartDataExport.setEnabled(false);
		btnStartDataExport.setBounds(207, 383, 197, 23);
		panel.add(btnStartDataExport);
		
		JLabel lblProgressLabel = new JLabel("");
		lblProgressLabel.setBounds(10, 417, 579, 14);
		panel.add(lblProgressLabel);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(10, 442, 579, 14);
		panel.add(progressBar);
		
		JButton btnSelectSQLiteFile = new JButton("Select SQLite File");
		btnSelectSQLiteFile.setIcon(null);
		btnSelectSQLiteFile.setBounds(452, 7, 137, 23);
		panel.add(btnSelectSQLiteFile);
		
		JLabel labelSelectFilesToExportToExcel = new JLabel("Select Files To Export To Excel");
		labelSelectFilesToExportToExcel.setBounds(10, 150, 261, 14);
		panel.add(labelSelectFilesToExportToExcel);
		

		//JTable PackagesTable = new JTable();
		PackagesTable.setEnabled(false);
		PackagesTable.setBounds(10, 94, 579, 264);
		
		JScrollPane PackagesTableScrollPane = new JScrollPane();
		PackagesTableScrollPane.setBounds(10, 41, 261, 98);
		PackagesTableScrollPane.setViewportView(PackagesTable);
		panel.add(PackagesTableScrollPane);
		
//		DefaultTableModel PackagesTableModel = new DefaultTableModel() {
//			public Class<?> getColumnClass(int column) {
//				switch (column) {
//				case 0: return String.class;
//				case 1: return String.class;
//					default: return String.class;
//				}
//			}
//		}; // DefaultTableModel
			

		PackagesTable.setModel(PackagesTableModel);
		
		PackagesTableModel.addColumn("Package");
		PackagesTableModel.addColumn("Data Ver");
		PackagesTableModel.addColumn("Data Struct");
		
		
		//JTable FilesTable = new JTable();
		FilesTable.setEnabled(false);
		FilesTable.setBounds(10, 94, 579, 264);
		//panel.add(table);
		
		JScrollPane FilesTableScrollPane = new JScrollPane();
		FilesTableScrollPane.setBounds(10, 175, 579, 197);
		FilesTableScrollPane.setViewportView(FilesTable);
		panel.add(FilesTableScrollPane);
		
//		DefaultTableModel FilesTableModel = new DefaultTableModel() {
//			public Class<?> getColumnClass(int column) {
//				switch (column) {
//				case 0: return Boolean.class;
//				case 1: return String.class;
//				case 2: return String.class;
//				case 3: return String.class;
//				case 4: return String.class;
//				case 5: return String.class;
//					default: return String.class;
//				}
//			}
//		}; // DefaultTableModel
		
		FilesTable.setModel(FilesTableModel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 41, 261, 98);
		panel.add(scrollPane);
		
		FilesTableModel.addColumn("Export");
		FilesTableModel.addColumn("Package");
		FilesTableModel.addColumn("File Name");
		FilesTableModel.addColumn("Data Ver");
		FilesTableModel.addColumn("Start Date");
		FilesTableModel.addColumn("End Date");
		
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Auto Files Selector", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(281, 7, 161, 132);
		panel.add(panel_3);
		panel_3.setLayout(null);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Export Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(447, 46, 143, 93);
		panel.add(panel_4);
		panel_4.setLayout(null);
		chckbxIsExportToExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxIsExportToExcel.isSelected()) {
					EnableControls();
					FilledTablesWithDataSuccessfully();
					PackagesTable.setEnabled(true);
					FilesTable.setEnabled(true);
				} else {
					ClearTheTables();
					DisableControls();
					chckbxIsExportToExcel.setEnabled(true);
					chckbxIsExportToSqlite.setEnabled(true);
					PackagesTable.setEnabled(false);
					FilesTable.setEnabled(false);
					if (chckbxIsExportToSqlite.isSelected() == true) {
						btnStartDataExport.setEnabled(true);
					}
				} //if else
			} // actionPerformed
		});
		chckbxIsExportToExcel.setSelected(true);
		
		//JCheckBox chckbxIsExportToExcel = new JCheckBox("Export to Excel");
		chckbxIsExportToExcel.setBounds(6, 16, 131, 23);
		panel_4.add(chckbxIsExportToExcel);
		chckbxIsExportToExcel.setEnabled(false);
		chckbxIsExportToSqlite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxIsExportToSqlite.isSelected()) {
					btnStartDataExport.setEnabled(true);
				} else {
					if (chckbxIsExportToExcel.isSelected() == false) {
						btnStartDataExport.setEnabled(false);
					}
				}
			}
		});
		chckbxIsExportToSqlite.setSelected(true);
		
		//JCheckBox chckbxIsExportToSqlite = new JCheckBox("Export to SQLite");
		chckbxIsExportToSqlite.setBounds(6, 42, 131, 23);
		panel_4.add(chckbxIsExportToSqlite);
		chckbxIsExportToSqlite.setEnabled(false);

		//JRadioButton rbtnAutoFilesSelector_ValidFiles = new JRadioButton("Select Valid Files", true);
		rbtnAutoFilesSelector_ValidFiles.setEnabled(false);
		rbtnAutoFilesSelector_ValidFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Select Valid Files");
				
				// Clear SetIsExportToExcel property
				for (int i = 0; i < MyEggedData.getEggedDataFileList().size(); i++) {
					MyEggedData.getEggedDataFileList().get(i).getFileHeader().SetIsExportToExcel(false);
				}
				
				// Clear table selections
				ClearTableBooleanSelection(FilesTableModel);
				
				
				
				
				//go through MyEggedData
				// if isValidFale -> select in table, select isExport
				for (int i = 0; i < MyEggedData.getEggedDataFileList().size(); i++) {
					if (MyEggedData.getEggedDataFileList().get(i).getFileHeader().getIsValidFile() == true) {
						
						// select isExport in Data
						MyEggedData.getEggedDataFileList().get(i).getFileHeader().SetIsExportToExcel(true);
						
						// find row in Table
						int row = getRowByValue(FilesTableModel, MyEggedData.getEggedDataFileList().get(i).getFileHeader().getFileNameFull());
						
						// mark isExportToExcel column
						FilesTableModel.setValueAt(true, row, 0);						
					}//if
				}//for
			}//actionPerformed
		});
		rbtnAutoFilesSelector_ValidFiles.setBounds(6, 19, 149, 23);
		panel_3.add(rbtnAutoFilesSelector_ValidFiles);
		
		//JRadioButton rbtnAutoFilesSelector_CurrentFiles = new JRadioButton("Select Current Files", false);
		rbtnAutoFilesSelector_CurrentFiles.setEnabled(false);
		rbtnAutoFilesSelector_CurrentFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Select Current Files");
				
				// Clear SetIsExportToExcel property
				for (int i = 0; i < MyEggedData.getEggedDataFileList().size(); i++) {
					MyEggedData.getEggedDataFileList().get(i).getFileHeader().SetIsExportToExcel(false);
				}
				
				// Clear table selections
				ClearTableBooleanSelection(FilesTableModel);

				
				//go through MyEggedData
				// if isCurrentFale -> select in table, select isExport
				for (int i = 0; i < MyEggedData.getEggedDataFileList().size(); i++) {
					if (MyEggedData.getEggedDataFileList().get(i).getFileHeader().getIsCurrentFile() == true) {
						
						// select isExport in Data
						MyEggedData.getEggedDataFileList().get(i).getFileHeader().SetIsExportToExcel(true);
						
						// find row in Table
						int row = getRowByValue(FilesTableModel, MyEggedData.getEggedDataFileList().get(i).getFileHeader().getFileNameFull());
						
						// mark isExportToExcel column
						FilesTableModel.setValueAt(true, row, 0);						
					}//if
				}//for
			}//actionPerformed
		});
		rbtnAutoFilesSelector_CurrentFiles.setBounds(6, 42, 149, 23);
		panel_3.add(rbtnAutoFilesSelector_CurrentFiles);
		
		//JRadioButton rbtnAutoFilesSelector_NewFiles = new JRadioButton("Select New Files", false);
		rbtnAutoFilesSelector_NewFiles.setEnabled(false);
		rbtnAutoFilesSelector_NewFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Select New Files");
				
				// Clear SetIsExportToExcel property
				for (int i = 0; i < MyEggedData.getEggedDataFileList().size(); i++) {
					MyEggedData.getEggedDataFileList().get(i).getFileHeader().SetIsExportToExcel(false);
				}
				
				// Clear table selections
				ClearTableBooleanSelection(FilesTableModel);

				
				//go through MyEggedData
				// if isNewFale -> select in table, select isExport
				for (int i = 0; i < MyEggedData.getEggedDataFileList().size(); i++) {
					if (MyEggedData.getEggedDataFileList().get(i).getFileHeader().getIsNewFile() == true) {
						
						// select isExport in Data
						MyEggedData.getEggedDataFileList().get(i).getFileHeader().SetIsExportToExcel(true);
						
						// find row in Table
						int row = getRowByValue(FilesTableModel, MyEggedData.getEggedDataFileList().get(i).getFileHeader().getFileNameFull());
						
						// mark isExportToExcel column
						FilesTableModel.setValueAt(true, row, 0);						
					}//if
				}//for
			}//actionPerformed
		});
		rbtnAutoFilesSelector_NewFiles.setBounds(6, 68, 149, 23);
		panel_3.add(rbtnAutoFilesSelector_NewFiles);

		//JRadioButton rbtnAutoFilesSelector_AllFiles = new JRadioButton("Select All Files", false);
		rbtnAutoFilesSelector_AllFiles.setEnabled(false);
		rbtnAutoFilesSelector_AllFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Select All Files");
				
				// Clear SetIsExportToExcel property
				for (int i = 0; i < MyEggedData.getEggedDataFileList().size(); i++) {
					MyEggedData.getEggedDataFileList().get(i).getFileHeader().SetIsExportToExcel(false);
				}
				
				// Clear table selections
				ClearTableBooleanSelection(FilesTableModel);

				
				//go through MyEggedData
				// for every file select in table, select isExport
				for (int i = 0; i < MyEggedData.getEggedDataFileList().size(); i++) {
						// select isExport in Data
						MyEggedData.getEggedDataFileList().get(i).getFileHeader().SetIsExportToExcel(true);
						
						// find row in Table
						int row = getRowByValue(FilesTableModel, MyEggedData.getEggedDataFileList().get(i).getFileHeader().getFileNameFull());
						
						// mark isExportToExcel column
						FilesTableModel.setValueAt(true, row, 0);						
				}//for
			} //actionPerformed
		});
		rbtnAutoFilesSelector_AllFiles.setBounds(6, 94, 149, 23);
		panel_3.add(rbtnAutoFilesSelector_AllFiles);

		ButtonGroup RadioButtonGroup = new ButtonGroup();
		RadioButtonGroup.add(rbtnAutoFilesSelector_ValidFiles);
		RadioButtonGroup.add(rbtnAutoFilesSelector_CurrentFiles);
		RadioButtonGroup.add(rbtnAutoFilesSelector_NewFiles);
		RadioButtonGroup.add(rbtnAutoFilesSelector_AllFiles);

		
		
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Validator View", null, panel_1, null);
		tabbedPane.setEnabledAt(1, false);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("TIM View", null, panel_2, null);
		tabbedPane.setEnabledAt(2, false);
		
		
		
		
	} // initialize()

	
	
	
	
	
// IMPLEMENTED CLASS METHODS
////////////////////////////	
	
	
	void DeclareUsableEggedPackages () {
		//This method is used to declare EggedPackages and whether to use them
		EggedExistingPackages.clear();
		EggedExistingPackages.put ("LineStop", 	true);
		EggedExistingPackages.put ("Prices", 	true);
		EggedExistingPackages.put ("Params", 	false);
		EggedExistingPackages.put ("BlackList", false);
		EggedExistingPackages.put ("Dnm8000", 	false);
		EggedExistingPackages.put ("Pub8000", 	false);
	} //DeclareUsableEggedPackages
	
	
	
	boolean AllActiveEggedPackagesExist() {
		// This method checks if all active egged packages exist as files in the selected folder
		
		boolean isAllFilesExist = true;
		
		for (int i = 0; i < EggedActivePackages.size(); i++) {
			
			File f = new File(EggedActivePackages.get(i).get(2)); //SET
			if (!f.exists() || !f.isFile()) {
				isAllFilesExist = false;
			}
		}
		return isAllFilesExist;
	} //AllActiveEggedPackagesExist
	

	
	void FormActiveEggedPackages (String SelectedFolder) {
		//This method creates a list of egged active packages
		
		//start with an empty List
		EggedActivePackages.clear();
		
		for (String i:EggedExistingPackages.keySet()) {
			if (EggedExistingPackages.get(i) == true) {
				String line =  i + "," + i + ".zip," + SelectedFolder + "\\" + i + ".zip," + SelectedFolder + "\\" + i;
				String values[] = line.split(",",-1);
				
				//0 = PackageName
				//1 = PackageName.zip
				//2 = Package Full Path
				//3 = Path where to unzip the package
				EggedActivePackages.add(Arrays.asList(values));
			}
		} // for
	} // FormActiveEggedPackages
	

	
	String FormActivePackageNames () {
		// This method returns a string with active egged packages names
		
		String PackagesNames = "";
		for (int i = 0; i < EggedActivePackages.size(); i++) {
			if (PackagesNames.length() > 0) {
				PackagesNames = PackagesNames + "\n" + EggedActivePackages.get(i).get(1); // SET
			}
			else {
				PackagesNames = EggedActivePackages.get(i).get(1); // SET
			}
		}
		return PackagesNames;
	} // FormActivePackageNames

	
	
	boolean AllFilesunzippedSuccessfully() {
		// This method unzips all active egged packages
		
		boolean isAllFilesUnziped = true;
		boolean isAllFilesExist = true;
		
		Unziper MyUnziper = new Unziper();
		
		for (int i = 0; i < EggedActivePackages.size(); i++) {
			
			File f = new File(EggedActivePackages.get(i).get(2)); // SET
			if (f.exists() && f.isFile() && isAllFilesExist) {
				if (MyUnziper.UnzipFileIntoFolder(f, EggedActivePackages.get(i).get(3)) != true) { // SET
					isAllFilesUnziped = false;
				}
			} else {
				isAllFilesExist = false;
				JOptionPane.showMessageDialog(null, "The Selected Folder Doesn't Contain: "+ EggedActivePackages.get(i).get(1) +"\nPlease Choose Another Folder..."); // SET
			}
		} //for

		//System.out.println("AllFilesunzippedSuccessfully : " + isAllFilesUnziped);
		return isAllFilesUnziped;
	} //UnzipActivePackages
	
	
	
	
	
	
	boolean VersionFilesReadSuccessfully () {
		// This method reads the versions files into the EggedData object

		boolean isAllVersionFilesRead = true; 
		
		EggedPackageFileReader MyPackageFileReader = new EggedPackageFileReader();
		
		//clearing MyEggedData from all files
		MyEggedData.ClearData();
		
		try {
			for (int i = 0; i < EggedActivePackages.size(); i++) {
				File f = new File(EggedActivePackages.get(i).get(3) + "\\Version.txt"); // SET
				if (f.exists()) {
	//				System.out.println(f + " exist");
	
					//create new EggedVersionFile: read data from f, setPackageName from EggedActivePackages.get(i).get(0)
					EggedVersionFile evf = new EggedVersionFile(MyPackageFileReader.ReadVersionFile(f), EggedActivePackages.get(i).get(0));
					
	//				System.out.println("PackageName: " + evf.getPackageName());
	//				System.out.println("PackageDataVer: "+ evf.getPackageDataVer());
	//				System.out.println("PacakageDataStructure: " + evf.getPacakageDataStructure());
	//				System.out.println(evf.getData().toString());
					
					MyEggedData.ImportNewVersionFile(evf);
				} //if (f.exists())
			}//for
		}
		catch (Exception e) {
			System.out.println("VersionFilesReadSuccessfully: Exception occured: " + e.toString());
			isAllVersionFilesRead = false;
		} //catch

		return isAllVersionFilesRead;
	} //isAllVersionFilesRead
	
	
	
	
	
	boolean DataFilesHeadersReadSuccessfully () {
		// This method reads the data files into the EggedData object

		boolean isAllHeadersRead = true;
		String FileNameShort, FileExtension;
		
		EggedPackageFileReader MyPackageFileReader = new EggedPackageFileReader();
		
		try {
			
			for (int i = 0; i < MyEggedData.getEggedVersionFileList().size()   ; i++) {
				for (int j = 0; j < MyEggedData.getEggedVersionFileList().get(i).getData().size(); j ++) {
					for (int k = 0; k < EggedActivePackages.size(); k++) {
						
						String PackageName = EggedActivePackages.get(k).get(0);
						String FileNameFull = MyEggedData.getEggedVersionFileList().get(i).getData().get(j).get(0);
						String filePath = EggedActivePackages.get(k).get(3) + "\\" + FileNameFull;
						
						File f = new File(filePath);
						if (f.exists()) {						
							if (FileNameFull.length() > 4) {
								FileNameShort = FileNameFull.substring(0, FileNameFull.length()-4);
								FileExtension = FileNameFull.substring(FileNameFull.length()-3, FileNameFull.length()).toLowerCase();
							}
							else {
								FileNameShort = FileNameFull;
								FileExtension = FileNameFull;
							}
							
							//System.out.println(f.toString() + " exists");
							//System.out.println("FileNameFull: " + FileNameFull + "; FileNameShort: " + FileNameShort + "; FileExtension: " + FileExtension + ";");
							//System.out.println(f + " exist");
		
							//create new EggedVersionFile: read data from f, setPackageName from EggedActivePackages.get(i).get(0)
							EggedDataFileHeader edfh = new EggedDataFileHeader(MyPackageFileReader.ReadEggedDataFileHeader(f), PackageName, FileNameFull, FileNameShort, FileExtension);
	
							//create new EggedDataFile with EggedDataFileHeader
							EggedDataFile edf = new EggedDataFile(edfh);
							MyEggedData.ImportNewDataFile(edf);
							
						} //if (f.exists())
						else {
							//System.out.println("DataFilesHeadersReadSuccessfully: error :" + f + " does not exist");
						}
					}//for k
				}//for j
			}//for i
		}
		catch (Exception e) {
			System.out.println("DataFilesHeadersReadSuccessfully Exception occured: " + e.toString());
			isAllHeadersRead = false;
		} //catch

		return isAllHeadersRead;
		
	} //DataFilesHeadersReadSuccessfully
	
	
	
	
	boolean FilledTablesWithDataSuccessfully() {
		
		
		boolean isNoFileToExportToExce = true;
		// This method fills the tables for selecting what files to export to Excel
		
		//Clear the tables
		ClearTheTables();
		
		
		//Sorting EggedVersionFileList by name
		MyEggedData.getEggedVersionFileList().sort(Comparator.comparing(EggedVersionFile::getPackageName));
		
			
//		for debugging purposes		
//		for (int i = 0; i< MyEggedData.getEggedVersionFileList().size(); i++) {
//			System.out.println(MyEggedData.getEggedVersionFileList().get(i).getPackageName() + " " 
//			+ MyEggedData.getEggedVersionFileList().get(i).getPackageDataVer() + " " 
//			+ MyEggedData.getEggedVersionFileList().get(i).getPacakageDataStructure());
//		}
		
		
		
		
		
		//fill in the Packages Table
		for (int i = 0; i< MyEggedData.getEggedVersionFileList().size(); i++) {
//			for debugging purposes			
//			System.out.println(MyEggedData.getEggedVersionFileList().get(i).getPackageName() + " " 
//								+ MyEggedData.getEggedVersionFileList().get(i).getPackageDataVer() + " " 
//								+ MyEggedData.getEggedVersionFileList().get(i).getPacakageDataStructure());
		
			PackagesTableModel.addRow(new Object[] {MyEggedData.getEggedVersionFileList().get(i).getPackageName(),
					MyEggedData.getEggedVersionFileList().get(i).getPackageDataVer(),
					MyEggedData.getEggedVersionFileList().get(i).getPacakageDataStructure()});
			
		} // for
		
		
		
		//Sorting EggedDataFileList by PackageName, FileNameShort, FileExtension.reversed
		MyEggedData.getEggedDataFileList().sort(Comparator.comparing(EggedDataFile::getPackageName)
														  .thenComparing(Comparator.comparing(EggedDataFile::getFileNameShort))
														  .thenComparing(Comparator.comparing(EggedDataFile::getFileExtension)
														  .reversed())
				);

//		for debugging purposes
//		for (int i = 0; i < MyEggedData.getEggedDataFileList().size(); i++) {
//			System.out.print(MyEggedData.getEggedDataFileList().get(i).getFileHeader().getPackageName() + " ");
//			System.out.print(MyEggedData.getEggedDataFileList().get(i).getFileHeader().getFileNameFull() + " ");
//			System.out.print(MyEggedData.getEggedDataFileList().get(i).getFileHeader().getDataVer() + " ");
//			System.out.print(MyEggedData.getEggedDataFileList().get(i).getFileHeader().getStartDate() + " ");
//			System.out.println(MyEggedData.getEggedDataFileList().get(i).getFileHeader().getEndDate());
//		}
		
		

		//fill in the Files Table
		//FilesTableModel

		for (int i = 0; i < MyEggedData.getEggedDataFileList().size(); i++) {
			
			//System.out.println(MyEggedData.getEggedDataFileList().get(i).getFileHeader().getPackageName());
			if (MyEggedData.getEggedDataFileList().get(i).getIsExportToExcel() == true) {
				isNoFileToExportToExce = false;
			}
			
			FilesTableModel.addRow(new Object[] {MyEggedData.getEggedDataFileList().get(i).getIsExportToExcel(),
					MyEggedData.getEggedDataFileList().get(i).getFileHeader().getPackageName(),
					MyEggedData.getEggedDataFileList().get(i).getFileHeader().getFileNameFull(),
					MyEggedData.getEggedDataFileList().get(i).getFileHeader().getDataVer(),
					MyEggedData.getEggedDataFileList().get(i).getFileHeader().getStartDate(),
					MyEggedData.getEggedDataFileList().get(i).getFileHeader().getEndDate()
					});
		} // for
		
		EnableControls();
		if (isNoFileToExportToExce == true ) {
			rbtnAutoFilesSelector_ValidFiles.doClick();
		}
		
		return true;
	} //FilledTableWithDataSuccessfully
	
	void ClearTheTables() {
		PackagesTableModel.getDataVector().removeAllElements();
		PackagesTableModel.fireTableDataChanged();
		FilesTableModel.getDataVector().removeAllElements();
		FilesTableModel.fireTableDataChanged();
		DisableControls();
	} //ClearTheTables
	
	void EnableControls() {
		FilesTable.setEnabled(true);
		PackagesTable.setEnabled(true);
		btnStartDataExport.setEnabled(true);
		chckbxIsExportToExcel.setEnabled(true);
		chckbxIsExportToSqlite.setEnabled(true);
		rbtnAutoFilesSelector_ValidFiles.setEnabled(true);
		rbtnAutoFilesSelector_CurrentFiles.setEnabled(true);
		rbtnAutoFilesSelector_NewFiles.setEnabled(true);
		rbtnAutoFilesSelector_AllFiles.setEnabled(true);
	}//EnableControls
	
	void DisableControls() {
		FilesTable.setEnabled(false);
		PackagesTable.setEnabled(false);
		btnStartDataExport.setEnabled(false);
		chckbxIsExportToExcel.setEnabled(false);
		chckbxIsExportToSqlite.setEnabled(false);
		rbtnAutoFilesSelector_ValidFiles.setEnabled(false);
		rbtnAutoFilesSelector_CurrentFiles.setEnabled(false);
		rbtnAutoFilesSelector_NewFiles.setEnabled(false);
		rbtnAutoFilesSelector_AllFiles.setEnabled(false);
	} //DisableControls
	
	 int getRowByValue(DefaultTableModel model, Object value) {
		    for (int i = model.getRowCount() - 1; i >= 0; --i) {
		        for (int j = model.getColumnCount() - 1; j >= 0; --j) {
		            if (model.getValueAt(i, j).equals(value)) {
		                // what if value is not unique?
		                return i;
		            }
		        }
		    }
			return 0;
		 } //getRowByValue
	 
	 void ClearTableBooleanSelection(DefaultTableModel model) {
		    for (int i = model.getRowCount() - 1; i >= 0; --i) {
		    	model.setValueAt(false, i, 0);
		    }
	 }

	 

	
} //class MainWindow
