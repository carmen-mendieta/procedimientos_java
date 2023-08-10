package py.edu.ucsa.dao;


public class DAOFactory {
	public static FuncionarioDAO getFuncionarioDAO() {
		return new FuncionarioDAOJdbcImpl();
	}

}
