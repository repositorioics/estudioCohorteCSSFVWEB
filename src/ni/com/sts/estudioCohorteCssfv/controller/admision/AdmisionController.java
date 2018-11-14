package ni.com.sts.estudioCohorteCssfv.controller.admision;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.Admision;
import ni.com.sts.estudioCohorteCSSFV.modelo.EstadosHoja;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;
import ni.com.sts.estudioCohorteCSSFV.modelo.Paciente;
import ni.com.sts.estudioCohorteCSSFV.modelo.ParametrosSistemas;
import ni.com.sts.estudioCohorteCSSFV.modelo.TipoConsulta;
import ni.com.sts.estudioCohorteCSSFV.modelo.UsuariosView;
import ni.com.sts.estudioCohorteCssfv.datos.admision.AdmisionDA;
import ni.com.sts.estudioCohorteCssfv.datos.admision.TipoConsultaDA;
import ni.com.sts.estudioCohorteCssfv.datos.hojaConsulta.HojaConsultaDA;
import ni.com.sts.estudioCohorteCssfv.datos.paciente.PacienteDA;
import ni.com.sts.estudioCohorteCssfv.datos.parametro.ParametrosDA;
import ni.com.sts.estudioCohorteCssfv.datos.usuario.UsuariosDA;
import ni.com.sts.estudioCohorteCssfv.dto.OrdenesExamenes;
import ni.com.sts.estudioCohorteCssfv.servicios.AdmisionService;
import ni.com.sts.estudioCohorteCssfv.servicios.HojaConsultaService;
import ni.com.sts.estudioCohorteCssfv.servicios.PacienteService;
import ni.com.sts.estudioCohorteCssfv.servicios.ParametroService;
import ni.com.sts.estudioCohorteCssfv.servicios.TipoConsultaService;
import ni.com.sts.estudioCohorteCssfv.servicios.UsuariosService;
import ni.com.sts.estudioCohorteCssfv.util.Generico;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilDate;
import ni.com.sts.estudioCohorteCssfv.util.Utilidades;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 * Controlador para la página admision.zul
 *
 * <p>
 *
 * @author STS
 * @version 1.0, &nbsp; 10/02/2015
 * @since jdk1.6
 */
