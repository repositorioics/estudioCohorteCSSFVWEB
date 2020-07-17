package ni.com.sts.estudioCohorteCssfv.controller.actualizarDatos;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Label;

import ni.com.sts.estudioCohorteCSSFV.modelo.UsuariosView;
import ni.com.sts.estudioCohorteCssfv.datos.actualizarDatos.ActualizarDatosDA;
import ni.com.sts.estudioCohorteCssfv.datos.usuario.UsuariosDA;
import ni.com.sts.estudioCohorteCssfv.servicios.ActualizarDatosService;
import ni.com.sts.estudioCohorteCssfv.servicios.UsuariosService;
import ni.com.sts.estudioCohorteCssfv.util.InfoResultado;
import ni.com.sts.estudioCohorteCssfv.util.Mensajes;
import ni.com.sts.estudioCohorteCssfv.util.Utilidades;
import ni.com.sts.estudioCohorteCssfv.util.ConnectionDAO;

public class ActualizarDatosController extends SelectorComposer<Component> {

	private ConnectionDAO connectionDAOClass = new ConnectionDAO();
	private static UsuariosService usuariosService = new UsuariosDA();
	private static ActualizarDatosService actualizarDatosService = new ActualizarDatosDA();
	private static String[] dataDb = new String[0];
	private static List<String> listOfTables = new ArrayList<String>();
	private Connection conn = null;
	PreparedStatement pst = null;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// init();
	}

	@Wire("[id$=txtNumHoja]")
	private Intbox txtNumHoja;

	@Wire("[id$=txtValor]")
	private Textbox txtValor;

	@Wire("[id$=cmbTablasDb]")
	private Combobox cmbTablasDb;

	@Wire("[id$=cmbCampoModificar]")
	private Combobox cmbCampoModificar;

	@Wire("[id$=txtControlDia]")
	private Intbox txtControlDia;

	@Wire("[id$=lblControlDia]")
	private Label lblControlDia;

	@Wire("[id$=lblControlDiaRequired]")
	private Label lblControlDiaRequired;

	@Wire("[id$=lblValorAnterior]")
	private Label lblValorAnterior;

	@Wire("[id$=txtValorAnterior]")
	private Textbox txtValorAnterior;

	@Wire("[id$=chkValorNull]")
	private Checkbox chkValorNull;

	@Wire("[id$=btnGuardar]")
	private Button btnGuardar;

	@Wire("[id$=btnCancelar]")
	private Button btnCancelar;

	/**
	 * Menejador del evento Click del ComboBox "cmbTablasDb"
	 * 
	 */
	@Listen("onChange=[id$=cmbTablasDb]")
	public void cmbTablasDb_onChange() throws Exception {
		String selectedNameTableDb = "";
		this.txtValor.setValue("");
		this.txtControlDia.setValue(null);
		this.lblValorAnterior.setVisible(false);
		this.txtValorAnterior.setValue("");
		this.txtValorAnterior.setVisible(false);
		this.cmbCampoModificar.setSelectedItem(null);
		this.cmbCampoModificar.setModel(null);
		this.txtControlDia.setReadonly(true);
		this.chkValorNull.setDisabled(false);
		this.chkValorNull.setChecked(false);
		if (cmbTablasDb.getSelectedItem().getValue() != null) {
			selectedNameTableDb = cmbTablasDb.getSelectedItem().getValue().toString();
			// activarControlDia(selectedNameTableDb);
			if (selectedNameTableDb.equals("0")) {
				this.cmbCampoModificar.setSelectedItem(null);
				this.cmbCampoModificar.setModel(null);

				this.txtValor.setValue("");
			} else {
				if (selectedNameTableDb.equals("seguimiento_influenza")
						|| selectedNameTableDb.equals("seguimiento_zika")) {
					this.txtControlDia.setReadonly(false);
					this.chkValorNull.setDisabled(true);
					// this.chkValorNull.setChecked(false);
				} else {
					// this.chkValorNull.setChecked(false);
					this.chkValorNull.setDisabled(false);
				}
				ListModel dictModel = new SimpleListModel(getTableMetaData(selectedNameTableDb));
				this.cmbCampoModificar.setModel(dictModel);
			}
		}
	}

	/**
	 * Menejador del evento Click del ComboBox "cmbCampoModificar"
	 * 
	 */
	@Listen("onChange=[id$=cmbCampoModificar]")
	public void cmbCampoModificar_onChange() throws Exception {
		if (this.txtNumHoja.getValue() != null && this.cmbCampoModificar.getSelectedItem() != null) {
			this.txtValor.setText("");
			this.obtenerValorAnterior();
		}
	}

	/**
	 * Menejador del evento Click del botón "Guardar"
	 * 
	 * @throws Exception
	 */
	@Listen("onClick=[id$=btnGuardar]")
	public void btnGuardar_onClick() throws Exception {
		validarDatosRequerido();
	}

	/**
	 * Menejador del evento Click del botón "Cancelar"
	 * 
	 */
	@Listen("onClick=[id$=btnCancelar]")
	public void btnCancelar_onClick() {
		limpiarCampos();
	}

	/**
	 * Menejador del evento Check
	 */
	@Listen("onCheck=[id$=chkValorNull]")
	public void chkValorNull_onCheck() {
		if (this.chkValorNull.isChecked()) {
			this.txtValor.setReadonly(true);
			this.txtValor.setValue("");
		} else {
			this.txtValor.setReadonly(false);
		}
	}

	/**
	 * Menejador del evento OK(Enter) del campo "Numero de Hoja"
	 */
	@Listen("onOK=[id$=txtNumHoja]")
	public void txtNumHoja_onOk() {
		if (this.txtNumHoja.getValue() != null && this.cmbCampoModificar.getSelectedItem() != null
				&& this.cmbTablasDb.getSelectedItem() != null) {
			this.txtValor.setText("");
			this.obtenerValorAnterior();
		}
	}

	/**
	 * Menejador del evento OK(Enter) del campo "Control dia"
	 */
	@Listen("onOK=[id$=txtControlDia]")
	public void txtControlDia_onOk() {
		if (this.txtNumHoja.getValue() != null && this.cmbCampoModificar.getSelectedItem() != null
				&& this.cmbTablasDb.getSelectedItem() != null) {
			this.txtValor.setText("");
			this.obtenerValorAnterior();
		}
	}

	// Metodo para obtener el valor anterior
	public void obtenerValorAnterior() {
		String valorAnterior = "";
		String nombreTabla = "";
		String nombreCampo = "";
		nombreTabla = cmbTablasDb.getSelectedItem().getValue().toString();
		nombreCampo = cmbCampoModificar.getSelectedItem().getValue().toString();
		Integer numHoja = this.txtNumHoja.getValue();
		int controlDia = this.txtControlDia.getValue() != null ? this.txtControlDia.getValue() : 0;

		valorAnterior = actualizarDatosService.getValorAnterior(nombreTabla, numHoja, nombreCampo, controlDia);
		this.lblValorAnterior.setVisible(true);
		this.txtValorAnterior.setVisible(true);
		this.txtValorAnterior.setValue(valorAnterior);
	}

	// Obteniendo las columnas a travez de nombre de la tabla
	public List<String> getTableMetaData(String tableName) throws Exception {
		List<String> columnTableName = new ArrayList<String>();
		conn = connectionDAOClass.getConection();
		try {
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet rs = metaData.getColumns(null, null, tableName.toLowerCase(), null);
			while (rs.next()) {
				if (!rs.getString("COLUMN_NAME").equalsIgnoreCase("cod_expediente")
						&& !rs.getString("COLUMN_NAME").equalsIgnoreCase("sec_hoja_consulta")
						&& !rs.getString("COLUMN_NAME").equalsIgnoreCase("num_hoja_consulta")
						&& !rs.getString("COLUMN_NAME").equalsIgnoreCase("num_orden_llegada")
						//&& !rs.getString("COLUMN_NAME").equalsIgnoreCase("lugar_atencion")
						&& !rs.getString("COLUMN_NAME").equalsIgnoreCase("estudios_participantes")
						&& !rs.getString("COLUMN_NAME").equalsIgnoreCase("estado_carga")
						&& !rs.getString("COLUMN_NAME").equalsIgnoreCase("sec_hoja_influenza")
						&& !rs.getString("COLUMN_NAME").equalsIgnoreCase("num_hoja_seguimiento")
						&& !rs.getString("COLUMN_NAME").equalsIgnoreCase("repeat_key")
						&& !rs.getString("COLUMN_NAME").equalsIgnoreCase("sec_hoja_zika")
						&& !rs.getString("COLUMN_NAME").equalsIgnoreCase("sec_seg_influenza")
						&& !rs.getString("COLUMN_NAME").equalsIgnoreCase("sec_seg_zika")
						&& !rs.getString("COLUMN_NAME").equalsIgnoreCase("supervisor")) {

					columnTableName.add(rs.getString("COLUMN_NAME"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return columnTableName;
	}

	/**
	 * Método que reinicia los valores de los campos en el formulario
	 */
	private void limpiarCampos() {
		this.txtNumHoja.setValue(null);
		if (this.cmbTablasDb.getSelectedItem() != null) {
			this.cmbTablasDb.setSelectedIndex(0);
		}
		if (this.cmbCampoModificar.getSelectedItem() != null) {
			ListModel dictModel = new SimpleListModel(dataDb);
			this.cmbCampoModificar.setModel(dictModel);
			this.cmbCampoModificar.setValue(null);
		}
		this.chkValorNull.setChecked(false);
		this.txtValor.setValue("");
		this.txtValor.setReadonly(false);
		this.txtControlDia.setValue(null);
		this.lblValorAnterior.setVisible(false);
		this.txtValorAnterior.setValue("");
		this.txtValorAnterior.setVisible(false);
		this.txtControlDia.setReadonly(true);
		this.chkValorNull.setDisabled(false);
	}

	/**
	 * Método que ejecuta la acción de actualizacion la hoja de consulta
	 * 
	 * @throws Exception
	 */
	private void actualizarHojaConsulta(String nombreTabla, Integer numHoja, String nombreCampo, String valor,
			Boolean valorNull, String usuario) throws Exception {
		InfoResultado resultado = actualizarDatosService.updateHojaConsulta(nombreTabla, numHoja, nombreCampo, valor,
				valorNull, usuario);
		if (resultado.isOk() && resultado.getObjeto() != null) {
			Mensajes.enviarMensaje(resultado);
		} else {
			this.txtValor.setText("");
			this.chkValorNull.setChecked(false);
			this.txtValor.setReadonly(false);
			this.obtenerValorAnterior();
			Mensajes.enviarMensaje(resultado);
		}
	}

	/**
	 * Método que ejecuta la acción de actualizacion de los seguimientos
	 * 
	 * @throws Exception
	 */
	private void actualizarSeguimientos(String nombreTabla, String secHoja, Integer numHoja, String nombreCampo,
			String valor, Boolean valorNull, String usuario, Integer dia, String secSegSeguimiento) throws Exception {
		InfoResultado resultado = actualizarDatosService.updateSeguimientos(nombreTabla, secHoja, numHoja, nombreCampo,
				valor, valorNull, usuario, dia, secSegSeguimiento);
		if (resultado.isOk() && resultado.getObjeto() != null) {
			Mensajes.enviarMensaje(resultado);

		} else {
			this.txtValor.setText("");
			this.chkValorNull.setChecked(false);
			this.txtValor.setReadonly(false);
			this.obtenerValorAnterior();
			Mensajes.enviarMensaje(resultado);
		}
	}

	private void validarDatosRequerido() throws Exception {

		if (this.txtNumHoja.getValue() == null) {
			Messagebox.show("Ingrese en número de hoja", "Validación", Messagebox.OK, Messagebox.INFORMATION);
			this.txtNumHoja.setFocus(true);
			return;
		}
		if (this.cmbTablasDb.getSelectedItem() == null) {
			Messagebox.show("Debe de seleccionar el nombre de la tabla", "Validación", Messagebox.OK,
					Messagebox.INFORMATION);
			this.cmbTablasDb.setFocus(true);
			return;
		}
		if (this.cmbTablasDb.getSelectedItem().getValue().equals("0")) {
			Messagebox.show("El valor seleccionado no es valido", "Validación", Messagebox.OK, Messagebox.INFORMATION);
			this.cmbTablasDb.setFocus(true);
			return;
		}
		if (this.txtControlDia.getValue() == null
				&& (cmbTablasDb.getSelectedItem().getValue().toString().equals("seguimiento_influenza")
						|| cmbTablasDb.getSelectedItem().getValue().toString().equals("seguimiento_zika"))) {
			Messagebox.show("Debe ingresar el día", "Validación", Messagebox.OK, Messagebox.INFORMATION);
			this.txtControlDia.setFocus(true);
			return;
		}
		if (this.cmbCampoModificar.getSelectedItem() == null) {
			Messagebox.show("Debe de seleccionar el nombre del campo que se va a modificar", "Validación",
					Messagebox.OK, Messagebox.INFORMATION);
			this.cmbCampoModificar.setFocus(true);
			return;
		}
		if ((this.txtValor.getValue() == null || this.txtValor.getValue() == "") && !this.chkValorNull.isChecked()) {
			Messagebox.show("Debe ingresar el nuevo valor", "Validación", Messagebox.OK, Messagebox.INFORMATION);
			this.txtValor.setFocus(true);
			return;
		}

		String nombreTabla = cmbTablasDb.getSelectedItem().getValue().toString();
		String nombreCampo = cmbCampoModificar.getSelectedItem().getValue().toString();
		Integer numHoja = this.txtNumHoja.getValue();
		String valor = this.txtValor.getValue();
		Boolean valorNull = this.chkValorNull.isChecked();
		UsuariosView user = usuariosService.obtenerUsuarioById(Utilidades.obtenerInfoSesion().getUsuarioId());
		String usuario = user.getUsuario();
		if (nombreTabla.equals("seguimiento_influenza")) {
			String secHoja = "sec_hoja_influenza";
			String secSegSeguimiento = "sec_seg_influenza";
			Integer dia = this.txtControlDia.getValue();
			actualizarSeguimientos(nombreTabla, secHoja, numHoja, nombreCampo, valor, valorNull, usuario, dia, secSegSeguimiento);
		} else if (nombreTabla.equals("seguimiento_zika")) {
			String secHoja = "sec_hoja_zika";
			Integer dia = this.txtControlDia.getValue();
			String secSegSeguimiento = "sec_seg_zika";
			actualizarSeguimientos(nombreTabla, secHoja, numHoja, nombreCampo, valor, valorNull, usuario, dia, secSegSeguimiento);
		} else {
			actualizarHojaConsulta(nombreTabla, numHoja, nombreCampo, valor, valorNull, usuario);
		}
	}
}
