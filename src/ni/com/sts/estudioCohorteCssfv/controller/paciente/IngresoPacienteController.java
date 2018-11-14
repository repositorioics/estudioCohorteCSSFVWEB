package ni.com.sts.estudioCohorteCssfv.controller.paciente;

import java.util.Date;

import ni.com.sts.estudioCohorteCssfv.util.UtilDate;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;

/**
 * Servicio para la capa de presentación de la página login.zul
 *
 * <p>
 *
 * @author STS
 * @version 1.0, &nbsp; 10/02/2015
 * @since jdk1.6
 */
public class IngresoPacienteController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    private static final String SELECCIONE = "Seleccione";
    
    
    @Override
    public void doAfterCompose(Component comp) throws Exception{
        super.doAfterCompose(comp);
        init();
    }

    /**
     *
     */
    private void init() {
       this.txtFechaEntrada.setValue(UtilDate.DateToString(new Date(), "dd/MM/yyyy hh:mm a"));
       this.txtFechaSalida.setValue(UtilDate.DateToString(new Date(), "dd/MM/yyyy hh:mm a"));
    }
    
    /** Componentes */

    @Wire("[id$=cmbTipoConsulta]")
    private Combobox cmbTipoConsulta;

    @Wire("[id$=cmbEntregadoPor]")
    private Combobox cmbEntregadoPor;

    @Wire("[id$=cmbRecibidoPor]")
    private Combobox cmbRecibidoPor;
    
    @Wire("[id$=cmbRegresadoA]")
    private Combobox cmbRegresadoA;

    @Wire("[id$=txtCodExpediente]")
    private Textbox txtCodExpediente;
    
    @Wire("[id$=txtNombrePaciente]")
    private Textbox txtNombrePaciente;
    
    @Wire("[id$=txtFechaSalida]")
    private Textbox txtFechaSalida;
    
    @Wire("[id$=txtObservSalida]")
    private Textbox txtObservSalida;
    
    @Wire("[id$=txtFechaEntrada]")
    private Textbox txtFechaEntrada;
    
    @Wire("[id$=txtObservEntrada]")
    private Textbox txtObservEntrada;
    
    @Wire("[id$=txtHojaConsulta]")
    private Textbox txtHojaConsulta;
    
    @Wire("[id$=txtOrdenLlegada]")
    private Textbox txtOrdenLlegada;

    @Wire("[id$=btnRegistrar]")
    private Button btnGuardar;

    @Wire("[id$=btnLimpiar]")
    private Button btnLimpiar;
    
    @Wire("[id$=btnBuscar]")
    private Button btnBuscar;
    
    
}
