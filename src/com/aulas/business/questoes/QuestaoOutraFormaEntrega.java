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
import org.apache.commons.fileupload.*;
import java.util.*;
import java.io.*;
import com.aulas.business.*;

public class QuestaoOutraFormaEntrega extends Questao {

	private final static String idTipoQuestao = "OutraForma"; // usado no bd
	private final static String descTipoQuestao = "Outra Forma de Entrega"; // usada na interface
public QuestaoOutraFormaEntrega(String cod, String enunciado, String criterios) throws Exception {
	super(cod, enunciado, criterios, getIdTipoQuestao());
}
public boolean comparaDuasRespostas(String resp1, String resp2) throws java.lang.Exception {

	return resp1.equals(resp2);
	
}
public String formataRespostaParaExibicao(PaginaHTML pagina, String resposta) throws Exception {

	return resposta;
	
}
public static final String getDescTipoQuestao() {
	return descTipoQuestao;
}
public static final String getIdTipoQuestao() {
	return idTipoQuestao;
}
public static String htmlFormCadastroNovaQuestao(Formulario form) throws Exception {
	return "";
}
public String htmlParaImpressao() throws Exception {
	return "<B> "+StringConverter.replace(this.getEnunciado(),"\n","<BR>")+"</B>";
}
public String htmlParaResolucao(String resp, Formulario form, ListaGerada listaGerada) throws Exception {

	String str = "<B>"+this.getEnunciado()+"</B><BR>";
	return str;	

}
public String leRespostaFromHtmlParameters(Hashtable parametros, ListaGerada listaGerada) throws Exception {

	return "-";

}
public void processaDadosDeCadastroDeNovaQuestao(javax.servlet.http.HttpServletRequest request) throws java.lang.Exception {

	return;
		
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
