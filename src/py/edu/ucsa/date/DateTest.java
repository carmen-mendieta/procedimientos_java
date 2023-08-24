package py.edu.ucsa.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DateTest {
	public static void main(String[] args) {
		Calendar currentTime = Calendar.getInstance();
		java.sql.Date startDate = new java.sql.Date(currentTime.getTime().getTime());
		System.out.println(startDate);

		String fechaString = "20/08/2023";

		LocalDate fecha = LocalDate.parse(fechaString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		java.sql.Date date = java.sql.Date.valueOf(fecha);

		java.sql.Timestamp fechaHora = java.sql.Timestamp.valueOf(LocalDateTime.now());
         
		System.out.println(fecha);
		System.out.println(date);
		System.out.println(fechaHora);

	}

}
