package tab_ImportPackages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EggedPackageFileReader {
	
	
	
	public EggedVersionFile ReadVersionFile (File f) {
		
		EggedVersionFile evf = new EggedVersionFile();
		List<List<String>> records = new ArrayList<>();
		
		try (
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(f.toString())), "UTF-16LE"))
			)
		{
		    String line;
		    int LineNumber = 1;
		    while ((line = br.readLine()) != null) {
    			String[] values = line.split(",",-1);
    			
    			switch (LineNumber) {
    			case 1:
    				break;
    			case 2:
    				evf.setPackageDataVer(values[1]);
    				break;
    			case 3:
    				evf.setPacakageDataStructure(values[1]);
    				break;
    				default:
    					//System.out.println("values: " + values[0]);
    					records.add(Arrays.asList(values));
    			break;
		    	}//switch LineNumber
		    
    			LineNumber++;
		    } // while ((line = br.readLine()) != null)
		    
		    evf.setData(records);
		} //try
		
    	catch (UnsupportedEncodingException e) 
    	{
    		System.out.println(e.getMessage());
    	} 
		catch (FileNotFoundException e) {
			System.out.println("EggedPackageFileReader Class: Method: ReadVersionFile. FileNotFoundException: " + f.toString());
		} catch (IOException e) {
			System.out.println("EggedPackageFileReader Class: Method: ReadVersionFile. IOException: " + f.toString());
		}
		
		return evf;
		
	} // ReadVersionFile

	
	public EggedDataFileHeader ReadEggedDataFileHeader(File f) {
		
		EggedDataFileHeader edfh = new EggedDataFileHeader();

		try (
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(f.toString())), "UTF-16LE"))
			)
		{
		    String line;
		    int LineNumber = 1;
		    while ((line = br.readLine()) != null) {
		    	if (LineNumber > 2) {
		    		//System.out.println("EggedDataFileHeader: File: " + f.toString() + ", LineNumer = " + LineNumber + ". Exiting while loop...");
		    		//Read only 2 lines of the file and exit WHILE loop
		    		break;
		    	}
    			String[] values = line.split(",",-1);
    			
    			switch (LineNumber) {
    			case 1:
    				break;
    			case 2:
    				if (values.length >= 1) {
    					edfh.setDataVer(values[0]);
    				}
    				if (values.length >= 2) {
    					edfh.setStartDate(values[1]);
    				}
    				if (values.length >= 3) {
    					edfh.setEndDate(values[2]);
    				}
    				if (values.length >= 4) {
    					edfh.setKeyVersion(values[3]);
    				}
    				if (values.length >= 5) {
    					edfh.setSignatureFlag(values[4]);
    				}
    				break;
    				default:
    			break;
		    	}//switch LineNumber
		    
    			
    			LineNumber++;
		    } // while ((line = br.readLine()) != null)
		} //try
		
		
    	catch (UnsupportedEncodingException e) 
    	{
    		System.out.println(e.getMessage());
    	} 
		catch (FileNotFoundException e) {
			System.out.println("EggedPackageFileReader Class: Method ReadEggedDataFileHeader. FileNotFoundException: " + f.toString());
		} catch (IOException e) {
			System.out.println("EggedPackageFileReader Class: Method: ReadEggedDataFileHeader. IOException: " + f.toString());
		}

		
		return edfh;
	}//ReadEggedDataFileHeader
	
}
