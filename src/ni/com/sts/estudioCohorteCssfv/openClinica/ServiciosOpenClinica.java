package ni.com.sts.estudioCohorteCssfv.openClinica;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaInfluenza;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaZika;
import ni.com.sts.estudioCohorteCSSFV.modelo.SeguimientoInfluenza;
import ni.com.sts.estudioCohorteCSSFV.modelo.SeguimientoZika;
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
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Método para consumir el web service Data de OpenClinica creando manualmente la petición SOAP
	 * @param hojaInfluenza que se va a ingresar data
	 * @return InfoResultado con el resultado de la petición
	 */
	public InfoResultado consumirDataHojaInfluenza(HojaInfluenza hojaInfluenza, List<SeguimientoInfluenza> seguimientoInfluenzas, int sec, String repeatKey) {

		logger.info("consumirDataCliente()  :: inicio");
		
		InfoResultado resultado = new InfoResultado();
		
		try {
			String urlServicio = config.getString("data.import.urlService");
			logger.info("data.import.urlService :: "+urlServicio);
			
			// Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequestHojaInfluenza(hojaInfluenza, seguimientoInfluenzas, sec, repeatKey), urlServicio);

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
	
	private SOAPMessage createSOAPRequestHojaInfluenza(HojaInfluenza hojaInfluenza, List<SeguimientoInfluenza> seguimientoInfluenza, int sec, String repeatKey) throws Exception {
		
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
		String formOID = config.getString("data.import.form.OID.HI"); //F_SEGUIMIENTOP_33;
		String subjectKey = config.getString("data.import.subjectKey")+String.valueOf(hojaInfluenza.getCodExpediente());
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
        soapBodyElem4.setAttribute("StudyEventOID", config.getString("data.import.eventDefinitionOID.HI"));
        if (hojaInfluenza.getRepeatKey() == null) {
        	soapBodyElem4.setAttribute("StudyEventRepeatKey", repeatKey);
        } else {
        	soapBodyElem4.setAttribute("StudyEventRepeatKey", hojaInfluenza.getRepeatKey());
        }
        
        
        //<FormData FormOID="F_HOJADECONSUL_23">
        Name name5 = soapFactory.createName("FormData");
        SOAPElement soapBodyElem5 = soapBodyElem4.addChildElement(name5);
        soapBodyElem5.setAttribute("FormOID", formOID);
        
        //<ItemGroupData ItemGroupOID="IG_HOJAD_UNGROUPED" ItemGroupRepeatKey="1" TransactionType="Insert">
        Name name6 = soapFactory.createName("ItemGroupData");
        SOAPElement soapBodyElem6 = soapBodyElem5.addChildElement(name6);
        soapBodyElem6.setAttribute("ItemGroupOID", config.getString("data.import.itemGroupOID.HI"));
        soapBodyElem6.setAttribute("ItemGroupRepeatKey", itemGroupRepeatKey);
        if (hojaInfluenza.getRepeatKey() == null) {
        	soapBodyElem6.setAttribute("TransactionType", "Insert");
        } else {
        	soapBodyElem6.setAttribute("TransactionType", "Update");
        }
        
        
        //<ItemData ItemOID="I_HOJAD_NUM" Value="4"/>
		Name name7 = soapFactory.createName("ItemData");
		
		Class objClass = hojaInfluenza.getClass();
		
		Method[] methodos = objClass.getMethods();
		
		for(Method metodo : methodos) {
			String nombre = metodo.getName();
			String datosCrf = null;
			String value="";
			
			if (!nombre.toUpperCase().contains("CLASS") && nombre.contains("get")) {
				Object val = metodo.invoke(hojaInfluenza, null);
				if (val != null) {
					nombre = nombre.replaceAll("get", "").toUpperCase();
					if (val instanceof BigDecimal)
						value = String.valueOf((BigDecimal)val);
					else if (val instanceof Short)
						value = String.valueOf((Short)val);
					else if (val instanceof Date)
						value = String.valueOf((Date)val);
					else if (val instanceof Character) { // Se asingnan los valores
						value = String.valueOf(val);
						if (value.trim().equals("N"))
							value = "0";
						else if (value.trim().equals("S"))
							value = "1";
						else if (value.trim().equals("D"))
							value = "2";
						else if (value.trim().equals("NA"))
							value = "3";
					} 
					else	
						value = val.toString();
						logger.error("HI"+" "+nombre+" "+ value);
					try{
						datosCrf = config.getString(nombre+"HI").toUpperCase();
					} catch(Exception ex) {
						logger.error("no se encontró propertie para: "+nombre);
						datosCrf = null;
					}
					
					if (datosCrf!=null && !datosCrf.isEmpty()) { 
						String[] datosCrfArray = datosCrf.trim().split(":");
						//NUMERO DE HOJA SEGUIMIENTO
						/*if (nombre.trim().equals("NUMHOJASEGUIMIENTO")) {
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], value.trim(), 2);
						}*/
						//FIS
						if (nombre.trim().equals("FIF")) {
							//if (value.trim() != null) {
							if (value != null && value.trim() != null && !value.trim().isEmpty()) {
								DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
								Date dateFif = format.parse(value.trim());
								String fif = UtilDate.DateToString(dateFif, "yyyy-MM-dd");
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], fif, 2);
							} else {
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], "", 2);
							}
						}
						//FIF
						else if (nombre.trim().equals("FIS")) {
							//if (value.trim() != null) {
							if (value != null && value.trim() != null && !value.trim().isEmpty()) {
								DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
								Date dateFis = format.parse(value.trim());
								String fis = UtilDate.DateToString(dateFis, "yyyy-MM-dd");
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], fis, 2);
							} else {
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], "", 2);
							}
						} else {
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], value.trim(), 2);
						}
					}
				} else {
					logger.debug("metodo valor null: "+nombre);
					nombre = nombre.replaceAll("get", "").toUpperCase();
					try{
						datosCrf = config.getString(nombre+"HI").toUpperCase();
					} catch(Exception ex){
						logger.error("valor null. no se encontró propertie para: "+nombre);
						datosCrf = null;
					}
				}
			}
		}
		//HCE
		addSoapItem(name7, soapBodyElem6, config.getString("HCEHI"), "1", 2); //Desarrollo
		/*addSoapItem(name7, soapBodyElem6, "HOJACONSULTA", "1", 2);*/ //Producion 
		
		for(SeguimientoInfluenza segInfluenza:seguimientoInfluenza) {
			//<ItemGroupData ItemGroupOID="IG_SEGUI_SINTOMAS" ItemGroupRepeatKey="1" TransactionType="Insert">
	        Name name8 = soapFactory.createName("ItemGroupData");
	        SOAPElement soapBodyElem8 = soapBodyElem5.addChildElement(name8);
	        soapBodyElem8.setAttribute("ItemGroupOID", config.getString("data.import.itemGroupOID.SEG.HI"));
	        soapBodyElem8.setAttribute("ItemGroupRepeatKey", String.valueOf(segInfluenza.getControlDia())); // dia
	        soapBodyElem8.setAttribute("TransactionType", "Insert");
	        			
			Class objClassSeg = segInfluenza.getClass();
			
			Method[] methodosSeg = objClassSeg.getMethods();
			for(Method metodo : methodosSeg) {
				String nombre = metodo.getName();
				String datosCrf = null;
				String value="";
				
				if (!nombre.toUpperCase().contains("CLASS") && nombre.contains("get")) {
					Object val = metodo.invoke(segInfluenza, null);
					if (val != null) {
						nombre = nombre.replaceAll("get", "").toUpperCase();
						if (val instanceof BigDecimal)
							value = String.valueOf((BigDecimal)val);
						else if (val instanceof Short)
							value = String.valueOf((Short)val);
						else if (val instanceof Date)
							value = String.valueOf((Date)val);
						else if (val instanceof Character) { // Se asingnan los valores
							value = String.valueOf(val);
							if (value.trim().equals("N"))
								value = "0";
							else if (value.trim().equals("S"))
								value = "1";
							else if (value.trim().equals("D"))
								value = "2";
							else if (value.trim().equals("NA"))
								value = "3";
						} 
						else	
							value = val.toString();
						if (value.trim().equals("N"))
							value = "0";
						else if (value.trim().equals("S"))
							value = "1";
						else if (value.trim().equals("D"))
							value = "2";
						else if (value.trim().equals("NA"))
							value = "3";
							logger.error("SEG-HI"+" "+ nombre+" "+ value);
						try{
							datosCrf = config.getString(nombre+"HI").toUpperCase();
						} catch(Exception ex) {
							logger.error("no se encontró propertie para: "+nombre);
							datosCrf = null;
						}
						
						if (datosCrf!=null && !datosCrf.isEmpty()){ 
							String[] datosCrfArray = datosCrf.trim().split(":");
							//USUARIOMEDICO
							if (nombre.trim().equals("USUARIOMEDICO")) {
								UsuariosView usuario = usuarioService.obtenerUsuarioById(Integer.valueOf(value.trim()));
								//si se encontró el usuario, y tiene código personal se agrega al ODM
								if (usuario!=null && usuario.getCodigoPersonal()!=null){
									addSoapItem(name7, soapBodyElem8, datosCrfArray[0], usuario.getCodigoPersonal(), 2);
								}
								if (hojaInfluenza.getSupervisor() != null) {
									String supervisor = usuarioService.obtenerCodigoPersonalById(Integer.valueOf(hojaInfluenza.getSupervisor()));
									addSoapItem(name7, soapBodyElem8, "SUPERVISOR", supervisor, 2);
								} else {
									addSoapItem(name7, soapBodyElem8, "SUPERVISOR", "", 2);
								} 
								
							}
							//FECHASEGUIMIENTO
							else if(nombre.trim().equals("FECHASEGUIMIENTO")) {
								Date fechaSeguimiento = segInfluenza.getFechaSeguimiento();
								String fecha = UtilDate.DateToString(fechaSeguimiento, "yyyy-MM-dd");
								addSoapItem(name7, soapBodyElem8, datosCrfArray[0], fecha.trim(), 2);
							} 
							//INTESIDAD FIEBRE
							else if (nombre.trim().equals("FIEBRELEVE") || nombre.trim().equals("FIEBREMODERADA") || 
									nombre.trim().equals("FIEBRESEVERA")) {
								logger.debug("entra intensidad fiebre: "+nombre+"-"+value.trim());
								String intensidadFiebre = intensidadSintomas(nombre, value.trim());
								if (intensidadFiebre != null) {
									addSoapItem(name7, soapBodyElem8, datosCrfArray[0], intensidadFiebre, 2);	
								}
							}
							//INTESIDAD TOS
							else if (nombre.trim().equals("TOSLEVE") || nombre.trim().equals("TOSMODERADA") ||
									nombre.trim().equals("TOSSEVERA")) {
								logger.debug("entra intensidad tos: "+nombre+"-"+value.trim());
								String intensidadTos = intensidadSintomas(nombre, value.trim());
								if (intensidadTos != null) {
									addSoapItem(name7, soapBodyElem8, datosCrfArray[0], intensidadTos, 2);	
								}
							}
							//INTESIDAD SECRECION NASAL
							else if (nombre.trim().equals("SECRECIONNASALLEVE") || nombre.trim().equals("SECRECIONNASALMODERADA") ||
									nombre.trim().equals("SECRECIONNASALSEVERA")) {
								logger.debug("entra intensidad secrecion nasal: "+nombre+"-"+value.trim());
								String intensidadSecrecionNasal = intensidadSintomas(nombre, value.trim());
								if (intensidadSecrecionNasal != null) {
									addSoapItem(name7, soapBodyElem8, datosCrfArray[0], intensidadSecrecionNasal, 2);
								}
							}
							//INTESIDAD DOLOR GARGANTA
							else if (nombre.trim().equals("DOLORGARGANTALEVE") || nombre.trim().equals("DOLORGARGANTAMODERADA") ||
									nombre.trim().equals("DOLORGARGANTASEVERA")) {
								logger.debug("entra intensidad dolor de garganta: "+nombre+"-"+value.trim());
								String intensidadDolorGarganta = intensidadSintomas(nombre, value.trim());
								if (intensidadDolorGarganta != null) {
									addSoapItem(name7, soapBodyElem8, datosCrfArray[0], intensidadDolorGarganta, 2);	
								}
							}
							//INTESIDAD DOLOR CABEZA
							else if (nombre.trim().equals("DOLORCABEZALEVE") || nombre.trim().equals("DOLORCABEZAMODERADA") ||
									nombre.trim().equals("DOLORCABEZASEVERA")) {
								logger.debug("entra intensidad dolor de cabeza: "+nombre+"-"+value.trim());
								String intensidadDolorCabeza = intensidadSintomas(nombre, value.trim());
								if (intensidadDolorCabeza != null) {
									addSoapItem(name7, soapBodyElem8, datosCrfArray[0], intensidadDolorCabeza, 2);	
								}
							}
							//INTESIDAD DOLOR MUSCULAR
							else if (nombre.trim().equals("DOLORMUSCULARLEVE") || nombre.trim().equals("DOLORMUSCULARMODERADA") ||
									nombre.trim().equals("DOLORMUSCULARSEVERA")) {
								logger.debug("entra intensidad dolor muscular: "+nombre+"-"+value.trim());
								String intensidadDolorMuscular = intensidadSintomas(nombre, value.trim());
								if (intensidadDolorMuscular != null) {
									addSoapItem(name7, soapBodyElem8, datosCrfArray[0], intensidadDolorMuscular, 2);
								}
							}
							//INTESIDAD DOLOR ARTICULAR
							else if (nombre.trim().equals("DOLORARTICULARLEVE") || nombre.trim().equals("DOLORARTICULARMODERADA") ||
									nombre.trim().equals("DOLORARTICULARSEVERA")) {
								logger.debug("entra intensidad dolor articular: "+nombre+"-"+value.trim());
								String intensidadDolorArticular = intensidadSintomas(nombre, value.trim());
								if (intensidadDolorArticular != null) {
									addSoapItem(name7, soapBodyElem8, datosCrfArray[0], intensidadDolorArticular, 2);	
								}
							}
							else {
								addSoapItem(name7, soapBodyElem8, datosCrfArray[0], value.trim(), 2);
							}
						}
					} else {
						logger.debug("metodo valor null: "+nombre);
						nombre = nombre.replaceAll("get", "").toUpperCase();
						try{
							datosCrf = config.getString(nombre+"HI").toUpperCase();
						} catch(Exception ex){
							logger.error("valor null. no se encontró propertie para: "+nombre);
							datosCrf = null;
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
	
	//--------------------------------------------------------------------------------------------------------------------
	/**
	 * Método para consumir el web service Data de OpenClinica creando manualmente la petición SOAP
	 * @return InfoResultado con el resultado de la petición
	 */
	public InfoResultado consumirDataHojaZika(HojaZika hojaZika, List<SeguimientoZika> seguimientoZikas, int sec, String repeatKey) {

		logger.info("consumirDataCliente()  :: inicio");
		
		InfoResultado resultado = new InfoResultado();
		
		try {
			String urlServicio = config.getString("data.import.urlService");
			logger.info("data.import.urlService :: "+urlServicio);
			
			// Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequestHojaZika(hojaZika, seguimientoZikas, sec, repeatKey), urlServicio);

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
	
	private SOAPMessage createSOAPRequestHojaZika(HojaZika hojaZika, List<SeguimientoZika> seguimientoZika, int sec, String repeatKey) throws Exception {
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
		String formOID = config.getString("data.import.form.OID.ZK"); //F_SEGUIMIENTOP;
		String subjectKey = config.getString("data.import.subjectKey")+String.valueOf(hojaZika.getCodExpediente());
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
        soapBodyElem4.setAttribute("StudyEventOID", config.getString("data.import.eventDefinitionOID.ZK"));
        if (hojaZika.getRepeatKey() == null) {
        	soapBodyElem4.setAttribute("StudyEventRepeatKey", repeatKey);
        } else {
        	soapBodyElem4.setAttribute("StudyEventRepeatKey", hojaZika.getRepeatKey());
        }
        
        
        //<FormData FormOID="F_HOJADECONSUL_23">
        Name name5 = soapFactory.createName("FormData");
        SOAPElement soapBodyElem5 = soapBodyElem4.addChildElement(name5);
        soapBodyElem5.setAttribute("FormOID", formOID);
        
        //<ItemGroupData ItemGroupOID="IG_HOJAD_UNGROUPED" ItemGroupRepeatKey="1" TransactionType="Insert">
        Name name6 = soapFactory.createName("ItemGroupData");
        SOAPElement soapBodyElem6 = soapBodyElem5.addChildElement(name6);
        soapBodyElem6.setAttribute("ItemGroupOID", config.getString("data.import.itemGroupOID.ZK")); //Prod "IG_SEGUI_UNGROUPED_3790"
        soapBodyElem6.setAttribute("ItemGroupRepeatKey", itemGroupRepeatKey);
        if (hojaZika.getRepeatKey() == null) {
        	soapBodyElem6.setAttribute("TransactionType", "Insert");
        } else {
        	soapBodyElem6.setAttribute("TransactionType", "Update");
        }
        
        //<ItemData ItemOID="I_HOJAD_NUM" Value="4"/>
		Name name7 = soapFactory.createName("ItemData");
		
		Class objClass = hojaZika.getClass();
		
		Method[] methodos = objClass.getMethods();
		
		for(Method metodo : methodos) {
			String nombre = metodo.getName();
			String datosCrf = null;
			String value="";
			
			if (!nombre.toUpperCase().contains("CLASS") && nombre.contains("get")) {
				Object val = metodo.invoke(hojaZika, null);
				if (val != null) {
					nombre = nombre.replaceAll("get", "").toUpperCase();
					if (val instanceof BigDecimal)
						value = String.valueOf((BigDecimal)val);
					else if (val instanceof Short)
						value = String.valueOf((Short)val);
					else if (val instanceof Date)
						value = String.valueOf((Date)val);
					else if (val instanceof Character) { // Se asingnan los valores
						value = String.valueOf(val);
						if (value.trim().equals("N"))
							value = "0";
						else if (value.trim().equals("S"))
							value = "1";
						else if (value.trim().equals("D"))
							value = "2";
						else if (value.trim().equals("NA"))
							value = "3";
					} 
					else	
						value = val.toString();
						logger.error("ZK"+" "+nombre+" "+ value);
					try{
						datosCrf = config.getString(nombre+"ZK").toUpperCase();
					} catch(Exception ex) {
						logger.error("no se encontró propertie para: "+nombre);
						datosCrf = null;
					}
					
					if (datosCrf!=null && !datosCrf.isEmpty()) {
						String[] datosCrfArray = datosCrf.trim().split(":");
						if (nombre.trim().equals("FIF")) {
							String ff = "";
							//if (value.trim() != null) {
							if (value != null && value.trim() != null && !value.trim().isEmpty()) {
								DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
								Date dateFif = format.parse(value.trim());
								String fif = UtilDate.DateToString(dateFif, "yyyy-MM-dd");
								ff = fif;
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], fif, 2);
							} else {
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], ff, 2);
							}
						}
						//FIF
						else if (nombre.trim().equals("FIS")) {
							String fs = "";
							//if (value.trim() != null) {
							if (value != null && value.trim() != null && !value.trim().isEmpty()) {
								DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
								Date dateFis = format.parse(value.trim());
								String fis = UtilDate.DateToString(dateFis, "yyyy-MM-dd");
								fs = fis;
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], fis, 2); // Prod FIF_7212
							} else {
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], fs, 2);
							}
						
						}				
						// SINTOMAINICIAL1
						else if (nombre.trim().equals("SINTOMAINICIAL1")) {
							if (value.trim() != null) {
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], SetValorSintomasZika(value.trim()), 2);
							} else {
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], "", 2);
							}
						}
						// SINTOMAINICIAL2
						else if (nombre.trim().equals("SINTOMAINICIAL2")) {
							if (value.trim() != null) {
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], SetValorSintomasZika(value.trim()), 2);
							} else {
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], "", 2);
							}
						}
						// SINTOMAINICIAL3
						else if (nombre.trim().equals("SINTOMAINICIAL3")) {
							if (value.trim() != null) {
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], SetValorSintomasZika(value.trim()), 2);
							} else {
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], "", 2);
							}
							
						}
						// SINTOMAINICIAL4
						else if (nombre.trim().equals("SINTOMAINICIAL4")) {
							if (value.trim() != null) {
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], SetValorSintomasZika(value.trim()), 2);
							} else {
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], "", 2);
							}
						} else {
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], value.trim(), 2);
						}
					}
				} else {
					logger.debug("metodo valor null: "+nombre);
					nombre = nombre.replaceAll("get", "").toUpperCase();
					try{
						datosCrf = config.getString(nombre+"ZK").toUpperCase();
					} catch(Exception ex){
						logger.error("valor null. no se encontró propertie para: "+nombre);
						datosCrf = null;
					}
				}
			}
		}
		//HCE
		addSoapItem(name7, soapBodyElem6, config.getString("HCEZK"), "1", 2);
		
		for(SeguimientoZika segZika:seguimientoZika) {
			//<ItemGroupData ItemGroupOID="IG_SEGUI_SINTOMAS" ItemGroupRepeatKey="1" TransactionType="Insert">
	        Name name8 = soapFactory.createName("ItemGroupData");
	        SOAPElement soapBodyElem8 = soapBodyElem5.addChildElement(name8);
	        soapBodyElem8.setAttribute("ItemGroupOID", config.getString("data.import.itemGroupOID.SEG.ZK")); // Prod "IG_SEGUI_SINTOMAS_4930"
	        soapBodyElem8.setAttribute("ItemGroupRepeatKey", String.valueOf(segZika.getControlDia())); // dia
	        soapBodyElem8.setAttribute("TransactionType", "Insert");
	        			
			Class objClassSeg = segZika.getClass();
			
			Method[] methodosSeg = objClassSeg.getMethods();
			for(Method metodo : methodosSeg) {
				String nombre = metodo.getName();
				String datosCrf = null;
				String value="";
				
				if (!nombre.toUpperCase().contains("CLASS") && nombre.contains("get")) {
					Object val = metodo.invoke(segZika, null);
					if (val != null) {
						nombre = nombre.replaceAll("get", "").toUpperCase();
						if (val instanceof BigDecimal)
							value = String.valueOf((BigDecimal)val);
						else if (val instanceof Short)
							value = String.valueOf((Short)val);
						else if (val instanceof Date)
							value = String.valueOf((Date)val);
						else if (val instanceof Character) { // Se asingnan los valores
							value = String.valueOf(val);
							if (value.trim().equals("N"))
								value = "0";
							else if (value.trim().equals("S"))
								value = "1";
							else if (value.trim().equals("D"))
								value = "2";
							else if (value.trim().equals("NA"))
								value = "3";
						} 
						else	
							value = val.toString();
						if (value.trim().equals("N"))
							value = "0";
						else if (value.trim().equals("S"))
							value = "1";
						else if (value.trim().equals("D"))
							value = "2";
						else if (value.trim().equals("NA"))
							value = "3";
							logger.error("SEG-ZK"+" "+ nombre+" "+ value);
						try{
							datosCrf = config.getString(nombre+"ZK").toUpperCase();
						} catch(Exception ex) {
							logger.error("no se encontró propertie para: "+nombre);
							datosCrf = null;
						}
						
						if (datosCrf!=null && !datosCrf.isEmpty()){ 
							String[] datosCrfArray = datosCrf.trim().split(":");
							//USUARIOMEDICO
							if (nombre.trim().equals("USUARIOMEDICO")) {
								UsuariosView usuario = usuarioService.obtenerUsuarioById(Integer.valueOf(value.trim()));
								//si se encontró el usuario, y tiene código personal se agrega al ODM
								if (usuario!=null && usuario.getCodigoPersonal()!=null){
									addSoapItem(name7, soapBodyElem8, datosCrfArray[0], usuario.getCodigoPersonal(), 2);
								}
								if (hojaZika.getSupervisor() != null) {
									String supervisor = usuarioService.obtenerCodigoPersonalById(Integer.valueOf(hojaZika.getSupervisor()));
									addSoapItem(name7, soapBodyElem8, "SUPERVISOR_7422", supervisor, 2);
								} else {
									addSoapItem(name7, soapBodyElem8, "SUPERVISOR_7422", "", 2);
								}
							}
							//FECHASEGUIMIENTO
							else if(nombre.trim().equals("FECHASEGUIMIENTO")) {
								Date fechaSeguimiento = segZika.getFechaSeguimiento();
								String fecha = UtilDate.DateToString(fechaSeguimiento, "yyyy-MM-dd");
								addSoapItem(name7, soapBodyElem8, datosCrfArray[0], fecha.trim(), 2); //Prod FECHA_2128
							} 
							//SUPERVISOR
							/*else if (nombre.trim().equals("SUPERVISOR")) {
								if (hojaZika.getSupervisor() != null) {
									String supervisor = usuarioService.obtenerCodigoPersonalById(Integer.valueOf(hojaZika.getSupervisor()));
									addSoapItem(name7, soapBodyElem8, datosCrfArray[0], supervisor, 2);
								} else {
									addSoapItem(name7, soapBodyElem8, datosCrfArray[0], "", 2);
								}
								
							}*/
							else {
								addSoapItem(name7, soapBodyElem8, datosCrfArray[0], value.trim(), 2);
							}
						}
					} else {
						logger.debug("metodo valor null: "+nombre);
						nombre = nombre.replaceAll("get", "").toUpperCase();
						try{
							datosCrf = config.getString(nombre+"ZK").toUpperCase();
						} catch(Exception ex){
							logger.error("valor null. no se encontró propertie para: "+nombre);
							datosCrf = null;
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

	//-------------------------------------------------------------------------------------------------------
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
        //soapBodyElem4.setAttribute("StudyEventRepeatKey", repeatKey);
        if (hoja.getRepeatKey() == null) {
        	soapBodyElem4.setAttribute("StudyEventRepeatKey", repeatKey);
        } else {
        	soapBodyElem4.setAttribute("StudyEventRepeatKey", hoja.getRepeatKey());
        }
        
        //<FormData FormOID="F_HOJADECONSUL_23">
        Name name5 = soapFactory.createName("FormData");
        SOAPElement soapBodyElem5 = soapBodyElem4.addChildElement(name5);
        soapBodyElem5.setAttribute("FormOID", formOID);
        
        //<ItemGroupData ItemGroupOID="IG_HOJAD_UNGROUPED" ItemGroupRepeatKey="1" TransactionType="Insert">
        Name name6 = soapFactory.createName("ItemGroupData");
        SOAPElement soapBodyElem6 = soapBodyElem5.addChildElement(name6);
        soapBodyElem6.setAttribute("ItemGroupOID", "IG_HOJAD_UNGROUPED");
        soapBodyElem6.setAttribute("ItemGroupRepeatKey", itemGroupRepeatKey);
        //soapBodyElem6.setAttribute("TransactionType", "Insert");
        if (hoja.getRepeatKey() == null) {
        	soapBodyElem6.setAttribute("TransactionType", "Insert");
        } else {
        	soapBodyElem6.setAttribute("TransactionType", "Update");
        }
        
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
										addSoapItem(name7, soapBodyElem6, datosCrfCompletar[0], valorEspecial.trim(), 1);
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
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], usuario.getCodigoPersonal(), 1);
							}
						}else if (nombre.trim().equals("USUARIOMEDICO")){
							UsuariosView usuario = usuarioService.obtenerUsuarioById(Integer.valueOf(value.trim()));
							//si se encontró el usuario, y tiene código personal se agrega al ODM
							if (usuario!=null && usuario.getCodigoPersonal()!=null){
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], usuario.getCodigoPersonal(), 1);
							}
						}else if(nombre.trim().equals("SUPERVISOR")) {
							if (hoja.getSupervisor() != null) {
								UsuariosView usuario = usuarioService.obtenerUsuarioById(Integer.valueOf(hoja.getSupervisor()));
								if (usuario!=null && usuario.getCodigoPersonal()!=null){
									addSoapItem(name7, soapBodyElem6, datosCrfArray[0], usuario.getCodigoPersonal(), 1);
								}
							}
						}else if (nombre.trim().equals("HORAULTDOSISANTIPIRETICO")){
							String ampm = hoja.getAmPmUltDosisAntipiretico();
							if (ampm != null && !ampm.trim().equals("")) {
								String ampmResult1 = ampm.replaceAll(" ", "");
								String ampmResult = ampmResult1.replaceAll("\\.", "");
								//String ampmResult2 = ampmResult.replaceAll(" ","");
								if (ampmResult.trim().equals("p m")) {
									ampmResult = "pm";
								}
								if (ampmResult.trim().equals("a m")) {
									ampmResult = "am";
								}
								String soloHora = UtilDate.DateToString(hoja.getHoraUltDosisAntipiretico(),"hh:mm:ss");
								//String hora = UtilDate.DateToString(UtilDate.StringToDate(UtilDate.DateToString(new Date(), "dd/MM/yyyy") + " " + soloHora + " " + ampm.toUpperCase() , "dd/MM/yyyy h:mm:ss a",Locale.US),"HH:mm");
								String hora = UtilDate.DateToString(UtilDate.StringToDate(UtilDate.DateToString(new Date(), "dd/MM/yyyy") + " " + soloHora + " " + ampmResult.toUpperCase().trim() , "dd/MM/yyyy h:mm:ss a",Locale.US),"HH:mm");
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], hora.trim(), 1);
							}
							
						}else if (nombre.trim().equals("FECHACONSULTA")){
							//sacar FSUPERVISOR,FENFERMERIA
							Date fecConsulta =hoja.getFechaConsulta();
							String fecha = UtilDate.DateToString(fecConsulta, "yyyy-MM-dd");
							
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], fecha, 1);
							
							/*String[] datosCrfCompletar = config.getString("FSUPERVISOR").toUpperCase().trim().split(":");
							addSoapItem(name7, soapBodyElem6, datosCrfCompletar[0], fecha.trim());*/
							
							String[] datosCrfCompletar = config.getString("FENFERMERIA").toUpperCase().trim().split(":");
							addSoapItem(name7, soapBodyElem6, datosCrfCompletar[0], fecha.trim(), 1);
							
						//***************************************************************************
						}else if(nombre.trim().equals("FECHACIERRE")){
							Date fechaCierre = hoja.getFechaCierre();
							logger.error("FECHACIERRE");
							String fMedico = UtilDate.DateToString(fechaCierre, "yyyy-MM-dd");
							logger.error("valor FMEDICO: " + fMedico);
							String hMedico = UtilDate.DateToString(hoja.getFechaCierre(), "HH:mm");
							logger.error("valor HMEDICO: " + hMedico);
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], fMedico, 1);
							addSoapItem(name7, soapBodyElem6, "HMEDICO", hMedico, 1);
							addSoapItem(name7, soapBodyElem6, "FSUPERVISOR", fMedico, 1);
						//****************************************************************************
						} else if (nombre.trim().equals("EXPEDIENTEFISICO")) {
							logger.error("EXPEDIENTEFISICO");
							String expFisico = value.trim();
							logger.error("valor EXPEDIENTEFISICO: " + expFisico);
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], expFisico, 1);
						//****************************************************************************
						} else if (nombre.trim().equals("COLEGIO")) {
							logger.error("COLEGIO");
							String colegio = value.trim();
							logger.error("valor COLEGIO: " + colegio);
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], colegio, 1);
						//****************************************************************************	
						}else if (nombre.trim().equals("DIAGNOSTICO2") || nombre.trim().equals("DIAGNOSTICO3") || nombre.trim().equals("DIAGNOSTICO4")){ 
								if (!value.trim().equals("0")){
									logger.debug("entro diagnostico: "+nombre+" - "+value);
									addSoapItem(name7, soapBodyElem6, datosCrfArray[0], value.trim(), 1);
								}
						}/*else if (nombre.equals("OTROEXAMENLAB")){
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], value.trim());
							String[] datosCrfCompletar = config.getString("OTROEX").toUpperCase().trim().split(":");
							addSoapItem(name7, soapBodyElem6, datosCrfCompletar[0], "1");
						}*/else if (nombre.equals("HORA") || nombre.equals("HORASV")){
							String fecha = UtilDate.DateToString(new Date(), "yyyy-MM-dd");
							fecha = fecha + " " + value.trim();
							String hora24 = UtilDate.DateToString(UtilDate.StringToDate(fecha, "yyyy-MM-dd hh:mm aa"),"HH:mm");
							addSoapItem(name7, soapBodyElem6, datosCrfArray[0], hora24, 1);
						}else if (nombre.equals("TELEF")) {
							if (value.trim().isEmpty() || value.trim().equalsIgnoreCase("0") || !(value.trim().length()==8)) {
								logger.debug("telefono no válido: "+nombre+" - "+value);
							}else {
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], value.trim(), 1);
							}
						}else{
							String valorEspecial = valorCasosEspeciales(nombre, value.trim());
							if (valorEspecial!=null){
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], valorEspecial.trim(), 1);
							}else{
									addSoapItem(name7, soapBodyElem6, datosCrfArray[0], value.trim(), 1);
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
										addSoapItem(name7, soapBodyElem6, datosCrfCompletar[0], valorEspecial.trim(), 1);
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
								addSoapItem(name7, soapBodyElem6, datosCrfArray[0], defecto.trim(), 1);
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
    
	private void addSoapItem(Name name, SOAPElement soapBodyElem, String nombreItem, String valorItem, int valor){
		SOAPElement soapBodyElem7;
		try {
			soapBodyElem7 = soapBodyElem.addChildElement(name);
			if (valor == 1) {
				soapBodyElem7.setAttribute("ItemOID", "I_HOJAD_"+nombreItem);
			} else if (valor == 2) {
				soapBodyElem7.setAttribute("ItemOID", "I_SEGUI_"+nombreItem);
			} /*else {
				soapBodyElem7.setAttribute("ItemOID", "I_HOJAD_"+nombreItem);
			}*/
	        
	        soapBodyElem7.setAttribute("Value", valorItem);
		} catch (SOAPException e) {
			logger.error(e);
			e.printStackTrace();
		}

	}
	
	private String SetValorSintomasZika(String valorCampo) {
		String valor = "";
		if (valorCampo.equals("Adenopatia Cervical Anterior")) {
			return "1";
		} 
		if (valorCampo.equals("Adenopatia Cervical Posterior")) {
			return "2";
		} 
		if (valorCampo.equals("Adenopatia RetroAuricular")) {
			return "3";
		} 
		if (valorCampo.equals("Artralgia Distal")) {
			return "4";
		} 
		if (valorCampo.equals("Artralgia Proximal")) {
			return "5";
		} 
		if (valorCampo.equals("Astenia")) {
			return "6";
		} 
		if (valorCampo.equals("Cefalea")) {
			return "7";
		} 
		if (valorCampo.equals("Conjuntivitis no purulenta")) {
			return "8";
		} 
		if (valorCampo.equals("Convulsiones")) {
			return "9";
		} 
		if (valorCampo.equals("Debilidad Muscular en MI")) {
			return "10";
		} 
		if (valorCampo.equals("Debilidad Muscular en MS")) {
			return "11";
		} 
		if (valorCampo.equals("Diarrea")) {
			return "12";
		} 
		if (valorCampo.equals("Dificultad Respiratoria")) {
			return "13";
		} 
		if (valorCampo.equals("Dolor Abdominal Continuo")) {
			return "14";
		} 
		if (valorCampo.equals("Dolor Garganta")) {
			return "15";
		} 
		if (valorCampo.equals("Dolor retroocular")) {
			return "16";
		} 
		if (valorCampo.equals("Edema articular Distal en MI")) {
			return "17";
		} 
		if (valorCampo.equals("Edema articular Distal en MS")) {
			return "18";
		}
		if (valorCampo.equals("Edema articular Proximal en MI")) {
			return "19";
		}
		if (valorCampo.equals("Edema articular Proximal en MS")) {
			return "20";
		} 
		if (valorCampo.equals("Edema Periauricular")) {
			return "21";
		} 
		if (valorCampo.equals("Epistaxis")) {
			return "22";
		} 
		if (valorCampo.equals("Equimosis")) {
			return "23";
		} 
		if (valorCampo.equals("Escalosfrios")) {
			return "24";
		} 
		if (valorCampo.equals("Fiebre")) {
			return "25";
		} 
		if (valorCampo.equals("Gingivorragia")) {
			return "26";
		} 
		if (valorCampo.equals("Hematemesis")) {
			return "27";
		} 
		if (valorCampo.equals("Mal estado general")) {
			return "28";
		}
		if (valorCampo.equals("Melena")) {
			return "29";
		} 
		if (valorCampo.equals("Mialgia")) {
			return "30";
		} 
		if (valorCampo.equals("Nauseas")) {
			return "31";
		} 
		if (valorCampo.equals("Oftalmoplejia")) {
			return "32";
		} 
		if (valorCampo.equals("Paralisis Muscular MI")) {
			return "33";
		} 
		if (valorCampo.equals("Paralisis Muscular MS")) {
			return "34";
		} 
		if (valorCampo.equals("Parestesia MI")) {
			return "35";
		} 
		if (valorCampo.equals("Parestesia MS")) {
			return "36";
		} 
		if (valorCampo.equals("Petequias espontaneas")) {
			return "37";
		} 
		if (valorCampo.equals("Poco apetito")) {
			return "38";
		} 
		if (valorCampo.equals("Prueba de torniquete Positiva")) {
			return "39";
		} 
		if (valorCampo.equals("Prurito")) {
			return "40";
		} 
		if (valorCampo.equals("Rash")) {
			return "41";
		} 
		if (valorCampo.equals("Rigidez de cuello")) {
			return "42";
		} 
		if (valorCampo.equals("Rinorrea")) {
			return "43";
		}
		if (valorCampo.equals("Tos")) {
			return "44";
		} 
		if (valorCampo.equals("Vomitos")) {
			return "45";
		}
		if (valorCampo.equals("Fotofobia")) {
			return "46";
		} 
		if (valorCampo.equals("Mareos")) {
			return "47";
		} 
		if (valorCampo.equals("Sudoracion")) {
			return "48";
		} 
		return valor;
	}
		
	private String valorCasosEspeciales(String nombreCampo, String valorCampo) {
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
	
	private String intensidadSintomas(String nombreCampo, String valorCampo) {
		String valor = null;
		if (nombreCampo.equals("FIEBRELEVE") && valorCampo.equals("1"))
			return "1";
		if (nombreCampo.equals("FIEBREMODERADA") && valorCampo.equals("1"))
			return "2";
		if (nombreCampo.equals("FIEBRESEVERA") && valorCampo.equals("1"))
			return "3";
		if (nombreCampo.equals("TOSLEVE") && valorCampo.equals("1"))
			return "1";
		if (nombreCampo.equals("TOSMODERADA") && valorCampo.equals("1"))
			return "2";
		if (nombreCampo.equals("TOSSEVERA") && valorCampo.equals("1"))
			return "3";
		if (nombreCampo.equals("SECRECIONNASALLEVE") && valorCampo.equals("1"))
			return "1";
		if (nombreCampo.equals("SECRECIONNASALMODERADA") && valorCampo.equals("1"))
			return "2";
		if (nombreCampo.equals("SECRECIONNASALSEVERA") && valorCampo.equals("1"))
			return "3";
		if (nombreCampo.equals("DOLORGARGANTALEVE") && valorCampo.equals("1"))
			return "1";
		if (nombreCampo.equals("DOLORGARGANTAMODERADA") && valorCampo.equals("1"))
			return "2";
		if (nombreCampo.equals("DOLORGARGANTASEVERA") && valorCampo.equals("1"))
			return "3";
		if (nombreCampo.equals("DOLORCABEZALEVE") && valorCampo.equals("1"))
			return "1";
		if (nombreCampo.equals("DOLORCABEZAMODERADA") && valorCampo.equals("1"))
			return "2";
		if (nombreCampo.equals("DOLORCABEZASEVERA") && valorCampo.equals("1"))
			return "3";
		if (nombreCampo.equals("DOLORMUSCULARLEVE") && valorCampo.equals("1"))
			return "1";
		if (nombreCampo.equals("DOLORMUSCULARMODERADA") && valorCampo.equals("1"))
			return "2";
		if (nombreCampo.equals("DOLORMUSCULARSEVERA") && valorCampo.equals("1"))
			return "3";
		if (nombreCampo.equals("DOLORARTICULARLEVE") && valorCampo.equals("1"))
			return "1";
		if (nombreCampo.equals("DOLORARTICULARMODERADA") && valorCampo.equals("1"))
			return "2";
		if (nombreCampo.equals("DOLORARTICULARSEVERA") && valorCampo.equals("1"))
			return "3";
		return valor;
	}
}
