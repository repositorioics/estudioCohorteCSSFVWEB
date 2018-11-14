package ni.com.sts.estudioCohorteCssfv.util;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;

import ni.com.sts.seguridadWS.webservices.InfoSesionDTO;

@SuppressWarnings({"all"})
public class Utilidades {

    // ----------------------------------------- Constructor

    public Utilidades() { // clase no instanciable

    }

    public static String getCodigoSistema() throws Exception{
    	return UtilityProperty.getValueProperty("CODIGO_SISTEMA");
    }
    
    public static InfoSesionDTO obtenerInfoSesion() {

        InfoSesionDTO infoSesionDTO = null;
        Session session = Sessions.getCurrent();
        if (session != null) {
            infoSesionDTO = (InfoSesionDTO) session.getAttribute("usuarioActual");
        }

        return infoSesionDTO;

    }

    public static void eliminarSesion() {
        Session session = Sessions.getCurrent();
        if (session != null) {
            session.removeAttribute("usuarioActual");
        }
    }

    /**
    * Obtiene item en combo de acuerdo al valor especificado
    * @param combobox Objeto combobox
    * @param valor, Valor del item a buscar
    * @return Objeto Comboitem
    */
    public synchronized static Comboitem obtenerItemEnCombo(Combobox combobox, String valor) throws Exception {
        try {

            if(combobox != null && combobox.getItemCount()>0){

                List<Comboitem> items = combobox.getItems();

                Comboitem comboitem = items.get(0);

                for (int i = 0; i < items.size(); i++) {

                    comboitem = items.get(i);

                    String cval = (String)comboitem.getValue().toString();

                    if ((cval != null  && cval.equalsIgnoreCase(valor)))
                    break;

                }
                return comboitem;
                }else{
                return null;
                }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
    * Selecciona item en combo de acuerdo al valor especificado
    * @param combobox Objeto combobox
    * @param valor, Valor del item a seleccionar
    */
    public synchronized static void seleccionarItemEnCombo(Combobox combobox, String valor) throws Exception {
        try{

            if(combobox != null && combobox.getItemCount() > 0) {

            for (Comboitem comboitem : combobox.getItems()) {

                String cval = (String)comboitem.getValue().toString();

                if ((cval != null  && cval.equalsIgnoreCase(valor))) {
                    combobox.setSelectedItem(comboitem);
                    break;
                }
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
    * Selecciona item en combo de acuerdo al valor especificado
    * @param combobox Objeto combobox
    * @param objValor, Valor del item a seleccionar
    */
    public synchronized static void seleccionarItemEnCombo(Combobox combobox, Object objValor) throws Exception {
        try{
            Class classObj = objValor.getClass();


            Method methodClass = classObj.getMethod("getIdentificador", null);
            Integer objValorResult = (Integer) methodClass.invoke(objValor, null);

            if(combobox != null && combobox.getItemCount() > 0) {

                Integer comboItemResult = 0;

            for (Comboitem comboitem : combobox.getItems()) {

                comboItemResult = (Integer) methodClass.invoke(comboitem.getValue(), null);

                if (objValorResult.equals(comboItemResult)) {
                    combobox.setSelectedItem(comboitem);
                    break;
                }
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
    * Selecciona item en combo de acuerdo al valor especificado
    * @param combobox Objeto combobox
    * @param valor, Valor del item a seleccionar
    * @param metodo, Nombre del método para obtener el valor del objeto
    */
    public synchronized static void seleccionarItemEnCombo(Combobox combobox, Object objValor, String metodo) throws Exception {
        try{

            Class classObj = objValor.getClass();

            Method methodClass = classObj.getMethod(metodo, null);
            Integer objValorResult = (Integer) methodClass.invoke(objValor, null);

            if(combobox != null && combobox.getItemCount() > 0) {

            Integer comboItemResult = 0;

            for (Comboitem comboitem : combobox.getItems()) {

                comboItemResult = (Integer) methodClass.invoke(comboitem.getValue(), null);

                if (objValorResult.equals(comboItemResult)) {
                    combobox.setSelectedItem(comboitem);
                    break;
                }
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
    * Selecciona item en combo de acuerdo al valor especificado
    * @param combobox Objeto combobox
    * @param identificador, Identificador del objeto a seleccionar
    */
    public synchronized static void seleccionarItemEnCombo(Combobox combobox, Integer identificador) throws Exception {
        try{

            if(combobox != null && combobox.getItemCount() > 0) {

            Class classObj = combobox.getItems().get(0).getValue().getClass();

            Method methodClass = classObj.getMethod("getIdentificador", null);

            Integer comboItemResult = 0;

            for (Comboitem comboitem : combobox.getItems()) {

                comboItemResult = (Integer) methodClass.invoke(comboitem.getValue(), null);

                if (identificador.equals(comboItemResult)) {
                    combobox.setSelectedItem(comboitem);
                    break;
                }
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
    * Selecciona item en combo de acuerdo al valor especificado
    * @param combobox Objeto combobox
    * @param identificador, Identificador del objeto a seleccionar
    * @param metodo, Nombre del método para obtener el valor del objeto
    */
    public synchronized static void seleccionarItemEnCombo(Combobox combobox, Integer identificador, String metodo) throws Exception {
        try{
            if(combobox != null && combobox.getItemCount() > 0) {

            Class classObj = combobox.getItems().get(0).getValue().getClass();

            Method methodClass = classObj.getMethod(metodo, null);

            Integer comboItemResult = 0;

            for (Comboitem comboitem : combobox.getItems()) {

                comboItemResult = (Integer) methodClass.invoke(comboitem.getValue(), null);

                if (identificador.equals(comboItemResult)) {
                    combobox.setSelectedItem(comboitem);
                    break;
                }
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
    * Selecciona item radio group de acuerdo al valor especificado
    * @param radioGroup Objeto radioGroup
    * @param valor, Valor del item a seleccionar
    */
    public synchronized static void seleccionarItemEnRadioGroup(Radiogroup radioGroup, String valor) throws Exception {
        try{

            if(radioGroup != null && radioGroup.getItemCount() > 0) {

            for (Radio radioItem : radioGroup.getItems()) {

                String cval = (String)radioItem.getValue().toString();

                if ((cval != null  && cval.equalsIgnoreCase(valor))) {
                    radioGroup.setSelectedItem(radioItem);
                    break;
                }
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
    * Permite eliminar selección de fila en grid.
    * @param grid Objeto grid
    * @param idxRowSelec Indice de fila a eliminar selección
    * @throws Exception
    */
    public synchronized static void deseleccionarFilaEnGrid(Grid grid, Integer idxRowSelec) throws Exception {
        try {
                List<Component> components = grid.getRows().getChildren();

                for(Object obj:components){

                Row rw = (Row) obj;

                if(rw.getIndex() == idxRowSelec.intValue()) {
                    rw.setStyle(null);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
