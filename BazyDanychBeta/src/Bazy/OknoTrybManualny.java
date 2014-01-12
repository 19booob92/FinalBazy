package Bazy;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class OknoTrybManualny extends JFrame implements KeyListener{
	private JTextField zapytanie = null;
	private JTextField wynik = null;
	
	public OknoTrybManualny() {
		super("Super User");
		
		setVisible(true);
		setSize(700,400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setLayout(new FlowLayout());
		
		zapytanie = new JTextField();
		zapytanie.setPreferredSize(new Dimension(600,20));		
		zapytanie.addKeyListener(this);
		wynik = new JTextField();
		wynik.setPreferredSize(new Dimension(300,300));
		
		add(zapytanie);
		add(wynik);
	}

	@Override
	public void keyPressed(KeyEvent e) {										//jeśli klawisz = enter to loguj
	if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			try {
				new Polaczenie().pobierzDane(null, zapytanie.getText());
			}
			catch (Exception ex) {
				System.err.println(ex);
				wynik.setText(ex.toString());
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
