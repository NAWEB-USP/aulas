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


import java.util.*;
import com.aulas.util.*;

public class Turma implements Comparable {
	private String cod;
	private String nome;
	private String descricao;
	private Disciplina disc;
	private Vector listas;
	private Vector listas_seguirpadrao;
	private Vector listas_ativa;
	private Vector listas_data1;
	private Vector listas_peso1;
	private Vector listas_data2;
	private Vector listas_peso2;
	private Vector listas_data3;
	private Vector listas_peso3;
	private Vector listas_data4;
	private Vector listas_peso4;
	private Vector listas_data5;
	private Vector listas_peso5;
	private Vector listas_data6;
	private Vector listas_peso6;

protected Turma(String cod, String nome, String descricao, Disciplina disc, Vector listas, Vector listas_seguirPadrao, Vector listas_ativa, Vector listas_data1, Vector listas_peso1, Vector listas_data2, Vector listas_peso2, Vector listas_data3, Vector listas_peso3, Vector listas_data4, Vector listas_peso4, Vector listas_data5, Vector listas_peso5, Vector listas_data6, Vector listas_peso6) throws Exception {

	this.cod = cod;
	this.nome = nome;
	this.descricao = descricao;
	this.disc = disc;
	this.listas = listas;
	this.listas_seguirpadrao = listas_seguirPadrao;
	this.listas_ativa = listas_ativa;
	this.listas_data1 = listas_data1;
	this.listas_peso1 = listas_peso1;
	this.listas_data2 = listas_data2;
	this.listas_peso2 = listas_peso2;
	this.listas_data3 = listas_data3;
	this.listas_peso3 = listas_peso3;
	this.listas_data4 = listas_data4;
	this.listas_peso4 = listas_peso4;
	this.listas_data5 = listas_data5;
	this.listas_peso5 = listas_peso5;
	this.listas_data6 = listas_data6;
	this.listas_peso6 = listas_peso6;
	
}

public void addLista(Lista lista, boolean seguirPadrao, boolean ativa, java.util.Date data1, String peso1,  java.util.Date data2, String peso2, java.util.Date data3, String peso3, java.util.Date data4, String peso4, java.util.Date data5, String peso5, java.util.Date data6, String peso6) {
	this.listas.addElement(lista);
	this.listas_seguirpadrao.addElement(new Boolean(seguirPadrao));
	this.listas_ativa.addElement(new Boolean(ativa));
	this.listas_data1.addElement(data1);
	this.listas_peso1.addElement(peso1);
	this.listas_data2.addElement(data2);
	this.listas_peso2.addElement(peso2);
	this.listas_data3.addElement(data3);
	this.listas_peso3.addElement(peso3);
	this.listas_data4.addElement(data4);
	this.listas_peso4.addElement(peso4);
	this.listas_data5.addElement(data5);
	this.listas_peso5.addElement(peso5);
	this.listas_data6.addElement(data6);
	this.listas_peso6.addElement(peso6);
	
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
		String str1 = StringConverter.removeAcentos(this.getDisc().getNome().toUpperCase()+" "+this.getNome().toUpperCase());
		String str2 = StringConverter.removeAcentos(((Turma) o).getDisc().getNome().toUpperCase()+" "+((Turma) o).getNome().toUpperCase());
		return str1.compareTo(str2);
	}
	catch (Exception ex) {
		return -1;
	}
}
public String getCod() {
	return this.cod;
}

public java.util.Date getData1(Lista lista) {

	if (isSeguirPadrao(lista)) return lista.getData1();

	int pos = this.listas.indexOf(lista);
	if (pos == -1) return null;
	return (java.util.Date) this.listas_data1.elementAt(pos);
	
}

public java.util.Date getData2(Lista lista) {

	if (isSeguirPadrao(lista)) return lista.getData2();

	int pos = this.listas.indexOf(lista);
	if (pos == -1) return null;
	return (java.util.Date) this.listas_data2.elementAt(pos);
	
}

