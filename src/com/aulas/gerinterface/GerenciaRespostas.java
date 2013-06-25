package com.aulas.gerinterface;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 12/02/2002 11:48:56 -- *
 * -- Gerador versão 1.0                                       *
 * ----------------------------------------------------------- *

 Histórico de Versões
 --------------------
 versão	Autor		Modificação
 ------	-----		-----------
 1.0	Gerosa 		Geração automática da classe
 * 
 */



import com.aulas.modelos.*;
import com.aulas.util.*;
import com.aulas.util.sessionmanager.*;
import com.aulas.business.*;
import com.aulas.business.questoes.*;
import com.aulas.objinterface.*;

import java.sql.*;
import java.util.*;
import java.io.*;

public class GerenciaRespostas extends Servlet {

	
public void action(javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	// Variaveis comuns
	Questao questao = null;
	
	// Inicializa SessionManager
	SessionManager sessionManager = new SessionManager(request);

	// Somente professor tem acesso
	sessionManager.checaPermissaoProfessor();

	String codQuestao = (String) request.getParameter("codQuestao");
	// Le Questao
	String idLista = sessionManager.getElement("idLista");
	if (codQuestao != null) {
		Questao_ger questger = new Questao_ger();
		questao = questger.getElementByCod(codQuestao);
	}
	
	String toDo = (String) request.getParameter("toDo");

	if (codQuestao == null) {
		mensagemEscolherQuestao(sessionManager,out);
		
	} else if ((toDo == null) || (toDo.equals("")) || (toDo.equals("listaRespostas"))) {
		listaRespostas (sessionManager,questao,out);

	} else if (toDo.equals("cadastroResposta")) {
		String idResposta = (String) request.getParameter("codResposta");
		Vector v = null;
		if ((idResposta != null) && (!idResposta.equals(""))) {
			Resposta_ger ger = new Resposta_ger();
			v = new Vector();
			v.addElement(ger.getElementByCod(idResposta));
		}
		cadastroResposta(sessionManager,questao,out,v);

	} else if (toDo.equals("corrigeTodasResposta")) {
		String idResposta = (String) request.getParameter("corrigeTodasResposta");
		Resposta_ger ger = new Resposta_ger();
		cadastroResposta(sessionManager,questao,out,ger.getElements(questao));

	} else if (toDo.equals("corrigeNovasResposta")) {
		String idResposta = (String) request.getParameter("corrigeNovasResposta");
		Resposta_ger ger = new Resposta_ger();
		cadastroResposta(sessionManager,questao,out,ger.getRespostasNovasByQuestao(questao));

	} else if (toDo.equals("efetuaCadastro")) {
		efetuaCadastro(sessionManager,questao,request,out);

	} else if (toDo.equals("removerResposta")) {
		String idResposta = (String) request.getParameter("idResposta");
		if ((idResposta == null) || (idResposta.equals(""))) {throw new Exception ("Falta o cod");}
		Resposta_ger ger = new Resposta_ger();
		Resposta obj = ger.getElementByCod(idResposta);
		removerResposta (sessionManager,out, obj);

	} else {
		throw new Exception ("Opção não reconhecida.");
	}

}
private void cadastroResposta(SessionManager sessionManager, Questao questao, PrintWriter out, Vector respostas) throws Exception {

	boolean criacao = (respostas == null);

	String titulo = criacao ? "Criação de Resposta" : "Alteração de Resposta";

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,titulo);
	pagina.init();

	pagina.titulo (titulo);
	pagina.descricao("Preencha os dados nos campos abaixo:");

	Formulario form = new Formulario(pagina,"GerenciaRespostas?codQuestao="+questao.getCod()+"&"+sessionManager.generateEncodedParameter());
	form.hidden("toDo","efetuaCadastro");
	
	if (criacao) form.hidden("criacao","true"); 

	Vector comentariosPadroes = new Vector();
	Vector pontuacoesComents = new Vector();
	Resposta_ger respger = new Resposta_ger();
	respger.preencheVetoresComentarios(comentariosPadroes,pontuacoesComents,questao);

