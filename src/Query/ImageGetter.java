package Query;

import java.awt.Container;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageGetter {
	
	private String imageurl;
	private String champsurl;
	
	public ImageGetter() {
		this.imageurl = "http://ddragon.leagueoflegends.com/cdn/8.24.1/img/item/";
		this.champsurl = "http://ddragon.leagueoflegends.com/cdn/8.24.1/img/champion/";
	}
	
	
	public void getItemImage(int id) throws MalformedURLException, IOException {
		 	JFrame frame = new JFrame(); 
		    frame.setSize(48, 100); 
		    Container contentPane = frame.getContentPane(); 
		    JLabel sentenceLabel= new JLabel(new ImageIcon(ImageIO.read(new URL(imageurl + id + ".png"))));
		    contentPane.add(sentenceLabel);
		    frame.add(sentenceLabel);
		    frame.setVisible(true);
	}
	
	public void getChampImage(String name) throws MalformedURLException, IOException {
		JFrame frame = new JFrame(); 
	    frame.setSize(150, 150); 
	    Container contentPane = frame.getContentPane(); 
	    JLabel sentenceLabel= new JLabel(new ImageIcon(ImageIO.read(new URL(champsurl + name + ".png"))));
	    contentPane.add(sentenceLabel);
	    frame.add(sentenceLabel);
	    frame.setVisible(true);
	}
	
}
