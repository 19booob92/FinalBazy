package Bazy;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class OknoDrop extends JFrame{
	static String jakaTablica = "";
	
	public OknoDrop(final String nazwaTablicy) {
		super("Okno DROP");
		
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(855, 120);
		final JTextField coUsunac = new JTextField();
		JButton usun = new JButton("Usun");
		setLocationRelativeTo(null);
		
		final Choice lista = new Choice();
		coUsunac.setPreferredSize(new Dimension(100, 20));
		
		ArrayList<String> tmp = OknoGlowneRodzic.zwrocWszystkieNazwy();
		for (String nazwa : tmp) {
			lista.add(nazwa);
		}
		
		setLayout(new FlowLayout());
		
		add(new JLabel("DELETE FROM "+nazwaTablicy+" WHERE"));
		add(lista);
		add(new JLabel(" = '"));
		add(coUsunac);
		add(new JLabel(" \'"));
		
		add(usun);
		
		lista.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				jakaTablica = lista.getSelectedItem();
			}
			
		});
		
		usun.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Polaczenie polaczenieDlaDrop = new Polaczenie();
					polaczenieDlaDrop.wykonajUpdate("DELETE FROM "+ nazwaTablicy + " WHERE "+ jakaTablica + "='"
								+coUsunac.getText()+"\'");
				}
				catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Pole musi byc wypelnione poprawna wartoscia");
					System.err.println(e);
				}
			}
		});
		
	}
}
