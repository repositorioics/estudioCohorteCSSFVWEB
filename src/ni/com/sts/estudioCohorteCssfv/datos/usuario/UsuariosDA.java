package ni.com.sts.estudioCohorteCssfv.datos.usuario;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import ni.com.sts.estudioCohorteCSSFV.modelo.Admision;
import ni.com.sts.estudioCohorteCSSFV.modelo.UsuariosView;
import ni.com.sts.estudioCohorteCssfv.servicios.UsuariosService;
import ni.com.sts.estudioCohorteCssfv.util.HibernateResource;

public class UsuariosDA implements UsuariosService {

    private static final HibernateResource hibernateResource = new HibernateResource();
    
	@Override
	public UsuariosView obtenerUsuarioById(Integer id) {
		UsuariosView resultado = new UsuariosView();
		try {
			// Construir query
			String sql = 	"select a "+
								"from UsuariosView a " +
								"where a.id = :id ";
		
	        Query q = hibernateResource.getSession().createQuery(sql);
	        q.setInteger("id",id);
	        resultado = (UsuariosView)q.uniqueResult();
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
	public List<UsuariosView> obtenerUsuariosByPerfil(String perfil) {
		List<UsuariosView> resultado = new ArrayList<UsuariosView>();
		try {
			// Construir query
			String sql = 	"select distinct a "+
								"from UsuariosView a, RolesView r " +
								"where a.usuario = r.usuario and r.nombre = :perfil ";
		
	        Query q = hibernateResource.getSession().createQuery(sql);
	        q.setParameter("perfil", perfil);
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
	public List<UsuariosView> obtenerUsuariosMedicos() {
		List<UsuariosView> resultado = new ArrayList<UsuariosView>();
		try {
			// Construir query
			
			String sql = "select a from UsuariosView a, RolesView b "
					+ "where a.usuario = b.usuario "
					+ "and b.nombre = 'Médico' "
					+ "and (a.nombre like 'DR.%' or a.nombre like 'DRA.%') "
					+ "and (a.nombre not in('DR. ROMERO','DRA. KUAN')) order by a.nombre";
		
	        Query q = hibernateResource.getSession().createQuery(sql);
	        //q.setParameter("perfil", perfil);
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
	public List<UsuariosView> obtenerSupervisores() {
		List<UsuariosView> resultado = new ArrayList<UsuariosView>();
		try {
			// Construir query
			
			String sql = "select valores " + 
					" from ParametrosSistemas p where p.nombreParametro ='SUPERVISORES_HC'";
			
			Query query = hibernateResource.getSession().createQuery(sql);
			
			String valorParametro = query.uniqueResult().toString();
			
			 sql = "SELECT a FROM UsuariosView a WHERE a.id in(" + valorParametro + ")";

		
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
	public List<UsuariosView> obtenerUsuariosByPerfiles(String perfiles) {
		List<UsuariosView> resultado = new ArrayList<UsuariosView>();
		try {
			// Construir query
			String sql = 	"select distinct a "+
								"from UsuariosView a, RolesView r " +
								"where a.usuario = r.usuario and r.nombre in ('"+perfiles+"') " +
								"order by a.nombre asc";
		
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
	public List<UsuariosView> obtenerUsuariosByName(String nombre) {
		List<UsuariosView> resultado = new ArrayList<UsuariosView>();
		try {
			// Construir query
			String sql = 	"select a "+
								"from UsuariosView a " +
								"where lower(a.nombre) like :nombre order by a.nombre asc ";
		
	        Query q = hibernateResource.getSession().createQuery(sql);
	        q.setParameter("nombre", "%"+nombre.toLowerCase()+"%");
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
	public List<UsuariosView> obtenerUsuarios() {
		List<UsuariosView> resultado = new ArrayList<UsuariosView>();
		try {
			// Construir query
			String sql = 	"select a "+
								"from UsuariosView a order by a.nombre asc";
		
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
	public String obtenerCodigoPersonalById(Integer id) {
		String resultado = null;
		try {
			// Construir query
			String sql = 	"select a.codigoPersonal "+
								"from UsuariosView a " +
								"where a.id = :id ";
		
	        Query q = hibernateResource.getSession().createQuery(sql);
	        q.setInteger("id",id);
	        resultado = (String) q.uniqueResult();
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
