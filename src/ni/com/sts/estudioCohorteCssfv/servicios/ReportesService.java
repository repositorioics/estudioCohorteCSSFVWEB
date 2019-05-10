package ni.com.sts.estudioCohorteCssfv.servicios;

import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.HojaInfluenza;
import ni.com.sts.estudioCohorteCssfv.util.FiltroReporte;
import ni.com.sts.estudioCohorteCssfv.util.Generico;

public interface ReportesService {

	public List<Generico> getReporteGeneral(FiltroReporte filtro) throws Exception;
	public List<Generico> getReporteAuditoria(FiltroReporte filtro) throws Exception;
	public List<Generico> getReporteHojaInfluenza(String estado) throws Exception;
	List<Generico> getReporteHojaZika(String estado) throws Exception;
}
