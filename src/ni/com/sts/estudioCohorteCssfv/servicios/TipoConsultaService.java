package ni.com.sts.estudioCohorteCssfv.servicios;

import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.Admision;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;
import ni.com.sts.estudioCohorteCSSFV.modelo.TipoConsulta;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;


public interface TipoConsultaService {
	
	public TipoConsulta getTipoConsultaById(int secTipoCon) throws Exception;;
	
	public List<TipoConsulta> getTiposConsultas() throws Exception;
	
	public InfoResultado guardarTipoConsulta(TipoConsulta tipoConsulta);

	public InfoResultado actualizarTipoConsulta(TipoConsulta tipoConsulta);

	public TipoConsulta getTipoConsultaByCodigo(String codigo);

	TipoConsulta existeTipoConsulta(String codigo, int secTipoCon);

	List<TipoConsulta> getTipoConsultaByPatron(String patron);

	List<TipoConsulta> getTiposConsultasActivas();

}
