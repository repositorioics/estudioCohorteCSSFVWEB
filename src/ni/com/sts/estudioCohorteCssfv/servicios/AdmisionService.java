package ni.com.sts.estudioCohorteCssfv.servicios;

import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.Admision;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;


public interface AdmisionService {
	
	public Admision getAdmisionById(int secuenciaAdmision) throws Exception;;
	
	public List<Admision> getAdmisiones() throws Exception;
	
	public InfoResultado guardarAdmision(Admision admision);

	public List<Admision> getAdmisionesPendienteEntregaByCodExp(int codExpediente);
	
	public Admision getAdmisionPendienteEntregaByCodExp(int codExpediente);

	public InfoResultado actualizarAdmision(Admision admision);

	public Admision getAdmisionByNumHojaCon(int numHoja);

	public List<Admision> getAdmisionesPendienteEntregaAndHojaActByCodExp(int codExpediente) throws Exception;

}
