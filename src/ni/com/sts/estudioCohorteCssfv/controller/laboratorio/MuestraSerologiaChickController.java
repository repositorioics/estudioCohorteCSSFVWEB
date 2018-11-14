package ni.com.sts.estudioCohorteCssfv.controller.laboratorio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.EgoResultados;
import ni.com.sts.estudioCohorteCSSFV.modelo.OrdenLaboratorio;
import ni.com.sts.estudioCohorteCSSFV.modelo.ParametrosSistemas;
import ni.com.sts.estudioCohorteCSSFV.modelo.SerologiaChikMuestra;
import ni.com.sts.estudioCohorteCSSFV.modelo.SerologiaDengueMuestra;
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

public class MuestraSerologiaChickController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	private static LaboratorioService laboratorioService = new LaboratorioDA();
	
	private static UsuariosService usuariosService = new UsuariosDA();

	private static ParametroService parametroService = new ParametrosDA();	
	
	private static final String SELECCIONE = "Seleccione";
	
	private int SEC_SEROLOGIA_CHICK = 0;
	
	
	@Wire("[id$=muestraSerologiaChick]")
	private Window wmuestraSerologiaChick;
	
	@Wire("[id$=chkTomaMx]")
    private Checkbox chkTomaMx;
	
	@Wire("[id$=txtCodigoMuestra]")
	private Textbox txtCodigoMuestra;
	
	@Wire("[id$=cmbBioanalista]")
    private Combobox cmbBioanalista;
	
	@Wire("[id$=btnEnviar]")
	private Button btnEnviar;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		//init();
	}
	
	@Listen("onCreate=[id$=muestraSerologiaChick]")
	public void onCreate_muestraSerologiaChick() throws Exception{
	  init();
	}

	private void init() throws Exception {
		cargarBioanalista();
		
		OrdenesExamenes ordenesExamenes = new OrdenesExamenes();
		ordenesExamenes = (OrdenesExamenes) wmuestraSerologiaChick
				.getAttribute("ordenesExamenes");
		
		SerologiaChikMuestra resultado = laboratorioService.obtenerSerologiaChick(ordenesExamenes.getSecOrdenLaboratorio());
		if(resultado != null && resultado.getSecChikMuestra() != 0){
			txtCodigoMuestra.setText(resultado.getCodigoMuestra());
			SEC_SEROLOGIA_CHICK = resultado.getSecChikMuestra();
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
	 wmuestraSerologiaChick.setAttribute("accionGuardado", false);
	 wmuestraSerologiaChick.onClose();
    }
 
 private void validarEdicion(OrdenesExamenes ordenExamen){
		//Si el estado de la orden es Enviado no se permite editar ni enviar resultados
		if (ordenExamen.getEstado().equals("Enviado")){
			this.btnEnviar.setDisabled(true);
			this.txtCodigoMuestra.setReadonly(true);
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
	 if (this.txtCodigoMuestra.getValue().length() <= 0){
	     	Messagebox.show("Ingrese Código de la Muestra", "Validación", Messagebox.OK, Messagebox.INFORMATION);
	         return;
	     }
	 guardaResultadosChick();
	 
	}

private void guardaResultadosChick() throws Exception{
	 
	SerologiaChikMuestra examenChickMuestra = new SerologiaChikMuestra();
	Calendar horaReporte = Calendar.getInstance();
	//SimpleDateFormat sdfHoraReporte = new SimpleDateFormat("hh:mm a");
	 
		OrdenesExamenes ordenesExamenes = new OrdenesExamenes();
		ordenesExamenes = (OrdenesExamenes) wmuestraSerologiaChick
				.getAttribute("ordenesExamenes");
		
		if(SEC_SEROLOGIA_CHICK != 0){
			examenChickMuestra.setSecChikMuestra(SEC_SEROLOGIA_CHICK);
		}
	 
	 examenChickMuestra.setHoraReporte(horaReporte.getTime());
	 Short bioanalista = Short.valueOf(this.cmbBioanalista.getSelectedItem().getValue().toString());
	 OrdenLaboratorio ordenLaboratorio = laboratorioService.obtenerOrdenLab(ordenesExamenes.getSecOrdenLaboratorio());
	 examenChickMuestra.setOrdenLaboratorio(ordenLaboratorio);
	 examenChickMuestra.setCodigoMuestra(this.txtCodigoMuestra.getValue());
	 examenChickMuestra.setUsuarioBioanalista(bioanalista);
	 examenChickMuestra.setEstado('1');

	 InfoResultado result = laboratorioService.guardarMuestraChick(examenChickMuestra);
	 if(result.isOk() && result.getObjeto()!=null){
		 examenChickMuestra = (SerologiaChikMuestra)result.getObjeto();
		 
		 Mensajes.enviarMensaje(Mensajes.REGISTRO_GUARDADO, 
				 Mensajes.TipoMensaje.INFO);
		 wmuestraSerologiaChick.setAttribute("accionGuardado", true);
		 wmuestraSerologiaChick.onClose();
		
   	}else{
       	Mensajes.enviarMensaje(Mensajes.REGISTRO_NO_GUARDADO, 
       			Mensajes.TipoMensaje.ERROR);
       	wmuestraSerologiaChick.onClose();
   	}
	 
}

/**
 * Menejador del evento Check de la casilla de verificación "Muestra Tomada"
 */
@Listen("onCheck=[id$=chkTomaMx]")
public void chkTomaMx_onCheck() {
	OrdenesExamenes ordenesExamenes = new OrdenesExamenes();
	ordenesExamenes = (OrdenesExamenes) wmuestraSerologiaChick
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
	wmuestraSerologiaChick.setAttribute("accionTomaMx", true);
}

/**
 * Si orden laboratorio tiene toma mx, se marca check sino se desmarca
 */
private void validarTomaMx(){
	OrdenesExamenes ordenesExamenes = new OrdenesExamenes();
	ordenesExamenes = (OrdenesExamenes) wmuestraSerologiaChick
			.getAttribute("ordenesExamenes");	
	OrdenLaboratorio ordenLaboratorio = laboratorioService.obtenerOrdenLab(ordenesExamenes.getSecOrdenLaboratorio());
	if (ordenLaboratorio!=null && ordenLaboratorio.getTomaMx()=='1')
		chkTomaMx.setChecked(true);
	else
		chkTomaMx.setChecked(false);
}
}
