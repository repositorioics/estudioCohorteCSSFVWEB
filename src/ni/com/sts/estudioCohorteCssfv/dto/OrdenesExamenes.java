package ni.com.sts.estudioCohorteCssfv.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class OrdenesExamenes implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3805588767820229082L;
	
	private int secHojaConsulta;
	private int codigoExpediente;
	private int secOrdenLaboratorio;
	private String nombrePaciente;
	private String usuarioMedico;
	private String numOrdenLaboratorio;
	private Calendar fechaOrdenLaboratorio;
	private String estado;
	private String medicoCambioTurno;
	private Integer sexo; 
	private Integer edad;
	private String examen;
	private Calendar horaReporte;
	private String nombre;
	private String tomaMx;
	private String fechaHoraTomaMx;
	private String estudios;
	private String razonCancela;
	
	public int getSecHojaConsulta() {
		return secHojaConsulta;
	}
	public void setSecHojaConsulta(int secHojaConsulta) {
		this.secHojaConsulta = secHojaConsulta;
	}
	public int getCodigoExpediente() {
		return codigoExpediente;
	}
	public void setCodigoExpediente(int codigoExpediente) {
		this.codigoExpediente = codigoExpediente;
	}
	public String getNombrePaciente() {
		return nombrePaciente;
	}
	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}
	public String getUsuarioMedico() {
		return usuarioMedico;
	}
	public void setUsuarioMedico(String usuarioMedico) {
		this.usuarioMedico = usuarioMedico;
	}
	public String getNumOrdenLaboratorio() {
		return numOrdenLaboratorio;
	}
	public void setNumOrdenLaboratorio(String numOrdenLaboratorio) {
		this.numOrdenLaboratorio = numOrdenLaboratorio;
	}
	public Calendar getFechaOrdenLaboratorio() {
		return fechaOrdenLaboratorio;
	}
	public void setFechaOrdenLaboratorio(Calendar fechaOrdenLaboratorio) {
		this.fechaOrdenLaboratorio = fechaOrdenLaboratorio;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Integer getSexo() {
		return sexo;
	}
	public void setSexo(Integer sexo) {
		this.sexo = sexo;
	}
	public Integer getEdad() {
		return edad;
	}
	public void setEdad(Integer edad) {
		this.edad = edad;
	}
	public String getExamen() {
		return examen;
	}
	public void setExamen(String examen) {
		this.examen = examen;
	}
	public Calendar getHoraReporte() {
		return horaReporte;
	}
	public void setHoraReporte(Calendar horaReporte) {
		this.horaReporte = horaReporte;
	}
	public int getSecOrdenLaboratorio() {
		return secOrdenLaboratorio;
	}
	public void setSecOrdenLaboratorio(int secOrdenLaboratorio) {
		this.secOrdenLaboratorio = secOrdenLaboratorio;
	}
	/**
	 * @return the medicoCambioTurno
	 */
	public String getMedicoCambioTurno() {
		return medicoCambioTurno;
	}
	/**
	 * @param medicoCambioTurno the medicoCambioTurno to set
	 */
	public void setMedicoCambioTurno(String medicoCambioTurno) {
		this.medicoCambioTurno = medicoCambioTurno;
	}
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTomaMx() {
		return tomaMx;
	}
	public void setTomaMx(String tomaMx) {
		this.tomaMx = tomaMx;
	}
	public String getFechaHoraTomaMx() {
		return fechaHoraTomaMx;
	}
	public void setFechaHoraTomaMx(String fechaHoraTomaMx) {
		this.fechaHoraTomaMx = fechaHoraTomaMx;
	}
	public String getEstudios() {
		return estudios;
	}
	public void setEstudios(String estudios) {
		this.estudios = estudios;
	}
	public String getRazonCancela() {
		return razonCancela;
	}
	public void setRazonCancela(String razonCancela) {
		this.razonCancela = razonCancela;
	}
	
}
