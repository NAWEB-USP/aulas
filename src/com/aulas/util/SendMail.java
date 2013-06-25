package com.aulas.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
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

/**
 * 
 * Classe utilit�ria de envio de e-mails.
 *  
 */

public class SendMail {

	private String mailHost = "smtp.ime.usp.br";

	private String mailUser = "";

	private String password = "";
	
	private String mailPort = "25";

	private String messageText;

	private String messageSubject;

	private String nameFrom;

	private String addressFrom;

	private ArrayList addressTo = new ArrayList();

	private ArrayList addressCcTo = new ArrayList();

	private ArrayList addressBccTo = new ArrayList();

	private ArrayList addressReplyTo = new ArrayList();

	//private static Logger logger = Logger.getLogger(SimpleEMailSender.class);

	public SendMail() {

	}

	// M�todos Get's e Set's

	public void addBCC(String nome, String email) throws Exception {
		Address address = new InternetAddress(email, nome);
		this.addressBccTo.add(address);
	}

	public void addCC(String nome, String email) throws Exception {
		Address address = new InternetAddress(email, nome);
		this.addressCcTo.add(address);
	}

	public void addTo(String nome, String email) throws Exception {
		Address address = new InternetAddress(email, nome);
		this.addressTo.add(address);
	}

	public void addReplyTo(String nome, String email) throws Exception {
		Address address = new InternetAddress(email, nome);
		this.addressReplyTo.add(address);
	}

	public String getMailHost() {
		return this.mailHost;
	}

	/**
	 * @param mailHost
	 *            The mailHost to set.
	 */
	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	/**
	 * @return Returns the mailUser.
	 */
	public String getMailUser() {
		return this.mailUser;
	}

	/**
	 * @param mailUser
	 *            The mailUser to set.
	 */
	public void setMailUser(String mailUser) {
		this.mailUser = mailUser;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return Returns the messageSubject.
	 */
	public String getMessageSubject() {
		return this.messageSubject;
	}

	/**
	 * @param messageSubject
	 *            The messageSubject to set.
	 */
	public void setMessageSubject(String messageSubject) {
		this.messageSubject = messageSubject;
	}

	/**
	 * @return Returns the messageText.
	 */
	public String getMessageText() {
		return this.messageText;
	}

	/**
	 * @param messageText
	 *            The messageText to set.
	 */
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	/**
	 * @return Returns the nameFrom.
	 */
	public String getNameFrom() {
		return this.nameFrom;
	}

	/**
	 * @param nameFrom
	 *            The nameFrom to set.
	 */
	public void setNameFrom(String nameFrom) {
		this.nameFrom = nameFrom;
	}

	/**
	 * @return Returns the addressFrom.
	 */
	public String getAddressFrom() {
		return this.addressFrom;
	}

	/**
	 * @param addressFrom
	 *            The addressFrom to set.
	 */
	public void setAddressFrom(String addressFrom) {
		this.addressFrom = addressFrom;
	}

	// Fim dos m�todos Get's e Set's

	/**
	 * 
	 * Envia o e-mail.
	 * 
	 * 
	 * 
	 * @throws MessagingException
	 * 
	 * @throws UnsupportedEncodingException
	 *  
	 */

	public final void send() throws MessagingException,
	UnsupportedEncodingException {

		Session mailSession = getMailSession();

		// Criando a mensagem
		MimeMessage msg = new MimeMessage(mailSession);

		// Inserindo os dados na mensagem
		
		//Multipart parts = new MimeMultipart () ; 
		//MimeBodyPart mainBody = new MimeBodyPart ();
		//mainBody.setText(messageText);
		//parts.addBodyPart(mainBody);
		//msg.setContent(parts);
		msg.setText(messageText);
		
		msg.setSubject(messageSubject);

		//Dados do remetente

		Address fromAddress = new InternetAddress(addressFrom, nameFrom);
		msg.setFrom(fromAddress);

		//Dados do destinat�rio
		Iterator emails = addressTo.iterator();
		while (emails.hasNext()) {
			Address email = (Address) emails.next();
			msg.addRecipient(Message.RecipientType.TO, email);
		}

		emails = addressCcTo.iterator();
		while (emails.hasNext()) {
			Address email = (Address) emails.next();
			msg.addRecipient(Message.RecipientType.CC, email);
		}

		emails = addressBccTo.iterator();
		while (emails.hasNext()) {
			Address email = (Address) emails.next();
			msg.addRecipient(Message.RecipientType.BCC, email);
		}

		if (addressReplyTo.size() != 0) {
			Address[] type = {};
			msg.setReplyTo((Address[]) addressReplyTo.toArray(type));
		}

		//Enviando a mensagem

		//logger.debug("Enviando e-mail para " + toAddress + "...");

		Transport.send(msg);

		//logger.debug("E-mail para " + toAddress + " enviado.");

	}

	/**
	 * 
	 * Retorna a sessão de e-mail.
	 * 
	 * 
	 * 
	 * @return sessão de e-mail.
	 *  
	 */

	private Session getMailSession() {

		Session mailSession;

		// Throw exception if mail host or user are not set

		if (mailHost == null || mailUser == null) {
			throw new IllegalStateException(
					"Parametros mailHost e mailUser não foram configurados.");
		}

		// Create mail session if it doesn't exist
		Properties p = new Properties();

		p.setProperty("mail.host", this.mailHost);
		p.setProperty("mail.user", this.mailUser);
		if (this.mailPort != null) {
			p.put("mail.smtp.port", this.mailPort);
		}
		Authenticator auth = null;
		if (this.password != null) {
			p.setProperty("mail.smtp.auth", "true");
			auth = new SMTPAuthenticator(this.mailUser, this.password);   
		}

		mailSession = Session.getDefaultInstance(p, auth);

		return mailSession;

	}

	/**
	 * @return Returns the mailPort.
	 */
	public String getMailPort() {
		return this.mailPort;
	}

	/**
	 * @param mailPort The mailPort to set.
	 */
	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}

	private class SMTPAuthenticator extends javax.mail.Authenticator {   

		private String username;   
		private String password;   

		SMTPAuthenticator(String username, String password) {   
			this.username = username;   
			this.password = password;   
		}   

		public PasswordAuthentication getPasswordAuthentication() {   
			return new PasswordAuthentication(username, password);   
		}   
	}  

}

