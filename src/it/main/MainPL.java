package it.main;

import org.opencv.core.Core;

import it.cam.GestoreAcquisizioni;
import it.cam.ThreadAcquisizioni;
import it.gui.PLFrame;
import it.util.Utility;

public class MainPL {
 
    @SuppressWarnings("unused")
	

	public static void main(String[] args) {
    	//Leggo la libreria di opencv
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Utility utility=new Utility();
		if(utility.outputSuFile()) {
			if(args.length>0 && args[0].equals("start") && utility.controllaValori()) {
				//controllo se il destinatario,il mittente e la password sono stati
    			//settati nel file properties	        				        	    			
       			//se il programma viene avviato passandogli l'argomento start
       			//viene eseguito tutto in backgroud
       			System.out.println(utility.getTimestamp()+" INIZIO");
      			ThreadAcquisizioni ta=new ThreadAcquisizioni(new GestoreAcquisizioni(utility));
       			ta.start();	        				        	        	        
	        }
	        else {
	        	//avvio il frame per la configurazione
	        	PLFrame plframe = new PLFrame(utility);    	        	
	        }
		}        
    }
    
}