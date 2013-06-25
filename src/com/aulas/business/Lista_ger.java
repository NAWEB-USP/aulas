package com.aulas.business;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 06/02/2003 10:17:38 -- *
 * -- Gerador versão alpha                                     *
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
import java.util.Date;

public class Lista_ger {

	static private Vector listaObj; // Campo lista de objetos gerenciados

/* Construtor do objeto - carrega todas as inst�ncias do objeto gerenciado do banco de dados para a mem�ria */

public Lista_ger() throws Exception {
	InicializaGerentes.inicializaGerentes();
}

public synchronized Vector getAllElements() {

	return (Vector) listaObj.clone();

}
public Lista getElement(String cod) throws Exception {

	return getElementByCod (cod);

}
public synchronized Lista getElementByCod (String cod) throws Exception {

	Lista obj;

	// percorre a lista procurando o id
	for (int i = 0; i < this.listaObj.size(); i++) {
		obj = (Lista) this.listaObj.elementAt(i);
		if (cod.equals(obj.getCod())) {
			return obj;
		}
	}

	//throw new Exception ("Lista com código " + cod + " não foi encontrado em mem�ria.");
	return null;
}
public synchronized Vector getElements(Disciplina obj) throws Exception {

	Vector elements = new Vector();
	Lista elem;

	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Lista) this.listaObj.elementAt(i);
		if (elem.getDisc().equals(obj)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;

}

public synchronized void remove(Lista obj) throws Exception {

	// PROCEDIMENTOS PR�-Remoção
	// remove agrupamentos correspondentes
	Agrupamento_ger agrupger = new Agrupamento_ger();
	agrupger.removeByLista(obj);
	
	// Informa turmas
	Turma_ger turmager = new Turma_ger();
	turmager.removeListaTodasTurmas(obj);

	// Remoção
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM Lista WHERE cod="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	dbStmt.close();
	dbCon.close();
	
	// ------- Remove da mem�ria -------
	this.listaObj.removeElement(obj);

	// PROCEDIMENTOS P�S-Remoção


}
public synchronized void removeByDisciplina(Disciplina disc) throws Exception {

	// PROCEDIMENTOS PR�-Remoção


	// Remoção
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM Lista WHERE codDisc="+disc.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	dbStmt.close();
	dbCon.close();

	// ------- Remove da mem�ria -------
	Agrupamento_ger agrupger = new Agrupamento_ger();
	Turma_ger turmager = new Turma_ger();
	Enumeration elems = getElements(disc).elements();
	while (elems.hasMoreElements()) {
		Lista l = (Lista) elems.nextElement();
		turmager.removeListaTodasTurmas(l);
		agrupger.removeByLista(l);
		this.listaObj.removeElement(l);
	}

	// PROCEDIMENTOS P�S-Remoção


}

/* Para inclusão utilize o código identificador o objeto igual a null */
/* Retorna o nome do campo inconsistente ou null para tudo ok */

public synchronized String testaConsistencia(Lista obj, String nome, String enunciado, int numMinAlunosPorGrupo, int numMaxAlunosPorGrupo, boolean seguirOrdemQuestoes, boolean autoCorrection, boolean permitirTestar, boolean ativa, java.util.Date data1, int peso1, java.util.Date data2, int peso2, java.util.Date data3, int peso3, java.util.Date data4, int peso4, java.util.Date data5, int peso5, java.util.Date data6, int peso6, Disciplina disc)  throws Exception {

	boolean inclusao = (obj==null);

	// Testa atributos obrigat�rios
	if (nome == null) return "nome";
	if (nome.equals("")) return "nome";
	if (disc == null) return "disc";

	// Testa atributos exclusivos
	Enumeration listas = getElementsByNome(nome).elements();
	while (listas.hasMoreElements()) {
		Lista l = (Lista) listas.nextElement();
		if (l.getDisc() == disc) {
			if (!inclusao && (l != obj)) { // testa se o elemento repetido nao e' o proprio
				return "nome";
			}
		}
	}

	/* Coloque aqui o código restante do código referente ao teste de consist�ncia */

	return null;

}

// Os vetores listas_* sao vetores com os dados na ordem definida pelo vetor turmas

public void altera(Lista obj, String nome, String enunciado, int numMinAlunosPorGrupo, int numMaxAlunosPorGrupo, boolean seguirOrdemQuestoes, boolean autoCorrection, boolean permitirTestar, boolean ativa, java.util.Date data1, int peso1, java.util.Date data2, int peso2, java.util.Date data3, int peso3, java.util.Date data4, int peso4, java.util.Date data5, int peso5, java.util.Date data6, int peso6, Disciplina disc, Vector turmas, Vector listas_seguirPadrao, Vector listas_ativa, Vector listas_data1, Vector listas_peso1, Vector listas_data2, Vector listas_peso2, Vector listas_data3, Vector listas_peso3, Vector listas_data4, Vector listas_peso4, Vector listas_data5, Vector listas_peso5, Vector listas_data6, Vector listas_peso6)  throws Exception {

	// ------- Testa consist�ncia dos dados -------
	String testeCons = testaConsistencia(obj,nome,enunciado,numMinAlunosPorGrupo,numMaxAlunosPorGrupo,seguirOrdemQuestoes,autoCorrection,permitirTestar,ativa,data1,peso1,data2,peso2,data3,peso3,data4,peso4,data5,peso5,data6,peso6,disc);
	if (testeCons != null) throw new Exception("não foi possível alterar devido ao campo "+testeCons+"");

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	nome = StringConverter.toDataBaseNotation(nome);
	enunciado = StringConverter.toDataBaseNotation(enunciado);
	
	String codDisc = (disc != null) ? disc.getCod() : "0";
	str = "UPDATE Lista SET nome='"+nome+"', enunciado='"+enunciado+"', numMinAlunosPorGrupo="+numMinAlunosPorGrupo+", numMaxAlunosPorGrupo="+numMaxAlunosPorGrupo+", seguirOrdemQuestoes="+(seguirOrdemQuestoes ? "1" : "0")+", autoCorrection="+(autoCorrection ? "1" : "0")+", permitirTestar="+(permitirTestar ? "1" : "0")+", ativa="+(ativa ? "1" : "0")+", data1="+((data1==null)?"null":"'"+data1.getTime()+"'")+", peso1="+peso1+", data2="+((data2==null)?"null":"'"+data2.getTime()+"'")+", peso2="+peso2+", data3="+((data3==null)?"null":"'"+data3.getTime()+"'")+", peso3="+peso3+", data4="+((data4==null)?"null":"'"+data4.getTime()+"'")+", peso4="+peso4+", data5="+((data5==null)?"null":"'"+data5.getTime()+"'")+", peso5="+peso5+", data6="+((data6==null)?"null":"'"+data6.getTime()+"'")+", peso6="+peso6+", codDisc="+codDisc+" WHERE cod="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	// Insere as turmas correspondentes
	for (int j = 0; j < turmas.size(); j++) {
		Turma t = (Turma) turmas.elementAt(j);

		// testa se existe o registro
		str = "SELECT * FROM TurmaLista WHERE codLista="+obj.getCod()+" AND codTurma="+t.getCod();
		BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);
		if (!dbRs.next()) {
			str = "INSERT INTO TurmaLista (codLista,codTurma) VALUES ("+obj.getCod()+","+t.getCod()+")";
			BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
		}
		
		// faz atualizacao
		str = "UPDATE TurmaLista SET ";
		str += "seguirPadrao="+(((Boolean) listas_seguirPadrao.elementAt(j)).booleanValue() ? "1" : "0");
		str += ",ativa="+(((Boolean) listas_ativa.elementAt(j)).booleanValue() ? "1" : "0");
		str += ",data1="+((((java.util.Date) listas_data1.elementAt(j)) == null) ? "null" : "'"+((java.util.Date) listas_data1.elementAt(j)).getTime()+"'");
		str += ",peso1="+Integer.parseInt((String) listas_peso1.elementAt(j)); 
		str += ",data2="+((((java.util.Date) listas_data2.elementAt(j)) == null) ? "null" : "'"+((java.util.Date) listas_data2.elementAt(j)).getTime()+"'");
		str += ",peso2="+Integer.parseInt((String) listas_peso2.elementAt(j)); 
		str += ",data3="+((((java.util.Date) listas_data3.elementAt(j)) == null) ? "null" : "'"+((java.util.Date) listas_data3.elementAt(j)).getTime()+"'");
		str += ",peso3="+Integer.parseInt((String) listas_peso3.elementAt(j)); 
		str += ",data4="+((((java.util.Date) listas_data4.elementAt(j)) == null) ? "null" : "'"+((java.util.Date) listas_data4.elementAt(j)).getTime()+"'");
		str += ",peso4="+Integer.parseInt((String) listas_peso4.elementAt(j)); 
		str += ",data5="+((((java.util.Date) listas_data5.elementAt(j)) == null) ? "null" : "'"+((java.util.Date) listas_data5.elementAt(j)).getTime()+"'");
		str += ",peso5="+Integer.parseInt((String) listas_peso5.elementAt(j)); 
		str += ",data6="+((((java.util.Date) listas_data6.elementAt(j)) == null) ? "null" : "'"+((java.util.Date) listas_data6.elementAt(j)).getTime()+"'");
		str += ",peso6="+Integer.parseInt((String) listas_peso6.elementAt(j)); 
		str += " WHERE codLista="+obj.getCod()+" AND codTurma="+t.getCod();
		
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	}

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	if (((obj.getNome() == null) && (nome != null)) || ((obj.getNome() != null) && !obj.getNome().equals(nome))) {
		obj.setNome(nome);
	}
	if (((obj.getEnunciado() == null) && (enunciado != null)) || ((obj.getEnunciado() != null) && !obj.getEnunciado().equals(enunciado))) {
		obj.setEnunciado(enunciado);
	}
	if (obj.getNumMinAlunosPorGrupo() != numMinAlunosPorGrupo) {
		obj.setNumMinAlunosPorGrupo(numMinAlunosPorGrupo);
	}
	if (obj.getNumMaxAlunosPorGrupo() != numMaxAlunosPorGrupo) {
		obj.setNumMaxAlunosPorGrupo(numMaxAlunosPorGrupo);
	}
	if (obj.isSeguirOrdemQuestoes() != seguirOrdemQuestoes) {
		obj.setSeguirOrdemQuestoes(seguirOrdemQuestoes);
	}
	if (obj.isAutoCorrection() != autoCorrection) {
		obj.setAutoCorrection(autoCorrection);
	}
	if (obj.isPermitirTestar() != permitirTestar) {
		obj.setPermitirTestar(permitirTestar);
	}
	if (obj.isAtiva() != ativa) {
		obj.setAtiva(ativa);
	}
	if (((obj.getData1() == null) && (data1 != null)) || ((obj.getData1() != null) && !obj.getData1().equals(data1))) {
		obj.setData1(data1);
	}
	if (obj.getPeso1() != peso1) {
		obj.setPeso1(peso1);
	}
	if (((obj.getData2() == null) && (data2 != null)) || ((obj.getData2() != null) && !obj.getData2().equals(data2))) {
		obj.setData2(data2);
	}
	if (obj.getPeso2() != peso2) {
		obj.setPeso2(peso2);
	}
	if (((obj.getData3() == null) && (data3 != null)) || ((obj.getData3() != null) && !obj.getData3().equals(data3))) {
		obj.setData3(data3);
	}
	if (obj.getPeso3() != peso3) {
		obj.setPeso3(peso3);
	}
	if (((obj.getData4() == null) && (data4 != null)) || ((obj.getData4() != null) && !obj.getData4().equals(data4))) {
		obj.setData4(data4);
	}
	if (obj.getPeso4() != peso4) {
		obj.setPeso4(peso4);
	}
	if (((obj.getData5() == null) && (data5 != null)) || ((obj.getData5() != null) && !obj.getData5().equals(data5))) {
		obj.setData5(data5);
	}
	if (obj.getPeso5() != peso5) {
		obj.setPeso5(peso5);
	}
	if (((obj.getData6() == null) && (data6 != null)) || ((obj.getData6() != null) && !obj.getData6().equals(data6))) {
		obj.setData6(data6);
	}
	if (obj.getPeso6() != peso6) {
		obj.setPeso6(peso6);
	}
	if (((obj.getDisc() == null) && (disc != null)) || ((obj.getDisc() != null) && !obj.getDisc().equals(disc))) {
		obj.setDisc(disc);
	}

