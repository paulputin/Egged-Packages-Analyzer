package tab_ImportPackages;

import java.util.ArrayList;
import java.util.List;

public class EggedVersionFile {

	private String PackageName, PackageDataVer, PacakageDataStructure;
	private List<List<String>> FileNames = new ArrayList<>();
	
	public EggedVersionFile() {

	}
	
	EggedVersionFile (EggedVersionFile evf, String PackageName) {
		this.PackageName = PackageName;
		this.PackageDataVer = evf.getPackageDataVer();
		this.PacakageDataStructure = evf.getPacakageDataStructure();
		this.FileNames = evf.getData();
	}// constructor
	
	//Setters
	public void setPackageName (String PackageName) {
		this.PackageName = PackageName;
	}
	
	public void setPackageDataVer (String PackageDataVer) {
		this.PackageDataVer = PackageDataVer;
	}
	
	public void setPacakageDataStructure (String PacakageDataStructure) {
		this.PacakageDataStructure = PacakageDataStructure;
	}
	
	public void setData(List<List<String>> records) {
		this.FileNames = records;
	}

	
	//Getters
	public String getPackageName () {
		return this.PackageName;
	}
	
	public String getPackageDataVer () {
		return this.PackageDataVer;
	}
	
	public String getPacakageDataStructure () {
		return this.PacakageDataStructure;
	}
	
	public List<List<String>> getData() {
		return this.FileNames;
	}
	// end Setters/Getters

	
} //class EggedVersionFile
