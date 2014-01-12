package Bazy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Timer;

public class ObserwatorZmian implements RezerwacjePodmiot {

	private static final int MINUT_15 = 900000;
	static int iloscRezerwacji = 0;

	private RezerwacjeObserwator obserwator = null;

	private Polaczenie liczenieRezerwacji = new Polaczenie();
	private int ile = 0;

	ObserwatorZmian() {

		Timer zegar = new Timer(MINUT_15, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ile = 0;

				try {
					ResultSet rezerwacje = liczenieRezerwacji.pobierzDane("",
							"SELECT * FROM REZERWACJE");
					while (rezerwacje.next()) {
						ile++;
					}
					if (ile > iloscRezerwacji) {
						iloscRezerwacji = ile;
						informuj();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		zegar.start();
	}

	@Override
	public void dodajObserwatora(RezerwacjeObserwator obs) {
		obserwator = obs;
	}

	@Override
	public void informuj() {
		if (obserwator != null) {
			obserwator.aktualizuj(null);
		}
	}
}
