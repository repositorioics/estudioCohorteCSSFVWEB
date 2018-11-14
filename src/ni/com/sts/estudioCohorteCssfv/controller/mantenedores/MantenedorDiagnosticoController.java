package ni.com.sts.estudioCohorteCssfv.controller.mantenedores;

import java.util.ArrayList;
import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.Diagnostico;
import ni.com.sts.estudioCohorteCssfv.datos.diagnostico.DiagnosticoDA;
import ni.com.sts.estudioCohorteCssfv.servicios.DiagnosticoService;
import ni.com.sts.estudioCohorteCssfv.util.ExportarListBox;
import ni.com.sts.estudioCohorteCssfv.util.Generico;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.Utilidades;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class MantenedorDiagnosticoController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	private static DiagnosticoService diagnosticoService = new DiagnosticoDA();
	private List<Diagnostico> listaExportar = new ArrayList<Diagnostico>();
	@Override
    public void doAfterCompose(Component comp) throws Exception{
        super.doAfterCompose(comp);
        init();
    }

    /**
     *
     */
    private void init() {
    	limpiar();
    	cargarDiagnosticos();
    }
    
    @Wire("[id$=txtSecuencia]")
    private Textbox txtSecuencia;    
    
    @Wire("[id$=txtDiagnostico]")
    private Textbox txtDiagnostico;    
    
    @Wire("[id$=txtCodigo]")
    private Intbox txtCodigo;
    
    @Wire("[id$=cmbEstado]")
    private Combobox cmbEstado;
    
	@Wire("[id$=listaDx]")
	private Listbox listaDx;

    @Wire("[id$=btnGuardar]")
    private Button btnGuardar;
    
    @Wire("[id$=btnBuscar]")
    private Button btnBuscar;
    
    @Wire("[id$=btnNuevo]")
    private Button btnNuevo;
    
    @Wire("[id$=btnHojaExcel]")
    private Button btnHojaExcel;
    
    @Listen("onClick=[id$=btnNuevo]")
    public void btnNuevo_onClick() {
    	limpiar();
    	this.txtCodigo.setReadonly(false);
    }
    
    @Listen("onClick=[id$=btnBuscar]")
    public void btnBuscar_onClick() {
    	buscar();
    }
    
    @SuppressWarnings("static-access")	
    private void buscar(){
    	try {

			this.listaDx.setModel(new ListModelList<Generico>(new ArrayList<Generico>()));
			List<Diagnostico> resultado;
			String patron = this.txtDiagnostico.getValue().toString().toLowerCase();
			if (!patron.isEmpty()){
				resultado = this.diagnosticoService.getDiagnosticoByPatron(patron);
			}else{
				resultado = diagnosticoService.getDiagnosticos();
			}
			listaExportar = resultado;
			if (resultado.size() > 0) {
				this.listaDx.setModel(new ListModelList<Diagnostico>(resultado));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS,
					Mensajes.TipoMensaje.ERROR);
		}
    }
    
    @Listen("onClick=[id$=btnHojaExcel]")
    public void btnHojaExcel_onClick() {
    	Diagnostico[] resultado = (Diagnostico[]) ExportarListBox.copyArrayListObject(listaExportar.toArray(), Diagnostico.class);
    	String [] metodos = {"getDiagnostico","getCodigoDignostico","getEstado"};
		try {
			ExportarListBox.export_to_csv(listaDx,metodos,resultado,"diagnosticos","Diagnósticos Registrados");
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_EXPORTAR_EXCEL + ". \n " +e.getMessage() , Mensajes.TipoMensaje.ERROR);
		}
    }
    
    @Listen("onClick=[id$=btnGuardar]")
    public void btnGuardar_onClick() {
        try {
        	if (this.txtDiagnostico.getValue().length()<=0){
            	Messagebox.show("Favor ingrese nombre diagnóstico", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
            }
        	if (this.txtCodigo.getValue()==null){
            	Messagebox.show("Favor ingrese código", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
            }
        	if (this.cmbEstado.getSelectedIndex() <= 0) {
        		Messagebox.show("Seleccione estado", "Validación", Messagebox.OK, Messagebox.INFORMATION);
        		return;
            }

        	if (diagnosticoService.getDiagnosticoByCodigo(Integer.valueOf(this.txtCodigo.getValue().toString()),Integer.valueOf(this.txtSecuencia.getValue()))!=null){
            	Messagebox.show("Código de diagnóstico ingresado ya está registrado", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
            }
        	Diagnostico diagnostico = diagnosticoService.getDiagnosticoById(Integer.valueOf(this.txtSecuencia.getValue()));
        	if (diagnostico==null)
        		diagnostico = new Diagnostico();
        	
        	diagnostico.setDiagnostico(this.txtDiagnostico.getValue());
        	diagnostico.setCodigoDignostico(Short.valueOf(this.txtCodigo.getValue().toString()));
        	diagnostico.setEstado((char)this.cmbEstado.getSelectedItem().getValue().toString().charAt(0));
        	InfoResultado resultado = diagnosticoService.guardarDiagnostico(diagnostico);
        	Mensajes.enviarMensaje(resultado);
        	if(resultado.isOk() && resultado.getObjeto()!=null){
        		limpiar();
        		cargarDiagnosticos();
            	//this.txtCodigo.setReadonly(true);
            	//this.cmbEstado.setDisabled(true);
        	}
        	
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_PROCESAR_DATOS + ". " +e.getMessage() , Mensajes.TipoMensaje.ERROR);
		}
    }
    
	@Listen("onEditarDx=[id$=listaDx]")
	public void onClickEditar(ForwardEvent evt) {

		// Inicializar objeto a editar
		Diagnostico dxSeleccionado = new Diagnostico();

		try {
			// Obtener objeto seleccionado
			Toolbarbutton button = (Toolbarbutton) evt.getOrigin().getTarget();
			if (button != null) {
				Listitem listitem = (Listitem) button.getParent().getParent();
				if (listitem != null) {
					dxSeleccionado = (Diagnostico) listitem.getValue();
				}
			}

			// Validar que se seleccione el objeto de la lista
			if (dxSeleccionado == null
					|| dxSeleccionado.getSecDiagnostico() <= 0) {
				Messagebox.show("La fila seleccionada no contiene información del diagnóstico. Favor verifique",
								Mensajes.REGISTRO_NO_SELECCIONADO,
								new Messagebox.Button[] { Messagebox.Button.OK },
								Messagebox.EXCLAMATION, null);
				return;
			} else {
				Utilidades.seleccionarItemEnCombo(cmbEstado, String.valueOf(dxSeleccionado.getEstado()));
				this.txtDiagnostico.setValue(dxSeleccionado.getDiagnostico());
				this.txtCodigo.setValue(Integer.valueOf(dxSeleccionado.getCodigoDignostico()));
				this.txtSecuencia.setValue(String.valueOf(dxSeleccionado.getSecDiagnostico()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS,
					Mensajes.TipoMensaje.ERROR);
		}
	}
	
    private void cargarDiagnosticos(){
    	try {

			this.listaDx.setModel(new ListModelList<Generico>(new ArrayList<Generico>()));

			@SuppressWarnings("static-access")
			List<Diagnostico> resultado = this.diagnosticoService.getDiagnosticos();
			listaExportar = resultado;
			if (resultado.size() > 0) {
				this.listaDx.setModel(new ListModelList<Diagnostico>(resultado));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS,
					Mensajes.TipoMensaje.ERROR);
		}
    }
    
    private void limpiar(){
    	this.txtDiagnostico.setValue("");
    	this.txtCodigo.setValue(null);
    	this.txtSecuencia.setValue("0");
    	this.cmbEstado.setSelectedIndex(0);
    }
}
