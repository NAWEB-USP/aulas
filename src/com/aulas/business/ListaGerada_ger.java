package com.aulas.business;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 14/02/2003 19:36:41 -- *
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
import com.sun.media.sound.MidiUtils.TempoCache;

import java.sql.*;
import java.util.*;

public class ListaGerada_ger {

	static private Vector listaObj; // Campo lista de objetos gerenciados
	
/* Construtor do objeto - carrega todas as inst�ncias do objeto gerenciado do banco de dados para a mem�ria */

public ListaGerada_ger() throws Exception {
	InicializaGerentes.inicializaGerentes();
}
public void addAlunoListaGerada(Aluno aluno, ListaGerada listaG)  throws Exception {

	// ------- Insere na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;


	// Armazena na base de dados os alunos
	str = "INSERT INTO ListaGeradaAluno (codListaGerada, codAluno)";
	str += " VALUES ("+listaG.getCod()+","+aluno.getCod()+")";
	
	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}
	
	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// ------- Insere na mem�ria -------
	listaG.addAluno(aluno);
	
}
public void altera(ListaGerada obj, int status, double nota, java.util.Date dataGeracao, java.util.Date dataFinalizacao, Lista lista)  throws Exception {

	// ------- Testa consist�ncia dos dados -------
	String testeCons = testaConsistencia(obj,status,nota,dataGeracao,dataFinalizacao,lista);
	if (testeCons != null) throw new Exception("não foi possível alterar devido ao campo "+testeCons+"");

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	String codLista = (lista != null) ? lista.getCod() : "0";
	str = "UPDATE ListaGerada SET status="+status+", nota="+nota+", dataGeracao="+((dataGeracao==null)?"null":"'"+dataGeracao.getTime()+"'")+", dataFinalizacao="+((dataFinalizacao==null)?"null":"'"+dataFinalizacao.getTime()+"'")+", codLista="+codLista+" WHERE cod="+obj.getCod();
	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	if (obj.getStatus() != status) {
		obj.setStatus(status);
	}
	if (obj.getNota() != nota) {
		obj.setNota(nota);
	}
	if (((obj.getDataGeracao() == null) && (dataGeracao != null)) || ((obj.getDataGeracao() != null) && !obj.getDataGeracao().equals(dataGeracao))) {
		obj.setDataGeracao(dataGeracao);
	}
	if (((obj.getDataFinalizacao() == null) && (dataFinalizacao != null)) || ((obj.getDataFinalizacao() != null) && !obj.getDataFinalizacao().equals(dataFinalizacao))) {
		obj.setDataFinalizacao(dataFinalizacao);
	}
	if (((obj.getLista() == null) && (lista != null)) || ((obj.getLista() != null) && !obj.getLista().equals(lista))) {
		obj.setLista(lista);
	}
}
public void alteraRespostaQuestao(ListaGerada obj, Questao questao, Resposta respOld, String respNova)  throws Exception {

	Resposta resp;
	String textoResposta = "";

	if ((respOld==null) && ((respNova==null) || respNova.equals(""))) {
		// era nula e continua nula
		return; // nao faz nada
		
	} else if ((respOld!=null) && questao.comparaDuasRespostas(respOld.getResposta(),respNova)) {
		// nao era nula e a resposta nova � equivalente
		Resposta tempResp = (respOld.getPontuacao() != 100) ? questao.processaResposta(respNova) : null; // processa novamente
		if (!(tempResp == null || (tempResp.getPontuacao() == respOld.getPontuacao() && tempResp.getComentario().equals(respOld.getComentario())))) {
			// Mudou a correção
			respOld.setPontuacao(tempResp.getPontuacao());
			respOld.setComentario(tempResp.getComentario());
			(new Resposta_ger()).altera(respOld);
		}
		String oldText = obj.getTextoResposta(questao);
		if ((obj.getTextoResposta(questao) != null) && oldText.equals(respNova)) {
			// o texto da resposta não mudou
			return;
		} else {
			resp = respOld;
			textoResposta = respNova;
			// Procede para atualizar a base
		}
		
	} else {
		// resposta anterior nula ou resposta mudou
		Resposta_ger respger = new Resposta_ger();
		resp = respger.getElementByResposta(questao,respNova); // ve se já existe resposta

		if (resp == null) { // resposta nao existe na base
			if ((respNova != null) && !respNova.equals("")) { 
				// cria nova resposta, se a resposta nao for nula
				Resposta tempResp = questao.processaResposta(respNova);
				if (tempResp != null && (tempResp.getPontuacao() != 0 || !tempResp.getComentario().equals(""))) {
					// Para o caso por exemplo de questao Java
					resp = respger.inclui(respNova,StringConverter.toDataBaseNotation(tempResp.getComentario()),tempResp.getPontuacao(),1,questao,false);
				} else {
					resp = respger.inclui(respNova,"",0,1,questao,true);
				}
			}
		} else { // resposta já existe
			if (!resp.getResposta().equals(respNova)) { // se a resposta não for id�ntica
				textoResposta = respNova;
			}
			respger.hitsIncrementa(resp);
		}
		if (respOld != null) { // resposta anterior não era nula
			respger.hitsDecrementa(respOld);
			if ((respOld.getHits() == 0) && (respOld.isNova())) { // nao � mais usada
				respger.remove(respOld);
			}
		}
	}
	
	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	textoResposta = StringConverter.toDataBaseNotation(textoResposta);
	
	String versao;
	if (respOld == null) { // não existia resposta anterior
		versao = "1";
		str = "UPDATE ListaGeradaQuestao SET codResposta="+resp.getCod()+", versaoResposta=1, TextoResposta='"+textoResposta+"' WHERE codListaGerada="+obj.getCod()+" AND codQuestao="+questao.getCod();
	} else if (resp == null) { // Nova resposta e' nula (a pessoa apagou o que escreveu)
		versao = "0";
		str = "UPDATE ListaGeradaQuestao SET codResposta=0, versaoResposta=0, TextoResposta='' WHERE codListaGerada="+obj.getCod()+" AND codQuestao="+questao.getCod();
	} else { // alteracao de resposta
		versao = String.valueOf(Integer.parseInt(obj.getQuestoesVersaoRespostas().elementAt(obj.getQuestoes().indexOf(questao)).toString())+1);
		str = "UPDATE ListaGeradaQuestao SET codResposta="+resp.getCod()+", versaoResposta="+versao+", TextoResposta='"+textoResposta+"' WHERE codListaGerada="+obj.getCod()+" AND codQuestao="+questao.getCod();
	}

	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	obj.setRespostaQuestao(questao,resp,versao,textoResposta);
}
public void alteraStatusCorrigida(ListaGerada obj)  throws Exception {

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	java.util.Date dataFinalizacao = new java.util.Date();
	str = "UPDATE ListaGerada SET status="+ListaGerada.CORRIGIDA+" WHERE cod="+obj.getCod();
	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	obj.setStatus(ListaGerada.CORRIGIDA);

}
public void alteraStatusEdicao(ListaGerada obj)  throws Exception {

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	java.util.Date dataFinalizacao = new java.util.Date();
	str = "UPDATE ListaGerada SET status="+ListaGerada.EDICAO+" WHERE cod="+obj.getCod();
	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	obj.setStatus(ListaGerada.EDICAO);

}
public void alteraStatusFinalizada(ListaGerada obj)  throws Exception {

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	java.util.Date dataFinalizacao = new java.util.Date();
	str = "UPDATE ListaGerada SET DataFinalizacao='"+dataFinalizacao.getTime()+"', status="+ListaGerada.FINALIZADA+" WHERE cod="+obj.getCod();
	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	obj.setStatus(ListaGerada.FINALIZADA);
	obj.setDataFinalizacao(dataFinalizacao);

	// Se a autocorreção da lista estiver habilitada, vai direto para auto-correção
	if (obj.getLista().isAutoCorrection()) {
		corrige(obj);
		return;
	}
}
public void corrige(ListaGerada obj)  throws Exception {

	Lista lista = obj.getLista();
	
	// Percorre todas questoes da lista contabilizando-as.
	Enumeration questoes = obj.getQuestoes().elements();
	Enumeration respostas = obj.getQuestoesRespostas().elements();

	int somaNotas = 0;
	int somaPesos = 0;
	boolean totalmenteCorrigida = true;
	while (questoes.hasMoreElements()) {
		Questao questao = (Questao) questoes.nextElement();
		Resposta resp = (Resposta) respostas.nextElement();

		int peso = lista.getPesoQuestao(questao);
		if ((resp != null) && (!resp.isNova())) {
			somaNotas += peso * resp.getPontuacao();
		}
		somaPesos += peso; // o peso entra para o c�lculo mesmo se a questão estiver sem corrigir.

		if ((resp != null) && resp.isNova()) {
			totalmenteCorrigida = false;
		}
	}

	if (somaPesos == 0)	{
		somaPesos = 1;
	}
		
	// Calcula media parcial
	double media = (1.0/10.0) * somaNotas/somaPesos;
	
	// Descobre turma
	Turma turma = ((Aluno) obj.getAlunos().firstElement()).getTurma(obj.getLista().getDisc());
	
	// Aplica a correção de prazo
	java.util.Date dataFinalizacao = obj.getDataFinalizacao();
	if (Data.verificaPrazoSemHoras(turma.getData1(lista),dataFinalizacao)) {
		media = media * turma.getPeso1(lista) / 100;
	} else if (Data.verificaPrazoSemHoras(turma.getData2(lista),dataFinalizacao)) { 
		media = media * turma.getPeso2(lista) / 100;
	} else if (Data.verificaPrazoSemHoras(turma.getData3(lista),dataFinalizacao)) {
		media = media * turma.getPeso3(lista) / 100;
	} else if (Data.verificaPrazoSemHoras(turma.getData4(lista),dataFinalizacao)) {
		media = media * turma.getPeso4(lista) / 100;
	} else if (Data.verificaPrazoSemHoras(turma.getData5(lista),dataFinalizacao)) {
		media = media * turma.getPeso5(lista) / 100;
	} else if (Data.verificaPrazoSemHoras(turma.getData6(lista),dataFinalizacao)) {
		media = media * turma.getPeso6(lista) / 100;
	} else {
		media = 0;
	}

	// arredonda
	media = Math.round(media*100.0)/100.0;
	
	int oldStatus = obj.getStatus();
	double oldNota = obj.getNota();
	
	// Se nao for, passa status para em correcao	
	if ((obj.getStatus() != ListaGerada.EMCORRECAO) || (obj.getNota() != media)) {
		
		// ------- Altera na base de dados -------
		// Inicia a conexão com a base de dados
		Connection dbCon = BancoDados.abreConexao();
		Statement dbStmt = dbCon.createStatement();
		ResultSet dbRs;
		String str;

		str = "UPDATE ListaGerada SET nota="+media+", status="+ListaGerada.EMCORRECAO+" WHERE cod="+obj.getCod();
		try {
			BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
		} catch (Exception e) {
			throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
		}

		// Finaliza conexao
		dbStmt.close();
		dbCon.close();

		// Altera dados do objeto
		obj.setStatus(ListaGerada.EMCORRECAO);
		obj.setNota(media);

	}

	// Se nao houver mais questao a ser corrigida, passa status para corrigida.
	if (totalmenteCorrigida) {
		// ------- Altera na base de dados -------
		// Inicia a conexão com a base de dados
		Connection dbCon = BancoDados.abreConexao();
		Statement dbStmt = dbCon.createStatement();
		ResultSet dbRs;
		String str;

		str = "UPDATE ListaGerada SET status="+ListaGerada.CORRIGIDA+" WHERE cod="+obj.getCod();
		try {
			BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
		} catch (Exception e) {
			throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
		}

		// Finaliza conexao
		dbStmt.close();
		dbCon.close();

		// Altera dados do objeto
		obj.setStatus(ListaGerada.CORRIGIDA);

		// Manda e-mail para o grupo notificando o resultado
		if ((oldNota != obj.getNota()) || (oldStatus != ListaGerada.CORRIGIDA)) {
			Enumeration alunos = obj.getAlunos().elements();
			while (alunos.hasMoreElements()) {
				Aluno a = (Aluno) alunos.nextElement();
				str = a.getSexo().equals("M")?"Caro ":"Cara ";
				str += a.getNome()+",\n\n";
				str += "A \""+StringConverter.concatenateWithoutRepetion("lista",obj.getLista().getNome())+"\"\n";
				str += " da disciplina \""+obj.getLista().getDisc().getNome()+"\" foi totalmente corrigida.\n\n";
				str += "Sua nota é "+obj.getNota()+". Acesse o site para verificar a correção.\n\n";
				str += "Atenciosamente,\n\nMarco Gerosa\n\n";
				
				SendMail sender = new SendMail();
				sender.addTo(a.getEmail(), a.getEmail());
				sender.setAddressFrom("gerosa@ime.usp.br");
			    sender.setNameFrom("Site de Aulas Gerosa");
			    sender.setMessageSubject("[Gerosa Site] Notificação de resultado");
			    sender.setMessageText(str);
			    SendMailThread send = new SendMailThread();
			    send.setSendMail(sender);
			    send.start();
			}
		}
	}
		
}
public synchronized ListaGerada duplicaListaGerada(ListaGerada listaGerada, Aluno aluno)  throws Exception {

	// ------- Insere na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	// Pega Id maximo
	long maxId = 1;
	str = "SELECT Max(cod) AS maxId From ListaGerada";
	BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);
	if (dbRs.next()) {
		maxId = dbRs.getLong("maxId");
		maxId++;
	}
	String id = Long.toString(maxId);

	// Insere o elemento na base de dados
	str = "INSERT INTO ListaGerada (cod, status, nota, dataGeracao, dataFinalizacao, codLista)";
	str += " VALUES ("+id+","+listaGerada.getStatus()+","+listaGerada.getNota()+","+((listaGerada.getDataGeracao()==null)?"null":"'"+listaGerada.getDataGeracao().getTime()+"'")+","+((listaGerada.getDataFinalizacao()==null)?"null":"'"+listaGerada.getDataFinalizacao().getTime()+"'")+","+listaGerada.getLista().getCod()+")";

	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}

	// Armazena na base de dados os alunos
	str = "INSERT INTO ListaGeradaAluno (codListaGerada, codAluno)";
	str += " VALUES ("+id+","+aluno.getCod()+")";
	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}
	
	// Gera lista de questoes
	Vector questoes = (Vector) listaGerada.getQuestoes().clone();
	Vector questoesRespostas = (Vector) listaGerada.getQuestoesRespostas().clone();
	Vector questoesAgrupamentos = (Vector) listaGerada.getQuestoesAgrupamentos().clone();
	Vector questoesVersaoRespostas = (Vector) listaGerada.getQuestoesVersaoRespostas().clone();
	Vector questoesTextoRespostas = (Vector) listaGerada.getQuestoesTextosRespostas().clone();

	// Armazena na base de dados as questoes
	for (int i = 0; i < questoes.size(); i++) {
		Questao questao = (Questao) questoes.elementAt(i);
		Resposta resposta = (Resposta) questoesRespostas.elementAt(i);
		Agrupamento agrup = (Agrupamento) questoesAgrupamentos.elementAt(i);
		String versaoResposta = (String) questoesVersaoRespostas.elementAt(i);
		String textoResposta = (String) questoesTextoRespostas.elementAt(i);
		str = "INSERT INTO ListaGeradaQuestao (codListaGerada, codQuestao, codAgrupamento, codResposta, versaoResposta, textoResposta)";
		str += " VALUES ("+id+","+questao.getCod()+","+agrup.getCod()+","+((resposta==null)?"0":resposta.getCod())+","+versaoResposta+",'"+textoResposta+"')";
		try {
			BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
		} catch (Exception e) {
			throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
		}
	}	
	
	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// ------- Insere na mem�ria -------
	// Cria um novo objeto
	Vector alunos = new Vector();
	alunos.addElement(aluno);
	ListaGerada obj = new ListaGerada(id,listaGerada.getStatus(),listaGerada.getNota(),listaGerada.getDataGeracao(),listaGerada.getDataFinalizacao(),"",listaGerada.getLista(),alunos,questoes,questoesRespostas,questoesVersaoRespostas,questoesAgrupamentos,questoesTextoRespostas);
	geraNovamenteCodigoIdentificacao(obj);

	// Insere o objeto na lista do gerente
	this.listaObj.addElement(obj);

	return obj;

}
public Vector getAllElements() {

	return (Vector) listaObj.clone();

}
public synchronized ListaGerada getElement(Lista lista, Aluno aluno) throws Exception {

	 if (this.listaObj == null) throw new Exception("Não inicializado.");
	
	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		ListaGerada elem = (ListaGerada) this.listaObj.elementAt(i);
		if (elem.getLista().equals(lista) && elem.getAlunos().contains(aluno)) {
			return elem;
		}
	}

	return null;

}
public synchronized ListaGerada getElement(String cod) throws Exception {

	return getElementByCod (cod);

}
public synchronized ListaGerada getElementByCod (String cod) throws Exception {

	ListaGerada obj;

	// percorre a lista procurando o id
	for (int i = 0; i < this.listaObj.size(); i++) {
		obj = (ListaGerada) this.listaObj.elementAt(i);
		if (cod.equals(obj.getCod())) {
			return obj;
		}
	}

	//throw new Exception ("ListaGerada com código " + cod + " não foi encontrado em memória.");
	return null;
}
public synchronized Vector getElements(Lista obj) throws Exception {

	Vector elements = new Vector();
	ListaGerada elem;

	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (ListaGerada) this.listaObj.elementAt(i);
		if (elem.getLista().equals(obj)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;

}
public synchronized Vector getElements(Questao questao) throws Exception {

	Vector elements = new Vector();
	ListaGerada elem;

	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (ListaGerada) this.listaObj.elementAt(i);
		if (elem.getQuestoes().contains(questao)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;

}
public synchronized void remove(ListaGerada obj) throws Exception {

	// PROCEDIMENTOS PR�-Remoção
	// atualiza hits e apaga respostas não corrigidas
	Enumeration respostas = obj.getQuestoesRespostas().elements();
	Resposta_ger respger = new Resposta_ger();
	while (respostas.hasMoreElements()) {
		Resposta resp = (Resposta) respostas.nextElement();
		if (resp != null) {
			respger.hitsDecrementa(resp);
			if ((resp.getHits() == 0) && (resp.isNova())) { // nao � mais usada
				respger.remove(resp);
			}	
		}
	}

	// Remoção
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM ListaGeradaAluno WHERE codListaGerada="+obj.getCod();
	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}

	str = "DELETE FROM ListaGeradaQuestao WHERE codListaGerada="+obj.getCod();
	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}
	
	str = "DELETE FROM ListaGerada WHERE cod="+obj.getCod();
	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}

	dbStmt.close();
	dbCon.close();

	// ------- Remove da mem�ria -------
	this.listaObj.removeElement(obj);

	// PROCEDIMENTOS P�S-Remoção


}
public synchronized void removeAlunoListaGerada(Aluno aluno, ListaGerada listaGerada)  throws Exception {

	// ------- Insere na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	// Remove da base de dados
	str = "DELETE FROM ListaGeradaAluno WHERE CodListaGerada="+listaGerada.getCod()+" AND CodAluno="+aluno.getCod();
	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}
	
	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// ------- Insere na mem�ria -------
	listaGerada.removeAluno(aluno);
	if (listaGerada.getAlunos().size() == 0) {
		this.remove(listaGerada);
	}
	
}
public synchronized void removeAlunoTodasListas(Aluno aluno) throws Exception {
	
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM ListaGeradaAluno WHERE codAluno="+aluno.getCod();
	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}
	
	dbStmt.close();
	dbCon.close();
	
	// --- percorre a lista, removendo da memoria ---
	for (int i = 0; i < this.listaObj.size(); i++) {
		ListaGerada obj = (ListaGerada) this.listaObj.elementAt(i);
		obj.removeAluno(aluno);
		if (obj.getAlunos().size() == 0) {
			this.remove(obj);
		}
	}


}
public synchronized void removeQuestaoTodasListas(Questao questao) throws Exception {
	
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM ListaGeradaQuestao WHERE codQuestao="+questao.getCod();
	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}
	
	dbStmt.close();
	dbCon.close();
	
	// --- percorre a lista, removendo da memoria ---
	for (int i = 0; i < this.listaObj.size(); i++) {
		ListaGerada obj = (ListaGerada) this.listaObj.elementAt(i);
		obj.removeQuestao(questao);
	}


}
/* Verifica a consist�ncia dos dados antes de uma inclusão ou Alteração */
/* Para inclusão utilize o código identificador o objeto igual a null */
/* Retorna o nome do campo inconsistente ou null para tudo ok */

