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
 
public class TabelaSimples {
	private PrintWriter out;
	private int coluna = 1;

public TabelaSimples(PaginaHTML pagina) throws Exception {

	this.out = pagina.out;

	out.print ("<table class=AreaConteudoFormularioFundo border=0 cellpadding=2 cellspacing=1>");
	out.print ("<tr valign=top>");

}

public void addCelula(String texto) throws Exception {

	if ((texto == null) || (texto.equals(""))) {
		texto = " ";
	}

	if (coluna == 1) out.println("<TR>");
	
	out.println("<td valign=top>"+texto+"</td>");

	coluna++;
	if (coluna == 3) coluna = 1;

}

public void addCelulaNOWRAP(String texto) throws Exception {

	if ((texto == null) || (texto.equals(""))) {
		texto = " ";
	}
	
	out.println("<td valign=top nowrap>"+texto+"</td>");

}

public void addCelulaWithColSpan(String texto, int colSpan) throws Exception {

	if ((texto == null) || (texto.equals(""))) {
		texto = " ";
	}
	
	out.println("<td valign=top colspan="+colSpan+">"+texto+"</td>");

}

public void fim() throws Exception {

	out.println ("</tr></table>");

}

public void novaLinha() throws Exception {

	out.print ("</TR><TR>");

}
}
