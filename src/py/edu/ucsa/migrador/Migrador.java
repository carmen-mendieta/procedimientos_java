package py.edu.ucsa.migrador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import py.edu.ucsa.jdbc01.conexion.ConexionBD;

public class Migrador {

	public static void migrarPostgresAOracle() {
		try {
			Connection pg = ConexionBD.getConexionPG();
			Connection orcl = ConexionBD.getConexionORCL();
			PreparedStatement ps = orcl.prepareStatement(
					"INSERT INTO funcionarios_bkp (ID, LEGAJO, NOMBRE, APELLIDO, CEDULA, CELULAR, DIRECCION, EMAIL, EDAD) VALUES (?,?,?,?,?,?,?,?,?)");
			ResultSet rs = pg.createStatement().executeQuery("SELECT * FROM funcionarios");
			while (rs.next()) {
				ps.setLong(1, rs.getLong("id"));
				ps.setInt(2, rs.getInt("legajo"));
				ps.setString(3, rs.getString("nombre"));
				ps.setString(4, rs.getString("apellido"));
				ps.setInt(5, rs.getInt("cedula"));
				ps.setString(6, rs.getString("celular"));
				ps.setString(7, rs.getString("direccion"));
				ps.setString(8, rs.getString("email"));
				ps.setInt(9, rs.getInt("edad"));
				ps.executeUpdate();
			}
			rs.close();
			ps.close();
			ConexionBD.cerrarConexion(pg);
			ConexionBD.cerrarConexion(orcl);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Migrador.migrarPostgresAOracle();
	}
}
