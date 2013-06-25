package com.aulas.util;

public class SendMailThread extends Thread {
	
	private SendMail sendMail;
	
	public void setSendMail (SendMail sendMail) {
		this.sendMail = sendMail;
	}
	
	public void run() {
		try {
			sendMail.send();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

}
