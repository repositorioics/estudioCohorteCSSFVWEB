package ni.com.sts.estudioCohorteCssfv.controller.laboratorio;

//import ni.com.sts.estudioCohorteCssfv.datos.admision.AdmisionDA;
import java.util.ArrayList;
import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.Paciente;
import ni.com.sts.estudioCohorteCssfv.datos.laboratorio.LaboratorioDA;
import ni.com.sts.estudioCohorteCssfv.datos.paciente.PacienteDA;
import ni.com.sts.estudioCohorteCssfv.dto.OrdenesExamenes;
import ni.com.sts.estudioCohorteCssfv.servicios.LaboratorioService;
import ni.com.sts.estudioCohorteCssfv.servicios.PacienteService;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;

public class OrdenesExamenesController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	private static LaboratorioService laboratorioService = new LaboratorioDA();
	private static PacienteService pacienteService = new PacienteDA();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		init();
	}

	private void init() {
		retornaLista(null,null);
		//cargarExpedientes();
	}

	@Wire("[id$=listaOrdenesExamen]")
	private Listbox listaOrdenesExamen;
	
	@Wire("[id$=btnBuscar]")
    private Button btnBuscar;
    
	@Wire("[id$=cmbCodigoExpediente]")
    private Combobox cmbCodigoExpediente;
	
	@Wire("[id$=cmbEstado]")
    private Combobox cmbEstado;

	public void retornaLista(String codigoExpediente, String estado) {
		try {

			this.listaOrdenesExamen
					.setModel(new ListModelList<OrdenesExamenes>(
							new ArrayList<OrdenesExamenes>()));

			List<OrdenesExamenes> resultado = this.laboratorioService
					.listaPacienteLab(codigoExpediente,estado);
			if (resultado.size() > 0) {
				this.listaOrdenesExamen
						.setModel(new ListModelList<OrdenesExamenes>(resultado));
			}else if (codigoExpediente!=null){
				Mensajes.enviarMensaje(Mensajes.REGISTRO_NO_ENCONTRADO,	Mensajes.TipoMensaje.EXCLAM);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS,
					Mensajes.TipoMensaje.ERROR);
		}
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
    
    @Listen("onClick=[id$=btnBuscar]")
    public void btnBuscar_onClick() {
    	buscar();
    }
    
    private void buscar(){
    	String codigoExpediente = null, estado = null;
    	/*if (this.cmbCodigoExpediente.getSelectedItem()==null && this.cmbEstado.getSelectedIndex() <= 0) {
			Messagebox.show("Debe seleccionar al menos un filtro de búsqueda (Cód. Expediente, Estado orden)", "Validación", Messagebox.OK, Messagebox.INFORMATION);
            return;
		}else{*/
			if (this.cmbCodigoExpediente.getSelectedItem()!=null){
				codigoExpediente = this.cmbCodigoExpediente.getSelectedItem().getLabel().toString();
			}
			if (this.cmbEstado.getSelectedIndex() > 0){
				estado = this.cmbEstado.getSelectedItem().getValue().toString();
			}
			retornaLista(codigoExpediente,estado);
		//}
    }

	/**
	 * Método para editar grupos
	 * 
	 * @param evt
	 */
	@Listen("onEditarExamen=[id$=listaOrdenesExamen]")
	public void onClickEditar(ForwardEvent evt) {

		// Inicializar objeto a editar
		OrdenesExamenes ordenesExamenesSeleccionado = new OrdenesExamenes();

		try {
			// Obtener objeto seleccionado
			Toolbarbutton button = (Toolbarbutton) evt.getOrigin().getTarget();
			if (button != null) {
				Listitem listitem = (Listitem) button.getParent().getParent();
				if (listitem != null) {
					ordenesExamenesSeleccionado = (OrdenesExamenes) listitem
							.getValue();
				}
			}

			// Validar que se seleccione el objeto de la lista
			if (ordenesExamenesSeleccionado == null
					|| ordenesExamenesSeleccionado.getSecHojaConsulta() <= 0) {
				Messagebox
						.show("La fila seleccionada no contiene información de la Orden Examenes. Favor verifique",
								Mensajes.REGISTRO_NO_SELECCIONADO,
								new Messagebox.Button[] { Messagebox.Button.OK },
								Messagebox.EXCLAMATION, null);
				return;
			} else {
				Include include = (Include) Selectors
						.iterable(this.getPage(), "[id$=mainInclude]")
						.iterator().next();
				include.setAttribute("ordenExamenSeleccionada", ordenesExamenesSeleccionado);
				include.setSrc("/laboratorio/ingreso_resultados.zul");

			}

		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS,
					Mensajes.TipoMensaje.ERROR);
		}
	}
	
	public static String[] expedientes(){
		return pacienteService.getCodigosExpediente();
	}
}
