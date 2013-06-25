package com.aulas.business;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 06/02/2003 10:07:33 -- *
 * -- Gerador versão alpha                                     *
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

public class Lista implements Comparable {
	private String cod;
	private String nome;
	private int numMinAlunosPorGrupo;
	private int numMaxAlunosPorGrupo;
	private boolean seguirOrdemQuestoes;
	private boolean ativa;
	private boolean autoCorrection;
	private boolean permitirTestar;
	private String enunciado;
	private java.util.Date data1;
	private int peso1;
	private java.util.Date data2;
	private int peso2;
	private java.util.Date data3;
	private int peso3;
	private java.util.Date data4;
	private int peso4;
	private java.util.Date data5;
	private int peso5;
	private java.util.Date data6;
	private int peso6;
	private Disciplina disc;


	protected Lista(String cod, String nome, String enunciado,
			int numMinAlunosPorGrupo, int numMaxAlunosPorGrupo,
			boolean autoCorrection, boolean seguirOrdemQuestoes, boolean permitirTestar, boolean ativa, 
			java.util.Date data1, int peso1, java.util.Date data2, int peso2,
			java.util.Date data3, int peso3, java.util.Date data4, int peso4,
			java.util.Date data5, int peso5, java.util.Date data6, int peso6,
			Disciplina disc) throws Exception {

		this.cod = cod;
		this.nome = nome;
		this.enunciado = enunciado;
		this.numMinAlunosPorGrupo = numMinAlunosPorGrupo;
		this.numMaxAlunosPorGrupo = numMaxAlunosPorGrupo;
		this.seguirOrdemQuestoes = seguirOrdemQuestoes;
		this.autoCorrection = autoCorrection;
		this.permitirTestar = permitirTestar;
		this.ativa = ativa;
		this.data1 = data1;
		this.peso1 = peso1;
		this.data2 = data2;
		this.peso2 = peso2;
		this.data3 = data3;
		this.peso3 = peso3;
		this.data4 = data4;
		this.peso4 = peso4;
		this.data5 = data5;
		this.peso5 = peso5;
		this.data6 = data6;
		this.peso6 = peso6;
		this.disc = disc;
	}

	/**
	 * Compares this object with the specified object for order. Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.
	 * <p>
	 * 
	 * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
	 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>. (This
	 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
	 * <tt>y.compareTo(x)</tt> throws an exception.)
	 * <p>
	 * 
	 * The implementor must also ensure that the relation is transitive:
	 * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
	 * <tt>x.compareTo(z)&gt;0</tt>.
	 * <p>
	 * 
	 * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
	 * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for all
	 * <tt>z</tt>.
	 * <p>
	 * 
	 * It is strongly recommended, but <i>not</i> strictly required that
	 * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>. Generally speaking, any
	 * class that implements the <tt>Comparable</tt> interface and violates this
	 * condition should clearly indicate this fact. The recommended language is
	 * "Note: this class has a natural ordering that is inconsistent with
	 * equals."
	 * 
	 * @param o
	 *            the Object to be compared.
	 * @return a negative integer, zero, or a positive integer as this object is
	 *         less than, equal to, or greater than the specified object.
	 * 
	 * @throws ClassCastException
	 *             if the specified object's type prevents it from being
	 *             compared to this Object.
	 */
	public int compareTo(java.lang.Object o) {

		try {

			String str1 = StringConverter.removeAcentos(this.getNome()
					.toUpperCase());
			String str2 = StringConverter.removeAcentos(((Lista) o).getNome()
					.toUpperCase());
			return str1.compareTo(str2);
		} catch (Exception ex) {
			return -1;
		}

	}

	public String getCod() {
		return this.cod;
	}

	public java.util.Date getData1() {
		return this.data1;
	}

	public java.util.Date getData2() {
		return this.data2;
	}

	public java.util.Date getData3() {
		return this.data3;
	}

	public java.util.Date getData4() {
		return this.data4;
	}

	public java.util.Date getData5() {
		return this.data5;
	}

	public java.util.Date getData6() {
		return this.data6;
	}

	public Disciplina getDisc() {
		return this.disc;
	}

	public String getEnunciado() {
		return this.enunciado;
	}

	public String getNome() {
		return this.nome;
	}

	public int getNumMaxAlunosPorGrupo() {
		return this.numMaxAlunosPorGrupo;
	}

	public int getNumMinAlunosPorGrupo() {
		return this.numMinAlunosPorGrupo;
	}

	public int getPeso1() {
		return this.peso1;
	}

	public int getPeso2() {
		return this.peso2;
	}

	public int getPeso3() {
		return this.peso3;
	}

	public int getPeso4() {
		return this.peso4;
	}

	public int getPeso5() {
		return this.peso5;
	}

	public int getPeso6() {
		return this.peso6;
	}

