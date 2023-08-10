package py.edu.ucsa.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import py.edu.ucsa.jdbc01.conexion.ConexionBD;

public class Metadatos {

	public void getMetadata() {

		Connection c = ConexionBD.getConexion();
		try {
			DatabaseMetaData metadatos = c.getMetaData();
			String nombreBD = metadatos.getDatabaseProductName();
			System.out.println("El nombre de la base de datos es: " + nombreBD);
			/*
			 * ResultSet rs= metadatos.getSchemas(); while(rs.next()) {
			 * rs.getString("TABLE_CATALOG");
			 * System.out.println(rs.getString("TABLE_SCHEM")); }
			 */

			String[] types = { "TABLE" };
			ResultSet rstablas = metadatos.getTables(null, "public", null, types);

			while (rstablas.next()) {
				System.out.println("TABLA: " + rstablas.getString("TABLE_NAME"));
				System.out.print(" - ");
				System.out.println("TIPO: " + rstablas.getString("TABLE_TYPE"));
				System.out.print(" - ");
				System.out.println("ESQUEMA: " + rstablas.getString("TABLE_SCHEM"));
				System.out.println("\n");
				ResultSet rsDatosTabla = metadatos.getColumns(null, rstablas.getString("TABLE_SCHEM"),
						rstablas.getString("TABLE_NAME"), null);
				while (rsDatosTabla.next()) {
					String nombreColumna = rsDatosTabla.getString("COLUMN_NAME");
					String tipoColumna = rsDatosTabla.getString("TYPE_NAME");
					String permiteNulos = rsDatosTabla.getString("NULLABLE");
					System.out.println("COLUMNA, nombre: " + nombreColumna + "\n Tipo: " + tipoColumna + "\n Nulable:"
							+ ("1".equals(permiteNulos) ? "Si" : "No"));
					System.out.println("\n");
				}

				System.out.println("************************************");
				ResultSet rsconsulta = c.createStatement()
						.executeQuery("SELECT * FROM " + rstablas.getString("TABLE_NAME"));
				ResultSetMetaData rst = rsconsulta.getMetaData();
				String nombreColumna = rst.getColumnName(1);
				System.out.println("El nombre de la columna es: " + nombreColumna);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
