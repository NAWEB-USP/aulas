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
import com.aulas.business.*;

import java.util.*;

public class QuestaoTexto extends Questao {

	private final static String idTipoQuestao = "QuestaoTexto"; // usado no bd
	private final static String descTipoQuestao = "Texto Discursivo"; // usada na interface

    public QuestaoTexto(String cod, String enunciado, String criterios) throws Exception {
        super(cod, enunciado, criterios, getIdTipoQuestao());
    }
    
public boolean comparaDuasRespostas(String resp1, String resp2) throws java.lang.Exception {

	// --- prepara respostas para comparacao ---
	
    // From DataBase Notation
    String temp1 = StringConverter.fromDataBaseNotation(resp1);
    String temp2 = StringConverter.fromDataBaseNotation(resp2);

    // lowercase
    temp1 = temp1.toLowerCase();
    temp2 = temp2.toLowerCase();

	// sem acentos
	temp1 = StringConverter.removeAcentos(temp1);
	temp2 = StringConverter.removeAcentos(temp2);

	// corrige parenteses
	temp1 = StringConverter.replace(temp1,") (",")(");
	temp1 = StringConverter.replace(temp1,"( ","(");
	temp1 = StringConverter.replace(temp1," )",")");
	temp2 = StringConverter.replace(temp2,") (",")(");
	temp2 = StringConverter.replace(temp2,"( ","(");
	temp2 = StringConverter.replace(temp2," )",")");
	
	return temp1.equals(temp2);
}
public String formataRespostaParaExibicao(PaginaHTML pagina, String resposta) {
	return resposta;
}
public static final String getDescTipoQuestao() {
	return descTipoQuestao;
}
public static final String getIdTipoQuestao() {
	return idTipoQuestao;
}
public static String htmlFormCadastroNovaQuestao(Formulario form) throws Exception {
	
	String str = "<table class=AreaConteudoFormularioFundo border=0 cellpadding=0 cellspacing=0>";
	str += "<TR valign=top><TD><span class=AreaConteudoFormularioTexto>Resposta Correta: &nbsp; </span></TD>";
	str += "<TD>"+form.textArea("respostaCorreta","", 4, 45)+"</TD></TR></TABLE>";
	return str;
	
}
public String htmlParaImpressao() throws Exception {
	return "<B>"+StringConverter.replace(this.getEnunciado(),"\n","<BR>")+"</B>";
}
public String htmlParaResolucao(String resp, Formulario form, ListaGerada listaGerada) throws Exception {
	String str = "<B>"+this.getEnunciado()+"</B><BR>";
	if (resp == null) {
		str += form.textArea("quest"+this.getCod(),"",3,60);
	} else {
		str += form.textArea("quest"+this.getCod(),resp,3,60);
	}
	return str;	
	
}
public String leRespostaFromHtmlParameters(Hashtable parametros, ListaGerada listaGerada) throws Exception {
	
	String resp = (String) parametros.get("quest"+this.getCod());
	if (resp == null) {
		resp = "";
	}
	return trataResposta(resp);
	
}
public void processaDadosDeCadastroDeNovaQuestao(javax.servlet.http.HttpServletRequest request) throws java.lang.Exception {

	String resp = (String) request.getParameter("respostaCorreta");
	if (resp == null) return;
	resp = trataResposta(resp);
	if (resp.equals("")) return;
	
	Resposta_ger respger = new Resposta_ger();
	respger.inclui(resp,"",100,0,this,false);
		
}
public String trataResposta(String resposta) throws Exception {

	if (resposta == null) return "";

	// remove tabs da resposta
	resposta = StringConverter.replace(resposta,"\t"," ");
	
	// remove espacos duplos
	while (resposta.indexOf("  ") != -1) {
		resposta = StringConverter.replace(resposta,"  "," ");
	}

	// remove espacos do inicio e do final
	resposta.trim();

	// Corrige posicionamento da virgula
	resposta = StringConverter.replace(resposta," ,",",");
	resposta = StringConverter.replace(resposta,",",", ");

	// Corrige posicionamento dos pontos
	//resposta = StringConverter.replace(resposta," .",".");
	//resposta = StringConverter.replace(resposta,".",". ");

	// remove espacos duplos
	resposta = StringConverter.replace(resposta,"  "," ");
	
	// remove enter no inicio e no final da string
	while (resposta.startsWith("\n") || resposta.startsWith("\r") || resposta.startsWith(" ")) {
		resposta = resposta.substring(1);
	}
	while (resposta.endsWith("\n") || resposta.endsWith("\r") || resposta.endsWith(" ")) {
		resposta = resposta.substring(0,resposta.length()-1);
	}

	
	//resposta = StringConverter.replace(resposta,"  "," ");

	
	//while 

	
	return resposta;
}

@Override
public Resposta processaResposta(String respNova) {
	return null;
}
@Override
public boolean isReusaResposta() {
	return true;
}

}
