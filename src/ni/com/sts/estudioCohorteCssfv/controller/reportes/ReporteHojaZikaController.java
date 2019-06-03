package ni.com.sts.estudioCohorteCssfv.controller.reportes;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import ni.com.sts.estudioCohorteCssfv.datos.reportes.ReportesDA;
import ni.com.sts.estudioCohorteCssfv.servicios.ReportesService;
import ni.com.sts.estudioCohorteCssfv.util.ExportarListBox;
import ni.com.sts.estudioCohorteCssfv.util.Generico;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilProperty;

public class ReporteHojaZikaController extends SelectorComposer<Component>{
	
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
	
	@Wire("[id$=cmbTipoEstado]")
    private Combobox cmbTipoEstado;
	
	@Wire("[id$=listaResultados]")
	private Listbox listaResultados;
	
	@Wire("[id$=btnGenerar]")
    private Button btnGenerar;
    
    @Wire("[id$=btnHojaExcel]")
    private Button btnHojaExcel;
    
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
    
    private void limpiar() {
    	this.listaResultados.setModel(new ListModelList<Generico>(new ArrayList<Generico>()));
    	listaExportar = new ArrayList<Generico>();
    	this.cmbTipoEstado.setSelectedIndex(0);
	}
    
    private void buscar() {
    	this.listaResultados.setModel(new ListModelList<Generico>(new ArrayList<Generico>()));
    	String estado = "";
    	try {
        	if (this.cmbTipoEstado.getSelectedIndex() > 0) {
        		estado = cmbTipoEstado.getSelectedItem().getValue().toString();
    			if (!estado.isEmpty()){
    				listaExportar = reporteService.getReporteHojaZika(estado);
    			}
    		}
        	if (listaExportar.size() > 0) {
				this.listaResultados.setModel(new ListModelList<Generico>(listaExportar));
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}	
    }
    
    private void exportarExcel(){
    	Generico[] resultado = (Generico[]) ExportarListBox.copyArrayListObject(listaExportar.toArray(), Generico.class);
    	String [] metodos = {"getTexto1","getTexto2","getTexto3","getTexto4","getTexto5","getTexto6","getTexto7","getTexto8","getTexto9","getTexto10"};
		try {
			ExportarListBox.export_to_csv(listaResultados,metodos,resultado,"reporteHojaZika","Registros Encontrados");
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_EXPORTAR_EXCEL + ". \n " +e.getMessage() , Mensajes.TipoMensaje.ERROR);
		}
    }


}
