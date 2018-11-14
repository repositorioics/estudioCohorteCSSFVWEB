package ni.com.sts.estudioCohorteCssfv.controller.mantenedores;

import java.util.ArrayList;
import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.TipoConsulta;
import ni.com.sts.estudioCohorteCssfv.datos.admision.TipoConsultaDA;
import ni.com.sts.estudioCohorteCssfv.servicios.TipoConsultaService;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class TipoConsultaController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	private static TipoConsultaService tipoConsultaService = new TipoConsultaDA();
	//private List<TipoConsulta> listaExportar = new ArrayList<TipoConsulta>();
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
    	cargarTiposConsulta();
    }
    
    @Wire("[id$=txtSecuencia]")
    private Textbox txtSecuencia;    
    
    @Wire("[id$=txtDescripcion]")
    private Textbox txtDescripcion;    
    
    @Wire("[id$=txtCodigo]")
    private Textbox txtCodigo;
    
    @Wire("[id$=cmbEstado]")
    private Combobox cmbEstado;
    
	@Wire("[id$=listaTipoConsulta]")
	private Listbox listaTipoConsulta;

    @Wire("[id$=btnGuardar]")
    private Button btnGuardar;
    
    @Wire("[id$=btnBuscar]")
    private Button btnBuscar;
    
    @Wire("[id$=btnNuevo]")
    private Button btnNuevo;
    
    /*@Wire("[id$=btnHojaExcel]")
    private Button btnHojaExcel;*/
    
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

			this.listaTipoConsulta.setModel(new ListModelList<Generico>(new ArrayList<Generico>()));
			List<TipoConsulta> resultado;
			String patron = this.txtDescripcion.getValue().toString().toLowerCase();
			if (!patron.isEmpty()){
				resultado = this.tipoConsultaService.getTipoConsultaByPatron(patron);
			}else{
				resultado = tipoConsultaService.getTiposConsultas();
			}
			//listaExportar = resultado;
			if (resultado.size() > 0) {
				this.listaTipoConsulta.setModel(new ListModelList<TipoConsulta>(resultado));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS,
					Mensajes.TipoMensaje.ERROR);
		}
    }
    
    /*@Listen("onClick=[id$=btnHojaExcel]")
    public void btnHojaExcel_onClick() {
    	Diagnostico[] resultado = (Diagnostico[]) ExportarListBox.copyArrayListObject(listaExportar.toArray(), Diagnostico.class);
    	String [] metodos = {"getDiagnostico","getCodigoDignostico","getEstado"};
		try {
			ExportarListBox.export_to_csv(listaTipoConsulta,metodos,resultado,"diagnosticos","Diagnósticos Registrados");
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_EXPORTAR_EXCEL + ". \n " +e.getMessage() , Mensajes.TipoMensaje.ERROR);
		}
    }*/
    
    @Listen("onClick=[id$=btnGuardar]")
    public void btnGuardar_onClick() {
        try {
        	if (this.txtDescripcion.getValue().length()<=0){
            	Messagebox.show("Favor ingrese nombre diagnóstico", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
            }
        	if (this.txtCodigo.getValue().length()<=0){
            	Messagebox.show("Favor ingrese código", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
            }
        	if (this.cmbEstado.getSelectedIndex() <= 0) {
        		Messagebox.show("Seleccione estado", "Validación", Messagebox.OK, Messagebox.INFORMATION);
        		return;
            }

        	if (tipoConsultaService.existeTipoConsulta(this.txtCodigo.getValue().toString(),Integer.valueOf(this.txtSecuencia.getValue()))!=null){
            	Messagebox.show("Código de tipo de consulta ingresado ya está registrado", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
            }
        	TipoConsulta tipoConsulta = tipoConsultaService.getTipoConsultaById(Integer.valueOf(this.txtSecuencia.getValue()));
        	if (tipoConsulta==null)
        		tipoConsulta = new TipoConsulta();
        	
        	tipoConsulta.setDescripcion(this.txtDescripcion.getValue());
        	tipoConsulta.setCodigo(this.txtCodigo.getValue().toString());
        	tipoConsulta.setEstado((char)this.cmbEstado.getSelectedItem().getValue().toString().charAt(0));
        	InfoResultado resultado;
        	if (this.txtSecuencia.getValue().equals("0")){       		 
        		resultado = tipoConsultaService.guardarTipoConsulta(tipoConsulta);
        	}else{
         		resultado = tipoConsultaService.actualizarTipoConsulta(tipoConsulta);
        	}
        	Mensajes.enviarMensaje(resultado);
        	if(resultado.isOk() && resultado.getObjeto()!=null){
        		limpiar();
        		cargarTiposConsulta();
        	}
        	
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_PROCESAR_DATOS + ". " +e.getMessage() , Mensajes.TipoMensaje.ERROR);
		}
    }
    
	@Listen("onEditarDx=[id$=listaTipoConsulta]")
	public void onClickEditar(ForwardEvent evt) {

		// Inicializar objeto a editar
		TipoConsulta dxSeleccionado = new TipoConsulta();

		try {
			// Obtener objeto seleccionado
			Toolbarbutton button = (Toolbarbutton) evt.getOrigin().getTarget();
			if (button != null) {
				Listitem listitem = (Listitem) button.getParent().getParent();
				if (listitem != null) {
					dxSeleccionado = (TipoConsulta) listitem.getValue();
				}
			}

			// Validar que se seleccione el objeto de la lista
			if (dxSeleccionado == null
					|| dxSeleccionado.getSecTipocon() <= 0) {
				Messagebox.show("La fila seleccionada no contiene información del tipo de consulta. Favor verifique",
								Mensajes.REGISTRO_NO_SELECCIONADO,
								new Messagebox.Button[] { Messagebox.Button.OK },
								Messagebox.EXCLAMATION, null);
				return;
			} else {
				Utilidades.seleccionarItemEnCombo(cmbEstado, String.valueOf(dxSeleccionado.getEstado()));
				this.txtDescripcion.setValue(dxSeleccionado.getDescripcion());
				this.txtCodigo.setValue(String.valueOf(dxSeleccionado.getCodigo()));
				this.txtSecuencia.setValue(String.valueOf(dxSeleccionado.getSecTipocon()));
				this.txtCodigo.setReadonly(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS,
					Mensajes.TipoMensaje.ERROR);
		}
	}
	
    private void cargarTiposConsulta(){
    	try {

			this.listaTipoConsulta.setModel(new ListModelList<Generico>(new ArrayList<Generico>()));

			@SuppressWarnings("static-access")
			List<TipoConsulta> resultado = this.tipoConsultaService.getTiposConsultas();
			//listaExportar = resultado;
			if (resultado.size() > 0) {
				this.listaTipoConsulta.setModel(new ListModelList<TipoConsulta>(resultado));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS,
					Mensajes.TipoMensaje.ERROR);
		}
    }
    
    private void limpiar(){
    	this.txtDescripcion.setValue("");
    	this.txtCodigo.setValue("");
    	this.txtSecuencia.setValue("0");
    	this.cmbEstado.setSelectedIndex(0);
    }
}
