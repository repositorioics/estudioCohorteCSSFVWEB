package ni.com.sts.estudioCohorteCssfv.controller.reportes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.EstadosHoja;
import ni.com.sts.estudioCohorteCSSFV.modelo.Paciente;
import ni.com.sts.estudioCohorteCSSFV.modelo.ParametrosSistemas;
import ni.com.sts.estudioCohorteCSSFV.modelo.UsuariosView;
import ni.com.sts.estudioCohorteCssfv.datos.hojaConsulta.HojaConsultaDA;
import ni.com.sts.estudioCohorteCssfv.datos.paciente.PacienteDA;
import ni.com.sts.estudioCohorteCssfv.datos.parametro.ParametrosDA;
import ni.com.sts.estudioCohorteCssfv.datos.reportes.ReportesDA;
import ni.com.sts.estudioCohorteCssfv.datos.usuario.UsuariosDA;
import ni.com.sts.estudioCohorteCssfv.servicios.HojaConsultaService;
import ni.com.sts.estudioCohorteCssfv.servicios.PacienteService;
import ni.com.sts.estudioCohorteCssfv.servicios.ParametroService;
import ni.com.sts.estudioCohorteCssfv.servicios.ReportesService;
import ni.com.sts.estudioCohorteCssfv.servicios.UsuariosService;
import ni.com.sts.estudioCohorteCssfv.util.ExportarListBox;
import ni.com.sts.estudioCohorteCssfv.util.FiltroReporte;
import ni.com.sts.estudioCohorteCssfv.util.Generico;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilDate;
import ni.com.sts.estudioCohorteCssfv.util.UtilProperty;

import org.apache.commons.configuration.CompositeConfiguration;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;


