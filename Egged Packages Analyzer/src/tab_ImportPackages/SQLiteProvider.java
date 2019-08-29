package tab_ImportPackages;

import java.io.File;
import java.nio.file.Files;
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
			
			// Deleting Older Version of SQLite File
			File file = new File(CurrentFolder + "\\ValidatorPackages.db");
			try {
				Files.deleteIfExists(file.toPath());
			} catch (Exception e) {
				System.out.println("Creaing Database: Previous file " + CurrentFolder + "\\ValidatorPackages.db cannot be deleted. " + e.toString());
			}
			
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
		
		// Countable Actions:
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
			if (EggedDataFileList.get(i).getFileHeader().getIsCurrentFile()) {
				ActionsCount++;				
			}

		}
		
		//Counting DBPopulateStoredProcedures
		// TODO
		
		return ActionsCount;
	} //getSQLiteExportActionsCount
	
	public void ExportSQLiteData () {
		conn = DbConnect(CurrentFolder);
		if (DBPopulateVersions() == true) {
			if (DBPopulateData() == true) {
				if (DBPopulateStoredProcedures() == true) {
					lblProgressLabel.setText("Creaing Database: Database Creaded Successfully.");			
				}else {
					System.out.println("SQLiteProvider: Error in populating Stored Procedures.");
				}
			}else {
				System.out.println("SQLiteProvider: Error in populating Data Tables.");
			}
		} else {
			System.out.println("SQLiteProvider: Error in populating Versions Table.");
		} // if + else
		
	} //ExportSQLiteData
	
	
	
	private boolean DBPopulateVersions() {

		lblProgressLabel.setText("Creaing Database: Pupulating Versions table");

		CreateTable_Package();
		CreateTable_Files();
		
		//InsertIntoTable_Package
		for (int i = 0; i < EggedVersionFileList.size(); i++) {
			String Package = EggedVersionFileList.get(i).getPackageName();
			String DataVer = EggedVersionFileList.get(i).getPackageDataVer();
			String DataStruct = EggedVersionFileList.get(i).getPacakageDataStructure();
			InsertIntoTable_Package(Package, DataVer, DataStruct);
		} // for
		
		//InsertIntoTable_Files
		for (int i = 0; i < EggedVersionFileList.size(); i++) {
			String Package = EggedVersionFileList.get(i).getPackageName();
			for (int j = 0; j < EggedVersionFileList.get(i).getData().size(); j++) {
				String FileNameFull = EggedVersionFileList.get(i).getData().get(j).get(0);
				String DataVer="", StartDate="", EndDate="";
				for (int k = 0; k < EggedDataFileList.size(); k++) {
					if (FileNameFull == EggedDataFileList.get(k).getFileNameFull()) {
						DataVer = EggedDataFileList.get(k).getFileHeader().getDataVer();
						StartDate = EggedDataFileList.get(k).getFileHeader().getStartDate();
						EndDate = EggedDataFileList.get(k).getFileHeader().getEndDate();
					}
				} // for K
 
				InsertIntoTable_Files(Package, FileNameFull, DataVer, StartDate, EndDate);
			} // for j
		} // for i
		
		progressBar.setValue(progressBar.getValue()+1);
		
		return true;
		
	} // DBPopulateVersions
	
	private boolean DBPopulateData() {
		for (int i = 0; i < EggedDataFileList.size(); i++) {
			if (EggedDataFileList.get(i).getFileHeader().getIsCurrentFile()) {
				lblProgressLabel.setText("Creaing Database: Populating " + EggedDataFileList.get(i).getFileNameFull()+ " table");
				CreateAndPopulateTable(EggedDataFileList.get(i).getFileNameShort(), EggedDataFileList.get(i).getData());
				progressBar.setValue(progressBar.getValue()+1);
			}
		}

		return true;
	} //DBPopulateData
	
	private boolean DBPopulateStoredProcedures() {
		
		return true;
	} //DBPopulateStoredProcedures
	
	private void CreateAndPopulateTable(String FileNameShort, List<List<String>> Data) {
		
		// Create Table
		// prepare SQL Statement
		// Create a new instance of Statement
		// call ExecuteUpdate for the Statement object
		
		// Populate Table
		
		
		// Prepare sql statement for creating table
		String sql = "CREATE TABLE IF NOT EXISTS [" + FileNameShort + "] (\n"
					+ "id integer PRIMARY KEY AUTOINCREMENT NOT NULL, \n"; 
		
		for (int i = 0; i < Data.get(0).size(); i++) {
			String ColumnName = Data.get(0).get(i);
			if (i == Data.get(0).size()-1) {
				sql = sql +"[" + ColumnName + "] text \n";	
			} else {
				sql = sql + "[" +ColumnName + "] text, \n";
			}
		} // for
		sql = sql +  ");";

		//System.out.println(sql);

		// Executing sql statement for creating table
		try (Statement stmp = conn.createStatement()) {
			stmp.execute(sql);
			} catch (SQLException e) {
				System.out.println("Class: ExportSQLiteData Method: CreateAndPopulateTable (Create )SQLException " + e.toString());
		}
		
		//Prepare sqlInsert Statement
		
		
		//"INSERT INTO warehouses(name,capacity) VALUES(?,?)";
		String SQLInsertBeginning = "INSERT INTO [" + FileNameShort + "] (";
		for (int i = 0; i < Data.get(0).size(); i++) {
			String ColumnName = Data.get(0).get(i);
			if (i == Data.get(0).size()-1) {
				SQLInsertBeginning = SQLInsertBeginning +"[" + ColumnName + "]) VALUES(";	
			} else {
				SQLInsertBeginning = SQLInsertBeginning + "[" +ColumnName + "],";
			}
		} // for i
		// Formed SQLInsertBeginning
		
 
		// Formatting SQLInsertEndings and Executing TRANSACTION BEGIN/COMMIT with a batch
		String SQLInsertFinal="";
		try {
			Statement stat = conn.createStatement();
			stat.addBatch("BEGIN TRANSACTION;");
			
			String SQLInsertEnding="";
			boolean wasExecuted = false;
			
			//Create Ending
			for (int i = 1; i < Data.size(); i++) {
				if (wasExecuted) {
					//Forming the beginning of transaction for every N lines
					stat.addBatch("BEGIN TRANSACTION;");
					wasExecuted = false;
				}
				for (int j = 0; j < Data.get(i).size(); j++) {
					String ColumnData = Data.get(i).get(j);
					//removing the " symbol from the Data. It results in errors in the transactions 
					ColumnData = ColumnData.replaceAll("[\"]", "");
					if (j == Data.get(i).size()-1) {
						SQLInsertEnding = SQLInsertEnding + "\"" + ColumnData + "\""+ ");";	
					} else {
						SQLInsertEnding = SQLInsertEnding + "\"" + ColumnData + "\"" + ',';
					}
				}//for j
				SQLInsertFinal = SQLInsertBeginning + SQLInsertEnding;
				//System.out.println(SQLInsertFinal);
				stat.addBatch(SQLInsertFinal);
				SQLInsertEnding="";

				//Execute COMMIT For Every N lines 
				if ((i%1000==0) || (i  == Data.size()-1)) {
					stat.addBatch("COMMIT;");
					stat.executeBatch();
					wasExecuted = true;
				} // if (i%100==0)
			} // for i
		
		} catch (SQLException e) {
			System.out.println("Class: ExportSQLiteData Method: CreateAndPopulateTable (Insert) SQLException " + e.toString());
			//System.out.println(SQLInsertFinal);
        }

	} //CreateAndPopulateTable
	

	
	private void CreateTable_Package() {
		
		// Prepare sql statement
		String sql = "CREATE TABLE IF NOT EXISTS Package (\n"
				+ "id integer PRIMARY KEY AUTOINCREMENT NOT NULL, \n"
				+ "Package text NOT NULL, \n"
				+ "DataVer text, \n"
				+ "DataStruct text \n"
				+ ");";
	
		// Executing sql statement
		try (Statement stmp = conn.createStatement()) {
			stmp.execute(sql);
			} catch (SQLException e) {
				System.out.println("Class: ExportSQLiteData Method: CreateTable_Packages SQLException " + e.toString());
		}
	} //CreateTable_Packages
	
	private void InsertIntoTable_Package(String Package, String DataVer, String DataStruct) {
		
		// Prepare sql statement
		String sql = "INSERT INTO Package(Package,DataVer,DataStruct) VALUES(?,?,?)";
		
		// Executing sql statement
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
		    pstmt.setString(1, Package);
		    pstmt.setString(2, DataVer);
		    pstmt.setString(3, DataStruct);
		    pstmt.executeUpdate();
        	} catch (SQLException e) {
        			System.out.println("Class: ExportSQLiteData Method: InsertIntoTable_Package SQLException " + e.toString());
        }
	} // InsertIntoTable_Package
	
	private void CreateTable_Files () {
		
		// Prepare sql statement
		String sql = "CREATE TABLE IF NOT EXISTS Files (\n"
				+ "id integer PRIMARY KEY AUTOINCREMENT NOT NULL, \n"
				+ "Package text NOT NULL, \n"
				+ "FileNameFull text NOT NULL, \n"
				+ "DataVer text, \n"
				+ "StartDate text, \n"
				+ "EndDate text \n"
				+");";
	
		// Executing sql statement
		try (Statement stmp = conn.createStatement()) {
				stmp.execute(sql);
			} catch (SQLException e) {
				System.out.println("Class: ExportSQLiteData Method: CreateTable_Files SQLException " + e.toString());
		}
	} //CreateTable_Files
	
	private void InsertIntoTable_Files (String Package, String FileNameFull, String DataVer, String StartDate, String EndDate) {
		
		String sql = "INSERT INTO Files(Package,FileNameFull,DataVer,StartDate,EndDate) VALUES(?,?,?,?,?)";
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
		    pstmt.setString(1, Package);
		    pstmt.setString(2, FileNameFull);
		    pstmt.setString(3, DataVer);
		    pstmt.setString(4, StartDate);
		    pstmt.setString(5, EndDate);
		    pstmt.executeUpdate();
        	} catch (SQLException e) {
        			System.out.println("Class: ExportSQLiteData Method: InsertIntoTable_Package SQLException " + e.toString());
        }
	} //InsertIntoTable_Files

	
	
	
	
	
	
	
	
	
} //class SQLiteProvider




//  TODO
//
//  1. Create tables according to FileNames, ColumnNames and Column Types;
//  2. Create VERSION tables
//  3. Populate DATA tables from EggedData Object
//  4. Create Stored Procedures
//  5. 