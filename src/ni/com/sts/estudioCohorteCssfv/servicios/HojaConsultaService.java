package ni.com.sts.estudioCohorteCssfv.servicios;

import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.EstadosHoja;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaInfluenza;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaZika;
import ni.com.sts.estudioCohorteCSSFV.modelo.SeguimientoInfluenza;
import ni.com.sts.estudioCohorteCSSFV.modelo.SeguimientoZika;

public interface HojaConsultaService {

	public List<HojaConsulta> getHojasConsultaPendientesCarga() throws Exception;
	
	public void updateHojaConsulta(HojaConsulta hoja) throws Exception;
	
	public List<EstadosHoja> getEstadosHojaConsulta() throws Exception;

	public List<HojaConsulta> getHojaConsultaActivaHoyByCodExpediente(int codExpediente) throws Exception;

	public HojaConsulta getHojaConsultaActivaByCodExpediente(int codExpediente) throws Exception;

	public HojaConsulta getHojaConsultaByNumHoja(int numHoja) throws Exception;

	public List<HojaConsulta> getHojaConsultaActivaAndAdmiPenByCodExp(int codExpediente) throws Exception;
	
	public List<HojaConsulta> getHojaConsultaNoActivaAndAdmiPenByCodExp(int codExpediente) throws Exception;

	public EstadosHoja getEstadoHojaConsultaByNumHoja(int numHoja) throws Exception;

	public List<HojaConsulta> getHojasConsultaSinAdmision(int codExpediente) throws Exception;

	// hoja influenza
	public List<HojaInfluenza> getHojasInfluenzasPendientesCarga() throws Exception;
	
	public void updateHojaInfluenza(HojaInfluenza hoja) throws Exception;

	public List<SeguimientoInfluenza> getSeguimientoInfluenza(int secHojaInfluenza) throws Exception;

	// hoja zika
	public List<HojaZika> getHojasZikaPendientesCarga() throws Exception;

	public List<SeguimientoZika> getSeguimientoZika(int secHojaZika) throws Exception;
	
	public void updateHojaZika(HojaZika hoja) throws Exception;
}
