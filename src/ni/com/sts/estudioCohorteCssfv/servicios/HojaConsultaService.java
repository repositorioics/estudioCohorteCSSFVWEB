package ni.com.sts.estudioCohorteCssfv.servicios;

import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.Diagnostico;
import ni.com.sts.estudioCohorteCSSFV.modelo.EscuelaCatalogo;
import ni.com.sts.estudioCohorteCSSFV.modelo.EstadosHoja;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaInfluenza;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaZika;
import ni.com.sts.estudioCohorteCSSFV.modelo.SeguimientoInfluenza;
import ni.com.sts.estudioCohorteCSSFV.modelo.SeguimientoZika;
import ni.com.sts.estudioCohorteCssfv.dto.ExpedienteDTO;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;

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
	
	public void updateHojaConsultaRepeatKey(HojaConsulta hoja) throws Exception;
	
	//Hoja consulta web
	public List<Diagnostico> getDiagnosticos() throws Exception;
	
	public List<EscuelaCatalogo> getColegios() throws Exception;

	public InfoResultado guardarHojaConsulta(HojaConsulta hojaConsulta);
	
	public List<ExpedienteDTO> listaExpedienteHojaConsulta(int codigoExpediente);
}
