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

public class Debug {

public static void grava(double valor) {
	// converte para string antes de gravar
 	grava (String.valueOf(valor));
}
public static void grava(int valor) {
	// converte para string antes de gravar
 	grava (String.valueOf(valor));
}
public static void grava(Object obj) {
	// converte para string antes de gravar
 	grava (obj.toString());
}
/**
 *
 * Salva informacoes de debug na base de dados
 *
 */
 
public static void grava(String texto) {
 
	try {
		// Inicializa a conexão com a base de dados
		Connection dbCon = BancoDados.abreConexao();
		
		Statement dbStmt = dbCon.createStatement();

		// Converte texto para notacao da base de dados
		texto = StringConverter.toDataBaseNotation(texto);

		// Salva o texto
		String str = "INSERT INTO Debug (Texto) VALUES ('" + texto + "')";

		dbStmt.executeUpdate(str);
		dbStmt.close();
		dbCon.close();

	} 
	catch (Exception ex) {
		Erro.trataExcecao (ex, null, null, null);
	}

	
}
public static void grava(boolean valor) {
	// converte para string antes de gravar
 	if (valor) {
	 	grava ("true");
 	} else {
	 	grava ("false");
 	}
}
}
