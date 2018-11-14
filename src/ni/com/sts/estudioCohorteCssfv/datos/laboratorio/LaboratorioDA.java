package ni.com.sts.estudioCohorteCssfv.datos.laboratorio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Query;

import ni.com.sts.estudioCohorteCSSFV.modelo.EghResultados;
import ni.com.sts.estudioCohorteCSSFV.modelo.EgoResultados;
import ni.com.sts.estudioCohorteCSSFV.modelo.EstudioCatalogo;
import ni.com.sts.estudioCohorteCSSFV.modelo.InfluenzaMuestra;
import ni.com.sts.estudioCohorteCSSFV.modelo.MalariaResultados;
import ni.com.sts.estudioCohorteCSSFV.modelo.OrdenLaboratorio;
import ni.com.sts.estudioCohorteCSSFV.modelo.Paciente;
import ni.com.sts.estudioCohorteCSSFV.modelo.PerifericoResultado;
import ni.com.sts.estudioCohorteCSSFV.modelo.ResultadoExamenMuestra;
import ni.com.sts.estudioCohorteCSSFV.modelo.SerologiaChikMuestra;
import ni.com.sts.estudioCohorteCSSFV.modelo.SerologiaDengueMuestra;
import ni.com.sts.estudioCohorteCssfv.dto.OrdenesExamenes;
import ni.com.sts.estudioCohorteCssfv.servicios.LaboratorioService;
import ni.com.sts.estudioCohorteCssfv.util.HibernateResource;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilDate;

public class LaboratorioDA implements LaboratorioService {

	private static final HibernateResource HIBERNATE_RESOURCE = new HibernateResource();

