package ni.com.sts.estudioCohorteCssfv.controller.mantenedores;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.HorarioAtencion;
import ni.com.sts.estudioCohorteCssfv.datos.horarioAtencion.HorarioAtencionDA;
import ni.com.sts.estudioCohorteCssfv.servicios.HorarioAtencionService;
import ni.com.sts.estudioCohorteCssfv.util.Generico;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilDate;
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

public class HorarioAtencionController extends SelectorComposer<Component> {
    private static final long serialVersionUID = 1L;
    private static HorarioAtencionService horarioAtencionService = new HorarioAtencionDA();
    
    @Override
    public void doAfterCompose(Component comp) throws Exception{
        super.doAfterCompose(comp);
        init();
    }

    /**
     *
     */
    private void init() {
    	cargarHorariosAtencion();
    	cmbDia.setSelectedIndex(0);
    	cmbTurno.setSelectedIndex(0);
    	cmbPeriodoHI.setSelectedIndex(0);
    	cmbPeriodoHF.setSelectedIndex(0);
    }
    
    @Wire("[id$=txtSecuencia]")
    private Textbox txtSecuencia;    
    
    @Wire("[id$=txtHoraInicio]")
    private Textbox txtHoraInicio;
    
    @Wire("[id$=txtHoraFin]")
    private Textbox txtHoraFin;
    
    @Wire("[id$=cmbTurno]")
    private Combobox cmbTurno;
    
    @Wire("[id$=cmbDia]")
    private Combobox cmbDia;
    
    @Wire("[id$=cmbPeriodoHI]")
    private Combobox cmbPeriodoHI;
    
    @Wire("[id$=cmbPeriodoHF]")
    private Combobox cmbPeriodoHF;
    
	@Wire("[id$=listaHorarios]")
	private Listbox listaHorarios;

    @Wire("[id$=btnGuardar]")
    private Button btnGuardar;
    
    @Wire("[id$=btnNuevo]")
    private Button btnNuevo;
    
    private void cargarHorariosAtencion(){
		try {

			this.listaHorarios.setModel(new ListModelList<Generico>(new ArrayList<Generico>()));

			@SuppressWarnings("static-access")
			List<Generico> resultado = this.horarioAtencionService.getListaHorariosAtencion();
			if (resultado.size() > 0) {
				this.listaHorarios.setModel(new ListModelList<Generico>(resultado));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS,
					Mensajes.TipoMensaje.ERROR);
		}
    }
    
