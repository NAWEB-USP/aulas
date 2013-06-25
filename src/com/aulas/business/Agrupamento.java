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


import java.util.*;

public class Agrupamento {
	private String cod;
	private boolean seguirOrdem;
    private boolean randomizarSomenteUmaVez;
	private boolean distribuicaoUniforme;
	private String nome;
	private String enunciado;
	private int numMaxQuestoes;
	private Lista lista;
	private Vector lstQuestoes;
	private Vector lstPesosQuestoes;
protected Agrupamento(String cod, boolean seguirOrdem, boolean randomizarSomenteUmaVez, boolean distribuicaoUniforme, String nome, String enunciado, int numMaxQuestoes, Lista lista, Vector questoes, Vector pesosQuestoes) throws Exception {

	this.cod = cod;
	this.seguirOrdem = seguirOrdem;
    this.randomizarSomenteUmaVez = randomizarSomenteUmaVez;
	this.distribuicaoUniforme = distribuicaoUniforme;
	this.nome = nome;
	this.enunciado = enunciado;
	this.numMaxQuestoes = numMaxQuestoes;
	this.lista = lista;
	this.lstQuestoes = questoes;
	this.lstPesosQuestoes = pesosQuestoes;

}
protected void addPeso(String peso) {
	this.lstPesosQuestoes.addElement(peso);
}
protected void addQuestao(Questao questao) {
	this.lstQuestoes.addElement(questao);
}
public String getCod() {
	return this.cod;
}
public String getEnunciado() {
	return this.enunciado;
}
public Lista getLista() {
	return this.lista;
}
public String getNome() {
	return this.nome;
}
public int getNumMaxQuestoes() {
	return this.numMaxQuestoes;
}
public Vector getPesosQuestoes() {
	return this.lstPesosQuestoes;
}
public Vector getQuestoes() {
	return this.lstQuestoes;
}
public boolean isDistribuicaoUniforme() {
	return this.distribuicaoUniforme;
}
public boolean isSeguirOrdem() {
	return this.seguirOrdem;
}
protected void removeQuestao(Questao questao) {
	int pos = lstQuestoes.indexOf(questao);

	if (pos == -1) return;
	
	this.lstQuestoes.removeElementAt(pos);
	this.lstPesosQuestoes.removeElementAt(pos);

}
protected void setDistribuicaoUniforme(boolean distribuicaoUniforme) {
	this.distribuicaoUniforme = distribuicaoUniforme;
}
protected void setEnunciado(String enunciado) {
	this.enunciado = enunciado;
}
protected void setLista(Lista lista) {
	this.lista = lista;
}
protected void setNome(String nome) {
	this.nome = nome;
}
protected void setNumMaxQuestoes(int numMaxQuestoes) {
	this.numMaxQuestoes = numMaxQuestoes;
}
protected void setPesoQuestao(Questao quest, String peso) {
	int pos = this.lstQuestoes.indexOf(quest);

	if (pos == -1) return;

	this.lstPesosQuestoes.setElementAt(peso,pos);
}
protected void setPesosQuestoes(Vector pesos) {
	this.lstPesosQuestoes = pesos;
}
protected void setQuestoes(Vector questoes) {
	this.lstQuestoes = questoes;
}
protected void setSeguirOrdem(boolean seguirOrdem) {
	this.seguirOrdem = seguirOrdem;
}
public boolean isRandomizarSomenteUmaVez() {
    return this.randomizarSomenteUmaVez;
}
public void setRandomizarSomenteUmaVez(boolean randomizarSomenteUmaVez) {
    this.randomizarSomenteUmaVez = randomizarSomenteUmaVez;
}
}
