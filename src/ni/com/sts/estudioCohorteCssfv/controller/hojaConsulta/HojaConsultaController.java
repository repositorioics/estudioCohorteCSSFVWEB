package ni.com.sts.estudioCohorteCssfv.controller.hojaConsulta;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ni.com.sts.estudioCohorteCSSFV.modelo.Diagnostico;
import ni.com.sts.estudioCohorteCSSFV.modelo.EscuelaCatalogo;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;
import ni.com.sts.estudioCohorteCSSFV.modelo.Paciente;
import ni.com.sts.estudioCohorteCSSFV.modelo.UsuariosView;
import ni.com.sts.estudioCohorteCssfv.controller.visorReporte.VisorController.TipoReporte;
import ni.com.sts.estudioCohorteCssfv.datos.hojaConsulta.HojaConsultaDA;
import ni.com.sts.estudioCohorteCssfv.datos.paciente.PacienteDA;
import ni.com.sts.estudioCohorteCssfv.datos.usuario.UsuariosDA;
import ni.com.sts.estudioCohorteCssfv.servicios.HojaConsultaService;
import ni.com.sts.estudioCohorteCssfv.servicios.PacienteService;
import ni.com.sts.estudioCohorteCssfv.servicios.UsuariosService;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilDate;
import ni.com.sts.estudioCohorteCssfv.util.UtilProperty;
import ni.com.sts.estudioCohorteCssfv.util.Utilidades;

public class HojaConsultaController extends SelectorComposer<Component> {
	
	private static PacienteService pacienteService = new PacienteDA();
	
	private static HojaConsultaService hojaConsultaService = new HojaConsultaDA();
	
	private static UsuariosService usuariosService = new UsuariosDA();
		
	private static CompositeConfiguration config;
	
	
	/** Componentes */

    @Wire("[id$=txtCodExpediente]")
    private Intbox txtCodExpediente;
    
    @Wire("[id$=txtNombrePaciente]")
    private Textbox txtNombrePaciente;
    
    @Wire("[id$=txtEstudiosPaciente]")
    private Textbox txtEstudiosPaciente;
    
    @Wire("[id$=txtExpediente]")
    private Textbox txtExpediente;
    
    @Wire("[id$=txtEdad]")
    private Textbox txtEdad;
    
    @Wire("[id$=txtSexo]")
    private Textbox txtSexo;
    
    @Wire("[id$=txtPeso]")
    private Textbox txtPeso;
    
    @Wire("[id$=txtTalla]")
    private Textbox txtTalla;
    
    @Wire("[id$=txtTemp]")
    private Textbox txtTemp;
    
    @Wire("[id$=txtFechaConsulta]")
    private Datebox txtFechaConsulta;
    
    @Wire("[id$=txtHoraConsulta]")
    private Textbox txtHoraConsulta;
    
    @Wire("[id$=txtPas]")
    private Textbox txtPas;
    
    @Wire("[id$=txtPad]")
    private Textbox txtPad;
    
    @Wire("[id$=txtFciaResp]")
    private Textbox txtFciaResp;
    
    @Wire("[id$=txtFciaCard]")
    private Textbox txtFciaCard;
    
    @Wire("[id$=txtHora]")
    private Textbox txtHora;
    
    @Wire("[id$=txtHoraMedico]")
    private Textbox txtHoraMedico;
    
    @Wire("[id$=cmbHora]")
    private Combobox cmbHora;
    
    @Wire("[id$=cmbHoraMedico]")
    private Combobox cmbHoraMedico;
    
    @Wire("[id$=chkCssfv]")
    private Checkbox chkCssfv;
    
    @Wire("[id$=chkTerreno]")
    private Checkbox chkTerreno;
    
    @Wire("[id$=chkCInicial]")
    private Checkbox chkCInicial;
    
    @Wire("[id$=chkCSeguimiento]")
    private Checkbox chkCSeguimiento;
    
    @Wire("[id$=chkCConvaleciente]")
    private Checkbox chkCConvaleciente;
    
    @Wire("[id$=chkSChik1]")
    private Checkbox chkSChik1;
    
    @Wire("[id$=chkSChik2]")
    private Checkbox chkSChik2;
    
    @Wire("[id$=chkSChik3]")
    private Checkbox chkSChik3;
    
    @Wire("[id$=chkSChik4]")
    private Checkbox chkSChik4;
    
    @Wire("[id$=chkSChik5]")
    private Checkbox chkSChik5;
    
    @Wire("[id$=chkRegular]")
    private Checkbox chkRegular;
    
    @Wire("[id$=chkNoche]")
    private Checkbox chkNoche;
    
    @Wire("[id$=chkFinSemana]")
    private Checkbox chkFinSemana;
    
    @Wire("[id$=txtFis]")
    private Datebox txtFis;
    
    @Wire("[id$=txtFif]")
    private Datebox txtFif;
    
    @Wire("[id$=txtUltDiaFiebre]")
    private Datebox txtUltDiaFiebre;
    
    @Wire("[id$=cmbUltDiaFiebre]")
    private Combobox cmbUltDiaFiebre;
    
    @Wire("[id$=txtUltDosisAntipiretico]")
    private Datebox txtUltDosisAntipiretico;
    
    @Wire("[id$=txtHoraUltDosisAntipiretico]")
    private Textbox txtHoraUltDosisAntipiretico;
    
    @Wire("[id$=cmbUltDosisAntipiretico]")
    private Combobox cmbUltDosisAntipiretico;
    
    /*Estado General*/
    @Wire("[id$=chkFiebreS]")
    private Checkbox chkFiebreS;
    @Wire("[id$=chkFiebreN]")
    private Checkbox chkFiebreN;
    @Wire("[id$=chkFiebreD]")
    private Checkbox chkFiebreD;
    
    @Wire("[id$=chkAsteniaS]")
    private Checkbox chkAsteniaS;
    @Wire("[id$=chkAsteniaN]")
    private Checkbox chkAsteniaN;
    @Wire("[id$=chkAsteniaD]")
    private Checkbox chkAsteniaD;
    
    @Wire("[id$=chkAnormalMenteSomnolientoS]")
    private Checkbox chkAnormalMenteSomnolientoS;
    @Wire("[id$=chkAnormalMenteSomnolientoN]")
    private Checkbox chkAnormalMenteSomnolientoN;
    
    @Wire("[id$=chkMalEstadoGeneralS]")
    private Checkbox chkMalEstadoGeneralS;
    @Wire("[id$=chkMalEstadoGeneralN]")
    private Checkbox chkMalEstadoGeneralN;
    
    @Wire("[id$=chkPerdidaConcienciaS]")
    private Checkbox chkPerdidaConcienciaS;
    @Wire("[id$=chkPerdidaConcienciaN]")
    private Checkbox chkPerdidaConcienciaN;
    
    @Wire("[id$=chkInquietoIrritableS]")
    private Checkbox chkInquietoIrritableS;
    @Wire("[id$=chkInquietoIrritableN]")
    private Checkbox chkInquietoIrritableN;
    
    @Wire("[id$=chkConvulsionesS]")
    private Checkbox chkConvulsionesS;
    @Wire("[id$=chkConvulsionesN]")
    private Checkbox chkConvulsionesN;
    
    @Wire("[id$=chkHipotermiaS]")
    private Checkbox chkHipotermiaS;
    @Wire("[id$=chkHipotermiaN]")
    private Checkbox chkHipotermiaN;
    
    @Wire("[id$=chkLetargiaS]")
    private Checkbox chkLetargiaS;
    @Wire("[id$=chkLetargiaN]")
    private Checkbox chkLetargiaN;
    
    /*Gastrointestinal*/
    @Wire("[id$=chkPocoApetitoS]")
    private Checkbox chkPocoApetitoS;
    @Wire("[id$=chkPocoApetitoN]")
    private Checkbox chkPocoApetitoN;
    @Wire("[id$=chkPocoApetitoD]")
    private Checkbox chkPocoApetitoD;
    
    @Wire("[id$=chkNauseaS]")
    private Checkbox chkNauseaS;
    @Wire("[id$=chkNauseaN]")
    private Checkbox chkNauseaN;
    @Wire("[id$=chkNauseaD]")
    private Checkbox chkNauseaD;
    
    @Wire("[id$=chkDificultadAlimentaseS]")
    private Checkbox chkDificultadAlimentaseS;
    @Wire("[id$=chkDificultadAlimentaseN]")
    private Checkbox chkDificultadAlimentaseN;
    @Wire("[id$=chkDificultadAlimentaseD]")
    private Checkbox chkDificultadAlimentaseD;
    
    @Wire("[id$=chkVomito12HorasS]")
    private Checkbox chkVomito12HorasS;
    @Wire("[id$=chkVomito12HorasN]")
    private Checkbox chkVomito12HorasN;
    @Wire("[id$=chkVomito12HorasD]")
    private Checkbox chkVomito12HorasD;
    @Wire("[id$=txtVomito12Horas]")
    private Textbox txtVomito12Horas;
    
    @Wire("[id$=chkDiarreaS]")
    private Checkbox chkDiarreaS;
    @Wire("[id$=chkDiarreaN]")
    private Checkbox chkDiarreaN;
    @Wire("[id$=chkDiarreaD]")
    private Checkbox chkDiarreaD;
    
    @Wire("[id$=chkDiarreaSangreS]")
    private Checkbox chkDiarreaSangreS;
    @Wire("[id$=chkDiarreaSangreN]")
    private Checkbox chkDiarreaSangreN;
    @Wire("[id$=chkDiarreaSangreD]")
    private Checkbox chkDiarreaSangreD;
    
    @Wire("[id$=chkEstrenimientoS]")
    private Checkbox chkEstrenimientoS;
    @Wire("[id$=chkEstrenimientoN]")
    private Checkbox chkEstrenimientoN;
    @Wire("[id$=chkEstrenimientoD]")
    private Checkbox chkEstrenimientoD;
    
    @Wire("[id$=chkDolorAbInterS]")
    private Checkbox chkDolorAbInterS;
    @Wire("[id$=chkDolorAbInterN]")
    private Checkbox chkDolorAbInterN;
    @Wire("[id$=chkDolorAbInterD]")
    private Checkbox chkDolorAbInterD;
    
    @Wire("[id$=chkDolorAbContinuoS]")
    private Checkbox chkDolorAbContinuoS;
    @Wire("[id$=chkDolorAbContinuoN]")
    private Checkbox chkDolorAbContinuoN;
    @Wire("[id$=chkDolorAbContinuoD]")
    private Checkbox chkDolorAbContinuoD;
    
    @Wire("[id$=chkEpigastralgiaS]")
    private Checkbox chkEpigastralgiaS;
    @Wire("[id$=chkEpigastralgiaN]")
    private Checkbox chkEpigastralgiaN;
    @Wire("[id$=chkEpigastralgiaD]")
    private Checkbox chkEpigastralgiaD;
    
    @Wire("[id$=chkIntoleranciaViaOralS]")
    private Checkbox chkIntoleranciaViaOralS;
    @Wire("[id$=chkIntoleranciaViaOralN]")
    private Checkbox chkIntoleranciaViaOralN;
    @Wire("[id$=chkIntoleranciaViaOralD]")
    private Checkbox chkIntoleranciaViaOralD;
    
    @Wire("[id$=chkDistensionAbdominalS]")
    private Checkbox chkDistensionAbdominalS;
    @Wire("[id$=chkDistensionAbdominalN]")
    private Checkbox chkDistensionAbdominalN;
    @Wire("[id$=chkDistensionAbdominalD]")
    private Checkbox chkDistensionAbdominalD;
    
    @Wire("[id$=chkHepatomegaliaS]")
    private Checkbox chkHepatomegaliaS;
    @Wire("[id$=chkHepatomegaliaN]")
    private Checkbox chkHepatomegaliaN;
    @Wire("[id$=txtHepatomegalia]")
    private Textbox txtHepatomegalia;
    
    /*Osteomuscular*/
    @Wire("[id$=chkArtralgiaS]")
    private Checkbox chkArtralgiaS;
    @Wire("[id$=chkArtralgiaN]")
    private Checkbox chkArtralgiaN;
    @Wire("[id$=chkArtralgiaD]")
    private Checkbox chkArtralgiaD;
    
    @Wire("[id$=chkMialgiaS]")
    private Checkbox chkMialgiaS;
    @Wire("[id$=chkMialgiaN]")
    private Checkbox chkMialgiaN;
    @Wire("[id$=chkMialgiaD]")
    private Checkbox chkMialgiaD;
    
    @Wire("[id$=chkLumbalgiaS]")
    private Checkbox chkLumbalgiaS;
    @Wire("[id$=chkLumbalgiaN]")
    private Checkbox chkLumbalgiaN;
    @Wire("[id$=chkLumbalgiaD]")
    private Checkbox chkLumbalgiaD;
    
    @Wire("[id$=chkDolorCuelloS]")
    private Checkbox chkDolorCuelloS;
    @Wire("[id$=chkDolorCuelloN]")
    private Checkbox chkDolorCuelloN;
    @Wire("[id$=chkDolorCuelloD]")
    private Checkbox chkDolorCuelloD;
    
    @Wire("[id$=chkTenosinovitisS]")
    private Checkbox chkTenosinovitisS;
    @Wire("[id$=chkTenosinovitisN]")
    private Checkbox chkTenosinovitisN;
    @Wire("[id$=chkTenosinovitisD]")
    private Checkbox chkTenosinovitisD;
    
    @Wire("[id$=chkArtralgiaProximalS]")
    private Checkbox chkArtralgiaProximalS;
    @Wire("[id$=chkArtralgiaProximalN]")
    private Checkbox chkArtralgiaProximalN;
    @Wire("[id$=chkArtralgiaProximalD]")
    private Checkbox chkArtralgiaProximalD;
    
    @Wire("[id$=chkArtralgiaDistalS]")
    private Checkbox chkArtralgiaDistalS;
    @Wire("[id$=chkArtralgiaDistalN]")
    private Checkbox chkArtralgiaDistalN;
    @Wire("[id$=chkArtralgiaDistalD]")
    private Checkbox chkArtralgiaDistalD;
    
    @Wire("[id$=chkConjuntivitisS]")
    private Checkbox chkConjuntivitisS;
    @Wire("[id$=chkConjuntivitisN]")
    private Checkbox chkConjuntivitisN;
    @Wire("[id$=chkConjuntivitisD]")
    private Checkbox chkConjuntivitisD;
    
    @Wire("[id$=chkEdemaMunecasS]")
    private Checkbox chkEdemaMunecasS;
    @Wire("[id$=chkEdemaMunecasN]")
    private Checkbox chkEdemaMunecasN;
    @Wire("[id$=chkEdemaMunecasD]")
    private Checkbox chkEdemaMunecasD;
    
    @Wire("[id$=chkEdemaCodosS]")
    private Checkbox chkEdemaCodosS;
    @Wire("[id$=chkEdemaCodosN]")
    private Checkbox chkEdemaCodosN;
    @Wire("[id$=chkEdemaCodosD]")
    private Checkbox chkEdemaCodosD;
    
    @Wire("[id$=chkEdemaHombrosS]")
    private Checkbox chkEdemaHombrosS;
    @Wire("[id$=chkEdemaHombrosN]")
    private Checkbox chkEdemaHombrosN;
    @Wire("[id$=chkEdemaHombrosD]")
    private Checkbox chkEdemaHombrosD;
    
    @Wire("[id$=chkEdemaRodillasS]")
    private Checkbox chkEdemaRodillasS;
    @Wire("[id$=chkEdemaRodillasN]")
    private Checkbox chkEdemaRodillasN;
    @Wire("[id$=chkEdemaRodillasD]")
    private Checkbox chkEdemaRodillasD;
    
    @Wire("[id$=chkEdemaTobillosS]")
    private Checkbox chkEdemaTobillosS;
    @Wire("[id$=chkEdemaTobillosN]")
    private Checkbox chkEdemaTobillosN;
    @Wire("[id$=chkEdemaTobillosD]")
    private Checkbox chkEdemaTobillosD;
    
    /*Cabeza*/
    @Wire("[id$=chkCefaleaS]")
    private Checkbox chkCefaleaS;
    @Wire("[id$=chkCefaleaN]")
    private Checkbox chkCefaleaN;
    @Wire("[id$=chkCefaleaD]")
    private Checkbox chkCefaleaD;
    
    @Wire("[id$=chkRigidezCuelloS]")
    private Checkbox chkRigidezCuelloS;
    @Wire("[id$=chkRigidezCuelloN]")
    private Checkbox chkRigidezCuelloN;
    
    @Wire("[id$=chkInyecciónConjuntivalS]")
    private Checkbox chkInyecciónConjuntivalS;
    @Wire("[id$=chkInyecciónConjuntivalN]")
    private Checkbox chkInyecciónConjuntivalN;
    
    @Wire("[id$=chkHemorragiaSubconjuntivalS]")
    private Checkbox chkHemorragiaSubconjuntivalS;
    @Wire("[id$=chkHemorragiaSubconjuntivalN]")
    private Checkbox chkHemorragiaSubconjuntivalN;
    
    @Wire("[id$=chkDolorRetroocularS]")
    private Checkbox chkDolorRetroocularS;
    @Wire("[id$=chkDolorRetroocularN]")
    private Checkbox chkDolorRetroocularN;
    @Wire("[id$=chkDolorRetroocularD]")
    private Checkbox chkDolorRetroocularD;
    
    @Wire("[id$=chkFontanelaAbombadaS]")
    private Checkbox chkFontanelaAbombadaS;
    @Wire("[id$=chkFontanelaAbombadaN]")
    private Checkbox chkFontanelaAbombadaN;
    
    @Wire("[id$=chkIctericiaConjuntivalS]")
    private Checkbox chkIctericiaConjuntivalS;
    @Wire("[id$=chkIctericiaConjuntivalN]")
    private Checkbox chkIctericiaConjuntivalN;
    
    /*Deshidratación*/
    @Wire("[id$=chkLenguaMucosaSecasS]")
    private Checkbox chkLenguaMucosaSecasS;
    @Wire("[id$=chkLenguaMucosaSecasN]")
    private Checkbox chkLenguaMucosaSecasN;
    
    @Wire("[id$=chkPliegueCutaneoS]")
    private Checkbox chkPliegueCutaneoS;
    @Wire("[id$=chkPliegueCutaneoN]")
    private Checkbox chkPliegueCutaneoN;
    
    @Wire("[id$=chkOrinaReducidaS]")
    private Checkbox chkOrinaReducidaS;
    @Wire("[id$=chkOrinaReducidaN]")
    private Checkbox chkOrinaReducidaN;
    @Wire("[id$=chkOrinaReducidaD]")
    private Checkbox chkOrinaReducidaD;
    
    @Wire("[id$=chkBebeAvidoSedS]")
    private Checkbox chkBebeAvidoSedS;
    @Wire("[id$=chkBebeAvidoSedN]")
    private Checkbox chkBebeAvidoSedN;
    
    @Wire("[id$=chkOjosHundidosS]")
    private Checkbox chkOjosHundidosS;
    @Wire("[id$=chkOjosHundidosN]")
    private Checkbox chkOjosHundidosN;
    
    @Wire("[id$=chkFontanelaHundidaS]")
    private Checkbox chkFontanelaHundidaS;
    @Wire("[id$=chkFontanelaHundidaN]")
    private Checkbox chkFontanelaHundidaN;
    
    /*Cutáneo*/
    @Wire("[id$=chkRashLocalizadoS]")
    private Checkbox chkRashLocalizadoS;
    @Wire("[id$=chkRashLocalizadoN]")
    private Checkbox chkRashLocalizadoN;
    
    @Wire("[id$=chkRashGeneralizadoS]")
    private Checkbox chkRashGeneralizadoS;
    @Wire("[id$=chkRashGeneralizadoN]")
    private Checkbox chkRashGeneralizadoN;
    
    @Wire("[id$=chkRashEritematosoS]")
    private Checkbox chkRashEritematosoS;
    @Wire("[id$=chkRashEritematosoN]")
    private Checkbox chkRashEritematosoN;
    
    @Wire("[id$=chkRashMacularS]")
    private Checkbox chkRashMacularS;
    @Wire("[id$=chkRashMacularN]")
    private Checkbox chkRashMacularN;
    
    @Wire("[id$=chkRashPapularS]")
    private Checkbox chkRashPapularS;
    @Wire("[id$=chkRashPapularN]")
    private Checkbox chkRashPapularN;
    
    @Wire("[id$=chkPielMoteadaS]")
    private Checkbox chkPielMoteadaS;
    @Wire("[id$=chkPielMoteadaN]")
    private Checkbox chkPielMoteadaN;
    
    @Wire("[id$=chkRuborFacialS]")
    private Checkbox chkRuborFacialS;
    @Wire("[id$=chkRuborFacialN]")
    private Checkbox chkRuborFacialN;
    
    @Wire("[id$=chkEquimosisS]")
    private Checkbox chkEquimosisS;
    @Wire("[id$=chkEquimosisN]")
    private Checkbox chkEquimosisN;
    
    @Wire("[id$=chkCianosisCentralS]")
    private Checkbox chkCianosisCentralS;
    @Wire("[id$=chkCianosisCentralN]")
    private Checkbox chkCianosisCentralN;
    
    @Wire("[id$=chkIctericiaS]")
    private Checkbox chkIctericiaS;
    @Wire("[id$=chkIctericiaN]")
    private Checkbox chkIctericiaN;
    
    /*Garganta*/
    @Wire("[id$=chkEritemaS]")
    private Checkbox chkEritemaS;
    @Wire("[id$=chkEritemaN]")
    private Checkbox chkEritemaN;
    
    @Wire("[id$=chkDolorGargantaS]")
    private Checkbox chkDolorGargantaS;
    @Wire("[id$=chkDolorGargantaN]")
    private Checkbox chkDolorGargantaN;
    @Wire("[id$=chkDolorGargantaD]")
    private Checkbox chkDolorGargantaD;
    
    @Wire("[id$=chkAdenopatiasCervicalesS]")
    private Checkbox chkAdenopatiasCervicalesS;
    @Wire("[id$=chkAdenopatiasCervicalesN]")
    private Checkbox chkAdenopatiasCervicalesN;
    
    @Wire("[id$=chkExudadoS]")
    private Checkbox chkExudadoS;
    @Wire("[id$=chkExudadoN]")
    private Checkbox chkExudadoN;
    
    @Wire("[id$=chkPetequiasMucosaS]")
    private Checkbox chkPetequiasMucosaS;
    @Wire("[id$=chkPetequiasMucosaN]")
    private Checkbox chkPetequiasMucosaN;
    
    /*Renal*/
    @Wire("[id$=chkSintomasUrinariosS]")
    private Checkbox chkSintomasUrinariosS;
    @Wire("[id$=chkSintomasUrinariosN]")
    private Checkbox chkSintomasUrinariosN;
    @Wire("[id$=chkSintomasUrinariosD]")
    private Checkbox chkSintomasUrinariosD;
    
    @Wire("[id$=chkLeucocituriaXCampoS]")
    private Checkbox chkLeucocituriaXCampoS;
    @Wire("[id$=chkLeucocituriaXCampoN]")
    private Checkbox chkLeucocituriaXCampoN;
    @Wire("[id$=chkLeucocituriaXCampoD]")
    private Checkbox chkLeucocituriaXCampoD;
    
    @Wire("[id$=chkNitritosS]")
    private Checkbox chkNitritosS;
    @Wire("[id$=chkNitritosN]")
    private Checkbox chkNitritosN;
    @Wire("[id$=chkNitritosD]")
    private Checkbox chkNitritosD;
    
    @Wire("[id$=chkEritrocitosXCampoS]")
    private Checkbox chkEritrocitosXCampoS;
    @Wire("[id$=chkEritrocitosXCampoN]")
    private Checkbox chkEritrocitosXCampoN;
    @Wire("[id$=chkEritrocitosXCampoD]")
    private Checkbox chkEritrocitosXCampoD;
    
    @Wire("[id$=chkBilirrubinuriaS]")
    private Checkbox chkBilirrubinuriaS;
    @Wire("[id$=chkBilirrubinuriaN]")
    private Checkbox chkBilirrubinuriaN;
    @Wire("[id$=chkBilirrubinuriaD]")
    private Checkbox chkBilirrubinuriaD;
    
    /*Estado Nutricional*/
    @Wire("[id$=chkObesoS]")
    private Checkbox chkObesoS;
    @Wire("[id$=chkObesoN]")
    private Checkbox chkObesoN;
    @Wire("[id$=chkObesoD]")
    private Checkbox chkObesoD;
    
    @Wire("[id$=chkSobrepesoS]")
    private Checkbox chkSobrepesoS;
    @Wire("[id$=chkSobrepesoN]")
    private Checkbox chkSobrepesoN;
    @Wire("[id$=chkSobrepesoD]")
    private Checkbox chkSobrepesoD;
    
    @Wire("[id$=chkSospechaProblemaS]")
    private Checkbox chkSospechaProblemaS;
    @Wire("[id$=chkSospechaProblemaN]")
    private Checkbox chkSospechaProblemaN;
    @Wire("[id$=chkSospechaProblemaD]")
    private Checkbox chkSospechaProblemaD;
    
    @Wire("[id$=chkNormalS]")
    private Checkbox chkNormalS;
    @Wire("[id$=chkNormalN]")
    private Checkbox chkNormalN;
    @Wire("[id$=chkNormalD]")
    private Checkbox chkNormalD;
    
    @Wire("[id$=chkBajoPesoS]")
    private Checkbox chkBajoPesoS;
    @Wire("[id$=chkBajoPesoN]")
    private Checkbox chkBajoPesoN;
    @Wire("[id$=chkBajoPesoD]")
    private Checkbox chkBajoPesoD;
    
    @Wire("[id$=chkBajoPesoSeveroS]")
    private Checkbox chkBajoPesoSeveroS;
    @Wire("[id$=chkBajoPesoSeveroN]")
    private Checkbox chkBajoPesoSeveroN;
    @Wire("[id$=chkBajoPesoSeveroD]")
    private Checkbox chkBajoPesoSeveroD;
    
    @Wire("[id$=txtImc]")
    private Textbox txtImc;
    
    /*Respiratorio*/
    @Wire("[id$=chkTosS]")
    private Checkbox chkTosS;
    @Wire("[id$=chkTosN]")
    private Checkbox chkTosN;
    @Wire("[id$=chkTosD]")
    private Checkbox chkTosD;
    
    @Wire("[id$=chkRinorreaS]")
    private Checkbox chkRinorreaS;
    @Wire("[id$=chkRinorreaN]")
    private Checkbox chkRinorreaN;
    @Wire("[id$=chkRinorreaD]")
    private Checkbox chkRinorreaD;
    
    @Wire("[id$=chkCongestionNasalS]")
    private Checkbox chkCongestionNasalS;
    @Wire("[id$=chkCongestionNasalN]")
    private Checkbox chkCongestionNasalN;
    @Wire("[id$=chkCongestionNasalD]")
    private Checkbox chkCongestionNasalD;
    
    @Wire("[id$=chkOtalgiaS]")
    private Checkbox chkOtalgiaS;
    @Wire("[id$=chkOtalgiaN]")
    private Checkbox chkOtalgiaN;
    @Wire("[id$=chkOtalgiaD]")
    private Checkbox chkOtalgiaD;
    
    @Wire("[id$=chkAleteoNasalS]")
    private Checkbox chkAleteoNasalS;
    @Wire("[id$=chkAleteoNasalN]")
    private Checkbox chkAleteoNasalN;
    
    @Wire("[id$=chkApneaS]")
    private Checkbox chkApneaS;
    @Wire("[id$=chkApneaN]")
    private Checkbox chkApneaN;
    
    @Wire("[id$=chkRespiracionRapidaS]")
    private Checkbox chkRespiracionRapidaS;
    @Wire("[id$=chkRespiracionRapidaN]")
    private Checkbox chkRespiracionRapidaN;
    
    @Wire("[id$=chkQuejidoEspiratorioS]")
    private Checkbox chkQuejidoEspiratorioS;
    @Wire("[id$=chkQuejidoEspiratorioN]")
    private Checkbox chkQuejidoEspiratorioN;
    
    @Wire("[id$=chkEstridorReposoS]")
    private Checkbox chkEstridorReposoS;
    @Wire("[id$=chkEstridorReposoN]")
    private Checkbox chkEstridorReposoN;
    
    @Wire("[id$=chkTirajeSubcostalS]")
    private Checkbox chkTirajeSubcostalS;
    @Wire("[id$=chkTirajeSubcostalN]")
    private Checkbox chkTirajeSubcostalN;
    
    @Wire("[id$=chkSibilanciasS]")
    private Checkbox chkSibilanciasS;
    @Wire("[id$=chkSibilanciasN]")
    private Checkbox chkSibilanciasN;
    
    @Wire("[id$=chkCrepitosS]")
    private Checkbox chkCrepitosS;
    @Wire("[id$=chkCrepitosN]")
    private Checkbox chkCrepitosN;
    
    @Wire("[id$=chkRoncosS]")
    private Checkbox chkRoncosS;
    @Wire("[id$=chkRoncosN]")
    private Checkbox chkRoncosN;
    
    @Wire("[id$=chkOtraFIFS]")
    private Checkbox chkOtraFIFS;
    @Wire("[id$=chkOtraFIFN]")
    private Checkbox chkOtraFIFN;
    
    @Wire("[id$=txtNuevaFif]")
    private Datebox txtNuevaFif;
    
    /*Referencia*/
    @Wire("[id$=chkInterconsultaPediatraS]")
    private Checkbox chkInterconsultaPediatraS;
    @Wire("[id$=chkInterconsultaPediatraN]")
    private Checkbox chkInterconsultaPediatraN;
    
    @Wire("[id$=chkReferenciaHospitalS]")
    private Checkbox chkReferenciaHospitalS;
    @Wire("[id$=chkReferenciaHospitalN]")
    private Checkbox chkReferenciaHospitalN;
    
    @Wire("[id$=chkReferenciaDengueS]")
    private Checkbox chkReferenciaDengueS;
    @Wire("[id$=chkReferenciaDengueN]")
    private Checkbox chkReferenciaDengueN;
    
    @Wire("[id$=chkReferenciaIRAGS]")
    private Checkbox chkReferenciaIRAGS;
    @Wire("[id$=chkReferenciaIRAGN]")
    private Checkbox chkReferenciaIRAGN;
    
    @Wire("[id$=chkReferenciaCHIKS]")
    private Checkbox chkReferenciaCHIKS;
    @Wire("[id$=chkReferenciaCHIKN]")
    private Checkbox chkReferenciaCHIKN;
    
    @Wire("[id$=chkETIS]")
    private Checkbox chkETIS;
    @Wire("[id$=chkETIN]")
    private Checkbox chkETIN;
    
    @Wire("[id$=chkIRAGS]")
    private Checkbox chkIRAGS;
    @Wire("[id$=chkIRAGN]")
    private Checkbox chkIRAGN;
    
    @Wire("[id$=chkNeumoniaS]")
    private Checkbox chkNeumoniaS;
    @Wire("[id$=chkNeumoniaN]")
    private Checkbox chkNeumoniaN;
    
    @Wire("[id$=chkCVS]")
    private Checkbox chkCVS;
    @Wire("[id$=chkCVN]")
    private Checkbox chkCVN;
    
    /*Vacunas*/
    @Wire("[id$=chkLactanciaMaternaS]")
    private Checkbox chkLactanciaMaternaS;
    @Wire("[id$=chkLactanciaMaternaN]")
    private Checkbox chkLactanciaMaternaN;
    @Wire("[id$=chkLactanciaMaternaD]")
    private Checkbox chkLactanciaMaternaD;
    
    @Wire("[id$=chkVacunasCompletasS]")
    private Checkbox chkVacunasCompletasS;
    @Wire("[id$=chkVacunasCompletasN]")
    private Checkbox chkVacunasCompletasN;
    @Wire("[id$=chkVacunasCompletasD]")
    private Checkbox chkVacunasCompletasD;
    
    @Wire("[id$=chkVacunaInfluenzaS]")
    private Checkbox chkVacunaInfluenzaS;
    @Wire("[id$=chkVacunaInfluenzaN]")
    private Checkbox chkVacunaInfluenzaN;
    @Wire("[id$=chkVacunaInfluenzaD]")
    private Checkbox chkVacunaInfluenzaD;
    
    @Wire("[id$=txtFechaVacuna]")
    private Datebox txtFechaVacuna;
    
    /*Categoría*/
    @Wire("[id$=chkManifestacionHemorragicaS]")
    private Checkbox chkManifestacionHemorragicaS;
    @Wire("[id$=chkManifestacionHemorragicaN]")
    private Checkbox chkManifestacionHemorragicaN;
    
    @Wire("[id$=chkPruebaTorniquetePositivaS]")
    private Checkbox chkPruebaTorniquetePositivaS;
    @Wire("[id$=chkPruebaTorniquetePositivaN]")
    private Checkbox chkPruebaTorniquetePositivaN;
    
    @Wire("[id$=chkPetequias10S]")
    private Checkbox chkPetequias10S;
    @Wire("[id$=chkPetequias10N]")
    private Checkbox chkPetequias10N;
    
    @Wire("[id$=chkPetequias20S]")
    private Checkbox chkPetequias20S;
    @Wire("[id$=chkPetequias20N]")
    private Checkbox chkPetequias20N;
    
    @Wire("[id$=chkPielExtremidadesFriasS]")
    private Checkbox chkPielExtremidadesFriasS;
    @Wire("[id$=chkPielExtremidadesFriasN]")
    private Checkbox chkPielExtremidadesFriasN;
    
    @Wire("[id$=chkPalidezExtremidadesS]")
    private Checkbox chkPalidezExtremidadesS;
    @Wire("[id$=chkPalidezExtremidadesN]")
    private Checkbox chkPalidezExtremidadesN;
    
    @Wire("[id$=txtSaturacion]")
    private Textbox txtSaturacion;
    
    @Wire("[id$=cmbCategoria]")
    private Combobox cmbCategoria;
    
    @Wire("[id$=chkEpistaxisS]")
    private Checkbox chkEpistaxisS;
    @Wire("[id$=chkEpistaxisN]")
    private Checkbox chkEpistaxisN;
    
    @Wire("[id$=chkGingivorragiaS]")
    private Checkbox chkGingivorragiaS;
    @Wire("[id$=chkGingivorragiaN]")
    private Checkbox chkGingivorragiaN;
    
    @Wire("[id$=chkPetequiasEspontaneasS]")
    private Checkbox chkPetequiasEspontaneasS;
    @Wire("[id$=chkPetequiasEspontaneasN]")
    private Checkbox chkPetequiasEspontaneasN;
    
    @Wire("[id$=chkLlenadoCapilarS]")
    private Checkbox chkLlenadoCapilarS;
    @Wire("[id$=chkLlenadoCapilarN]")
    private Checkbox chkLlenadoCapilarN;
    
    @Wire("[id$=chkCianosisS]")
    private Checkbox chkCianosisS;
    @Wire("[id$=chkCianosisN]")
    private Checkbox chkCianosisN;
    
    @Wire("[id$=txtLinfocitoAtipicos]")
    private Textbox txtLinfocitoAtipicos;
    
    @Wire("[id$=txtFechaLinfocitoAtipicos]")
    private Datebox txtFechaLinfocitoAtipicos;
    
    @Wire("[id$=cmbCambioCategoria]")
    private Combobox cmbCambioCategoria;
    
    @Wire("[id$=chkHipermenorreaS]")
    private Checkbox chkHipermenorreaS;
    @Wire("[id$=chkHipermenorreaN]")
    private Checkbox chkHipermenorreaN;
    @Wire("[id$=chkHipermenorreaD]")
    private Checkbox chkHipermenorreaD;
    
    @Wire("[id$=chkHematemesisS]")
    private Checkbox chkHematemesisS;
    @Wire("[id$=chkHematemesisN]")
    private Checkbox chkHematemesisN;
    
    @Wire("[id$=chkMelenaS]")
    private Checkbox chkMelenaS;
    @Wire("[id$=chkMelenaN]")
    private Checkbox chkMelenaN;
    
    @Wire("[id$=chkHemoconcentracionS]")
    private Checkbox chkHemoconcentracionS;
    @Wire("[id$=chkHemoconcentracionN]")
    private Checkbox chkHemoconcentracionN;
    @Wire("[id$=chkHemoconcentracionD]")
    private Checkbox chkHemoconcentracionD;
    
    @Wire("[id$=txtHemoconcentracion]")
    private Textbox txtHemoconcentracion;
    
    @Wire("[id$=chkHospitalizadoUltimoAnioS]")
    private Checkbox chkHospitalizadoUltimoAnioS;
    @Wire("[id$=chkHospitalizadoUltimoAnioN]")
    private Checkbox chkHospitalizadoUltimoAnioN;
    
    @Wire("[id$=txtHospitalizadoUltimoAnio]")
    private Textbox txtHospitalizadoUltimoAnio;
    
    @Wire("[id$=chkTransfusionSangreS]")
    private Checkbox chkTransfusionSangreS;
    @Wire("[id$=chkTransfusionSangreN]")
    private Checkbox chkTransfusionSangreN;
    
    @Wire("[id$=txtTransfusionSangre]")
    private Textbox txtTransfusionSangre;
    
    @Wire("[id$=chkEstaTomandoMedicamentoS]")
    private Checkbox chkEstaTomandoMedicamentoS;
    @Wire("[id$=chkEstaTomandoMedicamentoN]")
    private Checkbox chkEstaTomandoMedicamentoN;
    
    @Wire("[id$=txtEstaTomandoMedicamento]")
    private Textbox txtEstaTomandoMedicamento;
    
    @Wire("[id$=chkTomaMedicamentoDistintoS]")
    private Checkbox chkTomaMedicamentoDistintoS;
    @Wire("[id$=chkTomaMedicamentoDistintoN]")
    private Checkbox chkTomaMedicamentoDistintoN;
    
    @Wire("[id$=txtTomaMedicamentoDistinto]")
    private Textbox txtTomaMedicamentoDistinto;
    
    /*Exámenes*/
    @Wire("[id$=chkBhcS]")
    private Checkbox chkBhcS;
    @Wire("[id$=chkBhcN]")
    private Checkbox chkBhcN;
    
    @Wire("[id$=chkSerologiaDengueS]")
    private Checkbox chkSerologiaDengueS;
    @Wire("[id$=chkSerologiaDengueN]")
    private Checkbox chkSerologiaDengueN;
    
    @Wire("[id$=chkSerologiaChikS]")
    private Checkbox chkSerologiaChikS;
    @Wire("[id$=chkSerologiaChikN]")
    private Checkbox chkSerologiaChikN;
    
    @Wire("[id$=chkGotaGruesaS]")
    private Checkbox chkGotaGruesaS;
    @Wire("[id$=chkGotaGruesaN]")
    private Checkbox chkGotaGruesaN;
    
    @Wire("[id$=chkExtendidoPerifericoS]")
    private Checkbox chkExtendidoPerifericoS;
    @Wire("[id$=chkExtendidoPerifericoN]")
    private Checkbox chkExtendidoPerifericoN;
    
    @Wire("[id$=chkEgoS]")
    private Checkbox chkEgoS;
    @Wire("[id$=chkEgoN]")
    private Checkbox chkEgoN;
    
    @Wire("[id$=chkEghS]")
    private Checkbox chkEghS;
    @Wire("[id$=chkEghN]")
    private Checkbox chkEghN;
    
    @Wire("[id$=chkCitologiaFecalS]")
    private Checkbox chkCitologiaFecalS;
    @Wire("[id$=chkCitologiaFecalN]")
    private Checkbox chkCitologiaFecalN;
    
    @Wire("[id$=chkFactorReumatoideoS]")
    private Checkbox chkFactorReumatoideoS;
    @Wire("[id$=chkFactorReumatoideoN]")
    private Checkbox chkFactorReumatoideoN;
    
    @Wire("[id$=chkAlbuminaS]")
    private Checkbox chkAlbuminaS;
    @Wire("[id$=chkAlbuminaN]")
    private Checkbox chkAlbuminaN;
    
    @Wire("[id$=chkAstAltS]")
    private Checkbox chkAstAltS;
    @Wire("[id$=chkAstAltN]")
    private Checkbox chkAstAltN;
    
    @Wire("[id$=chkBilirubinasS]")
    private Checkbox chkBilirubinasS;
    @Wire("[id$=chkBilirubinasN]")
    private Checkbox chkBilirubinasN;
    
    @Wire("[id$=chkCpkS]")
    private Checkbox chkCpkS;
    @Wire("[id$=chkCpkN]")
    private Checkbox chkCpkN;
    
    @Wire("[id$=chkColesterolS]")
    private Checkbox chkColesterolS;
    @Wire("[id$=chkColesterolN]")
    private Checkbox chkColesterolN;
    
    @Wire("[id$=chkInfluenzaS]")
    private Checkbox chkInfluenzaS;
    @Wire("[id$=chkInfluenzaN]")
    private Checkbox chkInfluenzaN;
    
    @Wire("[id$=chkOtroExamenS]")
    private Checkbox chkOtroExamenS;
    @Wire("[id$=chkOtroExamenN]")
    private Checkbox chkOtroExamenN;
    
    @Wire("[id$=txtOtroExamen]")
    private Textbox txtOtroExamen;
    
    /*Tratamiento*/
    @Wire("[id$=chkAcetaminofenS]")
    private Checkbox chkAcetaminofenS;
    @Wire("[id$=chkAcetaminofenN]")
    private Checkbox chkAcetaminofenN;
    
    @Wire("[id$=chkAsaS]")
    private Checkbox chkAsaS;
    @Wire("[id$=chkAsaN]")
    private Checkbox chkAsaN;
    
    @Wire("[id$=chkIbuprofenS]")
    private Checkbox chkIbuprofenS;
    @Wire("[id$=chkIbuprofenN]")
    private Checkbox chkIbuprofenN;
    
    @Wire("[id$=chkPenicilinaS]")
    private Checkbox chkPenicilinaS;
    @Wire("[id$=chkPenicilinaN]")
    private Checkbox chkPenicilinaN;
    
    @Wire("[id$=chkAmoxicilinaS]")
    private Checkbox chkAmoxicilinaS;
    @Wire("[id$=chkAmoxicilinaN]")
    private Checkbox chkAmoxicilinaN;
    
    @Wire("[id$=chkDicloxacilinaS]")
    private Checkbox chkDicloxacilinaS;
    @Wire("[id$=chkDicloxacilinaN]")
    private Checkbox chkDicloxacilinaN;
    
    @Wire("[id$=chkOtroAntibioticoS]")
    private Checkbox chkOtroAntibioticoS;
    @Wire("[id$=chkOtroAntibioticoN]")
    private Checkbox chkOtroAntibioticoN;
    
    @Wire("[id$=txtOtroAntibiotico]")
    private Textbox txtOtroAntibiotico;
    
    @Wire("[id$=chkFurazolidonaS]")
    private Checkbox chkFurazolidonaS;
    @Wire("[id$=chkFurazolidonaN]")
    private Checkbox chkFurazolidonaN;
    
    @Wire("[id$=chkMetronidazolTinidazolS]")
    private Checkbox chkMetronidazolTinidazolS;
    @Wire("[id$=chkMetronidazolTinidazolN]")
    private Checkbox chkMetronidazolTinidazolN;
    
    @Wire("[id$=chkAlbendazolMebendazolS]")
    private Checkbox chkAlbendazolMebendazolS;
    @Wire("[id$=chkAlbendazolMebendazolN]")
    private Checkbox chkAlbendazolMebendazolN;
    
    @Wire("[id$=chkSulfatoFerrosoS]")
    private Checkbox chkSulfatoFerrosoS;
    @Wire("[id$=chkSulfatoFerrosoN]")
    private Checkbox chkSulfatoFerrosoN;
    
    @Wire("[id$=chkSueroOralS]")
    private Checkbox chkSueroOralS;
    @Wire("[id$=chkSueroOralN]")
    private Checkbox chkSueroOralN;
    
    @Wire("[id$=chkSulfatoZincS]")
    private Checkbox chkSulfatoZincS;
    @Wire("[id$=chkSulfatoZincN]")
    private Checkbox chkSulfatoZincN;
    
    @Wire("[id$=chkLiquidosIVS]")
    private Checkbox chkLiquidosIVS;
    @Wire("[id$=chkLiquidosIVN]")
    private Checkbox chkLiquidosIVN;
    
    @Wire("[id$=chkPrednisonaS]")
    private Checkbox chkPrednisonaS;
    @Wire("[id$=chkPrednisonaN]")
    private Checkbox chkPrednisonaN;
    
    @Wire("[id$=chkHidrocortisonaIVS]")
    private Checkbox chkHidrocortisonaIVS;
    @Wire("[id$=chkHidrocortisonaIVN]")
    private Checkbox chkHidrocortisonaIVN;
    
    @Wire("[id$=chkSalbutamolS]")
    private Checkbox chkSalbutamolS;
    @Wire("[id$=chkSalbutamolN]")
    private Checkbox chkSalbutamolN;
    
    @Wire("[id$=chkOseltamivirS]")
    private Checkbox chkOseltamivirS;
    @Wire("[id$=chkOseltamivirN]")
    private Checkbox chkOseltamivirN;
    
    /*Historia y Exámen Físico*/
    @Wire("[id$=txtHistoriaExamenFisico]")
    private Textbox txtHistoriaExamenFisico;
    
    /*Planes*/
    @Wire("[id$=txtPlanes]")
    private Textbox txtPlanes;
    
    @Wire("[id$=cmbDiagnostico1]")
    private Combobox cmbDiagnostico1;
    
    @Wire("[id$=cmbDiagnostico2]")
    private Combobox cmbDiagnostico2;
    
    @Wire("[id$=cmbDiagnostico3]")
    private Combobox cmbDiagnostico3;
    
    @Wire("[id$=cmbDiagnostico4]")
    private Combobox cmbDiagnostico4;
    
    @Wire("[id$=txtOtroDiagnostico]")
    private Textbox txtOtroDiagnostico;
    
    @Wire("[id$=txtTelefono]")
    private Textbox txtTelefono;
    
    @Wire("[id$=txtFechaProximaCita]")
    private Datebox txtFechaProximaCita;
    
    @Wire("[id$=cmbColegio]")
    private Combobox cmbColegio;
    
    @Wire("[id$=cmbHorarioClases]")
    private Combobox cmbHorarioClases;
    
    @Wire("[id$=cmbMedico]")
    private Combobox cmbMedico;
    
    @Wire("[id$=cmbEnfermeria]")
    private Combobox cmbEnfermeria;
    
    @Wire("[id$=cmbSupervisor]")
    private Combobox cmbSupervisor;
    
    @Wire("[id$=txtFechaCierre]")
    private Datebox txtFechaCierre;
    
    @Wire("[id$=txtHoraFechaCierre]")
    private Textbox txtHoraFechaCierre;
    
    @Wire("[id$=txtNumHojaConsulta]")
    private Textbox txtNumHojaConsulta;
    
    @Wire("[id$=txtSecHojaConsulta]")
    private Textbox txtSecHojaConsulta;
    
    
    /*Botones S N D*/
    @Wire("[id$=btnEstadoGeneralS]")
    private Button btnEstadoGeneralS;
    @Wire("[id$=btnEstadoGeneralN]")
    private Button btnEstadoGeneralN;
    @Wire("[id$=btnEstadoGeneralD]")
    private Button btnEstadoGeneralD;
    
    @Wire("[id$=btnGastrointestinalS]")
    private Button btnGastrointestinalS;
    @Wire("[id$=btnGastrointestinalN]")
    private Button btnGastrointestinalN;
    @Wire("[id$=btnGastrointestinalD]")
    private Button btnGastrointestinalD;
    
    @Wire("[id$=btnOsteomuscularS]")
    private Button btnOsteomuscularS;
    @Wire("[id$=btnOsteomuscularN]")
    private Button btnOsteomuscularN;
    @Wire("[id$=btnOsteomuscularD]")
    private Button btnOsteomuscularD;
    
    @Wire("[id$=btnCabezaS]")
    private Button btnCabezaS;
    @Wire("[id$=btnCabezaN]")
    private Button btnCabezaN;
    @Wire("[id$=btnCabezaD]")
    private Button btnCabezaD;
    
    @Wire("[id$=btnDeshidratacionS]")
    private Button btnDeshidratacionS;
    @Wire("[id$=btnDeshidratacionN]")
    private Button btnDeshidratacionN;
    @Wire("[id$=btnDeshidratacionD]")
    private Button btnDeshidratacionD;
    
    @Wire("[id$=btnCutaneoS]")
    private Button btnCutaneoS;
    @Wire("[id$=btnCutaneoN]")
    private Button btnCutaneoN;
    
    @Wire("[id$=btnGargantaS]")
    private Button btnGargantaS;
    @Wire("[id$=btnGargantaN]")
    private Button btnGargantaN;
    @Wire("[id$=btnGargantaD]")
    private Button btnGargantaD;
    
    @Wire("[id$=btnRenalS]")
    private Button btnRenalS;
    @Wire("[id$=btnRenalN]")
    private Button btnRenalN;
    @Wire("[id$=btnRenalD]")
    private Button btnRenalD;
    
    @Wire("[id$=btnEstadoNutricionalS]")
    private Button btnEstadoNutricionalS;
    @Wire("[id$=btnEstadoNutricionalN]")
    private Button btnEstadoNutricionalN;
    @Wire("[id$=btnEstadoNutricionalD]")
    private Button btnEstadoNutricionalD;
    
    @Wire("[id$=btnRespiratorioS]")
    private Button btnRespiratorioS;
    @Wire("[id$=btnRespiratorioN]")
    private Button btnRespiratorioN;
    @Wire("[id$=btnRespiratorioD]")
    private Button btnRespiratorioD;
    
    @Wire("[id$=btnReferenciaS]")
    private Button btnReferenciaS;
    @Wire("[id$=btnReferenciaN]")
    private Button btnReferenciaN;
    
    @Wire("[id$=btnVacunasS]")
    private Button btnVacunasS;
    @Wire("[id$=btnVacunasN]")
    private Button btnVacunasN;
    @Wire("[id$=btnVacunasD]")
    private Button btnVacunasD;
    
    @Wire("[id$=btnCategoriaS]")
    private Button btnCategoriaS;
    @Wire("[id$=btnCategoriaN]")
    private Button btnCategoriaN;
    @Wire("[id$=btnCategoriaD]")
    private Button btnCategoriaD;
    
    @Wire("[id$=btnExamenesS]")
    private Button btnExamenesS;
    @Wire("[id$=btnExamenesN]")
    private Button btnExamenesN;
    
    @Wire("[id$=btnTratamientoS]")
    private Button btnTratamientoS;
    @Wire("[id$=btnTratamientoN]")
    private Button btnTratamientoN;
    
    @Wire("[id$=btnBuscar]")
    private Button btnBuscar;
    
    @Wire("[id$=btnGuardar]")
    private Button btnGuardar;
    
    @Wire("[id$=btnNuevo]")
    private Button btnNuevo;
    
    @Wire("[id$=btnImprimir]")
    private Button btnImprimir;
    
    @Wire("[id$=btnVerPdf]")
    private Button btnVerPdf;
    
    
    @Override
    public void doAfterCompose(Component comp) throws Exception{
        super.doAfterCompose(comp);
        init();
    }
    
    /**
    *
    */
   private void init() {
      cargarDiagnosticos();
      cargarColegios();
      cargarMedicos();
      cargarEnfermeros();
      cargarSupervisor();
      this.txtCodExpediente.setFocus(true);
   }
    
    /**
     * Menejador del evento Click del botón "Buscar (Lupa junto al Código)"
     */
    @Listen("onClick=[id$=btnBuscar]")
    public void btnBuscar_onClick() {
        buscarPaciente();
    }
    
    
    /**
     * Menejador del evento OK(Enter) del campo "Código"
     */
    @Listen("onOK=[id$=txtCodExpediente]")
    public void txtCodExpediente_onOk() {
        try {
        	buscarPaciente();
		} catch (Exception e) {
			e.printStackTrace();			
			Mensajes.enviarMensaje(Mensajes.ERROR_PROCESAR_DATOS, Mensajes.TipoMensaje.ERROR);
		}
    }
    
    /**
     * Menejador del evento Check Cssfv"
     */
    @Listen("onCheck=[id$=chkCssfv]")
    public void chkchkCssfv_onCheck() {
    	if (this.chkCssfv.isChecked()) {
    		this.chkTerreno.setChecked(false);
    	}
    }
    
    /**
     * Menejador del evento Check Terreno"
     */
    @Listen("onCheck=[id$=chkTerreno]")
    public void chkTerreno_onCheck() {
    	if (this.chkTerreno.isChecked()) {
    		this.chkCssfv.setChecked(false);
    	}
    }
    
    /**
     * Menejador del evento Check Si la consulta es Inicial"
     */
    @Listen("onCheck=[id$=chkCInicial]")
    public void chkCInicial_onCheck() {
    	if (this.chkCInicial.isChecked()) {
    		this.chkCSeguimiento.setChecked(false);
    		this.chkCConvaleciente.setChecked(false);
    	}
    }
    
    /**
     * Menejador del evento Check Si la consulta es de Seguimiento"
     */
    @Listen("onCheck=[id$=chkCSeguimiento]")
    public void chkCSeguimiento_onCheck() {
    	if (this.chkCSeguimiento.isChecked()) {
    		this.chkCInicial.setChecked(false);
    		this.chkCConvaleciente.setChecked(false);
    	}
    }
    
    /**
     * Menejador del evento Check Si la consulta es Convaleciente"
     */
    @Listen("onCheck=[id$=chkCConvaleciente]")
    public void chkCConvaleciente_onCheck() {
    	if (this.chkCConvaleciente.isChecked()) {
    		this.chkCInicial.setChecked(false);
    		this.chkCSeguimiento.setChecked(false);
    	}
    }
    
    
    /**
     * Menejador del evento Check Seguimiento CHIK"
     */
    @Listen("onCheck=[id$=chkSChik1]")
    public void chkSChik1_onCheck() {
    	if (this.chkSChik1.isChecked()) {
    		this.chkSChik2.setChecked(false);
    		this.chkSChik3.setChecked(false);
    		this.chkSChik4.setChecked(false);
    		this.chkSChik5.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkSChik2]")
    public void chkSChik2_onCheck() {
    	if (this.chkSChik2.isChecked()) {
    		this.chkSChik1.setChecked(false);
    		this.chkSChik3.setChecked(false);
    		this.chkSChik4.setChecked(false);
    		this.chkSChik5.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkSChik3]")
    public void chkSChik3_onCheck() {
    	if (this.chkSChik3.isChecked()) {
    		this.chkSChik1.setChecked(false);
    		this.chkSChik2.setChecked(false);
    		this.chkSChik4.setChecked(false);
    		this.chkSChik5.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkSChik4]")
    public void chkSChik4_onCheck() {
    	if (this.chkSChik4.isChecked()) {
    		this.chkSChik1.setChecked(false);
    		this.chkSChik2.setChecked(false);
    		this.chkSChik3.setChecked(false);
    		this.chkSChik5.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkSChik5]")
    public void chkSChik5_onCheck() {
    	if (this.chkSChik5.isChecked()) {
    		this.chkSChik1.setChecked(false);
    		this.chkSChik2.setChecked(false);
    		this.chkSChik3.setChecked(false);
    		this.chkSChik4.setChecked(false);
    	}
    }
    
    /**
     * Menejador del evento Check para los turnos
     */
    @Listen("onCheck=[id$=chkRegular]")
    public void chkRegular_onCheck() {
    	if (this.chkRegular.isChecked()) {
    		this.chkNoche.setChecked(false);
    		this.chkFinSemana.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkNoche]")
    public void chkNoche_onCheck() {
    	if (this.chkNoche.isChecked()) {
    		this.chkRegular.setChecked(false);
    		this.chkFinSemana.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkFinSemana]")
    public void chkFinSemana_onCheck() {
    	if (this.chkFinSemana.isChecked()) {
    		this.chkNoche.setChecked(false);
    		this.chkRegular.setChecked(false);
    	}
    }
    
    /**
     * Manejador del evento check para Estado General
     */
    @Listen("onCheck=[id$=chkFiebreS]")
    public void chkFiebreS_onCheck() {
    	if (this.chkFiebreS.isChecked()) {
    		this.chkFiebreN.setChecked(false);
    		this.chkFiebreD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkFiebreN]")
    public void chkFiebreN_onCheck() {
    	if (this.chkFiebreN.isChecked()) {
    		this.chkFiebreS.setChecked(false);
    		this.chkFiebreD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkFiebreD]")
    public void chkFiebreD_onCheck() {
    	if (this.chkFiebreD.isChecked()) {
    		this.chkFiebreN.setChecked(false);
    		this.chkFiebreS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkAsteniaS]")
    public void chkAsteniaS_onCheck() {
    	if (this.chkAsteniaS.isChecked()) {
    		this.chkAsteniaN.setChecked(false);
    		this.chkAsteniaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkAsteniaN]")
    public void chkAsteniaN_onCheck() {
    	if (this.chkAsteniaN.isChecked()) {
    		this.chkAsteniaS.setChecked(false);
    		this.chkAsteniaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkAsteniaD]")
    public void chkAsteniaD_onCheck() {
    	if (this.chkAsteniaD.isChecked()) {
    		this.chkAsteniaS.setChecked(false);
    		this.chkAsteniaN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkAnormalMenteSomnolientoS]")
    public void chkAnormalMenteSomnolientoS_onCheck() {
    	if (this.chkAnormalMenteSomnolientoS.isChecked()) {
    		this.chkAnormalMenteSomnolientoN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkAnormalMenteSomnolientoN]")
    public void chkAnormalMenteSomnolientoN_onCheck() {
    	if (this.chkAnormalMenteSomnolientoN.isChecked()) {
    		this.chkAnormalMenteSomnolientoS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkMalEstadoGeneralS]")
    public void chkMalEstadoGeneralS_onCheck() {
    	if (this.chkMalEstadoGeneralS.isChecked()) {
    		this.chkMalEstadoGeneralN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkMalEstadoGeneralN]")
    public void chkMalEstadoGeneralN_onCheck() {
    	if (this.chkMalEstadoGeneralN.isChecked()) {
    		this.chkMalEstadoGeneralS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkPerdidaConcienciaS]")
    public void chkPerdidaConcienciaS_onCheck() {
    	if (this.chkPerdidaConcienciaS.isChecked()) {
    		this.chkPerdidaConcienciaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkPerdidaConcienciaN]")
    public void chkPerdidaConcienciaN_onCheck() {
    	if (this.chkPerdidaConcienciaN.isChecked()) {
    		this.chkPerdidaConcienciaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkInquietoIrritableS]")
    public void chkInquietoIrritableS_onCheck() {
    	if (this.chkInquietoIrritableS.isChecked()) {
    		this.chkInquietoIrritableN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkInquietoIrritableN]")
    public void chkInquietoIrritableN_onCheck() {
    	if (this.chkInquietoIrritableN.isChecked()) {
    		this.chkInquietoIrritableS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkConvulsionesS]")
    public void chkConvulsionesS_onCheck() {
    	if (this.chkConvulsionesS.isChecked()) {
    		this.chkConvulsionesN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkConvulsionesN]")
    public void chkConvulsionesN_onCheck() {
    	if (this.chkConvulsionesN.isChecked()) {
    		this.chkConvulsionesS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkHipotermiaS]")
    public void chkHipotermiaS_onCheck() {
    	if (this.chkHipotermiaS.isChecked()) {
    		this.chkHipotermiaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkHipotermiaN]")
    public void chkHipotermiaN_onCheck() {
    	if (this.chkHipotermiaN.isChecked()) {
    		this.chkHipotermiaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkLetargiaS]")
    public void chkLetargiaS_onCheck() {
    	if (this.chkLetargiaS.isChecked()) {
    		this.chkLetargiaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkLetargiaN]")
    public void chkLetargiaN_onCheck() {
    	if (this.chkLetargiaN.isChecked()) {
    		this.chkLetargiaS.setChecked(false);
    	}
    }
    
    /**
     * Manejador del evento check para Gastrointestinal
     */
    @Listen("onCheck=[id$=chkPocoApetitoS]")
    public void chkPocoApetitoS_onCheck() {
    	if (this.chkPocoApetitoS.isChecked()) {
    		this.chkPocoApetitoN.setChecked(false);
    		this.chkPocoApetitoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkPocoApetitoN]")
    public void chkPocoApetitoN_onCheck() {
    	if (this.chkPocoApetitoN.isChecked()) {
    		this.chkPocoApetitoS.setChecked(false);
    		this.chkPocoApetitoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkPocoApetitoD]")
    public void chkPocoApetitoD_onCheck() {
    	if (this.chkPocoApetitoD.isChecked()) {
    		this.chkPocoApetitoS.setChecked(false);
    		this.chkPocoApetitoN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkNauseaS]")
    public void chkNauseaS_onCheck() {
    	if (this.chkNauseaS.isChecked()) {
    		this.chkNauseaN.setChecked(false);
    		this.chkNauseaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkNauseaN]")
    public void chkNauseaN_onCheck() {
    	if (this.chkNauseaN.isChecked()) {
    		this.chkNauseaS.setChecked(false);
    		this.chkNauseaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkNauseaD]")
    public void chkNauseaD_onCheck() {
    	if (this.chkNauseaD.isChecked()) {
    		this.chkNauseaS.setChecked(false);
    		this.chkNauseaN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkDificultadAlimentaseS]")
    public void chkDificultadAlimentaseS_onCheck() {
    	if (this.chkDificultadAlimentaseS.isChecked()) {
    		this.chkDificultadAlimentaseN.setChecked(false);
    		this.chkDificultadAlimentaseD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDificultadAlimentaseN]")
    public void chkDificultadAlimentaseN_onCheck() {
    	if (this.chkDificultadAlimentaseN.isChecked()) {
    		this.chkDificultadAlimentaseS.setChecked(false);
    		this.chkDificultadAlimentaseD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDificultadAlimentaseD]")
    public void chkDificultadAlimentaseD_onCheck() {
    	if (this.chkDificultadAlimentaseD.isChecked()) {
    		this.chkDificultadAlimentaseS.setChecked(false);
    		this.chkDificultadAlimentaseN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkVomito12HorasS]")
    public void chkVomito12HorasS_onCheck() {
    	if (this.chkVomito12HorasS.isChecked()) {
    		this.chkVomito12HorasN.setChecked(false);
    		this.chkVomito12HorasD.setChecked(false);
    		this.txtVomito12Horas.setDisabled(false);
    	} else {
    		this.txtVomito12Horas.setDisabled(true);
    		this.txtVomito12Horas.setValue("");
    	}
    }
    @Listen("onCheck=[id$=chkVomito12HorasN]")
    public void chkVomito12HorasN_onCheck() {
    	if (this.chkVomito12HorasN.isChecked()) {
    		this.chkVomito12HorasS.setChecked(false);
    		this.chkVomito12HorasD.setChecked(false);
    		this.txtVomito12Horas.setDisabled(true);
    		this.txtVomito12Horas.setValue("");
    	}
    }
    @Listen("onCheck=[id$=chkVomito12HorasD]")
    public void chkVomito12HorasD_onCheck() {
    	if (this.chkVomito12HorasD.isChecked()) {
    		this.chkVomito12HorasS.setChecked(false);
    		this.chkVomito12HorasN.setChecked(false);
    		this.txtVomito12Horas.setDisabled(true);
    		this.txtVomito12Horas.setValue("");
    	}
    }
    
    @Listen("onCheck=[id$=chkDiarreaS]")
    public void chkDiarreaS_onCheck() {
    	if (this.chkDiarreaS.isChecked()) {
    		this.chkDiarreaN.setChecked(false);
    		this.chkDiarreaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDiarreaN]")
    public void chkDiarreaN_onCheck() {
    	if (this.chkDiarreaN.isChecked()) {
    		this.chkDiarreaS.setChecked(false);
    		this.chkDiarreaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDiarreaD]")
    public void chkDiarreaD_onCheck() {
    	if (this.chkDiarreaD.isChecked()) {
    		this.chkDiarreaS.setChecked(false);
    		this.chkDiarreaN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkDiarreaSangreS]")
    public void chkDiarreaSangreS_onCheck() {
    	if (this.chkDiarreaSangreS.isChecked()) {
    		this.chkDiarreaSangreN.setChecked(false);
    		this.chkDiarreaSangreD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDiarreaSangreN]")
    public void chkDiarreaSangreN_onCheck() {
    	if (this.chkDiarreaSangreN.isChecked()) {
    		this.chkDiarreaSangreS.setChecked(false);
    		this.chkDiarreaSangreD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDiarreaSangreD]")
    public void chkDiarreaSangreD_onCheck() {
    	if (this.chkDiarreaSangreD.isChecked()) {
    		this.chkDiarreaSangreS.setChecked(false);
    		this.chkDiarreaSangreN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkEstrenimientoS]")
    public void chkEstrenimientoS_onCheck() {
    	if (this.chkEstrenimientoS.isChecked()) {
    		this.chkEstrenimientoN.setChecked(false);
    		this.chkEstrenimientoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEstrenimientoN]")
    public void chkEstrenimientoN_onCheck() {
    	if (this.chkEstrenimientoN.isChecked()) {
    		this.chkEstrenimientoS.setChecked(false);
    		this.chkEstrenimientoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEstrenimientoD]")
    public void chkEstrenimientoD_onCheck() {
    	if (this.chkEstrenimientoD.isChecked()) {
    		this.chkEstrenimientoS.setChecked(false);
    		this.chkEstrenimientoN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkDolorAbInterS]")
    public void chkDolorAbInterS_onCheck() {
    	if (this.chkDolorAbInterS.isChecked()) {
    		this.chkDolorAbInterN.setChecked(false);
    		this.chkDolorAbInterD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDolorAbInterN]")
    public void chkDolorAbInterN_onCheck() {
    	if (this.chkDolorAbInterN.isChecked()) {
    		this.chkDolorAbInterS.setChecked(false);
    		this.chkDolorAbInterD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDolorAbInterD]")
    public void chkDolorAbInterD_onCheck() {
    	if (this.chkDolorAbInterD.isChecked()) {
    		this.chkDolorAbInterS.setChecked(false);
    		this.chkDolorAbInterN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkDolorAbContinuoS]")
    public void chkDolorAbContinuoS_onCheck() {
    	if (this.chkDolorAbContinuoS.isChecked()) {
    		this.chkDolorAbContinuoN.setChecked(false);
    		this.chkDolorAbContinuoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDolorAbContinuoN]")
    public void chkDolorAbContinuoN_onCheck() {
    	if (this.chkDolorAbContinuoN.isChecked()) {
    		this.chkDolorAbContinuoS.setChecked(false);
    		this.chkDolorAbContinuoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDolorAbContinuoD]")
    public void chkDolorAbContinuoD_onCheck() {
    	if (this.chkDolorAbContinuoD.isChecked()) {
    		this.chkDolorAbContinuoS.setChecked(false);
    		this.chkDolorAbContinuoN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkEpigastralgiaS]")
    public void chkEpigastralgiaS_onCheck() {
    	if (this.chkEpigastralgiaS.isChecked()) {
    		this.chkEpigastralgiaN.setChecked(false);
    		this.chkEpigastralgiaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEpigastralgiaN]")
    public void chkEpigastralgiaN_onCheck() {
    	if (this.chkEpigastralgiaN.isChecked()) {
    		this.chkEpigastralgiaS.setChecked(false);
    		this.chkEpigastralgiaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEpigastralgiaD]")
    public void chkEpigastralgiaD_onCheck() {
    	if (this.chkEpigastralgiaD.isChecked()) {
    		this.chkEpigastralgiaS.setChecked(false);
    		this.chkEpigastralgiaN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkIntoleranciaViaOralS]")
    public void chkIntoleranciaViaOralS_onCheck() {
    	if (this.chkIntoleranciaViaOralS.isChecked()) {
    		this.chkIntoleranciaViaOralN.setChecked(false);
    		this.chkIntoleranciaViaOralD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkIntoleranciaViaOralN]")
    public void chkIntoleranciaViaOralN_onCheck() {
    	if (this.chkIntoleranciaViaOralN.isChecked()) {
    		this.chkIntoleranciaViaOralS.setChecked(false);
    		this.chkIntoleranciaViaOralD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkIntoleranciaViaOralD]")
    public void chkIntoleranciaViaOralD_onCheck() {
    	if (this.chkIntoleranciaViaOralD.isChecked()) {
    		this.chkIntoleranciaViaOralS.setChecked(false);
    		this.chkIntoleranciaViaOralN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkDistensionAbdominalS]")
    public void chkDistensionAbdominalS_onCheck() {
    	if (this.chkDistensionAbdominalS.isChecked()) {
    		this.chkDistensionAbdominalN.setChecked(false);
    		this.chkDistensionAbdominalD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDistensionAbdominalN]")
    public void chkDistensionAbdominalN_onCheck() {
    	if (this.chkDistensionAbdominalN.isChecked()) {
    		this.chkDistensionAbdominalS.setChecked(false);
    		this.chkDistensionAbdominalD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDistensionAbdominalD]")
    public void chkDistensionAbdominalD_onCheck() {
    	if (this.chkDistensionAbdominalD.isChecked()) {
    		this.chkDistensionAbdominalS.setChecked(false);
    		this.chkDistensionAbdominalN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkHepatomegaliaS]")
    public void chkHepatomegaliaS_onCheck() {
    	if (this.chkHepatomegaliaS.isChecked()) {
    		this.chkHepatomegaliaN.setChecked(false);
    		this.txtHepatomegalia.setDisabled(false);
    	} else {
    		this.txtHepatomegalia.setDisabled(true);
    		this.txtHepatomegalia.setValue("");
    	}
    }
    @Listen("onCheck=[id$=chkHepatomegaliaN]")
    public void chkHepatomegaliaN_onCheck() {
    	if (this.chkHepatomegaliaN.isChecked()) {
    		this.chkHepatomegaliaS.setChecked(false);
    		this.txtHepatomegalia.setDisabled(true);
    		this.txtHepatomegalia.setValue("");
    	}
    }
    
    /**
     * Manejador del evento check para Osteomuscular
     */
    @Listen("onCheck=[id$=chkArtralgiaS]")
    public void chkArtralgiaS_onCheck() {
    	if (this.chkArtralgiaS.isChecked()) {
    		this.chkArtralgiaN.setChecked(false);
    		this.chkArtralgiaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkArtralgiaN]")
    public void chkArtralgiaN_onCheck() {
    	if (this.chkArtralgiaN.isChecked()) {
    		this.chkArtralgiaS.setChecked(false);
    		this.chkArtralgiaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkArtralgiaD]")
    public void chkArtralgiaD_onCheck() {
    	if (this.chkArtralgiaD.isChecked()) {
    		this.chkArtralgiaS.setChecked(false);
    		this.chkArtralgiaN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkMialgiaS]")
    public void chkMialgiaS_onCheck() {
    	if (this.chkMialgiaS.isChecked()) {
    		this.chkMialgiaN.setChecked(false);
    		this.chkMialgiaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkMialgiaN]")
    public void chkMialgiaN_onCheck() {
    	if (this.chkMialgiaN.isChecked()) {
    		this.chkMialgiaS.setChecked(false);
    		this.chkMialgiaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkMialgiaD]")
    public void chkMialgiaD_onCheck() {
    	if (this.chkMialgiaD.isChecked()) {
    		this.chkMialgiaS.setChecked(false);
    		this.chkMialgiaN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkLumbalgiaS]")
    public void chkLumbalgiaS_onCheck() {
    	if (this.chkLumbalgiaS.isChecked()) {
    		this.chkLumbalgiaN.setChecked(false);
    		this.chkLumbalgiaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkLumbalgiaN]")
    public void chkLumbalgiaN_onCheck() {
    	if (this.chkLumbalgiaN.isChecked()) {
    		this.chkLumbalgiaS.setChecked(false);
    		this.chkLumbalgiaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkLumbalgiaD]")
    public void chkLumbalgiaD_onCheck() {
    	if (this.chkLumbalgiaD.isChecked()) {
    		this.chkLumbalgiaS.setChecked(false);
    		this.chkLumbalgiaN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkDolorCuelloS]")
    public void chkDolorCuelloS_onCheck() {
    	if (this.chkDolorCuelloS.isChecked()) {
    		this.chkDolorCuelloN.setChecked(false);
    		this.chkDolorCuelloD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDolorCuelloN]")
    public void chkDolorCuelloN_onCheck() {
    	if (this.chkDolorCuelloN.isChecked()) {
    		this.chkDolorCuelloS.setChecked(false);
    		this.chkDolorCuelloD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDolorCuelloD]")
    public void chkDolorCuelloD_onCheck() {
    	if (this.chkDolorCuelloD.isChecked()) {
    		this.chkDolorCuelloS.setChecked(false);
    		this.chkDolorCuelloN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkTenosinovitisS]")
    public void chkTenosinovitisS_onCheck() {
    	if (this.chkTenosinovitisS.isChecked()) {
    		this.chkTenosinovitisN.setChecked(false);
    		this.chkTenosinovitisD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkTenosinovitisN]")
    public void chkTenosinovitisN_onCheck() {
    	if (this.chkTenosinovitisN.isChecked()) {
    		this.chkTenosinovitisS.setChecked(false);
    		this.chkTenosinovitisD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkTenosinovitisD]")
    public void chkTenosinovitisD_onCheck() {
    	if (this.chkTenosinovitisD.isChecked()) {
    		this.chkTenosinovitisS.setChecked(false);
    		this.chkTenosinovitisN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkArtralgiaProximalS]")
    public void chkArtralgiaProximalS_onCheck() {
    	if (this.chkArtralgiaProximalS.isChecked()) {
    		this.chkArtralgiaProximalN.setChecked(false);
    		this.chkArtralgiaProximalD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkArtralgiaProximalN]")
    public void chkArtralgiaProximalN_onCheck() {
    	if (this.chkArtralgiaProximalN.isChecked()) {
    		this.chkArtralgiaProximalS.setChecked(false);
    		this.chkArtralgiaProximalD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkArtralgiaProximalD]")
    public void chkArtralgiaProximalD_onCheck() {
    	if (this.chkArtralgiaProximalD.isChecked()) {
    		this.chkArtralgiaProximalS.setChecked(false);
    		this.chkArtralgiaProximalN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkArtralgiaDistalS]")
    public void chkArtralgiaDistalS_onCheck() {
    	if (this.chkArtralgiaDistalS.isChecked()) {
    		this.chkArtralgiaDistalN.setChecked(false);
    		this.chkArtralgiaDistalD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkArtralgiaDistalN]")
    public void chkArtralgiaDistalN_onCheck() {
    	if (this.chkArtralgiaDistalN.isChecked()) {
    		this.chkArtralgiaDistalS.setChecked(false);
    		this.chkArtralgiaDistalD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkArtralgiaDistalD]")
    public void chkArtralgiaDistalD_onCheck() {
    	if (this.chkArtralgiaDistalD.isChecked()) {
    		this.chkArtralgiaDistalS.setChecked(false);
    		this.chkArtralgiaDistalN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkConjuntivitisS]")
    public void chkConjuntivitisS_onCheck() {
    	if (this.chkConjuntivitisS.isChecked()) {
    		this.chkConjuntivitisN.setChecked(false);
    		this.chkConjuntivitisD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkConjuntivitisN]")
    public void chkConjuntivitisN_onCheck() {
    	if (this.chkConjuntivitisN.isChecked()) {
    		this.chkConjuntivitisS.setChecked(false);
    		this.chkConjuntivitisD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkConjuntivitisD]")
    public void chkConjuntivitisD_onCheck() {
    	if (this.chkConjuntivitisD.isChecked()) {
    		this.chkConjuntivitisS.setChecked(false);
    		this.chkConjuntivitisN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkEdemaMunecasS]")
    public void chkEdemaMunecasS_onCheck() {
    	if (this.chkEdemaMunecasS.isChecked()) {
    		this.chkEdemaMunecasN.setChecked(false);
    		this.chkEdemaMunecasD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEdemaMunecasN]")
    public void chkEdemaMunecasN_onCheck() {
    	if (this.chkEdemaMunecasN.isChecked()) {
    		this.chkEdemaMunecasS.setChecked(false);
    		this.chkEdemaMunecasD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEdemaMunecasD]")
    public void chkEdemaMunecasD_onCheck() {
    	if (this.chkEdemaMunecasD.isChecked()) {
    		this.chkEdemaMunecasS.setChecked(false);
    		this.chkEdemaMunecasN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkEdemaCodosS]")
    public void chkEdemaCodosS_onCheck() {
    	if (this.chkEdemaCodosS.isChecked()) {
    		this.chkEdemaCodosN.setChecked(false);
    		this.chkEdemaCodosD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEdemaCodosN]")
    public void chkEdemaCodosN_onCheck() {
    	if (this.chkEdemaCodosN.isChecked()) {
    		this.chkEdemaCodosS.setChecked(false);
    		this.chkEdemaCodosD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEdemaCodosD]")
    public void chkEdemaCodosD_onCheck() {
    	if (this.chkEdemaCodosD.isChecked()) {
    		this.chkEdemaCodosS.setChecked(false);
    		this.chkEdemaCodosN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkEdemaHombrosS]")
    public void chkEdemaHombrosS_onCheck() {
    	if (this.chkEdemaHombrosS.isChecked()) {
    		this.chkEdemaHombrosN.setChecked(false);
    		this.chkEdemaHombrosD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEdemaHombrosN]")
    public void chkEdemaHombrosN_onCheck() {
    	if (this.chkEdemaHombrosN.isChecked()) {
    		this.chkEdemaHombrosS.setChecked(false);
    		this.chkEdemaHombrosD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEdemaHombrosD]")
    public void chkEdemaHombrosD_onCheck() {
    	if (this.chkEdemaHombrosD.isChecked()) {
    		this.chkEdemaHombrosS.setChecked(false);
    		this.chkEdemaHombrosN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkEdemaRodillasS]")
    public void chkEdemaRodillasS_onCheck() {
    	if (this.chkEdemaRodillasS.isChecked()) {
    		this.chkEdemaRodillasN.setChecked(false);
    		this.chkEdemaRodillasD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEdemaRodillasN]")
    public void chkEdemaRodillasN_onCheck() {
    	if (this.chkEdemaRodillasN.isChecked()) {
    		this.chkEdemaRodillasS.setChecked(false);
    		this.chkEdemaRodillasD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEdemaRodillasD]")
    public void chkEdemaRodillasD_onCheck() {
    	if (this.chkEdemaRodillasD.isChecked()) {
    		this.chkEdemaRodillasS.setChecked(false);
    		this.chkEdemaRodillasN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkEdemaTobillosS]")
    public void chkEdemaTobillosS_onCheck() {
    	if (this.chkEdemaTobillosS.isChecked()) {
    		this.chkEdemaTobillosN.setChecked(false);
    		this.chkEdemaTobillosD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEdemaTobillosN]")
    public void chkEdemaTobillosN_onCheck() {
    	if (this.chkEdemaTobillosN.isChecked()) {
    		this.chkEdemaTobillosS.setChecked(false);
    		this.chkEdemaTobillosD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEdemaTobillosD]")
    public void chkEdemaTobillosD_onCheck() {
    	if (this.chkEdemaTobillosD.isChecked()) {
    		this.chkEdemaTobillosS.setChecked(false);
    		this.chkEdemaTobillosN.setChecked(false);
    	}
    }
    
    /**
     * Manejador del evento check para Cabeza
     */
    @Listen("onCheck=[id$=chkCefaleaS]")
    public void chkCefaleaS_onCheck() {
    	if (this.chkCefaleaS.isChecked()) {
    		this.chkCefaleaN.setChecked(false);
    		this.chkCefaleaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkCefaleaN]")
    public void chkCefaleaN_onCheck() {
    	if (this.chkCefaleaN.isChecked()) {
    		this.chkCefaleaS.setChecked(false);
    		this.chkCefaleaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkCefaleaD]")
    public void chkCefaleaD_onCheck() {
    	if (this.chkCefaleaD.isChecked()) {
    		this.chkCefaleaS.setChecked(false);
    		this.chkCefaleaN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkRigidezCuelloS]")
    public void chkRigidezCuelloS_onCheck() {
    	if (this.chkRigidezCuelloS.isChecked()) {
    		this.chkRigidezCuelloN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkRigidezCuelloN]")
    public void chkRigidezCuelloN_onCheck() {
    	if (this.chkRigidezCuelloN.isChecked()) {
    		this.chkRigidezCuelloS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkInyecciónConjuntivalS]")
    public void chkInyecciónConjuntivalS_onCheck() {
    	if (this.chkInyecciónConjuntivalS.isChecked()) {
    		this.chkInyecciónConjuntivalN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkInyecciónConjuntivalN]")
    public void chkInyecciónConjuntivalN_onCheck() {
    	if (this.chkInyecciónConjuntivalN.isChecked()) {
    		this.chkInyecciónConjuntivalS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkHemorragiaSubconjuntivalS]")
    public void chkHemorragiaSubconjuntivalS_onCheck() {
    	if (this.chkHemorragiaSubconjuntivalS.isChecked()) {
    		this.chkHemorragiaSubconjuntivalN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkHemorragiaSubconjuntivalN]")
    public void chkHemorragiaSubconjuntivalN_onCheck() {
    	if (this.chkHemorragiaSubconjuntivalN.isChecked()) {
    		this.chkHemorragiaSubconjuntivalS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkDolorRetroocularS]")
    public void chkDolorRetroocularS_onCheck() {
    	if (this.chkDolorRetroocularS.isChecked()) {
    		this.chkDolorRetroocularN.setChecked(false);
    		this.chkDolorRetroocularD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDolorRetroocularN]")
    public void chkDolorRetroocularN_onCheck() {
    	if (this.chkDolorRetroocularN.isChecked()) {
    		this.chkDolorRetroocularS.setChecked(false);
    		this.chkDolorRetroocularD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDolorRetroocularD]")
    public void chkDolorRetroocularD_onCheck() {
    	if (this.chkDolorRetroocularD.isChecked()) {
    		this.chkDolorRetroocularS.setChecked(false);
    		this.chkDolorRetroocularN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkFontanelaAbombadaS]")
    public void chkFontanelaAbombadaS_onCheck() {
    	if (this.chkFontanelaAbombadaS.isChecked()) {
    		this.chkFontanelaAbombadaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkFontanelaAbombadaN]")
    public void chkFontanelaAbombadaN_onCheck() {
    	if (this.chkFontanelaAbombadaN.isChecked()) {
    		this.chkFontanelaAbombadaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkIctericiaConjuntivalS]")
    public void chkIctericiaConjuntivalS_onCheck() {
    	if (this.chkIctericiaConjuntivalS.isChecked()) {
    		this.chkIctericiaConjuntivalN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkIctericiaConjuntivalN]")
    public void chkIctericiaConjuntivalN_onCheck() {
    	if (this.chkIctericiaConjuntivalN.isChecked()) {
    		this.chkIctericiaConjuntivalS.setChecked(false);
    	}
    }
    
    /**
     * Manejador del evento check para Deshidratación
     */
    @Listen("onCheck=[id$=chkLenguaMucosaSecasS]")
    public void chkLenguaMucosaSecasS_onCheck() {
    	if (this.chkLenguaMucosaSecasS.isChecked()) {
    		this.chkLenguaMucosaSecasN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkLenguaMucosaSecasN]")
    public void chkLenguaMucosaSecasN_onCheck() {
    	if (this.chkLenguaMucosaSecasN.isChecked()) {
    		this.chkLenguaMucosaSecasS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkPliegueCutaneoS]")
    public void chkPliegueCutaneoS_onCheck() {
    	if (this.chkPliegueCutaneoS.isChecked()) {
    		this.chkPliegueCutaneoN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkPliegueCutaneoN]")
    public void chkPliegueCutaneoN_onCheck() {
    	if (this.chkPliegueCutaneoN.isChecked()) {
    		this.chkPliegueCutaneoS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkOrinaReducidaS]")
    public void chkOrinaReducidaS_onCheck() {
    	if (this.chkOrinaReducidaS.isChecked()) {
    		this.chkOrinaReducidaN.setChecked(false);
    		this.chkOrinaReducidaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkOrinaReducidaN]")
    public void chkOrinaReducidaN_onCheck() {
    	if (this.chkOrinaReducidaN.isChecked()) {
    		this.chkOrinaReducidaS.setChecked(false);
    		this.chkOrinaReducidaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkOrinaReducidaD]")
    public void chkOrinaReducidaD_onCheck() {
    	if (this.chkOrinaReducidaD.isChecked()) {
    		this.chkOrinaReducidaS.setChecked(false);
    		this.chkOrinaReducidaN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkBebeAvidoSedS]")
    public void chkBebeAvidoSedS_onCheck() {
    	if (this.chkBebeAvidoSedS.isChecked()) {
    		this.chkBebeAvidoSedN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkBebeAvidoSedN]")
    public void chkBebeAvidoSedN_onCheck() {
    	if (this.chkBebeAvidoSedN.isChecked()) {
    		this.chkBebeAvidoSedS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkOjosHundidosS]")
    public void chkOjosHundidosS_onCheck() {
    	if (this.chkOjosHundidosS.isChecked()) {
    		this.chkOjosHundidosN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkOjosHundidosN]")
    public void chkOjosHundidosN_onCheck() {
    	if (this.chkOjosHundidosN.isChecked()) {
    		this.chkOjosHundidosS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkFontanelaHundidaS]")
    public void chkFontanelaHundidaS_onCheck() {
    	if (this.chkFontanelaHundidaS.isChecked()) {
    		this.chkFontanelaHundidaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkFontanelaHundidaN]")
    public void chkFontanelaHundidaN_onCheck() {
    	if (this.chkFontanelaHundidaN.isChecked()) {
    		this.chkFontanelaHundidaS.setChecked(false);
    	}
    }
    
    /**
     * Manejador del evento check para Cutáneo
     */
    @Listen("onCheck=[id$=chkRashLocalizadoS]")
    public void chkRashLocalizadoS_onCheck() {
    	if (this.chkRashLocalizadoS.isChecked()) {
    		this.chkRashLocalizadoN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkRashLocalizadoN]")
    public void chkRashLocalizadoN_onCheck() {
    	if (this.chkRashLocalizadoN.isChecked()) {
    		this.chkRashLocalizadoS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkRashGeneralizadoS]")
    public void chkRashGeneralizadoS_onCheck() {
    	if (this.chkRashGeneralizadoS.isChecked()) {
    		this.chkRashGeneralizadoN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkRashGeneralizadoN]")
    public void chkRashGeneralizadoN_onCheck() {
    	if (this.chkRashGeneralizadoN.isChecked()) {
    		this.chkRashGeneralizadoS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkRashEritematosoS]")
    public void chkRashEritematosoS_onCheck() {
    	if (this.chkRashEritematosoS.isChecked()) {
    		this.chkRashEritematosoN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkRashEritematosoN]")
    public void chkRashEritematosoN_onCheck() {
    	if (this.chkRashEritematosoN.isChecked()) {
    		this.chkRashEritematosoS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkRashMacularS]")
    public void chkRashMacularS_onCheck() {
    	if (this.chkRashMacularS.isChecked()) {
    		this.chkRashMacularN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkRashMacularN]")
    public void chkRashMacularN_onCheck() {
    	if (this.chkRashMacularN.isChecked()) {
    		this.chkRashMacularS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkRashPapularS]")
    public void chkRashPapularS_onCheck() {
    	if (this.chkRashPapularS.isChecked()) {
    		this.chkRashPapularN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkRashPapularN]")
    public void chkRashPapularN_onCheck() {
    	if (this.chkRashPapularN.isChecked()) {
    		this.chkRashPapularS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkPielMoteadaS]")
    public void chkPielMoteadaS_onCheck() {
    	if (this.chkPielMoteadaS.isChecked()) {
    		this.chkPielMoteadaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkPielMoteadaN]")
    public void chkPielMoteadaN_onCheck() {
    	if (this.chkPielMoteadaN.isChecked()) {
    		this.chkPielMoteadaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkRuborFacialS]")
    public void chkRuborFacialS_onCheck() {
    	if (this.chkRuborFacialS.isChecked()) {
    		this.chkRuborFacialN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkRuborFacialN]")
    public void chkRuborFacialN_onCheck() {
    	if (this.chkRuborFacialN.isChecked()) {
    		this.chkRuborFacialS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkEquimosisS]")
    public void chkEquimosisS_onCheck() {
    	if (this.chkEquimosisS.isChecked()) {
    		this.chkEquimosisN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEquimosisN]")
    public void chkEquimosisN_onCheck() {
    	if (this.chkEquimosisN.isChecked()) {
    		this.chkEquimosisS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkCianosisCentralS]")
    public void chkCianosisCentralS_onCheck() {
    	if (this.chkCianosisCentralS.isChecked()) {
    		this.chkCianosisCentralN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkCianosisCentralN]")
    public void chkCianosisCentralN_onCheck() {
    	if (this.chkCianosisCentralN.isChecked()) {
    		this.chkCianosisCentralS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkIctericiaS]")
    public void chkIctericiaS_onCheck() {
    	if (this.chkIctericiaS.isChecked()) {
    		this.chkIctericiaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkIctericiaN]")
    public void chkIctericiaN_onCheck() {
    	if (this.chkIctericiaN.isChecked()) {
    		this.chkIctericiaS.setChecked(false);
    	}
    }
    
    /**
     * Manejador del evento check para Garganta
     */
    @Listen("onCheck=[id$=chkEritemaS]")
    public void chkEritemaS_onCheck() {
    	if (this.chkEritemaS.isChecked()) {
    		this.chkEritemaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEritemaN]")
    public void chkEritemaN_onCheck() {
    	if (this.chkEritemaN.isChecked()) {
    		this.chkEritemaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkDolorGargantaS]")
    public void chkDolorGargantaS_onCheck() {
    	if (this.chkDolorGargantaS.isChecked()) {
    		this.chkDolorGargantaN.setChecked(false);
    		this.chkDolorGargantaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDolorGargantaN]")
    public void chkDolorGargantaN_onCheck() {
    	if (this.chkDolorGargantaN.isChecked()) {
    		this.chkDolorGargantaS.setChecked(false);
    		this.chkDolorGargantaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDolorGargantaD]")
    public void chkDolorGargantaD_onCheck() {
    	if (this.chkDolorGargantaD.isChecked()) {
    		this.chkDolorGargantaS.setChecked(false);
    		this.chkDolorGargantaN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkAdenopatiasCervicalesS]")
    public void chkAdenopatiasCervicalesS_onCheck() {
    	if (this.chkAdenopatiasCervicalesS.isChecked()) {
    		this.chkAdenopatiasCervicalesN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkAdenopatiasCervicalesN]")
    public void chkAdenopatiasCervicalesN_onCheck() {
    	if (this.chkAdenopatiasCervicalesN.isChecked()) {
    		this.chkAdenopatiasCervicalesS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkExudadoS]")
    public void chkExudadoS_onCheck() {
    	if (this.chkExudadoS.isChecked()) {
    		this.chkExudadoN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkExudadoN]")
    public void chkExudadoN_onCheck() {
    	if (this.chkExudadoN.isChecked()) {
    		this.chkExudadoS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkPetequiasMucosaS]")
    public void chkPetequiasMucosaS_onCheck() {
    	if (this.chkPetequiasMucosaS.isChecked()) {
    		this.chkPetequiasMucosaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkPetequiasMucosaN]")
    public void chkPetequiasMucosaN_onCheck() {
    	if (this.chkPetequiasMucosaN.isChecked()) {
    		this.chkPetequiasMucosaS.setChecked(false);
    	}
    }
    
    /**
     * Manejador del evento check para Renal
     */
    @Listen("onCheck=[id$=chkSintomasUrinariosS]")
    public void chkSintomasUrinariosS_onCheck() {
    	if (this.chkSintomasUrinariosS.isChecked()) {
    		this.chkSintomasUrinariosN.setChecked(false);
    		this.chkSintomasUrinariosD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkSintomasUrinariosN]")
    public void chkSintomasUrinariosN_onCheck() {
    	if (this.chkSintomasUrinariosN.isChecked()) {
    		this.chkSintomasUrinariosS.setChecked(false);
    		this.chkSintomasUrinariosD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkSintomasUrinariosD]")
    public void chkSintomasUrinariosD_onCheck() {
    	if (this.chkSintomasUrinariosD.isChecked()) {
    		this.chkSintomasUrinariosS.setChecked(false);
    		this.chkSintomasUrinariosN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkLeucocituriaXCampoS]")
    public void chkLeucocituriaXCampoS_onCheck() {
    	if (this.chkLeucocituriaXCampoS.isChecked()) {
    		this.chkLeucocituriaXCampoN.setChecked(false);
    		this.chkLeucocituriaXCampoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkLeucocituriaXCampoN]")
    public void chkLeucocituriaXCampoN_onCheck() {
    	if (this.chkLeucocituriaXCampoN.isChecked()) {
    		this.chkLeucocituriaXCampoS.setChecked(false);
    		this.chkLeucocituriaXCampoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkLeucocituriaXCampoD]")
    public void chkLeucocituriaXCampoD_onCheck() {
    	if (this.chkLeucocituriaXCampoD.isChecked()) {
    		this.chkLeucocituriaXCampoS.setChecked(false);
    		this.chkLeucocituriaXCampoN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkNitritosS]")
    public void chkNitritosS_onCheck() {
    	if (this.chkNitritosS.isChecked()) {
    		this.chkNitritosN.setChecked(false);
    		this.chkNitritosD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkNitritosN]")
    public void chkNitritosN_onCheck() {
    	if (this.chkNitritosN.isChecked()) {
    		this.chkNitritosS.setChecked(false);
    		this.chkNitritosD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkNitritosD]")
    public void chkNitritosD_onCheck() {
    	if (this.chkNitritosD.isChecked()) {
    		this.chkNitritosS.setChecked(false);
    		this.chkNitritosN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkEritrocitosXCampoS]")
    public void chkEritrocitosXCampoS_onCheck() {
    	if (this.chkEritrocitosXCampoS.isChecked()) {
    		this.chkEritrocitosXCampoN.setChecked(false);
    		this.chkEritrocitosXCampoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEritrocitosXCampoN]")
    public void chkEritrocitosXCampoN_onCheck() {
    	if (this.chkEritrocitosXCampoN.isChecked()) {
    		this.chkEritrocitosXCampoS.setChecked(false);
    		this.chkEritrocitosXCampoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEritrocitosXCampoD]")
    public void chkEritrocitosXCampoD_onCheck() {
    	if (this.chkEritrocitosXCampoD.isChecked()) {
    		this.chkEritrocitosXCampoS.setChecked(false);
    		this.chkEritrocitosXCampoN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkBilirrubinuriaS]")
    public void chkBilirrubinuriaS_onCheck() {
    	if (this.chkBilirrubinuriaS.isChecked()) {
    		this.chkBilirrubinuriaN.setChecked(false);
    		this.chkBilirrubinuriaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkBilirrubinuriaN]")
    public void chkBilirrubinuriaN_onCheck() {
    	if (this.chkBilirrubinuriaN.isChecked()) {
    		this.chkBilirrubinuriaS.setChecked(false);
    		this.chkBilirrubinuriaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkBilirrubinuriaD]")
    public void chkBilirrubinuriaD_onCheck() {
    	if (this.chkBilirrubinuriaD.isChecked()) {
    		this.chkBilirrubinuriaS.setChecked(false);
    		this.chkBilirrubinuriaN.setChecked(false);
    	}
    }
    
    /**
     * Manejador del evento check para Estado Nutricional
     */
    @Listen("onCheck=[id$=chkObesoS]")
    public void chkObesoS_onCheck() {
    	if (this.chkObesoS.isChecked()) {
    		this.chkObesoN.setChecked(false);
    		this.chkObesoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkObesoN]")
    public void chkObesoN_onCheck() {
    	if (this.chkObesoN.isChecked()) {
    		this.chkObesoS.setChecked(false);
    		this.chkObesoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkObesoD]")
    public void chkObesoD_onCheck() {
    	if (this.chkObesoD.isChecked()) {
    		this.chkObesoS.setChecked(false);
    		this.chkObesoN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkSobrepesoS]")
    public void chkSobrepesoS_onCheck() {
    	if (this.chkSobrepesoS.isChecked()) {
    		this.chkSobrepesoN.setChecked(false);
    		this.chkSobrepesoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkSobrepesoN]")
    public void chkSobrepesoN_onCheck() {
    	if (this.chkSobrepesoN.isChecked()) {
    		this.chkSobrepesoS.setChecked(false);
    		this.chkSobrepesoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkSobrepesoD]")
    public void chkSobrepesoD_onCheck() {
    	if (this.chkSobrepesoD.isChecked()) {
    		this.chkSobrepesoS.setChecked(false);
    		this.chkSobrepesoN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkSospechaProblemaS]")
    public void chkSospechaProblemaS_onCheck() {
    	if (this.chkSospechaProblemaS.isChecked()) {
    		this.chkSospechaProblemaN.setChecked(false);
    		this.chkSospechaProblemaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkSospechaProblemaN]")
    public void chkSospechaProblemaN_onCheck() {
    	if (this.chkSospechaProblemaN.isChecked()) {
    		this.chkSospechaProblemaS.setChecked(false);
    		this.chkSospechaProblemaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkSospechaProblemaD]")
    public void chkSospechaProblemaD_onCheck() {
    	if (this.chkSospechaProblemaD.isChecked()) {
    		this.chkSospechaProblemaS.setChecked(false);
    		this.chkSospechaProblemaN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkNormalS]")
    public void chkNormalS_onCheck() {
    	if (this.chkNormalS.isChecked()) {
    		this.chkNormalN.setChecked(false);
    		this.chkNormalD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkNormalN]")
    public void chkNormalN_onCheck() {
    	if (this.chkNormalN.isChecked()) {
    		this.chkNormalS.setChecked(false);
    		this.chkNormalD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkNormalD]")
    public void chkNormalD_onCheck() {
    	if (this.chkNormalD.isChecked()) {
    		this.chkNormalS.setChecked(false);
    		this.chkNormalN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkBajoPesoS]")
    public void chkBajoPesoS_onCheck() {
    	if (this.chkBajoPesoS.isChecked()) {
    		this.chkBajoPesoN.setChecked(false);
    		this.chkBajoPesoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkBajoPesoN]")
    public void chkBajoPesoN_onCheck() {
    	if (this.chkBajoPesoN.isChecked()) {
    		this.chkBajoPesoS.setChecked(false);
    		this.chkBajoPesoD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkBajoPesoD]")
    public void chkBajoPesoD_onCheck() {
    	if (this.chkBajoPesoD.isChecked()) {
    		this.chkBajoPesoS.setChecked(false);
    		this.chkBajoPesoN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkBajoPesoSeveroS]")
    public void chkBajoPesoSeveroS_onCheck() {
    	if (this.chkBajoPesoSeveroS.isChecked()) {
    		this.chkBajoPesoSeveroN.setChecked(false);
    		this.chkBajoPesoSeveroD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkBajoPesoSeveroN]")
    public void chkBajoPesoSeveroN_onCheck() {
    	if (this.chkBajoPesoSeveroN.isChecked()) {
    		this.chkBajoPesoSeveroS.setChecked(false);
    		this.chkBajoPesoSeveroD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkBajoPesoSeveroD]")
    public void chkBajoPesoSeveroD_onCheck() {
    	if (this.chkBajoPesoSeveroD.isChecked()) {
    		this.chkBajoPesoSeveroS.setChecked(false);
    		this.chkBajoPesoSeveroN.setChecked(false);
    	}
    }
    
    /**
     * Manejador del evento check para Respiratorio
     */
    @Listen("onCheck=[id$=chkTosS]")
    public void chkTosS_onCheck() {
    	if (this.chkTosS.isChecked()) {
    		this.chkTosN.setChecked(false);
    		this.chkTosD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkTosN]")
    public void chkTosN_onCheck() {
    	if (this.chkTosN.isChecked()) {
    		this.chkTosS.setChecked(false);
    		this.chkTosD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkTosD]")
    public void chkTosD_onCheck() {
    	if (this.chkTosD.isChecked()) {
    		this.chkTosS.setChecked(false);
    		this.chkTosN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkRinorreaS]")
    public void chkRinorreaS_onCheck() {
    	if (this.chkRinorreaS.isChecked()) {
    		this.chkRinorreaN.setChecked(false);
    		this.chkRinorreaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkRinorreaN]")
    public void chkRinorreaN_onCheck() {
    	if (this.chkRinorreaN.isChecked()) {
    		this.chkRinorreaS.setChecked(false);
    		this.chkRinorreaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkRinorreaD]")
    public void chkRinorreaD_onCheck() {
    	if (this.chkRinorreaD.isChecked()) {
    		this.chkRinorreaS.setChecked(false);
    		this.chkRinorreaN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkCongestionNasalS]")
    public void chkCongestionNasalS_onCheck() {
    	if (this.chkCongestionNasalS.isChecked()) {
    		this.chkCongestionNasalN.setChecked(false);
    		this.chkCongestionNasalD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkCongestionNasalN]")
    public void chkCongestionNasalN_onCheck() {
    	if (this.chkCongestionNasalN.isChecked()) {
    		this.chkCongestionNasalS.setChecked(false);
    		this.chkCongestionNasalD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkCongestionNasalD]")
    public void chkCongestionNasalD_onCheck() {
    	if (this.chkCongestionNasalD.isChecked()) {
    		this.chkCongestionNasalS.setChecked(false);
    		this.chkCongestionNasalN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkOtalgiaS]")
    public void chkOtalgiaS_onCheck() {
    	if (this.chkOtalgiaS.isChecked()) {
    		this.chkOtalgiaN.setChecked(false);
    		this.chkOtalgiaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkOtalgiaN]")
    public void chkOtalgiaN_onCheck() {
    	if (this.chkOtalgiaN.isChecked()) {
    		this.chkOtalgiaS.setChecked(false);
    		this.chkOtalgiaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkOtalgiaD]")
    public void chkOtalgiaD_onCheck() {
    	if (this.chkOtalgiaD.isChecked()) {
    		this.chkOtalgiaS.setChecked(false);
    		this.chkOtalgiaN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkAleteoNasalS]")
    public void chkAleteoNasalS_onCheck() {
    	if (this.chkAleteoNasalS.isChecked()) {
    		this.chkAleteoNasalN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkAleteoNasalN]")
    public void chkAleteoNasalN_onCheck() {
    	if (this.chkAleteoNasalN.isChecked()) {
    		this.chkAleteoNasalS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkApneaS]")
    public void chkApneaS_onCheck() {
    	if (this.chkApneaS.isChecked()) {
    		this.chkApneaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkApneaN]")
    public void chkApneaN_onCheck() {
    	if (this.chkApneaN.isChecked()) {
    		this.chkApneaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkRespiracionRapidaS]")
    public void chkRespiracionRapidaS_onCheck() {
    	if (this.chkRespiracionRapidaS.isChecked()) {
    		this.chkRespiracionRapidaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkRespiracionRapidaN]")
    public void chkRespiracionRapidaN_onCheck() {
    	if (this.chkRespiracionRapidaN.isChecked()) {
    		this.chkRespiracionRapidaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkQuejidoEspiratorioS]")
    public void chkQuejidoEspiratorioS_onCheck() {
    	if (this.chkQuejidoEspiratorioS.isChecked()) {
    		this.chkQuejidoEspiratorioN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkQuejidoEspiratorioN]")
    public void chkQuejidoEspiratorioN_onCheck() {
    	if (this.chkQuejidoEspiratorioN.isChecked()) {
    		this.chkQuejidoEspiratorioS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkEstridorReposoS]")
    public void chkEstridorReposoS_onCheck() {
    	if (this.chkEstridorReposoS.isChecked()) {
    		this.chkEstridorReposoN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEstridorReposoN]")
    public void chkEstridorReposoN_onCheck() {
    	if (this.chkEstridorReposoN.isChecked()) {
    		this.chkEstridorReposoS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkTirajeSubcostalS]")
    public void chkTirajeSubcostalS_onCheck() {
    	if (this.chkTirajeSubcostalS.isChecked()) {
    		this.chkTirajeSubcostalN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkTirajeSubcostalN]")
    public void chkTirajeSubcostalN_onCheck() {
    	if (this.chkTirajeSubcostalN.isChecked()) {
    		this.chkTirajeSubcostalS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkSibilanciasS]")
    public void chkSibilanciasS_onCheck() {
    	if (this.chkSibilanciasS.isChecked()) {
    		this.chkSibilanciasN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkSibilanciasN]")
    public void chkSibilanciasN_onCheck() {
    	if (this.chkSibilanciasN.isChecked()) {
    		this.chkSibilanciasS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkCrepitosS]")
    public void chkCrepitosS_onCheck() {
    	if (this.chkCrepitosS.isChecked()) {
    		this.chkCrepitosN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkCrepitosN]")
    public void chkCrepitosN_onCheck() {
    	if (this.chkCrepitosN.isChecked()) {
    		this.chkCrepitosS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkRoncosS]")
    public void chkRoncosS_onCheck() {
    	if (this.chkRoncosS.isChecked()) {
    		this.chkRoncosN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkRoncosN]")
    public void chkRoncosN_onCheck() {
    	if (this.chkRoncosN.isChecked()) {
    		this.chkRoncosS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkOtraFIFS]")
    public void chkOtraFIFS_onCheck() {
    	if (this.chkOtraFIFS.isChecked()) {
    		this.chkOtraFIFN.setChecked(false);
    		this.txtNuevaFif.setDisabled(false);
    	} else {
    		this.txtNuevaFif.setDisabled(true);
    		this.txtNuevaFif.setValue(null);
    	}
    }
    @Listen("onCheck=[id$=chkOtraFIFN]")
    public void chkOtraFIFN_onCheck() {
    	if (this.chkOtraFIFN.isChecked()) {
    		this.chkOtraFIFS.setChecked(false);
    		this.txtNuevaFif.setDisabled(true);
    		this.txtNuevaFif.setValue(null);
    	}
    }
    
    /**
     * Manejador del evento check para Referencia
     */
    @Listen("onCheck=[id$=chkInterconsultaPediatraS]")
    public void chkInterconsultaPediatraS_onCheck() {
    	if (this.chkInterconsultaPediatraS.isChecked()) {
    		this.chkInterconsultaPediatraN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkInterconsultaPediatraN]")
    public void chkInterconsultaPediatraN_onCheck() {
    	if (this.chkInterconsultaPediatraN.isChecked()) {
    		this.chkInterconsultaPediatraS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkReferenciaHospitalS]")
    public void chkReferenciaHospitalS_onCheck() {
    	if (this.chkReferenciaHospitalS.isChecked()) {
    		this.chkReferenciaHospitalN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkReferenciaHospitalN]")
    public void chkReferenciaHospitalN_onCheck() {
    	if (this.chkReferenciaHospitalN.isChecked()) {
    		this.chkReferenciaHospitalS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkReferenciaDengueS]")
    public void chkReferenciaDengueS_onCheck() {
    	if (this.chkReferenciaDengueS.isChecked()) {
    		this.chkReferenciaDengueN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkReferenciaDengueN]")
    public void chkReferenciaDengueN_onCheck() {
    	if (this.chkReferenciaDengueN.isChecked()) {
    		this.chkReferenciaDengueS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkReferenciaIRAGS]")
    public void chkReferenciaIRAGS_onCheck() {
    	if (this.chkReferenciaIRAGS.isChecked()) {
    		this.chkReferenciaIRAGN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkReferenciaIRAGN]")
    public void chkReferenciaIRAGN_onCheck() {
    	if (this.chkReferenciaIRAGN.isChecked()) {
    		this.chkReferenciaIRAGS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkReferenciaCHIKS]")
    public void chkReferenciaCHIKS_onCheck() {
    	if (this.chkReferenciaCHIKS.isChecked()) {
    		this.chkReferenciaCHIKN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkReferenciaCHIKN]")
    public void chkReferenciaCHIKN_onCheck() {
    	if (this.chkReferenciaCHIKN.isChecked()) {
    		this.chkReferenciaCHIKS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkETIS]")
    public void chkETIS_onCheck() {
    	if (this.chkETIS.isChecked()) {
    		this.chkETIN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkETIN]")
    public void chkETIN_onCheck() {
    	if (this.chkETIN.isChecked()) {
    		this.chkETIS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkIRAGS]")
    public void chkIRAGS_onCheck() {
    	if (this.chkIRAGS.isChecked()) {
    		this.chkIRAGN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkIRAGN]")
    public void chkIRAGN_onCheck() {
    	if (this.chkIRAGN.isChecked()) {
    		this.chkIRAGS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkNeumoniaS]")
    public void chkNeumoniaS_onCheck() {
    	if (this.chkNeumoniaS.isChecked()) {
    		this.chkNeumoniaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkNeumoniaN]")
    public void chkNeumoniaN_onCheck() {
    	if (this.chkNeumoniaN.isChecked()) {
    		this.chkNeumoniaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkCVS]")
    public void chkCVS_onCheck() {
    	if (this.chkCVS.isChecked()) {
    		this.chkCVN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkCVN]")
    public void chkCVN_onCheck() {
    	if (this.chkCVN.isChecked()) {
    		this.chkCVS.setChecked(false);
    	}
    }
    
    
    /**
     * Manejador del evento check para Vacunas
     */
    @Listen("onCheck=[id$=chkLactanciaMaternaS]")
    public void chkLactanciaMaternaS_onCheck() {
    	if (this.chkLactanciaMaternaS.isChecked()) {
    		this.chkLactanciaMaternaN.setChecked(false);
    		this.chkLactanciaMaternaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkLactanciaMaternaN]")
    public void chkLactanciaMaternaN_onCheck() {
    	if (this.chkLactanciaMaternaN.isChecked()) {
    		this.chkLactanciaMaternaS.setChecked(false);
    		this.chkLactanciaMaternaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkLactanciaMaternaD]")
    public void chkLactanciaMaternaD_onCheck() {
    	if (this.chkLactanciaMaternaD.isChecked()) {
    		this.chkLactanciaMaternaS.setChecked(false);
    		this.chkLactanciaMaternaN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkVacunasCompletasS]")
    public void chkVacunasCompletasS_onCheck() {
    	if (this.chkVacunasCompletasS.isChecked()) {
    		this.chkVacunasCompletasN.setChecked(false);
    		this.chkVacunasCompletasD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkVacunasCompletasN]")
    public void chkVacunasCompletasN_onCheck() {
    	if (this.chkVacunasCompletasN.isChecked()) {
    		this.chkVacunasCompletasS.setChecked(false);
    		this.chkVacunasCompletasD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkVacunasCompletasD]")
    public void chkVacunasCompletasD_onCheck() {
    	if (this.chkVacunasCompletasD.isChecked()) {
    		this.chkVacunasCompletasS.setChecked(false);
    		this.chkVacunasCompletasN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkVacunaInfluenzaS]")
    public void chkVacunaInfluenzaS_onCheck() {
    	if (this.chkVacunaInfluenzaS.isChecked()) {
    		this.chkVacunaInfluenzaN.setChecked(false);
    		this.chkVacunaInfluenzaD.setChecked(false);
    		this.txtFechaVacuna.setDisabled(false);
    	} else {
    		this.txtFechaVacuna.setDisabled(true);
    		this.txtFechaVacuna.setValue(null);
    	}
    }
    @Listen("onCheck=[id$=chkVacunaInfluenzaN]")
    public void chkVacunaInfluenzaN_onCheck() {
    	if (this.chkVacunaInfluenzaN.isChecked()) {
    		this.chkVacunaInfluenzaS.setChecked(false);
    		this.chkVacunaInfluenzaD.setChecked(false);
    		this.txtFechaVacuna.setDisabled(true);
    		this.txtFechaVacuna.setValue(null);
    	}
    }
    @Listen("onCheck=[id$=chkVacunaInfluenzaD]")
    public void chkVacunaInfluenzaD_onCheck() {
    	if (this.chkVacunaInfluenzaD.isChecked()) {
    		this.chkVacunaInfluenzaS.setChecked(false);
    		this.chkVacunaInfluenzaN.setChecked(false);
    		this.txtFechaVacuna.setDisabled(true);
    		this.txtFechaVacuna.setValue(null);
    	}
    }
    
    /**
     * Manejador del evento check para Categoría
     */
    @Listen("onCheck=[id$=chkManifestacionHemorragicaS]")
    public void chkManifestacionHemorragicaS_onCheck() {
    	if (this.chkManifestacionHemorragicaS.isChecked()) {
    		this.chkManifestacionHemorragicaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkManifestacionHemorragicaN]")
    public void chkManifestacionHemorragicaN_onCheck() {
    	if (this.chkManifestacionHemorragicaN.isChecked()) {
    		this.chkManifestacionHemorragicaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkPruebaTorniquetePositivaS]")
    public void chkPruebaTorniquetePositivaS_onCheck() {
    	if (this.chkPruebaTorniquetePositivaS.isChecked()) {
    		this.chkPruebaTorniquetePositivaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkPruebaTorniquetePositivaN]")
    public void chkPruebaTorniquetePositivaN_onCheck() {
    	if (this.chkPruebaTorniquetePositivaN.isChecked()) {
    		this.chkPruebaTorniquetePositivaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkPetequias10S]")
    public void chkPetequias10S_onCheck() {
    	if (this.chkPetequias10S.isChecked()) {
    		this.chkPetequias10N.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkPetequias10N]")
    public void chkPetequias10N_onCheck() {
    	if (this.chkPetequias10N.isChecked()) {
    		this.chkPetequias10S.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkPetequias20S]")
    public void chkPetequias20S_onCheck() {
    	if (this.chkPetequias20S.isChecked()) {
    		this.chkPetequias20N.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkPetequias20N]")
    public void chkPetequias20N_onCheck() {
    	if (this.chkPetequias20N.isChecked()) {
    		this.chkPetequias20S.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkPielExtremidadesFriasS]")
    public void chkPielExtremidadesFriasS_onCheck() {
    	if (this.chkPielExtremidadesFriasS.isChecked()) {
    		this.chkPielExtremidadesFriasN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkPielExtremidadesFriasN]")
    public void chkPielExtremidadesFriasN_onCheck() {
    	if (this.chkPielExtremidadesFriasN.isChecked()) {
    		this.chkPielExtremidadesFriasS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkPalidezExtremidadesS]")
    public void chkPalidezExtremidadesS_onCheck() {
    	if (this.chkPalidezExtremidadesS.isChecked()) {
    		this.chkPalidezExtremidadesN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkPalidezExtremidadesN]")
    public void chkPalidezExtremidadesN_onCheck() {
    	if (this.chkPalidezExtremidadesN.isChecked()) {
    		this.chkPalidezExtremidadesS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkEpistaxisS]")
    public void chkEpistaxisS_onCheck() {
    	if (this.chkEpistaxisS.isChecked()) {
    		this.chkEpistaxisN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEpistaxisN]")
    public void chkEpistaxisN_onCheck() {
    	if (this.chkEpistaxisN.isChecked()) {
    		this.chkEpistaxisS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkGingivorragiaS]")
    public void chkGingivorragiaS_onCheck() {
    	if (this.chkGingivorragiaS.isChecked()) {
    		this.chkGingivorragiaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkGingivorragiaN]")
    public void chkGingivorragiaN_onCheck() {
    	if (this.chkGingivorragiaN.isChecked()) {
    		this.chkGingivorragiaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkPetequiasEspontaneasS]")
    public void chkPetequiasEspontaneasS_onCheck() {
    	if (this.chkPetequiasEspontaneasS.isChecked()) {
    		this.chkPetequiasEspontaneasN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkPetequiasEspontaneasN]")
    public void chkPetequiasEspontaneasN_onCheck() {
    	if (this.chkPetequiasEspontaneasN.isChecked()) {
    		this.chkPetequiasEspontaneasS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkLlenadoCapilarS]")
    public void chkLlenadoCapilarS_onCheck() {
    	if (this.chkLlenadoCapilarS.isChecked()) {
    		this.chkLlenadoCapilarN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkLlenadoCapilarN]")
    public void chkLlenadoCapilarN_onCheck() {
    	if (this.chkLlenadoCapilarN.isChecked()) {
    		this.chkLlenadoCapilarS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkCianosisS]")
    public void chkCianosisS_onCheck() {
    	if (this.chkCianosisS.isChecked()) {
    		this.chkCianosisN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkCianosisN]")
    public void chkCianosisN_onCheck() {
    	if (this.chkCianosisN.isChecked()) {
    		this.chkCianosisS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkHipermenorreaS]")
    public void chkHipermenorreaS_onCheck() {
    	if (this.chkHipermenorreaS.isChecked()) {
    		this.chkHipermenorreaN.setChecked(false);
    		this.chkHipermenorreaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkHipermenorreaN]")
    public void chkHipermenorreaN_onCheck() {
    	if (this.chkHipermenorreaN.isChecked()) {
    		this.chkHipermenorreaS.setChecked(false);
    		this.chkHipermenorreaD.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkHipermenorreaD]")
    public void chkHipermenorreaD_onCheck() {
    	if (this.chkHipermenorreaD.isChecked()) {
    		this.chkHipermenorreaS.setChecked(false);
    		this.chkHipermenorreaN.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkHematemesisS]")
    public void chkHematemesisS_onCheck() {
    	if (this.chkHematemesisS.isChecked()) {
    		this.chkHematemesisN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkHematemesisN]")
    public void chkHematemesisN_onCheck() {
    	if (this.chkHematemesisN.isChecked()) {
    		this.chkHematemesisS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkMelenaS]")
    public void chkMelenaS_onCheck() {
    	if (this.chkMelenaS.isChecked()) {
    		this.chkMelenaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkMelenaN]")
    public void chkMelenaN_onCheck() {
    	if (this.chkMelenaN.isChecked()) {
    		this.chkMelenaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkHemoconcentracionS]")
    public void chkHemoconcentracionS_onCheck() {
    	if (this.chkHemoconcentracionS.isChecked()) {
    		this.chkHemoconcentracionN.setChecked(false);
    		this.chkHemoconcentracionD.setChecked(false);
    		this.txtHemoconcentracion.setDisabled(false);
    	} else {
    		this.txtHemoconcentracion.setDisabled(true);
    		this.txtHemoconcentracion.setValue("");
    	}
    }
    @Listen("onCheck=[id$=chkHemoconcentracionN]")
    public void chkHemoconcentracionN_onCheck() {
    	if (this.chkHemoconcentracionN.isChecked()) {
    		this.chkHemoconcentracionS.setChecked(false);
    		this.chkHemoconcentracionD.setChecked(false);
    		this.txtHemoconcentracion.setDisabled(true);
    		this.txtHemoconcentracion.setValue("");
    	}
    }
    @Listen("onCheck=[id$=chkHemoconcentracionD]")
    public void chkHemoconcentracionD_onCheck() {
    	if (this.chkHemoconcentracionD.isChecked()) {
    		this.chkHemoconcentracionS.setChecked(false);
    		this.chkHemoconcentracionN.setChecked(false);
    		this.txtHemoconcentracion.setDisabled(true);
    		this.txtHemoconcentracion.setValue("");
    	}
    }
    
    @Listen("onCheck=[id$=chkHospitalizadoUltimoAnioS]")
    public void chkHospitalizadoUltimoAnioS_onCheck() {
    	if (this.chkHospitalizadoUltimoAnioS.isChecked()) {
    		this.chkHospitalizadoUltimoAnioN.setChecked(false);
    		this.txtHospitalizadoUltimoAnio.setDisabled(false);
    	} else {
    		this.txtHospitalizadoUltimoAnio.setDisabled(true);
    		this.txtHospitalizadoUltimoAnio.setValue("");
    	}
    }
    @Listen("onCheck=[id$=chkHospitalizadoUltimoAnioN]")
    public void chkHospitalizadoUltimoAnioN_onCheck() {
    	if (this.chkHospitalizadoUltimoAnioN.isChecked()) {
    		this.chkHospitalizadoUltimoAnioS.setChecked(false);
    		this.txtHospitalizadoUltimoAnio.setDisabled(true);
    		this.txtHospitalizadoUltimoAnio.setValue("");
    	}
    }
    
    @Listen("onCheck=[id$=chkTransfusionSangreS]")
    public void chkTransfusionSangreS_onCheck() {
    	if (this.chkTransfusionSangreS.isChecked()) {
    		this.chkTransfusionSangreN.setChecked(false);
    		this.txtTransfusionSangre.setDisabled(false);
    	} else {
    		this.txtTransfusionSangre.setDisabled(true);
    		this.txtTransfusionSangre.setValue("");
    	}
    }
    @Listen("onCheck=[id$=chkTransfusionSangreN]")
    public void chkTransfusionSangreN_onCheck() {
    	if (this.chkTransfusionSangreN.isChecked()) {
    		this.chkTransfusionSangreS.setChecked(false);
    		this.txtTransfusionSangre.setDisabled(true);
    		this.txtTransfusionSangre.setValue("");
    	}
    }
    
    @Listen("onCheck=[id$=chkEstaTomandoMedicamentoS]")
    public void chkEstaTomandoMedicamentoS_onCheck() {
    	if (this.chkEstaTomandoMedicamentoS.isChecked()) {
    		this.chkEstaTomandoMedicamentoN.setChecked(false);
    		this.txtEstaTomandoMedicamento.setDisabled(false);
    	} else {
    		this.txtEstaTomandoMedicamento.setDisabled(true);
    		this.txtEstaTomandoMedicamento.setValue("");
    	}
    }
    @Listen("onCheck=[id$=chkEstaTomandoMedicamentoN]")
    public void chkEstaTomandoMedicamentoN_onCheck() {
    	if (this.chkEstaTomandoMedicamentoN.isChecked()) {
    		this.chkEstaTomandoMedicamentoS.setChecked(false);
    		this.txtEstaTomandoMedicamento.setDisabled(true);
    		this.txtEstaTomandoMedicamento.setValue("");
    	}
    }
    
    @Listen("onCheck=[id$=chkTomaMedicamentoDistintoS]")
    public void chkTomaMedicamentoDistintoS_onCheck() {
    	if (this.chkTomaMedicamentoDistintoS.isChecked()) {
    		this.chkTomaMedicamentoDistintoN.setChecked(false);
    		this.txtTomaMedicamentoDistinto.setDisabled(false);
    	} else {
    		this.txtTomaMedicamentoDistinto.setDisabled(true);
    		this.txtTomaMedicamentoDistinto.setValue("");
    	}
    } 
    @Listen("onCheck=[id$=chkTomaMedicamentoDistintoN]")
    public void chkTomaMedicamentoDistintoN_onCheck() {
    	if (this.chkTomaMedicamentoDistintoN.isChecked()) {
    		this.chkTomaMedicamentoDistintoS.setChecked(false);
    		this.txtTomaMedicamentoDistinto.setDisabled(true);
    		this.txtTomaMedicamentoDistinto.setValue("");
    	}
    }
    
    /**
     * Manejador del evento check para Exámenes
     */
    @Listen("onCheck=[id$=chkBhcS]")
    public void chkBhcS_onCheck() {
    	if (this.chkBhcS.isChecked()) {
    		this.chkBhcN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkBhcN]")
    public void chkBhcN_onCheck() {
    	if (this.chkBhcN.isChecked()) {
    		this.chkBhcS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkSerologiaDengueS]")
    public void chkSerologiaDengueS_onCheck() {
    	if (this.chkSerologiaDengueS.isChecked()) {
    		this.chkSerologiaDengueN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkSerologiaDengueN]")
    public void chkSerologiaDengueN_onCheck() {
    	if (this.chkSerologiaDengueN.isChecked()) {
    		this.chkSerologiaDengueS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkSerologiaChikS]")
    public void chkSerologiaChikS_onCheck() {
    	if (this.chkSerologiaChikS.isChecked()) {
    		this.chkSerologiaChikN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkSerologiaChikN]")
    public void chkSerologiaChikN_onCheck() {
    	if (this.chkSerologiaChikN.isChecked()) {
    		this.chkSerologiaChikS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkGotaGruesaS]")
    public void chkGotaGruesaS_onCheck() {
    	if (this.chkGotaGruesaS.isChecked()) {
    		this.chkGotaGruesaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkGotaGruesaN]")
    public void chkGotaGruesaN_onCheck() {
    	if (this.chkGotaGruesaN.isChecked()) {
    		this.chkGotaGruesaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkExtendidoPerifericoS]")
    public void chkExtendidoPerifericoS_onCheck() {
    	if (this.chkExtendidoPerifericoS.isChecked()) {
    		this.chkExtendidoPerifericoN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkExtendidoPerifericoN]")
    public void chkExtendidoPerifericoN_onCheck() {
    	if (this.chkExtendidoPerifericoN.isChecked()) {
    		this.chkExtendidoPerifericoS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkEgoS]")
    public void chkEgoS_onCheck() {
    	if (this.chkEgoS.isChecked()) {
    		this.chkEgoN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEgoN]")
    public void chkEgoN_onCheck() {
    	if (this.chkEgoN.isChecked()) {
    		this.chkEgoS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkEghS]")
    public void chkEghS_onCheck() {
    	if (this.chkEghS.isChecked()) {
    		this.chkEghN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkEghN]")
    public void chkEghN_onCheck() {
    	if (this.chkEghN.isChecked()) {
    		this.chkEghS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkCitologiaFecalS]")
    public void chkCitologiaFecalS_onCheck() {
    	if (this.chkCitologiaFecalS.isChecked()) {
    		this.chkCitologiaFecalN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkCitologiaFecalN]")
    public void chkCitologiaFecalN_onCheck() {
    	if (this.chkCitologiaFecalN.isChecked()) {
    		this.chkCitologiaFecalS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkFactorReumatoideoS]")
    public void chkFactorReumatoideoS_onCheck() {
    	if (this.chkFactorReumatoideoS.isChecked()) {
    		this.chkFactorReumatoideoN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkFactorReumatoideoN]")
    public void chkFactorReumatoideoN_onCheck() {
    	if (this.chkFactorReumatoideoN.isChecked()) {
    		this.chkFactorReumatoideoS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkAlbuminaS]")
    public void chkAlbuminaS_onCheck() {
    	if (this.chkAlbuminaS.isChecked()) {
    		this.chkAlbuminaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkAlbuminaN]")
    public void chkAlbuminaN_onCheck() {
    	if (this.chkAlbuminaN.isChecked()) {
    		this.chkAlbuminaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkAstAltS]")
    public void chkAstAltS_onCheck() {
    	if (this.chkAstAltS.isChecked()) {
    		this.chkAstAltN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkAstAltN]")
    public void chkAstAltN_onCheck() {
    	if (this.chkAstAltN.isChecked()) {
    		this.chkAstAltS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkBilirubinasS]")
    public void chkBilirubinasS_onCheck() {
    	if (this.chkBilirubinasS.isChecked()) {
    		this.chkBilirubinasN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkBilirubinasN]")
    public void chkBilirubinasN_onCheck() {
    	if (this.chkBilirubinasN.isChecked()) {
    		this.chkBilirubinasS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkCpkS]")
    public void chkCpkS_onCheck() {
    	if (this.chkCpkS.isChecked()) {
    		this.chkCpkN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkCpkN]")
    public void chkCpkN_onCheck() {
    	if (this.chkCpkN.isChecked()) {
    		this.chkCpkS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkColesterolS]")
    public void chkColesterolS_onCheck() {
    	if (this.chkColesterolS.isChecked()) {
    		this.chkColesterolN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkColesterolN]")
    public void chkColesterolN_onCheck() {
    	if (this.chkColesterolN.isChecked()) {
    		this.chkColesterolS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkInfluenzaS]")
    public void chkInfluenzaS_onCheck() {
    	if (this.chkInfluenzaS.isChecked()) {
    		this.chkInfluenzaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkInfluenzaN]")
    public void chkInfluenzaN_onCheck() {
    	if (this.chkInfluenzaN.isChecked()) {
    		this.chkInfluenzaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkOtroExamenS]")
    public void chkOtroExamenS_onCheck() {
    	if (this.chkOtroExamenS.isChecked()) {
    		this.chkOtroExamenN.setChecked(false);
    		this.txtOtroExamen.setDisabled(false);
    	} else {
    		this.txtOtroExamen.setDisabled(true);
    		this.txtOtroExamen.setValue("");
    	}
    }
    @Listen("onCheck=[id$=chkOtroExamenN]")
    public void chkOtroExamenN_onCheck() {
    	if (this.chkOtroExamenN.isChecked()) {
    		this.chkOtroExamenS.setChecked(false);
    		this.txtOtroExamen.setDisabled(true);
    		this.txtOtroExamen.setValue("");
    	}
    }
    
    /**
     * Manejador del evento check para Tratamiento
     */
    @Listen("onCheck=[id$=chkAcetaminofenS]")
    public void chkAcetaminofenS_onCheck() {
    	if (this.chkAcetaminofenS.isChecked()) {
    		this.chkAcetaminofenN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkAcetaminofenN]")
    public void chkAcetaminofenN_onCheck() {
    	if (this.chkAcetaminofenN.isChecked()) {
    		this.chkAcetaminofenS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkAsaS]")
    public void chkAsaS_onCheck() {
    	if (this.chkAsaS.isChecked()) {
    		this.chkAsaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkAsaN]")
    public void chkAsaN_onCheck() {
    	if (this.chkAsaN.isChecked()) {
    		this.chkAsaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkIbuprofenS]")
    public void chkIbuprofenS_onCheck() {
    	if (this.chkIbuprofenS.isChecked()) {
    		this.chkIbuprofenN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkIbuprofenN]")
    public void chkIbuprofenN_onCheck() {
    	if (this.chkIbuprofenN.isChecked()) {
    		this.chkIbuprofenS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkPenicilinaS]")
    public void chkPenicilinaS_onCheck() {
    	if (this.chkPenicilinaS.isChecked()) {
    		this.chkPenicilinaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkPenicilinaN]")
    public void chkPenicilinaN_onCheck() {
    	if (this.chkPenicilinaN.isChecked()) {
    		this.chkPenicilinaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkAmoxicilinaS]")
    public void chkAmoxicilinaS_onCheck() {
    	if (this.chkAmoxicilinaS.isChecked()) {
    		this.chkAmoxicilinaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkAmoxicilinaN]")
    public void chkAmoxicilinaN_onCheck() {
    	if (this.chkAmoxicilinaN.isChecked()) {
    		this.chkAmoxicilinaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkDicloxacilinaS]")
    public void chkDicloxacilinaS_onCheck() {
    	if (this.chkDicloxacilinaS.isChecked()) {
    		this.chkDicloxacilinaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkDicloxacilinaN]")
    public void chkDicloxacilinaN_onCheck() {
    	if (this.chkDicloxacilinaN.isChecked()) {
    		this.chkDicloxacilinaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkOtroAntibioticoS]")
    public void chkOtroAntibioticoS_onCheck() {
    	if (this.chkOtroAntibioticoS.isChecked()) {
    		this.chkOtroAntibioticoN.setChecked(false);
    		this.txtOtroAntibiotico.setDisabled(false);
    	} else {
    		this.txtOtroAntibiotico.setDisabled(true);
    		this.txtOtroAntibiotico.setValue("");
    	}
    }
    @Listen("onCheck=[id$=chkOtroAntibioticoN]")
    public void chkOtroAntibioticoN_onCheck() {
    	if (this.chkOtroAntibioticoN.isChecked()) {
    		this.chkOtroAntibioticoS.setChecked(false);
    		this.txtOtroAntibiotico.setDisabled(true);
    		this.txtOtroAntibiotico.setValue("");
    	}
    }
    
    @Listen("onCheck=[id$=chkFurazolidonaS]")
    public void chkFurazolidonaS_onCheck() {
    	if (this.chkFurazolidonaS.isChecked()) {
    		this.chkFurazolidonaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkFurazolidonaN]")
    public void chkFurazolidonaN_onCheck() {
    	if (this.chkFurazolidonaN.isChecked()) {
    		this.chkFurazolidonaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkMetronidazolTinidazolS]")
    public void chkMetronidazolTinidazolS_onCheck() {
    	if (this.chkMetronidazolTinidazolS.isChecked()) {
    		this.chkMetronidazolTinidazolN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkMetronidazolTinidazolN]")
    public void chkMetronidazolTinidazolN_onCheck() {
    	if (this.chkMetronidazolTinidazolN.isChecked()) {
    		this.chkMetronidazolTinidazolS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkAlbendazolMebendazolS]")
    public void chkAlbendazolMebendazolS_onCheck() {
    	if (this.chkAlbendazolMebendazolS.isChecked()) {
    		this.chkAlbendazolMebendazolN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkAlbendazolMebendazolN]")
    public void chkAlbendazolMebendazolN_onCheck() {
    	if (this.chkAlbendazolMebendazolN.isChecked()) {
    		this.chkAlbendazolMebendazolS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkSulfatoFerrosoS]")
    public void chkSulfatoFerrosoS_onCheck() {
    	if (this.chkSulfatoFerrosoS.isChecked()) {
    		this.chkSulfatoFerrosoN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkSulfatoFerrosoN]")
    public void chkSulfatoFerrosoN_onCheck() {
    	if (this.chkSulfatoFerrosoN.isChecked()) {
    		this.chkSulfatoFerrosoS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkSueroOralS]")
    public void chkSueroOralS_onCheck() {
    	if (this.chkSueroOralS.isChecked()) {
    		this.chkSueroOralN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkSueroOralN]")
    public void chkSueroOralN_onCheck() {
    	if (this.chkSueroOralN.isChecked()) {
    		this.chkSueroOralS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkSulfatoZincS]")
    public void chkSulfatoZincS_onCheck() {
    	if (this.chkSulfatoZincS.isChecked()) {
    		this.chkSulfatoZincN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkSulfatoZincN]")
    public void chkSulfatoZincN_onCheck() {
    	if (this.chkSulfatoZincN.isChecked()) {
    		this.chkSulfatoZincS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkLiquidosIVS]")
    public void chkLiquidosIVS_onCheck() {
    	if (this.chkLiquidosIVS.isChecked()) {
    		this.chkLiquidosIVN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkLiquidosIVN]")
    public void chkLiquidosIVN_onCheck() {
    	if (this.chkLiquidosIVN.isChecked()) {
    		this.chkLiquidosIVS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkPrednisonaS]")
    public void chkPrednisonaS_onCheck() {
    	if (this.chkPrednisonaS.isChecked()) {
    		this.chkPrednisonaN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkPrednisonaN]")
    public void chkPrednisonaN_onCheck() {
    	if (this.chkPrednisonaN.isChecked()) {
    		this.chkPrednisonaS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkHidrocortisonaIVS]")
    public void chkHidrocortisonaIVS_onCheck() {
    	if (this.chkHidrocortisonaIVS.isChecked()) {
    		this.chkHidrocortisonaIVN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkHidrocortisonaIVN]")
    public void chkHidrocortisonaIVN_onCheck() {
    	if (this.chkHidrocortisonaIVN.isChecked()) {
    		this.chkHidrocortisonaIVS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkSalbutamolS]")
    public void chkSalbutamolS_onCheck() {
    	if (this.chkSalbutamolS.isChecked()) {
    		this.chkSalbutamolN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkSalbutamolN]")
    public void chkSalbutamolN_onCheck() {
    	if (this.chkSalbutamolN.isChecked()) {
    		this.chkSalbutamolS.setChecked(false);
    	}
    }
    
    @Listen("onCheck=[id$=chkOseltamivirS]")
    public void chkOseltamivirS_onCheck() {
    	if (this.chkOseltamivirS.isChecked()) {
    		this.chkOseltamivirN.setChecked(false);
    	}
    }
    @Listen("onCheck=[id$=chkOseltamivirN]")
    public void chkOseltamivirN_onCheck() {
    	if (this.chkOseltamivirN.isChecked()) {
    		this.chkOseltamivirS.setChecked(false);
    	}
    }
    
    private void buscarPaciente() {
    	 if (this.txtCodExpediente.getValue()==null){
         	//limpiarCampos();
         	Messagebox.show("Ingrese código", "Validación", Messagebox.OK, Messagebox.INFORMATION);
         	this.txtCodExpediente.setFocus(true);
             return;
         }
        try {
        	String codExpediente = this.txtCodExpediente.getValue().toString();
        	Paciente paciente = pacienteService.getPacienteById(Integer.valueOf(codExpediente));
        	String estudiosPaciente = pacienteService.getEstudiosPaciente(Integer.valueOf(codExpediente));
        	if (paciente != null) {
        		//el paciente esta retirado
        		if (paciente.getRetirado().equals('1')) {
        			//limpiarCampos();
            		Mensajes.enviarMensaje(Mensajes.PACIENTE_RETIRADO, Mensajes.TipoMensaje.INFO);
            		this.txtCodExpediente.setFocus(true);
        		} else {
        			String nombrePaciente = "";
        			String sexo = "";
        			String expedienteFisico = "";
        			//Calendar fechaNac = new GregorianCalendar();
        			
        			nombrePaciente = paciente.getNombre1();
        			sexo =  String.valueOf(paciente.getSexo());
        			Date fechaNac = paciente.getFechaNac();
        			Calendar cal = Calendar.getInstance();
      			  	cal.setTime(fechaNac);
    	    	
        			if (paciente.getNombre2()!=null) nombrePaciente += " " + paciente.getNombre2();
        			if (paciente.getApellido1()!=null) nombrePaciente += " " + paciente.getApellido1();
        			if (paciente.getApellido2()!=null) nombrePaciente += " " + paciente.getApellido2();
        			
        			this.txtNombrePaciente.setValue(nombrePaciente);
        			this.txtEstudiosPaciente.setValue(estudiosPaciente);
        			this.txtSexo.setValue(sexo);
        			this.txtEdad.setValue(UtilDate.obtenerEdad(cal));
        			
        			//Número de expediente fisico con la fecha de nacimiento
                    SimpleDateFormat numExpFis = new SimpleDateFormat("ddMMyy");
                    expedienteFisico = numExpFis.format(cal.getTime());
                    this.txtExpediente.setValue(expedienteFisico);
                    this.txtFechaConsulta.setFocus(true);
        		
        		}
        	}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
    		Mensajes.enviarMensaje("Sucedio un error al realizar búsqueda de expediente","Error",Mensajes.TipoMensaje.ERROR);
		}
    }
    
    /**
     * Método que se encarga de cargar los elementos del combo Diagnostico desde la base de datos
     */
    private void cargarDiagnosticos() {
    	// Limpiar combo
    	Diagnostico seleccione = new Diagnostico();
    	seleccione.setSecDiagnostico(0);
    	//seleccione.setCodigoDignostico((short) 0);
    	seleccione.setDiagnostico("Seleccione");
    	List<Diagnostico> oList = new ArrayList<Diagnostico>();
    	oList.add(0,seleccione);
    	try {
			oList.addAll(hojaConsultaService.getDiagnosticos());
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje("Error consultar los diagnosticos",
					Mensajes.TipoMensaje.ERROR);
		}
    	
        this.cmbDiagnostico1.setModel(new ListModelList<Diagnostico>(new ArrayList<Diagnostico>()));
        this.cmbDiagnostico1.setModel(new ListModelList<Diagnostico>(oList));
        this.cmbDiagnostico1.setItemRenderer(new ComboitemRenderer<Diagnostico>() {
               @Override
               public void render(Comboitem comboitem, Diagnostico fila, int index) throws Exception {
                   comboitem.setLabel( fila.getDiagnostico());
                   comboitem.setValue((fila.getSecDiagnostico()));
                   cmbDiagnostico1.setSelectedIndex(0);
           }});
        
        this.cmbDiagnostico2.setModel(new ListModelList<Diagnostico>(new ArrayList<Diagnostico>()));
        this.cmbDiagnostico2.setModel(new ListModelList<Diagnostico>(oList));
        this.cmbDiagnostico2.setItemRenderer(new ComboitemRenderer<Diagnostico>() {
               @Override
               public void render(Comboitem comboitem, Diagnostico fila, int index) throws Exception {
                   comboitem.setLabel( fila.getDiagnostico());
                   comboitem.setValue((fila.getSecDiagnostico()));
                   cmbDiagnostico2.setSelectedIndex(0);
           }});
        
        this.cmbDiagnostico3.setModel(new ListModelList<Diagnostico>(new ArrayList<Diagnostico>()));
        this.cmbDiagnostico3.setModel(new ListModelList<Diagnostico>(oList));
        this.cmbDiagnostico3.setItemRenderer(new ComboitemRenderer<Diagnostico>() {
               @Override
               public void render(Comboitem comboitem, Diagnostico fila, int index) throws Exception {
                   comboitem.setLabel( fila.getDiagnostico());
                   comboitem.setValue((fila.getSecDiagnostico()));
                   cmbDiagnostico3.setSelectedIndex(0);
           }});
        
        this.cmbDiagnostico4.setModel(new ListModelList<Diagnostico>(new ArrayList<Diagnostico>()));
        this.cmbDiagnostico4.setModel(new ListModelList<Diagnostico>(oList));
        this.cmbDiagnostico4.setItemRenderer(new ComboitemRenderer<Diagnostico>() {
               @Override
               public void render(Comboitem comboitem, Diagnostico fila, int index) throws Exception {
                   comboitem.setLabel( fila.getDiagnostico());
                   comboitem.setValue((fila.getSecDiagnostico()));
                   cmbDiagnostico4.setSelectedIndex(0);
           }});
    }
    
    /**
     * Método que se encarga de cargar los elementos del combo Colegio desde la base de datos
     */
    private void cargarColegios() {
    	// Limpiar combo
    	EscuelaCatalogo seleccione = new EscuelaCatalogo();
    	seleccione.setCodEscuela(0);
    	seleccione.setDescripcion("Seleccione");
    	List<EscuelaCatalogo> oList = new ArrayList<EscuelaCatalogo>();
    	oList.add(0,seleccione);
    	try {
			oList.addAll(hojaConsultaService.getColegios());
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje("Error consultar los colegios",
					Mensajes.TipoMensaje.ERROR);
		}
    	
        this.cmbColegio.setModel(new ListModelList<EscuelaCatalogo>(new ArrayList<EscuelaCatalogo>()));
        this.cmbColegio.setModel(new ListModelList<EscuelaCatalogo>(oList));
        this.cmbColegio.setItemRenderer(new ComboitemRenderer<EscuelaCatalogo>() {
               @Override
               public void render(Comboitem comboitem, EscuelaCatalogo fila, int index) throws Exception {
                   comboitem.setLabel( fila.getDescripcion());
                   comboitem.setValue((fila.getCodEscuela()));
                   cmbColegio.setSelectedIndex(0);
           }});
    }
    
    /**
     * Método que se encarga de cargar los elementos del combo Medicos desde la base de datos
     */
    private void cargarMedicos() {
    	// Limpiar combo
    	UsuariosView seleccione = new UsuariosView();
    	seleccione.setId(0);
    	seleccione.setNombre("Seleccione");
    	List<UsuariosView> oList = new ArrayList<UsuariosView>();
    	oList.add(0,seleccione);
    	try {
			//oList.addAll(usuariosService.obtenerUsuariosByPerfiles("Médico"));
    		oList.addAll(usuariosService.obtenerUsuariosMedicos());
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje("Error consultar los medicos",
					Mensajes.TipoMensaje.ERROR);
		}
    	
        this.cmbMedico.setModel(new ListModelList<UsuariosView>(new ArrayList<UsuariosView>()));
        this.cmbMedico.setModel(new ListModelList<UsuariosView>(oList));
        this.cmbMedico.setItemRenderer(new ComboitemRenderer<UsuariosView>() {
               @Override
               public void render(Comboitem comboitem, UsuariosView fila, int index) throws Exception {
            	   comboitem.setLabel(fila.getNombre());
                   comboitem.setValue(fila.getId().toString());
                   cmbMedico.setSelectedIndex(0);
           }});
    }
    
    /**
     * Método que se encarga de cargar los elementos del combo Enfermer@s desde la base de datos
     */
    private void cargarEnfermeros() {
    	// Limpiar combo
    	UsuariosView seleccione = new UsuariosView();
    	seleccione.setId(0);
    	seleccione.setNombre("Seleccione");
    	List<UsuariosView> oList = new ArrayList<UsuariosView>();
    	oList.add(0,seleccione);
    	try {
			oList.addAll(usuariosService.obtenerUsuariosByPerfiles("Enfermeria"));
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje("Error consultar los enfermer@s",
					Mensajes.TipoMensaje.ERROR);
		}

        this.cmbEnfermeria.setModel(new ListModelList<UsuariosView>(new ArrayList<UsuariosView>()));
        this.cmbEnfermeria.setModel(new ListModelList<UsuariosView>(oList));
        this.cmbEnfermeria.setItemRenderer(new ComboitemRenderer<UsuariosView>() {
               @Override
               public void render(Comboitem comboitem, UsuariosView fila, int index) throws Exception {
            	   comboitem.setLabel(fila.getNombre());
                   comboitem.setValue(fila.getId());
                   cmbEnfermeria.setSelectedIndex(0);
           }});
    }
    
    /**
     * Método que se encarga de cargar los elementos del combo Supervisor desde la base de datos
     */
    private void cargarSupervisor() {
    	// Limpiar combo
    	UsuariosView seleccione = new UsuariosView();
    	seleccione.setId(0);
    	seleccione.setNombre("Seleccione");
    	List<UsuariosView> oList = new ArrayList<UsuariosView>();
    	oList.add(0,seleccione);
    	try {
			//oList.addAll(usuariosService.obtenerUsuariosByPerfiles("Médico"));
    		oList.addAll(usuariosService.obtenerSupervisores());
		} catch (Exception e) {
			e.printStackTrace();
			Mensajes.enviarMensaje("Error consultar los supervisores",
					Mensajes.TipoMensaje.ERROR);
		}
    	
        this.cmbSupervisor.setModel(new ListModelList<UsuariosView>(new ArrayList<UsuariosView>()));
        this.cmbSupervisor.setModel(new ListModelList<UsuariosView>(oList));
        this.cmbSupervisor.setItemRenderer(new ComboitemRenderer<UsuariosView>() {
               @Override
               public void render(Comboitem comboitem, UsuariosView fila, int index) throws Exception {
            	   comboitem.setLabel(fila.getNombre());
                   comboitem.setValue(fila.getId().toString());
                   cmbSupervisor.setSelectedIndex(0);
           }});
    }
    
    
    /**
     * Menejador del evento Click de los botones S, N, D Estados Generales
     */
    @Listen("onClick=[id$=btnEstadoGeneralS]")
    public void btnEstadoGeneraS_onClick() {
    	marcarDesmarcarEstadoGeneral("S");
    }
    
    @Listen("onClick=[id$=btnEstadoGeneralN]")
    public void btnEstadoGeneraN_onClick() {
    	marcarDesmarcarEstadoGeneral("N");
    }
    
    @Listen("onClick=[id$=btnEstadoGeneralD]")
    public void btnEstadoGeneraD_onClick() {
    	marcarDesmarcarEstadoGeneral("D");
    }
    
    /**
     * Menejador del evento Click de los botones S, N, D Gastrointestinal
     */
    @Listen("onClick=[id$=btnGastrointestinalS]")
    public void btnGastrointestinalS_onClick() {
    	marcarDesmarcarGastrointestinal("S");
    }
    
    @Listen("onClick=[id$=btnGastrointestinalN]")
    public void btnGastrointestinalN_onClick() {
    	marcarDesmarcarGastrointestinal("N");
    }
    
    @Listen("onClick=[id$=btnGastrointestinalD]")
    public void btnGastrointestinalD_onClick() {
    	marcarDesmarcarGastrointestinal("D");
    }
    
    /**
     * Menejador del evento Click de los botones S, N, D Osteomuscular
     */
    @Listen("onClick=[id$=btnOsteomuscularS]")
    public void btnOsteomuscularS_onClick() {
    	marcarDesmarcarOsteomuscular("S");
    }
    
    @Listen("onClick=[id$=btnOsteomuscularN]")
    public void btnOsteomuscularN_onClick() {
    	marcarDesmarcarOsteomuscular("N");
    }
    
    @Listen("onClick=[id$=btnOsteomuscularD]")
    public void btnOsteomuscularD_onClick() {
    	marcarDesmarcarOsteomuscular("D");
    }
    
    /**
     * Menejador del evento Click de los botones S, N, D Cabeza
     */
    
    @Listen("onClick=[id$=btnCabezaS]")
    public void btnCabezaS_onClick() {
    	marcarDesmarcarCabeza("S"); 
    }
    
    @Listen("onClick=[id$=btnCabezaN]")
    public void btnCabezaN_onClick() {
    	marcarDesmarcarCabeza("N");
    }
    
    @Listen("onClick=[id$=btnCabezaD]")
    public void btnCabezaD_onClick() {
    	marcarDesmarcarCabeza("D");
    }
    
    /**
     * Menejador del evento Click de los botones S, N, D Deshidratación
     */
    
    @Listen("onClick=[id$=btnDeshidratacionS]")
    public void btnDeshidratacionS_onClick() {
    	marcarDesmarcarDeshidratacion("S"); 
    }
    
    @Listen("onClick=[id$=btnDeshidratacionN]")
    public void btnDeshidratacionN_onClick() {
    	marcarDesmarcarDeshidratacion("N");
    }
    
    @Listen("onClick=[id$=btnDeshidratacionD]")
    public void btnDeshidratacionD_onClick() {
    	marcarDesmarcarDeshidratacion("D");
    }
    
    /**
     * Menejador del evento Click de los botones S, N Cutáneo
     */
    
    @Listen("onClick=[id$=btnCutaneoS]")
    public void btnCutaneoS_onClick() {
    	marcarDesmarcarCutaneo("S");
    }
    
    @Listen("onClick=[id$=btnCutaneoN]")
    public void btnCutaneoN_onClick() {
    	marcarDesmarcarCutaneo("N");
    }
    
    /**
     * Menejador del evento Click de los botones S, N, D Garganta
     */
    
    @Listen("onClick=[id$=btnGargantaS]")
    public void btnGargantaS_onClick() {
    	marcarDesmarcarGarganta("S");
    }
    
    @Listen("onClick=[id$=btnGargantaN]")
    public void btnGargantaN_onClick() {
    	marcarDesmarcarGarganta("N");
    }
    
    @Listen("onClick=[id$=btnGargantaD]")
    public void btnGargantaD_onClick() {
    	marcarDesmarcarGarganta("D");
    }
    
    /**
     * Menejador del evento Click de los botones S, N, D Renal
     */
    
    @Listen("onClick=[id$=btnRenalS]")
    public void btnRenalS_onClick() {
    	marcarDesmarcarRenal("S");
    }
    
    @Listen("onClick=[id$=btnRenalN]")
    public void btnRenalN_onClick() {
    	marcarDesmarcarRenal("N");
    }
    
    @Listen("onClick=[id$=btnRenalD]")
    public void btnRenalD_onClick() {
    	marcarDesmarcarRenal("D");
    }
    
    /**
     * Menejador del evento Click de los botones S, N, D Estado Nutricional
     */
    
    @Listen("onClick=[id$=btnEstadoNutricionalS]")
    public void btnEstadoNutricionalS_onClick() {
    	marcarDesmarcarEstadoNutricional("S");
    }
    
    @Listen("onClick=[id$=btnEstadoNutricionalN]")
    public void btnEstadoNutricionalN_onClick() {
    	marcarDesmarcarEstadoNutricional("N");
    }
    
    @Listen("onClick=[id$=btnEstadoNutricionalD]")
    public void btnEstadoNutricionalD_onClick() {
    	marcarDesmarcarEstadoNutricional("D");
    }
    
    /**
     * Menejador del evento Click de los botones S, N, D Respiratorio
     */
    
    @Listen("onClick=[id$=btnRespiratorioS]")
    public void btnRespiratorioS_onClick() {
    	marcarDesmarcarRespiratorio("S");
    }
    
    @Listen("onClick=[id$=btnRespiratorioN]")
    public void btnRespiratorioN_onClick() {
    	marcarDesmarcarRespiratorio("N");
    }
    
    @Listen("onClick=[id$=btnRespiratorioD]")
    public void btnRespiratorioD_onClick() {
    	marcarDesmarcarRespiratorio("D");
    }
    
    /**
     * Menejador del evento Click de los botones S, N Referencia
     */
    
    @Listen("onClick=[id$=btnReferenciaS]")
    public void btnReferenciaS_onClick() {
    	marcarDesmarcarReferencia("S");
    }
    
    @Listen("onClick=[id$=btnReferenciaN]")
    public void btnReferenciaN_onClick() {
    	marcarDesmarcarReferencia("N");
    }

    /**
     * Menejador del evento Click de los botones S, N, D Vacunas
     */
    
    @Listen("onClick=[id$=btnVacunasS]")
    public void btnVacunasS_onClick() {
    	marcarDesmarcarVacunas("S");
    }
    
    @Listen("onClick=[id$=btnVacunasN]")
    public void btnVacunasN_onClick() {
    	marcarDesmarcarVacunas("N");
    }
    
    @Listen("onClick=[id$=btnVacunasD]")
    public void btnVacunasD_onClick() {
    	marcarDesmarcarVacunas("D");
    }
    
    /**
     * Menejador del evento Click de los botones S, N Exámenes
     */
    @Listen("onClick=[id$=btnExamenesS]")
    public void btnExamenesS_onClick() {
    	marcarDesmarcarExamenes("S");
    }
    
    @Listen("onClick=[id$=btnExamenesN]")
    public void btnExamenesN_onClick() {
    	marcarDesmarcarExamenes("N");
    }
    
    /**
     * Menejador del evento Click de los botones S, N Tratamiento
     */
    @Listen("onClick=[id$=btnTratamientoS]")
    public void btnTratamientoS_onClick() {
    	marcarDesmarcarTratamientos("S");
    }
    
    @Listen("onClick=[id$=btnTratamientoN]")
    public void btnTratamientoN_onClick() {
    	marcarDesmarcarTratamientos("N");
    }
    
    /**
     * Menejador del evento Click de los botones S, N, D Categoría
     */
    @Listen("onClick=[id$=btnCategoriaS]")
    public void btnCategoriaS_onClick() {
    	marcarDesmarcarCategorias("S");
    }
    
    @Listen("onClick=[id$=btnCategoriaN]")
    public void btnCategoriaN_onClick() {
    	marcarDesmarcarCategorias("N");
    }
    
    @Listen("onClick=[id$=btnCategoriaD]")
    public void btnCategoriaD_onClick() {
    	marcarDesmarcarCategorias("D");
    }
    
    public void marcarDesmarcarEstadoGeneral(String valor) {
    	if (valor.trim().equals("S")) {
    		this.chkFiebreS.setChecked(true);
    		this.chkAsteniaS.setChecked(true);
    		this.chkAnormalMenteSomnolientoS.setChecked(true);
    		this.chkMalEstadoGeneralS.setChecked(true);
    		this.chkPerdidaConcienciaS.setChecked(true);
    		this.chkInquietoIrritableS.setChecked(true);
    		this.chkConvulsionesS.setChecked(true);
    		this.chkHipotermiaS.setChecked(true);
    		this.chkLetargiaS.setChecked(true);
    		this.chkFiebreN.setChecked(false);
    		this.chkAsteniaN.setChecked(false);
    		this.chkAnormalMenteSomnolientoN.setChecked(false);
    		this.chkMalEstadoGeneralN.setChecked(false);
    		this.chkPerdidaConcienciaN.setChecked(false);
    		this.chkInquietoIrritableN.setChecked(false);
    		this.chkConvulsionesN.setChecked(false);
    		this.chkHipotermiaN.setChecked(false);
    		this.chkLetargiaN.setChecked(false);
    		this.chkFiebreD.setChecked(false);
    		this.chkAsteniaD.setChecked(false);
    		
    	} else if (valor.trim().equals("N")) {
    		this.chkFiebreS.setChecked(false);
    		this.chkAsteniaS.setChecked(false);
    		this.chkAnormalMenteSomnolientoS.setChecked(false);
    		this.chkMalEstadoGeneralS.setChecked(false);
    		this.chkPerdidaConcienciaS.setChecked(false);
    		this.chkInquietoIrritableS.setChecked(false);
    		this.chkConvulsionesS.setChecked(false);
    		this.chkHipotermiaS.setChecked(false);
    		this.chkLetargiaS.setChecked(false);
    		this.chkFiebreN.setChecked(true);
    		this.chkAsteniaN.setChecked(true);
    		this.chkAnormalMenteSomnolientoN.setChecked(true);
    		this.chkMalEstadoGeneralN.setChecked(true);
    		this.chkPerdidaConcienciaN.setChecked(true);
    		this.chkInquietoIrritableN.setChecked(true);
    		this.chkConvulsionesN.setChecked(true);
    		this.chkHipotermiaN.setChecked(true);
    		this.chkLetargiaN.setChecked(true);
    		this.chkFiebreD.setChecked(false);
    		this.chkAsteniaD.setChecked(false);
    	} else {
    		this.chkFiebreS.setChecked(false);
    		this.chkAsteniaS.setChecked(false);
    		this.chkAnormalMenteSomnolientoS.setChecked(false);
    		this.chkMalEstadoGeneralS.setChecked(false);
    		this.chkPerdidaConcienciaS.setChecked(false);
    		this.chkInquietoIrritableS.setChecked(false);
    		this.chkConvulsionesS.setChecked(false);
    		this.chkHipotermiaS.setChecked(false);
    		this.chkLetargiaS.setChecked(false);
    		this.chkFiebreN.setChecked(false);
    		this.chkAsteniaN.setChecked(false);
    		this.chkAnormalMenteSomnolientoN.setChecked(false);
    		this.chkMalEstadoGeneralN.setChecked(false);
    		this.chkPerdidaConcienciaN.setChecked(false);
    		this.chkInquietoIrritableN.setChecked(false);
    		this.chkConvulsionesN.setChecked(false);
    		this.chkHipotermiaN.setChecked(false);
    		this.chkLetargiaN.setChecked(false);
    		this.chkFiebreD.setChecked(true);
    		this.chkAsteniaD.setChecked(true);
    	}
    }
    
    public void marcarDesmarcarGastrointestinal(String valor) {
    	if (valor.trim().equals("S")) {
    		this.chkPocoApetitoS.setChecked(true);
    		this.chkNauseaS.setChecked(true);
    		this.chkDificultadAlimentaseS.setChecked(true);
    		this.chkVomito12HorasS.setChecked(true);
    		this.txtVomito12Horas.setDisabled(false);
    		this.chkDiarreaS.setChecked(true);
    		this.chkDiarreaSangreS.setChecked(true);
    		this.chkEstrenimientoS.setChecked(true);
    		this.chkDolorAbInterS.setChecked(true);
    		this.chkDolorAbContinuoS.setChecked(true);
    		this.chkEpigastralgiaS.setChecked(true);
    		this.chkIntoleranciaViaOralS.setChecked(true);
    		this.chkDistensionAbdominalS.setChecked(true);
    		this.chkHepatomegaliaS.setChecked(true);
    		this.txtHepatomegalia.setDisabled(false);
    		this.chkPocoApetitoN.setChecked(false);
    		this.chkNauseaN.setChecked(false);
    		this.chkDificultadAlimentaseN.setChecked(false);
    		this.chkVomito12HorasN.setChecked(false);
    		this.chkDiarreaN.setChecked(false);
    		this.chkDiarreaSangreN.setChecked(false);
    		this.chkEstrenimientoN.setChecked(false);
    		this.chkDolorAbInterN.setChecked(false);
    		this.chkDolorAbContinuoN.setChecked(false);
    		this.chkEpigastralgiaN.setChecked(false);
    		this.chkIntoleranciaViaOralN.setChecked(false);
    		this.chkDistensionAbdominalN.setChecked(false);
    		this.chkHepatomegaliaN.setChecked(false);
    		this.chkPocoApetitoD.setChecked(false);
    		this.chkNauseaD.setChecked(false);
    		this.chkDificultadAlimentaseD.setChecked(false);
    		this.chkVomito12HorasD.setChecked(false);
    		this.chkDiarreaD.setChecked(false);
    		this.chkDiarreaSangreD.setChecked(false);
    		this.chkEstrenimientoD.setChecked(false);
    		this.chkDolorAbInterD.setChecked(false);
    		this.chkDolorAbContinuoD.setChecked(false);
    		this.chkEpigastralgiaD.setChecked(false);
    		this.chkIntoleranciaViaOralD.setChecked(false);
    		this.chkDistensionAbdominalD.setChecked(false);
    	} else if (valor.trim().equals("N")) {
    		this.chkPocoApetitoS.setChecked(false);
    		this.chkNauseaS.setChecked(false);
    		this.chkDificultadAlimentaseS.setChecked(false);
    		this.chkVomito12HorasS.setChecked(false);
    		this.txtVomito12Horas.setDisabled(true);
    		this.txtVomito12Horas.setValue("");
    		this.chkDiarreaS.setChecked(false);
    		this.chkDiarreaSangreS.setChecked(false);
    		this.chkEstrenimientoS.setChecked(false);
    		this.chkDolorAbInterS.setChecked(false);
    		this.chkDolorAbContinuoS.setChecked(false);
    		this.chkEpigastralgiaS.setChecked(false);
    		this.chkIntoleranciaViaOralS.setChecked(false);
    		this.chkDistensionAbdominalS.setChecked(false);
    		this.chkHepatomegaliaS.setChecked(false);
    		this.txtHepatomegalia.setDisabled(true);
    		this.txtHepatomegalia.setValue("");
    		this.chkPocoApetitoN.setChecked(true);
    		this.chkNauseaN.setChecked(true);
    		this.chkDificultadAlimentaseN.setChecked(true);
    		this.chkVomito12HorasN.setChecked(true);
    		this.chkDiarreaN.setChecked(true);
    		this.chkDiarreaSangreN.setChecked(true);
    		this.chkEstrenimientoN.setChecked(true);
    		this.chkDolorAbInterN.setChecked(true);
    		this.chkDolorAbContinuoN.setChecked(true);
    		this.chkEpigastralgiaN.setChecked(true);
    		this.chkIntoleranciaViaOralN.setChecked(true);
    		this.chkDistensionAbdominalN.setChecked(true);
    		this.chkHepatomegaliaN.setChecked(true);
    		this.chkPocoApetitoD.setChecked(false);
    		this.chkNauseaD.setChecked(false);
    		this.chkDificultadAlimentaseD.setChecked(false);
    		this.chkVomito12HorasD.setChecked(false);
    		this.chkDiarreaD.setChecked(false);
    		this.chkDiarreaSangreD.setChecked(false);
    		this.chkEstrenimientoD.setChecked(false);
    		this.chkDolorAbInterD.setChecked(false);
    		this.chkDolorAbContinuoD.setChecked(false);
    		this.chkEpigastralgiaD.setChecked(false);
    		this.chkIntoleranciaViaOralD.setChecked(false);
    		this.chkDistensionAbdominalD.setChecked(false);
    	} else {
    		this.chkPocoApetitoS.setChecked(false);
    		this.chkNauseaS.setChecked(false);
    		this.chkDificultadAlimentaseS.setChecked(false);
    		this.chkVomito12HorasS.setChecked(false);
    		this.txtVomito12Horas.setDisabled(true);
    		this.txtVomito12Horas.setValue("");
    		this.chkDiarreaS.setChecked(false);
    		this.chkDiarreaSangreS.setChecked(false);
    		this.chkEstrenimientoS.setChecked(false);
    		this.chkDolorAbInterS.setChecked(false);
    		this.chkDolorAbContinuoS.setChecked(false);
    		this.chkEpigastralgiaS.setChecked(false);
    		this.chkIntoleranciaViaOralS.setChecked(false);
    		this.chkDistensionAbdominalS.setChecked(false);
    		//this.chkHepatomegaliaS.setChecked(false);
    		//this.txtHepatomegalia.setDisabled(false);
    		//this.txtHepatomegalia.setValue("");
    		this.chkPocoApetitoN.setChecked(false);
    		this.chkNauseaN.setChecked(false);
    		this.chkDificultadAlimentaseN.setChecked(false);
    		this.chkVomito12HorasN.setChecked(false);
    		this.chkDiarreaN.setChecked(false);
    		this.chkDiarreaSangreN.setChecked(false);
    		this.chkEstrenimientoN.setChecked(false);
    		this.chkDolorAbInterN.setChecked(false);
    		this.chkDolorAbContinuoN.setChecked(false);
    		this.chkEpigastralgiaN.setChecked(false);
    		this.chkIntoleranciaViaOralN.setChecked(false);
    		this.chkDistensionAbdominalN.setChecked(false);
    		//this.chkHepatomegaliaN.setChecked(false);
    		this.chkPocoApetitoD.setChecked(true);
    		this.chkNauseaD.setChecked(true);
    		this.chkDificultadAlimentaseD.setChecked(true);
    		this.chkVomito12HorasD.setChecked(true);
    		this.chkDiarreaD.setChecked(true);
    		this.chkDiarreaSangreD.setChecked(true);
    		this.chkEstrenimientoD.setChecked(true);
    		this.chkDolorAbInterD.setChecked(true);
    		this.chkDolorAbContinuoD.setChecked(true);
    		this.chkEpigastralgiaD.setChecked(true);
    		this.chkIntoleranciaViaOralD.setChecked(true);
    		this.chkDistensionAbdominalD.setChecked(true);
    	}
	}
    
    public void marcarDesmarcarOsteomuscular(String valor) {
    	if (valor.trim().equals("S")) {
    		this.chkArtralgiaS.setChecked(true);
    		this.chkMialgiaS.setChecked(true);
    		this.chkLumbalgiaS.setChecked(true);
    		this.chkDolorCuelloS.setChecked(true);
    		this.chkTenosinovitisS.setChecked(true);
    		this.chkArtralgiaProximalS.setChecked(true);
    		this.chkArtralgiaDistalS.setChecked(true);
    		this.chkConjuntivitisS.setChecked(true);
    		this.chkEdemaMunecasS.setChecked(true);
    		this.chkEdemaCodosS.setChecked(true);
    		this.chkEdemaHombrosS.setChecked(true);
    		this.chkEdemaRodillasS.setChecked(true);
    		this.chkEdemaTobillosS.setChecked(true);
    		this.chkArtralgiaN.setChecked(false);
    		this.chkMialgiaN.setChecked(false);
    		this.chkLumbalgiaN.setChecked(false);
    		this.chkDolorCuelloN.setChecked(false);
    		this.chkTenosinovitisN.setChecked(false);
    		this.chkArtralgiaProximalN.setChecked(false);
    		this.chkArtralgiaDistalN.setChecked(false);
    		this.chkConjuntivitisN.setChecked(false);
    		this.chkEdemaMunecasN.setChecked(false);
    		this.chkEdemaCodosN.setChecked(false);
    		this.chkEdemaHombrosN.setChecked(false);
    		this.chkEdemaRodillasN.setChecked(false);
    		this.chkEdemaTobillosN.setChecked(false);
    		this.chkArtralgiaD.setChecked(false);
    		this.chkMialgiaD.setChecked(false);
    		this.chkLumbalgiaD.setChecked(false);
    		this.chkDolorCuelloD.setChecked(false);
    		this.chkTenosinovitisD.setChecked(false);
    		this.chkArtralgiaProximalD.setChecked(false);
    		this.chkArtralgiaDistalD.setChecked(false);
    		this.chkConjuntivitisD.setChecked(false);
    		this.chkEdemaMunecasD.setChecked(false);
    		this.chkEdemaCodosD.setChecked(false);
    		this.chkEdemaHombrosD.setChecked(false);
    		this.chkEdemaRodillasD.setChecked(false);
    		this.chkEdemaTobillosD.setChecked(false);
    	} else if (valor.trim().equals("N")) {
    		this.chkArtralgiaS.setChecked(false);
    		this.chkMialgiaS.setChecked(false);
    		this.chkLumbalgiaS.setChecked(false);
    		this.chkDolorCuelloS.setChecked(false);
    		this.chkTenosinovitisS.setChecked(false);
    		this.chkArtralgiaProximalS.setChecked(false);
    		this.chkArtralgiaDistalS.setChecked(false);
    		this.chkConjuntivitisS.setChecked(false);
    		this.chkEdemaMunecasS.setChecked(false);
    		this.chkEdemaCodosS.setChecked(false);
    		this.chkEdemaHombrosS.setChecked(false);
    		this.chkEdemaRodillasS.setChecked(false);
    		this.chkEdemaTobillosS.setChecked(false);
    		this.chkArtralgiaN.setChecked(true);
    		this.chkMialgiaN.setChecked(true);
    		this.chkLumbalgiaN.setChecked(true);
    		this.chkDolorCuelloN.setChecked(true);
    		this.chkTenosinovitisN.setChecked(true);
    		this.chkArtralgiaProximalN.setChecked(true);
    		this.chkArtralgiaDistalN.setChecked(true);
    		this.chkConjuntivitisN.setChecked(true);
    		this.chkEdemaMunecasN.setChecked(true);
    		this.chkEdemaCodosN.setChecked(true);
    		this.chkEdemaHombrosN.setChecked(true);
    		this.chkEdemaRodillasN.setChecked(true);
    		this.chkEdemaTobillosN.setChecked(true);
    		this.chkArtralgiaD.setChecked(false);
    		this.chkMialgiaD.setChecked(false);
    		this.chkLumbalgiaD.setChecked(false);
    		this.chkDolorCuelloD.setChecked(false);
    		this.chkTenosinovitisD.setChecked(false);
    		this.chkArtralgiaProximalD.setChecked(false);
    		this.chkArtralgiaDistalD.setChecked(false);
    		this.chkConjuntivitisD.setChecked(false);
    		this.chkEdemaMunecasD.setChecked(false);
    		this.chkEdemaCodosD.setChecked(false);
    		this.chkEdemaHombrosD.setChecked(false);
    		this.chkEdemaRodillasD.setChecked(false);
    		this.chkEdemaTobillosD.setChecked(false);
    	} else {
    		this.chkArtralgiaS.setChecked(false);
    		this.chkMialgiaS.setChecked(false);
    		this.chkLumbalgiaS.setChecked(false);
    		this.chkDolorCuelloS.setChecked(false);
    		this.chkTenosinovitisS.setChecked(false);
    		this.chkArtralgiaProximalS.setChecked(false);
    		this.chkArtralgiaDistalS.setChecked(false);
    		this.chkConjuntivitisS.setChecked(false);
    		this.chkEdemaMunecasS.setChecked(false);
    		this.chkEdemaCodosS.setChecked(false);
    		this.chkEdemaHombrosS.setChecked(false);
    		this.chkEdemaRodillasS.setChecked(false);
    		this.chkEdemaTobillosS.setChecked(false);
    		this.chkArtralgiaN.setChecked(false);
    		this.chkMialgiaN.setChecked(false);
    		this.chkLumbalgiaN.setChecked(false);
    		this.chkDolorCuelloN.setChecked(false);
    		this.chkTenosinovitisN.setChecked(false);
    		this.chkArtralgiaProximalN.setChecked(false);
    		this.chkArtralgiaDistalN.setChecked(false);
    		this.chkConjuntivitisN.setChecked(false);
    		this.chkEdemaMunecasN.setChecked(false);
    		this.chkEdemaCodosN.setChecked(false);
    		this.chkEdemaHombrosN.setChecked(false);
    		this.chkEdemaRodillasN.setChecked(false);
    		this.chkEdemaTobillosN.setChecked(false);
    		this.chkArtralgiaD.setChecked(true);
    		this.chkMialgiaD.setChecked(true);
    		this.chkLumbalgiaD.setChecked(true);
    		this.chkDolorCuelloD.setChecked(true);
    		this.chkTenosinovitisD.setChecked(true);
    		this.chkArtralgiaProximalD.setChecked(true);
    		this.chkArtralgiaDistalD.setChecked(true);
    		this.chkConjuntivitisD.setChecked(true);
    		this.chkEdemaMunecasD.setChecked(true);
    		this.chkEdemaCodosD.setChecked(true);
    		this.chkEdemaHombrosD.setChecked(true);
    		this.chkEdemaRodillasD.setChecked(true);
    		this.chkEdemaTobillosD.setChecked(true);
    	}
    }
    
    public void marcarDesmarcarCabeza(String valor) {
    	if (valor.trim().equals("S")) {
    		this.chkCefaleaS.setChecked(true);
    		this.chkRigidezCuelloS.setChecked(true);
    		this.chkInyecciónConjuntivalS.setChecked(true);
    		this.chkHemorragiaSubconjuntivalS.setChecked(true);
    		this.chkDolorRetroocularS.setChecked(true);
    		this.chkFontanelaAbombadaS.setChecked(true);
    		this.chkIctericiaConjuntivalS.setChecked(true);
    		this.chkCefaleaN.setChecked(false);
    		this.chkRigidezCuelloN.setChecked(false);
    		this.chkInyecciónConjuntivalN.setChecked(false);
    		this.chkHemorragiaSubconjuntivalN.setChecked(false);
    		this.chkDolorRetroocularN.setChecked(false);
    		this.chkFontanelaAbombadaN.setChecked(false);
    		this.chkIctericiaConjuntivalN.setChecked(false);
    		this.chkCefaleaD.setChecked(false);
    		this.chkDolorRetroocularD.setChecked(false);
    	} else if (valor.trim().equals("N")) {
    		this.chkCefaleaS.setChecked(false);
    		this.chkRigidezCuelloS.setChecked(false);
    		this.chkInyecciónConjuntivalS.setChecked(false);
    		this.chkHemorragiaSubconjuntivalS.setChecked(false);
    		this.chkDolorRetroocularS.setChecked(false);
    		this.chkFontanelaAbombadaS.setChecked(false);
    		this.chkIctericiaConjuntivalS.setChecked(false);
    		this.chkCefaleaN.setChecked(true);
    		this.chkRigidezCuelloN.setChecked(true);
    		this.chkInyecciónConjuntivalN.setChecked(true);
    		this.chkHemorragiaSubconjuntivalN.setChecked(true);
    		this.chkDolorRetroocularN.setChecked(true);
    		this.chkFontanelaAbombadaN.setChecked(true);
    		this.chkIctericiaConjuntivalN.setChecked(true);
    		this.chkCefaleaD.setChecked(false);
    		this.chkDolorRetroocularD.setChecked(false);
    	} else {
    		this.chkCefaleaS.setChecked(false);
    		this.chkRigidezCuelloS.setChecked(false);
    		this.chkInyecciónConjuntivalS.setChecked(false);
    		this.chkHemorragiaSubconjuntivalS.setChecked(false);
    		this.chkDolorRetroocularS.setChecked(false);
    		this.chkFontanelaAbombadaS.setChecked(false);
    		this.chkIctericiaConjuntivalS.setChecked(false);
    		this.chkCefaleaN.setChecked(false);
    		this.chkRigidezCuelloN.setChecked(false);
    		this.chkInyecciónConjuntivalN.setChecked(false);
    		this.chkHemorragiaSubconjuntivalN.setChecked(false);
    		this.chkDolorRetroocularN.setChecked(false);
    		this.chkFontanelaAbombadaN.setChecked(false);
    		this.chkIctericiaConjuntivalN.setChecked(false);
    		this.chkCefaleaD.setChecked(true);
    		this.chkDolorRetroocularD.setChecked(true);
    	}
    }
    
    public void marcarDesmarcarDeshidratacion(String valor) {
    	if (valor.trim().equals("S")) {
    		this.chkLenguaMucosaSecasS.setChecked(true);
    		this.chkPliegueCutaneoS.setChecked(true);
    		this.chkOrinaReducidaS.setChecked(true);
    		this.chkBebeAvidoSedS.setChecked(true);
    		this.chkOjosHundidosS.setChecked(true);
    		this.chkFontanelaHundidaS.setChecked(true);
    		this.chkLenguaMucosaSecasN.setChecked(false);
    		this.chkPliegueCutaneoN.setChecked(false);
    		this.chkOrinaReducidaN.setChecked(false);
    		this.chkBebeAvidoSedN.setChecked(false);
    		this.chkOjosHundidosN.setChecked(false);
    		this.chkFontanelaHundidaN.setChecked(false);
    		this.chkOrinaReducidaD.setChecked(false);
    	} else if (valor.trim().equals("N")) {
    		this.chkLenguaMucosaSecasS.setChecked(false);
    		this.chkPliegueCutaneoS.setChecked(false);
    		this.chkOrinaReducidaS.setChecked(false);
    		this.chkBebeAvidoSedS.setChecked(false);
    		this.chkOjosHundidosS.setChecked(false);
    		this.chkFontanelaHundidaS.setChecked(false);
    		this.chkLenguaMucosaSecasN.setChecked(true);
    		this.chkPliegueCutaneoN.setChecked(true);
    		this.chkOrinaReducidaN.setChecked(true);
    		this.chkBebeAvidoSedN.setChecked(true);
    		this.chkOjosHundidosN.setChecked(true);
    		this.chkFontanelaHundidaN.setChecked(true);
    		this.chkOrinaReducidaD.setChecked(false);
    	} else {
    		this.chkLenguaMucosaSecasS.setChecked(false);
    		this.chkPliegueCutaneoS.setChecked(false);
    		this.chkOrinaReducidaS.setChecked(false);
    		this.chkBebeAvidoSedS.setChecked(false);
    		this.chkOjosHundidosS.setChecked(false);
    		this.chkFontanelaHundidaS.setChecked(false);
    		this.chkLenguaMucosaSecasN.setChecked(false);
    		this.chkPliegueCutaneoN.setChecked(false);
    		this.chkOrinaReducidaN.setChecked(false);
    		this.chkBebeAvidoSedN.setChecked(false);
    		this.chkOjosHundidosN.setChecked(false);
    		this.chkFontanelaHundidaN.setChecked(false);
    		this.chkOrinaReducidaD.setChecked(true);
    	}
    }
    
    public void marcarDesmarcarCutaneo(String valor) {
    	if (valor.trim().equals("S")) {
    		this.chkRashLocalizadoS.setChecked(true);
    		this.chkRashGeneralizadoS.setChecked(true);
    		this.chkRashEritematosoS.setChecked(true);
    		this.chkRashMacularS.setChecked(true);
    		this.chkRashPapularS.setChecked(true);
    		this.chkPielMoteadaS.setChecked(true);
    		this.chkRuborFacialS.setChecked(true);
    		this.chkEquimosisS.setChecked(true);
    		this.chkCianosisCentralS.setChecked(true);
    		this.chkIctericiaS.setChecked(true);
    		this.chkRashLocalizadoN.setChecked(false);
    		this.chkRashGeneralizadoN.setChecked(false);
    		this.chkRashEritematosoN.setChecked(false);
    		this.chkRashMacularN.setChecked(false);
    		this.chkRashPapularN.setChecked(false);
    		this.chkPielMoteadaN.setChecked(false);
    		this.chkRuborFacialN.setChecked(false);
    		this.chkEquimosisN.setChecked(false);
    		this.chkCianosisCentralN.setChecked(false);
    		this.chkIctericiaN.setChecked(false);
    	} else {
    		this.chkRashLocalizadoS.setChecked(false);
    		this.chkRashGeneralizadoS.setChecked(false);
    		this.chkRashEritematosoS.setChecked(false);
    		this.chkRashMacularS.setChecked(false);
    		this.chkRashPapularS.setChecked(false);
    		this.chkPielMoteadaS.setChecked(false);
    		this.chkRuborFacialS.setChecked(false);
    		this.chkEquimosisS.setChecked(false);
    		this.chkCianosisCentralS.setChecked(false);
    		this.chkIctericiaS.setChecked(false);
    		this.chkRashLocalizadoN.setChecked(true);
    		this.chkRashGeneralizadoN.setChecked(true);
    		this.chkRashEritematosoN.setChecked(true);
    		this.chkRashMacularN.setChecked(true);
    		this.chkRashPapularN.setChecked(true);
    		this.chkPielMoteadaN.setChecked(true);
    		this.chkRuborFacialN.setChecked(true);
    		this.chkEquimosisN.setChecked(true);
    		this.chkCianosisCentralN.setChecked(true);
    		this.chkIctericiaN.setChecked(true);
    	}
    }
    
    public void marcarDesmarcarGarganta(String valor) {
    	if (valor.trim().equals("S")) {
    		this.chkEritemaS.setChecked(true);
    		this.chkDolorGargantaS.setChecked(true);
    		this.chkAdenopatiasCervicalesS.setChecked(true);
    		this.chkExudadoS.setChecked(true);
    		this.chkPetequiasMucosaS.setChecked(true);
    		this.chkEritemaN.setChecked(false);
    		this.chkDolorGargantaN.setChecked(false);
    		this.chkAdenopatiasCervicalesN.setChecked(false);
    		this.chkExudadoN.setChecked(false);
    		this.chkPetequiasMucosaN.setChecked(false);
    		this.chkDolorGargantaD.setChecked(false);
    	} else if (valor.trim().equals("N")) {
    		this.chkEritemaS.setChecked(false);
    		this.chkDolorGargantaS.setChecked(false);
    		this.chkAdenopatiasCervicalesS.setChecked(false);
    		this.chkExudadoS.setChecked(false);
    		this.chkPetequiasMucosaS.setChecked(false);
    		this.chkEritemaN.setChecked(true);
    		this.chkDolorGargantaN.setChecked(true);
    		this.chkAdenopatiasCervicalesN.setChecked(true);
    		this.chkExudadoN.setChecked(true);
    		this.chkPetequiasMucosaN.setChecked(true);
    		this.chkDolorGargantaD.setChecked(false);
    	} else {
    		this.chkEritemaS.setChecked(false);
    		this.chkDolorGargantaS.setChecked(false);
    		this.chkAdenopatiasCervicalesS.setChecked(false);
    		this.chkExudadoS.setChecked(false);
    		this.chkPetequiasMucosaS.setChecked(false);
    		this.chkEritemaN.setChecked(false);
    		this.chkDolorGargantaN.setChecked(false);
    		this.chkAdenopatiasCervicalesN.setChecked(false);
    		this.chkExudadoN.setChecked(false);
    		this.chkPetequiasMucosaN.setChecked(false);
    		this.chkDolorGargantaD.setChecked(true);
    	}
	}
    
    public void marcarDesmarcarRenal(String valor) {
    	if (valor.trim().equals("S")) {
    		this.chkSintomasUrinariosS.setChecked(true);
    		this.chkLeucocituriaXCampoS.setChecked(true);
    		this.chkNitritosS.setChecked(true);
    		this.chkEritrocitosXCampoS.setChecked(true);
    		this.chkBilirrubinuriaS.setChecked(true);
    		this.chkSintomasUrinariosN.setChecked(false);
    		this.chkLeucocituriaXCampoN.setChecked(false);
    		this.chkNitritosN.setChecked(false);
    		this.chkEritrocitosXCampoN.setChecked(false);
    		this.chkBilirrubinuriaN.setChecked(false);
    		this.chkSintomasUrinariosD.setChecked(false);
    		this.chkLeucocituriaXCampoD.setChecked(false);
    		this.chkNitritosD.setChecked(false);
    		this.chkEritrocitosXCampoD.setChecked(false);
    		this.chkBilirrubinuriaD.setChecked(false);
    	} else if (valor.trim().equals("N")) {
    		this.chkSintomasUrinariosS.setChecked(false);
    		this.chkLeucocituriaXCampoS.setChecked(false);
    		this.chkNitritosS.setChecked(false);
    		this.chkEritrocitosXCampoS.setChecked(false);
    		this.chkBilirrubinuriaS.setChecked(false);
    		this.chkSintomasUrinariosN.setChecked(true);
    		this.chkLeucocituriaXCampoN.setChecked(true);
    		this.chkNitritosN.setChecked(true);
    		this.chkEritrocitosXCampoN.setChecked(true);
    		this.chkBilirrubinuriaN.setChecked(true);
    		this.chkSintomasUrinariosD.setChecked(false);
    		this.chkLeucocituriaXCampoD.setChecked(false);
    		this.chkNitritosD.setChecked(false);
    		this.chkEritrocitosXCampoD.setChecked(false);
    		this.chkBilirrubinuriaD.setChecked(false);
    	} else {
    		this.chkSintomasUrinariosS.setChecked(false);
    		this.chkLeucocituriaXCampoS.setChecked(false);
    		this.chkNitritosS.setChecked(false);
    		this.chkEritrocitosXCampoS.setChecked(false);
    		this.chkBilirrubinuriaS.setChecked(false);
    		this.chkSintomasUrinariosN.setChecked(false);
    		this.chkLeucocituriaXCampoN.setChecked(false);
    		this.chkNitritosN.setChecked(false);
    		this.chkEritrocitosXCampoN.setChecked(false);
    		this.chkBilirrubinuriaN.setChecked(false);
    		this.chkSintomasUrinariosD.setChecked(true);
    		this.chkLeucocituriaXCampoD.setChecked(true);
    		this.chkNitritosD.setChecked(true);
    		this.chkEritrocitosXCampoD.setChecked(true);
    		this.chkBilirrubinuriaD.setChecked(true);
    	}
    }
    
    public void marcarDesmarcarEstadoNutricional(String valor) {
    	if (valor.trim().equals("S")) {
    		this.chkObesoS.setChecked(true);
    		this.chkSobrepesoS.setChecked(true);
    		this.chkSospechaProblemaS.setChecked(true);
    		this.chkNormalS.setChecked(true);
    		this.chkBajoPesoS.setChecked(true);
    		this.chkBajoPesoSeveroS.setChecked(true);
    		this.chkObesoN.setChecked(false);
    		this.chkSobrepesoN.setChecked(false);
    		this.chkSospechaProblemaN.setChecked(false);
    		this.chkNormalN.setChecked(false);
    		this.chkBajoPesoN.setChecked(false);
    		this.chkBajoPesoSeveroN.setChecked(false);
    		this.chkObesoD.setChecked(false);
    		this.chkSobrepesoD.setChecked(false);
    		this.chkSospechaProblemaD.setChecked(false);
    		this.chkNormalD.setChecked(false);
    		this.chkBajoPesoD.setChecked(false);
    		this.chkBajoPesoSeveroD.setChecked(false);
    	} else if (valor.trim().equals("N")) {
    		this.chkObesoS.setChecked(false);
    		this.chkSobrepesoS.setChecked(false);
    		this.chkSospechaProblemaS.setChecked(false);
    		this.chkNormalS.setChecked(false);
    		this.chkBajoPesoS.setChecked(false);
    		this.chkBajoPesoSeveroS.setChecked(false);
    		this.chkObesoN.setChecked(true);
    		this.chkSobrepesoN.setChecked(true);
    		this.chkSospechaProblemaN.setChecked(true);
    		this.chkNormalN.setChecked(true);
    		this.chkBajoPesoN.setChecked(true);
    		this.chkBajoPesoSeveroN.setChecked(true);	
    		this.chkObesoD.setChecked(false);
    		this.chkSobrepesoD.setChecked(false);
    		this.chkSospechaProblemaD.setChecked(false);
    		this.chkNormalD.setChecked(false);
    		this.chkBajoPesoD.setChecked(false);
    		this.chkBajoPesoSeveroD.setChecked(false);
    	} else {
    		this.chkObesoS.setChecked(false);
    		this.chkSobrepesoS.setChecked(false);
    		this.chkSospechaProblemaS.setChecked(false);
    		this.chkNormalS.setChecked(false);
    		this.chkBajoPesoS.setChecked(false);
    		this.chkBajoPesoSeveroS.setChecked(false);
    		this.chkObesoN.setChecked(false);
    		this.chkSobrepesoN.setChecked(false);
    		this.chkSospechaProblemaN.setChecked(false);
    		this.chkNormalN.setChecked(false);
    		this.chkBajoPesoN.setChecked(false);
    		this.chkBajoPesoSeveroN.setChecked(false);
    		this.chkObesoD.setChecked(true);
    		this.chkSobrepesoD.setChecked(true);
    		this.chkSospechaProblemaD.setChecked(true);
    		this.chkNormalD.setChecked(true);
    		this.chkBajoPesoD.setChecked(true);
    		this.chkBajoPesoSeveroD.setChecked(true);
    	}
    }
    
    public void marcarDesmarcarRespiratorio(String valor) {
    	if (valor.trim().equals("S")) {
    		this.chkTosS.setChecked(true);
    		this.chkRinorreaS.setChecked(true);
    		this.chkCongestionNasalS.setChecked(true);
    		this.chkOtalgiaS.setChecked(true);
    		this.chkAleteoNasalS.setChecked(true);
    		this.chkApneaS.setChecked(true);
    		this.chkRespiracionRapidaS.setChecked(true);
    		this.chkQuejidoEspiratorioS.setChecked(true);
    		this.chkEstridorReposoS.setChecked(true);
    		this.chkTirajeSubcostalS.setChecked(true);
    		this.chkSibilanciasS.setChecked(true);
    		this.chkCrepitosS.setChecked(true);
    		this.chkRoncosS.setChecked(true);
    		this.chkOtraFIFS.setChecked(true);
    		this.txtNuevaFif.setDisabled(false);
    		this.chkTosN.setChecked(false);
    		this.chkRinorreaN.setChecked(false);
    		this.chkCongestionNasalN.setChecked(false);
    		this.chkOtalgiaN.setChecked(false);
    		this.chkAleteoNasalN.setChecked(false);
    		this.chkApneaN.setChecked(false);
    		this.chkRespiracionRapidaN.setChecked(false);
    		this.chkQuejidoEspiratorioN.setChecked(false);
    		this.chkEstridorReposoN.setChecked(false);
    		this.chkTirajeSubcostalN.setChecked(false);
    		this.chkSibilanciasN.setChecked(false);
    		this.chkCrepitosN.setChecked(false);
    		this.chkRoncosN.setChecked(false);
    		this.chkOtraFIFN.setChecked(false);
    		this.chkTosD.setChecked(false);
    		this.chkRinorreaD.setChecked(false);
    		this.chkCongestionNasalD.setChecked(false);
    		this.chkOtalgiaD.setChecked(false);
    	} else if (valor.trim().equals("N")) {
    		this.chkTosS.setChecked(false);
    		this.chkRinorreaS.setChecked(false);
    		this.chkCongestionNasalS.setChecked(false);
    		this.chkOtalgiaS.setChecked(false);
    		this.chkAleteoNasalS.setChecked(false);
    		this.chkApneaS.setChecked(false);
    		this.chkRespiracionRapidaS.setChecked(false);
    		this.chkQuejidoEspiratorioS.setChecked(false);
    		this.chkEstridorReposoS.setChecked(false);
    		this.chkTirajeSubcostalS.setChecked(false);
    		this.chkSibilanciasS.setChecked(false);
    		this.chkCrepitosS.setChecked(false);
    		this.chkRoncosS.setChecked(false);
    		this.chkOtraFIFS.setChecked(false);
    		this.txtNuevaFif.setDisabled(true);
    		this.txtNuevaFif.setValue(null);
    		this.chkTosN.setChecked(true);
    		this.chkRinorreaN.setChecked(true);
    		this.chkCongestionNasalN.setChecked(true);
    		this.chkOtalgiaN.setChecked(true);
    		this.chkAleteoNasalN.setChecked(true);
    		this.chkApneaN.setChecked(true);
    		this.chkRespiracionRapidaN.setChecked(true);
    		this.chkQuejidoEspiratorioN.setChecked(true);
    		this.chkEstridorReposoN.setChecked(true);
    		this.chkTirajeSubcostalN.setChecked(true);
    		this.chkSibilanciasN.setChecked(true);
    		this.chkCrepitosN.setChecked(true);
    		this.chkRoncosN.setChecked(true);
    		this.chkOtraFIFN.setChecked(true);
    		this.chkTosD.setChecked(false);
    		this.chkRinorreaD.setChecked(false);
    		this.chkCongestionNasalD.setChecked(false);
    		this.chkOtalgiaD.setChecked(false);
    	} else {
    		this.chkTosS.setChecked(false);
    		this.chkRinorreaS.setChecked(false);
    		this.chkCongestionNasalS.setChecked(false);
    		this.chkOtalgiaS.setChecked(false);
    		this.chkAleteoNasalS.setChecked(false);
    		this.chkApneaS.setChecked(false);
    		this.chkRespiracionRapidaS.setChecked(false);
    		this.chkQuejidoEspiratorioS.setChecked(false);
    		this.chkEstridorReposoS.setChecked(false);
    		this.chkTirajeSubcostalS.setChecked(false);
    		this.chkSibilanciasS.setChecked(false);
    		this.chkCrepitosS.setChecked(false);
    		this.chkRoncosS.setChecked(false);
    		this.chkOtraFIFS.setChecked(false);
    		this.txtNuevaFif.setDisabled(true);
    		this.txtNuevaFif.setValue(null);
    		this.chkTosN.setChecked(false);
    		this.chkRinorreaN.setChecked(false);
    		this.chkCongestionNasalN.setChecked(false);
    		this.chkOtalgiaN.setChecked(false);
    		this.chkAleteoNasalN.setChecked(false);
    		this.chkApneaN.setChecked(false);
    		this.chkRespiracionRapidaN.setChecked(false);
    		this.chkQuejidoEspiratorioN.setChecked(false);
    		this.chkEstridorReposoN.setChecked(false);
    		this.chkTirajeSubcostalN.setChecked(false);
    		this.chkSibilanciasN.setChecked(false);
    		this.chkCrepitosN.setChecked(false);
    		this.chkRoncosN.setChecked(false);
    		this.chkOtraFIFN.setChecked(false);
    		this.chkTosD.setChecked(true);
    		this.chkRinorreaD.setChecked(true);
    		this.chkCongestionNasalD.setChecked(true);
    		this.chkOtalgiaD.setChecked(true);
    	}
    }
    
    public void marcarDesmarcarReferencia(String valor) {
    	if (valor.trim().equals("S")) {
    		this.chkInterconsultaPediatraS.setChecked(true);
    		this.chkReferenciaHospitalS.setChecked(true);
    		this.chkReferenciaDengueS.setChecked(true);
    		this.chkReferenciaIRAGS.setChecked(true);
    		this.chkReferenciaCHIKS.setChecked(true);
    		this.chkETIS.setChecked(true);
    		this.chkIRAGS.setChecked(true);
    		this.chkNeumoniaS.setChecked(true);
    		this.chkCVS.setChecked(true);
    		this.chkInterconsultaPediatraN.setChecked(false);
    		this.chkReferenciaHospitalN.setChecked(false);
    		this.chkReferenciaDengueN.setChecked(false);
    		this.chkReferenciaIRAGN.setChecked(false);
    		this.chkReferenciaCHIKN.setChecked(false);
    		this.chkETIN.setChecked(false);
    		this.chkIRAGN.setChecked(false);
    		this.chkNeumoniaN.setChecked(false);
    		this.chkCVN.setChecked(false);
    	} else {
    		this.chkInterconsultaPediatraS.setChecked(false);
    		this.chkReferenciaHospitalS.setChecked(false);
    		this.chkReferenciaDengueS.setChecked(false);
    		this.chkReferenciaIRAGS.setChecked(false);
    		this.chkReferenciaCHIKS.setChecked(false);
    		this.chkETIS.setChecked(false);
    		this.chkIRAGS.setChecked(false);
    		this.chkNeumoniaS.setChecked(false);
    		this.chkCVS.setChecked(false);
    		this.chkInterconsultaPediatraN.setChecked(true);
    		this.chkReferenciaHospitalN.setChecked(true);
    		this.chkReferenciaDengueN.setChecked(true);
    		this.chkReferenciaIRAGN.setChecked(true);
    		this.chkReferenciaCHIKN.setChecked(true);
    		this.chkETIN.setChecked(true);
    		this.chkIRAGN.setChecked(true);
    		this.chkNeumoniaN.setChecked(true);
    		this.chkCVN.setChecked(true);
    	}
    }
    
    public void marcarDesmarcarVacunas(String valor) {
    	if (valor.trim().equals("S")) {
    		this.chkLactanciaMaternaS.setChecked(true);
    		this.chkVacunasCompletasS.setChecked(true);
    		this.chkVacunaInfluenzaS.setChecked(true);
    		this.txtFechaVacuna.setDisabled(false);
    		this.chkLactanciaMaternaN.setChecked(false);
    		this.chkVacunasCompletasN.setChecked(false);
    		this.chkVacunaInfluenzaN.setChecked(false);
    		this.chkLactanciaMaternaD.setChecked(false);
    		this.chkVacunasCompletasD.setChecked(false);
    		this.chkVacunaInfluenzaD.setChecked(false);
    	} else if (valor.trim().equals("N")) {
    		this.chkLactanciaMaternaS.setChecked(false);
    		this.chkVacunasCompletasS.setChecked(false);
    		this.chkVacunaInfluenzaS.setChecked(false);
    		this.txtFechaVacuna.setDisabled(true);
    		this.txtFechaVacuna.setValue(null);
    		this.chkLactanciaMaternaN.setChecked(true);
    		this.chkVacunasCompletasN.setChecked(true);
    		this.chkVacunaInfluenzaN.setChecked(true);
    		this.chkLactanciaMaternaD.setChecked(false);
    		this.chkVacunasCompletasD.setChecked(false);
    		this.chkVacunaInfluenzaD.setChecked(false);
    	} else {
    		this.chkLactanciaMaternaS.setChecked(false);
    		this.chkVacunasCompletasS.setChecked(false);
    		this.chkVacunaInfluenzaS.setChecked(false);
    		this.txtFechaVacuna.setDisabled(true);
    		this.txtFechaVacuna.setValue(null);
    		this.chkLactanciaMaternaN.setChecked(false);
    		this.chkVacunasCompletasN.setChecked(false);
    		this.chkVacunaInfluenzaN.setChecked(false);
    		this.chkLactanciaMaternaD.setChecked(true);
    		this.chkVacunasCompletasD.setChecked(true);
    		this.chkVacunaInfluenzaD.setChecked(true);
    	}
    }
    
    public void marcarDesmarcarExamenes(String valor) {
    	if (valor.trim().equals("S")) {
    		this.chkBhcS.setChecked(true);
    		this.chkSerologiaDengueS.setChecked(true);
    		this.chkSerologiaChikS.setChecked(true);
    		this.chkGotaGruesaS.setChecked(true);
    		this.chkExtendidoPerifericoS.setChecked(true);
    		this.chkEgoS.setChecked(true);
    		this.chkEghS.setChecked(true);
    		this.chkCitologiaFecalS.setChecked(true);
    		this.chkFactorReumatoideoS.setChecked(true);
    		this.chkAlbuminaS.setChecked(true);
    		this.chkAstAltS.setChecked(true);
    		this.chkBilirubinasS.setChecked(true);
    		this.chkCpkS.setChecked(true);
    		this.chkColesterolS.setChecked(true);
    		this.chkInfluenzaS.setChecked(true);
    		this.chkOtroExamenS.setChecked(true);
    		this.txtOtroExamen.setDisabled(false);
    		this.chkBhcN.setChecked(false);
    		this.chkSerologiaDengueN.setChecked(false);
    		this.chkSerologiaChikN.setChecked(false);
    		this.chkGotaGruesaN.setChecked(false);
    		this.chkExtendidoPerifericoN.setChecked(false);
    		this.chkEgoN.setChecked(false);
    		this.chkEghN.setChecked(false);
    		this.chkCitologiaFecalN.setChecked(false);
    		this.chkFactorReumatoideoN.setChecked(false);
    		this.chkAlbuminaN.setChecked(false);
    		this.chkAstAltN.setChecked(false);
    		this.chkBilirubinasN.setChecked(false);
    		this.chkCpkN.setChecked(false);
    		this.chkColesterolN.setChecked(false);
    		this.chkInfluenzaN.setChecked(false);
    		this.chkOtroExamenN.setChecked(false);
    	} else {
    		this.chkBhcS.setChecked(false);
    		this.chkSerologiaDengueS.setChecked(false);
    		this.chkSerologiaChikS.setChecked(false);
    		this.chkGotaGruesaS.setChecked(false);
    		this.chkExtendidoPerifericoS.setChecked(false);
    		this.chkEgoS.setChecked(false);
    		this.chkEghS.setChecked(false);
    		this.chkCitologiaFecalS.setChecked(false);
    		this.chkFactorReumatoideoS.setChecked(false);
    		this.chkAlbuminaS.setChecked(false);
    		this.chkAstAltS.setChecked(false);
    		this.chkBilirubinasS.setChecked(false);
    		this.chkCpkS.setChecked(false);
    		this.chkColesterolS.setChecked(false);
    		this.chkInfluenzaS.setChecked(false);
    		this.chkOtroExamenS.setChecked(false);
    		this.txtOtroExamen.setDisabled(true);
    		this.txtOtroExamen.setValue("");
    		this.chkBhcN.setChecked(true);
    		this.chkSerologiaDengueN.setChecked(true);
    		this.chkSerologiaChikN.setChecked(true);
    		this.chkGotaGruesaN.setChecked(true);
    		this.chkExtendidoPerifericoN.setChecked(true);
    		this.chkEgoN.setChecked(true);
    		this.chkEghN.setChecked(true);
    		this.chkCitologiaFecalN.setChecked(true);
    		this.chkFactorReumatoideoN.setChecked(true);
    		this.chkAlbuminaN.setChecked(true);
    		this.chkAstAltN.setChecked(true);
    		this.chkBilirubinasN.setChecked(true);
    		this.chkCpkN.setChecked(true);
    		this.chkColesterolN.setChecked(true);
    		this.chkInfluenzaN.setChecked(true);
    		this.chkOtroExamenN.setChecked(true);
    	}
    }
    
    public void marcarDesmarcarTratamientos(String valor) {
    	if (valor.trim().equals("S")) { 
    		this.chkAcetaminofenS.setChecked(true);
    		this.chkAsaS.setChecked(true);
    		this.chkIbuprofenS.setChecked(true);
    		this.chkPenicilinaS.setChecked(true);
    		this.chkAmoxicilinaS.setChecked(true);
    		this.chkDicloxacilinaS.setChecked(true);
    		this.chkOtroAntibioticoS.setChecked(true);
    		this.txtOtroAntibiotico.setDisabled(false);
    		this.chkFurazolidonaS.setChecked(true);
    		this.chkMetronidazolTinidazolS.setChecked(true);
    		this.chkAlbendazolMebendazolS.setChecked(true);
    		this.chkSulfatoFerrosoS.setChecked(true);
    		this.chkSueroOralS.setChecked(true);
    		this.chkSulfatoZincS.setChecked(true);
    		this.chkLiquidosIVS.setChecked(true);
    		this.chkPrednisonaS.setChecked(true);
    		this.chkHidrocortisonaIVS.setChecked(true);
    		this.chkSalbutamolS.setChecked(true);
    		this.chkOseltamivirS.setChecked(true);
    		
    		this.chkAcetaminofenN.setChecked(false);
    		this.chkAsaN.setChecked(false);
    		this.chkIbuprofenN.setChecked(false);
    		this.chkPenicilinaN.setChecked(false);
    		this.chkAmoxicilinaN.setChecked(false);
    		this.chkDicloxacilinaN.setChecked(false);
    		this.chkOtroAntibioticoN.setChecked(false);
    		this.chkFurazolidonaN.setChecked(false);
    		this.chkMetronidazolTinidazolN.setChecked(false);
    		this.chkAlbendazolMebendazolN.setChecked(false);
    		this.chkSulfatoFerrosoN.setChecked(false);
    		this.chkSueroOralN.setChecked(false);
    		this.chkSulfatoZincN.setChecked(false);
    		this.chkLiquidosIVN.setChecked(false);
    		this.chkPrednisonaN.setChecked(false);
    		this.chkHidrocortisonaIVN.setChecked(false);
    		this.chkSalbutamolN.setChecked(false);
    		this.chkOseltamivirN.setChecked(false);
    		
    	} else {
    		this.chkAcetaminofenS.setChecked(false);
    		this.chkAsaS.setChecked(false);
    		this.chkIbuprofenS.setChecked(false);
    		this.chkPenicilinaS.setChecked(false);
    		this.chkAmoxicilinaS.setChecked(false);
    		this.chkDicloxacilinaS.setChecked(false);
    		this.chkOtroAntibioticoS.setChecked(false);
    		this.txtOtroAntibiotico.setDisabled(true);
    		this.txtOtroAntibiotico.setValue("");
    		this.chkFurazolidonaS.setChecked(false);
    		this.chkMetronidazolTinidazolS.setChecked(false);
    		this.chkAlbendazolMebendazolS.setChecked(false);
    		this.chkSulfatoFerrosoS.setChecked(false);
    		this.chkSueroOralS.setChecked(false);
    		this.chkSulfatoZincS.setChecked(false);
    		this.chkLiquidosIVS.setChecked(false);
    		this.chkPrednisonaS.setChecked(false);
    		this.chkHidrocortisonaIVS.setChecked(false);
    		this.chkSalbutamolS.setChecked(false);
    		this.chkOseltamivirS.setChecked(false);
    		this.chkAcetaminofenN.setChecked(true);
    		this.chkAsaN.setChecked(true);
    		this.chkIbuprofenN.setChecked(true);
    		this.chkPenicilinaN.setChecked(true);
    		this.chkAmoxicilinaN.setChecked(true);
    		this.chkDicloxacilinaN.setChecked(true);
    		this.chkOtroAntibioticoN.setChecked(true);
    		this.chkFurazolidonaN.setChecked(true);
    		this.chkMetronidazolTinidazolN.setChecked(true);
    		this.chkAlbendazolMebendazolN.setChecked(true);
    		this.chkSulfatoFerrosoN.setChecked(true);
    		this.chkSueroOralN.setChecked(true);
    		this.chkSulfatoZincN.setChecked(true);
    		this.chkLiquidosIVN.setChecked(true);
    		this.chkPrednisonaN.setChecked(true);
    		this.chkHidrocortisonaIVN.setChecked(true);
    		this.chkSalbutamolN.setChecked(true);
    		this.chkOseltamivirN.setChecked(true);
    	}
    }
    
    public void marcarDesmarcarCategorias(String valor) {
    	if (valor.trim().equals("S")) {
    		this.chkManifestacionHemorragicaS.setChecked(true);
    		this.chkPruebaTorniquetePositivaS.setChecked(true);
    		this.chkPetequias10S.setChecked(true);
    		this.chkPetequias20S.setChecked(true);
    		this.chkPielExtremidadesFriasS.setChecked(true);
    		this.chkPalidezExtremidadesS.setChecked(true);
    		this.chkEpistaxisS.setChecked(true);
    		this.chkGingivorragiaS.setChecked(true);
    		this.chkPetequiasEspontaneasS.setChecked(true);
    		this.chkLlenadoCapilarS.setChecked(true);
    		this.chkCianosisS.setChecked(true);
    		this.chkHipermenorreaS.setChecked(true);
    		this.chkHematemesisS.setChecked(true);
    		this.chkMelenaS.setChecked(true);
    		this.chkHemoconcentracionS.setChecked(true);
    		this.txtHemoconcentracion.setDisabled(false);
    		
    		this.chkHospitalizadoUltimoAnioS.setChecked(true);
    		this.chkTransfusionSangreS.setChecked(true);
    		this.chkEstaTomandoMedicamentoS.setChecked(true);
    		this.chkTomaMedicamentoDistintoS.setChecked(true);
    		this.txtHospitalizadoUltimoAnio.setDisabled(false);
    		this.txtTransfusionSangre.setDisabled(false);
    		this.txtEstaTomandoMedicamento.setDisabled(false);
    		this.txtTomaMedicamentoDistinto.setDisabled(false);
    		
    		this.chkManifestacionHemorragicaN.setChecked(false);
    		this.chkPruebaTorniquetePositivaN.setChecked(false);
    		this.chkPetequias10N.setChecked(false);
    		this.chkPetequias20N.setChecked(false);
    		this.chkPielExtremidadesFriasN.setChecked(false);
    		this.chkPalidezExtremidadesN.setChecked(false);
    		this.chkEpistaxisN.setChecked(false);
    		this.chkGingivorragiaN.setChecked(false);
    		this.chkPetequiasEspontaneasN.setChecked(false);
    		this.chkLlenadoCapilarN.setChecked(false);
    		this.chkCianosisN.setChecked(false);
    		this.chkHipermenorreaN.setChecked(false);
    		this.chkHematemesisN.setChecked(false);
    		this.chkMelenaN.setChecked(false);
    		this.chkHemoconcentracionN.setChecked(false);
    		
    		this.chkHospitalizadoUltimoAnioN.setChecked(false);
    		this.chkTransfusionSangreN.setChecked(false);
    		this.chkEstaTomandoMedicamentoN.setChecked(false);
    		this.chkTomaMedicamentoDistintoN.setChecked(false);
    		
    		this.chkHipermenorreaD.setChecked(false);
    		this.chkHemoconcentracionD.setChecked(false);
    		
    		
    	} else if (valor.trim().equals("N")) {
    		this.chkManifestacionHemorragicaS.setChecked(false);
    		this.chkPruebaTorniquetePositivaS.setChecked(false);
    		this.chkPetequias10S.setChecked(false);
    		this.chkPetequias20S.setChecked(false);
    		this.chkPielExtremidadesFriasS.setChecked(false);
    		this.chkPalidezExtremidadesS.setChecked(false);
    		this.chkEpistaxisS.setChecked(false);
    		this.chkGingivorragiaS.setChecked(false);
    		this.chkPetequiasEspontaneasS.setChecked(false);
    		this.chkLlenadoCapilarS.setChecked(false);
    		this.chkCianosisS.setChecked(false);
    		this.chkHipermenorreaS.setChecked(false);
    		this.chkHematemesisS.setChecked(false);
    		this.chkMelenaS.setChecked(false);
    		this.chkHemoconcentracionS.setChecked(false);
    		this.txtHemoconcentracion.setDisabled(true);
    		this.txtHemoconcentracion.setValue("");
    		
    		this.chkHospitalizadoUltimoAnioS.setChecked(false);
    		this.chkTransfusionSangreS.setChecked(false);
    		this.chkEstaTomandoMedicamentoS.setChecked(false);
    		this.chkTomaMedicamentoDistintoS.setChecked(false);
    		
    		this.chkManifestacionHemorragicaN.setChecked(true);
    		this.chkPruebaTorniquetePositivaN.setChecked(true);
    		this.chkPetequias10N.setChecked(true);
    		this.chkPetequias20N.setChecked(true);
    		this.chkPielExtremidadesFriasN.setChecked(true);
    		this.chkPalidezExtremidadesN.setChecked(true);
    		this.chkEpistaxisN.setChecked(true);
    		this.chkGingivorragiaN.setChecked(true);
    		this.chkPetequiasEspontaneasN.setChecked(true);
    		this.chkLlenadoCapilarN.setChecked(true);
    		this.chkCianosisN.setChecked(true);
    		this.chkHipermenorreaN.setChecked(true);
    		this.chkHematemesisN.setChecked(true);
    		this.chkMelenaN.setChecked(true);
    		this.chkHemoconcentracionN.setChecked(true);
    		
    		this.chkHospitalizadoUltimoAnioN.setChecked(true);
    		this.chkTransfusionSangreN.setChecked(true);
    		this.chkEstaTomandoMedicamentoN.setChecked(true);
    		this.chkTomaMedicamentoDistintoN.setChecked(true);
    		
    		this.txtTomaMedicamentoDistinto.setDisabled(true);
    		this.txtHospitalizadoUltimoAnio.setDisabled(true);
    		this.txtTransfusionSangre.setDisabled(true);
    		this.txtEstaTomandoMedicamento.setDisabled(true);
    		
    		this.txtTomaMedicamentoDistinto.setValue("");
    		this.txtHospitalizadoUltimoAnio.setValue("");
    		this.txtTransfusionSangre.setValue("");
    		this.txtEstaTomandoMedicamento.setValue("");
    		
    		this.chkHipermenorreaD.setChecked(false);
    		this.chkHemoconcentracionD.setChecked(false);
    		
    	} else {
    		this.chkManifestacionHemorragicaS.setChecked(false);
    		this.chkPruebaTorniquetePositivaS.setChecked(false);
    		this.chkPetequias10S.setChecked(false);
    		this.chkPetequias20S.setChecked(false);
    		this.chkPielExtremidadesFriasS.setChecked(false);
    		this.chkPalidezExtremidadesS.setChecked(false);
    		this.chkEpistaxisS.setChecked(false);
    		this.chkGingivorragiaS.setChecked(false);
    		this.chkPetequiasEspontaneasS.setChecked(false);
    		this.chkLlenadoCapilarS.setChecked(false);
    		this.chkCianosisS.setChecked(false);
    		this.chkHipermenorreaS.setChecked(false);
    		this.chkHematemesisS.setChecked(false);
    		this.chkMelenaS.setChecked(false);
    		this.chkHemoconcentracionS.setChecked(false);
    		this.txtHemoconcentracion.setDisabled(true);
    		this.txtHemoconcentracion.setValue("");
    		
    		this.chkHospitalizadoUltimoAnioS.setChecked(false);
    		this.chkTransfusionSangreS.setChecked(false);
    		this.chkEstaTomandoMedicamentoS.setChecked(false);
    		this.chkTomaMedicamentoDistintoS.setChecked(false);
    		
    		this.chkManifestacionHemorragicaN.setChecked(false);
    		this.chkPruebaTorniquetePositivaN.setChecked(false);
    		this.chkPetequias10N.setChecked(false);
    		this.chkPetequias20N.setChecked(false);
    		this.chkPielExtremidadesFriasN.setChecked(false);
    		this.chkPalidezExtremidadesN.setChecked(false);
    		this.chkEpistaxisN.setChecked(false);
    		this.chkGingivorragiaN.setChecked(false);
    		this.chkPetequiasEspontaneasN.setChecked(false);
    		this.chkLlenadoCapilarN.setChecked(false);
    		this.chkCianosisN.setChecked(false);
    		this.chkHipermenorreaN.setChecked(false);
    		this.chkHematemesisN.setChecked(false);
    		this.chkMelenaN.setChecked(false);
    		this.chkHemoconcentracionN.setChecked(false);
    		
    		this.chkHospitalizadoUltimoAnioN.setChecked(false);
    		this.chkTransfusionSangreN.setChecked(false);
    		this.chkEstaTomandoMedicamentoN.setChecked(false);
    		this.chkTomaMedicamentoDistintoN.setChecked(false);
    		
    		this.txtTomaMedicamentoDistinto.setDisabled(true);
    		this.txtHospitalizadoUltimoAnio.setDisabled(true);
    		this.txtTransfusionSangre.setDisabled(true);
    		this.txtEstaTomandoMedicamento.setDisabled(true);
    		
    		this.txtTomaMedicamentoDistinto.setValue("");
    		this.txtHospitalizadoUltimoAnio.setValue("");
    		this.txtTransfusionSangre.setValue("");
    		this.txtEstaTomandoMedicamento.setValue("");
    		
    		this.chkHipermenorreaD.setChecked(true);
    		this.chkHemoconcentracionD.setChecked(true);
    	}
    }
    
    /**
     * Menejador del evento OK(Enter) del campo "Hora Consulta"
     */
    @Listen("onOK=[id$=txtHoraConsulta]")
    public void txtHoraConsulta_onOk() {
    	this.txtPeso.setFocus(true);
	}
    
    /**
     * Menejador del evento OK(Enter) del campo "Peso"
     */
    @Listen("onOK=[id$=txtPeso]")
    public void txtPeso_onOk() {
    	this.txtTalla.setFocus(true);
	}
    
    /**
     * Menejador del evento OK(Enter) del campo "txtTalla"
     */
    @Listen("onOK=[id$=txtTalla]")
    public void txtTalla_onOk() {
    	this.txtTemp.setFocus(true);
	}
    
    /**
     * Menejador del evento OK(Enter) del campo "txtTemp"
     */
    @Listen("onOK=[id$=txtTemp]")
    public void txtTemp_onOk() {
    	this.txtHora.setFocus(true);
	}
    
    /**
     * Menejador del evento OK(Enter) del campo "txtPas"
     */
    @Listen("onOK=[id$=txtPas]")
    public void txtPas_onOk() {
    	this.txtPad.setFocus(true);
	}
    
    /**
     * Menejador del evento OK(Enter) del campo "txtPad"
     */
    @Listen("onOK=[id$=txtPad]")
    public void txtPad_onOk() {
    	this.txtFciaResp.setFocus(true);
	}
    
    /**
     * Menejador del evento OK(Enter) del campo "txtFciaResp"
     */
    @Listen("onOK=[id$=txtFciaResp]")
    public void txtFciaResp_onOk() {
    	this.txtFciaCard.setFocus(true);
	}
    
    @Listen("onChange=[id$=txtHoraConsulta]")
    public void onChange_txtHoraConsulta() {
    	
    	String txtHoraConsulta = this.txtHoraConsulta.getValue();
    	
    	if (txtHoraConsulta == "" || txtHoraConsulta == null) {
    		return;
    	}
    	
    	boolean result = isValidTime24Hours(txtHoraConsulta);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido, ingresar hora formato 24 horas", Mensajes.TipoMensaje.ERROR);
    		this.txtHoraConsulta.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtPeso]")
    public void onChange_txtPeso() {
    	
    	String txtPeso = this.txtPeso.getValue();
    	
    	if (txtPeso == "" || txtPeso == null) {
    		return;
    	}
    	
    	boolean result = isValidNumeric_5_2(txtPeso);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    		this.txtPeso.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtTalla]")
    public void onChange_txtTalla() {
    	
    	String txtTalla = this.txtTalla.getValue();
    	
    	if (txtTalla == "" || txtTalla == null) {
    		return;
    	}
    	
    	boolean result = isValidNumeric_5_2(txtTalla);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    		this.txtTalla.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtTemp]")
    public void onChange_txtTemp() {
    	
    	String txtTemp = this.txtTemp.getValue();
    	
    	if (txtTemp == "" || txtTemp == null) {
    		return;
    	}
    	
    	boolean result = isValidNumeric_4_2(txtTemp);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    		this.txtTemp.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtHora]")
    public void onChange_txtHora() {
    	
    	String txtHora = this.txtHora.getValue();
    	
    	if (txtHora == "" || txtHora == null) {
    		return;
    	}
    	
    	boolean result = isValidTime12Hours(txtHora);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido, ingresar hora formato 12 horas", Mensajes.TipoMensaje.ERROR);
    		this.txtHora.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtHoraMedico]")
    public void onChange_txtHoraMedico() {
    	
    	String txtHoraMedico = this.txtHoraMedico.getValue();
    	
    	if (txtHoraMedico == "" || txtHoraMedico == null) {
    		return;
    	}
    	
    	boolean result = isValidTime12Hours(txtHoraMedico);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido, ingresar hora formato 12 horas", Mensajes.TipoMensaje.ERROR);
    		this.txtHoraMedico.setFocus(true);
    	}
    }
    
    
    @Listen("onChange=[id$=txtPas]")
    public void onChange_txtPas() {
    	
    	String txtPas = this.txtPas.getValue();
    	
    	if (txtPas == "" || txtPas == null) {
    		return;
    	}
    	
    	boolean result = isValidNumeric(txtPas);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    		this.txtPas.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtPad]")
    public void onChange_txtPad() {
    	
    	String txtPad = this.txtPad.getValue();
    	
    	if (txtPad == "" || txtPad == null) {
    		return;
    	}
    	
    	boolean result = isValidNumeric(txtPad);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    		this.txtPad.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtFciaResp]")
    public void onChange_txtFciaResp() {
    	
    	String txtFciaResp = this.txtFciaResp.getValue();
    	
    	if (txtFciaResp == "" || txtFciaResp == null) {
    		return;
    	}
    	
    	boolean result = isValidNumeric(txtFciaResp);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    		this.txtFciaResp.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtFciaCard]")
    public void onChange_txtFciaCard() {
    	
    	String txtFciaCard = this.txtFciaCard.getValue();
    	
    	if (txtFciaCard == "" || txtFciaCard == null) {
    		return;
    	}
    	
    	boolean result = isValidNumeric(txtFciaCard);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    		this.txtFciaCard.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtHoraUltDosisAntipiretico]")
    public void onChange_txtHoraUltDosisAntipiretico() {
    	
    	String txtHoraUltDosisAntipiretico = this.txtHoraUltDosisAntipiretico.getValue();
    	
    	if (txtHoraUltDosisAntipiretico == "" || txtHoraUltDosisAntipiretico == null) {
    		return;
    	}
    	
    	boolean result = isValidTime12Hours(txtHoraUltDosisAntipiretico);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido, ingresar hora formato 12 horas", Mensajes.TipoMensaje.ERROR);
    		this.txtHoraUltDosisAntipiretico.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtImc]")
    public void onChange_txtImc() {
    	
    	String txtImc = this.txtImc.getValue();
    	
    	if (txtImc == "" || txtImc == null) {
    		return;
    	}
    	
    	boolean result = isValidNumeric_5_2(txtImc);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    		this.txtImc.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtSaturacion]")
    public void onChange_txtSaturacion() {
    	
    	String txtSaturacion = this.txtSaturacion.getValue();
    	
    	if (txtSaturacion == "" || txtSaturacion == null) {
    		return;
    	}
    	
    	boolean result = isValidNumeric(txtSaturacion);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    		this.txtSaturacion.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtLinfocitoAtipicos]")
    public void onChange_txtLinfocitoAtipicos() {
    	
    	String txtLinfocitoAtipicos = this.txtLinfocitoAtipicos.getValue();
    	
    	if (txtLinfocitoAtipicos == "" || txtLinfocitoAtipicos == null) {
    		return;
    	}
    	
    	boolean result = isValidNumeric_5_2(txtLinfocitoAtipicos);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    		this.txtLinfocitoAtipicos.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtHemoconcentracion]")
    public void onChange_txtHemoconcentracion() {
    	
    	String txtHemoconcentracion = this.txtHemoconcentracion.getValue();
    	
    	if (txtHemoconcentracion == "" || txtHemoconcentracion == null) {
    		return;
    	}
    	
    	boolean result = isValidNumeric(txtHemoconcentracion);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    		this.txtHemoconcentracion.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtTelefono]")
    public void onChange_txtTelefono() {
    	
    	String txtTelefono = this.txtTelefono.getValue();
    	
    	if (txtTelefono == "" || txtTelefono == null) {
    		return;
    	}
    	
    	boolean result = isValidNumeric(txtTelefono);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    		this.txtTelefono.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtHoraFechaCierre]")
    public void onChange_txtHoraFechaCierre() {
    	
    	String txtHoraFechaCierre = this.txtHoraFechaCierre.getValue();
    	
    	if (txtHoraFechaCierre == "" || txtHoraFechaCierre == null) {
    		return;
    	}
    	
    	boolean result = isValidTime24Hours(txtHoraFechaCierre);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido, ingresar hora formato 24 horas", Mensajes.TipoMensaje.ERROR);
    		this.txtHoraFechaCierre.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtVomito12Horas]")
    public void onChange_txtVomito12Horas() {
    	
    	String txtVomito12Horas = this.txtVomito12Horas.getValue();
    	
    	if (txtVomito12Horas == "" || txtVomito12Horas == null) {
    		return;
    	}
    	
    	boolean result = isValidNumeric(txtVomito12Horas);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    		this.txtVomito12Horas.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=txtHepatomegalia]")
    public void onChange_txtHepatomegalia() {
    	
    	String txtHepatomegalia = this.txtHepatomegalia.getValue();
    	
    	if (txtHepatomegalia == "" || txtHepatomegalia == null) {
    		return;
    	}
    	
    	boolean result = isValidNumeric_5_2(txtHepatomegalia);
    	if (!result) {
    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    		this.txtHepatomegalia.setFocus(true);
    	}
    }
    
    @Listen("onChange=[id$=cmbDiagnostico1]")
    public void onChange_cmbDiagnostico1() {
    	activarDesactivarOtroDiagnostico();
    }
    
    @Listen("onChange=[id$=cmbDiagnostico2]")
    public void onChange_cmbDiagnostico2() {
    	activarDesactivarOtroDiagnostico();
    }
    
    @Listen("onChange=[id$=cmbDiagnostico3]")
    public void onChange_cmbDiagnostico3() {
    	activarDesactivarOtroDiagnostico();
    }
    
    @Listen("onChange=[id$=cmbDiagnostico4]")
    public void onChange_cmbDiagnostico4() {
    	activarDesactivarOtroDiagnostico();
    }
    
    public void activarDesactivarOtroDiagnostico() {
    	String diagnostico1 = "0";
    	String diagnostico2 = "0";
    	String diagnostico3 = "0";
    	String diagnostico4 = "0";
    	if (this.cmbDiagnostico1.getSelectedItem() != null) {
    		diagnostico1 = this.cmbDiagnostico1.getSelectedItem().getValue().toString();
    	}
    	if (this.cmbDiagnostico2.getSelectedItem() != null) {
    		diagnostico2 = this.cmbDiagnostico2.getSelectedItem().getValue().toString();
    	}
    	if (this.cmbDiagnostico3.getSelectedItem() != null) {
    		diagnostico3 = this.cmbDiagnostico3.getSelectedItem().getValue().toString();
    	}
    	if (this.cmbDiagnostico4.getSelectedItem() != null) {
    		diagnostico4 = this.cmbDiagnostico4.getSelectedItem().getValue().toString();
    	}
    	if (diagnostico1.trim().equals("50") || diagnostico2.trim().equals("50") || diagnostico3.trim().equals("50")
    			|| diagnostico4.trim().equals("50")) {
    		this.txtOtroDiagnostico.setDisabled(false);
    	}
    	
    	if (!diagnostico1.trim().equals("50") && !diagnostico2.trim().equals("50") && !diagnostico3.trim().equals("50")
    			&& !diagnostico4.trim().equals("50")) {
    		this.txtOtroDiagnostico.setDisabled(true);
    		this.txtOtroDiagnostico.setValue("");
    	}
    }
    
    
    /*Metodo para evaluar si el formato de la hora es de 24*/
    public static boolean isValidTime24Hours(String time)
    {
    	String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    	Pattern p = Pattern.compile(regex);
    	Matcher m = p.matcher(time);
    	
    	return m.matches();
    }
    
    /*Metodo para evaluar si el formato de la hora es de 12*/
    public static boolean isValidTime12Hours(String time)
    {
    	String regex = "([0-9]|0[0-9]|1[0-2]):[0-5][0-9]";
    	Pattern p = Pattern.compile(regex);
    	Matcher m = p.matcher(time);
    	
    	return m.matches();
    }
    
    /*Metodo para evaluar si el formato es numerico 5,2*/
    public static boolean isValidNumeric_5_2(String valor) {
    	String regex = "^(?:\\d{0,5}\\.\\d{1,2})$|^\\d{0,5}";
    	Pattern p = Pattern.compile(regex);
    	Matcher m = p.matcher(valor);
    	
    	return m.matches();
    }
    
    /*Metodo para evaluar si el formato es numerico 4,2*/
    public static boolean isValidNumeric_4_2(String valor) {
    	String regex = "^(?:\\d{0,4}\\.\\d{1,2})$|^\\d{0,4}";
    	Pattern p = Pattern.compile(regex);
    	Matcher m = p.matcher(valor);
    	
    	return m.matches();
    }
    
    /*Metodo para evaluar si el formato es numerico*/
    public static boolean isValidNumeric(String valor) {
    	String regex = "^\\d+$";
    	Pattern p = Pattern.compile(regex);
    	Matcher m = p.matcher(valor);
    	
    	return m.matches();
    } 
    
    /**
     * Método que reinicia los valores de los campos en el formulario
     */
    private void limpiarCampos() {
    	this.txtCodExpediente.setFocus(true);
    	this.txtCodExpediente.setValue(null);
    	this.txtNombrePaciente.setValue("");
    	this.txtEstudiosPaciente.setValue("");
    	this.txtExpediente.setValue("");
    	this.txtEdad.setValue("");
    	this.txtSexo.setValue("");
    	this.txtFechaConsulta.setValue(null);
    	this.txtHoraConsulta.setValue("");
    	this.txtPeso.setValue("");
    	this.txtTalla.setValue("");
    	this.txtTemp.setValue("");
    	this.txtHora.setValue("");
    	this.txtHoraMedico.setValue("");
    	this.cmbHora.setValue(null);
    	this.cmbHoraMedico.setValue(null);
    	this.txtPas.setValue("");
    	this.txtPad.setValue("");
    	this.txtFciaResp.setValue("");
    	this.txtFciaCard.setValue("");
    	this.chkCssfv.setChecked(false);
    	this.chkTerreno.setChecked(false);
    	this.chkCInicial.setChecked(false);
    	this.chkCSeguimiento.setChecked(false);
    	this.chkCConvaleciente.setChecked(false);
    	this.chkSChik1.setChecked(false);
    	this.chkSChik2.setChecked(false);
    	this.chkSChik3.setChecked(false);
    	this.chkSChik4.setChecked(false);
    	this.chkSChik5.setChecked(false);
    	this.chkRegular.setChecked(false);
    	this.chkNoche.setChecked(false);
    	this.chkFinSemana.setChecked(false);
		/*Estado General*/
    	this.chkFiebreS.setChecked(false);
		this.chkAsteniaS.setChecked(false);
		this.chkAnormalMenteSomnolientoS.setChecked(false);
		this.chkMalEstadoGeneralS.setChecked(false);
		this.chkPerdidaConcienciaS.setChecked(false);
		this.chkInquietoIrritableS.setChecked(false);
		this.chkConvulsionesS.setChecked(false);
		this.chkHipotermiaS.setChecked(false);
		this.chkLetargiaS.setChecked(false);
		this.chkFiebreN.setChecked(false);
		this.chkAsteniaN.setChecked(false);
		this.chkAnormalMenteSomnolientoN.setChecked(false);
		this.chkMalEstadoGeneralN.setChecked(false);
		this.chkPerdidaConcienciaN.setChecked(false);
		this.chkInquietoIrritableN.setChecked(false);
		this.chkConvulsionesN.setChecked(false);
		this.chkHipotermiaN.setChecked(false);
		this.chkLetargiaN.setChecked(false);
		this.chkFiebreD.setChecked(false);
		this.chkAsteniaD.setChecked(false);
		/*Gastrointestinal*/
		this.chkPocoApetitoS.setChecked(false);
    	this.chkNauseaS.setChecked(false);
    	this.chkDificultadAlimentaseS.setChecked(false);
    	this.chkVomito12HorasS.setChecked(false);
    	this.txtVomito12Horas.setDisabled(true);
    	this.txtVomito12Horas.setValue("");
    	this.chkDiarreaS.setChecked(false);
    	this.chkDiarreaSangreS.setChecked(false);
    	this.chkEstrenimientoS.setChecked(false);
    	this.chkDolorAbInterS.setChecked(false);
    	this.chkDolorAbContinuoS.setChecked(false);
    	this.chkEpigastralgiaS.setChecked(false);
    	this.chkIntoleranciaViaOralS.setChecked(false);
    	this.chkDistensionAbdominalS.setChecked(false);
    	this.chkHepatomegaliaS.setChecked(false);
    	this.txtHepatomegalia.setDisabled(true);
    	this.txtHepatomegalia.setValue("");
    	this.chkPocoApetitoN.setChecked(false);
    	this.chkNauseaN.setChecked(false);
    	this.chkDificultadAlimentaseN.setChecked(false);
    	this.chkVomito12HorasN.setChecked(false);
    	this.chkDiarreaN.setChecked(false);
    	this.chkDiarreaSangreN.setChecked(false);
    	this.chkEstrenimientoN.setChecked(false);
    	this.chkDolorAbInterN.setChecked(false);
    	this.chkDolorAbContinuoN.setChecked(false);
    	this.chkEpigastralgiaN.setChecked(false);
    	this.chkIntoleranciaViaOralN.setChecked(false);
    	this.chkDistensionAbdominalN.setChecked(false);
    	this.chkHepatomegaliaN.setChecked(false);
    	this.chkPocoApetitoD.setChecked(false);
    	this.chkNauseaD.setChecked(false);
    	this.chkDificultadAlimentaseD.setChecked(false);
    	this.chkVomito12HorasD.setChecked(false);
    	this.chkDiarreaD.setChecked(false);
    	this.chkDiarreaSangreD.setChecked(false);
    	this.chkEstrenimientoD.setChecked(false);
    	this.chkDolorAbInterD.setChecked(false);
    	this.chkDolorAbContinuoD.setChecked(false);
    	this.chkEpigastralgiaD.setChecked(false);
    	this.chkIntoleranciaViaOralD.setChecked(false);
    	this.chkDistensionAbdominalD.setChecked(false);
		/*Osteomuscular*/
		this.chkArtralgiaS.setChecked(false);
    	this.chkMialgiaS.setChecked(false);
    	this.chkLumbalgiaS.setChecked(false);
    	this.chkDolorCuelloS.setChecked(false);
    	this.chkTenosinovitisS.setChecked(false);
    	this.chkArtralgiaProximalS.setChecked(false);
    	this.chkArtralgiaDistalS.setChecked(false);
    	this.chkConjuntivitisS.setChecked(false);
    	this.chkEdemaMunecasS.setChecked(false);
    	this.chkEdemaCodosS.setChecked(false);
    	this.chkEdemaHombrosS.setChecked(false);
    	this.chkEdemaRodillasS.setChecked(false);
    	this.chkEdemaTobillosS.setChecked(false);
    	this.chkArtralgiaN.setChecked(false);
    	this.chkMialgiaN.setChecked(false);
    	this.chkLumbalgiaN.setChecked(false);
    	this.chkDolorCuelloN.setChecked(false);
    	this.chkTenosinovitisN.setChecked(false);
    	this.chkArtralgiaProximalN.setChecked(false);
    	this.chkArtralgiaDistalN.setChecked(false);
    	this.chkConjuntivitisN.setChecked(false);
    	this.chkEdemaMunecasN.setChecked(false);
    	this.chkEdemaCodosN.setChecked(false);
    	this.chkEdemaHombrosN.setChecked(false);
    	this.chkEdemaRodillasN.setChecked(false);
    	this.chkEdemaTobillosN.setChecked(false);
    	this.chkArtralgiaD.setChecked(false);
    	this.chkMialgiaD.setChecked(false);
    	this.chkLumbalgiaD.setChecked(false);
    	this.chkDolorCuelloD.setChecked(false);
    	this.chkTenosinovitisD.setChecked(false);
    	this.chkArtralgiaProximalD.setChecked(false);
    	this.chkArtralgiaDistalD.setChecked(false);
    	this.chkConjuntivitisD.setChecked(false);
    	this.chkEdemaMunecasD.setChecked(false);
    	this.chkEdemaCodosD.setChecked(false);
    	this.chkEdemaHombrosD.setChecked(false);
    	this.chkEdemaRodillasD.setChecked(false);
    	this.chkEdemaTobillosD.setChecked(false);
		/*Cabeza*/
		this.chkCefaleaS.setChecked(false);
    	this.chkRigidezCuelloS.setChecked(false);
    	this.chkInyecciónConjuntivalS.setChecked(false);
    	this.chkHemorragiaSubconjuntivalS.setChecked(false);
    	this.chkDolorRetroocularS.setChecked(false);
    	this.chkFontanelaAbombadaS.setChecked(false);
    	this.chkIctericiaConjuntivalS.setChecked(false);
    	this.chkCefaleaN.setChecked(false);
    	this.chkRigidezCuelloN.setChecked(false);
    	this.chkInyecciónConjuntivalN.setChecked(false);
    	this.chkHemorragiaSubconjuntivalN.setChecked(false);
    	this.chkDolorRetroocularN.setChecked(false);
    	this.chkFontanelaAbombadaN.setChecked(false);
    	this.chkIctericiaConjuntivalN.setChecked(false);
    	this.chkCefaleaD.setChecked(false);
    	this.chkDolorRetroocularD.setChecked(false);
		/*Deshidratación*/
		this.chkLenguaMucosaSecasS.setChecked(false);
    	this.chkPliegueCutaneoS.setChecked(false);
    	this.chkOrinaReducidaS.setChecked(false);
    	this.chkBebeAvidoSedS.setChecked(false);
    	this.chkOjosHundidosS.setChecked(false);
    	this.chkFontanelaHundidaS.setChecked(false);
    	this.chkLenguaMucosaSecasN.setChecked(false);
    	this.chkPliegueCutaneoN.setChecked(false);
    	this.chkOrinaReducidaN.setChecked(false);
    	this.chkBebeAvidoSedN.setChecked(false);
    	this.chkOjosHundidosN.setChecked(false);
    	this.chkFontanelaHundidaN.setChecked(false);
    	this.chkOrinaReducidaD.setChecked(false);
		/*Cutáneo*/
		this.chkRashLocalizadoS.setChecked(false);
    	this.chkRashGeneralizadoS.setChecked(false);
    	this.chkRashEritematosoS.setChecked(false);
    	this.chkRashMacularS.setChecked(false);
    	this.chkRashPapularS.setChecked(false);
    	this.chkPielMoteadaS.setChecked(false);
    	this.chkRuborFacialS.setChecked(false);
    	this.chkEquimosisS.setChecked(false);
    	this.chkCianosisCentralS.setChecked(false);
    	this.chkIctericiaS.setChecked(false);
    	this.chkRashLocalizadoN.setChecked(false);
    	this.chkRashGeneralizadoN.setChecked(false);
    	this.chkRashEritematosoN.setChecked(false);
    	this.chkRashMacularN.setChecked(false);
    	this.chkRashPapularN.setChecked(false);
    	this.chkPielMoteadaN.setChecked(false);
    	this.chkRuborFacialN.setChecked(false);
    	this.chkEquimosisN.setChecked(false);
    	this.chkCianosisCentralN.setChecked(false);
    	this.chkIctericiaN.setChecked(false);
		/*Garganta*/
		this.chkEritemaS.setChecked(false);
    	this.chkDolorGargantaS.setChecked(false);
    	this.chkAdenopatiasCervicalesS.setChecked(false);
    	this.chkExudadoS.setChecked(false);
    	this.chkPetequiasMucosaS.setChecked(false);
    	this.chkEritemaN.setChecked(false);
    	this.chkDolorGargantaN.setChecked(false);
    	this.chkAdenopatiasCervicalesN.setChecked(false);
    	this.chkExudadoN.setChecked(false);
    	this.chkPetequiasMucosaN.setChecked(false);
    	this.chkDolorGargantaD.setChecked(false);
		/*Renal*/
		this.chkSintomasUrinariosS.setChecked(false);
    	this.chkLeucocituriaXCampoS.setChecked(false);
    	this.chkNitritosS.setChecked(false);
    	this.chkEritrocitosXCampoS.setChecked(false);
    	this.chkBilirrubinuriaS.setChecked(false);
    	this.chkSintomasUrinariosN.setChecked(false);
    	this.chkLeucocituriaXCampoN.setChecked(false);
    	this.chkNitritosN.setChecked(false);
    	this.chkEritrocitosXCampoN.setChecked(false);
    	this.chkBilirrubinuriaN.setChecked(false);
    	this.chkSintomasUrinariosD.setChecked(false);
    	this.chkLeucocituriaXCampoD.setChecked(false);
    	this.chkNitritosD.setChecked(false);
    	this.chkEritrocitosXCampoD.setChecked(false);
    	this.chkBilirrubinuriaD.setChecked(false);
		/*Estado Nutricional*/
		this.chkObesoS.setChecked(false);
    	this.chkSobrepesoS.setChecked(false);
    	this.chkSospechaProblemaS.setChecked(false);
    	this.chkNormalS.setChecked(false);
    	this.chkBajoPesoS.setChecked(false);
    	this.chkBajoPesoSeveroS.setChecked(false);
    	this.chkObesoN.setChecked(false);
    	this.chkSobrepesoN.setChecked(false);
    	this.chkSospechaProblemaN.setChecked(false);
    	this.chkNormalN.setChecked(false);
    	this.chkBajoPesoN.setChecked(false);
    	this.chkBajoPesoSeveroN.setChecked(false);	
    	this.chkObesoD.setChecked(false);
    	this.chkSobrepesoD.setChecked(false);
    	this.chkSospechaProblemaD.setChecked(false);
    	this.chkNormalD.setChecked(false);
    	this.chkBajoPesoD.setChecked(false);
    	this.chkBajoPesoSeveroD.setChecked(false);
    	this.txtImc.setValue("");
		/*Respiratorio*/
		this.chkTosS.setChecked(false);
    	this.chkRinorreaS.setChecked(false);
    	this.chkCongestionNasalS.setChecked(false);
    	this.chkOtalgiaS.setChecked(false);
    	this.chkAleteoNasalS.setChecked(false);
    	this.chkApneaS.setChecked(false);
    	this.chkRespiracionRapidaS.setChecked(false);
    	this.chkQuejidoEspiratorioS.setChecked(false);
    	this.chkEstridorReposoS.setChecked(false);
    	this.chkTirajeSubcostalS.setChecked(false);
    	this.chkSibilanciasS.setChecked(false);
    	this.chkCrepitosS.setChecked(false);
    	this.chkRoncosS.setChecked(false);
    	this.chkOtraFIFS.setChecked(false);
    	this.txtNuevaFif.setDisabled(true);
    	this.txtNuevaFif.setValue(null);
    	this.chkTosN.setChecked(false);
    	this.chkRinorreaN.setChecked(false);
    	this.chkCongestionNasalN.setChecked(false);
    	this.chkOtalgiaN.setChecked(false);
    	this.chkAleteoNasalN.setChecked(false);
    	this.chkApneaN.setChecked(false);
    	this.chkRespiracionRapidaN.setChecked(false);
    	this.chkQuejidoEspiratorioN.setChecked(false);
    	this.chkEstridorReposoN.setChecked(false);
    	this.chkTirajeSubcostalN.setChecked(false);
    	this.chkSibilanciasN.setChecked(false);
    	this.chkCrepitosN.setChecked(false);
    	this.chkRoncosN.setChecked(false);
    	this.chkOtraFIFN.setChecked(false);
    	this.chkTosD.setChecked(false);
    	this.chkRinorreaD.setChecked(false);
    	this.chkCongestionNasalD.setChecked(false);
    	this.chkOtalgiaD.setChecked(false);
		/*Referencia*/
		this.chkInterconsultaPediatraS.setChecked(false);
    	this.chkReferenciaHospitalS.setChecked(false);
    	this.chkReferenciaDengueS.setChecked(false);
    	this.chkReferenciaIRAGS.setChecked(false);
    	this.chkReferenciaCHIKS.setChecked(false);
    	this.chkETIS.setChecked(false);
    	this.chkIRAGS.setChecked(false);
    	this.chkNeumoniaS.setChecked(false);
    	this.chkInterconsultaPediatraN.setChecked(false);
    	this.chkReferenciaHospitalN.setChecked(false);
    	this.chkReferenciaDengueN.setChecked(false);
    	this.chkReferenciaIRAGN.setChecked(false);
    	this.chkReferenciaCHIKN.setChecked(false);
    	this.chkETIN.setChecked(false);
    	this.chkIRAGN.setChecked(false);
    	this.chkNeumoniaN.setChecked(false);
		/*Vacunas*/
		this.chkLactanciaMaternaS.setChecked(false);
    	this.chkVacunasCompletasS.setChecked(false);
    	this.chkVacunaInfluenzaS.setChecked(false);
    	this.txtFechaVacuna.setDisabled(true);
    	this.txtFechaVacuna.setValue(null);
    	this.chkLactanciaMaternaN.setChecked(false);
    	this.chkVacunasCompletasN.setChecked(false);
    	this.chkVacunaInfluenzaN.setChecked(false);
    	this.chkLactanciaMaternaD.setChecked(false);
    	this.chkVacunasCompletasD.setChecked(false);
    	this.chkVacunaInfluenzaD.setChecked(false);
		/*Categoría*/
		this.chkManifestacionHemorragicaS.setChecked(false);
    	this.chkPruebaTorniquetePositivaS.setChecked(false);
    	this.chkPetequias10S.setChecked(false);
    	this.chkPetequias20S.setChecked(false);
    	this.chkPielExtremidadesFriasS.setChecked(false);
    	this.chkPalidezExtremidadesS.setChecked(false);
    	this.chkEpistaxisS.setChecked(false);
    	this.chkGingivorragiaS.setChecked(false);
    	this.chkPetequiasEspontaneasS.setChecked(false);
    	this.chkLlenadoCapilarS.setChecked(false);
    	this.chkCianosisS.setChecked(false);
    	this.chkHipermenorreaS.setChecked(false);
    	this.chkHematemesisS.setChecked(false);
    	this.chkMelenaS.setChecked(false);
    	this.chkHemoconcentracionS.setChecked(false);
    	this.txtHemoconcentracion.setDisabled(true);
    	this.txtHemoconcentracion.setValue("");
    	this.chkManifestacionHemorragicaN.setChecked(false);
    	this.chkPruebaTorniquetePositivaN.setChecked(false);
    	this.chkPetequias10N.setChecked(false);
    	this.chkPetequias20N.setChecked(false);
    	this.chkPielExtremidadesFriasN.setChecked(false);
    	this.chkPalidezExtremidadesN.setChecked(false);
    	this.chkEpistaxisN.setChecked(false);
    	this.chkGingivorragiaN.setChecked(false);
    	this.chkPetequiasEspontaneasN.setChecked(false);
    	this.chkLlenadoCapilarN.setChecked(false);
    	this.chkCianosisN.setChecked(false);
    	this.chkHipermenorreaN.setChecked(false);
    	this.chkHematemesisN.setChecked(false);
    	this.chkMelenaN.setChecked(false);
    	this.chkHemoconcentracionN.setChecked(false);
    	this.chkHipermenorreaD.setChecked(false);
    	this.chkHemoconcentracionD.setChecked(false);
    	this.txtSaturacion.setValue("");
    	this.cmbCategoria.setValue(null);
    	this.cmbCambioCategoria.setValue(null);
    	this.txtLinfocitoAtipicos.setValue("");
    	this.txtFechaLinfocitoAtipicos.setValue(null);
    	
    	this.chkHospitalizadoUltimoAnioS.setChecked(false);
    	this.chkHospitalizadoUltimoAnioN.setChecked(false);
    	this.txtHospitalizadoUltimoAnio.setDisabled(true);
    	this.txtHospitalizadoUltimoAnio.setValue("");
    	this.chkTransfusionSangreS.setChecked(false);
    	this.chkTransfusionSangreN.setChecked(false);
    	this.txtTransfusionSangre.setDisabled(true);
    	this.txtTransfusionSangre.setValue("");
    	this.chkEstaTomandoMedicamentoS.setChecked(false);
    	this.chkEstaTomandoMedicamentoN.setChecked(false);
    	this.txtEstaTomandoMedicamento.setDisabled(true);
    	this.txtEstaTomandoMedicamento.setValue("");
    	this.chkTomaMedicamentoDistintoS.setChecked(false);
    	this.chkTomaMedicamentoDistintoN.setChecked(false);
    	this.txtTomaMedicamentoDistinto.setDisabled(true);
    	this.txtTomaMedicamentoDistinto.setValue("");
		/*Exámenes*/
		this.chkBhcS.setChecked(false);
    	this.chkSerologiaDengueS.setChecked(false);
    	this.chkSerologiaChikS.setChecked(false);
    	this.chkGotaGruesaS.setChecked(false);
    	this.chkExtendidoPerifericoS.setChecked(false);
    	this.chkEgoS.setChecked(false);
    	this.chkEghS.setChecked(false);
    	this.chkCitologiaFecalS.setChecked(false);
    	this.chkFactorReumatoideoS.setChecked(false);
    	this.chkAlbuminaS.setChecked(false);
    	this.chkAstAltS.setChecked(false);
    	this.chkBilirubinasS.setChecked(false);
    	this.chkCpkS.setChecked(false);
    	this.chkColesterolS.setChecked(false);
    	this.chkInfluenzaS.setChecked(false);
    	this.chkOtroExamenS.setChecked(false);
    	this.txtOtroExamen.setDisabled(true);
    	this.txtOtroExamen.setValue("");
    	this.chkBhcN.setChecked(false);
    	this.chkSerologiaDengueN.setChecked(false);
    	this.chkSerologiaChikN.setChecked(false);
    	this.chkGotaGruesaN.setChecked(false);
    	this.chkExtendidoPerifericoN.setChecked(false);
    	this.chkEgoN.setChecked(false);
    	this.chkEghN.setChecked(false);
    	this.chkCitologiaFecalN.setChecked(false);
    	this.chkFactorReumatoideoN.setChecked(false);
    	this.chkAlbuminaN.setChecked(false);
    	this.chkAstAltN.setChecked(false);
    	this.chkBilirubinasN.setChecked(false);
    	this.chkCpkN.setChecked(false);
    	this.chkColesterolN.setChecked(false);
    	this.chkInfluenzaN.setChecked(false);
    	this.chkOtroExamenN.setChecked(false);
		/*Tratamiento*/
		this.chkAcetaminofenS.setChecked(false);
    	this.chkAsaS.setChecked(false);
    	this.chkIbuprofenS.setChecked(false);
    	this.chkPenicilinaS.setChecked(false);
    	this.chkAmoxicilinaS.setChecked(false);
    	this.chkDicloxacilinaS.setChecked(false);
    	this.chkOtroAntibioticoS.setChecked(false);
    	this.txtOtroAntibiotico.setDisabled(true);
    	this.txtOtroAntibiotico.setValue("");
    	this.chkFurazolidonaS.setChecked(false);
    	this.chkMetronidazolTinidazolS.setChecked(false);
    	this.chkAlbendazolMebendazolS.setChecked(false);
    	this.chkSulfatoFerrosoS.setChecked(false);
    	this.chkSueroOralS.setChecked(false);
    	this.chkSulfatoZincS.setChecked(false);
    	this.chkLiquidosIVS.setChecked(false);
    	this.chkPrednisonaS.setChecked(false);
    	this.chkHidrocortisonaIVS.setChecked(false);
    	this.chkSalbutamolS.setChecked(false);
    	this.chkOseltamivirS.setChecked(false);
    	this.chkAcetaminofenN.setChecked(false);
    	this.chkAsaN.setChecked(false);
    	this.chkIbuprofenN.setChecked(false);
    	this.chkPenicilinaN.setChecked(false);
    	this.chkAmoxicilinaN.setChecked(false);
    	this.chkDicloxacilinaN.setChecked(false);
    	this.chkOtroAntibioticoN.setChecked(false);
    	this.chkFurazolidonaN.setChecked(false);
    	this.chkMetronidazolTinidazolN.setChecked(false);
    	this.chkAlbendazolMebendazolN.setChecked(false);
    	this.chkSulfatoFerrosoN.setChecked(false);
    	this.chkSueroOralN.setChecked(false);
    	this.chkSulfatoZincN.setChecked(false);
    	this.chkLiquidosIVN.setChecked(false);
    	this.chkPrednisonaN.setChecked(false);
    	this.chkHidrocortisonaIVN.setChecked(false);
    	this.chkSalbutamolN.setChecked(false);
    	this.chkOseltamivirN.setChecked(false);
    	
    	this.txtHistoriaExamenFisico.setValue("");
    	this.txtPlanes.setValue("");
    	this.cmbDiagnostico1.setSelectedIndex(0);
    	this.cmbDiagnostico2.setSelectedIndex(0);
    	this.cmbDiagnostico3.setSelectedIndex(0);
    	this.cmbDiagnostico4.setSelectedIndex(0);
    	this.txtOtroDiagnostico.setValue("");
    	this.txtOtroDiagnostico.setDisabled(true);
    	this.txtTelefono.setText("");
    	this.txtFechaProximaCita.setValue(null);
    	this.cmbColegio.setSelectedIndex(0);
    	this.cmbHorarioClases.setValue(null);
    	this.cmbMedico.setSelectedIndex(0);
    	this.cmbEnfermeria.setSelectedIndex(0);
    	this.cmbSupervisor.setSelectedIndex(0);
    	this.txtFechaCierre.setValue(null);
    	this.txtHoraFechaCierre.setValue("");
    	this.txtFis.setValue(null);
    	this.txtFif.setValue(null);
    	this.txtUltDiaFiebre.setValue(null);
    	this.cmbUltDiaFiebre.setValue(null);
    	this.txtUltDosisAntipiretico.setValue(null);
    	this.txtHoraUltDosisAntipiretico.setValue("");
    	this.cmbUltDosisAntipiretico.setValue(null);
    	this.chkCVS.setChecked(false);
    	this.chkCVN.setChecked(false);
    	this.txtNumHojaConsulta.setValue(null);
    	this.txtSecHojaConsulta.setValue(null);
    	
    }
    
    @Listen("onClick=[id$=btnNuevo]")
    public void btnNuevo_onClick() {
    	if(Messagebox.show("¿Está seguro que ingresara una nueva hoja de consulta?", "Validación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION) == Messagebox.NO) {
    		return;
		} else {
			this.btnGuardar.setDisabled(false);
			limpiarCampos();
		}
    }
    
    @Listen("onClick=[id$=btnImprimir]")
    public void btnImprimir_onClick() {
    	//hojaConsultaService.reImprimirHojaConsultaPdf(Integer.valueOf(this.txtSecHojaConsulta.getValue()), 0);
    	try {
    		if (this.txtSecHojaConsulta.getValue() != "" && this.txtSecHojaConsulta.getValue() != null) {
    			getMethodImprimirPdf();
        	} else {
        		Mensajes.enviarMensaje("Debe guardar la información, para poder imprimir", Mensajes.TipoMensaje.ERROR);
        		return;
        	}
    		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Listen("onClick=[id$=btnVerPdf]")
    public void btnVerPdf_onClick() {
    	try {
			getPdf();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /*Metodo para validar todos los registros requeridos antes de guardar*/
    public boolean validarDatosGenerales() {
    	if (this.txtCodExpediente.getValue() == null) {
			Mensajes.enviarMensaje("Código de expediente requerido", Mensajes.TipoMensaje.ERROR);
			this.txtCodExpediente.setFocus(true);
			return false;
		}
    	if (this.txtFechaConsulta.getValue() == null ) {
    		Mensajes.enviarMensaje("Debe ingresar la fecha de la consulta", Mensajes.TipoMensaje.ERROR);
    		this.txtFechaConsulta.setFocus(true);
    		return false;
    	}
    	if (this.txtHoraConsulta.getValue() == null || this.txtHoraConsulta.getValue() == "") {
    		Mensajes.enviarMensaje("Debe ingresar la hora de la consulta", Mensajes.TipoMensaje.ERROR);
    		this.txtHoraConsulta.setFocus(true);
    		return false;
    	} else {
    		String txtHoraConsulta = this.txtHoraConsulta.getValue();	
        	boolean result = isValidTime24Hours(txtHoraConsulta);
        	if (!result) {
        		Mensajes.enviarMensaje("Formato invalido, ingresar hora formato 24 horas", Mensajes.TipoMensaje.ERROR);
        		this.txtHoraConsulta.setFocus(true);
        	}
    	}
    	if (this.txtPeso.getValue() == "" || this.txtPeso.getValue() == null) {
    		Mensajes.enviarMensaje("El Peso es requerido", Mensajes.TipoMensaje.ERROR);
    		this.txtPeso.setFocus(true);
    		return false;
    	} else {
    		String txtPeso = this.txtPeso.getValue();
    		boolean result = isValidNumeric_5_2(txtPeso);
    		if (!result) {
    			Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    			this.txtPeso.setFocus(true);
    			return false;
    		}
    		if (!estaEnRango(1, 200, this.txtPeso.getValue())) {
    			Mensajes.enviarMensaje("Peso, esta fuera de rango", Mensajes.TipoMensaje.ERROR);
    			this.txtPeso.setFocus(true);
				return false;
            }
    	}
		if (this.txtTalla.getValue() == "" || this.txtTalla.getValue() == null) {
			Mensajes.enviarMensaje("La Talla es requerida", Mensajes.TipoMensaje.ERROR);
    		this.txtTalla.setFocus(true);
    		return false;	
		} else {
			String txtTalla = this.txtTalla.getValue();
    		boolean result = isValidNumeric_5_2(txtTalla);
    		if (!result) {
    			Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    			this.txtTalla.setFocus(true);
    			return false;
    		}
    		if (!estaEnRango(20, 220, this.txtTalla.getText().toString())) {
    			Mensajes.enviarMensaje("Talla, esta fuera de rango", Mensajes.TipoMensaje.ERROR);
    			this.txtTalla.setFocus(true);
				return false;
            }
		}
		if (this.txtTemp.getValue() == "" || this.txtTemp.getValue() == null) {
			Mensajes.enviarMensaje("La Temperatura es requerida", Mensajes.TipoMensaje.ERROR);
    		this.txtTemp.setFocus(true);
    		return false;	
		} else {
			String txtTemp = this.txtTemp.getValue();
	    	boolean result = isValidNumeric_4_2(txtTemp);
	    	if (!result) {
	    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
	    		this.txtTemp.setFocus(true);
	    		return false;
	    	}
	    	if (!estaEnRango(35.5, 41, this.txtTemp.getValue())) {
	    		Mensajes.enviarMensaje("Temp, esta fuera de rango", Mensajes.TipoMensaje.ERROR);
	    		this.txtTemp.setFocus(true);
				return false;
	         }
		}
		if (this.txtHora.getValue() == "" || this.txtHora.getValue() == null) {
			Mensajes.enviarMensaje("Hora enfermeria es requerida", Mensajes.TipoMensaje.ERROR);
    		this.txtHora.setFocus(true);
			return false;
		} else {
			String txtHora = this.txtHora.getValue();
	    	boolean result = isValidTime12Hours(txtHora);
	    	if (!result) {
	    		Mensajes.enviarMensaje("Formato invalido, ingresar hora formato 12 horas", Mensajes.TipoMensaje.ERROR);
	    		this.txtHora.setFocus(true);
	    		return false;
	    	}
		}
		
		if (this.txtHoraMedico.getValue() == "" || this.txtHoraMedico.getValue() == null) {
			Mensajes.enviarMensaje("Hora medico es requerida", Mensajes.TipoMensaje.ERROR);
    		this.txtHoraMedico.setFocus(true);
			return false;
		} else {
			String txtHoraMedico = this.txtHoraMedico.getValue();
	    	boolean result = isValidTime12Hours(txtHoraMedico);
	    	if (!result) {
	    		Mensajes.enviarMensaje("Formato invalido, ingresar hora formato 12 horas", Mensajes.TipoMensaje.ERROR);
	    		this.txtHoraMedico.setFocus(true);
	    		return false;
	    	}
		}
		
		
		if (this.cmbHora.getValue() == "" || this.cmbHora.getValue() == null) {
			Mensajes.enviarMensaje("AM/PM hora enfermeria es requerida", Mensajes.TipoMensaje.ERROR);
    		this.cmbHora.setFocus(true);
			return false;
		}
		
		if (this.cmbHoraMedico.getValue() == "" || this.cmbHoraMedico.getValue() == null) {
			Mensajes.enviarMensaje("AM/PM hora medico es requerida", Mensajes.TipoMensaje.ERROR);
    		this.cmbHoraMedico.setFocus(true);
			return false;
		}
		
		if (this.txtPas.getValue() == "" || this.txtPas.getValue() == null) {
			Mensajes.enviarMensaje("PAS es requerida", Mensajes.TipoMensaje.ERROR);
    		this.txtPas.setFocus(true);
			return false;
		} else {
			String txtPas = this.txtPas.getValue();
	    	boolean result = isValidNumeric(txtPas);
	    	if (!result) {
	    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
	    		this.txtPas.setFocus(true);
	    		return false;
	    	}
	    	
	    	if (!estaEnRango(55, 220, this.txtPas.getValue())) {
				Mensajes.enviarMensaje("PAS, esta fuera de rango", Mensajes.TipoMensaje.ERROR);
				this.txtPas.setFocus(true);
				return false;
		    }
		}
		if (this.txtPad.getValue() == "" || this.txtPad.getValue() == null) {
			Mensajes.enviarMensaje("PAD es requerida", Mensajes.TipoMensaje.ERROR);
    		this.txtPad.setFocus(true);
			return false;
		} else {
			String txtPad = this.txtPad.getValue();
	    	boolean result = isValidNumeric(txtPad);
	    	if (!result) {
	    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
	    		this.txtPad.setFocus(true);
	    		return false;
	    	}
	    	if (!estaEnRango(35, 160, this.txtPad.getValue())) {
				Mensajes.enviarMensaje("PAD, esta fuera de rango", Mensajes.TipoMensaje.ERROR);
				this.txtPad.setFocus(true);
				return false;
		    }
		}
		if (this.txtFciaResp.getValue() == "" || this.txtFciaResp.getValue() == null) {
			Mensajes.enviarMensaje("FciaResp es requerida", Mensajes.TipoMensaje.ERROR);
    		this.txtFciaResp.setFocus(true);
			return false;
		} else {
			String txtFciaResp = this.txtFciaResp.getValue();
	    	boolean result = isValidNumeric(txtFciaResp);
	    	if (!result) {
	    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
	    		this.txtFciaResp.setFocus(true);
	    		return false;
	    	}
	    	if (!estaEnRango(11, 80, this.txtFciaResp.getValue())) {
				Mensajes.enviarMensaje("FciaResp, esta fuera de rango", Mensajes.TipoMensaje.ERROR);
				this.txtFciaResp.setFocus(true);
				return false;
		    }
		}
		if (this.txtFciaCard.getValue() == "" || this.txtFciaCard.getValue() == null) {
			Mensajes.enviarMensaje("FciaCard es requerida", Mensajes.TipoMensaje.ERROR);
    		this.txtFciaCard.setFocus(true);
			return false;
		} else {
			String txtFciaCard = this.txtFciaCard.getValue();
	    	boolean result = isValidNumeric(txtFciaCard);
	    	if (!result) {
	    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
	    		this.txtFciaCard.setFocus(true);
	    		return false;
	    	}
	    	if (!estaEnRango(45, 200, this.txtFciaCard.getValue())) {
				Mensajes.enviarMensaje("FciaCard, esta fuera de rango", Mensajes.TipoMensaje.ERROR);
				this.txtFciaCard.setFocus(true);
				return false;
		    }
		}
		if (!this.chkCssfv.isChecked() && !this.chkTerreno.isChecked()) {
			Mensajes.enviarMensaje("Debe seleccionar el lugar de atención", Mensajes.TipoMensaje.ERROR);
    		return false;
		}
		if (!this.chkCInicial.isChecked() && !this.chkCSeguimiento.isChecked() && !this.chkCConvaleciente.isChecked()) {
			Mensajes.enviarMensaje("Debe seleccionar el tipo de consulta", Mensajes.TipoMensaje.ERROR);
    		return false;
		}
		if (!this.chkRegular.isChecked() && !this.chkNoche.isChecked() && !this.chkFinSemana.isChecked()) {
			Mensajes.enviarMensaje("Debe seleccionar el turno", Mensajes.TipoMensaje.ERROR);
    		return false;
		}
		if (this.txtUltDiaFiebre.getValue() != null) {
			if (this.cmbUltDiaFiebre.getValue() == null || this.cmbUltDiaFiebre.getValue() == "") {
				Mensajes.enviarMensaje("Debe seleccionar AM/PM ultimo día fiebre", Mensajes.TipoMensaje.ERROR);
	    		return false;
			}
		}
		
		if (this.txtUltDosisAntipiretico.getValue() != null) {
			if (this.txtHoraUltDosisAntipiretico.getValue() == null || this.txtHoraUltDosisAntipiretico.getValue() == "") {
				Mensajes.enviarMensaje("Debe ingresar la hora de la ultima dosis antipiretico", Mensajes.TipoMensaje.ERROR);
	    		return false;
			}
			if (this.txtHoraUltDosisAntipiretico.getValue() != null && this.txtHoraUltDosisAntipiretico.getValue() != "" &&
					(this.cmbUltDosisAntipiretico.getValue() == null || this.cmbUltDosisAntipiretico.getValue() == "")) {
				Mensajes.enviarMensaje("Debe seleccionar AM/PM ultima dosis antipiretico", Mensajes.TipoMensaje.ERROR);
	    		return false;
			}
		}
		
		return true;
	}
    
    public boolean validarEstadoGeneral() {
    	if ((!this.chkFiebreS.isChecked() && !this.chkFiebreN.isChecked() && !this.chkFiebreD.isChecked())
    			|| (!this.chkAstAltS.isChecked() && !this.chkAsteniaN.isChecked() && !this.chkAsteniaD.isChecked())
    			|| (!this.chkAnormalMenteSomnolientoS.isChecked() && !this.chkAnormalMenteSomnolientoN.isChecked())
    			|| (!this.chkMalEstadoGeneralS.isChecked() && !this.chkMalEstadoGeneralN.isChecked())
    			|| (!this.chkPerdidaConcienciaS.isChecked() && !this.chkPerdidaConcienciaN.isChecked())
    			|| (!this.chkInquietoIrritableS.isChecked() && !this.chkInquietoIrritableN.isChecked())
    			|| (!this.chkConvulsionesS.isChecked() && !this.chkConvulsionesN.isChecked())
    			|| (!this.chkHipotermiaS.isChecked() && !this.chkHipotermiaN.isChecked())
    			|| (!this.chkLetargiaS.isChecked() && !this.chkLetargiaN.isChecked())) {
    		Mensajes.enviarMensaje("Debe completar la información, Estado General", Mensajes.TipoMensaje.ERROR);
    		return false;
    	}
    	return true;
    }
    
    public boolean validarGastrointestinal() {
    	if ((!this.chkPocoApetitoS.isChecked() && !this.chkPocoApetitoN.isChecked() && !this.chkPocoApetitoD.isChecked())
    			|| (!this.chkNauseaS.isChecked() && !this.chkNauseaN.isChecked() && !this.chkNauseaD.isChecked())
    			|| (!this.chkDificultadAlimentaseS.isChecked() && !this.chkDificultadAlimentaseN.isChecked() && !this.chkDificultadAlimentaseD.isChecked())
    			|| (!this.chkVomito12HorasS.isChecked() && !this.chkVomito12HorasN.isChecked() && !this.chkVomito12HorasD.isChecked())
    			|| (!this.chkDiarreaS.isChecked() && !this.chkDiarreaN.isChecked() && !this.chkDiarreaD.isChecked())
    			|| (!this.chkDiarreaSangreS.isChecked() && !this.chkDiarreaSangreN.isChecked() && !this.chkDiarreaSangreD.isChecked())
    			|| (!this.chkEstrenimientoS.isChecked() && !this.chkEstrenimientoN.isChecked() && !this.chkEstrenimientoD.isChecked())
    			|| (!this.chkDolorAbInterS.isChecked() && !this.chkDolorAbInterN.isChecked() && !this.chkDolorAbInterD.isChecked())
    			|| (!this.chkDolorAbContinuoS.isChecked() && !this.chkDolorAbContinuoN.isChecked() && !this.chkDolorAbContinuoD.isChecked())
    			|| (!this.chkEpigastralgiaS.isChecked() && !this.chkEpigastralgiaN.isChecked() && !this.chkEpigastralgiaD.isChecked())
    			|| (!this.chkIntoleranciaViaOralS.isChecked() && !this.chkIntoleranciaViaOralN.isChecked() && !this.chkIntoleranciaViaOralD.isChecked())
    			|| (!this.chkDistensionAbdominalS.isChecked() && !this.chkDistensionAbdominalN.isChecked() && !this.chkDistensionAbdominalD.isChecked())
    			|| (!this.chkHepatomegaliaS.isChecked() && !this.chkHepatomegaliaN.isChecked())) {
    		Mensajes.enviarMensaje("Debe completar la información, Gastrointestinal", Mensajes.TipoMensaje.ERROR);
    		return false;
    	}
    	if (this.chkVomito12HorasS.isChecked()) {
    		if (this.txtVomito12Horas.getValue() == "" || this.txtHepatomegalia.getValue() == null) {
    			Mensajes.enviarMensaje("Debe ingresar vómito # ultima 12 horas", Mensajes.TipoMensaje.ERROR);
        		this.txtVomito12Horas.setFocus(true);
        		return false;
    		} else {
    			String txtVomito12Horas = this.txtVomito12Horas.getValue();
    	    	boolean result = isValidNumeric(txtVomito12Horas);
    	    	if (!result) {
    	    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    	    		this.txtVomito12Horas.setFocus(true);
    	    		return false;
    	    	}
    	    	if (!estaEnRango(0, 24, this.txtVomito12Horas.getValue())) {
    				Mensajes.enviarMensaje("Vomito12Horas, esta fuera de rango", Mensajes.TipoMensaje.ERROR);
    				this.txtVomito12Horas.setFocus(true);
    				return false;
    		    }
    		}
    	}
    	if (this.chkHepatomegaliaS.isChecked()) {
    		if (this.txtHepatomegalia.getValue() == "" || this.txtHepatomegalia.getValue() == null) {
    			Mensajes.enviarMensaje("Debe ingresar el valor hepatomegalia", Mensajes.TipoMensaje.ERROR);
        		this.txtHepatomegalia.setFocus(true);
        		return false;
    		} else {
    			String txtHepatomegalia = this.txtHepatomegalia.getValue();
    	    	boolean result = isValidNumeric_5_2(txtHepatomegalia);
    	    	if (!result) {
    	    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    	    		this.txtHepatomegalia.setFocus(true);
    	    		return false;
    	    	}
    	    	if (!estaEnRango(0, 5, this.txtHepatomegalia.getValue())) {
    				Mensajes.enviarMensaje("Hepatomegalia, esta fuera de rango", Mensajes.TipoMensaje.ERROR);
    				this.txtHepatomegalia.setFocus(true);
    				return false;
    		    }
    		}
    	}
    	return true;
    }
    
    public boolean validarOsteomuscular() {
    	if ((!this.chkArtralgiaS.isChecked() && !this.chkArtralgiaN.isChecked() && !this.chkArtralgiaD.isChecked())
    			|| (!this.chkMialgiaS.isChecked() && !this.chkMialgiaN.isChecked() && !this.chkMialgiaD.isChecked())
    			|| (!this.chkLumbalgiaS.isChecked() && !this.chkLumbalgiaN.isChecked() && !this.chkLumbalgiaD.isChecked())
    			|| (!this.chkDolorCuelloS.isChecked() && !this.chkDolorCuelloN.isChecked() && !this.chkDolorCuelloD.isChecked())
    			|| (!this.chkTenosinovitisS.isChecked() && !this.chkTenosinovitisN.isChecked() && !this.chkTenosinovitisD.isChecked())
    			|| (!this.chkArtralgiaProximalS.isChecked() && !this.chkArtralgiaProximalN.isChecked() && !this.chkArtralgiaProximalD.isChecked())
    			|| (!this.chkArtralgiaDistalS.isChecked() && !this.chkArtralgiaDistalN.isChecked() && !this.chkArtralgiaDistalD.isChecked())
    			|| (!this.chkConjuntivitisS.isChecked() && !this.chkConjuntivitisN.isChecked() && !this.chkConjuntivitisD.isChecked())
    			|| (!this.chkEdemaMunecasS.isChecked() && !this.chkEdemaMunecasN.isChecked() && !this.chkEdemaMunecasD.isChecked())
    			|| (!this.chkEdemaCodosS.isChecked() && !this.chkEdemaCodosN.isChecked() && !this.chkEdemaCodosD.isChecked())
    			|| (!this.chkEdemaHombrosS.isChecked() && !this.chkEdemaHombrosN.isChecked() && !this.chkEdemaHombrosD.isChecked())
    			|| (!this.chkEdemaRodillasS.isChecked() && !this.chkEdemaRodillasN.isChecked() && !this.chkEdemaRodillasD.isChecked())
    			|| (!this.chkEdemaTobillosS.isChecked() && !this.chkEdemaTobillosN.isChecked() && !this.chkEdemaTobillosD.isChecked())) {
    		Mensajes.enviarMensaje("Debe completar la información, Osteomuscular", Mensajes.TipoMensaje.ERROR);
    		return false;
    	}
    	return true;
    }
    
    public boolean validarCabeza() {
    	if ((!this.chkCefaleaS.isChecked() && !this.chkCefaleaN.isChecked() && !this.chkCefaleaD.isChecked())
    			|| (!this.chkRigidezCuelloS.isChecked() && !this.chkRigidezCuelloN.isChecked())
    			|| (!this.chkInyecciónConjuntivalS.isChecked() && !this.chkInyecciónConjuntivalN.isChecked())
    			|| (!this.chkHemorragiaSubconjuntivalS.isChecked() && !this.chkHemorragiaSubconjuntivalN.isChecked())
    			|| (!this.chkDolorRetroocularS.isChecked() && !this.chkDolorRetroocularN.isChecked() && !this.chkDolorRetroocularD.isChecked())
    			|| (!this.chkFontanelaAbombadaS.isChecked() && !this.chkFontanelaAbombadaN.isChecked())
    			|| (!this.chkIctericiaConjuntivalS.isChecked() && !this.chkIctericiaConjuntivalN.isChecked())) {
    		Mensajes.enviarMensaje("Debe completar la información Cabeza", Mensajes.TipoMensaje.ERROR);
    		return false;
    	}
    	return true;
    }
    
    public boolean validarDeshidratacion() {
    	if ((!this.chkLenguaMucosaSecasS.isChecked() && !this.chkLenguaMucosaSecasN.isChecked())
    			|| (!this.chkPliegueCutaneoS.isChecked() && !this.chkPliegueCutaneoN.isChecked())
    			|| (!this.chkOrinaReducidaS.isChecked() && !this.chkOrinaReducidaN.isChecked() && !this.chkOrinaReducidaD.isChecked())
    			|| (!this.chkBebeAvidoSedS.isChecked() && !this.chkBebeAvidoSedN.isChecked())
    			|| (!this.chkOjosHundidosS.isChecked() && !this.chkOjosHundidosN.isChecked())
    			|| (!this.chkFontanelaHundidaS.isChecked() && !this.chkFontanelaHundidaN.isChecked())) {
    		Mensajes.enviarMensaje("Debe completar la información, Deshidratación", Mensajes.TipoMensaje.ERROR);
    		return false;
    	}
    	return true;
    }
    
    public boolean validarCutaneo() {
    	if ((!this.chkRashLocalizadoS.isChecked() && !this.chkRashLocalizadoN.isChecked())
    			|| (!this.chkRashGeneralizadoS.isChecked() && !this.chkRashGeneralizadoN.isChecked())
    			|| (!this.chkRashEritematosoS.isChecked() && !this.chkRashEritematosoN.isChecked())
    			|| (!this.chkRashMacularS.isChecked() && !this.chkRashMacularN.isChecked())
    			|| (!this.chkRashPapularS.isChecked() && !this.chkRashPapularN.isChecked())
    			|| (!this.chkPielMoteadaS.isChecked() && !this.chkPielMoteadaN.isChecked())
    			|| (!this.chkRuborFacialS.isChecked() && !this.chkRuborFacialN.isChecked())
    			|| (!this.chkEquimosisS.isChecked() && !this.chkEquimosisN.isChecked())
    			|| (!this.chkCianosisCentralS.isChecked() && !this.chkCianosisCentralN.isChecked())
    			|| (!this.chkIctericiaS.isChecked() && !this.chkIctericiaN.isChecked())) {
    		Mensajes.enviarMensaje("Debe completar la información, Cutáneo", Mensajes.TipoMensaje.ERROR);
    		return false;
    	}
    	return true;
    }
    
    public boolean validarGarganta() {
    	if ((!this.chkEritemaS.isChecked() && !this.chkEritemaN.isChecked())
    			|| (!this.chkDolorGargantaS.isChecked() && !this.chkDolorGargantaN.isChecked() && !this.chkDolorGargantaD.isChecked())
    			|| (!this.chkAdenopatiasCervicalesS.isChecked() && !this.chkAdenopatiasCervicalesN.isChecked())
    			|| (!this.chkExudadoS.isChecked() && !this.chkExudadoN.isChecked())
    			|| (!this.chkPetequiasMucosaS.isChecked() && !this.chkPetequiasMucosaN.isChecked())) {
    		Mensajes.enviarMensaje("Debe completar la información, Garganta", Mensajes.TipoMensaje.ERROR);
    		return false;
    	}
    	return true;
    }
    
    public boolean validarRenal() {
    	if ((!this.chkSintomasUrinariosS.isChecked() && !this.chkSintomasUrinariosN.isChecked() && !this.chkSintomasUrinariosD.isChecked())
    			|| (!this.chkLeucocituriaXCampoS.isChecked() && !this.chkLeucocituriaXCampoN.isChecked() && !this.chkLeucocituriaXCampoD.isChecked())
    			|| (!this.chkNitritosS.isChecked() && !this.chkNitritosN.isChecked() && !this.chkNitritosD.isChecked())
    			|| (!this.chkEritrocitosXCampoS.isChecked() && !this.chkEritrocitosXCampoN.isChecked() && !this.chkEritrocitosXCampoD.isChecked())
    			|| (!this.chkBilirrubinuriaS.isChecked() && !this.chkBilirrubinuriaN.isChecked() && !this.chkBilirrubinuriaD.isChecked())) {
    		Mensajes.enviarMensaje("Debe completar la información, Renal", Mensajes.TipoMensaje.ERROR);
    		return false;
    	}
    	return true;
    }
    
    public boolean validarEstadoNutricional() {
    	if ((!this.chkObesoS.isChecked() && !this.chkObesoN.isChecked() && !this.chkObesoD.isChecked())
    			|| (!this.chkSobrepesoS.isChecked() && !this.chkSobrepesoN.isChecked() && !this.chkSobrepesoD.isChecked())
    			|| (!this.chkSospechaProblemaS.isChecked() && !this.chkSospechaProblemaN.isChecked() && !this.chkSospechaProblemaD.isChecked())
    			|| (!this.chkNormalS.isChecked() && !this.chkNormalN.isChecked() && !this.chkNormalD.isChecked())
    			|| (!this.chkBajoPesoS.isChecked() && !this.chkBajoPesoN.isChecked() && !this.chkBajoPesoD.isChecked())
    			|| (!this.chkBajoPesoSeveroS.isChecked() && !this.chkBajoPesoSeveroN.isChecked() && !this.chkBajoPesoSeveroD.isChecked())) {
    		Mensajes.enviarMensaje("Debe completar la información, Estado Nutricional", Mensajes.TipoMensaje.ERROR);
    		return false;
    	}
    	if (this.txtImc.getValue() == "" || this.txtImc.getValue() == null) {
			Mensajes.enviarMensaje("Debe ingresar el IMC", Mensajes.TipoMensaje.ERROR);
    		this.txtImc.setFocus(true);
    		return false;
		} else {
			String txtImc = this.txtImc.getValue();
	    	boolean result = isValidNumeric_5_2(txtImc);
	    	if (!result) {
	    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
	    		this.txtImc.setFocus(true);
	    		return false;
	    	}
	    	if (!estaEnRango(5, 70, this.txtImc.getValue())) {
	    		Mensajes.enviarMensaje("IMC, esta fuera de rango", Mensajes.TipoMensaje.ERROR);
	    		this.txtImc.setFocus(true);
	    		return false;
	        }
		}
    	return true;
    }
    
    public boolean validarRespiratorio() {
    	if ((!this.chkTosS.isChecked() && !this.chkTosN.isChecked() && !this.chkTosD.isChecked())
    			|| (!this.chkRinorreaS.isChecked() && !this.chkRinorreaN.isChecked() && !this.chkRinorreaD.isChecked())
    			|| (!this.chkCongestionNasalS.isChecked() && !this.chkCongestionNasalN.isChecked() && !this.chkCongestionNasalD.isChecked())
    			|| (!this.chkOtalgiaS.isChecked() && !this.chkOtalgiaN.isChecked() && !this.chkOtalgiaD.isChecked())
    			|| (!this.chkAleteoNasalS.isChecked() && !this.chkAleteoNasalN.isChecked())
    			|| (!this.chkApneaS.isChecked() && !this.chkApneaN.isChecked())
    			|| (!this.chkRespiracionRapidaS.isChecked() && !this.chkRespiracionRapidaN.isChecked())
    			|| (!this.chkQuejidoEspiratorioS.isChecked() && !this.chkQuejidoEspiratorioN.isChecked())
    			|| (!this.chkEstridorReposoS.isChecked() && !this.chkEstridorReposoN.isChecked())
    			|| (!this.chkTirajeSubcostalS.isChecked() && !this.chkTirajeSubcostalN.isChecked())
    			|| (!this.chkSibilanciasS.isChecked() && !this.chkSibilanciasN.isChecked())
    			|| (!this.chkCrepitosS.isChecked() && !this.chkCrepitosN.isChecked())
    			|| (!this.chkRoncosS.isChecked() && !this.chkRoncosN.isChecked())
    			|| (!this.chkOtraFIFS.isChecked() && !this.chkOtraFIFN.isChecked())) {
    		Mensajes.enviarMensaje("Debe completar la información, Respiratorio", Mensajes.TipoMensaje.ERROR);
    		return false;
    	}
    	if (this.chkOtraFIFS.isChecked()) {
    		if (this.txtNuevaFif.getValue() == null) {
    			Mensajes.enviarMensaje("Debe ingresar la nueva fif", Mensajes.TipoMensaje.ERROR);
        		this.txtNuevaFif.setFocus(true);
        		return false;
    		}
    	}
    	return true;
    }
    
    public boolean validarReferencia() {
    	if ((!this.chkInterconsultaPediatraS.isChecked() && !this.chkInterconsultaPediatraN.isChecked())
    			|| (!this.chkReferenciaHospitalS.isChecked() && !this.chkReferenciaHospitalN.isChecked())
    			|| (!this.chkReferenciaDengueS.isChecked() && !this.chkReferenciaDengueN.isChecked())
    			|| (!this.chkReferenciaIRAGS.isChecked() && !this.chkReferenciaIRAGN.isChecked())
    			|| (!this.chkReferenciaCHIKS.isChecked() && !this.chkReferenciaCHIKN.isChecked())
    			|| (!this.chkETIS.isChecked() && !this.chkETIN.isChecked())
    			|| (!this.chkIRAGS.isChecked() && !this.chkIRAGN.isChecked())
    			|| (!this.chkNeumoniaS.isChecked() && !this.chkNeumoniaN.isChecked())
    			|| (!this.chkCVS.isChecked() && !this.chkCVN.isChecked())) {
    		Mensajes.enviarMensaje("Debe completar la información, Referencia", Mensajes.TipoMensaje.ERROR);
    		return false;
    	}
    	return true;
    }
    
    public boolean validarVacunas() {
    	if ((!this.chkLactanciaMaternaS.isChecked() && !this.chkLactanciaMaternaN.isChecked() && !this.chkLactanciaMaternaD.isChecked())
    			|| (!this.chkVacunasCompletasS.isChecked() && !this.chkVacunasCompletasN.isChecked() && !this.chkVacunasCompletasD.isChecked())
    			|| (!this.chkVacunaInfluenzaS.isChecked() && !this.chkVacunaInfluenzaN.isChecked() && !this.chkVacunaInfluenzaD.isChecked())) {
    		Mensajes.enviarMensaje("Debe completar la información, Vacunas", Mensajes.TipoMensaje.ERROR);
    		return false;
    	}
    	if (this.chkVacunaInfluenzaS.isChecked()) {
    		if (this.txtFechaVacuna.getValue() == null) {
    			Mensajes.enviarMensaje("Debe ingresar la nueva fecha de vacuna", Mensajes.TipoMensaje.ERROR);
        		this.txtNuevaFif.setFocus(true);
        		return false;
    		}
    	}
    	return true;
    }
    
    public boolean validarCategoria() {
    	if ((!this.chkManifestacionHemorragicaS.isChecked() && !this.chkManifestacionHemorragicaN.isChecked())
    			|| (!this.chkPruebaTorniquetePositivaS.isChecked() && !this.chkPruebaTorniquetePositivaN.isChecked())
    			|| (!this.chkPetequias10S.isChecked() && !this.chkPetequias10N.isChecked())
    			|| (!this.chkPetequias20S.isChecked() && !this.chkPetequias20N.isChecked())
    			|| (!this.chkPielExtremidadesFriasS.isChecked() && !this.chkPielExtremidadesFriasN.isChecked())
    			|| (!this.chkPalidezExtremidadesS.isChecked() && !this.chkPalidezExtremidadesN.isChecked())
    			|| (!this.chkEpistaxisS.isChecked() && !this.chkEpistaxisN.isChecked())
    			|| (!this.chkGingivorragiaS.isChecked() && !this.chkGingivorragiaN.isChecked())
    			|| (!this.chkPetequiasEspontaneasS.isChecked() && !this.chkPetequiasEspontaneasN.isChecked())
    			|| (!this.chkLlenadoCapilarS.isChecked() && !this.chkLlenadoCapilarN.isChecked())
    			|| (!this.chkCianosisS.isChecked() && !this.chkCianosisN.isChecked())
    			|| (!this.chkHipermenorreaS.isChecked() && !this.chkHipermenorreaN.isChecked() && !this.chkHipermenorreaD.isChecked())
    			|| (!this.chkHematemesisS.isChecked() && !this.chkHematemesisN.isChecked())
    			|| (!this.chkMelenaS.isChecked() && !this.chkMelenaN.isChecked())
    			|| (!this.chkHemoconcentracionS.isChecked() && !this.chkHemoconcentracionN.isChecked() && !this.chkHemoconcentracionD.isChecked())
    			|| (!this.chkHospitalizadoUltimoAnioS.isChecked() && !this.chkHospitalizadoUltimoAnioN.isChecked())
    			|| (!this.chkTransfusionSangreS.isChecked() && !this.chkTransfusionSangreN.isChecked())
    			|| (!this.chkEstaTomandoMedicamentoS.isChecked() && !this.chkEstaTomandoMedicamentoN.isChecked())
    			|| (!this.chkTomaMedicamentoDistintoS.isChecked() && !this.chkTomaMedicamentoDistintoN.isChecked())) {
    		Mensajes.enviarMensaje("Debe completar la información, Categoría", Mensajes.TipoMensaje.ERROR);
    		return false;
    	}
    	if (this.txtSaturacion.getValue() == "" || this.txtSaturacion.getValue() == null) {
    		Mensajes.enviarMensaje("Debe ingresar la saturación ", Mensajes.TipoMensaje.ERROR);
    		this.txtSaturacion.setFocus(true);
    		return false;
    	} else {
    		String txtSaturacion = this.txtSaturacion.getValue();
        	boolean result = isValidNumeric(txtSaturacion);
        	if (!result) {
        		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
        		this.txtSaturacion.setFocus(true);
        		return false;
        	}
        	if (!estaEnRango(0, 100, this.txtSaturacion.getValue())) {
    			Mensajes.enviarMensaje("La saturación, esta fuera de rango", Mensajes.TipoMensaje.ERROR);
    			this.txtSaturacion.setFocus(true);
				return false;
            }
    	}
    	if (this.txtLinfocitoAtipicos.getValue() != "" && this.txtLinfocitoAtipicos.getValue() != null) {
    		String txtLinfocitoAtipicos = this.txtLinfocitoAtipicos.getValue();	
        	boolean result = isValidNumeric_5_2(txtLinfocitoAtipicos);
        	if (!result) {
        		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
        		this.txtLinfocitoAtipicos.setFocus(true);
        		return false;
        	}
        	
        	if (txtFechaLinfocitoAtipicos.getValue() == null) {
        		Mensajes.enviarMensaje("Debe ingresar la fecha linfocito atipicos", Mensajes.TipoMensaje.ERROR);
        		this.txtFechaLinfocitoAtipicos.setFocus(true);
        		return false;
        	}
    	}
    	if (this.chkHemoconcentracionS.isChecked()) {
    		if (this.txtHemoconcentracion.getValue() == "" || this.txtHemoconcentracion.getValue() == null) {
    			Mensajes.enviarMensaje("Debe ingresar la hemoconcentración", Mensajes.TipoMensaje.ERROR);
        		this.txtHemoconcentracion.setFocus(true);
        		return false;
    		} else {
    			String txtHemoconcentracion = this.txtHemoconcentracion.getValue();
    	    	boolean result = isValidNumeric(txtHemoconcentracion);
    	    	if (!result) {
    	    		Mensajes.enviarMensaje("Formato invalido", Mensajes.TipoMensaje.ERROR);
    	    		this.txtHemoconcentracion.setFocus(true);
    	    		return false;
    	    	}
    	    	if (!estaEnRango(0, 75, this.txtHemoconcentracion.getValue())) {
        			Mensajes.enviarMensaje("La hemoconcentracion, esta fuera de rango", Mensajes.TipoMensaje.ERROR);
        			this.txtHemoconcentracion.setFocus(true);
    				return false;
                }
    		}
    	}
    	
    	if (this.chkHospitalizadoUltimoAnioS.isChecked()) {
    		if (this.txtHospitalizadoUltimoAnio.getValue() == "" || this.txtHospitalizadoUltimoAnio.getValue() == null) {
    			Mensajes.enviarMensaje("Debe especificar", Mensajes.TipoMensaje.ERROR);
        		this.txtHospitalizadoUltimoAnio.setFocus(true);
        		return false;
    		}
    	}
		if (this.chkTransfusionSangreS.isChecked()) {
			if (this.txtTransfusionSangre.getValue() == "" || this.txtTransfusionSangre.getValue() == null) {
    			Mensajes.enviarMensaje("Debe especificar", Mensajes.TipoMensaje.ERROR);
        		this.txtTransfusionSangre.setFocus(true);
        		return false;
    		}
		}
		if (this.chkEstaTomandoMedicamentoS.isChecked()) {
			if (this.txtEstaTomandoMedicamento.getValue() == "" || this.txtEstaTomandoMedicamento.getValue() == null) {
    			Mensajes.enviarMensaje("Debe especificar", Mensajes.TipoMensaje.ERROR);
        		this.txtEstaTomandoMedicamento.setFocus(true);
        		return false;
    		}
		}
		if (this.chkTomaMedicamentoDistintoS.isChecked()) {
			if (this.txtTomaMedicamentoDistinto.getValue() == "" || this.txtTomaMedicamentoDistinto.getValue() == null) {
    			Mensajes.enviarMensaje("Debe especificar", Mensajes.TipoMensaje.ERROR);
        		this.txtTomaMedicamentoDistinto.setFocus(true);
        		return false;
    		}
		}
		//if (this.cmbCategoria.getValue() == "" || this.cmbCategoria.getValue() == null) {
		if(this.cmbCategoria.getSelectedIndex() <= 0) {
			Mensajes.enviarMensaje("Debe seleccionar la categoria", Mensajes.TipoMensaje.ERROR);
    		this.cmbCategoria.setFocus(true);
    		return false;
		}
    	return true;
    }
    
    public boolean validarExamenes() {
    	if ((!this.chkBhcS.isChecked() && !this.chkBhcN.isChecked())
    			|| (!this.chkSerologiaDengueS.isChecked() && !this.chkSerologiaDengueN.isChecked())
    			|| (!this.chkSerologiaChikS.isChecked() && !this.chkSerologiaChikN.isChecked())
    			|| (!this.chkGotaGruesaS.isChecked() && !this.chkGotaGruesaN.isChecked())
    			|| (!this.chkExtendidoPerifericoS.isChecked() && !this.chkExtendidoPerifericoN.isChecked())
    			|| (!this.chkEgoS.isChecked() && !this.chkEgoN.isChecked())
    			|| (!this.chkEghS.isChecked() && !this.chkEghN.isChecked())
    			|| (!this.chkCitologiaFecalS.isChecked() && !this.chkCitologiaFecalN.isChecked())
    			|| (!this.chkFactorReumatoideoS.isChecked() && !this.chkFactorReumatoideoN.isChecked())
    			|| (!this.chkAlbuminaS.isChecked() && !this.chkAlbuminaN.isChecked())
    			|| (!this.chkAstAltS.isChecked() && !this.chkAstAltN.isChecked())
    			|| (!this.chkBilirubinasS.isChecked() && !this.chkBilirubinasN.isChecked())
    			|| (!this.chkCpkS.isChecked() && !this.chkCpkN.isChecked())
    			|| (!this.chkColesterolS.isChecked() && !this.chkColesterolN.isChecked())
    			|| (!this.chkInfluenzaS.isChecked() && !this.chkInfluenzaN.isChecked())
    			|| (!this.chkOtroExamenS.isChecked() && !this.chkOtroExamenN.isChecked())) {
    		Mensajes.enviarMensaje("Debe completar la información, Exámenes", Mensajes.TipoMensaje.ERROR);
    		return false;
    	}
    	if (this.chkOtroExamenS.isChecked()) {
			if (this.txtOtroExamen.getValue() == "" || this.txtOtroExamen.getValue() == null) {
    			Mensajes.enviarMensaje("Debe ingresar el otro examen", Mensajes.TipoMensaje.ERROR);
        		this.txtOtroExamen.setFocus(true);
        		return false;
    		}
		}
    	return true;
    }
    
    public boolean validarTratamiento() {
    	if ((!this.chkAcetaminofenS.isChecked() && !this.chkAcetaminofenN.isChecked())
    			|| (!this.chkAsaS.isChecked() && !this.chkAsaN.isChecked())
    			|| (!this.chkIbuprofenS.isChecked() && !this.chkIbuprofenN.isChecked())
    			|| (!this.chkPenicilinaS.isChecked() && !this.chkPenicilinaN.isChecked())
    			|| (!this.chkDicloxacilinaS.isChecked() && !this.chkDicloxacilinaN.isChecked())
    			|| (!this.chkOtroAntibioticoS.isChecked() && !this.chkOtroAntibioticoN.isChecked())
    			|| (!this.chkFurazolidonaS.isChecked() && !this.chkFurazolidonaN.isChecked())
    			|| (!this.chkMetronidazolTinidazolS.isChecked() && !this.chkMetronidazolTinidazolN.isChecked())
    			|| (!this.chkAlbendazolMebendazolS.isChecked() && !this.chkAlbendazolMebendazolN.isChecked())
    			|| (!this.chkSulfatoFerrosoS.isChecked() && !this.chkSulfatoFerrosoN.isChecked())
    			|| (!this.chkSueroOralS.isChecked() && !this.chkSueroOralN.isChecked())
    			|| (!this.chkSulfatoZincS.isChecked() && !this.chkSulfatoZincN.isChecked())
    			|| (!this.chkLiquidosIVS.isChecked() && !this.chkLiquidosIVN.isChecked())
    			|| (!this.chkPrednisonaS.isChecked() && !this.chkPrednisonaN.isChecked())
    			|| (!this.chkHidrocortisonaIVS.isChecked() && !this.chkHidrocortisonaIVN.isChecked())
    			|| (!this.chkSalbutamolS.isChecked() && !this.chkSalbutamolN.isChecked())
    			|| (!this.chkOseltamivirS.isChecked() && !this.chkOseltamivirN.isChecked())) {
    		Mensajes.enviarMensaje("Debe completar la información, Tratamiento", Mensajes.TipoMensaje.ERROR);
    		return false;
    	}
    	if (this.chkOtroAntibioticoS.isChecked()) {
			if (this.txtOtroAntibiotico.getValue() == "" || this.txtOtroAntibiotico.getValue() == null) {
    			Mensajes.enviarMensaje("Debe ingresar el otro antibiotico", Mensajes.TipoMensaje.ERROR);
        		this.txtOtroAntibiotico.setFocus(true);
        		return false;
    		}
		}
    	return true;
    }
    
    public boolean validarCierre() {
    	String diagnostico1 = this.cmbDiagnostico1.getSelectedItem().getValue().toString();
    	String diagnostico2 = this.cmbDiagnostico2.getSelectedItem().getValue().toString();
    	String diagnostico3 = this.cmbDiagnostico3.getSelectedItem().getValue().toString();
    	String diagnostico4 = this.cmbDiagnostico4.getSelectedItem().getValue().toString();
    	if (diagnostico1.trim().equals("50") || diagnostico2.trim().equals("50") || diagnostico3.trim().equals("50")
    			|| diagnostico4.trim().equals("50")) {
    		this.txtOtroDiagnostico.setDisabled(false);
    	}
    	if (this.txtHistoriaExamenFisico.getValue() == "" || this.txtHistoriaExamenFisico.getValue() == null) {
    		Mensajes.enviarMensaje("Debe ingresar la historia y exámen físico", Mensajes.TipoMensaje.ERROR);
    		this.txtHistoriaExamenFisico.setFocus(true);
    		return false;
    	}
    	if (this.txtPlanes.getValue() == "" || this.txtPlanes.getValue() == null) {
    		Mensajes.enviarMensaje("Debe ingresar los planes", Mensajes.TipoMensaje.ERROR);
    		this.txtPlanes.setFocus(true);
    		return false;
    	}
    	if (this.cmbDiagnostico1.getSelectedIndex() <= 0) {
    		Mensajes.enviarMensaje("Debe seleccionar el diagnostico 1", Mensajes.TipoMensaje.ERROR);
    		this.cmbDiagnostico1.setFocus(true);
    		return false;
    	}
    	/*if (this.cmbDiagnostico1.getSelectedIndex() <= 0 && this.cmbDiagnostico2.getSelectedIndex() <= 0
    			&& this.cmbDiagnostico3.getSelectedIndex() <= 0 && this.cmbDiagnostico4.getSelectedIndex() <= 0) {
    		Mensajes.enviarMensaje("Debe seleccionar un diagnostico", Mensajes.TipoMensaje.ERROR);
    		this.cmbDiagnostico1.setFocus(true);
    		return false;
    	}*/
    	if ((this.cmbDiagnostico1.getSelectedItem().getValue().toString().equals("50")
    			|| this.cmbDiagnostico2.getSelectedItem().getValue().toString().equals("50")
    			|| this.cmbDiagnostico3.getSelectedItem().getValue().toString().equals("50")
    			|| this.cmbDiagnostico4.getSelectedItem().getValue().toString().equals("50")) &&
    			this.txtOtroDiagnostico.getValue() == "" || this.txtOtroAntibiotico.getValue() == null) {
    		Mensajes.enviarMensaje("Debe ingresar el otro diagnostico", Mensajes.TipoMensaje.ERROR);
    		this.txtOtroDiagnostico.setFocus(true);
    		return false;
    	}
    	if (this.txtFechaProximaCita.getValue() == null) {
    		Mensajes.enviarMensaje("Debe ingresar la fecha para la próxima cita", Mensajes.TipoMensaje.ERROR);
    		this.txtFechaProximaCita.setFocus(true);
    		return false;
    	}
    	if (this.cmbMedico.getSelectedIndex() <=0) {
    		Mensajes.enviarMensaje("Debe seleccionar el médico", Mensajes.TipoMensaje.ERROR);
    		this.cmbMedico.setFocus(true);
    		return false;
    	}
    	if (this.cmbEnfermeria.getSelectedIndex() <=0) {
    		Mensajes.enviarMensaje("Debe seleccionar el enfermer@", Mensajes.TipoMensaje.ERROR);
    		this.cmbEnfermeria.setFocus(true);
    		return false;
    	}
    	if (this.cmbSupervisor.getSelectedIndex() <= 0) {
    		Mensajes.enviarMensaje("Debe seleccionar el supervisor", Mensajes.TipoMensaje.ERROR);
    		this.cmbSupervisor.setFocus(true);
    		return false;
    	}
    	if (this.txtFechaCierre.getValue() == null) {
    		Mensajes.enviarMensaje("Debe seleccionar la fecha de cierre", Mensajes.TipoMensaje.ERROR);
    		this.txtFechaCierre.setFocus(true);
    		return false;
    	}
    	if (this.txtHoraFechaCierre.getValue() == "" || this.txtHoraFechaCierre.getValue() == null) {
    		Mensajes.enviarMensaje("Debe ingresar la hora del cierre", Mensajes.TipoMensaje.ERROR);
    		this.txtHoraFechaCierre.setFocus(true);
    		return false;
    	} else {
    		String txtHoraFechaCierre = this.txtHoraFechaCierre.getValue();
        	boolean result = isValidTime24Hours(txtHoraFechaCierre);
        	if (!result) {
        		Mensajes.enviarMensaje("Formato invalido, ingresar hora formato 24 horas", Mensajes.TipoMensaje.ERROR);
        		this.txtHoraFechaCierre.setFocus(true);
        	}
    	}
    	return true;
    }
    
	/*Metodo para evaluar guardar la informacion*/
    @Listen("onClick=[id$=btnGuardar]")
    public void btnGuardar_onClick() {
    	try {
    		if (validarDatosGenerales() && validarEstadoGeneral() && validarGastrointestinal()
    				&& validarOsteomuscular() && validarCabeza() && validarDeshidratacion()
    				&& validarCutaneo() && validarGarganta() && validarRenal()
    				&& validarEstadoNutricional() && validarRespiratorio() && validarReferencia()
    				&& validarVacunas() && validarCategoria() && validarExamenes()
    				&& validarTratamiento() && validarCierre()) {
    			/**
    			 * Enviar a guardar
    			 * */
    			this.btnGuardar.setDisabled(true);
    			HojaConsulta hojaConsulta = new HojaConsulta();
    			
    			hojaConsulta.setCodExpediente(this.txtCodExpediente.getValue());
    			hojaConsulta.setEstado('7');
    			/*Fecha Consulta*/
    			Date fC = this.txtFechaConsulta.getValue();
    	    	String horaC = this.txtHoraConsulta.getValue();
    	    	String fecha = UtilDate.DateToString(fC, "yyyy-MM-dd");
    	    	String res = fecha + " " + horaC;
    	    	Date fConsulta = UtilDate.StringToDate(res, "yyyy-MM-dd HH:mm");
    			hojaConsulta.setFechaConsulta(fConsulta);
    			
    			hojaConsulta.setUsuarioEnfermeria(Short.valueOf(this.cmbEnfermeria.getSelectedItem().getValue().toString()));
    			hojaConsulta.setUsuarioMedico(Short.valueOf(this.cmbMedico.getSelectedItem().getValue().toString()));
    			hojaConsulta.setSupervisor(Short.valueOf(this.cmbSupervisor.getSelectedItem().getValue().toString()));
    			hojaConsulta.setPesoKg(new BigDecimal(this.txtPeso.getValue()));
    			hojaConsulta.setTallaCm(new BigDecimal(this.txtTalla.getValue()));
    			hojaConsulta.setTemMedc(new BigDecimal(this.txtTemp.getValue()));
    			hojaConsulta.setTemperaturac(new BigDecimal(this.txtTemp.getValue()));
    			hojaConsulta.setPresion(null);
    			hojaConsulta.setFciaResp(Short.valueOf(this.txtFciaResp.getValue()));
    			hojaConsulta.setFciaCard(Short.valueOf(this.txtFciaCard.getValue()));
    			if (this.chkCssfv.isChecked()) {
    				hojaConsulta.setLugarAtencion("CS SFV");
    			} else {
    				hojaConsulta.setLugarAtencion("Terreno");
    			}
    			if (this.chkCInicial.isChecked()) {
    				hojaConsulta.setConsulta("Inicial");
    			} else if (this.chkCSeguimiento.isChecked()) {
    				hojaConsulta.setConsulta("Seguimiento");
    			} else {
    				hojaConsulta.setConsulta("Convaleciente");
    			}
    			if (this.chkSChik1.isChecked()) {
    				hojaConsulta.setSegChick('0');
    			} else if (this.chkSChik2.isChecked()) {
    				hojaConsulta.setSegChick('1');
    			} else if (this.chkSChik3.isChecked()) {
    				hojaConsulta.setSegChick('2');
    			} else if (this.chkSChik4.isChecked()) {
    				hojaConsulta.setSegChick('3');
    			} else if (this.chkSChik5.isChecked()) {
    				hojaConsulta.setSegChick('4');
    			} else {
    				hojaConsulta.setSegChick(null);
    			}
    			if (this.chkRegular.isChecked()) {
    				hojaConsulta.setTurno('1');
    			} else if (this.chkNoche.isChecked()) {
    				hojaConsulta.setTurno('2');
    			} else {
    				hojaConsulta.setTurno('3');
    			}
    			hojaConsulta.setUltDiaFiebre(this.txtUltDiaFiebre.getValue());
    			hojaConsulta.setUltDosisAntipiretico(this.txtUltDosisAntipiretico.getValue());
    			hojaConsulta.setFiebre(resultadoGenericoChkbSND(this.chkFiebreS, this.chkFiebreN, this.chkFiebreD));
    			hojaConsulta.setAstenia(resultadoGenericoChkbSND(this.chkAsteniaS, this.chkAsteniaN, this.chkAsteniaD));
    			hojaConsulta.setAsomnoliento(resultadoGenericoChkbSN(this.chkAnormalMenteSomnolientoS, this.chkAnormalMenteSomnolientoN));
    			hojaConsulta.setMalEstado(resultadoGenericoChkbSN(this.chkMalEstadoGeneralS, this.chkMalEstadoGeneralN));
    			hojaConsulta.setPerdidaConsciencia(resultadoGenericoChkbSN(this.chkPerdidaConcienciaS, this.chkPerdidaConcienciaN));
    			hojaConsulta.setInquieto(resultadoGenericoChkbSN(this.chkInquietoIrritableS, this.chkInquietoIrritableN));
    			hojaConsulta.setConvulsiones(resultadoGenericoChkbSN(this.chkConvulsionesS, this.chkConvulsionesN));
    			hojaConsulta.setHipotermia(resultadoGenericoChkbSN(this.chkHipotermiaS, this.chkHipotermiaN));
    			hojaConsulta.setLetargia(resultadoGenericoChkbSN(this.chkLetargiaS, this.chkLetargiaN));
    			hojaConsulta.setCefalea(resultadoGenericoChkbSND(this.chkCefaleaS, this.chkCefaleaN, this.chkCefaleaD));
    			hojaConsulta.setRigidezCuello(resultadoGenericoChkbSN(this.chkRigidezCuelloS, this.chkRigidezCuelloN));
    			hojaConsulta.setInyeccionConjuntival(resultadoGenericoChkbSN(this.chkInyecciónConjuntivalS, this.chkInyecciónConjuntivalN));
    			hojaConsulta.setHemorragiaSuconjuntival(resultadoGenericoChkbSN(this.chkHemorragiaSubconjuntivalS, this.chkHemorragiaSubconjuntivalN));
    			hojaConsulta.setDolorRetroocular(resultadoGenericoChkbSND(this.chkDolorRetroocularS, this.chkDolorRetroocularN, this.chkDolorRetroocularD));
    			hojaConsulta.setFontanelaAbombada(resultadoGenericoChkbSN(this.chkFontanelaAbombadaS, this.chkFontanelaAbombadaN));
    			hojaConsulta.setIctericiaConuntival(resultadoGenericoChkbSN(this.chkIctericiaConjuntivalS, this.chkIctericiaConjuntivalN));
    			hojaConsulta.setEritema(resultadoGenericoChkbSN(this.chkEritemaS, this.chkEritemaN));
    			hojaConsulta.setDolorGarganta(resultadoGenericoChkbSND(this.chkDolorGargantaS, this.chkDolorGargantaN, this.chkDolorGargantaD));
    			hojaConsulta.setAdenopatiasCervicales(resultadoGenericoChkbSN(this.chkAdenopatiasCervicalesS, this.chkAdenopatiasCervicalesN));
    			hojaConsulta.setExudado(resultadoGenericoChkbSN(this.chkExudadoS, this.chkExudadoN));
    			hojaConsulta.setPetequiasMucosa(resultadoGenericoChkbSN(this.chkPetequiasMucosaS, this.chkPetequiasMucosaN));
    			hojaConsulta.setTos(resultadoGenericoChkbSND(this.chkTosS, this.chkTosN, this.chkTosD));
    			hojaConsulta.setRinorrea(resultadoGenericoChkbSND(this.chkRinorreaS, this.chkRinorreaN, this.chkRinorreaD));
    			hojaConsulta.setCongestionNasal(resultadoGenericoChkbSND(this.chkCongestionNasalS, this.chkCongestionNasalN, this.chkCongestionNasalD));
    			hojaConsulta.setOtalgia(resultadoGenericoChkbSND(this.chkOtalgiaS, this.chkOtalgiaN, this.chkOtalgiaD));
    			hojaConsulta.setAleteoNasal(resultadoGenericoChkbSN(this.chkAleteoNasalS, this.chkAleteoNasalN));
    			hojaConsulta.setApnea(resultadoGenericoChkbSN(this.chkApneaS, this.chkApneaN));
    			hojaConsulta.setRespiracionRapida(resultadoGenericoChkbSN(this.chkRespiracionRapidaS, this.chkRespiracionRapidaN));
    			hojaConsulta.setQuejidoEspiratorio(resultadoGenericoChkbSN(this.chkQuejidoEspiratorioS, this.chkQuejidoEspiratorioN));
    			hojaConsulta.setEstiradorReposo(resultadoGenericoChkbSN(this.chkEstridorReposoS, this.chkEstridorReposoN));
    			hojaConsulta.setTirajeSubcostal(resultadoGenericoChkbSN(this.chkTirajeSubcostalS, this.chkTirajeSubcostalN));
    			hojaConsulta.setSibilancias(resultadoGenericoChkbSN(this.chkSibilanciasS, this.chkSibilanciasN));
    			hojaConsulta.setCrepitos(resultadoGenericoChkbSN(this.chkCrepitosS, this.chkCrepitosN));
    			hojaConsulta.setRoncos(resultadoGenericoChkbSN(this.chkRoncosS, this.chkRoncosN));
    			hojaConsulta.setOtraFif(resultadoGenericoChkbSN(this.chkOtraFIFS, this.chkOtraFIFN));
    			hojaConsulta.setNuevaFif(this.txtNuevaFif.getValue());
    			hojaConsulta.setPocoApetito(resultadoGenericoChkbSND(this.chkPocoApetitoS, this.chkPocoApetitoN, this.chkPocoApetitoD));
    			hojaConsulta.setNausea(resultadoGenericoChkbSND(this.chkNauseaS, this.chkNauseaN, this.chkNauseaD));
    			hojaConsulta.setDificultadAlimentarse(resultadoGenericoChkbSND(this.chkDificultadAlimentaseS, this.chkDificultadAlimentaseN, this.chkDificultadAlimentaseD));
    			hojaConsulta.setVomito12horas(resultadoGenericoChkbSND(this.chkVomito12HorasS, this.chkVomito12HorasN, this.chkVomito12HorasD));
    			hojaConsulta.setDiarrea(resultadoGenericoChkbSND(this.chkDiarreaS, this.chkDiarreaN, this.chkDiarreaD));
    			hojaConsulta.setDiarreaSangre(resultadoGenericoChkbSND(this.chkDiarreaSangreS, this.chkDiarreaSangreN, this.chkDiarreaSangreD));
    			hojaConsulta.setEstrenimiento(resultadoGenericoChkbSND(this.chkEstrenimientoS, this.chkEstrenimientoN, this.chkEstrenimientoD));
    			hojaConsulta.setDolorAbIntermitente(resultadoGenericoChkbSND(this.chkDolorAbInterS, this.chkDolorAbInterN, this.chkDolorAbInterD));
    			hojaConsulta.setDolorAbContinuo(resultadoGenericoChkbSND(this.chkDolorAbContinuoS, this.chkDolorAbContinuoN, this.chkDolorAbContinuoD));
    			hojaConsulta.setEpigastralgia(resultadoGenericoChkbSND(this.chkEpigastralgiaS, this.chkEpigastralgiaN, this.chkEpigastralgiaD));
    			hojaConsulta.setIntoleranciaOral(resultadoGenericoChkbSND(this.chkIntoleranciaViaOralS, this.chkIntoleranciaViaOralN, this.chkIntoleranciaViaOralD));
    			hojaConsulta.setDistensionAbdominal(resultadoGenericoChkbSND(this.chkDistensionAbdominalS, this.chkDistensionAbdominalN, this.chkDistensionAbdominalD));
    			hojaConsulta.setHepatomegalia(resultadoGenericoChkbSN(this.chkHepatomegaliaS, this.chkHepatomegaliaN));
    			hojaConsulta.setLenguaMucosasSecas(resultadoGenericoChkbSN(this.chkLenguaMucosaSecasS, this.chkLenguaMucosaSecasN));
    			hojaConsulta.setPliegueCutaneo(resultadoGenericoChkbSN(this.chkPliegueCutaneoS, this.chkPliegueCutaneoN));
    			hojaConsulta.setOrinaReducida(resultadoGenericoChkbSND(this.chkOrinaReducidaS, this.chkOrinaReducidaN, this.chkOrinaReducidaD));
    			hojaConsulta.setBebeConSed(resultadoGenericoChkbSN(this.chkBebeAvidoSedS, this.chkBebeAvidoSedN));
    			hojaConsulta.setOjosHundidos(String.valueOf(resultadoGenericoChkbSN(this.chkOjosHundidosS, this.chkOjosHundidosN)));
    			hojaConsulta.setFontanelaHundida(resultadoGenericoChkbSN(this.chkFontanelaHundidaS, this.chkFontanelaHundidaN));
    			hojaConsulta.setSintomasUrinarios(resultadoGenericoChkbSND(this.chkSintomasUrinariosS, this.chkSintomasUrinariosN, this.chkSintomasUrinariosD));
    			hojaConsulta.setLeucocituria(resultadoGenericoChkbSND(this.chkLeucocituriaXCampoS, this.chkLeucocituriaXCampoN, this.chkLeucocituriaXCampoD));
    			hojaConsulta.setNitritos(resultadoGenericoChkbSND(this.chkNitritosS, this.chkNitritosN, this.chkNitritosD));
    			hojaConsulta.setBilirrubinuria(resultadoGenericoChkbSND(this.chkBilirrubinuriaS, this.chkBilirrubinuriaN, this.chkBilirrubinuriaD));
    			hojaConsulta.setAltralgia(resultadoGenericoChkbSND(this.chkArtralgiaS, this.chkArtralgiaN, this.chkArtralgiaD));
    			hojaConsulta.setMialgia(resultadoGenericoChkbSND(this.chkMialgiaS, this.chkMialgiaN, this.chkMialgiaD));
    			hojaConsulta.setLumbalgia(resultadoGenericoChkbSND(this.chkLumbalgiaS, this.chkLumbalgiaN, this.chkLumbalgiaD));
    			hojaConsulta.setDolorCuello(resultadoGenericoChkbSND(this.chkDolorCuelloS, this.chkDolorCuelloN, this.chkDolorCuelloD));
    			hojaConsulta.setTenosinovitis(resultadoGenericoChkbSND(this.chkTenosinovitisS, this.chkTenosinovitisN, this.chkTenosinovitisD));
    			hojaConsulta.setArtralgiaProximal(resultadoGenericoChkbSND(this.chkArtralgiaProximalS, this.chkArtralgiaProximalN, this.chkArtralgiaProximalD));
    			hojaConsulta.setArtralgiaDistal(resultadoGenericoChkbSND(this.chkArtralgiaDistalS, this.chkArtralgiaDistalN, this.chkArtralgiaDistalD));
    			hojaConsulta.setConjuntivitis(resultadoGenericoChkbSND(this.chkConjuntivitisS, this.chkConjuntivitisN, this.chkConjuntivitisD));
    			hojaConsulta.setEdemaMunecas(resultadoGenericoChkbSND(this.chkEdemaMunecasS, this.chkEdemaMunecasN, this.chkEdemaMunecasD));
    			hojaConsulta.setEdemaCodos(resultadoGenericoChkbSND(this.chkEdemaCodosS, this.chkEdemaCodosN, this.chkEdemaCodosD));
    			hojaConsulta.setEdemaHombros(resultadoGenericoChkbSND(this.chkEdemaHombrosS, this.chkEdemaHombrosN, this.chkEdemaHombrosD));
    			hojaConsulta.setEdemaRodillas(resultadoGenericoChkbSND(this.chkEdemaRodillasS, this.chkEdemaRodillasN, this.chkEdemaRodillasD));
    			hojaConsulta.setEdemaTobillos(resultadoGenericoChkbSND(this.chkEdemaTobillosS, this.chkEdemaTobillosN, this.chkEdemaTobillosD));
    			hojaConsulta.setRahsLocalizado(resultadoGenericoChkbSN(this.chkRashLocalizadoS, this.chkRashLocalizadoN));
    			hojaConsulta.setRahsGeneralizado(resultadoGenericoChkbSN(this.chkRashGeneralizadoS, this.chkRashGeneralizadoN));
    			hojaConsulta.setRashEritematoso(resultadoGenericoChkbSN(this.chkRashEritematosoS, this.chkRashEritematosoN));
    			hojaConsulta.setRahsMacular(resultadoGenericoChkbSN(this.chkRashMacularS, this.chkRashMacularN));
    			hojaConsulta.setRashPapular(resultadoGenericoChkbSN(this.chkRashPapularS, this.chkRashPapularN));
    			hojaConsulta.setRahsMoteada(resultadoGenericoChkbSN(this.chkPielMoteadaS, this.chkPielMoteadaN));
    			hojaConsulta.setRuborFacial(resultadoGenericoChkbSN(this.chkRuborFacialS, this.chkRuborFacialN));
    			hojaConsulta.setEquimosis(resultadoGenericoChkbSN(this.chkEquimosisS, this.chkEquimosisN));
    			hojaConsulta.setCianosisCentral(resultadoGenericoChkbSN(this.chkCianosisCentralS, this.chkCianosisCentralN));
    			hojaConsulta.setIctericia(resultadoGenericoChkbSN(this.chkIctericiaS, this.chkIctericiaN));
    			hojaConsulta.setObeso(resultadoGenericoChkbSND(this.chkObesoS, this.chkObesoN, this.chkObesoD));
    			hojaConsulta.setSobrepeso(resultadoGenericoChkbSND(this.chkSobrepesoS, this.chkSobrepesoN, this.chkSobrepesoD));
    			hojaConsulta.setSospechaProblema(resultadoGenericoChkbSND(this.chkSospechaProblemaS, this.chkSospechaProblemaN, this.chkSospechaProblemaD));
    			hojaConsulta.setNormal(resultadoGenericoChkbSND(this.chkNormalS, this.chkNormalN, this.chkNormalD));
    			hojaConsulta.setBajoPeso(resultadoGenericoChkbSND(this.chkBajoPesoS, this.chkBajoPesoN, this.chkBajoPesoD));
    			hojaConsulta.setBajoPesoSevero(resultadoGenericoChkbSND(this.chkBajoPesoSeveroS, this.chkBajoPesoSeveroN, this.chkBajoPesoSeveroD));
    			hojaConsulta.setLactanciaMaterna(resultadoGenericoChkbSND(this.chkLactanciaMaternaS, this.chkLactanciaMaternaN, this.chkLactanciaMaternaD));
    			hojaConsulta.setVacunasCompletas(resultadoGenericoChkbSND(this.chkVacunasCompletasS, this.chkVacunasCompletasN, this.chkVacunasCompletasD));
    			hojaConsulta.setVacunaInfluenza(resultadoGenericoChkbSND(this.chkVacunaInfluenzaS, this.chkVacunaInfluenzaN, this.chkVacunaInfluenzaD));
    			hojaConsulta.setFechaVacuna(this.txtFechaVacuna.getValue());
    			hojaConsulta.setInterconsultaPediatrica(resultadoGenericoChkbSN(this.chkInterconsultaPediatraS, this.chkInterconsultaPediatraN));
    			hojaConsulta.setReferenciaHospital(resultadoGenericoChkbSN(this.chkReferenciaHospitalS, this.chkReferenciaHospitalN));
    			hojaConsulta.setReferenciaDengue(resultadoGenericoChkbSN(this.chkReferenciaDengueS, this.chkReferenciaDengueN));
    			hojaConsulta.setReferenciaIrag(resultadoGenericoChkbSN(this.chkReferenciaIRAGS, this.chkReferenciaIRAGN));
    			hojaConsulta.setReferenciaChik(resultadoGenericoChkbSN(this.chkReferenciaCHIKS, this.chkReferenciaCHIKN));
    			hojaConsulta.setEti(resultadoGenericoChkbSN(this.chkETIS, this.chkETIN));
    			hojaConsulta.setIrag(resultadoGenericoChkbSN(this.chkIRAGS, this.chkIRAGN));
    			hojaConsulta.setNeumonia(resultadoGenericoChkbSN(this.chkNeumoniaS, this.chkNeumoniaN));
    			hojaConsulta.setSaturaciono2(Short.valueOf(this.txtSaturacion.getValue()));
    			hojaConsulta.setImc(new BigDecimal(this.txtImc.getValue()));
    			hojaConsulta.setCategoria(this.cmbCategoria.getSelectedItem().getValue().toString().toUpperCase());
    			if (this.cmbCambioCategoria.getValue().equals("0")) {
    				hojaConsulta.setCambioCategoria('0');
    			} else if (this.cmbCambioCategoria.getValue().equals("1")){
    				hojaConsulta.setCambioCategoria('1');
    			} else {
    				hojaConsulta.setCambioCategoria(null);
    			}
    			hojaConsulta.setManifestacionHemorragica(resultadoGenericoChkbSN(this.chkManifestacionHemorragicaS, this.chkManifestacionHemorragicaN));
    			hojaConsulta.setPruebaTorniquetePositiva(resultadoGenericoChkbSN(this.chkPruebaTorniquetePositivaS, this.chkPruebaTorniquetePositivaN));
    			hojaConsulta.setPetequia10Pt(resultadoGenericoChkbSN(this.chkPetequias10S, this.chkPetequias10N));
    			hojaConsulta.setPetequia20Pt(resultadoGenericoChkbSN(this.chkPetequias20S, this.chkPetequias20N));
    			hojaConsulta.setPielExtremidadesFrias(resultadoGenericoChkbSN(this.chkPielExtremidadesFriasS, this.chkPielExtremidadesFriasN));
    			hojaConsulta.setPalidezEnExtremidades(resultadoGenericoChkbSN(this.chkPalidezExtremidadesS, this.chkPalidezExtremidadesN));
    			hojaConsulta.setEpistaxis(resultadoGenericoChkbSN(this.chkEpistaxisS, this.chkEpistaxisN));
    			hojaConsulta.setGingivorragia(resultadoGenericoChkbSN(this.chkGingivorragiaS, this.chkGingivorragiaN));
    			hojaConsulta.setPetequiasEspontaneas(resultadoGenericoChkbSN(this.chkPetequiasEspontaneasS, this.chkPetequiasEspontaneasN));
    			hojaConsulta.setLlenadoCapilar2seg(resultadoGenericoChkbSN(this.chkLlenadoCapilarS, this.chkLlenadoCapilarN));
    			hojaConsulta.setCianosis(resultadoGenericoChkbSN(this.chkCianosisS, this.chkCianosisN));
    			if (this.txtLinfocitoAtipicos.getValue() != null && this.txtLinfocitoAtipicos.getValue() != "") {
    				hojaConsulta.setLinfocitosaAtipicos(new BigDecimal(this.txtLinfocitoAtipicos.getValue()));
    			} else {
    				hojaConsulta.setLinfocitosaAtipicos(null);
    			}
    			hojaConsulta.setFechaLinfocitos(this.txtFechaLinfocitoAtipicos.getValue());
    			hojaConsulta.setHipermenorrea(resultadoGenericoChkbSND(this.chkHipermenorreaS, this.chkHipermenorreaN, this.chkHipermenorreaD));
    			hojaConsulta.setHematemesis(resultadoGenericoChkbSN(this.chkHematemesisS, this.chkHematemesisN));
    			hojaConsulta.setMelena(resultadoGenericoChkbSN(this.chkMelenaS, this.chkMelenaN));
    			if (this.txtHemoconcentracion.getValue() != null && this.txtHemoconcentracion.getValue() != "") {
    				hojaConsulta.setHemoconcentracion(Short.valueOf(this.txtHemoconcentracion.getValue()));
    			} else {
    				hojaConsulta.setHemoconcentracion(null);
    			}
    			hojaConsulta.setHospitalizado(resultadoGenericoChkbSN(this.chkHospitalizadoUltimoAnioS, this.chkHospitalizadoUltimoAnioN));
    			hojaConsulta.setHospitalizadoEspecificar(this.txtHospitalizadoUltimoAnio.getValue());
    			hojaConsulta.setTransfusionSangre(resultadoGenericoChkbSN(this.chkTransfusionSangreS, this.chkTransfusionSangreN));
    			hojaConsulta.setTransfusionEspecificar(this.txtTransfusionSangre.getValue());
    			hojaConsulta.setTomandoMedicamento(resultadoGenericoChkbSN(this.chkEstaTomandoMedicamentoS, this.chkEstaTomandoMedicamentoN));
    			hojaConsulta.setMedicamentoEspecificar(this.txtEstaTomandoMedicamento.getValue());
    			hojaConsulta.setMedicamentoDistinto(resultadoGenericoChkbSN(this.chkTomaMedicamentoDistintoS, this.chkTomaMedicamentoDistintoN));
    			hojaConsulta.setMedicamentoDistEspecificar(this.txtTomaMedicamentoDistinto.getValue());
    			hojaConsulta.setBhc(resultadoGenericoChkbSN(this.chkBhcS, this.chkBhcN));
    			hojaConsulta.setSerologiaDengue(resultadoGenericoChkbSN(this.chkSerologiaDengueS, this.chkSerologiaDengueN));
    			hojaConsulta.setSerologiaChik(resultadoGenericoChkbSN(this.chkSerologiaChikS, this.chkSerologiaChikN));
    			hojaConsulta.setGotaGruesa(resultadoGenericoChkbSN(this.chkGotaGruesaS, this.chkGotaGruesaN));
    			hojaConsulta.setExtendidoPeriferico(resultadoGenericoChkbSN(this.chkExtendidoPerifericoS, this.chkExtendidoPerifericoN));
    			hojaConsulta.setEgo(resultadoGenericoChkbSN(this.chkEgoS, this.chkEgoN));
    			hojaConsulta.setEgh(resultadoGenericoChkbSN(this.chkEghS, this.chkEghN));
    			hojaConsulta.setCitologiaFecal(resultadoGenericoChkbSN(this.chkCitologiaFecalS, this.chkCitologiaFecalN));
    			hojaConsulta.setFactorReumatoideo(resultadoGenericoChkbSN(this.chkFactorReumatoideoS, this.chkFactorReumatoideoN));
    			hojaConsulta.setAlbumina(resultadoGenericoChkbSN(this.chkAlbuminaS, this.chkAlbuminaN));
    			hojaConsulta.setAstAlt(resultadoGenericoChkbSN(this.chkAstAltS, this.chkAstAltN));
    			hojaConsulta.setBilirrubinas(resultadoGenericoChkbSN(this.chkBilirubinasS, this.chkBilirubinasN));
    			hojaConsulta.setCpk(resultadoGenericoChkbSN(this.chkCpkS, this.chkCpkN));
    			hojaConsulta.setColesterol(resultadoGenericoChkbSN(this.chkColesterolS, this.chkColesterolN));
    			hojaConsulta.setInfluenza(resultadoGenericoChkbSN(this.chkInfluenzaS, this.chkInfluenzaN));
    			hojaConsulta.setOel(resultadoGenericoChkbSN(this.chkOtroExamenS, this.chkOtroExamenN));
    			hojaConsulta.setOtroExamenLab(this.txtOtroExamen.getValue());
    			hojaConsulta.setAcetaminofen(resultadoGenericoChkbSN(this.chkAcetaminofenS, this.chkAcetaminofenN));
    			hojaConsulta.setAsa(resultadoGenericoChkbSN(this.chkAsaS, this.chkAsaN));
    			hojaConsulta.setIbuprofen(resultadoGenericoChkbSN(this.chkIbuprofenS, this.chkIbuprofenN));
    			hojaConsulta.setPenicilina(resultadoGenericoChkbSN(this.chkPenicilinaS, this.chkPenicilinaN));
    			hojaConsulta.setAmoxicilina(resultadoGenericoChkbSN(this.chkAmoxicilinaS, this.chkAmoxicilinaN));
    			hojaConsulta.setDicloxacilina(resultadoGenericoChkbSN(this.chkDicloxacilinaS, this.chkDicloxacilinaN));
    			hojaConsulta.setOtroAntibiotico(this.txtOtroAntibiotico.getValue());
    			hojaConsulta.setFurazolidona(resultadoGenericoChkbSN(this.chkFurazolidonaS, this.chkFurazolidonaN));
    			hojaConsulta.setMetronidazolTinidazol(resultadoGenericoChkbSN(this.chkMetronidazolTinidazolS, this.chkMetronidazolTinidazolN));
    			hojaConsulta.setAlbendazolMebendazol(resultadoGenericoChkbSN(this.chkAlbendazolMebendazolS, this.chkAlbendazolMebendazolN));
    			hojaConsulta.setSulfatoFerroso(resultadoGenericoChkbSN(this.chkSulfatoFerrosoS, this.chkSulfatoFerrosoN));
    			hojaConsulta.setSueroOral(resultadoGenericoChkbSN(this.chkSueroOralS, this.chkSueroOralN));
    			hojaConsulta.setSulfatoZinc(resultadoGenericoChkbSN(this.chkSulfatoZincS, this.chkSulfatoZincN));
    			hojaConsulta.setLiquidosIv(resultadoGenericoChkbSN(this.chkLiquidosIVS, this.chkLiquidosIVN));
    			hojaConsulta.setPrednisona(resultadoGenericoChkbSN(this.chkPrednisonaS, this.chkPrednisonaN));
    			hojaConsulta.setHidrocortisonaIv(resultadoGenericoChkbSN(this.chkHidrocortisonaIVS, this.chkHidrocortisonaIVN));
    			hojaConsulta.setSalbutamol(resultadoGenericoChkbSN(this.chkSalbutamolS, this.chkSalbutamolN));
    			hojaConsulta.setOseltamivir(resultadoGenericoChkbSN(this.chkOseltamivirS, this.chkOseltamivirN));
    			hojaConsulta.setHistoriaExamenFisico(this.txtHistoriaExamenFisico.getValue());
    			hojaConsulta.setDiagnostico1(Short.valueOf(this.cmbDiagnostico1.getSelectedItem().getValue().toString()));
    			hojaConsulta.setDiagnostico2(Short.valueOf(this.cmbDiagnostico2.getSelectedItem().getValue().toString()));
    			hojaConsulta.setDiagnostico3(Short.valueOf(this.cmbDiagnostico3.getSelectedItem().getValue().toString()));
    			hojaConsulta.setDiagnostico4(Short.valueOf(this.cmbDiagnostico4.getSelectedItem().getValue().toString()));
    			hojaConsulta.setOtroDiagnostico(this.txtOtroDiagnostico.getValue());
    			hojaConsulta.setProximaCita(this.txtFechaProximaCita.getValue());
    			if (this.cmbHorarioClases.getValue() != null && this.cmbHorarioClases.getValue() != "") {
    				if (this.cmbHorarioClases.getValue().trim().equals("AM")) {
    					hojaConsulta.setHorarioClases("M");
    				}
    				else if (this.cmbHorarioClases.getValue().trim().equals("PM")) {
    					hojaConsulta.setHorarioClases("V");
    				}
    				else {
    					hojaConsulta.setHorarioClases("N");
    				}
    			}
    			/*Fecha Cierre*/
    			Date fechaCierre = this.txtFechaCierre.getValue();
    	    	String horaCierre = this.txtHoraFechaCierre.getValue();
    	    	String fec = UtilDate.DateToString(fechaCierre, "yyyy-MM-dd");
    	    	String result = fec + " " + horaCierre;
    	    	Date fCierre = UtilDate.StringToDate(result, "yyyy-MM-dd HH:mm");
    			hojaConsulta.setFechaCierre(fCierre);
    			
    			hojaConsulta.setFechaCambioTurno(null);
    			hojaConsulta.setFechaCierreCambioTurno(null);
    			hojaConsulta.setAmPmUltDiaFiebre(this.cmbUltDiaFiebre.getValue());
    			String horaUltDosisAntipiretico = this.txtHoraUltDosisAntipiretico.getValue();
    			if(horaUltDosisAntipiretico != null && horaUltDosisAntipiretico != "") {
    				hojaConsulta.setHoraUltDosisAntipiretico( horaUltDosisAntipiretico.trim().length() > 0 ? new SimpleDateFormat("hh:mm").parse(horaUltDosisAntipiretico) : null);
    			} else {
    				hojaConsulta.setHoraUltDosisAntipiretico(null);
    			}
    			if(this.cmbUltDosisAntipiretico.getValue().trim().equals("AM")) {
    				hojaConsulta.setAmPmUltDosisAntipiretico("a. m.");
    			} else if (this.cmbUltDosisAntipiretico.getValue().trim().equals("PM")) {
    				hojaConsulta.setAmPmUltDosisAntipiretico("p. m.");
    			} else {
    				hojaConsulta.setAmPmUltDosisAntipiretico(null);
    			}
    			
    			hojaConsulta.setExpedienteFisico(this.txtExpediente.getValue());
    			if (this.cmbColegio.getSelectedItem().getValue().toString().trim().equals("0")) {
    				hojaConsulta.setColegio(null);
    			} else {
    				hojaConsulta.setColegio(this.cmbColegio.getSelectedItem().getValue().toString());
    			}
    			
    			hojaConsulta.setFechaOrdenLaboratorio(null);
    			hojaConsulta.setEstadoCarga(null);
    			hojaConsulta.setOtro(resultadoGenericoChkbSN(this.chkOtroAntibioticoS, this.chkOtroAntibioticoN));
    			hojaConsulta.setFis(this.txtFis.getValue());
    			hojaConsulta.setFif(this.txtFif.getValue());
    			if (this.txtHepatomegalia.getValue() != null && this.txtHepatomegalia.getValue() != "") {
    				hojaConsulta.setHepatomegaliaCm(new BigDecimal(this.txtHepatomegalia.getValue()));
    			} else {
    				hojaConsulta.setHepatomegaliaCm(null);
    			}
    			
    			hojaConsulta.setEritrocitos(resultadoGenericoChkbSND(this.chkEritrocitosXCampoS, this.chkEritrocitosXCampoN, this.chkEritrocitosXCampoD));
    			hojaConsulta.setPlanes(this.txtPlanes.getValue());
    			hojaConsulta.setMedicoCambioTurno(null);
    			hojaConsulta.setHemoconc(resultadoGenericoChkbSND(this.chkHemoconcentracionS, this.chkHemoconcentracionN, this.chkHemoconcentracionD));
    			if (this.txtVomito12Horas.getValue() != null && this.txtVomito12Horas.getValue() != "") {
    				hojaConsulta.setVomito12h(Short.valueOf(this.txtVomito12Horas.getValue()));
    			} else {
    				hojaConsulta.setVomito12h(null);
    			}
    			hojaConsulta.setPad(Short.valueOf(this.txtPad.getValue()));
    			hojaConsulta.setPas(Short.valueOf(this.txtPas.getValue()));
    			if (this.txtTelefono.getValue() != null && this.txtTelefono.getValue() != "") {
    				hojaConsulta.setTelef(Long.valueOf(this.txtTelefono.getValue()));
    			} else {
    				hojaConsulta.setTelef(null);
    			}
    			
    			hojaConsulta.setHora(this.txtHoraMedico.getValue() + " " +this.cmbHoraMedico.getValue());
    			hojaConsulta.setHorasv(this.txtHora.getValue() + " " +this.cmbHora.getValue());
    			hojaConsulta.setNoAtiendeLlamadoEnfermeria(null);
    			hojaConsulta.setNoAtiendeLlamadoMedico(null);
    			hojaConsulta.setEstudiosParticipantes(this.txtEstudiosPaciente.getValue());
    			hojaConsulta.setUaf(false);
    			hojaConsulta.setRepeatKey(null);
    			hojaConsulta.setCv(resultadoGenericoChkbSN(this.chkCVS, this.chkCVN));
    			hojaConsulta.setConsultaRespiratorio('1');
    			Short usuario =  (short) Utilidades.obtenerInfoSesion().getUsuarioId();
    			hojaConsulta.setUsuarioCierraHoja(usuario);
    			hojaConsulta.setHojaImpresa('S');
    			hojaConsulta.setNumOrdenLaboratorio(null);
    			hojaConsulta.setDigitada(true);
    			
    			if (fCierre.before(fConsulta)) {
    				Mensajes.enviarMensaje("La Fecha de Consulta es mayor que la Fecha de Cierre, favor verificar", Mensajes.TipoMensaje.ERROR);
    				this.btnGuardar.setDisabled(false);
    				return;
    			}
   
    			InfoResultado resultado = hojaConsultaService.guardarHojaConsulta(hojaConsulta);
    			if(resultado.isOk() && resultado.getObjeto()!=null) {
    				hojaConsulta = (HojaConsulta)resultado.getObjeto();
    				this.txtNumHojaConsulta.setValue(String.valueOf(hojaConsulta.getNumHojaConsulta()));
    				this.txtSecHojaConsulta.setValue(String.valueOf(hojaConsulta.getSecHojaConsulta()));
    				this.btnGuardar.setDisabled(true);
    				Mensajes.enviarMensaje(resultado);
    			} else {
    				this.btnGuardar.setDisabled(false);
    				Mensajes.enviarMensaje(resultado);
    			}
    		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();		
			this.btnGuardar.setDisabled(false);
			Mensajes.enviarMensaje(Mensajes.ERROR_PROCESAR_DATOS + ". " +e.getMessage() , Mensajes.TipoMensaje.ERROR);
		}
    }
    /***
     * Metodo para obtener el valor para S y N, en seleccion de chekbox
     *
     * @return Character, S = 0, N = 1
     */
	public static Character resultadoGenericoChkbSN(Checkbox viewS, Checkbox viewN) {
        return viewS.isChecked() ? '0' : '1';
    }
	
	/***
     * Metodo para obtener el valor para S, N, D, en seleccion de chekbox
     *
     * @return Character, S = 0, N = 1, D = 2
     */
    public static Character resultadoGenericoChkbSND(Checkbox viewS, Checkbox viewN, Checkbox viewD) {
        return ((viewS).isChecked()) ? '0' : ((viewN).isChecked()) ? '1' : '2';
    }
    
    private boolean estaEnRango(double min, double max, String valor) {
        double valorComparar = Double.parseDouble(valor);
        return (valorComparar >= min && valorComparar <= max) ? true : false;
    }

    public void getMethodImprimirPdf() throws Exception {
    	
    	config = UtilProperty.getConfigurationfromExternalFile("MovilWS.properties");
    	
    	SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        
        // Generar la solicitud SOAP XML

        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        MimeHeaders header = soapMessage.getMimeHeaders();
        header.setHeader("SOAPAction", "http://webservice.estudiocohortecssfv.sts_ni.com/EstudioCohorteCSSFVMovilWSService/imprimirConsultaPdf");
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("web", "http://webservice.estudiocohortecssfv.sts_ni.com/");
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("imprimirConsultaPdf", "web");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("secHojaConsulta");
        soapBodyElem1.addTextNode(this.txtSecHojaConsulta.getValue());
        soapMessage.saveChanges();
        soapMessage.writeTo(System.out);
        
        // Llamando al servicio web
        String url = config.getString("CSSFV.URLWS");
        
        SOAPMessage soapResponse = soapConnection.call(soapMessage, url);
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        
        // Imprimiendo la respuesta del SOAP
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        System.out.println("Response SOAP Message \n");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
        soapConnection.close();
        
    }
    
    public void getPdf() throws Exception {
		if (this.txtSecHojaConsulta.getValue() != "" && this.txtSecHojaConsulta.getValue() != null) {
			config = UtilProperty.getConfiguration("EstudioCohorteCssfvWEBExt.properties",
					"ni/com/sts/estudioCohorteCssfv/properties/EstudioCohorteCssfvWEBInt.properties");
			String nombre = "HojaConsulta_" + this.txtSecHojaConsulta.getValue();
			String path = System.getProperty("jboss.server.data.dir") + System.getProperty("file.separator").charAt(0)
					+ config.getString("ruta.pdf") + (nombre.contains(".pdf") ? nombre : nombre + ".pdf");
			path = path.replace('/', System.getProperty("file.separator").charAt(0));

			File myFile = new File(path);
			URL url = myFile.toURI().toURL();

			byte[] ba = new byte[(int) myFile.length()];
			if (ba.length > 0) {
				Window win = (Window) Executions.createComponents("/visorReportes/hojaConsultaReporte.zul", null, null);
				win.setTitle("Hoja Consulta");

				int baLength;
				InputStream is = null;
				ByteArrayOutputStream bios = new ByteArrayOutputStream();
				URLConnection urlConn = url.openConnection();
				// Comprobando si la URL contiene un PDF
				if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
					Mensajes.enviarMensaje("El archivo no es PDF", Mensajes.TipoMensaje.ERROR);
				} else {
					// Se lee el PDF de la URL y se guarda en un archivo local
					is = url.openStream();
					while ((baLength = is.read(ba)) != -1) {
						bios.write(ba, 0, baLength);
					}
					final AMedia amedia = new AMedia(nombre, "pdf", "application/pdf", bios.toByteArray());
					Iframe iframe = (Iframe) win.getFellow("iframeHojaConsulta");
					iframe.setContent(amedia);
				}
			} else {
				Mensajes.enviarMensaje("No existe el archivo PDF", Mensajes.TipoMensaje.ERROR);
				return;
			}

		} else {
			Mensajes.enviarMensaje("Debe guardar la información, para poder ver el Pdf", Mensajes.TipoMensaje.ERROR);
			return;
		}
    }
}