	out.println("<SCRIPT LANGUAGE=JavaScript>");
	out.println("var coments = new Array();");
	out.println("var comentsPontos = new Array();");
	out.println("coments[0] = ''");
	out.println("comentsPontos[0] = 100");
	for (int i = 0; i < comentariosPadroes.size(); i++) {
		String comentario = (String) comentariosPadroes.elementAt(i);
		double nota = Double.parseDouble(pontuacoesComents.elementAt(i).toString());
		out.println("coments["+(i-1)+"] = '"+StringConverter.toJavaScriptNotation(comentario)+" ("+nota+"%)'");
		out.println("comentsPontos["+(i-1)+"] = "+nota);
	}

	out.println("function atualizaSelectBox (selectBox) {");
	out.println("	for (var i=0; i<coments.length; i++) {");
	out.println("		var option = new Option (coments[i],comentsPontos[i]);");
	out.println("		selectBox.options[i] = option;");
	out.println("	}");
	out.println("}");
	out.println("function atualizaCampos (selectBox,campoComentario,campoNota) {");
	out.println("	if (selectBox.selectedIndex != -1) {");
	out.println("		campoComentario.value = limpaPontuacaoFromTexto(selectBox.options[selectBox.selectedIndex].text);");
	out.println("		campoNota.value = selectBox.options[selectBox.selectedIndex].value;");
	out.println("	}");
	out.println("}");
	out.println("function novoComentario(comentario, pontuacao) {");
	out.println("	coments[coments.length] = comentario;");
	out.println("	comentsPontos[comentsPontos.length] = pontuacao;");
	out.println("}");
	out.println("function limpaPontuacaoFromTexto(texto) {");
	out.println("	if ((texto.lastIndexOf('%') > texto.lastIndexOf('(')) && (texto.indexOf('(') != -1)) {");
	out.println("		return texto.substring(0,texto.lastIndexOf('('));");
	out.println("	} else {");
	out.println("		return texto;");
	out.println("	}");
	out.println("}");

	out.println("</SCRIPT>");
	
	
	TabelaFormulario tab = new TabelaFormulario (pagina);

		tab.addCelula("<B>Tipo questão: </B>");
		tab.addCelula(questao.getTipo());
		tab.addCelula("<B>Enunciado questão: </B>");
		tab.addCelula(questao.getEnunciado());
		tab.addCelula("<B>Crit�rios questão: </B>");
		tab.addCelula(form.textArea("criterios", questao.getCriterios(), 2, 60, "", "lightblue"));

