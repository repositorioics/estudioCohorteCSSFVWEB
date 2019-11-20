package ni.com.sts.estudioCohorteCssfv.util;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.log4j.Logger;
import org.zkoss.zul.Messagebox;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox.ClickEvent;

public class Mensajes {

    public enum TipoMensaje {INFO, EXCLAM, QUESTION , ERROR}

    private final static CompositeConfiguration config;

    /**
    * Mensaje de error cuando se produce un error no controlado y se produce una
    * excepción
    */
    public static final String ERROR_NO_CONTROLADO="Se ha producido un error no controlado: ";
    /**
    * Mensaje que según la gravedad del error, se solicita la notificación al
    * administrador del sistema
    */
    public static final String NOTIFICACION_ADMINISTRADOR="Anote el detalle del mensaje y notifique al administrador del sistema. ";
    /**
    * Mensaje que se presenta cuando se guarda un registro
    * de forma exitosa.
    */
    public static final String REGISTRO_GUARDADO="El registro ha sido almacenado con éxito. ";
    public static final String REGISTRO_ACTUALIZADO="El registro ha sido actualizado con éxito. ";
    public static final String REGISTRO_NO_SELECCIONADO="El registro no pudo ser seleccionado. ";
    public static final String REGISTRO_ELIMINADO="El registro ha sido eliminado con éxito. ";
    public static final String EXCEPCION_REGISTRO_EXISTE="El registro no puede ser guardado. El registro ya existe. ";
    public static final String REGISTRO_NO_GUARDADO="Se ha producido un error al guardar el registro. ";
    public static final String REGISTRO_DUPLICADO="El registro ya existe y no puede estar duplicado.";
    public static final String ELIMINAR_REGISTRO_NO_EXISTE="El registro ya no existe y por tanto, no puede ser eliminado";
    public static final String ENCONTRAR_REGISTRO_NO_EXISTE="El registro ya no existe, ha sido eliminado por otro usuario";
    public static final String ELIMINAR_RESTRICCION="El registro no puede ser eliminado ya que otros registros dependen de él";
    public static final String REGISTRO_NO_ELIMINADO="Se ha producido un error al eliminar el registro. ";
    public static final String REGISTRO_NO_ELIMINADO_CONSTRAINT="Se ha producido un error al eliminar el registro. El registro tiene dependencias";
    public static final String REGISTRO_AGREGADO="Registro Agregado con éxito";
    public static final String REGISTRO_NO_ENCONTRADO="No se han encontrado resultados";
    public static final String EXCEPCION_VALORES_REQUERIDOS="El registro no puede ser guardado. Valores requeridos sin definir.";
    public static final String AUTENTICACION_FALLIDA="El nombre de usuario o contraseña es incorrecto.";
    public static final String ALUMNO_YA_FUE_REGISTRADO="Alumno ya se encuentra registrado para la asignatura y/o grupo seleccionadas";

    public static final String FECHAFINAL_MAYORIGUAL_FECHAINICIO = "Fecha final debe ser mayor o igual a Fecha inicio.";
    public static final String PARAMETROS_BUSQUEDA_REQUERIDOS="Debe seleccionar e ingresar los parámetros de búsqueda.";
    public static final String ERROR_CONSULTAR_DATOS="Se produjo un error al consultar los datos.";
    public static final String ERROR_VALIDAR_DATOS="Se ha producido un error al validar los datos.";
    public static final String ERROR_PROCESAR_DATOS="Se ha producido un error al procesar los datos.";
    public static final String CONFIRMA_ELIMINAR_REGISTRO="¿Está seguro que desea eliminar el registro?";
    public static final String CRITERIO_BUSQUEDA_REQUERIDO="Debe especificar criterio de búsqueda.";
    public static final String SELECCIONE_REGISTRO_LISTADO="Debe seleccionar registro del listado.";
    public static final String ERROR_ACTUALIZAR_REGISTRO="Se ha producido un error al actualizar el registro.";
    public static final String ERROR_CONSULTAR_REGISTRO="Se producido un error al consultar registro.";
    public static final String VALORES_REQUERIDOS="Algunos datos son requeridos, por favor verifique.";
    public static final String VALORES_ILEGALES="Valor ilegal, por favor verifique.";
    
    public static final String PACIENTE_RETIRADO = "Paciente en estado Retirado";
    public static final String EXPEDIENTE_NO_ENCONTRADO = "Código no encontrado";
    public static final String ERROR_CONSULTAR_PROP_CONFIG="Se produjo error al consultar propiedades de configuración.";
    public static final String PROCESO_OPEN_CLINICA_TERMINADO = "Proceso de carga manual servicio openClinica terminado. Hojas Consulta procesadas {0}, Hojas Influenza procesadas {1}, Hojas Zika procesadas {2} ";
    
    public static final String ERROR_EXPORTAR_EXCEL = "Error al exportar a excel";

    public static final String NO_DATOS="No se han encontrado datos.";
    
    public static final String NO_RESULTADOS_SISTEMA= "El resultado de este examen no es registrado en el sistema. No se puede generar PDF";
    public static final String EXAMEN_ESTADO_PENDIENTE= "El resultado del exámen está pendiente. No se puede generar PDF";
    public static final String EXAMEN_ESTADO_CANCELADO= "Examen cancelado. No se puede generar PDF";
    public static final String ERROR_GENERAR_PDF = "Error al generar PDF";
    
