package ni.com.sts.estudioCohorteCssfv.datos.diagnostico;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import ni.com.sts.estudioCohorteCSSFV.modelo.Diagnostico;
import ni.com.sts.estudioCohorteCssfv.servicios.DiagnosticoService;
import ni.com.sts.estudioCohorteCssfv.util.HibernateResource;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;

public class DiagnosticoDA implements DiagnosticoService {

    private static final HibernateResource hibernateResource = new HibernateResource();
	@Override
	public Diagnostico getDiagnosticoById(int secDiagnostico) {
		Diagnostico resultado = new Diagnostico();
   	 	try {
           // Construir query
           String sql = 	"select d "+
							"from Diagnostico d " +
							"where d.secDiagnostico = :secDiagnostico ";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setInteger("secDiagnostico",  secDiagnostico);
           
           resultado = (Diagnostico)q.uniqueResult();
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
	public List<Diagnostico> getDiagnosticos() {
		List<Diagnostico> resultado = new ArrayList<Diagnostico>();
   	 	try {
           // Construir query
           String sql = 	"select d "+
							"from Diagnostico d order by d.diagnostico ";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           
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

	@Override
	public InfoResultado guardarDiagnostico(Diagnostico dato) {
    	InfoResultado infoResultado = new InfoResultado();

        try {

            hibernateResource.begin();
            hibernateResource.getSession().saveOrUpdate(dato);
            hibernateResource.getSession().flush();
            hibernateResource.commit();

            infoResultado.setOk(true);
            infoResultado.setObjeto(dato);
            infoResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
        } catch (Exception e) {
            e.printStackTrace();
            hibernateResource.rollback();
            infoResultado.setOk(false);
            infoResultado.setExcepcion(true);
            infoResultado.setGravedad(infoResultado.ERROR);
            infoResultado.setMensaje(Mensajes.REGISTRO_NO_GUARDADO + "\n" + e.getMessage());
            infoResultado.setFuenteError("Diagnóstico");
        } finally {
            if (hibernateResource.getSession().isOpen()) {
                hibernateResource.close();
            }
        }

        return infoResultado;
	}
	
	@Override
	public Diagnostico getDiagnosticoByCodigo(int codigoDignostico, int secDiagnostico) {
		Diagnostico resultado = new Diagnostico();
   	 	try {
           // Construir query
           String sql = 	"select d "+
							"from Diagnostico d " +
							"where d.codigoDignostico = :codigoDignostico and d.secDiagnostico != :secDiagnostico";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setInteger("codigoDignostico",  codigoDignostico);
           q.setInteger("secDiagnostico", secDiagnostico);
           
           resultado = (Diagnostico)q.uniqueResult();
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
	public List<Diagnostico> getDiagnosticoByPatron(String patron) {
		List<Diagnostico> resultado = new ArrayList<Diagnostico>();
   	 	try {
           // Construir query
           String sql = 	"select d "+
							"from Diagnostico d " +
							"where lower(d.diagnostico) like :patron order by d.diagnostico ";
	
           Query q = hibernateResource.getSession().createQuery(sql);
           q.setString("patron", "%"+patron+"%" );
           
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
