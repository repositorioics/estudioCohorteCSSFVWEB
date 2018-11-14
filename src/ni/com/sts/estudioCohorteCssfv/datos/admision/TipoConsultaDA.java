package ni.com.sts.estudioCohorteCssfv.datos.admision;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import ni.com.sts.estudioCohorteCSSFV.modelo.TipoConsulta;
import ni.com.sts.estudioCohorteCssfv.servicios.TipoConsultaService;
import ni.com.sts.estudioCohorteCssfv.util.HibernateResource;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;

public class TipoConsultaDA implements TipoConsultaService {

    private static final HibernateResource hibernateResource = new HibernateResource();
	
    @Override
    public TipoConsulta getTipoConsultaById(int secTipoCon){
    	TipoConsulta resultado = new TipoConsulta();
   	 	try {
           // Construir query
           String sql = 	"select a "+
							"from TipoConsulta a " +
							"where a.secTipocon = :secTipoCon ";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setInteger("secTipoCon",  secTipoCon);
           
           List<TipoConsulta> lista = (List<TipoConsulta>) q.list();

           if (lista != null && lista.size() > 0) {
           		resultado = lista.get(0);
           } 
   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 	} finally {
   	 		if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}
   	 	}

   	 	return resultado;	
	}
    
    @Override
    public List<TipoConsulta> getTiposConsultas(){
    	List<TipoConsulta> resultado = new ArrayList<TipoConsulta>();
   	 	try {
           // Construir query
           String sql = 	"select a "+
							"from TipoConsulta a ";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           
           resultado = (List<TipoConsulta>) q.list();

   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 	} finally {
   	 		if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}
   	 	}

       return resultado;	
	}
    
    @Override
    public List<TipoConsulta> getTiposConsultasActivas(){
    	List<TipoConsulta> resultado = new ArrayList<TipoConsulta>();
   	 	try {
           // Construir query
           String sql = 	"select a "+
							"from TipoConsulta a where a.estado = '1' ";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           
           resultado = (List<TipoConsulta>) q.list();

   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 	} finally {
   	 		if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}
   	 	}

       return resultado;	
	}

    @SuppressWarnings("static-access")
	public InfoResultado guardarTipoConsulta(TipoConsulta tipoConsulta){
    	InfoResultado infoResultado = new InfoResultado();

        try {

            hibernateResource.begin();
            hibernateResource.getSession().save(tipoConsulta);
            hibernateResource.getSession().flush();
            hibernateResource.commit();

            infoResultado.setOk(true);
            infoResultado.setObjeto(tipoConsulta);
            infoResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
        } catch (Exception e) {
            e.printStackTrace();
            hibernateResource.rollback();
            infoResultado.setOk(false);
            infoResultado.setExcepcion(true);
            infoResultado.setGravedad(infoResultado.ERROR);
            infoResultado.setMensaje(Mensajes.REGISTRO_NO_GUARDADO + "\n" + e.getMessage());
            infoResultado.setFuenteError("Admisión");
        } finally {
            if (hibernateResource.getSession().isOpen()) {
                hibernateResource.close();
            }
        }

        return infoResultado;
    }
    
    @Override
    @SuppressWarnings("static-access")
	public InfoResultado actualizarTipoConsulta(TipoConsulta tipoConsulta){
    	InfoResultado infoResultado = new InfoResultado();

        try {

            hibernateResource.begin();
            hibernateResource.getSession().update(tipoConsulta);
            hibernateResource.getSession().flush();
            hibernateResource.commit();

            infoResultado.setOk(true);
            infoResultado.setObjeto(tipoConsulta);
            infoResultado.setMensaje(Mensajes.REGISTRO_ACTUALIZADO);
        } catch (Exception e) {
            e.printStackTrace();
            hibernateResource.rollback();
            infoResultado.setOk(false);
            infoResultado.setExcepcion(true);
            infoResultado.setGravedad(infoResultado.ERROR);
            infoResultado.setMensaje(Mensajes.REGISTRO_NO_GUARDADO + "\n" + e.getMessage());
            infoResultado.setFuenteError("Admisión");
        } finally {
            if (hibernateResource.getSession().isOpen()) {
                hibernateResource.close();
            }
        }

        return infoResultado;
    }
    
    @Override
    public TipoConsulta getTipoConsultaByCodigo(String codigo){
    	TipoConsulta resultado = null;
   	 	try {
           // Construir query
           String sql = 	"select a "+
							"from TipoConsulta a " +
							"where a.codigo = :codigo";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setParameter("codigo",  codigo);
           
           List<TipoConsulta> lista = (List<TipoConsulta>) q.list();

           if (lista != null && lista.size() > 0) {
           		resultado = lista.get(0);
           } 
   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 	} finally {
   	 		if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}
   	 	}

   	 	return resultado;	
	}
	
		@Override
	public TipoConsulta existeTipoConsulta(String codigo, int secTipoCon) {
		TipoConsulta resultado = new TipoConsulta();
   	 	try {
           // Construir query
           String sql = 	"select d "+
							"from TipoConsulta d " +
							"where d.codigo = :codigo and d.secTipocon != :secTipoCon";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setParameter("codigo",  codigo);
           q.setInteger("secTipoCon", secTipoCon);
           
           resultado = (TipoConsulta)q.uniqueResult();
   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 	} finally {
   	 		if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}
   	 	}

   	 	return resultado;	
	}
	
	@Override
	public List<TipoConsulta> getTipoConsultaByPatron(String patron) {
		List<TipoConsulta> resultado = new ArrayList<TipoConsulta>();
   	 	try {
           // Construir query
           String sql = 	"select d "+
							"from TipoConsulta d " +
							"where lower(d.descripcion) like :patron order by d.descripcion ";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setString("patron", "%"+patron.toLowerCase()+"%" );
           
           resultado = q.list();
   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 	} finally {
   	 		if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}
   	 	}

   	 	return resultado;	
	}
}
