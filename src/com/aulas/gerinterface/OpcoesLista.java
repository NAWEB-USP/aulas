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
import com.aulas.util.sessionmanager.SecurityException;
import com.aulas.business.*;
import com.aulas.objinterface.*;

import java.util.*;
import java.io.*;

public class OpcoesLista extends Servlet {


public void action(javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	// Variaveis comuns
	Aluno aluno = null;
	Disciplina disc = null;
	Lista lista = null;
	ListaGerada listaGerada = null;
	Turma turma = null;
	boolean isProfessor;

 	// Inicializa SessionManager
	SessionManager sessionManager = new SessionManager(request);

	// Trata request para o caso de ser multipart (upload de arquivo)
	Hashtable parametros = Formulario.trataRequestEmCasoDeUpload(request);
	
	// Le aluno
	String idAluno = sessionManager.getElement("idUsuario");
	Aluno_ger alunoger = new Aluno_ger();
	if (idAluno.equals("0")) {
		isProfessor = true;
		aluno = alunoger.getElement(parametros.get("codAluno").toString());
	} else {
		aluno = alunoger.getElement(idAluno);
		isProfessor = false;
	}
	
	// Le disciplina
	String idDisciplina = sessionManager.getElement("idDisciplina");
	Disciplina_ger disciplinager = new Disciplina_ger();
	disc = disciplinager.getElement(idDisciplina);

	// Le turma
	String idTurma = sessionManager.getElement("idTurma");
	Turma_ger turmager = new Turma_ger();
	turma = turmager.getElement(idTurma);

	// Le lista
	String cod = (String) parametros.get("codLista");
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

	if (isProfessor) {
		// Somente professor tem acesso
		sessionManager.checaPermissaoProfessor();
		opcoesListaFechada(sessionManager,aluno, disc, lista, listaGerada, turma, isProfessor, out);
		return;
	}
	
	// A partir do toDo seleciona m�todo
	String toDo = (String) parametros.get("toDo");
	if ((toDo == null) || (toDo.equals("")) || (toDo.equals("opcoesLista"))) {
		if ((listaGerada == null) || (listaGerada.getStatus() == ListaGerada.ABERTA) || (listaGerada.getStatus() == ListaGerada.EDICAO)) {
			opcoesListaAberta (sessionManager,aluno, disc, lista, listaGerada, turma, isProfessor, out);
		} else if ((listaGerada.getStatus() == ListaGerada.FINALIZADA) || (listaGerada.getStatus() == ListaGerada.EMCORRECAO) || (listaGerada.getStatus() == ListaGerada.CORRIGIDA)) {
			opcoesListaFechada (sessionManager,aluno, disc, lista, listaGerada, turma, isProfessor, out);
		}
	} else if (toDo.equals("testar")) {
		if (!listaGerada.getLista().isPermitirTestar()) throw new SecurityException("Opção inv�lida."); 
		opcoesListaFechada (sessionManager,aluno, disc, lista, listaGerada, turma, isProfessor, out);
		
	} else if (toDo.equals("versaoImpressao")) {
		versaoImpressao (sessionManager,aluno, disc, lista, listaGerada, turma, isProfessor, out);
		
	} else if (toDo.equals("resolverLista")) {
		resolverLista(sessionManager,aluno, disc, lista, listaGerada, turma, isProfessor, out);
		
	} else if (toDo.equals("efetuaResolucao")) {
		efetuaResolucao(sessionManager,aluno, disc, lista, listaGerada, turma, isProfessor, parametros, out);
		
	} else if (toDo.equals("definirGrupo")) {
		definirGrupo(sessionManager,aluno, disc, lista, listaGerada, turma, isProfessor, out);

	} else if (toDo.equals("efetuaDefinicaoGrupo")) {
		efetuaDefinicaoGrupo(sessionManager,aluno, disc, lista, listaGerada, turma, isProfessor, parametros, out);

	} else if (toDo.equals("efetuaRemocaoAluno")) {
		efetuaRemocaoAluno(sessionManager,aluno, disc, lista, listaGerada, turma, isProfessor, request, out);
		
	} else {
		throw new Exception ("Opção não reconhecida.");
	}

}
private void auxCabecalhoLista(Aluno aluno, Disciplina disc, Lista lista, ListaGerada listaGerada, Turma turma, boolean isProfessor, PaginaHTML pagina) throws Exception {

	TabelaSimples tab = new TabelaSimples(pagina);

	if (listaGerada != null) {
		tab.addCelula("<B>código de Identificação: </B>");
		tab.addCelula("<B>"+listaGerada.getCodigoIdentificador()+"</B>");
		tab.novaLinha();
	}
	
	if (aluno != null) {
		tab.addCelula("<B>Aluno: </B>");
		tab.addCelula(aluno.getNome());
		tab.novaLinha();
	}
	tab.addCelula("<B>Disciplina: </B>");
	tab.addCelula(disc.getNome());
	tab.novaLinha();
	tab.addCelula("<B>Turma: </B>");
	tab.addCelula(turma.getNome());
	tab.novaLinha();
	tab.addCelula("<B>Nome da Lista: </B>");
	tab.addCelula(lista.getNome());
	tab.novaLinha();
	tab.addCelula("<B>Grupo: </B>");
	if ((lista.getNumMinAlunosPorGrupo() == 1) && (lista.getNumMaxAlunosPorGrupo() == 1)) {
		tab.addCelula("Individual");
	} else {
		tab.addCelula("M�nimo de "+lista.getNumMinAlunosPorGrupo()+" e m�ximo de "+lista.getNumMaxAlunosPorGrupo()+" alunos");
	}
	tab.novaLinha();
	if (turma.getData1(lista) != null) {
		tab.addCelula("<B>Prazo 1: </B>");
		tab.addCelula(Data.formataData(turma.getData1(lista)) + " &nbsp; Peso="+turma.getPeso1(lista)+"%");
		tab.novaLinha();
	}
	if (turma.getData2(lista) != null) {
		tab.addCelula("<B>Prazo 2: </B>");
		tab.addCelula(Data.formataData(turma.getData2(lista)) + " &nbsp; Peso="+turma.getPeso2(lista)+"%");
		tab.novaLinha();
	}
	if (turma.getData3(lista) != null) {
		tab.addCelula("<B>Prazo 3: </B>");
		tab.addCelula(Data.formataData(turma.getData3(lista)) + " &nbsp; Peso="+turma.getPeso3(lista)+"%");
		tab.novaLinha();
	}
	if (turma.getData4(lista) != null) {
		tab.addCelula("<B>Prazo 4: </B>");
		tab.addCelula(Data.formataData(turma.getData4(lista)) + " &nbsp; Peso="+turma.getPeso4(lista)+"%");
		tab.novaLinha();
	}
	if (turma.getData5(lista) != null) {
		tab.addCelula("<B>Prazo 5: </B>");
		tab.addCelula(Data.formataData(turma.getData5(lista)) + " &nbsp; Peso="+turma.getPeso5(lista)+"%");
		tab.novaLinha();
	}
	if (turma.getData6(lista) != null) {
		tab.addCelula("<B>Prazo 6: </B>");
		tab.addCelula(Data.formataData(turma.getData6(lista)) + " &nbsp; Peso="+turma.getPeso6(lista)+"%");
		tab.novaLinha();
	}
	if (!(lista.getNumMinAlunosPorGrupo() == 1) || !(lista.getNumMaxAlunosPorGrupo() == 1)) {
		tab.addCelula("<B>Componentes do Grupo: </B>");
		if (listaGerada == null) {
			tab.addCelula("Grupo não formado.");
		} else {
			Enumeration alunos = listaGerada.getAlunos().elements();
			String str = "";
			while (alunos.hasMoreElements()) {
				Aluno a = (Aluno) alunos.nextElement();
				str += a.getNome()+"<BR>";
			}
			tab.addCelula(str);
		}
		tab.novaLinha();
	}
	if ((lista.getEnunciado() != null) && (!lista.getEnunciado().equals(""))) {
		tab.addCelula("<B>Enunciado: </B>");
		tab.addCelula(lista.getEnunciado());
		tab.novaLinha();
	}
	
	tab.fim();


}
private void definirGrupo(SessionManager sessionManager, Aluno a, Disciplina disc, Lista lista, ListaGerada listaGerada, Turma turma, boolean isProfessor, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Definição de grupo");
	pagina.init();

	pagina.titulo ("Definição de grupo para a "+StringConverter.concatenateWithoutRepetion("Lista",lista.getNome()));

	auxCabecalhoLista(a, disc, lista, listaGerada, turma, isProfessor, pagina);
	
	pagina.saltaLinha();
	pagina.descricao("Escolha abaixo os componentes de seu grupo (m�ximo de "+lista.getNumMaxAlunosPorGrupo()+" alunos, incluindo Você):");
	pagina.saltaLinha();

	Formulario form = new Formulario(pagina,"OpcoesLista?"+sessionManager.generateEncodedParameter());
	form.hidden("toDo","efetuaDefinicaoGrupo");
	Menu menu = new Menu (pagina,"Alunos de sua turma");
	menu.fim();

	Aluno_ger alunoger = new Aluno_ger();
	ListaGerada_ger listageradager = new ListaGerada_ger();
	Vector alunos = alunoger.getElementsByTurma(turma);
	Collections.sort(alunos);

	out.println("<TABLE cellpadding=1 cellspacing=1>");
	for (int i = 0; i < alunos.size(); i++) {
		Aluno aluno = (Aluno) alunos.elementAt(i)	;
		ListaGerada listaGAluno = listageradager.getElement(lista,aluno);
		if (aluno.equals(a)) { 
			// aluno � o proprio usuario
			out.println("<TR><TD><INPUT TYPE=checkbox checked DISABLED></TD><TD><span class=MenuItem><B><a href=\"mailto:"+aluno.getEmail()+"\">"+aluno.getNome()+"</A></B></span><FONT COLOR=green> (Você)</font></TD></TR>");
		} else if ((listaGerada != null) && listaGerada.getAlunos().contains(aluno)) {
			// aluno já faz parte do grupo em questão
			out.println("<TR><TD>&nbsp;</TD><TD><span class=MenuItem><B><a href=\"mailto:"+aluno.getEmail()+"\">"+aluno.getNome()+"</A></B></span><FONT COLOR=green> (faz parte do seu grupo) </font>"+form.button("Retirar","if (confirm('Deseja realmente retirar o aluno de seu grupo?')) {document.dados.toDo.value=''; window.location='OpcoesLista?toDo=efetuaRemocaoAluno&codAluno="+aluno.getCod()+"&"+sessionManager.generateEncodedParameter()+"';}")+"</TD></TR>");
		} else if ((listaGAluno != null) && (listaGAluno.getAlunos().size() > 1)) {
			// aluno já for parte de outro grupo
			out.println("<TR><TD>&nbsp;</TD><TD><span class=MenuItem><a href=\"mailto:"+aluno.getEmail()+"\">"+aluno.getNome()+"</A></span><FONT COLOR=red> (aluno já faz parte de outro grupo)</font></TD></TR>");
		} else if ((listaGAluno != null) && (listaGAluno.getStatus() != ListaGerada.ABERTA)) {
			// aluno já iniciou sozinho a resolução da lista
			out.println("<TR><TD>&nbsp;</TD><TD><span class=MenuItem><a href=\"mailto:"+aluno.getEmail()+"\">"+aluno.getNome()+"</A></span><FONT COLOR=red> (aluno já iniciou sozinho a resolução da lista)</font></TD></TR>");
		} else { 
			// aluno está livre
			out.println("<TR><TD>"+form.checkbox("aluno"+aluno.getCod(),"aluno"+aluno.getCod(),false)+"&nbsp;</TD><TD><span class=MenuItem><a href=\"mailto:"+aluno.getEmail()+"\">"+aluno.getNome()+"</A></span></TD></TR>");
		}
	}
	out.println("</TABLE>");
	
	pagina.saltaLinha();
	out.println(form.botaoSubmit("Definir Grupo"));

	
	pagina.saltaLinha();
	pagina.saltaLinha();
	pagina.descricao("OBS: Se algum aluno não está listado aqui, ele não se cadastrou, não se matr�culou ou se matriculou numa turma errada.");
	pagina.saltaLinha();
	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opções");
		menu2.addItem("Voltar sem salvar as alterações","OpcoesLista?"+sessionManager.generateEncodedParameter());
	menu2.fim();



	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();

}
private void efetuaRemocaoAluno(SessionManager sessionManager, Aluno aluno, Disciplina disc, Lista lista, ListaGerada listaGerada, Turma turma, boolean isProfessor, javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Definição de Grupo");
	pagina.init();

	String codAluno = request.getParameter("codAluno").toString();

	Aluno_ger alunoger = new Aluno_ger();
	Aluno a = alunoger.getElementByCod(codAluno);

	// Testa se aluno faz parte
	if (!listaGerada.getAlunos().contains(a)) {
		pagina.javascript("alert('"+a.getNome()+" não faz parte do grupo.'); window.location = 'OpcoesLista?toDo=definirGrupo&"+sessionManager.generateEncodedParameter()+"';");
		return;
	}

	// Testa se � menor que o minimo
	if (listaGerada.getAlunos().size()-1<lista.getNumMinAlunosPorGrupo()) {
		pagina.javascript("alert('O grupo não pode ficar com menos de "+lista.getNumMinAlunosPorGrupo()+" alunos'); window.history.back();");
		return;
	}
	
	ListaGerada_ger listageradager = new ListaGerada_ger();
	listageradager.removeAlunoListaGerada(a, listaGerada);
	Vector alunos = new Vector ();
	alunos.addElement(a);
	ListaGerada listaGerada2 = listageradager.duplicaListaGerada(listaGerada,a);

	
	// Manda e-mail para o aluno removido
	String str = (a.getSexo().equals("M")?"Caro ":"Cara ") + a.getNome()+",\n\n";
	str += aluno.getNome()+" removeu Você de seu grupo na disciplina "+disc.getNome()+", "+StringConverter.concatenateWithoutRepetion("lista",lista.getNome())+".\n\n";
	str += "Você continua com as eventuais respostas que o grupo já salvou na lista.";
	str += "\n\nAtenciosamente,\n\nSite de Aulas Marco Gerosa\n\n";

	SendMail sender = new SendMail();
	sender.addTo(a.getEmail(), a.getEmail());
	sender.setAddressFrom("gerosa@ime.usp.br");
    sender.setNameFrom("Site de Aulas Gerosa");
    sender.setMessageSubject("[Gerosa Site] Notificação de Remoção de grupo");
    sender.setMessageText(str);
    SendMailThread send = new SendMailThread();
    send.setSendMail(sender);
    send.start();

	// Manda e-mail para os membros do grupo
	for (int i = 0; i < listaGerada.getAlunos().size(); i++) {
		Aluno aa = (Aluno) listaGerada.getAlunos().elementAt(i);
		str = (aa.getSexo().equals("M")?"Caro ":"Cara ") + aa.getNome()+",\n\n";
		str += aluno.getNome()+" removeu o aluno "+a.getNome()+" do seu grupo na disciplina "+disc.getNome()+", "+StringConverter.concatenateWithoutRepetion("lista",lista.getNome())+".\n\n";
		str += "Os atuais membros do grupo são:\n";
		for (int j = 0; j < listaGerada.getAlunos().size(); j++) {
			Aluno a2 = (Aluno) listaGerada.getAlunos().elementAt(j);
			str += " - "+a2.getNome() + " ("+a2.getEmail()+")\n";
		}
		str += "\n\nAtenciosamente,\n\nSite de Aulas Marco Gerosa\n\n";
		
		SendMail sender2 = new SendMail();
		sender2.addTo(aa.getEmail(), aa.getEmail());
		sender2.setAddressFrom("gerosa@ime.usp.br");
	    sender2.setNameFrom("Site de Aulas Gerosa");
	    sender2.setMessageSubject("[Gerosa Site] Notificação de Remoção de membro do seu grupo");
	    sender2.setMessageText(str);
	    SendMailThread send2 = new SendMailThread();
	    send2.setSendMail(sender2);
	    send2.start();
	}
	
	// Monta pagina de resposta
	pagina.javascript("alert('Remoção efetuada com sucesso.'); window.location = 'OpcoesLista?"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();
	
}
private void opcoesListaAberta(SessionManager sessionManager, Aluno aluno, Disciplina disc, Lista lista, ListaGerada listaGerada, Turma turma, boolean isProfessor, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Opções da "+StringConverter.concatenateWithoutRepetion("Disciplina",lista.getNome()));
	pagina.init();

	pagina.titulo ("Opções da "+StringConverter.concatenateWithoutRepetion("Lista",lista.getNome()));

	auxCabecalhoLista(aluno, disc, lista, listaGerada, turma, isProfessor, pagina);

	boolean prazoExpirou = lista.isPrazoExpirou(turma);
	pagina.saltaLinha();
	Menu menu = new Menu (pagina,"Opções");
		if (!prazoExpirou) {
			if (lista.getNumMaxAlunosPorGrupo() > 1) {
				menu.addItem("Definir Grupo","OpcoesLista?toDo=definirGrupo&"+sessionManager.generateEncodedParameter());
			}
			menu.addItem("Resolver lista","OpcoesLista?toDo=resolverLista&"+sessionManager.generateEncodedParameter());
		}
		menu.addItem("Gerar versão para impressão","OpcoesLista?toDo=versaoImpressao&"+sessionManager.generateEncodedParameter());
	menu.fim();

	if (prazoExpirou) {
		pagina.saltaLinha();
		pagina.descricao("não � possível resolver a lista pois o prazo acabou.");
	}
	
	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opções");
		menu2.addItem("Voltar para a escolha de lista","OpcoesAluno?toDo=opcoesDisciplina&"+sessionManager.generateEncodedParameter());
		menu2.addItem("Escolher outra disciplina","OpcoesAluno?"+sessionManager.generateEncodedParameter());
		menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter());
	menu2.fim();

	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();

}
private void opcoesListaFechada(SessionManager sessionManager, Aluno aluno, Disciplina disc, Lista lista, ListaGerada listaGerada, Turma turma, boolean isProfessor, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,StringConverter.concatenateWithoutRepetion("Lista",lista.getNome()));
	pagina.init();

