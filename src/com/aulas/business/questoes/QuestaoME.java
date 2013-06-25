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
import java.util.*;
import com.aulas.business.*;

public class QuestaoME extends Questao {

	private final static String idTipoQuestao = "Múltipla Escolha"; // usado no bd
	private final static String descTipoQuestao = "Múltipla Escolha"; // usada na interface
public QuestaoME(String cod, String enunciado, String criterios) throws Exception {
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
	
	String str = "<table class=AreaConteudoFormularioFundo border=0 cellpadding=1 cellspacing=1>";
	str += "<TR valign=top><TD colspan=2><span class=AreaConteudoFormularioTexto><B>Respostas:</B></span></TD></TR>";

	str += "<TR valign=top><TD>1. "+form.textboxTexto("resp1","",48,0)+"</TD>";
	str += "<TD><span class=AreaConteudoFormularioTexto> &nbsp; Pontuação: "+form.textboxNumero("pont1","0",4,4)+"%</span></TD></TR>";
	str += "<TR valign=top><TD colspan = 2>Comentário: "+form.textboxTexto("coment1","",33,0)+"</TD></TR>";

	str += "<TR valign=top><TD>2."+form.textboxTexto("resp2","",48,0)+"</TD>";
	str += "<TD><span class=AreaConteudoFormularioTexto> &nbsp; Pontuação: "+form.textboxNumero("pont2","0",4,4)+"%</span></TD></TR>";
	str += "<TR valign=top><TD colspan = 2>Comentário: "+form.textboxTexto("coment2","",33,0)+"</TD></TR>";

	str += "<TR valign=top><TD>3. "+form.textboxTexto("resp3","",48,0)+"</TD>";
	str += "<TD><span class=AreaConteudoFormularioTexto> &nbsp; Pontuação: "+form.textboxNumero("pont3","0",4,4)+"%</span></TD></TR>";
	str += "<TR valign=top><TD colspan = 2>Comentário: "+form.textboxTexto("coment3","",33,0)+"</TD></TR>";
	
	str += "<TR valign=top><TD>4. "+form.textboxTexto("resp4","",48,0)+"</TD>";
	str += "<TD><span class=AreaConteudoFormularioTexto> &nbsp; Pontuação: "+form.textboxNumero("pont4","0",4,4)+"%</span></TD></TR>";
	str += "<TR valign=top><TD colspan = 2>Comentário: "+form.textboxTexto("coment4","",33,0)+"</TD></TR>";
	
	str += "<TR valign=top><TD>5. "+form.textboxTexto("resp5","",48,0)+"</TD>";
	str += "<TD><span class=AreaConteudoFormularioTexto> &nbsp; Pontuação: "+form.textboxNumero("pont5","0",4,4)+"%</span></TD></TR>";
	str += "<TR valign=top><TD colspan = 2>Comentário: "+form.textboxTexto("coment5","",33,0)+"</TD></TR>";
	
	str += "<TR valign=top><TD>6. "+form.textboxTexto("resp6","",48,0)+"</TD>";
	str += "<TD><span class=AreaConteudoFormularioTexto> &nbsp; Pontuação: "+form.textboxNumero("pont6","0",4,4)+"%</span></TD></TR>";
	str += "<TR valign=top><TD colspan = 2>Comentário: "+form.textboxTexto("coment6","",33,0)+"</TD></TR>";

	str += "</TABLE>";
	return str;
	
}
public String htmlParaImpressao() throws Exception {
	
	String str = "<B>"+this.getEnunciado()+"</B><BR>";
	Resposta_ger respger = new Resposta_ger();
	Enumeration resps = respger.getElements(this).elements();
	char letra = 'a';
	while (resps.hasMoreElements()) {
		Resposta resp = (Resposta) resps.nextElement();
		str += letra + ") " + resp.getResposta()+"<BR>";
		letra++;
	}
	return str;
	
}
public String htmlParaResolucao(String resp, Formulario form, ListaGerada listaGerada) throws Exception {


	String str = "<B>"+this.getEnunciado()+"</B><BR>";
	Resposta_ger respger = new Resposta_ger();
	Enumeration resps = respger.getElements(this).elements();
	while (resps.hasMoreElements()) {
		Resposta r = (Resposta) resps.nextElement();
		str += form.radioButton("quest"+this.getCod(),r.getCod(),((resp!=null)&&resp.equals(r.getResposta())))+ " " + r.getResposta()+"<BR>";
	}
	return str;

}
public String leRespostaFromHtmlParameters(Hashtable parametros, ListaGerada listaGerada) throws Exception {
	
	String codResp = (String) parametros.get("quest"+this.getCod());
	if (codResp == null) {
		return "";
	}

	Resposta_ger respger = new Resposta_ger();
	
	return respger.getElementByCod(codResp).getResposta();
	
}
public void processaDadosDeCadastroDeNovaQuestao(javax.servlet.http.HttpServletRequest request) throws java.lang.Exception {
	
	Resposta_ger respger = new Resposta_ger();

	String resp1 = (String) request.getParameter("resp1");
	String pont1 = (String) request.getParameter("pont1");
	String coment1 = (String) request.getParameter("coment1");
	if (resp1 == null) return;
	resp1 = trataResposta(resp1);
	if (resp1.equals("")) return;
	respger.inclui(resp1,coment1,Integer.parseInt(pont1),0,this,false);

	String resp2 = (String) request.getParameter("resp2");
	String pont2 = (String) request.getParameter("pont2");
	String coment2 = (String) request.getParameter("coment2");
	if (resp2 == null) return;
	resp2 = trataResposta(resp2);
	if (resp2.equals("")) return;
	respger.inclui(resp2,coment2,Integer.parseInt(pont2),0,this,false);

	String resp3 = (String) request.getParameter("resp3");
	String pont3 = (String) request.getParameter("pont3");
	String coment3 = (String) request.getParameter("coment3");
	if (resp3 == null) return;
	resp3 = trataResposta(resp3);
	if (resp3.equals("")) return;
	respger.inclui(resp3,coment3,Integer.parseInt(pont3),0,this,false);

	String resp4 = (String) request.getParameter("resp4");
	String pont4 = (String) request.getParameter("pont4");
	String coment4 = (String) request.getParameter("coment4");
	if (resp4 == null) return;
	resp4 = trataResposta(resp4);
	if (resp4.equals("")) return;
	respger.inclui(resp4,coment4,Integer.parseInt(pont4),0,this,false);

	String resp5 = (String) request.getParameter("resp5");
	String pont5 = (String) request.getParameter("pont5");
	String coment5 = (String) request.getParameter("coment5");
	if (resp5 == null) return;
	resp5 = trataResposta(resp5);
	if (resp5.equals("")) return;
	respger.inclui(resp5,coment5,Integer.parseInt(pont5),0,this,false);

	String resp6 = (String) request.getParameter("resp6");
	String pont6 = (String) request.getParameter("pont6");
	String coment6 = (String) request.getParameter("coment6");
	if (resp6 == null) return;
	resp6 = trataResposta(resp6);
	if (resp6.equals("")) return;
	respger.inclui(resp6,coment6,Integer.parseInt(pont6),0,this,false);

		
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
