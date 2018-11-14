package ni.com.sts.estudioCohorteCssfv.datos.parametro;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;

import ni.com.sts.estudioCohorteCSSFV.modelo.ParametrosSistemas;
import ni.com.sts.estudioCohorteCssfv.servicios.ParametroService;
import ni.com.sts.estudioCohorteCssfv.util.HibernateResource;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;

public class ParametrosDA implements ParametroService {

	private static final HibernateResource hibernateResource = new HibernateResource();
	
	@Override
	public ParametrosSistemas getParametroByName(String nombre) throws Exception {
		ParametrosSistemas resultado = null;
		try{
			String query = "select a from ParametrosSistemas a where a.nombreParametro = :nombre";
			Query q = hibernateResource.getSession().createQuery(query);
			q.setString("nombre", nombre);
			resultado = (ParametrosSistemas)q.uniqueResult();
			
		} catch (Exception e) {
		 		e.printStackTrace();
		 		throw new Exception(e);
		} finally {
			
			
        }
		return resultado;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<ParametrosSistemas> getListaParametros() {
		String query = "select a from ParametrosSistemas a";
		Query q = hibernateResource.getSession().createQuery(query);
		return q.list();
	}


	@SuppressWarnings("static-access")
	@Override
	public InfoResultado actualizarParametro(ParametrosSistemas dato) {
		InfoResultado infoResultado = new InfoResultado();

        try {

            hibernateResource.begin();
            hibernateResource.getSession().save(dato);
            hibernateResource.getSession().flush();
            hibernateResource.commit();

            infoResultado.setOk(true);
            infoResultado.setObjeto(dato);
            infoResultado.setMensaje(Mensajes.REGISTRO_ACTUALIZADO);
        } catch (Exception e) {
            e.printStackTrace();
            hibernateResource.rollback();
            infoResultado.setOk(false);
            infoResultado.setExcepcion(true);
            infoResultado.setGravedad(infoResultado.ERROR);
            infoResultado.setMensaje(Mensajes.REGISTRO_NO_GUARDADO + "\n" + e.getCause());
            infoResultado.setFuenteError("Parámetros");
        } finally {
            if (hibernateResource.getSession().isOpen()) {
                hibernateResource.close();
            }
        }

        return infoResultado;
	}

}
