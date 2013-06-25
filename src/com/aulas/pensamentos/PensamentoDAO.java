package com.aulas.pensamentos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.aulas.util.BancoDados;

public class PensamentoDAO {
	public static ArrayList<Pensamento> pensamentos;
	
	public Pensamento getAPensamento() throws Exception {
		// Carrega para memoria
		if (pensamentos == null) {
			pensamentos = new ArrayList<Pensamento>();
			Connection dbCon = BancoDados.abreConexao();
			Statement aStmt = dbCon.createStatement();
			ResultSet aRs;
			String str;

			str = "SELECT * FROM Pensamentos";
			aRs = aStmt.executeQuery(str);
			while (aRs.next()) {
				Pensamento p = new Pensamento();
				p.setCod(aRs.getInt(1));
				p.setPensamento(aRs.getString(2));
				p.setAutor(aRs.getString(3));
				pensamentos.add(p);
			}
			
			aStmt.close();
			dbCon.close();
		}
		
		// Faz o sorteio
		int qtde = pensamentos.size();
		int sorteio = (int) Math.ceil(Math.random()*(qtde-1));
		return pensamentos.get(sorteio);

	}
}
