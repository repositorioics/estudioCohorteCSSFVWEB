package ni.com.sts.estudioCohorteCssfv.dto;

public class ExpedienteDTO {
	private int secHojaConsulta;
	private String numeroHoja;
    private String fechaCierre;
    private String horaCierre;
    private String medico;
    private String estado;
    
	public int getSecHojaConsulta() {
		return secHojaConsulta;
	}
	public void setSecHojaConsulta(int secHojaConsulta) {
		this.secHojaConsulta = secHojaConsulta;
	}
	public String getNumeroHoja() {
		return numeroHoja;
	}
	public void setNumeroHoja(String numeroHoja) {
		this.numeroHoja = numeroHoja;
	}
	public String getFechaCierre() {
		return fechaCierre;
	}
	public void setFechaCierre(String fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
	public String getHoraCierre() {
		return horaCierre;
	}
	public void setHoraCierre(String horaCierre) {
		this.horaCierre = horaCierre;
	}
	public String getMedico() {
		return medico;
	}
	public void setMedico(String medico) {
		this.medico = medico;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
}
