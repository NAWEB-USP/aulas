package com.aulas.util.oldSessionmanagerEmMemoria;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 10/02/2003 22:39:37 -- *
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

import java.sql.*;
import java.util.*;

class SessionItem_ger {

	static private Vector listaObj; // Campo lista de objetos gerenciados

/* Construtor do objeto - carrega todas as inst�ncias do objeto gerenciado do banco de dados para a mem�ria */

public SessionItem_ger() throws Exception {

	if (listaObj == null) { // primeira utilização do gerente de objetos

		listaObj = new Vector();

		// Inicia a conexão com a base de dados
		Connection dbCon = BancoDados.abreConexao();
		Statement dbStmt = dbCon.createStatement();
		ResultSet dbRs;

		// seleciona todos objetos
		String str = "SELECT * FROM SessionItem";
		dbRs = dbStmt.executeQuery(str);

		while (dbRs.next()) {
			// Le dados da base
			String id = dbRs.getString("id");
			String idSessao = dbRs.getString("idSessao");
			String campo = dbRs.getString("campo");
			String valor = dbRs.getString("valor");

			// Instancia o objeto
			SessionItem obj = new SessionItem (id,idSessao,campo,valor);

			// Coloca-o na lista de objetos
			listaObj.addElement(obj);
		}

		listaObj.trimToSize();

		// Finaliza conexao
		dbStmt.close();
		dbCon.close();

	}

}
public void altera(SessionItem obj, String idSessao, String campo, String valor)  throws Exception {

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "UPDATE SessionItem SET idSessao='"+idSessao+"', campo='"+campo+"', valor='"+valor+"' WHERE id="+obj.getId();
	dbStmt.executeUpdate(str);

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	if (((obj.getIdSessao() == null) && (idSessao != null)) || ((obj.getIdSessao() != null) && !obj.getIdSessao().equals(idSessao))) {
		obj.setIdSessao(idSessao);
	}
	if (((obj.getCampo() == null) && (campo != null)) || ((obj.getCampo() != null) && !obj.getCampo().equals(campo))) {
		obj.setCampo(campo);
	}
	if (((obj.getValor() == null) && (valor != null)) || ((obj.getValor() != null) && !obj.getValor().equals(valor))) {
		obj.setValor(valor);
	}

}
public Vector getAllElements() {

	return listaObj;

}
public SessionItem getElementById (String id) throws Exception {

	SessionItem obj;

	// percorre a lista procurando o id
	for (int i = 0; i < this.listaObj.size(); i++) {
		obj = (SessionItem) this.listaObj.elementAt(i);
		if (id.equals(obj.getId())) {
			return obj;
		}
	}

	//throw new Exception ("SessionItem com código " + cod + " não foi encontrado em mem�ria.");
	return null;
}
public Vector getElementsByIdSessao(String valor) throws Exception {

	Vector elements = new Vector();
	SessionItem elem;
	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (SessionItem) this.listaObj.elementAt(i);
		if (elem.getIdSessao().equals(valor)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;

}
public SessionItem inclui(String idSessao, String campo, String valor)  throws Exception {

	// ------- Insere na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	// Pega Id maximo
	long maxId = 1;
	str = "SELECT Max(id) AS maxId From SessionItem";
	dbRs = dbStmt.executeQuery(str);
	if (dbRs.next()) {
		maxId = dbRs.getLong("maxId");
		maxId++;
	}
	String id = Long.toString(maxId);

	// Insere o elemento na base de dados
	str = "INSERT INTO SessionItem (id, idSessao, campo, valor)";
	str += " VALUES ("+id+",'"+idSessao+"'"+",'"+campo+"'"+",'"+valor+"'"+")";

	dbStmt.executeUpdate(str);

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// ------- Insere na mem�ria -------
	// Cria um novo objeto
	SessionItem obj = new SessionItem(id,idSessao,campo,valor);

	// Insere o objeto na lista do gerente
	this.listaObj.addElement(obj);

	return obj;

}
public void remove(SessionItem obj) throws Exception {

	// PROCEDIMENTOS PR�-Remoção


	// Remoção
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM SessionItem WHERE id="+obj.getId();
	dbStmt.executeUpdate(str);

	dbStmt.close();
	dbCon.close();

	// ------- Remove da mem�ria -------
	this.listaObj.removeElement(obj);

	// PROCEDIMENTOS P�S-Remoção


}
public void removeElementsByIdSessao(String idSessao) throws Exception {

	// Remoção
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM SessionItem WHERE idSessao='"+idSessao+"'";
	dbStmt.executeUpdate(str);

	dbStmt.close();
	dbCon.close();

	// ------- Remove da mem�ria -------
	Enumeration elems = getElementsByIdSessao(idSessao).elements();
	while (elems.hasMoreElements()) {
		SessionItem s = (SessionItem) elems.nextElement();
		this.listaObj.removeElement(s);
	}

}
}
