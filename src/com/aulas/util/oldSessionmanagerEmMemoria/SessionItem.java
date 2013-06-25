package com.aulas.util.oldSessionmanagerEmMemoria;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 10/02/2003 22:39:37 -- *
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

class SessionItem {
	private String id;
	private String idSessao;
	private String campo;
	private String valor;

protected SessionItem(String id, String idSessao, String campo, String valor) throws Exception {

	this.id = id;
	this.idSessao = idSessao;
	this.campo = campo;
	this.valor = valor;

}
public String getCampo() {
	return this.campo;
}
public String getId() {
	return this.id;
}
public String getIdSessao() {
	return this.idSessao;
}
public String getValor() {
	return this.valor;
}
protected void setCampo(String campo) {
	this.campo = campo;
}
protected void setIdSessao(String idSessao) {
	this.idSessao = idSessao;
}
protected void setValor(String valor) {
	this.valor = valor;
}
}
