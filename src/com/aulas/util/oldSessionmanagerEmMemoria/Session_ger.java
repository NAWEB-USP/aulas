package com.aulas.util.oldSessionmanagerEmMemoria;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 10/02/2003 22:17:57 -- *
 * -- Gerador versão 1.0                                       *
 * ----------------------------------------------------------- *

 Histórico de Versões
 --------------------
 versão	Autor		Modificação
 ------	-----		-----------
 1.0	Gerosa 		Geração automática da classe
 * 
 */


import com.aulas.util.*;

import java.sql.*;
import java.util.*;

class Session_ger {

	static private Vector listaObj; // Campo lista de objetos gerenciados

	public final static int TEMPOEXPIRACAO = 1000*60*60*2; // 120 minutos
	
/* Construtor do objeto - carrega todas as inst�ncias do objeto gerenciado do banco de dados para a mem�ria */

public Session_ger() throws Exception {

	if (listaObj == null) { // primeira utilização do gerente de objetos

		listaObj = new Vector();

		// Inicia a conexão com a base de dados
		Connection dbCon = BancoDados.abreConexao();
		Statement dbStmt = dbCon.createStatement();
		ResultSet dbRs;

		// seleciona todos objetos
		String str = "SELECT * FROM Session";
		dbRs = dbStmt.executeQuery(str);

		while (dbRs.next()) {
			// Le dados da base
			String id = dbRs.getString("id");
			java.util.Date dataUltAcesso = (((str = dbRs.getString("dataUltAcesso")) == null) ? null : new java.util.Date(Long.parseLong(str)));

			// Instancia o objeto
			Session obj = new Session (id,dataUltAcesso);

			// Coloca-o na lista de objetos
			listaObj.addElement(obj);
		}

		listaObj.trimToSize();

		// Finaliza conexao
		dbStmt.close();
		dbCon.close();

	}

}
public void altera(Session obj, java.util.Date dataUltAcesso)  throws Exception {

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "UPDATE Session SET dataUltAcesso="+((dataUltAcesso==null)?"null":"'"+dataUltAcesso.getTime()+"'")+" WHERE id='"+obj.getId()+"'";
	dbStmt.executeUpdate(str);

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	obj.setDataUltAcesso(dataUltAcesso);

}
public Vector getAllElements() {

	return listaObj;

}
public Session getElementById (String id) throws Exception {

	Session obj;

	// percorre a lista procurando o id
	for (int i = 0; i < this.listaObj.size(); i++) {
		obj = (Session) this.listaObj.elementAt(i);
		if (id.equals(obj.getId())) {
			return obj;
		}
	}

	//throw new Exception ("Session com código " + cod + " não foi encontrado em mem�ria.");
	return null;
}
public Session inclui(java.util.Date dataUltAcesso)  throws Exception {

	// ------- Insere na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	// Gera Id
	String id;
	do {
		Random r = new Random(); // gerador de numero aleatorio
	 	id = String.valueOf(r.nextInt());
	} while (this.getElementById(id) != null);

	// ------- Insere na mem�ria -------
	// Cria um novo objeto
	Session obj = new Session(id,dataUltAcesso);

	// Insere o objeto na lista do gerente
	this.listaObj.addElement(obj);
	
	// Insere o elemento na base de dados
	str = "INSERT INTO Session (id, dataUltAcesso)";
	str += " VALUES ('"+id+"',"+((dataUltAcesso==null)?"null":"'"+dataUltAcesso.getTime()+"'")+")";

	dbStmt.executeUpdate(str);

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();


	return obj;

}
public void remove(Session obj) throws Exception {

	// PROCEDIMENTOS PR�-Remoção
	SessionItem_ger sessionitemger = new SessionItem_ger();
	sessionitemger.removeElementsByIdSessao(obj.getId());

	// Remoção
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM Session WHERE id='"+obj.getId()+"'";
	dbStmt.executeUpdate(str);

	dbStmt.close();
	dbCon.close();

	// ------- Remove da mem�ria -------
	this.listaObj.removeElement(obj);

	// PROCEDIMENTOS P�S-Remoção


}
public void removeSessoesExpiradas()  throws Exception {

	long agora = (new java.util.Date()).getTime();

	Enumeration sessoes = this.listaObj.elements();
	while (sessoes.hasMoreElements()) {
		Session sessao = (Session) sessoes.nextElement();
		if (agora - sessao.getDataUltAcesso().getTime() > TEMPOEXPIRACAO) { // Testa se a sessao expirou
			this.remove(sessao);
		}
	}

}
}
