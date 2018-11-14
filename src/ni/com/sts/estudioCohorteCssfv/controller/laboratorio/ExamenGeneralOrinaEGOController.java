package ni.com.sts.estudioCohorteCssfv.controller.laboratorio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.EgoResultados;
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

public class ExamenGeneralOrinaEGOController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	private static LaboratorioService laboratorioService = new LaboratorioDA();
	
	private static UsuariosService usuariosService = new UsuariosDA();

	private static ParametroService parametroService = new ParametrosDA();
	
	private static final String SELECCIONE = "Seleccione";
	
	private int SEC_ORDEN_LABORATORIO_EGO = 0;
			
/*Componentes*/
	
	@Wire("[id$=examenGeneralOrinaEGO]")
	private Window wExamenGeneralOrinaEGO;
	
	@Wire("[id$=chkTomaMx]")
    private Checkbox chkTomaMx;
	
	@Wire("[id$=txtColor]")
	private Textbox txtColor;
	
	@Wire("[id$=txtCelulasEpiteliales]")
	private Textbox txtCelulasEpiteliales;
	
	@Wire("[id$=txtAspecto]")
	private Textbox txtAspecto;
	
	@Wire("[id$=txtLeucositos]")
	private Textbox txtLeucositos;
	
	@Wire("[id$=txtSedimento]")
	private Textbox txtSedimento;
	
	@Wire("[id$=txtEritrocitos]")
	private Textbox txtEritrocitos;
	
	@Wire("[id$=txtCilindros]")
	private Textbox txtCilindros;
	
	@Wire("[id$=txtDensidad]")
	private Textbox txtDensidad;
	
	@Wire("[id$=txtCristales]")
	private Textbox txtCristales;
	
	@Wire("[id$=txtProteinas]")
	private Textbox txtProteinas;
	
	@Wire("[id$=txtHilosMucosos]")
	private Textbox txtHilosMucosos;
	
	@Wire("[id$=txtHemoglobinas]")
	private Textbox txtHemoglobinas;
	
	@Wire("[id$=txtBacterias]")
	private Textbox txtBacterias;
	
	@Wire("[id$=txtCuerpoCetonico]")
	private Textbox txtCuerpoCetonico;
	
	@Wire("[id$=txtLevaduras]")
	private Textbox txtLevaduras;
	
	@Wire("[id$=txtPH]")
	private Textbox txtPH;
	
	@Wire("[id$=txtObservaciones]")
	private Textbox txtObservaciones;
	
	@Wire("[id$=txtUrobilinogeno]")
	private Textbox txtUrobilinogeno;
	
	@Wire("[id$=txtGlucosa]")
	private Textbox txtGlucosa;
	
	@Wire("[id$=txtBilirrubinas]")
	private Textbox txtBilirrubinas;
	
	@Wire("[id$=txtNitritos]")
	private Textbox txtNitritos;
	
	//@Wire("[id$=txtCodigoMuestra]")
	//private Textbox txtCodigoMuestra;
	
	@Wire("[id$=cmbBioanalista]")
    private Combobox cmbBioanalista;
	
	@Wire("[id$=btnEnviar]")
	private Button btnEnviar;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		//init();
	}
	
	@Listen("onCreate=[id$=examenGeneralOrinaEGO]")
	public void onCreate_examenGeneralOrinaEGO() throws Exception{
	  init();
	  
	 }

	private void init() throws Exception {
		cargarBioanalista();
		
		OrdenesExamenes ordenesExamenes = new OrdenesExamenes();
		ordenesExamenes = (OrdenesExamenes) wExamenGeneralOrinaEGO
				.getAttribute("ordenesExamenes");

		EgoResultados resultado = laboratorioService.obtenerEgoResultado(ordenesExamenes.getSecOrdenLaboratorio());
		if(resultado != null && resultado.getSecEgoResultado() != 0){
			txtColor.setText(resultado.getColor());
			txtAspecto.setText(resultado.getAspecto());
			txtSedimento.setText(resultado.getSedimento());
			txtDensidad.setText(resultado.getDensidad());
			txtProteinas.setText(resultado.getProteinas());
			txtHemoglobinas.setText(resultado.getHomoglobinas());
			txtCuerpoCetonico.setText(resultado.getCuerpoCetonico());
			txtLeucositos.setText(resultado.getLeucositos());
			txtPH.setText(resultado.getPh());
			txtUrobilinogeno.setText(resultado.getUrobilinogeno());
			txtGlucosa.setText(resultado.getGlucosa());
			txtBilirrubinas.setText(resultado.getBilirrubinas());
			txtNitritos.setText(resultado.getNitritos());
			txtCelulasEpiteliales.setText(resultado.getCelulasEpiteliales());
			txtEritrocitos.setText(resultado.getEritrocitos());
			txtCilindros.setText(resultado.getCilindros());
			txtCristales.setText(resultado.getCristales());
			txtHilosMucosos.setText(resultado.getHilosMucosos());
			txtBacterias.setText(resultado.getBacterias());
			txtLevaduras.setText(resultado.getLevaduras());
			txtObservaciones.setText(resultado.getObservaciones());
			//txtCodigoMuestra.setText(resultado.getCodigoMuestra());
			//cmbBioanalista.setValue(resultado.getUsuarioBioanalista());
			SEC_ORDEN_LABORATORIO_EGO = resultado.getSecEgoResultado();
			//bioanalista es el usuario ya registrado
			cargarBioanalista(resultado.getUsuarioBioanalista());
		}else{//bioanalista es el usuario auntenticado
			cargarBioanalista();
		}
		//Se solicita quitar esta restricción. Siempre se puede modificar el resultado
		//validarEdicion(ordenesExamenes);
		validarTomaMx();
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
		 wExamenGeneralOrinaEGO.setAttribute("accionGuardado", false);
		 wExamenGeneralOrinaEGO.onClose();
	    }
	 
	 private void validarEdicion(OrdenesExamenes ordenExamen){
			//Si el estado de la orden es Enviado no se permite editar ni enviar resultados
			if (ordenExamen.getEstado().equals("Enviado")){
				this.btnEnviar.setDisabled(true);
				this.txtAspecto.setReadonly(true);
				this.txtBacterias.setReadonly(true);
				this.txtBilirrubinas.setReadonly(true);
				this.txtCelulasEpiteliales.setReadonly(true);
				this.txtCilindros.setReadonly(true);
				this.txtColor.setReadonly(true);
				this.txtCristales.setReadonly(true);
				this.txtCuerpoCetonico.setReadonly(true);
				this.txtDensidad.setReadonly(true);
				this.txtEritrocitos.setReadonly(true);
				this.txtGlucosa.setReadonly(true);
				this.txtHemoglobinas.setReadonly(true);
				this.txtHilosMucosos.setReadonly(true);
				this.txtLeucositos.setReadonly(true);
				this.txtLevaduras.setReadonly(true);
				this.txtNitritos.setReadonly(true);
				this.txtObservaciones.setReadonly(true);
				this.txtPH.setReadonly(true);
				this.txtProteinas.setReadonly(true);
				this.txtSedimento.setReadonly(true);
				this.txtUrobilinogeno.setReadonly(true);
				this.cmbBioanalista.setDisabled(true);
				this.chkTomaMx.setDisabled(true);
			}
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
	    /*if (this.txtCodigoMuestra.getValue().length()<=0){
	        	Messagebox.show("Ingrese Código de la Muestra", "Validación", Messagebox.OK, Messagebox.INFORMATION);
	            return;
	        }*/
	    guardaResultadosEGO();
	    
	 }
	 
	 /**
	 * @throws Exception
	 */
	private void guardaResultadosEGO() throws Exception{
		 
		 EgoResultados examenGeneralORinaEGO = new EgoResultados();
		 
		 Calendar horaReporte = Calendar.getInstance();
		 //SimpleDateFormat sdfHoraReporte = new SimpleDateFormat("hh:mm a");
		 
			OrdenesExamenes ordenesExamenes = new OrdenesExamenes();
			ordenesExamenes = (OrdenesExamenes) wExamenGeneralOrinaEGO
					.getAttribute("ordenesExamenes");
			
			if(SEC_ORDEN_LABORATORIO_EGO != 0){
				examenGeneralORinaEGO.setSecEgoResultado(SEC_ORDEN_LABORATORIO_EGO);
			}

		 Short bioanalista = Short.valueOf(this.cmbBioanalista.getSelectedItem().getValue().toString());
		 OrdenLaboratorio ordenLaboratorio = laboratorioService.obtenerOrdenLab(ordenesExamenes.getSecOrdenLaboratorio());
		 		 
		 examenGeneralORinaEGO.setHoraReporte(horaReporte.getTime());
		 examenGeneralORinaEGO.setOrdenLaboratorio(ordenLaboratorio);
		 examenGeneralORinaEGO.setColor(this.txtColor.getValue());
		 examenGeneralORinaEGO.setAspecto(this.txtAspecto.getValue());
		 examenGeneralORinaEGO.setSedimento(this.txtSedimento.getValue());
		 examenGeneralORinaEGO.setDensidad(this.txtDensidad.getValue());
		 examenGeneralORinaEGO.setProteinas(this.txtProteinas.getValue());
		 examenGeneralORinaEGO.setHomoglobinas(this.txtHemoglobinas.getValue());
		 examenGeneralORinaEGO.setCuerpoCetonico(this.txtCuerpoCetonico.getValue());
		 examenGeneralORinaEGO.setPh(this.txtPH.getValue());
		 examenGeneralORinaEGO.setUrobilinogeno(this.txtUrobilinogeno.getValue());
		 examenGeneralORinaEGO.setGlucosa(this.txtGlucosa.getValue());
		 examenGeneralORinaEGO.setBilirrubinas(this.txtBilirrubinas.getValue());
		 examenGeneralORinaEGO.setNitritos(this.txtNitritos.getValue());
		 examenGeneralORinaEGO.setCelulasEpiteliales(this.txtCelulasEpiteliales.getValue());
		 examenGeneralORinaEGO.setLeucositos(this.txtLeucositos.getValue());
		 examenGeneralORinaEGO.setEritrocitos(this.txtEritrocitos.getValue());
		 examenGeneralORinaEGO.setCilindros(this.txtCilindros.getValue());
		 examenGeneralORinaEGO.setCristales(this.txtCristales.getValue());
		 examenGeneralORinaEGO.setHilosMucosos(this.txtHilosMucosos.getValue());
		 examenGeneralORinaEGO.setBacterias(this.txtBacterias.getValue());
		 examenGeneralORinaEGO.setLevaduras(this.txtLevaduras.getValue());
		 examenGeneralORinaEGO.setObservaciones(this.txtObservaciones.getValue());
		 //examenGeneralORinaEGO.setCodigoMuestra(this.txtCodigoMuestra.getValue());
		 examenGeneralORinaEGO.setUsuarioBioanalista(bioanalista);
		 examenGeneralORinaEGO.setEstado('1');
		 //examenGeneralORinaEGO.setHoraReporte();
		 InfoResultado result = laboratorioService.guardarExamenEgo(examenGeneralORinaEGO);
		 if(result.isOk() && result.getObjeto()!=null){
			 examenGeneralORinaEGO = (EgoResultados)result.getObjeto();
			 
			 Mensajes.enviarMensaje(Mensajes.REGISTRO_GUARDADO, 
					 Mensajes.TipoMensaje.INFO);
			 wExamenGeneralOrinaEGO.setAttribute("accionGuardado", true);
			 wExamenGeneralOrinaEGO.onClose();
			
	    	}else{
	        	Mensajes.enviarMensaje(Mensajes.REGISTRO_NO_GUARDADO, 
	        			Mensajes.TipoMensaje.ERROR);
	        	wExamenGeneralOrinaEGO.onClose();
	    	}
		 
	 }
	
	/**
	 * Menejador del evento Check de la casilla de verificación "Muestra Tomada"
	 */
	@Listen("onCheck=[id$=chkTomaMx]")
	public void chkTomaMx_onCheck() {
		OrdenesExamenes ordenesExamenes = new OrdenesExamenes();
		ordenesExamenes = (OrdenesExamenes) wExamenGeneralOrinaEGO
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
		wExamenGeneralOrinaEGO.setAttribute("accionTomaMx", true);
	}
	
	/**
	 * Si orden laboratorio tiene toma mx, se marca check sino se desmarca
	 */
	private void validarTomaMx(){
		OrdenesExamenes ordenesExamenes = new OrdenesExamenes();
		ordenesExamenes = (OrdenesExamenes) wExamenGeneralOrinaEGO
				.getAttribute("ordenesExamenes");	
		OrdenLaboratorio ordenLaboratorio = laboratorioService.obtenerOrdenLab(ordenesExamenes.getSecOrdenLaboratorio());
		if (ordenLaboratorio!=null && ordenLaboratorio.getTomaMx()=='1')
			chkTomaMx.setChecked(true);
		else
			chkTomaMx.setChecked(false);
	}
}
