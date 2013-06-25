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
 
public class TabelaComBorda {
	private PrintWriter out;
	private int numColunas = 0;
	private int colAtual = 0;

public TabelaComBorda(PaginaHTML pagina) throws Exception {

	this.out = pagina.out;

	out.print ("<table cellpadding=1 cellspacing=0 border=1 bordercolor=black>");
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
	
	out.println("<td valign=top><span class=MenuTexto2>"+texto+"</span></td>");

	colAtual++;
}

public void addCelula(String texto, String bgColor) throws Exception {

	if (colAtual == numColunas) {
		colAtual = 0;
		out.print ("<TR>");
	}

	if ((texto == null) || (texto.equals(""))) {
		texto = " ";
	}
	
	out.println("<td valign=top bgcolor="+bgColor+"><span class=MenuTexto2>"+texto+"</span></td>");

	colAtual++;
}

public void addCelulaNOWRAP(String texto) throws Exception {

	if (colAtual == numColunas) {
		colAtual = 0;
		out.print ("<TR>");
	}

	if ((texto == null) || (texto.equals(""))) {
		texto = " ";
	}
	
	out.println("<td valign=top nowrap><span class=MenuTexto2>"+texto+"</span></td>");

	colAtual++;
}

public void addCelulaWithColspan(String texto, int colspan) throws Exception {

	if (colAtual == numColunas) {
		colAtual = 0;
		out.print ("<TR>");
	}

	if ((texto == null) || (texto.equals(""))) {
		texto = " ";
	}
	
	out.println("<td valign=top colspan="+colspan+"><span class=MenuTexto2>"+texto+"</span></td>");

	colAtual += colspan;
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
