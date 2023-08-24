package py.edu.ucsa.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import py.edu.ucsa.dto.FuncionarioDTO;
import py.edu.ucsa.jdbc01.conexion.ConexionBD;

public class FuncionarioDAOJdbcImpl implements FuncionarioDAO {
	public void crearTabla() {
		try {
			Connection c = ConexionBD.getConexion();
			Statement s = c.createStatement();
			String createStmt = "create table funcionarios " +  
					"(id serial, legajo integer, " +
					"nombre varchar(50), " +
					"apellido varchar(50), " +
					"direccion varchar(200) , " +
					"cedula integer , " +
					"celular varchar(50) , " +
					"email varchar(50), " +
					"constraint pk_funcionarios PRIMARY KEY (id)) ";
			s.executeUpdate(createStmt);
			s.close();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<FuncionarioDTO> listarFuncionarios() {
		String query = "SELECT * FROM funcionarios";
		List<FuncionarioDTO> lista = new ArrayList<>();
		Connection c;
		try {
			c = ConexionBD.getConexion();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(query);
			FuncionarioDTO f = null;
			while(rs.next()) {
				f = new FuncionarioDTO();
				f.setApellido(rs.getString("apellido"));
				f.setNombre(rs.getString("nombre"));
				f.setLegajo(rs.getInt("legajo"));
				f.setId(rs.getLong("id"));
				f.setCedula(rs.getInt("cedula"));
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
	
	public void insertar(FuncionarioDTO f) {
		Connection c;
		try {
			c = ConexionBD.getConexion();
			PreparedStatement ps = 
					c.prepareStatement("INSERT INTO funcionarios "
										+ "( nombre, direccion, cedula, celular, email,fecha_nacimiento,fecha_creacion) "
										+ "VALUES(?,?,?,?,?,?,?)");
			System.out.println(f);
			ps.setString(1, f.getNombre());
			ps.setString(2, f.getDireccion());
			ps.setInt(3, f.getCedula());
			ps.setString(4, f.getCelular());
			ps.setString(5, f.getEmail());
			ps.setDate(6,f.getFechaNacimiento());
			ps.setTimestamp(7, Timestamp.valueOf(f.getFechaCreacion()));
			
			int cant = ps.executeUpdate(); //devuelve la cantidad de registros afectados, cuando es insert siempre es 1
			//ParameterMetaData parameterMetaData = ps.getParameterMetaData();
			System.out.println("REGISTROS INSERTADOS: " + cant);
			ps.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void modificar(FuncionarioDTO f) {
		Connection c;
		try {
			c = ConexionBD.getConexion();
			PreparedStatement ps = 
					c.prepareStatement("UPDATE funcionarios SET legajo = ?, nombre = ?, apellido = ?, "
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
			int cant = ps.executeUpdate(); //devuelve la cantidad de registros afectados, cuando es update puede ser mayor a 1
			System.out.println("REGISTROS ACTUALIZADOS: " + cant);
			ps.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<FuncionarioDTO> buscar(int edadMin, int edadMax, String nombre, String apellido){
		List<FuncionarioDTO> resultado = new ArrayList<>();
		Connection c;
		try {
			c = ConexionBD.getConexion();
			StringBuilder sentencia = new StringBuilder("SELECT id, legajo, nombre, apellido, edad, cedula, celular, direccion, email FROM funcionarios ");
			boolean yaTieneWhere = false;
			if (edadMin > 0) { //se envio el parametro edadMin, añadir el campo al query
				sentencia.append("WHERE edad >= " + edadMin);
				yaTieneWhere = true;
			}
			if (edadMax > 0) { //se envio el parametro edadMin, añadir el campo al query
				if (yaTieneWhere) {
					sentencia.append(" AND edad <= " + edadMax);		
				}else {
					sentencia.append("WHERE edad <= " + edadMax);
					yaTieneWhere = true;
				}
			}
			if (nombre != null && !nombre.isEmpty()) {
				if (yaTieneWhere) {
					sentencia.append(" AND nombre like '%" + nombre + "%'");		
				}else {
					sentencia.append("WHERE nombre like '%" + nombre + "%'");
					yaTieneWhere = true;
				}
			}
			if (apellido != null && !apellido.isEmpty()) {
				if (yaTieneWhere) {
					sentencia.append(" AND apellido like '%" + apellido + "%'");		
				}else {
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
		FuncionarioDAO dao = DAOFactory.getFuncionarioDAO();
		//dao.crearTabla();
//		FuncionarioDTO f = new FuncionarioDTO();
//		Scanner s = new Scanner(System.in);
//		
//		f.setNombre(s.next());
//		f.setApellido(s.next());
//		f.setLegajo(s.nextInt());
//		f.setCedula(s.nextInt());
//		f.setCelular(s.next());
//		f.setDireccion(s.next());
//		f.setEmail(s.next());
//		
//		s.close();
//		
//		dao.insertar(f);
//		
//		dao.listarFuncionarios().forEach((e) -> {
//			System.out.println(e.getNombre());
//			System.out.println(e.getApellido());
//			System.out.println(e.getEmail());
//		});
//		
//		dao.buscar(30, 45, null, null);
		
		FuncionarioDTO f = new FuncionarioDTO();
		f.setNombre("Nombre test");
		f.setDireccion("Direccion");
		f.setCedula(12345667);
		f.setCelular("09812345");
		f.setEmail("gmail.com test");
		f.setFechaNacimiento(Date.valueOf(LocalDate.now()));
		f.setFechaCreacion(LocalDateTime.now());
		
		dao.insertar(f);
	}

}
