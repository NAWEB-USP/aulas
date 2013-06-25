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

public class OpcoesAluno extends Servlet {

public void action(javax.servlet.http.HttpServletRequest request, PrintWriter out) throws Exception {

	// Inicializa SessionManager
	SessionManager sessionManager = new SessionManager(request);

	// Le aluno
	String idAluno = sessionManager.getElement("idUsuario");
	Aluno_ger ger = new Aluno_ger();
	Aluno aluno = ger.getElement(idAluno);

	// A partir do toDo seleciona m�todo
	String toDo = (String) request.getParameter("toDo");
	if ((toDo == null) || (toDo.equals("")) || (toDo.equals("opcoesAluno"))) {
		opcoesAluno (sessionManager,out, aluno);

	} else if (toDo.equals("alterarDados")) {
		GerenciaAlunos g = new GerenciaAlunos();
		g.cadastroAluno(sessionManager,out,aluno,true);
		
	} else if (toDo.equals("pedirMatricula")) {
		pedirMatricula (sessionManager,out, aluno);
		
	} else if (toDo.equals("escolherTurma")) {
		escolherTurma (sessionManager,request, out, aluno);

	} else if (toDo.equals("efetivarMatricula")) {
		efetivarMatricula (sessionManager,request, out, aluno);
		
	} else if (toDo.equals("opcoesDisciplina")) {
		String cod = (String) request.getParameter("codDisciplina");
		String idDisciplina = cod;
		if ((cod == null) || (cod.equals(""))) {
			idDisciplina = sessionManager.getElement("idDisciplina");
		} else {
			sessionManager.addElement("idDisciplina",idDisciplina);
		}
		if ((idDisciplina == null) || (idDisciplina.equals(""))) {throw new Exception ("Falta o cod");}
		Disciplina_ger gerdisc = new Disciplina_ger();
		Disciplina disc = gerdisc.getElement(idDisciplina);

		opcoesDisciplina (sessionManager,out, aluno, disc);

	} else {
		throw new Exception ("Opção não reconhecida.");
	}

}
private void efetivarMatricula(SessionManager sessionManager, javax.servlet.http.HttpServletRequest request, PrintWriter out, Aluno aluno) throws Exception {

	// Inicializa pagina
	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Matrícula");
	pagina.init();

	// Le dados do formulario
	String codTurma = (String) request.getParameter("codTurma");
	if (codTurma.equals("0")) {
		pagina.javascript("alert('Por favor, escolha uma turma.'); window.history.back();");
		return;
	}
	Turma_ger gerturma = new Turma_ger();
	Turma turma = gerturma.getElementByCod(codTurma);

	// Efetua matricula
	if (aluno.getTurmas().contains(turma)) { // Ja esta matriculado
		pagina.javascript("alert('Aluno já matriculado nesta disciplina.'); window.location = 'OpcoesAluno?"+sessionManager.generateEncodedParameter()+"';");
		return;
	}
	
	Aluno_ger alunoger = new Aluno_ger();
	alunoger.matriculaAlunoTurma (aluno,turma);

	// Monta pagina de resposta
	pagina.javascript("alert('Matrícula efetivada com sucesso.'); window.location = 'OpcoesAluno?"+sessionManager.generateEncodedParameter()+"';");
	
	pagina.fim();

	

}
private void escolherTurma(SessionManager sessionManager, javax.servlet.http.HttpServletRequest request, PrintWriter out, Aluno aluno) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Escolher turma");
	pagina.init();
	
	// Le dados do formulario
	String codDisc = (String) request.getParameter("codDisc");
	if (codDisc.equals("0")) {
		pagina.javascript("alert('Por favor, escolha uma disciplina.'); window.history.back();");
		return;
	}
	Disciplina_ger gerDisciplina = new Disciplina_ger();
	Disciplina disc = gerDisciplina.getElement(codDisc);

	Turma_ger turmager = new Turma_ger();
	Vector turmas = turmager.getElements(disc);