	public int getPesoQuestao(Questao questao) throws Exception {

		Agrupamento_ger agrupger = new Agrupamento_ger();
		Enumeration agrups = agrupger.getElements(this).elements();
		while (agrups.hasMoreElements()) {
			Agrupamento agrup = (Agrupamento) agrups.nextElement();
			Vector questoes = agrup.getQuestoes();
			int pos = questoes.indexOf(questao);
			if (pos != -1) {
				return Integer.parseInt(agrup.getPesosQuestoes().elementAt(pos)
						.toString());
			}
		}
		return 0;

	}

	public boolean isAtiva() {
		return this.ativa;
	}

	public boolean isAutoCorrection() {
		return this.autoCorrection;
	}

	public boolean isPrazoExpirou(Turma turma) throws Exception {

		Date dataAtual = new Date();

		Lista lista = this;

		if ((turma.getData1(lista) == null)
				|| (Data
						.verificaPrazoSemHoras(turma.getData1(lista), dataAtual))) {
			// prazo1 pode ser null (sem prazo)
			return false;
		} else if ((turma.getData2(lista) != null)
				&& (Data
						.verificaPrazoSemHoras(turma.getData2(lista), dataAtual))) {
			return false;
		} else if ((turma.getData3(lista) != null)
				&& (Data
						.verificaPrazoSemHoras(turma.getData3(lista), dataAtual))) {
			return false;
		} else if ((turma.getData4(lista) != null)
				&& (Data
						.verificaPrazoSemHoras(turma.getData4(lista), dataAtual))) {
			return false;
		} else if ((turma.getData5(lista) != null)
				&& (Data
						.verificaPrazoSemHoras(turma.getData5(lista), dataAtual))) {
			return false;
		} else if ((turma.getData6(lista) != null)
				&& (Data
						.verificaPrazoSemHoras(turma.getData6(lista), dataAtual))) {
			return false;
		}

		return true;

	}

	public boolean isSeguirOrdemQuestoes() {
		return this.seguirOrdemQuestoes;
	}

	protected void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	protected void setAutoCorrection(boolean newAutoCorrection) {
		this.autoCorrection = newAutoCorrection;
	}

	protected void setData1(java.util.Date data1) {
		this.data1 = data1;
	}

	protected void setData2(java.util.Date data2) {
		this.data2 = data2;
	}

	protected void setData3(java.util.Date data3) {
		this.data3 = data3;
	}

	protected void setData4(java.util.Date data4) {
		this.data4 = data4;
	}

	protected void setData5(java.util.Date data5) {
		this.data5 = data5;
	}

	protected void setData6(java.util.Date data6) {
		this.data6 = data6;
	}

	protected void setDisc(Disciplina disc) {
		this.disc = disc;
	}

	protected void setEnunciado(String newEnunciado) {
		this.enunciado = newEnunciado;
	}

	protected void setNome(String nome) {
		this.nome = nome;
	}

	protected void setNumMaxAlunosPorGrupo(int numMaxAlunosPorGrupo) {
		this.numMaxAlunosPorGrupo = numMaxAlunosPorGrupo;
	}

	protected void setNumMinAlunosPorGrupo(int numMinAlunosPorGrupo) {
		this.numMinAlunosPorGrupo = numMinAlunosPorGrupo;
	}

	protected void setPeso1(int peso1) {
		this.peso1 = peso1;
	}

	protected void setPeso2(int peso2) {
		this.peso2 = peso2;
	}

	protected void setPeso3(int peso3) {
		this.peso3 = peso3;
	}

	protected void setPeso4(int peso4) {
		this.peso4 = peso4;
	}

	protected void setPeso5(int peso5) {
		this.peso5 = peso5;
	}

	protected void setPeso6(int peso6) {
		this.peso6 = peso6;
	}

	protected void setSeguirOrdemQuestoes(boolean seguirOrdemQuestoes) {
		this.seguirOrdemQuestoes = seguirOrdemQuestoes;
	}
	
	public boolean isPermitirTestar() {
		return permitirTestar;
	}

	public void setPermitirTestar(boolean permitirTestar) {
		this.permitirTestar = permitirTestar;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public int contaOcorrenciasDeQuestaoEmUmaTurma(Questao questao, Turma turma) throws Exception {

		int count = 0;

		// recupera listas geradas desta lista que contem a questao
		ListaGerada_ger listageradager = new ListaGerada_ger();
		Vector listasGeradas = listageradager.getElements(this, questao);

		// ve quais listas geradas sao da turma pedida
		Enumeration enumeration = listasGeradas.elements();
		while (enumeration.hasMoreElements()) {
			ListaGerada lg = (ListaGerada) enumeration.nextElement();
			if (lg != null && lg.getTurma() != null && lg.getTurma().equals(turma)) {
				count++;
			}
		}

		return count;

	}
}
