package com.aulas.gerinterface;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 12/02/2003 17:29:30 -- *
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

public class GerenciaAlunos extends Servlet {


public void action(javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	// Inicializa SessionManager
	SessionManager sessionManager = new SessionManager(request);

	// A partir do toDo seleciona m�todo
	String toDo = (String) request.getParameter("toDo");
	if ((toDo == null) || (toDo.equals("")) || (toDo.equals("listaAlunos"))) {
		// Somente professor tem acesso
		sessionManager.checaPermissaoProfessor();
		sessionManager.removeElement("idAluno");
		listaAlunos (sessionManager,out);

	} else if (toDo.equals("opcoesAluno")) {
		// Somente professor tem acesso
		sessionManager.checaPermissaoProfessor();
		String cod = (String) request.getParameter("cod");
		String idAluno = cod;
		if ((cod == null) || (cod.equals(""))) {
			idAluno = sessionManager.getElement("idAluno");
		}
		if ((idAluno == null) || (idAluno.equals(""))) {throw new Exception ("Falta o cod");}
		sessionManager.addElement("idAluno",idAluno);
		Aluno_ger ger = new Aluno_ger();
		Aluno obj = ger.getElement(idAluno);

		opcoesAluno (sessionManager,out, obj);

	} else if (toDo.equals("acessarComo")) {
		// Somente professor tem acesso
		sessionManager.checaPermissaoProfessor();
		String cod = (String) request.getParameter("cod");
		String idAluno = cod;
		if ((cod == null) || (cod.equals(""))) {
			idAluno = sessionManager.getElement("idAluno");
		}
		if ((idAluno == null) || (idAluno.equals(""))) {throw new Exception ("Falta o cod");}
		sessionManager.addElement("idAluno",idAluno);
		Aluno_ger ger = new Aluno_ger();
		Aluno obj = ger.getElement(idAluno);

		acessarComo (sessionManager,out, obj);

	} else if (toDo.equals("cadastroAluno")) {
		String idAluno = sessionManager.getElement("idAluno");
		Aluno obj = null;
		if ((idAluno != null) && (!idAluno.equals(""))) {
			Aluno_ger ger = new Aluno_ger();
			obj = ger.getElement(idAluno);
		}

		cadastroAluno(sessionManager,out,obj,false);

	} else if (toDo.equals("efetuaCadastro")) {
		efetuaCadastro(sessionManager,request,out);

	} else if (toDo.equals("removerAluno")) {
		// Somente professor tem acesso
		sessionManager.checaPermissaoProfessor();
		String idAluno = sessionManager.getElement("idAluno");
		if ((idAluno == null) || (idAluno.equals(""))) {throw new Exception ("Falta o cod");}
		Aluno_ger ger = new Aluno_ger();
		Aluno obj = ger.getElement(idAluno);

		removerAluno (sessionManager,out, obj);

	} else {
		throw new Exception ("Opção não reconhecida.");
	}

}
public void cadastroAluno(SessionManager sessionManager, PrintWriter out, Aluno obj, boolean selfCadastro) throws Exception {

	boolean criacao = (obj == null);

	String titulo = criacao ? "Cadastro de Aluno" : "Alteração de Aluno";

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,titulo);
	pagina.init();

	pagina.titulo (titulo);
	pagina.descricao("Preencha os dados nos campos abaixo:");

	Formulario form = new Formulario(pagina,"GerenciaAlunos?"+sessionManager.generateEncodedParameter());
	form.hidden("toDo","efetuaCadastro");
	if (!criacao) form.hidden("cod",obj.getCod());
	if (selfCadastro) form.hidden("selfCadastro","true");

	TabelaFormulario tab = new TabelaFormulario (pagina);

		tab.addCelula("Nome: ");
		tab.addCelula(form.textboxTexto("nome",(criacao?"":obj.getNome()), 50, 255));
		tab.addCelula("E-mail: ");
		tab.addCelula(form.textboxTexto("email",(criacao?"":obj.getEmail()), 50, 255));
		tab.addCelula("Número Matrícula: ");
		tab.addCelula(form.textboxTexto("numeroMatricula",(criacao?"":obj.getNumeroMatricula()), 50, 255));
		tab.addCelula("Curso: ");
		tab.addCelula(form.textboxTexto("curso",(criacao?"":obj.getCurso()), 50, 255));
		tab.addCelula("Data Nascimento: ");
		tab.addCelula(form.textboxTexto("dataNascimento",(criacao?"":obj.getDataNascimento()), 50, 255));
		tab.addCelula("Sexo: ");
		Vector opcoes = new Vector();
		Vector valores = new Vector();
		opcoes.addElement("Masculino");
		valores.addElement("M");
		opcoes.addElement("Feminino");
		valores.addElement("F");
		tab.addCelula(form.selectBox("sexo",valores,opcoes,(criacao?"":(obj.getSexo().equals("F")?"Feminino":"Masculino"))));
		tab.addCelula("Login: ");
		tab.addCelula(form.textboxTexto("login",(criacao?"":obj.getLogin()), 50, 255));
		tab.addCelula("Senha: ");
		tab.addCelula(form.textboxPassword("senha",(criacao?"":obj.getSenha()), 50, 255));
		tab.novaLinha();
		String botaoCancelar;
		if (selfCadastro) {
			if (criacao) {
				botaoCancelar = "window.location='LoginManager'";
			} else {
				botaoCancelar = "window.location='OpcoesAluno?"+sessionManager.generateEncodedParameter()+"'";
			}
		} else {
			if (criacao) {
				botaoCancelar = "window.location='GerenciaAlunos?"+sessionManager.generateEncodedParameter()+"'";
			} else {
				botaoCancelar = "window.location='GerenciaAlunos?toDo=opcoesAluno&"+sessionManager.generateEncodedParameter()+"'";
			}
		}
		tab.addCelulaCom2Botes(form.botaoSubmit(criacao ? "Criar" : "Alterar"),form.button("Cancelar",botaoCancelar));

