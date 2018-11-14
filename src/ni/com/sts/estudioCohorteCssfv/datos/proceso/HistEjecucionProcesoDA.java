package ni.com.sts.estudioCohorteCssfv.datos.proceso;

import java.util.Date;

import org.hibernate.Query;

import ni.com.sts.estudioCohorteCSSFV.modelo.HistEjecucionProcesoAutomatico;
import ni.com.sts.estudioCohorteCssfv.servicios.HistEjecucionProcesoService;
import ni.com.sts.estudioCohorteCssfv.util.HibernateResource;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;

public class HistEjecucionProcesoDA implements	HistEjecucionProcesoService {
	
	private static final HibernateResource hibernateResource = new HibernateResource();
	
	@Override
	public HistEjecucionProcesoAutomatico getEjecucionProcesoFechaHoy() {
		HistEjecucionProcesoAutomatico resultado = null;
		try{
			String sql = "select secEjecucion, fechaEjecucion from HistEjecucionProcesoAutomatico "+
							"where to_char(fechaEjecucion,'ddMMyyyy') = to_char(CURRENT_DATE,'ddMMyyyy') ";
			Query q = hibernateResource.getSession().createQuery(sql);
   			resultado = (HistEjecucionProcesoAutomatico) q.uniqueResult();						
			
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
	public InfoResultado registrarEjecucionProceso(){
		InfoResultado infoResultado = new InfoResultado();
		try {
        	HistEjecucionProcesoAutomatico dato = new HistEjecucionProcesoAutomatico();
			dato.setFechaEjecucion(new Date());
			hibernateResource.begin();
            hibernateResource.getSession().save(dato);
            hibernateResource.getSession().flush();
            hibernateResource.commit();

            infoResultado.setOk(true);
            infoResultado.setObjeto(dato);
            infoResultado.setMensaje("Registro exitoso :: histo_ejecucion_openclinica");
        } catch (Exception e) {
            e.printStackTrace();
            hibernateResource.rollback();
            infoResultado.setOk(false);
            infoResultado.setExcepcion(true);
            infoResultado.setGravedad(infoResultado.ERROR);
            infoResultado.setMensaje("Se ha producido un error al guardar el registro :: histo_ejecucion_openclinica" + "\n" + e.getMessage());
            infoResultado.setFuenteError("registrarEjecucionProceso");
        } finally {
        	if (hibernateResource.getSession().isOpen()) {
                hibernateResource.close();
            }
        }

        return infoResultado;
	}
}
