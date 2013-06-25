package com.aulas.business;
/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 11/02/2003 09:00:18 -- *
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
import java.util.Date;

public class Turma_ger {

	static private Vector listaObj; // Campo lista de objetos gerenciados

/* Construtor do objeto - carrega todas as inst�ncias do objeto gerenciado do banco de dados para a mem�ria */

public Turma_ger() throws Exception {
	InicializaGerentes.inicializaGerentes();
}

public synchronized Vector getAllElements() {

	return (Vector) listaObj.clone();

}

public Turma getElement(String cod) throws Exception {

	return getElementByCod (cod);

}

public synchronized Turma getElementByCod (String cod) throws Exception {

	Turma obj;

	// percorre a lista procurando o id
	for (int i = 0; i < this.listaObj.size(); i++) {
		obj = (Turma) this.listaObj.elementAt(i);
		if (cod.equals(obj.getCod())) {
			return obj;
		}
	}

	//throw new Exception ("Turma com código " + cod + " não foi encontrado em mem�ria.");
	return null;
}


public synchronized Vector getElementsByNome(String valor) throws Exception {

	Vector elements = new Vector();
	Turma elem;
	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Turma) this.listaObj.elementAt(i);
		if (elem.getNome().equals(valor)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;

}

public synchronized Vector getElements(Disciplina obj) throws Exception {

	Vector elements = new Vector();
	Turma elem;

	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Turma) this.listaObj.elementAt(i);
		if (elem.getDisc().equals(obj)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;

}

public synchronized void remove(Turma obj) throws Exception {

	// PROCEDIMENTOS PR�-Remoção
	// Informa alunos
	Aluno_ger alunoger = new Aluno_ger();
	alunoger.removeTurmaTodosAlunos(obj);


	// Remoção
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM AlunoTurma WHERE codTurma="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	str = "DELETE FROM TurmaLista WHERE codTurma="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	
	str = "DELETE FROM Turma WHERE cod="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	dbStmt.close();
	dbCon.close();

	// ------- Remove da mem�ria -------
	this.listaObj.removeElement(obj);

	// PROCEDIMENTOS P�S-Remoção


}

public void altera(Turma obj, String nome, String descricao)  throws Exception {

	// ------- Testa consist�ncia dos dados -------
	String testeCons = testaConsistencia(obj,nome,descricao,obj.getDisc());
	if (testeCons != null) throw new Exception("não foi possível alterar devido ao campo "+testeCons+"");

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	nome = StringConverter.toDataBaseNotation(nome);

	str = "UPDATE Turma SET nome='"+nome+"', descricao='"+StringConverter.toDataBaseNotation(descricao)+"' WHERE cod="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	if (((obj.getNome() == null) && (nome != null)) || ((obj.getNome() != null) && !obj.getNome().equals(nome))) {
		obj.setNome(nome);
	}

	if (((obj.getDescricao() == null) && (descricao != null)) || ((obj.getDescricao() != null) && !obj.getDescricao().equals(descricao))) {
		obj.setDescricao(descricao);
	}

}

public synchronized Turma inclui(String nome, String descricao, Disciplina disc)  throws Exception {

	// ------- Testa consist�ncia dos dados -------
	String testeCons = testaConsistencia(null,nome,descricao,disc);
	if (testeCons != null) throw new Exception("não foi possível inserir devido ao campo "+testeCons+"");

	// ------- Insere na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	Statement dbStmt2 = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	// Pega Id maximo
	long maxId = 1;
	str = "SELECT Max(cod) AS maxId From Turma";
	BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);
	if (dbRs.next()) {
		maxId = dbRs.getLong("maxId");
		maxId++;
	}
	String id = Long.toString(maxId);

	nome = StringConverter.toDataBaseNotation(nome);

