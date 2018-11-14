package ni.com.sts.estudioCohorteCssfv.controller.laboratorio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.EghResultados;
import ni.com.sts.estudioCohorteCSSFV.modelo.OrdenLaboratorio;
import ni.com.sts.estudioCohorteCSSFV.modelo.ParametrosSistemas;
import ni.com.sts.estudioCohorteCSSFV.modelo.UsuariosView;
import ni.com.sts.estudioCohorteCssfv.datos.laboratorio.LaboratorioDA;
import ni.com.sts.estudioCohorteCssfv.datos.parametro.ParametrosDA;
import ni.com.sts.estudioCohorteCssfv.datos.usuario.UsuariosDA;
import ni.com.sts.estudioCohorteCssfv.dto.OrdenesExamenes;
import ni.com.sts.estudioCohorteCssfv.servicios.LaboratorioService;
import ni.com.sts.estudioCohorteCssfv.servicios.ParametroService;
import ni.com.sts.estudioCohorteCssfv.servicios.UsuariosService;
import ni.com.sts.estudioCohorteCssfv.util.Generico;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilDate;
import ni.com.sts.estudioCohorteCssfv.util.Utilidades;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ExamenGeneralHecesEGHController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	private static LaboratorioService laboratorioService = new LaboratorioDA();
	
	private static UsuariosService usuariosService = new UsuariosDA();

	private static ParametroService parametroService = new ParametrosDA();
	
	private static final String SELECCIONE = "Seleccione";
	
	private int SEC_ORDEN_LABORATORIO_EGH = 0;
	
	//Componentes
	
		@Wire("[id$=examenGeneralHecesEGH]")
		private Window wExamenGeneralHecesEGH;
		
		@Wire("[id$=txtColor]")
		private Textbox txtColor;
		
		@Wire("[id$=txtEcoli]")
		private Textbox txtEcoli;
		
		@Wire("[id$=txtConsistencia]")
		private Textbox txtConsistencia;
		
		@Wire("[id$=txtEndolimaxNana]")
		private Textbox txtEndolimaxNana;
		
		@Wire("[id$=txtRestosAlimenticios]")
		private Textbox txtRestosAlimenticios;
		
		@Wire("[id$=txtEHistolityca]")
		private Textbox txtEHistolityca;
		
		@Wire("[id$=txtMucus]")
		private Textbox txtMucus;
		
		@Wire("[id$=txtGardiaLambia]")
		private Textbox txtGardiaLambia;
		
		@Wire("[id$=txtPH]")
		private Textbox txtPH;
		
		@Wire("[id$=txtTrichurisTrichura]")
		private Textbox txtTrichurisTrichura;
		
		@Wire("[id$=txtSangreOculta]")
		private Textbox txtSangreOculta;
		
		@Wire("[id$=txtHymenolepisNana]")
		private Textbox txtHymenolepisNana;
		
		@Wire("[id$=txtStrongyloideSertcolaris]")
		private Textbox txtStrongyloideSertcolaris;
		
		@Wire("[id$=txtBacterias]")
		private Textbox txtBacterias;
		
		@Wire("[id$=txtUnicinarias]")
		private Textbox txtUnicinarias;
		
		@Wire("[id$=txtLevaduras]")
		private Textbox txtLevaduras;

		@Wire("[id$=txtEnteroviusVermiculares]")
		private Textbox txtEnteroviusVermiculares;
		
		@Wire("[id$=txtLeucocitos]")
		private Textbox txtLeucocitos;
		
		@Wire("[id$=txtObservaciones]")
		private Textbox txtObservaciones;
		
		@Wire("[id$=txtEritrocitos]")
		private Textbox txtEritrocitos;
		
		@Wire("[id$=txtFilamentosMucosos]")
		private Textbox txtFilamentosMucosos;
		
		//@Wire("[id$=txtCodigoMuestra]")
		//private Textbox txtCodigoMuestra;
	    @Wire("[id$=chkTomaMx]")
	    private Checkbox chkTomaMx;
		
		@Wire("[id$=cmbBioanalista]")
	    private Combobox cmbBioanalista;
		
		@Wire("[id$=btnEnviar]")
		private Button btnEnviar;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
	}
	
	@Listen("onCreate=[id$=examenGeneralHecesEGH]")
	public void onCreate_examenGeneralHecesEGH() throws Exception{
	  init();
	  
	 }

	private void init() throws Exception {
		cargarBioanalista();
		OrdenesExamenes ordenesExamenes = new OrdenesExamenes();
		ordenesExamenes = (OrdenesExamenes) wExamenGeneralHecesEGH
				.getAttribute("ordenesExamenes");
		
		//OrdenesExamenes ordenesExamenes = (OrdenesExamenes) wExamenGeneralHecesEGH.getAttribute("ordenesExamenes");
		EghResultados resultado = laboratorioService.obtenerEghResultado(ordenesExamenes.getSecOrdenLaboratorio());
		if(resultado != null && resultado.getSecEghResultado() != 0){
			txtColor.setText(resultado.getColor());
			txtEcoli.setText(resultado.getEColi());
			txtConsistencia.setText(resultado.getConsistencia());
			txtEndolimaxNana.setText(resultado.getEndolimaxNana());
			txtRestosAlimenticios.setText(resultado.getRestosAlimenticios());
			txtEHistolityca.setText(resultado.getEHistolytica());
			txtMucus.setText(resultado.getMucus());
			txtGardiaLambia.setText(resultado.getGardiaAmblia());
			txtPH.setText(resultado.getPh());
			txtTrichurisTrichura.setText(resultado.getTrichuris());
			txtSangreOculta.setText(resultado.getSangreOculta());
			txtHymenolepisNana.setText(resultado.getHymenolepisNana());
			txtStrongyloideSertcolaris.setText(resultado.getStrongyloideStercolaris());
			txtBacterias.setText(resultado.getBacterias());
			txtUnicinarias.setText(resultado.getUnicinarias());
			txtLevaduras.setText(resultado.getLevaduras());
			txtEnteroviusVermiculares.setText(resultado.getEnterovirus());
			txtLeucocitos.setText(resultado.getLeucocitos());
			txtObservaciones.setText(resultado.getObservaciones());
			txtEritrocitos.setText(resultado.getEritrocitos());
			txtFilamentosMucosos.setText(resultado.getFilamentosMucosos());
			//txtCodigoMuestra.setText(resultado.getCodigoMuestra());
			SEC_ORDEN_LABORATORIO_EGH = resultado.getSecEghResultado();
			//bioanalista es el usuario ya registrado
			cargarBioanalista(resultado.getUsuarioBionalista());
		}else{//bioanalista es el usuario auntenticado
			cargarBioanalista();
		}
		//Se solicita quitar esta restricción. Siempre se puede modificar el resultado
		//validarEdicion(ordenesExamenes);
		validarTomaMx();
	}
	
	private void validarEdicion(OrdenesExamenes ordenExamen){
		//Si el estado de la orden es Enviado no se permite editar ni enviar resultados.
		if (ordenExamen.getEstado().equals("Enviado")){
			this.btnEnviar.setDisabled(true);
			this.txtBacterias.setReadonly(true);
			this.txtColor.setReadonly(true);
			this.txtConsistencia.setReadonly(true);
			this.txtEcoli.setReadonly(true);
			this.txtEHistolityca.setReadonly(true);
			this.txtEndolimaxNana.setReadonly(true);
			this.txtEnteroviusVermiculares.setReadonly(true);
			this.txtEritrocitos.setReadonly(true);
			this.txtFilamentosMucosos.setReadonly(true);
			this.txtGardiaLambia.setReadonly(true);
			this.txtHymenolepisNana.setReadonly(true);
			this.txtLeucocitos.setReadonly(true);
			this.txtLevaduras.setReadonly(true);
			this.txtMucus.setReadonly(true);
			this.txtObservaciones.setReadonly(true);
			this.txtPH.setReadonly(true);
			this.txtRestosAlimenticios.setReadonly(true);
			this.txtSangreOculta.setReadonly(true);
			this.txtStrongyloideSertcolaris.setReadonly(true);
			this.txtTrichurisTrichura.setReadonly(true);
			this.txtUnicinarias.setReadonly(true);
			this.cmbBioanalista.setDisabled(true);
			this.chkTomaMx.setDisabled(true);
		}
	}
	
	@Listen("onClick=[id$=btnEnviar]")
    public void btnEnviar_onClick() {
	 try {
        	validarCamposRequerido();
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_PROCESAR_DATOS, Mensajes.TipoMensaje.ERROR);
		}
    }
 
 @Listen("onClick=[id$=btnCerrar]")
    public void btnCerrar_onClick() {
	 wExamenGeneralHecesEGH.setAttribute("accionGuardado", false);
	 wExamenGeneralHecesEGH.onClose();
    }
 
 private void cargarBioanalista() throws Exception{

 	Generico usuarioBioanalista = new Generico();
 	usuarioBioanalista.setNumero1(Utilidades.obtenerInfoSesion().getUsuarioId());
 	usuarioBioanalista.setTexto1(Utilidades.obtenerInfoSesion().getUsername());
 	Generico seleccione = new Generico();
 	seleccione.setNumero1(0);
 	seleccione.setTexto1(SELECCIONE);
 	List<Generico> oList = new ArrayList<Generico>();
 	oList.add(0,seleccione);
 	oList.add(1,usuarioBioanalista);
 	
 	//ademas del usuario logueado, cargar el resto de usuarios con el perfil laboratorio
 	ParametrosSistemas perfilLab = parametroService.getParametroByName("PERFIL_BIOANALISTA");
	if(perfilLab!=null){
		List<UsuariosView> usuarios = usuariosService.obtenerUsuariosByPerfiles(perfilLab.getValores().replace(",", "','"));
		int indice = 2;
		for(UsuariosView usuario: usuarios){
			if (!usuario.getId().equals(usuarioBioanalista.getNumero1())){
				Generico usuarioBio = new Generico();
				usuarioBio.setNumero1(usuario.getId());
				usuarioBio.setTexto1(usuario.getUsuario());
				oList.add(indice,usuarioBio);
				indice++;
			}
		}
	}
	
 	final String idBioanalista = String.valueOf(usuarioBioanalista.getNumero1());
     this.cmbBioanalista.setModel(new ListModelList<Generico>(new ArrayList<Generico>()));
     this.cmbBioanalista.setModel(new ListModelList<Generico>(oList));
     this.cmbBioanalista.setItemRenderer(new ComboitemRenderer<Generico>() {
            @Override
            public void render(Comboitem comboitem, Generico fila, int index) throws Exception {
                comboitem.setLabel( fila.getTexto1());
                comboitem.setValue(String.valueOf((fila.getNumero1())));
                if (idBioanalista.equals(comboitem.getValue().toString())) {
                	cmbBioanalista.setSelectedItem(comboitem);
				}
        }});
        
 }

 private void cargarBioanalista(final Short idBioanalista) throws Exception{
	 	Generico seleccione = new Generico();
	 	seleccione.setNumero1(0);
	 	seleccione.setTexto1(SELECCIONE);
	 	Generico usuarioBioanalista = new Generico();
	 	UsuariosView bioanalista = usuariosService.obtenerUsuarioById(idBioanalista.intValue());
	 	usuarioBioanalista.setNumero1(bioanalista.getId());
	 	usuarioBioanalista.setTexto1(bioanalista.getUsuario());
	 	List<Generico> oList = new ArrayList<Generico>();
	 	oList.add(0,seleccione);
	 	oList.add(1,usuarioBioanalista);
	 	
	 	//ademas del usuario logueado, cargar el resto de usuarios con el perfil laboratorio
	 	ParametrosSistemas perfilLab = parametroService.getParametroByName("PERFIL_BIOANALISTA");
    	if(perfilLab!=null){
    		List<UsuariosView> usuarios = usuariosService.obtenerUsuariosByPerfiles(perfilLab.getValores().replace(",", "','"));
    		int indice = 2;
    		for(UsuariosView usuario: usuarios){
    			if (!usuario.getId().equals(bioanalista.getId())){
    				Generico usuarioBio = new Generico();
    				usuarioBio.setNumero1(usuario.getId());
    				usuarioBio.setTexto1(usuario.getUsuario());
    				oList.add(indice,usuarioBio);
    				indice++;
    			}
    		}
    	}
    	
	     this.cmbBioanalista.setModel(new ListModelList<Generico>(new ArrayList<Generico>()));
	     this.cmbBioanalista.setModel(new ListModelList<Generico>(oList));
	     this.cmbBioanalista.setItemRenderer(new ComboitemRenderer<Generico>() {
	            @Override
	            public void render(Comboitem comboitem, Generico fila, int index) throws Exception {
	                comboitem.setLabel( fila.getTexto1());
	                comboitem.setValue(String.valueOf((fila.getNumero1())));
	                if (String.valueOf(idBioanalista).equals(comboitem.getValue().toString())) {
	                	cmbBioanalista.setSelectedItem(comboitem);
					}
	        }});
	 }
 
