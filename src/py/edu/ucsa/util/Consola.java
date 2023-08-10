package py.edu.ucsa.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Consola {
	
	/**
	 * 
	 * @param prompt Texto a imprimir en la consola
	 * @return El valor ingresado desde la consola.
	 * 
	 */
	public static String leerDatos(String prompt) {
		String res = "";
 	    BufferedReader stdin = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.print("Ingrese " + prompt);
		try {
			res = stdin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return res;
	}

}
