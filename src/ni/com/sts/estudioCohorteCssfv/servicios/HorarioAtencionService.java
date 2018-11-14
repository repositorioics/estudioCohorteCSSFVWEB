package ni.com.sts.estudioCohorteCssfv.servicios;

import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.HorarioAtencion;
import ni.com.sts.estudioCohorteCSSFV.modelo.ParametrosSistemas;
import ni.com.sts.estudioCohorteCssfv.util.Generico;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;

public interface HorarioAtencionService  {

	public HorarioAtencion getHorarioAtencionBySec(Integer secHorarioAtencion) throws Exception;
	
	public List<Generico> getListaHorariosAtencion();
	
	public InfoResultado guardarHorarioAtencion(HorarioAtencion dato);
	
	public InfoResultado eliminarHorarioAtencion(HorarioAtencion dato);
	
	public List<HorarioAtencion> getHorarioAtencionByDiaHora(Integer secuenciaActual, String dia, String horaInicio, String horaFin) throws Exception;
	
}
