package ni.com.sts.estudioCohorteCssfv.util;

import org.apache.log4j.PropertyConfigurator;

public class UtilLog
{

    public UtilLog()
    {
    }

    public static void setLog(String logFileName)
    {
    	String log = System.getProperty("jboss.server.data.dir") + logFileName;
        PropertyConfigurator.configure(log);
    }
}