public class AdmisionController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    private static AdmisionService admisionService = new AdmisionDA();
    
    private static TipoConsultaService tipoConsultaService = new TipoConsultaDA();
    
    private static PacienteService pacienteService = new PacienteDA();
    
    private static HojaConsultaService hojaConsultaService = new HojaConsultaDA();
    
    private static UsuariosService usuariosService = new UsuariosDA();
    
    private static ParametroService parametroService = new ParametrosDA();
    
    private Admision admisionPendienteEntrega = null;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception{
        super.doAfterCompose(comp);
        init();
    }

    /**
     *
     */
    private void init() {
       this.txtFechaSalida.setValue(UtilDate.DateToString(new Date(), "dd/MM/yyyy hh:mm a"));
       cargarTiposConsulta();
       cargarUsuarioSolicita();
       this.txtCodExpediente.setFocus(true);
    }
    
    /** Componentes */

    @Wire("[id$=txtCodExpediente]")
    private Intbox txtCodExpediente;
    
    @Wire("[id$=txtNombrePaciente]")
    private Textbox txtNombrePaciente;
    
    @Wire("[id$=txtEstudios]")
    private Textbox txtEstudios;
    
    @Wire("[id$=txtFechaSalida]")
    private Textbox txtFechaSalida;
    
    @Wire("[id$=txtFechaEntrada]")
    private Textbox txtFechaEntrada;
    
    @Wire("[id$=txtHojaConsulta]")
    private Textbox txtHojaConsulta;
    
    @Wire("[id$=txtOrdenLlegada]")
    private Textbox txtOrdenLlegada;
    
    @Wire("[id$=cmbTipoConsulta]")
    private Combobox cmbTipoConsulta;    
    
    @Wire("[id$=txtHojaConsultaScan]")
    private Intbox txtHojaConsultaScan;
    
    @Wire("[id$=txtCaso]")
    private Textbox txtCaso;
    
    @Wire("[id$=txtObservaciones]")
    private Textbox txtObservaciones;
    
    @Wire("[id$=txtUsuarioEntrega]")
    private Textbox txtUsuarioEntrega;
    
    @Wire("[id$=txtUsuarioRegresa]")
    private Textbox txtUsuarioRegresa;
    
    @Wire("[id$=cmbUsuarioSoli]")
    private Combobox cmbUsuarioSoli;
    
    @Wire("[id$=chkRecepcion]")
    private Checkbox chkRecepcion;

    @Wire("[id$=btnGuardar]")
    private Button btnGuardar;
    
    @Wire("[id$=btnNuevo]")
    private Button btnNuevo;

    @Wire("[id$=btnBuscar]")
    private Button btnBuscar;
    
    @Wire("[id$=btnAsociar]")
    private Button btnAsociar;
    
	/**
     * Menejador del evento Click del botón "Buscar (Lupa junto al Código)"
     */
    @Listen("onClick=[id$=btnBuscar]")
    public void btnBuscar_onClick() {
        buscarPaciente();
    }
    
    /**
     * Menejador del evento Check de la casilla de verificación "Recepción Expediente"
     */
    @Listen("onCheck=[id$=chkRecepcion]")
    public void chkRecepcion_onCheck() {
    	
    	if (!this.chkRecepcion.isChecked()){
    		this.txtFechaEntrada.setValue(null);
    		this.txtHojaConsultaScan.setReadonly(true);
    		this.txtHojaConsultaScan.setValue(null);
    	}else{
    		this.txtHojaConsulta.setValue("");
        	this.txtOrdenLlegada.setValue("");
    	}
    	buscarPaciente();    	
    }
    
    /**
     * Menejador del evento Click del botón "Guardar"
     */
    @Listen("onClick=[id$=btnGuardar]")
    public void btnGuardar_onClick() {
        try {
        	validarDatosRequerido();
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_PROCESAR_DATOS, Mensajes.TipoMensaje.ERROR);
		}
    }
    
    /**
     * Menejador del evento OK(Enter) del campo "Código"
     */
    @Listen("onOK=[id$=txtCodExpediente]")
    public void txtCodExpediente_onOk() {
        try {
        	buscarPaciente();
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_PROCESAR_DATOS, Mensajes.TipoMensaje.ERROR);
		}
    }
    
    /**
	 * Menejador del evento OK(Enter) del campo "Escanear No. Hoja de Consulta"
	 */
    @Listen("onOK=[id$=txtHojaConsultaScan]")
    public void txtHojaConsultaScan_onOk() {
        try {
        	if (this.chkRecepcion.isChecked()){
        		validarHojaConsulta(false);
        	}else {
        		if (validarHojaConsultaEmergencia(false)){
        			habilitarCampos();
        		}else{
        			deshabilitarCampos();
        		}
        	}
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_PROCESAR_DATOS, Mensajes.TipoMensaje.ERROR);
		}
    }
    
    /**
	 * Menejador del evento Click del botón Nueva
	 */
	@Listen("onClick=[id$=btnNuevo]")
    public void btnNuevo_onClick() {
        limpiarCampos();
        deshabilitarCampos();
        this.txtCodExpediente.setFocus(true);
    }
    
	/**
	 * Menejador del evento onSelect del combo Tipo de Consulta
	 */
    @Listen("onSelect=[id$=cmbTipoConsulta]")
    public void cmbTipoConsulta_onSelect() {
        if (cmbTipoConsulta.getSelectedIndex()>0){
        	if (!this.chkRecepcion.isChecked()){
        		if (cmbTipoConsulta.getSelectedItem().getValue().equals("EMERGENCIA")){
        			Mensajes.enviarMensaje("Para tipo de consulta \"Emergencia\" debe ingresar número de hoja de consulta", Mensajes.TipoMensaje.INFO);
        			this.txtHojaConsultaScan.setReadonly(false);
        			this.txtHojaConsultaScan.setFocus(true);
        		}else{
        			this.txtHojaConsultaScan.setReadonly(true);
        			this.txtHojaConsultaScan.setFocus(false);
        		}
        	}
        }
    }

    /**
     * Cuándo es tipo de consulta EMERGENCIA éste método se encarga de validar que se esta recibiendo el expediente correcto en la hoja de consulta correcta
     * @param guardar: Si es true se envia mensaje indicando que la hoja de consulta es válida para el expediente ingresado
     * @return TRUE: Si es valida la hoja de consulta ingresada, FALSE: en caso contrario
     */
    private boolean validarHojaConsultaEmergencia(boolean guardar) {
		if (cmbTipoConsulta.getSelectedIndex()>0){               		
			if (cmbTipoConsulta.getSelectedItem().getValue().equals("EMERGENCIA")){
		    	if (this.txtHojaConsultaScan.getValue()==null){
		        	Messagebox.show("Ingrese No. Hoja de Consulta", "Validación", Messagebox.OK, Messagebox.INFORMATION);
		        	this.txtHojaConsultaScan.setFocus(true);
		            return false;
		        }
		    	
		        try{
		        	Integer numHojaScan = this.txtHojaConsultaScan.getValue();
		        		HojaConsulta hojaConsultaScan = hojaConsultaService.getHojaConsultaByNumHoja(numHojaScan);
		        		if (hojaConsultaScan!=null){
		        			Admision admisionConsulta = admisionService.getAdmisionByNumHojaCon(numHojaScan);
		        			if (admisionConsulta!=null){
		        				Messagebox.show("Hoja de Consulta ya se encuentra asignada a otra admisión", "Validación", Messagebox.OK, Messagebox.INFORMATION);
		        				return false;
		        			}else {
		        				if (!this.txtCodExpediente.getValue().toString().trim().equalsIgnoreCase(String.valueOf(hojaConsultaScan.getCodExpediente()))){
		        					Messagebox.show("Hoja de consulta asociada a otro expediente. Expediente: "+hojaConsultaScan.getCodExpediente(), "Validación", Messagebox.OK, Messagebox.INFORMATION);
		        					return false;
		        				} else {
		        					if (!guardar)		        				
		        						Messagebox.show("Hoja de consulta válida", "Validación", Messagebox.OK, Messagebox.INFORMATION);
		        					return true;
		        				}
		        			}
		        		}else {
		            		Messagebox.show("No se encontró hoja de consulta para el No. Hoja Consulta escaneado", "Validación", Messagebox.OK, Messagebox.INFORMATION);
		            		return false;
		            	}
		        	
		 		}catch(WrongValueException ex){			
					return false;
				} catch(Exception e){
					Mensajes.enviarMensaje("Sucedio un error al realizar búsqueda de hoja de consulta","Error",Mensajes.TipoMensaje.ERROR);
					return false;
				}
			}else{
				return true;
			}
		}else{
			return true;
		}	
		
	}

    /**
     * Cuándo es recepción de expediente éste método se encarga de validar que se esta recibiendo el expediente correcto en la hoja de consulta correcta
     * @param guardar: Si es true se envia mensaje indicando que la hoja de consulta es correcta para el expediente que esta pendiente de recepción
     * @return TRUE: Si es valida la hoja de consulta ingresada, FALSE: en caso contrario
     */
    private boolean validarHojaConsulta(boolean guardar){
    	if (cmbTipoConsulta.getSelectedIndex()>0){               		
			if (cmbTipoConsulta.getSelectedItem().getValue().equals("EMERGENCIA") || cmbTipoConsulta.getSelectedItem().getValue().equals("CONSULTA")){	
		    		if (this.admisionPendienteEntrega == null){
			    		Messagebox.show("Primero debe realizar búsqueda de expediente", "Validación", Messagebox.OK, Messagebox.INFORMATION);
			            return false;
			    	}
		    		
		    		if (this.txtHojaConsultaScan.getValue()==null){
			        	Messagebox.show("Ingrese No. Hoja de Consulta", "Validación", Messagebox.OK, Messagebox.INFORMATION);
			        	this.txtHojaConsultaScan.setFocus(true);
			            return false;
			        }
			    	
			        try{
			        	Integer numHojaScan = this.txtHojaConsultaScan.getValue();
			        		HojaConsulta hojaConsultaScan = hojaConsultaService.getHojaConsultaByNumHoja(numHojaScan);
			        		if (hojaConsultaScan!=null){
			        			if (admisionPendienteEntrega.getNumHojaConsulta()!=hojaConsultaScan.getNumHojaConsulta()){
			        				Messagebox.show("Hoja de Consulta no corresponde al asignado en la Consulta", "Validación", Messagebox.OK, Messagebox.INFORMATION);
			        				return false;
			        			}else {
			        				if (!guardar)
			        					Messagebox.show("Hoja de Consulta correcta", "Validación", Messagebox.OK, Messagebox.INFORMATION);
			        				return true;
			        			}
			        		}else {
			            		Messagebox.show("No se encontró hoja de consulta para el No. Hoja Consulta escaneado", "Validación", Messagebox.OK, Messagebox.INFORMATION);
			            		return false;
			            	}
			        	
			 		}catch(WrongValueException ex){			
						return false;
					} catch(Exception e){
						Mensajes.enviarMensaje("Sucedio un error al realizar búsqueda de hoja de consulta","Error",Mensajes.TipoMensaje.ERROR);
						return false;
					}
			}else {
				return true;
			}
    	}else {
    		return true;
    	}
    }
    
    /**
     * Método que reinicia los valores de los campos en el formulario
     */
    private void limpiarCampos(){
    	this.txtFechaSalida.setValue(UtilDate.DateToString(new Date(), "dd/MM/yyyy hh:mm a"));
    	this.txtCodExpediente.setValue(null);
    	this.txtHojaConsulta.setValue("");
    	this.txtNombrePaciente.setValue("");
    	this.txtEstudios.setValue("");
    	this.txtOrdenLlegada.setValue("");
    	this.txtFechaEntrada.setValue(null);
    	this.txtFechaEntrada.setReadonly(true);
    	this.cmbTipoConsulta.setSelectedIndex(0);
    	this.txtHojaConsultaScan.setValue(null);
    	this.txtHojaConsultaScan.setReadonly(true);
    	this.cmbUsuarioSoli.setSelectedIndex(0);
    	this.admisionPendienteEntrega = null;
    	this.chkRecepcion.setChecked(false);
    	this.btnGuardar.setDisabled(true);
    	this.txtObservaciones.setValue("");
    	this.txtUsuarioEntrega.setValue("");
    	this.txtUsuarioRegresa.setValue("");
    	this.txtCaso.setValue("");
    }

    /**
     * Método que se encarga de cargar los elementos del combo tipo de consulta desde la base de datos
     */
    private void cargarTiposConsulta(){
    	// Limpiar combo
    	TipoConsulta seleccione = new TipoConsulta();
    	seleccione.setCodigo("0");
    	seleccione.setDescripcion("Seleccione");
    	List<TipoConsulta> oList = new ArrayList<TipoConsulta>();
    	oList.add(0,seleccione);
    	try {
			oList.addAll(tipoConsultaService.getTiposConsultas());
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje("Error consultar tipos de consulta",
					Mensajes.TipoMensaje.ERROR);
		}

    	
        this.cmbTipoConsulta.setModel(new ListModelList<TipoConsulta>(new ArrayList<TipoConsulta>()));
        this.cmbTipoConsulta.setModel(new ListModelList<TipoConsulta>(oList));
        this.cmbTipoConsulta.setItemRenderer(new ComboitemRenderer<TipoConsulta>() {
               @Override
               public void render(Comboitem comboitem, TipoConsulta fila, int index) throws Exception {
                   comboitem.setLabel( fila.getDescripcion());
                   comboitem.setValue((fila.getCodigo()));
                   cmbTipoConsulta.setSelectedIndex(0);
           }});
    }
    
    /**
     * Habilitar campos en el formulario
     */
    private void habilitarCampos(){
    	this.btnGuardar.setDisabled(false);    	
    }
    
    /**
     * Deshabilitar campos en el formulario
     */
    private void deshabilitarCampos(){
    	this.btnGuardar.setDisabled(true);    	
    }
    
    
    private void prepararNuevaAdmision(){
   		this.cmbTipoConsulta.setDisabled(false);
		this.cmbTipoConsulta.setFocus(true);
		this.cmbUsuarioSoli.setDisabled(false);
		this.cmbUsuarioSoli.setSelectedIndex(0);
		this.cmbTipoConsulta.setSelectedIndex(0);
		this.txtOrdenLlegada.setValue("");
		this.txtHojaConsulta.setValue("");
		this.txtCaso.setValue("");
		this.txtFechaSalida.setValue(UtilDate.DateToString(new Date(), "dd/MM/yyyy hh:mm a"));
		this.txtFechaEntrada.setValue(null);
		this.txtObservaciones.setValue("");
		UsuariosView user = usuariosService.obtenerUsuarioById(Utilidades.obtenerInfoSesion().getUsuarioId());
    	this.txtUsuarioEntrega.setValue(user.getNombre());
    	this.txtUsuarioRegresa.setValue("");
    }
    
    private void prepararNuevaRecepcion(){
    	this.cmbTipoConsulta.setDisabled(true);
		this.txtHojaConsultaScan.setReadonly(false);
		this.cmbUsuarioSoli.setDisabled(true);
		this.txtHojaConsultaScan.setFocus(true);
		UsuariosView user = usuariosService.obtenerUsuarioById(Utilidades.obtenerInfoSesion().getUsuarioId());
    	this.txtUsuarioRegresa.setValue(user.getNombre());
    }
    
    private void buscarPaciente(){
        if (this.txtCodExpediente.getValue()==null){
        	limpiarCampos();
        	Messagebox.show("Ingrese código", "Validación", Messagebox.OK, Messagebox.INFORMATION);
        	this.txtCodExpediente.setFocus(true);
            return;
        }
        try{
        	String codExpediente = this.txtCodExpediente.getValue().toString();
        	boolean habilitar = true;
        	Paciente paciente = pacienteService.getPacienteById(Integer.valueOf(codExpediente));
        	if (paciente!=null){
        		//el paciente esta retirado, no es apto para admisión
        		if (paciente.getRetirado().equals('1')){
        			limpiarCampos();
            		Mensajes.enviarMensaje(Mensajes.PACIENTE_RETIRADO, Mensajes.TipoMensaje.INFO);
            		this.txtCodExpediente.setFocus(true);
        		}else{
        			String nombrePaciente = "";
        			nombrePaciente = paciente.getNombre1();
    	    	
        			if (paciente.getNombre2()!=null) nombrePaciente += " " + paciente.getNombre2();
        			if (paciente.getApellido1()!=null) nombrePaciente += " " + paciente.getApellido1();
        			if (paciente.getApellido2()!=null) nombrePaciente += " " + paciente.getApellido2();
        			//es Admisión
        			if (!this.chkRecepcion.isChecked()){
        				//ver si hay admisiones con expedientes pendientes de recepción
        	        	List<Admision> admisionesPendiente = admisionService.getAdmisionesPendienteEntregaByCodExp(Integer.valueOf(codExpediente));
        				//no hay admisiones pendientes
        	        	if (admisionesPendiente.size()==0){
        					//dejar pasar a hacer la admisión
        	        		prepararNuevaAdmision();
        				}else{//si hay admisiones pendiente de recepción
        					//validar si las admisiones pendientes tienen hojas de consulta activa
        					List<HojaConsulta> hojasActivasAdminPendiente = hojaConsultaService.getHojaConsultaActivaAndAdmiPenByCodExp(Integer.valueOf(codExpediente));
        					//no hay hojas de consulta activas para las admisiones pendientes
            				if (hojasActivasAdminPendiente.size()==0){
            					//validar si hay admisiones pendiente de recepción, para hojas de consulta en abandono o cerradas
            					List<HojaConsulta> hojasNoActivasAdminPendiente = hojaConsultaService.getHojaConsultaNoActivaAndAdmiPenByCodExp(Integer.valueOf(codExpediente));
            					//no hay hojas de consulta no activas para las admisiones pendientes
                				if (hojasNoActivasAdminPendiente.size()==0){
                					//ver si es que no se generó hoja de consulta porque el tipo de consulta no lo genera
                					for(Admision admin : admisionesPendiente){
                						if (!admin.getTipoConsulta().getCodigo().equals("OPENCLINICA") || !admin.getTipoConsulta().getCodigo().equals("CONSULTA")){
                        					cargarListaAdmisionesPendientes(admisionesPendiente,false);
                        					popAdmisionesPendientes.doModal();
                        					break;
                						}
                					}
                					//dejar pasar a hacer la admisión
                					prepararNuevaAdmision();                					
                				}else{
                					cargarListaAdmisionesPendientes(admisionesPendiente,false);
                					popAdmisionesPendientes.doModal();
                					//dejar pasar a hacer la admisión
                					prepararNuevaAdmision();
                					//si los valores de los campos es “A” o “B”, entonces deberá mostrar esos valores en pantalla    	        	
	                        		if (admisionesPendiente.get(0).getNumHojaConsulta()!=null){
	    	        	        		HojaConsulta hojaConsulta = hojaConsultaService.getHojaConsultaByNumHoja(admisionesPendiente.get(0).getNumHojaConsulta());
	    	        	        		if (hojaConsulta!=null){
	    	        	        			//no importa que categoria sea, siempre mostrarla si se ingresó en el MOVIL. //10/11/2016
	    	        	        			if (hojaConsulta.getCategoria()!=null){ // && (hojaConsulta.getCategoria().trim().equalsIgnoreCase("A") || hojaConsulta.getCategoria().trim().equalsIgnoreCase("B"))){
	    	        	        				this.txtCaso.setValue(hojaConsulta.getCategoria().trim());
	    	        	        			}else{
	    	        	        				this.txtCaso.setValue("");
	    	        	        			}
	    	        	        		}else{
	    	        	        			Messagebox.show("No se recuperó hoja de consulta asociada a la admisión", "Validación", Messagebox.OK, Messagebox.INFORMATION);
	    	        	        		}
	                        		}
                				}

            				}else{//mostrar que hay mas de una admisión pendiente de recepción y mostrar cuáles son
	            	        	if (admisionesPendiente.size()==1){
	            					Messagebox.show("Expediente físico está pendiente de recepción y hoja de consulta está activa", "Validación", Messagebox.OK, Messagebox.INFORMATION);
	            	        		//pero deberá mostrar toda la información para conocer su ubicación
	                    			this.txtHojaConsultaScan.setReadonly(true);
	                    			this.txtFechaEntrada.setValue(null);
	                    			this.txtFechaSalida.setValue(UtilDate.DateToString(admisionesPendiente.get(0).getFechaSalida(), "dd/MM/yyyy hh:mm a"));
	                    			Utilidades.seleccionarItemEnCombo(cmbTipoConsulta, admisionesPendiente.get(0).getTipoConsulta().getCodigo());
	                        		this.cmbTipoConsulta.setDisabled(true);
	                        		this.txtNombrePaciente.setValue(nombrePaciente);
	                        		this.txtOrdenLlegada.setValue(admisionesPendiente.get(0).getOrdenLlegada()!=null?String.valueOf(admisionesPendiente.get(0).getOrdenLlegada()):"");
	                        		this.txtHojaConsulta.setValue(admisionesPendiente.get(0).getNumHojaConsulta()!=null?String.valueOf(admisionesPendiente.get(0).getNumHojaConsulta()):"");
	                        		this.txtObservaciones.setValue(admisionesPendiente.get(0).getObservaSalida());
	                        		this.cmbUsuarioSoli.setDisabled(true);
	                        		if (admisionesPendiente.get(0).getUsuarioRecibe()!=null){
	    	                    		UsuariosView usuarioRecibe = usuariosService.obtenerUsuarioById(admisionesPendiente.get(0).getUsuarioRecibe().intValue());
	    	                    		if (usuarioRecibe!=null)
	    	                    			Utilidades.seleccionarItemEnCombo(cmbUsuarioSoli, usuarioRecibe.getId().toString());                    		
	                        		}else{
	                        			this.cmbUsuarioSoli.setSelectedIndex(0);
	                        		}
	                        		if(admisionesPendiente.get(0).getUsuarioEntrega()!=null){
	                        			UsuariosView usuarioEntrega = usuariosService.obtenerUsuarioById(admisionesPendiente.get(0).getUsuarioEntrega().intValue());
	                        			if(usuarioEntrega!=null)
	                        				this.txtUsuarioEntrega.setValue(usuarioEntrega.getNombre());
	                        		}else{
	                        			this.txtUsuarioEntrega.setValue("");
	                        		}
	                        		if(admisionesPendiente.get(0).getUsuarioRegresa()!=null){
	                        			UsuariosView usuarioRegresa = usuariosService.obtenerUsuarioById(admisionesPendiente.get(0).getUsuarioRegresa().intValue());
	                        			if (usuarioRegresa!=null)
	                        			this.txtUsuarioRegresa.setValue(usuarioRegresa.getNombre());
	                        		}else{
	                        			this.txtUsuarioRegresa.setValue("");
	                        		}
	                        		this.btnGuardar.setDisabled(true);
	                        		//si los valores de los campos es “A” o “B”, entonces deberá mostrar esos valores en pantalla    	        	
	                        		if (admisionesPendiente.get(0).getNumHojaConsulta()!=null){
	    	        	        		HojaConsulta hojaConsulta = hojaConsultaService.getHojaConsultaByNumHoja(admisionesPendiente.get(0).getNumHojaConsulta());
	    	        	        		if (hojaConsulta!=null){
	    	        	        			//no importa que categoria sea, siempre mostrarla si se ingresó en el MOVIL. //10/11/2016
	    	        	        			if (hojaConsulta.getCategoria()!=null){ // && (hojaConsulta.getCategoria().trim().equalsIgnoreCase("A") || hojaConsulta.getCategoria().trim().equalsIgnoreCase("B"))){
	    	        	        				this.txtCaso.setValue(hojaConsulta.getCategoria().trim());
	    	        	        			}else{
	    	        	        				this.txtCaso.setValue("");
	    	        	        			}
	    	        	        		}else{
	    	        	        			Messagebox.show("No se recuperó hoja de consulta asociada a la admisión", "Validación", Messagebox.OK, Messagebox.INFORMATION);
	    	        	        		}
	                        		}
	            	        		habilitar = false;
	            	        		this.txtCodExpediente.setFocus(true);
	            	        		return;
	            	        	}else{	            	        		
	            	        		cargarListaAdmisionesPendientes(admisionesPendiente,true);
                					popAdmisionesPendientes.doModal();
                					//no dejar pasar a hacer la admisión
                					//this.txtNombrePaciente.setValue(nombrePaciente);
                					//this.txtHojaConsultaScan.setReadonly(false);
                					//this.txtFechaEntrada.setValue(null);
                					//this.cmbTipoConsulta.setDisabled(true);
                					//this.cmbUsuarioSoli.setDisabled(true);
                					//this.btnGuardar.setDisabled(true);
                					limpiarCampos();
	            	        		habilitar = false;
	            	        		this.txtCodExpediente.setFocus(true);
                					return;
	            	        	}
            				}
        				}        				
        			}else{ //es recepción
        				this.txtCaso.setValue("");
        				List<Admision> admisionesPendiente = admisionService.getAdmisionesPendienteEntregaByCodExp(Integer.valueOf(codExpediente));
                		if (admisionesPendiente.size()==0){
                			this.txtFechaEntrada.setValue(null);
                    		this.txtHojaConsultaScan.setReadonly(true);
                    		this.txtFechaSalida.setValue(UtilDate.DateToString(new Date(), "dd/MM/yyyy hh:mm a"));
                    		limpiarCampos();
                			Messagebox.show("Expediente ya se encuentra en Admisión", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                			this.txtCodExpediente.setFocus(true);
                			this.admisionPendienteEntrega = null;
                			return;        			
                		}else if (admisionesPendiente.size()==1) {
                			this.admisionPendienteEntrega = admisionesPendiente.get(0); 
                			//this.txtHojaConsultaScan.setReadonly(false);
                			this.txtFechaEntrada.setValue(UtilDate.DateToString(new Date(), "dd/MM/yyyy hh:mm a"));
                			this.txtFechaSalida.setValue(UtilDate.DateToString(admisionesPendiente.get(0).getFechaSalida(), "dd/MM/yyyy hh:mm a"));
                			Utilidades.seleccionarItemEnCombo(cmbTipoConsulta, admisionesPendiente.get(0).getTipoConsulta().getCodigo());
                    		//this.cmbTipoConsulta.setDisabled(true);
                    		UsuariosView usuarioRecibe = usuariosService.obtenerUsuarioById(admisionesPendiente.get(0).getUsuarioRecibe().intValue());
                    		if (usuarioRecibe!=null){
                    			Utilidades.seleccionarItemEnCombo(cmbUsuarioSoli, usuarioRecibe.getId().toString());
                    		}
                    		if(admisionesPendiente.get(0).getUsuarioEntrega()!=null){
                    			UsuariosView usuarioEntrega = usuariosService.obtenerUsuarioById(admisionesPendiente.get(0).getUsuarioEntrega().intValue());
                    			if (usuarioEntrega!=null)
                    				this.txtUsuarioEntrega.setValue(usuarioEntrega.getNombre());
                    		}else{
                    			this.txtUsuarioEntrega.setValue("");
                    		}
                    		if(admisionesPendiente.get(0).getUsuarioRegresa()!=null){
                    			UsuariosView usuarioRegresa = usuariosService.obtenerUsuarioById(admisionesPendiente.get(0).getUsuarioRegresa().intValue());
                    			if (usuarioRegresa!=null)
                    				this.txtUsuarioRegresa.setValue(usuarioRegresa.getNombre());
                    		}else{
                    			this.txtUsuarioRegresa.setValue("");
                    		}
                    		this.txtObservaciones.setValue("");
                    		if (admisionesPendiente.get(0).getNumHojaConsulta()!=null){
	        	        		HojaConsulta hojaConsulta = hojaConsultaService.getHojaConsultaByNumHoja(admisionesPendiente.get(0).getNumHojaConsulta());
	        	        		if (hojaConsulta!=null){
	        	        			//no importa que categoria sea, siempre mostrarla si se ingresó en el MOVIL. //10/11/2016
	        	        			if (hojaConsulta.getCategoria()!=null){ // && (hojaConsulta.getCategoria().trim().equalsIgnoreCase("A") || hojaConsulta.getCategoria().trim().equalsIgnoreCase("B"))){
	        	        				this.txtCaso.setValue(hojaConsulta.getCategoria().trim());
	        	        			}else{
	        	        				this.txtCaso.setValue("");
	        	        			}
	        	        		}else{
	        	        			Messagebox.show("No se recuperó hoja de consulta asociada a la admisión", "Validación", Messagebox.OK, Messagebox.INFORMATION);
	        	        		}
                    		}
                    		prepararNuevaRecepcion();
                		}else{
                			this.admisionPendienteEntrega = null;
                			cargarListaAdmisionesPendientesRecep(admisionesPendiente);
                			popAdmisionesPendientesRecep.doModal();
                		}
                		this.txtHojaConsultaScan.setValue(null);
        			}
           			this.txtNombrePaciente.setValue(nombrePaciente);

        			//se obtienen estudios asociados
        			String estudios = pacienteService.getEstudiosPaciente(Integer.valueOf(codExpediente));
            		this.txtEstudios.setValue(estudios);
        			if (habilitar)
        				habilitarCampos();
        		}
        	} else { //no se encontró paciente
        		limpiarCampos();
        		Mensajes.enviarMensaje(Mensajes.EXPEDIENTE_NO_ENCONTRADO, Mensajes.TipoMensaje.INFO);
            }
    	}catch(WrongValueException ex){			
    		return;
    	} catch(Exception e){
    		e.printStackTrace();
    		Mensajes.enviarMensaje("Sucedio un error al realizar búsqueda de expediente","Error",Mensajes.TipoMensaje.ERROR);
    	}
    }
    
    
    /**
     * Método que realiza la validación de los datos requeridos en el formulario de recepción, para que ésta pueda ser guardada
     * @throws Exception
     */
    private void validarDatosRequerido() throws Exception{
        if (this.txtCodExpediente.getValue()==null){
        	Messagebox.show("Ingrese o lea código", "Validación", Messagebox.OK, Messagebox.INFORMATION);
        	this.txtCodExpediente.setFocus(true);
            return;
        }
        if (this.cmbTipoConsulta.getSelectedIndex() <= 0) {
        	Messagebox.show("Seleccione tipo de consulta", "Validación", Messagebox.OK, Messagebox.INFORMATION);
        	this.cmbTipoConsulta.setFocus(true);
        	return;
        }
        if (!this.chkRecepcion.isChecked() && (this.cmbUsuarioSoli.getSelectedIndex()<=0)) {
        	Messagebox.show("Seleccione usuario que solicita el expediente", "Validación", Messagebox.OK, Messagebox.INFORMATION);
        	this.cmbUsuarioSoli.setFocus(true);
        	return;
        }
        if (this.txtFechaSalida.getValue().length()<=0){
        	Messagebox.show("Ingrese fecha de salida", "Validación", Messagebox.OK, Messagebox.INFORMATION);
            return;
        }
        if (cmbTipoConsulta.getSelectedIndex()>0){
        	if (!this.chkRecepcion.isChecked()){
        		if (cmbTipoConsulta.getSelectedItem().getValue().equals("EMERGENCIA") && this.txtHojaConsultaScan.getValue()==null){
        			Mensajes.enviarMensaje("Para tipo de consulta \"Emergencia\" debe ingresar número de hoja de consulta", Mensajes.TipoMensaje.INFO);
        			this.txtHojaConsultaScan.setReadonly(false);
        			this.txtHojaConsultaScan.setFocus(true);
        			return;
        		}else if (cmbTipoConsulta.getSelectedItem().getValue().equals("OPENCLINICA") || cmbTipoConsulta.getSelectedItem().getValue().equals("CONSULTA")){
        			if(Messagebox.show("¿Está seguro de generar hoja de consulta?", "Validación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.NO){
        				return;
        			}
        		}
        	}
        }
        
        guardarAdmision();
    }
    
    /**
     * Método que ejecuta la acción de guardar la admisión, ya sea nueva o recepción de expediente.
     * @throws Exception
     */
    private void guardarAdmision() throws Exception{
    	Short usuario =  (short) Utilidades.obtenerInfoSesion().getUsuarioId();//Short.valueOf(this.cmbEntregadoPor.getSelectedItem().getValue().toString());
    	if (this.chkRecepcion.isChecked()){
    		if (validarHojaConsulta(true)){
    			Admision admisionRecepcionar = this.admisionPendienteEntrega;
    			admisionRecepcionar.setUsuarioRegresa(usuario);
	    		Date fechaEntrada = UtilDate.StringToDate(this.txtFechaEntrada.getValue(), "dd/MM/yyyy hh:mm a");
	    		admisionRecepcionar.setFechaEntrada(fechaEntrada);
	    		admisionRecepcionar.setObservaEntrada(this.txtObservaciones.getValue());
	    		InfoResultado resultado = admisionService.actualizarAdmision(admisionRecepcionar);
		    	if(resultado.isOk() && resultado.getObjeto()!=null){
		    		limpiarCampos();
		    		Mensajes.enviarMensaje(resultado);
		    		this.txtCodExpediente.setFocus(true);
		    	}else{
		        	Mensajes.enviarMensaje(resultado);
		    	}
    		}
    	}else {
    		if (validarHojaConsultaEmergencia(true)){
	        	Admision admision = new Admision();
	        	//Short usuarioRegresa = Short.valueOf(this.cmbRegresadoA.getSelectedItem().getValue().toString());
	        	//Short usuarioRecibe = Short.valueOf(this.cmbRecibidoPor.getSelectedItem().getValue().toString());        	
	        	String tipoConsulta = this.cmbTipoConsulta.getSelectedItem().getValue(); //"CONSULTA";
	        	//Date fechaEntrada = UtilDate.StringToDate(this.txtFechaEntrada.getValue(), "dd/MM/yyyy hh:mm a");
	        	Date fechaSalida = UtilDate.StringToDate(this.txtFechaSalida.getValue(), "dd/MM/yyyy hh:mm a");
	        	
	        	admision.setCodExpediente(Integer.valueOf(this.txtCodExpediente.getValue()));
	        	admision.setObservaSalida(this.txtObservaciones.getValue());
	        	TipoConsulta tipoConsultaObj = tipoConsultaService.getTipoConsultaByCodigo(tipoConsulta);
	        	admision.setTipoConsulta(tipoConsultaObj);
	        	//admision.setFechaEntrada(fechaEntrada);
	        	admision.setFechaSalida(fechaSalida);
	        	//admision.setUsuarioRegresa(usuarioReresa);
	        	admision.setUsuarioEntrega(usuario);
	        	admision.setNumHojaConsulta(this.txtHojaConsultaScan.getValue());
	        	admision.setUsuarioRecibe(Short.valueOf(this.cmbUsuarioSoli.getSelectedItem().getValue().toString()));
	        	admision.setObservaSalida(this.txtObservaciones.getValue());
		    	InfoResultado resultado = admisionService.guardarAdmision(admision);
		    	if(resultado.isOk() && resultado.getObjeto()!=null){
		    		admision = (Admision)resultado.getObjeto();
		    		admision = admisionService.getAdmisionById(admision.getSecAdmision());
		    		this.txtHojaConsulta.setValue((admision.getNumHojaConsulta()!=null?String.valueOf(admision.getNumHojaConsulta()):""));
		    		this.txtOrdenLlegada.setValue((admision.getOrdenLlegada()!=null?admision.getOrdenLlegada().toString():""));
		    		this.btnGuardar.setDisabled(true);
		    		this.txtHojaConsultaScan.setValue(null);
		    		this.txtCodExpediente.setFocus(true);
		    		Mensajes.enviarMensaje(resultado);
		    	}else{
		        	Mensajes.enviarMensaje(resultado);
		    	}
    		}
    	}    	
    }
    
    /**
     * Método que se encarga de cargar los elementos del combo tipo de consulta desde la base de datos
     */
    private void cargarUsuarioSolicita(){
    	// Limpiar combo
    	UsuariosView seleccione = new UsuariosView();
    	seleccione.setId(0);
    	seleccione.setNombre("Seleccione");
    	List<UsuariosView> oList = new ArrayList<UsuariosView>();
    	oList.add(0,seleccione);
    	try {
    		ParametrosSistemas perfilSoExpediente = parametroService.getParametroByName("PERFIL_SOLICITA_EXPEDIENTE");
	    	if(perfilSoExpediente!=null){
				oList.addAll(usuariosService.obtenerUsuariosByPerfiles(perfilSoExpediente.getValores().replace(",", "','")));
	    	}else{
	    		Messagebox.show("No se encontró parámetro PERFIL_SOLICITA_EXPEDIENTE",
						Mensajes.REGISTRO_NO_ENCONTRADO,
						new Messagebox.Button[] { Messagebox.Button.OK },
						Messagebox.EXCLAMATION, null);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje("Error consultar tipos de consulta",
					Mensajes.TipoMensaje.ERROR);
		}

    	
        this.cmbUsuarioSoli.setModel(new ListModelList<UsuariosView>(new ArrayList<UsuariosView>()));
        this.cmbUsuarioSoli.setModel(new ListModelList<UsuariosView>(oList));
        this.cmbUsuarioSoli.setItemRenderer(new ComboitemRenderer<UsuariosView>() {
               @Override
               public void render(Comboitem comboitem, UsuariosView fila, int index) throws Exception {
                   comboitem.setLabel(fila.getNombre());
                   comboitem.setValue(fila.getId());
                   cmbUsuarioSoli.setSelectedIndex(0);
           }});
    }
    
    
    /*********************ADMISIONES PENDIENTES*********************/
    
	@Wire("[id$=popAdmisionesPendientes]")
	private Window popAdmisionesPendientes;
	
	@Wire("[id$=popAdmisionesPendientesRecep]")
	private Window popAdmisionesPendientesRecep;
	
    @Wire("[id$=lblAdmisionesPen]")
    private Label lblAdmisionesPen;

    @Wire("[id$=listaAdmisiones]")
	private Listbox listaAdmisiones;
	
	@Wire("[id$=listaAdmisionesRecep]")
	private Listbox listaAdmisionesRecep;
    
    @Listen("onClick=[id$=btnSalirPopup]")
    public void btnSalirPopup_onClick() {
    	limpiarCerrarPop();
    }
    
    @Listen("onClick=[id$=btnSalirPopupRecep]")
    public void btnSalirPopupRecep_onClick() {
    	limpiarCerrarPopRecep();
    	limpiarCampos();
    }
    
    private void limpiarCerrarPop(){
		//listaAdmisiones.setModel(new ListModelList<Admision>(new ArrayList<Admision>()));
		listaAdmisiones.setModel(new ListModelList<Generico>(new ArrayList<Generico>()));
		popAdmisionesPendientes.setVisible(false);
	}
    
    private void limpiarCerrarPopRecep(){
		listaAdmisionesRecep.setModel(new ListModelList<Admision>(new ArrayList<Admision>()));
		popAdmisionesPendientesRecep.setVisible(false);
	}
	
	private void cargarListaAdmisionesPendientes(List<Admision> admisiones, boolean soloConHojas) throws Exception{
		//this.listaAdmisiones.setModel(new ListModelList<Admision>(admisiones));
		List<Generico> genericoList = new ListModelList<Generico>();
		
		for(Admision admisionPendiente : admisiones){
			Generico datoAdmision = new Generico();
			datoAdmision.setNumero1(admisionPendiente.getCodExpediente());
			datoAdmision.setFecha1(admisionPendiente.getFechaSalida());
			datoAdmision.setTexto1(admisionPendiente.getTipoConsulta().getDescripcion());
			if (admisionPendiente.getNumHojaConsulta()!=null){
				datoAdmision.setTexto3(String.valueOf(admisionPendiente.getNumHojaConsulta()));
				EstadosHoja estadoHoja = hojaConsultaService.getEstadoHojaConsultaByNumHoja(admisionPendiente.getNumHojaConsulta());
				if (estadoHoja != null)  
					datoAdmision.setTexto2(estadoHoja.getDescripcion());
				else
					datoAdmision.setTexto2("");
			}else{
				datoAdmision.setTexto3("");
				datoAdmision.setTexto2("");
			}
			genericoList.add(datoAdmision);
		}
		this.listaAdmisiones.setModel(new ListModelList<Generico>(genericoList));
		if (!soloConHojas) {
			this.lblAdmisionesPen.setValue("ADMISIONES PENDIENTES DE RECEPCIÓN");
		}else{
			this.lblAdmisionesPen.setValue("ADMISIONES PENDIENTES DE RECEPCIÓN CON HOJAS ACTIVAS");
		}		
	}
	
	private void cargarListaAdmisionesPendientesRecep(List<Admision> admin){
		this.listaAdmisionesRecep.setModel(new ListModelList<Admision>(admin));
	}

	@Listen("onSeleccionar=[id$=listaAdmisionesRecep]")
	public void onClick_Seleccionar(ForwardEvent evt) {

		// Inicializar objeto a editar
		Admision admisionPendienteSelec = new Admision();

		try {
			// Obtener objeto seleccionado
			Toolbarbutton button = (Toolbarbutton) evt.getOrigin().getTarget();
			if (button != null) {
				Listitem listitem = (Listitem) button.getParent().getParent();
				if (listitem != null) {
					admisionPendienteSelec = (Admision) listitem
							.getValue();
				}
			}

			// Validar que se seleccione el objeto de la lista
			if (admisionPendienteSelec == null
					|| admisionPendienteSelec.getSecAdmision() <= 0) {
				Messagebox
						.show("La fila seleccionada no contiene información de la admisión. Favor verifique",
								Mensajes.REGISTRO_NO_SELECCIONADO,
								new Messagebox.Button[] { Messagebox.Button.OK },
								Messagebox.EXCLAMATION, null);
				return;
			} else {
				seleccionarAdmisionPendiente(admisionPendienteSelec);
				prepararNuevaRecepcion();
				limpiarCerrarPopRecep();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_GENERAR_PDF,	Mensajes.TipoMensaje.ERROR);
		}
	}
	
	private void seleccionarAdmisionPendiente(Admision admision) throws Exception{
		this.admisionPendienteEntrega = admision;
		this.txtFechaEntrada.setValue(UtilDate.DateToString(new Date(), "dd/MM/yyyy hh:mm a"));
		this.txtFechaSalida.setValue(UtilDate.DateToString(admisionPendienteEntrega.getFechaSalida(), "dd/MM/yyyy hh:mm a"));
		Utilidades.seleccionarItemEnCombo(cmbTipoConsulta, admisionPendienteEntrega.getTipoConsulta().getCodigo());
		UsuariosView usuarioRecibe = usuariosService.obtenerUsuarioById(admisionPendienteEntrega.getUsuarioRecibe().intValue());
		if (usuarioRecibe!=null){
			Utilidades.seleccionarItemEnCombo(cmbUsuarioSoli, usuarioRecibe.getId().toString());
		}
		if(admisionPendienteEntrega.getUsuarioEntrega()!=null){
			UsuariosView usuarioEntrega = usuariosService.obtenerUsuarioById(admisionPendienteEntrega.getUsuarioEntrega().intValue());
			this.txtUsuarioEntrega.setValue(usuarioEntrega.getNombre());
		}else{
			this.txtUsuarioEntrega.setValue("");
		}
	}
}
