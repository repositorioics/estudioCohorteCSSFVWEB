package ni.com.sts.estudioCohorteCssfv.controller.laboratorio;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.EghResultados;
import ni.com.sts.estudioCohorteCSSFV.modelo.EgoResultados;
import ni.com.sts.estudioCohorteCSSFV.modelo.MalariaResultados;
import ni.com.sts.estudioCohorteCSSFV.modelo.OrdenLaboratorio;
import ni.com.sts.estudioCohorteCSSFV.modelo.Paciente;
import ni.com.sts.estudioCohorteCSSFV.modelo.PerifericoResultado;
import ni.com.sts.estudioCohorteCSSFV.modelo.UsuariosView;
import ni.com.sts.estudioCohorteCssfv.controller.visorReporte.VisorController;
import ni.com.sts.estudioCohorteCssfv.datos.laboratorio.LaboratorioDA;
import ni.com.sts.estudioCohorteCssfv.datos.paciente.PacienteDA;
import ni.com.sts.estudioCohorteCssfv.datos.usuario.UsuariosDA;
import ni.com.sts.estudioCohorteCssfv.dto.OrdenesExamenes;
import ni.com.sts.estudioCohorteCssfv.servicios.LaboratorioService;
import ni.com.sts.estudioCohorteCssfv.servicios.PacienteService;
import ni.com.sts.estudioCohorteCssfv.servicios.UsuariosService;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilDate;
import ni.com.sts.estudioCohorteCssfv.util.UtilProperty;
import ni.com.sts.estudioCohorteCssfv.util.Utilidades;

