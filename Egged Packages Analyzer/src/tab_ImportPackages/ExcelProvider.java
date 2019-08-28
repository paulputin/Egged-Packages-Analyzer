package tab_ImportPackages;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelProvider extends Thread{
	
	private static final int WIDTH_ARROW_BUTTON = 340;

	int PercentOfAllThreads;
	String ExcelFolder;
    Workbook workbook = new XSSFWorkbook();
    private List<EggedDataFile> EggedDataFileList = new ArrayList<EggedDataFile>();
    
    JLabel lblProgressLabel;
    JProgressBar progressBar;
    
    //constructor
    ExcelProvider(List<EggedDataFile> EggedDataFileList, String ExcelFolder, int PercentOfAllThreads) {
    	setData(EggedDataFileList);
    	setExcelFolder(ExcelFolder);
    	setPercentOfAllThreads(PercentOfAllThreads);
    } //constructor
    
    
    public void setData(List<EggedDataFile> EggedDataFileList) {
    	this.EggedDataFileList = EggedDataFileList;
    }
    
    public void setExcelFolder(String ExcelFolder) {
    	this.ExcelFolder = ExcelFolder;
    }
    
    public void setPercentOfAllThreads(int PercentOfAllThreads) {
    	this.PercentOfAllThreads = PercentOfAllThreads;
    }
    
    public void setlblProgressLabel(JLabel lblProgressLabel) {
    	this.lblProgressLabel = lblProgressLabel;
    }
    
    public void setProgressBar(JProgressBar progressBar) {
    	this.progressBar = progressBar;
    	
    }
    
	@Override
	public void run() {

		//count MaxValue for Progress bar: Sheets + Columns + Write File
		int ProgressBarMaxValue = 0;
		for (int i = 0; i < EggedDataFileList.size(); i++) {
			if (EggedDataFileList.get(i).getIsExportToExcel() == true) {
				//Counting Sheets 
				ProgressBarMaxValue++;
				for (int j = 0; j < EggedDataFileList.get(i).getData().get(0).size(); j++) {
					//Counting Columns
					ProgressBarMaxValue++;
				}
			}
		}
		//Counting Excel File Save
		ProgressBarMaxValue++;
		
		this.progressBar.setMinimum(0);
		this.progressBar.setMaximum(ProgressBarMaxValue);
		this.progressBar.setValue(0);
		


		//iterate through all DataFiles and Export them to Worksheets = 5% Time Consuming

		for (int i = 0; i < EggedDataFileList.size(); i++) {
			if (EggedDataFileList.get(i).getIsExportToExcel() == true) {
				ImportSheet(EggedDataFileList.get(i).getFileNameFull(), EggedDataFileList.get(i).getData());
				
				//System.out.println("Class: ExcelProvider, Method: run, Dealing with: " + EggedDataFileList.get(i).getFileNameFull());
				lblProgressLabel.setText("Creaing Excel: Creating Sheet " + EggedDataFileList.get(i).getFileNameFull());
				progressBar.setValue(progressBar.getValue()+1);
				
			}
		} // for
		//progressBar.setValue(5);
		
		
		//autosizing columns width = 95% Time Consuming
		autoSizeColumns(workbook);
		//progressBar.setValue(99);
		
		
		//Creating Excel file = 1% Time Consuming
		if (WriteExcelFile (ExcelFolder) == true) {
			//System.out.println("Class: ExcelProvider, Method: run, WriteExcelFile successfull.");
			lblProgressLabel.setText("Creaing Excel: Saved Excel File successfully.");
		} else {
			//System.out.println("Class: ExcelProvider, Method: run, WriteExcelFile failed.");
			lblProgressLabel.setText("Creaing Excel: Saving Excel File failed.");
		}
		progressBar.setValue(progressBar.getValue()+1);
		//progressBar.setValue(100);
		
		
	} //run
    
    
    
	public void ImportSheet (String SheetName, List<List<String>> records) {
		
		//System.out.println("ExcelImporter: ImportSheet: " + SheetName);
		//lblProgressLabel.setText("ExcelImporter: ImportSheet: " + SheetName);
		
		XSSFSheet sheet = (XSSFSheet) workbook.createSheet(SheetName);
        Map<String, ArrayList<Object>> data = new LinkedHashMap<String, ArrayList<Object>>();
		
		//Iterate through records
		for (Integer i = 0; i < records.size(); i++) {
			ArrayList<Object> arrList = new ArrayList<Object>();
			for (int j = 0; j < records.get(i).size(); j++) {
				arrList.add(records.get(i).get(j));
			}
			data.put(i.toString(), arrList);
		} //Iterate through records
		

		XSSFCellStyle style=(XSSFCellStyle) workbook.createCellStyle();
		style.setDataFormat(workbook.createDataFormat().getFormat("0"));

		
        Set<String> keyset = data.keySet();
        int rownum = 0;

        for (String key : keyset) {
            Row row = sheet.createRow(rownum++);
            ArrayList<Object> ListArr2 = data.get(key);
            int cellnum = 0;

            for (Object obj : ListArr2) {
            	Cell cell = row.createCell(cellnum++);
            	try {
            			Integer i = Integer.parseInt((String) obj);
            			cell.setCellValue(i);
            	}
            	catch (Exception e) {
            		try {
            			Double d = Double.parseDouble((String) obj);
            			cell.setCellValue(d);
            			//if StartDate or EndDate cell
            			if ((d > 200000000000.0) && (d < 290000000000.0)) {
            				cell.setCellStyle(style);
            			}
            		}
            		catch (Exception e1) {
	                    if (obj instanceof Integer) {
	                        cell.setCellValue((Integer) obj);
	                    } else if (obj instanceof Boolean) {
	                        cell.setCellValue((Boolean) obj);
	                    } else if (obj instanceof String) {
	                        cell.setCellValue((String) obj);
	                    } else if (obj instanceof Double) {
	                        cell.setCellValue((Double) obj);
	                    } else if (obj instanceof Date) {
	                        cell.setCellValue((Date) obj);
	                    }
            		} //catch 2
        		} //catch 1
            } //for
        } // for (String key : keyset) 
        

        // Add auto filter and freeze panes to the first row 
        if (sheet.getPhysicalNumberOfRows() > 0) {
        	int numColumns = sheet.getRow(0).getPhysicalNumberOfCells();
        	sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, numColumns-1));
        	sheet.createFreezePane(0, 1);
        }

	} // ImportSheet
	
	public boolean WriteExcelFile(String ExcelFolder) {
		
        try {
            FileOutputStream out
                    = new FileOutputStream(new File(ExcelFolder + "\\ValidatorPackages.xlsx"));
            workbook.write(out);
            out.close();
            
            //System.out.println("Excel written successfully..");
            //lblProgressLabel.setText("Creaing Excel: Excel written successfully..");
            
        } catch (FileNotFoundException e) {
        	return false;
            //e.printStackTrace();
        } catch (IOException e) {
        	return false;
            //e.printStackTrace();
        }
        
        try {
			workbook.close();
		} catch (IOException e) {
			return false;
			//e.printStackTrace();
		}
        return true;
	} // WriteExcelFile()
	
	
	private void autoSizeColumns(Workbook workbook) {
	    int numberOfSheets = workbook.getNumberOfSheets();
	    for (int i = 0; i < numberOfSheets; i++) {
	    	XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(i);
	    	
	    	//System.out.println ("ExcelImporter: autoSizeColumns: Working on Sheet [" + sheet.getSheetName() + "]");
	    	//lblProgressLabel.setText("Creaing Excel: Autosizing Columns: Working on Sheet [" + sheet.getSheetName() + "]");
	    	
	        if (sheet.getPhysicalNumberOfRows() > 0) {
	            Row row = sheet.getRow(sheet.getFirstRowNum());
	            Iterator<Cell> cellIterator = row.cellIterator();
	            while (cellIterator.hasNext()) {
	                Cell cell = cellIterator.next();
	                int columnIndex = cell.getColumnIndex();
	                
	                DataFormatter formatter = new DataFormatter();
	                String val = formatter.formatCellValue(sheet.getRow(row.getRowNum()).getCell(columnIndex));
	                
	                //System.out.println ("ExcelImporter: autoSizeColumns: Working on Sheet [" + sheet.getSheetName() 
	                //					+ "], Column [" + val + "]");
					progressBar.setValue(progressBar.getValue()+1);
	                lblProgressLabel.setText("Creaing Excel: Autosizing Columns: Sheet " + sheet.getSheetName() 
					+ ", Column " + val);
	                
	                sheet.autoSizeColumn(columnIndex);
	                sheet.setColumnWidth(columnIndex, sheet.getColumnWidth(columnIndex) + WIDTH_ARROW_BUTTON);
	            }
	        }
	    }
	} //autoSizeColumns



	
} //Class


//	TODO
// 	Show progress in the main windows: Labels or progress bar or both.