		for (int i = 0; ((criacao && (i==0)) || ((!criacao) && (i < respostas.size()))); i++) {
			Resposta obj = criacao?null:(Resposta) respostas.elementAt(i);

			if (i != 0) {
				tab.addCelulaWithColSpan("<CENTER><HR size=2 color=black width=100%></CENTER>",2);
			}
			
			tab.addCelula("<B>Resposta: </B>");
			tab.addCelula(form.textArea("resposta"+(criacao?"":obj.getCod()),(criacao?"":obj.getResposta()), 4, 60));

			// -*- Se for Upload de arquivo -*-
			if (!criacao && (questao.getTipo() == QuestaoSubmissaoArquivo.getIdTipoQuestao())) {
				tab.addCelula("<B>Visualizar: </B>");
				String resposta = obj.getResposta();
				if (!resposta.equals("")) {
					String fileName = resposta;
				    if (fileName.lastIndexOf(File.separator) != -1) {
						fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1);
				    }
					File arq = new File (resposta);
					String infoSize = "";
					if ((arq != null) && arq.exists()) {
						long size = arq.length();
						if (size / (1024*1024) >= 1) { // MB
							infoSize = " &nbsp;("+size / (1024*1024)+" MB)";
						} else if (size / (1024) >= 1) { // KB
							infoSize = " &nbsp;("+size / (1024)+" KB)";
						} else { // B
							infoSize = " &nbsp;("+size+" Bytes)";
						}
					}
					tab.addCelula("<A HREF=\"LeArquivo?"+sessionManager.generateEncodedParameter()+"&resposta="+obj.getCod()+"\">"+fileName+"</A>"+infoSize);
				}
			}
			// -*- Se for SQL -*-
			if (!criacao && (questao.getTipo() == QuestaoSQL.getIdTipoQuestao())) {
				try {
					Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
					Connection dbCon = DriverManager.getConnection("jdbc:odbc:SQL", "", "");
					Statement dbStmt = dbCon.createStatement();
					String sql = obj.getResposta();
					tab.addCelula("<B>Execução: </b>");
					if (!sql.toUpperCase().startsWith("SELECT")) {
						tab.addCelula("<B><FONT color=red>não se inicia por SELECT!!!</font></b>");
					} else {
						sql = StringConverter.fromDataBaseNotation(sql);
					    sql = StringConverter.replace(sql,"\"","'");
						ResultSet rs = dbStmt.executeQuery(sql);
						int count = 0;
						StringBuffer resposta = new StringBuffer();
						resposta = resposta.append("<TABLE border=1 bordercolor=black cellspacing=0><TR>");
						// monta cabe�alho dos nomes dos campos
						int qtdeColunas = rs.getMetaData().getColumnCount();
						for (int j = 1; j <= qtdeColunas; j++) {
							resposta = resposta.append("<TD bgcolor=#AAAAAA><FONT size=1><B>"+rs.getMetaData().getColumnName(j)+"</B></FONT></TD>");
						}
						resposta = resposta.append("</TR>");
						// percore resultados
						while (rs.next()) {
							if (count <= 5) { // 5 primeiros resultados
								resposta = resposta.append("<TR>");
								for (int j = 1; j <= qtdeColunas; j++) {
									String valor = rs.getString(j);
									if (valor.length() > 18) { // se tiver mais do que 18 caracters
										valor = valor.substring(0,14) + " (...)"; // mostra apenas os primeiros 14
									}
									resposta = resposta.append("<TD><FONT size=1>"+valor+"</FONT></TD>");
								}
								resposta = resposta.append("</TR>");
							}
							count++;
						}
						resposta = resposta.append("</TABLE>");
						tab.addCelula(resposta+"<BR><B><FONT color=green>"+count+" row(s).</font></b>");
					}
				} catch (Exception ex) {
					tab.addCelula("<B><FONT color=red>Problema na execução: "+ex.getMessage()+"</font></b>");
				}
			}
			// -*- fim SQL -*-

			tab.addCelula("<B>Correções: </B>");
			tab.addCelula("<SELECT onFocus=\"atualizaSelectBox(this)\" onChange=\"atualizaCampos(this,comentario"+(criacao?"":obj.getCod())+",pontuacao"+(criacao?"":obj.getCod())+")\"><OPTION>Correções</SELECT>");
			
			tab.addCelula("<B>Pontuação: </B>");
			String txtPontuacao = form.textboxNumero("pontuacao"+(criacao?"":obj.getCod()),(criacao?"100":(obj.isNova()?"100":""+obj.getPontuacao())), 4, 4)+"%";
			txtPontuacao += " &nbsp; <a href=\"javascript://\" onclick=\"document.dados.pontuacao"+(criacao?"":obj.getCod())+".value=0\">0</a>";
			txtPontuacao += " &nbsp;<a href=\"javascript://\" onclick=\"document.dados.pontuacao"+(criacao?"":obj.getCod())+".value=50\">50</a>";
			txtPontuacao += " &nbsp;<a href=\"javascript://\" onclick=\"document.dados.pontuacao"+(criacao?"":obj.getCod())+".value=100\">100</a>";
			tab.addCelula("<span class=MenuItem>"+txtPontuacao+"</span>");

			tab.addCelula("<B>Comentário: </B>");
			tab.addCelula(form.textArea("comentario"+(criacao?"":obj.getCod()),(criacao?"":obj.getComentario()), 3, 50,"novoComentario(this.value,pontuacao"+(criacao?"":obj.getCod())+".value)"));

