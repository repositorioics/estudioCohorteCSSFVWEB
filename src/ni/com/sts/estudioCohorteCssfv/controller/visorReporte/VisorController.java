package ni.com.sts.estudioCohorteCssfv.controller.visorReporte;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ni.com.sts.estudioCohorteCssfv.util.UtilProperty;

import org.apache.commons.configuration.CompositeConfiguration;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Window;

public class VisorController extends SelectorComposer<Component> {

	private static CompositeConfiguration config;
	
	// Enumeraciones para el tipo de reporte a generar.
    public enum TipoReporte {
        PDF("pdf"), HTML("html"), RTF("rtf"), JXL("jxl"), CSV("csv"), ODT("odt");

        private final String tipo;

        private TipoReporte(String tipo) {
            this.tipo = tipo;
        }

        public String toString() {
            return this.tipo;
        }
    }
    
    private static String source;
    private static Map<String, Object> parameters;
    private static List<?> dataSources;
    
    @Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		init();
	}

	private void init() {
		// Obtener el archivo properties.
        config = UtilProperty.getConfiguration("EstudioCohorteCssfvWEBExt.properties", "ni/com/sts/estudioCohorteCssfv/properties/EstudioCohorteCssfvWEBInt.properties");
	}
    
    /**
     * Método utilitario para generar reporte.
     *
     * @param titulo         Titulo del visor de reporte.
     * @param source         Ruta del reporte compilado (.jasper).
     * @param parametros     Lista de parámetros para el reporte.
     * @param collectionDataSource Colección de DataSources para el reporte.
     */
     public static void mostrarReporte(String titulo, String pathReporte,
             Map<String, Object> parametros, List<?> collectionDataSource) {

        mostrarReporte(titulo, pathReporte, parametros, collectionDataSource, false, false);
     }

    /**
    * Método utilitario para generar reporte.
    *
    * @param titulo         Titulo del visor de reporte.
    * @param source         Ruta del reporte compilado (.jasper)
    * @param parametros     Lista de parámetros para el reporte.
    * @param collectionDataSource Colección de DataSources para el reporte
    * @param indicarDirSubReport  Indica si se le pasará al sub-reporte el path
    *                             donde se encuentra el reporte principal.
    * @param indicarDirImagenes   Indica si se le pasará el path de donde se
    *                             encuentran las imagenes para el reporte.
    */
    public static void mostrarReporte(String titulo, String pathReporte,
            Map<String, Object> parametros, List<?> collectionDataSource,
            boolean indicarDirSubReport, boolean indicarDirImagenes) {

        Window win = (Window) Executions.createComponents("/visorReportes/reporte.zul", null, null);
        if (titulo != null && !titulo.isEmpty()) {
            win.setTitle(titulo);
        }

        if(parametros == null)
            parametros = new HashMap<String, Object>();
       
        // Indicar si se le pasará al subreporte la URL donde se encuentra el reporte principal.
        if (indicarDirSubReport) {

            String pathSubReport = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) +  config.getString("ProductoTracking.URL.Reportes");
            pathSubReport = pathSubReport.replace('/', System.getProperty("file.separator").charAt(0));
            parametros.put("SUBREPORT_DIR", pathSubReport); // Parámetro estandar de JasperReport.

        }

        if (indicarDirImagenes) {

            String pathImagenes = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) +  config.getString("ProductoTracking.URL.Reportes.Imagenes");
            pathImagenes = pathImagenes.replace('/', System.getProperty("file.separator").charAt(0));
            parametros.put("pRutaImagenes", pathImagenes); // Parámetro a agregar.

        }

        Jasperreport report = null;
        report = (Jasperreport) win.getFellow("reporte");

        report.setSrc(pathReporte);
        report.setType(TipoReporte.PDF.toString());
        report.setParameters(parametros);

        if (collectionDataSource != null) {
            report.setDatasource(new JRBeanCollectionDataSource(collectionDataSource));
        }
        
        source = pathReporte;
        parameters = parametros;
        dataSources = collectionDataSource;
    }
}
