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

import java.io.*;
import java.sql.*;
import java.util.*;

public class BancoDados {
	
	public static void init() throws Exception {
		BancoDadosLog.init();
	}

	public static Connection abreConexao() throws Exception {

		// Inicializa a conexão com a base de dados
		// Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		// Connection dbCon = DriverManager.getConnection("jdbc:odbc:aulas", "",
		// "");

		Class.forName("com.mysql.jdbc.Driver");
		Connection dbCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/aulas?user=root&password=root");

		return dbCon;

	}

	public static String getProxId(Connection dbCon, String tableName, String nomeCampo) throws Exception {

		Statement aStmt = dbCon.createStatement();
		ResultSet aRs;
		String str;

		str = "SELECT Max(" + nomeCampo + ") AS maxId From " + tableName; // Pega
																			// Id
																			// maximo
		long maxId = 1;
		aRs = aStmt.executeQuery(str);
		if (aRs.next()) {
			maxId = aRs.getLong("maxId");
			maxId++;
		}

		aStmt.close();

		return Long.toString(maxId);

	}

	/**
	 * 
	 * Retorna um vetor contendo as informacoes do ResultSet
	 * 
	 */

	public static String[] retornaArrayString(ResultSet aRs, int numColuna) throws Exception {

		String a[] = null;
		int pos = 0;

		while (aRs.next()) {
			a[pos] = aRs.getString(1);
			pos++;
		}

		return a;

	}
}
