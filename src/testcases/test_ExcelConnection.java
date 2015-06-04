package testcases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import plugins.excel.client.ExcelController;

public class test_ExcelConnection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		try {
//			FileInputStream file = new FileInputStream(new File("test.xlsx"));
//			
//			XSSFWorkbook workbook = new XSSFWorkbook(file);
//			
//			XSSFSheet sheet = workbook.getSheetAt(0);
//			Iterator<Row> rowIterator = sheet.iterator();
//			Iterator<Cell> cellIterator = null;
//			Cell cell = null;
//			
//			while (rowIterator.hasNext()){
//				cellIterator = rowIterator.next().cellIterator();
//				while (cellIterator.hasNext()){
//					cell = cellIterator.next();
//					switch (cell.getCellType()){
//					case(Cell.CELL_TYPE_NUMERIC):
//						System.out.println(cell.getNumericCellValue());
//						break;
//					case(Cell.CELL_TYPE_STRING):
//						System.out.println(cell.getStringCellValue());	
//						break;
//					}
//				}
//			}
//			
//			file.close();
//			
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		ExcelController ec = new ExcelController();
		ec.importExcelFile();
	}	
}
