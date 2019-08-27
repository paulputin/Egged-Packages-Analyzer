package tab_ImportPackages;





import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class Unziper {
	

	Unziper() {
	}

	public boolean UnzipFileIntoFolder(File FileToUnzip, String TargetFolder) {
		
		if (FileToUnzip != null) {
			new File(TargetFolder).mkdirs();
			try {
				Unzip(FileToUnzip.toString(), TargetFolder);
			} catch (IOException e) {
				System.out.println("Unziper class: Unzipping file Exception: \nFolder: " + TargetFolder + "\nFilee: " + FileToUnzip.toString() + "\n");
				//e.printStackTrace();
			}
			//System.out.println("Unzipper class: Unzipping file: " + FileToUnzip.toString());
			return true;
		}
		else {
			System.out.println("Unzipper class:  file that was atempted to unzip is NULL, cannot proceed...");
			return false;
		}
 	} //UnzipFileIntoFolder
	
	
    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
         
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
         
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("PackagesUnziper class: Entry is outside of the target dir: " + zipEntry.getName());
        }
         
        return destFile;
    }
	
    public static void Unzip (String strZipFile, String strDestDir) throws IOException {
	    String fileZip = strZipFile;
	    File destDir = new File(strDestDir);
	    byte[] buffer = new byte[1024];
	    ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
	    ZipEntry zipEntry = zis.getNextEntry();
	    while (zipEntry != null) {
	        File newFile = newFile(destDir, zipEntry);
	        FileOutputStream fos = new FileOutputStream(newFile);
	        int len;
	        while ((len = zis.read(buffer)) > 0) {
	            fos.write(buffer, 0, len);
	        }
	        fos.close();
	        zipEntry = zis.getNextEntry();
	    }
	    zis.closeEntry();
	    zis.close();
    } //Unzip
    
} //class
