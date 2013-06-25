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

public class OpcoesListaProfessor extends Servlet {

public void action(javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	// Variaveis comuns
	Disciplina disc = null;
	Turma turma = null;
	
	// Inicializa SessionManager
	SessionManager sessionManager = new SessionManager(request);

	// Somente professor tem acesso
	sessionManager.checaPermissaoProfessor();

	// Le disciplina
	String idDisciplina = sessionManager.getElement("idDisciplina");
	Disciplina_ger disciplinager = new Disciplina_ger();
	disc = disciplinager.getElement(idDisciplina);

	// Le turma
	String idTurma = (String) request.getParameter("idTurma"); // quando entrar no gerenciador
	if ((idTurma == null) || (idTurma.equals(""))) {
		idTurma = sessionManager.getElement("idTurma");
	} else {
		sessionManager.addElement("idTurma",idTurma);
	}
	if ((idTurma == null) || (idTurma.equals(""))) {throw new Exception ("Falta a turma");}
	Turma_ger turmager = new Turma_ger();
	turma = turmager.getElement(idTurma);

	// A partir do toDo seleciona m�todo
	String toDo = (String) request.getParameter("toDo");
	if ((toDo == null) || (toDo.equals("")) && (toDo.equals("mostraListasAlunos"))) {
		mostraListasAlunos (sessionManager,disc,turma,out);

	} else if (toDo.equals("efetuaDesmatricula")) {
		efetuaDesmatricula (sessionManager,disc,turma,request,out);

	} else if (toDo.equals("efetuaRecorrigir")) {
		efetuaRecorrigir (sessionManager,disc,turma,request,out);
	} else if (toDo.equals("efetuaRecorrigirTodas")) {
		efetuaRecorrigirTodas(sessionManager,disc,turma,request,out);
	} else if (toDo.equals("efetuaReabrir")) {
		efetuaReabrir (sessionManager,disc,turma,request,out);
	} else if (toDo.equals("efetuaReabrirTodas")) {
		efetuaReabrirTodas (sessionManager,disc,turma,request,out);
	} else if (toDo.equals("efetuaZerar")) {
		efetuaZerar (sessionManager,disc,turma,request,out);
	} else if (toDo.equals("efetuaFinalizar")) {
		efetuaFinalizar (sessionManager,disc,turma,request,out);
	} else if (toDo.equals("efetuaFinalizarTodas")) {
		efetuaFinalizarTodas (sessionManager,disc,turma,request,out);
	} else if (toDo.equals("efetuaCorrigirNota")) {
		efetuaCorrigirNota (sessionManager,disc,turma,request,out);
		
	} else {
		throw new Exception ("Opção não reconhecida.");
	}

}
private void efetuaCorrigirNota(SessionManager sessionManager, Disciplina disc, Turma turma, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Corrigir Nota");
	pagina.init();

	String codAluno = request.getParameter("codAluno").toString();
	String codLista = request.getParameter("codLista").toString();
	String codListaGerada = request.getParameter("idListaGerada").toString();

	ListaGerada_ger listageradager = new ListaGerada_ger();
	ListaGerada listaGerada = listageradager.getElementByCod(codListaGerada);

	if (listaGerada == null) {
		pagina.javascript("alert('Lista não encontrada.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");
		return;
	}

	Date novaData = Data.novaData(request.getParameter("novaDatad"),request.getParameter("novaDatam"),request.getParameter("novaDataa"));
	double novaNota = Double.parseDouble(request.getParameter("novaNota").toString());

	if (listaGerada.getStatus() == listaGerada.EMCORRECAO) {
		listageradager.alteraStatusCorrigida(listaGerada);
	}
	
	listageradager.altera(listaGerada,listaGerada.getStatus(),novaNota,listaGerada.getDataGeracao(),novaData,listaGerada.getLista());
	
	// Monta pagina de resposta
	pagina.javascript("alert('Operação efetuada com sucesso.'); window.location = 'OpcoesLista?toDo=opcoesLista&codAluno="+codAluno+"&codLista="+codLista+"&"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();
	
}
private void efetuaDesmatricula(SessionManager sessionManager, Disciplina disc, Turma turma, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"desmatrícula de Aluno");
	pagina.init();

	String codAluno = request.getParameter("codAluno").toString();

	Aluno_ger alunoger = new Aluno_ger();
	Aluno aluno = alunoger.getElementByCod(codAluno);

	// Testa se aluno faz parte da turma
	if (!alunoger.getElementsByTurma(turma).contains(aluno)) {
		pagina.javascript("alert('"+aluno.getNome()+" não faz parte da turma.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");
		return;
	}

	alunoger.desmatriculaAlunoTurma (aluno,turma);
	
	// Manda e-mail para o aluno removido
	String str = (aluno.getSexo().equals("M")?"Caro ":"Cara ") + aluno.getNome()+",\n\n";
	str += "Você foi desmatriculado da disciplina "+disc.getNome()+".\n\n";
	str += "Atenciosamente,\n\nSite de Aulas Marco Gerosa\n\n";
	SendMail sender = new SendMail();
	sender.addTo(aluno.getEmail(), aluno.getEmail());
	sender.setAddressFrom("gerosa@ime.usp.br");
    sender.setNameFrom("Site de Aulas Gerosa");
    sender.setMessageSubject("[Gerosa Site] Notificação de desmatrícula");
    sender.setMessageText(str);
    SendMailThread send = new SendMailThread();
    send.setSendMail(sender);
    send.start();
	
	// Monta pagina de resposta
	pagina.javascript("alert('desmatrícula efetuada com sucesso.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();
	
}
private void efetuaFinalizar(SessionManager sessionManager, Disciplina disc, Turma turma, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"desmatrícula de Aluno");
	pagina.init();

	String codAluno = request.getParameter("codAluno").toString();
	String codLista = request.getParameter("codLista").toString();

	Aluno_ger alunoger = new Aluno_ger();
	Aluno aluno = alunoger.getElementByCod(codAluno);

	Lista_ger listager = new Lista_ger();
	Lista lista = listager.getElementByCod(codLista);
	
	// Testa se aluno faz parte da turma
	if (!alunoger.getElementsByTurma(turma).contains(aluno)) {
		pagina.javascript("alert('"+aluno.getNome()+" não faz parte da turma.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");
		return;
	}

	ListaGerada_ger listageradager = new ListaGerada_ger();
	ListaGerada listaGerada = listageradager.getElement(lista,aluno);

	// Testa se aluno faz parte da turma
	if (listaGerada == null) {
		pagina.javascript("alert('Lista não encontrada.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");
		return;
	}

	listageradager.alteraStatusFinalizada(listaGerada);
	
	// Monta pagina de resposta
	pagina.javascript("alert('Operação efetuada com sucesso.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();
	
}
private void efetuaFinalizarTodas(SessionManager sessionManager, Disciplina disc, Turma turma, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"desmatrícula de Aluno");
	pagina.init();

	// Pega lista
	String codLista = request.getParameter("codLista").toString();
	Lista_ger listager = new Lista_ger();
	Lista lista = listager.getElementByCod(codLista);

	// Pega listas geradas
	ListaGerada_ger listageradager = new ListaGerada_ger();
	Enumeration listas = listageradager.getElements(lista).elements();

	while (listas.hasMoreElements()) {
		ListaGerada listaGerada = (ListaGerada) listas.nextElement();
		if ((listaGerada.getStatus() == ListaGerada.ABERTA) || (listaGerada.getStatus() == ListaGerada.EDICAO)) {
			listageradager.alteraStatusFinalizada(listaGerada);
		}
	}
	
	// Monta pagina de resposta
	pagina.javascript("alert('Operação efetuada com sucesso.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();
	
}
private void efetuaReabrir(SessionManager sessionManager, Disciplina disc, Turma turma, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"desmatrícula de Aluno");
	pagina.init();

	String codAluno = request.getParameter("codAluno").toString();
	String codLista = request.getParameter("codLista").toString();

	Aluno_ger alunoger = new Aluno_ger();
	Aluno aluno = alunoger.getElementByCod(codAluno);

	Lista_ger listager = new Lista_ger();
	Lista lista = listager.getElementByCod(codLista);
	
	// Testa se aluno faz parte da turma
	if (!alunoger.getElementsByTurma(turma).contains(aluno)) {
		pagina.javascript("alert('"+aluno.getNome()+" não faz parte da turma.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");
		return;
	}

	ListaGerada_ger listageradager = new ListaGerada_ger();
	ListaGerada listaGerada = listageradager.getElement(lista,aluno);

	// Testa se aluno faz parte da turma
	if (listaGerada == null) {
		pagina.javascript("alert('Lista não encontrada.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");
		return;
	}

	listageradager.alteraStatusEdicao(listaGerada);
	
	// Monta pagina de resposta
	pagina.javascript("alert('Operação efetuada com sucesso.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();
	
}
private void efetuaReabrirTodas(SessionManager sessionManager, Disciplina disc, Turma turma, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"desmatrícula de Aluno");
	pagina.init();

	// Pega lista
	String codLista = request.getParameter("codLista").toString();
	Lista_ger listager = new Lista_ger();
	Lista lista = listager.getElementByCod(codLista);

	// Pega listas geradas
	ListaGerada_ger listageradager = new ListaGerada_ger();
	Enumeration listas = listageradager.getElements(lista).elements();

	while (listas.hasMoreElements()) {
		ListaGerada listaGerada = (ListaGerada) listas.nextElement();
		if ((listaGerada.getStatus() == ListaGerada.FINALIZADA) || (listaGerada.getStatus() == ListaGerada.EMCORRECAO) || (listaGerada.getStatus() == ListaGerada.CORRIGIDA)) {
			listageradager.alteraStatusEdicao(listaGerada);
		}
	}
	
	// Monta pagina de resposta
	pagina.javascript("alert('Operação efetuada com sucesso.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();
	
}
private void efetuaRecorrigir(SessionManager sessionManager, Disciplina disc, Turma turma, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"desmatrícula de Aluno");
	pagina.init();

	String codAluno = request.getParameter("codAluno").toString();
	String codLista = request.getParameter("codLista").toString();

	Aluno_ger alunoger = new Aluno_ger();
	Aluno aluno = alunoger.getElementByCod(codAluno);

	Lista_ger listager = new Lista_ger();
	Lista lista = listager.getElementByCod(codLista);
	
	// Testa se aluno faz parte da turma
	if (!alunoger.getElementsByTurma(turma).contains(aluno)) {
		pagina.javascript("alert('"+aluno.getNome()+" não faz parte da turma.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");
		return;
	}

	ListaGerada_ger listageradager = new ListaGerada_ger();
	ListaGerada listaGerada = listageradager.getElement(lista,aluno);

	// Testa se aluno faz parte da turma
	if (listaGerada == null) {
		pagina.javascript("alert('Lista não encontrada.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");
		return;
	}

	listageradager.corrige(listaGerada);
	
	// Monta pagina de resposta
	pagina.javascript("alert('Operação efetuada com sucesso.'); window.location = 'OpcoesLista?toDo=opcoesLista&codAluno="+aluno.getCod()+"&codLista="+lista.getCod()+"&"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();
	
}
private void efetuaRecorrigirTodas(SessionManager sessionManager, Disciplina disc, Turma turma, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"desmatrícula de Aluno");
	pagina.init();

	// Pega lista
	String codLista = request.getParameter("codLista").toString();
	Lista_ger listager = new Lista_ger();
	Lista lista = listager.getElementByCod(codLista);

	// Pega listas geradas
	ListaGerada_ger listageradager = new ListaGerada_ger();
	Enumeration listas = listageradager.getElements(lista).elements();

	while (listas.hasMoreElements()) {
		ListaGerada listaGerada = (ListaGerada) listas.nextElement();
		if ((listaGerada.getStatus() == ListaGerada.FINALIZADA) || (listaGerada.getStatus() == ListaGerada.EMCORRECAO) || (listaGerada.getStatus() == ListaGerada.CORRIGIDA)) {
			listageradager.corrige(listaGerada);
		}
	}
	
	// Monta pagina de resposta
	pagina.javascript("alert('Operação efetuada com sucesso.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();
	
}
private void efetuaZerar(SessionManager sessionManager, Disciplina disc, Turma turma, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"desmatrícula de Aluno");
	pagina.init();

	String codAluno = request.getParameter("codAluno").toString();
	String codLista = request.getParameter("codLista").toString();

	Aluno_ger alunoger = new Aluno_ger();
	Aluno aluno = alunoger.getElementByCod(codAluno);

	Lista_ger listager = new Lista_ger();
	Lista lista = listager.getElementByCod(codLista);
	
	// Testa se aluno faz parte da turma
	if (!alunoger.getElementsByTurma(turma).contains(aluno)) {
		pagina.javascript("alert('"+aluno.getNome()+" não faz parte da turma.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");
		return;
	}

	ListaGerada_ger listageradager = new ListaGerada_ger();
	ListaGerada listaGerada = listageradager.getElement(lista,aluno);

	// Testa se aluno faz parte da turma
	if (listaGerada == null) {
		pagina.javascript("alert('Lista não encontrada.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");
		return;
	}

	listageradager.zerarLista(listaGerada);
	
	// Monta pagina de resposta
	pagina.javascript("alert('Operação efetuada com sucesso.'); window.location = 'OpcoesListaProfessor?"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();
	
}
private void mostraListasAlunos(SessionManager sessionManager, Disciplina disc, Turma turma, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Relatório de turma da "+StringConverter.concatenateWithoutRepetion("Disciplina",disc.getNome()));
	pagina.init();

	pagina.titulo ("Relatório da "+StringConverter.concatenateWithoutRepetion("Turma",turma.getNome())+" da "+StringConverter.concatenateWithoutRepetion("Disciplina",disc.getNome()));

	Aluno_ger alunoger = new Aluno_ger();
	Vector alunos = alunoger.getElementsByTurma(turma);
	StringBuffer emails = new StringBuffer();

	if (alunos.size()==0) {
		pagina.descricao("não há alunos matriculados nesta turma.");
	} else {
		
		Collections.sort(alunos);

		Vector listas = turma.getListasAtivas();
		ListaGerada_ger listageradager = new ListaGerada_ger();
		
		TabelaComBorda tab = new TabelaComBorda(pagina);
		Formulario form = new Formulario(pagina,"");

		// Titulos
		tab.addTitulo("Alunos");
		for (int i = 0; i < listas.size(); i++) {
			Lista lista = (Lista) listas.elementAt(i);
			tab.addTitulo(lista.getNome());
		}

		// Vetores para calcular as medias
		int numAlunos = 0;
		Vector somasNotas = new Vector();
		Vector numNotas = new Vector();
		for (int i = 0; i < listas.size(); i++) {
			Lista lista = (Lista) listas.elementAt(i);
			somasNotas.addElement(new Float(0.0));
			numNotas.addElement(new Integer(0));
		}
	
		// Valores
		for (int z = 0; z < alunos.size(); z++) {
			Aluno aluno = (Aluno) alunos.elementAt(z);
			numAlunos++;
			emails.append(aluno.getEmail()+",");

			tab.addCelula("<table border=0 cellpadding=0 cellspacing=0 width=100%><tr><td><span class=MenuItem><A HREF=\"mailto:"+aluno.getEmail()+"\">"+(z+1)+". "+aluno.getNome()+"</A>&nbsp;</span></td><td align=right><span class=MenuItem> (<A HREF=\"GerenciaAlunos?toDo=opcoesAluno&cod="+aluno.getCod()+"&"+sessionManager.generateEncodedParameter()+"\">A</A>) (<A HREF=\"javascript://\" onclick=\"if (confirm('Confirma desmatrícula do aluno?')) window.location.href='OpcoesListaProfessor?toDo=efetuaDesmatricula&codAluno="+aluno.getCod()+"&"+sessionManager.generateEncodedParameter()+"'\">D</A>)</SPAN></td></tr></table>");

			for (int i = 0; i < listas.size(); i++) {
				Lista lista = (Lista) listas.elementAt(i);
				ListaGerada listaGerada = listageradager.getElement(lista,aluno);

				String a = "<span class=MenuItem><A HREF=\"OpcoesLista?toDo=opcoesLista&codAluno="+aluno.getCod()+"&codLista="+lista.getCod()+"&"+sessionManager.generateEncodedParameter()+"\">";
				if (listaGerada == null) {
					tab.addCelula("não iniciada");
				} else if (listaGerada.getStatus() == ListaGerada.ABERTA) {
					tab.addCelula(a+"Em branco</a>");
				} else if (listaGerada.getStatus() == ListaGerada.EDICAO) {
					tab.addCelula(a+"Em edição ("+100*listaGerada.getQuestoesSemRespostas().size()/listaGerada.getQuestoes().size()+"%)</a>");
				} else if (listaGerada.getStatus() == ListaGerada.FINALIZADA) {
					tab.addCelula(a+"Aguardando correção</a>");
				} else if (listaGerada.getStatus() == ListaGerada.EMCORRECAO) {
					tab.addCelula(a+"Em correção (nota="+listaGerada.getNota()+")</a>");
					somasNotas.setElementAt(new Float(((Float) somasNotas.elementAt(i)).floatValue()+listaGerada.getNota()),i);
					numNotas.setElementAt(new Integer(((Integer) numNotas.elementAt(i)).intValue()+1),i);
				} else if (listaGerada.getStatus() == ListaGerada.CORRIGIDA) {
					if (listaGerada.getNota() <= 5) {
						tab.addCelula(a+Double.toString(listaGerada.getNota())+"</a>","#FF9D9D");
					} else if (listaGerada.getNota() < 8) {
						tab.addCelula(a+Double.toString(listaGerada.getNota())+"</a>","#FFFFCC");
					} else if (listaGerada.getNota() < 10) {
						tab.addCelula(a+Double.toString(listaGerada.getNota())+"</a>","#C0FFC0");
					} else {
						tab.addCelula(a+Double.toString(listaGerada.getNota())+"</a>","#B7BAFB");
					}
					
					somasNotas.setElementAt(new Float(((Float) somasNotas.elementAt(i)).floatValue()+listaGerada.getNota()),i);
					numNotas.setElementAt(new Integer(((Integer) numNotas.elementAt(i)).intValue()+1),i);
				}
			}
		}	

		// Numero de alunos
		tab.addCelula("<B>Número de alunos: "+numAlunos+"</B>");
		for (int i = 0; i < listas.size(); i++) {
			tab.addCelula("&nbsp;");
		}
		
		// Medias
		tab.addCelula("<B>Média: </B>");
		for (int i = 0; i < listas.size(); i++) {
			Lista lista = (Lista) listas.elementAt(i);
			tab.addCelula("<CENTER>"+(Math.round(100*((Float) somasNotas.elementAt(i)).floatValue()/((Integer) numNotas.elementAt(i)).intValue()))/100.00+"</CENTER>");
		}
		
			
		// Opcoes das listas
		tab.addCelula("<B>Opções: </B>");
		for (int i = 0; i < listas.size(); i++) {
			Lista lista = (Lista) listas.elementAt(i);
			tab.addCelula("<CENTER>"+form.button("Re-corrigir Todas","window.location.href='OpcoesListaProfessor?toDo=efetuaRecorrigirTodas&codLista="+lista.getCod()+"&"+sessionManager.generateEncodedParameter()+"'")+"<BR>"+form.button("Finalizar Todas","window.location.href='OpcoesListaProfessor?toDo=efetuaFinalizarTodas&codLista="+lista.getCod()+"&"+sessionManager.generateEncodedParameter()+"'")+"<BR>"+form.button("Re-abrir Todas","window.location.href='OpcoesListaProfessor?toDo=efetuaReabrirTodas&codLista="+lista.getCod()+"&"+sessionManager.generateEncodedParameter()+"'")+"</CENTER>");
		}

		tab.fim();
		form.fim();
	
	}	
		
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opções");
		if (alunos.size() != 0) {
			menu2.addItem("Mandar e-mail para todos os alunos","mailto:"+emails.toString());
		}
		menu2.addItem("Voltar para as Opções da disciplina","GerenciaDisciplinas?toDo=opcoesDisciplina&"+sessionManager.generateEncodedParameter());
		menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter());
	menu2.fim();

	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();

}
}
