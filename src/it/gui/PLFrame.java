package it.gui;

import javax.swing.JFrame;

import it.util.Utility;

//frame per il configuratore del programma

public class PLFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Utility utility;
	
	public PLFrame(Utility ut) {
		super();
		this.utility=ut;		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("PhotoLogin Configurator");
		getContentPane().add(new PLPanel(this.utility));
		//imposto la dimensione minima per visualizzare tutti i componenti
		pack();
		//posiziono la finestra al centro dello schermo
		setLocationRelativeTo(null);
		//rendo visibile la finestra
		setVisible(true);
	}
}
