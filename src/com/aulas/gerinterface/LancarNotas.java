package com.aulas.gerinterface;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *

 Histórico de Versões
 --------------------
 versão	Autor		Modificação
 ------	-----		-----------
 1.0	Gerosa 		Criação
 * 
 */

import com.aulas.modelos.*;
import com.aulas.util.*;
import com.aulas.util.sessionmanager.*;
import com.aulas.business.*;
import com.aulas.objinterface.*;

import java.util.*;
import java.io.*;

public class LancarNotas extends Servlet {
	
	private final static int qtdeCampos = 6;
	
public void action(javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	// Inicializa SessionManager
	SessionManager sessionManager = new SessionManager(request);
	
	// Somente professor tem acesso
	sessionManager.checaPermissaoProfessor();

	// A partir do toDo seleciona m�todo
	String toDo = (String) request.getParameter("toDo");
	if ((toDo == null) || (toDo.equals("")) || (toDo.equals("entradaDeDados"))) {
		entradaDeDados (sessionManager,out);

	} else if (toDo.equals("lancaNotas")) {
		lancaNotas (sessionManager,request,out);

	} else {
		throw new Exception ("Opção não reconhecida.");
	}

}
private void entradaDeDados(SessionManager sessionManager, PrintWriter out) throws Exception {

	String titulo = "Lançamento Avulso de Notas";

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,titulo);
	pagina.init();

	pagina.titulo (titulo);
	pagina.descricao("Preencha os dados nos campos abaixo:");

	Formulario form = new Formulario(pagina,"LancarNotas?"+sessionManager.generateEncodedParameter());
	form.hidden("toDo","lancaNotas");

	TabelaFormulario tab = new TabelaFormulario (pagina,4);

	for (int i = 0; i < qtdeCampos; i++) {
		
		tab.addCelula("código: ");
		tab.addCelula(form.textboxTexto("codigo"+i,"",11,20));
		tab.addCelula("Nota: ");
		tab.addCelula(form.textboxTexto("nota"+i,"",5,6));
	}

	tab.addCelulaCom2Botes(form.botaoSubmit("Lan�ar Notas"),form.button("Cancelar","window.location='GerenciaDisciplinas?"+sessionManager.generateEncodedParameter()+"'"));

	tab.fim();
	form.fim();

	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();


}
private void lancaNotas(SessionManager sessionManager, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	// Inicializa Página
	String titulo = "Lançamento Avulso de Notas";
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,titulo);
	pagina.init();
	pagina.titulo (titulo);
	pagina.descricao("Resultado do lan�amento.");
	pagina.saltaLinha();
	TabelaSimples tab = new TabelaSimples(pagina);

	// Inicializar gerente
	ListaGerada_ger listageradager = new ListaGerada_ger();
	
	// Atualiza notas
	for (int i = 0; i < qtdeCampos; i++) {
		String codigo = request.getParameter("codigo"+i);
		String nota = request.getParameter("nota"+i);

		if ((codigo != null) && !codigo.equals("") && (nota != null) && !nota.equals("")) {
			ListaGerada listaGer = listageradager.getElementByCodigoIdentificacao(codigo);
			if (listaGer == null) {
				tab.novaLinha();
				tab.addCelula(codigo);
				tab.addCelula("&nbsp;<FONT color=red> código não encontrado.</FONT>");
			} else {
				listageradager.alteraNota(listaGer,Double.parseDouble(nota));
				tab.novaLinha();
				tab.addCelula(codigo);
				tab.addCelula("&nbsp;<FONT color=blue> Nota lan�ada!</FONT>");
			}
		}
	}
	tab.fim();
	
	// Opções
	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Opções");
		menu2.addItem("Continuar Lançamento Avulso de Notas","LancarNotas?"+sessionManager.generateEncodedParameter());
		menu2.addItem("Lista de Disciplina","GerenciaDisciplinas?"+sessionManager.generateEncodedParameter());
		menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter());
	menu2.fim();

	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();


}
}
