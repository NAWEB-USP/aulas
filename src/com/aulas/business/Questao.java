package com.aulas.business;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 11/02/2003 00:48:55 -- *
 * -- Gerador versão 1.0                                       *
 * ----------------------------------------------------------- *

 Histórico de Versões
 --------------------
 versão	Autor		Modificação
 ------	-----		-----------
 1.0	Gerosa 		Geração automática da classe
 * 
 */

// OBS: Al�m dos m�todos abstratos, a subclasse dever� implementar
// o m�todo public static String htmlFormCadastroNovaQuestao(Formulario form) throws Exception
import java.util.*;
import com.aulas.objinterface.*;

public abstract class Questao {
	private String cod;
	private String enunciado;
	private String criterios;
	private String tipo;

	protected Questao(String cod, String enunciado, String criterios, String tipo) throws Exception {
		this.cod = cod;
		this.enunciado = enunciado;
		this.criterios = criterios;
		this.tipo = tipo;
	}

	public abstract boolean comparaDuasRespostas(String resp1, String resp2)
			throws Exception;

	public abstract String formataRespostaParaExibicao(PaginaHTML pagina,
			String resposta) throws Exception;

	public String getCod() {
		return this.cod;
	}

	public String getCriterios() {
		return this.criterios;
	}

	public String getEnunciado() {
		return this.enunciado;
	}

	public String getTipo() {
		return this.tipo;
	}

	public abstract String htmlParaImpressao() throws Exception;

	public abstract String htmlParaResolucao(String resp, Formulario form,
			ListaGerada listaGerada) throws Exception;

	public abstract String leRespostaFromHtmlParameters(Hashtable parametros,
			ListaGerada listaGerada) throws Exception;

	public abstract void processaDadosDeCadastroDeNovaQuestao(
			javax.servlet.http.HttpServletRequest request) throws Exception;

	protected void setCriterios(String criterios) {
		this.criterios = criterios;
	}

	protected void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public abstract String trataResposta(String resposta) throws Exception;

	public abstract Resposta processaResposta(String respNova) throws Exception;

	public abstract boolean isReusaResposta();
}
