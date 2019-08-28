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
	
	
	// Reads Version.txt files
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

	
	//Reads headers of Egged Validator Data Files
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

	
	//Reads Data from EggedDataFile
	public List<List<String>> ReadEggedDataFile(File f, boolean isFileHasFooter) {
		List<List<String>> records = new ArrayList<>();

		try (
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(f.toString())), "UTF-16LE"))
			)
		{
		    String line;
		    int LineNumber = 1;
		    while ((line = br.readLine()) != null) {
		    	if (LineNumber > 2) {
		    		String[] values = line.split(",",-1);
		    		records.add(Arrays.asList(values));
		    	}
    			
    			LineNumber++;
		    } // while ((line = br.readLine()) != null)
		} //try
		
		
    	catch (UnsupportedEncodingException e) 
    	{
    		System.out.println(e.getMessage());
    	} 
		catch (FileNotFoundException e) {
			System.out.println("EggedPackageFileReader Class: Method ReadEggedDataFile. FileNotFoundException: file is " + f.toString() + "Exception is " + e.toString());
		} catch (IOException e) {
			System.out.println("EggedPackageFileReader Class: Method: ReadEggedDataFile. IOException: file is " + f.toString()+ "Exception is " + e.toString());
		}

		
		//Take Care Of the Footer of the Data file
		// if the last line not equals in size to the first line => it is probably junk => Delete last Line. Repeat.
	    if (isFileHasFooter)
	    {
	    	boolean isProbablyTheLastJunkLine = false;
	    
	    	while (!isProbablyTheLastJunkLine) {
	    		if (records.size() > 0)
	    		{
	    		if (records.get(records.size()-1).size() != records.get(0).size())
	    			{
	    			records.remove(records.size() - 1);
	    			}
	    			else {
	    					isProbablyTheLastJunkLine = true;
	    				}
	    		}
	    		else {
	    				isProbablyTheLastJunkLine = true;
	    			}
	    	}//while isProbablyNotLastJunkLine 
	    }  //Take Care Of the Footer of the Data file
	    
	    
		return records;
	} //ReadEggedDataFile()
	
}
