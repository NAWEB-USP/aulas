package com.aulas.gerinterface;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 11/02/2003 09:00:18 -- *
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
import com.aulas.objinterface.*;

import java.util.*;
import java.io.*;

public class GerenciaTurmas extends Servlet {


public void action(javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	// Variaveis comuns
	Disciplina disc = null;
	
	// Inicializa SessionManager
	SessionManager sessionManager = new SessionManager(request);

	// Somente professor tem acesso
	sessionManager.checaPermissaoProfessor();

	// Le disciplina
	String idDisciplina = sessionManager.getElement("idDisciplina");
	if ((idDisciplina == null) || (idDisciplina.equals(""))) {throw new Exception ("Falta a disciplina");}
	Disciplina_ger discger = new Disciplina_ger();
	disc = discger.getElement(idDisciplina);
	
	// A partir do toDo seleciona m�todo
	String toDo = (String) request.getParameter("toDo");
	if ((toDo == null) || (toDo.equals("")) || (toDo.equals("listaTurmas"))) {
		sessionManager.removeElement("idTurma");
		listaTurmas (sessionManager,disc,out);

	} else if (toDo.equals("opcoesTurma")) {
		String cod = (String) request.getParameter("cod");
		String idTurma = cod;
		if ((cod == null) || (cod.equals(""))) {
			idTurma = sessionManager.getElement("idTurma");
		}
		if ((idTurma == null) || (idTurma.equals(""))) {throw new Exception ("Falta o cod");}
		sessionManager.addElement("idTurma",idTurma);
		Turma_ger ger = new Turma_ger();
		Turma obj = ger.getElement(idTurma);

		opcoesTurma (sessionManager,out, obj);

	} else if (toDo.equals("cadastroTurma")) {
		String cod = (String) request.getParameter("cod");
		String idTurma = cod;
		if ((cod != null) && (!cod.equals(""))) {
			sessionManager.addElement("idTurma",idTurma);
		} else {
			idTurma = sessionManager.getElement("idTurma");
		}
		Turma obj = null;
		if ((idTurma != null) && (!idTurma.equals(""))) {
			Turma_ger ger = new Turma_ger();
			obj = ger.getElement(idTurma);
		}

		cadastroTurma(sessionManager,out,obj);
		
	} else if (toDo.equals("inclusaoTurma")) {
		cadastroTurma(sessionManager,out,null);

	} else if (toDo.equals("efetuaCadastro")) {
		efetuaCadastro(sessionManager,disc,request,out);

	} else if (toDo.equals("removerTurma")) {
		String cod = (String) request.getParameter("cod");
		String idTurma = cod;
		if ((idTurma == null) || (idTurma.equals(""))) {throw new Exception ("Falta o cod");}
		Turma_ger ger = new Turma_ger();
		Turma obj = ger.getElement(idTurma);

		removerTurma (sessionManager,out, obj);

	} else {
		throw new Exception ("Opção não reconhecida.");
	}

}
private void cadastroTurma(SessionManager sessionManager, PrintWriter out, Turma obj) throws Exception {

	boolean criacao = (obj == null);

	String titulo = criacao ? "Criação de Turma" : "Alteração de Turma";

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,titulo);
	pagina.init();

	pagina.titulo (titulo);
	pagina.descricao("Preencha os dados nos campos abaixo:");

	Formulario form = new Formulario(pagina,"GerenciaTurmas?"+sessionManager.generateEncodedParameter());
	form.hidden("toDo","efetuaCadastro");
	if (!criacao) form.hidden("cod",obj.getCod()); 

	TabelaFormulario tab = new TabelaFormulario (pagina);

		tab.addCelula("Nome: ");
		tab.addCelula(form.textboxTexto("nome",(criacao?"":obj.getNome()), 50, 255));
		tab.addCelula("Descrição: ");
		tab.addCelula(form.textArea("descricao",(criacao?"":obj.getDescricao()), 5, 50));
		tab.novaLinha();
		tab.novaLinha();
		tab.addCelulaCom2Botes(form.botaoSubmit(criacao ? "Criar" : "Alterar"),form.button("Cancelar","window.location='GerenciaDisciplinas?toDo=opcoesDisciplina&"+sessionManager.generateEncodedParameter()+"'"));

