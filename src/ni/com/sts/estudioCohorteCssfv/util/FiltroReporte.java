package ni.com.sts.estudioCohorteCssfv.util;

import java.util.Date;

public class FiltroReporte {

	Integer codigoExpediente;
	String medico;
	String estadoHojaConsulta;
	Date fechaInicioConsulta;
	Date fechaFinConsulta;
	String estadoAdmision;	
	Boolean mostrarTodo;
	String tipoControl;
	
	//por defecto mostrar todo
	public FiltroReporte(){
		this.mostrarTodo = true;
	}
	
	public Integer getCodigoExpediente() {
		return codigoExpediente;
	}
	public void setCodigoExpediente(Integer codigoExpediente) {
		this.codigoExpediente = codigoExpediente;
	}
	public String getMedico() {
		return medico;
	}
	public void setMedico(String medico) {
		this.medico = medico;
	}
	public String getEstadoHojaConsulta() {
		return estadoHojaConsulta;
	}
	public void setEstadoHojaConsulta(String estadoHojaConsulta) {
		this.estadoHojaConsulta = estadoHojaConsulta;
	}
	public Date getFechaInicioConsulta() {
		return fechaInicioConsulta;
	}
	public void setFechaInicioConsulta(Date fechaInicioConsulta) {
		this.fechaInicioConsulta = fechaInicioConsulta;
	}
	public Date getFechaFinConsulta() {
		return fechaFinConsulta;
	}
	public void setFechaFinConsulta(Date fechaFinConsulta) {
		this.fechaFinConsulta = fechaFinConsulta;
	}
	public Boolean getMostrarTodo() {
		return mostrarTodo;
	}
	public void setMostrarTodo(Boolean mostrarTodo) {
		this.mostrarTodo = mostrarTodo;
	}

	public String getEstadoAdmision() {
		return estadoAdmision;
	}

	public void setEstadoAdmision(String estadoAdmision) {
		this.estadoAdmision = estadoAdmision;
	}

	public String getTipoControl() {
		return tipoControl;
	}

	public void setTipoControl(String tipoControl) {
		this.tipoControl = tipoControl;
	}	
	
	
}
