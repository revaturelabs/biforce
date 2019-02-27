package util;

//Singleton class
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class ConnectionUtil {
	
	private static Connection connectionInstance = null;
	//private static final Logger log = LogManager.getLogger(ConnectionUtil.class);
	
	
	private ConnectionUtil() {
		
	}
	
	public static Connection getConnection() {
		//log.traceEntry();
		if (connectionInstance != null) {
			return connectionInstance;//log.traceExit(connectionInstance);
		}
		
		InputStream in = null;
		
		try {
		// load information from properties file
		Properties props = new Properties();
      
		in = new FileInputStream("\\SRS\\src\\main\\resources\\.connection.properties");
      props.load(in);

      // get the connection object
      Class.forName("oracle.jdbc.driver.OracleDriver");
      Connection con = null;

      String endpoint = props.getProperty("jdbc.url");
      String username = props.getProperty("jdbc.username");
      String password = props.getProperty("jdbc.password");
      
      con = DriverManager.getConnection(endpoint, username, password);
      return con;//log.traceExit(con);
      }
		catch (Exception e) {
			//log.catching(e);
		} finally {
			try {
				in.close();	
			} catch (Exception e) {
				//log.catching(e);
				//log.traceExit();
			}
			//log.traceExit();
		}

		return null;
	}

}
