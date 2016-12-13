package magnit;

import java.sql.*;
import javax.xml.stream.*;
import javax.xml.transform.*;
import java.io.*;

import magnit.*;

public class Processor {
  	private DataAccess m_dataAccess;

    private String m_db;
    private String m_user;
    private String m_password;

    private int m_N;

    public void setDB(String db) {
      m_db = db;
    }

    public void setUser(String user) {
      m_user = user;
    }

    public void setPassword(String password) {
      m_password = password;
    }

    public void setN(int n) {
      m_N = n;
    }

    public void init() {
      m_dataAccess = new DataAccess(m_db, m_user, m_password);
      m_dataAccess.truncateTable();
    }

    public void writeRecords() {
      m_dataAccess.writeRecords(m_N);
    }

    public void readRecords() {
      Connection conn = m_dataAccess.getConnection();
      ResultSet rs = m_dataAccess.getRecordsStream(conn);

      XMLOutputFactory xof =  XMLOutputFactory.newInstance();
      XMLStreamWriter xtw = null;

      System.out.println("Write XML document");

      try {
        xtw = xof.createXMLStreamWriter(new FileWriter("1.xml"));

        xtw.writeStartDocument("utf-8","1.0");
        xtw.writeStartElement("entires");

        while (rs.next())
        {
          int num = rs.getInt("field");

          xtw.writeStartElement("entry");
          xtw.writeStartElement("field");
          xtw.writeCharacters(Integer.toString(num));
          xtw.writeEndElement();
          xtw.writeEndElement();
        }

        xtw.writeEndElement();
        xtw.writeEndDocument();

        xtw.flush();
        xtw.close();
      } catch (SQLException e) {
        m_dataAccess.printError(e);
      } catch (XMLStreamException e) {
        System.out.println("XML error: " + e.getMessage());
      } catch (IOException e) {
        System.out.println("IO error: " + e.getMessage());
      }

      m_dataAccess.closeConnection(conn);
  }

  public void transform() {
    System.out.println("Transforming");

    try {
      javax.xml.transform.Source xmlSource =
        new javax.xml.transform.stream.StreamSource(
          new FileInputStream("1.xml"));

      javax.xml.transform.Result xmlResult =
        new javax.xml.transform.stax.StAXResult(
          XMLOutputFactory.newInstance().createXMLStreamWriter(
            new FileWriter("2.xml")));

      javax.xml.transform.Source xsltSource =
        new javax.xml.transform.stream.StreamSource(
          new FileInputStream("xsl.xsl"));

      javax.xml.transform.TransformerFactory transFact =
        javax.xml.transform.TransformerFactory.newInstance();

      javax.xml.transform.Transformer trans = transFact.newTransformer(xsltSource);

      trans.transform(xmlSource, xmlResult);
    } catch (IOException e) {
      System.out.println("IO error: " + e.getMessage());
    } catch(XMLStreamException e) {
      System.out.println("XML error: " + e.getMessage());
    } catch (TransformerConfigurationException e) {
      System.out.println("XSLT config error: " + e.getMessage());
    } catch (TransformerException e) {
      System.out.println("XSLT error: " + e.getMessage());
    }
  }

  public void sum() {
    System.out.println("Summing");
    int sum = 0;

    try {
      XMLInputFactory xmlif = XMLInputFactory.newInstance();
      XMLStreamReader xmlr =
        xmlif.createXMLStreamReader(new FileInputStream("2.xml"));

      while(xmlr.hasNext()) {
        int event = xmlr.next();

        switch (event) {
	        case XMLStreamConstants.START_ELEMENT:
	          if ("entry".equals(xmlr.getLocalName())) {
	            int num = Integer.parseInt(xmlr.getAttributeValue(0));
              sum += num;
	          }

            break;
        }
      }
    } catch (IOException e) {
      System.out.println("IO error: " + e.getMessage());
    } catch (XMLStreamException e) {
      System.out.println("XML Stream exception: " + e.getMessage());
    }

    System.out.println("Sum of the fields: " + Integer.toString(sum));
  }
}
