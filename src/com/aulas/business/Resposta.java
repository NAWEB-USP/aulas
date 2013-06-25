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


import java.util.*;

public class Resposta implements Comparable {
	private String cod;
	private String resposta;
	private String comentario;
	private int pontuacao;
	private int hits;
	private Questao questao;
	private boolean nova;

public Resposta(String cod, String resposta, String comentario, int pontuacao, int hits, Questao questao, boolean nova) throws Exception {

	this.cod = cod;
	this.resposta = resposta;
	this.comentario = comentario;
	this.pontuacao = pontuacao;
	this.hits = hits;
	this.questao = questao;
	this.nova = nova;

}
	/**
	 * Compares this object with the specified object for order.  Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.<p>
	 *
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)<p>
	 *
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
	 * <tt>x.compareTo(z)&gt;0</tt>.<p>
	 *
	 * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
	 * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
	 * all <tt>z</tt>.<p>
	 *
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
	 * class that implements the <tt>Comparable</tt> interface and violates
	 * this condition should clearly indicate this fact.  The recommended
	 * language is "Note: this class has a natural ordering that is
	 * inconsistent with equals."
	 * 
	 * @param   o the Object to be compared.
	 * @return  a negative integer, zero, or a positive integer as this object
	 *		is less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException if the specified object's type prevents it
	 *         from being compared to this Object.
	 */
public int compareTo(java.lang.Object o) {
	try {
		Resposta resp2 = (Resposta) o;
		int num1 = this.getHits();
		int num2 = resp2.getHits();
		if (num1<num2) {
			return 1;
		} else if (num1 == num2) {
			return 0;
		} else {
			return -1;
		}
	}
	catch (Exception ex) {
		return -1;
	}
}
public String getCod() {
	return this.cod;
}
public String getComentario() {
	return this.comentario;
}
public int getHits() {
	return this.hits;
}
public int getPontuacao() {
	return this.pontuacao;
}
public Questao getQuestao() {
	return this.questao;
}
public String getResposta() {
	return this.resposta;
}
public boolean isNova() {
	return this.nova;
}
public void setComentario(String comentario) {
	this.comentario = comentario;
}
protected void setHits(int hits) {
	this.hits = hits;
}
protected void setNova(boolean newNova) {
	this.nova = newNova;
}
public void setPontuacao(int pontuacao) {
	this.pontuacao = pontuacao;
}
protected void setQuestao(Questao questao) {
	this.questao = questao;
}
protected void setResposta(String resposta) {
	this.resposta = resposta;
}
}