	// testa se aluno já está matriculado
	for (int i = 0; i < turmas.size(); i++) {
		Turma t = (Turma) turmas.elementAt(i);
		if (aluno.getTurmas().contains(t)) {
			pagina.javascript("alert('Aluno já matriculado nesta disciplina.'); window.location = 'OpcoesAluno?"+sessionManager.generateEncodedParameter()+"';");
			return;
		}
	}
	
	
	if (turmas.size() == 1) { // s� tem uma turma
		pagina.javascript("window.location = 'OpcoesAluno?toDo=efetivarMatricula&codTurma="+((Turma) turmas.elementAt(0)).getCod()+"&"+sessionManager.generateEncodedParameter()+"';");

	} else { // mais de uma turma
	
		pagina.titulo ("Escolha da turma para a "+StringConverter.concatenateWithoutRepetion("Disciplina",disc.getNome()));

		pagina.descricao((aluno.getSexo().equals("M")?"Caro ":"Cara ")+aluno.getNome()+", escolha a turma da disciplina "+disc.getNome()+" que Você esteja cursando.");
		pagina.saltaLinha();

		Formulario form = new Formulario(pagina,"OpcoesAluno?toDo=efetivarMatricula&"+sessionManager.generateEncodedParameter());
		
		TabelaFormulario tab = new TabelaFormulario (pagina);

			tab.addCelula("<B>Disciplina: </B>");
			tab.addCelula(disc.getNome());
			
			tab.addCelula("<B>Turma: </B>");
			Enumeration elements = turmas.elements();
			Vector opcoes = new Vector();
			Vector valores = new Vector();
			valores.addElement("0");
			opcoes.addElement("--------------------------------");
			while (elements.hasMoreElements()) {
				Turma obj1 = (Turma) elements.nextElement();
				valores.addElement(obj1.getCod());
				opcoes.addElement(obj1.getNome());
			}
			tab.addCelula(form.selectBox("codTurma",valores,opcoes,null));
			tab.novaLinha();
			tab.addCelulaCom2Botes(form.botaoSubmit("Escolher"),form.button("Cancelar","window.location='OpcoesAluno?"+sessionManager.generateEncodedParameter()+"'"));
		
		tab.fim();
		form.fim();

		pagina.rodape(TextoPadrao.copyright());

	
	}
	
