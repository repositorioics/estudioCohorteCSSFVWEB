/**
 *
 */
package ni.com.sts.seguridadWS.webservices;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import org.apache.axis.AxisFault;

import ni.com.sts.seguridadWS.webservices.SeguridadWSServiceSoapBindingStub;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilityProperty;

/**
 * @author Ricardo Calero
 *
 */
public class SeguridadWS {

	  private static SeguridadWSServiceSoapBindingStub seguridadWSService = null;

	  public static SeguridadWSServiceSoapBindingStub getSeguridadWSService() throws AxisFault, RemoteException, MalformedURLException{
		  getSeguridadWS();
		  return seguridadWSService;
	  }

	/**
	 *
	 * @return
	 * @throws AxisFault
	 * @throws MalformedURLException
	 */
	private static SeguridadWSServiceSoapBindingStub getSeguridadWS() throws AxisFault, MalformedURLException {
	    String urlWS = getUrlWS();
	    seguridadWSService = new SeguridadWSServiceSoapBindingStub(new java.net.URL(urlWS), null);
	    return seguridadWSService;
	}

	/**
	 * Obtener url de servicio web definida en propiedad de configuración
	 * @return
	 */
	private static String getUrlWS() {
		String url = "";
		try{
			  url = UtilityProperty.getValueProperty("Seguridad.URL.WS");
		  } catch(Exception ex) {
			  System.out.println("-------------------------------------------............");
			  System.out.println("EXCEPTION: Desde SeguridadWS().getUrlWS");
			  System.out.println("-------------------------------------------............");
			  System.out.println(Mensajes.ERROR_CONSULTAR_PROP_CONFIG + "Error:" + ex.getMessage());
			  ex.printStackTrace();
		  }
		return url;
	}

}
