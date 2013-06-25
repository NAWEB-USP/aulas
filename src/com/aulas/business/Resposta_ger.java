package com.aulas.business;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 12/02/2002 11:48:56 -- *
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

public class Resposta_ger {

	static private Vector listaObj; // Campo lista de objetos gerenciados

/* Construtor do objeto - carrega todas as inst�ncias do objeto gerenciado do banco de dados para a mem�ria */

public Resposta_ger() throws Exception {
	InicializaGerentes.inicializaGerentes();
}

public void altera(Resposta obj) throws Exception {
	altera(obj, obj.getResposta(), obj.getComentario(), obj.getPontuacao(), obj.getHits(), obj.getQuestao(), obj.isNova());
}

public void altera(Resposta obj, String resposta, String comentario, int pontuacao, int hits, Questao questao, boolean nova)  throws Exception {

	// ------- Testa consist�ncia dos dados -------
	String testeCons = testaConsistencia(obj,resposta,comentario,pontuacao,hits,questao);
	if (testeCons != null) throw new Exception("não foi possível alterar devido ao campo "+testeCons+"");

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	resposta = StringConverter.toDataBaseNotation(resposta);
	comentario = StringConverter.toDataBaseNotation(comentario);
	
	String codQuestao = (questao != null) ? questao.getCod() : "0";
	str = "UPDATE Resposta SET resposta='"+resposta+"', comentario='"+comentario+"', pontuacao="+pontuacao+", hits="+hits+", nova="+(nova ? "1" : "0")+" WHERE cod="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	
	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	boolean precisaRecorrigirListas = false;
	if (((obj.getResposta() == null) && (resposta != null)) || ((obj.getResposta() != null) && !obj.getResposta().equals(resposta))) {
		obj.setResposta(resposta);
	}
	if (((obj.getComentario() == null) && (comentario != null)) || ((obj.getComentario() != null) && !obj.getComentario().equals(comentario))) {
		obj.setComentario(comentario);
	}
	if (obj.getPontuacao() != pontuacao) {
		obj.setPontuacao(pontuacao);
		precisaRecorrigirListas = true;
	}
	if (obj.getHits() != hits) {
		obj.setHits(hits);
	}
	if (((obj.getQuestao() == null) && (questao != null)) || ((obj.getQuestao() != null) && !obj.getQuestao().equals(questao))) {
		obj.setQuestao(questao);
	}
	if (obj.isNova() != nova) {
		obj.setNova(nova);
		precisaRecorrigirListas = true;
	}

	// Recorrige listas
	if (precisaRecorrigirListas) {
		ListaGerada_ger listageradager = new ListaGerada_ger();
		Enumeration listas = listageradager.getElements(questao).elements();
		while (listas.hasMoreElements()) {
			ListaGerada listaGerada = (ListaGerada) listas.nextElement();
			if (listaGerada.getStatus() == ListaGerada.EMCORRECAO) {
				listageradager.corrige(listaGerada);
			}
		}
	}
	
}
public synchronized Vector getAllElements() {

	return (Vector) listaObj.clone();

}
public synchronized Resposta getElementByCod (String cod) throws Exception {

	Resposta obj;

	// percorre a lista procurando o id
	for (int i = 0; i < this.listaObj.size(); i++) {
		obj = (Resposta) this.listaObj.elementAt(i);
		if (cod.equals(obj.getCod())) {
			return obj;
		}
	}

	//throw new Exception ("Resposta com código " + cod + " não foi encontrado em mem�ria.");
	return null;
}
public synchronized Resposta getElementByResposta(Questao quest, String valor) throws Exception {

	Resposta elem;
	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Resposta) this.listaObj.elementAt(i);
		if ((elem.getQuestao().equals(quest)) && quest.comparaDuasRespostas(elem.getResposta(),valor)) {
			return elem;
		}
	}

	return null;

}
public synchronized Vector getElements(Questao obj) throws Exception {

	Vector elements = new Vector();
	Resposta elem;

	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Resposta) this.listaObj.elementAt(i);
		if (elem.getQuestao().equals(obj)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;

}
public int getNumRespostasNovasByDisciplina(Disciplina disc) throws Exception {

	int num = 0;

	Lista_ger listager = new Lista_ger();
	Enumeration listas = listager.getElements(disc).elements();

	while (listas.hasMoreElements()) {
		Lista lista = (Lista) listas.nextElement();
		num += getNumRespostasNovasByLista(lista);
	}

	return num;
	
}
public int getNumRespostasNovasByLista(Lista lista) throws Exception {

	int num = 0;

	Agrupamento_ger agrupamentoger = new Agrupamento_ger();
	Enumeration agrups = agrupamentoger.getElements(lista).elements();

	while (agrups.hasMoreElements()) {
		Agrupamento agrup = (Agrupamento) agrups.nextElement();
		Enumeration questoes = agrup.getQuestoes().elements();
		
		while (questoes.hasMoreElements()) {
			Questao quest = (Questao) questoes.nextElement();
			num += getNumRespostasNovasByQuestao(quest);
		}
	}

	return num;
	
}
public synchronized int getNumRespostasNovasByQuestao(Questao obj) throws Exception {

	int num = 0;
	Resposta elem;

	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Resposta) this.listaObj.elementAt(i);
		if (elem.isNova() && elem.getQuestao().equals(obj)) {
			num++;
		}
	}

	return num;

}
public synchronized Vector getRespostasNovasByQuestao(Questao obj) throws Exception {

	Vector elements = new Vector();
	Resposta elem;

	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Resposta) this.listaObj.elementAt(i);
		if (elem.isNova() && elem.getQuestao().equals(obj)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;

}
public void hitsDecrementa(Resposta obj)  throws Exception {

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "UPDATE Resposta SET Hits=Hits-1 WHERE cod="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	obj.setHits(obj.getHits()-1);

}
public void hitsIncrementa(Resposta obj)  throws Exception {

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "UPDATE Resposta SET Hits=Hits+1 WHERE cod="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	obj.setHits(obj.getHits()+1);

}
public synchronized Resposta inclui(String resposta, String comentario, int pontuacao, int hits, Questao questao, boolean nova)  throws Exception {

	// ------- Testa consist�ncia dos dados -------
	String testeCons = testaConsistencia(null,resposta, comentario, pontuacao, hits, questao);
	if (testeCons != null) throw new Exception("não foi possível inserir devido ao campo "+testeCons+"");

	// ------- Insere na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	// Pega Id maximo
	long maxId = 1;
	str = "SELECT Max(cod) AS maxId From Resposta";
	BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);
	if (dbRs.next()) {
		maxId = dbRs.getLong("maxId");
		maxId++;
	}
	String id = Long.toString(maxId);

	resposta = StringConverter.toDataBaseNotation(resposta);
	comentario = StringConverter.toDataBaseNotation(comentario);

	// Insere o elemento na base de dados
	PreparedStatement pStmt = dbCon.prepareStatement("INSERT INTO Resposta (cod, resposta, comentario, pontuacao, hits, codQuestao, nova) VALUES (?, ?, ?, ?, ?, ?, ?)");
	pStmt.setInt(1, Integer.parseInt(id));
	pStmt.setString(2, resposta);
	pStmt.setString(3, comentario);
	pStmt.setInt(4, pontuacao);
	pStmt.setInt(5, hits);
	pStmt.setString(6, (questao==null)?"null":questao.getCod());
	pStmt.setBoolean(7, nova);
	pStmt.execute();

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// ------- Insere na memoria -------
	// Cria um novo objeto
	Resposta obj = new Resposta(id,resposta,comentario,pontuacao,hits,questao,nova);

	// Insere o objeto na lista do gerente
	this.listaObj.addElement(obj);

	return obj;

}
protected synchronized static void inicializa() throws Exception {
		listaObj = new Vector();

		// Inicia a conexão com a base de dados
		Connection dbCon = BancoDados.abreConexao();
		Statement dbStmt = dbCon.createStatement();
		ResultSet dbRs;

		// seleciona todos objetos
		String str = "SELECT * FROM Resposta WHERE codQuestao IN (SELECT Questao.Cod FROM (Disciplina INNER JOIN Lista ON Disciplina.Cod = Lista.CodDisc) INNER JOIN (Questao INNER JOIN (Agrupamento INNER JOIN AgrupamentoQuestao ON Agrupamento.Cod = AgrupamentoQuestao.codAgrupamento) ON Questao.Cod = AgrupamentoQuestao.codQuestao) ON Lista.Cod = Agrupamento.CodLista WHERE (((Disciplina.Desativada)=0)))";
		BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);

		// gerentes auxiliares
		Questao_ger gerQuestao = new Questao_ger();
		
		while (dbRs.next()) {
			// Le dados da base
			String cod = dbRs.getString("cod");
			String resposta = StringConverter.fromDataBaseNotation(dbRs.getString("resposta"));
			String comentario = StringConverter.fromDataBaseNotation(dbRs.getString("comentario"));
			int pontuacao = dbRs.getInt("pontuacao");
			int hits = dbRs.getInt("hits");
			String codQuestao = dbRs.getString("codQuestao");
			boolean nova = dbRs.getBoolean("nova");
			Questao questao = null;
			if (codQuestao != null) {
				questao = gerQuestao.getElementByCod(codQuestao);
			} 

			// Instancia o objeto
			Resposta obj = new Resposta (cod,resposta,comentario,pontuacao,hits,questao,nova);

			// Coloca-o na lista de objetos
			listaObj.addElement(obj);
		}

		listaObj.trimToSize();

		// Finaliza conexao
		dbStmt.close();
		dbCon.close();

}
public synchronized void preencheVetoresComentarios(Vector comentariosPadroes, Vector pontuacoesComents, Questao questao) throws Exception {

	Resposta elem;

	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Resposta) this.listaObj.elementAt(i);
		if (elem.getQuestao().equals(questao)) {
			if (!elem.isNova() && (elem.getComentario()!=null) && (!elem.getComentario().equals(""))) {
				if (!comentariosPadroes.contains(elem.getComentario())) {
					comentariosPadroes.addElement(elem.getComentario());
					pontuacoesComents.addElement(String.valueOf(elem.getPontuacao()));
				}
			}
		}
	}

	comentariosPadroes.trimToSize();
	pontuacoesComents.trimToSize();

}
public synchronized void remove(Resposta obj) throws Exception {

	// PROCEDIMENTOS PR�-Remoção


	// Remoção
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM Resposta WHERE cod="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	dbStmt.close();
	dbCon.close();

	// ------- Remove da mem�ria -------
	this.listaObj.removeElement(obj);

	// PROCEDIMENTOS P�S-Remoção


}
/* Verifica a consist�ncia dos dados antes de uma inclusão ou Alteração */
/* Para inclusão utilize o código identificador o objeto igual a null */
/* Retorna o nome do campo inconsistente ou null para tudo ok */

public synchronized String testaConsistencia(Resposta obj, String resposta, String comentario, int pontuacao, int hits, Questao questao)  throws Exception {

	boolean inclusao = (obj==null);

	// Testa atributos obrigat�rios
	if (resposta == null) return "resposta";
	//Resposta resp = getElementByResposta(questao,resposta);
	//if ((resp != null) && (resp != obj)) return "resposta";
	if (resposta.equals("")) return "resposta";
	if (questao == null) return "questao";

	// Testa atributos exclusivos

	/* Coloque aqui o código restante do código referente ao teste de consist�ncia */

	return null;

}

}
