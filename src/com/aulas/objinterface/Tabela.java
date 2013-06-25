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
 
public class Tabela {
	private PrintWriter out;
	private int numColunas = 0;
	private int colAtual = 0;

public Tabela(PaginaHTML pagina) throws Exception {

	this.out = pagina.out;

	out.print ("<table width=95% cellpadding=5 cellspacing=0>");
	out.print ("<tr class=MenuTitulo>");

}

public void addCelula(String texto) throws Exception {

	if (colAtual == numColunas) {
		colAtual = 0;
		out.print ("<TR>");
	}

	if ((texto == null) || (texto.equals(""))) {
		texto = " ";
	}
	
	out.println("<td valign=top><span class=MenuItem>"+texto+"</span></td>");

	colAtual++;
}

public void addTitulo(String texto) throws Exception {

	numColunas++;
	colAtual++;

	out.println("<td><span class=MenuTitulo>"+texto+"</span></td>");

}

public void addTitulo(String texto, String size) throws Exception {

	numColunas++;
	colAtual++;

	out.println("<td width="+size+"><span class=MenuTitulo>"+texto+"</span></td>");

}

public void fim() throws Exception {

	out.println ("</tr></table>");

}
}