private void validarCamposRequerido() throws Exception{
	 
	if (this.cmbBioanalista.getSelectedIndex() <= 0) {
         Messagebox.show("Seleccione Bioanalista", "Validación", Messagebox.OK, Messagebox.INFORMATION);
         return;
     }
 /*if (this.txtCodigoMuestra.getValue().length() <= 0){
     	Messagebox.show("Ingrese Código de la Muestra", "Validación", Messagebox.OK, Messagebox.INFORMATION);
         return;
     }*/
 guardaResultadosEGH();
 
}
private void guardaResultadosEGH() throws Exception{
	 
	EghResultados examenGeneralHecesEGH = new EghResultados();
	 Calendar horaReporte = Calendar.getInstance();
	 //SimpleDateFormat sdfHoraReporte = new SimpleDateFormat("hh:mm a");
	 
		OrdenesExamenes ordenesExamenes = new OrdenesExamenes();
		ordenesExamenes = (OrdenesExamenes) wExamenGeneralHecesEGH
				.getAttribute("ordenesExamenes");
		
		if(SEC_ORDEN_LABORATORIO_EGH!= 0){
			examenGeneralHecesEGH.setSecEghResultado(SEC_ORDEN_LABORATORIO_EGH);
		}
	 
	 examenGeneralHecesEGH.setHoraReporte(horaReporte.getTime());
	 Short bioanalista = Short.valueOf(this.cmbBioanalista.getSelectedItem().getValue().toString());
	 //OrdenLaboratorio ordenLaboratorio = laboratorioService.obtenerOrdenLab(ordenesExamenes.getSecOrdenLaboratorio());	 
	 examenGeneralHecesEGH.setSecOrdenLaboratorio(ordenesExamenes.getSecOrdenLaboratorio());
	 examenGeneralHecesEGH.setColor(this.txtColor.getValue());
	 examenGeneralHecesEGH.setConsistencia(this.txtConsistencia.getValue());
	 examenGeneralHecesEGH.setRestosAlimenticios(this.txtRestosAlimenticios.getValue());
	 examenGeneralHecesEGH.setMucus(this.txtMucus.getValue());
	 examenGeneralHecesEGH.setPh(this.txtPH.getValue());
	 examenGeneralHecesEGH.setSangreOculta(this.txtSangreOculta.getValue());
	 examenGeneralHecesEGH.setBacterias(this.txtBacterias.getValue());
	 examenGeneralHecesEGH.setLevaduras(this.txtLevaduras.getValue());
	 examenGeneralHecesEGH.setLeucocitos(this.txtLeucocitos.getValue());
	 examenGeneralHecesEGH.setEritrocitos(this.txtEritrocitos.getValue());
	 examenGeneralHecesEGH.setFilamentosMucosos(this.txtFilamentosMucosos.getValue());
	 examenGeneralHecesEGH.setEColi(this.txtEcoli.getValue());
	 examenGeneralHecesEGH.setEndolimaxNana(this.txtEndolimaxNana.getValue());
	 examenGeneralHecesEGH.setEHistolytica(this.txtEHistolityca.getValue());
	 examenGeneralHecesEGH.setGardiaAmblia(this.txtGardiaLambia.getValue());
	 examenGeneralHecesEGH.setTrichuris(this.txtTrichurisTrichura.getValue());
	 examenGeneralHecesEGH.setHymenolepisNana(this.txtHymenolepisNana.getValue());
	 examenGeneralHecesEGH.setStrongyloideStercolaris(this.txtStrongyloideSertcolaris.getValue());
	 examenGeneralHecesEGH.setUnicinarias(this.txtUnicinarias.getValue());
	 examenGeneralHecesEGH.setEnterovirus(this.txtEnteroviusVermiculares.getValue());
	 examenGeneralHecesEGH.setObservaciones(this.txtObservaciones.getValue());
	 //examenGeneralHecesEGH.setCodigoMuestra(this.txtCodigoMuestra.getValue());
	 
	 examenGeneralHecesEGH.setUsuarioBionalista(bioanalista);
	 examenGeneralHecesEGH.setEstado('1');
	 

	 InfoResultado result = laboratorioService.guardarExamenEgh(examenGeneralHecesEGH);
	 if(result.isOk() && result.getObjeto()!=null){
		 examenGeneralHecesEGH = (EghResultados)result.getObjeto();
		 
		 Mensajes.enviarMensaje(Mensajes.REGISTRO_GUARDADO, 
				 Mensajes.TipoMensaje.INFO);
		 wExamenGeneralHecesEGH.setAttribute("accionGuardado", true);
		 wExamenGeneralHecesEGH.onClose();
		
   	}else{
       	Mensajes.enviarMensaje(Mensajes.REGISTRO_NO_GUARDADO, 
       			Mensajes.TipoMensaje.ERROR);
       	wExamenGeneralHecesEGH.onClose();
   	}
	 
}

	/**
	 * Menejador del evento Check de la casilla de verificación "Muestra Tomada"
	 */
	@Listen("onCheck=[id$=chkTomaMx]")
	public void chkTomaMx_onCheck() {
		OrdenesExamenes ordenesExamenes = new OrdenesExamenes();
		ordenesExamenes = (OrdenesExamenes) wExamenGeneralHecesEGH
				.getAttribute("ordenesExamenes");	
		OrdenLaboratorio ordenLaboratorio = laboratorioService.obtenerOrdenLab(ordenesExamenes.getSecOrdenLaboratorio());
		if (!this.chkTomaMx.isChecked()){
			ordenLaboratorio.setTomaMx('0');
			ordenLaboratorio.setFechaHoraTomaMx(null);
		}else{
			ordenLaboratorio.setTomaMx('1');
			ordenLaboratorio.setFechaHoraTomaMx(UtilDate.DateToString(new Date(),"dd/MM/yyyy HH:mm:ss"));
		}
		//INFO, EXCLAM, QUESTION , ERROR
		InfoResultado result = laboratorioService.actualizarOrdenLaboratorio(ordenLaboratorio);
		if(result.isOk() && result.getObjeto()!=null){
			Mensajes.enviarMensaje(result.getMensaje(),	Mensajes.TipoMensaje.INFO);
		}else{
			Mensajes.enviarMensaje(result.getMensaje(),	Mensajes.TipoMensaje.ERROR);
		}
		wExamenGeneralHecesEGH.setAttribute("accionTomaMx", true);
	}
	
	/**
	 * Si orden laboratorio tiene toma mx, se marca check sino se desmarca
	 */
	private void validarTomaMx(){
		OrdenesExamenes ordenesExamenes = new OrdenesExamenes();
		ordenesExamenes = (OrdenesExamenes) wExamenGeneralHecesEGH
				.getAttribute("ordenesExamenes");	
		OrdenLaboratorio ordenLaboratorio = laboratorioService.obtenerOrdenLab(ordenesExamenes.getSecOrdenLaboratorio());
		if (ordenLaboratorio!=null && ordenLaboratorio.getTomaMx()=='1')
			chkTomaMx.setChecked(true);
		else
			chkTomaMx.setChecked(false);
	}
}
