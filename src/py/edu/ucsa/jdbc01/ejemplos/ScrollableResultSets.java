package py.edu.ucsa.jdbc01.ejemplos;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import py.edu.ucsa.jdbc01.conexion.ConexionBD;
import py.edu.ucsa.util.Consola;


public class ScrollableResultSets {

	static void testResultSetType(final int RESULT_SET_TYPE) {
		if (RESULT_SET_TYPE == ResultSet.TYPE_FORWARD_ONLY)
			System.out.println("ES DE TIPO: TYPE_FORWARD_ONLY ");
		if (RESULT_SET_TYPE == ResultSet.TYPE_SCROLL_INSENSITIVE) 
			System.out.println("ES DE TIPO: TYPE_SCROLL_INSENSITIVE ");		
		if (RESULT_SET_TYPE == ResultSet.TYPE_SCROLL_SENSITIVE)
			System.out.println("ES DE TIPO: TYPE_SCROLL_SENSITIVE ");
	}

	static void testResultSetUpdate(int RESULT_SET_TYPE) {
		if  (RESULT_SET_TYPE == ResultSet.CONCUR_READ_ONLY)
			System.out.println("CONCUR_READ_ONLY ");
		if (RESULT_SET_TYPE == ResultSet.CONCUR_UPDATABLE)
			System.out.println("CONCUR_UPDATABLE");		
	}

	
	
	public static void queryAlumnos(Connection c) throws SQLException{
		Statement s;
		
		String selectStmt = "select " +  
		"nombre, direccion, cedula, celular,  email " +
		" from alumnos order by nombre";
		
		s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		//s = c.createStatement(); //antes
		int rst = s.getResultSetType();
		testResultSetType(rst);
		
		int rsc = s.getResultSetConcurrency();
		testResultSetUpdate(rsc);
		
		ResultSet rs = s.executeQuery(selectStmt);
		rs.beforeFirst(); //nos posicionamos antes del primer elemento
		
		System.out.println("***** Primero recorremos hacia adelante *****");
		int ultima = 0;
		while (rs.next()) {
			int fila = rs.getRow();
			System.out.print("Fila # " + fila + "Nombre: " + rs.getString("nombre"));
			System.out.print(" Direccion: " + rs.getString("direccion"));
			System.out.println(" Cedula: " + rs.getInt("cedula"));
			ultima = rs.getRow();			
		}
		System.out.println("***** Despues recorremos hacia atras *****");		
		rs.afterLast();

		while(rs.previous()){
			int fila = rs.getRow();
			System.out.print("Fila # " + fila + "Nombre: " + rs.getString("nombre"));
			System.out.print(" Direccion: " + rs.getString("direccion"));
			System.out.println(" Cedula: " + rs.getInt("cedula"));
		}

		int medio  = ultima / 2;		
		System.out.println("Posicion destino " + medio);
		if (medio > 0) {
			rs.absolute(2);
			int fila = rs.getRow();
			System.out.print("Fila # " + fila + "Nombre: " + rs.getString("nombre"));
			System.out.print(" Direccion: " + rs.getString("direccion"));
			System.out.println(" Cedula: " + rs.getInt("cedula"));
		}
		
		rs.close();
		s.close();
		
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		
			Connection c = ConexionBD.getConexion();
			
			queryAlumnos(c);
			//updateAlumnos(c);
			//queryAlumnos(c);	
			//insertAlumnos(c);
			//queryAlumnos(c);
			//deleteAlumnos(c);
			//queryAlumnos(c);		
			batchUpdateAlumnos(c);
			queryAlumnos(c);				
     		c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}


