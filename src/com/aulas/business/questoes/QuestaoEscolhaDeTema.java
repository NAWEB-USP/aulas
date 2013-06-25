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

public class QuestaoEscolhaDeTema extends Questao {

	private final static String idTipoQuestao = "Escolha de Tema"; // usado no bd
	private final static String descTipoQuestao = "Escolha de Tema"; // usada na interface
	
public QuestaoEscolhaDeTema(String cod, String enunciado, String criterios) throws Exception {
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
	str += "<TR valign=top><TD colspan=2><span class=AreaConteudoFormularioTexto><B>Temas:</B></span></TD></TR>";

	str += "<TR valign=top><TD>01. "+form.textboxTexto("tema1","",48,0)+"</TD>";
	str += "<TR valign=top><TD>02. "+form.textboxTexto("tema2","",48,0)+"</TD>";
	str += "<TR valign=top><TD>03. "+form.textboxTexto("tema3","",48,0)+"</TD>";
	str += "<TR valign=top><TD>04. "+form.textboxTexto("tema4","",48,0)+"</TD>";
	str += "<TR valign=top><TD>05. "+form.textboxTexto("tema5","",48,0)+"</TD>";
	str += "<TR valign=top><TD>06. "+form.textboxTexto("tema6","",48,0)+"</TD>";
	str += "<TR valign=top><TD>07. "+form.textboxTexto("tema7","",48,0)+"</TD>";
	str += "<TR valign=top><TD>08. "+form.textboxTexto("tema8","",48,0)+"</TD>";
	str += "<TR valign=top><TD>09. "+form.textboxTexto("tema9","",48,0)+"</TD>";
	str += "<TR valign=top><TD>10. "+form.textboxTexto("tema10","",48,0)+"</TD>";
	str += "<TR valign=top><TD>11. "+form.textboxTexto("tema11","",48,0)+"</TD>";
	str += "<TR valign=top><TD>12. "+form.textboxTexto("tema12","",48,0)+"</TD>";
	str += "<TR valign=top><TD>13. "+form.textboxTexto("tema13","",48,0)+"</TD>";
	str += "<TR valign=top><TD>14. "+form.textboxTexto("tema14","",48,0)+"</TD>";
	str += "<TR valign=top><TD>15. "+form.textboxTexto("tema15","",48,0)+"</TD>";
	str += "<TR valign=top><TD>16. "+form.textboxTexto("tema16","",48,0)+"</TD>";
	str += "<TR valign=top><TD>17. "+form.textboxTexto("tema17","",48,0)+"</TD>";
	str += "<TR valign=top><TD>18. "+form.textboxTexto("tema18","",48,0)+"</TD>";
	str += "<TR valign=top><TD>19. "+form.textboxTexto("tema19","",48,0)+"</TD>";
	str += "<TR valign=top><TD>20. "+form.textboxTexto("tema20","",48,0)+"</TD>";

	str += "</TABLE>";
	return str;
	

}
public String htmlParaImpressao() throws Exception {
	
	String str = "<B>"+this.getEnunciado()+"</B><BR>";
	Resposta_ger respger = new Resposta_ger();
	Enumeration resps = respger.getElements(this).elements();
	int num = 1;
	while (resps.hasMoreElements()) {
		Resposta resp = (Resposta) resps.nextElement();
		str += num + ") " + resp.getResposta()+"<BR>";
		num++;
	}
	return str;
	
}
public String htmlParaResolucao(String resp, Formulario form, ListaGerada listaGerada) throws Exception {

	String str = "<B>"+this.getEnunciado()+"</B><BR>";
	Resposta_ger respger = new Resposta_ger();
	// para testar se alguem da mesma turma respondeu
	ListaGerada_ger listageradager = new ListaGerada_ger ();
	Enumeration listasGeradas = listageradager.getElements(this).elements();
	Vector respostas = new Vector();
	Vector alunosAlocados = new Vector();
	while (listasGeradas.hasMoreElements()) {
		ListaGerada lg = (ListaGerada) listasGeradas.nextElement();
		if ((lg != listaGerada) && lg.getTurma().getCod().equals(listaGerada.getTurma().getCod())) {
			// se nao for o proprio aluno e for da mesma turma
			int pos = lg.getQuestoes().indexOf(this);
			if (pos != -1) {
				Resposta r = (Resposta) lg.getQuestoesRespostas().elementAt(pos);
				respostas.addElement(r);
				alunosAlocados.addElement(lg.getAlunos());
			}
		}
	}
	
	Enumeration resps = respger.getElements(this).elements();
	int num = 1;
	while (resps.hasMoreElements()) {
		Resposta r = (Resposta) resps.nextElement();
		int pos = respostas.indexOf(r);
		if (pos != -1) {
			str += num + ") " + r.getResposta() + " [alocado a ";
			Enumeration alunos = ((Vector) alunosAlocados.elementAt(pos)).elements();
			while (alunos.hasMoreElements()) {
				Aluno aluno = (Aluno) alunos.nextElement();
				str += aluno.getNome() + ", ";
			}
			str = str.substring(0,str.length()-2); // tira a ultima virgula
			str += "]<BR>";
		} else {
			str += form.radioButton("quest"+this.getCod(),r.getCod(),((resp!=null)&&resp.equals(r.getResposta())))+ " " + num + ") " + r.getResposta()+"<BR>";
		}
		num++;
	}
	return str;

}
public String leRespostaFromHtmlParameters(Hashtable parametros, ListaGerada listaGerada) throws Exception {
	
	String codResp = (String) parametros.get("quest"+this.getCod());
	if (codResp == null) {
		return "";
	}

	Resposta_ger respger = new Resposta_ger();
	
	String resposta = respger.getElementByCod(codResp).getResposta();

	// Testa se outra pessoa já escolheu esta resposta
	ListaGerada_ger listageradager = new ListaGerada_ger ();
	Enumeration listasGeradas = listageradager.getElements(this).elements();
	while (listasGeradas.hasMoreElements()) {
		ListaGerada lg = (ListaGerada) listasGeradas.nextElement();
		if ((lg != listaGerada) && lg.getTurma().getCod().equals(listaGerada.getTurma().getCod())) {
			// se nao for o proprio aluno e for da mesma turma
			int pos = lg.getQuestoes().indexOf(this);
			if (pos != -1) {
				Resposta r = (Resposta) lg.getQuestoesRespostas().elementAt(pos);
				if ((r != null) && r.getResposta().equals(resposta)) {
					throw new Exception ("Tema já escolhido.");
				}
			}
		}
	}	

	return resposta;
	
}
public void processaDadosDeCadastroDeNovaQuestao(javax.servlet.http.HttpServletRequest request) throws java.lang.Exception {
	
	Resposta_ger respger = new Resposta_ger();

	String tema;
	
	tema = (String) request.getParameter("tema1");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema2");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema3");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema4");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema5");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema6");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema7");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema8");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema9");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema10");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema11");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema12");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema13");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema14");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema15");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema16");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema17");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema18");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema19");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);

	tema = (String) request.getParameter("tema20");
	if (tema == null) return;
	tema = trataResposta(tema);
	if (tema.equals("")) return;
	respger.inclui(tema,"",-1,0,this,false);


		
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