public class ReporteGeneralController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	private static ReportesService reporteService = new ReportesDA();
	private static HojaConsultaService hojaConsultaService = new HojaConsultaDA();
	private static PacienteService pacienteService = new PacienteDA();
	private static UsuariosService usuariosService = new UsuariosDA();
	private static ParametroService parametroService = new ParametrosDA();

	private CompositeConfiguration config;
	
	private List<Generico> listaExportar = new ArrayList<Generico>();
	
	@Override
    public void doAfterCompose(Component comp) throws Exception{
        super.doAfterCompose(comp);
        init();
    }

    /**
     *
     */
    private void init() {
    	config = UtilProperty.getConfigurationfromExternalFile("EstudioCohorteCssfvWEBExt.properties");
    	cargarExpedientes();
    	cargarEstadosHoja();
    	cargarMedicos();
    	this.cmbEstadoAdmi.setSelectedIndex(0);
    }
    
    @Wire("[id$=cmbCodigoExpediente]")
    private Combobox cmbCodigoExpediente;    
    
    @Wire("[id$=cmbMedico]")
    private Combobox cmbMedico;    
    
    @Wire("[id$=txtFechaInicio]")
    private Datebox txtFechaInicio;
    
    @Wire("[id$=txtFechaFin]")
    private Datebox txtFechaFin;
    
    @Wire("[id$=cmbEstadoHoja]")
    private Combobox cmbEstadoHoja;
    
    @Wire("[id$=cmbEstadoAdmi]")
    private Combobox cmbEstadoAdmi;
    
	@Wire("[id$=listaResultados]")
	private Listbox listaResultados;

	@Wire("[id$=btnGenerar]")
    private Button btnGenerar;
    
    //@Wire("[id$=btnImprimir]")
    //private Button btnImprimir;
    
    @Wire("[id$=btnHojaExcel]")
    private Button btnHojaExcel;
    
    @Wire("[id$=btnLimpiar]")
    private Button btnLimpiar;    
    
    @Listen("onClick=[id$=btnGenerar]")
    public void btnGenerar_onClick() {
    	buscar();
    }
    
    @Listen("onClick=[id$=btnLimpiar]")
    public void btnLimpiar_onClick() {
    	limpiar();
    }
    
    @Listen("onClick=[id$=btnHojaExcel]")
    public void btnHojaExcel_onClick() {
    	exportarExcel();
    }
    
    private void limpiar(){
    	this.txtFechaInicio.setValue(null);
    	this.txtFechaFin.setValue(null);
    	//this.txtCodigoExpediente.setValue(null);
    	//this.txtMedico.setValue("");
    	this.cmbCodigoExpediente.setSelectedIndex(0);
    	this.cmbMedico.setSelectedIndex(0);
    	this.listaResultados.setModel(new ListModelList<Generico>(new ArrayList<Generico>()));
    	listaExportar = new ArrayList<Generico>();
    	this.cmbEstadoHoja.setSelectedIndex(0);
    	this.cmbEstadoAdmi.setSelectedIndex(0);
    	
    }
    private void cargarEstadosHoja(){
    	// Limpiar combo
    	EstadosHoja seleccione = new EstadosHoja();
    	seleccione.setCodigo('0');
    	seleccione.setDescripcion("Seleccione");
    	List<EstadosHoja> oList = new ArrayList<EstadosHoja>();
    	try {
			oList = hojaConsultaService.getEstadosHojaConsulta();
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje("Error consultar estados hoja consulta",
					Mensajes.TipoMensaje.ERROR);
		}
    	oList.add(0,seleccione);
    	
        this.cmbEstadoHoja.setModel(new ListModelList<EstadosHoja>(new ArrayList<EstadosHoja>()));
        this.cmbEstadoHoja.setModel(new ListModelList<EstadosHoja>(oList));
        this.cmbEstadoHoja.setItemRenderer(new ComboitemRenderer<EstadosHoja>() {
               @Override
               public void render(Comboitem comboitem, EstadosHoja fila, int index) throws Exception {
                   comboitem.setLabel( fila.getDescripcion());
                   comboitem.setValue(String.valueOf((fila.getCodigo())));
                   cmbEstadoHoja.setSelectedIndex(0);
           }});
    }
    
    private void cargarExpedientes(){
    	// Limpiar combo
    	Paciente seleccione = new Paciente();
    	seleccione.setCodExpediente(0);
    	List<Paciente> oList = new ArrayList<Paciente>();
    	try {
			oList = pacienteService.getPacientes();
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje("Error consultar expedientes",
					Mensajes.TipoMensaje.ERROR);
		}
    	oList.add(0,seleccione);
    	
        this.cmbCodigoExpediente.setModel(new ListModelList<Paciente>(new ArrayList<Paciente>()));
        this.cmbCodigoExpediente.setModel(new ListModelList<Paciente>(oList));
        this.cmbCodigoExpediente.setItemRenderer(new ComboitemRenderer<Paciente>() {
               @Override
               public void render(Comboitem comboitem, Paciente fila, int index) throws Exception {
                   comboitem.setLabel(String.valueOf(fila.getCodExpediente()));
                   comboitem.setValue(String.valueOf((fila.getCodExpediente())));
                   cmbCodigoExpediente.setSelectedIndex(0);
           }});
    }
    
    private void cargarMedicos(){
    	// Limpiar combo
    	UsuariosView seleccione = new UsuariosView();
    	seleccione.setId(0);
    	seleccione.setNombre("Seleccione");
    	List<UsuariosView> oList = new ArrayList<UsuariosView>();
    	try {
    		ParametrosSistemas perfilMedico = parametroService.getParametroByName("PERFIL_MEDICO");
	    	if(perfilMedico!=null){
				oList = usuariosService.obtenerUsuariosByPerfil(perfilMedico.getValores());
	    	}else{
	    		Messagebox.show("No se encontró parámetro perfil médico",
						Mensajes.REGISTRO_NO_ENCONTRADO,
						new Messagebox.Button[] { Messagebox.Button.OK },
						Messagebox.EXCLAMATION, null);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje("Error consultar estados hoja consulta",
					Mensajes.TipoMensaje.ERROR);
		}
    	oList.add(0,seleccione);
    	
        this.cmbMedico.setModel(new ListModelList<UsuariosView>(new ArrayList<UsuariosView>()));
        this.cmbMedico.setModel(new ListModelList<UsuariosView>(oList));
        this.cmbMedico.setItemRenderer(new ComboitemRenderer<UsuariosView>() {
               @Override
               public void render(Comboitem comboitem, UsuariosView fila, int index) throws Exception {
                   comboitem.setLabel( fila.getNombre());
                   comboitem.setValue(String.valueOf((fila.getId())));
                   cmbMedico.setSelectedIndex(0);
           }});
    }
    
   
	private void buscar(){
    	try {
    		
			this.listaResultados.setModel(new ListModelList<Generico>(new ArrayList<Generico>()));

        	Date hoy = UtilDate.StringToDate(UtilDate.DateToString(new Date(), "dd/MM/yyyy"), "dd/MM/yyyy");
        	
        	Date fechaInicio = null;
        	Date fechaFin = null; 
        	
        	try{
	        	if (this.txtFechaInicio.getValue()!=null)
	        		fechaInicio = new Date(this.txtFechaInicio.getValue().getTime());
        	}catch(WrongValueException wex){
        		return;
        	}
        	try{
	        	if (this.txtFechaFin.getValue()!=null)
	        		fechaFin = new Date(this.txtFechaFin.getValue().getTime());
        	}catch(WrongValueException wex){
        		return;
        	}
        	if (fechaInicio == null && fechaFin!=null){
        		Messagebox.show("Favor ingresar fecha de inicio de consulta", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
        	}
        	if (fechaInicio != null && fechaFin==null){
        		Messagebox.show("Favor ingresar fecha de fin de consulta", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
        	}
        	if (fechaInicio != null && fechaInicio.compareTo(hoy)>0){
        		Messagebox.show("Fecha de inicio no puede ser mayor a fecha actual", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
        	}
        	if (fechaInicio != null && fechaFin!=null && fechaInicio.compareTo(fechaFin)>0){
        		Messagebox.show("Fecha de inicio no puede ser mayor a fecha de fin", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
        	}
        	
			FiltroReporte filtro = new FiltroReporte();
			String campoFiltro = "";
			/*try{
				if (this.txtCodigoExpediente.getValue()!=null){				
					campoFiltro = this.txtCodigoExpediente.getValue().toString();	
				}
			}catch(WrongValueException ex){
				return;
			}*/
			if (this.cmbCodigoExpediente.getSelectedIndex() > 0) {
				campoFiltro = this.cmbCodigoExpediente.getSelectedItem().getValue().toString();
				if (!campoFiltro.isEmpty()){
					filtro.setCodigoExpediente(Integer.valueOf(campoFiltro));
				}
			}
			
			if (this.cmbMedico.getSelectedIndex() > 0) {
				campoFiltro = this.cmbMedico.getSelectedItem().getValue().toString();
				if (!campoFiltro.isEmpty()){
					filtro.setMedico(campoFiltro.toLowerCase());
					filtro.setMostrarTodo(false);
				}
			}
			/*campoFiltro = this.txtFechaInicio.getValue().toString();
			if (!campoFiltro.isEmpty()){
				filtro.setFechaInicioConsulta(UtilDate.StringToDate(campoFiltro,"dd/MM/yyyy"));
				filtro.setMostrarTodo(false);
			}
			campoFiltro = this.txtFechaFin.getValue();
			if (!campoFiltro.isEmpty()){
				filtro.setFechaFinConsulta(UtilDate.StringToDate(campoFiltro,"dd/MM/yyyy"));
				filtro.setMostrarTodo(false);
			}*/
			if (fechaInicio!=null){
				filtro.setFechaInicioConsulta(fechaInicio);
				filtro.setMostrarTodo(false);
			}
			if (fechaFin!=null){
				filtro.setFechaFinConsulta(fechaFin);
				filtro.setMostrarTodo(false);
			}
			if (this.cmbEstadoHoja.getSelectedIndex() > 0) {
				campoFiltro = this.cmbEstadoHoja.getSelectedItem().getValue().toString();
				if (!campoFiltro.isEmpty()){
					filtro.setEstadoHojaConsulta(campoFiltro);
					filtro.setMostrarTodo(false);
				}
			}
			
			if (this.cmbEstadoAdmi.getSelectedIndex() > 0) {
				campoFiltro = this.cmbEstadoAdmi.getSelectedItem().getValue().toString();
				if (!campoFiltro.isEmpty()){
					filtro.setEstadoAdmision(campoFiltro);
					filtro.setMostrarTodo(false);
				}
			}
			
			listaExportar = reporteService.getReporteGeneral(filtro);
			if (listaExportar.size() > 0) {
				this.listaResultados.setModel(new ListModelList<Generico>(listaExportar));
			}else{
				Mensajes.enviarMensaje(Mensajes.REGISTRO_NO_ENCONTRADO,	Mensajes.TipoMensaje.EXCLAM);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS,
					Mensajes.TipoMensaje.ERROR);
		}
    }
    
    private void exportarExcel(){
    	Generico[] resultado = (Generico[]) ExportarListBox.copyArrayListObject(listaExportar.toArray(), Generico.class);
    	String [] metodos = {"getTexto1","getTexto2","getTexto5","getTexto6","getTexto7","getTexto3","getTexto9","getTexto8","getTexto4","getTexto10","getTexto11","getTexto12"};
		try {
			ExportarListBox.export_to_csv(listaResultados,metodos,resultado,"reporteGeneral","Consultas Encontradas");
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_EXPORTAR_EXCEL + ". \n " +e.getMessage() , Mensajes.TipoMensaje.ERROR);
		}
    }
}
