package py.edu.ucsa.dao;

import java.util.List;

import py.edu.ucsa.dto.FuncionarioDTO;

public interface FuncionarioDAO {

	public void crearTabla();
	public List<FuncionarioDTO> listarFuncionarios();
	public void insertar(FuncionarioDTO f);
	public void modificar(FuncionarioDTO f);
	public List<FuncionarioDTO> buscar(int edadMin, int edadMax, String nombre, String apellido);
}
