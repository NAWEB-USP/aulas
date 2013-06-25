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

public class QuestaoSQL extends Questao {

	private final static String idTipoQuestao = "QuestaoSQL"; // usado no bd
	private final static String descTipoQuestao = "Questão de SQL"; // usada na interface
public QuestaoSQL(String cod, String enunciado, String criterios) throws Exception {
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

	// espacos
	temp1 = StringConverter.removeUnecessarySpaces(temp1);
	temp2 = StringConverter.removeUnecessarySpaces(temp2);
	
	// retira os Enters
	temp1 = StringConverter.replace(temp1,"\r","");
	temp2 = StringConverter.replace(temp2,"\r","");
	temp1 = StringConverter.replace(temp1,"\t","");
	temp2 = StringConverter.replace(temp2,"\t","");
	temp1 = StringConverter.replace(temp1,"\n"," ");
	temp2 = StringConverter.replace(temp2,"\n"," ");

	// converte aspas duplas para simples
	temp1 = StringConverter.replace(temp1,"\"","'");
	temp2 = StringConverter.replace(temp2,"\"","'");
	
	// corrige espaçamento de parenteses
	temp1 = StringConverter.replace(temp1,") (",")(");
	temp1 = StringConverter.replace(temp1,"( ","(");
	temp1 = StringConverter.replace(temp1," )",")");
	temp2 = StringConverter.replace(temp2,") (",")(");
	temp2 = StringConverter.replace(temp2,"( ","(");
	temp2 = StringConverter.replace(temp2," )",")");

	// tira espaçamento do igual
	temp1 = StringConverter.replace(temp1," =","=");
	temp1 = StringConverter.replace(temp1,"= ","=");
	temp2 = StringConverter.replace(temp2," =","=");
	temp2 = StringConverter.replace(temp2,"= ","=");
	
	// espacos
	temp1 = StringConverter.removeUnecessarySpaces(temp1);
	temp2 = StringConverter.removeUnecessarySpaces(temp2);

	// tira o ponto e vírgula no final
	while (temp1.endsWith(";")) {
		temp1 = temp1.substring(0,temp1.length()-1);
	}
	while (temp2.endsWith(";")) {
		temp2 = temp2.substring(0,temp2.length()-1);
	}

	// espacos
	temp1 = StringConverter.removeUnecessarySpaces(temp1);
	temp2 = StringConverter.removeUnecessarySpaces(temp2);

	return temp1.equalsIgnoreCase(temp2);
	
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
	
	// remove espaços duplos
	while (resposta.indexOf("  ") != -1) {
		resposta = StringConverter.replace(resposta,"  "," ");
	}

	// remove espaços do início e do final
	resposta.trim();

	// Corrige posicionamento da vírgula
	resposta = StringConverter.replace(resposta," ,",",");
	resposta = StringConverter.replace(resposta,",",", ");

	// Corrige posicionamento dos pontos
	//resposta = StringConverter.replace(resposta," .",".");
	//resposta = StringConverter.replace(resposta,".",". ");

	// remove espaços duplos
	resposta = StringConverter.replace(resposta,"  "," ");
	
	// remove enter no início e no final da string
	while (resposta.startsWith("\n") || resposta.startsWith("\r") || resposta.startsWith(" ")) {
		resposta = resposta.substring(1);
	}
	while (resposta.endsWith("\n") || resposta.endsWith("\r") || resposta.endsWith(" ")) {
		resposta = resposta.substring(0,resposta.length()-1);
	}

	
	//resposta = StringConverter.replace(resposta,"  "," ");

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