	pagina.titulo (StringConverter.concatenateWithoutRepetion("Lista",lista.getNome()));

	auxCabecalhoLista(aluno, disc, lista, listaGerada, turma, isProfessor, pagina);
	
	pagina.saltaLinha();

	// Pega lista de questoes
	Enumeration questoes = listaGerada.getQuestoes().elements();
	// Pega lista de respostas
	Enumeration respostas = listaGerada.getQuestoesRespostas().elements();
	// Pega lista de agrupamentos das questoes
	Enumeration agrupamentos = listaGerada.getQuestoesAgrupamentos().elements();
	// Imprime a lista de questoes
	int num = 0;
	Agrupamento agrupAtual = null;
	int somaNotasObtidas = 0; // acumulador
	int somaNotasACorrigir = 0; // acumulador
	int somaPesos = 0; // acumulador
	while (questoes.hasMoreElements()) {
		num++;
		Questao questao = (Questao) questoes.nextElement();
		Resposta resp = (Resposta) respostas.nextElement();
		Agrupamento agrup = (Agrupamento) agrupamentos.nextElement();
		if ((agrupAtual == null) || (!agrupAtual.equals(agrup))) {
			if ((agrup.getEnunciado() != null) && (!agrup.getEnunciado().equals(""))) {
				pagina.descricao(agrup.getEnunciado()+"<BR>");
			}
			agrupAtual = agrup;
		}
		out.println("<B>"+num+") "+questao.getEnunciado()+"</B><BR>");
		String txtResposta = (((resp == null) || (resp.getResposta()==null))?"&nbsp;":StringConverter.toHtmlNotation(StringConverter.replace(StringConverter.replace(resp.getResposta(),"&quot;","\""),"&#39;","'")));
		out.println("<table cellpadding=1 cellspacing=1 border=0><tr><td valign=top><B>"+(isProfessor?"Resposta do aluno: ":"Sua resposta: ")+"</B></td><td>"+txtResposta+"</td></tr></table>");
		if (resp == null) {
			out.println("<B><FONT COLOR=red>Em branco.</FONT></B><BR>");
		} else if (resp.isNova()) {
			out.println("<B><FONT COLOR=purple>Aguardando correção.</FONT></B><BR>");
		} else if (resp.getPontuacao() == 0) {
			out.println("<B><FONT COLOR=red>Errado!</FONT></B><BR>");
		} else if (resp.getPontuacao() == 100) {
			out.println("<B><FONT COLOR=green>Correto!</FONT></B><BR>");
		} else {
			out.println("<B><FONT COLOR=navy>Porcentagem de acerto="+resp.getPontuacao()+"%</FONT></B><BR>");
		}
		if ((resp != null) && (resp.getComentario() != null) && !resp.getComentario().equals("")) {
			out.println("<B>Comentário: </B>"+StringConverter.replace(resp.getComentario(),"\n","<BR>")+"<BR>");
		}
		// computa nota
		int peso = lista.getPesoQuestao(questao);
		if (resp != null) {
			if (resp.isNova()) {
				somaNotasACorrigir += peso * 100;
			} else {
				somaNotasObtidas += peso * resp.getPontuacao();
			}
		}
		somaPesos += peso; // o peso entra para o c�lculo mesmo se a questão estiver sem corrigir.
		
		out.println("<BR>");
	}
	if (somaPesos == 0)	somaPesos = 1;
	
