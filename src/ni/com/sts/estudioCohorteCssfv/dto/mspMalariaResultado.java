package ni.com.sts.estudioCohorteCssfv.dto;

import java.util.Date;

public class mspMalariaResultado {
	
	private int secMalariaResultado;
	private int secOrdenLaboratorio;
	private String PFalciparum;
	private String PVivax;
	private String negativo;
	private String codigoMuestra;
	private Short usuarioBioanalista;
	private Date horaReporte;
	private Character estado;
	
	public int getSecMalariaResultado() {
		return secMalariaResultado;
	}
	public void setSecMalariaResultado(int secMalariaResultado) {
		this.secMalariaResultado = secMalariaResultado;
	}
	public int getSecOrdenLaboratorio() {
		return secOrdenLaboratorio;
	}
	public void setSecOrdenLaboratorio(int secOrdenLaboratorio) {
		this.secOrdenLaboratorio = secOrdenLaboratorio;
	}
	public String getPFalciparum() {
		return PFalciparum;
	}
	public void setPFalciparum(String pFalciparum) {
		PFalciparum = pFalciparum;
	}
	public String getPVivax() {
		return PVivax;
	}
	public void setPVivax(String pVivax) {
		PVivax = pVivax;
	}
	public String getNegativo() {
		return negativo;
	}
	public void setNegativo(String negativo) {
		this.negativo = negativo;
	}
	public String getCodigoMuestra() {
		return codigoMuestra;
	}
	public void setCodigoMuestra(String codigoMuestra) {
		this.codigoMuestra = codigoMuestra;
	}
	public Short getUsuarioBioanalista() {
		return usuarioBioanalista;
	}
	public void setUsuarioBioanalista(Short usuarioBioanalista) {
		this.usuarioBioanalista = usuarioBioanalista;
	}
	public Date getHoraReporte() {
		return horaReporte;
	}
	public void setHoraReporte(Date horaReporte) {
		this.horaReporte = horaReporte;
	}
	public Character getEstado() {
		return estado;
	}
	public void setEstado(Character estado) {
		this.estado = estado;
	}

}
