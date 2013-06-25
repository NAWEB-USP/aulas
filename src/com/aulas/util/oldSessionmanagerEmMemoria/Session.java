package com.aulas.util.oldSessionmanagerEmMemoria;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 10/02/2003 22:17:57 -- *
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

class Session {
	private String id;
	private java.util.Date dataUltAcesso;

protected Session(String id, java.util.Date dataUltAcesso) throws Exception {

	this.id = id;
	this.dataUltAcesso = dataUltAcesso;

}
public java.util.Date getDataUltAcesso() {
	return this.dataUltAcesso;
}
public String getId() {
	return this.id;
}
protected void setDataUltAcesso(java.util.Date dataUltAcesso) {
	this.dataUltAcesso = dataUltAcesso;
}
}
