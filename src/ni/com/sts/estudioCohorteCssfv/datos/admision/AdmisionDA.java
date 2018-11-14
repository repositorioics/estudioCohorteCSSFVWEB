package ni.com.sts.estudioCohorteCssfv.datos.admision;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import ni.com.sts.estudioCohorteCSSFV.modelo.Admision;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;
import ni.com.sts.estudioCohorteCssfv.servicios.AdmisionService;
import ni.com.sts.estudioCohorteCssfv.util.HibernateResource;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;

public class AdmisionDA implements AdmisionService {

    private static final HibernateResource hibernateResource = new HibernateResource();
	
    @Override
    public Admision getAdmisionById(int secuenciaAdmision){
    	Admision resultado = new Admision();
   	 	try {
           // Construir query
           String sql = 	"select a "+
							"from Admision a " +
							"where a.secAdmision = :secAdmision ";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setInteger("secAdmision",  secuenciaAdmision);
           
           List<Admision> lista = (List<Admision>) q.list();

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
    public List<Admision> getAdmisiones(){
    	List<Admision> resultado = new ArrayList<Admision>();
   	 	try {
           // Construir query
           String sql = 	"select a "+
							"from Admision a ";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           
           resultado = (List<Admision>) q.list();

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
	public InfoResultado guardarAdmision(Admision admision){
    	InfoResultado infoResultado = new InfoResultado();

        try {

            hibernateResource.begin();
            hibernateResource.getSession().save(admision);
            hibernateResource.getSession().flush();
            hibernateResource.commit();

            infoResultado.setOk(true);
            infoResultado.setObjeto(admision);
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
    public Admision getAdmisionPendienteEntregaByCodExp(int codExpediente){
    	Admision resultado = null;
   	 	try {
           // Construir query
           String sql = 	"select a "+
							"from Admision a " +
							"where a.codExpediente = :codExpediente and a.fechaEntrada is null order by a.fechaSalida desc ";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setInteger("codExpediente",  codExpediente);
           
           List<Admision> lista = (List<Admision>) q.list();

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
    public List<Admision> getAdmisionesPendienteEntregaByCodExp(int codExpediente){
    	List<Admision> resultado = null;
   	 	try {
           // Construir query
           String sql = 	"select a "+
							"from Admision a " +
							"where a.codExpediente = :codExpediente and a.fechaEntrada is null order by a.fechaSalida desc ";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setInteger("codExpediente",  codExpediente);
           
           resultado = (List<Admision>) q.list();
   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 	} finally {
   	 		if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}
   	 	}

   	 	return resultado;	
	}
    
    @SuppressWarnings("unchecked")
	@Override
    public List<Admision> getAdmisionesPendienteEntregaAndHojaActByCodExp(int codExpediente) throws Exception{
		List<Admision> lista = new ArrayList<Admision>();
		try {
           // Construir query
   	 	String sql = "select ad from Admision ad, HojaConsulta hc "+
				"where ad.numHojaConsulta = hc.numHojaConsulta and ad.codExpediente = hc.codExpediente " +
				"and hc.estado not in ('7','8') "+ //cerrado o abandono
				"and hc.codExpediente = :codExpediente  and ad.fechaEntrada is null";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setInteger("codExpediente",  codExpediente);
           
           lista = (List<Admision>) q.list();

   	 	} catch (Exception e) {
   	 		e.printStackTrace();
   	 		throw new Exception(e);
   	 	} finally {
   	 		if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}
   	 	}

   	 	return lista;	
	}
    
    @Override
    @SuppressWarnings("static-access")
	public InfoResultado actualizarAdmision(Admision admision){
    	InfoResultado infoResultado = new InfoResultado();

        try {

            hibernateResource.begin();
            hibernateResource.getSession().update(admision);
            hibernateResource.getSession().flush();
            hibernateResource.commit();

            infoResultado.setOk(true);
            infoResultado.setObjeto(admision);
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
    public Admision getAdmisionByNumHojaCon(int numHoja){
    	Admision resultado = null;
   	 	try {
           // Construir query
           String sql = 	"select a "+
							"from Admision a " +
							"where a.numHojaConsulta = :numHoja";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setInteger("numHoja",  numHoja);
           
           List<Admision> lista = (List<Admision>) q.list();

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
}