public synchronized String testaConsistencia(ListaGerada obj, int status, double nota, java.util.Date dataGeracao, java.util.Date dataFinalizacao, Lista lista)  throws Exception {

	boolean inclusao = (obj==null);

	// Testa atributos obrigat�rios
	if (lista == null) return "lista";

	// Testa atributos exclusivos

	/* Coloque aqui o código restante do código referente ao teste de consist�ncia */

	return null;

}
public void zerarLista(ListaGerada obj)  throws Exception {

	// Zera as respostas
	ListaGerada_ger listageradager = new ListaGerada_ger();
	Enumeration questoes = obj.getQuestoes().elements();
	Enumeration respostas = obj.getQuestoesRespostas().elements();
	while (questoes.hasMoreElements()) {
		Questao questao = (Questao) questoes.nextElement();
		Resposta resp = (Resposta) respostas.nextElement();
		if (resp != null) {
			listageradager.alteraRespostaQuestao(obj,questao,resp,"");
		}
	}
		
	
	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	java.util.Date dataFinalizacao = new java.util.Date();
	str = "UPDATE ListaGerada SET status="+ListaGerada.ABERTA+" WHERE cod="+obj.getCod();
	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	obj.setStatus(ListaGerada.ABERTA);

}

public void alteraNota(ListaGerada obj, double nota)  throws Exception {

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	if (obj.getStatus() == ListaGerada.CORRIGIDA) {
		str = "UPDATE ListaGerada SET nota="+nota+" WHERE cod="+obj.getCod();
		try {
			BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
		} catch (Exception e) {
			throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
		}
		obj.setNota(nota);
	} else {
		java.util.Date dataFinalizacao = new java.util.Date();
		str = "UPDATE ListaGerada SET status="+ListaGerada.CORRIGIDA+", nota="+nota+", dataFinalizacao='"+dataFinalizacao.getTime()+"'"+" WHERE cod="+obj.getCod();
		try {
			BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
		} catch (Exception e) {
			throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
		}
		obj.setStatus(ListaGerada.CORRIGIDA);
		obj.setNota(nota);
		obj.setDataFinalizacao(dataFinalizacao);
	}

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

}

