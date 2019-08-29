package tab_ImportPackages;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JProgressBar;



public class SQLiteProvider{
	
	String CurrentFolder;
	private Connection conn;

    JLabel lblProgressLabel;
    JProgressBar progressBar;

    private List<EggedVersionFile> EggedVersionFileList = new ArrayList<EggedVersionFile>();
	private List<EggedDataFile> EggedDataFileList = new ArrayList<EggedDataFile>();

	
    //Constructor
    SQLiteProvider(
	  		  	   List<EggedVersionFile> EggedVersionFileList,
	  		  	   List<EggedDataFile> EggedDataFileList,
    		       String CurrentFolder, 
    		       JLabel lblProgressLabel,
    		       JProgressBar progressBar
    		       ) {
    	setCurrentFolder(CurrentFolder);
    	setlblProgressLabel(lblProgressLabel);
    	setProgressBar(progressBar);
    	setEggedVersionFileList(EggedVersionFileList);
    	setEggedDataFileList(EggedDataFileList);
    	
    }	//Constructor
    
    private void setEggedVersionFileList (List<EggedVersionFile> EggedVersionFileList) {
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
    
    private void setProgressBar(JProgressBar progressBar) {
    	this.progressBar = progressBar;
    }
	
	
	public Connection DbConnect (String CurrentFolder) {
		try {
			Class.forName("org.sqlite.JDBC");
			
			//System.out.println ("SQLiteProvider: DbConnect: " + CurrentFolder + "\\ValidatorPackages.db");
			lblProgressLabel.setText("Creaing Database: Files is ValidatorPackages.db");
			
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + CurrentFolder + "\\ValidatorPackages.db");
			progressBar.setValue(progressBar.getValue()+1);
			
			return conn;
		}catch (Exception e) {
			System.out.println("SQLiteProvider: DbConnect: " + e.toString());
			lblProgressLabel.setText("SQLiteProvider: DbConnect: " + e.toString());
			return null;
		}
	} //DbConnect

	public int getSQLiteExportActionsCount() {
		
		// 1. DbConnect
		// 2. DBPopulateVersions
		// 3. DBPopulateData
		// 4. DBPopulateStoredProcedures
		
		int ActionsCount = 0;
		
		//Counting DBConnect
		ActionsCount++;
		
		//Counting DBPopulateVersions
		ActionsCount++;
		
		//Counting DBPopulateData
		// Count Number of Data Tables
		for (int i = 0; i < EggedDataFileList.size(); i++) {
			ActionsCount++;
		}
		
		//Counting DBPopulateStoredProcedures
		// TODO
		
		return ActionsCount;
	}
	
	public void ExportSQLiteData () {
		DbConnect(CurrentFolder);
		DBPopulateVersions();
		DBPopulateData();
		DBPopulateStoredProcedures();
		lblProgressLabel.setText("Creaing Database: Database Creaded Successfully.");
	}
	
	private void DBPopulateVersions() {
		lblProgressLabel.setText("Creaing Database: Pupulating Versions table");
		progressBar.setValue(progressBar.getValue()+1);
		
	} // DBPopulateVersions
	
	private void DBPopulateData() {
		for (int i = 0; i < EggedDataFileList.size(); i++) {
			lblProgressLabel.setText("Creaing Database: Pupulating " + EggedDataFileList.get(i).getFileNameFull()+ " table");
			progressBar.setValue(progressBar.getValue()+1);
		}

		
	} //DBPopulateData
	
	private void DBPopulateStoredProcedures() {
		
	} //DBPopulateStoredProcedures
	
} //class SQLiteProvider





//  TODO
//
//  1. Create tables according to FileNames, ColumnNames and Column Types;
//  2. Create VERSION tables
//  3. Populate DATA tables from EggedData Object
//  4. Create Stored Procedures
//  5. 