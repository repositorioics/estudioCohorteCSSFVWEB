package ni.com.sts.estudioCohorteCssfv.servicios;

import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.ParametrosSistemas;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;

public interface ParametroService  {

	public ParametrosSistemas getParametroByName(String codigo) throws Exception;
	
	public List<ParametrosSistemas> getListaParametros();
	
	public InfoResultado actualizarParametro(ParametrosSistemas dato);
	
}
