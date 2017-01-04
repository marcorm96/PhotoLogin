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