	@SuppressWarnings("unchecked")
	@Override
	public List<OrdenesExamenes> listaPacienteLab(String codigoExpediente, String estado) {
		List<OrdenesExamenes> result = new ArrayList<OrdenesExamenes>();
		try {
			// Construir query
			
			String sql = "select h.secHojaConsulta, h.codExpediente, uv.nombre, "
					+ " h.numOrdenLaboratorio, to_char(h.fechaOrdenLaboratorio, 'yyyyMMdd HH:MI:SS am'), "
					+ " p.nombre1, p.nombre2, p.apellido1, p.apellido2, "
					+ " case when (select count(ol.numOrdenLaboratorio) from OrdenLaboratorio ol where h.numOrdenLaboratorio = ol.numOrdenLaboratorio and ol.estado = '0') > 0 then 'Pendiente' else 'Enviado' end "
					+ " from HojaConsulta h, Paciente p, UsuariosView uv"
					+ " where h.codExpediente = p.codExpediente "
					+ " and CASE WHEN h.medicoCambioTurno is null THEN h.usuarioMedico ELSE h.medicoCambioTurno END = uv.id "
					+ " and h.fechaOrdenLaboratorio is not null";
			if (codigoExpediente!=null){
				sql = sql +" and h.codExpediente = :codigoExpediente ";
			}
			if (codigoExpediente==null && estado==null){
				sql = sql + " and to_char(h.fechaOrdenLaboratorio, 'yyyyMMdd') = to_char(current_date, 'yyyyMMdd') ";
			}
					sql = sql + " group by h.secHojaConsulta, h.codExpediente, uv.nombre, h.numOrdenLaboratorio, "
					+ " h.fechaOrdenLaboratorio, "
					+ " p.nombre1, p.nombre2, p.apellido1, p.apellido2 " +
					"order by h.numOrdenLaboratorio asc, h.fechaOrdenLaboratorio asc";

			Query query = HIBERNATE_RESOURCE.getSession().createQuery(sql);
			if (codigoExpediente!=null){
				query.setParameter("codigoExpediente", Integer.valueOf(codigoExpediente));
			}

			List<Object[]> lista = (List<Object[]>) query.list();

			if (lista != null && lista.size() > 0) {
				for (Object[] object : lista) {
					Calendar fechaOrdenLab = new GregorianCalendar();

					SimpleDateFormat sdfFechaOrdenLab = new SimpleDateFormat(
							"yyyyMMdd hh:mm:ss a", Locale.ENGLISH);

					fechaOrdenLab.setTime(sdfFechaOrdenLab.parse(object[4]
							.toString()));

					OrdenesExamenes ordenesExamenes = new OrdenesExamenes();
					// UsuariosView usuario = new UsuariosView();

					ordenesExamenes.setSecHojaConsulta(Integer
							.valueOf(object[0].toString()));
					ordenesExamenes.setCodigoExpediente(Integer
							.valueOf(object[1].toString()));
					ordenesExamenes.setNombre(String.valueOf(object[2]
							.toString()));
					/*
					 * if (object[2] != null){
					 * ordenesExamenes.setUsuarioMedico(String
					 * .valueOf(object[2].toString())); } else if (object[3] !=
					 * null){
					 * ordenesExamenes.setMedicoCambioTurno(String.valueOf
					 * (object[3].toString())); }
					 */
					ordenesExamenes.setNumOrdenLaboratorio(String
							.valueOf(object[3].toString()));
					ordenesExamenes.setFechaOrdenLaboratorio(fechaOrdenLab);
					ordenesExamenes.setNombrePaciente(String.valueOf(object[5].toString() + " "
							+ ((object[6] != null) ? object[6].toString() : "") + " "
							+ object[7].toString() + " "
							+ ((object[8] != null) ? object[8].toString() : "")));
					ordenesExamenes.setEstado((object[9].toString()));
					//se filtra por estado
					if (estado !=null) {
						if (ordenesExamenes.getEstado().equals(estado)){
							result.add(ordenesExamenes);
						}
					}else{
						result.add(ordenesExamenes);
					}
					
					sql = "select ec from ConsEstudios c, EstudioCatalogo ec " + 
							" where c.codigoConsentimiento = ec.codEstudio"  + 
							" and c.codigoExpediente = :codExpediente " + 
							" and c.retirado != '1' " +
							" group by ec.codEstudio, ec.descEstudio";
					
					query = HIBERNATE_RESOURCE.getSession().createQuery(sql);

					query.setParameter("codExpediente", ordenesExamenes.getCodigoExpediente());

					List<EstudioCatalogo> lstConsEstudios = query.list();
					StringBuffer codigosEstudios = new StringBuffer();

					for (EstudioCatalogo estudioCatalogo : lstConsEstudios) {
						codigosEstudios.append(
								estudioCatalogo.getDescEstudio()).append(",");
					}
					
					ordenesExamenes.setEstudios(codigosEstudios != null
							&& !codigosEstudios.toString().isEmpty() ? (codigosEstudios
							.substring(0, (codigosEstudios.length() - 1)))
							: "");

				}
				Set set = new TreeSet<OrdenesExamenes>(new Comparator<OrdenesExamenes>() {
					@Override
					public int compare(OrdenesExamenes o1, OrdenesExamenes o2) {
						if(o1.getNumOrdenLaboratorio().compareToIgnoreCase(o2.getNumOrdenLaboratorio()) == 0 &&
			        			o1.getEstado().compareTo("Enviado") == 0) {
							return 0;
						}
			        	return 1;
					}
				});
				set.addAll(result);

				result = new ArrayList(set);

				Collections.sort(result, new Comparator<OrdenesExamenes>(){

					@Override
					public int compare(OrdenesExamenes o1, OrdenesExamenes o2) {
						// TODO Auto-generated method stub
						return o2.getEstado().compareTo(o1.getEstado());
					}

				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}

		return result;
	}

	@Override
	public Paciente obtenerInfoPaciente(int codExpediente) {
		Paciente result = new Paciente();
		try {
			String sql = "select p " + " from Paciente p "
					+ " where p.codExpediente = :codExpediente ";

			Query q = HIBERNATE_RESOURCE.getSession().createQuery(sql);
			q.setParameter("codExpediente", codExpediente);

			Paciente paciente = (Paciente) q.uniqueResult();

			if (paciente != null && paciente.getSecPaciente() > 0) {
				result = paciente;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}
		return result;
	}
	
	@Override
	public String obtenerEdadCalculadaPaciente(int codExpediente) {
		String result = "";
		try {
			String sql = "select obtenerEdad(fecha_nac) edad " +
					"from paciente p "
					+ " where p.cod_expediente = :codExpediente ";

			Query q = HIBERNATE_RESOURCE.getSession().createSQLQuery(sql);
			q.setParameter("codExpediente", codExpediente);

			Object edadCalculada =  q.uniqueResult();

			if (edadCalculada != null) {
				result = edadCalculada.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}
		return result;
	}

	@Override
	public List<OrdenesExamenes> listaIngresoResultados(int codExpediente, int secHojaConsulta) {
		List<OrdenesExamenes> result = new ArrayList<OrdenesExamenes>();
		try {
			// Construir query
			String sql = "select ol.examen, case when ol.estado = '0' then 'Pendiente' when ol.estado = '1' then 'Enviado' else 'Cancelado' end , "
					+ " to_char(h.fechaOrdenLaboratorio, 'yyyyMMdd HH:MI:SS am' ), "
					+ " ol.secOrdenLaboratorio, ol.hojaConsulta.secHojaConsulta, ol.tomaMx, "
					+ " h.codExpediente, h.usuarioMedico, h.medicoCambioTurno, ol.fechaHoraTomaMx, ol.razonCancela "
					+ " from HojaConsulta h, OrdenLaboratorio ol "
					+ " where h.numOrdenLaboratorio = ol.numOrdenLaboratorio "
					+ " and h.codExpediente = :codExpediente "
					//+ " and ol.examen not in('BHC', 'AST/ALT', 'Factor Reumatoideo', 'Albumina', 'Billirubinas', 'CPK', 'Colesterol') "
					+ " and h.secHojaConsulta = :secHojaConsulta";

			Query query = HIBERNATE_RESOURCE.getSession().createQuery(sql);
			query.setParameter("codExpediente", codExpediente);
			query.setParameter("secHojaConsulta", secHojaConsulta);

			List<Object[]> lista = (List<Object[]>) query.list();

			if (lista != null && lista.size() > 0) {
				for (Object[] object : lista) {
					Calendar fechaOrdenLab = new GregorianCalendar();
					Calendar fechaTomaMx = new GregorianCalendar();
					// Calendar horaOrden = new GregorianCalendar();

					SimpleDateFormat sdfFechaOrdenLab = new SimpleDateFormat(
							"yyyyMMdd hh:mm:ss a", Locale.ENGLISH);

					fechaOrdenLab.setTime(sdfFechaOrdenLab.parse(object[2]
							.toString()));

					OrdenesExamenes ordenesExamenes = new OrdenesExamenes();

					ordenesExamenes.setExamen((object[0].toString()));
					ordenesExamenes.setEstado((object[1].toString()));
					ordenesExamenes.setFechaOrdenLaboratorio(fechaOrdenLab);
					ordenesExamenes.setSecOrdenLaboratorio(Integer
							.valueOf(object[3].toString()));
					ordenesExamenes.setSecHojaConsulta(Integer.valueOf(object[4].toString()));
					ordenesExamenes.setTomaMx((object[5].toString()));
					ordenesExamenes.setCodigoExpediente(Integer.valueOf(object[6].toString()));
					if (object[7]!=null)
					ordenesExamenes.setUsuarioMedico(object[7].toString());
					if (object[8]!=null)
					ordenesExamenes.setMedicoCambioTurno(object[8].toString());
					if (object[9]!=null){
						ordenesExamenes.setFechaHoraTomaMx(object[9].toString());
					}

					if (ordenesExamenes.getEstado().equalsIgnoreCase("Cancelado")){
						ordenesExamenes.setRazonCancela(object[10].toString());
					}else{
						ordenesExamenes.setRazonCancela("");
						if (object[0].toString().trim().toUpperCase()
								.compareTo("EGO") == 0) {
							sql = " select to_char(e.horaReporte, 'yyyyMMdd HH:MI:SS am')"
									+ " from EgoResultados e "
									+ " join e.ordenLaboratorio o"
									+ " where o.secOrdenLaboratorio = :secOrdenLaboratorio";
	
							Query queryEGO = HIBERNATE_RESOURCE.getSession()
									.createQuery(sql);
							queryEGO.setParameter("secOrdenLaboratorio", object[3]);
	
							String listaEGO = (String) queryEGO.uniqueResult();
	
							if (listaEGO != null) {
								Calendar horaReporte = new GregorianCalendar();
								SimpleDateFormat sdfhoraReporte = new SimpleDateFormat(
										"yyyyMMdd hh:mm:ss a", Locale.ENGLISH);
								horaReporte.setTime(sdfhoraReporte.parse(listaEGO));
								ordenesExamenes.setHoraReporte(horaReporte);
	
								ordenesExamenes.setEstado("Enviado");
							}
	
						} else if (object[0].toString().trim().toUpperCase()
								.compareTo("EGH") == 0) {
							sql = " select to_char(e.horaReporte, 'yyyyMMdd HH:MI:SS am')"
									+ " from EghResultados e "
									+ " where e.secOrdenLaboratorio = :secOrdenLaboratorio";
	
							Query queryEGH = HIBERNATE_RESOURCE.getSession()
									.createQuery(sql);
							queryEGH.setParameter("secOrdenLaboratorio", object[3]);
	
							String listaEGH = (String) queryEGH.uniqueResult();
							if (listaEGH != null) {
								Calendar horaReporte = new GregorianCalendar();
								SimpleDateFormat sdfhoraReporte = new SimpleDateFormat(
										"yyyyMMdd hh:mm:ss a", Locale.ENGLISH);
								horaReporte.setTime(sdfhoraReporte.parse(listaEGH));
								ordenesExamenes.setHoraReporte(horaReporte);
								ordenesExamenes.setEstado("Enviado");
							}
	
						} else if (object[0].toString().trim().toUpperCase()
								.compareTo("CITOLOGIA FECAL") == 0) {
							sql = " select to_char(e.horaReporte, 'yyyyMMdd HH:MI:SS am')"
									+ " from EghResultados e "
									+ " where e.secOrdenLaboratorio = :secOrdenLaboratorio";
	
							Query queryEGH = HIBERNATE_RESOURCE.getSession()
									.createQuery(sql);
							queryEGH.setParameter("secOrdenLaboratorio", object[3]);
	
							String listaEGH = (String) queryEGH.uniqueResult();
							if (listaEGH != null) {
								Calendar horaReporte = new GregorianCalendar();
								SimpleDateFormat sdfhoraReporte = new SimpleDateFormat(
										"yyyyMMdd hh:mm:ss a", Locale.ENGLISH);
								horaReporte.setTime(sdfhoraReporte.parse(listaEGH));
								ordenesExamenes.setHoraReporte(horaReporte);
								ordenesExamenes.setEstado("Enviado");
							}
	
						} else if (object[0].toString().trim().toUpperCase()
								.compareTo("GOTA GRUESA") == 0) {
							sql = " select to_char(m.horaReporte, 'yyyyMMdd HH:MI:SS am') "
									+ " from MalariaResultados m "
									+ " where m.secOrdenLaboratorio = :secOrdenLaboratorio ";
	
							Query queryMalariaResult = HIBERNATE_RESOURCE
									.getSession().createQuery(sql);
							queryMalariaResult.setParameter("secOrdenLaboratorio",
									object[3]);
	
							String listaMalariaResult = (String) queryMalariaResult
									.uniqueResult();
							if (listaMalariaResult != null) {
								Calendar horaReporte = new GregorianCalendar();
								SimpleDateFormat sdfhoraReporte = new SimpleDateFormat(
										"yyyyMMdd hh:mm:ss a", Locale.ENGLISH);
								horaReporte.setTime(sdfhoraReporte
										.parse(listaMalariaResult));
								ordenesExamenes.setHoraReporte(horaReporte);
								ordenesExamenes.setEstado("Enviado");
	
							}
	
						} else if (object[0].toString().trim().toUpperCase()
								.compareTo("EXTENDIDO PERIFERICO") == 0) {
							sql = " select to_char(p.horaReporte, 'yyyyMMdd HH:MI:SS am') "
									+ " from PerifericoResultado p "
									+ " where p.secOrdenLaboratorio = :secOrdenLaboratorio ";
	
							Query queryExtendidoPer = HIBERNATE_RESOURCE
									.getSession().createQuery(sql);
							queryExtendidoPer.setParameter("secOrdenLaboratorio",
									object[3]);
	
							String listaExtendidoPer = (String) queryExtendidoPer
									.uniqueResult();
	
							if (listaExtendidoPer != null) {
								Calendar horaReporte = new GregorianCalendar();
								SimpleDateFormat sdfhoraReporte = new SimpleDateFormat(
										"yyyyMMdd hh:mm:ss a", Locale.ENGLISH);
								horaReporte.setTime(sdfhoraReporte
										.parse(listaExtendidoPer));
								ordenesExamenes.setHoraReporte(horaReporte);
								ordenesExamenes.setEstado("Enviado");
							}
							// ===================================================================================
	
						} else if (object[0].toString().trim().toUpperCase()
								.compareTo("INFLUENZA") == 0) {
							sql = " select to_char(im.horaReporte, 'yyyyMMdd HH:MI:SS am') "
									+ " from InfluenzaMuestra im "
									+ " join im.ordenLaboratorio o"
									+ " where o.secOrdenLaboratorio = :secOrdenLaboratorio ";
	
							Query queryInfluenza = HIBERNATE_RESOURCE
									.getSession().createQuery(sql);
							queryInfluenza.setParameter("secOrdenLaboratorio",
									object[3]);
	
							String listaInfluenza = (String) queryInfluenza
									.uniqueResult();
	
							if (listaInfluenza != null) {
								Calendar horaReporte = new GregorianCalendar();
								SimpleDateFormat sdfhoraReporte = new SimpleDateFormat(
										"yyyyMMdd hh:mm:ss a", Locale.ENGLISH);
								horaReporte.setTime(sdfhoraReporte
										.parse(listaInfluenza));
								ordenesExamenes.setHoraReporte(horaReporte);
								ordenesExamenes.setEstado("Enviado");
							}
						} else if (object[0].toString().trim().toUpperCase()
								.compareTo("SEROLOGIA DENGUE") == 0) {
							sql = " select to_char(sd.horaReporte, 'yyyyMMdd HH:MI:SS am') "
									+ " from SerologiaDengueMuestra sd "
									+ " join sd.ordenLaboratorio o"
									+ " where o.secOrdenLaboratorio = :secOrdenLaboratorio ";
	
							Query querySerologiaDengue = HIBERNATE_RESOURCE
									.getSession().createQuery(sql);
							querySerologiaDengue.setParameter("secOrdenLaboratorio",
									object[3]);
	
							String listaSerologiaDengue = (String) querySerologiaDengue
									.uniqueResult();
	
							if (listaSerologiaDengue != null) {
								Calendar horaReporte = new GregorianCalendar();
								SimpleDateFormat sdfhoraReporte = new SimpleDateFormat(
										"yyyyMMdd hh:mm:ss a", Locale.ENGLISH);
								horaReporte.setTime(sdfhoraReporte
										.parse(listaSerologiaDengue));
								ordenesExamenes.setHoraReporte(horaReporte);
								ordenesExamenes.setEstado("Enviado");
							}
						} else if (object[0].toString().trim().toUpperCase()
								.compareTo("SEROLOGIA CHICK") == 0) {
							sql = " select to_char(sc.horaReporte, 'yyyyMMdd HH:MI:SS am') "
									+ " from SerologiaChikMuestra sc "
									+ " join sc.ordenLaboratorio o"
									+ " where o.secOrdenLaboratorio = :secOrdenLaboratorio ";
	
							Query querySerologiaChick = HIBERNATE_RESOURCE
									.getSession().createQuery(sql);
							querySerologiaChick.setParameter("secOrdenLaboratorio",
									object[3]);
	
							String listaSerologiaChick = (String) querySerologiaChick
									.uniqueResult();
	
							if (listaSerologiaChick != null) {
								Calendar horaReporte = new GregorianCalendar();
								SimpleDateFormat sdfhoraReporte = new SimpleDateFormat(
										"yyyyMMdd hh:mm:ss a", Locale.ENGLISH);
								horaReporte.setTime(sdfhoraReporte
										.parse(listaSerologiaChick));
								ordenesExamenes.setHoraReporte(horaReporte);
								ordenesExamenes.setEstado("Enviado");
							}
						}else {
							sql = " select sc.fechaHoraReporte "
									+ " from ResultadoExamenMuestra sc "
									+ " join sc.ordenLaboratorio o"
									+ " where o.secOrdenLaboratorio = :secOrdenLaboratorio ";
	
							Query queryResultadoExamen = HIBERNATE_RESOURCE
									.getSession().createQuery(sql);
							queryResultadoExamen.setParameter("secOrdenLaboratorio",
									object[3]);
	
							String sHoraReporte = (String) queryResultadoExamen
									.uniqueResult();
	
							if (sHoraReporte != null) {
								Date dHoraReporte = UtilDate.StringToDate(sHoraReporte, "dd/MM/yyyy HH:mm:ss");
								Calendar cHoraReporte = new GregorianCalendar();
								cHoraReporte.setTime(dHoraReporte);
								ordenesExamenes.setHoraReporte(cHoraReporte);
								ordenesExamenes.setEstado("Enviado");
							}
						}
					}
					result.add(ordenesExamenes);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}

		return result;
	}

	@Override
	public InfoResultado guardarExamenEgo(EgoResultados egoResultados) {
		InfoResultado infoResultadoEGO = new InfoResultado();
		OrdenesExamenes ordenesExamenes = new OrdenesExamenes();
		if (egoResultados.getEstado() == 0) {

		}
		try {

			HIBERNATE_RESOURCE.begin();
			HIBERNATE_RESOURCE.getSession().saveOrUpdate(egoResultados);
			if (esTodoEstadosEnviadoExamenes(egoResultados
					.getOrdenLaboratorio().getSecOrdenLaboratorio())) {
				egoResultados.getOrdenLaboratorio().setEstado('1');
				HIBERNATE_RESOURCE.getSession().saveOrUpdate(
						egoResultados.getOrdenLaboratorio());
			}
			HIBERNATE_RESOURCE.getSession().flush();
			HIBERNATE_RESOURCE.commit();

			infoResultadoEGO.setOk(true);
			infoResultadoEGO.setObjeto(egoResultados);
			infoResultadoEGO.setMensaje(Mensajes.REGISTRO_GUARDADO);
		} catch (Exception e) {
			e.printStackTrace();
			infoResultadoEGO.setOk(false);
			infoResultadoEGO.setExcepcion(true);
			infoResultadoEGO.setGravedad(infoResultadoEGO.ERROR);
			infoResultadoEGO.setMensaje(Mensajes.REGISTRO_NO_GUARDADO + "\n"
					+ e.getMessage());
			infoResultadoEGO.setFuenteError("EgoResultados");
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}

		return infoResultadoEGO;
	}

	@Override
	public InfoResultado guardarExamenEgh(EghResultados eghResultadosId) {
		InfoResultado infoResultadoEGH = new InfoResultado();
		try {

			HIBERNATE_RESOURCE.begin();
			HIBERNATE_RESOURCE.getSession().saveOrUpdate(eghResultadosId);
			if (esTodoEstadosEnviadoExamenes(eghResultadosId
					.getSecOrdenLaboratorio())) {
				OrdenLaboratorio ordenLab = new OrdenLaboratorio();
				ordenLab = ordenLabCambioEstado(eghResultadosId
						.getSecOrdenLaboratorio());
				ordenLab.setEstado('1');
				HIBERNATE_RESOURCE.getSession().saveOrUpdate(ordenLab);
			}
			HIBERNATE_RESOURCE.getSession().flush();
			HIBERNATE_RESOURCE.commit();

			infoResultadoEGH.setOk(true);
			infoResultadoEGH.setObjeto(eghResultadosId);
			infoResultadoEGH.setMensaje(Mensajes.REGISTRO_GUARDADO);
		} catch (Exception e) {
			e.printStackTrace();
			infoResultadoEGH.setOk(false);
			infoResultadoEGH.setExcepcion(true);
			infoResultadoEGH.setGravedad(infoResultadoEGH.ERROR);
			infoResultadoEGH.setMensaje(Mensajes.REGISTRO_NO_GUARDADO + "\n"
					+ e.getMessage());
			infoResultadoEGH.setFuenteError("EghResultados");
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}

		return infoResultadoEGH;
	}

	@Override
	public InfoResultado guardarExamenMSPGotaGruesa(
			MalariaResultados malariaResultadoId) {
		InfoResultado infoResultadoMSP = new InfoResultado();

		try {

			HIBERNATE_RESOURCE.begin();
			HIBERNATE_RESOURCE.getSession().saveOrUpdate(malariaResultadoId);
			if (esTodoEstadosEnviadoExamenes(malariaResultadoId
					.getSecOrdenLaboratorio())) {
				OrdenLaboratorio ordenLab = new OrdenLaboratorio();
				ordenLab = ordenLabCambioEstado(malariaResultadoId
						.getSecOrdenLaboratorio());
				ordenLab.setEstado('1');
				HIBERNATE_RESOURCE.getSession().saveOrUpdate(ordenLab);
			}
			HIBERNATE_RESOURCE.getSession().flush();
			HIBERNATE_RESOURCE.commit();

			infoResultadoMSP.setOk(true);
			infoResultadoMSP.setObjeto(malariaResultadoId);
			infoResultadoMSP.setMensaje(Mensajes.REGISTRO_GUARDADO);
		} catch (Exception e) {
			e.printStackTrace();
			infoResultadoMSP.setOk(false);
			infoResultadoMSP.setExcepcion(true);
			infoResultadoMSP.setGravedad(infoResultadoMSP.ERROR);
			infoResultadoMSP.setMensaje(Mensajes.REGISTRO_NO_GUARDADO + "\n"
					+ e.getMessage());
			infoResultadoMSP.setFuenteError("EgoResultados");
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}

		return infoResultadoMSP;
	}

	@Override
	public InfoResultado guardarExamenExtendidoPeriferico(
			PerifericoResultado perifericoResultadoId) {
		InfoResultado infoResultadoEP = new InfoResultado();

		try {

			HIBERNATE_RESOURCE.begin();
			HIBERNATE_RESOURCE.getSession().saveOrUpdate(perifericoResultadoId);
			if (esTodoEstadosEnviadoExamenes(perifericoResultadoId
					.getSecOrdenLaboratorio())) {
				OrdenLaboratorio ordenLab = new OrdenLaboratorio();
				ordenLab = ordenLabCambioEstado(perifericoResultadoId
						.getSecOrdenLaboratorio());
				ordenLab.setEstado('1');
				HIBERNATE_RESOURCE.getSession().saveOrUpdate(ordenLab);
			}
			HIBERNATE_RESOURCE.getSession().flush();
			HIBERNATE_RESOURCE.commit();

			infoResultadoEP.setOk(true);
			infoResultadoEP.setObjeto(perifericoResultadoId);
			infoResultadoEP.setMensaje(Mensajes.REGISTRO_GUARDADO);
		} catch (Exception e) {
			e.printStackTrace();
			infoResultadoEP.setOk(false);
			infoResultadoEP.setExcepcion(true);
			infoResultadoEP.setGravedad(infoResultadoEP.ERROR);
			infoResultadoEP.setMensaje(Mensajes.REGISTRO_NO_GUARDADO + "\n"
					+ e.getMessage());
			infoResultadoEP.setFuenteError("EgoResultados");
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}

		return infoResultadoEP;
	}

	// ============================================================================

	@Override
	public InfoResultado guardarMuestraDengue(
			SerologiaDengueMuestra serologiaDengueMuestra) {
		InfoResultado infoResultadoSDengueM = new InfoResultado();

		try {

			HIBERNATE_RESOURCE.begin();
			HIBERNATE_RESOURCE.getSession()
					.saveOrUpdate(serologiaDengueMuestra);
			if (esTodoEstadosEnviadoExamenes(serologiaDengueMuestra
					.getOrdenLaboratorio().getSecOrdenLaboratorio())) {
				serologiaDengueMuestra.getOrdenLaboratorio().setEstado('1');
				HIBERNATE_RESOURCE.getSession().saveOrUpdate(
						serologiaDengueMuestra.getOrdenLaboratorio());
			}
			HIBERNATE_RESOURCE.getSession().flush();
			HIBERNATE_RESOURCE.commit();

			infoResultadoSDengueM.setOk(true);
			infoResultadoSDengueM.setObjeto(serologiaDengueMuestra);
			infoResultadoSDengueM.setMensaje(Mensajes.REGISTRO_GUARDADO);
		} catch (Exception e) {
			e.printStackTrace();
			infoResultadoSDengueM.setOk(false);
			infoResultadoSDengueM.setExcepcion(true);
			infoResultadoSDengueM.setGravedad(infoResultadoSDengueM.ERROR);
			infoResultadoSDengueM.setMensaje(Mensajes.REGISTRO_NO_GUARDADO
					+ "\n" + e.getMessage());
			infoResultadoSDengueM.setFuenteError("EgoResultados");
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}

		return infoResultadoSDengueM;
	}

	@Override
	public InfoResultado guardarMuestraInfluenza(
			InfluenzaMuestra influenzaMuestra) {
		InfoResultado infoResultadoMuestraInfluenza = new InfoResultado();

		try {

			HIBERNATE_RESOURCE.begin();
			HIBERNATE_RESOURCE.getSession().saveOrUpdate(influenzaMuestra);
			if (esTodoEstadosEnviadoExamenes(influenzaMuestra
					.getOrdenLaboratorio().getSecOrdenLaboratorio())) {
				influenzaMuestra.getOrdenLaboratorio().setEstado('1');
				HIBERNATE_RESOURCE.getSession().saveOrUpdate(
						influenzaMuestra.getOrdenLaboratorio());
			}
			HIBERNATE_RESOURCE.getSession().flush();
			HIBERNATE_RESOURCE.commit();

			infoResultadoMuestraInfluenza.setOk(true);
			infoResultadoMuestraInfluenza.setObjeto(influenzaMuestra);
			infoResultadoMuestraInfluenza
					.setMensaje(Mensajes.REGISTRO_GUARDADO);
		} catch (Exception e) {
			e.printStackTrace();
			infoResultadoMuestraInfluenza.setOk(false);
			infoResultadoMuestraInfluenza.setExcepcion(true);
			infoResultadoMuestraInfluenza
					.setGravedad(infoResultadoMuestraInfluenza.ERROR);
			infoResultadoMuestraInfluenza
					.setMensaje(Mensajes.REGISTRO_NO_GUARDADO + "\n"
							+ e.getMessage());
			infoResultadoMuestraInfluenza.setFuenteError("EgoResultados");
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}

		return infoResultadoMuestraInfluenza;
	}

	@Override
	public InfoResultado guardarMuestraChick(
			SerologiaChikMuestra serologiaChikMuestra) {
		InfoResultado infoResultadoMuestraChick = new InfoResultado();

		try {

			HIBERNATE_RESOURCE.begin();
			HIBERNATE_RESOURCE.getSession().saveOrUpdate(serologiaChikMuestra);
			if (esTodoEstadosEnviadoExamenes(serologiaChikMuestra
					.getOrdenLaboratorio().getSecOrdenLaboratorio())) {
				serologiaChikMuestra.getOrdenLaboratorio().setEstado('1');
				HIBERNATE_RESOURCE.getSession().saveOrUpdate(
						serologiaChikMuestra.getOrdenLaboratorio());
			}
			HIBERNATE_RESOURCE.getSession().flush();
			HIBERNATE_RESOURCE.commit();

			infoResultadoMuestraChick.setOk(true);
			infoResultadoMuestraChick.setObjeto(serologiaChikMuestra);
			infoResultadoMuestraChick.setMensaje(Mensajes.REGISTRO_GUARDADO);
		} catch (Exception e) {
			e.printStackTrace();
			infoResultadoMuestraChick.setOk(false);
			infoResultadoMuestraChick.setExcepcion(true);
			infoResultadoMuestraChick
					.setGravedad(infoResultadoMuestraChick.ERROR);
			infoResultadoMuestraChick.setMensaje(Mensajes.REGISTRO_NO_GUARDADO
					+ "\n" + e.getMessage());
			infoResultadoMuestraChick.setFuenteError("EgoResultados");
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}

		return infoResultadoMuestraChick;
	}

	@Override
	public OrdenLaboratorio obtenerOrdenLab(int secOrdenLaboratorio) {
		OrdenLaboratorio result = new OrdenLaboratorio();
		try {
			String sql = "select ol " +
					" from OrdenLaboratorio ol " +
					" where ol.secOrdenLaboratorio = :secOrdenLaboratorio";

			Query q = HIBERNATE_RESOURCE.getSession().createQuery(sql);
			q.setParameter("secOrdenLaboratorio", secOrdenLaboratorio);

			OrdenLaboratorio obtenerOrdenLab = (OrdenLaboratorio) q
					.uniqueResult();

			if (obtenerOrdenLab != null
					&& obtenerOrdenLab.getSecOrdenLaboratorio() > 0) {
				result = obtenerOrdenLab;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}
		return result;
	}

	private boolean esTodoEstadosEnviadoExamenes(int secOrdenLaboratorio) {
		boolean retorno = false;

		try {
			String sql = "select ol.examen " +
							" from OrdenLaboratorio ol " +
					 		" where ol.secOrdenLaboratorio = :secOrdenLaboratorio ";

			Query query = HIBERNATE_RESOURCE.getSession().createQuery(sql);
			query.setParameter("secOrdenLaboratorio", secOrdenLaboratorio);

			// Object[] verificarExamenes = (Object[]) query.uniqueResult();
			List<String> verificarExamenes = (List<String>) query.list();

			if (verificarExamenes != null) {
				for (String examen : verificarExamenes) {
					if (examen.toString().toUpperCase().compareTo("EGO") == 0) {

						sql = "select nullif(e.estado, '0') "
								+ " from OrdenLaboratorio ol, EgoResultados e "
								+ " where ol.secOrdenLaboratorio = :secOrdenLaboratorio "
								+ " and e.ordenLaboratorio.secOrdenLaboratorio = ol.secOrdenLaboratorio ";

						Query queryCambioEstadoEGO = HIBERNATE_RESOURCE
								.getSession().createQuery(sql);
						queryCambioEstadoEGO.setParameter(
								"secOrdenLaboratorio", secOrdenLaboratorio);

						Character cambioEstadoExamenEGO = (Character) queryCambioEstadoEGO
								.uniqueResult();
						if (cambioEstadoExamenEGO != null) {

							if (cambioEstadoExamenEGO.toString().compareTo("1") == 0) {
								retorno = true;
							} else {
								retorno = false;
							}
						}
					} else if (examen.toString().toUpperCase().compareTo("EGH") == 0) {
						sql = "select nullif(e.estado, '0') "
								+ " from OrdenLaboratorio ol, EghResultados e "
								+ " where ol.secOrdenLaboratorio = :secOrdenLaboratorio "
								+ " and e.secOrdenLaboratorio = ol.secOrdenLaboratorio ";

						Query queryCambioEstadoEGH = HIBERNATE_RESOURCE
								.getSession().createQuery(sql);
						queryCambioEstadoEGH.setParameter(
								"secOrdenLaboratorio", secOrdenLaboratorio);

						Character cambioEstadoExamenEGH = (Character) queryCambioEstadoEGH
								.uniqueResult();
						if (cambioEstadoExamenEGH != null) {

							if (cambioEstadoExamenEGH.toString().compareTo("1") == 0) {
								retorno = true;
							} else {
								retorno = false;
							}
						}
					} else if (examen.toString().toUpperCase().compareTo("CITOLOGIA FECAL") == 0) {
						sql = "select nullif(e.estado, '0') "
								+ " from OrdenLaboratorio ol, EghResultados e "
								+ " where ol.secOrdenLaboratorio = :secOrdenLaboratorio "
								+ " and e.secOrdenLaboratorio = ol.secOrdenLaboratorio ";

						Query queryCambioEstadoEGH = HIBERNATE_RESOURCE
								.getSession().createQuery(sql);
						queryCambioEstadoEGH.setParameter(
								"secOrdenLaboratorio", secOrdenLaboratorio);

						Character cambioEstadoExamenEGH = (Character) queryCambioEstadoEGH
								.uniqueResult();
						if (cambioEstadoExamenEGH != null) {

							if (cambioEstadoExamenEGH.toString().compareTo("1") == 0) {
								retorno = true;
							} else {
								retorno = false;
							}
						}
					} else if (examen.toString().toUpperCase()
							.compareTo("GOTA GRUESA") == 0) {
						sql = "select nullif(mr.estado, '0') "
								+ " from OrdenLaboratorio ol, MalariaResultados mr "
								+ " where ol.secOrdenLaboratorio = :secOrdenLaboratorio "
								+ " and mr.secOrdenLaboratorio = ol.secOrdenLaboratorio ";

						Query queryMalariaResultado = HIBERNATE_RESOURCE
								.getSession().createQuery(sql);
						queryMalariaResultado.setParameter(
								"secOrdenLaboratorio", secOrdenLaboratorio);

						Character cambioEstadoExamenMalaria = (Character) queryMalariaResultado
								.uniqueResult();

						if (cambioEstadoExamenMalaria != null) {

							if (cambioEstadoExamenMalaria.toString().compareTo(
									"1") == 0) {
								retorno = true;
							} else {
								retorno = false;
							}
						}
					} else if (examen.toString().toUpperCase()
							.compareTo("EXTENDIDO PERIFERICO") == 0) {
						sql = "select nullif(pr.estado, '0') "
								+ " from OrdenLaboratorio ol, PerifericoResultado pr "
								+ " where ol.secOrdenLaboratorio = :secOrdenLaboratorio "
								+ " and pr.secOrdenLaboratorio = ol.secOrdenLaboratorio ";

						Query queryPerifericoResultado = HIBERNATE_RESOURCE
								.getSession().createQuery(sql);
						queryPerifericoResultado.setParameter(
								"secOrdenLaboratorio", secOrdenLaboratorio);

						Character cambioEstadoPerifericoResultado = (Character) queryPerifericoResultado
								.uniqueResult();

						if (cambioEstadoPerifericoResultado != null) {

							if (cambioEstadoPerifericoResultado.toString()
									.compareTo("1") == 0) {
								retorno = true;
							} else {
								retorno = false;
							}
						}
					} else if (examen.toString().toUpperCase()
							.compareTo("INFLUENZA") == 0) {
						sql = "select nullif(im.estado, '0') "
								+ " from OrdenLaboratorio ol, InfluenzaMuestra im "
								+ " where ol.secOrdenLaboratorio = :secOrdenLaboratorio "
								+ " and im.ordenLaboratorio.secOrdenLaboratorio = ol.secOrdenLaboratorio ";

						Query queryInfluenzaMuestra = HIBERNATE_RESOURCE
								.getSession().createQuery(sql);
						queryInfluenzaMuestra.setParameter(
								"secOrdenLaboratorio", secOrdenLaboratorio);

						Character cambioEstadoInfluenzaMuestra = (Character) queryInfluenzaMuestra
								.uniqueResult();

						if (cambioEstadoInfluenzaMuestra != null) {

							if (cambioEstadoInfluenzaMuestra.toString()
									.compareTo("1") == 0) {
								retorno = true;
							} else {
								retorno = false;
							}
						}
					} else if (examen.toString().toUpperCase()
							.compareTo("SEROLOGIA DENGUE") == 0) {
						sql = "select nullif(sd.estado, '0') "
								+ " from OrdenLaboratorio ol, SerologiaDengueMuestra sd "
								+ " where ol.secOrdenLaboratorio = :secOrdenLaboratorio "
								+ " and sd.ordenLaboratorio.secOrdenLaboratorio = ol.secOrdenLaboratorio ";

						Query querySerologiaDengue = HIBERNATE_RESOURCE
								.getSession().createQuery(sql);
						querySerologiaDengue.setParameter(
								"secOrdenLaboratorio", secOrdenLaboratorio);

						Character cambioSerologiaDengue = (Character) querySerologiaDengue
								.uniqueResult();

						if (cambioSerologiaDengue != null) {

							if (cambioSerologiaDengue.toString().compareTo("1") == 0) {
								retorno = true;
							} else {
								retorno = false;
							}
						}
					} else if (examen.toString().toUpperCase()
							.compareTo("SEROLOGIA CHICK") == 0) {
						sql = "select nullif(sc.estado, '0') "
								+ " from OrdenLaboratorio ol, SerologiaChikMuestra sc "
								+ " where ol.secOrdenLaboratorio = :secOrdenLaboratorio "
								+ " and sc.ordenLaboratorio.secOrdenLaboratorio = ol.secOrdenLaboratorio ";

						Query querySerologiaChick = HIBERNATE_RESOURCE
								.getSession().createQuery(sql);
						querySerologiaChick.setParameter("secOrdenLaboratorio",
								secOrdenLaboratorio);

						Character cambioSerologiaChick = (Character) querySerologiaChick
								.uniqueResult();

						if (cambioSerologiaChick != null) {

							if (cambioSerologiaChick.toString().compareTo("1") == 0) {
								retorno = true;
							} else {
								retorno = false;
							}
						}
					}else {
						sql = "select nullif(sc.estado, '0') "
								+ " from OrdenLaboratorio ol, ResultadoExamenMuestra sc "
								+ " where ol.secOrdenLaboratorio = :secOrdenLaboratorio "
								+ " and sc.ordenLaboratorio.secOrdenLaboratorio = ol.secOrdenLaboratorio ";

						Query querySerologiaChick = HIBERNATE_RESOURCE
								.getSession().createQuery(sql);
						querySerologiaChick.setParameter("secOrdenLaboratorio",
								secOrdenLaboratorio);

						Character cambioSerologiaChick = (Character) querySerologiaChick
								.uniqueResult();

						if (cambioSerologiaChick != null) {

							if (cambioSerologiaChick.toString().compareTo("1") == 0) {
								retorno = true;
							} else {
								retorno = false;
							}
						}
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}

	public OrdenLaboratorio ordenLabCambioEstado(int secOrdenLaboratorio) {
		OrdenLaboratorio result = new OrdenLaboratorio();
		try {
			String sql = "select ol " +
							" from OrdenLaboratorio ol " +
					 		" where ol.secOrdenLaboratorio = :secOrdenLaboratorio";

			Query q = HIBERNATE_RESOURCE.getSession().createQuery(sql);
			q.setParameter("secOrdenLaboratorio", secOrdenLaboratorio);

			OrdenLaboratorio obtenerOrdenLab = (OrdenLaboratorio) q
					.uniqueResult();

			if (obtenerOrdenLab != null
					&& obtenerOrdenLab.getSecOrdenLaboratorio() > 0) {
				result = obtenerOrdenLab;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// ===================================================================================
	@Override
	public EgoResultados obtenerEgoResultado(int secOrdenLaboratorio) {
		EgoResultados result = new EgoResultados();
		try {
			String sql = "select er " +
							" from EgoResultados er " +
					 		" join er.ordenLaboratorio o " +
					 		" where o.secOrdenLaboratorio = :secOrdenLaboratorio ";

			Query q = HIBERNATE_RESOURCE.getSession().createQuery(sql);
			q.setParameter("secOrdenLaboratorio", secOrdenLaboratorio);

			EgoResultados obtenerSecEGO = (EgoResultados) q.uniqueResult();

			if (obtenerSecEGO != null && obtenerSecEGO.getSecEgoResultado() > 0) {
				result = obtenerSecEGO;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}
		return result;

	}

	@Override
	public EghResultados obtenerEghResultado(int secOrdenLaboratorio) {
		EghResultados result = new EghResultados();
		try {
			String sql = "select er " +
						 " from EghResultados er" +
						 " where er.secOrdenLaboratorio = :secOrdenLaboratorio";

			Query q = HIBERNATE_RESOURCE.getSession().createQuery(sql);
			q.setParameter("secOrdenLaboratorio", secOrdenLaboratorio);

			EghResultados obtenerSecEGH = (EghResultados) q.uniqueResult();

			if (obtenerSecEGH != null && obtenerSecEGH.getSecEghResultado() > 0) {
				result = obtenerSecEGH;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}
		return result;

	}

	@Override
	public PerifericoResultado obtenerExtendidoPeriferico(
			int secOrdenLaboratorio) {
		PerifericoResultado result = new PerifericoResultado();
		try {
			String sql = "select pr " +
							" from PerifericoResultado pr " +
					 		" where pr.secOrdenLaboratorio = :secOrdenLaboratorio ";

			Query q = HIBERNATE_RESOURCE.getSession().createQuery(sql);
			q.setParameter("secOrdenLaboratorio", secOrdenLaboratorio);

			PerifericoResultado obtenerPerifericoRes = (PerifericoResultado) q
					.uniqueResult();

			if (obtenerPerifericoRes != null
					&& obtenerPerifericoRes.getSecPerifericoResultado() > 0) {
				result = obtenerPerifericoRes;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}
		return result;

	}

	@Override
	public MalariaResultados obtenerMalariaResult(int secOrdenLaboratorio) {
		MalariaResultados result = new MalariaResultados();
		try {
			String sql = "select mr " +
							" from MalariaResultados mr " +
					 		" where mr.secOrdenLaboratorio = :secOrdenLaboratorio ";

			Query q = HIBERNATE_RESOURCE.getSession().createQuery(sql);
			q.setParameter("secOrdenLaboratorio", secOrdenLaboratorio);

			MalariaResultados obtenerMalariaRes = (MalariaResultados) q
					.uniqueResult();

			if (obtenerMalariaRes != null
					&& obtenerMalariaRes.getSecMalariaResultado() > 0) {
				result = obtenerMalariaRes;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}
		return result;

	}

	@Override
	public SerologiaDengueMuestra obtenerSerologiaDengue(int secOrdenLaboratorio) {
		SerologiaDengueMuestra result = new SerologiaDengueMuestra();
		try {
			String sql = "select sd " +
							" from SerologiaDengueMuestra sd " +
					 		" join sd.ordenLaboratorio o " +
					 		" where o.secOrdenLaboratorio = :secOrdenLaboratorio ";

			Query q = HIBERNATE_RESOURCE.getSession().createQuery(sql);
			q.setParameter("secOrdenLaboratorio", secOrdenLaboratorio);

			SerologiaDengueMuestra obtenerSeroloDengue = (SerologiaDengueMuestra) q
					.uniqueResult();

			if (obtenerSeroloDengue != null
					&& obtenerSeroloDengue.getSecSerologiaDengue() > 0) {
				result = obtenerSeroloDengue;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}
		return result;

	}

	@Override
	public SerologiaChikMuestra obtenerSerologiaChick(int secOrdenLaboratorio) {
		SerologiaChikMuestra result = new SerologiaChikMuestra();
		try {
			String sql = "select sc " +
						" from SerologiaChikMuestra sc " +
						" join sc.ordenLaboratorio o " +
						" where o.secOrdenLaboratorio = :secOrdenLaboratorio ";

			Query q = HIBERNATE_RESOURCE.getSession().createQuery(sql);
			q.setParameter("secOrdenLaboratorio", secOrdenLaboratorio);

			SerologiaChikMuestra obtenerSeroloChick = (SerologiaChikMuestra) q
					.uniqueResult();

			if (obtenerSeroloChick != null
					&& obtenerSeroloChick.getSecChikMuestra() > 0) {
				result = obtenerSeroloChick;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}
		return result;

	}

	@Override
	public InfluenzaMuestra obtenerInfluenzaMuestra(int secOrdenLaboratorio) {
		InfluenzaMuestra result = new InfluenzaMuestra();
		try {
			String sql = "select im " +
							" from InfluenzaMuestra im " +
					 		" join im.ordenLaboratorio o " +
					 		" where o.secOrdenLaboratorio = :secOrdenLaboratorio ";

			Query q = HIBERNATE_RESOURCE.getSession().createQuery(sql);
			q.setParameter("secOrdenLaboratorio", secOrdenLaboratorio);

			InfluenzaMuestra obtenerInfluMuestra = (InfluenzaMuestra) q
					.uniqueResult();

			if (obtenerInfluMuestra != null
					&& obtenerInfluMuestra.getSecInfluenzaMuestra() > 0) {
				result = obtenerInfluMuestra;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}
		return result;

	}
	
	@Override
	public InfoResultado actualizarOrdenLaboratorio(
			OrdenLaboratorio ordenLaboratorio) {
		InfoResultado resultado = new InfoResultado();

		try {

			HIBERNATE_RESOURCE.begin();
			HIBERNATE_RESOURCE.getSession().saveOrUpdate(ordenLaboratorio);
			HIBERNATE_RESOURCE.getSession().flush();
			HIBERNATE_RESOURCE.commit();

			resultado.setOk(true);
			resultado.setObjeto(ordenLaboratorio);
			resultado.setMensaje(Mensajes.REGISTRO_ACTUALIZADO);
			resultado.setGravedad(resultado.INFO);
		} catch (Exception e) {
			e.printStackTrace();
			resultado.setOk(false);
			resultado.setExcepcion(true);
			resultado.setGravedad(resultado.ERROR);
			resultado.setMensaje(Mensajes.ERROR_ACTUALIZAR_REGISTRO
					+ "\n" + e.getMessage());
			resultado.setFuenteError("TomaMx");
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}

		return resultado;
	}

/*  @Override
	public OrdenLaboratorio cambioEstadoCitologiaFecal(int secOrdenLaboratorio ) {
	OrdenLaboratorio result = new OrdenLaboratorio();
	try {
		String sql = "select ol.examen " +
						" from OrdenLaboratorio ol " +
				 		" where ol.secOrdenLaboratorio = :secOrdenLaboratorio " +
				 		" and ol.examen = Citologia Fecal ";

		Query q = HIBERNATE_RESOURCE.getSession().createQuery(sql);
		q.setParameter("secOrdenLaboratorio", string);

		OrdenLaboratorio obtenerOrdenLab = (OrdenLaboratorio) q
				.uniqueResult();

		if (obtenerOrdenLab != null
				&& obtenerOrdenLab.getSecOrdenLaboratorio() > 0) {
			result = obtenerOrdenLab;
		}

	} catch (Exception e) {
		e.printStackTrace();
	}
	return result;
}*/

	// ====================================================================================
	
	@Override
	public ResultadoExamenMuestra obtenerResultadoExamen(int secOrdenLaboratorio) {
		ResultadoExamenMuestra result = new ResultadoExamenMuestra();
		try {
			String sql = "select re " +
							" from ResultadoExamenMuestra re " +
					 		" join re.ordenLaboratorio o " +
					 		" where o.secOrdenLaboratorio = :secOrdenLaboratorio ";

			Query q = HIBERNATE_RESOURCE.getSession().createQuery(sql);
			q.setParameter("secOrdenLaboratorio", secOrdenLaboratorio);

			ResultadoExamenMuestra resultadoExamen = (ResultadoExamenMuestra) q
					.uniqueResult();

			if (resultadoExamen != null
					&& resultadoExamen.getSecResultadoExamen() > 0) {
				result = resultadoExamen;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}
		return result;

	}
	
	@Override
	public InfoResultado guardarResultadoExamen(
			ResultadoExamenMuestra resultadoExamen) {
		InfoResultado infoResultado = new InfoResultado();

		try {

			HIBERNATE_RESOURCE.begin();
			HIBERNATE_RESOURCE.getSession()
					.saveOrUpdate(resultadoExamen);
			if (esTodoEstadosEnviadoExamenes(resultadoExamen
					.getOrdenLaboratorio().getSecOrdenLaboratorio())) {
				resultadoExamen.getOrdenLaboratorio().setEstado('1');
				HIBERNATE_RESOURCE.getSession().saveOrUpdate(
						resultadoExamen.getOrdenLaboratorio());
			}
			HIBERNATE_RESOURCE.getSession().flush();
			HIBERNATE_RESOURCE.commit();

			infoResultado.setOk(true);
			infoResultado.setObjeto(resultadoExamen);
			infoResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
		} catch (Exception e) {
			e.printStackTrace();
			infoResultado.setOk(false);
			infoResultado.setExcepcion(true);
			infoResultado.setGravedad(infoResultado.ERROR);
			infoResultado.setMensaje(Mensajes.REGISTRO_NO_GUARDADO
					+ "\n" + e.getMessage());
			infoResultado.setFuenteError("ResultadoExamen-Resto");
		} finally {
			if (HIBERNATE_RESOURCE.getSession().isOpen()) {
				HIBERNATE_RESOURCE.close();
			}
		}

		return infoResultado;
	}

}
