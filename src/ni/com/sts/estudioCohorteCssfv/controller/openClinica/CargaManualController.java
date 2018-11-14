package ni.com.sts.estudioCohorteCssfv.controller.openClinica;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

//import ni.com.sts.estudioCohorteCssfv.datos.proceso.*;
import ni.com.sts.estudioCohorteCssfv.datos.hojaConsulta.*;
import ni.com.sts.estudioCohorteCssfv.datos.paciente.PacienteDA;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;
import ni.com.sts.estudioCohorteCSSFV.modelo.Paciente;
import ni.com.sts.estudioCohorteCssfv.openClinica.EventScheduleParams;
import ni.com.sts.estudioCohorteCssfv.openClinica.ServiciosOpenClinica;
import ni.com.sts.estudioCohorteCssfv.servicios.PacienteService;
import ni.com.sts.estudioCohorteCssfv.util.UtilProperty;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
//import ni.com.sts.estudioCohorteCssfv.servicios.HistEjecucionProcesoService;
import ni.com.sts.estudioCohorteCssfv.servicios.HojaConsultaService;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilDate;

import org.apache.commons.configuration.CompositeConfiguration;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;

public class CargaManualController extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HojaConsultaService hojaConsultaService = new HojaConsultaDA();
	//private HistEjecucionProcesoService histEjecCargaOpenClinicaService = new HistEjecucionProcesoDA();
	private PacienteService pacienteService = new PacienteDA();
	private CompositeConfiguration config;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception{
	    super.doAfterCompose(comp);
	    init();
	}

	/**
	 *
	 */
	private void init() {
		config = UtilProperty.getConfigurationfromExternalFile("estudioCohorteCSSFVOPC.properties");
	}
	
    @Wire("[id$=btnEjecutar]")
    private Button btnEjecutar;
    
    @SuppressWarnings("rawtypes")
	@Listen("onClick=[id$=btnEjecutar]")
    public void btnEjecutar_onClick() {
        try {
        	List<HojaConsulta> hojasPendientesCarga = hojaConsultaService.getHojasConsultaPendientesCarga();
        	if (hojasPendientesCarga.size()>0){
        		EventScheduleParams eventParams;
        		Date dFechaHoy = new Date();
        		int registrosProcesados =0;
        		int sec = 1;
        		ServiciosOpenClinica cliente = new ServiciosOpenClinica();
        		for(HojaConsulta hoja:hojasPendientesCarga){
					InfoResultado resultado = new InfoResultado();
					//se consumen webservices
					Paciente paciente = pacienteService.getPacienteById(hoja.getCodExpediente());
					eventParams = new EventScheduleParams();
					eventParams.setLabel(String.valueOf(paciente.getCodExpediente())); //<label>9803</label>
					eventParams.setEventDefinitionOID(config.getString("event.schedule.eventDefinitionOID"));//<eventDefinitionOID>SE_CONSULTACS</eventDefinitionOID>
					eventParams.setLocation(config.getString("event.schedule.location")); //<location>CS</location>
					eventParams.setIdentifier(config.getString("event.schedule.identifier")); //<identifier>S_1</identifier>
					eventParams.setSiteidentifier(config.getString("event.schedule.site.identifier")); //<identifier></identifier>
					eventParams.setStartDate(hoja.getFechaConsulta()); //<startDate>2008-12-12</startDate> //<startTime>12:00</startTime>
					if (hoja.getFechaCierre()!=null){
						eventParams.setEndDate(hoja.getFechaCierre()); //<endDate>2008-12-12</endDate> //<endTime>15:00</endTime>
					}else {
						eventParams.setEndDate(null);
					}
						resultado = cliente.consumirEventCliente(eventParams);
						if (resultado.isOk()){
							resultado = cliente.consumirDataClienteV2(hoja, sec, resultado.getMensaje());
							if (resultado.isOk()){
								//se registra estado carga cerrado
								hoja.setEstadoCarga('1');
								hojaConsultaService.updateHojaConsulta(hoja);
								registrosProcesados++;
							}
						//logger.debug("Hoja de consulta procesada: "+resultado.getObjeto().toString());
					}
					//se registra estado carga cerrado
					 hojaConsultaService.updateHojaConsulta(hoja);
				}
        	Mensajes.enviarMensaje(Mensajes.PROCESO_OPEN_CLINICA_TERMINADO.replace("{0}", String.valueOf(registrosProcesados)), Mensajes.TipoMensaje.INFO);
        	}else{
        		Mensajes.enviarMensaje(Mensajes.NO_DATOS, Mensajes.TipoMensaje.EXCLAM);
        	}
        	
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_PROCESAR_DATOS, Mensajes.TipoMensaje.ERROR);
		}
    }
}