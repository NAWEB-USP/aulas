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

public class QuestaoSubmissaoArquivo extends Questao {

	private final static String idTipoQuestao = "Submissão de Arquivo"; // usado no bd
	private final static String descTipoQuestao = "Submissão de Arquivo"; // usada na interface
public QuestaoSubmissaoArquivo(String cod, String enunciado, String criterios) throws Exception {
	super(cod, enunciado, criterios, getIdTipoQuestao());
}
public boolean comparaDuasRespostas(String resp1, String resp2) throws java.lang.Exception {

	return resp1.equals(resp2);
	
}
public String formataRespostaParaExibicao(PaginaHTML pagina, String resposta) throws Exception {

	if (resposta.equals("")) {
		return resposta;
	}
	
	// nome do arquivo
	String fileName = resposta;
    if (fileName.lastIndexOf(File.separator) != -1) {
		fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1);
    }
    
	// tamanho
    File arq = new File (resposta);
	String infoSize = "";
	if ((arq != null) && arq.exists()) {
		long size = arq.length();
		if (size / (1024*1024) >= 1) { // MB
			infoSize = " &nbsp;("+size / (1024*1024)+" MB)";
		} else if (size / (1024) >= 1) { // KB
			infoSize = " &nbsp;("+size / (1024)+" KB)";
		} else { // B
			infoSize = " &nbsp;("+size+" Bytes)";
		}
	}
	
	return "<A HREF=\"LeArquivo?"+pagina.getSessionManager().generateEncodedParameter()+"&questao="+this.getCod()+"\">"+fileName+"</A>"+infoSize;
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
	return "<B>[Submissão de arquivo] "+StringConverter.replace(this.getEnunciado(),"\n","<BR>")+"</B>";
}
public String htmlParaResolucao(String resp, Formulario form, ListaGerada listaGerada) throws Exception {

	form.changeEncTypeToMultipart();
	
	String str = "<B>"+this.getEnunciado()+"</B><BR>";
	if ((resp == null) || (resp.equals(""))) {
		str += "Arquivo de resposta: "+form.fileUpload("quest"+this.getCod());
	} else {
		// possibilitar visualizar
		str += "Novo arquivo de resposta: "+form.fileUpload("quest"+this.getCod());
		str += "<BR>Resposta Anterior: "+formataRespostaParaExibicao(form.getPagina(),resp);
	}
	return str;	
	
}
public String leRespostaFromHtmlParameters(Hashtable parametros, ListaGerada listaGerada) throws Exception {

	// Le resposta anterior
	Resposta oldResposta = listaGerada.getResposta(this);

	// Parametro vazio
	String resp = (String) parametros.get("quest"+this.getCod());
	if ((resp == null) || resp.equals("")) {
		if (oldResposta == null) {
			return "";
		} else {
			return oldResposta.getResposta();
		}
	}

	// Remove arquivo antigo se existir
	if (oldResposta != null) {
		File arquivoAnterior = new File (oldResposta.getResposta());
		arquivoAnterior.delete();
	}
		
	// Monta paths
	String oldPath = resp;
	String fileName = resp;
    if (fileName.lastIndexOf(File.separator) != -1) {
		fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1);
    }
	String newPath = (new Configuracao_ger()).getElement("RepositorioArquivos")+File.separator+"respostas"+File.separator+this.getCod()+File.separator+listaGerada.getCod()+File.separator;

	// Constroi diretorios se não existirem
	File diretorio = new File (newPath);
	diretorio.mkdirs();
	
	// Move arquivo do diretorio temporario para o repositorio
	File arqOld = new File (oldPath);
	arqOld.renameTo(new File(newPath+fileName));
	
	return trataResposta(newPath+fileName);

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
