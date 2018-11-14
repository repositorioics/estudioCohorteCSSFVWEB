package ni.com.sts.estudioCohorteCssfv.servicios;

import ni.com.sts.estudioCohorteCSSFV.modelo.HistEjecucionProcesoAutomatico;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;

public interface HistEjecucionProcesoService {

	public HistEjecucionProcesoAutomatico getEjecucionProcesoFechaHoy();
	
	public InfoResultado registrarEjecucionProceso();
}
