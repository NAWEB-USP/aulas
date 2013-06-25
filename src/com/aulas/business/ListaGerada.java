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


import java.util.*;
import com.aulas.util.*;

public class ListaGerada {
	private String cod;
	private int status;
	private double nota;
	private java.util.Date dataGeracao;
	private java.util.Date dataFinalizacao;
	private String codigoIndentificador;
	private Lista lista;
	private Vector alunos;
	private Vector questoes;
	private Vector questoesRespostas;
	private Vector questoesVersoesRespostas;
	private Vector questoesAgrupamentos;
	private Vector questoesTextosRespostas;

	// status
	public final static int ABERTA = 1;
	public final static int EDICAO = 2;
	public final static int FINALIZADA = 3;
	public final static int EMCORRECAO = 4;
	public final static int CORRIGIDA = 5;

protected ListaGerada(String cod, int status, double nota, java.util.Date dataGeracao, java.util.Date dataFinalizacao, String codigoIdentificador, Lista lista, Vector alunos, Vector questoes, Vector questoesRespostas, Vector questoesVersaoRespostas, Vector questoesAgrupamentos, Vector questoesTextosRespostas) throws Exception {

	this.cod = cod;
	this.status = status;
	this.nota = Math.round(nota*100.0)/100.0;
	this.dataGeracao = dataGeracao;
	this.dataFinalizacao = dataFinalizacao;
	this.codigoIndentificador = codigoIdentificador;
	this.lista = lista;
	this.alunos = alunos;
	this.questoes = questoes;
	this.questoesRespostas = questoesRespostas;
	this.questoesVersoesRespostas = questoesVersaoRespostas;
	this.questoesAgrupamentos = questoesAgrupamentos;
	this.questoesTextosRespostas = questoesTextosRespostas;

}
protected void addAluno(Aluno aluno) {
	this.alunos.addElement(aluno);
}
protected void addQuestao(Questao questao, Resposta resp, String versaoResposta, Agrupamento agrup) {
	this.questoes.addElement(questao);
	this.questoesRespostas.addElement(resp);
	this.questoesVersoesRespostas.addElement(versaoResposta);
	this.questoesAgrupamentos.addElement(agrup);
	this.questoesTextosRespostas.addElement(resp.getResposta());
}

protected void geraCodigoIdentificador() {

	String codigo = "";

	// Gera o código
	// parte 1 - código da lista gerada
	codigo += this.cod;
	codigo += ".";
	// parte 2 - dataGeracao e código da lista
	codigo += (this.dataGeracao.getTime() + Long.parseLong(this.lista.getCod())) % 1000;
	codigo += ".";
	// parte 3 - checksum
	java.util.zip.CRC32 checksum = new java.util.zip.CRC32();
	checksum.update(codigo.getBytes());
	codigo += checksum.getValue() % 1000;
	
	// Atribui
	this.codigoIndentificador = codigo;
	
}
public Vector<Aluno> getAlunos() {
	return this.alunos;
}
public String getCod() {
	return this.cod;
}
public String getCodigoIdentificador() {
	return this.codigoIndentificador;
}
public java.util.Date getDataFinalizacao() {
	return this.dataFinalizacao;
}
public java.util.Date getDataGeracao() {
	return this.dataGeracao;
}
public Lista getLista() {
	return this.lista;
}
public double getNota() {
	return this.nota;
}
public Vector<Questao> getQuestoes() {
	return this.questoes;
}
public Vector getQuestoes(Agrupamento agrup) {
    Vector questoesRet = new Vector();
    for (int i = 0; i < questoes.size(); i++) {
        Agrupamento ag = (Agrupamento) questoesAgrupamentos.elementAt(i);  
        if (ag.equals(agrup)) {
            questoesRet.add(questoes.elementAt(i));
        }
    }
    return questoesRet;
}

public Vector getQuestoesAgrupamentos() {
	return this.questoesAgrupamentos;
}
public Vector getQuestoesRespostas() {
	return this.questoesRespostas;
}
public Vector getQuestoesSemRespostas() {

	Vector elements = new Vector();
	Resposta elem;

	// percorre a lista
	for (int i = 0; i < this.questoesRespostas.size(); i++) {
		elem = (Resposta) this.questoesRespostas.elementAt(i);
		if (elem != null) {
			elements.addElement(this.questoes.elementAt(i));
		}
	}

	elements.trimToSize();
	return elements;
	
}
public Vector getQuestoesTextosRespostas() {
	return this.questoesTextosRespostas;
}
public Vector getQuestoesVersaoRespostas() {
	return this.questoesVersoesRespostas;
}
public Resposta getResposta(Questao questao) {
	int pos = questoes.indexOf(questao);
	if (pos == -1) return null;
	return (Resposta) this.questoesRespostas.elementAt(pos);
}
public int getStatus() {
	return this.status;
}
public String getTextoResposta(Questao questao) {
	int pos = questoes.indexOf(questao);
	if (pos == -1) return "";
	return (String) this.questoesTextosRespostas.elementAt(pos);
}
public Turma getTurma() throws Exception {

	Aluno aluno = (Aluno) this.alunos.firstElement();
	return aluno.getTurma(this.getLista().getDisc());
	
}
protected void removeAluno(Aluno aluno) {
	int pos = alunos.indexOf(aluno);
	if (pos == -1) return;
	this.alunos.removeElementAt(pos);
}
protected void removeQuestao(Questao questao) {
	int pos = questoes.indexOf(questao);
	if (pos == -1) return;
	this.questoes.removeElementAt(pos);
	this.questoesRespostas.removeElementAt(pos);
	this.questoesVersoesRespostas.removeElementAt(pos);
	this.questoesTextosRespostas.removeElementAt(pos);
}
protected void setAlunos(Vector alunos) {
	this.alunos = alunos;
}
protected void setDataFinalizacao(java.util.Date dataFinalizacao) {
	this.dataFinalizacao = dataFinalizacao;
}
protected void setDataGeracao(java.util.Date dataGeracao) {
	this.dataGeracao = dataGeracao;
}
protected void setLista(Lista lista) {
	this.lista = lista;
}
protected void setNota(double nota) {
	this.nota = nota;
}
protected void setQuestoes(Vector questoes, Vector questoesRespostas, Vector questoesVersaoRespostas, Vector questoesAgrupamentos, Vector questoesTextosRespostas) {
	this.questoes = questoes;
	this.questoesRespostas = questoesRespostas;
	this.questoesVersoesRespostas = questoesVersaoRespostas;
	this.questoesAgrupamentos = questoesAgrupamentos;
	this.questoesTextosRespostas = questoesTextosRespostas;
}
protected void setRespostaQuestao(Questao questao, Resposta resp, String versao, String textoResposta) {
	int pos = questoes.indexOf(questao);
	if (pos == -1) return;
	this.questoesRespostas.setElementAt(resp,pos);
	this.questoesVersoesRespostas.setElementAt(versao,pos);
	this.questoesTextosRespostas.setElementAt(textoResposta,pos);
}
protected void setStatus(int status) {
	this.status = status;
}
}
