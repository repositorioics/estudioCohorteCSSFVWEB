package ni.com.sts.estudioCohorteCssfv.controller.seguridad;

import ni.com.sts.estudioCohorteCssfv.datos.seguridad.SeguridadDA;
import ni.com.sts.estudioCohorteCssfv.servicios.SeguridadService;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilProperty;
import ni.com.sts.estudioCohorteCssfv.util.Utilidades;

import org.apache.commons.configuration.CompositeConfiguration;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
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

public class LoginController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;
    private static final SeguridadService seguridadService = new SeguridadDA();

    private static CompositeConfiguration config;

    @Wire("[id$=txbUsername]")
    Textbox txbUsername;

    @Wire("[id$=txbPass]")
    Textbox txbPass;

    @Wire("[id$=lbTituloApp]")
    Label lbTituloApp;
    

    @Wire("[id$=imgClienteLogin]")
    Image imgClienteLogin;

    // -------------------------------------------- Constructor

    public LoginController(){
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception{
        super.doAfterCompose(comp);
        init();
    }

    private void init() {
        try {
            // Obtener el archivo properties.
            config = UtilProperty.getConfiguration("EstudioCohorteCssfvWEBExt.properties", "ni/com/sts/estudioCohorteCssfv/properties/EstudioCohorteCssfvWEBInt.properties");
            //lbTituloApp.setValue(config.getString("EstudioCohorteCssfv.titulo.aplicacion"));
        	    /* CODIGO PARA AGREGAR IMAGEN DEL CLIENTE*/
                String logo = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.imagen.logo");
                logo = logo.replace('/', System.getProperty("file.separator").charAt(0));

                AImage imagen = new org.zkoss.image.AImage(logo);

                // TODO: CUANDO SE DESARROLLE EL PROYECTO, AGREGAR ESTE OBJETO EN EL ZUL PARA MOSTRAR IMAGEN DE EMPRESA
                if (this.imgClienteLogin!=null)
                	this.imgClienteLogin.setContent(imagen);
                
            
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    // -------------------------------------------- Eventos

    @Listen("onClick=[id$=btnConfigSeguridad]")
    public void onClickConfigSeguridad(){
        String urlSeguridad = seguridadService.obtenerUrlSistema("SEG");
        if(urlSeguridad!=null){
            if(!urlSeguridad.isEmpty()){
                Utilidades.eliminarSesion();
                Executions.sendRedirect(urlSeguridad);
            }
        }
    }

    @Listen("onClick=[id$=btnLogin];onOK=[id$=txbUsername];onOK=[id$=txbPass]")
    public void onClickLogin() throws WrongValueException, Exception{

        InfoResultado infoResultado = seguridadService.verificarCredenciales(Utilidades.getCodigoSistema(),
                this.txbUsername.getValue(), this.txbPass.getValue());
        if (!infoResultado.isOk()) {
            this.txbUsername.setValue("");
            this.txbPass.setValue("");
           Mensajes.enviarMensaje(infoResultado);
           return;
        }

        Session session = Sessions.getCurrent();
        if (session != null) {
            session.setAttribute("usuarioActual", infoResultado.getObjeto());
            Executions.sendRedirect("/contenido.zul");
            
        }

    }

    @Listen("onLogout=[id$=btnLogout]")
    public void onClickLogout(ForwardEvent evt){
        Utilidades.eliminarSesion();
        Executions.sendRedirect("/login.zul");
    }

}