	// Demostrativo da Nota
	pagina.saltaLinha();
	TabelaSimples tab = new TabelaSimples(pagina);
	tab.addCelula("<B>Data de entrega: </B>");
	tab.addCelula(Data.formataData(listaGerada.getDataFinalizacao()));
	tab.novaLinha();

	if (listaGerada.getStatus() == ListaGerada.CORRIGIDA) {
		// Corrigida 
		tab.addCelula("<B>Total de pontos: </B>");
		tab.addCelula(Math.round((1.0/10.0) * somaNotasObtidas/somaPesos*100.0)/100.0+"");
		tab.novaLinha();
		tab.addCelula("<B>Nota: </B>");
		tab.addCelula(listaGerada.getNota()+"");
		tab.novaLinha();
	} else {
		// Em correcao
		tab.addCelula("<B>Total de pontos acumulados: </B>");
		tab.addCelula(Math.round((1.0/10.0) * somaNotasObtidas/somaPesos*100.0)/100.0+" (considerando apenas as questões corrigidas)");
		tab.novaLinha();
		tab.addCelula("<B>Total de pontos a corrigir: </B>");
		tab.addCelula(Math.round((1.0/10.0) * somaNotasACorrigir/somaPesos*100.0)/100.0+"");
		tab.novaLinha();
	}

