package ni.com.sts.estudioCohorteCssfv.datos.actualizarDatos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.formula.functions.Value;
import org.hibernate.Query;

import ni.com.sts.estudioCohorteCSSFV.modelo.HojaConsulta;
import ni.com.sts.estudioCohorteCssfv.servicios.ActualizarDatosService;
import ni.com.sts.estudioCohorteCssfv.util.HibernateResource;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.UtilDate;
import ni.com.sts.estudioCohorteCssfv.util.ConnectionDAO;

public class ActualizarDatosDA extends ConnectionDAO implements ActualizarDatosService {

	private final Logger logger = Logger.getLogger(this.getClass());
	private Connection conn = null;
	PreparedStatement pst = null;

	/*
	 * Metodo para actualizar la hoja de consulta, hoja influenza y hoja de zika
	 */
	@SuppressWarnings("static-access")
	public InfoResultado updateHojaConsulta(String nombreTabla, int numHoja, String nombreCampo, String valor,
			Boolean valorNull, String usuario) {

		InfoResultado infoResultado = new InfoResultado();
		conn = getConection();
		try {

			StringBuilder builder = new StringBuilder();
			StringBuilder builderSelect = new StringBuilder();
			String repeatKey = null;
			
			if (nombreTabla.equals("hoja_consulta")) {
				//Obtener la hoja de consulta para verificar si tiene repeat_key
				
				builderSelect.append("SELECT ").append(" repeat_key ").append(" FROM ").append(nombreTabla)
				.append(" WHERE ").append("num_hoja_consulta = ").append(numHoja);
				
				pst = conn.prepareStatement(builderSelect.toString());
				ResultSet rs = pst.executeQuery();
				while (rs.next()) {
					repeatKey = rs.getString(1);
				}
				
				if (repeatKey != null) {
					// update en la hoja de consulta
					builder.append(" UPDATE ").append(nombreTabla).append(" SET ").append(nombreCampo).append(" = ")
							.append(" ? ")
							.append(" , ")
							.append(" estado_carga ").append(" = ").append(" ? ")
							.append(" WHERE ").append(" num_hoja_consulta ").append(" = ").append(numHoja);
				} else {
					// update en la hoja de consulta
					builder.append(" UPDATE ").append(nombreTabla).append(" SET ").append(nombreCampo).append(" = ")
							.append(" ? ")
							.append(" WHERE ").append(" num_hoja_consulta ").append(" = ").append(numHoja);
				}
				
			} else {
				builderSelect.append("SELECT ").append(" repeat_key ").append(" FROM ").append(nombreTabla)
				.append(" WHERE ").append("num_hoja_seguimiento = ").append(numHoja);
				
				pst = conn.prepareStatement(builderSelect.toString());
				ResultSet rs = pst.executeQuery();
				while (rs.next()) {
					repeatKey = rs.getString(1);
				}
				
				if (repeatKey != null) {
					// update para la hoja de influenza y zika
					builder.append(" UPDATE ").append(nombreTabla).append(" SET ").append(nombreCampo).append(" = ")
							.append(" ? ")
							.append(" , ")
							.append(" estado_carga ").append(" = ").append(" ? ")
							.append(" WHERE ").append(" num_hoja_seguimiento ").append(" = ").append(numHoja);
				} else {
					// update para la hoja de influenza y zika
					builder.append(" UPDATE ").append(nombreTabla).append(" SET ").append(nombreCampo).append(" = ")
							.append(" ? ")
							.append(" WHERE ").append(" num_hoja_seguimiento ").append(" = ").append(numHoja);
				}
			}

			pst = conn.prepareStatement(builder.toString());
			if (repeatKey != null) {
				pst.setString(2, null);
			}
			
			int type = getColumnType(nombreTabla, nombreCampo, conn);
			if (valorNull) {
				
				if (type == Types.VARCHAR)
					pst.setNull(1, Types.VARCHAR);
				else if (type == Types.CHAR)
					pst.setNull(1, Types.CHAR);
				else if (type == Types.DATE) {
					pst.setNull(1, Types.DATE);
				} else if (type == Types.NUMERIC) {
					pst.setNull(1, Types.NUMERIC);
				} else if (type == Types.TIMESTAMP) {
					pst.setNull(1, Types.TIMESTAMP);
				} else if (type == Types.TIME) {
					pst.setNull(1, Types.TIME);
				} else if (type == Types.SMALLINT) {
					pst.setNull(1, Types.SMALLINT);
				} else if (type == Types.INTEGER)
					pst.setNull(1, Types.INTEGER);
				else if (type == Types.FLOAT || type == Types.REAL)
					pst.setNull(1, Types.FLOAT);
			} else {
				if (type == Types.VARCHAR)
					pst.setString(1, valor);
				else if (type == Types.CHAR)
					/*if (nombreCampo.equals("consulta")) {
						pst.setString(1, valor);
					} else if (nombreCampo.equals("colegio")) {
						pst.setString(1, valor);
					} else if (nombreCampo.equals("am_pm_ult_dia_fiebre")) {
						pst.setString(1, valor);
					} else if (nombreCampo.equals("am_pm_ult_dosis_antipiretico")) {
						pst.setString(1, valor);
					} else if (nombreCampo.equals("categoria")) {
						pst.setString(1, valor);
					} else if (nombreCampo.equals("horario_clases")) {
						pst.setString(1, valor);
					}*/
					if (valor.length() > 1) {
						pst.setString(1, valor);
					} else { 
						pst.setString(1, String.valueOf(valor.charAt(0)));
					}
				else if (type == Types.DATE) {
					if (valor.trim().contains(" ")) {
						pst.setDate(1, new java.sql.Date(UtilDate.StringToDate(valor, "dd/MM/yyyy").getTime()));
					} else  {
						pst.setDate(1, new java.sql.Date(UtilDate.StringToDate(valor, "dd/MM/yyyy").getTime()));
					}
				} else if (type == Types.SMALLINT) {
					pst.setInt(1, Integer.parseInt(valor));
				} else if (type == Types.NUMERIC) {
					BigDecimal valorDecimal = new BigDecimal(valor);
					pst.setBigDecimal(1, valorDecimal);
				} else if (type == Types.TIME) {
					SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
					long ms = sdf.parse(valor).getTime();
					pst.setTime(1, new java.sql.Time(ms));
				} else if (type == Types.TIMESTAMP) {
					if (valor.trim().contains(" ")) {
						pst.setTimestamp(1, new Timestamp(UtilDate.StringToDate(valor, "dd/MM/yyyy HH:mm").getTime()));
					} else {
						pst.setDate(1, new java.sql.Date(UtilDate.StringToDate(valor, "dd/MM/yyyy").getTime()));
					}
				} else if (type == Types.INTEGER)
					pst.setInt(1, Integer.valueOf(valor));
				else if (type == Types.FLOAT || type == Types.REAL)
					pst.setFloat(1, Float.valueOf(valor));
			}

			if (nombreTabla.equals("hoja_consulta")) {
				// Guardando los cambios de la hoja de consulta en la tabla control de cambios
				insertControlCambios(nombreTabla, numHoja, usuario, nombreCampo, conn);
			} else {
				// Guardando los cambios de la hoja de influenza ó hoja de zika en la tabla
				// bitacora
				String secHoja = "";
				int dia = 0;
				int id = 0;
				String secSegSeguimiento = null;
				insertBitacora(nombreTabla, numHoja, usuario, nombreCampo, conn, valor, secHoja, dia, id, secSegSeguimiento);
			}

			pst.executeUpdate();
			infoResultado.setOk(true);
			infoResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);

		} catch (Exception e) {
			e.printStackTrace();
			infoResultado.setOk(false);
			infoResultado.setExcepcion(true);
			infoResultado.setGravedad(infoResultado.ERROR);
			infoResultado.setMensaje(Mensajes.REGISTRO_NO_GUARDADO + "\n" + e.getMessage());
			infoResultado.setFuenteError("Actualizar Datos");

		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				logger.error(" No se pudo cerrar conexión ", ex);
			}
		}
		return infoResultado;
	}

	/*
	 * Metodo para actualizar los seguimientos de influenza y de zika
	 */
	@SuppressWarnings("static-access")
	public InfoResultado updateSeguimientos(String nombreTabla, String secHoja, int numHoja, String nombreCampo,
			String valor, Boolean valorNull, String usuario, int dia, String secSegSeguimiento) {

		InfoResultado infoResultado = new InfoResultado();
		int id = 0;
		conn = getConection();
		try {
			StringBuilder builderSelect = new StringBuilder();
			StringBuilder builder = new StringBuilder();
			String repeatKey = null;
			if (nombreTabla.equals("seguimiento_influenza")) {
				builderSelect.append("SELECT ").append(secHoja + ", " + "repeat_key ").append(" FROM ").append(" hoja_influenza ")
						.append(" WHERE ").append("num_hoja_seguimiento = ").append(numHoja);
				
				pst = conn.prepareStatement(builderSelect.toString());
				ResultSet rs = pst.executeQuery();
				while (rs.next()) {
					id = rs.getInt(1);
					repeatKey = rs.getString(2);
				}
				
				if (repeatKey != null) {
					builder.append(" UPDATE ").append(" hoja_influenza ").append(" SET ")
					.append(" estado_carga ").append(" = ").append(" ? ")
					.append(" WHERE ").append(" num_hoja_seguimiento ").append(" = ").append(numHoja);
					
					pst = conn.prepareStatement(builder.toString());
					pst.setString(1, null);
					pst.executeUpdate();
				}
			} else {
				builderSelect.append("SELECT ").append(secHoja  + ", " + "repeat_key ").append(" FROM ").append(" hoja_zika ")
						.append(" WHERE ")
						.append("num_hoja_seguimiento = ").append(numHoja);
				
				pst = conn.prepareStatement(builderSelect.toString());
				ResultSet rs = pst.executeQuery();
				while (rs.next()) {
					id = rs.getInt(1);
					repeatKey = rs.getString(2);
				}
				
				if (repeatKey != null) {
					builder.append(" UPDATE ").append(" hoja_zika ").append(" SET ")
					.append(" estado_carga ").append(" = ").append(" ? ")
					.append(" WHERE ").append(" num_hoja_seguimiento ").append(" = ").append(numHoja);
					
					pst = conn.prepareStatement(builder.toString());
					pst.setString(1, null);
					pst.executeUpdate();
				}
			}

			StringBuilder builderUpdate = new StringBuilder();
			builderUpdate.append("UPDATE ").append(nombreTabla).append(" SET ").append(nombreCampo).append(" = ")
					.append(" ? ").append(" WHERE ").append(secHoja).append(" = ").append(id).append(" AND ")
					.append("control_dia = ").append(dia);

			pst = conn.prepareStatement(builderUpdate.toString());

			int type = getColumnType(nombreTabla, nombreCampo, conn);

			if (valorNull) {
				if (type == Types.VARCHAR)
					pst.setNull(1, Types.VARCHAR);
				else if (type == Types.CHAR)
					pst.setNull(1, Types.CHAR);
				else if (type == Types.DATE) {
					pst.setNull(1, Types.DATE);
				} else if (type == Types.NUMERIC) {
					pst.setNull(1, Types.NUMERIC);
				} else if (type == Types.TIMESTAMP) {
					pst.setNull(1, Types.TIMESTAMP);
				} else if (type == Types.TIME) {
					pst.setNull(1, Types.TIME);
				} else if (type == Types.SMALLINT) {
					pst.setNull(1, Types.SMALLINT);
				} else if (type == Types.INTEGER)
					pst.setNull(1, Types.INTEGER);
				else if (type == Types.FLOAT || type == Types.REAL)
					pst.setNull(1, Types.FLOAT);
			} else {
				if (type == Types.VARCHAR)
					pst.setString(1, valor);
				else if (type == Types.CHAR)
					/*if (nombreCampo.equals("consulta")) {
						pst.setString(1, valor);
					} else if (nombreCampo.equals("colegio")) {
						pst.setString(1, valor);
					} else if (nombreCampo.equals("am_pm_ult_dia_fiebre")) {
						pst.setString(1, valor);
					} else if (nombreCampo.equals("am_pm_ult_dosis_antipiretico")) {
						pst.setString(1, valor);
					} else if (nombreCampo.equals("categoria")) {
						pst.setString(1, valor);
					} else if (nombreCampo.equals("horario_clases")) {
						pst.setString(1, valor);
					}*/
					if (valor.length() > 1) {
						pst.setString(1, valor);
					} else { 
						pst.setString(1, String.valueOf(valor.charAt(0)));
					}
				else if (type == Types.DATE) {
					if (valor.trim().contains(" ")) {
						pst.setDate(1, new java.sql.Date(UtilDate.StringToDate(valor, "dd/MM/yyyy").getTime()));
					} else  {
						pst.setDate(1, new java.sql.Date(UtilDate.StringToDate(valor, "dd/MM/yyyy").getTime()));
					}
				} else if (type == Types.SMALLINT) {
					pst.setInt(1, Integer.parseInt(valor));
				} else if (type == Types.NUMERIC) {
					BigDecimal valorDecimal = new BigDecimal(valor);
					pst.setBigDecimal(1, valorDecimal);
				} else if (type == Types.TIME) {
					SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
					long ms = sdf.parse(valor).getTime();
					pst.setTime(1, new java.sql.Time(ms));
				} else if (type == Types.TIMESTAMP) {
					if (valor.trim().contains(" ")) {
						pst.setTimestamp(1, new Timestamp(UtilDate.StringToDate(valor, "dd/MM/yyyy HH:mm").getTime()));
					} else {
						pst.setDate(1, new java.sql.Date(UtilDate.StringToDate(valor, "dd/MM/yyyy").getTime()));
					}
				} else if (type == Types.INTEGER)
					pst.setInt(1, Integer.valueOf(valor));
				else if (type == Types.FLOAT || type == Types.REAL)
					pst.setFloat(1, Float.valueOf(valor));
			}

			insertBitacora(nombreTabla, numHoja, usuario, nombreCampo, conn, valor, secHoja, dia, id, secSegSeguimiento);

			pst.executeUpdate();
			infoResultado.setOk(true);
			infoResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);

		} catch (Exception e) {
			e.printStackTrace();
			infoResultado.setOk(false);
			infoResultado.setExcepcion(true);
			infoResultado.setGravedad(infoResultado.ERROR);
			infoResultado.setMensaje(Mensajes.REGISTRO_NO_GUARDADO + "\n" + e.getMessage());
			infoResultado.setFuenteError("Actualizar Datos");
			// TODO: handle exception
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				logger.error(" No se pudo cerrar conexión ", ex);
			}
		}
		return infoResultado;
	}

	public void insertControlCambios(String nombreTabla, int numHoja, String usuario, String nombreCampo,
			Connection conn) {

		PreparedStatement pstm = null;
		try {

			StringBuilder builderSelect = new StringBuilder();
			StringBuilder builderInsert = new StringBuilder();

			builderSelect.append(" SELECT ").append(nombreCampo).append(", ").append(" cod_expediente ")
					.append(" FROM ").append(nombreTabla).append(" WHERE ").append(" num_hoja_consulta ").append(" = ")
					.append(numHoja);
			pstm = conn.prepareStatement(builderSelect.toString());

			ResultSet rs = pstm.executeQuery();
			String valorAnterior = null;
			Integer codExpediente = 0;
			while (rs.next()) {
				valorAnterior = rs.getString(1);
				codExpediente = rs.getInt(2);
			}
			if (valorAnterior == null) {
				valorAnterior = "";
			}
			pstm.close();
			builderInsert.append("INSERT INTO ").append("control_cambios").append(" ( ").append("sec_control_cambios, ")
					.append("num_hoja_consulta, ").append("cod_expediente, ").append("fecha, ").append("usuario, ")
					.append("tipo_control, ").append("nombre_campo, ").append("valor_campo, ").append("controlador ")
					.append(") ").append("VALUES ").append("( ")
					.append("nextval('control_cambios_sec_control_cambios_seq'::regclass), ").append("?, ")
					.append("?, ").append("?, ").append("?, ").append("?, ").append("?, ").append("?, ").append("? ")
					.append(")");

			pstm = conn.prepareStatement(builderInsert.toString());

			Calendar calendar = Calendar.getInstance();
			java.util.Date now = calendar.getTime();
			java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

			// pstm.setInt(1, 1);
			pstm.setInt(1, numHoja);
			pstm.setInt(2, codExpediente);
			pstm.setTimestamp(3, currentTimestamp);
			pstm.setString(4, usuario);
			pstm.setString(5, "Modificacion");
			pstm.setString(6, nombreCampo);
			pstm.setString(7, valorAnterior);
			pstm.setString(8, "modificacion_web_cc");

			pstm.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
				}
			} catch (SQLException ex) {
				logger.error(" No se pudo cerrar conexión ", ex);
			}
		}
	}

	/*
	 * Guardando los datos en la tabla bitacora para llevar el control de los
	 * cambios realizados
	 */
	public void insertBitacora(String nombreTabla, int numHoja, String usuario, String nombreCampo, Connection conn,
			String valor, String secHoja, int dia, int id, String secSegSeguimiento) {
		
		PreparedStatement pstmBitacora = null;
		try {
			StringBuilder builderSelect = new StringBuilder();
			StringBuilder builderInsert = new StringBuilder();

			if (nombreTabla.equals("hoja_influenza") || nombreTabla.equals("hoja_zika")) {
				builderSelect.append(" SELECT ").append(nombreCampo).append(" FROM ").append(nombreTabla)
						.append(" WHERE ").append(" num_hoja_seguimiento ").append(" = ").append(numHoja);
				// pstmBitacora = conn.prepareStatement(builderSelect.toString());
			} else {
				builderSelect.append("SELECT ").append(nombreCampo).append(" , ").append(secSegSeguimiento).append(" FROM ").append(nombreTabla)
						.append(" WHERE ").append(secHoja).append(" = ").append(id).append(" AND ")
						.append(" control_dia = ").append(dia);
				// pstmBitacora = conn.prepareStatement(builderSelect.toString());
			}

			pstmBitacora = conn.prepareStatement(builderSelect.toString());
			ResultSet rs = pstmBitacora.executeQuery();
			String valorAnterior = null;
			String valorSeg = null;

			while (rs.next()) {
				valorAnterior = rs.getString(1);
				if (secSegSeguimiento != null) {
					valorSeg = rs.getString(2);	//Cambio aplicado el 27/11/2019 (Oteniendo el secSegSeguimiento) para guardarlo en bitacora
				}
			}

			if (valorAnterior == null) {
				valorAnterior = "NULL";
			}
			pstmBitacora.close();
			Calendar calendar = Calendar.getInstance();
			java.util.Date now = calendar.getTime();
			java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

			builderInsert.append("INSERT INTO ").append("bitacora ").append("( ").append("sec_bitacora, ")
					.append("controlador, ").append("id_entidad, ").append("nombre_entidad, ").append("propiedad, ")
					.append("nuevo_valor, ").append("valor_anterior, ").append("fecha_operacion, ")
					.append("tipo_operacion, ").append("nombre_usuario ").append(") ").append("VALUES ").append("( ")
					.append("nextval('bitacora_sec_bitacora_seq'::regclass), ").append("?, ").append("?, ")
					.append("?, ").append("?, ").append("?, ").append("?, ").append("?, ").append("?, ").append("? ")
					.append(")");

			pstmBitacora = conn.prepareStatement(builderInsert.toString());

			pstmBitacora.setString(1, "modificacion_web_cc");
			if (nombreTabla.trim().equals("seguimiento_influenza") || nombreTabla.trim().equals("seguimiento_zika")) {
				pstmBitacora.setString(2, valorSeg);
			} else {
				pstmBitacora.setInt(2, numHoja);
			}
			pstmBitacora.setString(3, nombreTabla);
			pstmBitacora.setString(4, nombreCampo);
			pstmBitacora.setString(5, valor);
			pstmBitacora.setString(6, valorAnterior);
			pstmBitacora.setTimestamp(7, currentTimestamp);
			pstmBitacora.setString(8, "Modificacion");
			pstmBitacora.setString(9, usuario);

			pstmBitacora.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (pstmBitacora != null) {
					pstmBitacora.close();
				}
			} catch (SQLException ex) {
				logger.error(" No se pudo cerrar conexión ", ex);
			}
		}
	}

	public int getColumnType(String tableName, String columnName, Connection conn) throws Exception {
		int type = 0;
		try {
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet res = meta.getColumns(null, null, tableName, null);
			while (res.next()) {
				if (res.getString("COLUMN_NAME").equalsIgnoreCase(columnName)) {
					// el record_id siempre debe ser el primer campo
					type = res.getInt("DATA_TYPE");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return type;
	}

	/*
	 * Metodo para obtener el valor anterior de las hojas y segumientos
	 */
	public String getValorAnterior(String nombreTabla, int numHoja, String nombreCampo, int controlDia) {
		PreparedStatement pstmValor = null;
		PreparedStatement pstmSecSeguimiento = null;
		String valorAnterior = null;
		int id = 0;
		conn = getConection();
		try {
			StringBuilder builderSelect = new StringBuilder();
			StringBuilder builderSelectSec = new StringBuilder();

			if (nombreTabla.equals("hoja_consulta")) {
				builderSelect.append(" SELECT ").append(nombreCampo).append(" FROM ").append(nombreTabla)
						.append(" WHERE ").append(" num_hoja_consulta ").append(" = ").append(numHoja);
			} else if (nombreTabla.equals("hoja_influenza") || nombreTabla.equals("hoja_zika")) {
				builderSelect.append(" SELECT ").append(nombreCampo).append(" FROM ").append(nombreTabla)
						.append(" WHERE ").append(" num_hoja_seguimiento ").append(" = ").append(numHoja);
			} else {
				if (nombreTabla.equals("seguimiento_influenza")) {
					builderSelectSec.append("SELECT ").append("sec_hoja_influenza ").append(" FROM ")
							.append(" hoja_influenza ").append(" WHERE ").append("num_hoja_seguimiento = ")
							.append(numHoja);

					pstmSecSeguimiento = conn.prepareStatement(builderSelectSec.toString());
					ResultSet rs = pstmSecSeguimiento.executeQuery();
					while (rs.next()) {
						id = rs.getInt(1);
					}

					builderSelect.append("SELECT ").append(nombreCampo).append(" FROM ").append(nombreTabla)
							.append(" WHERE ").append("sec_hoja_influenza =").append(id).append(" AND ")
							.append("control_dia =").append(controlDia);

				} else {
					builderSelectSec.append("SELECT ").append("sec_hoja_zika ").append(" FROM ").append(" hoja_zika ")
							.append(" WHERE ").append("num_hoja_seguimiento = ").append(numHoja);

					pstmSecSeguimiento = conn.prepareStatement(builderSelectSec.toString());
					ResultSet rs = pstmSecSeguimiento.executeQuery();
					while (rs.next()) {
						id = rs.getInt(1);
					}

					builderSelect.append("SELECT ").append(nombreCampo).append(" FROM ").append(nombreTabla)
							.append(" WHERE ").append("sec_hoja_zika =").append(id).append(" AND ")
							.append("control_dia =").append(controlDia);
				}
			}

			pstmValor = conn.prepareStatement(builderSelect.toString());
			ResultSet rs = pstmValor.executeQuery();

			while (rs.next()) {
				valorAnterior = rs.getString(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (pstmValor != null) {
					pstmValor.close();
				}
				if (pstmSecSeguimiento != null) {
					pstmSecSeguimiento.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				logger.error(" No se pudo cerrar conexión ", ex);
			}
		}
		return valorAnterior;
	}
}