    public static final String PACIENTE_EN_CONSULTA="El codigo del paciente ya fue ingresado";


    static{
        config = UtilProperty.getConfiguration("EstudioCohorteCssfvWEBExt.properties","ni/com/sts/estudioCohorteCssfv/properties/EstudioCohorteCssfvWEBInt.properties");
    }

    public Mensajes() {

    }

    public static void enviarMensaje(InfoResultado pResultado) {

        if (pResultado.getMensaje()==null || pResultado.getMensaje().isEmpty()) {
            return;
        }

        if(pResultado.getGravedad()== InfoResultado.INFO){
            Messagebox.show(pResultado.getMensaje(),"Información", new Messagebox.Button[]{
                Messagebox.Button.OK }, Messagebox.INFORMATION, null);
        }else if(pResultado.getGravedad() == InfoResultado.WARN){
            Messagebox.show(pResultado.getMensaje(),"Advertencia", new Messagebox.Button[]{
                Messagebox.Button.OK}, Messagebox.EXCLAMATION, null);
        }else{
            Messagebox.show(pResultado.getMensaje()+": "+ pResultado.getMensajeDetalle(),
                    pResultado.getFuenteError(), new Messagebox.Button[]{
                Messagebox.Button.OK }, Messagebox.ERROR, null);
        }

    }

    public static void enviarMensaje(String mensaje,TipoMensaje tipoMsg) {

        String iconMsg = Messagebox.INFORMATION;

        if (!mensaje.isEmpty()) {

            switch(tipoMsg) {
                case INFO:
                    iconMsg = Messagebox.INFORMATION;
                    break;
                case EXCLAM:
                    iconMsg = Messagebox.EXCLAMATION;
                    break;
                case QUESTION:
                    iconMsg = Messagebox.QUESTION;
                    break;
                case ERROR:
                    iconMsg = Messagebox.ERROR;
                    break;
            }

            Messagebox.show(mensaje, "Mensaje", new Messagebox.Button[] { Messagebox.Button.OK }, iconMsg, null);
        }
    }

    public static void enviarMensaje(String mensaje,String titulo,TipoMensaje tipoMsg) {

        String iconMsg = Messagebox.INFORMATION;

        if (!mensaje.isEmpty()) {

            switch(tipoMsg) {
                case INFO:
                    iconMsg = Messagebox.INFORMATION;
                    break;
                case EXCLAM:
                    iconMsg = Messagebox.EXCLAMATION;
                    break;
                case QUESTION:
                    iconMsg = Messagebox.QUESTION;
                    break;
                case ERROR:
                    iconMsg = Messagebox.ERROR;
                    break;
            }

            Messagebox.show(mensaje, titulo, new Messagebox.Button[] { Messagebox.Button.OK }, iconMsg, null);
        }
    }

    public static void enviarMensajeConfirmacion(String mensaje, EventListener<ClickEvent> clickListener) {

        if(!mensaje.isEmpty()) {
            Messagebox.show(mensaje, "Mensaje",
                new Messagebox.Button[] { Messagebox.Button.YES,Messagebox.Button.NO }, Messagebox.QUESTION,clickListener);
        }
    }

    public static void enviarMensajeConfirmacion(String mensaje, String titulo, EventListener<ClickEvent> clickListener) {

        if(!mensaje.isEmpty()) {
            Messagebox.show(mensaje, titulo,
                new Messagebox.Button[] { Messagebox.Button.YES,Messagebox.Button.NO }, Messagebox.QUESTION,clickListener);
        }
    }

    public static <T> void enviarALog(Class<T> pType, InfoResultado pResultado){

        if (pResultado.getMensaje()==null || pResultado.getMensaje().isEmpty() || pType ==null) {
            return;
        }

        try{
            String mensaje = "";
            Logger logger = Logger.getLogger(pType);
            UtilLog.setLog(config.getString("EstudioCohorteCssfvDAO.log"));

            mensaje += pResultado.isOk() ? "OK:true, " : " OK:false, ";
            mensaje += pResultado.isExcepcion() ? "Excepcion:true, " : " Excepcion:false, ";

            if(pResultado.getFuenteError()!=null ){
                if(!pResultado.getFuenteError().isEmpty()){
                    mensaje +="Fuente Error:" + pResultado.getFuenteError() + ", ";
                }
            }

            if(pResultado.getMensaje()!=null){
                if( !pResultado.getMensaje().isEmpty()){
                    mensaje +="Mensaje:" + pResultado.getMensaje() + ", ";
                }
            }

            if(pResultado.getMensajeDetalle()!=null){
                if(!pResultado.getMensajeDetalle().isEmpty()){
                    mensaje +="Detalle:" + pResultado.getMensajeDetalle();
                }
            }
            logger.debug(mensaje);
            if(pResultado.getGravedad() == 1){
                logger.info(mensaje);
            }else if(pResultado.getGravedad() == 2){

            }else if(pResultado.getGravedad() == 3){
                logger.error(mensaje);
            }else{
                logger.fatal(mensaje);
            }

        }catch(Throwable e){
            System.out.println("---------------------------------------------------");
            System.out.println("EXCEPTION: EstudioCohorteCSSFV WEB Mensajes.enviarALog");
            System.out.println("---------------------------------------------------");
            e.printStackTrace();
        }
    }

}