	if (isProfessor) {
		Formulario form = new Formulario(pagina,"");
		String botoes = "";
		if ((listaGerada.getStatus() == ListaGerada.ABERTA) || (listaGerada.getStatus() == ListaGerada.EDICAO)) {
			botoes += form.button("Finalizar lista","window.location.href='OpcoesListaProfessor?toDo=efetuaFinalizar&codAluno="+aluno.getCod()+"&codLista="+lista.getCod()+"&"+sessionManager.generateEncodedParameter()+"'");
		}
		if (listaGerada.getStatus() == ListaGerada.EDICAO) {
			botoes += " "+form.button("Zerar lista","window.location.href='OpcoesListaProfessor?toDo=efetuaZerar&codAluno="+aluno.getCod()+"&codLista="+lista.getCod()+"&"+sessionManager.generateEncodedParameter()+"'");
		}
		if (listaGerada.getStatus() >= ListaGerada.FINALIZADA) {
			botoes += form.button("Re-abrir lista","window.location.href='OpcoesListaProfessor?toDo=efetuaReabrir&codAluno="+aluno.getCod()+"&codLista="+lista.getCod()+"&"+sessionManager.generateEncodedParameter()+"'");
			botoes += " "+form.button("Re-corrigir lista","window.location.href='OpcoesListaProfessor?toDo=efetuaRecorrigir&codAluno="+aluno.getCod()+"&codLista="+lista.getCod()+"&"+sessionManager.generateEncodedParameter()+"'");
		}
		tab.addCelulaWithColSpan("<center>"+botoes+"</center>",2);
		form.fim();
	}
	