	tab.fim();
	form.fim();

	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();


}
private void efetuaCadastro(SessionManager sessionManager, Disciplina disc, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	String cod = (String) request.getParameter("cod");
	Turma obj = null;
	Turma_ger ger = new Turma_ger();
	if ((cod != null) && (!cod.equals(""))) {
		obj = ger.getElement(cod);
	}

	boolean criacao = (obj == null);

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,criacao ? "Criação de Turma" : "Alteração de Turma");
	pagina.init();
	
	// Le dados do formulario
	String nome = (String) request.getParameter("nome");
	String descricao = (String) request.getParameter("descricao");

	// Checa consist�ncia	
	String problema = ger.testaConsistencia(obj,nome,descricao,disc);
	if (problema != null) {
		pagina.javascript("alert('Campo nulo ou repetido: "+problema+".'); window.history.back();");
		return;
	}

	// Efetua cadastro
	if (criacao) {
		ger.inclui(nome,descricao,disc);
	} else {
		ger.altera(obj,nome,descricao);
	}

	// Monta pagina de resposta
	if (criacao) {
		pagina.javascript("alert('Criação efetuada com sucesso.'); window.location = 'GerenciaDisciplinas?toDo=opcoesDisciplina&"+sessionManager.generateEncodedParameter()+"';");
	} else {
		pagina.javascript("alert('Alteração efetuada com sucesso.'); window.location = 'GerenciaDisciplinas?toDo=opcoesDisciplina&"+sessionManager.generateEncodedParameter()+"';");
	}
	
	pagina.fim();


}
private void listaTurmas(SessionManager sessionManager, Disciplina disc, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Lista Turmas da Disciplina "+disc.getNome());
	pagina.init();

	pagina.titulo ("Lista de Turmas da Disciplina "+disc.getNome());

	Turma_ger ger = new Turma_ger();
	Enumeration lista = ger.getElements(disc).elements();

	pagina.descricao("Escolha abaixo a Turma.");
	pagina.saltaLinha();
	Menu menu = new Menu (pagina,"Turmas");
	if (!lista.hasMoreElements()) {
		pagina.descricao("não há Turmas cadastradas.");
	} else {
		while (lista.hasMoreElements()) {
			Turma obj = (Turma) lista.nextElement();
			menu.addItem(obj.getNome(),"GerenciaTurmas?toDo=opcoesTurma&cod="+obj.getCod()+"&"+sessionManager.generateEncodedParameter());
		}
	}
	menu.fim();


	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opções");
		menu2.addItem("Criar nova Turma","GerenciaTurmas?toDo=cadastroTurma"+"&"+sessionManager.generateEncodedParameter());
		menu2.addItem("Escolher outra Disciplina","GerenciaDisciplinas?"+sessionManager.generateEncodedParameter());
		menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter());
	menu2.fim();


	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();


}
private void opcoesTurma(SessionManager sessionManager, PrintWriter out, Turma obj) throws Exception {

/*	PaginaHTML pagina = new PaginaHTML (out,"Opções da "+StringConverter.concatenateWithoutRepetion("Turma",obj.getNome()));
	pagina.init();

	pagina.titulo ("Opções da "+StringConverter.concatenateWithoutRepetion("Turma",obj.getNome()));

	pagina.descricao("Escolha abaixo a Opção.");
	pagina.saltaLinha();

	Menu menu = new Menu (pagina,"Opções");
	menu.addItem("Ver alunos e notas","OpcoesListaProfessor?"+sessionManager.generateEncodedParameter());
	menu.addItem("Alterar dados","GerenciaTurmas?toDo=cadastroTurma&"+sessionManager.generateEncodedParameter());
	menu.addItem("Remover Turma","javascript:if (confirm('Confirma exclusão?')) window.location='GerenciaTurmas?toDo=removerTurma&"+sessionManager.generateEncodedParameter()+"';");
	menu.fim();

	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opções");
		//menu2.addItem("Escolher outra Turma","GerenciaTurmas"+"?"+sessionManager.generateEncodedParameter());
		//menu2.addItem("Escolher outra Disciplina","GerenciaDisciplinas?"+sessionManager.generateEncodedParameter());
		menu2.addItem("Voltar","GerenciaDisciplinas?toDo=opcoesDisciplina&"+sessionManager.generateEncodedParameter());
		menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter());
	menu2.fim();


	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();
*/
}
private void removerTurma(SessionManager sessionManager, PrintWriter out, Turma obj) throws Exception {

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Remoção de Turma");
	pagina.init();

	// Efetua exclusão
	Turma_ger ger = new Turma_ger();
	ger.remove(obj);

	// Monta pagina de resposta
	pagina.javascript("alert('Remoção efetuada com sucesso.'); window.location = 'GerenciaDisciplinas?toDo=opcoesDisciplina&"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();

}
}
