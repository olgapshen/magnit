package magnit;

import java.sql.*;

public class DataAccess {
  private String m_password;
  private String m_user;
  private String m_db;

  private static final int BATCH_SIZE = 1000;

  public DataAccess(String db, String user, String password) {
    m_password = password;
    m_user = user;
    m_db = db;
  }

  public void printError(SQLException ex) {
    System.out.println("SQLException: " + ex.getMessage());
    System.out.println("SQLState: " + ex.getSQLState());
    System.out.println("VendorError: " + ex.getErrorCode());
  }

  public Connection getConnection() {
    Connection conn = null;

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    }
    catch (ClassNotFoundException e) {
        System.out.println("Driver not found: " + e.getMessage());
        return null;
    }

    String connString =
      "jdbc:mysql://localhost/" + m_db +
      "?useSSL=false&" +
      "user=" + m_user +
      "&password=" + m_password;

    try {
        conn = DriverManager.getConnection(connString);
    } catch (SQLException ex) {
      printError(ex);
    }

    return conn;
  }

  public void closeConnection(Connection conn) {
    try {
      conn.close();
    } catch (SQLException e) {
      printError(e);
    }
  }

  public void truncateTable() {
    Connection conn = getConnection();

    if (conn == null) return;

    try
    {
      String query = "truncate table `test`";
      PreparedStatement preparedStmt = conn.prepareStatement(query);
      preparedStmt.execute();
    }
    catch (SQLException e)
    {
      printError(e);
    }
    finally
    {
      closeConnection(conn);
    }
  }

  public void writeRecords(int num) {
    Connection conn = getConnection();

    if (conn == null) return;

    try
    {
      String query =
        "insert into `test` (`field`) " +
        "values (?)";

      PreparedStatement preparedStmt = conn.prepareStatement(query);

      for (int i = 1; i <= num ; i++) {
        preparedStmt.setInt (1, i);
        preparedStmt.addBatch();

        if (i % BATCH_SIZE == 0 || i == num) {
          System.out.println("Write batch: " + i);
          preparedStmt.executeBatch();
        }
      }
    }
    catch (SQLException e)
    {
      printError(e);
    }
  }

  public ResultSet getRecordsStream(Connection conn) {
    ResultSet rs = null;
    String query = "SELECT * FROM test";

    try {
      Statement st = conn.createStatement();
      rs = st.executeQuery(query);
    } catch(SQLException ex) {
      printError(ex);
    }

    return rs;
  }
}
