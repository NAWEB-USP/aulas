package com.aulas.gerinterface;

import com.aulas.objinterface.*;

import java.util.*;
import java.io.*;

public class ExceptionHandler {
	

public static void securityException(PrintWriter out) throws Exception {
	
	PaginaHTML pagina = new PaginaHTML (null, out,"Session timeout");
	pagina.init();

	pagina.titulo ("Erro de segurança");

	pagina.descricao("Ocorreu em erro relativo a permissão de acesso.");
	pagina.saltaLinha();
	pagina.descricao("Por favor, clique <a href=\"LoginManager\">aqui</a> para fazer login novamente.");
	pagina.saltaLinha();
	pagina.saltaLinha();
		
	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();

}	
	

	
	


public static void sessionException(PrintWriter out) throws Exception {
	
	PaginaHTML pagina = new PaginaHTML (null, out,"Session timeout");
	pagina.init();

	pagina.titulo ("Tempo de Sessão Esgotado ou Sessão não Inicializada");

	pagina.descricao("Sua sessão foi finalizada por tempo de inatividade ou ela não foi inicializada de forma correta.");
	pagina.saltaLinha();
	pagina.descricao("Por favor, clique <a href=\"LoginManager\">aqui</a> para fazer login novamente.");
	pagina.saltaLinha();
	pagina.saltaLinha();
		
	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();

}	
	

	
	

}
