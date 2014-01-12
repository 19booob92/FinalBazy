package Bazy;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.annotation.Documented;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OknoDaneKsiazek extends JFrame {
	static final String INF = "9999";
	ResultSet wszystkieDane;
	private StringBuilder daneDoKopiowania;
	private String czytelnikID = "";

	public OknoDaneKsiazek(final String zJakiejTabeli,
			final String nazwaKolumny, final Object wartosc,
			final ArrayList<String> kopiaNazwKolumn) {

		super("Dane książek");

		daneDoKopiowania = new StringBuilder();
		setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(500, 300);
		setLocationRelativeTo(null);
		setLayout(new FlowLayout());
		DefaultTableModel modelTabeliDane;
		JScrollPane panel = null;
		Boolean czyDodawacWiersze = true;
		JLabel naJakDlugo = new JLabel("na ile miesiecy");
		final JTextField czas = new JTextField();
		czas.setPreferredSize(new Dimension(100, 20));
		JButton rezerwuj = new JButton("Rezerwuj");
		JButton oznaczWyporzyczenie = new JButton("Oznacz jako wyporzyczone");
		oznaczWyporzyczenie.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				zapiszWWyporzyczonych();
				usunZRezerwacji();
			}
		});

		try {
			remove(panel);
		} catch (NullPointerException exc) {
			System.out.println("Brak pane' a, nie mam czego usuwac... ");
		}
		modelTabeliDane = new DefaultTableModel(new String[] { "Nazwa kolumny",
				"Wartosc" }, 0);
		final JTable tabelkaInfo = new JTable(modelTabeliDane);
		panel = new JScrollPane(tabelkaInfo);
		panel.setPreferredSize(new Dimension(400, 200));

		int i = -1, j = -1;
		String[] mock = { "", "" };

		add(panel);
		if (zJakiejTabeli.equals("KSIAZKI")
				|| zJakiejTabeli.equals("CZASOPISMA")) {
			add(naJakDlugo);
			add(czas);
			add(rezerwuj);
		} else if (zJakiejTabeli.equals("REZERWACJE")) {
			add(oznaczWyporzyczenie);
		}

		try {
			wszystkieDane = new Polaczenie().pobierzDane("",
					"SELECT FIRST 1 * FROM " + zJakiejTabeli + " WHERE "
							+ nazwaKolumny + " = '" + wartosc + "'");
			while (wszystkieDane.next()) {
				for (int k = 1; k < kopiaNazwKolumn.size(); k++) {
					i++;

					if (czyDodawacWiersze) {
						modelTabeliDane.addRow(mock); // dodwanie sztucznego
														// wiersza
					}
					modelTabeliDane
							.setValueAt(wszystkieDane.getString(k), i, 1);
					if (wszystkieDane.getString(k) != null) {
						daneDoKopiowania.append("'"
								+ wszystkieDane.getString(k) + "'" + ",");
					} else {
						daneDoKopiowania.append("null,");
					}
					czytelnikID = wszystkieDane.getString(5);

					modelTabeliDane.setValueAt(kopiaNazwKolumn.get(i).trim(),
							i, 0);
				}
				i = -1;
				czyDodawacWiersze = false;
			}
		} catch (SQLException e) {
			System.out.print(e);
		}
		setVisible(true);

		rezerwuj.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (czas.getText().isEmpty()
						|| Integer.parseInt(czas.getText()) > 6) {
					JOptionPane
							.showMessageDialog(null,
									"Pole musi zawierac wartosc liczbowa mniejsza niz 6");
				} else {
					try {
						wszystkieDane = new Polaczenie().pobierzDane("",
								"SELECT FIRST 1 * FROM " + zJakiejTabeli
										+ " WHERE " + nazwaKolumny + " = '"
										+ wartosc + "'");
						wszystkieDane.next();

						(new RezerwacjaIWYporzyczenie(kopiaNazwKolumn.get(2)
								.trim(), wszystkieDane.getString(1),
								wszystkieDane.getString(8))).rezerwuj(Integer
								.parseInt(czas.getText()));
					} catch (NumberFormatException e) {
						e.printStackTrace();
						;
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	private void usunZRezerwacji() {

		try {
			Polaczenie polaczenieDlaDrop = new Polaczenie();
			System.out.println("DELETE FROM REZERWACJE WHERE CZYTELNIK_ID ='"
					+ czytelnikID + "\'");
			ObserwatorZmian.iloscRezerwacji -= polaczenieDlaDrop
					.wykonajUpdate("DELETE FROM REZERWACJE WHERE CZYTELNIK_ID ='"
							+ czytelnikID + "\'");
		} catch (SQLException e) {
			System.err.println(e);
		}
	}

	private void zapiszWWyporzyczonych() {
		try {
			System.out.println("SELECT * INTO WYPOZYCZENIA "
					+ "FROM REZERWACJE WHERE CZYTELNIK_ID ='" + czytelnikID
					+ "'");
			Polaczenie kopiowanie = new Polaczenie();

			kopiowanie
					.wykonajUpdate("INSERT INTO WYPOZYCZENIA (DATA_ROZP_WYPOZYCZENIA ,DATA_ZWROTU, "
							+ "CZASOPISMO_IND, KSIAZKA_WYP, CZYTELNIK_ID, PRACOWNIK_ID)"
							+ " SELECT DATA_ROZP_REZERWACJI, TERMIN, CZASOPISMO_ID, KSIAZKA_REZ, CZYTELNIK_ID,"
							+ "'"
							+ OknoLogowania.loginUsera
							+ "' FROM REZERWACJE WHERE CZYTELNIK_ID='"
							+ czytelnikID + "'");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}