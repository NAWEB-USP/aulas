package com.aulas.gerinterface;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 04/02/2003 10:25:56 -- *
 * -- Gerador versão alpha                                       *
 * ----------------------------------------------------------- *

 Histórico de Versões
 --------------------
 versão	Autor		Modificação
 ------	-----		-----------
 1.0	Gerosa 		Geração automática da classe
 1.1	Gerosa		Inclusao da opcao Gerenciar Listas
 * 
 */



import com.aulas.modelos.*;
import com.aulas.util.*;
import com.aulas.util.sessionmanager.*;
import com.aulas.business.*;
import com.aulas.objinterface.*;

import java.util.*;
import java.io.*;

public class GerenciaDisciplinas extends Servlet {


public void action(javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	// Inicializa SessionManager
	SessionManager sessionManager = new SessionManager(request);
	
	// Somente professor tem acesso
	sessionManager.checaPermissaoProfessor();

	// A partir do toDo seleciona m�todo
	String toDo = (String) request.getParameter("toDo");
	if ((toDo == null) || (toDo.equals("")) || (toDo.equals("listaDisciplinas"))) {
		sessionManager.removeElement("idDisciplina");
		listaDisciplinas (sessionManager,out);

	} else if (toDo.equals("opcoesDisciplina")) {
		String cod = (String) request.getParameter("cod");
		String idDisciplina = cod;
		if ((cod == null) || (cod.equals(""))) {
			idDisciplina = sessionManager.getElement("idDisciplina");
		}
		if ((idDisciplina == null) || (idDisciplina.equals(""))) {throw new Exception ("Falta o cod");}
		sessionManager.addElement("idDisciplina",idDisciplina);
		Disciplina_ger ger = new Disciplina_ger();
		Disciplina obj = ger.getElement(idDisciplina);

		opcoesDisciplina (sessionManager,out, obj);

	} else if (toDo.equals("cadastroDisciplina")) {
		String cod = (String) request.getParameter("cod");
		String idDisciplina = cod;
		if ((cod != null) && (!cod.equals(""))) {
			sessionManager.addElement("idDisciplina",idDisciplina);
		} else {
			idDisciplina = sessionManager.getElement("idDisciplina");
		}
		Disciplina obj = null;
		if ((idDisciplina != null) && (!idDisciplina.equals(""))) {
			Disciplina_ger ger = new Disciplina_ger();
			obj = ger.getElement(idDisciplina);
		}

		cadastroDisciplina(sessionManager,out,obj);

	} else if (toDo.equals("efetuaCadastro")) {
		efetuaCadastro(sessionManager,request,out);

    } else if (toDo.equals("detectaPlagio")) {
        String idDisciplina = sessionManager.getElement("idDisciplina");
        Disciplina_ger ger = new Disciplina_ger();
        Disciplina disc = ger.getElement(idDisciplina);
        DetectaPlagio.detectaPlagio(sessionManager, disc, out);
		
	} else if (toDo.equals("removerDisciplina")) {
		String cod = (String) request.getParameter("cod");
		String idDisciplina = cod;
		if ((idDisciplina == null) || (idDisciplina.equals(""))) {throw new Exception ("Falta o cod");}
		Disciplina_ger ger = new Disciplina_ger();
		Disciplina obj = ger.getElement(idDisciplina);

		removerDisciplina (sessionManager,out, obj);

	} else {
		throw new Exception ("Opção não reconhecida.");
	}

}
private void cadastroDisciplina(SessionManager sessionManager, PrintWriter out, Disciplina obj) throws Exception {

	boolean criacao = (obj == null);

	String titulo = criacao ? "Criação de Disciplina" : "Alteração de Disciplina";

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,titulo);
	pagina.init();

	pagina.titulo (titulo);
	pagina.descricao("Preencha os dados nos campos abaixo:");

	Formulario form = new Formulario(pagina,"GerenciaDisciplinas?"+sessionManager.generateEncodedParameter());
	form.hidden("toDo","efetuaCadastro");
	if (!criacao) form.hidden("cod",obj.getCod()); 

	TabelaFormulario tab = new TabelaFormulario (pagina);

		tab.addCelula("Nome: ");
		tab.addCelula(form.textboxTexto("nome",(criacao?"":obj.getNome()), 50, 255));
		tab.novaLinha();
		tab.addCelula("Descrição: ");
		tab.addCelula(form.textArea("descricao",(criacao?"":obj.getDescricao()), 5, 50));
		tab.novaLinha();
		tab.addCelulaCom2Botes(form.botaoSubmit(criacao ? "Criar" : "Alterar"),form.button("Cancelar",(criacao)?"window.location='GerenciaDisciplinas?"+sessionManager.generateEncodedParameter()+"'":"window.location='GerenciaDisciplinas?"+sessionManager.generateEncodedParameter()+"'"));

