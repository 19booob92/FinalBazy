package Bazy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Przypominanie implements SystemWysylaniaMaili {

	private final static int TYDZIEN = 7;
	Set<String> zbior = null;

	@Override
	public void sprawdzStatusy() {
		Polaczenie datyZwrotuPolaczenie = new Polaczenie();
		ResultSet tabelaDaty = null, maile = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dataZTabeli = null;
		Date dzisiaj = new Date();
		zbior = new HashSet<String>();
		Statement statement;

		try {
			tabelaDaty = datyZwrotuPolaczenie.pobierzDane("",
					"SELECT * FROM WYPOZYCZENIA");

			Class.forName("org.firebirdsql.jdbc.FBDriver");

			Connection polacz = DriverManager
					.getConnection(
							"jdbc:firebirdsql:localhost://home/booob/workspace/baza_dobra.gdb",
							"sysdba", "1234");
			statement = polacz.createStatement();

			while (tabelaDaty.next()) {
				String kolumnDwa = tabelaDaty.getString(2).toString();
				dataZTabeli = format.parse(kolumnDwa);

				if ((dataZTabeli.getTime() - dzisiaj.getTime())
						/ (1000 * 60 * 60 * 24) <= TYDZIEN) {
					maile = statement
							.executeQuery("SELECT CZYTELNICY.E_MAIL FROM CZYTELNICY INNER JOIN WYPOZYCZENIA ON CZYTELNICY.ID ="
									+ " WYPOZYCZENIA.CZYTELNIK_ID WHERE data_zwrotu = '"
									+ kolumnDwa + "'");

					while (maile.next()) {
						zbior.add(maile.getString(1));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		for (String mail : zbior) {
			wyslijPrzypomnienie(mail);
			System.out.println(mail);
		}
	}

	@Override
	public void wyslijPrzypomnienie(String mail) {
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtps.auth", "true");
		Session mailSession = Session.getDefaultInstance(props);
		mailSession.setDebug(true);
		
		MimeMessage message = new MimeMessage(mailSession);
		try {
			message.setSubject("Termin oddania ksiazek");
			message.setContent("Prosze o zwocenie wyporzyczonych ksiazek do biblioteki", "text/plain; charset=ISO-8859-2");
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
			
			Transport transport = mailSession.getTransport();
			transport.connect("smtp.gmail.com", 465, "bibliotekadatabase", "bibliotekadatabase2000");
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			System.out.println("Wysylam maila do: " + mail);
			transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}

}
