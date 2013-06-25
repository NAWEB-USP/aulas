package com.aulas.modelos;

/* ----------------------------------------------- *
 *				Marco Aurélio Gerosa               *
 * ----------------------------------------------- *

 Histórico de Versões
 --------------------
 versão	Autor		Modificação
 ------	-----		-----------
 1.0	Gerosa 		Importação e adaptação da classe
 * 
 */

import com.aulas.util.*;
import com.aulas.gerinterface.*;
import com.aulas.util.sessionmanager.*;
 
import java.util.*;
import java.io.*;
 
public abstract class Servlet extends javax.servlet.http.HttpServlet {
	
public static String instalationPath = null;

public abstract void action(javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception;
/**
 * Process incoming HTTP GET requests 
 * 
 * @param request Object that encapsulates the request to the servlet 
 * @param response Object that encapsulates the response from the servlet
 */
public void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {

	performTask(request, response);

}
/**
 * Process incoming HTTP POST requests 
 * 
 * @param request Object that encapsulates the request to the servlet 
 * @param response Object that encapsulates the response from the servlet
 */
public void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {

	performTask(request, response);

}
/**
 * Returns the servlet info string.
 */
public String getServletInfo() {

	return super.getServletInfo();

}
/**
 * Initializes the servlet.
 */
public void init() {
	// insert code to initialize the servlet here

}
/**
 * Process incoming requests for information
 * 
 * @param request Object that encapsulates the request to the servlet 
 * @param response Object that encapsulates the response from the servlet
 */
 
public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse res) {

	PrintWriter out = null;
	
	try	{
		if (instalationPath == null) instalationPath = this.getServletContext().getRealPath("/");

		res.setContentType("text/html");
		res.setCharacterEncoding("ISO-8859-1");
		
		// get the communication channel with the requesting client
		out = res.getWriter();
		
		action (request, out);

		out.close();

	}
	// --- Tratamento de Exce��es ---
	catch (SessionException ex) {
		System.out.println("Site de aulas - Exceção "+ex.getMessage());
        ex.printStackTrace();
        try {
			ExceptionHandler.sessionException(out);
			out.close();
		} catch (Exception ex1) {
			Erro.trataExcecao(ex1,out,request,null);
			out.close();
		}
	}
	catch (com.aulas.util.sessionmanager.SecurityException ex) {
        System.out.println("Site de aulas - Exceção "+ex.getMessage());
        ex.printStackTrace();
		try {
			ExceptionHandler.securityException(out);
			out.close();
		} catch (Exception ex1) {
			Erro.trataExcecao(ex1,out,request,null);
			out.close();
		}
	}
	catch (Exception ex) {
        System.out.println("Site de aulas - Exceção "+ex.getMessage());
        ex.printStackTrace();
		SessionManager sessionManager = null;
		try {
			sessionManager = new SessionManager(request);
		} catch(Exception ex2) {};
		Erro.trataExcecao(ex,out,request,sessionManager);
		out.close();
	}
}
}