	// Insere o elemento na base de dados
	String idDisciplina = (disc != null) ? disc.getCod() : "0";
	str = "INSERT INTO Turma (cod, nome, descricao, codDisc)";
	str += " VALUES ("+id+",'"+nome+"'"+",'"+StringConverter.toDataBaseNotation(descricao)+"'"+","+((disc==null)?"null":disc.getCod())+")";

	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	// Cria um novo objeto
	Turma obj = new Turma(id,nome,descricao,disc,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
	
	// Le listas de todas turmas
	str = "SELECT * FROM Lista WHERE Lista.CodDisc="+disc.getCod();
	BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);
	Vector listas = new Vector();
	Vector listas_seguirPadrao = new Vector();
	Vector listas_ativa = new Vector();
	Vector listas_data1 = new Vector();
	Vector listas_peso1 = new Vector();
	Vector listas_data2 = new Vector();
	Vector listas_peso2 = new Vector();
	Vector listas_data3 = new Vector();
	Vector listas_peso3 = new Vector();
	Vector listas_data4 = new Vector();
	Vector listas_peso4 = new Vector();
	Vector listas_data5 = new Vector();
	Vector listas_peso5 = new Vector();
	Vector listas_data6 = new Vector();
	Vector listas_peso6 = new Vector();
	Lista_ger listager = new Lista_ger();
	while (dbRs.next()) {
		String codLista = dbRs.getString("cod");
		boolean seguirPadrao = true;
		boolean ativa = dbRs.getBoolean("ativa");
		java.util.Date data1 = (((str = dbRs.getString("data1")) == null) ? null : new java.util.Date(Long.parseLong(str)));
		String peso1 = dbRs.getString("peso1");
		java.util.Date data2 = (((str = dbRs.getString("data2")) == null) ? null : new java.util.Date(Long.parseLong(str)));
		String peso2 = dbRs.getString("peso2");
		java.util.Date data3 = (((str = dbRs.getString("data3")) == null) ? null : new java.util.Date(Long.parseLong(str)));
		String peso3 = dbRs.getString("peso3");
		java.util.Date data4 = (((str = dbRs.getString("data4")) == null) ? null : new java.util.Date(Long.parseLong(str)));
		String peso4 = dbRs.getString("peso4");
		java.util.Date data5 = (((str = dbRs.getString("data5")) == null) ? null : new java.util.Date(Long.parseLong(str)));
		String peso5 = dbRs.getString("peso5");
		java.util.Date data6 = (((str = dbRs.getString("data6")) == null) ? null : new java.util.Date(Long.parseLong(str)));
		String peso6 = dbRs.getString("peso6");
		
		Lista lst = listager.getElementByCod(codLista);
		listas.addElement(lst);
		listas_seguirPadrao.addElement(new Boolean(seguirPadrao));
		listas_ativa.addElement(new Boolean(ativa));
		listas_data1.addElement(data1);
		listas_peso1.addElement(peso1);
		listas_data2.addElement(data2);
		listas_peso2.addElement(peso2);
		listas_data3.addElement(data3);
		listas_peso3.addElement(peso3);
		listas_data4.addElement(data4);
		listas_peso4.addElement(peso4);
		listas_data5.addElement(data5);
		listas_peso5.addElement(peso5);
		listas_data6.addElement(data6);
		listas_peso6.addElement(peso6);

		// Insere na base de dados
		str = "INSERT INTO TurmaLista (codLista, codTurma, SeguirPadrao, Ativa, Data1, Peso1, Data2, Peso2, Data3, Peso3, Data4, Peso4, Data5, Peso5, Data6, Peso6)";
		str += " VALUES ("+codLista+","+id+","+(seguirPadrao?"1":"0")+","+(ativa?"1":"0")+","+((data1==null)?"null":"'"+data1.getTime()+"'")+","+peso1+","+((data2==null)?"null":"'"+data2.getTime()+"'")+","+peso2+","+((data3==null)?"null":"'"+data3.getTime()+"'")+","+peso3+","+((data4==null)?"null":"'"+data4.getTime()+"'")+","+peso4+","+((data5==null)?"null":"'"+data5.getTime()+"'")+","+peso5+","+((data6==null)?"null":"'"+data6.getTime()+"'")+","+peso6+")";

		dbStmt2.executeUpdate(str);
		
	}
	listas.trimToSize();
	listas_seguirPadrao.trimToSize();
	listas_ativa.trimToSize();
	listas_data1.trimToSize();
	listas_peso1.trimToSize();
	listas_data2.trimToSize();
	listas_peso2.trimToSize();
	listas_data3.trimToSize();
	listas_peso3.trimToSize();
	listas_data4.trimToSize();
	listas_peso4.trimToSize();
	listas_data5.trimToSize();
	listas_peso5.trimToSize();
	listas_data6.trimToSize();
	listas_peso6.trimToSize();
	obj.setListas(listas,listas_seguirPadrao,listas_ativa,listas_data1,listas_peso1,listas_data2,listas_peso2,listas_data3,listas_peso3,listas_data4,listas_peso4,listas_data5,listas_peso5,listas_data6,listas_peso6);

