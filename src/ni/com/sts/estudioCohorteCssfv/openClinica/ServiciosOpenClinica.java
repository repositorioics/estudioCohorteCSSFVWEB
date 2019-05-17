package ni.com.sts.estudioCohorteCssfv.openClinica;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Locale;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;
import ni.com.sts.estudioCohorteCSSFV.modelo.UsuariosView;
import ni.com.sts.estudioCohorteCssfv.datos.usuario.UsuariosDA;
import ni.com.sts.estudioCohorteCssfv.servicios.UsuariosService;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.UtilDate;
import ni.com.sts.estudioCohorteCssfv.util.UtilProperty;

import org.apache.axis.AxisFault;
import org.apache.axis.message.PrefixedQName;
import org.apache.axis.types.NormalizedString;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.log4j.Logger;
import org.openclinica.ws.beans.EventType;
import org.openclinica.ws.beans.StudyRefType;
import org.openclinica.ws.beans.StudySubjectRefType;
import org.openclinica.ws.event.v1.ScheduleResponse;

public class ServiciosOpenClinica {
	private final Logger logger = Logger.getLogger(this.getClass());
	private CompositeConfiguration config;
	private UsuariosService usuarioService = new UsuariosDA();
	
	public ServiciosOpenClinica(){
		config = UtilProperty.getConfigurationfromExternalFile("estudioCohorteCSSFVOPC.properties");
	}
	
