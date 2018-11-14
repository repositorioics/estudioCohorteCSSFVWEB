package ni.com.sts.estudioCohorteCssfv.controller.seguridad;

import ni.com.sts.estudioCohorteCssfv.datos.seguridad.SeguridadDA;
import ni.com.sts.estudioCohorteCssfv.servicios.SeguridadService;
import ni.com.sts.estudioCohorteCssfv.util.Utilidades;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

import ni.com.sts.seguridadWS.webservices.NodoItemDTO;


public class SidebarController  extends SelectorComposer<Component>{

    private static final long serialVersionUID = 1L;
    private static SeguridadService seguridadService = new SeguridadDA();

    @Wire("[id$=menuNavegacion]")
    Menubar menuNavegacion;

    // -------------------------------------------- Constructor
    public SidebarController(){

    }

    //Inicializando el menú de navegación.
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        init();
    }

    // -------------------------------------------- Métodos
    private void init() throws Exception{
        if(Utilidades.obtenerInfoSesion()!=null){
            obtenerMenuNavegacion();
        }
    }

    private void obtenerMenuNavegacion() throws Exception {

        NodoItemDTO[] arbolMenu = seguridadService.obtenerArbolMenuAutorizado(Utilidades.obtenerInfoSesion().getUsuarioId(),
                Utilidades.getCodigoSistema());
        if(arbolMenu==null){
            return;
        }

        for (NodoItemDTO oMenuRaiz:arbolMenu){

            if(oMenuRaiz.getNodoMenuItems()==null || oMenuRaiz.getNodoMenuItems().length < 1){
                Menuitem oNodoPrincipal = new Menuitem();
                oNodoPrincipal.setLabel(oMenuRaiz.getEtiqueta());
                oNodoPrincipal.setDisabled(oMenuRaiz.isAutorizado() == true ? false:true);
                //Se vincula un evento al menú item de último nivel, que permite redirigir
                //al usuario a la url vinculada a éste.
                if (oMenuRaiz.isAutorizado()) {
                    final String url = oMenuRaiz.getUrl();
                    EventListener<Event> actionListener = new SerializableEventListener<Event>() {
                        private static final long serialVersionUID = 1L;
                        public void onEvent(Event event) throws Exception {
                            Include include = (Include)Selectors.iterable(menuNavegacion.getPage(),
                                     "[id$=mainInclude]").iterator().next();
                            include.setSrc(url);
                        }
                    };
                    oNodoPrincipal.addEventListener(Events.ON_CLICK, actionListener);
                }
                this.menuNavegacion.appendChild(oNodoPrincipal);
            }else{
                //busca mas hijos
                Menu oMenu = new Menu();
                Menupopup oMenupopup = new Menupopup();
                oMenu.setLabel(oMenuRaiz.getEtiqueta());
                poblarMenuArbol(oMenuRaiz.getNodoMenuItems(),oMenupopup);
                oMenu.appendChild(oMenupopup);
                this.menuNavegacion.appendChild(oMenu);
            }
        }
    }

    private void poblarMenuArbol(NodoItemDTO[] pChildMenus,Menupopup pNodoSubmenu){
        for(NodoItemDTO oMenuItem:pChildMenus){
            if(oMenuItem.getNodoMenuItems()==null || oMenuItem.getNodoMenuItems().length < 1){
                Menuitem oNodoHijo = new Menuitem();
                oNodoHijo.setLabel(oMenuItem.getEtiqueta());
                oNodoHijo.setDisabled(oMenuItem.isAutorizado() == true ? false:true);
                //Se vincula un evento al menú item de último nivel, que permite redirigir
                //al usuario a la url vinculada a éste.
                if (oMenuItem.isAutorizado()) {
                    final String url = oMenuItem.getUrl();
                    EventListener<Event> actionListener = new SerializableEventListener<Event>() {
                        private static final long serialVersionUID = 1L;
                        public void onEvent(Event event) throws Exception {
                             Include include = (Include)Selectors.iterable(menuNavegacion.getPage(),
                                     "[id$=mainInclude]").iterator().next();
                             include.setSrc(url);
                        }
                    };
                    oNodoHijo.addEventListener(Events.ON_CLICK, actionListener);
                }
                pNodoSubmenu.appendChild(oNodoHijo);
            }else{
                //busca mas hijos
                Menu oMenu = new Menu();
                Menupopup oMenupopup = new Menupopup();
                oMenu.setLabel(oMenuItem.getEtiqueta());
                poblarMenuArbol(oMenuItem.getNodoMenuItems(),oMenupopup);
                oMenu.appendChild(oMenupopup);
                pNodoSubmenu.appendChild(oMenu);
            }

        }

    }

}
