/* 
 * Copyright (C) 2013 Torsten Dietl <Torsten.Dietl@gmx.de>
 *
 * This source code is released under the Microsoft Reference Source License 
 * (MS-RSL).
 *
 * MICROSOFT REFERENCE SOURCE LICENSE (MS-RSL)
 *
 * This license governs use of the accompanying software. 
 * If you use the software, you accept this license. 
 * If you do not accept the license, do not use the software.
 *
 *
 * 1. Definitions
 *
 * The terms "reproduce," "reproduction," and "distribution" have the same 
 * meaning here as under U.S. copyright law.
 * "You" means the licensee of the software.
 * "Your company" means the company you worked for when you downloaded the 
 * software.
 * "Reference use" means use of the software within your company as a reference,
 * in read only form, for the sole purposes of debugging your products, 
 * maintaining your products, or enhancing the interoperability of your products
 * with the software, and specifically excludes the right to distribute the 
 * software outside of your company.
 * "Licensed patents" means any Licensor patent claims which read directly on 
 * the software as distributed by the Licensor under this license.
 * 
 *
 * 2. Grant of Rights
 * 
 * (A) Copyright Grant- Subject to the terms of this license, the Licensor 
 * grants you a non-transferable, non-exclusive, worldwide, royalty-free 
 * copyright license to reproduce the software for reference use.
 * (B) Patent Grant- Subject to the terms of this license, the Licensor grants 
 * you a non-transferable, non-exclusive, worldwide, royalty-free patent license
 * under licensed patents for reference use.
 * 
 *
 * 3. Limitations
 *
 * (A) No Trademark License- This license does not grant you any rights to use 
 * the Licensor’s name, logo, or trademarks.
 * (B) If you begin patent litigation against the Licensor over patents that you
 * think may apply to the software (including a cross-claim or counterclaim in a
 * lawsuit), your license to the software ends automatically.
 * (C) The software is licensed "as-is." You bear the risk of using it. 
 * The Licensor gives no express warranties, guarantees or conditions. You may 
 * have additional consumer rights under your local laws which this license 
 * cannot change. To the extent permitted under your local laws, the Licensor 
 * excludes the implied warranties of merchantability, fitness for a particular 
 * purpose and non-infringement. 
 *
 */

package plugins.excel.client.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.text.DateFormatter;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import shared.util.Debugger;
import shared.util.Dialog;
import shared.util.ClinSysException;
import shared.util.RecordSet;

/**
 * @author torstend
 *
 */
public class ExcelReader {
	
	private ArrayList<String> getColumnNames(Row rowObject, boolean fromFirstLine){
		Cell cell = null;
		ArrayList<String> columnNames = new ArrayList<String>();
		int columnNameNr = 0;
		
		// go through all columns
		for (int i=0; i<rowObject.getLastCellNum(); i++){ 
			// grab the cell
			cell = rowObject.getCell(i);
			
			// if the first line contains the column headers
			if (fromFirstLine){
				// only accept string type 
				switch (cell.getCellType()){
				case(Cell.CELL_TYPE_STRING):
				case(Cell.CELL_TYPE_FORMULA):
					String cellVal = cell.getStringCellValue();
					// for redundant column names add an incrementing number
					if (columnNames.contains(cellVal)){
						columnNameNr++;
						columnNames.add(cellVal + columnNameNr);
					}else{
						columnNames.add(cellVal);
					}
					break;
				// if it's not a string type just create a FieldX name and print a warning
				default: 
					columnNames.add("Field" + columnNameNr);
					Debugger.debug("WARNING: CellType of column #" + (columnNameNr) + " of the first line is no string! " +
							"Only text can be used as column names. Using 'Field" + columnNameNr + "' as column name");
					columnNameNr++;
					break;
				}
			// the first line contains data, just create FieldX to FieldY as name
			}else{
				columnNames.add("Field" + columnNameNr);
				columnNameNr++;
			}						
		}
		
		return columnNames;		
	}
	
	private HashMap<String, Integer> getColumnTypes(Sheet sheet, ArrayList<String> columnNames, boolean columnNamesInFirstLine){
		Cell c = null;
		int type = Types.NULL;
		boolean stopFor = false;
		HashMap<String, Integer> types = new HashMap<String, Integer>();
		
		for (int i=0; i<columnNames.size(); i++){
			type = Types.NULL;
			stopFor = false;
			for(int j=sheet.getFirstRowNum()+(columnNamesInFirstLine?1:0); j<=sheet.getLastRowNum(); j++) {
				c = sheet.getRow(j).getCell(i);
				if (c!=null){
					switch(c.getCellType()){
					case (Cell.CELL_TYPE_STRING):
					case (Cell.CELL_TYPE_FORMULA):
						type = Types.VARCHAR;
						stopFor = true;
						break;
					case (Cell.CELL_TYPE_NUMERIC):
						if (DateUtil.isCellDateFormatted(c)){
							if (type!=Types.VARCHAR && type!=Types.DOUBLE){
								type = Types.DATE;
							}
						}else{
							if (type!=Types.VARCHAR){
								type = Types.DOUBLE;
							}
						}
						break;
					case (Cell.CELL_TYPE_ERROR):
						if (type==Types.NULL || type==Types.BOOLEAN){
							type = Types.INTEGER;
						}
						break;
					case (Cell.CELL_TYPE_BOOLEAN):
						if (type==Types.NULL){
							type = Types.BOOLEAN;
						}
						break;
					}
				}
				
				if (stopFor){
					break;
				}
			}
			
			types.put(columnNames.get(i), type);
		}
		return types;		
	}
	
