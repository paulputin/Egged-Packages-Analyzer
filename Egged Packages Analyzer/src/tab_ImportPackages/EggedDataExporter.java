package tab_ImportPackages;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class EggedDataExporter extends Thread{
	
	boolean isExportToExcelNeeded, isExportToSQLiteNeeded;
	String CurrentFolder;
	
    JLabel lblProgressLabel;
    JProgressBar progressBar;

	private List<EggedVersionFile> EggedVersionFileList = new ArrayList<EggedVersionFile>();
	private List<EggedDataFile> EggedDataFileList = new ArrayList<EggedDataFile>();
	
	//Constructor
	EggedDataExporter(boolean isExportToExcelNeeded,
		 	  		  boolean isExportToSQLiteNeeded,
		 	  		  List<EggedVersionFile> EggedVersionFileList,
		 	  		  List<EggedDataFile> EggedDataFileList,
		 	  		  String CurrentFolder,
		 	  		  JLabel lblProgressLabel,
		 	  		  JProgressBar progressBar) {
		
		setisExportToExcelNeeded(isExportToExcelNeeded);
		setisExportToSQLiteNeeded(isExportToSQLiteNeeded);
		setEggedVersionFileList(EggedVersionFileList);
		setEggedDataFileList(EggedDataFileList);
		setCurrentFolder(CurrentFolder);
		setlblProgressLabel(lblProgressLabel);
		setJProgressBar(progressBar);
		
	} //Constructor
	
	//Setters
	private void setisExportToExcelNeeded(boolean isExportToExcelNeeded) {
		this.isExportToExcelNeeded = isExportToExcelNeeded;
	}
	
	private void setisExportToSQLiteNeeded(boolean isExportToSQLiteNeeded) {
		this.isExportToSQLiteNeeded = isExportToSQLiteNeeded;
	}
	
	private void setEggedVersionFileList(List<EggedVersionFile> EggedVersionFileList) {
		this.EggedVersionFileList = EggedVersionFileList;
	}
	
	private void setEggedDataFileList(List<EggedDataFile> EggedDataFileList) {
		this.EggedDataFileList = EggedDataFileList;
	}
	
	private void setCurrentFolder(String CurrentFolder) {
		this.CurrentFolder = CurrentFolder;
	}
	
	private void setlblProgressLabel(JLabel lblProgressLabel) {
		this.lblProgressLabel = lblProgressLabel;
	}
	
	private void setJProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}
	
	
	@Override
	public void run() {
		
		// Count Actions
		// Execute Excel Exporter
		// Execute SQLite Exporter
		
		int ActionsCount = 0;
		
		
		//Counting Excel Exporter Actions
		if (isExportToExcelNeeded) {
			ExcelProvider MyExcelProvider = new ExcelProvider(EggedDataFileList, 
															  CurrentFolder, 
															  lblProgressLabel, 
															  progressBar);
			ActionsCount = ActionsCount + MyExcelProvider.getExcelExportActionsCount();
		}

		//Counting SQLite Exporter Actions
		if (isExportToSQLiteNeeded) {
			SQLiteProvider mySQLiteProvider = new SQLiteProvider(EggedVersionFileList, 
																 EggedDataFileList, 
																 CurrentFolder, 
																 lblProgressLabel, 
																 progressBar);
			ActionsCount = ActionsCount + mySQLiteProvider.getSQLiteExportActionsCount();
		}

		//Setting Progress Bar
		this.progressBar.setMinimum(0);
		this.progressBar.setMaximum(ActionsCount);
		this.progressBar.setValue(0);

		//Executing Excel Export
		if (isExportToExcelNeeded) {
			ExcelProvider MyExcelProvider = new ExcelProvider(EggedDataFileList, 
					  CurrentFolder, 
					  lblProgressLabel, 
					  progressBar);
			MyExcelProvider.ExportExcelData();
		}
		
		
		//Small Pause between Excel Exporter and SQLite Exporter
		try {
			if (isExportToExcelNeeded) {
				Thread.sleep(2000);
			} else {
				Thread.sleep(100);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Executing SQLite Export
		if (isExportToSQLiteNeeded) {
			SQLiteProvider mySQLiteProvider = new SQLiteProvider(EggedVersionFileList, 
																 EggedDataFileList, 
																 CurrentFolder, 
																 lblProgressLabel, 
																 progressBar);
			mySQLiteProvider.ExportSQLiteData();
		}


		
		
		
		
	} //run()

} //class EggedDataExporter
