package com.aulas.util;

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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;

import com.aulas.util.sessionmanager.SessionManager;

public class Erro {

private static String processaEnumeration(Enumeration e) {

	String str = "";
	while (e.hasMoreElements()) {
		Object obj = e.nextElement();
		str += obj.toString()+" || ";
	}
	return str;
	
}
// Trata as Excessoes
//
// Se o outWriter nao estiver disponivel, passar null como parametro
// 
//
// Deve-se atentar de nao usar aqui nenhuma funcao que quando gerar Excessao chame estre metodo, pois cria um loop.

public static void trataExcecao (Exception ex, PrintWriter out, javax.servlet.http.HttpServletRequest request, SessionManager session) {

	boolean debug = true;
	boolean mandaMensagem = true;
	
	// * Declara variaveis *
	StringBuffer mensagem = new StringBuffer();
	Connection dbCon;
	Statement dbStmt;
	String str;
	ResultSet aRs;
	String agora;
	String titMensagemErro;
	String descMensagemErro;

	
	// * Finaliza sistema *
	try {
		System.runFinalization();
	} catch (Exception ex2) {}
	
	
	// * Preenche t�tulo e Descrição da Página *
	titMensagemErro = "Ocorreu um erro";
	descMensagemErro = "Enquanto sua ação era processada, ocorreu um erro interno. Por favor notifique o professor.";

	// * Le data atual *
	try {
		agora = Data.agora();
	}
	catch (Exception ex2) {
		agora = "não foi possível ler a hora atual";
	}
	
	// * Monta dados do erro *
	try {
		mensagem.append("\n Veja abaixo os dados do erro:\n");
		mensagem.append("\n <B>Data do erro: </B>" + agora);

		// Ambiente de execução
		mensagem.append("\n");
		mensagem.append("\n <B>Mensagem: </B>" + ex.getMessage());
		mensagem.append("\n <B>Classe: </B>" + ex.getClass());
		
		// Requisição
		mensagem.append("\n\n<B>Requisição:</B>");
		mensagem.append("\n <B>URI = </B>" + request.getRequestURI());
		mensagem.append("\n <B>QueryString = </B>" + request.getQueryString());
		mensagem.append("\n <B>ParameterNames = </B>" + processaEnumeration(request.getParameterNames()));
		mensagem.append("\n <B>Protocol = </B>" + request.getProtocol());
		mensagem.append(" &nbsp; <B>Method = </B>" + request.getMethod());

		// Cliente
		mensagem.append("\n\n<B>Cliente:</B>");
		mensagem.append("\n <B>HeaderNames = </B>" + processaEnumeration(request.getHeaderNames()));
		Enumeration e = request.getHeaderNames();
		while (e.hasMoreElements()) {
			String header = (String) e.nextElement();
			mensagem.append("\n <B>"+header+" = </B>" + request.getHeader(header));
		}		
		mensagem.append("\n <B>Locales = </B>" + processaEnumeration(request.getLocales()));
		mensagem.append("\n <B>RemoteAddr = </B>" + request.getRemoteAddr());
		mensagem.append("\n <B>RemoteHost = </B>" + request.getRemoteHost());
		mensagem.append("\n <B>RemoteUser = </B>" + request.getRemoteUser());

		// Servidor
		mensagem.append("\n\n<B>Servidor:</B>");
		mensagem.append("\n <B>ServerName = </B>" + request.getServerName());
		mensagem.append("\n <B>ServerPort = </B>" + request.getServerPort());
		mensagem.append("\n <B>ContextPath = </B>" + request.getContextPath());
		
		try {
			mensagem.append("\n <B>IdUsuario = </B>" + request.getSession().getAttribute("idUsuario"));	
		} catch (Exception e2) {
		}
		
		// -- parametros do request não utilizados --
		//mensagem.append("\n getScheme: " + request.getScheme());
		//mensagem += "\n getServletPath: " + request.getServletPath();
		//mensagem += "\n getCookies: " + request.getCookies();
		//mensagem += "\n getHeader: " + request.getHeader();
		//mensagem += "\n getHeaders: " + request.getHeaders();
		//mensagem += "\n getPathInfo: " + request.getPathInfo();
		//mensagem += "\n getPathTranslated: " + request.getPathTranslated();
		//mensagem += "\n getRequestedSessionId: " + request.getRequestedSessionId();
		//mensagem += "\n getAttributeNames: " + processaEnumeration(request.getAttributeNames());
		//mensagem += "\n getAttribute: " + request.getAttribute("filters.ExampleFilter.PATH_MAPPED");
		//mensagem += "\n getAuthType: " + request.getAuthType();
		//mensagem += "\n getCharacterEncoding: " + request.getCharacterEncoding();
		//mensagem += "\n getContentType: " + request.getContentType();
		//mensagem += "\n getSession: " + request.getSession();
		//mensagem += "\n getUserPrincipal: " + request.getUserPrincipal();
		//mensagem += "\n isSecure: " + request.isSecure();
		//mensagem += "\n toString: " + request.toString();

	}
	catch (Exception ex2) {
		mensagem.append("não foi possível montar a mensagem.\n\n" + ex2.toString());
	}

	// * Anexa pilha de execução *
	try {
		StringWriter sw = new StringWriter();
		PrintWriter  pw = new PrintWriter(sw);
		ex.printStackTrace(pw);

		pw.close();
		sw.close();
		mensagem.append("\n\n<B>Pilha de Execução: </B>\n" + sw.toString());
		
	} catch (Exception ex2) {
		mensagem.append("\n\nnão foi possível anexar a pilha de execução.\n\n" + ex2.toString());
	}
	
	String mensagem2 = "\n\nÚltimos comandos SQL:\n";
	try {
		mensagem2 += BancoDadosLog.lastCommands();
	} catch (Exception ex2) {
		mensagem.append("\n\nnão foi possível anexar os comandos sql.\n\n" + ex2.toString());
	}

	// * Envia Mensagem para os desenvolvedores *
	try {
		if (mandaMensagem) {
			String titulo = "[Aulas] Mensagem automática de ocorrência de Erro";
			String nome   = "Mensagem de Erro";

			// Envia a mensagem
        	SendMail sender = new SendMail();
			sender.addTo("gerosa@ime.usp.br", "gerosa@ime.usp.br");
			sender.setAddressFrom("gerosa@ime.usp.br");
            sender.setNameFrom(nome);
            sender.setMessageSubject(titulo);
            sender.setMessageText(mensagem.toString() + mensagem2);
            SendMailThread send = new SendMailThread();
            send.setSendMail(sender);
            send.start();
		}
	}
	catch (Exception ex2) {}
		
	// * Escreve mensagem na tela do usuario *
	try {
		if (out != null) {
			out.print("<BR><BR><span class=Titulo><font color=red>" + titMensagemErro + "</font></span><BR><BR><span class=Descricao>" + descMensagemErro);
			if (debug) {
				out.print(StringConverter.toHtmlNotation(mensagem.toString()));
			}
			out.print("</span>");
		}

	} catch (Exception ex2) {}

	// * Finaliza programa *
	try {
		System.runFinalization();
		
	} catch (Exception ex2) {}

}
}