import org.apache.commons.configuration.CompositeConfiguration;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class IngresoResultadosController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	
	private static CompositeConfiguration config;

	private static LaboratorioService laboratorioService = new LaboratorioDA();
	
	private static UsuariosService usuariosService = new UsuariosDA();
	private static PacienteService pacienteService = new PacienteDA();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		init();
	}

	private void init() {
		cargarDatosCabezera();
		// Obtener el archivo properties.
        config = UtilProperty.getConfiguration("EstudioCohorteCssfvWEBExt.properties", "ni/com/sts/estudioCohorteCssfv/properties/EstudioCohorteCssfvWEBInt.properties");
	}

	// Componentes

	@Wire("[id$=txtCodigo]")
	private Textbox txtCodigo;
	
	@Wire("[id$=txtNombrePaciente]")
	private Textbox txtNombrePaciente;

	@Wire("[id$=txtEdad]")
	private Textbox txtEdad;

	@Wire("[id$=txtSexo]")
	private Textbox txtSexo;

	@Wire("[id$=lstIngresoResultados]")
	private Listbox lstIngresoResultados;

	@Listen("onClick=[id$=btnCerrar]")
    public void btnCerrar_onClick() {
		Include include = (Include) Selectors
				.iterable(this.getPage(), "[id$=mainInclude]")
				.iterator().next();
		include.setSrc("/laboratorio/ordenes_examenes.zul");
	}

	public void retornaLista(int codExpediente, int secHojaConsulta) {
		try {

			this.lstIngresoResultados
					.setModel(new ListModelList<OrdenesExamenes>(
							new ArrayList<OrdenesExamenes>()));

			List<OrdenesExamenes> resultado = this.laboratorioService
					.listaIngresoResultados(codExpediente, secHojaConsulta);
			if (resultado.size() > 0) {
				orderOrdenesExamenes(resultado);
				this.lstIngresoResultados
						.setModel(new ListModelList<OrdenesExamenes>(resultado));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS,
					Mensajes.TipoMensaje.ERROR);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void orderOrdenesExamenes(List<OrdenesExamenes> resultado) {

	    Collections.sort(resultado, new Comparator() {

	        public int compare(Object o1, Object o2) {

	            String x1 = ((OrdenesExamenes) o1).getEstado();
	            String x2 = ((OrdenesExamenes) o2).getEstado();
	            int sComp = x1.compareTo(x2);

	            if (sComp != 0) {
	               return sComp*-1; // para que sea descendente por estado
	            } else {
	               Calendar y1 = ((OrdenesExamenes) o1).getFechaOrdenLaboratorio();
	               Calendar y2 = ((OrdenesExamenes) o2).getFechaOrdenLaboratorio();
	               return y1.compareTo(y2);
	            }
	    }});
	}

	/**
	 * Método para editar grupos
	 *
	 * @param evt
	 */
	@Listen("onEditarIngresarResultado=[id$=lstIngresoResultados]")
	public void onClickEditar(ForwardEvent evt) {

		// Inicializar objeto a editar
		OrdenesExamenes ingresoResultadoSeleccionado = new OrdenesExamenes();

		try {
			// Obtener objeto seleccionado
			Toolbarbutton button = (Toolbarbutton) evt.getOrigin().getTarget();
			if (button != null) {
				Listitem listitem = (Listitem) button.getParent().getParent();
				if (listitem != null) {
					ingresoResultadoSeleccionado = (OrdenesExamenes) listitem
							.getValue();
				}
			}

			// Validar que se seleccione el objeto de la lista
			if (ingresoResultadoSeleccionado == null
					|| ingresoResultadoSeleccionado.getSecOrdenLaboratorio() <= 0) {
				Messagebox
						.show("La fila seleccionada no contiene información del Ingreso de Resultados. Favor verifique",
								Mensajes.REGISTRO_NO_SELECCIONADO,
								new Messagebox.Button[] { Messagebox.Button.OK },
								Messagebox.EXCLAMATION, null);
				return;
			}else if (ingresoResultadoSeleccionado.getEstado().equalsIgnoreCase("Cancelado")){
				Messagebox
				.show("Examen se encuentra cancelado, no se permite ingresar resultado",
						"Examen Cancelado",
						new Messagebox.Button[] { Messagebox.Button.OK },
						Messagebox.EXCLAMATION, null);
				return;
			} else {
				if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().compareTo("EGO") == 0) {

					Window winPopEGOResultado = (Window) Executions
							.createComponents(
									"laboratorio/examen_general_orina_ego.zul",
									null, null);
					winPopEGOResultado.setAttribute("ordenesExamenes",
							ingresoResultadoSeleccionado);

					winPopEGOResultado.doModal();
					if ((Boolean)winPopEGOResultado.getAttribute("accionGuardado")
							|| (winPopEGOResultado.getAttribute("accionTomaMx")!=null && (Boolean)winPopEGOResultado.getAttribute("accionTomaMx"))){
						cargarDatosCabezera();
					}


				} else if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().contains("EGH")) {
					Window winPopEGHResultado = (Window) Executions
							.createComponents(
									"laboratorio/examen_general_heces_egh.zul",
									null, null);
					winPopEGHResultado.setAttribute("ordenesExamenes",
							ingresoResultadoSeleccionado);

					winPopEGHResultado.doModal();
					if ((Boolean)winPopEGHResultado.getAttribute("accionGuardado")
							|| (winPopEGHResultado.getAttribute("accionTomaMx")!=null && (Boolean)winPopEGHResultado.getAttribute("accionTomaMx"))){
						cargarDatosCabezera();
					}

				} else if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().contains("CITOLOGIA FECAL")) {
					Window winPopEGHResultado = (Window) Executions
							.createComponents(
									"laboratorio/citologia_fecal.zul",
									null, null);
					winPopEGHResultado.setAttribute("ordenesExamenes",
							ingresoResultadoSeleccionado);

					winPopEGHResultado.doModal();
					if ((Boolean)winPopEGHResultado.getAttribute("accionGuardado")
							|| (winPopEGHResultado.getAttribute("accionTomaMx")!=null && (Boolean)winPopEGHResultado.getAttribute("accionTomaMx"))){
						cargarDatosCabezera();
					}

				} else if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().compareTo("GOTA GRUESA") == 0) {
					Window winPopMSPGotaGResultado = (Window) Executions
							.createComponents(
									"laboratorio/msp_gota_gruesa_r_malaria.zul",
									null, null);
					winPopMSPGotaGResultado.setAttribute("ordenesExamenes",
							ingresoResultadoSeleccionado);

					winPopMSPGotaGResultado.doModal();
					if ((Boolean)winPopMSPGotaGResultado.getAttribute("accionGuardado")
							|| (winPopMSPGotaGResultado.getAttribute("accionTomaMx")!=null && (Boolean)winPopMSPGotaGResultado.getAttribute("accionTomaMx"))){
						cargarDatosCabezera();
					}

				} else if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().compareTo("EXTENDIDO PERIFERICO") == 0) {
					Window winPopExtPerResultado = (Window) Executions
							.createComponents(
									"laboratorio/extendido_periferico.zul",
									null, null);
					winPopExtPerResultado.setAttribute("ordenesExamenes",
							ingresoResultadoSeleccionado);

					winPopExtPerResultado.doModal();
					if ((Boolean)winPopExtPerResultado.getAttribute("accionGuardado")
							|| (winPopExtPerResultado.getAttribute("accionTomaMx")!=null && (Boolean)winPopExtPerResultado.getAttribute("accionTomaMx"))){
						cargarDatosCabezera();
					}
				} else if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().compareTo("INFLUENZA") == 0) {
					Window winPopInfluenza = (Window) Executions
							.createComponents(
									"laboratorio/muestra_influenza.zul", null,
									null);
					winPopInfluenza.setAttribute("ordenesExamenes",
							ingresoResultadoSeleccionado);

					winPopInfluenza.doModal();
					if ((Boolean)winPopInfluenza.getAttribute("accionGuardado")
							|| (winPopInfluenza.getAttribute("accionTomaMx")!=null && (Boolean)winPopInfluenza.getAttribute("accionTomaMx"))){
						cargarDatosCabezera();
					}

				} else if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().compareTo("SEROLOGIA CHICK") == 0) {
					Window winPopSerologiaChick = (Window) Executions
							.createComponents(
									"laboratorio/muestra_serologia_chick.zul",
									null, null);
					winPopSerologiaChick.setAttribute("ordenesExamenes",
							ingresoResultadoSeleccionado);

					winPopSerologiaChick.doModal();
					if ((Boolean)winPopSerologiaChick.getAttribute("accionGuardado")
							|| (winPopSerologiaChick.getAttribute("accionTomaMx")!=null && (Boolean)winPopSerologiaChick.getAttribute("accionTomaMx"))){
						cargarDatosCabezera();
					}

				} else if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().compareTo("SEROLOGIA DENGUE") == 0) {
					Window winPopSerologiaDengue = (Window) Executions
							.createComponents(
									"laboratorio/muestra_serologia_dengue.zul",
									null, null);
					winPopSerologiaDengue.setAttribute("ordenesExamenes",
							ingresoResultadoSeleccionado);

					winPopSerologiaDengue.doModal();
					if ((Boolean)winPopSerologiaDengue.getAttribute("accionGuardado")
							|| (winPopSerologiaDengue.getAttribute("accionTomaMx")!=null && (Boolean)winPopSerologiaDengue.getAttribute("accionTomaMx"))){
						cargarDatosCabezera();
					}

				} /*else if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().compareTo("CITOLOGIA FECAL") == 0) {
					Window winPopEGHResultado = (Window) Executions
							.createComponents(
									"laboratorio/examen_general_heces_egh.zul",
									null, null);
					winPopEGHResultado.setAttribute("ordenesExamenes",
							ingresoResultadoSeleccionado);

					winPopEGHResultado.doModal();
					if ((boolean)winPopEGHResultado.getAttribute("accionGuardado")){
						cargarDatosCabezera();
					}
				}*/
				else {
					Window winPopExamenRestante = (Window) Executions
							.createComponents(
									"laboratorio/examenes_restantes.zul",
									null, null);
					winPopExamenRestante.setAttribute("ordenesExamenes",
							ingresoResultadoSeleccionado);

					winPopExamenRestante.doModal();
					if ((Boolean)winPopExamenRestante.getAttribute("accionGuardado")
							|| (winPopExamenRestante.getAttribute("accionTomaMx")!=null && (Boolean)winPopExamenRestante.getAttribute("accionTomaMx"))){
						cargarDatosCabezera();
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS,
					Mensajes.TipoMensaje.ERROR);
		}
	}

	private void cargarDatosCabezera() {
		try {
			Include include = (Include) Selectors
					.iterable(this.getPage(), "[id$=mainInclude]").iterator()
					.next();
			OrdenesExamenes ordenesExamenes = new OrdenesExamenes();
			ordenesExamenes = (OrdenesExamenes) include
					.getAttribute("ordenExamenSeleccionada");
			Paciente paciente = laboratorioService
					.obtenerInfoPaciente(ordenesExamenes.getCodigoExpediente());
			/*txtNombrePaciente.setText(paciente.getNombre1() + " "
					+ paciente.getNombre2() + " " + paciente.getApellido1()
					+ " " + paciente.getApellido2());*/
			txtCodigo.setText(String.valueOf(paciente.getCodExpediente()));
			txtNombrePaciente.setText(paciente.getNombre1() + " "
					+ ((paciente.getNombre2() != null) ? paciente.getNombre2() : "") + " "
					+ paciente.getApellido1() + " "
					+ ((paciente.getApellido2() != null) ? paciente.getApellido2() : ""));
			txtEdad.setText(laboratorioService.obtenerEdadCalculadaPaciente(ordenesExamenes.getCodigoExpediente()));
			txtSexo.setText(String.valueOf(paciente.getSexo()));
			retornaLista(ordenesExamenes.getCodigoExpediente(), ordenesExamenes.getSecHojaConsulta());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método para editar grupos
	 *
	 * @param evt
	 */
	@Listen("onPdfResultado=[id$=lstIngresoResultados]")
	public void onClickPdf(ForwardEvent evt) {

		// Inicializar objeto a editar
		OrdenesExamenes ingresoResultadoSeleccionado = new OrdenesExamenes();

		try {
			// Obtener objeto seleccionado
			Toolbarbutton button = (Toolbarbutton) evt.getOrigin().getTarget();
			if (button != null) {
				Listitem listitem = (Listitem) button.getParent().getParent();
				if (listitem != null) {
					ingresoResultadoSeleccionado = (OrdenesExamenes) listitem
							.getValue();
				}
			}

			// Validar que se seleccione el objeto de la lista
			if (ingresoResultadoSeleccionado == null
					|| ingresoResultadoSeleccionado.getSecOrdenLaboratorio() <= 0) {
				Messagebox
						.show("La fila seleccionada no contiene información del Ingreso de Resultados. Favor verifique",
								Mensajes.REGISTRO_NO_SELECCIONADO,
								new Messagebox.Button[] { Messagebox.Button.OK },
								Messagebox.EXCLAMATION, null);
				return;
			} else {
				if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().compareTo("EGO") == 0) {
					visualizarGeneralOrina(ingresoResultadoSeleccionado);

				} else if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().contains("EGH")) {
					visualizarGeneralHeces(ingresoResultadoSeleccionado);
					
				} else if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().contains("CITOLOGIA FECAL")) {
					visualizarGeneralHeces(ingresoResultadoSeleccionado);
					
				} else if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().compareTo("GOTA GRUESA") == 0) {
					visualizarGotaGruesa(ingresoResultadoSeleccionado);
					
				} else if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().compareTo("EXTENDIDO PERIFERICO") == 0) {
					visualizarExtendidoPeriferico(ingresoResultadoSeleccionado);

				} else if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().compareTo("INFLUENZA") == 0) {
					sinResultadoEnSistema();
					
				} else if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().compareTo("SEROLOGIA CHICK") == 0) {
					sinResultadoEnSistema();
					
				} else if (ingresoResultadoSeleccionado.getExamen().trim()
						.toUpperCase().compareTo("SEROLOGIA DENGUE") == 0) {
					sinResultadoEnSistema();
				}else {
					sinResultadoEnSistema();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_GENERAR_PDF,	Mensajes.TipoMensaje.ERROR);
		}
	}

	private void sinResultadoEnSistema(){
		Mensajes.enviarMensaje(Mensajes.NO_RESULTADOS_SISTEMA, Mensajes.TipoMensaje.INFO);
	}
	
	private void MensajeExamenPendiente(){
		Mensajes.enviarMensaje(Mensajes.EXAMEN_ESTADO_PENDIENTE, Mensajes.TipoMensaje.INFO);
	}
	
	private void MensajeExamenCancelado(){
		Mensajes.enviarMensaje(Mensajes.EXAMEN_ESTADO_CANCELADO, Mensajes.TipoMensaje.INFO);
	}
	
	private void visualizarExtendidoPeriferico(OrdenesExamenes ingresoResultadoSeleccionado) throws ParseException{
		
		if (ingresoResultadoSeleccionado.getEstado().equalsIgnoreCase("Pendiente")){
			MensajeExamenPendiente();
		}else if (ingresoResultadoSeleccionado.getEstado().equalsIgnoreCase("Cancelado")){
			MensajeExamenCancelado();
		}else{
		
			String sRutaJasper = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.extendido.periferico");
			String sRutaImgLogo = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.imagen.logo");
			
			PerifericoResultado resultado = laboratorioService.obtenerExtendidoPeriferico(ingresoResultadoSeleccionado.getSecOrdenLaboratorio());
			Paciente paciente = pacienteService.getPacienteById(ingresoResultadoSeleccionado.getCodigoExpediente());
			UsuariosView bioanalista = usuariosService.obtenerUsuarioById(resultado.getUsuarioBioanalista().intValue());
			UsuariosView medico = new UsuariosView();
			if (ingresoResultadoSeleccionado.getUsuarioMedico()!=null && !ingresoResultadoSeleccionado.getUsuarioMedico().isEmpty())
				medico = usuariosService.obtenerUsuarioById(Integer.valueOf(ingresoResultadoSeleccionado.getUsuarioMedico()));
			else if (ingresoResultadoSeleccionado.getMedicoCambioTurno()!=null && !ingresoResultadoSeleccionado.getMedicoCambioTurno().isEmpty())
				medico = usuariosService.obtenerUsuarioById(Integer.valueOf(ingresoResultadoSeleccionado.getMedicoCambioTurno()));
			
			// generando el documento
			HashMap<String, Object> parametros = new HashMap<String, Object>();
			
			parametros.put("logo", sRutaImgLogo);
			parametros.put("codigoP", String.valueOf(ingresoResultadoSeleccionado.getCodigoExpediente()));
			parametros.put("medico", medico.getNombre());
			parametros.put("fecha", UtilDate.DateToString(ingresoResultadoSeleccionado.getFechaOrdenLaboratorio().getTime(),"dd/MM/yyyy"));
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
			parametros.put("horaRecibido", UtilDate.DateToString(resultado.getHoraReporte(), "hh:mm a"));
			if (ingresoResultadoSeleccionado.getFechaHoraTomaMx()!=null){
				Date fechaHoraMx = UtilDate.StringToDate(ingresoResultadoSeleccionado.getFechaHoraTomaMx(),"dd/MM/yyyy HH:mm:ss");
				parametros.put("horaIngreso", UtilDate.DateToString(fechaHoraMx,"hh:mm a"));				
			}else{
				parametros.put("horaIngreso", null);
			}
			
			VisorController.mostrarReporte("Extendido Periferico", sRutaJasper, parametros, null);
		}
	}

	private void visualizarGotaGruesa(OrdenesExamenes ingresoResultadoSeleccionado) throws ParseException{

		if (ingresoResultadoSeleccionado.getEstado().equalsIgnoreCase("Pendiente")){
			MensajeExamenPendiente();
		}else if (ingresoResultadoSeleccionado.getEstado().equalsIgnoreCase("Cancelado")){
			MensajeExamenCancelado();
		}else{
		
			String sRutaJasper = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.gota.gruesa");
			String sRutaImgLogo = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.imagen.logo");
			String sRutaImgMinsa = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.imagen.logo.MINSA");
			String sRutaImgFlecha = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.imagen.flecha");
			String sRutaImgCheck = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.imagen.check");
			
			MalariaResultados resultado = laboratorioService.obtenerMalariaResult(ingresoResultadoSeleccionado.getSecOrdenLaboratorio());
			Paciente paciente = pacienteService.getPacienteById(ingresoResultadoSeleccionado.getCodigoExpediente());
			UsuariosView bioanalista = usuariosService.obtenerUsuarioById(resultado.getUsuarioBioanalista().intValue());
			UsuariosView medico = new UsuariosView();
			if (ingresoResultadoSeleccionado.getUsuarioMedico()!=null && !ingresoResultadoSeleccionado.getUsuarioMedico().isEmpty())
				medico = usuariosService.obtenerUsuarioById(Integer.valueOf(ingresoResultadoSeleccionado.getUsuarioMedico()));
			else if (ingresoResultadoSeleccionado.getMedicoCambioTurno()!=null && !ingresoResultadoSeleccionado.getMedicoCambioTurno().isEmpty())
				medico = usuariosService.obtenerUsuarioById(Integer.valueOf(ingresoResultadoSeleccionado.getMedicoCambioTurno()));
			
			// generando el documento
			HashMap<String, Object> parametros = new HashMap<String, Object>();
			
			String[] horaRecibido = UtilDate.DateToString(resultado.getHoraReporte(), "hh:mm a").split(" ");
			
			String sResultado = "";
			if (resultado.getPFalciparum())
				sResultado = "F";
			if (resultado.getPVivax())
				sResultado = "V";
			if (resultado.getNegativo())
				sResultado = "N";
			
			String sPaciente = paciente.getNombre1();
			if(paciente.getNombre2()!=null) sPaciente = sPaciente + " " + paciente.getNombre2();
			if(paciente.getApellido1()!=null) sPaciente = sPaciente + " " + paciente.getApellido1();
			if(paciente.getApellido2()!=null) sPaciente = sPaciente + " " + paciente.getApellido2();
			
			parametros.put("logo", sRutaImgLogo);
			parametros.put("logoMINSA", sRutaImgMinsa);
			parametros.put("horaRecibido",horaRecibido[0]);
			parametros.put("horaRecibidoMD",horaRecibido[1].toUpperCase());
			parametros.put("resultado", sResultado);		
			parametros.put("dia", UtilDate.DateToString(ingresoResultadoSeleccionado.getFechaOrdenLaboratorio().getTime(),"dd"));
			parametros.put("mes", UtilDate.DateToString(ingresoResultadoSeleccionado.getFechaOrdenLaboratorio().getTime(),"MM"));
			parametros.put("anio", UtilDate.DateToString(ingresoResultadoSeleccionado.getFechaOrdenLaboratorio().getTime(),"yyyy"));		
			parametros.put("paciente", sPaciente);
			parametros.put("medico", medico.getNombre());
			parametros.put("labCodigo", bioanalista.getCodigoPersonal());
			parametros.put("labNumero",null);
			parametros.put("flecha", sRutaImgFlecha);
			parametros.put("codigoP", String.valueOf(ingresoResultadoSeleccionado.getCodigoExpediente()));
			parametros.put("check",sRutaImgCheck);
			if (ingresoResultadoSeleccionado.getFechaHoraTomaMx()!=null){
				Date fechaHoraMx = UtilDate.StringToDate(ingresoResultadoSeleccionado.getFechaHoraTomaMx(),"dd/MM/yyyy HH:mm:ss");
				String[] horaMx = UtilDate.DateToString(fechaHoraMx,"hh:mm a").split(" ");
				parametros.put("horaMx", horaMx[0]);
				parametros.put("horaMxMD", horaMx[1]);
			}else{
				parametros.put("horaMx", null);
				parametros.put("horaMxMD", null);
			}
	
			VisorController.mostrarReporte("Gota Gruesa", sRutaJasper, parametros, null);
		}
	}

	private void visualizarGeneralHeces(OrdenesExamenes ingresoResultadoSeleccionado) throws ParseException{

		if (ingresoResultadoSeleccionado.getEstado().equalsIgnoreCase("Pendiente")){
			MensajeExamenPendiente();
		}else if (ingresoResultadoSeleccionado.getEstado().equalsIgnoreCase("Cancelado")){
			MensajeExamenCancelado();
		}else{
			String sRutaJasper ="";
			if (ingresoResultadoSeleccionado.getExamen().equalsIgnoreCase("Citologia Fecal")) {
				sRutaJasper = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.general.cito");
			}
			else {
				sRutaJasper = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.general.heces");
			}
			String sRutaImgLogo = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.imagen.logo");
			String sRutaImgMinsa = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.imagen.logo.MINSA");
			
			EghResultados resultado = laboratorioService.obtenerEghResultado(ingresoResultadoSeleccionado.getSecOrdenLaboratorio());
			Paciente paciente = pacienteService.getPacienteById(ingresoResultadoSeleccionado.getCodigoExpediente());
			UsuariosView bioanalista = usuariosService.obtenerUsuarioById(resultado.getUsuarioBionalista().intValue());
			UsuariosView medico = new UsuariosView();
			
			if (ingresoResultadoSeleccionado.getUsuarioMedico()!=null && !ingresoResultadoSeleccionado.getUsuarioMedico().isEmpty())
				medico = usuariosService.obtenerUsuarioById(Integer.valueOf(ingresoResultadoSeleccionado.getUsuarioMedico()));
			else if (ingresoResultadoSeleccionado.getMedicoCambioTurno()!=null && !ingresoResultadoSeleccionado.getMedicoCambioTurno().isEmpty())
				medico = usuariosService.obtenerUsuarioById(Integer.valueOf(ingresoResultadoSeleccionado.getMedicoCambioTurno()));
			
			// generando el documento
			HashMap<String, Object> parametros = new HashMap<String, Object>();
			
			String[] horaRecibido = UtilDate.DateToString(resultado.getHoraReporte(), "hh:mm a").split(" ");
			
			String sPaciente = paciente.getNombre1();
			if(paciente.getNombre2()!=null) sPaciente = sPaciente + " " + paciente.getNombre2();
			if(paciente.getApellido1()!=null) sPaciente = sPaciente + " " + paciente.getApellido1();
			if(paciente.getApellido2()!=null) sPaciente = sPaciente + " " + paciente.getApellido2();
			
			parametros.put("logo", sRutaImgLogo);
			parametros.put("logoMINSA", sRutaImgMinsa);
			parametros.put("horaRecibido",horaRecibido[0]);
			parametros.put("horaRecibidoMD",horaRecibido[1].toUpperCase());
			parametros.put("dia", UtilDate.DateToString(ingresoResultadoSeleccionado.getFechaOrdenLaboratorio().getTime(),"dd"));
			parametros.put("mes", UtilDate.DateToString(ingresoResultadoSeleccionado.getFechaOrdenLaboratorio().getTime(),"MM"));
			parametros.put("anio", UtilDate.DateToString(ingresoResultadoSeleccionado.getFechaOrdenLaboratorio().getTime(),"yyyy"));		
			parametros.put("paciente", sPaciente);
			parametros.put("medico", medico.getNombre());
			parametros.put("labCodigo", bioanalista.getCodigoPersonal());
			parametros.put("labNumero",bioanalista.getCodigoPersonal());
			parametros.put("codigoP", String.valueOf(ingresoResultadoSeleccionado.getCodigoExpediente()));
			parametros.put("color", resultado.getColor());
			parametros.put("consistencia", resultado.getConsistencia());
			parametros.put("restos", resultado.getRestosAlimenticios());
			parametros.put("mucus", resultado.getMucus());
			parametros.put("ph", resultado.getPh());
			parametros.put("sangreOculta", resultado.getSangreOculta());
			parametros.put("bacterias", resultado.getBacterias());
			parametros.put("levaduras", resultado.getLevaduras());
			parametros.put("leucocitos", resultado.getLeucocitos());
			parametros.put("eritrocitos", resultado.getEritrocitos());
			parametros.put("filamentosMucosos", resultado.getFilamentosMucosos());
			parametros.put("eColi", resultado.getEColi());
			parametros.put("endolimaxNana", resultado.getEndolimaxNana());
			parametros.put("eHistolytica", resultado.getEHistolytica());
			parametros.put("giardiaLambia", resultado.getGardiaAmblia());
			parametros.put("trichurisTrichura", resultado.getTrichuris());
			parametros.put("hymenolepisNana", resultado.getHymenolepisNana());
			parametros.put("strongyloideStercolaris", resultado.getStrongyloideStercolaris());
			parametros.put("uncinarias", resultado.getUnicinarias());
			parametros.put("enterovirus", resultado.getEnterovirus());
			parametros.put("observaciones", resultado.getObservaciones());
			if (ingresoResultadoSeleccionado.getFechaHoraTomaMx()!=null){
				Date fechaHoraMx = UtilDate.StringToDate(ingresoResultadoSeleccionado.getFechaHoraTomaMx(),"dd/MM/yyyy HH:mm:ss");
				String[] horaMx = UtilDate.DateToString(fechaHoraMx,"hh:mm a").split(" ");
				parametros.put("horaMx", horaMx[0]);
				parametros.put("horaMxMD", horaMx[1]);
			}else{
				parametros.put("horaMx", null);
				parametros.put("horaMxMD", null);
			}		
			VisorController.mostrarReporte("Examen General de Heces", sRutaJasper, parametros, null);
		}
	}

	private void visualizarGeneralOrina(OrdenesExamenes ingresoResultadoSeleccionado) throws ParseException{
		
		if (ingresoResultadoSeleccionado.getEstado().equalsIgnoreCase("Pendiente")){
			MensajeExamenPendiente();
		}else if (ingresoResultadoSeleccionado.getEstado().equalsIgnoreCase("Cancelado")){
			MensajeExamenCancelado();
		}else{
		
			String sRutaJasper = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.general.orina");
			String sRutaImgLogo = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.imagen.logo");
			String sRutaImgMinsa = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.jasper.imagen.logo.MINSA");
			
			EgoResultados resultado = laboratorioService.obtenerEgoResultado(ingresoResultadoSeleccionado.getSecOrdenLaboratorio());
			Paciente paciente = pacienteService.getPacienteById(ingresoResultadoSeleccionado.getCodigoExpediente());
			UsuariosView bioanalista = usuariosService.obtenerUsuarioById(resultado.getUsuarioBioanalista().intValue());
			UsuariosView medico = new UsuariosView();
			
			if (ingresoResultadoSeleccionado.getUsuarioMedico()!=null && !ingresoResultadoSeleccionado.getUsuarioMedico().isEmpty())
				medico = usuariosService.obtenerUsuarioById(Integer.valueOf(ingresoResultadoSeleccionado.getUsuarioMedico()));
			else if (ingresoResultadoSeleccionado.getMedicoCambioTurno()!=null && !ingresoResultadoSeleccionado.getMedicoCambioTurno().isEmpty())
				medico = usuariosService.obtenerUsuarioById(Integer.valueOf(ingresoResultadoSeleccionado.getMedicoCambioTurno()));
			
			// generando el documento
			HashMap<String, Object> parametros = new HashMap<String, Object>();
			
			String[] horaRecibido = UtilDate.DateToString(resultado.getHoraReporte(), "hh:mm a").split(" ");
			
			String sPaciente = paciente.getNombre1();
			if(paciente.getNombre2()!=null) sPaciente = sPaciente + " " + paciente.getNombre2();
			if(paciente.getApellido1()!=null) sPaciente = sPaciente + " " + paciente.getApellido1();
			if(paciente.getApellido2()!=null) sPaciente = sPaciente + " " + paciente.getApellido2();
			
			parametros.put("logo", sRutaImgLogo);
			parametros.put("logoMINSA", sRutaImgMinsa);
			parametros.put("horaRecibido",horaRecibido[0]);
			parametros.put("horaRecibidoMD",horaRecibido[1].toUpperCase());
			parametros.put("dia", UtilDate.DateToString(ingresoResultadoSeleccionado.getFechaOrdenLaboratorio().getTime(),"dd"));
			parametros.put("mes", UtilDate.DateToString(ingresoResultadoSeleccionado.getFechaOrdenLaboratorio().getTime(),"MM"));
			parametros.put("anio", UtilDate.DateToString(ingresoResultadoSeleccionado.getFechaOrdenLaboratorio().getTime(),"yyyy"));		
			parametros.put("paciente", sPaciente);
			parametros.put("medico", medico.getNombre());
			parametros.put("labCodigo", bioanalista.getCodigoPersonal());
			parametros.put("labNumero",bioanalista.getCodigoPersonal());
			parametros.put("codigoP", String.valueOf(ingresoResultadoSeleccionado.getCodigoExpediente()));
			parametros.put("color", resultado.getColor());
			parametros.put("aspecto", resultado.getAspecto());
			parametros.put("sedimento", resultado.getSedimento());
			parametros.put("densidad", resultado.getDensidad());
			parametros.put("ph", resultado.getPh());
			parametros.put("proteinas", resultado.getProteinas());
			parametros.put("bacterias", resultado.getBacterias());
			parametros.put("levaduras", resultado.getLevaduras());
			parametros.put("leucocitos", resultado.getLeucositos());
			parametros.put("eritrocitos", resultado.getEritrocitos());
			parametros.put("hilosMucosos", resultado.getHilosMucosos());
			parametros.put("hemoglobinas", resultado.getHomoglobinas());
			parametros.put("cuerpoCetonico", resultado.getCuerpoCetonico());
			parametros.put("urobilinogeno", resultado.getUrobilinogeno());
			parametros.put("glucosa", resultado.getGlucosa());
			parametros.put("bilirrubinas", resultado.getBilirrubinas());
			parametros.put("nitritos", resultado.getNitritos());
			parametros.put("celulasEpiteliales", resultado.getCelulasEpiteliales());
			parametros.put("cilindros", resultado.getCilindros());
			parametros.put("cristales", resultado.getCristales());
			parametros.put("observaciones", resultado.getObservaciones());
			if (ingresoResultadoSeleccionado.getFechaHoraTomaMx()!=null){
				Date fechaHoraMx = UtilDate.StringToDate(ingresoResultadoSeleccionado.getFechaHoraTomaMx(),"dd/MM/yyyy HH:mm:ss");
				String[] horaMx = UtilDate.DateToString(fechaHoraMx,"hh:mm a").split(" ");
				parametros.put("horaMx", horaMx[0]);
				parametros.put("horaMxMD", horaMx[1]);
			}else{
				parametros.put("horaMx", null);
				parametros.put("horaMxMD", null);
			}		
			VisorController.mostrarReporte("Examen General de Orina", sRutaJasper, parametros, null);
		}
	}

	/*****************************************************/
	/******************CANCELAR EXAMEN********************/
	@Wire("[id$=popCancelarExamen]")
	private Window popCancelarExamen;
	
	@Wire("[id$=idOrdenLaboratorio]")
    private Textbox idOrdenLaboratorio;
	
	@Wire("[id$=codExpediente]")
    private Textbox codExpediente;
	
	@Wire("[id$=txtHojaConsulta]")
    private Textbox txtHojaConsulta;
	
	@Wire("[id$=txtRazon]")
    private Textbox txtRazon;
	
	@Wire("[id$=lblTituloOrden]")
    private Label lblTituloOrden;
	
	private void inicializarPopCancelarExamen(OrdenesExamenes ordenCancelar){
		this.txtHojaConsulta.setValue(String.valueOf(ordenCancelar.getSecHojaConsulta()));
		this.idOrdenLaboratorio.setValue(String.valueOf(ordenCancelar.getSecOrdenLaboratorio()));
		this.lblTituloOrden.setValue("CANCELAR EXAMEN - "+ordenCancelar.getExamen().toUpperCase());;
		this.popCancelarExamen.doModal();
	}
	
    @Listen("onClick=[id$=btnSalirPopup]")
    public void btnSalirPopup_onClick() {
    	limpiarCerrarPop();
    }
    
    @Listen("onClick=[id$=btnCancelar]")
    public void btnCancelar_onClick() {
    	cancelarOrdenLaboratorio();
    }
    
	@Listen("onCancelar=[id$=lstIngresoResultados]")
	public void onClickCancelar(ForwardEvent evt) {

		// Inicializar objeto a editar
		OrdenesExamenes ingresoResultadoSeleccionado = new OrdenesExamenes();

		try {
			// Obtener objeto seleccionado
			Toolbarbutton button = (Toolbarbutton) evt.getOrigin().getTarget();
			if (button != null) {
				Listitem listitem = (Listitem) button.getParent().getParent();
				if (listitem != null) {
					ingresoResultadoSeleccionado = (OrdenesExamenes) listitem
							.getValue();
				}
			}

			// Validar que se seleccione el objeto de la lista
			if (ingresoResultadoSeleccionado == null
					|| ingresoResultadoSeleccionado.getSecOrdenLaboratorio() <= 0) {
				Messagebox
						.show("La fila seleccionada no contiene información del Ingreso de Resultados. Favor verifique",
								Mensajes.REGISTRO_NO_SELECCIONADO,
								new Messagebox.Button[] { Messagebox.Button.OK },
								Messagebox.EXCLAMATION, null);
				return;
			} else {
				if (!ingresoResultadoSeleccionado.getEstado().equalsIgnoreCase("Cancelado")){
					inicializarPopCancelarExamen(ingresoResultadoSeleccionado);
				}else{
					Messagebox
					.show("Examen clínico ya se encuentra cancelado",
							"Examen Cancelado",
							new Messagebox.Button[] { Messagebox.Button.OK },
							Messagebox.EXCLAMATION, null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_GENERAR_PDF,	Mensajes.TipoMensaje.ERROR);
		}
	}
	
	private void cancelarOrdenLaboratorio(){
		if (this.txtRazon.getValue().length()<=0){
        	Messagebox.show("Ingrese la razón de cancelación del examen", "Validación", Messagebox.OK, Messagebox.INFORMATION);
            return;
        }
		Short usuario =  (short) Utilidades.obtenerInfoSesion().getUsuarioId();
		OrdenLaboratorio ordenCancelar = laboratorioService.obtenerOrdenLab(Integer.valueOf(this.idOrdenLaboratorio.getValue().toString()));
		ordenCancelar.setEstado('2');
		ordenCancelar.setRazonCancela(this.txtRazon.getValue());
		ordenCancelar.setUsuarioCancela(usuario);
		ordenCancelar.setFechaHoraCancela(UtilDate.DateToString(new Date(), "dd/MM/yyyy HH:mm:ss"));
		
		InfoResultado resultado = laboratorioService.actualizarOrdenLaboratorio(ordenCancelar);
		if(resultado.isOk() && resultado.getObjeto()!=null){
			Mensajes.enviarMensaje(resultado.getMensaje(),	Mensajes.TipoMensaje.INFO);			
			retornaLista(Integer.valueOf(this.txtCodigo.getValue()), Integer.valueOf(this.txtHojaConsulta.getValue()));
			limpiarCerrarPop();
		}else{
			Mensajes.enviarMensaje(resultado.getMensaje(),	Mensajes.TipoMensaje.ERROR);
		}
	}
	
    private void limpiarCerrarPop(){
    	this.idOrdenLaboratorio.setValue("");
    	this.txtHojaConsulta.setValue("");
    	this.lblTituloOrden.setValue("");
		popCancelarExamen.setVisible(false);
	}
    


}
