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
 
public class Menu {
	private PrintWriter out;

public Menu(PaginaHTML pagina, String titulo) throws Exception {

	this.out = pagina.out;

	if (!titulo.equals("")) {
		out.print ("<div class=MenuTitulo>"+titulo+"</div><BR>");
	}

	//out.print ("<UL>");
}

public void addItem(String texto, String link) throws Exception {

	out.println("<span class=MenuItem><a href=\""+link+"\"><li>"+texto+"</a><BR></span>");

}

public void addItem(String texto, String link, String target) throws Exception {

	out.println("<span class=MenuItem><a href=\""+link+"\" target="+target+"><li>"+texto+"</a><BR></span>");

}

public void fim() throws Exception {

	//out.println ("</UL>");

}
}
