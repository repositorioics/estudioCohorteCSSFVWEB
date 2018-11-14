package ni.com.sts.estudioCohorteCssfv.util;

import org.apache.commons.configuration.CompositeConfiguration;

import ni.com.sts.estudioCohorteCssfv.util.UtilProperty;

/**
 * Utilitario para obtener las propiedades de configuracion del sistema.
 * @author Ricardo Calero
 *
 */
public class UtilityProperty extends UtilProperty {

	private static CompositeConfiguration config;

	static {
        try{
            config = getConfiguration("EstudioCohorteCssfvWEBExt.properties","ni/com/sts/estudioCohorteCssfv/properties/EstudioCohorteCssfvWEBInt.properties");
        } catch(Throwable e) {
            System.out.println("-------------------------------------------............");
            System.out.println("EXCEPTION: Desde UtilityProperty()");
            System.out.println("-------------------------------------------............");
            e.printStackTrace();
        }
    }

	public UtilityProperty() {
	 }

    /**
     * Obtener el valor de la propiedad
     * @param nameProperty
     * @return
     * @throws Exception
     */
    public static String getValueProperty(String nameProperty) throws Exception {
        String valueProp = config.getString(nameProperty);
        return valueProp;
    }

}
