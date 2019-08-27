package tab_ImportPackages;

import java.util.ArrayList;
import java.util.List;

public class EggedDataFile {
	

	private EggedDataFileHeader edfh; 
	private List<List<String>> Data = new ArrayList<>();
	
	EggedDataFile () {
		
	} //constructor

	EggedDataFile (EggedDataFileHeader edfh) {
		this.edfh = edfh;
	} //constructor

	//Setters
	
	
	
	public void setFileHeader(EggedDataFileHeader edfh) {
		this.edfh = edfh;
	}
	
	public void setData(List<List<String>> records) {
		this.Data = records;
	}
	
	
	//Getters
	public boolean getIsExportToExcel() {
		return this.getFileHeader().getIsExportToExcel();
	}
	
	public EggedDataFileHeader getFileHeader() {
		return this.edfh;
	}

	public List<List<String>> getData() {
		return this.Data;
	}
	
	public String getPackageName() {
		return this.getFileHeader().getPackageName();
	}
	
	public String getFileNameShort() {
		return this.getFileHeader().getFileNameShort();
	}
	
	public String getFileExtension() {
		return this.getFileHeader().getFileExtension();
	}

	// end Setters/Getters
	
	
} //class EggedDataFile