public java.util.Date getData3(Lista lista) {

	if (isSeguirPadrao(lista)) return lista.getData3();

	int pos = this.listas.indexOf(lista);
	if (pos == -1) return null;
	return (java.util.Date) this.listas_data3.elementAt(pos);
	
}

public java.util.Date getData4(Lista lista) {

	if (isSeguirPadrao(lista)) return lista.getData4();
	
	int pos = this.listas.indexOf(lista);
	if (pos == -1) return null;
	return (java.util.Date) this.listas_data4.elementAt(pos);
	
}

public java.util.Date getData5(Lista lista) {

	if (isSeguirPadrao(lista)) return lista.getData5();

	int pos = this.listas.indexOf(lista);
	if (pos == -1) return null;
	return (java.util.Date) this.listas_data5.elementAt(pos);
	
}

public java.util.Date getData6(Lista lista) {

	if (isSeguirPadrao(lista)) return lista.getData6();
	
	int pos = this.listas.indexOf(lista);
	if (pos == -1) return null;
	return (java.util.Date) this.listas_data6.elementAt(pos);
	
}
public String getDescricao() {
	return this.descricao;
}
public Disciplina getDisc() {
	return this.disc;
}

public Vector getListas() {
	return this.listas;
}

public Vector getListasAtivas() {
	Vector elements = new Vector();
	Lista elem;
	// percorre a lista
	for (int i = 0; i < this.listas.size(); i++) {
		elem = (Lista) this.listas.elementAt(i);
		if (this.isAtiva(elem)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;
}
public String getNome() {
	return this.nome;
}

public int getPeso1(Lista lista) throws Exception {

	if (isSeguirPadrao(lista)) return lista.getPeso1();

	int pos = this.listas.indexOf(lista);
	if (pos == -1) return 0;

	String peso = this.listas_peso1.elementAt(pos).toString();

	if (peso == null) return 0;
	
	int pesoInt;
	try {
		pesoInt = Integer.parseInt(peso);
	} catch (Exception ex) {
		throw new Exception("Problema ao converter peso: "+peso, ex);
	}

	return pesoInt;
	
}

public int getPeso2(Lista lista) {

	if (isSeguirPadrao(lista)) return lista.getPeso2();

	int pos = this.listas.indexOf(lista);
	if (pos == -1) return 0;

	String peso = this.listas_peso2.elementAt(pos).toString();

	if (peso == null) return 0;

	return Integer.parseInt(peso);
	
}

public int getPeso3(Lista lista) {

	if (isSeguirPadrao(lista)) return lista.getPeso3();

	int pos = this.listas.indexOf(lista);
	if (pos == -1) return 0;

	String peso = this.listas_peso3.elementAt(pos).toString();

	if (peso == null) return 0;

	return Integer.parseInt(peso);
	
}

public int getPeso4(Lista lista) {

	if (isSeguirPadrao(lista)) return lista.getPeso4();

	int pos = this.listas.indexOf(lista);
	if (pos == -1) return 0;

	String peso = this.listas_peso4.elementAt(pos).toString();

	if (peso == null) return 0;

	return Integer.parseInt(peso);

}

public int getPeso5(Lista lista) {

	if (isSeguirPadrao(lista)) return lista.getPeso5();

	int pos = this.listas.indexOf(lista);
	if (pos == -1) return 0;

	String peso = this.listas_peso5.elementAt(pos).toString();

	if (peso == null) return 0;

	return Integer.parseInt(peso);
	
}

public int getPeso6(Lista lista) {

	if (isSeguirPadrao(lista)) return lista.getPeso6();

	int pos = this.listas.indexOf(lista);
	if (pos == -1) return 0;

	String peso = this.listas_peso6.elementAt(pos).toString();

	if (peso == null) return 0;

	return Integer.parseInt(peso);
	
}

public boolean isAtiva(Lista lista) {

	if (isSeguirPadrao(lista)) return lista.isAtiva();

	int pos = this.listas.indexOf(lista);
	if (pos == -1) return true;
	return ((Boolean) this.listas_ativa.elementAt(pos)).booleanValue();
	
}

public boolean isSeguirPadrao(Lista lista) {

	int pos = this.listas.indexOf(lista);
	if (pos == -1) return true;
	return ((Boolean) this.listas_seguirpadrao.elementAt(pos)).booleanValue();
	
}

public void removeLista(Lista lista) {

	int pos = this.listas.indexOf(lista);
	if (pos == -1) return;
	this.listas.removeElementAt(pos);
	this.listas_seguirpadrao.removeElementAt(pos);
	this.listas_ativa.removeElementAt(pos);
	this.listas_data1.removeElementAt(pos);
	this.listas_peso1.removeElementAt(pos);
	this.listas_data2.removeElementAt(pos);
	this.listas_peso2.removeElementAt(pos);
	this.listas_data3.removeElementAt(pos);
	this.listas_peso3.removeElementAt(pos);
	this.listas_data4.removeElementAt(pos);
	this.listas_peso4.removeElementAt(pos);
	this.listas_data5.removeElementAt(pos);
	this.listas_peso5.removeElementAt(pos);
	this.listas_data6.removeElementAt(pos);
	this.listas_peso6.removeElementAt(pos);
	
}
protected void setDescricao(String descricao) {
	this.descricao = descricao;
}
protected void setDisc(Disciplina disc) {
	this.disc = disc;
}

public void setLista(Lista lista, boolean seguirPadrao, boolean ativa, java.util.Date data1, String peso1, java.util.Date data2, String peso2, java.util.Date data3, String peso3, java.util.Date data4, String peso4, java.util.Date data5, String peso5, java.util.Date data6, String peso6) {

	int pos = this.listas.indexOf(lista);
	if (pos == -1) return;
	this.listas_seguirpadrao.setElementAt(new Boolean(seguirPadrao),pos);
	this.listas_ativa.setElementAt(new Boolean (ativa), pos);
	this.listas_data1.setElementAt(data1,pos);
	this.listas_peso1.setElementAt(peso1,pos);
	this.listas_data2.setElementAt(data2,pos);
	this.listas_peso2.setElementAt(peso2,pos);
	this.listas_data3.setElementAt(data3,pos);
	this.listas_peso3.setElementAt(peso3,pos);
	this.listas_data4.setElementAt(data4,pos);
	this.listas_peso4.setElementAt(peso4,pos);
	this.listas_data5.setElementAt(data5,pos);
	this.listas_peso5.setElementAt(peso5,pos);
	this.listas_data6.setElementAt(data6,pos);
	this.listas_peso6.setElementAt(peso6,pos);
	
}

protected void setListas(Vector listas, Vector listas_seguiPadrao, Vector listas_ativa, Vector listas_data1, Vector listas_peso1, Vector listas_data2, Vector listas_peso2, Vector listas_data3, Vector listas_peso3, Vector listas_data4, Vector listas_peso4, Vector listas_data5, Vector listas_peso5, Vector listas_data6, Vector listas_peso6) {
	this.listas = listas;
	this.listas_seguirpadrao = listas_seguiPadrao;
	this.listas_ativa = listas_ativa;
	this.listas_data1 = listas_data1;
	this.listas_peso1 = listas_peso1;
	this.listas_data2 = listas_data2;
	this.listas_peso2 = listas_peso2;
	this.listas_data3 = listas_data3;
	this.listas_peso3 = listas_peso3;
	this.listas_data4 = listas_data4;
	this.listas_peso4 = listas_peso4;
	this.listas_data5 = listas_data5;
	this.listas_peso5 = listas_peso5;
	this.listas_data6 = listas_data6;
	this.listas_peso6 = listas_peso6;

}
protected void setNome(String nome) {
	this.nome = nome;
}
}
