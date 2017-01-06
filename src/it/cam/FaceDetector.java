/*FaceDetector.java
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

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;

//Classe contenente tutto ciò che serve per rilevare la presenza dei volti nei frame

public class FaceDetector {

    CascadeClassifier faceDetector;
    MatOfRect faceDetections;
    
    public FaceDetector() {
    	//carico il cascade per la rilevazione dei volti
    	faceDetector=new CascadeClassifier("resources/haarcascade_frontalface_alt.xml");
    	faceDetections=new MatOfRect();
    }
    
    
	public boolean trovaFacce(Mat frame) {
		//applico la ricerca dei volti sul frame
        faceDetector.detectMultiScale(frame, faceDetections);
        if(faceDetections.toArray().length>0) {
        	//se trovo almeno un volto restituisco true
        	System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        	return true;
        }
        return false;
        
	}
	
}
