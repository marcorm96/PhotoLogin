/*ThreadInvioMail.java
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

package it.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

import it.util.Utility;

//Thread che effettua l'invio della mail

public class ThreadInvioMail extends Thread implements Runnable {
	
	Utility ut;
	
	public ThreadInvioMail (Utility ut) {
		this.ut=ut;
		
	}
	
	//metodo che effettua l'invio vero e proprio
	private boolean invio() {
		String mittente=ut.getProp().getProperty("mittente");
		String pwd=ut.getProp().getProperty("password");
		//creazione della sessione
		Session session=Session.getInstance(ut.getProp(),new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mittente, pwd);
			}
		  });

		try {
			//Compilazione del messaggio
			Message msg=new MimeMessage(session);						
			msg.setFrom(new InternetAddress(mittente,ut.getProp().getProperty("nomeMitt")));			
			InternetAddress[] address ={new InternetAddress(ut.getProp().getProperty("destinatario"))};
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(ut.getProp().getProperty("ogg"));
			msg.setSentDate(new Date());
            //Creo il message part 
	        BodyPart messageBodyPart = new MimeBodyPart();
	        //Corpo del messaggio recuperato dal file di properties
	        messageBodyPart.setText(ut.getTimestamp()+" "+ut.getProp().getProperty("msg"));	         
	        //Multipart
	        Multipart multipart = new MimeMultipart();
	        //Aggiungo il part del testo al multipart
	        multipart.addBodyPart(messageBodyPart);
	        //Gestisco l'allegato
	        messageBodyPart = new MimeBodyPart();
	        String filename = ut.getDirPL()+"pl.png";
	        File allegato=new File(filename);	        
	        DataSource source = new FileDataSource(allegato);
	        messageBodyPart.setDataHandler(new DataHandler(source));
	        messageBodyPart.setFileName(allegato.getName());
	        
	        multipart.addBodyPart(messageBodyPart);
	        //Imposto il multipart come contenuto del messaggio
	        msg.setContent(multipart );
	        //Invio il messaggio
	        Transport.send(msg);
			//Elimino l'immagine dal disco
	        allegato.delete();
	        
			return true;
		}
		catch (MessagingException ex) {
			ex.printStackTrace();
			ut.eccezione(ex.getMessage());
		}catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
			ut.eccezione(e.getMessage());			
		}
		return false;
	}

	public void run() {
		try {
				boolean connOK;
				do {
						connOK=ut.verificaConnessione();						
						if(connOK)
							//Se il pc è connesso ad internet invio il messaggio
							invio();
						else {				
								if(ut.isProvaInvioGui())
									JOptionPane.showMessageDialog(null, "Attendo la connessione");
								System.out.println("Attendo la connessione");
								Thread.sleep(10000);
						}							
				}while(!connOK);
				System.out.println(ut.getTimestamp()+" Mail inviata");
				if(ut.isProvaInvioGui()) {
					JOptionPane.showMessageDialog(null, "Mail inviata");
				}
		} catch (InterruptedException e) { 
			e.printStackTrace();
			if(ut.isProvaInvioGui()) {
				JOptionPane.showMessageDialog(null, "Errore nell'invio della mail:"+e.getMessage());
			}
			ut.eccezione(e.getMessage());			
		}
	}
}
