package tab_ImportPackages;

import java.util.ArrayList;
import java.util.List;

public class EggedData {
	
	private List<EggedVersionFile> EggedVersionFileList = new ArrayList<EggedVersionFile>();
	private List<EggedDataFile> EggedDataFileList = new ArrayList<EggedDataFile>();
	
	public void ImportNewVersionFile (EggedVersionFile evf) {
		
		EggedVersionFileList.add(evf);
		
	} //ImportVersionFiles
	
	public void ImportNewDataFile (EggedDataFile edf ) {
		
		EggedDataFileList.add(edf);
		
	} //ImportDataFiles
	
	//Getters
	
	public List<EggedVersionFile> getEggedVersionFileList() {
		return this.EggedVersionFileList;
	}
	
	public List<EggedDataFile> getEggedDataFileList() {
		return this.EggedDataFileList;
	}
	
	public void ClearData() {
		EggedVersionFileList.clear();
		EggedDataFileList.clear();
	} //ClearData

}
