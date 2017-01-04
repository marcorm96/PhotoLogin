package it.gui;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import it.cam.GestoreAcquisizioni;
import it.cam.ThreadAcquisizioni;
import it.util.Utility;

//pannello per il configuratore del programma

public class PLPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Utility ut;
	private JButton buttonProva;
	
	//componente per la scelta della directory
	public static JFileChooser dirChooser;
	//componente per l'inserimento dell'email del destinatario
	public static JTextField destTF;
	//componente per l'inserimento dell'email del mittente
	public static JTextField mittTF;
	//componente per l'inserimento della password del mittente
	public static JPasswordField pwdField;
	//componente per mostrare la cartella contenente le info relative all'esecuzione
	public static JTextField dirTF;
	 
	public PLPanel(Utility ut) {
		super();
		this.ut=ut;		
		setLayout(new GridLayout(5, 2));
		riempiPannello();
	}
	
	public void riempiPannello() {
		JLabel destLabel=new JLabel("Destinatario:");
		destTF=new JTextField(ut.getProp().getProperty("destinatario"));
		JLabel mittLabel=new JLabel("Mittente:");
		mittTF=new JTextField(ut.getProp().getProperty("mittente"));
		JLabel pwdLabel=new JLabel("Password Mittente:");
		pwdField=new JPasswordField(ut.getProp().getProperty("password"));
		JLabel dirLabel=new JLabel("Cartella:");	
		dirTF=new JTextField(ut.getDirPL());
		dirTF.setEditable(false);        
        JButton buttonChange=new JButton(ut.getProp().getProperty("changeButton"));
        buttonChange.addActionListener(new CambiaDirButtonListener(ut));
        buttonProva=new JButton(ut.getProp().getProperty("provaButton"));
        buttonProva.addActionListener(new ProvaListener(ut));
        //aggiungo i componenti al pannello
		this.add(destLabel);
		this.add(destTF);
		this.add(mittLabel);
		this.add(mittTF);
		this.add(pwdLabel);
		this.add(pwdField);
		this.add(dirLabel);
		this.add(dirTF);		
		this.add(buttonChange);
		this.add(buttonProva);
	}
	
	@Override
	public void paint(Graphics g) {		
		super.paint(g);
		destTF.setText(ut.getProp().getProperty("destinatario"));			
		dirTF.setText(ut.getDirPL());		
	}

	//metodo che controlla se tutti i campi sono stati settati correttamente
	public boolean controllaCampi(){
		String dest=destTF.getText();
		String mitt=mittTF.getText();
		String pwd=String.valueOf(pwdField.getPassword());
		//controllo se le email inserite sono sintatticamente giusta
		if(!ut.verificaMail(dest)) {
			if(!ut.verificaMail(mitt))			
				JOptionPane.showMessageDialog(null, ut.getProp().getProperty("erroreMail"));				
			return false;
		}
		if(pwd.isEmpty() || pwd.equals("")) {
			JOptionPane.showMessageDialog(null, ut.getProp().getProperty("errorePassword"));
			return false;
		}
		ut.getProp().setProperty("destinatario", dest);
		ut.getProp().setProperty("mittente", mitt);
		ut.getProp().setProperty("password", pwd);		
		ut.salvaProp();
		
		return ut.controllaValori();		
	}
	//Listener per il cambio di cartella
	class CambiaDirButtonListener implements ActionListener{

		Utility ut;
		
		public CambiaDirButtonListener(Utility ut) {
			this.ut=ut;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser dirChooser=new JFileChooser(ut.getProp().getProperty("dirPL"));
			//indica che dobbiamo scegliere solo le cartelle ( se non specificato, potranno essere selezionati solo i file)
	        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        //imposto come cartella da visualizzare la attuale
	        dirChooser.setCurrentDirectory(new File(ut.getDirPL()));
	        //restituisce l'intero JFileChooser.APPROVE_OPTION solo se si ha premuto su "Apri"
	        if(dirChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
	        	String newPath=dirChooser.getSelectedFile().getPath();
	        	System.out.println(ut.getTimestamp()+" "+ut.getProp().getProperty("cambioDir")+" in "+newPath);
	            //salvo nel properties
	            ut.getProp().setProperty("dirSis", newPath);
	    		ut.salvaProp();
	        }
	        repaint();
		}

	}

	//Listener per la prova del programma
	class ProvaListener implements ActionListener{

		Utility ut;
		
		public ProvaListener(Utility ut) {
			this.ut=ut;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {						
			if(!controllaCampi()) 
				JOptionPane.showMessageDialog(null, ut.getProp().getProperty("erroreProva"));
			else {
				System.out.println(ut.getTimestamp()+" PROVA");
				ut.setProvaInvioGui(true);;
				ThreadAcquisizioni ta=new ThreadAcquisizioni(new GestoreAcquisizioni(ut));
				ta.start();	
				repaint();
			}
		}
		
		
	}

	//Fine Listeners
}