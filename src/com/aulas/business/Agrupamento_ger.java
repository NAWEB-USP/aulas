package com.aulas.business;
/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 11/02/2003 00:08:21 -- *
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

public class Agrupamento_ger {

	static private Vector listaObj; // Campo lista de objetos gerenciados

/* Construtor do objeto - carrega todas as instancias do objeto gerenciado do banco de dados para a memoria */

public Agrupamento_ger() throws Exception {
	InicializaGerentes.inicializaGerentes();
}

			

public synchronized Vector getAllElements() {

	return (Vector) listaObj.clone();

}

public synchronized Agrupamento getElementByCod (String cod) throws Exception {

	Agrupamento obj;

	// percorre a lista procurando o id
	for (int i = 0; i < this.listaObj.size(); i++) {
		obj = (Agrupamento) this.listaObj.elementAt(i);
		if (cod.equals(obj.getCod())) {
			return obj;
		}
	}

	//throw new Exception ("Agrupamento com código " + cod + " não foi encontrado em memória.");
	return null;
}


public synchronized Vector getElements(Lista obj) throws Exception {

	Vector elements = new Vector();
	Agrupamento elem;

	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Agrupamento) this.listaObj.elementAt(i);
		if (elem.getLista().equals(obj)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;

}

public synchronized void remove(Agrupamento obj) throws Exception {

	// PROCEDIMENTOS PRE-REMOCAO


	// REMOCAO
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM Agrupamento WHERE cod="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	str = "DELETE FROM AgrupamentoQuestao WHERE codAgrupamento="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	dbStmt.close();
	dbCon.close();

	// ------- Remove da memoria -------
	this.listaObj.removeElement(obj);

	// PROCEDIMENTOS POS-REMOCAO


}

public void alteraPesoQuestao(Agrupamento obj, Questao quest, String peso)  throws Exception {

	// ------- Altera na base de dados -------
	// Inicia a conexao com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "UPDATE AgrupamentoQuestao SET peso="+peso+" WHERE codAgrupamento="+obj.getCod()+" AND codQuestao="+quest.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	obj.setPesoQuestao(quest,peso);

}

public synchronized void removeByLista(Lista lista) throws Exception {

	// REMOCAO
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE AgrupamentoQuestao.* FROM AgrupamentoQuestao, Agrupamento WHERE AgrupamentoQuestao.codAgrupamento=Agrupamento.cod AND codLista="+lista.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	str = "DELETE FROM Agrupamento WHERE codLista="+lista.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	
	dbStmt.close();
	dbCon.close();

	// ------- Remove da memoria -------
	Enumeration elems = getElements(lista).elements();
	while (elems.hasMoreElements()) {
		Agrupamento a = (Agrupamento) elems.nextElement();
		this.listaObj.removeElement(a);
	}

}

public synchronized void removeQuestaoTodosAgrups(Questao questao) throws Exception {

	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM AgrupamentoQuestao WHERE codQuestao="+questao.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	
	dbStmt.close();
	dbCon.close();
	
	// --- percorre a lista, removendo da memoria ---
	for (int i = 0; i < this.listaObj.size(); i++) {
		Agrupamento obj = (Agrupamento) this.listaObj.elementAt(i);
		obj.removeQuestao(questao);
	}



}

/* Verifica a consistencia dos dados antes de uma inclusao ou alteracao */
/* Para inclusao utilize o código identificador o objeto igual a null */
/* Retorna o nome do campo inconsistente ou null para tudo ok */

public synchronized String testaConsistencia(Agrupamento obj, boolean seguirOrdem, boolean randomizarSomenteUmaVez, String nome, String enunciado, int numMaxQuestoes, Lista lista)  throws Exception {

	boolean inclusao = (obj==null);

	// Testa atributos obrigatorios

	// Testa atributos exclusivos

	/* Coloque aqui o código restante do código referente ao teste de consistencia */

	return null;

}

public void altera(Agrupamento obj, boolean seguirOrdem, boolean randomizarSomenteUmaVez, boolean distribuicaoUniforme, String nome, String enunciado, int numMaxQuestoes, Lista lista)  throws Exception {

	// ------- Testa consistencia dos dados -------
	String testeCons = testaConsistencia(obj,seguirOrdem,randomizarSomenteUmaVez,nome,enunciado,numMaxQuestoes,lista);
	if (testeCons != null) throw new Exception("não foi possível alterar devido ao campo "+testeCons+"");

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	nome = StringConverter.toDataBaseNotation(nome);
	enunciado = StringConverter.toDataBaseNotation(enunciado);
	
	String codLista = (lista != null) ? lista.getCod() : "0";
    str = "UPDATE Agrupamento SET seguirOrdem="+(seguirOrdem ? "1" : "0")+", randomizarSomenteUmaVez="+(randomizarSomenteUmaVez ? "1" : "0")+", distribuicaoUniforme="+(distribuicaoUniforme ? "1" : "0")+", nome='"+nome+"', enunciado='"+enunciado+"', numMaxQuestoes="+numMaxQuestoes+", codLista="+codLista+" WHERE cod="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	if (obj.isSeguirOrdem() != seguirOrdem) {
		obj.setSeguirOrdem(seguirOrdem);
	}
    if (obj.isRandomizarSomenteUmaVez() != randomizarSomenteUmaVez) {
        obj.setRandomizarSomenteUmaVez(randomizarSomenteUmaVez);
    }
	if (obj.isDistribuicaoUniforme() != distribuicaoUniforme) {
		obj.setDistribuicaoUniforme(distribuicaoUniforme);
	}
	if (((obj.getNome() == null) && (nome != null)) || ((obj.getNome() != null) && !obj.getNome().equals(nome))) {
		obj.setNome(nome);
	}
	if (((obj.getEnunciado() == null) && (enunciado != null)) || ((obj.getEnunciado() != null) && !obj.getEnunciado().equals(enunciado))) {
		obj.setEnunciado(enunciado);
	}
	if (obj.getNumMaxQuestoes() != numMaxQuestoes) {
		obj.setNumMaxQuestoes(numMaxQuestoes);
	}
	if (((obj.getLista() == null) && (lista != null)) || ((obj.getLista() != null) && !obj.getLista().equals(lista))) {
		obj.setLista(lista);
	}

}

public synchronized Agrupamento inclui(boolean seguirOrdem, boolean randomizarSomenteUmaVez, boolean distribuicaoUniforme, String nome, String enunciado, int numMaxQuestoes, Lista lista)  throws Exception {

	// ------- Testa consistencia dos dados -------
	String testeCons = testaConsistencia(null,seguirOrdem, randomizarSomenteUmaVez, nome, enunciado, numMaxQuestoes, lista);
	if (testeCons != null) throw new Exception("não foi possível inserir devido ao campo "+testeCons+"");

	// ------- Insere na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	// Pega Id maximo
	long maxId = 1;
	str = "SELECT Max(cod) AS maxId From Agrupamento";
	BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);
	if (dbRs.next()) {
		maxId = dbRs.getLong("maxId");
		maxId++;
	}
	String id = Long.toString(maxId);

