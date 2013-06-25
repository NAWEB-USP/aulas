package com.aulas.gerinterface;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *

 Histórico de Versões
 --------------------
 versão	Autor		Modificação
 ------	-----		-----------
 1.0	Gerosa 		Criação da classe
 * 
 */



import com.aulas.modelos.*;
import com.aulas.util.*;
import com.aulas.util.sessionmanager.*;
import com.aulas.business.*;
import com.aulas.objinterface.*;

import java.util.*;
import java.io.*;

public class LeArquivo extends javax.servlet.http.HttpServlet {
public void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {

	try	{

		// Variaveis comuns
		Aluno aluno = null;
		Disciplina disc = null;
		Lista lista = null;
		ListaGerada listaGerada = null;
		Turma turma = null;
		boolean isProfessor;

	 	// Inicializa SessionManager
		SessionManager sessionManager = new SessionManager(request);

		// Le Usu�rio
		String idAluno = sessionManager.getElement("idUsuario");
		Aluno_ger alunoger = new Aluno_ger();
		Resposta resp;
		if (idAluno.equals("0")) {
			// PROFESSOR
			sessionManager.checaPermissaoProfessor();
			Resposta_ger respger = new Resposta_ger();
			resp = respger.getElementByCod(request.getParameter("resposta"));
		} else {
			// ALUNO
			aluno = alunoger.getElement(idAluno);

			// Le disciplina
			String idDisciplina = sessionManager.getElement("idDisciplina");
			Disciplina_ger disciplinager = new Disciplina_ger();
			disc = disciplinager.getElement(idDisciplina);

			// Le turma
			String idTurma = sessionManager.getElement("idTurma");
			Turma_ger turmager = new Turma_ger();
			turma = turmager.getElement(idTurma);

			// Le lista
			String cod = (String) request.getParameter("codLista");
			String idLista = cod;
			if ((cod == null) || (cod.equals(""))) {
				idLista = sessionManager.getElement("idLista");
			} else {
				sessionManager.addElement("idLista",idLista);
			}
			Lista_ger listager = new Lista_ger();
			lista = listager.getElement(idLista);

			// Le ListaGerada
			ListaGerada_ger listageradager = new ListaGerada_ger();
			listaGerada = listageradager.getElement(lista,aluno);

			// Recupera a questão
			String idQuestao = request.getParameter("questao");
			Questao_ger questger = new Questao_ger();
			Questao questao = questger.getElementByCod(idQuestao);

			// Recupera a resposta
			resp = listaGerada.getResposta(questao);

		}
		
		// Pega o nome do arquivo
		String filePath = resp.getResposta();
		String fileName = filePath;
	    if (fileName.lastIndexOf("\\") != -1) {
			fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
	    }
	    if (fileName.lastIndexOf("/") != -1) {
			fileName = fileName.substring(fileName.lastIndexOf("/")+1);
	    }		

		// Transmite o conte�do do arquivo
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		OutputStream out = response.getOutputStream();

		File arquivo = new File (filePath);
//PrintWriter out = response.getWriter();
//out.println(filePath);
		FileInputStream entrada = new FileInputStream(arquivo);
		int dados;
		while ((dados = entrada.read()) != -1) {
			out.write(dados);
		}
		entrada.close();
		out.close();

	}
	// --- Tratamento de Exce��es ---
	catch (SessionException ex) {
		PrintWriter out = null;
		try {
			ExceptionHandler.sessionException(out);
			out.close();
		} catch (Exception ex1) {
			Erro.trataExcecao(ex1,out,request,null);
			out.close();
		}
	}
	catch (com.aulas.util.sessionmanager.SecurityException ex) {
		PrintWriter out = null;
		try {
			ExceptionHandler.securityException(out);
			out.close();
		} catch (Exception ex1) {
			Erro.trataExcecao(ex1,out,request,null);
			out.close();
		}
	}
	catch (Exception ex) {
		PrintWriter out = null;
		SessionManager sessionManager = null;
		try {
			sessionManager = new SessionManager(request);
		} catch(Exception ex2) {};
		Erro.trataExcecao(ex,out,request,sessionManager);
		out.close();
	}

}
}