	tab.fim();

	// Corregir nota e/ou prazo
	if (isProfessor && (listaGerada.getStatus() >= listaGerada.FINALIZADA)) {
		pagina.saltaLinha();
		pagina.saltaLinha();
		Formulario form = new Formulario(pagina,"OpcoesListaProfessor?toDo=efetuaCorrigirNota&"+sessionManager.generateEncodedParameter());
		form.hidden("codAluno",aluno.getCod());
		form.hidden("codLista",lista.getCod());
		form.hidden("idListaGerada",listaGerada.getCod());
		TabelaSimples tab2 = new TabelaSimples(pagina);
		tab2.addCelula("<B>Corrigir Data: </B>");
		tab2.addCelula(form.textboxData("novaData",listaGerada.getDataFinalizacao()));
		tab2.novaLinha();
		tab2.addCelula("<B>Corrigir Nota: </B>");
		tab2.addCelula(form.textboxTexto("novaNota",listaGerada.getNota()+"",5,5));
		tab2.novaLinha();
		tab2.addCelulaWithColSpan("<CENTER>"+form.botaoSubmit("Alterar")+"</CENTER>",2);
		tab2.fim();
		form.fim();
	}		


	pagina.saltaLinha();
	pagina.saltaLinha();

	if (isProfessor) {
		Menu menu2 = new Menu (pagina,"Outras Opções");
			menu2.addItem("Voltar","OpcoesListaProfessor?"+sessionManager.generateEncodedParameter());
			menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter());
		menu2.fim();
	} else {
		Menu menu2 = new Menu (pagina,"Outras Opções");
			menu2.addItem("Voltar","OpcoesAluno?toDo=opcoesDisciplina&"+sessionManager.generateEncodedParameter());
		menu2.fim();
	}
	
	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();

}
private void resolverLista(SessionManager sessionManager, Aluno aluno, Disciplina disc, Lista lista, ListaGerada listaGerada, Turma turma, boolean isProfessor, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,StringConverter.concatenateWithoutRepetion("Lista",lista.getNome()));
	pagina.init();

	pagina.titulo (StringConverter.concatenateWithoutRepetion("Lista",lista.getNome()));

	if (listaGerada == null) { // ainda nao foi gerada
		listaGerada = auxGeraLista(aluno,lista,turma);
	}
	
	auxCabecalhoLista(aluno, disc, lista, listaGerada, turma, isProfessor, pagina);
	pagina.saltaLinha();

	// Inicia formulario
	Formulario form = new Formulario(pagina,"OpcoesLista?"+sessionManager.generateEncodedParameter());
	form.hidden("toDo","efetuaResolucao");
	form.hidden("finaliza","false");
	form.hidden("testar","false");
	
	// Pega lista de questoes
	Enumeration questoes = listaGerada.getQuestoes().elements();
	// Pega lista de respostas
	Enumeration respostas = listaGerada.getQuestoesRespostas().elements();
	// Pega lista de agrupamentos das questoes
	Enumeration agrupamentos = listaGerada.getQuestoesAgrupamentos().elements();
	// Pega lista de respostas personalizadas das questoes
	Enumeration textosRespostas = listaGerada.getQuestoesTextosRespostas().elements();

	// Imprime a lista de questoes
	int num = 0;
	Agrupamento agrupAtual = null;
	while (questoes.hasMoreElements()) {
		num++;
		Questao questao = (Questao) questoes.nextElement();
		Resposta resp = (Resposta) respostas.nextElement();
		Agrupamento agrup = (Agrupamento) agrupamentos.nextElement();
		String textoResposta = (String) textosRespostas.nextElement();
		if (textoResposta==null) textoResposta = "";

		if ((agrupAtual == null) || (!agrupAtual.equals(agrup))) {
			if ((agrup.getEnunciado() != null) && (!agrup.getEnunciado().equals(""))) {
				pagina.descricao(agrup.getEnunciado()+"<BR>");
			}
			agrupAtual = agrup;
		}
		pagina.descricao("<B>"+num+") </B>"+questao.htmlParaResolucao((resp==null?"":(textoResposta.equals("")?resp.getResposta():textoResposta)),form,listaGerada)+"<BR>");
	}
	
	pagina.saltaLinha();
	if (num == 0) {
		if (!listaGerada.getLista().getEnunciado().equals("")) {
			pagina.descricao(listaGerada.getLista().getEnunciado());
		} else {
			pagina.descricao("não há questões definidas nesta lista.");
		}
	} else {
		out.println(form.botaoSubmit("Salvar e continuar depois"));
		if (listaGerada.getLista().isPermitirTestar()) {
			out.println(" &nbsp;&nbsp;&nbsp;&nbsp; "+form.button("Salvar e Testar","document.dados.testar.value='true'; document.dados.submit();"));
		}
		out.println(" &nbsp;&nbsp;&nbsp;&nbsp; "+form.button("Finalizar Lista","if (confirm('Ao finalizar uma lista, ela será considerada entregue e não será possível voltar a edit�-la. Confirma?')) {document.dados.finaliza.value='true'; document.dados.submit();}"));
			
	}
	
	form.fim();
	
	
	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opções");
		menu2.addItem("Voltar sem salvar as alterações","OpcoesLista?"+sessionManager.generateEncodedParameter());
	menu2.fim();

	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();

}
private void versaoImpressao(SessionManager sessionManager, Aluno aluno, Disciplina disc, Lista lista, ListaGerada listaGerada, Turma turma, boolean isProfessor, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Opções Lista");
	pagina.init();
	pagina.titulo (StringConverter.concatenateWithoutRepetion("Lista",lista.getNome()));

	pagina.saltaLinha();
	if (listaGerada == null) { // ainda nao foi gerada
		listaGerada = auxGeraLista(aluno,lista,turma);
	}	

	auxCabecalhoLista(aluno, disc, lista, listaGerada, turma, isProfessor, pagina);
	
	// Pega lista de questoes
	Enumeration questoes = listaGerada.getQuestoes().elements();
	// Pega lista de agrupamentos das questoes
	Enumeration agrupamentos = listaGerada.getQuestoesAgrupamentos().elements();

	// Imprime a lista de questoes
	int num = 0;
	Agrupamento agrupAtual = null;
	while (questoes.hasMoreElements()) {
		num++;
		Questao questao = (Questao) questoes.nextElement();
		Agrupamento agrup = (Agrupamento) agrupamentos.nextElement();
		if ((agrupAtual == null) || (!agrupAtual.equals(agrup))) {
			if ((agrup.getEnunciado() != null) && (!agrup.getEnunciado().equals(""))) {
				pagina.descricao(agrup.getEnunciado()+"<BR>");
			}
			agrupAtual = agrup;
		}
		pagina.descricao("<B>"+num+") </B>"+questao.htmlParaImpressao()+"<BR>");
	}

	pagina.saltaLinha();
	if (num == 0) {
		if (!listaGerada.getLista().getEnunciado().equals("")) {
			pagina.descricao(listaGerada.getLista().getEnunciado());
		} else {
			pagina.descricao("não há questões definidas nesta lista.");
		}
	}
	
	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opções");
		menu2.addItem("Voltar","OpcoesLista?"+sessionManager.generateEncodedParameter());
	menu2.fim();

	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();

}