	/**
	 * Método para editar parámetro
	 * 
	 * @param evt
	 */
	@Listen("onEditarHorario=[id$=listaHorarios]")
	public void onClickEditar(ForwardEvent evt) {

		// Inicializar objeto a editar
		Generico horarioSeleccionado = new Generico();

		try {
			// Obtener objeto seleccionado
			Toolbarbutton button = (Toolbarbutton) evt.getOrigin().getTarget();
			if (button != null) {
				Listitem listitem = (Listitem) button.getParent().getParent();
				if (listitem != null) {
					horarioSeleccionado = (Generico) listitem.getValue();
				}
			}

			// Validar que se seleccione el objeto de la lista
			if (horarioSeleccionado == null
					|| horarioSeleccionado.getNumero1() <= 0) {
				Messagebox.show("La fila seleccionada no contiene información del horario. Favor verifique",
								Mensajes.REGISTRO_NO_SELECCIONADO,
								new Messagebox.Button[] { Messagebox.Button.OK },
								Messagebox.EXCLAMATION, null);
				return;
			} else {
				Utilidades.seleccionarItemEnCombo(cmbTurno, horarioSeleccionado.getTexto10());
				Utilidades.seleccionarItemEnCombo(cmbDia, horarioSeleccionado.getTexto11());
				String horaInicio = UtilDate.DateToString(horarioSeleccionado.getFecha1(), "hh:mm a");
				String horaFin = UtilDate.DateToString(horarioSeleccionado.getFecha2(), "hh:mm a");
				String[] arrayHI = horaInicio.split(" ");
				String[] arrayHF = horaFin.split(" ");
				Utilidades.seleccionarItemEnCombo(cmbPeriodoHI, arrayHI[1]);
				Utilidades.seleccionarItemEnCombo(cmbPeriodoHF, arrayHF[1]);
				this.txtHoraInicio.setValue(arrayHI[0]);
				this.txtHoraFin.setValue(arrayHF[0]);
				this.txtSecuencia.setValue(String.valueOf(horarioSeleccionado.getNumero1()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_CONSULTAR_DATOS,
					Mensajes.TipoMensaje.ERROR);
		}
	}

    @Listen("onClick=[id$=btnNuevo]")
    public void btnNuevo_onClick() {
    	limpiar();
    }
    
    @Listen("onClick=[id$=btnGuardar]")
    public void btnGuardar_onClick() {
        try {
        	if (this.cmbTurno.getSelectedIndex() <= 0) {
        		Messagebox.show("Seleccione turno", "Validación", Messagebox.OK, Messagebox.INFORMATION);
        		return;
            }
        	if (this.cmbDia.getSelectedIndex() <= 0) {
        		Messagebox.show("Seleccione día", "Validación", Messagebox.OK, Messagebox.INFORMATION);
        		return;
            }
        	if (this.txtHoraInicio.getValue().length()<=0){
            	Messagebox.show("Ingrese hora de inicio", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
            }
        	if (this.txtHoraFin.getValue().length()<=0){
            	Messagebox.show("Ingrese hora de fin", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
            }
        	Date horaInicio = UtilDate.StringToDate(UtilDate.DateToString(new Date(), "dd/MM/yyyy") + " " + this.txtHoraInicio.getValue() + " " + this.cmbPeriodoHI.getSelectedItem().getValue() , "dd/MM/yyyy hh:mm a");
        	Date horaFin = UtilDate.StringToDate(UtilDate.DateToString(new Date(), "dd/MM/yyyy") + " " + this.txtHoraFin.getValue() + " " + this.cmbPeriodoHF.getSelectedItem().getValue() , "dd/MM/yyyy hh:mm a");
        	if(horaInicio.compareTo(horaFin)>0){
        		Messagebox.show("Hora Inicio no puede ser mayor a hora de fin", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
        	}
        	if (horarioAtencionService.getHorarioAtencionByDiaHora(Integer.valueOf(this.txtSecuencia.getValue()), this.cmbDia.getSelectedItem().getValue().toString(), UtilDate.DateToString(horaInicio, "HH:mm:ss"), UtilDate.DateToString(horaFin, "HH:mm:ss")).size()>0){
            	Messagebox.show("Ya existen horarios configurado para el dia seleccionado con hora de inicio o fin dentro de los rangos indicados", "Validación", Messagebox.OK, Messagebox.INFORMATION);
                return;
            }
        	HorarioAtencion horarioExistente = horarioAtencionService.getHorarioAtencionBySec(Integer.valueOf(this.txtSecuencia.getValue()));
        	if (horarioExistente==null)
        		horarioExistente = new HorarioAtencion();
        	
        	horarioExistente.setTurno((char) this.cmbTurno.getSelectedItem().getValue().toString().charAt(0));
        	horarioExistente.setDia((char) this.cmbDia.getSelectedItem().getValue().toString().charAt(0));
        	
        	horarioExistente.setHoraFin(horaFin);
        	horarioExistente.setHoraInicio(horaInicio);
        	InfoResultado resultado = horarioAtencionService.guardarHorarioAtencion(horarioExistente);
        	Mensajes.enviarMensaje(resultado);
        	if(resultado.isOk() && resultado.getObjeto()!=null){
        		limpiar();
        		cargarHorariosAtencion();
        	}
        	
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_PROCESAR_DATOS + ". " +e.getMessage() , Mensajes.TipoMensaje.ERROR);
		}
    }
    
    /**
	 * Método para editar parámetro
	 * 
	 * @param evt
	 */
	@Listen("onEliminarHorario=[id$=listaHorarios]")
	public void onClickEliminar(ForwardEvent evt) {

		// Inicializar objeto a editar
		Generico horarioSeleccionado = new Generico();

		try {
			// Obtener objeto seleccionado
			Toolbarbutton button = (Toolbarbutton) evt.getOrigin().getTarget();
			if (button != null) {
				Listitem listitem = (Listitem) button.getParent().getParent();
				if (listitem != null) {
					horarioSeleccionado = (Generico) listitem.getValue();
				}
			}

			// Validar que se seleccione el objeto de la lista
			if (horarioSeleccionado == null
					|| horarioSeleccionado.getNumero1() <= 0) {
				Messagebox.show("La fila seleccionada no contiene información del horario. Favor verifique",
								Mensajes.REGISTRO_NO_SELECCIONADO,
								new Messagebox.Button[] { Messagebox.Button.OK },
								Messagebox.EXCLAMATION, null);
				return;
			} else {
	        	HorarioAtencion horarioExistente = horarioAtencionService.getHorarioAtencionBySec(horarioSeleccionado.getNumero1());
	        	InfoResultado resultado = horarioAtencionService.eliminarHorarioAtencion(horarioExistente);
	        	Mensajes.enviarMensaje(resultado);
	        	if(resultado.isOk() && resultado.getObjeto()!=null){
	        		limpiar();
	        		cargarHorariosAtencion();
	        	}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje(Mensajes.ERROR_PROCESAR_DATOS,
					Mensajes.TipoMensaje.ERROR);
		}
	}
    
    private void limpiar(){
    	this.txtHoraInicio.setValue("00:00");
    	this.txtHoraFin.setValue("00:00");
    	this.txtSecuencia.setValue("0");
    	this.cmbTurno.setSelectedIndex(0);
    	this.cmbDia.setSelectedIndex(0);
    	this.cmbPeriodoHI.setSelectedIndex(0);
    	this.cmbPeriodoHF.setSelectedIndex(0);
    }
}
