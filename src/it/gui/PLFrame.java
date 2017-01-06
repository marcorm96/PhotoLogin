/*PLFrame.java
  Copyright (c)Marco Rosario Martino aka marcorm96 <marcorosm96@gmail.com>
  
  All rights reserved.

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 3.0 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public
  License along with this library.
*/

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
