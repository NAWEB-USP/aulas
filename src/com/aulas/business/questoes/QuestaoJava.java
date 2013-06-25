package com.aulas.business.questoes;

/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *

 Histórico de Versões
 --------------------
 versão	Autor		Modificação
 ------	-----		-----------
 1.0	Gerosa 		Criação da classe
 * 
 */

import com.aulas.objinterface.*;
import com.aulas.util.*;
import java.sql.*;
import java.util.*;
import com.aulas.business.*;
import com.aulas.business.questoes.javatester.JavaTester;

public class QuestaoJava extends Questao {

	private final static String idTipoQuestao = "QuestaoJava"; // usado no bd
	private final static String descTipoQuestao = "Questão de Java"; // usada na
																	// interface

	public QuestaoJava(String cod, String enunciado, String criterios)
			throws Exception {
		super(cod, enunciado, criterios, getIdTipoQuestao());

	}

	public boolean comparaDuasRespostas(String resp1, String resp2)
			throws java.lang.Exception {

		return resp1.equals(resp2);

	}

	public String formataRespostaParaExibicao(PaginaHTML pagina, String resposta) {
		return "<CODE>"+resposta+"</CODE>";
	}

	public static final String getDescTipoQuestao() {
		return descTipoQuestao;
	}

	public static final String getIdTipoQuestao() {
		return idTipoQuestao;
	}

	public static String htmlFormCadastroNovaQuestao(Formulario form)
			throws Exception {
		String str = "<table class=AreaConteudoFormularioFundo border=0 cellpadding=0 cellspacing=0>";
		str += "<TR valign=top><TD><span class=AreaConteudoFormularioTexto>Resposta Correta: &nbsp; </span></TD>";
		str += "<TD>" + form.textArea("respostaCorreta", "", 4, 45)
				+ "</TD></TR></TABLE>";
		return str;
	}

	public String htmlParaImpressao() throws Exception {
		return "<B>"
				+ StringConverter.replace(this.getEnunciado(), "\n", "<BR>")
				+ "</B>";
	}

	public String htmlParaResolucao(String resp, Formulario form,
			ListaGerada listaGerada) throws Exception {
		String str = "<B>" + this.getEnunciado() + "</B><BR>";
		if (resp == null) {
			str += form.textArea("quest" + this.getCod(), "", 10, 80);
		} else {
			str += form.textArea("quest" + this.getCod(), resp, 10, 80);
		}
		return str;

	}

	public String leRespostaFromHtmlParameters(Hashtable parametros,
			ListaGerada listaGerada) throws Exception {

		String resp = (String) parametros.get("quest" + this.getCod());
		if (resp == null) {
			resp = "";
		}
		return trataResposta(resp);

	}

	public void processaDadosDeCadastroDeNovaQuestao(
			javax.servlet.http.HttpServletRequest request)
			throws java.lang.Exception {

		String resp = (String) request.getParameter("respostaCorreta");
		if (resp == null)
			return;
		resp = trataResposta(resp);
		if (resp.equals(""))
			return;

		Resposta_ger respger = new Resposta_ger();
		respger.inclui(resp, "", 100, 0, this, false);

	}

	public String trataResposta(String resposta) throws Exception {

		if (resposta == null)
			return "";

		// remove espaços do início e do final
		resposta.trim();

		// remove enter no início e no final da string
		while (resposta.startsWith("\n") || resposta.startsWith("\r")
				|| resposta.startsWith(" ")) {
			resposta = resposta.substring(1);
		}
		while (resposta.endsWith("\n") || resposta.endsWith("\r")
				|| resposta.endsWith(" ")) {
			resposta = resposta.substring(0, resposta.length() - 1);
		}
		
		return resposta;

	}
	
	@Override
	public Resposta processaResposta(String respNova) throws Exception {
		Resposta tempResp = new Resposta("","","",0,0,null,false);
		JavaTester tester = new JavaTester();
		String result = tester.executeTest(respNova, StringConverter.fromDataBaseNotation(this.getCriterios()));
		if (result.startsWith("Executou corretamente.")) tempResp.setPontuacao(100);
		tempResp.setComentario(result);
		return tempResp;
	}
	@Override
	public boolean isReusaResposta() {
		return false;
	}

}
