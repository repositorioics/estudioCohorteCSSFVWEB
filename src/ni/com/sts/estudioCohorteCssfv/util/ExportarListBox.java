package ni.com.sts.estudioCohorteCssfv.util;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import ni.com.sts.estudioCohorteCSSFV.modelo.Diagnostico;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;

public class ExportarListBox {

    /**
	 * Exporta el contenido de la grilla hacia un archivo csv
	 * @param listbox
	 * @param metodos
	 * @param lista
	 * @param nombreArchivo
	 * @throws Exception
	 */
	public static void export_to_csv(Listbox listbox, String [] metodos, Object[] lista, String nombreArchivo, String titulo) throws Exception {
        String s = ",";
        StringBuffer sb = new StringBuffer();
        String l = "\n";

        sb.append(titulo + l);
        sb.append(l);
        sb.append("Total registros: " + lista.length + "\n");

        try{
	  		if(listbox.getItemCount() > 0){
	  			//cabecera de la grilla
		        for (Object head : listbox.getHeads()) {
		           String h = "";
		           for (Object header : ((Listhead) head).getChildren()) {
		        	   if(((Listheader) header).isVisible() && !((Listheader) header).getLabel().equalsIgnoreCase("editar"))
		        		   h += ((Listheader) header).getLabel().replace('á', 'a').replace('é', 'e').replace('í', 'i').replace('ó', 'o').replace('ú', 'u').replace("ñ","nn").replace('Á', 'A').replace('É', 'E').replace('Í', 'I').replace('Ó', 'O').replace('Ú', 'U').replace("Ñ","NN") + s;
		           }
		           sb.append(h + "\n");
			    }

		         //Clase y métodos del objeto de la lista
				 Class classObj = lista[0].getClass();
				 Method methodClass = null;
				 String columna = "";
				 String dato = null;

				 //detalle de la grilla
		         for (Object obj : lista) {

		           String fila = "";
		           for (String metodo : metodos) {
		        	   String [] nMetodos = metodo.split(",");

		        	   for(String m : nMetodos){
		        		   methodClass = classObj.getMethod(m, null);
		        		   Object o = methodClass.invoke(obj, null);

		        		   //if(o instanceof Diagnostico)
		        			   //dato = ((Diagnostico)o).getDiagnostico();
		        		   //else
			        		   if(o instanceof Calendar)
			        			   dato = UtilDate.DateToString(((Calendar)o).getTime(),"dd/MM/yyyy");
			        		   else
				        		   if(o instanceof Date)
				        			   dato = UtilDate.DateToString((Date) o,"dd/MM/yyyy");
				        		   else
				        			   if(o != null)
				        				   dato = (String)o.toString();
				        			   else
				        				   dato = "";

		        		   if(m.equals("getEstado") && !dato.isEmpty()){
		        			   dato = dato.equalsIgnoreCase("0")?"Inactivo":"Activo"; 
		        		   }
		        		   if(m.equals("getTexto8") && !dato.isEmpty()){
		        			   dato = dato.equalsIgnoreCase("0")?"Activo":"Retirado"; 
		        		   }

		        		   columna = columna.isEmpty()? dato : columna + " " + dato;
		        	   }

		        	   //arma la fila con cada una de las columnas
		        	   fila += columna.replace(',',' ').replace('á', 'a').replace('é', 'e').replace('í', 'i').replace('ó', 'o').replace('ú', 'u').replace("ñ","nn").replace('Á', 'A').replace('É', 'E').replace('Í', 'I').replace('Ó', 'O').replace('Ú', 'U').replace("Ñ","NN") + s;

		        	   //reinicia cada columna
		        	   columna = "";
		           }

		           sb.append(fila + "\n");
		         }

		         Filedownload.save(sb.toString().getBytes(), "text/plain", nombreArchivo + ".csv");

			 }else{
			     Messagebox.show("No se encontraron datos para exportar", "Exportar Documentos", Messagebox.OK, Messagebox.ERROR);
			}
		// mostrando los errores
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw ex;
		}
	}

	public static Object copyArrayListObject(Object[] aOrig, @SuppressWarnings("rawtypes") Class type) {
		Object copy = Array.newInstance(type, aOrig.length);
		System.arraycopy(aOrig, 0, copy, 0, aOrig.length);
		return copy;
	}	
}
