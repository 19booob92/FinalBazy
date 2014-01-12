/*
 * Dodać alerty (message box)
 * Inkrementacja ID wszystkiego co można dodawać (szare pole, ++)
 * Zmenić nazwy na bardziej przystępne
 */

package Bazy;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Glowna {	
	
	public static void main(String [] args) {		
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new OknoLogowania();
			}
		});
	}
	
}
