package py.edu.ucsa.jdbc01.transacciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

import py.edu.ucsa.jdbc01.conexion.ConexionBD;
import py.edu.ucsa.jdbc01.statements.TableAlumnos;
import py.edu.ucsa.util.Consola;

public class Transacciones {

	static Statement s;

	public static void savepoints(Connection conn) throws SQLException {

		PreparedStatement ps = conn.prepareStatement("INSERT INTO alumnos " + "(nombre, cedula) VALUES ( ?,  ? )");

		// Set a conservative transaction isolation level.
		// conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		System.out.println("INGERSAR DATOS");
		String nombre = Consola.leerDatos("nombre");
		String cedula = Consola.leerDatos("cedula");

		ps.setString(1, nombre);
		ps.setInt(2, Integer.parseInt(cedula));
		ps.executeUpdate();

		// Set a named savepoint.
		Savepoint svpt = conn.setSavepoint("SavePoint1");
		// ...
		try {
			ps.setString(1, " Paraguay ");
			ps.setInt(2, Integer.parseInt(cedula));
			ps.executeUpdate();
			// ...
		} catch (SQLException e) {
			System.out.println(e);
			conn.rollback(svpt);
		}
		// ...
		// The author has been added, but not updated.
		conn.commit();
	}

	public static void transacciones(Connection c) throws SQLException {
		int insertados = 0;

		String insertStmt = "insert into alumnos " + "( nombre, direccion, cedula, celular,  email )" + " values "
				+ " ( ?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = c.prepareStatement(insertStmt);

			ps.setString(1, "kaka");
			ps.setString(2, "Kioto y Yamamoto");
			ps.setInt(3, 864940);
			ps.setString(4, "981987654");
			ps.setString(5, "kakaroto at hotmail dot com");
			insertados += ps.executeUpdate();

			System.out.println("Cantidad de insertados = " + insertados);

			ps.setString(1, "vulma");
			ps.setString(2, "Gohan 578");
			ps.setInt(3, 864941);
			ps.setString(4, "992456321");
			ps.setString(5, "vulma at hotmail dot com");
			insertados += ps.executeUpdate();
			System.out.println("Cantidad de insertados = " + insertados);

			ps.setString(1, "rediver");
			ps.setString(2, "Vulcan");
			ps.setInt(3, 864942); // ERROR, Cedula repetida!!!!!!!!!!!!!!!!!
			ps.setString(4, "0972555888");
			ps.setString(5, "rediver at gmail dot com");
			insertados += ps.executeUpdate();
			System.out.println("Cantidad de insertados = " + insertados);
			ps.close();
			// comiteamos la transacci�n
			 c.commit();
			// hacemos un rollback de la transaccion
			// c.rollback();
			// Volvemos a poner en modo autocommit la conexi�n
			// esto puede comitear los cambios anteriores para algunos drivers.
			c.setAutoCommit(true);

			System.out.println("Insertamos " + insertados);
		} catch (SQLException sqle) {
			System.out.println(sqle.getLocalizedMessage());
			// hacemos un rollback de la transaccion
			System.out.println("voy a rollbackear");
			c.rollback();
		}
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Connection c = ConexionBD.getConexion();
		System.out.println("Soporta Transacciones: " + c.getMetaData().supportsTransactions());
		c.setAutoCommit(false);

	//	transacciones(c);

		 savepoints(c);

		TableAlumnos.queryAlumnos(c);
		c.close();
	}
}
