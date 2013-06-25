package com.aulas.util;

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

public class StringConverter {
	

public static String concatenateWithoutRepetion(String palavra, String str) throws Exception {

	if ((palavra == null) || palavra.equals("")) {
		return str;
	} 
	
	if ((str == null) || str.equals("")) {
		return palavra;
	} 

	if ((str.startsWith(inicialMinuscula(palavra))) || (str.startsWith(inicialMaiuscula(palavra)))) {
		return str;
	}

	return palavra+" "+str;
	
}
public static String convertSpaces(String aString) throws Exception {
	String returnStr = aString;
	StringChanger strChange = new StringChanger();

	if (returnStr == null)
		returnStr = "";

	else
		returnStr = strChange.changeSubstring(returnStr, " ", "%20");

	return returnStr;

}
/**
 * fromDataBaseNotation method
 *
 * changes the Html &; notation (used just to store in DB)
 * to special letters of an input string
 *
 * @return java.lang.String
 * @param aString java.lang.String	input string
 */
public static String fromDataBaseNotation(String aString) throws Exception {
	String returnStr = aString;
	StringChanger strChange = new StringChanger();

	if (returnStr == null) {
		returnStr = "";
	} 	else {
		returnStr = strChange.changeSubstring(returnStr, "&#39;", "'");
		returnStr = strChange.changeSubstring(returnStr, "&#63;", "?");
		returnStr = strChange.changeSubstring(returnStr, "&quot;", "\"");
		returnStr = strChange.changeSubstring(returnStr, "&#92;", "\\");
	}

	return returnStr;
}
/**
 * This method was created in VisualAge.
 * @return java.lang.String
 * @param aString java.lang.String
 */
public static String fromHtmlNotation(String aString) throws Exception {
	String returnStr = aString;
	StringChanger strChange = new StringChanger();

	if (returnStr == null)
		returnStr = "";

	else
	{
		returnStr = strChange.changeSubstring(returnStr, "&agrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&aacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&acirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&atilde;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&auml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&egrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&eacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&ecirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&euml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&igrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&iacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&icirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&iuml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&ograve;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&oacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&ocirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&otilde;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&ouml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&ugrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&uacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&ucirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&uuml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&#39;", "'");
		returnStr = strChange.changeSubstring(returnStr, "&#63;", "?");
		returnStr = strChange.changeSubstring(returnStr, "&Agrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Aacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Acirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Atilde;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Auml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Egrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Eacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Ecirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Euml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Igrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Iacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Icirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Iuml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Ograve;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Oacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Ocirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Otilde;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Ouml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Ugrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Uacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Ucirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Uuml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&ccedil;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Ccedil;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&#124;", "|");
		returnStr = strChange.changeSubstring(returnStr, "&#63;", "?");
		returnStr = strChange.changeSubstring(returnStr, "&quot;", "\"");
		returnStr = strChange.changeSubstring(returnStr, "&#39;", "'");
		returnStr = strChange.changeSubstring(returnStr, "<br>", "\n");
	}

	return returnStr;
}
/**
 * This method was created in VisualAge.
 * @return java.lang.String
 * @param aString java.lang.String
 */
public static String fromHtmlNotationWithoutLineBreak(String aString) throws Exception {
	String returnStr = aString;
	StringChanger strChange = new StringChanger();

	if (returnStr == null)
		returnStr = "";

	else
	{
		returnStr = strChange.changeSubstring(returnStr, "&agrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&aacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&acirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&atilde;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&auml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&egrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&eacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&ecirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&euml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&igrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&iacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&icirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&iuml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&ograve;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&oacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&ocirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&otilde;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&ouml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&ugrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&uacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&ucirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&uuml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&#39;", "'");
		returnStr = strChange.changeSubstring(returnStr, "&#63;", "?");
		returnStr = strChange.changeSubstring(returnStr, "&Agrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Aacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Acirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Atilde;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Auml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Egrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Eacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Ecirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Euml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Igrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Iacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Icirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Iuml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Ograve;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Oacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Ocirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Otilde;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Ouml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Ugrave;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Uacute;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Ucirc;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Uuml;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&ccedil;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&Ccedil;", "�");
		returnStr = strChange.changeSubstring(returnStr, "&#124;", "|");
		returnStr = strChange.changeSubstring(returnStr, "&#63;", "?");
		returnStr = strChange.changeSubstring(returnStr, "&quot;", "\"");
		returnStr = strChange.changeSubstring(returnStr, "&#39;", "'");
//		returnStr = strChange.changeSubstring(returnStr, "<br>", "\n");
	}

	return returnStr;
}
// Coloca a inicial de uma string em letra mai�scula

public static String inicialMaiuscula(String str) throws Exception {

	if (str == null) return null;

	if (str.equals("")) return "";

	return str.substring(0,1).toUpperCase() + str.substring(1);
	
}
// Coloca a inicial de uma string em letra minuscula

public static String inicialMinuscula(String str) throws Exception {

	if (str == null) return null;

	if (str.equals("")) return "";

	return str.substring(0,1).toLowerCase() + str.substring(1);
	
}


public static String removeAcentos(String aString) throws Exception {
	String returnStr = new String (aString);
	StringChanger strChange = new StringChanger();

	if (returnStr == null) {
		returnStr = "";
	} else {
		
		returnStr = strChange.changeSubstring(returnStr, "�", "a");
		returnStr = strChange.changeSubstring(returnStr, "�", "a");
		returnStr = strChange.changeSubstring(returnStr, "�", "a");
		returnStr = strChange.changeSubstring(returnStr, "�", "a");
		returnStr = strChange.changeSubstring(returnStr, "�", "a");
		returnStr = strChange.changeSubstring(returnStr, "�", "e");
		returnStr = strChange.changeSubstring(returnStr, "�", "e");
		returnStr = strChange.changeSubstring(returnStr, "�", "e");
		returnStr = strChange.changeSubstring(returnStr, "�", "e");
		returnStr = strChange.changeSubstring(returnStr, "�", "i");
		returnStr = strChange.changeSubstring(returnStr, "�", "i");
		returnStr = strChange.changeSubstring(returnStr, "�", "i");
		returnStr = strChange.changeSubstring(returnStr, "�", "i");
		returnStr = strChange.changeSubstring(returnStr, "�", "o");
		returnStr = strChange.changeSubstring(returnStr, "�", "o");
		returnStr = strChange.changeSubstring(returnStr, "�", "o");
		returnStr = strChange.changeSubstring(returnStr, "�", "o");
		returnStr = strChange.changeSubstring(returnStr, "�", "o");
		returnStr = strChange.changeSubstring(returnStr, "�", "u");
		returnStr = strChange.changeSubstring(returnStr, "�", "u");
		returnStr = strChange.changeSubstring(returnStr, "�", "u");
		returnStr = strChange.changeSubstring(returnStr, "�", "u");
		returnStr = strChange.changeSubstring(returnStr, "�", "A");
		returnStr = strChange.changeSubstring(returnStr, "�", "A");
		returnStr = strChange.changeSubstring(returnStr, "�", "A");
		returnStr = strChange.changeSubstring(returnStr, "�", "A");
		returnStr = strChange.changeSubstring(returnStr, "�", "A");
		returnStr = strChange.changeSubstring(returnStr, "�", "E");
		returnStr = strChange.changeSubstring(returnStr, "�", "E");
		returnStr = strChange.changeSubstring(returnStr, "�", "E");
		returnStr = strChange.changeSubstring(returnStr, "�", "E");
		returnStr = strChange.changeSubstring(returnStr, "�", "I");
		returnStr = strChange.changeSubstring(returnStr, "�", "I");
		returnStr = strChange.changeSubstring(returnStr, "�", "I");
		returnStr = strChange.changeSubstring(returnStr, "�", "I");
		returnStr = strChange.changeSubstring(returnStr, "�", "O");
		returnStr = strChange.changeSubstring(returnStr, "�", "O");
		returnStr = strChange.changeSubstring(returnStr, "�", "O");
		returnStr = strChange.changeSubstring(returnStr, "�", "O");
		returnStr = strChange.changeSubstring(returnStr, "�", "O");
		returnStr = strChange.changeSubstring(returnStr, "�", "U");
		returnStr = strChange.changeSubstring(returnStr, "�", "U");
		returnStr = strChange.changeSubstring(returnStr, "�", "U");
		returnStr = strChange.changeSubstring(returnStr, "�", "U");
		returnStr = strChange.changeSubstring(returnStr, "�", "c");
		returnStr = strChange.changeSubstring(returnStr, "�", "C");
	}

	return returnStr;
}
public static String removeUnecessarySpaces(String aString) throws Exception {

	String returnStr = aString;
	StringChanger strChange = new StringChanger();

	if (returnStr == null) return "";

	while (returnStr.indexOf("  ") != -1) {
		returnStr = strChange.changeSubstring(returnStr, "  ", " ");
	}

	while (returnStr.startsWith(" ")) {
		returnStr = returnStr.substring(1,returnStr.length());
	}

	while (returnStr.endsWith(" ")) {
		returnStr = returnStr.substring(0,returnStr.length()-1);
	}
	
	return returnStr;

}

public static String replace(String aString, String txtToFind, String txtToReplace) throws Exception {

	String returnStr = aString;
	StringChanger strChange = new StringChanger();

	if (returnStr == null) returnStr = "";

	returnStr = strChange.changeSubstring(returnStr, txtToFind,txtToReplace);

	return returnStr;
}
/**
 * toDataBaseNotation method
 *
 * changes the special letters of an input string
 * to Html &; notation (just to store in DB)
 *
 * @return java.lang.String
 * @param aString java.lang.String	input string
 */
public static String toDataBaseNotation(String aString) throws Exception {
	String returnStr = aString;
	StringChanger strChange = new StringChanger();

	if (returnStr == null) {
		returnStr = "";
	} else {
		char c[] = new char[1];
		c[0] = 147; // abre aspas do word
		returnStr = strChange.changeSubstring(returnStr, new String(c), "\"");
		c[0] = 148; // fecha aspas do word
		returnStr = strChange.changeSubstring(returnStr, new String(c), "\"");
		returnStr = strChange.changeSubstring(returnStr, "'", "&#39;");
		returnStr = strChange.changeSubstring(returnStr, "?", "&#63;");
		returnStr = strChange.changeSubstring(returnStr, "\"", "&quot;");
		returnStr = strChange.changeSubstring(returnStr, "\\", "&#92;");
	}

	return returnStr;
}
/**
 * toHtmlNotation method
 *
 * changes the stressed and the special letters of an input string
 * to Html &; notation
 *
 * @return java.lang.String
 * @param aString java.lang.String	input string
 */
public static String toHtmlNotation(String aString) throws Exception {
	String returnStr = aString;
	StringChanger strChange = new StringChanger();

	if (returnStr == null)
		returnStr = "";

	else
	{
		// Altera os amps existentes para nao confundir com os acrescentados
		returnStr = strChange.changeSubstring(returnStr, "&", "&amp;");
		// Troca os caracteres por seus correspondentes
		returnStr = strChange.changeSubstring(returnStr, "�", "&agrave;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&aacute;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&acirc;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&atilde;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&auml;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&egrave;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&eacute;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&ecirc;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&euml;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&igrave;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&iacute;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&icirc;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&iuml;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&ograve;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&oacute;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&ocirc;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&otilde;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&ouml;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&ugrave;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&uacute;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&ucirc;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&uuml;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Agrave;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Aacute;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Acirc;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Atilde;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Auml;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Egrave;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Eacute;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Ecirc;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Euml;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Igrave;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Iacute;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Icirc;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Iuml;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Ograve;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Oacute;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Ocirc;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Otilde;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Ouml;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Ugrave;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Uacute;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Ucirc;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Uuml;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&ccedil;");
		returnStr = strChange.changeSubstring(returnStr, "�", "&Ccedil;");
		returnStr = strChange.changeSubstring(returnStr, "'", "&#39;");
		returnStr = strChange.changeSubstring(returnStr, "?", "&#63;");
		returnStr = strChange.changeSubstring(returnStr, "|", "&#124;");
		returnStr = strChange.changeSubstring(returnStr, "\"", "&quot;");
		returnStr = strChange.changeSubstring(returnStr, "'", "&#39;");
		returnStr = strChange.changeSubstring(returnStr, "\r", "");
		returnStr = strChange.changeSubstring(returnStr, "<", "&lt;");
		returnStr = strChange.changeSubstring(returnStr, ">", "&gt;");
		// troca os caracteres por tags correspondentes
		returnStr = strChange.changeSubstring(returnStr, "\n", "<br>");
	}

	return returnStr;
}
/**
 * toHtmlNotation method
 *
 * changes the [\n] caracter to [\n" + "] string to avoid the "Unterminated string constant"
 * JavaScript error
 *
 * @return java.lang.String
 * @param aString java.lang.String	input string
 */
public static String toJavaScriptNotation(String aString) throws Exception {
	String returnStr = aString;
	StringChanger strChange = new StringChanger();

	if (returnStr == null)
		returnStr = "";
	else
	{
		returnStr = strChange.changeSubstring(returnStr, "\r", "");
		returnStr = strChange.changeSubstring(returnStr, "\n", "\\n");
		returnStr = strChange.changeSubstring(returnStr, "\"", "\\\"");
		returnStr = strChange.changeSubstring(returnStr, "'", "\\'");
	}

	return returnStr;
}
/**
 * toUnStressedNotation method
 *
 * changes the stressed, the special letters and the Html &; notation
 * to unstressed letters in an input string
 *
 * @return java.lang.String
 * @param aString java.lang.String	input string
 */
public static String toUnStressedNotation(String aString) throws Exception {
	String returnStr = aString;
	StringChanger strChange = new StringChanger();

	if (returnStr == null)
		returnStr = "";

	else
	{
		returnStr = strChange.changeSubstring(returnStr, "&agrave;", "a");
		returnStr = strChange.changeSubstring(returnStr, "�", "a");
		returnStr = strChange.changeSubstring(returnStr, "&aacute;", "a");
		returnStr = strChange.changeSubstring(returnStr, "�", "a");
		returnStr = strChange.changeSubstring(returnStr, "&acirc;",  "a");
		returnStr = strChange.changeSubstring(returnStr, "�",  "a");
		returnStr = strChange.changeSubstring(returnStr, "&atilde;", "a");
		returnStr = strChange.changeSubstring(returnStr, "�", "a");
		returnStr = strChange.changeSubstring(returnStr, "&auml;", "a");
		returnStr = strChange.changeSubstring(returnStr, "�", "a");
		returnStr = strChange.changeSubstring(returnStr, "&egrave;", "e");
		returnStr = strChange.changeSubstring(returnStr, "�", "e");
		returnStr = strChange.changeSubstring(returnStr, "&eacute;", "e");
		returnStr = strChange.changeSubstring(returnStr, "�", "e");
		returnStr = strChange.changeSubstring(returnStr, "&ecirc;", "e");
		returnStr = strChange.changeSubstring(returnStr, "�", "e");
		returnStr = strChange.changeSubstring(returnStr, "&euml;", "e");
		returnStr = strChange.changeSubstring(returnStr, "�", "e");
		returnStr = strChange.changeSubstring(returnStr, "&igrave;", "i");
		returnStr = strChange.changeSubstring(returnStr, "�", "i");
		returnStr = strChange.changeSubstring(returnStr, "&iacute;", "i");
		returnStr = strChange.changeSubstring(returnStr, "�", "i");
		returnStr = strChange.changeSubstring(returnStr, "&icirc;", "i");
		returnStr = strChange.changeSubstring(returnStr, "�", "i");
		returnStr = strChange.changeSubstring(returnStr, "&iuml;", "i");
		returnStr = strChange.changeSubstring(returnStr, "�", "i");
		returnStr = strChange.changeSubstring(returnStr, "&ograve;", "o");
		returnStr = strChange.changeSubstring(returnStr, "�", "o");
		returnStr = strChange.changeSubstring(returnStr, "&oacute;", "o");
		returnStr = strChange.changeSubstring(returnStr, "�", "o");
		returnStr = strChange.changeSubstring(returnStr, "&ocirc;", "o");
		returnStr = strChange.changeSubstring(returnStr, "�", "o");
		returnStr = strChange.changeSubstring(returnStr, "&otilde;", "o");
		returnStr = strChange.changeSubstring(returnStr, "�", "o");
		returnStr = strChange.changeSubstring(returnStr, "&ouml;", "o");
		returnStr = strChange.changeSubstring(returnStr, "�", "o");
		returnStr = strChange.changeSubstring(returnStr, "&ugrave;", "u");
		returnStr = strChange.changeSubstring(returnStr, "�", "u");
		returnStr = strChange.changeSubstring(returnStr, "&uacute;", "u");
		returnStr = strChange.changeSubstring(returnStr, "�", "u");
		returnStr = strChange.changeSubstring(returnStr, "&ucirc;", "u");
		returnStr = strChange.changeSubstring(returnStr, "�", "u");
		returnStr = strChange.changeSubstring(returnStr, "&uuml;", "u");
		returnStr = strChange.changeSubstring(returnStr, "�", "u");
		returnStr = strChange.changeSubstring(returnStr, "'", "");
		returnStr = strChange.changeSubstring(returnStr, "?", "");
		returnStr = strChange.changeSubstring(returnStr, "&Agrave;", "A");
		returnStr = strChange.changeSubstring(returnStr, "�", "A");
		returnStr = strChange.changeSubstring(returnStr, "&Aacute;", "A");
		returnStr = strChange.changeSubstring(returnStr, "�", "A");
		returnStr = strChange.changeSubstring(returnStr, "&Acirc;", "A");
		returnStr = strChange.changeSubstring(returnStr, "�", "A");
		returnStr = strChange.changeSubstring(returnStr, "&Atilde;", "A");
		returnStr = strChange.changeSubstring(returnStr, "�", "A");
		returnStr = strChange.changeSubstring(returnStr, "&Auml;", "A");
		returnStr = strChange.changeSubstring(returnStr, "�", "A");
		returnStr = strChange.changeSubstring(returnStr, "&Egrave;", "E");
		returnStr = strChange.changeSubstring(returnStr, "�", "E");
		returnStr = strChange.changeSubstring(returnStr, "&Eacute;", "E");
		returnStr = strChange.changeSubstring(returnStr, "�", "E");
		returnStr = strChange.changeSubstring(returnStr, "&Ecirc;", "E");
		returnStr = strChange.changeSubstring(returnStr, "�", "E");
		returnStr = strChange.changeSubstring(returnStr, "&Euml;", "E");
		returnStr = strChange.changeSubstring(returnStr, "�", "E");
		returnStr = strChange.changeSubstring(returnStr, "&Igrave;", "I");
		returnStr = strChange.changeSubstring(returnStr, "�", "I");
		returnStr = strChange.changeSubstring(returnStr, "&Iacute;", "I");
		returnStr = strChange.changeSubstring(returnStr, "�", "I");
		returnStr = strChange.changeSubstring(returnStr, "&Icirc;", "I");
		returnStr = strChange.changeSubstring(returnStr, "�", "I");
		returnStr = strChange.changeSubstring(returnStr, "&Iuml;", "I");
		returnStr = strChange.changeSubstring(returnStr, "�", "I");
		returnStr = strChange.changeSubstring(returnStr, "&Ograve;", "O");
		returnStr = strChange.changeSubstring(returnStr, "�", "O");
		returnStr = strChange.changeSubstring(returnStr, "&Oacute;", "O");
		returnStr = strChange.changeSubstring(returnStr, "�", "O");
		returnStr = strChange.changeSubstring(returnStr, "&Ocirc;", "O");
		returnStr = strChange.changeSubstring(returnStr, "�", "O");
		returnStr = strChange.changeSubstring(returnStr, "&Otilde;", "O");
		returnStr = strChange.changeSubstring(returnStr, "�", "O");
		returnStr = strChange.changeSubstring(returnStr, "&Ouml;", "O");
		returnStr = strChange.changeSubstring(returnStr, "�", "O");
		returnStr = strChange.changeSubstring(returnStr, "&Ugrave;", "U");
		returnStr = strChange.changeSubstring(returnStr, "�", "U");
		returnStr = strChange.changeSubstring(returnStr, "&Uacute;", "U");
		returnStr = strChange.changeSubstring(returnStr, "�", "U");
		returnStr = strChange.changeSubstring(returnStr, "&Ucirc;", "U");
		returnStr = strChange.changeSubstring(returnStr, "�", "U");
		returnStr = strChange.changeSubstring(returnStr, "&Uuml;", "U");
		returnStr = strChange.changeSubstring(returnStr, "�", "U");
		returnStr = strChange.changeSubstring(returnStr, "&ccedil;", "c");
		returnStr = strChange.changeSubstring(returnStr, "�", "c");
		returnStr = strChange.changeSubstring(returnStr, "&Ccedil;", "C");
		returnStr = strChange.changeSubstring(returnStr, "�", "C");
		returnStr = strChange.changeSubstring(returnStr, "&#124;", "|");
		returnStr = strChange.changeSubstring(returnStr, "&#63;", "?");
		returnStr = strChange.changeSubstring(returnStr, "&quot;","\"");
		returnStr = strChange.changeSubstring(returnStr, "&#39;","'");
		returnStr = strChange.changeSubstring(returnStr, "<br>","\n");
	}

	return returnStr;
}
}
