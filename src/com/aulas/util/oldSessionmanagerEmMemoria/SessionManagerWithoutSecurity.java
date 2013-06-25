package com.aulas.util.oldSessionmanagerEmMemoria;

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

import java.io.*;
import java.sql.*;
import java.util.*;

public class SessionManagerWithoutSecurity {
	private Vector names = new Vector();
	private Vector values = new Vector();
		

public SessionManagerWithoutSecurity(javax.servlet.http.HttpServletRequest request) throws Exception {

	String parametro = request.getParameter("SessionManagerParameter");

	if (parametro == null) return;
	
	while (!parametro.equals("")) {
		names.addElement(parametro.substring(0,parametro.indexOf("$")));
		parametro = parametro.substring(parametro.indexOf("$")+1);
		values.addElement(parametro.substring(0,parametro.indexOf("$")));
		parametro = parametro.substring(parametro.indexOf("$")+1);
	}
	
}
public void addElement (String name, String value) throws Exception {

	if (names.contains(name)) {
		removeElement(name);
	}
	
	names.addElement(name);
	values.addElement(value);
	
}
public String generateEncodedParameter () throws Exception {
	
	String str = "SessionManagerParameter=";

	for (int i = 0; i < names.size(); i++) {
		str += names.elementAt(i) + "$" + values.elementAt(i) + "$";
	}

	return str;
	
}
public String getElement (String name) throws Exception {
	
	int pos = names.indexOf(name);

	if (pos == -1) return null;
	
	return values.elementAt(pos).toString();
	
}
public void removeElement (String name) throws Exception {
	
	int pos = names.indexOf(name);

	if (pos == -1) return;
	
	names.removeElementAt(pos);
	values.removeElementAt(pos);
	
}
}
