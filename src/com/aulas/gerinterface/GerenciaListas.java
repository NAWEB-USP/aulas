package com.aulas.gerinterface;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 06/02/2003 10:13:41 -- *
 * -- Gerador versão 1.0                                       *
 * ----------------------------------------------------------- *

 Histórico de Versões
 --------------------
 versão	Autor		Modificação
 ------	-----		-----------
 1.0	Gerosa 		Geração automática da classe
 1.1 	Gerosa 		Inclusao do atributo disciplina, opcoes de escolher nova disciplina e listar apenas Listas das disciplina escolhida
 1.2	Gerosa		Tirado o seletor de disciplina
 
 * 
 */



import com.aulas.modelos.*;
import com.aulas.util.*;
import com.aulas.util.sessionmanager.*;
import com.aulas.business.*;
import com.aulas.objinterface.*;

import java.util.*;
import java.io.*;

public class GerenciaListas extends Servlet {


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
	if ((toDo == null) || (toDo.equals("")) || (toDo.equals("listaListas"))) {
		sessionManager.removeElement("idLista");
		listaListas (sessionManager,disc,out);

	} else if (toDo.equals("opcoesLista")) {
		String cod = (String) request.getParameter("cod");
		String idLista = cod;
		if ((cod == null) || (cod.equals(""))) {
			idLista = sessionManager.getElement("idLista");
		}
		if ((idLista == null) || (idLista.equals(""))) {throw new Exception ("Falta o cod");}
		sessionManager.addElement("idLista",idLista);
		Lista_ger ger = new Lista_ger();
		Lista obj = ger.getElement(idLista);

		opcoesLista (sessionManager,disc,out,obj);

	} else if (toDo.equals("cadastroLista")) {
		String cod = (String) request.getParameter("cod");
		String idLista = cod;
		if ((cod != null) && (!cod.equals(""))) {
			sessionManager.addElement("idLista",idLista);
		} else {
			idLista = sessionManager.getElement("idLista");
		}
		Lista obj = null;
		if ((idLista != null) && (!idLista.equals(""))) {
			Lista_ger ger = new Lista_ger();
			obj = ger.getElement(idLista);
		}

		cadastroLista(sessionManager,disc,out,obj);

	} else if (toDo.equals("inclusaoLista")) {
		cadastroLista(sessionManager,disc,out,null);
		
	} else if (toDo.equals("efetuaCadastro")) {
		efetuaCadastro(sessionManager,disc,request,out);

	} else if (toDo.equals("removerLista")) {
		String cod = (String) request.getParameter("cod");
		String idLista = cod;
		if ((idLista == null) || (idLista.equals(""))) {throw new Exception ("Falta o cod");}
		Lista_ger ger = new Lista_ger();
		Lista obj = ger.getElement(idLista);

		removerLista (sessionManager,disc,out,obj);

	} else {
		throw new Exception ("Opção não reconhecida.");
	}

}
private void cadastroLista(SessionManager sessionManager, Disciplina disc, PrintWriter out, Lista obj) throws Exception {

	boolean criacao = (obj == null);

	String titulo = criacao ? "Criação de Lista" : "Alteração de Lista";

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,titulo);
	pagina.init();

	pagina.titulo (titulo);
	pagina.descricao("Preencha os dados nos campos abaixo:");

	Formulario form = new Formulario(pagina,"GerenciaListas?"+sessionManager.generateEncodedParameter());
	form.hidden("toDo","efetuaCadastro");
	if (!criacao) form.hidden("cod",obj.getCod()); 

	TabelaFormulario tab = new TabelaFormulario (pagina);

		tab.addCelula("Nome: ");
		tab.addCelula(form.textboxTexto("nome",(criacao?"":obj.getNome()), 50, 255));
		tab.addCelula("Enunciado: ");
		tab.addCelula(form.textboxTexto("enunciado",(criacao?"":obj.getEnunciado()), 50, 255));
		tab.addCelula("Num Min Alunos Por Grupo: ");
		tab.addCelula(form.textboxNumero("numMinAlunosPorGrupo",(criacao?"1":""+obj.getNumMinAlunosPorGrupo()), 4, 4));
		tab.addCelula("Num Max Alunos Por Grupo: ");
		tab.addCelula(form.textboxNumero("numMaxAlunosPorGrupo",(criacao?"1":""+obj.getNumMaxAlunosPorGrupo()), 4, 4));
		tab.addCelula("Seguir Ordem dos Agrupamentos: ");
		tab.addCelula(form.checkbox("seguirOrdemQuestoes","true",(criacao?true:obj.isSeguirOrdemQuestoes())));
		tab.addCelula("Auto Correção: ");
		tab.addCelula(form.checkbox("autoCorrection","true",(criacao?true:obj.isAutoCorrection())));
		tab.addCelula("Permitir Testar: ");
		tab.addCelula(form.checkbox("permitirTestar","true",(criacao?false:obj.isPermitirTestar())));

		tab.novaLinha();
		StringBuffer outString = new StringBuffer(); // Acumulador da sa�da
		outString.append("<TABLE border=1 cellpadding=3 cellspacing=0 bordercolor=black>");
		
		Turma_ger turmager = new Turma_ger();
		Vector turmas = turmager.getElements(disc);

		// titulos
		outString.append("<TR>");
		outString.append("<TD align=center><B>padrão</B></td>");
		for (int i = 0; i < turmas.size(); i++) {
			Turma turma = (Turma) turmas.elementAt(i);
			outString.append("<TD><B>"+turma.getNome()+"</B></TD>");
		}
		outString.append("</TR>");
		
		// seguir padrao
		outString.append("<TR>");
		outString.append("<TD nowrap>&nbsp;</TD>");
		for (int i = 0; i < turmas.size(); i++) {
			Turma turma = (Turma) turmas.elementAt(i);
			outString.append("<TD nowrap>"+form.checkbox("seguirPadrao"+turma.getCod(),"true",(criacao?true:turma.isSeguirPadrao(obj)))+" Seguir padrão");
		}
		outString.append("</TR>");

		
		// ativa
		outString.append("<TR>");
		outString.append("<TD nowrap>Ativa: "+ form.checkbox("ativa","true",(criacao?true:obj.isAtiva())));
		for (int i = 0; i < turmas.size(); i++) {
			Turma turma = (Turma) turmas.elementAt(i);
			outString.append("<TD nowrap>Ativa: "+ form.checkbox("ativa"+turma.getCod(),"true",(criacao?true:turma.isAtiva(obj))));
		}
		outString.append("</TR>");

		// Data1
		outString.append("<TR>");
		outString.append("<TD nowrap>Data1: "+ form.textboxData("data1",(criacao?null:obj.getData1())));
		for (int i = 0; i < turmas.size(); i++) {
			Turma turma = (Turma) turmas.elementAt(i);
			outString.append("<TD nowrap>Data1: "+ form.textboxData("data1"+turma.getCod(),(criacao?null:turma.getData1(obj))));
		}
		outString.append("</TR>");
			
		// Peso1
		outString.append("<TR>");
		outString.append("<TD nowrap>Peso1: "+ form.textboxNumero("peso1",(criacao?"100":""+obj.getPeso1()), 4, 4));
		for (int i = 0; i < turmas.size(); i++) {
			Turma turma = (Turma) turmas.elementAt(i);
			outString.append("<TD nowrap>Peso1: "+ form.textboxNumero("peso1"+turma.getCod(),(criacao?"100":""+turma.getPeso1(obj)), 4, 4));
		}
		outString.append("</TR>");

		// Data2
		outString.append("<TR>");
		outString.append("<TD nowrap>Data2: "+ form.textboxData("data2",(criacao?null:obj.getData2())));
		for (int i = 0; i < turmas.size(); i++) {
			Turma turma = (Turma) turmas.elementAt(i);
			outString.append("<TD nowrap>Data2: "+ form.textboxData("data2"+turma.getCod(),(criacao?null:turma.getData2(obj))));
		}
		outString.append("</TR>");
			
		// Peso2
		outString.append("<TR>");
		outString.append("<TD nowrap>Peso2: "+ form.textboxNumero("peso2",(criacao?"0":""+obj.getPeso2()), 4, 4));
		for (int i = 0; i < turmas.size(); i++) {
			Turma turma = (Turma) turmas.elementAt(i);
			outString.append("<TD nowrap>Peso2: "+ form.textboxNumero("peso2"+turma.getCod(),(criacao?"0":""+turma.getPeso2(obj)), 4, 4));
		}
		outString.append("</TR>");

		// Data3
		outString.append("<TR>");
		outString.append("<TD nowrap>Data3: "+ form.textboxData("data3",(criacao?null:obj.getData3())));
		for (int i = 0; i < turmas.size(); i++) {
			Turma turma = (Turma) turmas.elementAt(i);
			outString.append("<TD nowrap>Data3: "+ form.textboxData("data3"+turma.getCod(),(criacao?null:turma.getData3(obj))));
		}
		outString.append("</TR>");
			
		// Peso3
		outString.append("<TR>");
		outString.append("<TD nowrap>Peso3: "+ form.textboxNumero("peso3",(criacao?"0":""+obj.getPeso3()), 4, 4));
		for (int i = 0; i < turmas.size(); i++) {
			Turma turma = (Turma) turmas.elementAt(i);
			outString.append("<TD nowrap>Peso3: "+ form.textboxNumero("peso3"+turma.getCod(),(criacao?"0":""+turma.getPeso3(obj)), 4, 4));
		}
		outString.append("</TR>");

		// Data4
		outString.append("<TR>");
		outString.append("<TD nowrap>Data4: "+ form.textboxData("data4",(criacao?null:obj.getData4())));
		for (int i = 0; i < turmas.size(); i++) {
			Turma turma = (Turma) turmas.elementAt(i);
			outString.append("<TD nowrap>Data4: "+ form.textboxData("data4"+turma.getCod(),(criacao?null:turma.getData4(obj))));
		}
		outString.append("</TR>");
			
		// Peso4
		outString.append("<TR>");
		outString.append("<TD nowrap>Peso4: "+ form.textboxNumero("peso4",(criacao?"0":""+obj.getPeso4()), 4, 4));
		for (int i = 0; i < turmas.size(); i++) {
			Turma turma = (Turma) turmas.elementAt(i);
			outString.append("<TD nowrap>Peso4: "+ form.textboxNumero("peso4"+turma.getCod(),(criacao?"0":""+turma.getPeso4(obj)), 4, 4));
		}
		outString.append("</TR>");

		// Data5
		outString.append("<TR>");
		outString.append("<TD nowrap>Data5: "+ form.textboxData("data5",(criacao?null:obj.getData5())));
		for (int i = 0; i < turmas.size(); i++) {
			Turma turma = (Turma) turmas.elementAt(i);
			outString.append("<TD nowrap>Data5: "+ form.textboxData("data5"+turma.getCod(),(criacao?null:turma.getData5(obj))));
		}
		outString.append("</TR>");
			
		// Peso5
		outString.append("<TR>");
		outString.append("<TD nowrap>Peso5: "+ form.textboxNumero("peso5",(criacao?"0":""+obj.getPeso5()), 4, 4));
		for (int i = 0; i < turmas.size(); i++) {
			Turma turma = (Turma) turmas.elementAt(i);
			outString.append("<TD nowrap>Peso5: "+ form.textboxNumero("peso5"+turma.getCod(),(criacao?"0":""+turma.getPeso5(obj)), 4, 4));
		}
		outString.append("</TR>");

		// Data6
		outString.append("<TR>");
		outString.append("<TD nowrap>Data6: "+ form.textboxData("data6",(criacao?null:obj.getData6())));
		for (int i = 0; i < turmas.size(); i++) {
			Turma turma = (Turma) turmas.elementAt(i);
			outString.append("<TD nowrap>Data6: "+ form.textboxData("data6"+turma.getCod(),(criacao?null:turma.getData6(obj))));
		}
		outString.append("</TR>");
			
		// Peso6
		outString.append("<TR>");
		outString.append("<TD>Peso6: "+ form.textboxNumero("peso6",(criacao?"0":""+obj.getPeso6()), 4, 4));
		for (int i = 0; i < turmas.size(); i++) {
			Turma turma = (Turma) turmas.elementAt(i);
			outString.append("<TD>Peso6: "+ form.textboxNumero("peso6"+turma.getCod(),(criacao?"0":""+turma.getPeso6(obj)), 4, 4));
		}
		outString.append("</TR>");
				
		outString.append("</TABLE>");

		tab.addCelulaWithColSpan(outString.toString(),2);

		tab.novaLinha();
		tab.addCelulaCom2Botes(form.botaoSubmit(criacao ? "Criar" : "Alterar"),form.button("Cancelar","window.location='GerenciaDisciplinas?toDo=opcoesDisciplina&"+sessionManager.generateEncodedParameter()+"'"));

	tab.fim();

	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();


}
private void efetuaCadastro(SessionManager sessionManager, Disciplina disc, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	String cod = (String) request.getParameter("cod");
	Lista obj = null;
	Lista_ger ger = new Lista_ger();
	if ((cod != null) && (!cod.equals(""))) {
		obj = ger.getElement(cod);
	}

	boolean criacao = (obj == null);

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,criacao ? "Criação de Lista" : "Alteração de Lista");
	pagina.init();
	
	// Le dados do formulario
	String nome = (String) request.getParameter("nome");
	String enunciado = (String) request.getParameter("enunciado");
	int numMinAlunosPorGrupo = Integer.parseInt(request.getParameter("numMinAlunosPorGrupo"));
	int numMaxAlunosPorGrupo = Integer.parseInt(request.getParameter("numMaxAlunosPorGrupo"));
	boolean autoCorrection = request.getParameter("autoCorrection") != null;
	boolean seguirOrdemQuestoes = request.getParameter("seguirOrdemQuestoes") != null;
	boolean permitirTestar = request.getParameter("permitirTestar") != null;
	boolean ativa = request.getParameter("ativa") != null;
	java.util.Date data1 = Data.novaData(request.getParameter("data1d"),request.getParameter("data1m"),request.getParameter("data1a"));
	int peso1 = Integer.parseInt(request.getParameter("peso1"));
	java.util.Date data2 = Data.novaData(request.getParameter("data2d"),request.getParameter("data2m"),request.getParameter("data2a"));
	int peso2 = Integer.parseInt(request.getParameter("peso2"));
	java.util.Date data3 = Data.novaData(request.getParameter("data3d"),request.getParameter("data3m"),request.getParameter("data3a"));
	int peso3 = Integer.parseInt(request.getParameter("peso3"));
	java.util.Date data4 = Data.novaData(request.getParameter("data4d"),request.getParameter("data4m"),request.getParameter("data4a"));
	int peso4 = Integer.parseInt(request.getParameter("peso4"));
	java.util.Date data5 = Data.novaData(request.getParameter("data5d"),request.getParameter("data5m"),request.getParameter("data5a"));
	int peso5 = Integer.parseInt(request.getParameter("peso5"));
	java.util.Date data6 = Data.novaData(request.getParameter("data6d"),request.getParameter("data6m"),request.getParameter("data6a"));
	int peso6 = Integer.parseInt(request.getParameter("peso6"));

	Turma_ger turmager = new Turma_ger();
	Enumeration turmas = turmager.getElements(disc).elements();
	Vector ts = new Vector();
	Vector listas_seguirPadrao = new Vector();
	Vector listas_ativa = new Vector();
	Vector listas_data1 = new Vector();
	Vector listas_peso1 = new Vector();
	Vector listas_data2 = new Vector();
	Vector listas_peso2 = new Vector();
	Vector listas_data3 = new Vector();
	Vector listas_peso3 = new Vector();
	Vector listas_data4 = new Vector();
	Vector listas_peso4 = new Vector();
	Vector listas_data5 = new Vector();
	Vector listas_peso5 = new Vector();
	Vector listas_data6 = new Vector();
	Vector listas_peso6 = new Vector();
	while (turmas.hasMoreElements()) {
		Turma t = (Turma) turmas.nextElement();
		ts.addElement(t);
		listas_seguirPadrao.addElement(new Boolean(request.getParameter("seguirPadrao"+t.getCod()) != null));
		listas_ativa.addElement(new Boolean(request.getParameter("ativa"+t.getCod()) != null));
		listas_data1.addElement(Data.novaData(request.getParameter("data1"+t.getCod()+"d"),request.getParameter("data1"+t.getCod()+"m"),request.getParameter("data1"+t.getCod()+"a")));
		listas_peso1.addElement((String) request.getParameter("peso1"+t.getCod()));
		listas_data2.addElement(Data.novaData(request.getParameter("data2"+t.getCod()+"d"),request.getParameter("data2"+t.getCod()+"m"),request.getParameter("data2"+t.getCod()+"a")));
		listas_peso2.addElement((String) request.getParameter("peso2"+t.getCod()));
		listas_data3.addElement(Data.novaData(request.getParameter("data3"+t.getCod()+"d"),request.getParameter("data3"+t.getCod()+"m"),request.getParameter("data3"+t.getCod()+"a")));
		listas_peso3.addElement((String) request.getParameter("peso3"+t.getCod()));
		listas_data4.addElement(Data.novaData(request.getParameter("data4"+t.getCod()+"d"),request.getParameter("data4"+t.getCod()+"m"),request.getParameter("data4"+t.getCod()+"a")));
		listas_peso4.addElement((String) request.getParameter("peso4"+t.getCod()));
		listas_data5.addElement(Data.novaData(request.getParameter("data5"+t.getCod()+"d"),request.getParameter("data5"+t.getCod()+"m"),request.getParameter("data5"+t.getCod()+"a")));
		listas_peso5.addElement((String) request.getParameter("peso5"+t.getCod()));
		listas_data6.addElement(Data.novaData(request.getParameter("data6"+t.getCod()+"d"),request.getParameter("data6"+t.getCod()+"m"),request.getParameter("data6"+t.getCod()+"a")));
		listas_peso6.addElement((String) request.getParameter("peso6"+t.getCod()));
	}
	listas_seguirPadrao.trimToSize();
	listas_ativa.trimToSize();
	listas_data1.trimToSize();
	listas_peso1.trimToSize();
	listas_data2.trimToSize();
	listas_peso2.trimToSize();
	listas_data3.trimToSize();
	listas_peso3.trimToSize();
	listas_data4.trimToSize();
	listas_peso4.trimToSize();
	listas_data5.trimToSize();
	listas_peso5.trimToSize();
	listas_data6.trimToSize();
	listas_peso6.trimToSize();

	// Checa consist�ncia	
	String problema = ger.testaConsistencia(obj,nome,enunciado,numMinAlunosPorGrupo,numMaxAlunosPorGrupo,seguirOrdemQuestoes,autoCorrection,permitirTestar,ativa,data1,peso1,data2,peso2,data3,peso3,data4,peso4,data5,peso5,data6,peso6,disc);
	if (problema != null) {
		pagina.javascript("alert('Campo nulo ou repetido: "+problema+".'); window.history.back();");
		return;
	}

	// Efetua cadastro
	if (criacao) {
		ger.inclui(nome,enunciado,numMinAlunosPorGrupo,numMaxAlunosPorGrupo,seguirOrdemQuestoes,autoCorrection,permitirTestar,ativa,data1,peso1,data2,peso2,data3,peso3,data4,peso4,data5,peso5,data6,peso6,disc,ts,listas_seguirPadrao,listas_ativa,listas_data1,listas_peso1,listas_data2,listas_peso2,listas_data3,listas_peso3,listas_data4,listas_peso4,listas_data5,listas_peso5,listas_data6,listas_peso6);
	} else {
		ger.altera(obj,nome,enunciado,numMinAlunosPorGrupo,numMaxAlunosPorGrupo,seguirOrdemQuestoes,autoCorrection,permitirTestar,ativa,data1,peso1,data2,peso2,data3,peso3,data4,peso4,data5,peso5,data6,peso6,disc,ts,listas_seguirPadrao,listas_ativa,listas_data1,listas_peso1,listas_data2,listas_peso2,listas_data3,listas_peso3,listas_data4,listas_peso4,listas_data5,listas_peso5,listas_data6,listas_peso6);
	}

	// Monta pagina de resposta
	if (criacao) {
		pagina.javascript("alert('Criação efetuada com sucesso.'); window.location = 'GerenciaDisciplinas?toDo=opcoesDisciplina&"+sessionManager.generateEncodedParameter()+"';");
	} else {
		pagina.javascript("alert('Alteração efetuada com sucesso.'); window.location = 'GerenciaDisciplinas?toDo=opcoesDisciplina&"+sessionManager.generateEncodedParameter()+"';");
	}
	
	pagina.fim();


}
private void listaListas(SessionManager sessionManager, Disciplina disc, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Listas");
	pagina.init();

	pagina.titulo ("Listas");

	Lista_ger ger = new Lista_ger();
	Enumeration lista = ger.getElements(disc).elements();

	Resposta_ger respger = new Resposta_ger();

	pagina.descricao("Escolha abaixo a Lista.");
	pagina.saltaLinha();
	Menu menu = new Menu (pagina,"Listas");
	if (!lista.hasMoreElements()) {
		pagina.descricao("não há Listas cadastradas.");
	} else {
		while (lista.hasMoreElements()) {
			Lista obj = (Lista) lista.nextElement();
			int numRespostasNovas = respger.getNumRespostasNovasByLista(obj);
			menu.addItem((numRespostasNovas==0)?obj.getNome():"<B>"+obj.getNome()+" ("+numRespostasNovas+" novas respostas)</B>","GerenciaListas?toDo=opcoesLista&cod="+obj.getCod()+"&"+sessionManager.generateEncodedParameter());
		}
	}
	menu.fim();


	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opções");
		menu2.addItem("Criar nova Lista","GerenciaListas?toDo=cadastroLista"+"&"+sessionManager.generateEncodedParameter());
		menu2.addItem("Escolher outra Disciplina","GerenciaDisciplinas?"+sessionManager.generateEncodedParameter());
		menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter());
	menu2.fim();


	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();


}
private void opcoesLista(SessionManager sessionManager, Disciplina disc, PrintWriter out, Lista obj) throws Exception {

	/*PaginaHTML pagina = new PaginaHTML (out,"Opções da "+StringConverter.concatenateWithoutRepetion("Lista",obj.getNome()));
	pagina.init();

	pagina.titulo ("Opções da "+StringConverter.concatenateWithoutRepetion("Lista",obj.getNome()));

	pagina.descricao("Escolha abaixo a Opção.");
	pagina.saltaLinha();

	Menu menu = new Menu (pagina,"Opções");
	menu.addItem("Gerenciar Questões","GerenciaQuestoes?"+sessionManager.generateEncodedParameter());
	menu.addItem("Alterar dados","GerenciaListas?toDo=cadastroLista&"+sessionManager.generateEncodedParameter());
	menu.addItem("Remover Lista","javascript:if (confirm('Confirma exclusão?')) window.location='GerenciaListas?toDo=removerLista&"+sessionManager.generateEncodedParameter()+"';");
	menu.fim();

	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opções");
		menu2.addItem("Voltar","GerenciaDisciplinas?toDo=opcoesDisciplina&"+sessionManager.generateEncodedParameter());
		menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter());
	menu2.fim();


	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();*/

	

}
private void removerLista(SessionManager sessionManager, Disciplina disc, PrintWriter out, Lista obj) throws Exception {

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Remoção de Lista");
	pagina.init();

	// Efetua exclusão
	Lista_ger ger = new Lista_ger();
	ger.remove(obj);

	// Monta pagina de resposta
	pagina.javascript("alert('Remoção efetuada com sucesso.'); window.location = 'GerenciaDisciplinas?toDo=opcoesDisciplina&"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();

}
}
