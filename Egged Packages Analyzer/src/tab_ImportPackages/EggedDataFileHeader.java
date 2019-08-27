package tab_ImportPackages;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EggedDataFileHeader {
	
	private boolean isExportToExcel, isValidFile, isCurrentFile, isNewFile;
	private String PackageName, FileNameFull, FileNameShort, FileExtension, DataVer, StartDate, EndDate, KeyVersion, SignatureFlag;

	EggedDataFileHeader() {
		
	}//constructor
	
	EggedDataFileHeader(EggedDataFileHeader edfh, String PackageName, String FileNameFull, String FileNameShort, String FileExtension) {
		this.SignatureFlag = edfh.getSignatureFlag();
		this.KeyVersion = edfh.getKeyVersion();
		this.PackageName = PackageName;
		this.FileNameFull = FileNameFull;
		this.FileNameShort = FileNameShort;
		this.FileExtension = FileExtension;
		this.DataVer = edfh.getDataVer();
		this.StartDate = edfh.getStartDate();
		this.EndDate = edfh.getEndDate();
		
		setValidProperty();
		setCurrentProperty();
		setNewProperty();

	}//constructor
	
	
	// Setters
	public void SetIsExportToExcel (boolean isExportToExcel) {
		this.isExportToExcel = isExportToExcel;
	}

	public void setIsValidFile(boolean isValidFile) {
		this.isValidFile = isValidFile;
	}

	public void setIsCurrentFile(boolean isCurrentFile) {
		this.isCurrentFile = isCurrentFile;
	}
	
	public void setIsNewFile(boolean isNewFile) {
		this.isNewFile = isNewFile;
	}

	public void setSignatureFlag (String SignatureFlag) {
		this.SignatureFlag = SignatureFlag;
	}
	
	public void setKeyVersion(String KeyVersion) {
		this.KeyVersion = KeyVersion;
	}

	public void setPackageName (String PackageName) {
		this.PackageName = PackageName;
	}
	
	public void setFileNameFull (String FileNameFull) {
		this.FileNameFull = FileNameFull;
	}
	
	public void setFileNameShort (String FileNameShort) {
		this.FileNameShort = FileNameShort;
	}
	
	public void setFileExtension (String FileExtension) {
		this.FileExtension = FileExtension;
	}

	public void setStartDate (String StartTime) {
		this.StartDate = StartTime;
	}
	
	public void setEndDate (String EndTime) {
		this.EndDate = EndTime;
	}
	
	public void setDataVer (String DataVer) {
		this.DataVer = DataVer;
	}

	// Getters
	public boolean getIsExportToExcel () {
		return this.isExportToExcel;
	}
	
	public boolean getIsValidFile() {
		return isValidFile;
	}


	public boolean getIsCurrentFile() {
		return isCurrentFile;
	}
	
	public boolean getIsNewFile() {
		return isNewFile;
	}
	
	public String getSignatureFlag () {
		return this.SignatureFlag;
	}
	
	public String getKeyVersion () {
		return this.KeyVersion;
	}

	public String getPackageName () {
		return this.PackageName;
	}
	
	public String getFileNameFull () {
		return this.FileNameFull;
	}
	
	public String getFileNameShort () {
		return this.FileNameShort;
	}

	public String getFileExtension () {
		return this.FileExtension;
	}

	public String getStartDate () {
		return this.StartDate;
	}
	
	public String getEndDate () {
		return this.EndDate;
	}
	
	public String getDataVer () {
		return this.DataVer;
	}
	//End Getters/Setters
	
	
	//Methods
	void setCurrentProperty() {
		//The File is Current if its extension = "txt
		if (this.FileExtension.equals("txt")) {
			//System.out.println("this.FileExtension: " + this.FileExtension + " => Selected curent...");
			this.isCurrentFile = true;
		} else {
			this.isCurrentFile = false;
		}
	} //setCurrentProperty
	
	void setNewProperty() {
		//The File is New if its extension = "new"
		if (this.FileExtension.equals("new")) {
			//System.out.println("this.FileExtension: " + this.FileExtension + " => Selected new...");
			this.isNewFile = true;
		} else {
			this.isNewFile = false;
		}
	}
	
	void setValidProperty() {
		// The File is valid if TODAY is between the StartDate and EndDate
		// OR no StartDate || EndDate exist
		
	    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

	    
	    //If Dates Exist
	    if (!this.StartDate.equals("") || !(this.EndDate.equals(""))) {
	    	//System.out.println("Start Date: " + this.StartDate + " End Date: " + this.EndDate);
		    try {
		    	Date todayDate = new Date();
		        Date ParsedStartDate = df.parse(this.StartDate);
		        Date ParsedEndDate = df.parse(this.EndDate);
		        
		        //if Today is between StartDate and End Date
		        if(todayDate.after(ParsedStartDate) && todayDate.before(ParsedEndDate)) {
		        	this.isValidFile = true;
		        }
		        else 
		        {
		        	this.isValidFile = false;
		        }
		        
			    //System.out.println("Now: " + todayDate.getTime() + " Parsed Start Date:" + ParsedStartDate.getTime() + " Parsed End Date:" + ParsedEndDate.getTime() + " isValidFile: " + isValidFile);
			    
		    } catch (Exception e) {
		        System.out.println("Failed to parse Start or End date of File Header: " + this.StartDate + " " + this.EndDate + e);
		    }
	    }//if
	    else {
	    	this.isValidFile = true;
	    }
	}//setValidProperty


} //class EggedDataFileHeader
