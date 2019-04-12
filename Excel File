package com.rkreja;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Excel
  implements TableOperation
{
  private String excelFilePath;
  private String sheetname;
  private FileInputStream inputStream;
  private Workbook workbook;
  private Sheet sheet;
  private Iterator<Row> iterator;
  private int header = 0;
  






  public Excel(String excelfile, String sheetname)
  {
    this(excelfile, sheetname, true);
  }
  





  public Excel(String excelfile, String sheetname, boolean header)
  {
    try
    {
      excelFilePath = excelfile;
      this.sheetname = sheetname;
      inputStream = new FileInputStream(new File(excelFilePath));
      
      if (excelfile.endsWith(".xls")) {
        workbook = new HSSFWorkbook(inputStream);
      } else if (excelfile.endsWith(".xlsx")) {
        workbook = new XSSFWorkbook(inputStream);
      }
      sheet = workbook.getSheet(sheetname);
      iterator = sheet.iterator();
      if ((header == true) && (this.header == 0)) {
        this.header += 1;
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("There is an exception. Possible reasons: [invalid sheet name]. Please contact AutoBots");
    }
  }
  
  public void writeInCellAsString(int row, int col, String txt)
  {
    Cell cell = getRow(row).createCell(col);
    cell.setCellType(CellType.STRING);
    cell.setCellValue(txt);
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(excelFilePath);
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }
    try {
      workbook.write(fos);
      fos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    closeWorkbook();
  }
  



  public Object[][] getRowValuesForDataProvider()
  {
    int totalRows = rowCount();
    int totalColumns = columnCount();
    Object[][] obj = new Object[totalRows][totalColumns];
    for (int c = 0; c < totalColumns; c++) {
      for (int r = 0; r < totalRows; r++) {
        obj[r][c] = getCellValueAsString(r + 1, c + 1);
      }
    }
    return obj;
  }
  
  public String getCellValue(int row, int col)
  {
    return getCellValueAsString(row, col);
  }
  

























  private String getCellValueAsString(Cell cell)
  {
    DataFormatter format = new DataFormatter();
    String s = format.formatCellValue(cell);
    closeWorkbook();
    return s;
  }
  








  private Cell getCell(int row, int col)
  {
    return getRow(row).getCell(getCol(col));
  }
  
  private Row getRow(int row) throws IllegalArgumentException
  {
    if (row != 0) {
      row -= 1;
    } else {
      throw new IllegalArgumentException("Row should be starts with 1. Found 0");
    }
    return sheet.getRow(row + header);
  }
  
  private int getCol(int col)
  {
    if (col != 0) {
      col -= 1;
    } else {
      throw new IllegalArgumentException("Column should be starts with 1. Found 0");
    }
    return col;
  }
  

  public List<String> getAllRowValuesByColumn(int col)
  {
    List<String> res = new ArrayList();
    for (int i = 1; i <= rowCount(); i++) {
      res.add(getCellValueAsString(i, col));
    }
    return res;
  }
  

  public String getCellValueAsString(int row, int col)
  {
    Cell cell = getCell(row, col);
    return getCellValueAsString(cell);
  }
  
  public String getCellValueAsString(int row, String column) {
    return getCellValueAsString(row, getColumnIndex(column).intValue());
  }
  
  public List<Map<String, String>> getColumnAndRowsAsMap() {
    List<Map<String, String>> ret = new ArrayList();
    

    for (int row = 1; row <= rowCount(); row++) {
      Map<String, String> colrows = new HashMap();
      for (int col = 0; col < getColHeaders().size(); col++)
      {

        colrows.put(getColHeaders().get(col), getCellValueAsString(row, (String)getColHeaders().get(col)));
      }
      

      ret.add(colrows);
    }
    

    return ret;
  }
  


  public boolean checkValueIsInCell(int row, int col, String dataToFind)
  {
    return getCellValueAsString(row, col).equals(dataToFind);
  }
  
  public boolean checkValueContainsInCell(int row, int col, String dataToFind)
  {
    return getCellValueAsString(row, col).contains(dataToFind);
  }
  

  public List<String> getAllColumnValueByRow(int row)
  {
    List<String> valuesI_in_rows = new ArrayList();
    List<String> headers = getColHeaders();
    for (String s : headers) {
      valuesI_in_rows.add(getCellValueAsString(row, s));
    }
    
    return valuesI_in_rows;
  }
  
  public String getValue(int rowNum, String colHeader)
  {
    return getCellValueAsString(rowNum, colHeader);
  }
  







  public int getRowByData(String colHeader, String dataToFind)
  {
    return getAllRowByData(colHeader, dataToFind)[0].intValue();
  }
  
  public Integer[] getAllRowByData(String colHeader, String dataToFind) {
    int colIndex = getColumnIndex(colHeader).intValue();
    List<Integer> res = new ArrayList();
    
    for (int i = 1; i <= rowCount(); i++) {
      String val = getCellValueAsString(i, colIndex);
      if (val.equalsIgnoreCase(dataToFind)) {
        res.add(Integer.valueOf(i));
      }
    }
    if (res.isEmpty())
      try {
        throw new Exception("Given data: [" + dataToFind + "] in column [" + colHeader + "] is not exist.");
      } catch (Exception e) {
        e.printStackTrace();
      }
    return (Integer[])res.toArray(new Integer[0]);
  }
  




  public Integer getColumnIndex(String columnName)
  {
    Row row = sheet.getRow(0);
    Iterator<Cell> cell = row.cellIterator();
    int counter = 0;
    boolean found = false;
    while (cell.hasNext()) {
      Cell c = (Cell)cell.next();
      if (getCellValueAsString(c).equalsIgnoreCase(columnName)) {
        found = true;
        break;
      }
      counter++;
    }
    
    closeWorkbook();
    if (found) {
      return Integer.valueOf(counter + 1);
    }
    return null;
  }
  
  public String getColumnNameByIndex(int col)
  {
    if (col < 0) {
      col -= 1;
    } else {
      throw new IllegalArgumentException("Column should be starts with 1. Found 0");
    }
    return (String)getColHeaders().get(col) + 1;
  }
  







  public List<String> getColHeaders()
  {
    List<String> cols = new ArrayList();
    Row row = sheet.getRow(0);
    Iterator<Cell> cell = row.cellIterator();
    while (cell.hasNext()) {
      Cell c = (Cell)cell.next();
      cols.add(getCellValueAsString(c));
    }
    closeWorkbook();
    return cols;
  }
  



  public int columnCount()
  {
    int res = 0;
    Iterator<Cell> cells = sheet.getRow(0).cellIterator();
    while (cells.hasNext()) {
      cells.next();
      res++;
    }
    
    closeWorkbook();
    return res;
  }
  

  public int rowCount()
  {
    int res = 0;
    res = sheet.getLastRowNum();
    closeWorkbook();
    return res;
  }
  


  public void closeWorkbook()
  {
    try
    {
      inputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
