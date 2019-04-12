package com.rkreja;

import java.util.List;

public abstract interface TableOperation
{
  public abstract int columnCount();
  
  public abstract int rowCount();
  
  public abstract Integer[] getAllRowByData(String paramString1, String paramString2);
  
  public abstract int getRowByData(String paramString1, String paramString2);
  
  public abstract String getCellValue(int paramInt1, int paramInt2);
  
  public abstract String getCellValueAsString(int paramInt1, int paramInt2);
  
  public abstract String getCellValueAsString(int paramInt, String paramString);
  
  public abstract Integer getColumnIndex(String paramString);
  
  public abstract String getColumnNameByIndex(int paramInt);
  
  public abstract List<String> getAllColumnValueByRow(int paramInt);
  
  public abstract List<String> getAllRowValuesByColumn(int paramInt);
  
  public abstract List<String> getColHeaders();
  
  public abstract boolean checkValueIsInCell(int paramInt1, int paramInt2, String paramString);
  
  public abstract boolean checkValueContainsInCell(int paramInt1, int paramInt2, String paramString);
}
