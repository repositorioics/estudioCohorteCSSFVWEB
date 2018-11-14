package ni.com.sts.estudioCohorteCssfv.servicios;

import java.util.List;

import ni.com.sts.estudioCohorteCSSFV.modelo.UsuariosView;

public interface UsuariosService {

	public UsuariosView obtenerUsuarioById(Integer id);

	public List<UsuariosView> obtenerUsuariosByPerfil(String perfil);

	List<UsuariosView> obtenerUsuariosByName(String nombre);

	List<UsuariosView> obtenerUsuarios();

	List<UsuariosView> obtenerUsuariosByPerfiles(String perfiles);
}