	// Finaliza conexao
	dbStmt.close();
	dbStmt2.close();
	dbCon.close();


	// Insere o objeto na lista do gerente
	this.listaObj.addElement(obj);

	return obj;

}

public synchronized void removeByDisciplina(Disciplina disc) throws Exception {

	// PROCEDIMENTOS PR�-Remoção

	// Remoção
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE TurmaLista.* FROM TurmaLista, Turma WHERE TurmaLista.codTurma=Turma.cod AND codDisc="+disc.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	str = "DELETE FROM Turma WHERE codDisc="+disc.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	
	dbStmt.close();
	dbCon.close();

	// ------- Remove da mem�ria -------
	Aluno_ger alunoger = new Aluno_ger();
	Enumeration elems = getElements(disc).elements();
	while (elems.hasMoreElements()) {
		Turma t = (Turma) elems.nextElement();
		alunoger.removeTurmaTodosAlunos(t);
		this.listaObj.removeElement(t);
	}


	// PROCEDIMENTOS P�S-Remoção


}

public synchronized void removeListaTodasTurmas(Lista lista) throws Exception {

	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM TurmaLista WHERE codLista="+lista.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	
	dbStmt.close();
	dbCon.close();
	
	// --- percorre a lista, removendo da memoria ---
	for (int i = 0; i < this.listaObj.size(); i++) {
		Turma obj = (Turma) this.listaObj.elementAt(i);
		obj.removeLista(lista);
	}

}

/* Verifica a consist�ncia dos dados antes de uma inclusão ou Alteração */
/* Para inclusão utilize o código identificador o objeto igual a null */
/* Retorna o nome do campo inconsistente ou null para tudo ok */

public synchronized String testaConsistencia(Turma obj, String nome, String descricao, Disciplina disc)  throws Exception {

	boolean inclusao = (obj==null);

	// Testa atributos obrigat�rios
	if (nome == null) return "nome";
	if (nome.equals("")) return "nome";

	Enumeration turmas = getElementsByNome(nome).elements();
	while (turmas.hasMoreElements()) {
		Turma t = (Turma) turmas.nextElement();
		if (t.getDisc() == disc) {
			if (!inclusao && (t != obj)) { // testa se o elemento repetido nao e' o proprio
				return "nome";
			}
		}
	}
	
	// Testa atributos exclusivos

	/* Coloque aqui o código restante do código referente ao teste de consist�ncia */

	return null;

}

