package ni.com.sts.estudioCohorteCssfv.datos.paciente;

import java.util.List;

import org.hibernate.Query;

import ni.com.sts.estudioCohorteCSSFV.modelo.EstudioCatalogo;
import ni.com.sts.estudioCohorteCSSFV.modelo.Paciente;
import ni.com.sts.estudioCohorteCssfv.servicios.PacienteService;
import ni.com.sts.estudioCohorteCssfv.util.HibernateResource;

public class PacienteDA implements PacienteService {

    private static final HibernateResource hibernateResource = new HibernateResource();
    
	@Override
	public Paciente getPacienteById(Integer codExpediente) {
		Paciente paciente = null;
		try{
			String sql = "select p from Paciente p where p.codExpediente = :codExpediente";
			Query q = hibernateResource.getSession().createQuery(sql);
			q.setInteger("codExpediente", codExpediente);
			paciente = (Paciente)q.uniqueResult();
		} catch (Exception e) {
		 		e.printStackTrace();
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}
		return paciente;
	}
	
	@Override
	public List<Paciente> getPacientes() {
		List<Paciente> paciente = null;
		try{
			String sql = "select p from Paciente p";
			Query q = hibernateResource.getSession().createQuery(sql);
			paciente = q.list();
		} catch (Exception e) {
		 		e.printStackTrace();
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}
		return paciente;
	}
	
	@Override
	public String[] getCodigosExpediente() {
		List<Integer> paciente = null;
		String[] codigos = null;
		try{
			String sql = "select p.codExpediente from Paciente p";
			Query q = hibernateResource.getSession().createQuery(sql);
			paciente = q.list();
			codigos =new String[paciente.size()];
			int i=0;
			for(Integer codigo : paciente){
				codigos[i] = codigo.toString();
				i++;
			}
		} catch (Exception e) {
		 		e.printStackTrace();
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}
		return codigos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getEstudiosPaciente(Integer codExpediente){
		String sql = "select ec from ConsEstudios c, EstudioCatalogo ec " + 
				" where c.codigoConsentimiento = ec.codEstudio"  + 
				" and c.codigoExpediente = :codExpediente " + 
				" and c.retirado != '1' " +
				" group by ec.codEstudio, ec.descEstudio";
		
		Query query = hibernateResource.getSession().createQuery(sql);

		query.setParameter("codExpediente", codExpediente);

		List<EstudioCatalogo> lstConsEstudios = query.list();
		StringBuffer codigosEstudios = new StringBuffer();

		for (EstudioCatalogo estudioCatalogo : lstConsEstudios) {
			codigosEstudios.append(
					estudioCatalogo.getDescEstudio()).append(",");
		}
		
		return 						codigosEstudios != null
				&& !codigosEstudios.toString().isEmpty() ? (codigosEstudios
				.substring(0, (codigosEstudios.length() - 1)))
				: "";
	}
}
