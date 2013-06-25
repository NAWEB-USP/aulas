package com.aulas.objinterface;

/* ----------------------------------------------- *
 *				Marco Aurélio Gerosa               *
 * ----------------------------------------------- *

 Histórico de Versões
 --------------------
 versão	Autor		Modificação
 ------	-----		-----------
 1.0	Gerosa 		Importação e adaptação da classe
 * 
 */

import com.aulas.util.*;

import org.apache.commons.fileupload.*;
 
import java.util.*;
import java.io.*;
 
public class Formulario {
	private PrintWriter out;
	private PaginaHTML pagina;

public Formulario(PaginaHTML pagina, String action) throws Exception {

	this.out = pagina.out;
	this.pagina = pagina;

	out.print ("<form name=dados method=post action=\""+action+"\">");
	//out.print ("<form name=dados method=post action=\""+action+"\" ENCTYPE=\"multipart/form-data\">");
	//out.print ("<form name=dados method=post action=\""+action+"\" ENCTYPE=\"multipart/mixed\">");

}
public String botaoSubmit(String texto) throws Exception {

	return "<INPUT TYPE=submit value=\""+texto+"\">";
	
}
public String button(String texto, String comandoJavascript) throws Exception {

	return "<INPUT TYPE=button value=\""+texto+"\" onclick=\""+comandoJavascript+"\">";
	
}

public void changeEncTypeToMultipart() {
	
	out.println("\n<SCRIPT>\ndocument.dados.encoding='multipart/form-data';\n</SCRIPT>");
	
}
public String checkbox(String name, String value, boolean checked) throws Exception {

	if (checked) {
		return "<INPUT TYPE=checkbox NAME="+name+" VALUE=\""+value+"\" checked>";
	} else {
		return "<INPUT TYPE=checkbox NAME="+name+" VALUE=\""+value+"\">";
	}
	
}
public String fileUpload(String name) throws Exception {

	return "<INPUT TYPE=file NAME="+name+" size=30>";
			
}
public void fim() throws Exception {

	out.println ("</form>");

}

public PaginaHTML getPagina() {
	
	return this.pagina;
		
}
public void hidden(String name, String value) throws Exception {

	out.println ("<INPUT TYPE=hidden NAME="+name+" value=\""+value+"\">");
	
}
public String radioButton(String name, String value, boolean checked) throws Exception {

	if (checked) {
		return "<INPUT TYPE=radio NAME="+name+" VALUE=\""+value+"\" checked>";
	} else {
		return "<INPUT TYPE=radio NAME="+name+" VALUE=\""+value+"\">";
	}
	
}
public String selectBox(String campo, Vector valores, Vector opcoes, String selected) throws Exception {

	String str = "<SELECT name=\""+campo+"\">";
	
	for (int i = 0; i < valores.size(); i++) {
		if (opcoes.elementAt(i).toString().equals(selected)) {
			str += "<OPTION value=\""+valores.elementAt(i)+"\" selected>"+opcoes.elementAt(i);
		} else {
			str += "<OPTION value=\""+valores.elementAt(i)+"\">"+opcoes.elementAt(i);
		}
	}
	
	str += "</SELECT>";
	
	return str;
		
}
public String selectBox(String campo, Vector valores, Vector opcoes, String selected, String onchange) throws Exception {

	String str = "<SELECT name=\""+campo+"\" onchange=\""+onchange+"\">";
	
	for (int i = 0; i < valores.size(); i++) {
		if (opcoes.elementAt(i).toString().equals(selected)) {
			str += "<OPTION value=\""+valores.elementAt(i)+"\" selected>"+opcoes.elementAt(i);
		} else {
			str += "<OPTION value=\""+valores.elementAt(i)+"\">"+opcoes.elementAt(i);
		}
	}
	
	str += "</SELECT>";
	
	return str;
		
}
public String textArea(String name, String value, int rows, int cols) throws Exception {

	String texto = StringConverter.replace(StringConverter.fromDataBaseNotation(value),"&","&amp;");
	texto = StringConverter.replace(StringConverter.fromDataBaseNotation(texto),"</TEXTAREA>","&lt;/TEXTAREA&gt;");
	texto = StringConverter.replace(StringConverter.fromDataBaseNotation(texto),"</textarea>","&lt;/textarea&gt;");
	texto = StringConverter.replace(StringConverter.fromDataBaseNotation(texto),"</Textarea>","&lt;/Textarea&gt;");
	texto = StringConverter.replace(StringConverter.fromDataBaseNotation(texto),"</TextArea>","&lt;/TextArea&gt;");
	
	String str = "<TEXTAREA NAME="+name+" rows="+rows+" cols="+cols+">"+texto+"</TEXTAREA>";

	return str;
	
}
public String textArea(String name, String value, int rows, int cols, String onChange) throws Exception {

	String texto = StringConverter.replace(StringConverter.fromDataBaseNotation(value),"&","&amp;");
	texto = StringConverter.replace(StringConverter.fromDataBaseNotation(texto),"</TEXTAREA>","&lt;/TEXTAREA&gt;");
	texto = StringConverter.replace(StringConverter.fromDataBaseNotation(texto),"</textarea>","&lt;/textarea&gt;");
	texto = StringConverter.replace(StringConverter.fromDataBaseNotation(texto),"</Textarea>","&lt;/Textarea&gt;");
	texto = StringConverter.replace(StringConverter.fromDataBaseNotation(texto),"</TextArea>","&lt;/TextArea&gt;");
	
	String str = "<TEXTAREA NAME="+name+" rows="+rows+" cols="+cols+" onChange=\""+onChange+"\">"+texto+"</TEXTAREA>";

	return str;
	
}

public String textArea(String name, String value, int rows, int cols, String onChange, String color) throws Exception {
	String texto = StringConverter.replace(StringConverter.fromDataBaseNotation(value),"&","&amp;");
	texto = StringConverter.replace(StringConverter.fromDataBaseNotation(texto),"</TEXTAREA>","&lt;/TEXTAREA&gt;");
	texto = StringConverter.replace(StringConverter.fromDataBaseNotation(texto),"</textarea>","&lt;/textarea&gt;");
	texto = StringConverter.replace(StringConverter.fromDataBaseNotation(texto),"</Textarea>","&lt;/Textarea&gt;");
	texto = StringConverter.replace(StringConverter.fromDataBaseNotation(texto),"</TextArea>","&lt;/TextArea&gt;");
	
	String str = "<TEXTAREA NAME="+name+" rows="+rows+" cols="+cols+" onChange=\""+onChange+"\" style=\"background-color: "+color+"\">"+texto+"</TEXTAREA>";
	return str;
}

public String textboxData(String name, Date data) throws Exception {

	String str;
	
	if (data == null) {
		str = "<INPUT NAME="+name+"d size=2 maxlength=2>/<INPUT NAME="+name+"m size=2 maxlength=2>/<INPUT NAME="+name+"a size=4 maxlength=4>";
	} else {
		str = "<INPUT NAME="+name+"d value="+Data.getDia(data)+" size=2 maxlength=2>/<INPUT NAME="+name+"m value="+Data.getMes(data)+" size=2 maxlength=2>/<INPUT NAME="+name+"a  value="+Data.getAno(data)+" size=4 maxlength=4>";
	}

	return str;
	
}
// Retorna o texto que monta uma caixa de texto
// name = nome do objeto do formul�rio, pelo qual será recuperado o valor
// value = texto a ser preenchido
// size = tamanho da caixa de texto. Se for 0, o valor default � assumido.
// maxlength = quantidade de caracteres maximo. Se for 0, o valor default � assumido.

public String textboxNumero(String name, String value, int size, int maxlength) throws Exception {

	String str = "<INPUT NAME="+name+" VALUE=\""+value+"\"";

	if (size!=0) {
		str += " size="+size;
	}

	if (maxlength != 0) {
		str += " maxlength="+maxlength;
	}
	
	str += ">";
			
	return str;
	
}
// Retorna o texto que monta uma caixa de texto
// name = nome do objeto do formul�rio, pelo qual será recuperado o valor
// value = texto a ser preenchido
// size = tamanho da caixa de texto. Se for 0, o valor default � assumido.
// maxlength = quantidade de caracteres maximo. Se for 0, o valor default � assumido.

public String textboxPassword(String name, String value, int size, int maxlength) throws Exception {

	String str = "<INPUT TYPE=password NAME="+name+" VALUE=\""+value+"\"";

	if (size!=0) {
		str += " size="+size;
	}

	if (maxlength != 0) {
		str += " maxlength="+maxlength;
	}
	
	str += ">";
			
	return str;
	
}
// Retorna o texto que monta uma caixa de texto
// name = nome do objeto do formul�rio, pelo qual será recuperado o valor
// value = texto a ser preenchido
// size = tamanho da caixa de texto. Se for 0, o valor default � assumido.
// maxlength = quantidade de caracteres maximo. Se for 0, o valor default � assumido.

public String textboxTexto(String name, String value, int size, int maxlength) throws Exception {

	String str = "<INPUT NAME="+name+" VALUE=\""+value+"\"";

	if (size!=0) {
		str += " size="+size;
	}

	if (maxlength != 0) {
		str += " maxlength="+maxlength;
	}
	
	str += ">";
			
	return str;
	
}
public static Hashtable trataRequestEmCasoDeUpload (javax.servlet.http.HttpServletRequest request) throws Exception {

	// Cria Hashtable de resposta
	Hashtable resp = new Hashtable();
	
	// first check if the upload request coming in is a multipart request
	boolean isMultipart = FileUpload.isMultipartContent(request);

	if (!isMultipart) { 
		// request normal
		Enumeration parametros = request.getParameterNames();
		while (parametros.hasMoreElements()) {
			String paramName = (String) parametros.nextElement();
			resp.put(paramName,request.getParameter(paramName));
		}
	} else {
		// Multipart request

		// Create a new file upload handler
		DiskFileUpload upload = new DiskFileUpload();

		// Set upload parameters
		//upload.setSizeThreshold(yourMaxMemorySize);
		//upload.setSizeMax(yourMaxRequestSize);
		//upload.setRepositoryPath(temporaryPath);

		// Parse the request
		List items = upload.parseRequest(request);

		// Process the uploaded items
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
		    FileItem item = (FileItem) iter.next();

			// Process a regular form field
			if (item.isFormField()) {
			    String name = item.getFieldName();
			    String value = item.getString();

			    resp.put(name,value);
			}

			// Process a file uploaded
			if (!item.isFormField()) {
			    String fileName = item.getName();
			    String contentType = item.getContentType();
			    boolean isInMemory = item.isInMemory();
			    long sizeInBytes = item.getSize();

			    // Pega o nome do arquivo
			    if (fileName.lastIndexOf("\\") != -1) {
					fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
			    }
			    if (fileName.lastIndexOf("/") != -1) {
					fileName = fileName.substring(fileName.lastIndexOf("/")+1);
			    }
			    	
			    // Salva o arquivo no disco
			    if (!fileName.equals("")) {
				    String filePath = (new Configuracao_ger()).getElement("RepositorioArquivos")+"\\temp\\"+fileName;
				    File uploadedFile = new File(filePath);
				    item.write(uploadedFile);

				    // poe na lista de parametros
					resp.put(item.getFieldName(),filePath);
			    }
			}
		}
	}

	return resp;

}


}