private ListaGerada auxGeraLista(Aluno aluno, Lista lista, Turma turma) throws Exception {

	ListaGerada_ger listageradager = new ListaGerada_ger();
	// cria Lista Gerada
	Vector alunos = new Vector();
	alunos.addElement(aluno);
	return listageradager.inclui(ListaGerada.ABERTA,0,new Date(),null,lista,turma,alunos);

}

private void efetuaDefinicaoGrupo(SessionManager sessionManager, Aluno aluno, Disciplina disc, Lista lista, ListaGerada listaGerada, Turma turma, boolean isProfessor, Hashtable parametros, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Definição de Grupo");
	pagina.init();

	// Pega lista de alunos escolhidos
	Vector escolhidos = new Vector();
	Aluno_ger alunoger = new Aluno_ger();
	Enumeration alunos = alunoger.getElementsByTurma(turma).elements();
	while (alunos.hasMoreElements()) {
		Aluno a = (Aluno) alunos.nextElement();
		if (parametros.get("aluno"+a.getCod()) != null) {
			escolhidos.addElement(a);
		}
	}
	escolhidos.trimToSize();

	int numAlunosListaAtual = 1;
	if (listaGerada != null) {
		numAlunosListaAtual = listaGerada.getAlunos().size();
	}
	
	// Testa se � maior que o maximo
	if (escolhidos.size()+numAlunosListaAtual>lista.getNumMaxAlunosPorGrupo()) {
		pagina.javascript("alert('O m�ximo de alunos � "+lista.getNumMaxAlunosPorGrupo()+"'); window.history.back();");
		return;
	}

	// Testa se � menor que o minimo
	if (escolhidos.size()+numAlunosListaAtual<lista.getNumMinAlunosPorGrupo()) {
		pagina.javascript("alert('O m�nimo de alunos � "+lista.getNumMinAlunosPorGrupo()+"'); window.history.back();");
		return;
	}

	// Testa se os escolhidos estao ok
	String mensagemErro = "";
	Vector listasParaRemover = new Vector();
	ListaGerada_ger listageradager = new ListaGerada_ger();
	for (int i = 0; i < escolhidos.size(); i++) {
		Aluno a = (Aluno) escolhidos.elementAt(i);
		ListaGerada listaGAluno = listageradager.getElement(lista,a);
		if (listaGAluno != null) { // A pessoa ja tem lista gerada
			if (listaGAluno.getAlunos().size() > 1) {
				mensagemErro += a.getNome()+" já está em outro grupo.\\n";
			} else if ((listaGAluno.getStatus() != ListaGerada.ABERTA)) {
				mensagemErro += a.getNome()+" já iniciou sozinho a resolução da lista.\\n";
			} else {
				listasParaRemover.addElement(listaGAluno);
			}
		}
	}

	// Se houver algum erro
	if (!mensagemErro.equals("")) {
		pagina.javascript("alert('"+mensagemErro+"'); window.history.back();");
		return;
	}

	// Remove as listas dos alunos que tiverem
	for (int i = 0; i < listasParaRemover.size(); i++) {
		ListaGerada l = (ListaGerada) listasParaRemover.elementAt(i);
		listageradager.remove(l);
	}

	Vector membrosAntigos;
	Vector novosMembros;
	// Se nao existir lista gerada, cria
	if (listaGerada == null) {
		membrosAntigos = new Vector();
		membrosAntigos.addElement(aluno);
		novosMembros = escolhidos;
		escolhidos.addElement(aluno);
		listaGerada = listageradager.inclui(ListaGerada.ABERTA,0,new Date(),null,lista,turma,escolhidos);
	} else {
		// Inclui os alunos na mesma lista
		membrosAntigos = (Vector) listaGerada.getAlunos().clone();
		novosMembros = escolhidos;
		for (int i = 0; i < escolhidos.size(); i++) {
			Aluno a = (Aluno) escolhidos.elementAt(i);
			listageradager.addAlunoListaGerada(a, listaGerada);
		}
	}
		
	// Manda e-mail
	if ((escolhidos.size() != 1) || !escolhidos.firstElement().equals(aluno)) {
		// e-mails para os atuais membros do grupo
		for (int i = 0; i < membrosAntigos.size(); i++) {
			Aluno a = (Aluno) membrosAntigos.elementAt(i);
			String str = (a.getSexo().equals("M")?"Caro ":"Cara ") + a.getNome()+",\n\n";
			str += aluno.getNome()+" adicionou os seguintes alunos em seu grupo na disciplina "+disc.getNome()+", "+StringConverter.concatenateWithoutRepetion("lista",lista.getNome())+".\n";
			for (int j = 0; j < escolhidos.size(); j++) {
				Aluno a2 = (Aluno) escolhidos.elementAt(j);
				str += " - "+a2.getNome() + " ("+a2.getEmail()+")\n";
			}
			str += "\n\nAtenciosamente,\n\nSite de Aulas Marco Gerosa\n\n";
			SendMail sender = new SendMail();
			sender.addTo(a.getEmail(), a.getEmail());
			sender.setAddressFrom("gerosa@ime.usp.br");
		    sender.setNameFrom("Site de Aulas Gerosa");
		    sender.setMessageSubject("[Gerosa Site] Notificação de inclusão de aluno em grupo");
		    sender.setMessageText(str);
		    SendMailThread send = new SendMailThread();
		    send.setSendMail(sender);
		    send.start();
		}
		
		// e-mails para os novos membros
		for (int i = 0; i < novosMembros.size(); i++) {
			Aluno a = (Aluno) novosMembros.elementAt(i);
			String str = (a.getSexo().equals("M")?"Caro ":"Cara ") + a.getNome()+",\n\n";
			str += aluno.getNome()+" adicionou Você em seu grupo na disciplina "+disc.getNome()+", "+StringConverter.concatenateWithoutRepetion("lista",lista.getNome())+".\n\n";
			str += "Os membros do grupo são:\n";
			for (int j = 0; j < listaGerada.getAlunos().size(); j++) {
				Aluno a2 = (Aluno) listaGerada.getAlunos().elementAt(j);
				str += " - "+a2.getNome() + " ("+a2.getEmail()+")\n";
			}
			str += "\n\nAtenciosamente,\n\nSite de Aulas Marco Gerosa\n\n";
			SendMail sender2 = new SendMail();
			sender2.addTo(a.getEmail(), a.getEmail());
			sender2.setAddressFrom("gerosa@ime.usp.br");
		    sender2.setNameFrom("Site de Aulas Gerosa");
		    sender2.setMessageSubject("[Gerosa Site] Notificação de inclusão em grupo");
		    sender2.setMessageText(str);
		    SendMailThread send2 = new SendMailThread();
		    send2.setSendMail(sender2);
		    send2.start();
		}
	}	

	// Monta pagina de resposta
	pagina.javascript("alert('Grupo definido com sucesso '); window.location = 'OpcoesLista?"+sessionManager.generateEncodedParameter()+"';");

	pagina.fim();
	
}