public void generateRelacaoQuestoes (Lista lista, Turma turma, Vector questoesOutput, Vector questoesAgrupamentosOutput) throws Exception {

	// Le agrupamentos
	Agrupamento_ger agrupger = new Agrupamento_ger();
	Enumeration agrupamentos;
	if (lista.isSeguirOrdemQuestoes()) {
		agrupamentos = agrupger.getElements(lista).elements();
	} else { // embaralha os agrupamentos
		agrupamentos = Vetor.embaralha(agrupger.getElements(lista)).elements();
	}
	
	// Monta lista de questoes
	while (agrupamentos.hasMoreElements()) {
		Agrupamento agrup = (Agrupamento) agrupamentos.nextElement();
		
		// Le questoes
		Vector questoes = (Vector) agrup.getQuestoes().clone();

		// embaralha as questoes se for necess�rio
		if (!agrup.isSeguirOrdem()) { 
			questoes = Vetor.embaralha(questoes);
		}

		// distribui uniformemente se for necess�rio
		int numMaxQuestoes = agrup.getNumMaxQuestoes();
		if (agrup.isDistribuicaoUniforme() && (numMaxQuestoes != 0)) {
			// verifica qual � o m�nimo de alocações e coloca em novoVetor
			Vector novoVetor = new Vector();
			for (int i = 0; i < numMaxQuestoes; i++) { // seleciona as questoes
				// pega a que tem menos ocorr�ncias
				Enumeration enumeration = questoes.elements();
				int numMinimoOcorrencias = Integer.MAX_VALUE;
				Questao questaoSelecionada = null;
				while (enumeration.hasMoreElements()) {
					Questao q = (Questao) enumeration.nextElement();
					int numOcorrencias = lista.contaOcorrenciasDeQuestaoEmUmaTurma(q,turma);
					if (numOcorrencias < numMinimoOcorrencias) {
						questaoSelecionada = q;
						numMinimoOcorrencias = numOcorrencias;
					}
				}
				// remove a questao selecionada do vetor de questoes
				questoes.removeElement(questaoSelecionada);
				// adiciona a questao selecionada no novo vetor
				novoVetor.addElement(questaoSelecionada);
			}
			questoes = novoVetor;
		}
        
        // copia ordem das questoes, se for o caso
        if (agrup.isRandomizarSomenteUmaVez()) {
            Vector listas = this.getElements(lista);
            if (listas.size() == 0) {
                // Primeira utilizacao da lista
                questoes = Vetor.embaralha(questoes);
            } else {
                ListaGerada umaListaGerada = (ListaGerada) listas.firstElement();
                questoes = (Vector) umaListaGerada.getQuestoes(agrup).clone();
            }
        }
		
		// remove o excesso de questoes se for necess�rio
		if (numMaxQuestoes != 0) { 
			while (questoes.size() > numMaxQuestoes) {
				questoes.removeElementAt(0);
			}
		}

		// Monta os vetores de saida (questoesAgrupamentosOutput e questoesOutput)
		Enumeration qs = questoes.elements();
		while (qs.hasMoreElements()) {
			Questao questao = (Questao) qs.nextElement();
			questoesOutput.addElement(questao);
			questoesAgrupamentosOutput.addElement(agrup);
		}
	}
	
	// Retorna vetores
	questoesOutput.trimToSize();
	questoesAgrupamentosOutput.trimToSize();
		
}

