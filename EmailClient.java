package com.rkreja;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPStore;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import javax.mail.search.BodyTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.RecipientStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;
import javax.mail.search.SubjectTerm;



public class EmailClient
{
  private IMAPStore store;
  private IMAPFolder folder = null;
  

  private Session session;
  

  private final String userId;
  

  private final String userEmailAddress;
  

  private final String userEmailPassword;
  


  public EmailClient(String emailAddress, String uid, String password, String incomingServer, String outgoingServer, int outgoingPort)
  {
    userId = uid;
    userEmailAddress = emailAddress;
    userEmailPassword = password;
    setSession(incomingServer, outgoingServer, outgoingPort);
  }
  

  private void setSession(String incomingServer, String outgoingServer, int outgoingPort)
  {
    String smtpPort = Integer.toString(outgoingPort);
    
    Properties properties = new Properties();
    
    properties.put("mail.smtp.host", outgoingServer);
    properties.put("mail.smtp.socketFactory.port", smtpPort);
    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.port", smtpPort);
    

    properties.put("mail.store.protocol", "imaps");
    
    Session session = Session.getInstance(properties, new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userId, userEmailPassword);
      }
    });
    
    try
    {
      store = ((IMAPStore)session.getStore("imaps"));
    } catch (NoSuchProviderException e) {
      e.printStackTrace();
    }
    try {
      store.connect(incomingServer, userId, userEmailPassword);
    }
    catch (MessagingException e) {
      e.printStackTrace();
    }
    
    this.session = session;
  }
  
  public void ComposeEmail(String toEmailAddress, String Subject, String messageBody)
  {
    try {
      MimeMessage message = new MimeMessage(session);
      
      new InternetAddress();
      message.setFrom(new InternetAddress(userEmailAddress));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddress));
      message.setHeader("Subject", Subject);
      message.setText(messageBody);
      
      Transport.send(message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  



  public void deleteAllEmailFromInbox()
  {
    Message[] msgs = searchEmail("Inbox", 2, new ReceivedDateTerm(1, 
      Calendar.getInstance().getTime()));
    String log = null;
    for (Message m : msgs) {
      try {
        m.setFlag(Flags.Flag.DELETED, true);
      } catch (MessagingException e) {
        e.printStackTrace();
      }
    }
    
    log = "Total email deleted " + msgs.length;
    System.err.println("***\n" + log + "\n***");
  }
  



  public void deleteAllEmailThatReceivedToday()
  {
    Date date = Calendar.getInstance().getTime();
    String formattedDate = new SimpleDateFormat("MM/dd/yyyy").format(date);
    deleteEmails(formattedDate);
  }
  








  public void deleteEmails(String dateReceivedOrSent)
  {
    List<Message> msgs = searchEmailByDateReceivedOrSent("Inbox", 2, dateReceivedOrSent, null);
    String log = null;
    for (Message m : msgs) {
      try {
        m.setFlag(Flags.Flag.DELETED, true);
      } catch (MessagingException e) {
        e.printStackTrace();
      }
    }
    
    log = "Total email deleted " + msgs.size();
    System.err.println("***\n" + log + "\n***");
  }
  










  public void deleteEmails(String dateReceivedOrSent, String subject)
  {
    int totalEmailDeleted = 0;
    List<Message> msgs = searchEmailByDateReceivedOrSent("Inbox", 2, dateReceivedOrSent, null);
    for (Message m : msgs) {
      try {
        if (m.getSubject().contains(subject)) {
          m.setFlag(Flags.Flag.DELETED, true);
          totalEmailDeleted++;
        }
      } catch (MessagingException e) {
        e.printStackTrace();
      }
    }
    
    String log = "Total email deleted " + totalEmailDeleted;
    System.err.println("***\n" + log + "\n***");
  }
  
  private IMAPStore getStore()
  {
    return store;
  }
  
  private void mark_email_read(Message msg) {
    try {
      msg.setFlag(Flags.Flag.SEEN, true);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
  
  private IMAPFolder getFolder(String folderName, int openStatus) {
    store = getStore();
    try {
      folder = ((IMAPFolder)store.getFolder(folderName));
      folder.open(openStatus);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    
    return folder;
  }
  






  public String get_most_recent_email()
  {
    return get_most_recent_email(userId);
  }
  
  private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
    String result = "";
    int count = mimeMultipart.getCount();
    for (int i = 0; i < count; i++) {
      BodyPart bodyPart = mimeMultipart.getBodyPart(i);
      if (bodyPart.isMimeType("text/plain")) {
        result = result + "\n" + bodyPart.getContent();
        break; }
      if (bodyPart.isMimeType("text/html")) {
        result = (String)bodyPart.getContent();
      } else if ((bodyPart.getContent() instanceof MimeMultipart)) {
        result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
      }
    }
    return result;
  }
  











  public void downloadMailAttachments(String downloadFilePath, String textContainsInSubject, String textContainsInBody, Date timebeforemailreceived)
    throws MessagingException, IOException
  {
    Folder inbox = getFolder("INBOX", 2);
    ReceivedDateTerm rd = new ReceivedDateTerm(3, Calendar.getInstance().getTime());
    SubjectTerm st = new SubjectTerm(textContainsInSubject);
    BodyTerm bt = new BodyTerm(textContainsInBody);
    SearchTerm s;
    SearchTerm s;
    if (textContainsInSubject != "") {
      s = new AndTerm(new SearchTerm[] { rd, st, bt });
    } else {
      s = new AndTerm(new SearchTerm[] { rd, bt });
    }
    
    Message[] m = null;
    try {
      m = inbox.search(s);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    
    if (m.length != 0) {
      for (int i = 0; i < m.length; i++) {
        Message message = m[i];
        String contentType = message.getContentType();
        
        String messageContent = "";
        
        String attachFiles = "";
        Date receivedDateTime = message.getReceivedDate();
        if ((receivedDateTime.after(timebeforemailreceived)) && 
          (contentType.contains("multipart"))) {
          Multipart multiPart = (Multipart)message.getContent();
          int numberOfParts = multiPart.getCount();
          for (int partCount = 0; partCount < numberOfParts; partCount++) {
            MimeBodyPart part = (MimeBodyPart)multiPart.getBodyPart(partCount);
            if ("attachment".equalsIgnoreCase(part.getDisposition()))
            {
              String fileName = part.getFileName();
              attachFiles = attachFiles + fileName + ", ";
              part.saveFile(downloadFilePath + fileName);
              mark_email_read(message);
            }
            else {
              messageContent = part.getContent().toString();
              mark_email_read(message);
            }
          }
        }
      }
    }
  }
  









  public String get_most_recent_email(String recipientEmailAddress)
  {
    int num = -1;
    IMAPFolder inbox = getFolder("INBOX", 2);
    RecipientStringTerm rs = new RecipientStringTerm(MimeMessage.RecipientType.TO, recipientEmailAddress);
    
    SearchTerm s = new AndTerm(new SearchTerm[] { rs });
    
    String contentString = null;
    IMAPMessage[] m = null;
    try {
      m = (IMAPMessage[])inbox.search(s);
      
      if (m.length > 0) {
        num = m.length - 1;
        
        try
        {
          if ((m[num].getContent() instanceof String)) {
            contentString = (String)m[num].getContent();
            mark_email_read(m[num]);
          } else if ((m[num].getContent() instanceof Multipart))
          {
            contentString = getTextFromMimeMultipart((MimeMultipart)m[num].getContent());
            mark_email_read(m[num]);
          }
        }
        catch (MessagingException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        
      }
      
    }
    catch (MessagingException e1)
    {
      e1.printStackTrace();
    }
    
    return contentString;
  }
  




  public String[] get_email()
  {
    return get_email(userId);
  }
  



















  public String[] get_email(String recipientEmailAddress)
  {
    ArrayList<String> list = new ArrayList();
    
    IMAPFolder inbox = getFolder("INBOX", 2);
    RecipientStringTerm rs = new RecipientStringTerm(MimeMessage.RecipientType.TO, recipientEmailAddress);
    


    SearchTerm s = new AndTerm(new SearchTerm[] { rs });
    

    String contentString = null;
    try
    {
      Message[] m = inbox.search(s);
      for (int i = 0; i < m.length; i++) {
        try
        {
          if ((m[i].getContent() instanceof String)) {
            contentString = (String)m[i].getContent();
            mark_email_read(m[i]);
          }
          else if ((m[i].getContent() instanceof Multipart)) {
            Multipart part = (Multipart)m[i].getContent();
            contentString = part.getBodyPart(0).getContent().toString();
            mark_email_read(m[i]);
          }
          
          list.add(contentString);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    String[] newArray = new String[list.size()];
    list.toArray(newArray);
    return newArray;
  }
  

  public String[] get_email(String senderEmailAddress, String recipientEmailAddress, String subject)
  {
    ArrayList<String> list = new ArrayList();
    
    Folder inbox = getFolder("INBOX", 2);
    FromStringTerm fs = new FromStringTerm(senderEmailAddress);
    RecipientStringTerm rs = new RecipientStringTerm(MimeMessage.RecipientType.TO, recipientEmailAddress);
    

    SubjectTerm st = new SubjectTerm(subject);
    
    SearchTerm s = new AndTerm(new SearchTerm[] { fs, rs, st });
    

    String contentString = null;
    try
    {
      Message[] m = inbox.search(s);
      for (int i = 0; i < m.length; i++) {
        try
        {
          if ((m[i].getContent() instanceof String)) {
            contentString = (String)m[i].getContent();
            mark_email_read(m[i]);
          } else if ((m[i].getContent() instanceof Multipart)) {
            Multipart part = (Multipart)m[i].getContent();
            contentString = part.getBodyPart(0).getContent().toString();
            mark_email_read(m[i]);
          }
          
          list.add(contentString);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    String[] newArray = new String[list.size()];
    list.toArray(newArray);
    return newArray;
  }
  

  public String[] get_email(String recipientEmailAddress, String subject)
  {
    ArrayList<String> list = new ArrayList();
    
    Folder inbox = getFolder("INBOX", 2);
    RecipientStringTerm rs = new RecipientStringTerm(MimeMessage.RecipientType.TO, recipientEmailAddress);
    

    SubjectTerm st = new SubjectTerm(subject);
    
    SearchTerm s = new AndTerm(new SearchTerm[] { rs, st });
    

    String contentString = null;
    try {
      Message[] m = inbox.search(s);
      for (int i = 0; i < m.length; i++) {
        try
        {
          if ((m[i].getContent() instanceof String)) {
            contentString = (String)m[i].getContent();
          } else if ((m[i].getContent() instanceof Multipart)) {
            Multipart part = (Multipart)m[i].getContent();
            contentString = part.getBodyPart(0).getContent().toString();
          }
          
          list.add(contentString);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    String[] newArray = new String[list.size()];
    list.toArray(newArray);
    return newArray;
  }
  











  public List<String> get_link_from_email(String recipientAddress)
  {
    List<String> res = new ArrayList();
    String str = null;
    
    str = get_most_recent_email(recipientAddress);
    HTMLLinkExtractor htmlLinkExtractor = new HTMLLinkExtractor();
    
    for (HTMLLinkExtractor.HtmlLink h : htmlLinkExtractor.grabHTMLLinks(str)) {
      res.add(link);
    }
    
    return res;
  }
  

  private Message[] searchEmail(String folderName, int folderOpenStatus, SearchTerm searchTerm)
  {
    Folder folder = getFolder(folderName, folderOpenStatus);
    Message[] msgs = null;
    try {
      msgs = folder.search(searchTerm);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    
    return msgs;
  }
  







  private List<Message> searchEmailByDateReceivedOrSent(String folderName, int folderOpenStatus, String sentOrRecievedDate, SearchTerm includeOtherSearchTerm)
  {
    List<Message> msgsToReturn = new ArrayList();
    Date userDate = null;
    String serverDate = null;
    Calendar calendar = new GregorianCalendar();
    SimpleDateFormat serverDateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
    SimpleDateFormat userDateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    try {
      userDate = userDateFormatter.parse(sentOrRecievedDate);
    }
    catch (ParseException e1) {
      e1.printStackTrace();
    }
    
    Date dayBefore = null;
    Date dayAfter = null;
    


    calendar.setTime(userDate);
    calendar.add(5, -1);
    dayBefore = calendar.getTime();
    calendar.add(5, 2);
    dayAfter = calendar.getTime();
    calendar.add(5, -1);
    



    AndTerm andTermRecDate = new AndTerm(new SearchTerm[] { new ReceivedDateTerm(6, userDate), new ReceivedDateTerm(5, dayBefore), new ReceivedDateTerm(1, dayAfter) });
    




    AndTerm andTermSentDate = new AndTerm(new SearchTerm[] { new SentDateTerm(6, userDate), new SentDateTerm(5, dayBefore), new SentDateTerm(1, dayAfter) });
    




    OrTerm orTerm = new OrTerm(andTermRecDate, andTermSentDate);
    SearchTerm term = null;
    if (includeOtherSearchTerm != null) {
      term = new AndTerm(new SearchTerm[] { orTerm, includeOtherSearchTerm });

    }
    else
    {
      term = new AndTerm(new SearchTerm[] { orTerm });
    }
    


    Message[] msgs = searchEmail(folderName, folderOpenStatus, term);
    for (Message m : msgs) {
      try {
        serverDate = m.getHeader("Date")[0];
      }
      catch (MessagingException e) {
        e.printStackTrace();
      }
      Date date = null;
      try {
        date = serverDateFormatter.parse(serverDate);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      calendar.setTime(date);
      
      if ((!userDate.after(calendar.getTime())) || (userDate.before(calendar.getTime()))) {
        msgsToReturn.add(m);
      }
    }
    



    return msgsToReturn;
  }
  












  public boolean check_todays_email_in_inbox(String recipientEmailAddress, String textContainsInSubject, String textContainsInBody)
  {
    return check_todays_email_in_inbox(recipientEmailAddress, textContainsInSubject, textContainsInBody, false);
  }
  

  public boolean check_todays_email_in_inbox(String recipientEmailAddress, String textContainsInSubject, String textContainsInBody, boolean includeReadEmailToo)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    String date = sdf.format(new GregorianCalendar().getTime());
    

    SubjectTerm st = new SubjectTerm(textContainsInSubject);
    BodyTerm bt = new BodyTerm(textContainsInBody);
    
    Flags flags = new Flags(Flags.Flag.SEEN);
    FlagTerm ft = new FlagTerm(flags, false);
    
    SearchTerm s = null;
    if (!includeReadEmailToo) {
      s = new AndTerm(new SearchTerm[] { st, bt, ft });
    } else {
      s = new AndTerm(new SearchTerm[] { st, bt });
    }
    
    return searchEmailByDateReceivedOrSent("Inbox", 2, date, s).size() > 0;
  }
}
