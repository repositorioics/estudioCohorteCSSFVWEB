package ni.com.sts.estudioCohorteCssfv.dto;

import java.util.Calendar;

import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;


public class IngresoResultados {

	private String examenClinico;
	private Calendar fecha;
	private Calendar horaOrden;
	private Calendar horaReporte;
	private String estado;
	
	private String getExamenClinico() {
		return examenClinico;
	}

	private void setExamenClinico(String examenClinico) {
		this.examenClinico = examenClinico;
	}

	private Calendar getFecha() {
		return fecha;
	}

	private void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	private Calendar getHoraOrden() {
		return horaOrden;
	}

	private void setHoraOrden(Calendar horaOrden) {
		this.horaOrden = horaOrden;
	}

	private Calendar getHoraReporte() {
		return horaReporte;
	}

	private void setHoraReporte(Calendar horaReporte) {
		this.horaReporte = horaReporte;
	}

	private String getEstado() {
		return estado;
	}

	private void setEstado(String estado) {
		this.estado = estado;
	}
	
	
}
