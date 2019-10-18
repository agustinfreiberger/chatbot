package Interface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import org.apache.lucene.queryparser.classic.ParseException;

import Query.Client;
import Query.Indexador;
import Utilities.DBGenerator;

import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Toolkit;

public class GameChooserWindow {

	private JFrame frmChooseYourDestiny;
	private JTextField ChooseTxt;
	private JFrame frame;
	private static Indexador indexer;
	private static Client client;
	@SuppressWarnings("unused")
	private static DBGenerator dbg;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameChooserWindow window = new GameChooserWindow();
					window.frmChooseYourDestiny.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GameChooserWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChooseYourDestiny = new JFrame();
		Image img = new ImageIcon(this.getClass().getResource("/icono.png")).getImage();
		frmChooseYourDestiny.setIconImage(img);
		frmChooseYourDestiny.getContentPane().setBackground(new Color(51, 51, 51));
		frmChooseYourDestiny.setTitle("Choose your destiny!");
		frmChooseYourDestiny.setBounds(100, 100, 450, 300);
		frmChooseYourDestiny.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmChooseYourDestiny.getContentPane().setLayout(null);
		JButton BotonLOL = new JButton("");
		BotonLOL.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				try {
					frmChooseYourDestiny.setVisible(false);
					initializeChat();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		BotonLOL.setHorizontalAlignment(SwingConstants.CENTER);
		BotonLOL.setIcon(new ImageIcon(this.getClass().getResource("/LoL.jpg")));
		BotonLOL.setBounds(0, 39, 220, 211);
		frmChooseYourDestiny.getContentPane().add(BotonLOL);
		
		JButton BotonHS = new JButton("");
		BotonHS.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.exit(0);
			}
		});
		BotonHS.setVerticalAlignment(SwingConstants.TOP);
		BotonHS.setHorizontalAlignment(SwingConstants.CENTER);
		BotonHS.setIcon(new ImageIcon(this.getClass().getResource("/Hearthstone.jpg")));
		BotonHS.setBounds(219, 39, 215, 211);
		frmChooseYourDestiny.getContentPane().add(BotonHS);
		
		ChooseTxt = new JTextField();
		ChooseTxt.setFont(new Font("Verdana", Font.BOLD, 18));
		ChooseTxt.setEditable(false);
		ChooseTxt.setHorizontalAlignment(SwingConstants.CENTER);
		ChooseTxt.setText("Elije tu juego");
		ChooseTxt.setBounds(126, 0, 161, 34);
		frmChooseYourDestiny.getContentPane().add(ChooseTxt);
		ChooseTxt.setColumns(10);
	}
	private void initializeChat() throws Exception {
		
		dbg = new DBGenerator("");        //Generación base de datos
		indexer = new Indexador();
		client = new Client(indexer);
		indexer.createIndex();                //Creación del indice
			
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				indexer.clear();
			}
		});
		frame.setBounds(100, 100, 695, 585);
		Image img = new ImageIcon(this.getClass().getResource("/icono.png")).getImage();
		frame.setIconImage(img);
		frame.setTitle("ChatBot-LoL ");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 679, 500);
		frame.getContentPane().add(scrollPane);
		
		final JTextArea Chat = new JTextArea();
		Chat.setFont(new Font("Verdana", Font.PLAIN, 13));
		Chat.setBackground(UIManager.getColor("Button.light"));
		Chat.setEditable(false);
		scrollPane.setViewportView(Chat);
		
		final JTextArea input = new JTextArea();
		input.setBorder(new LineBorder(new Color(0, 0, 0)));
		input.setLineWrap(true);
		input.setForeground(SystemColor.desktop);
		input.setBackground(UIManager.getColor("CheckBox.background"));
		input.setFont(new Font("Verdana", Font.PLAIN, 14));
		input.setBounds(0, 503, 679, 43);
		frame.getContentPane().add(input);
		frame.setVisible(true);
		input.addKeyListener(
								new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					input.setEditable(false);
					if(!input.getText().isEmpty()) {
						String quote=input.getText();                //Lee el input
						input.setText("");
						Chat.append("-->Yo: "+quote);
						quote.trim();
						
												
						int outPutType = 1 ; 	
												
						
						if( outPutType==1){
							String resultado = null;
			
							try {
								resultado = client.talk(quote);       
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							Chat.append("\n-->Bot: " + resultado);
					
						}	
					}
					Chat.append("\n");
			}
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
						input.setEditable(true);
					}
				}
			}
		);
		
	}
	
}