public static void geraNovamenteCodigoIdentificacao(ListaGerada obj)  throws Exception {

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	obj.geraCodigoIdentificador();
	str = "UPDATE ListaGerada SET CodigoIdentificacao='"+obj.getCodigoIdentificador()+"' WHERE cod="+obj.getCod();
	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

}

public synchronized ListaGerada getElementByCodigoIdentificacao (String codigoIdentificacao) throws Exception {

	ListaGerada obj;

	// percorre a lista procurando o id
	for (int i = 0; i < this.listaObj.size(); i++) {
		obj = (ListaGerada) this.listaObj.elementAt(i);
		if (codigoIdentificacao.equals(obj.getCodigoIdentificador())) {
			return obj;
		}
	}

	//throw new Exception ("ListaGerada com código " + cod + " não foi encontrado em mem�ria.");
	return null;
}

public synchronized Vector getElements(Lista lista, Questao questao) throws Exception {

	Vector elements = new Vector();
	ListaGerada elem;

	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (ListaGerada) this.listaObj.elementAt(i);
		if (elem.getLista().equals(lista) && elem.getQuestoes().contains(questao)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;

}

public synchronized ListaGerada inclui(int status, double nota, java.util.Date dataGeracao, java.util.Date dataFinalizacao, Lista lista, Turma turma, Vector alunos)  throws Exception {

	// ------- Testa consist�ncia dos dados -------
	String testeCons = testaConsistencia(null,status, nota, dataGeracao, dataFinalizacao, lista);
	if (testeCons != null) throw new Exception("não foi possível inserir devido ao campo "+testeCons+"");

	// ------- Insere na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	// Pega Id maximo
	long maxId = 1;
	str = "SELECT Max(cod) AS maxId From ListaGerada";
	BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);
	if (dbRs.next()) {
		maxId = dbRs.getLong("maxId");
		maxId++;
	}
	String id = Long.toString(maxId);

	// Insere o elemento na base de dados
	String idLista = (lista != null) ? lista.getCod() : "0";
	str = "INSERT INTO ListaGerada (cod, status, nota, dataGeracao, dataFinalizacao, codLista)";
	str += " VALUES ("+id+","+status+","+nota+","+((dataGeracao==null)?"null":"'"+dataGeracao.getTime()+"'")+","+((dataFinalizacao==null)?"null":"'"+dataFinalizacao.getTime()+"'")+","+((lista==null)?"null":lista.getCod())+")";

	try {
		BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	} catch (Exception e) {
		throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
	}
	// dbCon.commit(); // da erro no MySQL

	// Armazena na base de dados os alunos
	for (int i = 0; i < alunos.size(); i++) {
		Aluno aluno = (Aluno) alunos.elementAt(i);
		str = "INSERT INTO ListaGeradaAluno (codListaGerada, codAluno)";
		str += " VALUES ("+id+","+aluno.getCod()+")";
		try {
			BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
		} catch (Exception e) {
			throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
		}
	}	
	// dbCon.commit();
	
	// Gera lista de questoes
	Vector questoes = new Vector();
	Vector questoesAgrupamentos = new Vector();
	this.generateRelacaoQuestoes(lista,turma,questoes,questoesAgrupamentos);
	
	Vector questoesRespostas = new Vector();
	Vector questoesVersaoRespostas = new Vector();
	Vector questoesTextosRespostas = new Vector();

	// Armazena na base de dados as questoes
	for (int i = 0; i < questoes.size(); i++) {
		Questao questao = (Questao) questoes.elementAt(i);
		Agrupamento agrup = (Agrupamento) questoesAgrupamentos.elementAt(i);
		str = "INSERT INTO ListaGeradaQuestao (codListaGerada, codQuestao, codAgrupamento, codResposta, versaoResposta, textoResposta)";
		str += " VALUES ("+id+","+questao.getCod()+","+agrup.getCod()+",0,1,'')";
		try {
			BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
		} catch (Exception e) {
			throw new Exception("Erro ao executar o SQL: "+str+" \nErro: "+e.getMessage(), e); 
		}
		questoesRespostas.addElement(null);
		questoesVersaoRespostas.addElement("1");
		questoesTextosRespostas.addElement("");
	}	
	//dbCon.commit();
	
	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// ------- Insere na mem�ria -------
	// Cria um novo objeto
	ListaGerada obj = new ListaGerada(id,status,nota,dataGeracao,dataFinalizacao,"",lista,alunos,questoes,questoesRespostas,questoesVersaoRespostas,questoesAgrupamentos,questoesTextosRespostas);
	geraNovamenteCodigoIdentificacao(obj);

	// Insere o objeto na lista do gerente
	this.listaObj.addElement(obj);

	return obj;

}