	public static void batchUpdateAlumnos(Connection c)  {
		PreparedStatement ps;
		
		try {
			// especificamos que los resultsets van a ser updateables.
			ps = c.prepareStatement(
					"insert into alumnos (nombre, direccion, cedula, celular,  email )" +
					" values (?,?,?,?,?)", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			c.setAutoCommit(false);
			System.out.println("BATCH, INGRESAR DATOS");
			for (int i = 0; i < 2; i++) {
				String nombre = Consola.leerDatos("nombre");
				String email = Consola.leerDatos("email");		
				String direccion = Consola.leerDatos("direccion");		
				String celular = Consola.leerDatos("celular");		
				String ci = Consola.leerDatos("cedula");		
				ps.setString(1, nombre);
				ps.setString(2, email);
				ps.setString(3, direccion);
				ps.setInt(4, Integer.valueOf(celular));
				ps.setInt(5, Integer.valueOf(ci));
				ps.addBatch();
				
			}
	
			int [] updateCounts = ps.executeBatch();
			c.commit();
			
			for (int i = 0; i < updateCounts.length; i++) {
				System.err.println("<<<Updated Rows" + i + updateCounts[i] + ">>>>>>>");
			}		
			
			ps.close();		
		}catch(BatchUpdateException b) {
			System.err.println("SQLException: " + b.getMessage());
			System.err.println("SQLState:  " + b.getSQLState());
			System.err.println("Message:  " + b.getMessage());
			System.err.println("Vendor:  " + b.getErrorCode());
			System.err.print("Update counts:  ");
			int [] updateCounts = b.getUpdateCounts();
			for (int i = 0; i < updateCounts.length; i++) {
				System.err.print(updateCounts[i] + "   ");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void deleteAlumnos(Connection c) throws SQLException {
		Statement s;
		
		String selectStmt = "select " +  
		"nombre, direccion, cedula, celular,  email " +
		" from alumnos";
		
		//especificamos que los resultsets van a ser updateables.
		s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		int rst = s.getResultSetType();
		testResultSetType(rst);
		int rsc = s.getResultSetConcurrency();
		
		//TODO: comprobar que el rs es modifiable.
		testResultSetUpdate(rsc);

		ResultSet rs = s.executeQuery(selectStmt);
		
		String fila = Consola.leerDatos("fila a borrar");
		rs.absolute(Integer.parseInt(fila));
		rs.deleteRow();
		
		rs.close(); //cerramos result set.
		s.close();  //cerramos el statement.	
	}


	public static void insertAlumnos(Connection c) throws SQLException {
		Statement s;
		
		String selectStmt = "select " +  
		"nombre, direccion, cedula, celular,  email " +
		" from alumnos order by nombre";
		
		//especificamos que los resultsets van a ser updateables.
		s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
		testResultSetType(s.getResultSetType());
		testResultSetUpdate(s.getResultSetConcurrency());
	
		ResultSet rs = s.executeQuery(selectStmt);
		
		System.out.println("PASO 1: " + rs.absolute(2));
		System.out.println("PASO 1.1: " + rs.getRow());
	
		// IMPORTANTE, MOVERSE AL INSERT ROW.
		rs.moveToInsertRow();
		
		System.out.println("INSERT, INGERSAR DATOS");		
		String nombre = Consola.leerDatos("nombre");
		String email = Consola.leerDatos("email");		
		String direccion = Consola.leerDatos("direccion");		
		String celular = Consola.leerDatos("celular");	
		String cedula = Consola.leerDatos("cedula");	
		
		rs.updateString("nombre", nombre);
		rs.updateString("email", email);
		rs.updateString("direccion", direccion);
		rs.updateString("celular", celular);	
//		rs.updateInt("cedula", Integer.parseInt(cedula));
		rs.updateInt("cedula", Integer.parseInt(cedula));
		
		rs.insertRow();	
		
		System.out.println("PASO 2: " + rs.getRow());
		
		//volvemos a la fila actual.
		rs.moveToCurrentRow();
		
		System.out.println("PASO 3: " + rs.getRow());
		
		rs.beforeFirst();
		
		System.out.println("PASO 4: " + rs.getRow());
		while (rs.next()) {
			int fila = rs.getRow();
			System.out.print("Fila # " + fila + "Nombre: " + rs.getString("nombre"));
			System.out.print(" Direccion: " + rs.getString("direccion"));
			System.out.println(" Cedula: " + rs.getInt("cedula"));			
		}
	
		rs.close(); //cerramos result set.
		s.close();  //cerramos el statement.
	}



	public static void updateAlumnos(Connection c) throws SQLException {
		Statement s;
		
		String selectStmt = "select " +  
		"id, nombre, direccion, cedula, celular,  email " +
		" from alumnos order by nombre";
		
		//especificamos que los resultsets van a ser updateables.
		s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		int rst = s.getResultSetType();
		testResultSetType(rst);
		
		ResultSet rs = s.executeQuery(selectStmt);
		
		//obtener cedula a modificar.
		int cedula =  Integer.parseInt(Consola.leerDatos("Cedula a modificar:"));
		String nombre = Consola.leerDatos("Nuevo nombre: ");
		String email = Consola.leerDatos("Nuevo email: ");		

//		if(rs.last()) {
//			rs.updateString("nombre", nombre);
//			rs.updateString("email", email);
//			rs.updateRow();
//		}
		while(rs.next()){
			if (rs.getInt("cedula") == cedula){
				rs.updateString("nombre", nombre);
				rs.updateString("email", email);
				rs.updateRow();
				break;
			}
		}
		
		rs.close(); //cerramos result set.
		s.close();  //cerramos el statement.
	}

}
