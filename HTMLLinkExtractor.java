package com.rkreja;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class HTMLLinkExtractor
{
  private Pattern patternTag;
  private Pattern patternLink;
  private Matcher matcherTag;
  private Matcher matcherLink;
  private static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
  private static final String HTML_A_HREF_TAG_PATTERN = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
  
  HTMLLinkExtractor()
  {
    patternTag = Pattern.compile("(?i)<a([^>]+)>(.+?)</a>");
    patternLink = Pattern.compile("\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))");
  }
  







  public Vector<HtmlLink> grabHTMLLinks(String html)
  {
    Vector<HtmlLink> result = new Vector();
    
    matcherTag = patternTag.matcher(html);
    
    while (matcherTag.find())
    {
      String href = matcherTag.group(1);
      String linkText = matcherTag.group(2);
      
      matcherLink = patternLink.matcher(href);
      
      while (matcherLink.find())
      {
        String link = matcherLink.group(1);
        HtmlLink obj = new HtmlLink();
        obj.setLink(link);
        obj.setLinkText(linkText);
        
        result.add(obj);
      }
    }
    


    return result;
  }
  

  class HtmlLink
  {
    String link;
    
    String linkText;
    
    HtmlLink() {}
    
    public String toString()
    {
      return "Link : " + link + " Link Text : " + linkText;
    }
    
    public String getLink() {
      return link;
    }
    
    public void setLink(String link) {
      this.link = replaceInvalidChar(link);
    }
    
    public String getLinkText() {
      return linkText;
    }
    
    public void setLinkText(String linkText) {
      this.linkText = linkText;
    }
    
    private String replaceInvalidChar(String link) {
      link = link.replaceAll("'", "");
      link = link.replaceAll("\"", "");
      return link;
    }
  }
}
