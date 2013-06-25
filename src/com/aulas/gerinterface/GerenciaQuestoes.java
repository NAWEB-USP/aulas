package com.aulas.gerinterface;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 11/02/2003 00:48:55 -- *
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

public class GerenciaQuestoes extends Servlet {


public void action(javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	// Variaveis comuns
	Disciplina disc;
	Lista lista;
	
	// Inicializa SessionManager
	SessionManager sessionManager = new SessionManager(request);

	// Somente professor tem acesso
	sessionManager.checaPermissaoProfessor();

	// Le disciplina 
	String idDisciplina = sessionManager.getElement("idDisciplina");
	if ((idDisciplina == null) || (idDisciplina.equals(""))) {throw new Exception ("Falta a disciplina");}
	Disciplina_ger discger = new Disciplina_ger();
	disc = discger.getElement(idDisciplina);

	// Le lista
	String idLista = (String) request.getParameter("idLista"); // quando entrar no gerenciador
	if ((idLista == null) || (idLista.equals(""))) {
		idLista = sessionManager.getElement("idLista");
	} else {
		sessionManager.addElement("idLista",idLista);
		sessionManager.removeElement("codAgrup"); // zera agrupamento anterior
	}
	if ((idLista == null) || (idLista.equals(""))) {throw new Exception ("Falta a lista");}
	Lista_ger listager = new Lista_ger();
	lista = listager.getElement(idLista);

	// A partir do toDo seleciona m�todo
	String toDo = (String) request.getParameter("toDo");
	if ((toDo == null) || (toDo.equals(""))) {
		montaFrames(sessionManager,disc,lista,out);
		sessionManager.removeElement("codAgrup"); // tira o codAgrup do sessionmanager

	} else if (toDo.equals("montaFrames")) {
		montaFrames(sessionManager,disc,lista,out);

	} else if (toDo.equals("listaQuestoes")) {
		listaQuestoes (sessionManager,disc,lista,out);

	} else if (toDo.equals("cadastroQuestao")) {
		String codAgrup = (String) request.getParameter("selectAgrup");
		sessionManager.addElement("codAgrup",codAgrup);
		String idQuestao = (String) request.getParameter("idQuestao");
		Questao obj = null;
		if ((idQuestao != null) && (!idQuestao.equals(""))) {
			Questao_ger ger = new Questao_ger();
			obj = ger.getElementByCod(idQuestao);
		}

		cadastroQuestao(sessionManager,disc,lista,out,obj,"");

	} else if (toDo.equals("efetuaCadastro")) {
		efetuaCadastro(sessionManager,disc,lista,request,out,false);

	} else if (toDo.equals("continuarCriando")) {
		efetuaCadastro(sessionManager,disc,lista,request,out,true);
		
	} else if (toDo.equals("removerQuestao")) {
		String idQuestao = (String) request.getParameter("idQuestao");
		if ((idQuestao == null) || (idQuestao.equals(""))) {throw new Exception ("Falta o cod");}
		Questao_ger ger = new Questao_ger();
		Questao obj = ger.getElementByCod(idQuestao);

		removerQuestao (sessionManager,disc,lista,out, obj);

	} else if (toDo.equals("atualizaPesos")) {
		atualizaPesos(sessionManager,disc,lista,request,out);

	} else {
		throw new Exception ("Opção não reconhecida: "+toDo);
	}

}
private void atualizaPesos(SessionManager sessionManager, Disciplina disc, Lista lista, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Alteração de Pesos");
	pagina.init();

	// Atualiza
	Agrupamento_ger agrupger = new Agrupamento_ger();
	Enumeration agrupamentos = agrupger.getElements(lista).elements();
	while (agrupamentos.hasMoreElements()) {
		Agrupamento agrup = (Agrupamento) agrupamentos.nextElement();
		Vector questoes = agrup.getQuestoes();
		Vector pesos = agrup.getPesosQuestoes();
		for (int i = 0; i < questoes.size(); i++) {
			Questao quest = (Questao) questoes.elementAt(i);
			int peso = Integer.parseInt(pesos.elementAt(i).toString());
			String novopeso = (String) request.getParameter("pesoAgrup"+agrup.getCod()+"Q"+quest.getCod());
			if ((novopeso != null) && (Integer.parseInt(novopeso) != peso)) {
				agrupger.alteraPesoQuestao(agrup,quest,novopeso);
			}
		}
	}
		
	
	
/*	String cod = (String) request.getParameter("cod");
	Questao obj = null;
	Questao_ger ger = new Questao_ger();
	if ((cod != null) && (!cod.equals(""))) {
		obj = ger.getElementByCod(cod);
	}

	String codAgrup = sessionManager.getElement("codAgrup");
	Agrupamento agrup = agrupger.getElementByCod(codAgrup);

	boolean criacao = (obj == null);*/

	

	pagina.javascript("alert('Atualização de pesos efetuada com sucesso.'); window.location = 'GerenciaQuestoes?toDo=listaQuestoes&"+sessionManager.generateEncodedParameter()+"';");
	
	pagina.fim();


}
private void cadastroQuestao(SessionManager sessionManager, Disciplina disc, Lista lista, PrintWriter out, Questao obj, String tipoDefault) throws Exception {

	boolean criacao = (obj == null);

	String titulo = criacao ? "Criação de Questao" : "Alteração de Questao";

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,titulo);
	pagina.init();

