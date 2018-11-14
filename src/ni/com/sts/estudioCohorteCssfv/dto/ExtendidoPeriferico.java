package ni.com.sts.estudioCohorteCssfv.dto;

import java.util.Calendar;
import java.util.Date;

public class ExtendidoPeriferico {
	
	private int secPerifericoResultado;
	private int secOrdenLaboratorio;
	private String anisocitosis;
	private String anisocromia;
	private String poiquilocitosis;
	private String linfocitosAtipicos;
	private String observacionSblanca;
	private String observacionPlaqueta;
	private String codigoMuestra;
	private Short usuarioBioanalista;
	private Date horaReporte;
	private Character estado;
	
	public int getSecPerifericoResultado() {
		return secPerifericoResultado;
	}
	public void setSecPerifericoResultado(int secPerifericoResultado) {
		this.secPerifericoResultado = secPerifericoResultado;
	}
	public int getSecOrdenLaboratorio() {
		return secOrdenLaboratorio;
	}
	public void setSecOrdenLaboratorio(int secOrdenLaboratorio) {
		this.secOrdenLaboratorio = secOrdenLaboratorio;
	}
	public String getAnisocitosis() {
		return anisocitosis;
	}
	public void setAnisocitosis(String anisocitosis) {
		this.anisocitosis = anisocitosis;
	}
	public String getAnisocromia() {
		return anisocromia;
	}
	public void setAnisocromia(String anisocromia) {
		this.anisocromia = anisocromia;
	}
	public String getPoiquilocitosis() {
		return poiquilocitosis;
	}
	public void setPoiquilocitosis(String poiquilocitosis) {
		this.poiquilocitosis = poiquilocitosis;
	}
	public String getLinfocitosAtipicos() {
		return linfocitosAtipicos;
	}
	public void setLinfocitosAtipicos(String linfocitosAtipicos) {
		this.linfocitosAtipicos = linfocitosAtipicos;
	}
	public String getObservacionSblanca() {
		return observacionSblanca;
	}
	public void setObservacionSblanca(String observacionSblanca) {
		this.observacionSblanca = observacionSblanca;
	}
	/**
	 * @return the observacionPlaqueta
	 */
	public String getObservacionPlaqueta() {
		return observacionPlaqueta;
	}
	/**
	 * @param observacionPlaqueta the observacionPlaqueta to set
	 */
	public void setObservacionPlaqueta(String observacionPlaqueta) {
		this.observacionPlaqueta = observacionPlaqueta;
	}
	/**
	 * @return the codigoMuestra
	 */
	public String getCodigoMuestra() {
		return codigoMuestra;
	}
	/**
	 * @param codigoMuestra the codigoMuestra to set
	 */
	public void setCodigoMuestra(String codigoMuestra) {
		this.codigoMuestra = codigoMuestra;
	}
	/**
	 * @return the usuarioBioanalista
	 */
	public Short getUsuarioBioanalista() {
		return usuarioBioanalista;
	}
	/**
	 * @param usuarioBioanalista the usuarioBioanalista to set
	 */
	public void setUsuarioBioanalista(Short usuarioBioanalista) {
		this.usuarioBioanalista = usuarioBioanalista;
	}
	/**
	 * @return the horaReporte
	 */
	public Date getHoraReporte() {
		return horaReporte;
	}
	/**
	 * @param horaReporte the horaReporte to set
	 */
	public void setHoraReporte(Date horaReporte) {
		this.horaReporte = horaReporte;
	}
	/**
	 * @return the estado
	 */
	public Character getEstado() {
		return estado;
	}
	/**
	 * @param estado the estado to set
	 */
	public void setEstado(Character estado) {
		this.estado = estado;
	}

}
