package ni.com.sts.estudioCohorteCssfv.servicios;

import java.sql.Connection;

import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;

public interface ActualizarDatosService {
	InfoResultado updateHojaConsulta(String nombreTabla, int numHoja, String nombreCampo, String valor,
			Boolean valorNull, String usuario);

	InfoResultado updateSeguimientos(String nombreTabla, String secHoja, int numHoja, String nombreCampo, String valor,
			Boolean valorNull, String usuario, int dia);

	String getValorAnterior(String nombreTabla, int numHoja, String nombreCampo, int controlDia);
}
