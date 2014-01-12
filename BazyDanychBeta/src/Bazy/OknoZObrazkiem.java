package Bazy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class OknoZObrazkiem extends JFrame{
	public OknoZObrazkiem() {
		super ("Schemat");
		
		setVisible(true);
		setSize(834,652);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		JLabel sch = new JLabel();
		sch.setSize(834, 622);
		
		try {
			File obrazek = new File("/home/booob/workspace/schemat_bazy.png");
			BufferedImage buffor = ImageIO.read(obrazek);
			ImageIcon ikona = new ImageIcon(buffor);
			sch.setIcon(ikona);
			add(sch);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
