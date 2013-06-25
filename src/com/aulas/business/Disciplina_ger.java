package com.aulas.business;
/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 04/02/2003 10:14:00 -- *
 * -- Gerador versão alpha                                       *
 * ----------------------------------------------------------- *

 Histórico de Versões
 --------------------
 versão	Autor		Modificação
 ------	-----		-----------
 1.0		Gerosa 	Geração automática da classe
 * 
 */


import com.aulas.modelos.*;
import com.aulas.util.*;

import java.sql.*;
import java.util.*;

public class Disciplina_ger {

	static private Vector listaObj; // Campo lista de objetos gerenciados

/* Construtor do objeto - carrega todas as inst�ncias do objeto gerenciado do banco de dados para a mem�ria */

public Disciplina_ger() throws Exception {
	InicializaGerentes.inicializaGerentes();
}

public synchronized Vector getAllElements() {

	return (Vector) listaObj.clone();

}

public synchronized Disciplina getElement(String cod) throws Exception {

	return getElementByCod (cod);

}

public synchronized Disciplina getElementByCod (String cod) throws Exception {

	Disciplina obj;

	// percorre a lista procurando o id
	for (int i = 0; i < this.listaObj.size(); i++) {
		obj = (Disciplina) this.listaObj.elementAt(i);
		if (cod.equals(obj.getCod())) {
			return obj;
		}
	}

	//throw new Exception ("Disciplina com código " + cod + " não foi encontrado em mem�ria.");
	return null;
}

public synchronized Disciplina getElementByNome(String valor) throws Exception {

	Disciplina elem;
	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Disciplina) this.listaObj.elementAt(i);
		if (elem.getNome().equals(valor)) {
			return elem;
		}
	}

	return null;

}



public synchronized void remove(Disciplina obj) throws Exception {

	// PROCEDIMENTOS PR�-Remoção
	// remove listas correspondentes
	Lista_ger listager = new Lista_ger();
	listager.removeByDisciplina(obj);
	
	// remove turmas correspondentes
	Turma_ger turmager = new Turma_ger();
	turmager.removeByDisciplina(obj);

	// Remoção
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM Disciplina WHERE cod="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	dbStmt.close();
	dbCon.close();

	// ------- Remove da mem�ria -------
	this.listaObj.removeElement(obj);

	// PROCEDIMENTOS P�S-Remoção


}

public void altera(Disciplina obj, String nome, String descricao)  throws Exception {

	// ------- Testa consist�ncia dos dados -------
	String testeCons = testaConsistencia(obj,nome,descricao);
	if (testeCons != null) throw new Exception("não foi possível alterar devido ao campo "+testeCons+"");

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	nome = StringConverter.toDataBaseNotation(nome);

	str = "UPDATE Disciplina SET nome='"+nome+"', descricao='"+StringConverter.toDataBaseNotation(descricao)+"' WHERE cod="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	if (!obj.getNome().equals(nome)) {
		obj.setNome(nome);
	}

	if (!obj.getDescricao().equals(descricao)) {
		obj.setDescricao(descricao);
	}

}

public synchronized Disciplina inclui(String nome, String descricao)  throws Exception {

	// ------- Testa consist�ncia dos dados -------
	String testeCons = testaConsistencia(null,nome,descricao);
	if (testeCons != null) throw new Exception("não foi possível inserir devido ao campo "+testeCons+"");

	// ------- Insere na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	// Pega Id maximo
	long maxId = 1;
	str = "SELECT Max(cod) AS maxId From Disciplina";
	BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);
	if (dbRs.next()) {
		maxId = dbRs.getLong("maxId");
		maxId++;
	}
	String id = Long.toString(maxId);

	nome = StringConverter.toDataBaseNotation(nome);

	// Insere o elemento na base de dados
	str = "INSERT INTO Disciplina (cod, nome, descricao, desativada)";
	str += " VALUES ("+id+",'"+nome+"'"+",'"+StringConverter.toDataBaseNotation(descricao)+"',0)";

	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// ------- Insere na mem�ria -------
	// Cria um novo objeto
	Disciplina obj = new Disciplina(id,nome,descricao,false);

	// Insere o objeto na lista do gerente
	this.listaObj.addElement(obj);

	// ---- Cria uma nova turma ----
	Turma_ger turmager = new Turma_ger();
	turmager.inclui("Turma 1","",obj);

	return obj;

}

/* Verifica a consist�ncia dos dados antes de uma inclusão ou Alteração */
/* Para inclusão utilize o código identificador o objeto igual a null */
/* Retorna o nome do campo inconsistente ou null para tudo ok */

public synchronized String testaConsistencia(Disciplina obj, String nome, String descricao)  throws Exception {

	boolean inclusao = (obj==null);

	// Testa atributos obrigat�rios
	if (nome == null) return "nome";
	if (nome.equals("")) return "nome";

	// Testa atributos exclusivos
	if (inclusao || !(obj.getNome().equals(nome))) {
		if (getElementByNome(nome) != null) return "nome";
	}

	/* Coloque aqui o código restante do código referente ao teste de consist�ncia */

	return null;

}

public synchronized Vector getDisciplinasAtivas() throws Exception {

	Vector resp = new Vector();
	Disciplina elem;
	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Disciplina) this.listaObj.elementAt(i);
		if (!elem.isDesativada()) {
			resp.addElement(elem);
		}
	}

	return resp;

}

protected synchronized static void inicializa() throws Exception {

	if (listaObj == null) { // primeira utilização do gerente de objetos
		
		listaObj = new Vector();

		// Inicia a conexão com a base de dados
		Connection dbCon = BancoDados.abreConexao();
		Statement dbStmt = dbCon.createStatement();
		ResultSet dbRs;

		// seleciona todos objetos
		String str = "SELECT * FROM Disciplina ORDER BY Nome";
		BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);

		while (dbRs.next()) {
			// Le dados da base
			String cod = dbRs.getString("cod");
			String nome = dbRs.getString("nome");
			String descricao = StringConverter.fromDataBaseNotation(dbRs.getString("descricao"));
			boolean desativada = dbRs.getBoolean("desativada");

			// Instancia o objeto
			Disciplina obj = new Disciplina (cod,nome,descricao,desativada);

			// Coloca-o na lista de objetos
			listaObj.addElement(obj);
		}

		listaObj.trimToSize();

		// Finaliza conexao
		dbStmt.close();
		dbCon.close();

	}

}
}

