package py.edu.ucsa.jdbc01.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * Este es el primer ejemplo de conectividad a una base de datos. El alumno debe
 * completar las indicaciones marcadas con "TODO", solucionando los
 * inconvenientes que vayan surgiendo.
 * 
 * @author Pablo
 * 
 */
public class Conexion {

	public static void main(String[] args) {

		// TODO: Cargar el Driver, por ejemplo:
//		String driverName = "org.postgresql.Driver";
//		Class.forName(driverName);

		String url = "jdbc:postgresql://localhost:5432/conta"; //jdbc:mysql://localhost:3306/mydb
		String uname = "postgres";
		String passwd = "programacion1*";
		// TODO: Conectarse a la base de datos.
		Connection con;
		try {
			con = DriverManager.getConnection(url, uname, passwd);

			System.out.println("Estamos conectados al: " + con.getMetaData().getDatabaseProductName() + " "
					+ con.getMetaData().getDatabaseProductVersion());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO: Cerrar la conexion si todo anda bien.

	}

}
