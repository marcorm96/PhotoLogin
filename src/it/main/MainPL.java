/*MainPL.java
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
