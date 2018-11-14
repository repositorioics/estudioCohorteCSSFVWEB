package ni.com.sts.estudioCohorteCssfv.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UtilDate {
	
	public static String DateToString(Date fecha, String formato){
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		return sdf.format(fecha);
	}

	public static Date StringToDate(String fecha, String formato) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		return sdf.parse(fecha);
	}
	
	public static Date StringToDate(String fecha, String formato, Locale locale) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(formato, locale);
		return sdf.parse(fecha);
	}
}
