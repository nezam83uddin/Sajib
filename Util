package com.rkreja;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;




public class Util
{
  public Util() {}
  
  public static synchronized void sleep(long ms)
  {
    try
    {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  public static synchronized boolean isNumeric(String s)
  {
    boolean b = false;
    try {
      double d = Double.parseDouble(s);
    } catch (NumberFormatException e) {
      return false;
    }
    
    return true;
  }
  
  public static synchronized int get_string_occurrences(String source, String target) {
    String str = source;
    String findStr = target;
    int lastIndex = 0;
    int count = 0;
    
    while (lastIndex != -1)
    {
      lastIndex = str.indexOf(findStr, lastIndex);
      
      if (lastIndex != -1) {
        count++;
        lastIndex += findStr.length();
      }
    }
    return count;
  }
  
  public static String generate_random_alphanumeric_string(int len) {
    String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    SecureRandom rnd = new SecureRandom();
    
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++)
      sb.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(rnd.nextInt("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length())));
    return sb.toString().toUpperCase();
  }
  
  public static String generate_random_string(int len)
  {
    String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    SecureRandom rnd = new SecureRandom();
    
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++)
      sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(rnd.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length())));
    return sb.toString().toUpperCase();
  }
  
  public static String generate_random_numeric_string(int len)
  {
    String AB = "0123456789";
    SecureRandom rnd = new SecureRandom();
    
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++)
      sb.append("0123456789".charAt(rnd.nextInt("0123456789".length())));
    return sb.toString().toUpperCase();
  }
  


  public static synchronized String getTheNumbersOutOfString(CharSequence input)
  {
    StringBuilder sb = new StringBuilder(input.length());
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if ((c > '/') && (c < ':')) {
        sb.append(c);
      }
    }
    return sb.toString();
  }
  





  public static synchronized String getTodaysDate()
  {
    Calendar cal = Calendar.getInstance();
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    return df.format(cal.getTime());
  }
  






  public static synchronized String getPastDate(int numOfPastDay)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(5, -numOfPastDay);
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    return df.format(cal.getTime());
  }
  








  public static synchronized String get_date_time(String ACConstantsDatePattern)
  {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ACConstantsDatePattern);
    return simpleDateFormat.format(cal.getTime());
  }
  











  public static synchronized String convertDateFormat(String date, String datePattern, String newPattern)
  {
    SimpleDateFormat pat1 = new SimpleDateFormat(datePattern);
    SimpleDateFormat pat2 = new SimpleDateFormat(newPattern);
    
    Date dateObj = null;
    try {
      dateObj = pat1.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    
    return pat2.format(dateObj);
  }
  











  public static synchronized Date parseStringToDate(String date, String newPattern)
  {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(newPattern);
    try {
      cal.setTime(simpleDateFormat.parse(date));
      return cal.getTime();
    } catch (ParseException e) {
      e.printStackTrace(); }
    return null;
  }
  









  public static synchronized String getFutureDate(int numOfFutureDay)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(5, numOfFutureDay);
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    return df.format(cal.getTime());
  }
  
  public static synchronized String removeparenthesesWhiteSpaceAndDash(String s)
  {
    return s.replace(" ", "").replace("-", "").replace("(", "").replace(")", "").trim();
  }
}
