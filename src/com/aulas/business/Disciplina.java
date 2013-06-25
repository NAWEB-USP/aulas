package com.aulas.business;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 04/02/2003 10:14:00 -- *
 * -- Gerador versão alpha                                       *
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
 
public class Disciplina implements Comparable {
	private String cod;
	private String nome;
	private String descricao;
	private boolean desativada;

protected Disciplina(String cod, String nome, String descricao, boolean desativada) throws Exception {

	this.cod = cod;
	this.nome = nome;
	this.descricao = descricao;
	this.desativada = desativada;

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
		
		String str1 = StringConverter.removeAcentos(this.getNome().toUpperCase());
		String str2 = StringConverter.removeAcentos(((Disciplina) o).getNome().toUpperCase());
		return str1.compareTo(str2);
	}
	catch (Exception ex) {
		return -1;
	}
}
public String getCod() {
	return this.cod;
}
public String getDescricao() {
	return this.descricao;
}
public String getNome() {
	return this.nome;
}
public boolean isDesativada() {
	return this.desativada;
}
protected void setDesativada() {
	this.desativada = false;
}
protected void setDescricao(String descricao) {
	this.descricao = descricao;
}
protected void setNome(String nome) {
	this.nome = nome;
}
}