	tab.fim();
	form.fim();

	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();


}
private void efetuaCadastro(SessionManager sessionManager, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	String cod = (String) request.getParameter("cod");
	Aluno obj = null;
	Aluno_ger ger = new Aluno_ger();
	if ((cod != null) && (!cod.equals(""))) {
		obj = ger.getElement(cod);
	}

	boolean criacao = (obj == null);

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,criacao ? "Criação de Aluno" : "Alteração de Aluno");
	pagina.init();
	
	// Le dados do formulario
	String nome = (String) request.getParameter("nome");
	String email = (String) request.getParameter("email");
	String numeroMatricula = (String) request.getParameter("numeroMatricula");
	String curso = (String) request.getParameter("curso");
	String dataNascimento = (String) request.getParameter("dataNascimento");
	String sexo = (String) request.getParameter("sexo");
	String login = (String) request.getParameter("login");
	String senha = (String) request.getParameter("senha");

	// Checa consist�ncia	
	String problema = ger.testaConsistencia(obj,nome,email,numeroMatricula, curso, dataNascimento, sexo,login,senha);
	if (problema != null) {
		pagina.javascript("alert('Campo nulo ou repetido: "+problema+".'); window.history.back();");
		return;
	}

	// Efetua cadastro
	if (criacao) {
		ger.inclui(nome,email,numeroMatricula, curso, dataNascimento, sexo,login,senha);
	} else {
		ger.altera(obj,nome,email,numeroMatricula, curso, dataNascimento, sexo,login,senha);
	}

	// Monta pagina de resposta
	if (request.getParameter("selfCadastro") != null) {
		if (criacao) {
			pagina.javascript("alert('Cadastro efetuado com sucesso.'); window.location = 'LoginManager?"+sessionManager.generateEncodedParameter()+"';");
		} else {
			pagina.javascript("alert('Alteração efetuada com sucesso.'); window.location = 'OpcoesAluno?"+sessionManager.generateEncodedParameter()+"';");
		}
	} else if (criacao) {
		pagina.javascript("alert('Criação efetuada com sucesso.'); window.location = 'GerenciaAlunos?"+sessionManager.generateEncodedParameter()+"';");
	} else {
		pagina.javascript("alert('Alteração efetuada com sucesso.'); window.location = 'GerenciaAlunos?"+sessionManager.generateEncodedParameter()+"';");
	}
	
	pagina.fim();


}
private void listaAlunos(SessionManager sessionManager, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Lista Alunos");
	pagina.init();

	pagina.titulo ("Lista de Alunos");

	Aluno_ger ger = new Aluno_ger();
	Vector v = ger.getAllElements();
	Collections.sort(v);
	Enumeration lista = v.elements();

	pagina.descricao("Escolha abaixo o Aluno.");
	pagina.saltaLinha();
	Menu menu = new Menu (pagina,"Alunos");
	if (!lista.hasMoreElements()) {
		pagina.descricao("não há Alunos cadastrados.");
	} else {
		while (lista.hasMoreElements()) {
			Aluno obj = (Aluno) lista.nextElement();
			menu.addItem(obj.getNome(),"GerenciaAlunos?toDo=opcoesAluno&cod="+obj.getCod()+"&"+sessionManager.generateEncodedParameter());
		}
	}
	menu.fim();


	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opçães");
		menu2.addItem("Criar novo Aluno","GerenciaAlunos?toDo=cadastroAluno"+"&"+sessionManager.generateEncodedParameter());
		menu2.addItem("Gerenciar Disciplinas","GerenciaDisciplinas?"+sessionManager.generateEncodedParameter());
		menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter());
	menu2.fim();


	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();


}
private void opcoesAluno(SessionManager sessionManager, PrintWriter out, Aluno obj) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Opçães de Aluno");
	pagina.init();

	pagina.titulo ((obj.getSexo().equals("M")?"Opçães do Aluno ":"Opçães da Aluna ") + obj.getNome());

	pagina.descricao("Escolha abaixo a opção.");
	pagina.saltaLinha();

	Menu menu = new Menu (pagina,"Opçães");
	menu.addItem("Alterar dados","GerenciaAlunos?toDo=cadastroAluno&"+sessionManager.generateEncodedParameter());
	menu.addItem("Acessar como este aluno","GerenciaAlunos?toDo=acessarComo&"+sessionManager.generateEncodedParameter());
	menu.addItem("Remover Aluno","javascript://\" onclick=\"if (confirm('Confirma exclusão?')) window.location='GerenciaAlunos?toDo=removerAluno&"+sessionManager.generateEncodedParameter()+"';");
	menu.fim();

	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opções");
		menu2.addItem("Escolher outro Aluno","GerenciaAlunos"+"?"+sessionManager.generateEncodedParameter());
		menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter());
	menu2.fim();


	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();

}

private void acessarComo(SessionManager sessionManager, PrintWriter out, Aluno obj) throws Exception {

	sessionManager.addElement("idUsuario",obj.getCod());
	LoginManager.opcoesAluno(sessionManager,out,obj);

}

private void removerAluno(SessionManager sessionManager, PrintWriter out, Aluno obj) throws Exception {

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Remoção de Aluno");
	pagina.init();

	// Efetua exclusão
	Aluno_ger ger = new Aluno_ger();
	ger.remove(obj);

	// Monta pagina de resposta
	pagina.javascript("alert('Remoção efetuada com sucesso.'); window.location = 'GerenciaAlunos?"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();

}
}
