package com.aulas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class BancoDadosFazImportacaoAccessToMySQL {

	public static Connection abreAccess() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection dbCon = DriverManager.getConnection("jdbc:odbc:aulas", "", "");
		return dbCon;
	}
	
	public static Connection abreMySQL() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		//Connection dbCon = DriverManager.getConnection("jdbc:mysql://localhost/aulas?user=root&password=root");
		Connection dbCon = DriverManager.getConnection("jdbc:mysql://eclipse.ime.usp.br:3306/aulas?user=aulas&password=senhapro");
		return dbCon;
	}
	
	public static void fazMigracao(String nomeTabela) throws Exception {
		System.out.println(nomeTabela);
		Connection mysql = abreMySQL();
		mysql.createStatement().executeUpdate("DELETE FROM "+nomeTabela);

		Statement access = abreAccess().createStatement();
		ResultSet aRs = access.executeQuery("SELECT * FROM "+nomeTabela);
		while (aRs.next()) {
			int colunas = aRs.getMetaData().getColumnCount();
			String sql = "INSERT INTO "+nomeTabela+" VALUES (";
			for (int i = 1; i <= colunas; i++) sql += "?, ";
			sql = sql.substring(0, sql.length()-2) + ")"; // tira a ultima virgual
			//System.out.println(sql);
			PreparedStatement pStmt = mysql.prepareStatement(sql);
			for (int i = 1; i <= colunas; i++) {
				Object obj = aRs.getObject(i);
				//System.out.println(obj);
				pStmt.setObject(i, obj, aRs.getMetaData().getColumnType(i));
			}
			pStmt.execute();
			pStmt.close();
		}
	}
	
	public static void main(String[] parametros) throws Exception {
		/*fazMigracao("Disciplina");
		fazMigracao("Turma");
		fazMigracao("Lista");
		fazMigracao("TurmaLista");
		fazMigracao("Agrupamento");
		fazMigracao("Questao");
		fazMigracao("AgrupamentoQuestao");
		fazMigracao("Resposta");
		fazMigracao("Aluno");
		*/
		fazMigracao("AlunoTurma");
		/*
		fazMigracao("ListaGerada");
		fazMigracao("ListaGeradaAluno");
		fazMigracao("ListaGeradaQuestao"); // Tive que criar na mao novamente por conta de violacao de chave
		fazMigracao("Configuracoes");
		fazMigracao("Pensamentos");*/
		System.out.println("Fim!");
	}
	
}