	tab.fim();
	form.fim();

	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();


}
private void efetuaCadastro(SessionManager sessionManager, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	String cod = (String) request.getParameter("cod");
	Disciplina obj = null;
	Disciplina_ger ger = new Disciplina_ger();
	if ((cod != null) && (!cod.equals(""))) {
		obj = ger.getElement(cod);
	}

	boolean criacao = (obj == null);

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,criacao ? "Criação de Disciplina" : "Alteração de Disciplina");
	pagina.init();
	
	// Le dados do formulario
	String nome = (String) request.getParameter("nome");
	String descricao = (String) request.getParameter("descricao");

	// Checa consist�ncia	
	String problema = ger.testaConsistencia(obj,nome,descricao);
	if (problema != null) {
		pagina.javascript("alert('Campo nulo ou repetido: "+problema+".'); window.history.back();");
		return;
	}

	// Efetua cadastro
	if (criacao) {
		ger.inclui(nome,descricao);
	} else {
		ger.altera(obj,nome,descricao);
	}

	// Monta pagina de resposta
	if (criacao) {
		pagina.javascript("alert('Criação efetuada com sucesso.'); window.location = 'GerenciaDisciplinas?"+sessionManager.generateEncodedParameter()+"';");
	} else {
		pagina.javascript("alert('Alteração efetuada com sucesso.'); window.location = 'GerenciaDisciplinas?"+sessionManager.generateEncodedParameter()+"';");
	}
	
	pagina.fim();


}
private void listaDisciplinas(SessionManager sessionManager, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Lista Disciplinas");
	pagina.init();

	pagina.titulo ("Lista de Disciplinas");

	Disciplina_ger ger = new Disciplina_ger();
	Resposta_ger respger = new Resposta_ger();
	Vector v = ger.getAllElements();
	Collections.sort(v);
	Enumeration lista = v.elements();

	pagina.descricao("Escolha abaixo a Disciplina.");
	pagina.saltaLinha();
	ListaItens lst = new ListaItens (pagina,"Disciplinas");
	int total = 0;
	if (!lista.hasMoreElements()) {
		pagina.descricao("não há Disciplinas cadastradas.");
		lst.fim();
	} else {
		while (lista.hasMoreElements()) {
			Disciplina obj = (Disciplina) lista.nextElement();
			if (!obj.isDesativada()) {
				String opcoes = "<a href=\"javascript://\" onclick=\"if (confirm('Confirma exclusão?')) window.location='GerenciaDisciplinas?toDo=removerDisciplina&cod="+obj.getCod()+"&"+sessionManager.generateEncodedParameter()+"';\">Excluir</a>";
				opcoes += " &nbsp;<a href=\"GerenciaDisciplinas?toDo=cadastroDisciplina&cod="+obj.getCod()+"&"+sessionManager.generateEncodedParameter()+"\">Alterar</a>";
				int numRespostasNovas = respger.getNumRespostasNovasByDisciplina(obj);
				total += numRespostasNovas;
				if (obj.isDesativada()) {
					lst.addItem(obj.getNome()+" [desativada]","GerenciaDisciplinas?toDo=opcoesDisciplina&cod="+obj.getCod()+"&"+sessionManager.generateEncodedParameter(),opcoes);
				} else {
					lst.addItem((numRespostasNovas==0)?obj.getNome():"<B>"+obj.getNome()+" ("+numRespostasNovas+" novas respostas)</B>","GerenciaDisciplinas?toDo=opcoesDisciplina&cod="+obj.getCod()+"&"+sessionManager.generateEncodedParameter(),opcoes);
				}
			}
		}
		lst.fim();
		if (total != 0) {
			pagina.saltaLinha();
			pagina.descricao("Total de respostas novas: "+total);
		}
	}

	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opções");
		menu2.addItem("Lançamento Avulso de Notas","LancarNotas?"+sessionManager.generateEncodedParameter());
		menu2.addItem("Criar nova Disciplina","GerenciaDisciplinas?toDo=cadastroDisciplina"+"&"+sessionManager.generateEncodedParameter());
		menu2.addItem("Gerenciar Alunos","GerenciaAlunos?"+sessionManager.generateEncodedParameter());
		menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter());
	menu2.fim();


	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();


}
private void opcoesDisciplina(SessionManager sessionManager, PrintWriter out, Disciplina obj) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Opções da "+StringConverter.concatenateWithoutRepetion("Disciplina",obj.getNome()));
	pagina.init();

	pagina.titulo ("Opções da "+StringConverter.concatenateWithoutRepetion("Disciplina",obj.getNome()));

	pagina.descricao("Escolha abaixo uma Opção.");
	pagina.saltaLinha();

	// ------- Listas -------
	Lista_ger gerlista = new Lista_ger();
	Resposta_ger respger = new Resposta_ger();
	Enumeration listas = gerlista.getElements(obj).elements();
	ListaItens lst = new ListaItens (pagina,"Listas");
	int total = 0;
	if (!listas.hasMoreElements()) {
		pagina.descricao("não há Listas cadastradas.");
	} else {
		while (listas.hasMoreElements()) {
			Lista l = (Lista) listas.nextElement();
			String opcoes = "<a href=\"javascript://\" onclick=\"if (confirm('Confirma exclusão?')) window.location='GerenciaListas?toDo=removerLista&cod="+l.getCod()+"&"+sessionManager.generateEncodedParameter()+"';\">Excluir</a>";
			opcoes += " &nbsp;<a href=\"GerenciaListas?toDo=cadastroLista&cod="+l.getCod()+"&"+sessionManager.generateEncodedParameter()+"\">Alterar</a>";
			int numRespostasNovas = respger.getNumRespostasNovasByLista(l);
			total += numRespostasNovas;
			lst.addItem((numRespostasNovas==0)?l.getNome():"<B>"+l.getNome()+" ("+numRespostasNovas+" novas respostas)</B>"+(l.isAtiva()?"":" (padrão: desativada)"),"GerenciaQuestoes?idLista="+l.getCod()+"&"+sessionManager.generateEncodedParameter(),opcoes);
		}
	}
	lst.fim();
	if (total != 0) {
		pagina.saltaLinha();
		pagina.descricao("Total de respostas novas: "+total);
	}
	
	pagina.saltaLinha();
	pagina.saltaLinha();
	// ------- Turmas -------
	Aluno_ger alunoger = new Aluno_ger();
	Turma_ger ger = new Turma_ger();
	Enumeration lista = ger.getElements(obj).elements();
	lst = new ListaItens (pagina,"Turmas");
	int totalAlunos = 0;
	if (!lista.hasMoreElements()) {
		pagina.descricao("não há Turmas cadastradas.");
	} else {
		while (lista.hasMoreElements()) {
			Turma t = (Turma) lista.nextElement();
			String opcoes = "<a href=\"javascript://\" onclick=\"if (confirm('Confirma exclusão?')) window.location='GerenciaTurmas?toDo=removerTurma&cod="+t.getCod()+"&"+sessionManager.generateEncodedParameter()+"';\">Excluir</a>";
			opcoes += " &nbsp;<a href=\"GerenciaTurmas?toDo=cadastroTurma&cod="+t.getCod()+"&"+sessionManager.generateEncodedParameter()+"\">Alterar</a>";
			int numAlunos = alunoger.getNumAlunosPorTurma(t);
			totalAlunos += numAlunos;
			lst.addItem(t.getNome() + " ("+numAlunos+" alunos)" ,"OpcoesListaProfessor?idTurma="+t.getCod()+"&"+sessionManager.generateEncodedParameter(),opcoes);
		}
	}
	lst.fim();
	pagina.saltaLinha();
	pagina.descricao("Total de alunos: "+totalAlunos);

	pagina.saltaLinha();
	pagina.saltaLinha();
	
	// ------- Outras Opções -------
	Menu menu2 = new Menu (pagina,"Outras Opções");
	    menu2.addItem("Detectar plágio","GerenciaDisciplinas?toDo=detectaPlagio&"+sessionManager.generateEncodedParameter());
		menu2.addItem("Criar nova Lista","GerenciaListas?toDo=inclusaoLista&"+sessionManager.generateEncodedParameter());
		menu2.addItem("Criar nova Turma","GerenciaTurmas?toDo=inclusaoTurma&"+sessionManager.generateEncodedParameter());
		menu2.addItem("Escolher outra Disciplina","GerenciaDisciplinas"+"?"+sessionManager.generateEncodedParameter());
		menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter());
	menu2.fim();

	// ------- Mais informações -------
	if ((obj.getDescricao() != null) && (!obj.getDescricao().equals(""))) {
		pagina.saltaLinha();
		pagina.saltaLinha();
		ListaItens titulo = new ListaItens (pagina,"Mais informações sobre a Disciplina");
		titulo.fim();
		pagina.descricao(obj.getDescricao());
	}

	// ------- Rodape -------
	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();

}
private void removerDisciplina(SessionManager sessionManager, PrintWriter out, Disciplina obj) throws Exception {

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Remoção de Disciplina");
	pagina.init();

	// Efetua exclusão
	Disciplina_ger ger = new Disciplina_ger();
	ger.remove(obj);

	// Monta pagina de resposta
	pagina.javascript("alert('Remoção efetuada com sucesso.'); window.location = 'GerenciaDisciplinas"+"?"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();

}
}
