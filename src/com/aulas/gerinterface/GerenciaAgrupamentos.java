package com.aulas.gerinterface;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 11/02/2003 00:08:21 -- *
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

public class GerenciaAgrupamentos extends Servlet {


public void action(javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	// Variaveis comuns
	Lista lista = null;
	
	// Inicializa SessionManager
	SessionManager sessionManager = new SessionManager(request);

	// Somente professor tem acesso
	sessionManager.checaPermissaoProfessor();
	
	// Le lista
	String idLista = sessionManager.getElement("idLista");
	if ((idLista == null) || (idLista.equals(""))) {throw new Exception ("Falta a lista");}
	Lista_ger listager = new Lista_ger();
	lista = listager.getElement(idLista);
	
	// A partir do toDo seleciona m�todo
	String toDo = (String) request.getParameter("toDo");

	if (toDo.equals("cadastroAgrupamento")) {
		String idAgrupamento = request.getParameter("idAgrupamento");
		Agrupamento obj = null;
		if ((idAgrupamento != null) && (!idAgrupamento.equals(""))) {
			Agrupamento_ger ger = new Agrupamento_ger();
			obj = ger.getElementByCod(idAgrupamento);
		}
		cadastroAgrupamento(sessionManager,out,obj);

	} else if (toDo.equals("efetuaCadastro")) {
		efetuaCadastro(sessionManager,lista,request,out);

	} else if (toDo.equals("removerAgrupamento")) {
		String idAgrupamento = request.getParameter("idAgrupamento");
		if ((idAgrupamento == null) || (idAgrupamento.equals(""))) {throw new Exception ("Falta o cod");}
		Agrupamento_ger ger = new Agrupamento_ger();
		Agrupamento obj = ger.getElementByCod(idAgrupamento);

		removerAgrupamento (sessionManager,out, obj);

	} else {
		throw new Exception ("Opção não reconhecida.");
	}

}
private void cadastroAgrupamento(SessionManager sessionManager, PrintWriter out, Agrupamento obj) throws Exception {

	boolean criacao = (obj == null);

	String titulo = criacao ? "Criação de Agrupamento" : "Alteração de Agrupamento";

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,titulo);
	pagina.init();

	pagina.titulo (titulo);
	pagina.descricao("Preencha os dados nos campos abaixo:");

	Formulario form = new Formulario(pagina,"GerenciaAgrupamentos?"+sessionManager.generateEncodedParameter());
	form.hidden("toDo","efetuaCadastro");
	if (!criacao) form.hidden("cod",obj.getCod()); 

	TabelaFormulario tab = new TabelaFormulario (pagina);

		tab.addCelula("Nome: ");
		tab.addCelula(form.textboxTexto("nome",(criacao?"":obj.getNome()), 50, 255));
		tab.addCelula("Enunciado: ");
		tab.addCelula(form.textArea("enunciado",(criacao?"":obj.getEnunciado()), 3, 50));
		tab.addCelula("SeguirOrdem: ");
		tab.addCelula(form.checkbox("seguirOrdem","true",(criacao?true:obj.isSeguirOrdem())));
        tab.addCelula("randomizar uma vez para todos (excludente com o de cima): ");
        tab.addCelula(form.checkbox("randomizarSomenteUmaVez","false",(criacao?true:obj.isRandomizarSomenteUmaVez())));
		tab.addCelula("Distribuição Uniforme de Questões: ");
		tab.addCelula(form.checkbox("distribuicaoUniforme","true",(criacao?true:obj.isDistribuicaoUniforme())));
		tab.addCelula("numMaxQuestoes: ");
		tab.addCelula(form.textboxNumero("numMaxQuestoes",(criacao?"0":""+obj.getNumMaxQuestoes()), 4, 4));

		tab.novaLinha();
		tab.addCelulaCom2Botes(form.botaoSubmit(criacao ? "Criar" : "Alterar"),form.button("Cancelar","window.location='GerenciaQuestoes?toDo=montaFrames&"+sessionManager.generateEncodedParameter()+"'"));

	tab.fim();
	form.fim();

	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();


}
private void efetuaCadastro(SessionManager sessionManager, Lista lista, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	String cod = (String) request.getParameter("cod");
	Agrupamento obj = null;
	Agrupamento_ger ger = new Agrupamento_ger();
	if ((cod != null) && (!cod.equals(""))) {
		obj = ger.getElementByCod(cod);
	}

	boolean criacao = (obj == null);

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,criacao ? "Criação de Agrupamento" : "Alteração de Agrupamento");
	pagina.init();
	
	// Le dados do formulario
	boolean seguirOrdem = request.getParameter("seguirOrdem") != null;
    boolean randomizarSomenteUmaVez = request.getParameter("randomizarSomenteUmaVez") != null;
	boolean distribuicaoUniforme = request.getParameter("distribuicaoUniforme") != null;
	String nome = (String) request.getParameter("nome");
	String enunciado = (String) request.getParameter("enunciado");
	int numMaxQuestoes = Integer.parseInt(request.getParameter("numMaxQuestoes"));

	// Checa consist�ncia	
	String problema = ger.testaConsistencia(obj,seguirOrdem,randomizarSomenteUmaVez,nome,enunciado,numMaxQuestoes,lista);
	if (problema != null) {
		pagina.javascript("alert('Campo nulo ou repetido: "+problema+".'); window.history.back();");
		return;
	}

	// Efetua cadastro
	if (criacao) {
		obj = ger.inclui(seguirOrdem,randomizarSomenteUmaVez,distribuicaoUniforme,nome,enunciado,numMaxQuestoes,lista);
	} else {
		ger.altera(obj,seguirOrdem,randomizarSomenteUmaVez,distribuicaoUniforme,nome,enunciado,numMaxQuestoes,lista);
	}

	// Atualiza agrupamento no session manager
	sessionManager.addElement("codAgrup",obj.getCod());

	// Monta pagina de resposta
	if (criacao) {
		pagina.javascript("alert('Criação efetuada com sucesso.'); window.location = 'GerenciaQuestoes?toDo=montaFrames&"+sessionManager.generateEncodedParameter()+"';");
	} else {
		pagina.javascript("alert('Alteração efetuada com sucesso.'); window.location = 'GerenciaQuestoes?toDo=montaFrames&"+sessionManager.generateEncodedParameter()+"';");
	}
	
	pagina.fim();


}
private void removerAgrupamento(SessionManager sessionManager, PrintWriter out, Agrupamento obj) throws Exception {

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Remoção de Agrupamento");
	pagina.init();

	// Efetua exclusão
	Agrupamento_ger ger = new Agrupamento_ger();
	ger.remove(obj);

	// Monta pagina de resposta
	pagina.javascript("alert('Remoção efetuada com sucesso.'); window.location = 'GerenciaQuestoes?toDo=listaQuestoes&"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();

}
}