	pagina.titulo (titulo);

	Formulario form = new Formulario(pagina,"GerenciaQuestoes?"+sessionManager.generateEncodedParameter());
	form.hidden("toDo","efetuaCadastro");
	if (!criacao) form.hidden("cod",obj.getCod()); 

	TabelaFormulario tab = new TabelaFormulario (pagina);

		tab.addCelula("Enunciado: ");
		tab.addCelula(form.textArea("enunciado",(criacao?"":obj.getEnunciado()), 3, 50));
		tab.addCelula("Crit�rios: ");
		tab.addCelula(form.textArea("criterios",(criacao?"":obj.getCriterios()), 3, 50));
		
		if (criacao) {
			tab.addCelula("Tipo: ");
			Questao_ger quesger = new Questao_ger();
			tab.addCelula(form.selectBox("tipo", quesger.getIdTipos(), quesger.getDescTipos(),(criacao?tipoDefault:obj.getTipo()),"mudaTipoQuestao()"));
			quesger.htmlCadastroRespostasPadrao(out,tab,form);
		}

		tab.novaLinha();
		if (criacao) {
			tab.addCelulaCom3Botes(form.button("Continuar Criando","document.dados.toDo.value='continuarCriando'; document.dados.submit();"),form.botaoSubmit(criacao ? "Criar" : "Alterar"),form.button("Cancelar","window.location='GerenciaQuestoes?toDo=montaFrames&"+sessionManager.generateEncodedParameter()+"'"));
		} else {
			tab.addCelulaCom2Botes(form.botaoSubmit(criacao ? "Criar" : "Alterar"),form.button("Cancelar","window.location='GerenciaQuestoes?toDo=montaFrames&"+sessionManager.generateEncodedParameter()+"'"));
		}

	tab.fim();
	form.fim();

