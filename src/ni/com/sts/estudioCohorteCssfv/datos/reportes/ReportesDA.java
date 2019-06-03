package ni.com.sts.estudioCohorteCssfv.datos.reportes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.axis.types.Day;
import org.apache.poi.hssf.record.formula.functions.Days360;
import org.hibernate.Query;
import org.hibernate.mapping.Fetchable;

import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;
import ni.com.sts.estudioCohorteCSSFV.modelo.HojaInfluenza;
import ni.com.sts.estudioCohorteCssfv.dto.OrdenesExamenes;
import ni.com.sts.estudioCohorteCssfv.servicios.ReportesService;
import ni.com.sts.estudioCohorteCssfv.util.FiltroReporte;
import ni.com.sts.estudioCohorteCssfv.util.Generico;
import ni.com.sts.estudioCohorteCssfv.util.HibernateResource;
import ni.com.sts.estudioCohorteCssfv.util.UtilDate;

public class ReportesDA implements ReportesService {

	private static final HibernateResource hibernateResource = new HibernateResource();
	
	@Override
	public List<Generico> getReporteGeneral(FiltroReporte filtro) throws Exception {
		List<Generico> resultado = new ArrayList<Generico>();
		
		try{
			String sql = " select hc.numHojaConsulta, hc.codExpediente, hc.fechaConsulta, eh.descripcion," +
					"    p.nombre1,p.nombre2,p.apellido1,p.apellido2,p.fechaNac,p.sexo,p.retirado, (select usu.nombre from UsuariosView as usu where usu.id = hc.usuarioMedico) as nombre, " +
					" (select ad.secAdmision from Admision as ad where ad.numHojaConsulta = hc.numHojaConsulta and to_char(ad.fechaSalida, 'yyyyMMdd') = to_char(hc.fechaConsulta, 'yyyyMMdd')) as ex, " +
					" (select count(ad.codExpediente) from Admision as ad where ad.numHojaConsulta = hc.numHojaConsulta and to_char(ad.fechaSalida, 'yyyyMMdd') = to_char(hc.fechaConsulta, 'yyyyMMdd') and ad.fechaEntrada is null) as adm, " +
					" (select ad.tipoConsulta.descripcion from Admision ad where ad.numHojaConsulta = hc.numHojaConsulta), "+
					" (select usu.nombre from Admision ad,UsuariosView usu where ad.usuarioRecibe = usu.id and ad.numHojaConsulta = hc.numHojaConsulta) "+
					"    from HojaConsulta as hc, Paciente as p, EstadosHoja as eh" +
					"    where hc.codExpediente = p.codExpediente" +
					"	and eh.codigo = hc.estado";
			
			String sqlSoloAdmision = "select '', ad.codExpediente, ad.fechaSalida, '', p.nombre1,p.nombre2,p.apellido1,p.apellido2,p.fechaNac,p.sexo,p.retirado, " +
					" '', 1 as ex, " +
					" (select count(ad2.codExpediente) from Admision as ad2 where ad.secAdmision = ad2.secAdmision and ad2.fechaEntrada is null) as adm, " +
					" ad.tipoConsulta.descripcion, " +
					"(select usu.nombre from UsuariosView usu where ad.usuarioRecibe = usu.id) " +
					"from Admision as ad,Paciente as p where ad.codExpediente = p.codExpediente and ad.numHojaConsulta is null";
			
			if (filtro.getMostrarTodo()){
				sql = sql + "	and to_char(hc.fechaConsulta, 'yyyyMMdd') >= :tresmeses";
				sqlSoloAdmision = sqlSoloAdmision + "	and to_char(ad.fechaSalida, 'yyyyMMdd') >= :tresmeses";
			}
			
			if (filtro.getCodigoExpediente()!=null){
				sql = sql + "	and p.codExpediente = :codExpediente";
				sqlSoloAdmision = sqlSoloAdmision + "	and p.codExpediente = :codExpediente";
			}
			if(filtro.getEstadoHojaConsulta()!=null && !filtro.getEstadoHojaConsulta().isEmpty()){
				sql = sql + "	and eh.codigo =  :codigo";
			}
			if (filtro.getFechaInicioConsulta()!=null && filtro.getFechaFinConsulta()!=null){
				sql = sql + "	and to_char(hc.fechaConsulta, 'yyyyMMdd') >= :fecInicio" +
						"	    and to_char(hc.fechaConsulta, 'yyyyMMdd') <= :fecFin";
				sqlSoloAdmision = sqlSoloAdmision + "	and to_char(ad.fechaSalida, 'yyyyMMdd') >= :fecInicio" +
						"	    and to_char(ad.fechaSalida, 'yyyyMMdd') <= :fecFin";
			}
			if (filtro.getMedico()!=null && !filtro.getMedico().isEmpty()){
				//sql = sql + " and lower(usu.nombre) like :medico ";
				sql = sql + " and hc.usuarioMedico = :medico ";
			}
			
			sql = sql + " order by hc.numHojaConsulta asc";
			sqlSoloAdmision = sqlSoloAdmision + " order by ad.secAdmision asc";

			Query q = hibernateResource.getSession().createQuery(sql);
			if (filtro.getMostrarTodo()){
				Calendar c1 = GregorianCalendar.getInstance();
				c1.add(Calendar.MONTH, -3);
				String limiteTresMeses = UtilDate.DateToString(c1.getTime(), "yyyyMMdd");
				q.setParameter("tresmeses", limiteTresMeses);
			}
			if (filtro.getCodigoExpediente()!=null){
				q.setParameter("codExpediente", filtro.getCodigoExpediente());
			}
			if(filtro.getEstadoHojaConsulta()!=null && !filtro.getEstadoHojaConsulta().isEmpty()){
				q.setParameter("codigo", filtro.getEstadoHojaConsulta().charAt(0));
			}
			if (filtro.getFechaInicioConsulta()!=null && filtro.getFechaFinConsulta()!=null){
				q.setParameter("fecInicio", UtilDate.DateToString(filtro.getFechaInicioConsulta(),"yyyyMMdd"));
				q.setParameter("fecFin", UtilDate.DateToString(filtro.getFechaFinConsulta(),"yyyyMMdd"));
			}
			if (filtro.getMedico()!=null && !filtro.getMedico().isEmpty()){
				//q.setParameter("medico", "%"+filtro.getMedico()+"%");
				q.setInteger("medico", Integer.valueOf(filtro.getMedico()));
			}   
			
			List<Object[]> lista = (List<Object[]>) q.list();
			
			q = hibernateResource.getSession().createQuery(sqlSoloAdmision);
			if (filtro.getMostrarTodo()){
				Calendar c1 = GregorianCalendar.getInstance();
				c1.add(Calendar.MONTH, -3);
				String limiteTresMeses = UtilDate.DateToString(c1.getTime(), "yyyyMMdd");
				q.setParameter("tresmeses", limiteTresMeses);
			}
			if (filtro.getCodigoExpediente()!=null){
				q.setParameter("codExpediente", filtro.getCodigoExpediente());
			}
			if (filtro.getFechaInicioConsulta()!=null && filtro.getFechaFinConsulta()!=null){
				q.setParameter("fecInicio", UtilDate.DateToString(filtro.getFechaInicioConsulta(),"yyyyMMdd"));
				q.setParameter("fecFin", UtilDate.DateToString(filtro.getFechaFinConsulta(),"yyyyMMdd"));
			}
			lista.addAll(q.list());
			
			if (lista != null && lista.size() > 0) {
				for (Object[] object : lista) {
					String nombrePaciente = "";
					Generico data = new Generico();
					if (object[0]!=null){
						data.setTexto1(object[0].toString()); //num consulta
					}else{
						data.setTexto1("");
					}
					if (object[1]!=null){
						data.setTexto2(object[1].toString()); //codigo expediente
					}else{
						data.setTexto2("");
					}
					if (object[2]!=null && !object[2].toString().isEmpty()){
						data.setTexto3(UtilDate.DateToString((Date)object[2], "dd/MM/yyyy")); //fecha consulta o fecha de salida si no hay consulta solo admisión
					}else{
						data.setTexto3("");
					}
					if (object[3]!=null){
						data.setTexto4(object[3].toString()); //estado hoja
					}else{
						data.setTexto4("");
					}
					if (object[4]!=null){ //nombre paciente
						nombrePaciente = object[4].toString(); 
					}
					if (object[5]!=null){
						nombrePaciente = nombrePaciente + " " +object[5].toString();
					}
					if (object[6]!=null){
						nombrePaciente = nombrePaciente + " " +object[6].toString();
					}
					if (object[7]!=null){
						nombrePaciente = nombrePaciente + " " +object[7].toString();
					}
					data.setTexto5(nombrePaciente);
					data.setTexto6(UtilDate.DateToString((Date)object[8], "dd/MM/yyyy")); //fecha nacimiento
					data.setTexto7(object[9].toString()); //sexo
					data.setTexto8(object[10].toString()); //retirado
					if (object[11]!=null){
						data.setTexto9(object[11].toString()); //medico
					}else{
						data.setTexto9("");
					}
					
					if (object[12]!=null){
						if (object[13]!=null){
						data.setTexto10(object[13].toString().equals("0")?"Recibido":"Pendiente"); //en admisión
						}else{
							data.setTexto10("Pendiente");
						}
						data.setTexto11(object[14].toString()); // tipo consulta
						if (object[15]!=null){
							data.setTexto12(object[15].toString()); // usuario recibe
						}else{
							data.setTexto12("");
						}
					}else{
						data.setTexto10("");
						data.setTexto11("");
						data.setTexto12("");
					}
					if (filtro.getEstadoAdmision()==null){
						resultado.add(data);
					}else if (filtro.getEstadoAdmision().equals(data.getTexto10().toUpperCase())){
							resultado.add(data);						
					}
				}
			}
   			
		} catch (Exception e) {
		 		e.printStackTrace();
		 		throw new Exception(e);
		} finally {
			if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}	
        }
        return resultado;
	}

		@Override
	public List<Generico> getReporteAuditoria(FiltroReporte filtro) throws Exception {
		List<Generico> resultado = new ArrayList<Generico>();
		
		try{
			String sql = "select cc.fecha,cc.numHojaConsulta, cc.codExpediente, cc.usuario, cc.tipoControl, cc.nombreCampo,  cc.valorCampo  "+
			"from ControlCambios cc "+
			"where 1=1 ";
								
			if (filtro.getTipoControl()!=null){
				sql = sql + " and cc.tipoControl = :tipoControl";				
			}
			
			if (filtro.getFechaInicioConsulta()!=null && filtro.getFechaFinConsulta()!=null){
				sql = sql + "	and to_char(cc.fecha, 'yyyyMMdd') >= :fecInicio" +
						"	    and to_char(cc.fecha, 'yyyyMMdd') <= :fecFin";

			}
			
			Query q = hibernateResource.getSession().createQuery(sql);
			if (filtro.getTipoControl()!=null){
				q.setParameter("tipoControl",filtro.getTipoControl());
			}
			if (filtro.getFechaInicioConsulta()!=null && filtro.getFechaFinConsulta()!=null){
				q.setParameter("fecInicio", UtilDate.DateToString(filtro.getFechaInicioConsulta(),"yyyyMMdd"));
				q.setParameter("fecFin", UtilDate.DateToString(filtro.getFechaFinConsulta(),"yyyyMMdd"));				
			}
			List<Object[]> lista = (List<Object[]>) q.list();
			
			if (lista != null && lista.size() > 0) {
				for (Object[] object : lista) {
					Generico data = new Generico();
					data.setTexto1(UtilDate.DateToString((Date)object[0], "dd/MM/yyyy")); //fecha registro
					if (object[1]!=null)
						data.setTexto2(object[1].toString()); //num consulta
					else
						data.setTexto2("");
					
					if (object[2]!=null)
						data.setTexto3(object[2].toString()); //codigo expediente					
					else
						data.setTexto3("");
					
					if (object[3]!=null)
						data.setTexto4(object[3].toString()); //usuario
					else
						data.setTexto4("");
					
					if (object[4]!=null)
						data.setTexto5(object[4].toString()); //tipo control
					else
						data.setTexto5("");
					
					if (object[5]!=null)
						data.setTexto6(object[5].toString()); //nombre campo
					else
						data.setTexto6("");
					
					if (object[6]!=null)
						data.setTexto7(object[6].toString()); //valor campo
					else
						data.setTexto7("");
					
					
					resultado.add(data);
				}
			}
			
		} catch (Exception e) {
		 		e.printStackTrace();
		 		throw new Exception(e);
		} finally {
			if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}	
        }
        return resultado;
	}		

	//REPORTE HOJA INFLUENZA
	@Override
	public List<Generico> getReporteHojaInfluenza(String estado) throws Exception {
		List<Generico> result = new ArrayList<Generico>();
		try {
			
			String sql = " select h.numHojaSeguimiento, h.codExpediente, "
					+ " to_char(fechaInicio, 'dd/MM/yyyy'), h.fis, h.fif, h.cerrado "
					+ " from HojaInfluenza h ";
			
			/*if (estado == "N") {
				sql = sql + " where to_char(h.fechaInicio, 'dd/MM/yyyy') < to_char(current_date, 'dd/MM/yyyy' -14) ";
			}*/
			
			sql = sql + "order by h.numHojaSeguimiento";
			Query query = hibernateResource.getSession().createQuery(sql);
			// query.setParameter("estado", estado);
			
			List<Object[]> lista = (List<Object[]>) query.list();
			if (lista != null && lista.size() > 0) {
				for (Object[] object : lista) {
					
					Generico data = new Generico();

				    String fechaInicio = object[2].toString();//esta es la fecha que se estaba pasando
				    String fis = object[3].toString();
				    Date fechaI = UtilDate.StringToDate(fis, "dd/MM/yyyy");
				    Date fechaActual = new Date();
				    long days = (fechaActual.getTime() - fechaI.getTime());
				    long diffDays = days / (24 * 60 * 60 * 1000);
				    String day = Long.toString(diffDays);
				    
					data.setTexto1(object[0].toString()); // Número Hoja Seguimiento
					data.setTexto2(object[1].toString()); // Código Expediente
					// data.setTexto1(UtilDate.DateToString((Date)object[2], "dd/MM/yyyy"));
					data.setTexto3(object[2].toString()); // Fecha Inicio
					data.setTexto4(object[3] != null ? object[3].toString() : ""); // FIS
					data.setTexto5(object[4] != null ? object[4].toString() : ""); // FIF
					
					if (estado.equals("S")) {
						data.setTexto6("");
					}else {
						data.setTexto6(day);
					}
					
					//se filtra por estado
					if (estado.equals("N") && diffDays > 14) { // estado pendiente
						if (object[5].toString().charAt(0) == 'N'){
							result.add(data);
						}
					}
					if (estado.equals("S")) { // estado cerrado
						if(object[5].toString().charAt(0) == 'S') {
							result.add(data);
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
	 		throw new Exception(e);
		} finally {
			if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}	
        }
		 return result;
	}
	
	//REPORTE HOJA ZIKA
	@Override
	public List<Generico> getReporteHojaZika(String estado) throws Exception {
		List<Generico> result = new ArrayList<Generico>();
		try {
			
			String sql = " select h.numHojaSeguimiento, h.codExpediente, "
					+ " to_char(fechaInicio, 'dd/MM/yyyy'), h.fis, h.fif, h.cerrado, "
					+ " h.sintomaInicial1, h.sintomaInicial2, h.sintomaInicial3, h.sintomaInicial4 "
					+ " from HojaZika h ";
			
			sql = sql + "order by h.numHojaSeguimiento";
			Query query = hibernateResource.getSession().createQuery(sql);
			// query.setParameter("estado", estado);
			
			List<Object[]> lista = (List<Object[]>) query.list();
			if (lista != null && lista.size() > 0) {
				for (Object[] object : lista) {
					
					Generico data = new Generico();

				    String fechaInicio = object[2].toString();
				    String fis = object[3].toString();
				    Date fechaI = UtilDate.StringToDate(fis, "dd/MM/yyyy");
				    Date fechaActual = new Date();
				    long days = (fechaActual.getTime() - fechaI.getTime());
				    long diffDays = days / (24 * 60 * 60 * 1000);
				    String day = Long.toString(diffDays);
				    
					data.setTexto1(object[0].toString()); // Número Hoja Seguimiento
					data.setTexto2(object[1].toString()); // Código Expediente
					data.setTexto3(object[2].toString()); // Fecha Inicio
					data.setTexto4(object[3] != null ? object[3].toString() : ""); // FIS
					data.setTexto5(object[4] != null ? object[4].toString() : ""); // FIF
					
					if(object[6] != null)
					{
						data.setTexto6(object[6].toString()); // Sintoma Inicial1
					}else {
						data.setTexto6("");
					}
					if(object[7] != null)
					{
						data.setTexto7(object[7].toString()); // Sintoma Inicial2
					}else {
						data.setTexto7("");
					}
					if(object[8] != null)
					{
						data.setTexto8(object[8].toString()); // Sintoma Inicial3
					}else {
						data.setTexto8("");
					}
					if(object[9] != null) {
						data.setTexto9(object[9].toString()); // Sintoma Inicial4
					}else {
						data.setTexto9("");
					}
					
					
					if (estado.equals("S")) {
						data.setTexto10("");
					}else {
						data.setTexto10(day);
					}
					
					//se filtra por estado
					if (estado.equals("N") && diffDays > 21) { // estado pendiente
						if (object[5].toString().charAt(0) == 'N'){
							result.add(data);
						}
					}
					if (estado.equals("S")) { // estado cerrado
						if(object[5].toString().charAt(0) == 'S') {
							result.add(data);
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
	 		throw new Exception(e);
		} finally {
			if (hibernateResource.getSession().isOpen()) {
   	 			hibernateResource.close();
   	 		}	
        }
		 return result;
	}
}