protected synchronized static void inicializa() throws Exception {
	// Precisa do Lista_Ger, Questao_ger,agrup_ger,Resposta_ger e Aluno_ger

	if (listaObj == null) { // primeira utilização do gerente de objetos
		
		listaObj = new Vector();

		// Inicia a conexão com a base de dados
		Connection dbCon = BancoDados.abreConexao();
		Statement dbStmt = dbCon.createStatement();
		Statement dbStmt2 = dbCon.createStatement();
		ResultSet dbRs;

		// seleciona todos objetos
		String str = "SELECT * FROM ListaGerada";
		BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);

		// Gerentes de objetos
		Lista_ger gerLista = new Lista_ger();
		Questao_ger questaoger = new Questao_ger();
		Agrupamento_ger agrupger = new Agrupamento_ger();
		Resposta_ger respostaoger = new Resposta_ger();
		Aluno_ger alunoger = new Aluno_ger();
				
		while (dbRs.next()) {
			// Le dados da base
			String cod = dbRs.getString("cod");
			int status = dbRs.getInt("status");
			float nota = dbRs.getFloat("nota");
			java.util.Date dataGeracao = (((str = dbRs.getString("dataGeracao")) == null) ? null : new java.util.Date(Long.parseLong(str)));
			java.util.Date dataFinalizacao = (((str = dbRs.getString("dataFinalizacao")) == null) ? null : new java.util.Date(Long.parseLong(str)));
			String codigoIdentificacao = dbRs.getString("codigoIdentificacao");
			String codLista = dbRs.getString("codLista");
			Lista lista = null;
			if (codLista != null) {
				lista = gerLista.getElement(codLista);
			}

			// Instancia o objeto
			ListaGerada obj = new ListaGerada (cod,status,nota,dataGeracao,dataFinalizacao,codigoIdentificacao,lista,null,null,null,null,null,null);

			// Le questoes
			str = "SELECT * FROM ListaGeradaQuestao WHERE CodListaGerada="+cod;
			ResultSet dbRs2 = dbStmt2.executeQuery(str);
			Vector questoes = new Vector();
			Vector questoesRespostas = new Vector();
			Vector questoesVersoesRespostas = new Vector();
			Vector questoesAgrupamentos = new Vector();
			Vector questoesTextosRespostas = new Vector();
			while (dbRs2.next()) {
				String codQuestao = dbRs2.getString("codQuestao");
				String codReposta = dbRs2.getString("codResposta");
				String codAgrupamento = dbRs2.getString("codAgrupamento");
				String versaoResposta = dbRs2.getString("versaoResposta");
				String textoResposta = dbRs2.getString("textoResposta");
				Questao questao = questaoger.getElementByCod(codQuestao);
				Agrupamento agrup = agrupger.getElementByCod(codAgrupamento);
				Resposta resposta = ((codReposta == null) || codReposta.equals("0") || codReposta.equals("")) ? null : respostaoger.getElementByCod(codReposta);
				questoes.addElement(questao);
				questoesRespostas.addElement(resposta);
				questoesAgrupamentos.addElement(agrup);
				questoesVersoesRespostas.addElement(versaoResposta);
				questoesTextosRespostas.addElement(textoResposta);
			}
			questoes.trimToSize();
			questoesRespostas.trimToSize();
			questoesAgrupamentos.trimToSize();
			questoesVersoesRespostas.trimToSize();
			questoesTextosRespostas.trimToSize();
			obj.setQuestoes(questoes,questoesRespostas,questoesVersoesRespostas,questoesAgrupamentos,questoesTextosRespostas);
			
			// Le alunos
			str = "SELECT * FROM ListaGeradaAluno WHERE CodListaGerada="+cod;
			ResultSet dbRs3 = dbStmt2.executeQuery(str);
			Vector alunos = new Vector();
			while (dbRs3.next()) {
				String codAluno = dbRs3.getString("codAluno");
				Aluno a = alunoger.getElementByCod(codAluno);
				alunos.addElement(a);
			}
			alunos.trimToSize();
			obj.setAlunos(alunos);

			// Codigo Identificador
			if ((codigoIdentificacao == null) || (codigoIdentificacao.equals(""))) {
				geraNovamenteCodigoIdentificacao(obj);
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
}
