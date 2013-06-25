package com.aulas.business;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 11/02/2003 00:48:55 -- *
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
import com.aulas.objinterface.*;
import com.aulas.business.questoes.*;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Questao_ger {

	static private Vector listaObj; // Campo lista de objetos gerenciados

/* Construtor do objeto - carrega todas as inst�ncias do objeto gerenciado do banco de dados para a mem�ria */

public Questao_ger() throws Exception {
	InicializaGerentes.inicializaGerentes();
}
public void altera(Questao obj, String enunciado, String criterios, String tipo)  throws Exception {

	// ------- Testa consist�ncia dos dados -------
	String testeCons = testaConsistencia(obj,enunciado,criterios,tipo);
	if (testeCons != null) throw new Exception("não foi possível alterar devido ao campo "+testeCons+"");

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	criterios = StringConverter.toDataBaseNotation(criterios);
	enunciado = StringConverter.toDataBaseNotation(enunciado);
	
	str = "UPDATE Questao SET enunciado='"+enunciado+"', criterios='"+criterios+"', tipo='"+tipo+"' WHERE cod="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	if (((obj.getEnunciado() == null) && (enunciado != null)) || ((obj.getEnunciado() != null) && !obj.getEnunciado().equals(enunciado))) {
		obj.setEnunciado(enunciado);
	}
	if (((obj.getCriterios() == null) && (criterios != null)) || ((obj.getCriterios() != null) && !obj.getCriterios().equals(criterios))) {
		obj.setCriterios(criterios);
	}

}
public synchronized Vector getAllElements() {

	return (Vector) listaObj.clone();

}
public Vector getDescTipos() throws Exception {

	Vector tipos = new Vector();

	// ********* tipos **********
	tipos.addElement(QuestaoTexto.getDescTipoQuestao());
	tipos.addElement(QuestaoVouF.getDescTipoQuestao());
	tipos.addElement(QuestaoME.getDescTipoQuestao());
	tipos.addElement(QuestaoSubmissaoArquivo.getDescTipoQuestao());
	tipos.addElement(QuestaoSQL.getDescTipoQuestao());
	tipos.addElement(QuestaoJava.getDescTipoQuestao());
	tipos.addElement(QuestaoEscolhaDeTema.getDescTipoQuestao());
	tipos.addElement(QuestaoOutraFormaEntrega.getDescTipoQuestao());
	
	return tipos;

}
public synchronized Questao getElementByCod (String cod) throws Exception {

	Questao obj;

	// percorre a lista procurando o id
	for (int i = 0; i < this.listaObj.size(); i++) {
		obj = (Questao) this.listaObj.elementAt(i);
		if (obj == null) throw new Exception("Questão null na posição "+i+" da lista de tamanho "+this.listaObj.size());
		if (cod.equals(obj.getCod())) {
			return obj;
		}
	}

	//throw new Exception ("Questao com código " + cod + " não foi encontrado em mem�ria.");
	return null;
}
public synchronized Vector getElementsByTipo(String valor) throws Exception {

	Vector elements = new Vector();
	Questao elem;
	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Questao) this.listaObj.elementAt(i);
		if (elem.getTipo().equals(valor)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;

}
public Vector getIdTipos() throws Exception {

	Vector tipos = new Vector();

	// ********* tipos **********
	tipos.addElement(QuestaoTexto.getIdTipoQuestao());
	tipos.addElement(QuestaoVouF.getIdTipoQuestao());
	tipos.addElement(QuestaoME.getIdTipoQuestao());
	tipos.addElement(QuestaoSubmissaoArquivo.getIdTipoQuestao());
	tipos.addElement(QuestaoSQL.getIdTipoQuestao());
	tipos.addElement(QuestaoJava.getIdTipoQuestao());
	tipos.addElement(QuestaoEscolhaDeTema.getIdTipoQuestao());
	tipos.addElement(QuestaoOutraFormaEntrega.getIdTipoQuestao());
	
	return tipos;

}
public void htmlCadastroRespostasPadrao(PrintWriter out, TabelaFormulario tab, Formulario form) throws Exception {
	
	// ****** Cadastro de resposta padroes ********
	out.println("<SCRIPT LANGUAGE=JavaScript>"); // javascript de mudanca
	out.println("function mudaTipoQuestao () {");
	out.println("	eval (\"document.getElementById('"+QuestaoTexto.getIdTipoQuestao()+"').style.visibility='hidden';\");");
	out.println("	eval (\"document.getElementById('"+QuestaoTexto.getIdTipoQuestao()+"').style.display='none';\");");
	out.println("	eval (\"document.getElementById('"+QuestaoVouF.getIdTipoQuestao()+"').style.visibility='hidden';\");");
	out.println("	eval (\"document.getElementById('"+QuestaoVouF.getIdTipoQuestao()+"').style.display='none';\");");
	out.println("	eval (\"document.getElementById('"+QuestaoME.getIdTipoQuestao()+"').style.visibility='hidden';\");");
	out.println("	eval (\"document.getElementById('"+QuestaoME.getIdTipoQuestao()+"').style.display='none';\");");
	out.println("	eval (\"document.getElementById('"+QuestaoSubmissaoArquivo.getIdTipoQuestao()+"').style.visibility='hidden';\");");
	out.println("	eval (\"document.getElementById('"+QuestaoSubmissaoArquivo.getIdTipoQuestao()+"').style.display='none';\");");
	out.println("	eval (\"document.getElementById('"+QuestaoSQL.getIdTipoQuestao()+"').style.visibility='hidden';\");");
	out.println("	eval (\"document.getElementById('"+QuestaoSQL.getIdTipoQuestao()+"').style.display='none';\");");
	out.println("	eval (\"document.getElementById('"+QuestaoJava.getIdTipoQuestao()+"').style.visibility='hidden';\");");
	out.println("	eval (\"document.getElementById('"+QuestaoJava.getIdTipoQuestao()+"').style.display='none';\");");
	out.println("	eval (\"document.getElementById('"+QuestaoEscolhaDeTema.getIdTipoQuestao()+"').style.visibility='hidden';\");");
	out.println("	eval (\"document.getElementById('"+QuestaoEscolhaDeTema.getIdTipoQuestao()+"').style.display='none';\");");
	out.println("	eval (\"document.getElementById('"+QuestaoOutraFormaEntrega.getIdTipoQuestao()+"').style.visibility='hidden';\");");
	out.println("	eval (\"document.getElementById('"+QuestaoOutraFormaEntrega.getIdTipoQuestao()+"').style.display='none';\");");
	out.println("	eval (\"document.getElementById(document.dados.tipo.value).style.visibility='visible';\");");
	out.println("	eval (\"document.getElementById(document.dados.tipo.value).style.display='block';\");");
	out.println("}");
	out.println("</SCRIPT>");
	// ********* div para cada tipo de questao *********
	tab.novaLinha();
	tab.addCelulaWithColSpan("<div id=\""+QuestaoTexto.getIdTipoQuestao()+"\" style=\"visibility=hidden;display=none\">"+QuestaoTexto.htmlFormCadastroNovaQuestao(form)+"</div>",2);
	tab.novaLinha();
	tab.addCelulaWithColSpan("<div id=\""+QuestaoVouF.getIdTipoQuestao()+"\" style=\"visibility=hidden;display=none\">"+QuestaoVouF.htmlFormCadastroNovaQuestao(form)+"</div>",2);
	tab.novaLinha();
	tab.addCelulaWithColSpan("<div id=\""+QuestaoME.getIdTipoQuestao()+"\" style=\"visibility=hidden;display=none\">"+QuestaoME.htmlFormCadastroNovaQuestao(form)+"</div>",2);
	tab.novaLinha();
	tab.addCelulaWithColSpan("<div id=\""+QuestaoSubmissaoArquivo.getIdTipoQuestao()+"\" style=\"visibility=hidden;display=none\">"+QuestaoSubmissaoArquivo.htmlFormCadastroNovaQuestao(form)+"</div>",2);
	tab.novaLinha();
	tab.addCelulaWithColSpan("<div id=\""+QuestaoSQL.getIdTipoQuestao()+"\" style=\"visibility=hidden;display=none\">"+QuestaoSQL.htmlFormCadastroNovaQuestao(form)+"</div>",2);
	tab.novaLinha();
	tab.addCelulaWithColSpan("<div id=\""+QuestaoJava.getIdTipoQuestao()+"\" style=\"visibility=hidden;display=none\">"+QuestaoJava.htmlFormCadastroNovaQuestao(form)+"</div>",2);
	tab.novaLinha();
	tab.addCelulaWithColSpan("<div id=\""+QuestaoEscolhaDeTema.getIdTipoQuestao()+"\" style=\"visibility=hidden;display=none\">"+QuestaoEscolhaDeTema.htmlFormCadastroNovaQuestao(form)+"</div>",2);
	tab.novaLinha();
	tab.addCelulaWithColSpan("<div id=\""+QuestaoOutraFormaEntrega.getIdTipoQuestao()+"\" style=\"visibility=hidden;display=none\">"+QuestaoOutraFormaEntrega.htmlFormCadastroNovaQuestao(form)+"</div>",2);
	tab.novaLinha();
	
	// Coloca o padrao como visivel
	out.println("<SCRIPT LANGUAGE=JavaScript>");
	out.println("mudaTipoQuestao();");
	out.println("</SCRIPT>");

}
public synchronized Questao inclui(javax.servlet.http.HttpServletRequest request, String enunciado, String criterios, String tipo, Agrupamento agrup, String peso)  throws Exception {

	// ------- Testa consist�ncia dos dados -------
	String testeCons = testaConsistencia(null,enunciado, criterios, tipo);
	if (testeCons != null) throw new Exception("não foi possível inserir devido ao campo "+testeCons+"");

	// ------- Insere na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	// Pega Id maximo
	long maxId = 1;
	str = "SELECT Max(cod) AS maxId From Questao";
	BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);
	if (dbRs.next()) {
		maxId = dbRs.getLong("maxId");
		maxId++;
	}
	String id = Long.toString(maxId);

	criterios = StringConverter.toDataBaseNotation(criterios);
	enunciado = StringConverter.toDataBaseNotation(enunciado);
	
	// Insere o elemento na base de dados
	str = "INSERT INTO Questao (cod, enunciado, criterios, tipo)";
	str += " VALUES ("+id+",'"+enunciado+"'"+",'"+criterios+"'"+",'"+tipo+"'"+")";
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	str = "INSERT INTO AgrupamentoQuestao (codAgrupamento, codQuestao, peso)";
	str += " VALUES ("+agrup.getCod()+","+id+","+peso+")";
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	
	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// ------- Insere na mem�ria -------
	// Cria um novo objeto
	Questao obj = null;
	//**********SELECAO DE TIPO**********
	if (tipo.equals(QuestaoTexto.getIdTipoQuestao())) {
		obj = new QuestaoTexto (id,enunciado,criterios);
	} else if (tipo.equals(QuestaoVouF.getIdTipoQuestao())) {
		obj = new QuestaoVouF (id,enunciado,criterios);
	} else if (tipo.equals(QuestaoME.getIdTipoQuestao())) {
		obj = new QuestaoME (id,enunciado,criterios);
	} else if (tipo.equals(QuestaoSubmissaoArquivo.getIdTipoQuestao())) {
		obj = new QuestaoSubmissaoArquivo (id,enunciado,criterios);
	} else if (tipo.equals(QuestaoSQL.getIdTipoQuestao())) {
		obj = new QuestaoSQL (id,enunciado,criterios);
	} else if (tipo.equals(QuestaoJava.getIdTipoQuestao())) {
		obj = new QuestaoJava (id,enunciado,criterios);	
	} else if (tipo.equals(QuestaoEscolhaDeTema.getIdTipoQuestao())) {
		obj = new QuestaoEscolhaDeTema (id,enunciado,criterios);
	} else if (tipo.equals(QuestaoOutraFormaEntrega.getIdTipoQuestao())) {
		obj = new QuestaoOutraFormaEntrega (id,enunciado,criterios);
	}
	
	obj.processaDadosDeCadastroDeNovaQuestao(request);
	
	agrup.addQuestao(obj);
	agrup.addPeso(peso);

	// Insere o objeto na lista do gerente
	this.listaObj.addElement(obj);

	return obj;

}
protected synchronized static void inicializa() throws Exception {
	// Precisa do Disciplina_ger

	if (listaObj == null) { // primeira utilização do gerente de objetos
		
		listaObj = new Vector();

		// Inicia a conexão com a base de dados
		Connection dbCon = BancoDados.abreConexao();
		Statement dbStmt = dbCon.createStatement();
		ResultSet dbRs;

		// seleciona todos objetos
		String str = "SELECT * FROM Questao";
		BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);

		// Gerentes auxiliares
		Disciplina_ger gerDisciplina = new Disciplina_ger();
		
		while (dbRs.next()) {
			// Le dados da base
			String cod = dbRs.getString("cod");
			String enunciado = dbRs.getString("enunciado");
			String criterios = dbRs.getString("criterios");
			String tipo = dbRs.getString("tipo");
			String codDisc = dbRs.getString("codDisc");
			Disciplina disc = null;
			if (codDisc != null) {
				disc = gerDisciplina.getElement(codDisc);
			} 

			// Instancia o objeto
			Questao obj = null;
			//**********SELECAO DE TIPO**********
			if (tipo.equals(QuestaoTexto.getIdTipoQuestao())) {
				obj = new QuestaoTexto (cod,enunciado,criterios);
			} else if (tipo.equals(QuestaoVouF.getIdTipoQuestao())) {
				obj = new QuestaoVouF (cod,enunciado,criterios);
			} else if (tipo.equals(QuestaoME.getIdTipoQuestao())) {
				obj = new QuestaoME (cod,enunciado,criterios);
			} else if (tipo.equals(QuestaoSubmissaoArquivo.getIdTipoQuestao())) {
				obj = new QuestaoSubmissaoArquivo (cod,enunciado,criterios);
			} else if (tipo.equals(QuestaoSQL.getIdTipoQuestao())) {
				obj = new QuestaoSQL (cod,enunciado,criterios);
			} else if (tipo.equals(QuestaoJava.getIdTipoQuestao())) {
				obj = new QuestaoJava (cod,enunciado,criterios);
			} else if (tipo.equals(QuestaoEscolhaDeTema.getIdTipoQuestao())) {
				obj = new QuestaoEscolhaDeTema (cod,enunciado,criterios);
			} else if (tipo.equals(QuestaoOutraFormaEntrega.getIdTipoQuestao())) {
				obj = new QuestaoOutraFormaEntrega (cod,enunciado,criterios);
			} else {
				throw new Exception("Tipo "+tipo+" não reconhecido.");
			}

			// Coloca-o na lista de objetos
			listaObj.addElement(obj);
		}

		listaObj.trimToSize();

		// Finaliza conexao
		dbStmt.close();
		dbCon.close();

	}
	
}
public synchronized void remove(Questao obj) throws Exception {

	// PROCEDIMENTOS PR�-Remoção
	// Informa agrupamentos
	Agrupamento_ger agrupger = new Agrupamento_ger();
	agrupger.removeQuestaoTodosAgrups(obj);

	ListaGerada_ger listageradager = new ListaGerada_ger();
	listageradager.removeQuestaoTodasListas(obj);

	// Remoção
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM Questao WHERE cod="+obj.getCod();
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

public synchronized String testaConsistencia(Questao obj, String enunciado, String criterios, String tipo)  throws Exception {

	boolean inclusao = (obj==null);

	// Testa atributos obrigat�rios
	if (enunciado == null) return "enunciado";
	if (enunciado.equals("")) return "enunciado";

	// Testa atributos exclusivos

	/* Coloque aqui o código restante do código referente ao teste de consist�ncia */

	return null;

}
}
