package ni.com.sts.estudioCohorteCssfv.resultados;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.configuration.CompositeConfiguration;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import ni.com.sts.estudioCohorteCSSFV.modelo.Paciente;
import ni.com.sts.estudioCohorteCSSFV.modelo.PerifericoResultado;
import ni.com.sts.estudioCohorteCSSFV.modelo.UsuariosView;
import ni.com.sts.estudioCohorteCssfv.datos.laboratorio.LaboratorioDA;
import ni.com.sts.estudioCohorteCssfv.datos.paciente.PacienteDA;
import ni.com.sts.estudioCohorteCssfv.datos.usuario.UsuariosDA;
import ni.com.sts.estudioCohorteCssfv.dto.OrdenesExamenes;
import ni.com.sts.estudioCohorteCssfv.servicios.LaboratorioService;
import ni.com.sts.estudioCohorteCssfv.servicios.PacienteService;
import ni.com.sts.estudioCohorteCssfv.servicios.UsuariosService;
import ni.com.sts.estudioCohorteCssfv.util.UtilDate;
import ni.com.sts.estudioCohorteCssfv.util.UtilProperty;

public class ImprimirResultados {
	private static String directorioTemporal;
	private static CompositeConfiguration config;
	private static LaboratorioService laboratorioService = new LaboratorioDA();
	private static UsuariosService usuariosService = new UsuariosDA();
	private static PacienteService pacienteService = new PacienteDA();
		
    static {
        try{
            // Obtener el directorio temporal del sistema o del servidor.
            directorioTemporal = System.getProperty("jboss.server.temp.dir"); // Carpeta de temporales del usuario: java.io.tmpdir
            directorioTemporal = directorioTemporal.replace('/', System.getProperty("file.separator").charAt(0));
            directorioTemporal = directorioTemporal + (directorioTemporal.endsWith(System.getProperty("file.separator")) ? "" : System.getProperty("file.separator"));

            // Obtener el archivo properties.
            config = UtilProperty.getConfiguration("EstudioCohorteCssfvWEBExt.properties", "ni/com/sts/estudioCohorteCssfv/properties/EstudioCohorteCssfvWEBInt.properties");

        } catch(Throwable e) {
            e.printStackTrace();
        }
    }
    
	private void generarExtendidoPeriferico(OrdenesExamenes ordenExamen){
		
		String sRutaJasper = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.extendido.periferico");
		String sRutaImgLogo = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.imagen.logo");
		
		PerifericoResultado resultado = laboratorioService.obtenerExtendidoPeriferico(ordenExamen.getSecOrdenLaboratorio());
		Paciente paciente = pacienteService.getPacienteById(ordenExamen.getCodigoExpediente());
		UsuariosView bioanalista = usuariosService.obtenerUsuarioById(resultado.getUsuarioBioanalista().intValue());
		
		// generando el documento
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		
		parametros.put("logo", sRutaImgLogo);
		parametros.put("horaIngreso", "jodido");// UtilDate.DateToString(resultado.getHoraReporte(), "hh:mm a"));
		parametros.put("codigoP", ordenExamen.getCodigoExpediente());
		parametros.put("medico", ordenExamen.getUsuarioMedico());
		parametros.put("fecha", UtilDate.DateToString(ordenExamen.getFechaOrdenLaboratorio().getTime(),"dd/MM/yyyy"));
		parametros.put("apellido1", paciente.getApellido1());
		parametros.put("apellido2", paciente.getApellido2());
		parametros.put("nombre1", paciente.getNombre1());
		parametros.put("nombre2", paciente.getNombre2());
		parametros.put("anisocitosis", resultado.getAnisocitosis());
		parametros.put("anisocromia", resultado.getAnisocromia());
		parametros.put("poiquilositosis", resultado.getPoiquilocitosis());
		parametros.put("linfocitosAtipicos", resultado.getLinfocitosAtipicos());
		parametros.put("observacionesSB", resultado.getObservacionSblanca());
		parametros.put("observacionesIP", resultado.getObservacionPlaqueta());
		parametros.put("bioanalista", bioanalista.getNombre());
		parametros.put("codigoBio", bioanalista.getCodigoPersonal());
		parametros.put("horaRecibido", UtilDate.DateToString(new Date(), "hh:mm a"));
		
		try {
			
			JasperPrint file = JasperFillManager.fillReport(sRutaJasper, parametros);
            // Crear el arreblo de byte del PDF.
            byte[] reporte = JasperExportManager.exportReportToPdf(file);
            
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