	nome = StringConverter.toDataBaseNotation(nome);
	enunciado = StringConverter.toDataBaseNotation(enunciado);
	
	// Insere o elemento na base de dados
	String idLista = (lista != null) ? lista.getCod() : "0";
	str = "INSERT INTO Agrupamento (cod, seguirOrdem, randomizarSomenteUmaVez, distribuicaoUniforme, nome, enunciado, numMaxQuestoes, codLista)";
	str += " VALUES ("+id+(seguirOrdem ? ",1" : ",0")+(randomizarSomenteUmaVez ? ",1" : ",0")+(distribuicaoUniforme ? ",1" : ",0")+",'"+nome+"'"+",'"+enunciado+"'"+","+numMaxQuestoes+","+((lista==null)?"null":lista.getCod())+")";
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// ------- Insere na memoria -------
	// Cria um novo objeto
	Agrupamento obj = new Agrupamento(id,seguirOrdem,randomizarSomenteUmaVez,distribuicaoUniforme,nome,enunciado,numMaxQuestoes,lista,new Vector(),new Vector());

	// Insere o objeto na lista do gerente
	this.listaObj.addElement(obj);

	return obj;

}

protected synchronized static void inicializa() throws Exception {
	// Precisa do Questao_ger e Lista_ger
	
	if (listaObj == null) { // primeira utilizacao do gerente de objetos
		
		listaObj = new Vector();

		// Inicia a conexão com a base de dados
		Connection dbCon = BancoDados.abreConexao();
		Statement dbStmt = dbCon.createStatement();
		Statement dbStmt2 = dbCon.createStatement();
		ResultSet dbRs;

		// seleciona todos objetos
		String str = "SELECT * FROM Agrupamento";
		BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);

		// Intancia gerenciador de questoes
		Questao_ger quesger = new Questao_ger();
		Lista_ger gerLista = new Lista_ger();
		
		while (dbRs.next()) {
			// Le dados da base
			String cod = dbRs.getString("cod");
            boolean seguirOrdem = dbRs.getBoolean("seguirOrdem");
            boolean randomizarSomenteUmaVez = dbRs.getBoolean("randomizarSomenteUmaVez");
			boolean distribuicaoUniforme = dbRs.getBoolean("distribuicaoUniforme");
			String nome = dbRs.getString("nome");
			String enunciado = dbRs.getString("enunciado");
			int numMaxQuestoes = dbRs.getInt("numMaxQuestoes");
			String codLista = dbRs.getString("codLista");
			Lista lista = null;
			if (codLista != null) {
				lista = gerLista.getElement(codLista);
			}

			// Instancia o objeto
			Agrupamento obj = new Agrupamento (cod,seguirOrdem,randomizarSomenteUmaVez,distribuicaoUniforme,nome,enunciado,numMaxQuestoes,lista,null,null);
			
			// Le questoes deste agrupamento
			str = "SELECT * FROM AgrupamentoQuestao WHERE CodAgrupamento="+cod;
			ResultSet dbRs2 = dbStmt2.executeQuery(str);
			Vector questoes = new Vector();
			Vector pesos = new Vector();
			while (dbRs2.next()) {
				String codQuestao = dbRs2.getString("codQuestao");
				String peso = dbRs2.getString("Peso");
				if (codQuestao == null) throw new Exception("Código de questão nulo para agrupamento "+cod);
				Questao questao = quesger.getElementByCod(codQuestao);
				questoes.addElement(questao);
				pesos.addElement(peso);
			}
			questoes.trimToSize();
			pesos.trimToSize();

			obj.setQuestoes(questoes);
			obj.setPesosQuestoes(pesos);
			
			// Coloca-o na lista de objetos
			listaObj.addElement(obj);
		}

		listaObj.trimToSize();

		// Finaliza conexao
		dbStmt.close();
		dbStmt2.close();
		dbCon.close();
	}

}
}
 
