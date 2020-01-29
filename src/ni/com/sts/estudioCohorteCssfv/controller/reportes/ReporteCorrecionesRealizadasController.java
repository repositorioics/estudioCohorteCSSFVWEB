package ni.com.sts.estudioCohorteCssfv.controller.reportes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import ni.com.sts.estudioCohorteCssfv.datos.reportes.ReportesDA;
import ni.com.sts.estudioCohorteCssfv.servicios.ReportesService;
import ni.com.sts.estudioCohorteCssfv.util.ExportarListBox;
import ni.com.sts.estudioCohorteCssfv.util.Generico;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilDate;
import ni.com.sts.estudioCohorteCssfv.util.UtilProperty;

public class ReporteCorrecionesRealizadasController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	private static ReportesService reporteService = new ReportesDA();
	
	private CompositeConfiguration config;
	
	private List<Generico> listaExportar = new ArrayList<Generico>();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		init();
	}
	
	private void init() {
    	config = UtilProperty.getConfigurationfromExternalFile("EstudioCohorteCssfvWEBExt.properties");
    }
	
	@Wire("[id$=listaResultados]")
	private Listbox listaResultados;
	
	@Wire("[id$=btnGenerar]")
    private Button btnGenerar;
    
    @Wire("[id$=btnHojaExcel]")
    private Button btnHojaExcel;
    
    @Wire("[id$=txtFechaInicio]")
    private Datebox txtFechaInicio;
    
    @Wire("[id$=txtFechaFin]")
    private Datebox txtFechaFin;
    
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
    }
    
    private void buscar() {
    	try {
        	this.listaResultados.setModel(new ListModelList<Generico>(new ArrayList<Generico>()));
        	
        	Date hoy = UtilDate.StringToDate(UtilDate.DateToString(new Date(), "dd/MM/yyyy"), "dd/MM/yyyy");
        	
        	Date fechaInicio = null;
        	Date fechaFin = null; 
        	try{
            	if (this.txtFechaInicio.getValue()!=null) {
            		fechaInicio = new Date(this.txtFechaInicio.getValue().getTime());
            	}
            		
        	}catch(WrongValueException wex){
        		return;
        	}
        	try{
            	if (this.txtFechaFin.getValue()!=null)
            		fechaFin = new Date(this.txtFechaFin.getValue().getTime());
        	}catch(WrongValueException wex){
        		return;
        	}
        	if (fechaInicio == null && fechaFin==null){
        		Messagebox.show("Favor ingresar las fechas", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
        	}
        	if (fechaInicio == null && fechaFin!=null){
        		Messagebox.show("Favor ingresar fecha de inicio", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
        	}
        	if (fechaInicio != null && fechaFin==null){
        		Messagebox.show("Favor ingresar fecha de fin", "Validación", Messagebox.OK, Messagebox.INFORMATION);
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
    		listaExportar = reporteService.getReporteCorrecciones(fechaInicio, fechaFin);
			this.listaResultados.setModel(new ListModelList<Generico>(listaExportar));
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}	
    }
    
    private void exportarExcel() {
    	//Date fechaInicio = new Date(this.txtFechaInicio.getValue().getTime());
    	//Date fechaFin = new Date(this.txtFechaFin.getValue().getTime());
    	Generico[] resultado = (Generico[]) ExportarListBox.copyArrayListObject(listaExportar.toArray(), Generico.class);
    	String [] metodos = {"getTexto1","getTexto2","getTexto3","getTexto4"};
    	Date fechaInicio = null;
    	Date fechaFin = null;
    	fechaInicio = new Date(this.txtFechaInicio.getValue().getTime());
    	fechaFin = new Date(this.txtFechaFin.getValue().getTime());
		try {
			ExportarListBox.export_to_csv_con_fechas(listaResultados,metodos,resultado,"reporteCorreccionesRealizadas","REPORTE CORRECCIONES REALIZADAS A MEDICOS",fechaInicio,fechaFin);
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_EXPORTAR_EXCEL + ". \n " +e.getMessage() , Mensajes.TipoMensaje.ERROR);
		}
    }

}
