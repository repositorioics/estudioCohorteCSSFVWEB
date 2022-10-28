package ni.com.sts.estudioCohorteCssfv.controller.hojaConsulta;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.configuration.CompositeConfiguration;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Include;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import ni.com.sts.estudioCohorteCssfv.datos.hojaConsulta.HojaConsultaDA;
import ni.com.sts.estudioCohorteCssfv.dto.ExpedienteDTO;
import ni.com.sts.estudioCohorteCssfv.dto.OrdenesExamenes;
import ni.com.sts.estudioCohorteCssfv.servicios.HojaConsultaService;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilProperty;

public class ExpedienteController extends SelectorComposer<Component> {

	private static HojaConsultaService hojaConsultaService = new HojaConsultaDA();

	private static CompositeConfiguration config;

	@Wire("[id$=txtCodExpediente]")
	private Intbox txtCodExpediente;

	@Wire("[id$=listaHojaConsulta]")
	private Listbox listaHojaConsulta;

	@Wire("[id$=btnBuscar]")
	private Button btnBuscar;

	public void retornaLista(int codigoExpediente) {
		try {

			this.listaHojaConsulta.setModel(new ListModelList<ExpedienteDTO>(new ArrayList<ExpedienteDTO>()));

			List<ExpedienteDTO> resultado = this.hojaConsultaService.listaExpedienteHojaConsulta(codigoExpediente);
			if (resultado.size() > 0) {
				this.listaHojaConsulta.setModel(new ListModelList<ExpedienteDTO>(resultado));
			} else {
				Mensajes.enviarMensaje(Mensajes.REGISTRO_NO_ENCONTRADO, Mensajes.TipoMensaje.EXCLAM);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS, Mensajes.TipoMensaje.ERROR);
		}
	}

	@Listen("onClick=[id$=btnBuscar]")
	public void btnBuscar_onClick() {
		buscar();
	}

	/**
	 * Menejador del evento OK(Enter) del campo "Código"
	 */
	@Listen("onOK=[id$=txtCodExpediente]")
	public void txtCodExpediente_onOk() {
		if (this.txtCodExpediente.getValue() != null) {
			retornaLista(this.txtCodExpediente.getValue());
		} else {
			Mensajes.enviarMensaje("Debe ingresar el código", Mensajes.TipoMensaje.ERROR);
		}
	}

	private void buscar() {
		if (this.txtCodExpediente.getValue() != null) {
			retornaLista(this.txtCodExpediente.getValue());
		} else {
			Mensajes.enviarMensaje("Debe ingresar el código", Mensajes.TipoMensaje.ERROR);
		}
	}

