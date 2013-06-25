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
 
public class ListaItens {
	private PrintWriter out;

public ListaItens(PaginaHTML pagina, String titulo) throws Exception {

	this.out = pagina.out;

	if (!titulo.equals("")) {
		out.print ("<table class=MenuTitulo width=95% cellpadding=5 cellspacing=0>");
		out.print ("<tr><td><span class=MenuTitulo>"+titulo+"</span></td></tr></table><BR>");
	}

	out.println("	<TABLE border=0 cellspacing=0 cellpadding=0 width=95%>");

	
}

public void addItem(String texto, String link, String opcoes) throws Exception {

	out.println("	<TR><TD>");
	out.println("	<span class=MenuItem><a href=\""+link+"\"><LI>"+texto+"</a></span>");
	out.println("	</TD><TD align=right nowrap>");
	out.println("	<span class=MenuItem>"+opcoes+"</span></TD></TR>");

}

public void fim() throws Exception {

	out.println("	</table>");

}
}
