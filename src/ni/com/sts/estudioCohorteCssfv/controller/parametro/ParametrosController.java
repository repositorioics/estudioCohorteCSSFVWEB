package ni.com.sts.estudioCohorteCssfv.controller.parametro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.ParametrosSistemas;
import ni.com.sts.estudioCohorteCssfv.datos.parametro.ParametrosDA;
import ni.com.sts.estudioCohorteCssfv.dto.OrdenesExamenes;
import ni.com.sts.estudioCohorteCssfv.servicios.ParametroService;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilDate;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class ParametrosController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	private static ParametroService parametroService = new ParametrosDA();
	
	 @Override
	    public void doAfterCompose(Component comp) throws Exception{
	        super.doAfterCompose(comp);
	        init();
	    }

	    /**
	     *
	     */
	    private void init() {
	       cargarParametros();
	    }
	    
	    /** Componentes */
	    @Wire("[id$=txtCodigo]")
	    private Textbox txtCodigo;
	    
	    @Wire("[id$=txtNombre]")
	    private Textbox txtNombre;
	    
	    @Wire("[id$=txtValores]")
	    private Textbox txtValores;
	    
	    @Wire("[id$=txtDescripcion]")
	    private Textbox txtDescripcion;
	    
	    @Wire("[id$=btnGuardar]")
	    private Button btnGuardar;
	    
	    @Wire("[id$=btnLimpiar]")
	    private Button btnLimpiar;
	    
		@Wire("[id$=listaParametros]")
		private Listbox listaParametros;
	    
	    private void cargarParametros(){
			try {

				this.listaParametros.setModel(new ListModelList<OrdenesExamenes>(new ArrayList<OrdenesExamenes>()));

				@SuppressWarnings("static-access")
				List<ParametrosSistemas> resultado = this.parametroService.getListaParametros();
				if (resultado.size() > 0) {
					this.listaParametros.setModel(new ListModelList<ParametrosSistemas>(resultado));
				}
			} catch (Exception e) {
				e.printStackTrace();
				Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS,
						Mensajes.TipoMensaje.ERROR);
			}
	    }
	    
	    @Listen("onClick=[id$=btnLimpiar]")
	    public void btnLimpiar_onClick() {
	    	limpiar();	    	
	    }
	    
	    private void limpiar(){
	    	this.txtCodigo.setValue("");
			this.txtNombre.setValue("");
			this.txtValores.setValue("");
			this.txtDescripcion.setValue("");
	    	this.txtDescripcion.setReadonly(true);
			this.txtValores.setReadonly(true);
			this.btnGuardar.setDisabled(true);
	    }
	    
	    @Listen("onClick=[id$=btnGuardar]")
	    public void btnGuardar_onClick() throws WrongValueException, Exception {
	    	try{
	    		if (this.txtValores.getValue().length()<=0){
	            	Messagebox.show("Ingrese valor del parámetro", "Validación", Messagebox.OK, Messagebox.INFORMATION);
	                return;
	            }else{
			    	ParametrosSistemas parametroActualizar = parametroService.getParametroByName(this.txtNombre.getValue());
			    	if(parametroActualizar!=null){
			    		parametroActualizar.setValores(this.txtValores.getValue());
			    		parametroActualizar.setDescripcion(this.txtDescripcion.getValue());
			    		InfoResultado infoResultado = parametroService.actualizarParametro(parametroActualizar);
			    		Mensajes.enviarMensaje(infoResultado);
			    		if (infoResultado.isOk() && infoResultado.getObjeto()!=null){
			    			limpiar();
			    			cargarParametros();
			    		}
			    	}else{
			    		Messagebox.show("No se encontró parámetro a actualizar",
								Mensajes.REGISTRO_NO_ENCONTRADO,
								new Messagebox.Button[] { Messagebox.Button.OK },
								Messagebox.EXCLAMATION, null);
			    	}
	            }
	    	}catch(Throwable th){
	    		Messagebox.show("Sucedió un error al actualizar registro. \n "+ th.getMessage(),
						Mensajes.ERROR_ACTUALIZAR_REGISTRO,
						new Messagebox.Button[] { Messagebox.Button.OK },
						Messagebox.ERROR, null);
	    	}
	    	
	    }
	    
		/**
		 * Método para editar parámetro
		 * 
		 * @param evt
		 */
		@Listen("onEditarParametro=[id$=listaParametros]")
		public void onClickEditar(ForwardEvent evt) {

			// Inicializar objeto a editar
			ParametrosSistemas parametroSeleccionado = new ParametrosSistemas();

			try {
				// Obtener objeto seleccionado
				Toolbarbutton button = (Toolbarbutton) evt.getOrigin().getTarget();
				if (button != null) {
					Listitem listitem = (Listitem) button.getParent().getParent();
					if (listitem != null) {
						parametroSeleccionado = (ParametrosSistemas) listitem
								.getValue();
					}
				}

				// Validar que se seleccione el objeto de la lista
				if (parametroSeleccionado == null
						|| parametroSeleccionado.getSecParametro() <= 0) {
					Messagebox.show("La fila seleccionada no contiene información del parámetro. Favor verifique",
									Mensajes.REGISTRO_NO_SELECCIONADO,
									new Messagebox.Button[] { Messagebox.Button.OK },
									Messagebox.EXCLAMATION, null);
					return;
				} else {
					this.txtCodigo.setValue(String.valueOf(parametroSeleccionado.getCodigoParametro()));
					this.txtNombre.setValue(parametroSeleccionado.getNombreParametro());
					this.txtValores.setValue(parametroSeleccionado.getValores());
					this.txtDescripcion.setValue(parametroSeleccionado.getDescripcion());
					this.txtDescripcion.setReadonly(false);
					this.txtValores.setReadonly(false);
					this.btnGuardar.setDisabled(false);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS,
						Mensajes.TipoMensaje.ERROR);
			}
		}
}
