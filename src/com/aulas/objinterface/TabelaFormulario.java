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
 
public class TabelaFormulario {
	private PrintWriter out;
	private int coluna = 1;
	private int quantidadeDeColunas;

public TabelaFormulario(PaginaHTML pagina) throws Exception {

	this.out = pagina.out;
	this.quantidadeDeColunas = 2;

	out.print ("<table class=AreaConteudoFormularioFundo border=0 cellpadding=5 cellspacing=1>");
	out.print ("<tr valign=top>");

}
public TabelaFormulario(PaginaHTML pagina, int quantidadeDeColunas) throws Exception {

	this.out = pagina.out;

	this.quantidadeDeColunas = quantidadeDeColunas;
	
	out.print ("<table class=AreaConteudoFormularioFundo border=0 cellpadding=5 cellspacing=1>");
	out.print ("<tr valign=top>");

}
public void addCelula(String texto) throws Exception {

	if ((texto == null) || (texto.equals(""))) {
		texto = " ";
	}

	if (coluna == 1) out.println("<TR>");
	
	out.println("<td valign=top><span class=AreaConteudoFormularioTexto>"+texto+"</span></td>");

	coluna++;
	if (coluna == quantidadeDeColunas+1) {
		coluna = 1;
	}

}
public void addCelulaCom2Botes(String botao1, String botao2) throws Exception {

	out.println("<TR><td valign=top colspan="+quantidadeDeColunas+"><span class=AreaConteudoFormularioTexto><BR><CENTER>"+botao1+" &nbsp; "+ botao2 +"</CENTER></TD></TR>");

}
public void addCelulaCom3Botes(String botao1, String botao2, String botao3) throws Exception {

	out.println("<TR><td valign=top colspan="+quantidadeDeColunas+"><span class=AreaConteudoFormularioTexto><BR><CENTER>"+botao1+" &nbsp; "+botao2+" &nbsp; "+ botao3+"</CENTER></TD></TR>");

}
public void addCelulaNOWRAP(String texto) throws Exception {

	if ((texto == null) || (texto.equals(""))) {
		texto = " ";
	}
	
	out.println("<td valign=top nowrap><span class=AreaConteudoFormularioTexto>"+texto+"</span></td>");

}
public void addCelulaWithColSpan(String texto, int colSpan) throws Exception {

	if ((texto == null) || (texto.equals(""))) {
		texto = " ";
	}

	if (coluna == 1) out.println("<TR>");
	
	out.println("<td valign=top colspan="+colSpan+"><span class=AreaConteudoFormularioTexto>"+texto+"</span></td>");

	coluna += colSpan;
	if (coluna >= quantidadeDeColunas+1) {
		coluna = 1;
	}

}
public void fim() throws Exception {

	out.println ("</tr></table>");

}
public void novaLinha() throws Exception {

	out.print ("</TR><TR>");

}
}
