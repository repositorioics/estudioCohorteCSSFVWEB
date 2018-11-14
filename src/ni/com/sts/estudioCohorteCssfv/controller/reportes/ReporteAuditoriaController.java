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


public class ReporteAuditoriaController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	private static ReportesService reporteService = new ReportesDA();

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
    }
 
    
    @Wire("[id$=txtFechaInicio]")
    private Datebox txtFechaInicio;
    
    @Wire("[id$=txtFechaFin]")
    private Datebox txtFechaFin;
    
    @Wire("[id$=cmbTipoControl]")
    private Combobox cmbTipoControl;
    
	@Wire("[id$=listaResultados]")
	private Listbox listaResultados;

	@Wire("[id$=btnGenerar]")
    private Button btnGenerar;
    
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
    	this.listaResultados.setModel(new ListModelList<Generico>(new ArrayList<Generico>()));
    	listaExportar = new ArrayList<Generico>();
    	this.cmbTipoControl.setSelectedIndex(0);
    	
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
		
			if (fechaInicio!=null){
				filtro.setFechaInicioConsulta(fechaInicio);
			}
			if (fechaFin!=null){
				filtro.setFechaFinConsulta(fechaFin);
			}
			if (this.cmbTipoControl.getSelectedIndex() > 0) {
				campoFiltro = this.cmbTipoControl.getSelectedItem().getValue().toString();
				if (!campoFiltro.isEmpty()){
					filtro.setTipoControl(campoFiltro);
				}
			}		
			
			listaExportar = reporteService.getReporteAuditoria(filtro);
			
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
    	String [] metodos = {"getTexto1","getTexto2","getTexto3","getTexto4","getTexto5","getTexto6","getTexto7"};
		try {
			ExportarListBox.export_to_csv(listaResultados,metodos,resultado,"reporteAuditoria","Registros Encontrados");
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_EXPORTAR_EXCEL + ". \n " +e.getMessage() , Mensajes.TipoMensaje.ERROR);
		}
    }
}
