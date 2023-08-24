package py.edu.ucsa.jdbc01.statements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import py.edu.ucsa.jdbc01.conexion.ConexionBD;

public class FuncionarioDAO {

	public void migrarAMySQL(Connection postgres, Connection mysql) {
		String query = "SELECT * FROM funcionarios";
		try {
			PreparedStatement ps = mysql.prepareStatement("INSERT INTO funcionarios_bkp (nombre, "
					+ "direccion, cedula, celular, email, edad ) VALUES(?,?,?,?,?,?)");
			ResultSet rs = postgres.createStatement().executeQuery(query);
			while (rs.next()) {
				ps.setString(1, rs.getString("nombre"));
				ps.setString(2, rs.getString("direccion"));
				ps.setInt(3, rs.getInt("cedula"));
				ps.setString(4, rs.getString("celular"));
				ps.setString(5, rs.getString("email"));
				ps.setInt(6, rs.getInt("edad"));
				ps.executeUpdate(); // devuelve la cantidad de registros afectados, cuando es insert siempre
									// es 1
				// ParameterMetaData parameterMetaData = ps.getParameterMetaData();
			}
			// System.out.println("REGISTROS INSERTADOS: " + cant);
			rs.close();
			ps.close();
			ConexionBD.cerrarConexion(mysql);
			ConexionBD.cerrarConexion(postgres);
			// c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void agregarColumnaFuncionario() {

		try {
			Connection c = ConexionBD.getConexion();
			String alter = "ALTER TABLE funcionarios ADD COLUMN edad INTEGER";

			Statement s = c.createStatement();
			s.executeUpdate(alter);
			s.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void crearTabla() {
		try {
			// Instancia conexion
			Connection c = ConexionBD.getConexion();

			// Se instancia el Statement, al que se le pasara la query
			Statement s = c.createStatement();
			// Se prepare la query sql a ejecutarse
			String createStmt = "create table funcionarios " + "(id serial, legajo integer, " + "nombre varchar(50), "
					+ "apellido varchar(50), " + "direccion varchar(200) , " + "cedula integer , "
					+ "celular varchar(50) , " + "email varchar(50), "
					+ "constraint pk_funcionarios PRIMARY KEY (id)) ";

			// Ejecuta la query
			s.executeUpdate(createStmt);

			// Cierra conexiones
			s.close();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Funcionario> listarFuncionarios() {
		String query = "SELECT * FROM funcionarios";
		List<Funcionario> lista = new ArrayList<>();
		Connection c;
		try {
			c = ConexionBD.getConexion();
			Statement s = c.createStatement();
			// Se ejecuta la consulta y retorna el resultado del select en la variable rs
			ResultSet rs = s.executeQuery(query);
			Funcionario f = null;
			// recorre el cursor y verifica si hay regit¿stro o no en la siguiente fila
			while (rs.next()) {
				f = new Funcionario();
				f.setApellido(rs.getString("apellido"));
				f.setNombre(rs.getString("nombre"));
				f.setLegajo(rs.getInt("legajo"));
				f.setId(rs.getLong("id"));
				f.setCedula(rs.getInt("cedula"));
				f.setEmail(rs.getString("email"));
				f.setEdad(rs.getInt("edad"));
				lista.add(f);
			}
			rs.close();
			s.close();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lista;
	}
//	public void insertar(Funcionario f) {
//		Connection c;
//		try {
//			c = ConexionBD.getConexion();
//			Statement s = c.createStatement();
//			s.executeUpdate("INSERT INTO funcionarios (legajo, nombre, apellido, direccion, cedula, celular, email) VALUES(" 
//			+ f.getLegajo() + ",'" + f.getNombre() + "','" + f.getApellido() + "','" + f.getDireccion()
//			+ "'," +f.getCedula()+ ",'"	+ f.getCelular()+ "','" + f.getEmail()+ "')");
//			c.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

	public void insertar(Funcionario f) {
		Connection c;
		try {
			c = ConexionBD.getConexion();
			PreparedStatement ps = c.prepareStatement("INSERT INTO funcionarios (legajo, nombre, apellido, "
					+ "direccion, cedula, celular, email, edad,fecha_ingreso,fecha_nacimiento,fecha_creacion ) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
			System.out.println(f);
			ps.setInt(1, f.getLegajo());
			ps.setString(2, f.getNombre());
			ps.setString(3, f.getApellido());
			ps.setString(4, f.getDireccion());
			ps.setInt(5, f.getCedula());
			ps.setString(6, f.getCelular());
			ps.setString(7, f.getEmail());
			ps.setInt(8, f.getEdad());
			ps.setTimestamp(9, Timestamp.valueOf(f.getFechaIngreso()));
			ps.setDate(10, f.getFechaNacimiento());
			ps.setTimestamp(11, Timestamp.valueOf(f.getFechaCreacion()));
			int cant = ps.executeUpdate(); // devuelve la cantidad de registros afectados, cuando es insert siempre es 1
			// ParameterMetaData parameterMetaData = ps.getParameterMetaData();
			System.out.println("REGISTROS INSERTADOS: " + cant);
			ps.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void modificar(Funcionario f) {
		Connection c;
		try {
			c = ConexionBD.getConexion();
			PreparedStatement ps = c.prepareStatement("UPDATE funcionarios SET legajo = ?, nombre = ?, apellido = ?, "
					+ "direccion = ?, cedula = ?, celular = ?, email = ? WHERE id = ?");
			System.out.println(f);
			ps.setInt(1, f.getLegajo());
			ps.setString(2, f.getNombre());
			ps.setString(3, f.getApellido());
			ps.setString(4, f.getDireccion());
			ps.setInt(5, f.getCedula());
			ps.setString(6, f.getCelular());
			ps.setString(7, f.getEmail());
			ps.setLong(8, f.getId());
			int cant = ps.executeUpdate(); // devuelve la cantidad de registros afectados, cuando es update puede ser
											// mayor a 1
			System.out.println("REGISTROS ACTUALIZADOS: " + cant);
			ps.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Funcionario> buscar(int edadMin, int edadMax, String nombre, String apellido) {
		List<Funcionario> resultado = new ArrayList<>();
		Connection c;
		try {
			c = ConexionBD.getConexion();
			StringBuilder sentencia = new StringBuilder(
					"SELECT id, legajo, nombre, apellido, edad, cedula, celular, direccion, email FROM funcionarios ");
			boolean yaTieneWhere = false;
			if (edadMin > 0) { // se envio el parametro edadMin, añadir el campo al query
				sentencia.append("WHERE edad >= " + edadMin);
				yaTieneWhere = true;
			}
			if (edadMax > 0) { // se envio el parametro edadMin, añadir el campo al query
				if (yaTieneWhere) {
					sentencia.append(" AND edad <= " + edadMax);
				} else {
					sentencia.append("WHERE edad <= " + edadMax);
					yaTieneWhere = true;
				}
			}
			if (nombre != null && !nombre.isEmpty()) {
				if (yaTieneWhere) {
					sentencia.append(" AND nombre like '%" + nombre + "%'");
				} else {
					sentencia.append("WHERE nombre like '%" + nombre + "%'");
					yaTieneWhere = true;
				}
			}
			if (apellido != null && !apellido.isEmpty()) {
				if (yaTieneWhere) {
					sentencia.append(" AND apellido like '%" + apellido + "%'");
				} else {
					sentencia.append("WHERE apellido like '%" + apellido + "%'");
					yaTieneWhere = true;
				}
			}
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sentencia.toString());
			while (rs.next()) {
				System.out.println("Nombre: " + rs.getString("nombre") + ", Edad: " + rs.getInt("edad"));
			}
			rs.close();
			s.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
//	((OraclePreparedStatementWrapper) preparedStatement).getOriginalSql()
		return resultado;
	}

	public static void main(String[] args) {

		FuncionarioDAO dao = new FuncionarioDAO(); //
		Connection conPostgres = ConexionBD.getConexionPG();
		Connection conMysql = ConexionBD.getConexionMySQL();
		//dao.migrarAMySQL(conPostgres, conMysql);
		
		Funcionario f= new Funcionario();
        
		/*
		 * dao.agregarColumnaFuncionario(); // dao.crearTabla();
		 * 
		 * Funcionario f = new Funcionario();
		 * System.out.println("INGRESA LOS DATOS POR TECLADO: \n");
		 * 
		 * Scanner s = new Scanner(System.in);
		 * 
		 * System.out.print("Ingrese un nombre: "); f.setNombre(s.next());
		 * System.out.print("Ingrese un apellido: "); f.setApellido(s.next());
		 * System.out.print("Ingrese numero de legajo: "); f.setLegajo(s.nextInt());
		 * System.out.print("Ingrese numero de cedula: "); f.setCedula(s.nextInt());
		 * System.out.print("Ingrese numero de celular: "); f.setCelular(s.next());
		 * System.out.print("Ingrese direccion: "); f.setDireccion(s.next());
		 * System.out.print("Ingrese email: "); f.setEmail(s.next());
		 * System.out.print("Ingrese Edad: "); f.setEdad(s.nextInt());
		 * 
		 * s.close();
		 * 
		 * System.out.println("INSERCIONES: \n"); dao.insertar(f);
		 * 
		 * System.out.println("LISTANDO FUNCIONARIOS: \n");
		 * dao.listarFuncionarios().forEach((e) -> { System.out.println(e.getNombre());
		 * System.out.println(e.getApellido()); System.out.println(e.getEmail());
		 * System.out.println(e.getEdad()); }); System.out.println("BUSCAR");
		 * dao.buscar(30, 45, null, null);
		 */
	}
}
