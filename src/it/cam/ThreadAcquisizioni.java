/*ThreadAcquisizioni.java
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

package it.cam;

import org.opencv.highgui.VideoCapture;

import it.mail.ThreadInvioMail;

//Thread che gestisce l'acquisizione delle immagini

public class ThreadAcquisizioni extends Thread implements Runnable {

	private GestoreAcquisizioni ga;
	public GestoreAcquisizioni getGa() {
		return ga;
	}
	public void setGa(GestoreAcquisizioni ga) {
		this.ga = ga;
	}
		
	private FaceDetector faceDetector;
	
	
	public ThreadAcquisizioni(GestoreAcquisizioni ga) {
		this.ga=ga;		
		this.faceDetector=new FaceDetector();
		
	}
	
	//metodo che converte il frame acquisito e lo inserisce nei relativi pannelli
	public void elaboraFrame(){
		//rimpicciolisco le immagini
		ga.resizeFrame();		
				
	}
	
	@Override
	public void run() {
		try {
				//inizializzo la webcam 0 (quella di default)
				ga.setCamera(new VideoCapture(0));
				//leggo un frame
				ga.getCamera().read(ga.getFrame()); 
				//verifico che la telecamera sia aperta
				if(!ga.getCamera().isOpened()){					
					ga.getCamera().release();
					ga.getUtility().eccezione("Camera non aperta");					
				}

				else {						
					do{		
						//se la telecamera non riesce a leggere il frame,il programma viene arrestato
						if (!ga.getCamera().read(ga.getFrame())){													
							ga.getUtility().eccezione("Errore nella lettura del frame");							
						}										
					}//se viene rilevata almeno una faccia,viene inviata la mail
					while(!faceDetector.trovaFacce(ga.getFrame()));
					System.out.println(ga.getUtility().getTimestamp()+" Volto rilevato");
					//rilascio la telecamera
					ga.getCamera().release();
					//salvo l'immagine su disco
					ga.saveMatImage();
					//faccio partire il thread che effettua l'invio della mail
					ThreadInvioMail tim=new ThreadInvioMail(ga.getUtility());
					tim.start();
				}
				
			} catch(Exception ex) {
				ex.printStackTrace();
				ga.getCamera().release();
				ga.getUtility().eccezione(ex.getMessage());								
			}
	}
		
}		
