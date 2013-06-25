package com.aulas.util.sessionmanager;

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
import java.util.*;

import com.aulas.business.*;
import com.aulas.util.*;

//import javax.crypto.*;
//import javax.swing.JOptionPane;
import java.net.*;

import javax.servlet.http.HttpSession;
//import hcrypto.cipher.*;

public class SessionManager {

	//private static boolean manterSessoesAtivasEmMemoria = true;
	//private final static boolean CRIPTOGRAFA = true;
	// tempo expiracao = 0 - nunca expira
	//private final static int TEMPOEXPIRACAO = 1000*60*60*4; // 4 horas
	//private final static String SEPARADOR = "|";
	//private final static int MAXSESSOES = 100000;
	
	//private static Vector sessoesAtivas = new Vector();
	
	// Dados da sessão
    //private Vector<String> names;
	//private Vector<String> values;

	//private static SecretKey desKey = null;
	//private Criptografia cript;
	
	/*private static String antes1;
	private static String antes2;
	private static String antes3;
	private static String depois1;
	private static String depois2;*/
	
	private HttpSession session;

public SessionManager(javax.servlet.http.HttpServletRequest request) throws Exception {

    //if (desKey == null) {
    //    KeyGenerator keygen = KeyGenerator.getInstance("Blowfish");
	//    desKey = keygen.generateKey();
   // }
    // if (cript == null) {
    //    cript = new Criptografia();
    //}
    
	this.session = request.getSession();
	String parametro = (String) session.getAttribute("_sessionP");
	
	if ((parametro == null) || (parametro.equals(""))) {
		// ** nova sessao **
		session.setAttribute("_sessionP","5");

	} else {
		// valida parametro
		if (!parametro.equals("5")) {
			throw new SessionException("Problema na validação da seção.");  
		}

	}
	
}
 
public String descriptografa (String parametro) throws Exception {
    
	//TranspositionCipherAlgorithm tca = new TranspositionCipherAlgorithm("duck10");
	//parametro = tca.decrypt(parametro);
    /*Cipher desCipher = Cipher.getInstance("Blowfish");
    desCipher.init(Cipher.DECRYPT_MODE, desKey);
    //JOptionPane.showMessageDialog(null, parametro);
    byte[] ciphertext  = desCipher.doFinal(parametro.getBytes());
    return new String (ciphertext);*/
    /*Cipher cipher = Cipher.getInstance("Caesar");
    HistoricalKey key = HistoricalKey.getInstance("Caesar", cipher.getProvider());
    key.init("55/printable");
    cipher.init(key);
    return cipher.decrypt(parametro);*/
    Criptografia cript = new Criptografia();
    return cript.decrypt(parametro);
}

public String criptografa (String parametro) throws Exception {
    
	//TranspositionCipherAlgorithm tca = new TranspositionCipherAlgorithm("duck10");
	//parametro = tca.crypt(parametro);
    /*Cipher desCipher = Cipher.getInstance("Blowfish");
    desCipher.init(Cipher.ENCRYPT_MODE, desKey);
    //JOptionPane.showMessageDialog(null, parametro);
    byte[] ciphertext  = desCipher.doFinal(parametro.getBytes());
    return new String (ciphertext);*/
    /*Cipher cipher = Cipher.getInstance("Caesar");
    HistoricalKey key = HistoricalKey.getInstance("Caesar", cipher.getProvider());
    key.init("55/printable");
    cipher.init(key);
    return cipher.encrypt(parametro);*/
    Criptografia cript = new Criptografia();
    return cript.encrypt(parametro);
}


public void addElement (String name, String value) throws Exception {

	if (!name.equals("idUsuario")) securityTest(name,value);
	
	session.setAttribute(name, value);
	
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
	session.invalidate();
}

public String generateEncodedParameter () throws Exception {
	
	return "";
	
}

public String getElement (String name) throws Exception {
	
	String element = (String) session.getAttribute(name);
	if (name.equals("idUsuario") && (element == null || element.equals(""))) throw new SessionException();  
	return element;
}

public void removeElement (String name) throws Exception {
	
	session.removeAttribute(name);
	
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
