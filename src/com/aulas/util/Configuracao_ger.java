package com.aulas.util;

/* ----------------------------------------------- *
 *				Marco Aurélio Gerosa               *
 * ----------------------------------------------- *

 Histórico de Versões
 --------------------
 versão	Autor		Modificação
 ------	-----		-----------
 1.0	Gerosa 		Criação da classe
 * 
 */

import java.io.*;
import java.sql.*;
import java.util.*;

public class Configuracao_ger {
	private static Vector names = null;
	private static Vector values = null;
		

public Configuracao_ger() throws Exception {

	if (names == null) { // primeira utilização do gerente de objetos
	synchronized (this) {
		
		names = new Vector();
		values = new Vector();

		// Inicia a conexão com a base de dados
		Connection dbCon = BancoDados.abreConexao();
		Statement dbStmt = dbCon.createStatement();
		ResultSet dbRs;

		// seleciona todos objetos
		String str = "SELECT * FROM Configuracoes";
		dbRs = dbStmt.executeQuery(str);

		while (dbRs.next()) {
			// Le dados da base
			String dado = dbRs.getString("Dado");
			String valor = dbRs.getString("Valor");

			// Coloca-o na lista de objetos
			names.addElement(dado);
			values.addElement(valor);
			
		}

		names.trimToSize();
		values.trimToSize();

		// Finaliza conexao
		dbStmt.close();
		dbCon.close();

	}
	}
	
}
public String getElement (String name) throws Exception {
	
	int pos = names.indexOf(name);

	if (pos == -1) return null;
	
	return values.elementAt(pos).toString();
	
}
}
