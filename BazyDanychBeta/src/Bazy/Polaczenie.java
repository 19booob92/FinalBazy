package Bazy;

import java.awt.EventQueue;
import java.net.ProxySelector;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.JOptionPane;

public class Polaczenie {
	OknoLogowania oknoLogowania = null;
	private static Statement statement = null;

	Polaczenie() {
	}

	Polaczenie(OknoLogowania a) {
		oknoLogowania = a; // przekazanie referencji do okna
	}

	public void zalogujDoBazy(String login, String haslo) {
		try {
			Class.forName("org.firebirdsql.jdbc.FBDriver");

			Properties props = new Properties();
			props.setProperty("user", login);
			props.setProperty("password", haslo);
			props.setProperty("encoding", "WIN1250");

			try {
				if (Integer.parseInt(login) > 1000) {
					props.setProperty("roleName", "BIBLIOTEKARZ");
				}
			} catch (Exception e) {
				System.out.println("Blad");
			}

			Connection polacz = DriverManager
					.getConnection(
							"jdbc:firebirdsql:localhost://home/booob/workspace/baza_dobra.gdb",
							props);
			statement = polacz.createStatement();

			if ((login.equals("sysdba") || (login.equals("SYSDBA")))) {

				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						oknoLogowania.dispose();
						new OknoGlowneAdmina();
						try {
							ResultSet doListy = statement
									.executeQuery("SELECT * FROM TABELE");

							while (doListy.next()) {
								OknoGlowneAdmina.listaTablice.add(doListy
										.getString(1));
							}
						} catch (Exception e) {
							OknoGlowneZwyklegoUsera.listaTablice.add("KSIAZKI");
							OknoGlowneZwyklegoUsera.listaTablice
									.add("CZASOPISMA");
						}
					}
				});

			} else if ((Integer.parseInt(login.toString()) >= 1000)) {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						oknoLogowania.dispose();
						new OknoGlowneAdmina();

						OknoGlowneAdmina.listaTablice.add("KSIAZKI");
						OknoGlowneAdmina.listaTablice.add("CZASOPISMA");
						OknoGlowneAdmina.listaTablice.add("CZYTELNICY");
						OknoGlowneAdmina.listaTablice.add("REZERWACJE");
						OknoGlowneAdmina.listaTablice.add("WYPOZYCZENIA");

					}
				});
			} else {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						oknoLogowania.dispose();
						new OknoGlowneZwyklegoUsera();
						try {

						} catch (Exception e) {
						}
					}
				});
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Przykro mi złe hasło :(:/:|");
			System.out.print(e);
		}
	}

	public ResultSet pobierzPosortowaneDane(String zapytanie,
			String kryteriumSortowania) {
		try {
			return statement.executeQuery("SELECT " + zapytanie + " FROM "
					+ OknoGlowneAdmina.jakaTablica + " ORDER BY "
					+ kryteriumSortowania);
		} catch (Exception e) {
			System.out.print(e);
		}
		return null;
	}

	public ResultSet pobierzDane(String zapytanie) {
		try {
			return statement.executeQuery("SELECT " + zapytanie + " FROM "
					+ OknoGlowneAdmina.jakaTablica);
		} catch (Exception e) {
			System.out.print(e);
		}
		return null;
	}

	public int wykonajUpdate(String zapytanie) throws SQLException {
		try {
			return statement.executeUpdate(zapytanie);
		} catch (Exception e) {
			throw new SQLException();
		}
	}

	public ResultSet pobierzDane(String string, String zapytanie)
			throws SQLException {
		try {
			return statement.executeQuery(zapytanie);
		} catch (Exception e) {
			throw new SQLException();
		}

	}

	public ResultSet pobierzNazwyTablic() {
		try {
			// przeciążona wersja dla pobrania danych do zapytania "*"
			ResultSet doListy = statement.executeQuery("SELECT * FROM TABELE");
			return doListy;
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
	}

	public void updateIlosci(String tablica, String aktualnaWartosc,
			int zamienNa) {
		try {
			statement.executeUpdate("UPDATE " + tablica
					+ " SET ILOSC_DOSTEPNYCH = " + zamienNa
					+ " WHERE ILOSC_DOSTEPNYCH = " + aktualnaWartosc);
			System.out
					.println("UPDATE " + tablica + " SET ILOSC_DOSTEPNYCH = "
							+ zamienNa + " WHERE ILOSC_DOSTEPNYCH = "
							+ aktualnaWartosc);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