	/**
	 * Método para Imprimir el Pdf
	 * 
	 * @param evt
	 */
	@Listen("onImprimirPdf=[id$=listaHojaConsulta]")
	public void onClickImprimirPdf(ForwardEvent evt) {
		try {
			// Inicializar objeto a editar
			ExpedienteDTO expediente = new ExpedienteDTO();
			// Obtener objeto seleccionado
			Toolbarbutton button = (Toolbarbutton) evt.getOrigin().getTarget();
			if (button != null) {
				Listitem listitem = (Listitem) button.getParent().getParent();
				if (listitem != null) {
					expediente = (ExpedienteDTO) listitem.getValue();
				}
			}
			// Validar que se seleccione el objeto de la lista
			if (expediente != null && expediente.getSecHojaConsulta() > 0) {
				if (Messagebox.show("Desea imprimir la hoja de consulta?", "Validación", Messagebox.YES | Messagebox.NO,
						Messagebox.QUESTION) == Messagebox.NO) {
					return;
				} else {
					config = UtilProperty.getConfigurationfromExternalFile("MovilWS.properties");

					SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
					SOAPConnection soapConnection = soapConnectionFactory.createConnection();

					// Generar la solicitud SOAP XML

					MessageFactory messageFactory = MessageFactory.newInstance();
					SOAPMessage soapMessage = messageFactory.createMessage();
					MimeHeaders header = soapMessage.getMimeHeaders();
					header.setHeader("SOAPAction",
							"http://webservice.estudiocohortecssfv.sts_ni.com/EstudioCohorteCSSFVMovilWSService/reimpresionHojaConsulta");
					SOAPPart soapPart = soapMessage.getSOAPPart();
					SOAPEnvelope envelope = soapPart.getEnvelope();
					envelope.addNamespaceDeclaration("web", "http://webservice.estudiocohortecssfv.sts_ni.com/");
					SOAPBody soapBody = envelope.getBody();
					SOAPElement soapBodyElem = soapBody.addChildElement("reimpresionHojaConsulta", "web");
					SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("paramsecHojaConsulta");
					SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("paramImpresora");
					soapBodyElem1.addTextNode(String.valueOf(expediente.getSecHojaConsulta()));
					soapBodyElem2.addTextNode("0");
					soapMessage.saveChanges();
					soapMessage.writeTo(System.out);

					// Llamando al servicio web
					String url = config.getString("CSSFV.URLWS");

					SOAPMessage soapResponse = soapConnection.call(soapMessage, url);
					Source sourceContent = soapResponse.getSOAPPart().getContent();

					// Imprimiendo la respuesta del SOAP
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					System.out.println("Response SOAP Message \n");
					StreamResult result = new StreamResult(System.out);
					transformer.transform(sourceContent, result);
					soapConnection.close();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS, Mensajes.TipoMensaje.ERROR);
		}
	}

	/**
	 * Método para ver el Pdf
	 * 
	 * @param evt
	 */
	@Listen("onVerPdf=[id$=listaHojaConsulta]")
	public void onClickVerPdf(ForwardEvent evt) {
		try {
			// Inicializar objeto a editar
			ExpedienteDTO expediente = new ExpedienteDTO();
			// Obtener objeto seleccionado
			Toolbarbutton button = (Toolbarbutton) evt.getOrigin().getTarget();
			if (button != null) {
				Listitem listitem = (Listitem) button.getParent().getParent();
				if (listitem != null) {
					expediente = (ExpedienteDTO) listitem.getValue();
				}
			}
			// Validar que se seleccione el objeto de la lista
			if (expediente == null || expediente.getSecHojaConsulta() <= 0) {
				Messagebox.show("La fila seleccionada no contiene información. Favor verifique",
						Mensajes.REGISTRO_NO_SELECCIONADO, new Messagebox.Button[] { Messagebox.Button.OK },
						Messagebox.EXCLAMATION, null);
				return;
			} else {
				config = UtilProperty.getConfiguration("EstudioCohorteCssfvWEBExt.properties",
						"ni/com/sts/estudioCohorteCssfv/properties/EstudioCohorteCssfvWEBInt.properties");
				String nombre = "HojaConsulta_" + expediente.getSecHojaConsulta();
				String path = System.getProperty("jboss.server.data.dir")
						+ System.getProperty("file.separator").charAt(0) + config.getString("ruta.pdf")
						+ (nombre.contains(".pdf") ? nombre : nombre + ".pdf");
				path = path.replace('/', System.getProperty("file.separator").charAt(0));

				File myFile = new File(path);
				URL url = myFile.toURI().toURL();

				byte[] ba = new byte[(int) myFile.length()];
				if (ba.length > 0) {
					Window win = (Window) Executions.createComponents("/visorReportes/hojaConsultaReporte.zul", null,
							null);
					win.setTitle("Hoja Consulta");

					int baLength;
					InputStream is = null;
					ByteArrayOutputStream bios = new ByteArrayOutputStream();
					URLConnection urlConn = url.openConnection();
					// Comprobando si la URL contiene un PDF
					if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
						Mensajes.enviarMensaje("El archivo no es PDF", Mensajes.TipoMensaje.ERROR);
					} else {
						// Se lee el PDF de la URL y se guarda en un archivo local
						is = url.openStream();
						while ((baLength = is.read(ba)) != -1) {
							bios.write(ba, 0, baLength);
						}
						final AMedia amedia = new AMedia(nombre, "pdf", "application/pdf", bios.toByteArray());
						Iframe iframe = (Iframe) win.getFellow("iframeHojaConsulta");
						iframe.setContent(amedia);
					}
				} else {
					Mensajes.enviarMensaje("No existe el archivo PDF", Mensajes.TipoMensaje.ERROR);
					return;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS, Mensajes.TipoMensaje.ERROR);
		}
	}
}
