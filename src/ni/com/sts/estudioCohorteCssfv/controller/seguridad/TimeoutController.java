package ni.com.sts.estudioCohorteCssfv.controller.seguridad;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Window;

public class TimeoutController extends SelectorComposer<Component> {

	private static final long serialVersionUID = -5129886345640753381L;

	// --- --- --- --- --- ---
	// --- DATA GENERAL OBJECTS
	// --- --- --- --- --- ---

	private   Window winTimeout;
	protected Window getWindow() { return winTimeout; }

	// --- --- --- --- --- ---
	// --- CONSTRUCTORES
	// --- --- --- --- --- ---

	public TimeoutController() {
		super();
	}

	// --- --- --- --- --- ---
	// --- EVENTOS
	// --- --- --- --- --- ---

	@Listen("onClick=[id$=btnGotoLogin]")
    public void btnGotoLogin(){
		System.out.println("onClick$btnGotoLogin():start");

		//---
		// invalidamos la session
		//Sessions.getCurrent().invalidate();//lo hara el mismo login
		// redirigimos a login.zul
		Executions.sendRedirect("/login.zul");
		//---
		System.out.println("onClick$btnGotoLogin():ended");
	}
}
