package ni.com.sts.estudioCohorteCssfv.datos.horarioAtencion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import ni.com.sts.estudioCohorteCSSFV.modelo.HorarioAtencion;
import ni.com.sts.estudioCohorteCssfv.servicios.HorarioAtencionService;
import ni.com.sts.estudioCohorteCssfv.util.Generico;
import ni.com.sts.estudioCohorteCssfv.util.HibernateResource;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilDate;

public class HorarioAtencionDA implements HorarioAtencionService {

	private static final HibernateResource hibernateResource = new HibernateResource();

	@Override
	public HorarioAtencion getHorarioAtencionBySec(Integer secHorarioAtencion) throws Exception {
		String query = "select a from HorarioAtencion a " +
				"where a.secHorarioAtencion = :secHorarioAtencion";
		Query q = hibernateResource.getSession().createQuery(query);
		q.setParameter("secHorarioAtencion", secHorarioAtencion);
		return (HorarioAtencion)q.uniqueResult();
	}

	@Override
	public List<Generico> getListaHorariosAtencion() {
		String query = "select a.secHorarioAtencion, a.turno, a.dia, a.horaInicio, a.horaFin " +
				"from HorarioAtencion a order by a.dia asc, a.horaInicio asc";
		Query q = hibernateResource.getSession().createQuery(query);
		List<Object[]> lista = (List<Object[]>) q.list();

		List<Generico> resultado = new ArrayList<Generico>();
		if (lista != null && lista.size() > 0) {
			for (Object[] object : lista) {
				Generico data = new Generico();
				data.setNumero1((Integer)object[0]);
				data.setTexto10(object[1].toString());
				data.setTexto11(object[2].toString());
				data.setTexto1(obtenerTurno(object[1].toString().charAt(0)));
				data.setTexto2(obtenerDia(object[2].toString().charAt(0)));
				data.setTexto3(UtilDate.DateToString((Date)object[3], "hh:mm a"));
				data.setTexto4(UtilDate.DateToString((Date)object[4], "hh:mm a"));
				data.setFecha1((Date)object[3]);
				data.setFecha2((Date)object[4]);
				resultado.add(data);
			}
		}
		return resultado;
	}

	private String obtenerDia(char dia){
		switch (dia) {
		case '2':
			return "Lunes";
		case '3':
			return "Martes";
		case '4':
			return "Miércoles";
		case '5':
			return "Jueves";
		case '6':
			return "Viernes";
		case '7':
			return "Sábado";
		case '1':
			return "Domingo";
		default:
			return "";
		}
	}

	private String obtenerTurno(char turno){
		switch (turno) {
		case '1':
			return "Regular";
		case '2':
			return "Noche";
		case '3':
			return "Fin Semana";
		default:
			return "";
		}
	}

	@Override
	public InfoResultado guardarHorarioAtencion(HorarioAtencion dato) {
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
            infoResultado.setFuenteError("Horario Atención");
        } finally {
            if (hibernateResource.getSession().isOpen()) {
                hibernateResource.close();
            }
        }
        return infoResultado;
	}

	@Override
	public InfoResultado eliminarHorarioAtencion(HorarioAtencion dato) {
		InfoResultado infoResultado = new InfoResultado();

        try {

            hibernateResource.begin();
            hibernateResource.getSession().delete(dato);
            hibernateResource.getSession().flush();
            hibernateResource.commit();

            infoResultado.setOk(true);
            infoResultado.setObjeto(dato);
            infoResultado.setMensaje(Mensajes.REGISTRO_ELIMINADO);
        } catch (Exception e) {
            e.printStackTrace();
            hibernateResource.rollback();
            infoResultado.setOk(false);
            infoResultado.setExcepcion(true);
            infoResultado.setGravedad(infoResultado.ERROR);
            infoResultado.setMensaje(Mensajes.REGISTRO_NO_ELIMINADO + "\n" + e.getMessage());
            infoResultado.setFuenteError("Horario Atención");
        } finally {
            if (hibernateResource.getSession().isOpen()) {
                hibernateResource.close();
            }
        }
        return infoResultado;
	}

	@Override
	public List<HorarioAtencion> getHorarioAtencionByDiaHora(Integer secuenciaActual, String dia, String horaInicio, String horaFin)
			throws Exception {
		String query = "select a from HorarioAtencion a " +
				"where a.secHorarioAtencion != :secuencia and a.dia = :dia " +
				"and ( " +
				"	:horaInicio BETWEEN to_char(horaInicio,'HH24:mi:ss') and to_char(horaFin,'HH24:mi:ss') " +
				"   or :horaFin BETWEEN to_char(horaInicio,'HH24:mi:ss') and to_char(horaFin,'HH24:mi:ss') " +
				"	or to_char(horaInicio,'HH24:mi:ss') BETWEEN :horaInicio and :horaFin" +
				"	or to_char(horaFin,'HH24:mi:ss') BETWEEN :horaInicio and :horaFin" +
				")";
		Query q = hibernateResource.getSession().createQuery(query);
		q.setParameter("dia", dia.charAt(0));
		q.setParameter("secuencia", secuenciaActual);
		q.setParameter("horaInicio", horaInicio);
		q.setParameter("horaFin", horaFin);
		return q.list();
	}

}
