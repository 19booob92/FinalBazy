package Bazy;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class OknoInsert extends JFrame {
	static Choice listaTablice = null;
	Polaczenie polaczenieDlaInsert = null;
	private int x = 0;
	private String nazwaGeneratora = "";

	final TreeMap<String, JTextField> polaTekstowe = new TreeMap<String, JTextField>();

	public OknoInsert(final String jakaTab) {
		super("Wstawianie");

		setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setLocationRelativeTo(null);

		listaTablice = new Choice();
		JButton dodaj = new JButton("Dodaj");

		polaczenieDlaInsert = new Polaczenie();
		ArrayList<String> nazwyKolumn = OknoGlowneRodzic.zwrocWszystkieNazwy();
		setLayout(new GridLayout(nazwyKolumn.size() + 2, 1));

		setSize(200, nazwyKolumn.size() * 30);

		switch (jakaTab) {
		case "KSIAZKI": {
			nazwaGeneratora = "gen_id(ksiazki_id,1),";
			x = 1;
			break;
		}
		case "CZYTELNICY": {
			nazwaGeneratora = "gen_id(czytelnik,1),";
			x = 1;
			break;
		}
		case "PRACOWNICY": {
			nazwaGeneratora = "gen_id(pracownik,1),";
			x = 1;
			break;
		}

		default:
			x = 0;
			break;
		}

		for (int i = x; i < nazwyKolumn.size(); i++) {
			polaTekstowe.put("pole" + i, new JTextField("null"));
			add(new JLabel(nazwyKolumn.get(i).trim()));
			add(polaTekstowe.get("pole" + i));
			polaTekstowe.get("pole" + i).setPreferredSize(
					new Dimension(100, 20));
		}

		try {
			ResultSet nazwy = polaczenieDlaInsert.pobierzNazwyTablic();
			while (nazwy.next()) {
				this.listaTablice.add(nazwy.getString(1));
			}
		} catch (SQLException e) {
			System.err.println(e);
		}

		add(dodaj);

		dodaj.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<String> pola;
				String tmp = "(" + nazwaGeneratora;

				for (JTextField pole : polaTekstowe.values()) {
					if (pole.getText().equals("null") || pole.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Kazde pole musi byc wypelnione !");
					} else {
						tmp += "\'" + pole.getText() + "\',";	
					}
				}

				tmp = tmp.substring(0, tmp.length() - 1);
				tmp += ")";
				try {
					System.out.println("INSERT INTO " + jakaTab + " VALUES "
							+ tmp);
					polaczenieDlaInsert.wykonajUpdate("INSERT INTO " + jakaTab
							+ " VALUES " + tmp);
				} catch (SQLException e) {
					System.err.println(e);
				}
				dispose();
			}
		});
	}
}
