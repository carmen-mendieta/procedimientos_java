package py.edu.ucsa.jdbc01.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionBD {
	private static String defaultBD = "postgres";
	private static Logger logger = Logger.getLogger(ConexionBD.class.getName());

	public static String getDefaultBD() {
		return defaultBD;
	}

	public static void setDefaultBD(String defaultBD) {
		ConexionBD.defaultBD = defaultBD;
	}

	public static Connection getConexion() {
		return obtenerConexion(defaultBD);
	}

	public static Connection getConexionPG() {
		return obtenerConexion("postgres");
	}

	public static Connection getConexionORCL() {
		return obtenerConexion("oracle");
	}

	public static Connection getConexionMySQL() {
		return obtenerConexion("mysql");
	}

	public static Connection obtenerConexion(String fileName) {
		Connection con = null;
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(fileName);
			con = DriverManager.getConnection(bundle.getString("jdbc.url"), bundle.getString("jdbc.user"),
					bundle.getString("jdbc.pass"));

			System.out.println("Estamos conectados al: " + con.getMetaData().getDatabaseProductName() + " "
					+ con.getMetaData().getDatabaseProductVersion());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}

	public static boolean cerrarConexion(Connection c) {
		try {
			if (Objects.nonNull(c) && !c.isClosed()) {
				try {
					c.close();
					return true;
				} catch (SQLException e) {
					logger.log(Level.SEVERE, "NO SE PUDO CERRAR LA CONEXION");
					return false;
				}
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "LA CONEXION ES NULA O ESTA CERRADA");

		}
		return false;
	}

	
	public static void main(String[] args) {
		Connection conexionNueva = ConexionBD.getConexion();
		try {
			conexionNueva.close();
		} catch (SQLException e) {
			System.out.println("OCURRIO UN ERROR AL INTENTAR CERRAR LA CONEXION");
		}
	}
}
