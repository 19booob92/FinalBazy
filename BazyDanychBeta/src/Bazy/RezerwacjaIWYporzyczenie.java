package Bazy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class RezerwacjaIWYporzyczenie {
	
	Object identyfikator = null;
	Polaczenie polaczenieDlaInsert;
	private String nazwaPierwszejKolumny = "";
	private String iloscDostepnych ="";
	
	DateFormat formatDaty = new SimpleDateFormat("yyyy.MM.dd");
	Date data = new Date();
	Date dataZwrotu = new Date();
	String czasopismoID = null;
	String ksiazkaID = null;
	
	public RezerwacjaIWYporzyczenie(final String nazwaPierwszejKolumny, Object idKsiazki, String iloscDostepnych) {
		polaczenieDlaInsert = new Polaczenie();
		
		this.iloscDostepnych = iloscDostepnych;
		this.nazwaPierwszejKolumny = nazwaPierwszejKolumny;
		this.identyfikator = idKsiazki;
		if (Integer.parseInt(iloscDostepnych ) > 0) {
		}
	}
	
	void dekrementujKsiazki() {
		System.out.println("dekremntuje ksiazki");
		polaczenieDlaInsert.updateIlosci("Ksiazki", iloscDostepnych, (Integer.parseInt(iloscDostepnych))-1);
	}
	
	void dekrementujCzasopisma() {
		polaczenieDlaInsert.updateIlosci("Czasopisma", iloscDostepnych, Integer.parseInt(iloscDostepnych));
	}
	
	public void rezerwuj(int czas) {
		
		dataZwrotu.setMonth(data.getMonth()+czas);
		
		if (nazwaPierwszejKolumny.equals("TYTUL")) {
			ksiazkaID = "'" +identyfikator.toString() + "'";
			dekrementujKsiazki();
		}
		else if (nazwaPierwszejKolumny.equals("NR_WYDANIA")){
			czasopismoID = "'" + identyfikator.toString() + "'";
			dekrementujCzasopisma();
		}
		
		try {
			String userID = OknoLogowania.loginUsera;
			if (userID.toLowerCase().equals("sysdba")) {
				userID = String.valueOf(0);
			}
			System.out.println("INSERT INTO REZERWACJE VALUES ('" +
					formatDaty.format(data) + "','" + formatDaty.format(dataZwrotu) + "',"+czasopismoID
					+","+ksiazkaID+",'"+ userID +"',null)");
			
			polaczenieDlaInsert.wykonajUpdate("INSERT INTO REZERWACJE VALUES ('" +
					formatDaty.format(data) + "','" + formatDaty.format(dataZwrotu) + "',"+czasopismoID
					+","+ksiazkaID+",'"+ userID +"',null)");
			JOptionPane.showMessageDialog(null, "Dokonano rezerwacji");
			
		}
		catch (SQLException e) {
			System.err.println(e);
		}
	}

}
