package ni.com.sts.estudioCohorteCssfv.servicios;

import ni.com.sts.seguridadWS.webservices.NodoItemDTO;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;


public interface SeguridadService {

    /**
    *
    * @param pCodigoSistema
    * @param pUsername
    * @param pPass
    * @return
    */
    public InfoResultado verificarCredenciales(String pCodigoSistema,  String pUsername, String pPass);

    /**
    *
    * @param pUsuarioId
    * @param pCodigoSistema
    * @return
    */
    public boolean esUsuarioAdministrador(int pUsuarioId, String pCodigoSistema);

    /**
    *
    * @param pUsuarioId
    * @param pCodigoSistema
    * @param pURL
    * @return
    */
    public boolean esUsuarioAutorizado(int pUsuarioId, String pCodigoSistema, String pURL);

    /**
    *
    * @param pUserName
    * @return
    */
    public String obtenerNombreUsuario(java.lang.String pUserName);

    /**
    *
    * @param pUsuarioId
    * @param pPassActual
    * @param pNuevoPass
    * @param pConfirmacionPass
    * @return
    */
    public InfoResultado cambiarClave(int pUsuarioId, String pPassActual, String pNuevoPass,
            String pConfirmacionPass);

    /**
    *
    * @param pCodigoSistema
    * @return
    */
    public String obtenerUrlSistema(String pCodigoSistema);

    /**
    *
    * @param pUsuarioId
    * @param pCodigoSistema
    * @return
    */
    public NodoItemDTO[] obtenerArbolMenu(int pUsuarioId,String pCodigoSistema);

    /**
    *
    * @param pUsuarioId
    * @param pCodigoSistema
    * @return
    */
    public NodoItemDTO[] obtenerArbolMenuAutorizado(int pUsuarioId,String pCodigoSistema);

}
