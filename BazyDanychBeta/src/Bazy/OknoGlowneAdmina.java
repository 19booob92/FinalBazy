package Bazy;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class OknoGlowneAdmina extends OknoGlowneRodzic implements RezerwacjeObserwator{
	
	public OknoGlowneAdmina() {
		super("Biblioteka ROOT");
		
		ObserwatorZmian obs = new ObserwatorZmian();
		obs.dodajObserwatora(this);
		jakaTablica = "KSIAZKI";
		JButton dodaj = new JButton("Wstaw");
		JButton usun = new JButton("Usun");
		JButton  superUser= new JButton("SU");
		add(listaTablice);
		add(dodaj);
		add(usun);
		add(superUser);
		
		superUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						new OknoTrybManualny();
					}
				});
			}
		});
		new Przypominanie().sprawdzStatusy(); //dasdsadasdqwojhwqdibdnsbsdbhhs
		dodaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						new OknoInsert(jakaTablica);
					}
				});
			}
		});
		
		usun.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
					
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						new OknoDrop(jakaTablica);
					}
				});
			}
		});
	}
	
	@Override
	public void aktualizuj(ResultSet nowaTablicaRezerwacji) {
		JOptionPane.showMessageDialog(null, "Pojawila sie nowa rezerwacja !!!");
	}
}