	// Dados das turmas
	for (int i = 0; i < turmas.size(); i++) {
		Turma t = (Turma) turmas.elementAt(i);
		t.setLista(obj,((Boolean) listas_seguirPadrao.elementAt(i)).booleanValue(),((Boolean) listas_ativa.elementAt(i)).booleanValue(),(java.util.Date) listas_data1.elementAt(i),(String) listas_peso1.elementAt(i),(java.util.Date) listas_data2.elementAt(i),(String) listas_peso2.elementAt(i),(java.util.Date) listas_data3.elementAt(i),(String) listas_peso3.elementAt(i),(java.util.Date) listas_data4.elementAt(i),(String) listas_peso4.elementAt(i),(java.util.Date) listas_data5.elementAt(i),(String) listas_peso5.elementAt(i),(java.util.Date) listas_data6.elementAt(i),(String) listas_peso6.elementAt(i));
	}


}

public synchronized Vector getElementsByNome(String valor) throws Exception {

	Vector elements = new Vector();
	Lista elem;

	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Lista) this.listaObj.elementAt(i);
		if (elem.getNome().equals(valor)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;

}

// Os vetores listas_* sao vetores com os dados na ordem definida pelo vetor turmas

public synchronized Lista inclui(String nome, String enunciado, int numMinAlunosPorGrupo, int numMaxAlunosPorGrupo, boolean seguirOrdemQuestoes, boolean autoCorrection, boolean permitirTestar, boolean ativa, java.util.Date data1, int peso1, java.util.Date data2, int peso2, java.util.Date data3, int peso3, java.util.Date data4, int peso4, java.util.Date data5, int peso5, java.util.Date data6, int peso6, Disciplina disc, Vector turmas, Vector listas_seguirPadrao, Vector listas_ativa, Vector listas_data1, Vector listas_peso1, Vector listas_data2, Vector listas_peso2, Vector listas_data3, Vector listas_peso3, Vector listas_data4, Vector listas_peso4, Vector listas_data5, Vector listas_peso5, Vector listas_data6, Vector listas_peso6)  throws Exception {

	// ------- Testa consist�ncia dos dados -------
	String testeCons = testaConsistencia(null, nome, enunciado, numMinAlunosPorGrupo, numMaxAlunosPorGrupo, seguirOrdemQuestoes, autoCorrection, permitirTestar, ativa, data1, peso1, data2, peso2, data3, peso3, data4, peso4, data5, peso5, data6, peso6, disc);
	if (testeCons != null) throw new Exception("não foi possível inserir devido ao campo "+testeCons+"");

	// instancia gerente de turmas antes, para evitar problema
	Turma_ger turmager = new Turma_ger();
	
	// ------- Insere na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	// Pega Id maximo
	long maxId = 1;
	str = "SELECT Max(cod) AS maxId From Lista";
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
	String idDisciplina = (disc != null) ? disc.getCod() : "0";
	str = "INSERT INTO Lista (cod, nome, enunciado, numMinAlunosPorGrupo, numMaxAlunosPorGrupo, seguirOrdemQuestoes, autoCorrection, permitirTestar, ativa, data1, peso1, data2, peso2, data3, peso3, data4, peso4, data5, peso5, data6, peso6, codDisc)";
	str += " VALUES ("+id+",'"+nome+"'"+",'"+enunciado+"'"+","+numMinAlunosPorGrupo+","+numMaxAlunosPorGrupo+(seguirOrdemQuestoes ? ",1" : ",0")+(autoCorrection ? ",1" : ",0")+(permitirTestar? ",1" : ",0")+(ativa ? ",1" : ",0")+","+((data1==null)?"null":"'"+data1.getTime()+"'")+","+peso1+","+((data2==null)?"null":"'"+data2.getTime()+"'")+","+peso2+","+((data3==null)?"null":"'"+data3.getTime()+"'")+","+peso3+","+((data4==null)?"null":"'"+data4.getTime()+"'")+","+peso4+","+((data5==null)?"null":"'"+data5.getTime()+"'")+","+peso5+","+((data6==null)?"null":"'"+data6.getTime()+"'")+","+peso6+","+((disc==null)?"null":disc.getCod())+")";
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	// Insere as turmas correspondentes
	for (int j = 0; j < turmas.size(); j++) {
		Turma t = (Turma) turmas.elementAt(j);
		str = "INSERT INTO TurmaLista (codLista, codTurma, seguirPadrao, ativa, data1, peso1, data2, peso2, data3, peso3, data4, peso4, data5, peso5, data6, peso6)";
		str += " VALUES ("+id+","+t.getCod();
		str += ((Boolean) listas_seguirPadrao.elementAt(j)).booleanValue() ? ",1" : ",0";
		str += ((Boolean) listas_ativa.elementAt(j)).booleanValue() ? ",1" : ",0";
		str += (((java.util.Date) listas_data1.elementAt(j)) == null) ? ",null" : ",'"+((java.util.Date) listas_data1.elementAt(j)).getTime()+"'";
		str += ","+Integer.parseInt((String) listas_peso1.elementAt(j)); 
		str += (((java.util.Date) listas_data2.elementAt(j)) == null) ? ",null" : ",'"+((java.util.Date) listas_data2.elementAt(j)).getTime()+"'";
		str += ","+Integer.parseInt((String) listas_peso2.elementAt(j)); 
		str += (((java.util.Date) listas_data3.elementAt(j)) == null) ? ",null" : ",'"+((java.util.Date) listas_data3.elementAt(j)).getTime()+"'";
		str += ","+Integer.parseInt((String) listas_peso3.elementAt(j)); 
		str += (((java.util.Date) listas_data4.elementAt(j)) == null) ? ",null" : ",'"+((java.util.Date) listas_data4.elementAt(j)).getTime()+"'";
		str += ","+Integer.parseInt((String) listas_peso4.elementAt(j)); 
		str += (((java.util.Date) listas_data5.elementAt(j)) == null) ? ",null" : ",'"+((java.util.Date) listas_data5.elementAt(j)).getTime()+"'";
		str += ","+Integer.parseInt((String) listas_peso5.elementAt(j)); 
		str += (((java.util.Date) listas_data6.elementAt(j)) == null) ? ",null" : ",'"+((java.util.Date) listas_data6.elementAt(j)).getTime()+"'";
		str += ","+Integer.parseInt((String) listas_peso6.elementAt(j)); 
		str += ")";
		
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	}

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// ------- Insere na mem�ria -------
	// Cria um novo objeto
	Lista obj = new Lista(id,nome,enunciado,numMinAlunosPorGrupo,numMaxAlunosPorGrupo,seguirOrdemQuestoes,autoCorrection,permitirTestar,ativa,data1,peso1,data2,peso2,data3,peso3,data4,peso4,data5,peso5,data6,peso6,disc);

	for (int i = 0; i < turmas.size(); i++) {
		Turma t = (Turma) turmas.elementAt(i);
		t.addLista(obj,((Boolean) listas_seguirPadrao.elementAt(i)).booleanValue(),((Boolean) listas_ativa.elementAt(i)).booleanValue(),(java.util.Date) listas_data1.elementAt(i),(String) listas_peso1.elementAt(i),(java.util.Date) listas_data2.elementAt(i),(String) listas_peso2.elementAt(i),(java.util.Date) listas_data3.elementAt(i),(String) listas_peso3.elementAt(i),(java.util.Date) listas_data4.elementAt(i),(String) listas_peso4.elementAt(i),(java.util.Date) listas_data5.elementAt(i),(String) listas_peso5.elementAt(i),(java.util.Date) listas_data6.elementAt(i),(String) listas_peso6.elementAt(i));
	}

	// Insere o objeto na lista do gerente
	this.listaObj.addElement(obj);
	
	// toda lista tem que ter pelo menos um agrupamento
	Agrupamento_ger agrupger = new Agrupamento_ger();
	agrupger.inclui(seguirOrdemQuestoes,false,false,"Agrupamento padrão","",0,obj);

	return obj;

}

