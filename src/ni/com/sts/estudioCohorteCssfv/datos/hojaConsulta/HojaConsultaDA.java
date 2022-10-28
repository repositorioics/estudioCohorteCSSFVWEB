package ni.com.sts.estudioCohorteCssfv.datos.hojaConsulta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import ni.com.sts.estudioCohorteCSSFV.modelo.Diagnostico;
import ni.com.sts.estudioCohorteCSSFV.modelo.EscuelaCatalogo;
import ni.com.sts.estudioCohorteCSSFV.modelo.EstadosHoja;
import ni.com.sts.estudioCohorteCSSFV.modelo.EstudioCatalogo;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaInfluenza;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaZika;
import ni.com.sts.estudioCohorteCSSFV.modelo.SeguimientoInfluenza;
import ni.com.sts.estudioCohorteCSSFV.modelo.SeguimientoZika;
import ni.com.sts.estudioCohorteCssfv.dto.ExpedienteDTO;
import ni.com.sts.estudioCohorteCssfv.dto.OrdenesExamenes;
import ni.com.sts.estudioCohorteCssfv.servicios.HojaConsultaService;
import ni.com.sts.estudioCohorteCssfv.util.ConnectionDAO;
import ni.com.sts.estudioCohorteCssfv.util.HibernateResource;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;

public class HojaConsultaDA implements HojaConsultaService {
	
	private static String QUERY_HOJA_CONSULTA_BY_ID = "select h from HojaConsulta h where h.secHojaConsulta = :id";

	private static final HibernateResource hibernateResource = new HibernateResource();

	private ConnectionDAO connectionDAOClass = new ConnectionDAO();
	private Connection conn = null;
	PreparedStatement pst = null;

