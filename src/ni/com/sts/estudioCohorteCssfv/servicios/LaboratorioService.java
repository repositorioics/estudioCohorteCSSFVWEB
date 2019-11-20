package ni.com.sts.estudioCohorteCssfv.servicios;

import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.EghResultados;
import ni.com.sts.estudioCohorteCSSFV.modelo.EghResultadosId;
import ni.com.sts.estudioCohorteCSSFV.modelo.EgoResultados;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;
import ni.com.sts.estudioCohorteCSSFV.modelo.InfluenzaMuestra;
import ni.com.sts.estudioCohorteCSSFV.modelo.MalariaResultados;
import ni.com.sts.estudioCohorteCSSFV.modelo.OrdenLaboratorio;
import ni.com.sts.estudioCohorteCSSFV.modelo.Paciente;
import ni.com.sts.estudioCohorteCSSFV.modelo.PerifericoResultado;
import ni.com.sts.estudioCohorteCSSFV.modelo.ResultadoExamenMuestra;
import ni.com.sts.estudioCohorteCSSFV.modelo.SeguimientoInfluenza;
import ni.com.sts.estudioCohorteCSSFV.modelo.SerologiaChikMuestra;
import ni.com.sts.estudioCohorteCSSFV.modelo.SerologiaDengueMuestra;
import ni.com.sts.estudioCohorteCssfv.dto.ExamenGeneralOrinaEGO;
import ni.com.sts.estudioCohorteCssfv.dto.OrdenesExamenes;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;

public interface LaboratorioService {

	public List<OrdenesExamenes> listaPacienteLab(String codigoExpediente, String estado);

	public List<OrdenesExamenes> listaIngresoResultados(int codExpediente, int secHojaConsulta);

	public Paciente obtenerInfoPaciente(int codExpediente);

	public InfoResultado guardarExamenEgo(EgoResultados egoResultado);

	public OrdenLaboratorio obtenerOrdenLab(int numOrdenLaboratorio);

	public InfoResultado guardarExamenEgh(EghResultados eghResultadosId);

	public InfoResultado guardarExamenMSPGotaGruesa(MalariaResultados malariaResultadoId);

	public InfoResultado guardarExamenExtendidoPeriferico(PerifericoResultado perifericoResultadoId);

	public InfoResultado guardarMuestraDengue(SerologiaDengueMuestra serologiaDengueMuestra);

	public InfoResultado guardarMuestraChick(SerologiaChikMuestra serologiaChikMuestra);

	public InfoResultado guardarMuestraInfluenza(InfluenzaMuestra influenzaMuestra);

	public EgoResultados obtenerEgoResultado(int secOrdenLaboratorio);

	public EghResultados obtenerEghResultado(int secOrdenLaboratorio);

	public PerifericoResultado obtenerExtendidoPeriferico(int secOrdenLaboratorio);

	public MalariaResultados obtenerMalariaResult(int secOrdenLaboratorio);

	public SerologiaDengueMuestra obtenerSerologiaDengue(int secOrdenLaboratorio);

	public SerologiaChikMuestra obtenerSerologiaChick(int secOrdenLaboratorio);

	public InfluenzaMuestra obtenerInfluenzaMuestra(int secOrdenLaboratorio);

	public InfoResultado actualizarOrdenLaboratorio(OrdenLaboratorio ordenLaboratorio);

	public ResultadoExamenMuestra obtenerResultadoExamen(int secOrdenLaboratorio);

	public InfoResultado guardarResultadoExamen(ResultadoExamenMuestra resultadoExamen);

	String obtenerEdadCalculadaPaciente(int codExpediente);

	public HojaConsulta obtenerHCBySec(int secHojaConsulta);

}
