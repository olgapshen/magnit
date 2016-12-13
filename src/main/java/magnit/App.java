package magnit;

import java.util.*;
import java.text.*;

import magnit.*;

public class App
{
  public static void main( String[] args )
  {
      Processor processor = new Processor();
      processor.setDB("magnit");
      processor.setUser("magnit");
      processor.setPassword("123456");
      processor.setN(20);

      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      Date date = new Date();
      System.out.println("Start: " + dateFormat.format(date));

      processor.init();
      processor.writeRecords();
      processor.readRecords();
      processor.transform();
      processor.sum();

      date = new Date();
      System.out.println("Stop: " + dateFormat.format(date));
  }
}
