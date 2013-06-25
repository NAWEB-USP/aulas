package com.aulas.util.oldsessionmanager;

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
//import hcrypto.cipher.*;

public class SessionManager {

	private static boolean manterSessoesAtivasEmMemoria = true;
	private final static boolean CRIPTOGRAFA = true;
	// tempo expiracao = 0 - nunca expira
	private final static int TEMPOEXPIRACAO = 1000*60*60*4; // 4 horas
	private final static String SEPARADOR = "|";
	private final static int MAXSESSOES = 100000;
	
	private static Vector sessoesAtivas = new Vector();
	
	// Dados da sessão
    private Vector<String> names;
	private Vector<String> values;

	//private static SecretKey desKey = null;
	//private Criptografia cript;
	
	/*private static String antes1;
	private static String antes2;
	private static String antes3;
	private static String depois1;
	private static String depois2;*/

public SessionManager(javax.servlet.http.HttpServletRequest request) throws Exception {

    //if (desKey == null) {
    //    KeyGenerator keygen = KeyGenerator.getInstance("Blowfish");
	//    desKey = keygen.generateKey();
   // }
    // if (cript == null) {
    //    cript = new Criptografia();
    //}
    
	manterSessoesAtivasEmMemoria = !(new Configuracao_ger()).getElement("Debug").equals("true");
	
	// Inicializa sessoes ativas
	this.names = new Vector<String>();
	this.values = new Vector<String>();
	
	String parametro = request.getParameter("_sessionP");
	
	if ((parametro == null) || (parametro.equals(""))) {
		// ** nova sessao **
		addElement("validPar","5");
		if (manterSessoesAtivasEmMemoria) {
			synchronized (sessoesAtivas) {
				// gera id
				String id;
				do {
					Random r = new Random(); // gerador de numero aleatorio
				 	id = String.valueOf(r.nextInt()%MAXSESSOES);
				} while (this.sessoesAtivas.contains(id));
				// poe na memoria o id
				System.out.println("New Session "+id);
                addElement("sessionId",id);
				this.sessoesAtivas.addElement(id);
			}
		}

	} else {
		// ** sessao ja existente **
		// descriptografa a parametro -- chave = duck10
		if (CRIPTOGRAFA) {
		    //boolean ok = parametro.equals(antes3);
		    //parametro = URLDecoder.decode(parametro,"UTF-8");
		    //depois1 = parametro;
		    //ok = depois1.equals(antes2);
		    parametro = descriptografa (parametro);
		    //depois2 = parametro;
		    //ok = depois2.equals(antes1);
		    //int a = 1;
 		}
	
		// valida parametro
		if (!parametro.startsWith("validPar"+SEPARADOR+"5"+SEPARADOR)) {
			throw new SessionException("Problema na validação da seção.");  
		}

		// processa parametro
		while (!parametro.equals("")) { // processa parametro
            this.names.addElement(parametro.substring(0,parametro.indexOf(SEPARADOR)));
			parametro = parametro.substring(parametro.indexOf(SEPARADOR)+1);
            this.values.addElement(parametro.substring(0,parametro.indexOf(SEPARADOR)));
			parametro = parametro.substring(parametro.indexOf(SEPARADOR)+1);
		}

		// verifica sessionId
		if (manterSessoesAtivasEmMemoria) {
			synchronized (sessoesAtivas) {
				String sessionId = this.getElement("sessionId");
				if (!sessoesAtivas.contains(sessionId)) {
					throw new SessionException(sessionId + " is not in the memory.");
				}
			}
		}
			
		// verifica data expiracao
		String str = getElement("sessionDate");
		if ((str == null) || (str.equals(""))) throw new SessionException("Data não presente ou inv�lida.");
		if (TEMPOEXPIRACAO != 0) {
			long sessionDate = Long.parseLong(str);
			long agora = (new java.util.Date()).getTime();
			if ((agora - sessionDate) > TEMPOEXPIRACAO) { // Testa se a sessao expirou
				throw new SessionException("Session Expired");
			}
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

	securityTest(name,value);
	
	if (names.contains(name)) {
		removeElement(name);
	}
	
	names.addElement(name);
	values.addElement(value);
	
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
    String id = this.getElement("sessionId");
    System.out.println("Remove Session "+id);
	synchronized (sessoesAtivas) {
		if (manterSessoesAtivasEmMemoria) {
			this.sessoesAtivas.removeElement(id);
		}
	}
}
public String generateEncodedParameter () throws Exception {
	
	// Atualiza data
	addElement("sessionDate",Long.toString((new Date()).getTime()));

	// gera parametros
	String str = "_sessionP=";
	String parametro = "";
	for (int i = 0; i < names.size(); i++) {
		parametro += names.elementAt(i) + SEPARADOR + values.elementAt(i) + SEPARADOR;
	}
 
	// criptografa a parametro -- chave = duck10
	if (CRIPTOGRAFA) {
	    // antes1 = parametro;
	    parametro = criptografa(parametro);
	    //String xxx = descriptografa(parametro);
	    //boolean ok = xxx.equals(antes1);
	    //antes2 = parametro;
	    parametro = URLEncoder.encode(parametro,"UTF-8");
	    //ok = URLDecoder.decode(parametro, "UTF-8").equals(antes2);
	    //antes3 = parametro;
	}

	// retorna
	return str + parametro;
	
}
public String getElement (String name) throws Exception {
	
	int pos = names.indexOf(name);

	if (pos == -1) return null;
	
	return values.elementAt(pos).toString();
	
}
public Vector getElementsName () throws Exception {
	
	return this.names;
	
}
public void removeElement (String name) throws Exception {
	
	int pos = names.indexOf(name);

	if (pos == -1) return;
	
	names.removeElementAt(pos);
	values.removeElementAt(pos);
	
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