	pagina.fim();


}
private void efetuaCadastro(SessionManager sessionManager, Disciplina disc, Lista lista, javax.servlet.http.HttpServletRequest request, PrintWriter out, boolean continuarCriando) throws Exception {

	String cod = (String) request.getParameter("cod");
	Questao obj = null;
	Questao_ger ger = new Questao_ger();
	if ((cod != null) && (!cod.equals(""))) {
		obj = ger.getElementByCod(cod);
	}

	String codAgrup = sessionManager.getElement("codAgrup");
	Agrupamento_ger agrupger = new Agrupamento_ger();
	Agrupamento agrup = agrupger.getElementByCod(codAgrup);

	boolean criacao = (obj == null);

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,criacao ? "Criação de Questao" : "Alteração de Questao");
	pagina.init();

	// Le dados do formulario
	String enunciado = (String) request.getParameter("enunciado");
	String criterios = (String) request.getParameter("criterios");
	String tipo = (criacao) ? (String) request.getParameter("tipo") : obj.getTipo();

	// Checa consist�ncia	
	String problema = ger.testaConsistencia(obj,enunciado,criterios,tipo);
	if (problema != null) {
		pagina.javascript("alert('Campo nulo ou repetido: "+problema+".'); window.history.back();");
		return;
	}

	// Efetua cadastro
	if (criacao) {
		obj = ger.inclui(request,enunciado,criterios,tipo,agrup,"1");
	} else {
		ger.altera(obj,enunciado,criterios,tipo);
	}

	// Monta pagina de resposta
	if (criacao) {
		if (continuarCriando) {
			cadastroQuestao(sessionManager,disc,lista,out,null,obj.getTipo());
		} else {
			pagina.javascript("window.location = 'GerenciaQuestoes?toDo=montaFrames&"+sessionManager.generateEncodedParameter()+"';");
		}
	} else {
		pagina.javascript("alert('Alteração efetuada com sucesso.'); window.location = 'GerenciaQuestoes?toDo=montaFrames&"+sessionManager.generateEncodedParameter()+"';");
	}
	
	pagina.fim();


}
private void listaQuestoes(SessionManager sessionManager, Disciplina disc, Lista lista, PrintWriter out) throws Exception {

	// Le se h� algum agrupamento selecionado. Se nao houver, coloca o primeiro
	String codAgrup = sessionManager.getElement("codAgrup");
	Agrupamento_ger agrupger = new Agrupamento_ger();
	if (codAgrup == null) {
		codAgrup = ((Agrupamento) agrupger.getElements(lista).firstElement()).getCod();
		sessionManager.addElement("codAgrup",codAgrup);
	}
		
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Lista Questões");
	pagina.init();

	pagina.titulo ("Questões da "+StringConverter.concatenateWithoutRepetion("Lista",lista.getNome()));

	Questao_ger ger = new Questao_ger();
	Resposta_ger respger = new Resposta_ger();

	pagina.descricao("Escolha abaixo a questão.");
	pagina.saltaLinha();

	Formulario form = new Formulario(pagina,"GerenciaQuestoes?toDo=atualizaPesos&"+sessionManager.generateEncodedParameter());

	// Inicia tabela
	out.println("<TABLE border=1 bordercolor=black cellspacing=0 cellpadding=4 width=95%>");
    out.println("   <SCRIPT>");
    out.println("   var respostasNovas = '';");
    out.println("   function abreTodasQuestoes() {");
    out.println("       var a = respostasNovas.split('|');");
    out.println("       for (i = 0; i < a.length - 1; i++) {");
    out.println("            window.open('GerenciaRespostas?toDo=corrigeNovasResposta&codQuestao='+a[i]+'&"+sessionManager.generateEncodedParameter()+"','novasResps'+a[i],'toolbar=no,scrollbars=yes,resizable=yes,menubar=no,width=730,height=500,top=10,left=10');");
    out.println("       }");
    out.println("   }");
    out.println("   </SCRIPT>");
    
	int num = 0;
	Enumeration agrupamentos = agrupger.getElements(lista).elements();
	while (agrupamentos.hasMoreElements()) {
		Agrupamento agrup = (Agrupamento) agrupamentos.nextElement();
		out.println("<TR><TD bgcolor=yellow>");
		out.println("	<TABLE border=0 cellspacing=0 cellpadding=0 width=100%>");
		out.println("	<TR><TD>");
		out.println("		<INPUT TYPE=radio onclick=\"document.dados.agrup.value='"+agrup.getCod()+"'\" NAME=selectAgrup VALUE="+agrup.getCod()+((agrup.getCod().equals(codAgrup)) ? " checked":"")+"> &nbsp; "+agrup.getNome() + " &nbsp; (SeguirOrdem="+agrup.isSeguirOrdem()+"; numMaxQuestoes="+agrup.getNumMaxQuestoes()+")");
		if ((agrup.getEnunciado() != null) && (!agrup.getEnunciado().equals(""))) {
			out.println("		<BR><B>Enunciado: </B>"+agrup.getEnunciado());
		}
		out.println("	</TD><TD align=right valign=top>");
		out.println("		<span class=MenuItem>");
		out.println("		<a href=\"javascript://\" onclick=\"if (confirm('Confirma exclusão?')) window.location.href='GerenciaAgrupamentos?toDo=removerAgrupamento&idAgrupamento="+agrup.getCod()+"&"+sessionManager.generateEncodedParameter()+"';\">Excluir</a>");
		out.println("		&nbsp;<a href=\"GerenciaAgrupamentos?toDo=cadastroAgrupamento&idAgrupamento="+agrup.getCod()+"&"+sessionManager.generateEncodedParameter()+"\" target=_parent>Alterar</a></span>");
		out.println("	</TD></tr>");
		out.println("	</table>");
		out.println("</TD></TR>");
		out.println("<TR><TD>");
		out.println();
		out.println("	<TABLE border=0 cellspacing=0 cellpadding=0 width=100%>");
        
		// Imprime Questoes
		Vector questoes = agrup.getQuestoes();
		Vector pesos = agrup.getPesosQuestoes();
		for (int i = 0; i < questoes.size(); i++) {
			Questao quest = (Questao) questoes.elementAt(i);
			String peso = pesos.elementAt(i).toString();
			int numNovas = respger.getNumRespostasNovasByQuestao(quest);
			int numRespostas = respger.getElements(quest).size();
			boolean hasNovas = numNovas!=0;
			num++;
			out.println("	<TR><TD>");
			out.println("			<LI><span class=MenuItem><a href=\"GerenciaRespostas?codQuestao="+quest.getCod()+"&"+sessionManager.generateEncodedParameter()+"\" target=frmrespostas>"+(hasNovas?"<B>":"")+num+". ["+quest.getTipo()+"] "+quest.getEnunciado()+" ("+numNovas+"/"+numRespostas+")"+(hasNovas?"</B>":"")+"</a></span>");
			out.println("	</TD><TD align=right nowrap>");
			out.println("			<span class=MenuItem>");
			out.println("	&nbsp; Peso <INPUT TYPE=text NAME=pesoAgrup"+agrup.getCod()+"Q"+quest.getCod()+" size=1 maxlength=2 value="+peso+"> ");
			out.println("	&nbsp;<a href=\"javascript://\" onclick=\"if (confirm('Confirma exclusão?')) window.location.href='GerenciaQuestoes?toDo=removerQuestao&idQuestao="+quest.getCod()+"&"+sessionManager.generateEncodedParameter()+"';"+"\">Excluir</a>");
			out.println("	&nbsp;<a href=\"javascript://\" onclick=\"window.parent.location.href='GerenciaQuestoes?toDo=cadastroQuestao&idQuestao="+quest.getCod()+"&"+sessionManager.generateEncodedParameter()+"&selectAgrup='+document.dados.agrup.value\">Alterar</a>");
			out.println("	</span></TD></tr>");
            if (hasNovas) {
                out.println("<SCRIPT>respostasNovas += '"+quest.getCod()+"|'</SCRIPT>");
            }
		}
		out.println("	</table>");
		out.println("</TD></TR>");
	}	
	out.println("</TABLE>");
	
	pagina.saltaLinha();
	out.println(form.botaoSubmit("Alterar Pesos"));
    pagina.saltaLinha();
    out.println("<TD align=right>"+form.button("Corrigir todas novas respostas","abreTodasQuestoes()"));

	form.hidden("agrup",codAgrup);
	form.fim();	
	
	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opções");
		menu2.addItem("Criar nova questão","javascript://\" onclick=\"parent.window.location.href='GerenciaQuestoes?toDo=cadastroQuestao"+"&"+sessionManager.generateEncodedParameter()+"&selectAgrup='+document.dados.agrup.value;");
		menu2.addItem("Criar novo Agrupamento","GerenciaAgrupamentos?toDo=cadastroAgrupamento"+"&"+sessionManager.generateEncodedParameter(),"_parent");
		menu2.addItem("Escolher outra Lista","GerenciaDisciplinas?toDo=opcoesDisciplina&"+sessionManager.generateEncodedParameter(),"_parent");
		menu2.addItem("Escolher outra Disciplina","GerenciaDisciplinas?"+sessionManager.generateEncodedParameter(),"_parent");
		menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter(),"_parent");
	menu2.fim();

	pagina.fim();


}
private void montaFrames(SessionManager sessionManager, Disciplina disc, Lista lista, PrintWriter out) throws Exception {

	out.println("<HTML><HEAD><TITLE>Gerencia Questões</TITLE>");
	out.println("	<frameset border=1 frameborder=yes framespacing=1 cols=100% rows=\"50%,*\">");
	out.println("	<frame frameborder=YES marginheight=1 marginwidth=1 name=frmquestoes scrolling=AUTO src=\"GerenciaQuestoes?toDo=listaQuestoes&"+sessionManager.generateEncodedParameter()+"\">");
	out.println("	<frame frameborder=YES marginheight=1 marginwidth=1 name=frmrespostas scrolling=AUTO src=\"GerenciaRespostas?toDo=listaRespostas&"+sessionManager.generateEncodedParameter()+"\">");
	out.println("	</frameset>");
	out.println("</HTML>");

}
private void removerQuestao(SessionManager sessionManager, Disciplina disc, Lista lista, PrintWriter out, Questao obj) throws Exception {

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Remoção de Questao");
	pagina.init();

	// Efetua exclusão
	Questao_ger ger = new Questao_ger();
	ger.remove(obj);

	// Monta pagina de resposta
	pagina.javascript("alert('Remoção efetuada com sucesso.'); window.location = 'GerenciaQuestoes?toDo=listaQuestoes&"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();

}
}