protected synchronized static void inicializa() throws Exception {
	// Precisa do Disciplina_ger e do Lista_ger

	if (listaObj == null) { // primeira utilização do gerente de objetos

		listaObj = new Vector();

		// Inicia a conexão com a base de dados
		Connection dbCon = BancoDados.abreConexao();
		Statement dbStmt = dbCon.createStatement();
		Statement dbStmt2 = dbCon.createStatement();
		ResultSet dbRs;

		// seleciona todos objetos
		String str = "SELECT * FROM Turma";
		BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);

		// Gerentes auxiliares
		Disciplina_ger gerDisciplina = new Disciplina_ger();
		Lista_ger listager = new Lista_ger();

		while (dbRs.next()) {
			// Le dados da base
			String cod = dbRs.getString("cod");
			String nome = dbRs.getString("nome");
			String descricao = StringConverter.fromDataBaseNotation(dbRs.getString("descricao"));
			String codDisc = dbRs.getString("codDisc");
			Disciplina disc = null;
			if (codDisc != null) {
				disc = gerDisciplina.getElement(codDisc);
			} 

			// Instancia o objeto
			Turma obj = new Turma (cod,nome,descricao,disc,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);

			// Le listas desta turma
			str = "SELECT TurmaLista.* FROM TurmaLista, Lista WHERE TurmaLista.CodTurma=" + cod + " AND TurmaLista.CodLista = Lista.Cod ORDER BY Lista.Nome";
			ResultSet dbRs2 = dbStmt2.executeQuery(str);
			Vector listas = new Vector();
			Vector listas_seguirPadrao = new Vector();
			Vector listas_ativa = new Vector();
			Vector listas_data1 = new Vector();
			Vector listas_peso1 = new Vector();
			Vector listas_data2 = new Vector();
			Vector listas_peso2 = new Vector();
			Vector listas_data3 = new Vector();
			Vector listas_peso3 = new Vector();
			Vector listas_data4 = new Vector();
			Vector listas_peso4 = new Vector();
			Vector listas_data5 = new Vector();
			Vector listas_peso5 = new Vector();
			Vector listas_data6 = new Vector();
			Vector listas_peso6 = new Vector();
			while (dbRs2.next()) {
				String codLista = dbRs2.getString("codLista");
				boolean seguirPadrao = dbRs2.getBoolean("SeguirPadrao");
				boolean ativa = dbRs2.getBoolean("ativa");
	            java.util.Date data1 = trataData(dbRs2.getString("data1"));
	            int peso1 = dbRs2.getInt("peso1");
	            java.util.Date data2 = trataData(dbRs2.getString("data2"));
	            int peso2 = dbRs2.getInt("peso2");
	            java.util.Date data3 = trataData(dbRs2.getString("data3"));
	            int peso3 = dbRs2.getInt("peso3");
	            java.util.Date data4 = trataData(dbRs2.getString("data4"));
	            int peso4 = dbRs2.getInt("peso4");
	            java.util.Date data5 = trataData(dbRs2.getString("data5"));
	            int peso5 = dbRs2.getInt("peso5");
	            java.util.Date data6 = trataData(dbRs2.getString("data6"));
	            int peso6 = dbRs2.getInt("peso6");
				
				Lista lst = listager.getElementByCod(codLista);
				listas.addElement(lst);
				listas_seguirPadrao.addElement(new Boolean(seguirPadrao));
				listas_ativa.addElement(new Boolean(ativa));
				listas_data1.addElement(data1);
				listas_peso1.addElement(peso1);
				listas_data2.addElement(data2);
				listas_peso2.addElement(peso2);
				listas_data3.addElement(data3);
				listas_peso3.addElement(peso3);
				listas_data4.addElement(data4);
				listas_peso4.addElement(peso4);
				listas_data5.addElement(data5);
				listas_peso5.addElement(peso5);
				listas_data6.addElement(data6);
				listas_peso6.addElement(peso6);
				
			}
			listas.trimToSize();
			listas_seguirPadrao.trimToSize();
			listas_ativa.trimToSize();
			listas_data1.trimToSize();
			listas_peso1.trimToSize();
			listas_data2.trimToSize();
			listas_peso2.trimToSize();
			listas_data3.trimToSize();
			listas_peso3.trimToSize();
			listas_data4.trimToSize();
			listas_peso4.trimToSize();
			listas_data5.trimToSize();
			listas_peso5.trimToSize();
			listas_data6.trimToSize();
			listas_peso6.trimToSize();
			obj.setListas(listas,listas_seguirPadrao,listas_ativa,listas_data1,listas_peso1,listas_data2,listas_peso2,listas_data3,listas_peso3,listas_data4,listas_peso4,listas_data5,listas_peso5,listas_data6,listas_peso6);
			
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

private static Date trataData(String data) throws SQLException {
    if (data == null || data.equals("")) return null;
    return new java.util.Date(Long.parseLong(data));
}

}
