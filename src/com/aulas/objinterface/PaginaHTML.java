package com.aulas.objinterface;

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
 
import java.util.*;
import java.io.*;
import com.aulas.util.sessionmanager.*;
 
public class PaginaHTML {
	
	protected PrintWriter out;
	private String titulo;
	private SessionManager sessionManager;

public PaginaHTML(SessionManager sessionManager, PrintWriter out, String titulo) throws Exception {

	this.sessionManager = sessionManager;
	this.out = out;
	this.titulo = titulo;
}
public void descricao(String texto) {
	out.println("<span class=Descricao>"+texto+"<BR></span>");
}

public void print(String texto) {
	out.println(texto);
}

public void fim() throws Exception {

	out.println("</body>");
	out.println("</HTML>");
	
}

public SessionManager getSessionManager() {
	
	return this.sessionManager;
		
}
public void init() throws Exception {
	out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
	out.println("<HTML>");
	out.println("<head>");
	out.println("<title>Site de Avaliações - "+this.titulo+"</title>");
	out.println("<META HTTP-EQUIV=\"Expires\" CONTENT=\"Mon, 06 Jan 1990 00:00:01 GMT\">"); // não fazer cache
	out.println("<meta http-equiv=\"Pragma\" content=\"no-cache\">");
	out.println("<LINK REL=STYLESHEET TYPE=text/css HREF=\"/aulas/aulas.css\">");
	out.println("</head>");

	out.println("<body class=AreaConteudoPagina>");

}
public void javascript(String texto) {

	out.println("\n<script language=javascript>\n"+texto+"\n</script>\n");

}
public void rodape (String texto) {

	saltaLinha();
	saltaLinha();
	saltaLinha();
	out.println("<CENTER><hr width=70%><span class=Rodape>"+texto+"<BR></span></CENTER>");

}
public void saltaLinha() {

	out.println("<BR>");

}
public void titulo(String texto) {

	out.println("<span class=Titulo>"+texto+"<BR><BR></span>");

}
}
