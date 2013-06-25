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
import com.aulas.pensamentos.Pensamento;
import com.aulas.pensamentos.PensamentoDAO;

import java.util.*;
import java.io.*;

public class LoginManager extends Servlet {

	public void action(javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

		// Inicializa SessionManager
		SessionManager sessionManager = new SessionManager(request);

		// A partir do toDo seleciona m�todo
		String toDo = (String) request.getParameter("toDo");
		if ((toDo == null) || (toDo.equals(""))) {
			paginaInicial(sessionManager, out);

		} else if (toDo.equals("cadastro")) {
			GerenciaAlunos g = new GerenciaAlunos();
			g.cadastroAluno(sessionManager, out, null, true);

		} else if (toDo.equals("efetuaLogin")) {
			efetuaLogin(sessionManager, request, out);

		} else if (toDo.equals("esqueceuSenha")) {
			esqueceuSenha(sessionManager, out);

		} else if (toDo.equals("enviaSenha")) {
			enviaSenha(sessionManager, request, out);

		} else {
			throw new Exception("Opção não reconhecida.");
		}

	}

	private void efetuaLogin(SessionManager sessionManager, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

		// Le dados do formulario
		String login = (String) request.getParameter("login");
		String senha = (String) request.getParameter("senha");

		// Busca aluno
		Aluno_ger alunoger = new Aluno_ger();
		Aluno aluno = alunoger.getElementByLogin(login);

		Configuracao_ger config = new Configuracao_ger();

		if (config.getElement("LoginMaster").equals(login) && config.getElement("SenhaMaster").equals(senha)) { // professor
			sessionManager.addElement("idUsuario", "0");
			opcoesProfessor(sessionManager, out);
		} else if (aluno == null) { // nao encontrou
			PaginaHTML pagina = new PaginaHTML(sessionManager, out, "Efetua Login");
			pagina.init();
			pagina.javascript("alert('Login não encontrado.\\nTente novamente.'); window.history.back();");
			pagina.fim();
		} else if (!aluno.getSenha().equals(senha)) {
			PaginaHTML pagina = new PaginaHTML(sessionManager, out, "Efetua Login");
			pagina.init();
			pagina.javascript("alert('Senha não confere.\\nTente novamente.'); window.history.back();");
			pagina.fim();
		} else { // encontrou
			sessionManager.addElement("idUsuario", aluno.getCod());
			opcoesAluno(sessionManager, out, aluno);
		}

	}

	private void enviaSenha(SessionManager sessionManager, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

		// Inicializa pagina
		PaginaHTML pagina = new PaginaHTML(sessionManager, out, "Envio de Senha");
		pagina.init();

		// Le dados do formulario
		String email = (String) request.getParameter("email");

		// Busca aluno
		Aluno_ger alunoger = new Aluno_ger();
		Aluno aluno = alunoger.getElementByEmail(email);

		if (aluno == null) { // nao encontrou
			pagina.javascript("alert('Email não encontrado.\\nTente novamente.'); window.history.back();");
		} else { // encontrou
			// Envia a mensagem
			SendMail sender = new SendMail();
			sender.addTo(email, email);
			sender.setAddressFrom("gerosa@ime.usp.br");
			sender.setNameFrom("Site de Aulas Gerosa");
			sender.setMessageSubject("[Gerosa Site] Envio de senha");
			String text = (aluno.getSexo().equals("M") ? "Caro " : "Cara ") + aluno.getNome() + ",\n\nAqui estão seu login e senha de acesso ao site.\n\nLogin: " + aluno.getLogin() + "\nSenha: " + aluno.getSenha() + "\n\n";
			sender.setMessageText(text);
			SendMailThread send = new SendMailThread();
			send.setSendMail(sender);
			send.start();

			pagina.javascript("alert('Email enviado com sucesso.'); window.location = 'LoginManager'");
		}

		pagina.fim();

	}

	private void esqueceuSenha(SessionManager sessionManager, PrintWriter out) throws Exception {

		PaginaHTML pagina = new PaginaHTML(sessionManager, out, "Página Inicial");
		pagina.init();

		pagina.titulo("Esqueceu sua senha");
		pagina.descricao("Entre com seu e-mail. Sua senha será automaticamente enviada para ele.");

		Formulario form = new Formulario(pagina, "LoginManager?" + sessionManager.generateEncodedParameter());
		form.hidden("toDo", "enviaSenha");

		TabelaFormulario tab = new TabelaFormulario(pagina);

		tab.addCelula("Email: ");
		tab.addCelula(form.textboxTexto("email", "", 20, 50));
		tab.novaLinha();
		tab.addCelulaCom2Botes(form.botaoSubmit("Enviar"), form.button("Cancelar", "window.location='LoginManager'"));

		tab.fim();
		form.fim();

		pagina.saltaLinha();

		pagina.rodape(TextoPadrao.copyright());

		pagina.fim();

	}

	static void opcoesAluno(SessionManager sessionManager, PrintWriter out, Aluno obj) throws Exception {
		// � estatico para poder acessar a partir de outro gerente de interface
		PaginaHTML pagina = new PaginaHTML(sessionManager, out, "Opções");
		pagina.init();

		pagina.javascript("window.location = 'OpcoesAluno?" + sessionManager.generateEncodedParameter() + "';");

		pagina.fim();

	}

	private void opcoesProfessor(SessionManager sessionManager, PrintWriter out) throws Exception {

		PaginaHTML pagina = new PaginaHTML(sessionManager, out, "Opções");
		pagina.init();

		pagina.javascript("window.location = 'GerenciaDisciplinas?" + sessionManager.generateEncodedParameter() + "';");

		pagina.fim();

	}

	private void paginaInicial(SessionManager sessionManager, PrintWriter out) throws Exception {

		PaginaHTML pagina = new PaginaHTML(sessionManager, out, "Página Inicial");
		pagina.init();

		pagina.titulo("Site de avaliações");
		pagina.descricao("Entre com seu login e senha ou clique no link abaixo para se cadastrar.");

		Formulario form = new Formulario(pagina, "LoginManager?" + sessionManager.generateEncodedParameter());
		form.hidden("toDo", "efetuaLogin");

		TabelaFormulario tab = new TabelaFormulario(pagina);

		tab.addCelula("Login: ");
		tab.addCelula(form.textboxTexto("login", "", 15, 50));
		tab.addCelula("Senha: ");
		tab.addCelula(form.textboxPassword("senha", "", 15, 50));
		tab.novaLinha();
		tab.addCelulaWithColSpan("<CENTER>" + form.botaoSubmit("Login") + "</CENTER>", 2);

		tab.fim();
		form.fim();

		pagina.saltaLinha();
		Pensamento p = (new PensamentoDAO()).getAPensamento();
		pagina.print("<I>\"" + StringConverter.toHtmlNotation(StringConverter.replace(p.getPensamento(), "<BR>", "\n")) + "\"</I>");
		if (p.getAutor() != null && !p.getAutor().equals("")) {
			pagina.print("<BR>" + p.getAutor());
		}

		pagina.saltaLinha();
		pagina.saltaLinha();

		Menu menu2 = new Menu(pagina, "Opções");
		// menu2.addItem("Cadastrar-se (não está mais dispon�vel)","");
		menu2.addItem("Cadastrar-se", "LoginManager?toDo=cadastro&" + sessionManager.generateEncodedParameter());
		menu2.addItem("Esqueceu a senha", "LoginManager?toDo=esqueceuSenha&" + sessionManager.generateEncodedParameter());
		menu2.fim();

		pagina.rodape(TextoPadrao.copyright());

		pagina.fim();

	}
}