	@SuppressWarnings("unchecked")
	@Override
	public List<HojaConsulta> getHojasConsultaPendientesCarga() throws Exception {
		List<HojaConsulta> resultado = new ArrayList<HojaConsulta>();

		try {
			String sql = "select hc from HojaConsulta hc " + "where hc.estado = :estado "
					+ "and (hc.estadoCarga = :estadoCarga or hc.estadoCarga is null) "
					+ "and to_char(fechaCierre, 'yyyyMMdd') <= to_char(current_date, 'yyyyMMdd') "
					+ "and usuarioMedico not in(94, 60, 16, 98, 133) and usuarioEnfermeria not in(94, 60, 16, 98, 133)";

			Query q = hibernateResource.getSession().createQuery(sql);
			q.setCharacter("estado", '7');
			q.setParameter("estadoCarga", '0');

			String sql2 = "select d from Diagnostico d";
			Query q2 = hibernateResource.getSession().createQuery(sql2);

			// Diagnostico diagnostico = ((Diagnostico) q2.uniqueResult());
			List<Diagnostico> diagnostico = (List<Diagnostico>) q2.list();

			resultado = q.list();

			for (int i = 0; i < resultado.size(); i++) {
				for (int j = 0; j < diagnostico.size(); j++) {
					if (resultado.get(i).getDiagnostico1() != null) {
						if (resultado.get(i).getDiagnostico1() == diagnostico.get(j).getSecDiagnostico()) {
							resultado.get(i).setDiagnostico1(diagnostico.get(j).getCodigoDignostico());
						}
					}
					if (resultado.get(i).getDiagnostico2() != null) {
						if (resultado.get(i).getDiagnostico2() == diagnostico.get(j).getSecDiagnostico()) {
							resultado.get(i).setDiagnostico2(diagnostico.get(j).getCodigoDignostico());
						}
					}
					if (resultado.get(i).getDiagnostico3() != null) {
						if (resultado.get(i).getDiagnostico3() == diagnostico.get(j).getSecDiagnostico()) {
							resultado.get(i).setDiagnostico3(diagnostico.get(j).getCodigoDignostico());
						}
					}
					if (resultado.get(i).getDiagnostico4() != null) {
						if (resultado.get(i).getDiagnostico4() == diagnostico.get(j).getSecDiagnostico()) {
							resultado.get(i).setDiagnostico4(diagnostico.get(j).getCodigoDignostico());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}
		return resultado;
	}

	// Obteniendo todas las hojas de influenza que estan pendientes de subir a
	// OpenClinica
	@SuppressWarnings("unchecked")
	@Override
	public List<HojaInfluenza> getHojasInfluenzasPendientesCarga() throws Exception {
		List<HojaInfluenza> result = new ArrayList<HojaInfluenza>();
		try {
			String sql = "select hi" + " from HojaInfluenza hi" + " where hi.cerrado = :estado "
					+ " and (hi.estadoCarga = :estadoCarga or hi.estadoCarga is null)";

			Query q = hibernateResource.getSession().createQuery(sql);
			q.setCharacter("estado", 'S');
			q.setParameter("estadoCarga", '0');

			result = q.list();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}
		return result;
	}

	// Obteniendo todos los dias de seguimiento influenza por el numero de hoja
	// influenza
	@SuppressWarnings("unchecked")
	@Override
	public List<SeguimientoInfluenza> getSeguimientoInfluenza(int secHojaInfluenza) throws Exception {
		List<SeguimientoInfluenza> result = new ArrayList<SeguimientoInfluenza>();
		try {
			String sql = "select si " + " from SeguimientoInfluenza si "
					+ " where si.secHojaInfluenza = :secHojaInfluenza " + " order by si.controlDia asc";

			Query q = hibernateResource.getSession().createQuery(sql);
			q.setParameter("secHojaInfluenza", secHojaInfluenza);

			result = q.list();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}
		return result;
	}

	// Obteniendo todas las hojas de zika que estan pendientes de subir a
	// OpenClinica
	@SuppressWarnings("unchecked")
	@Override
	public List<HojaZika> getHojasZikaPendientesCarga() throws Exception {
		List<HojaZika> result = new ArrayList<HojaZika>();
		try {
			String sql = "select a " + " from HojaZika a " + " where a.cerrado = :estado "
					+ " and (a.estadoCarga = :estadoCarga or a.estadoCarga is null)";

			Query q = hibernateResource.getSession().createQuery(sql);
			q.setCharacter("estado", 'S');
			q.setParameter("estadoCarga", '0');

			result = q.list();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}
		return result;
	}

	// Obteniendo todos los dias de segumiento zika por el numero de hoja zika
	@SuppressWarnings("unchecked")
	@Override
	public List<SeguimientoZika> getSeguimientoZika(int secHojaZika) throws Exception {
		List<SeguimientoZika> result = new ArrayList<SeguimientoZika>();
		try {
			String sql = "select a " + " from  SeguimientoZika a " + " where a.secHojaZika = :secHojaZika "
					+ " order by a.controlDia asc";

			Query q = hibernateResource.getSession().createQuery(sql);
			q.setParameter("secHojaZika", secHojaZika);

			result = q.list();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}
		return result;
	}

	public void updateHojaConsulta(HojaConsulta hoja) throws Exception {
		try {
			conn = connectionDAOClass.getConection();
			pst = conn.prepareStatement("UPDATE hoja_consulta SET estado_carga = '1' where sec_hoja_consulta = ?");
			pst.setInt(1, hoja.getSecHojaConsulta());
			pst.executeUpdate();
			/*
			 * hibernateResource.begin(); hibernateResource.getSession().update(hoja);
			 * hibernateResource.getSession().flush(); hibernateResource.commit();
			 */
		} catch (Exception e) {
			e.printStackTrace();
			/* hibernateResource.rollback(); */
		} finally {
			/*
			 * if (hibernateResource.getSession().isOpen()) { hibernateResource.close(); }
			 */
			if (pst != null) {
				pst.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	public void updateHojaConsultaRepeatKey(HojaConsulta hoja) throws Exception {
		try {
			conn = connectionDAOClass.getConection();
			pst = conn.prepareStatement("UPDATE hoja_consulta SET repeat_key = ? where sec_hoja_consulta = ?");
			pst.setString(1, hoja.getRepeatKey());
			pst.setInt(2, hoja.getSecHojaConsulta());
			pst.executeUpdate();
			/*
			 * hibernateResource.begin(); hibernateResource.getSession().update(hoja);
			 * hibernateResource.getSession().flush(); hibernateResource.commit();
			 */
		} catch (Exception e) {
			e.printStackTrace();
			/* hibernateResource.rollback(); */
		} finally {
			/*
			 * if (hibernateResource.getSession().isOpen()) { hibernateResource.close(); }
			 */
			if (pst != null) {
				pst.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EstadosHoja> getEstadosHojaConsulta() throws Exception {
		List<EstadosHoja> resultado = new ArrayList<EstadosHoja>();

		try {
			String sql = "select eh from EstadosHoja eh " + "where eh.estado = :estado order by eh.codigo asc";

			Query q = hibernateResource.getSession().createQuery(sql);
			q.setCharacter("estado", '1');

			resultado = q.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}
		return resultado;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HojaConsulta> getHojaConsultaActivaHoyByCodExpediente(int codExpediente) throws Exception {
		List<HojaConsulta> lista = new ArrayList<HojaConsulta>();
		try {
			// Construir query
			String sql = "select hc from HojaConsulta hc " + "where hc.estado not in ('7','8') " + // cerrado o abandono
					"and hc.codExpediente = :codExpediente "
					+ "and to_char(hc.fechaConsulta,'ddMMyyyy') = to_char(CURRENT_DATE,'ddMMyyyy') ";

			Query q = hibernateResource.getSession().createQuery(sql);
			q.setInteger("codExpediente", codExpediente);

			lista = (List<HojaConsulta>) q.list();

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

	@SuppressWarnings("unchecked")
	@Override
	public HojaConsulta getHojaConsultaActivaByCodExpediente(int codExpediente) throws Exception {
		List<HojaConsulta> lista = new ArrayList<HojaConsulta>();
		HojaConsulta resultado = null;
		try {
			// Construir query
			String sql = "select hc from HojaConsulta hc "
					+ "where hc.codExpediente = :codExpediente order by hc.fechaConsulta desc";

			Query q = hibernateResource.getSession().createQuery(sql);
			q.setInteger("codExpediente", codExpediente);

			lista = (List<HojaConsulta>) q.list();
			if (lista != null && lista.size() > 0)
				resultado = lista.get(0);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}
		return resultado;
	}

	@Override
	public HojaConsulta getHojaConsultaByNumHoja(int numHoja) throws Exception {
		HojaConsulta resultado = null;
		try {
			// Construir query
			String sql = "select hc from HojaConsulta hc " + "where hc.numHojaConsulta = :numHoja";

			Query q = hibernateResource.getSession().createQuery(sql);
			q.setInteger("numHoja", numHoja);

			resultado = (HojaConsulta) q.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}
		return resultado;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HojaConsulta> getHojaConsultaActivaAndAdmiPenByCodExp(int codExpediente) throws Exception {
		List<HojaConsulta> lista = new ArrayList<HojaConsulta>();
		try {
			// Construir query
			String sql = "select hc from Admision ad, HojaConsulta hc "
					+ "where ad.numHojaConsulta = hc.numHojaConsulta and ad.codExpediente = hc.codExpediente "
					+ "and hc.estado not in ('7','8') " + // cerrado o abandono
					"and hc.codExpediente = :codExpediente  and ad.fechaEntrada is null";

			Query q = hibernateResource.getSession().createQuery(sql);
			q.setInteger("codExpediente", codExpediente);

			lista = (List<HojaConsulta>) q.list();

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

	@SuppressWarnings("unchecked")
	@Override
	public List<HojaConsulta> getHojaConsultaNoActivaAndAdmiPenByCodExp(int codExpediente) throws Exception {
		List<HojaConsulta> lista = new ArrayList<HojaConsulta>();
		try {
			// Construir query
			String sql = "select hc from Admision ad, HojaConsulta hc "
					+ "where ad.numHojaConsulta = hc.numHojaConsulta and ad.codExpediente = hc.codExpediente "
					+ "and hc.estado  in ('7','8') " + // cerrado o abandono
					"and hc.codExpediente = :codExpediente  and ad.fechaEntrada is null";

			Query q = hibernateResource.getSession().createQuery(sql);
			q.setInteger("codExpediente", codExpediente);

			lista = (List<HojaConsulta>) q.list();

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
	public EstadosHoja getEstadoHojaConsultaByNumHoja(int numHoja) throws Exception {
		EstadosHoja resultado = null;
		try {
			// Construir query
			String sql = "select eh from HojaConsulta hc, EstadosHoja eh "
					+ "where eh.codigo = hc.estado and hc.numHojaConsulta = :numHoja";

			Query q = hibernateResource.getSession().createQuery(sql);
			q.setInteger("numHoja", numHoja);

			resultado = (EstadosHoja) q.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}
		return resultado;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HojaConsulta> getHojasConsultaSinAdmision(int codExpediente) throws Exception {
		List<HojaConsulta> lista = new ArrayList<HojaConsulta>();
		try {
			// Construir query
			String sql = "select hc from HojaConsulta hc "
					+ "where hc.codExpediente = :codExpediente and hc.numHojaConsulta not in (select numHojaConsulta from Admision) "
					+ "order by hc.fechaConsulta desc";

			Query q = hibernateResource.getSession().createQuery(sql);
			q.setInteger("codExpediente", codExpediente);

			lista = (List<HojaConsulta>) q.list();
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

	public void updateHojaInfluenza(HojaInfluenza hoja) throws Exception {
		try {
			hibernateResource.begin();
			hibernateResource.getSession().update(hoja);
			hibernateResource.getSession().flush();
			hibernateResource.commit();
		} catch (Exception e) {
			e.printStackTrace();
			hibernateResource.rollback();
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}
	}

	public void updateHojaZika(HojaZika hoja) throws Exception {
		try {
			hibernateResource.begin();
			hibernateResource.getSession().update(hoja);
			hibernateResource.getSession().flush();
			hibernateResource.commit();
		} catch (Exception e) {
			e.printStackTrace();
			hibernateResource.rollback();
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}
	}

	/***
	 * Metodos para usar en la hoja de consulta web Fecha Creacion: 09/11/2021
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<Diagnostico> getDiagnosticos() throws Exception {
		List<Diagnostico> lista = new ArrayList<Diagnostico>();
		try {
			// Construir query
			String sql = "select d from Diagnostico d order by d.diagnostico asc";

			Query q = hibernateResource.getSession().createQuery(sql);

			lista = (List<Diagnostico>) q.list();

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

	@SuppressWarnings("unchecked")
	@Override
	public List<EscuelaCatalogo> getColegios() throws Exception {
		List<EscuelaCatalogo> lista = new ArrayList<EscuelaCatalogo>();
		try {
			// Construir query
			String sql = "select e from EscuelaCatalogo e order by e.descripcion asc";

			Query q = hibernateResource.getSession().createQuery(sql);

			lista = (List<EscuelaCatalogo>) q.list();

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
	public InfoResultado guardarHojaConsulta(HojaConsulta hojaConsulta) {
		InfoResultado infoResultado = new InfoResultado();
		int numHojaConsulta;
		try {

			String sql = "select max(h.ordenLlegada) " + " from HojaConsulta h "
					+ " where to_char(h.fechaConsulta, 'yyyyMMdd') = to_char(current_date, 'yyyyMMdd') ";

			Query query = hibernateResource.getSession().createQuery(sql);

			Integer maxOrdenLlegada = 0;

			if (query.uniqueResult() != null) {
				maxOrdenLlegada = ((Short) query.uniqueResult()).intValue();
			}

			sql = "select max(h.numHojaConsulta) " + " from HojaConsulta h";

			query = hibernateResource.getSession().createQuery(sql);

			numHojaConsulta = ((query.uniqueResult() == null) ? 1 : ((Integer) query.uniqueResult()).intValue()) + 1;

			hojaConsulta.setOrdenLlegada(Short.valueOf((maxOrdenLlegada + 1) + ""));
			hojaConsulta.setNumHojaConsulta(numHojaConsulta);

			hibernateResource.begin();
			hibernateResource.getSession().save(hojaConsulta);
			hibernateResource.getSession().flush();
			hibernateResource.commit();

			infoResultado.setOk(true);
			infoResultado.setObjeto(hojaConsulta);
			infoResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
		} catch (Exception e) {
			e.printStackTrace();
			hibernateResource.rollback();
			infoResultado.setOk(false);
			infoResultado.setExcepcion(true);
			infoResultado.setGravedad(infoResultado.ERROR);
			infoResultado.setMensaje(Mensajes.REGISTRO_NO_GUARDADO + "\n" + e.getMessage());
			infoResultado.setFuenteError("Hoja Consulta");
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}

		return infoResultado;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ExpedienteDTO> listaExpedienteHojaConsulta(int codigoExpediente) {
		List<ExpedienteDTO> result = new ArrayList<ExpedienteDTO>();
		try {
			
			String sql = "select  " + " h.numHojaConsulta, "
					+ " to_char(h.fechaCierre, 'DD-MON-YY'), "
					+ " to_char(h.fechaCierre, 'HH:MI:SS AM'), "
					+ " e.descripcion, " + 
					" (select um.nombre from UsuariosView um where h.usuarioMedico = um.id),  " +
					" h.secHojaConsulta "
					+ " from HojaConsulta h, EstadosHoja e"
					+ " where h.estado = e.codigo ";

			sql += " and h.codExpediente=:codExpediente ";
			
			sql += "order by h.numHojaConsulta desc";

			Query query = hibernateResource.getSession().createQuery(sql);
			
			if (codigoExpediente > 0)
				query.setParameter("codExpediente", codigoExpediente);
			
			List<Object[]> objLista = (List<Object[]>) query.list();
			
			if (objLista != null && objLista.size() > 0) {
				for (Object[] object : objLista) {
					ExpedienteDTO expediente = new ExpedienteDTO();
					expediente.setNumeroHoja(object[0].toString());
					expediente.setFechaCierre(object[1] != null ? object[1].toString() : "----");
					expediente.setHoraCierre(object[2] != null ? object[2].toString() : "----");
					expediente.setEstado(object[3].toString());
					expediente.setMedico(object[4] != null ? object[4].toString() : "----");
					expediente.setSecHojaConsulta(Integer.valueOf(object[5].toString()));
					
					result.add(expediente);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (hibernateResource.getSession().isOpen()) {
				hibernateResource.close();
			}
		}
		return result;
	}
}
