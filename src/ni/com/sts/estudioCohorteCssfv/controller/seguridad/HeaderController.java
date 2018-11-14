package ni.com.sts.estudioCohorteCssfv.controller.seguridad;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import ni.com.sts.estudioCohorteCssfv.util.UtilProperty;
import ni.com.sts.estudioCohorteCssfv.util.Utilidades;

import org.apache.commons.configuration.CompositeConfiguration;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class HeaderController extends SelectorComposer<Component> {
    private static final long serialVersionUID = 1L;
    
    @SuppressWarnings("unused")
    private static CompositeConfiguration config;

    @Wire("[id$=lblFecha]")
    Label lblFecha;

    @Wire("[id$=lblUsername]")
    Label lblUsername;

    @Wire("[id$=popUpNuevoPass]")
    Window popUpNuevoPass;

    @Wire("[id$=txbPass]")
    Textbox txbPass;

    @Wire("[id$=txbNuevoPass]")
    Textbox txbNuevoPass;

    @Wire("[id$=txbConfirmarPass]")
    Textbox txbConfirmarPass;

    @Wire("[id$=imgCliente]")
    Image imgCliente;
    
    // -------------------------------------------- Constructor
    public HeaderController() {

    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        init();
    }

    // -------------------------------------------- Métodos

    public void init(){
        Format formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm");
        this.lblFecha.setValue(formatter.format(new Date()));

        if(Utilidades.obtenerInfoSesion()!=null){
            this.lblUsername.setValue(Utilidades.obtenerInfoSesion().getUsername());

            //Si la contraseña del usuario es temporal, se le solicita a éste que la cambie.
            if(Utilidades.obtenerInfoSesion().isClaveTemporal()){
                this.popUpNuevoPass.doModal();
                Messagebox.show("Su contraseña está marcada como temporal." +
                        " Por favor modifíquela antes de continuar la sesión de trabajo.",
                        "Contraseña temporal",
                        new Messagebox.Button[] { Messagebox.Button.OK },
                        Messagebox.INFORMATION, null);
                return;
            }

        }

        // Obtener el archivo properties.
         try {
             config = UtilProperty.getConfiguration("EstudioCohorteCssfvWEBExt.properties", "ni/com/sts/estudioCohorteCssfv/properties/EstudioCohorteCssfvWEBInt.properties");
         } catch (Exception e) {
         }

         // Obtener imagen
         try {
             /* CODIGO PARA AGREGAR IMAGEN DEL CLIENTE*/
             String logo = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0) + config.getString("EstudioCohorteCssfv.imagen.logo");
             logo = logo.replace('/', System.getProperty("file.separator").charAt(0));

             AImage imagen = new org.zkoss.image.AImage(logo);

             // TODO: CUANDO SE DESARROLLE EL PROYECTO, AGREGAR ESTE OBJETO EN EL ZUL PARA MOSTRAR IMAGEN DE EMPRESA
             this.imgCliente.setContent(imagen);
             
         } catch (Exception e) {
             e.printStackTrace();
         }

    }

}
