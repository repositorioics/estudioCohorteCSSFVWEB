package ni.com.sts.estudioCohorteCssfv.servicios;

import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.Paciente;

public interface PacienteService  {

	public Paciente getPacienteById(Integer codExpediente);

	public List<Paciente> getPacientes();

	String[] getCodigosExpediente();

	String getEstudiosPaciente(Integer codExpediente);
	
}