	/**
	 * Método para consumir el web service Event de OpenClinica
	 * @param params Objeto con todos los parámetros que recibe el servicio
	 * @return {@link InfoResultado} Indicando si el llamado fue exitoso o no, y el objeto que respuesta del servicio
	 */
	public InfoResultado consumirEventCliente(EventScheduleParams params) {
		logger.info("consumirEventCliente()  :: inicio");
		
		InfoResultado resultado = new InfoResultado();		
		try {
			String urlServicio = config.getString("event.schedule.urlService");
			logger.info("event.schedule.urlService :: "+urlServicio);
			logger.info("params.getEventDefinitionOID() :: "+params.getEventDefinitionOID());
			logger.info("params.getIdentifier() :: "+params.getIdentifier());
			logger.info("params.getLabel() :: "+params.getLabel());
			logger.info("params.getLocation() :: "+params.getLocation());
			logger.info("params.getSiteidentifier() :: "+params.getSiteidentifier());
			logger.info("params.getEndDate() :: "+params.getEndDate());
			logger.info("params.getStartDate() :: "+params.getStartDate());
			
			PrefixedQName name = new PrefixedQName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security","wsse");
			
			org.apache.axis.message.SOAPHeaderElement sh = new org.apache.axis.message.SOAPHeaderElement(name);
			sh.setMustUnderstand(true);
			sh.setActor(null);
			
			SOAPElement userNameToken = sh.addChildElement("UsernameToken","wsse");
			userNameToken.setAttribute("wsu:Id", config.getString("openClinica.security.usernameToken.id"));
			userNameToken.setAttribute("xmlns:wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");			

			String clntUserName = config.getString("openClinica.security.user");
			String clntPassword = config.getString("openClinica.security.password");
			
			SOAPElement UNElement = userNameToken.addChildElement("Username","wsse");
			UNElement.addTextNode(clntUserName);

			SOAPElement PwdElement = userNameToken.addChildElement("Password","wsse");
			PwdElement.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
			PwdElement.addTextNode(clntPassword);
			
			org.openclinica.ws.event.v1.WsSoap11Stub wsEvent = new org.openclinica.ws.event.v1.WsSoap11Stub(new java.net.URL(urlServicio), null);
			wsEvent.setHeader(sh);
			
			EventType[] scheduleRequest= new EventType[1];
			
			scheduleRequest[0] = new EventType();
			if (params.getEndDate()!=null){
				scheduleRequest[0].setEndDate(UtilDate.StringToDate(UtilDate.DateToString(params.getEndDate(),"yyyy-MM-dd"),"yyyy-MM-dd"));
				//scheduleRequest[0].setEndTime(new Time(UtilDate.DateToString(params.getEndDate(),"HH:mm:ss")));
			}
			
			NormalizedString eventDefinitionOID = new NormalizedString(params.getEventDefinitionOID());
			scheduleRequest[0].setEventDefinitionOID(eventDefinitionOID);
			
			NormalizedString location = new NormalizedString(params.getLocation());
			scheduleRequest[0].setLocation(location);
			
			if (params.getStartDate()!=null){
				scheduleRequest[0].setStartDate(UtilDate.StringToDate(UtilDate.DateToString(params.getStartDate(),"yyyy-MM-dd"),"yyyy-MM-dd"));			
				//scheduleRequest[0].setStartTime(new Time(UtilDate.DateToString(params.getStartDate(),"HH:mm:ss")));
			}
			
			StudyRefType studyRef = new StudyRefType();
			
			NormalizedString identifier = new NormalizedString(params.getIdentifier());
			studyRef.setIdentifier(identifier);
			
			/*SiteRefType siteRef = new SiteRefType();
			NormalizedString identifier2 = new NormalizedString(params.getSiteidentifier());
			siteRef.setIdentifier(identifier2);			
			studyRef.setSiteRef(siteRef);*/
			scheduleRequest[0].setStudyRef(studyRef);
			
			StudySubjectRefType studySubjectRef = new StudySubjectRefType();
			NormalizedString label = new NormalizedString(params.getLabel());
			studySubjectRef.setLabel(label);
			scheduleRequest[0].setStudySubjectRef(studySubjectRef);
			
			ScheduleResponse response =	wsEvent.schedule(scheduleRequest);

			logger.info("respuesta event schedule result: "+response.getResult());
			
			if (response.getResult().toUpperCase().equalsIgnoreCase("SUCCESS")){
				resultado.setOk(true);
				resultado.setMensaje(response.getStudyEventOrdinal());
				resultado.setObjeto(response);
			}else {
				resultado.setOk(false);
				resultado.setMensaje(response.getResult());
				resultado.setObjeto(response);
				for(String error: response.getError()){
					logger.info("Detalle Error: "+error);
				}
			}
			
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			resultado.setOk(false);
			resultado.setMensaje(e.getMessage());
			e.printStackTrace();
			logger.error("AxisFault :: Error consumir servico Event",e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			resultado.setOk(false);
			resultado.setMensaje(e.getMessage());
			e.printStackTrace();
			logger.error("RemoteException :: Error consumir servico Event",e);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			resultado.setOk(false);
			resultado.setMensaje(e.getMessage());
			e.printStackTrace();
			logger.error("MalformedURLException :: Error consumir servico Event",e);
		} catch (Exception e){
			resultado.setOk(false);
			resultado.setMensaje(e.getMessage());
			e.printStackTrace();
			logger.error("Exception :: Error consumir servico Event",e);
		}
		logger.info("consumirEventCliente()  :: fin");
		return resultado;
	}
	
	/**
	 * Método para consumir el web service Data de OpenClinica
	 * @param params Objeto con todos los parámetros que recibe el servicio
	 * @return {@link InfoResultado} Indicando si el llamado fue exitoso o no, y el objeto que respuesta del servicio
	 */
	public InfoResultado consumirDataCliente(String xmlImportar) {
		logger.info("consumirDataCliente()  :: inicio");
		
		InfoResultado resultado = new InfoResultado();
		
		try {
			String urlServicio = config.getString("data.import.urlService");
			logger.info("data.import.urlService :: "+urlServicio);
			
			PrefixedQName name = new PrefixedQName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security","wsse");
			
			org.apache.axis.message.SOAPHeaderElement sh = new org.apache.axis.message.SOAPHeaderElement(name);
			sh.setMustUnderstand(true);
			sh.setActor(null);
			
			SOAPElement userNameToken = sh.addChildElement("UsernameToken","wsse");
			userNameToken.setAttribute("wsu:Id", config.getString("openClinica.security.usernameToken.id"));
			userNameToken.setAttribute("xmlns:wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");			

			String clntUserName = config.getString("openClinica.security.user");
			String clntPassword = config.getString("openClinica.security.password");
			
			SOAPElement UNElement = userNameToken.addChildElement("Username","wsse");
			UNElement.addTextNode(clntUserName);

			SOAPElement PwdElement = userNameToken.addChildElement("Password","wsse");
			PwdElement.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
			PwdElement.addTextNode(clntPassword);
			
			org.openclinica.ws.data.v1.WsSoap11Stub wsData = new org.openclinica.ws.data.v1.WsSoap11Stub(new java.net.URL(urlServicio), null);
			wsData.setHeader(sh);
			
			org.openclinica.ws.data.v1.ImportResponse response = wsData._import(xmlImportar);
			 
			logger.info("respuesta data import: "+response.getResult());
			
			if (response.getResult().toUpperCase().equalsIgnoreCase("SUCCESS")){
				resultado.setOk(true);
				resultado.setObjeto(response);
			}else {
				resultado.setOk(false);
				resultado.setMensaje(response.getResult());
				resultado.setObjeto(response);
				for(String error: response.getError()){
					logger.info("Detalle Error: "+error);
				}
			}
			
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			resultado.setOk(false);
			resultado.setMensaje(e.getMessage());
			e.printStackTrace();
			logger.error("AxisFault :: Error consumir servico Data",e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			resultado.setOk(false);
			resultado.setMensaje(e.getMessage());
			e.printStackTrace();
			logger.error("RemoteException :: Error consumir servico Data",e);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			resultado.setOk(false);
			resultado.setMensaje(e.getMessage());
			e.printStackTrace();
			logger.error("MalformedURLException :: Error consumir servico Data",e);
		} catch (Exception e){
			e.printStackTrace();
			resultado.setOk(false);
			resultado.setMensaje(e.getMessage());
			logger.error("Exception :: Error consumir servico Data",e);
		}
		logger.info("consumirDataCliente()  :: fin");
		return resultado;
	}	

	/**
	 * Método para consumir el web service Data de OpenClinica creando manualmente la petición SOAP
	 * @param hoja que se va a ingresar data
	 * @param sec secuencial que indica el indice actual del registro en la lista de hojas pendientes
	 * @param repeatKey devuelto por el webservice Event
	 * @return InfoResultado con el resultado de la petición
	 */
	public InfoResultado consumirDataClienteV2(HojaConsulta hoja, int sec, String repeatKey) {
		logger.info("consumirDataCliente()  :: inicio");
		
		InfoResultado resultado = new InfoResultado();
		
		try {
			String urlServicio = config.getString("data.import.urlService");
			logger.info("data.import.urlService :: "+urlServicio);
			
			// Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(hoja,sec, repeatKey), urlServicio);

			 // Process the SOAP Response
            resultado = printSOAPResponse(soapResponse);
           
            soapConnection.close();
			
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			resultado.setOk(false);
			resultado.setMensaje(e.getMessage());
			e.printStackTrace();
			logger.error("AxisFault :: Error consumir servico Data",e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			resultado.setOk(false);
			resultado.setMensaje(e.getMessage());
			e.printStackTrace();
			logger.error("RemoteException :: Error consumir servico Data",e);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			resultado.setOk(false);
			resultado.setMensaje(e.getMessage());
			e.printStackTrace();
			logger.error("MalformedURLException :: Error consumir servico Data",e);
		} catch (Exception e){
			e.printStackTrace();
			resultado.setOk(false);
			resultado.setMensaje(e.getMessage());
			logger.error("Exception :: Error consumir servico Data",e);
		}
		logger.info("consumirDataCliente()  :: fin");
		return resultado;
	}	

	private SOAPMessage createSOAPRequest(HojaConsulta hoja, int sec, String repeatKey) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        
        String serverURI = config.getString("data.import.serverURI");

        Date dFechaHoy = new Date();
		//String studyEventRepeatKey = config.getString("data.import.studyEventRepeatKey");
		String itemGroupRepeatKey = config.getString("data.import.itemGroupRepeatKey");
		String fileOID = String.valueOf(sec)+"D"+UtilDate.DateToString(dFechaHoy, "yyyyMMddHHmmss"); //1D20140223130400;
		logger.debug("fileOID :: "+fileOID);
		String formOID = config.getString("data.import.form.OID"); //F_HOJADECONSUL_23;
		String subjectKey = config.getString("data.import.subjectKey")+String.valueOf(hoja.getCodExpediente());
		logger.debug("formOID :: "+formOID);
        
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("v1", serverURI);
        
        /*****************HEADER**********************/
        //QName name = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security","wsse");
        Name nameh = soapFactory.createName("Security","wsse","http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        SOAPHeader soapHeader = soapMessage.getSOAPHeader();
        
        SOAPHeaderElement headerElement = soapHeader.addHeaderElement(nameh);
        headerElement.setMustUnderstand(true);
        headerElement.setActor(null);
       
        
		SOAPElement userNameToken = headerElement.addChildElement("UsernameToken","wsse");
		userNameToken.setAttribute("wsu:Id", config.getString("openClinica.security.usernameToken.id"));
		userNameToken.setAttribute("xmlns:wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");			

		String clntUserName = config.getString("openClinica.security.user");
		String clntPassword = config.getString("openClinica.security.password");
		
		SOAPElement UNElement = userNameToken.addChildElement("Username","wsse");
		UNElement.addTextNode(clntUserName);

		SOAPElement PwdElement = userNameToken.addChildElement("Password","wsse");
		PwdElement.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
		PwdElement.addTextNode(clntPassword);
        /*****************BODY*****************/
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("importRequest", "v1");
        Name name1 = soapFactory.createName("ODM");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement(name1);
        
        //<ClinicalData StudyOID="S_S_1" MetaDataVersionOID="v1.3.0">
        Name name2 = soapFactory.createName("ClinicalData");
        SOAPElement soapBodyElem2 = soapBodyElem1.addChildElement(name2);
        soapBodyElem2.setAttribute("StudyOID", config.getString("study.OID"));
        soapBodyElem2.setAttribute("MetaDataVersionOID", "v1.3.0");
        
        //<SubjectData SubjectKey="SS_8413">
        Name name3 = soapFactory.createName("SubjectData");
        SOAPElement soapBodyElem3 = soapBodyElem2.addChildElement(name3);
        soapBodyElem3.setAttribute("SubjectKey", subjectKey);
        
        //<StudyEventData StudyEventOID="SE_CONSULTACS" StudyEventRepeatKey="1">        
        Name name4 = soapFactory.createName("StudyEventData");
        SOAPElement soapBodyElem4 = soapBodyElem3.addChildElement(name4);
        soapBodyElem4.setAttribute("StudyEventOID", config.getString("data.import.eventDefinitionOID"));
        soapBodyElem4.setAttribute("StudyEventRepeatKey", repeatKey);
        
        //<FormData FormOID="F_HOJADECONSUL_23">
        Name name5 = soapFactory.createName("FormData");
        SOAPElement soapBodyElem5 = soapBodyElem4.addChildElement(name5);
        soapBodyElem5.setAttribute("FormOID", formOID);
        
        //<ItemGroupData ItemGroupOID="IG_HOJAD_UNGROUPED" ItemGroupRepeatKey="1" TransactionType="Insert">
        Name name6 = soapFactory.createName("ItemGroupData");
        SOAPElement soapBodyElem6 = soapBodyElem5.addChildElement(name6);
        soapBodyElem6.setAttribute("ItemGroupOID", "IG_HOJAD_UNGROUPED");
        soapBodyElem6.setAttribute("ItemGroupRepeatKey", itemGroupRepeatKey);
        soapBodyElem6.setAttribute("TransactionType", "Insert");
        
        //<ItemData ItemOID="I_HOJAD_NUM" Value="4"/>
		Name name7 = soapFactory.createName("ItemData");
		
        Class objClass = hoja.getClass();
		
		Method[] methodos = objClass.getMethods();
		boolean estadoNutricional = false;
		for(Method metodo : methodos){
			String nombre = metodo.getName();
			String datosCrf = null;
			String value="";
			if (!nombre.toUpperCase().contains("CLASS") && (!nombre.toUpperCase().contains("ESTADO") || nombre.toUpperCase().contains("MALESTADO")) && nombre.contains("get")){
				Object val = metodo.invoke(hoja, null);
				if (val != null){
					nombre = nombre.replaceAll("get", "").toUpperCase();
					
					if (val instanceof BigDecimal)
						value = String.valueOf((BigDecimal)val);
					else if (val instanceof Short)
						value = String.valueOf((Short)val);
					else if (val instanceof Date)
						value = String.valueOf((Date)val);
					else if (val instanceof Character){ //se invierten SI y NO
						value = String.valueOf(val);
						if (!nombre.equals("SEGCHICK") && !nombre.equals("TURNO") 
								//20112018.Nose porque estaba en este filtro, pero por esto no cargaba correctamente a open
								//&& !nombre.equals("MALESTADO") && !nombre.equals("MANIFESTACIONHEMORRAGICA")
								){
							if (value.trim().equals("0"))
								value = "1";
							else if (value.trim().equals("1"))
								value = "0";
						}
					}
					else	
						value = val.toString();
					
					try{
						datosCrf = config.getString(nombre).toUpperCase();
					}catch(Exception ex){
						logger.error("no se encontró propertie para: "+nombre);
						datosCrf = null;
						
							//es uno de los estados nutricionales
							if (nombre.equals("OBESO") || nombre.equals("SOBREPESO") || nombre.equals("SOSPECHAPROBLEMA") || nombre.equals("NORMAL") || nombre.equals("BAJOPESO") || nombre.equals("BAJOPESOSEVERO")){
								logger.debug("entra estado nutricianal: "+nombre+"-"+value.trim());
								String valorEspecial = valorCasosEspeciales(nombre, value.trim());
								if (valorEspecial!=null){
									//si aún no se  agrega estado nutricional. se setea el primero que se encuentre como SI
									if (!estadoNutricional){
										String[] datosCrfCompletar = config.getString("ESTNUT").toUpperCase().trim().split(":");
										addSoapItem(name7, soapBodyElem6, datosCrfCompletar[0], valorEspecial.trim());
									}
									estadoNutricional = true;
								}
						}
					}
					if (datosCrf!=null && !datosCrf.isEmpty()){
						String[] datosCrfArray = datosCrf.trim().split(":");
						if (nombre.trim().equals("USUARIOENFERMERIA")){
							UsuariosView usuario = usuarioService.obtenerUsuarioById(Integer.valueOf(value.trim()));
							//si se encontró el usuario, y tiene código personal se agrega al ODM
							if (usuario!=null && usuario.getCodigoPersonal()!=null){
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], usuario.getCodigoPersonal());
							}
						}else if (nombre.trim().equals("USUARIOMEDICO")){
							UsuariosView usuario = usuarioService.obtenerUsuarioById(Integer.valueOf(value.trim()));
							//si se encontró el usuario, y tiene código personal se agrega al ODM
							if (usuario!=null && usuario.getCodigoPersonal()!=null){
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], usuario.getCodigoPersonal());
							}
						}else if (nombre.trim().equals("HORAULTDOSISANTIPIRETICO")){
							String ampm = hoja.getAmPmUltDosisAntipiretico();
							String soloHora = UtilDate.DateToString(hoja.getHoraUltDosisAntipiretico(),"hh:mm:ss");
							String hora = UtilDate.DateToString(UtilDate.StringToDate(UtilDate.DateToString(new Date(), "dd/MM/yyyy") + " " + soloHora + " " + ampm.toUpperCase() , "dd/MM/yyyy h:mm:ss a",Locale.US),"HH:mm");
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], hora.trim());
						}else if (nombre.trim().equals("FECHACONSULTA")){
							//sacar FSUPERVISOR,FENFERMERIA
							Date fecConsulta =hoja.getFechaConsulta();
							String fecha = UtilDate.DateToString(fecConsulta, "yyyy-MM-dd");
							
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], fecha);
							
							/*String[] datosCrfCompletar = config.getString("FSUPERVISOR").toUpperCase().trim().split(":");
							addSoapItem(name7, soapBodyElem6, datosCrfCompletar[0], fecha.trim());*/
							
							String[] datosCrfCompletar = config.getString("FENFERMERIA").toUpperCase().trim().split(":");
							addSoapItem(name7, soapBodyElem6, datosCrfCompletar[0], fecha.trim());
							
						//***************************************************************************
						}else if(nombre.trim().equals("FECHACIERRE")){
							Date fechaCierre = hoja.getFechaCierre();
							logger.error("FECHACIERRE");
							String fMedico = UtilDate.DateToString(fechaCierre, "yyyy-MM-dd");
							logger.error("valor FMEDICO: " + fMedico);
							String hMedico = UtilDate.DateToString(hoja.getFechaCierre(), "HH:mm");
							logger.error("valor HMEDICO: " + hMedico);
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], fMedico);
							addSoapItem(name7, soapBodyElem6, "HMEDICO", hMedico);
						//****************************************************************************
						} else if (nombre.trim().equals("EXPEDIENTEFISICO")) {
							logger.error("EXPEDIENTEFISICO");
							String expFisico = value.trim();
							logger.error("valor EXPEDIENTEFISICO: " + expFisico);
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], expFisico);
						//****************************************************************************
						} else if (nombre.trim().equals("COLEGIO")) {
							logger.error("COLEGIO");
							String colegio = value.trim();
							logger.error("valor COLEGIO: " + colegio);
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], colegio);
						//****************************************************************************	
						}else if (nombre.trim().equals("DIAGNOSTICO2") || nombre.trim().equals("DIAGNOSTICO3") || nombre.trim().equals("DIAGNOSTICO4")){ 
								if (!value.trim().equals("0")){
									logger.debug("entro diagnostico: "+nombre+" - "+value);
									addSoapItem(name7, soapBodyElem6, datosCrfArray[0], value.trim());
								}
						}/*else if (nombre.equals("OTROEXAMENLAB")){
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], value.trim());
							String[] datosCrfCompletar = config.getString("OTROEX").toUpperCase().trim().split(":");
							addSoapItem(name7, soapBodyElem6, datosCrfCompletar[0], "1");
						}*/else if (nombre.equals("HORA") || nombre.equals("HORASV")){
							String fecha = UtilDate.DateToString(new Date(), "yyyy-MM-dd");
							fecha = fecha + " " + value.trim();
							String hora24 = UtilDate.DateToString(UtilDate.StringToDate(fecha, "yyyy-MM-dd hh:mm aa"),"HH:mm");
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], hora24);
						}else if (nombre.equals("TELEF")) {
							if (value.trim().isEmpty() || value.trim().equalsIgnoreCase("0") || !(value.trim().length()==8)) {
								logger.debug("telefono no válido: "+nombre+" - "+value);
							}else {
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], value.trim());
							}
						}else{
							String valorEspecial = valorCasosEspeciales(nombre, value.trim());
							if (valorEspecial!=null){
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], valorEspecial.trim());
							}else{
									addSoapItem(name7, soapBodyElem6, datosCrfArray[0], value.trim());
							}
						}
					}
				}else{
					logger.debug("metodo valor null: "+nombre);
					nombre = nombre.replaceAll("get", "").toUpperCase();
					try{
						datosCrf = config.getString(nombre).toUpperCase();
					}catch(Exception ex){
						logger.error("valor null. no se encontró propertie para: "+nombre);
						datosCrf = null;
						
							//es uno de los estados nutricionales
							if (nombre.equals("OBESO") || nombre.equals("SOBREPESO") || nombre.equals("SOSPECHAPROBLEMA") || nombre.equals("NORMAL") || nombre.equals("BAJOPESO") || nombre.equals("BAJOPESOSEVERO")){
								logger.debug("entra estado nutricianal: "+nombre+"-"+value.trim());
								String valorEspecial = valorCasosEspeciales(nombre, value.trim());
								if (valorEspecial!=null){
									//si aún no se  agrega estado nutricional. se setea el primero que se encuentre como SI
									if (!estadoNutricional){
										String[] datosCrfCompletar = config.getString("ESTNUT").toUpperCase().trim().split(":");
										addSoapItem(name7, soapBodyElem6, datosCrfCompletar[0], valorEspecial.trim());
									}
									estadoNutricional = true;
								}
						}
					}
					if (datosCrf!=null && !datosCrf.isEmpty()){
						String[] datosCrfArray = datosCrf.split(":");
						/*if (nombre.equals("OTROEXAMENLAB")){
							String[] datosCrfCompletar = config.getString("OTROEX").toUpperCase().trim().split(":");
							//xmlImportar += "<ItemData ItemOID=\"I_HOJAD_"+datosCrfCompletar[0]+"\" Value=\"0\"/>";
							addSoapItem(name7, soapBodyElem6, datosCrfCompletar[0], "0");
						}else*/
						if (datosCrfArray[2].equals("1")){																
							String defecto = valorDefecto(nombre);
							if (defecto!=null){
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], defecto.trim());
							}
						}
					}
				}
			}										
		}
        
		MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI+"import");

        soapMessage.saveChanges();
        
        System.out.print("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        soapMessage.writeTo(baos);
        logger.debug("Request SOAP Message = ");
        logger.debug(baos.toString());
        
        return soapMessage;
	}
	
	/**
     * Method used to print the SOAP Response
     */
    private InfoResultado printSOAPResponse(SOAPMessage soapResponse) throws Exception {
    	InfoResultado resultado = new InfoResultado();
    	TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        System.out.print("Response SOAP Message = ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        soapResponse.writeTo(baos);
        logger.debug("Response SOAP Message = ");
        logger.debug(baos.toString());
        String strResultado = soapResponse.getSOAPBody().getElementsByTagName("result").item(0).getFirstChild().getNodeValue();
        logger.debug("Importa Data response = "+strResultado);
        System.out.println("Importa Data response = "+strResultado);
        if (strResultado.toUpperCase().equalsIgnoreCase("SUCCESS")){
			resultado.setOk(true);
			resultado.setObjeto(baos);
		}else {
			resultado.setOk(false);
			String strError = soapResponse.getSOAPBody().getElementsByTagName("error").item(0).getFirstChild().getNodeValue();
			resultado.setMensaje(strError);
			resultado.setObjeto(baos);
		}
        return resultado;
    }
    
	private void addSoapItem(Name name, SOAPElement soapBodyElem, String nombreItem, String valorItem){
		SOAPElement soapBodyElem7;
		try {
			soapBodyElem7 = soapBodyElem.addChildElement(name);
	        soapBodyElem7.setAttribute("ItemOID", "I_HOJAD_"+nombreItem);
	        soapBodyElem7.setAttribute("Value", valorItem);
		} catch (SOAPException e) {
			logger.error(e);
			e.printStackTrace();
		}

	}
	
	private String valorCasosEspeciales(String nombreCampo, String valorCampo){
		String valor=null;
		if (nombreCampo.equals("LUGARATENCION")){
			if (valorCampo.toUpperCase().equals("CS SFV"))
				return "1";
			else //Terreno
				return "2";
		}
		if (nombreCampo.equals("CONSULTA")){
			if (valorCampo.toUpperCase().equals("INICIAL"))
				return "1";
			else if (valorCampo.toUpperCase().equals("SEGUIMIENTO"))
				return "2";
			else //Convaleciente
				return "3";
		}
		if (nombreCampo.equals("AMPMULTDIAFIEBRE")){
			if (valorCampo.toUpperCase().equals("AM"))
				return "1";
			else //PM
				return "2";
		}
		if (nombreCampo.equals("HORARIOCLASES")){
			if (valorCampo.toUpperCase().equals("M"))
				return "1";
			else if (valorCampo.toUpperCase().equals("V"))
				return "2";
			else //“N” o “D” 
				return "3";
		}
		if (nombreCampo.equals("OBESO") && valorCampo.equals("1"))
			return "1";
		if (nombreCampo.equals("SOBREPESO") && valorCampo.equals("1"))
			return "2";
		if (nombreCampo.equals("SOSPECHAPROBLEMA") && valorCampo.equals("1"))
			return "3";
		if (nombreCampo.equals("NORMAL") && valorCampo.equals("1"))
			return "4";
		if (nombreCampo.equals("BAJOPESO") && valorCampo.equals("1"))
			return "5";
		if (nombreCampo.equals("BAJOPESOSEVERO") && valorCampo.equals("1"))
			return "6";
		if (nombreCampo.equals("OJOSHUNDIDOS")){
			if (valorCampo.trim().equals("0"))
				return "1";
			else if (valorCampo.trim().equals("1"))
				return "0";
		}
			
		return valor;
	}

	
	private String valorDefecto(String nombreCampo){
		String valor=null;
		if (nombreCampo.equals("OEL"))
			return "0";
		if (nombreCampo.equals("HEPATOMEGALIA"))
			return "0";
		if (nombreCampo.equals("OTROANTIBIOTICO"))
			return "0";
		if (nombreCampo.equals("MELENA"))
			return "3";
		if (nombreCampo.equals("HEMATEMESIS"))
			return "3";
		if (nombreCampo.equals("HIPERMENORREA"))
			return "3";
		if (nombreCampo.equals("CIANOSIS"))
			return "3";
		if (nombreCampo.equals("LLENADOCAPILAR2SEG"))
			return "3";
		if (nombreCampo.equals("PETEQUIASESPONTANEAS"))
			return "3";		
		if (nombreCampo.equals("EPISTAXIS"))
			return "3";
		if (nombreCampo.equals("GINGIVORRAGIA"))
			return "3";
		if (nombreCampo.equals("PALIDEZENEXTREMIDADES"))
			return "3";
		if (nombreCampo.equals("PIELEXTREMIDADESFRIAS"))
			return "3";
		if (nombreCampo.equals("PETEQUIA20PT"))
			return "3";
		if (nombreCampo.equals("PETEQUIA10PT"))
			return "3";
		if (nombreCampo.equals("PRUEBATORNIQUETEPOSITIVA"))
			return "3";
		if (nombreCampo.equals("MHEMORRAG"))
			return "3";
		if (nombreCampo.equals("HORA"))
			return "00:00";
		if (nombreCampo.equals("HORASV"))
			return "00:00";
		return valor;
	}
}
