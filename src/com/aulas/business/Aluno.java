package com.aulas.business;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 12/02/2003 17:29:30 -- *
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

public class Aluno implements Comparable {
	private String cod;
	private String nome;
	private String email;
	private String numeroMatricula;
	private String curso;
	private String dataNascimento;
	private String sexo;
	private String login;
	private String senha;
	private Vector turmas;

protected Aluno(String cod, String nome, String email, String numeroMatricula, String curso, String dataNascimento, String sexo, String login, String senha, Vector turmas) throws Exception {

	this.cod = cod;
	this.nome = nome;
	this.email = email;
	this.numeroMatricula = numeroMatricula;
	this.curso = curso;
	this.dataNascimento = dataNascimento;
	this.sexo = sexo;
	this.login = login;
	this.senha = senha;
	this.turmas = turmas;

}

protected void addTurma(Turma turma) {
	this.turmas.addElement(turma);
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
		String str2 = StringConverter.removeAcentos(((Aluno) o).getNome().toUpperCase());
		return str1.compareTo(str2);
	}
	catch (Exception ex) {
		return -1;
	}
}
public String getCod() {
	return this.cod;
}
public String getCurso() {
	return this.curso;
}
public String getDataNascimento() {
	return this.dataNascimento;
}
public String getEmail() {
	return this.email;
}
public String getLogin() {
	return this.login;
}
public String getNome() {
	return this.nome;
}
public String getNumeroMatricula() {
	return this.numeroMatricula;
}



public String getSenha() {
	return this.senha;
}
public String getSexo() {
	return this.sexo;
}

public Turma getTurma(Disciplina disc) throws Exception {


	Enumeration turmas = this.turmas.elements();
	while (turmas.hasMoreElements()) {
		Turma t = (Turma) turmas.nextElement();
		if (t.getDisc().equals(disc)) {
			return t;
		}
	}

	return null;

}

public Vector getTurmas() {
	return this.turmas;
}

protected void removeTurma(Turma turma) {
	int pos = this.turmas.indexOf(turma);
	if (pos == -1) return;
	this.turmas.removeElementAt(pos);
}

protected void setCurso(String curso) {
	this.curso = curso;
}


protected void setDataNascimento(String dataNascimento) {
	this.dataNascimento = dataNascimento;
}


protected void setEmail(String email) {
	this.email = email;
}
protected void setLogin(String login) {
	this.login = login;
}
protected void setNome(String nome) {
	this.nome = nome;
}

protected void setNumeroMatricula(String numeroMatricula) {
	this.numeroMatricula = numeroMatricula;
}
protected void setSenha(String senha) {
	this.senha = senha;
}
protected void setSexo(String sexo) {
	this.sexo = sexo;
}

protected void setTurmas(Vector newTurmas) {
	this.turmas = newTurmas;
}
}
