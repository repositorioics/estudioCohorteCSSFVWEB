package ni.com.sts.estudioCohorteCssfv.datos.seguridad;

import ni.com.sts.estudioCohorteCssfv.servicios.SeguridadService;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;

import ni.com.sts.seguridadWS.webservices.InfoResultadoWSDTO;
import ni.com.sts.seguridadWS.webservices.NodoItemDTO;
import ni.com.sts.seguridadWS.webservices.SeguridadWS;

public class SeguridadDA implements SeguridadService {

    public SeguridadDA(){

    }

    @Override
    public InfoResultado verificarCredenciales(String pCodigoSistema,
            String pUsername, String pPass) {
        InfoResultado infoResultado = new InfoResultado();
        try{
            InfoResultadoWSDTO infoResultadoWSDTO = SeguridadWS.getSeguridadWSService().verificarCredenciales(pCodigoSistema, pUsername, pPass);

            infoResultado.setFilasAfectadas(infoResultadoWSDTO.getFilasAfectadas());
            infoResultado.setOk(infoResultadoWSDTO.isOk());
            infoResultado.setObjeto(infoResultadoWSDTO.getObjeto());
            infoResultado.setFuenteError(infoResultadoWSDTO.getFuenteError());
            infoResultado.setMensaje(infoResultadoWSDTO.getMensaje());
            infoResultado.setMensajeDetalle(infoResultadoWSDTO.getMensajeDetalle());
            infoResultado.setGravedad(infoResultadoWSDTO.getGravedad());

            return infoResultado;
        }catch(Throwable e){
            infoResultado.setExcepcion(true);
            infoResultado.setFuenteError("SeguridadDA.verificarCredenciales");
            infoResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO+ ": " +e.getMessage());
            infoResultado.setMensajeDetalle(e.getCause().getMessage().toString());
            infoResultado.setOk(false);
            infoResultado.setGravedad(InfoResultado.FATAL);
            infoResultado.setFilasAfectadas(0);
            return infoResultado;
        }finally{
            Mensajes.enviarALog(this.getClass(), infoResultado);
        }
    }

    @Override
    public boolean esUsuarioAdministrador(int pUsuarioId, String pCodigoSistema) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean esUsuarioAutorizado(int pUsuarioId, String pCodigoSistema,
            String pURL) {
        try{
            return SeguridadWS.getSeguridadWSService().esUsuarioAutorizado(pUsuarioId, pCodigoSistema, pURL);
        }catch(Throwable e){
            InfoResultado infoResultado = new InfoResultado();
            infoResultado.setExcepcion(true);
            infoResultado.setFuenteError("SeguridadDA.esUsuarioAutorizado");
            infoResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO+ ": " +e.getMessage());
            infoResultado.setMensajeDetalle(e.getCause().getMessage().toString());
            infoResultado.setOk(false);
            infoResultado.setGravedad(InfoResultado.FATAL);
            infoResultado.setFilasAfectadas(0);
            Mensajes.enviarALog(this.getClass(), infoResultado);
            return false;
        }
    }

    @Override
    public String obtenerNombreUsuario(String pUserName) {
        try{
            return SeguridadWS.getSeguridadWSService().obtenerNombreUsuario(pUserName);
        }catch(Throwable e){
            InfoResultado infoResultado = new InfoResultado();
            infoResultado.setExcepcion(true);
            infoResultado.setFuenteError("SeguridadDA.obtenerNombreUsuario");
            infoResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO+ ": " +e.getMessage());
            infoResultado.setMensajeDetalle(e.getCause().getMessage().toString());
            infoResultado.setOk(false);
            infoResultado.setGravedad(InfoResultado.FATAL);
            infoResultado.setFilasAfectadas(0);
            Mensajes.enviarALog(this.getClass(), infoResultado);
            return pUserName;
        }
    }

    @Override
    public InfoResultado cambiarClave(int pUsuarioId, String pPassActual,
            String pNuevoPass, String pConfirmacionPass) {
        InfoResultado infoResultado = new InfoResultado();

        try{
            InfoResultadoWSDTO infoResultadoWSDTO = SeguridadWS.getSeguridadWSService().cambiarClave(pUsuarioId, pPassActual, pNuevoPass, pConfirmacionPass);

            infoResultado.setFilasAfectadas(infoResultadoWSDTO.getFilasAfectadas());
            infoResultado.setOk(infoResultadoWSDTO.isOk());
            infoResultado.setObjeto(infoResultadoWSDTO.getObjeto());
            infoResultado.setFuenteError(infoResultadoWSDTO.getFuenteError());
            infoResultado.setMensaje(infoResultadoWSDTO.getMensaje());
            infoResultado.setMensajeDetalle(infoResultadoWSDTO.getMensajeDetalle());
            infoResultado.setGravedad(infoResultadoWSDTO.getGravedad());
            return infoResultado;
        }catch(Throwable e){
            infoResultado.setExcepcion(true);
            infoResultado.setFuenteError("SeguridadDA.cambiarClave");
            infoResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO+ ": " +e.getMessage());
            infoResultado.setMensajeDetalle(e.getCause().getMessage().toString());
            infoResultado.setOk(false);
            infoResultado.setGravedad(InfoResultado.FATAL);
            infoResultado.setFilasAfectadas(0);
            return infoResultado;
        }finally{
            Mensajes.enviarALog(this.getClass(), infoResultado);
        }
    }

    @Override
    public String obtenerUrlSistema(String pCodigoSistema) {
        try{
            return SeguridadWS.getSeguridadWSService().obtenerUrlSistema(pCodigoSistema);
        }catch(Throwable e){
            InfoResultado infoResultado = new InfoResultado();
            infoResultado.setExcepcion(true);
            infoResultado.setFuenteError("SeguridadDA.obtenerUrlSistema");
            infoResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO+ ": " +e.getMessage());
            infoResultado.setMensajeDetalle(e.getCause().getMessage().toString());
            infoResultado.setOk(false);
            infoResultado.setGravedad(InfoResultado.FATAL);
            infoResultado.setFilasAfectadas(0);
            Mensajes.enviarALog(this.getClass(), infoResultado);
            return "";
        }
    }

    @Override
    public NodoItemDTO[] obtenerArbolMenu(int pUsuarioId,String pCodigoSistema) {
        try{
            return SeguridadWS.getSeguridadWSService().obtenerArbolMenu(pUsuarioId, pCodigoSistema);
        }catch(Throwable e){
            InfoResultado infoResultado = new InfoResultado();
            infoResultado.setExcepcion(true);
            infoResultado.setFuenteError("SeguridadDA.obtenerArbolMenu");
            infoResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO+ ": " +e.getMessage());
            infoResultado.setMensajeDetalle(e.getCause().getMessage().toString());
            infoResultado.setOk(false);
            infoResultado.setGravedad(InfoResultado.FATAL);
            infoResultado.setFilasAfectadas(0);
            Mensajes.enviarALog(this.getClass(), infoResultado);
            return null;
        }
    }

    @Override
    public NodoItemDTO[] obtenerArbolMenuAutorizado(int pUsuarioId,String pCodigoSistema) {
        try{
            return SeguridadWS.getSeguridadWSService().obtenerArbolMenuAutorizado(pUsuarioId, pCodigoSistema);
        }catch(Throwable e){
            InfoResultado infoResultado = new InfoResultado();
            infoResultado.setExcepcion(true);
            infoResultado.setFuenteError("SeguridadDA.obtenerArbolMenuAutorizado");
            infoResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO+ ": " +e.getMessage());
            infoResultado.setMensajeDetalle(e.getCause().getMessage().toString());
            infoResultado.setOk(false);
            infoResultado.setGravedad(InfoResultado.FATAL);
            infoResultado.setFilasAfectadas(0);
            Mensajes.enviarALog(this.getClass(), infoResultado);
            return null;
        }
    }
}