			if (!criacao) {
				// Hits
				tab.addCelula("<B>Hits: </B>");
				String strHits = Integer.toString(obj.getHits());
				if (obj.getHits() > 0) {
					strHits += " <B>[</B>";
					ListaGerada_ger listageradager = new ListaGerada_ger();
					Enumeration listasGeradas = listageradager.getElements(questao).elements();
					while (listasGeradas.hasMoreElements()) {
						ListaGerada listaGerada = (ListaGerada) listasGeradas.nextElement();
						int pos = listaGerada.getQuestoes().indexOf(questao);
						if (pos != -1) {
							Resposta resp = (Resposta) listaGerada.getQuestoesRespostas().elementAt(pos);
							/*String textoResposta = (String) listaGerada.getQuestoesTextosRespostas().elementAt(pos);
							if ((textoResposta == null) || (textoResposta.equals(""))) {
								textoResposta = (resp==null)?"":resp.getResposta();
							}
							if (questao.comparaRespostas(obj.getResposta(),textoResposta)) {
							*/
							if ((resp != null) && (resp.equals(obj))) {
								// resposta igual
								Enumeration alunos = listaGerada.getAlunos().elements();
								while (alunos.hasMoreElements()) {
									Aluno aluno = (Aluno) alunos.nextElement();
									strHits += aluno.getNome()+", ";
								}
								strHits = strHits.substring(0,strHits.length()-2); // tira a ultima virgula
								strHits += " ("+listaGerada.getTurma().getNome()+")<B>;</B> ";
							}
						}
					}
					strHits += "<B>]</B>";
				}
				tab.addCelula(strHits);
				
			}
		}

		tab.novaLinha();
		tab.addCelulaCom2Botes(form.botaoSubmit(criacao ? "Criar" : "Alterar"),form.button("Cancelar","window.close()"));

	tab.fim();
	form.fim();

	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();


}
private void efetuaCadastro(SessionManager sessionManager, Questao questao, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	String c = (String) request.getParameter("criacao");
	boolean criacao = ((c != null) && (c.equals("true")));

	Resposta_ger respger = new Resposta_ger();
	Vector respostas = respger.getElements(questao);

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,criacao ? "Criação de Resposta" : "Alteração de Resposta");
	pagina.init();
	
	String criterios = (String) request.getParameter("criterios");
	(new Questao_ger()).altera(questao, questao.getEnunciado(), criterios, questao.getTipo());
	
		
	for (int i = 0; ((criacao && (i==0)) || ((!criacao) && (i < respostas.size()))); i++) {
		Resposta obj = criacao?null:(Resposta) respostas.elementAt(i);
		
		// Le dados do formulario
		String resposta = (String) request.getParameter("resposta"+(criacao?"":obj.getCod()));

		if (criacao || (resposta != null)) {
			
			String comentario = (String) request.getParameter("comentario"+(criacao?"":obj.getCod()));
			int pontuacao = Integer.parseInt(request.getParameter("pontuacao"+(criacao?"":obj.getCod())));

			// Checa consist�ncia	
			String problema = respger.testaConsistencia(obj,resposta,comentario,pontuacao,0,questao);
			if (problema != null) {
				pagina.javascript("alert('Campo nulo ou repetido: "+problema+".'); window.history.back();");
				return;
			}

			// Efetua cadastro
			if (criacao) {
				respger.inclui(resposta,comentario,pontuacao,0,questao,false);
			} else {
				respger.altera(obj,resposta,comentario,pontuacao,obj.getHits(),questao,false);
			}
		}
	}

	// Monta pagina de resposta
	pagina.javascript("window.opener.location.reload(); window.close();");
	
	pagina.fim();


}
private void listaRespostas(SessionManager sessionManager, Questao questao, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Lista Respostas");
	pagina.init();

	Resposta_ger ger = new Resposta_ger();
	Vector v = ger.getElements(questao);
	Collections.sort(v);
	Enumeration resps = v.elements();

	Formulario form = new Formulario(pagina,"");
	
	out.println("<TABLE width=95%><TR><TD><span class=MenuItem><B>questão:</B> ["+questao.getTipo()+"] "+questao.getEnunciado()+"</span></TD>");
	if (resps.hasMoreElements()) {
		out.println("<TD align=right>"+form.button("Corrigir novas respostas","window.open('GerenciaRespostas?toDo=corrigeNovasResposta&codQuestao="+questao.getCod()+"&"+sessionManager.generateEncodedParameter()+"','novasResps"+questao.getCod()+"','toolbar=no,scrollbars=yes,resizable=yes,menubar=no,width=730,height=500,top=10,left=10');")+"<BR>");
		out.println(form.button("Corrigir todas respostas","window.open('GerenciaRespostas?toDo=corrigeTodasResposta&codQuestao="+questao.getCod()+"&"+sessionManager.generateEncodedParameter()+"','novasResps"+questao.getCod()+"','toolbar=no,scrollbars=yes,resizable=yes,menubar=no,width=730,height=500,top=10,left=10');")+"</TD></TR></TABLE>");
	} else {
		out.println("</TR></TABLE>");
	}

	form.fim();

	// Inicia tabela
	out.println("<TABLE border=0 cellspacing=0 cellpadding=2 width=95%>");
	out.println("<TR bgcolor=lightblue>");
	out.println("<TD><b>Resposta</b></TD>");
	out.println("<TD align=center width=20><b>Pontuação</b></TD>");
	out.println("<TD align=center width=40><b>Hits</b></TD>");
	out.println("<TD align=center width=50>&nbsp;</TD>");
	out.println("</TR>");

	if (!resps.hasMoreElements()) {
		out.print("<TR><TD colspan=4 align=center>não há respostas cadastradas</td>");
	}
		
	while (resps.hasMoreElements()) {
		Resposta resp = (Resposta) resps.nextElement();
		String txtComent = ((resp.getComentario() == null) || (resp.getComentario().equals(""))) ? "" : " <FONT COLOR=green>(coment: "+resp.getComentario()+")</FONT>";
		out.println("<TR><TD><LI><span class=MenuItem><a href=\"javascript://\" onclick=\"window.open('GerenciaRespostas?toDo=cadastroResposta&codQuestao="+questao.getCod()+"&codResposta="+resp.getCod()+"&"+sessionManager.generateEncodedParameter()+"','resposta"+resp.getCod()+"','toolbar=no,scrollbars=yes,resizable=yes,menubar=no,width=730,height=500,top=10,left=10');\">"+(resp.isNova()?"<B>":"")+resp.getResposta()+txtComent+(resp.isNova()?"</B>":"")+"</a></span></TD>");
		out.println("<TD align=center><span class=MenuItem>"+(resp.isNova()?"":(resp.getPontuacao()<=0?"<FONT COLOR=red>":(resp.getPontuacao()>=80?"<FONT color=blue>":"")))+(resp.isNova()?"-":""+resp.getPontuacao())+"</font></span></TD>");
		out.println("<TD align=center><span class=MenuItem>"+resp.getHits()+"</span></TD>");
		out.println("<TD align=right><span class=MenuItem><a href=\"javascript://\" onclick=\"if (confirm('Confirma exclusão?')) window.location='GerenciaRespostas?toDo=removerQuestao?codQuestao="+questao.getCod()+"&codResposta="+resp.getCod()+"&"+sessionManager.generateEncodedParameter()+"';"+"\">Excluir</a></span></TD></tr>");
		
	}	

	out.println("</TABLE>");
	
	pagina.saltaLinha();
	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opções");
		menu2.addItem("Criar nova Resposta","javascript://\" onclick=\"window.open('GerenciaRespostas?toDo=cadastroResposta&codQuestao="+questao.getCod()+"&"+sessionManager.generateEncodedParameter()+"','novaResposta','toolbar=no,scrollbars=auto,resizable=yes,menubar=no,width=730,height=500,top=10,left=10');\"");
	menu2.fim();

	pagina.fim();


	


}
private void mensagemEscolherQuestao(SessionManager sessionManager, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Resposta");
	pagina.init();

	pagina.titulo ("Por favor, escolha uma questão.");

	pagina.saltaLinha();

	pagina.fim();

}
private void removerResposta(SessionManager sessionManager, PrintWriter out, Resposta obj) throws Exception {

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Remoção de Resposta");
	pagina.init();

	// Efetua exclusão
	Resposta_ger ger = new Resposta_ger();
	ger.remove(obj);

	// Monta pagina de resposta
	pagina.javascript("alert('Remoção efetuada com sucesso.'); window.location = 'GerenciaRespostas?"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();

}
}
