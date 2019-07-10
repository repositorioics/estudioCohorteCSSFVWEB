package ni.com.sts.estudioCohorteCssfv.controller.openClinica;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

//import ni.com.sts.estudioCohorteCssfv.datos.proceso.*;
import ni.com.sts.estudioCohorteCssfv.datos.hojaConsulta.*;
import ni.com.sts.estudioCohorteCssfv.datos.paciente.PacienteDA;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaInfluenza;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaZika;
import ni.com.sts.estudioCohorteCSSFV.modelo.Paciente;
import ni.com.sts.estudioCohorteCSSFV.modelo.SeguimientoInfluenza;
import ni.com.sts.estudioCohorteCSSFV.modelo.SeguimientoZika;
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
        	List<HojaInfluenza> hojasInfluenzasPendientesCarga = hojaConsultaService.getHojasInfluenzasPendientesCarga();
        	List<HojaZika> hojasZikaPendientesCarga = hojaConsultaService.getHojasZikaPendientesCarga();
        
        	int registrosProcesados =0;
        	int registrosInfluenzaProcesados =0;
        	int registrosZikaProcesados =0;
        	
        	if (hojasPendientesCarga.size()>0){
        		EventScheduleParams eventParams;
        		Date dFechaHoy = new Date();
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
        	//Mensajes.enviarMensaje(Mensajes.PROCESO_OPEN_CLINICA_TERMINADO.replace("{0}", String.valueOf(registrosProcesados)), Mensajes.TipoMensaje.INFO);
        	}/*else{
        		Mensajes.enviarMensaje(Mensajes.NO_DATOS, Mensajes.TipoMensaje.EXCLAM);
        	}*/
        	        	
        	if (hojasInfluenzasPendientesCarga.size()>0) {
        		EventScheduleParams eventParams;
        		
        		int sec = 1;
        		ServiciosOpenClinica cliente = new ServiciosOpenClinica();
        		for(HojaInfluenza hojaInfluenza:hojasInfluenzasPendientesCarga) {
        			InfoResultado resultado = new InfoResultado();
        			List<SeguimientoInfluenza> seguimientoInfluenzas = hojaConsultaService.getSeguimientoInfluenza(hojaInfluenza.getSecHojaInfluenza());
        			/* Verificamos si el repeat key de la hoja influenza sea igual a null
					 * si se cumple la condicion se crea el crf completo*/
        			if (hojaInfluenza.getRepeatKey() == null) {
        				//se consumen webservices
    					Paciente paciente = pacienteService.getPacienteById(hojaInfluenza.getCodExpediente());
    					eventParams = new EventScheduleParams();
    					eventParams.setLabel(String.valueOf(paciente.getCodExpediente())); //<label>9803</label>
    					eventParams.setEventDefinitionOID(config.getString("event.schedule.eventDefinitionOID.HI"));//<eventDefinitionOID>SE_CONSULTACS</eventDefinitionOID>
    					eventParams.setLocation(config.getString("event.schedule.location")); //<location>CS</location>
    					eventParams.setIdentifier(config.getString("event.schedule.identifier")); //<identifier>S_1</identifier>
    					eventParams.setSiteidentifier(config.getString("event.schedule.site.identifier")); //<identifier></identifier>
    					eventParams.setStartDate(hojaInfluenza.getFechaInicio()); //<startDate>2008-12-12</startDate> //<startTime>12:00</startTime>
    					if (hojaInfluenza.getFechaCierre() != null) {
    						eventParams.setEndDate(hojaInfluenza.getFechaCierre());
    					} else {
    						eventParams.setEndDate(null);
    					}
    					resultado = cliente.consumirEventCliente(eventParams);
    					if (resultado.isOk()) {
        					hojaInfluenza.setRepeatKey(resultado.getMensaje());
        					hojaConsultaService.updateHojaInfluenza(hojaInfluenza);
    						resultado = cliente.consumirDataHojaInfluenza(hojaInfluenza, seguimientoInfluenzas, sec, resultado.getMensaje());
    						if (resultado.isOk()) {
    							hojaInfluenza.setEstadoCarga('1'); // hay que agregarlo a la bd
    							hojaConsultaService.updateHojaInfluenza(hojaInfluenza);
    							registrosInfluenzaProcesados++;
    						}
    					}
        			} 
        			/* Si ya existe el repeat key en la hoja de influenza, nos indica que ya se a creado el crf entonces
					 * solo procedemos a hacer un update para ese repeat key*/
        			else {
        				//List<SeguimientoInfluenza> seguimientoInfluenzas = hojaConsultaService.getSeguimientoInfluenza(hojaInfluenza.getSecHojaInfluenza());
        				resultado = cliente.consumirDataHojaInfluenza(hojaInfluenza, seguimientoInfluenzas, sec, resultado.getMensaje());
						if (resultado.isOk()) {
							hojaInfluenza.setEstadoCarga('1'); // hay que agregarlo a la bd
							hojaConsultaService.updateHojaInfluenza(hojaInfluenza);
							registrosInfluenzaProcesados++;
						}
        			}
					//hojaConsultaService.updateHojaInfluenza(hojaInfluenza);
        		}
        		// FALTA MENSAJE QUE INDIQUE QUE EL PROCESO TERMINO
        		//Mensajes.enviarMensaje(Mensajes.PROCESO_OPEN_CLINICA_TERMINADO.replace("{0}", String.valueOf(registrosProcesados)), Mensajes.TipoMensaje.INFO);
        	}/*else{
        		Mensajes.enviarMensaje(Mensajes.NO_DATOS, Mensajes.TipoMensaje.EXCLAM);
        	}*/
        	
        	if (hojasZikaPendientesCarga.size() > 0) {
        		
        		EventScheduleParams eventParams;
        		int sec = 1;
        		ServiciosOpenClinica cliente = new ServiciosOpenClinica();
        		for(HojaZika hojaZika:hojasZikaPendientesCarga) {
        			
        			InfoResultado resultado = new InfoResultado();
        			List<SeguimientoZika> seguimientoZika = hojaConsultaService.getSeguimientoZika(hojaZika.getSecHojaZika());
        			
					/* Verificamos si el repeat key de la hoja zika sea igual a null
					 * si se cumple la condicion se crea el crf completo*/
        			if (hojaZika.getRepeatKey() == null) {
            			//se consumen webservices
    					Paciente paciente = pacienteService.getPacienteById(hojaZika.getCodExpediente());
    					eventParams = new EventScheduleParams();
    					eventParams.setLabel(String.valueOf(paciente.getCodExpediente())); //<label>9803</label>
    					eventParams.setEventDefinitionOID(config.getString("event.schedule.eventDefinitionOID.ZK"));//<eventDefinitionOID>SE_CONSULTACS</eventDefinitionOID>
    					eventParams.setLocation(config.getString("event.schedule.location")); //<location>CS</location>
    					eventParams.setIdentifier(config.getString("event.schedule.identifier")); //<identifier>S_1</identifier>
    					eventParams.setSiteidentifier(config.getString("event.schedule.site.identifier")); //<identifier></identifier>
    					eventParams.setStartDate(hojaZika.getFechaInicio()); //<startDate>2008-12-12</startDate> //<startTime>12:00</startTime>
    					if (hojaZika.getFechaCierre() != null) {
    						eventParams.setEndDate(hojaZika.getFechaCierre());
    					} else {
    						eventParams.setEndDate(null);
    					}
    					
    					resultado = cliente.consumirEventCliente(eventParams);
    					if (resultado.isOk()) {
    						hojaZika.setRepeatKey(resultado.getMensaje());
        					hojaConsultaService.updateHojaZika(hojaZika);
    						resultado = cliente.consumirDataHojaZika(hojaZika, seguimientoZika, sec, resultado.getMensaje());
    						if (resultado.isOk()){
    							hojaZika.setEstadoCarga('1'); // hay que agregarlo a la bd
    							hojaConsultaService.updateHojaZika(hojaZika);
    							registrosZikaProcesados++;
    						}
    					}
    					hojaConsultaService.updateHojaZika(hojaZika);
        			} 
        			/* Si ya existe el repeat key en la hoja de influenza, nos indica que ya se a creado el crf entonces
					 * solo procedemos a hacer un update para ese repeat key*/
        			else {
        				//List<SeguimientoZika> seguimientoZika = hojaConsultaService.getSeguimientoZika(hojaZika.getSecHojaZika());
        				resultado = cliente.consumirDataHojaZika(hojaZika, seguimientoZika, sec, resultado.getMensaje());
						if (resultado.isOk()){
							hojaZika.setEstadoCarga('1'); // hay que agregarlo a la bd
							hojaConsultaService.updateHojaZika(hojaZika);
							registrosZikaProcesados++;
						}
        			}
        			//hojaConsultaService.updateHojaZika(hojaZika);
        		}
        	}
        	if (hojasPendientesCarga.size() <= 0 && hojasInfluenzasPendientesCarga.size() <= 0 && hojasZikaPendientesCarga.size() <= 0) {
        		Mensajes.enviarMensaje(Mensajes.NO_DATOS, Mensajes.TipoMensaje.EXCLAM);
        	} else {
        		Mensajes.enviarMensaje(Mensajes.PROCESO_OPEN_CLINICA_TERMINADO.replace("{0}", String.valueOf(registrosProcesados))
        				.replace("{1}", String.valueOf(registrosInfluenzaProcesados))
        				.replace("{2}", String.valueOf(registrosZikaProcesados)), Mensajes.TipoMensaje.INFO);
        	}
        	
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_PROCESAR_DATOS, Mensajes.TipoMensaje.ERROR);
		}
    }
}