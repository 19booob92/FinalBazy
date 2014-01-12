package Bazy;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class OknoGlowneRodzic extends JFrame {
	private int blokowanieDrugiegoOkna = 0;
	static JScrollPane pane = null;
	private DefaultTableModel modelTabeli;
	private Boolean czyDodawacWiersze = true;
	static Choice listaTablice = null;
	static String jakaTablica = "CZASOPISMA"; // to musi być pierwsza tablica z
												// listy!!!!
	private String[] kopiaNazwKolumn;
	private String wybraneKryterium = null;

	static ArrayList<String> zwrocWszystkieNazwy() {
		try {
			ArrayList<String> nazwy = new ArrayList<String>();
			ResultSet nazwyKolumn = new Polaczenie()
					.pobierzDane(
							"*",
							"SELECT Fields.RDB$FIELD_NAME \"Column Name\" FROM RDB$RELATION_FIELDS Fields WHERE Fields.RDB$RELATION_NAME = '"
									+ jakaTablica
									+ "'  and Fields.RDB$SYSTEM_FLAG = 0 ORDER BY Fields.RDB$FIELD_POSITION");
			while (nazwyKolumn.next()) {
				nazwy.add(nazwyKolumn.getString(1));
			}
			return nazwy;

		} catch (SQLException blad) {
			System.out
					.println("problem z pobraniem danych albo ze stowrzeneim tablicy");
		}
		return null;
	}

	public OknoGlowneRodzic(String tytul) {
		super(tytul);

		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 600);
		setLocationRelativeTo(null);
		final JTable tabelka = null;
		listaTablice = new Choice();
		setLayout(new FlowLayout());

		final JButton przycisk = new JButton("Wykonaj");
		JButton logout = new JButton("Wyloguj");
		final JTextField poleTekstowe = new JTextField("nazwa kolumny");
		poleTekstowe.setPreferredSize(new Dimension(100, 20));
		JLabel select = new JLabel("Pobierz kolumne: ");
		JLabel from = new JLabel(" z tabeli: ");
		JButton schemat = new JButton("Pokaz schemat");
		add(select);
		add(poleTekstowe);
		add(from);
		add(przycisk);

		logout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						new OknoLogowania();
					}
				});
			}
		});

		schemat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {

					@Override
					public void run() {
						new OknoZObrazkiem();
					}
				});
			}
		});

		listaTablice.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				wybraneKryterium = null;
				jakaTablica = listaTablice.getSelectedItem();
			}

		});

		przycisk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				czyDodawacWiersze = true;
				String[] tabelki = null;
				try {
					remove(pane);
				} catch (NullPointerException exc) {
					System.out
							.println("Brak pane' a, nie mam czego usuwac... ");
				}

				if (poleTekstowe.getText().equals("*")) {
					ArrayList<String> tmp = zwrocWszystkieNazwy();

					tabelki = tmp.toArray(new String[tmp.size()]);
					final String[] kopia = tabelki;

				}else if (poleTekstowe.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Pole musi byc wypelnione");
				}
				else {
					tabelki = poleTekstowe.getText().split(",");
				}
			
				modelTabeli = new DefaultTableModel(tabelki, 0);
				final JTable tabelka = new JTable(modelTabeli);
				kopiaNazwKolumn = tabelki; // tutaj kopiuje żeby przekaz do
											// klasy anonimowej

				tabelka.setCellSelectionEnabled(true);
				ListSelectionModel modelWyboruKomorki = tabelka
						.getSelectionModel();
				modelWyboruKomorki
						.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

				tabelka.getTableHeader().addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						int nrKolumny = tabelka.columnAtPoint(e.getPoint());
						wybraneKryterium = tabelka.getColumnName(nrKolumny)
								.trim() + " ASC";
						przycisk.doClick();
					}
				});

				modelWyboruKomorki
						.addListSelectionListener(new ListSelectionListener() {

							@Override
							public void valueChanged(ListSelectionEvent arg0) {
								final int x = tabelka.getSelectedRow();
								final int y = tabelka.getSelectedColumn();

								EventQueue.invokeLater(new Runnable() {

									@Override
									public void run() {
										blokowanieDrugiegoOkna++;
										if (blokowanieDrugiegoOkna % 2 == 0) {
											OknoDaneKsiazek oknoDane = new OknoDaneKsiazek(
													jakaTablica, tabelka
															.getColumnName(y)
															.trim(), tabelka
															.getValueAt(x, y),
													zwrocWszystkieNazwy());// OknoDaneKsiazek(jakaTablica,
																			// tabelka.getColumnName(y).trim(),
																			// tabelka.getValueAt(x,
																			// y)
																			// ,
																			// zwrocWszystkieNazwy());
										}
									}
								});
							}
						});

				pane = new JScrollPane(tabelka);
				int i = -1, j = -1;
				String[] mock = { "", "" };

				add(pane);

				try {
					for (String kolumna : tabelki) {
						ResultSet wynikZapytania = null;

						if (wybraneKryterium != null) {
							wynikZapytania = new Polaczenie(null)
									.pobierzPosortowaneDane(kolumna.trim(),
											wybraneKryterium);
						} else {
							wynikZapytania = new Polaczenie(null)
									.pobierzDane(kolumna.trim());
						}

						j++;
						while (wynikZapytania.next()) {
							i++;
							if (czyDodawacWiersze) {
								modelTabeli.addRow(mock); // dodwanie sztucznego
															// wiersza
							}
							modelTabeli.setValueAt(
									wynikZapytania.getString(kolumna.trim()),
									i, j);
						}
						i = -1;
						czyDodawacWiersze = false;
					}
				} catch (SQLException e) {
					System.out.print(e);
				}
				setVisible(true);
			}
		});
		add(schemat);
		add(logout);
	}
}