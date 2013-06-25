package com.aulas.util.oldSessionmanagerEmMemoria;

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

import java.io.*;
import java.sql.*;
import java.util.*;

import com.aulas.business.*;

public class SessionManager {
	private Session sessao;	

public SessionManager(javax.servlet.http.HttpServletRequest request) throws Exception {

	this.sessao = null;
	
	String parametro = request.getParameter("SessionManagerParameter");

	Session_ger sessionger = new Session_ger();
	sessionger.removeSessoesExpiradas();

	if (parametro == null) {
		this.sessao = sessionger.inclui(new java.util.Date());
	} else {
		this.sessao = sessionger.getElementById(parametro);
		if (this.sessao == null) {
			throw new SessionException();
		}
		sessionger.altera(this.sessao,new java.util.Date());
	}
	
}
public void addElement (String name, String value) throws Exception {

	securityTest(name,value);
	
	SessionItem_ger ger = new SessionItem_ger();

	Enumeration elems = ger.getElementsByIdSessao(this.sessao.getId()).elements();

	// procura se ja existe o nome

	while (elems.hasMoreElements()) {
		SessionItem s = (SessionItem) elems.nextElement();
		if (s.getCampo().equals(name)) {
			ger.remove(s);
		}
	}
	
	ger.inclui(this.sessao.getId(),name,value);

}
public boolean checaPermissaoProfessor () throws Exception {
	
	// testa os parametros armazenados
	String idUsuario = getElement("idUsuario");
	if ((idUsuario == null) || (!idUsuario.equals("0"))) {
		throw new SecurityException();
	} else {
		return true;
	}
	
	
}

public void finalizarSessao() throws Exception {
	
	// Remove itens
	SessionItem_ger ger = new SessionItem_ger();
	ger.removeElementsByIdSessao(this.sessao.getId());

	// Remove sessao
	Session_ger sessionger = new Session_ger();
	sessionger.remove(this.sessao);
			
}
public String generateEncodedParameter () throws Exception {
	
	return "SessionManagerParameter="+this.sessao.getId();
	
}
public String getElement (String name) throws Exception {
	
	SessionItem_ger ger = new SessionItem_ger();

	Enumeration elems = ger.getElementsByIdSessao(this.sessao.getId()).elements();

	// procura o nome
	while (elems.hasMoreElements()) {
		SessionItem s = (SessionItem) elems.nextElement();
		if (s.getCampo().equals(name)) {
			return s.getValor();
		}
	}
	
	return null;
	
}
public void removeElement (String name) throws Exception {
	
	SessionItem_ger ger = new SessionItem_ger();

	Enumeration elems = ger.getElementsByIdSessao(this.sessao.getId()).elements();

	// procura o nome
	while (elems.hasMoreElements()) {
		SessionItem s = (SessionItem) elems.nextElement();
		if (s.getCampo().equals(name)) {
			ger.remove(s);
		}
	}

}
public void securityTest (String name, String value) throws Exception {
	
	// testa os parametros armazenados
	String idUsuario = getElement("idUsuario");
	if ((idUsuario == null) || (idUsuario.equals("0"))) return;

	Aluno aluno = (new Aluno_ger()).getElementByCod(idUsuario);
		
	// Disciplina
	if (name.equals("idDisciplina")) {
		Disciplina disc = null;
		if (value != null) {
			disc = (new Disciplina_ger()).getElementByCod(value);
			Enumeration turmas = aluno.getTurmas().elements();
			while (turmas.hasMoreElements()) {
				Turma t = (Turma) turmas.nextElement();
				if (t.getDisc().equals(disc)) {
					return;
				}
			}
			throw (new SecurityException());
		}
	}

	// Turma
	if (name.equals("idTurma")) {
		Turma turma;
		if (value != null) {
			turma = (new Turma_ger()).getElementByCod(value);
			if (!aluno.getTurmas().contains(turma)) {
				throw (new SecurityException());
			}
		} else {
			return;
		}
	}
		
	// Lista
	if (name.equals("idLista")) {
		Lista lista = null;
		if (value != null) {
			lista = (new Lista_ger()).getElementByCod(value);
			Enumeration turmas = aluno.getTurmas().elements();
			while (turmas.hasMoreElements()) {
				Turma t = (Turma) turmas.nextElement();
				if (t.getListasAtivas().contains(lista)) {
					return;
				}
			}
			throw (new SecurityException());
		}
	}

	// ListaGerada
	/*String idListaGerada = getElement("idListaGerada");
	ListaGerada listaGerada;
	if (idListaGerada != null) {
		listaGerada = (new ListaGerada_ger()).getElementByCod(idListaGerada);
		if (((lista != null) && !listaGerada.getLista().equals(lista)) || !listaGerada.getAlunos().contains(aluno)) {
			throw (new SecurityException());
		}
	}*/
	
}
}
