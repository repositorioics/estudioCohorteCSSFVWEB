package ni.com.sts.estudioCohorteCssfv.servicios;

import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.EstadosHoja;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;

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
}