private void efetuaResolucao(SessionManager sessionManager, Aluno aluno, Disciplina disc, Lista lista, ListaGerada listaGerada, Turma turma, boolean isProfessor, Hashtable parametros, PrintWriter out) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Resolução Lista");
	pagina.init();

	// Testa se a lista pode ser resolvida
	if ((listaGerada.getStatus() == ListaGerada.FINALIZADA) || (listaGerada.getStatus() == ListaGerada.EMCORRECAO) || (listaGerada.getStatus() == ListaGerada.CORRIGIDA)) {
		pagina.javascript("alert('não � possível salvar as alterações. A lista já foi finalizada anteriormente.'); window.history.back();");
		return;
	}
	
	boolean finaliza = parametros.get("finaliza").toString().equals("true");
	boolean testar = parametros.get("testar").toString().equals("true");
	if (testar && !listaGerada.getLista().isPermitirTestar()) {
		pagina.javascript("alert('não � possível testar.'); window.history.back();");
		return;
	}
	
	// Pega lista de questoes
	Enumeration questoes = listaGerada.getQuestoes().elements();
	Enumeration respostas = listaGerada.getQuestoesRespostas().elements();

	ListaGerada_ger listageradager = new ListaGerada_ger();
	// Processa a lista
	while (questoes.hasMoreElements()) {
		Questao questao = (Questao) questoes.nextElement();
		Resposta resp = (Resposta) respostas.nextElement();
		String newResp;
		//try {
			newResp = questao.leRespostaFromHtmlParameters(parametros,listaGerada);
		//} catch (Exception e) { // trata excessão na resposta do aluno
		//	pagina.javascript("alert('não foi possível salvar suas alterações.\\n"+e.getMessage()+"'); window.location = 'OpcoesLista?toDo=resolverLista&"+sessionManager.generateEncodedParameter()+"';");
		//	return;
		//}
		listageradager.alteraRespostaQuestao(listaGerada,questao,resp,newResp);
	}
	
	// Monta pagina de resposta
	if (finaliza) {
		listageradager.alteraStatusFinalizada(listaGerada);
		pagina.javascript("alert('Lista entregue.\\nObrigado.'); window.location = 'OpcoesLista?"+sessionManager.generateEncodedParameter()+"';");
	} else {
		if (listaGerada.getStatus() == ListaGerada.ABERTA) {
			listageradager.alteraStatusEdicao(listaGerada);
		}
		if (testar) {
			pagina.javascript("window.location = 'OpcoesLista?toDo=testar&"+sessionManager.generateEncodedParameter()+"';");
		} else {
			pagina.javascript("alert('Respostas salvas.\\nLembre-se que a lista s� � considerada entregue quando Você finaliz�-la.'); window.location = 'OpcoesLista?"+sessionManager.generateEncodedParameter()+"';");
		}
	}

	pagina.fim();
	
}
}