	private RecordSet createRecordSetFromSheet(Sheet sheet, boolean columnNamesInFirstLine){
		RecordSet rs = new RecordSet();
		ArrayList<String> columnNames = null;
		HashMap<String, Integer> types = null;
		
		if (sheet.getLastRowNum()>0){
			columnNames = this.getColumnNames(sheet.getRow(sheet.getFirstRowNum()), columnNamesInFirstLine);
			types = this.getColumnTypes(sheet, columnNames, columnNamesInFirstLine);
			
			rs.addFields(columnNames, types);
			
			return rs;
		}			
		return null;
	}
	
	private Object getCellValue(Cell c, int targetType){
		int cellType = c.getCellType();
		Object val;
		
		try{
			switch (cellType){
			case (Cell.CELL_TYPE_STRING):
			case (Cell.CELL_TYPE_FORMULA):
				val = c.getStringCellValue();
			
				switch (targetType){
				case Types.BOOLEAN:
					return Boolean.parseBoolean((String)val);
				case Types.DOUBLE:
					return Double.parseDouble((String)val);
				case Types.INTEGER:
					return Integer.parseInt((String)val);
				case Types.VARCHAR:
					return (String)val;
				case Types.DATE:
					SimpleDateFormat sdf = new SimpleDateFormat();
					try {
						return sdf.parse((String) val);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				break;
			case (Cell.CELL_TYPE_NUMERIC):
				if(DateUtil.isCellDateFormatted(c)){
					val = c.getDateCellValue();
					
					switch (targetType){
					case Types.BOOLEAN:
						return (((Date) val).getTime()>0);
					case Types.DOUBLE:
						return (double) ((Date) val).getTime();
					case Types.INTEGER:
						return ((Date) val).getTime();
					case Types.VARCHAR:
						DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
						return df.format((Date)val);
					case Types.DATE:
						return (Date) val;
					}
				}else{				
					val = c.getNumericCellValue();
					
					switch (targetType){
					case Types.BOOLEAN:
						return ((double) val>0.0);
					case Types.DOUBLE:
						return (double) val;
					case Types.INTEGER:
						return (long) val;
					case Types.VARCHAR:
						return new Double((double) val).toString();
					case Types.DATE:
						Date d = new Date();
						d.setTime((long) val);
						return d;
					}
				}
				break;
			case (Cell.CELL_TYPE_ERROR):
				val = c.getErrorCellValue();
				
				switch (targetType){
				case Types.BOOLEAN:
					return ((int) val>0);
				case Types.DOUBLE:
					return (double) val;
				case Types.INTEGER:
					return (int) val;
				case Types.VARCHAR:
					return new Integer((int) val).toString();
				case Types.DATE:
					Date d = new Date();
					d.setTime((long) val);
					return d;
				}
				break;
			case (Cell.CELL_TYPE_BOOLEAN):
				val = c.getBooleanCellValue();
			
				switch (targetType){
				case Types.BOOLEAN:
					return (boolean) val;
				case Types.DOUBLE:
					return (double) (((boolean)val?1:0));
				case Types.INTEGER:
					return (int) (((boolean)val?1:0));
				case Types.VARCHAR:
					return new Boolean((boolean) val).toString();
				case Types.DATE:
					Date d = new Date();					
					d.setTime((long)(((boolean)val?1:0)));
					return d;
				}
				break;
			}
		}catch (IllegalStateException e){
			Dialog.msgBox("Could not import cell r:" + c.getRowIndex() + " c: " + c.getColumnIndex() + " because of data type errors in the sheet", "Import Excel File", Dialog.ERROR_MESSAGE);
		}
		return null;
	}
	
	public RecordSet importExcelFile(File f, boolean columnNamesInFirstLine) throws ClinSysException{
		ArrayList<String> columnNames = null;
		RecordSet rs = new RecordSet();
		HashMap<String, Object> row;
		Row rowObject = null;
		Cell cell = null;
		FileInputStream file = null;
		Sheet sheet = null;
		
		try {
			file = new FileInputStream(f);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}	
		
		if (ExcelFileFilter.getExtension(f).equals("xls")){
			try {
				HSSFWorkbook workbook = new HSSFWorkbook(file);
				sheet = workbook.getSheetAt(0);
					
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {			
				XSSFWorkbook workbook = new XSSFWorkbook(file);				
				sheet = workbook.getSheetAt(0);					
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		
		rs = this.createRecordSetFromSheet(sheet, columnNamesInFirstLine);
		rs.setTableName(f.getName());
		columnNames = rs.getFields();
		
	
		for (int j=sheet.getFirstRowNum()+(columnNamesInFirstLine?1:0); j<=sheet.getLastRowNum(); j++){
			rowObject = sheet.getRow(j);
			row = new HashMap<String, Object>();
			
			for (int i=0; i<columnNames.size(); i++){
				cell = rowObject.getCell(i);
				if (cell!=null){
					row.put(columnNames.get(i), this.getCellValue(cell, rs.getType(columnNames.get(i))));					
				}else{
					row.put(columnNames.get(i), null);
				}
			}
			
			rs.addRow(columnNames, row);
		}			
			
		return rs;
	}
}
