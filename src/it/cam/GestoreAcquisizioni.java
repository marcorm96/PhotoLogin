/*GestoreAcquisizioni.java
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

import java.awt.image.BufferedImage;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import it.util.Utility;


//Classe che gestisce la videocamera,i frames e i metodi applicabili su di essi

public class GestoreAcquisizioni {
	

	
	private VideoCapture camera;
	public VideoCapture getCamera() {
		return camera;
	}
	public void setCamera(VideoCapture camera) {
		this.camera = camera;
	}
	
	
	private Mat frame;
	public Mat getFrame() {
		return frame;
	}
	public void setFrame(Mat frame) {
		this.frame = frame;
	}
	
	private Utility utility;
	public Utility getUtility() {
		return utility;
	}
	public void setUtility(Utility utility) {
		this.utility = utility;
	}
	
	//COSTRUTTORE
	public GestoreAcquisizioni(Utility ut) {		
		this.frame=new Mat();		
		this.setUtility(ut);
	}
		
	
	//metodo per convertire il frame in bufferedimage
	public BufferedImage matToBufferedImage(Mat matrix)
	{
		BufferedImage bimg = new BufferedImage(matrix.width(), matrix.height(), BufferedImage.TYPE_INT_RGB);
	    if ( matrix != null ) { 
	        int cols = matrix.cols();  
	        int rows = matrix.rows();  
	        int elemSize = (int)matrix.elemSize();  
	        byte[] data = new byte[cols * rows * elemSize];  
	        int type;  
	        matrix.get(0, 0, data);  
	        switch (matrix.channels()) {  
	        case 1:  
	            type = BufferedImage.TYPE_BYTE_GRAY;  
	            break;  
	        case 3:  
	            type = BufferedImage.TYPE_3BYTE_BGR;  
	            // bgr to rgb  
	            byte b;  
	            for(int i=0; i<data.length; i=i+3) {  
	                b = data[i];  
	                data[i] = data[i+2];  
	                data[i+2] = b;  
	            }  
	            break;  
	        default:  
	            return null;  
	        }  		        
	        // Reuse existing BufferedImage if possible
	        if (bimg == null || bimg.getWidth() != cols || bimg.getHeight() != rows || bimg.getType() != type) {
	            bimg = new BufferedImage(cols, rows, type);
	        }        
	        bimg.getRaster().setDataElements(0, 0, cols, rows, data);
	    }
	    return bimg;  
	}  	
	    
	//metodo che effettua la conversione dei colori del frame in scala di grigi
	public BufferedImage convertiInGrigi() {
		Mat grigi=new Mat();
		Imgproc.cvtColor(this.frame, grigi, Imgproc.COLOR_RGB2GRAY);
		return matToBufferedImage(grigi);
	}
	
	//metodo che imposta la dimensione del frame a 640x480
	public void resizeFrame() {
		Size s=new Size(640,480);
		Imgproc.resize(getFrame(), getFrame(), s);
	}
	
	//metodo che effettua il salvataggio del frame su disco
	public void saveMatImage() {
    	try {   //recupero la cartella in cui salvare l'immagine
    			String dir=utility.getDirPL();
    			Highgui.imwrite(dir+"pl.png", this.frame);    			
    	}
    	catch (Exception e) {
    		e.printStackTrace();    		 
        	utility.eccezione(e.getMessage());
		}
    	
    }
	
}
