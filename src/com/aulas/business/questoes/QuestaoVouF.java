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

public class QuestaoVouF extends Questao {

	private final static String idTipoQuestao = "V ou F"; // usado no bd
	private final static String descTipoQuestao = "V ou F"; // usada na interface
public QuestaoVouF(String cod, String enunciado, String criterios) throws Exception {
	super(cod, enunciado, criterios, getIdTipoQuestao());
}
public boolean comparaDuasRespostas(String resp1, String resp2) throws java.lang.Exception {
	return resp1.equals(resp2);
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
	str += "<TR valign=top><TD><span class=AreaConteudoFormularioTexto>Resposta Correta: &nbsp; </span>";
	str += "<span class=AreaConteudoFormularioTexto>"+form.radioButton("respostaCorretaVouF","V",true)+" V &nbsp; "+form.radioButton("respostaCorretaVouF","F",false)+" F"+"</span></TD></TR>";
	str += "<TR><TD colspan=2><span class=AreaConteudoFormularioTexto>Comentário para a resposta errada: "+form.textboxTexto("coment","",30,0)+"</span></TD></TR></TABLE>";
	return str;
	
}
public String htmlParaImpressao() throws Exception {
	return "<B>"+this.getEnunciado()+" (V ou F)?</B>";
}
public String htmlParaResolucao(String resp, Formulario form, ListaGerada listaGerada) throws Exception {
	String str = "<B>"+this.getEnunciado()+"</B><BR>";
	str += form.radioButton("quest"+this.getCod(),"",(resp==null))+" não sei &nbsp; ";
	str += form.radioButton("quest"+this.getCod(),"V",((resp!=null)&&resp.equals("V")))+" V &nbsp; ";
	str += form.radioButton("quest"+this.getCod(),"F",((resp!=null)&&resp.equals("F")))+" F";
	str += "<BR>";

	return str;	
	
}
public String leRespostaFromHtmlParameters(Hashtable parametros, ListaGerada listaGerada) throws Exception {
	
	String resp = (String) parametros.get("quest"+this.getCod());
	if (resp == null) {
		resp = "";
	}
	return resp;
	
}
public void processaDadosDeCadastroDeNovaQuestao(javax.servlet.http.HttpServletRequest request) throws java.lang.Exception {
	
	String resp = (String) request.getParameter("respostaCorretaVouF");
	String coment = (String) request.getParameter("coment");

	if (resp == null) return;
	resp = trataResposta(resp);
	if (resp.equals("")) return;
	
	Resposta_ger respger = new Resposta_ger();
	if (resp.equals("V")) {
		respger.inclui("V","",100,0,this,false);
		respger.inclui("F",coment,0,0,this,false);
	} else {
		respger.inclui("F","",100,0,this,false);
		respger.inclui("V",coment,0,0,this,false);
	}

}
public String trataResposta(String resposta) throws Exception {
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
