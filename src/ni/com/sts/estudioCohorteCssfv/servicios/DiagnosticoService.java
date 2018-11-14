package ni.com.sts.estudioCohorteCssfv.servicios;

import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.Diagnostico;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;

public interface DiagnosticoService {
	
	public Diagnostico getDiagnosticoById(int secDiagnostico);
	public List<Diagnostico> getDiagnosticos();
	public InfoResultado guardarDiagnostico(Diagnostico dato);
	Diagnostico getDiagnosticoByCodigo(int codigoDignostico, int secDiagnostico);
	List<Diagnostico> getDiagnosticoByPatron(String patron);
}