	pagina.fim();


}
private void opcoesAluno(SessionManager sessionManager, PrintWriter out, Aluno aluno) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Opções de Aluno " + aluno.getNome());
	pagina.init();

	pagina.titulo ((aluno.getSexo().equals("M")?"Bem-vindo ":"Bem-vinda ") + aluno.getNome());

	pagina.descricao("Escolha uma de suas disciplinas ou uma das Opções abaixo.");
	pagina.saltaLinha();

	Vector v = aluno.getTurmas();
	Collections.sort(v);
	Enumeration lista = v.elements();
	
	pagina.saltaLinha();
	Menu menu = new Menu (pagina,"Disciplinas");
	if (!lista.hasMoreElements()) {
		pagina.descricao("Você não está cadastrad"+(aluno.getSexo().equals("M")?"o":"a")+" em disciplinas neste site.<BR>Por favor, clique abaixo no link <B>Cadastrar-se em Disciplina</B>.");
	} else {
		while (lista.hasMoreElements()) {
			Turma turma = (Turma) lista.nextElement();
			Disciplina disc = turma.getDisc();
			menu.addItem(disc.getNome()+" ("+turma.getNome()+")","OpcoesAluno?toDo=opcoesDisciplina&codDisciplina="+disc.getCod()+"&"+sessionManager.generateEncodedParameter());
		}
	}
	menu.fim();

	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opções");
		menu2.addItem("Alterar dados cadastrais","OpcoesAluno?toDo=alterarDados&"+sessionManager.generateEncodedParameter());
		menu2.addItem("Cadastrar-se em disciplina","OpcoesAluno?toDo=pedirMatricula&"+sessionManager.generateEncodedParameter());
		menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter());
	menu2.fim();


	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();

}
private void opcoesDisciplina(SessionManager sessionManager, PrintWriter out, Aluno aluno, Disciplina disc) throws Exception {

	PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Opções");
	pagina.init();

	pagina.titulo (StringConverter.concatenateWithoutRepetion("Disciplina",disc.getNome()));

	pagina.descricao("Escolha uma das listas ou uma das Opções abaixo.");
	pagina.saltaLinha();
	pagina.saltaLinha();

	// descobre turma
	Turma turma = null;
	Enumeration lst = aluno.getTurmas().elements();
	while (lst.hasMoreElements()) {
		Turma t = (Turma) lst.nextElement();
		if (t.getDisc().equals(disc)) {
			turma = t;
		}
	}
	sessionManager.addElement("idTurma",turma.getCod());

	// Pega Listas da turma
	Enumeration listas = turma.getListasAtivas().elements();

	// Mostra listas
	ListaGerada_ger listageradager = new ListaGerada_ger();
	Menu menu = new Menu (pagina,"Listas");
	if (!listas.hasMoreElements()) {
		pagina.descricao("não há listas nesta disciplina.");
	} else {
		while (listas.hasMoreElements()) {
			Lista lista = (Lista) listas.nextElement();
			// Detecta o status da lista
			ListaGerada listaGerada = listageradager.getElement(lista,aluno);
			if (listaGerada == null) { // Nova
				menu.addItem(lista.getNome()+" (Nova) - Prazo = "+((turma.getData1(lista) == null)?"sem prazo":Data.formataData(turma.getData1(lista))),"OpcoesLista?toDo=opcoesLista&codLista="+lista.getCod()+"&"+sessionManager.generateEncodedParameter());
			} else if (listaGerada.getStatus() == ListaGerada.ABERTA) {
				menu.addItem(lista.getNome()+" (Em branco) - Prazo = "+((turma.getData1(lista) == null)?"sem prazo":Data.formataData(turma.getData1(lista))),"OpcoesLista?toDo=opcoesLista&codLista="+lista.getCod()+"&"+sessionManager.generateEncodedParameter());
			} else if (listaGerada.getStatus() == ListaGerada.EDICAO) {
				menu.addItem(lista.getNome()+" (Em edição) - Prazo = "+((turma.getData1(lista) == null)?"sem prazo":Data.formataData(turma.getData1(lista))),"OpcoesLista?toDo=opcoesLista&codLista="+lista.getCod()+"&"+sessionManager.generateEncodedParameter());
			} else if (listaGerada.getStatus() == ListaGerada.FINALIZADA) {
				menu.addItem(lista.getNome()+" (Finalizada)","OpcoesLista?toDo=opcoesLista&codLista="+lista.getCod()+"&"+sessionManager.generateEncodedParameter());
			} else if (listaGerada.getStatus() == ListaGerada.EMCORRECAO) {
				menu.addItem(lista.getNome()+" (Em correção)","OpcoesLista?toDo=opcoesLista&codLista="+lista.getCod()+"&"+sessionManager.generateEncodedParameter());
			} else if (listaGerada.getStatus() == ListaGerada.CORRIGIDA) {
				menu.addItem(lista.getNome()+" (Corrigida) - Nota = "+listaGerada.getNota(),"OpcoesLista?toDo=opcoesLista&codLista="+lista.getCod()+"&"+sessionManager.generateEncodedParameter());
			}
		}
	}
	menu.fim();

	pagina.saltaLinha();
	pagina.saltaLinha();
	Menu menu2 = new Menu (pagina,"Outras Opções");
		menu2.addItem("Escolher outra disciplina","OpcoesAluno?"+sessionManager.generateEncodedParameter());
		menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter());
	menu2.fim();


	// ------- Mais informações -------
	if (((disc.getDescricao() != null) && (!disc.getDescricao().equals(""))) || ((turma.getDescricao() != null) && (!turma.getDescricao().equals("")))) {
		pagina.saltaLinha();
		pagina.saltaLinha();
		ListaItens titulo = new ListaItens (pagina,"Mais informações sobre a Disciplina");
		titulo.fim();
		if (disc.getDescricao() != null) {
			pagina.descricao(disc.getDescricao());
		}
		if (turma.getDescricao() != null) {
			pagina.descricao(turma.getDescricao());
		}
	}

	
	pagina.rodape(TextoPadrao.copyright());

	pagina.fim();

}
private void pedirMatricula(SessionManager sessionManager, PrintWriter out, Aluno aluno) throws Exception {

	Disciplina_ger gerdisc = new Disciplina_ger();
	Vector disciplinas = gerdisc.getDisciplinasAtivas();
	// remove disciplinas ja cursadas
	Enumeration turmas = aluno.getTurmas().elements();
	while (turmas.hasMoreElements()) {
		Turma t = (Turma) turmas.nextElement();
		disciplinas.removeElement(t.getDisc());
	}
	Collections.sort(disciplinas);
	Enumeration elements = disciplinas.elements();
		
	if (!elements.hasMoreElements()) {
		PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Matrícula");
		pagina.init();
		pagina.javascript("alert('não há novas disciplinas'); window.location = 'OpcoesAluno?"+sessionManager.generateEncodedParameter()+"';");
		pagina.fim();
		
	} else {
		PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Opções de Aluno " + aluno.getNome());
		pagina.init();

		pagina.titulo ("Escolha de Disciplina");

		pagina.descricao((aluno.getSexo().equals("M")?"Caro ":"Cara ")+aluno.getNome()+", escolha uma disciplina que Você esteja cursando.");
		pagina.saltaLinha();
		Formulario form = new Formulario(pagina,"OpcoesAluno?toDo=escolherTurma&"+sessionManager.generateEncodedParameter());

		TabelaFormulario tab = new TabelaFormulario (pagina);
		tab.addCelula("Disciplina: ");
		Vector opcoes = new Vector();
		Vector valores = new Vector();
		valores.addElement("0");
		opcoes.addElement("--------------------------------");
		while (elements.hasMoreElements()) {
			Disciplina obj1 = (Disciplina) elements.nextElement();
			valores.addElement(obj1.getCod());
			opcoes.addElement(obj1.getNome());
		}
		tab.addCelula(form.selectBox("codDisc",valores,opcoes,null));
		tab.novaLinha();
		tab.addCelulaCom2Botes(form.botaoSubmit("Escolher"),form.button("Cancelar","window.location='OpcoesAluno?"+sessionManager.generateEncodedParameter()+"'"));
		
		tab.fim();
		form.fim();
		pagina.rodape(TextoPadrao.copyright());

		pagina.fim();

	}

}
}
