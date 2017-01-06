/*Utility.java
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

package it.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Properties;


public class Utility {
	
	//variabile che è true se si è premuto il bottone di prova
	private boolean provaInvioGui;
	public boolean isProvaInvioGui() {
		return provaInvioGui;
	}
	public void setProvaInvioGui(boolean provaInvioGui) {
		this.provaInvioGui = provaInvioGui;
	}
	
	public Utility() {
		leggiProperties();
	}
	
	private GregorianCalendar gc=new GregorianCalendar();
	private SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private Properties prop;
	
	
	public Properties getProp() {
		return prop;
	}
	public void setProp(Properties prop) {
		this.prop = prop;
	}
	
	//metodo che restituisce il timestamp di sistema sotto il formato dd/MM/yyyy HH:mm:ss
	@SuppressWarnings("static-access")
	public String getTimestamp() {
		return sdf.format(gc.getInstance().getTime());
	}
	//metodo che legge il file di properties contenente le configurazioni del programma
	private void leggiProperties() {
		try{			
			prop=new Properties();
			FileInputStream p=new FileInputStream("resources/pl.properties");
			prop.load(p);    	            	    					
		}catch(Exception ex){			
			ex.printStackTrace();				
			eccezione(ex.getMessage());			
		}
	}
	
	//metodo che restituisce true se i valori destinatario,mittente e password
	//sono stati settati nel file properties
	public boolean controllaValori(){
		//leggo i valori dal file di properties
		String destinatario=getProp().getProperty("destinatario");
		String mittente=getProp().getProperty("mittente");
		String password=getProp().getProperty("password");		
		//verifico se i valori sono stati settati
		if(   destinatario.equals("") || destinatario.isEmpty()
		   || mittente.equals("") || mittente.isEmpty()
		   || password.equals("") || password.isEmpty()
			)
				return false;
		//se tutto è settato restituisco true
		return true;
	}
	//metodo per salvare le modifiche al file di properties
	public void salvaProp() {
		try {
			getProp().store(
					new FileOutputStream(
							new File("resources/pl.properties")), getProp().getProperty("cambioDest"));
		} catch (IOException e) {			
			e.printStackTrace();
			eccezione(e.getMessage());
		}
	}
	
	//metodo da eseguire in caso di catch
	public void eccezione(String ex) {
		System.out.println("Errore: "+getTimestamp()+"\n"+ex);						
		System.exit(0);					
	}
	//metodo che restituisce il path della cartella contenente tutti i file per le info del sistema
	public String getDirPL() {
		String path=getProp().getProperty("dirSis");		
		//ottengo il path in base al sistema operativo
		if(path.isEmpty() || path.equals("")) {
			//se il path non è stato settato prendo quello della directory home
			path=System.getProperty("user.home");
		}
		path+="/";
		//estraggo la directory del programma dal properties
		path+=getProp().getProperty("dirPL")+"/";
		//controllo l'esistenza della directory
		File f=new File(path);
		if(!f.exists()) {
			//se non esiste la creo
			f.mkdir();
		}
		return path;
	}
	//metodo che setta un file per la scrittura dei log su standard output
	public boolean outputSuFile() {			
		try {		
			String path=getDirPL();
			//estraggo il nome del file di Log dal properties
			path+=getProp().getProperty("fileLog");
			//controllo l'esistenza del file
			File f=new File(path);
			if(!f.exists()) {
				//se non esiste lo creo
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(f,true);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);	
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	//metodo che verifica la connessione a internet
	public boolean verificaConnessione() {		
		try {
			URL url;
			url = new URL("http://www.google.com");
		    URLConnection connection=url.openConnection();			            
		    connection.connect();
            return true;
		} catch (MalformedURLException e) { 
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	//metodo che restituisce true se la mail inserita è corretta sintatticamente
	public boolean verificaMail(String mail) {		
		mail.trim();
		//controllo se la mail non è vuota
		if(mail.isEmpty())
			return false;
		//controllo se è presente almeno un carattere di . e una @
		if(mail.indexOf(".")>-1 && mail.indexOf("@")>-1) 
			return true;
		return false;
	}
}
