package Bazy;

import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

public class OknoLogowania extends JFrame implements KeyListener {
	final JButton loguj;
	final JTextField login;
	final JPasswordField haslo;
	final Polaczenie nowePolaczenie = new Polaczenie(this);
	static String loginUsera;

	public OknoLogowania() {
		super("Logowanie");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(200, 100);
		setLocationRelativeTo(null);
		setLayout(new FlowLayout());

		login = new JTextField("podaj login");
		haslo = new JPasswordField("podaj haslo");
		loguj = new JButton("Logouj");

		add(login);
		add(haslo);
		add(loguj);

		haslo.addKeyListener(this);

		loguj.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loginUsera = login.getText();
				nowePolaczenie.zalogujDoBazy(login.getText(), haslo.getText());
			}
		});
	}

	@Override
	public void keyPressed(KeyEvent e) { // jeśli klawisz = enter to loguj
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			loginUsera = login.getText();
			nowePolaczenie.zalogujDoBazy(login.getText(), haslo.getText());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