protected synchronized static void inicializa() throws Exception {
	// Precisa do Disciplina_Ger

	if (listaObj == null) { // primeira utilização do gerente de objetos
		
		listaObj = new Vector();

		// Inicia a conexão com a base de dados
		Connection dbCon = BancoDados.abreConexao();
		Statement dbStmt = dbCon.createStatement();
		ResultSet dbRs;

		Disciplina_ger gerDisciplina = new Disciplina_ger();
		
		// seleciona todos objetos
		String str = "SELECT * FROM Lista ORDER BY Nome";
		BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);

		while (dbRs.next()) {
			// Le dados da base
			String cod = dbRs.getString("cod");
			String nome = dbRs.getString("nome");
			String enunciado = dbRs.getString("enunciado");
			int numMinAlunosPorGrupo = dbRs.getInt("numMinAlunosPorGrupo");
			int numMaxAlunosPorGrupo = dbRs.getInt("numMaxAlunosPorGrupo");
			boolean seguirOrdemQuestoes = dbRs.getBoolean("seguirOrdemQuestoes");
			boolean autoCorrection = dbRs.getBoolean("autoCorrection");
			boolean permitirTestar = dbRs.getBoolean("permitirTestar");
			boolean ativa = dbRs.getBoolean("ativa");
			java.util.Date data1 = trataData(dbRs.getString("data1"));
			int peso1 = dbRs.getInt("peso1");
            java.util.Date data2 = trataData(dbRs.getString("data2"));
			int peso2 = dbRs.getInt("peso2");
            java.util.Date data3 = trataData(dbRs.getString("data3"));
			int peso3 = dbRs.getInt("peso3");
            java.util.Date data4 = trataData(dbRs.getString("data4"));
			int peso4 = dbRs.getInt("peso4");
            java.util.Date data5 = trataData(dbRs.getString("data5"));
			int peso5 = dbRs.getInt("peso5");
            java.util.Date data6 = trataData(dbRs.getString("data6"));
			int peso6 = dbRs.getInt("peso6");
			String codDisc = dbRs.getString("codDisc");
			Disciplina disc = null;
			if (codDisc != null) {
				disc = gerDisciplina.getElement(codDisc);
			} 

			// Instancia o objeto
			Lista obj = new Lista (cod,nome,enunciado,numMinAlunosPorGrupo,numMaxAlunosPorGrupo,seguirOrdemQuestoes,autoCorrection,permitirTestar,ativa,data1,peso1,data2,peso2,data3,peso3,data4,peso4,data5,peso5,data6,peso6,disc);

			// Coloca-o na lista de objetos
			listaObj.addElement(obj);
		}

		listaObj.trimToSize();

		// Finaliza conexao
		dbStmt.close();
		dbCon.close();

	}

}

    private static Date trataData(String data) throws SQLException {
        if (data == null || data.equals("")) return null;
        return new java.util.Date(Long.parseLong(data));
    }

}