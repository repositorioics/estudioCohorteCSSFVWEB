package ni.com.sts.estudioCohorteCssfv.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.log4j.Logger;

public class ConnectionDAO {
	private final Logger logger = Logger.getLogger(this.getClass());
	private CompositeConfiguration config;
	public ConnectionDAO(){
		config = UtilProperty.getConfigurationfromExternalFile("estudioCohorteCSSFVOPC.properties");
		//UtilLog.setLog(config.getString("estudioCohorteCSSFVOPC.log"));	
		logger.debug("Constructor ConnectionDAO():end");
	}
	
	public Connection getConection()
	{		
		logger.debug("getConection():start");
		try {
			ParametersFromManualConnection parametersConnection = new ParametersFromManualConnection();
			
			logger.debug("database.host ["+config.getString("database.host")+"]");
			parametersConnection.setHostName(config.getString("database.host"));
			
			logger.debug("database.name ["+config.getString("database.name")+"]");
			parametersConnection.setDatabaseName(config.getString("database.name"));
			
			logger.debug("database.user ["+config.getString("database.user")+"]");
			parametersConnection.setDatabaseUserName(config.getString("database.user"));
			
			logger.debug("database.password ["+config.getString("database.password")+"]");
			parametersConnection.setPassword(config.getString("database.password"));
			
			logger.debug("database.port ["+config.getString("database.port")+"]");
			parametersConnection.setPort(Integer.valueOf(config.getString("database.port")));
			
			logger.debug("database.maxlimit ["+config.getString("database.maxlimit")+"]");
			parametersConnection.setMaxlimit(config.getString("database.maxlimit"));
			
			logger.debug("database.minlimit ["+config.getString("database.minlimit")+"]");
			parametersConnection.setMinlimit(config.getString("database.minlimit"));
			return getConnectionManualToOracleDB(parametersConnection);
		} catch (Exception e1) {			
			logger.error(" No se pudo obtener una conexión ", e1);
			return null;
		}finally{
			logger.debug("getConection():end");
		}
	}

	
	private Connection getConnectionManualToOracleDB(ParametersFromManualConnection parametros) throws SQLException {

		final Properties props = new Properties();
		props.put("user", parametros.getDatabaseUserName());
		props.put("password", parametros.getPassword());
		try {
			Class.forName("org.postgresql.Driver").newInstance();
		} catch (final ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (final IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (final InstantiationException ex) {
			ex.printStackTrace();
		}

		final StringBuffer url = new StringBuffer();
		url.append("jdbc:postgresql://");
		url.append(parametros.getHostName());
		url.append(":");
		url.append(parametros.getPort().toString());
		url.append("/");
		url.append(parametros.getDatabaseName());
		return DriverManager.getConnection(url.toString(), props);
	}
	
}
