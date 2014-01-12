package Bazy;

import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class OknoGlowneZwyklegoUsera extends OknoGlowneRodzic {
	
	public OknoGlowneZwyklegoUsera() {
		super("Biblioteka");
		
		jakaTablica = "KSIAZKI";
		
		listaTablice.add("KSIAZKI");
		listaTablice.add("CZASOPISMA");
		add(listaTablice);
		
	}
}
