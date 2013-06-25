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

public class Logout extends Servlet {

public void action(javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	// Inicializa SessionManager
	SessionManager sessionManager = new SessionManager(request);

	sessionManager.finalizarSessao();

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Logout");
	pagina.javascript("window.location = 'LoginManager';");
	pagina.fim();
	
}
}
