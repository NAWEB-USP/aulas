package com.aulas.business;
/* ----------------------------------------------------------- *
 *	                    Marco Aurélio Gerosa                   *
 * ----------------------------------------------------------- *
 * -- Arquivo gerado automaticamente em 12/02/2003 17:29:30 -- *
 * -- Gerador versão 1.0                                       *
 * ----------------------------------------------------------- *

 Histórico de Versões
 --------------------
 versão	Autor		Modificação
 ------	-----		-----------
 1.0	Gerosa 		Geração automática da classe
 * 
 */


import com.aulas.util.*;

import java.sql.*;
import java.util.*;

public class Aluno_ger {

	static private Vector listaObj; // Campo lista de objetos gerenciados

/* Construtor do objeto - carrega todas as inst�ncias do objeto gerenciado do banco de dados para a mem�ria */

public Aluno_ger() throws Exception {
	InicializaGerentes.inicializaGerentes();
}

public synchronized Vector getAllElements() {

	return (Vector) listaObj.clone();

}

public synchronized Aluno getElement(String cod) throws Exception {

	return getElementByCod (cod);

}

public synchronized Aluno getElementByCod (String cod) throws Exception {

	Aluno obj;

	// percorre a lista procurando o id
	for (int i = 0; i < this.listaObj.size(); i++) {
		obj = (Aluno) this.listaObj.elementAt(i);
		if (cod.equals(obj.getCod())) {
			return obj;
		}
	}

	//throw new Exception ("Aluno com código " + cod + " não foi encontrado em mem�ria.");
	return null;
}

public synchronized Aluno getElementByEmail(String valor) throws Exception {

	Aluno elem;
	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Aluno) this.listaObj.elementAt(i);
		if (elem.getEmail().equals(valor)) {
			return elem;
		}
	}

	return null;

}
public synchronized Aluno getElementByLogin(String valor) throws Exception {

	Aluno elem;
	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Aluno) this.listaObj.elementAt(i);
		if (elem.getLogin().equalsIgnoreCase(valor)) {
			return elem;
		}
	}

	return null;

}

public synchronized Vector getElementsByNome(String valor) throws Exception {

	Vector elements = new Vector();
	Aluno elem;
	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Aluno) this.listaObj.elementAt(i);
		if (elem.getNome().equals(valor)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;

}
public synchronized void remove(Aluno obj) throws Exception {

	// PROCEDIMENTOS PR�-Remoção
	// Informa listas
	ListaGerada_ger listageradager = new ListaGerada_ger();
	listageradager.removeAlunoTodasListas(obj);


	// Remoção
	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	String str;

	str = "DELETE FROM Aluno WHERE cod=?";
	PreparedStatement pStmt = dbCon.prepareStatement(str);
	pStmt.setLong(1, Long.parseLong(obj.getCod()));
	pStmt.execute();
	

	str = "DELETE FROM AlunoTurma WHERE codAluno=?";
	pStmt = dbCon.prepareStatement(str);
	pStmt.setLong(1, Long.parseLong(obj.getCod()));
	pStmt.execute();

	dbCon.close();

	// ------- Remove da mem�ria -------
	this.listaObj.removeElement(obj);

	// PROCEDIMENTOS P�S-Remoção


}

public void altera(Aluno obj, String nome, String email, String numeroMatricula, String curso, String dataNascimento, String sexo, String login, String senha)  throws Exception {

	// ------- Testa consist�ncia dos dados -------
	String testeCons = testaConsistencia(obj,nome,email,numeroMatricula, curso, dataNascimento, sexo,login,senha);
	if (testeCons != null) throw new Exception("não foi possível alterar devido ao campo "+testeCons+"");

	// ------- Altera na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	nome = StringConverter.toDataBaseNotation(nome);
	
	str = "UPDATE Aluno SET nome='"+StringConverter.toDataBaseNotation(nome)+"', email='"+StringConverter.toDataBaseNotation(email)+"', numeroMatricula='"+StringConverter.toDataBaseNotation(numeroMatricula)+"', curso='"+StringConverter.toDataBaseNotation(curso)+"', dataNascimento='"+StringConverter.toDataBaseNotation(dataNascimento)+"', sexo='"+StringConverter.toDataBaseNotation(sexo)+"', login='"+StringConverter.toDataBaseNotation(login)+"', senha='"+StringConverter.toDataBaseNotation(senha)+"' WHERE cod="+obj.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// Altera dados do objeto
	if (((obj.getNome() == null) && (nome != null)) || ((obj.getNome() != null) && !obj.getNome().equals(nome))) {
		obj.setNome(nome);
	}
	if (((obj.getEmail() == null) && (email != null)) || ((obj.getEmail() != null) && !obj.getEmail().equals(email))) {
		obj.setEmail(email);
	}
	if (((obj.getLogin() == null) && (login != null)) || ((obj.getLogin() != null) && !obj.getLogin().equals(login))) {
		obj.setLogin(login);
	}
	if (((obj.getSenha() == null) && (senha != null)) || ((obj.getSenha() != null) && !obj.getSenha().equals(senha))) {
		obj.setSenha(senha);
	}
	if (((obj.getNumeroMatricula() == null) && (numeroMatricula != null)) || ((obj.getNumeroMatricula() != null) && !obj.getNumeroMatricula().equals(numeroMatricula))) {
		obj.setNumeroMatricula(numeroMatricula);
	}
	if (((obj.getCurso() == null) && (curso != null)) || ((obj.getCurso() != null) && !obj.getCurso().equals(curso))) {
		obj.setCurso(curso);
	}
	if (((obj.getDataNascimento() == null) && (dataNascimento != null)) || ((obj.getDataNascimento() != null) && !obj.getDataNascimento().equals(dataNascimento))) {
		obj.setDataNascimento(dataNascimento);
	}
	if (((obj.getSexo() == null) && (sexo != null)) || ((obj.getSexo() != null) && !obj.getSexo().equals(sexo))) {
		obj.setSexo(sexo);
	}

}

public void desmatriculaAlunoTurma(Aluno aluno, Turma turma) throws Exception {

	// Remove objetos dependentes
	ListaGerada_ger listaGeradaGer = new ListaGerada_ger();
	Enumeration listas = turma.getListas().elements();
	while (listas.hasMoreElements()) {
		Lista lista = (Lista) listas.nextElement();
		ListaGerada listaGerada = listaGeradaGer.getElement(lista,aluno);
		if (listaGerada != null) {
			listaGeradaGer.removeAlunoListaGerada(aluno,listaGerada);
		}
	}
	
	// ------- Atualiza banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM AlunoTurma WHERE codAluno="+aluno.getCod()+" AND CodTurma="+turma.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	
	dbStmt.close();
	dbCon.close();
	
	// atualiza memoria
	aluno.removeTurma(turma);


}

public synchronized Vector getElementsByCurso(String valor) throws Exception {

	Vector elements = new Vector();
	Aluno elem;
	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Aluno) this.listaObj.elementAt(i);
		if (elem.getCurso().equals(valor)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;

}

public synchronized Vector getElementsByTurma(Turma turma) throws Exception {

	Vector elements = new Vector();
	Aluno elem;
	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Aluno) this.listaObj.elementAt(i);
		if (elem.getTurmas().contains(turma)) {
			elements.addElement(elem);
		}
	}

	elements.trimToSize();
	return elements;

}

public int getNumAlunosPorTurma(Turma turma) throws Exception {

	int total = 0;
	Aluno elem;
	// percorre a lista
	for (int i = 0; i < this.listaObj.size(); i++) {
		elem = (Aluno) this.listaObj.elementAt(i);
		if (elem.getTurmas().contains(turma)) {
			total++;
		}
	}

	return total;

}

public synchronized Aluno inclui(String nome, String email, String numeroMatricula, String curso, String dataNascimento, String sexo, String login, String senha)  throws Exception {

	// ------- Testa consist�ncia dos dados -------
	String testeCons = testaConsistencia(null,nome, email, numeroMatricula, curso, dataNascimento, sexo, login, senha);
	if (testeCons != null) throw new Exception("não foi possível inserir devido ao campo "+testeCons+"");

	// ------- Insere na base de dados -------
	// Inicia a conexão com a base de dados
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	// Pega Id maximo
	long maxId = 1;
	str = "SELECT Max(cod) AS maxId From Aluno";
	BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);
	if (dbRs.next()) {
		maxId = dbRs.getLong("maxId");
		maxId++;
	}
	String id = Long.toString(maxId);

	nome = StringConverter.toDataBaseNotation(nome);
	
	// Insere o elemento na base de dados
	str = "INSERT INTO Aluno (cod, nome, email, numeroMatricula, curso, dataNascimento, sexo, login, senha)";
	str += " VALUES ("+id+",'"+nome+"'"+",'"+StringConverter.toDataBaseNotation(email)+"'"+",'"+StringConverter.toDataBaseNotation(numeroMatricula)+"','"+StringConverter.toDataBaseNotation(curso)+"','"+StringConverter.toDataBaseNotation(dataNascimento)+"','"+StringConverter.toDataBaseNotation(sexo)+"','"+StringConverter.toDataBaseNotation(login)+"'"+",'"+StringConverter.toDataBaseNotation(senha)+"'"+")";

	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);

	// Finaliza conexao
	dbStmt.close();
	dbCon.close();

	// ------- Insere na mem�ria -------
	// Cria um novo objeto
	Aluno obj = new Aluno(id,nome,email,numeroMatricula,curso,dataNascimento,sexo,login,senha, new Vector());

	// Insere o objeto na lista do gerente
	this.listaObj.addElement(obj);

	return obj;

}

public synchronized void matriculaAlunoTurma(Aluno aluno, Turma turma) throws Exception {

	// ------- Atualiza banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "INSERT INTO AlunoTurma (codAluno, codTurma) VALUES ("+aluno.getCod()+","+turma.getCod()+")";
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	
	dbStmt.close();
	dbCon.close();
	
	// atualiza memoria
	aluno.addTurma(turma);
	

}

public synchronized void removeTurmaTodosAlunos(Turma turma) throws Exception {

	// ------- Remove do banco -------
	Connection dbCon = BancoDados.abreConexao();
	Statement dbStmt = dbCon.createStatement();
	ResultSet dbRs;
	String str;

	str = "DELETE FROM AlunoTurma WHERE codTurma="+turma.getCod();
	BancoDadosLog.log(str);
	dbStmt.executeUpdate(str);
	
	dbStmt.close();
	dbCon.close();
	
	// --- percorre a lista, removendo da memoria ---
	for (int i = 0; i < this.listaObj.size(); i++) {
		Aluno obj = (Aluno) this.listaObj.elementAt(i);
		obj.removeTurma(turma);
	}

}

/* Verifica a consist�ncia dos dados antes de uma inclusão ou Alteração */
/* Para inclusão utilize o código identificador o objeto igual a null */
/* Retorna o nome do campo inconsistente ou null para tudo ok */

public synchronized String testaConsistencia(Aluno obj, String nome, String email, String numeroMatricula, String curso, String dataNascimento, String sexo, String login, String senha)  throws Exception {

	boolean inclusao = (obj==null);

	// Testa atributos obrigat�rios
	if (nome == null) return "nome";
	if (nome.equals("")) return "nome";
	if (email == null) return "email";
	if (email.equals("")) return "email";
	if (login == null) return "login";
	if (login.equals("")) return "login";
	if (senha == null) return "senha";
	if (senha.equals("")) return "senha";
	if (numeroMatricula == null) return "numeroMatricula";
	if (numeroMatricula.equals("")) return "numeroMatricula";
	if (curso == null) return "curso";
	if (curso.equals("")) return "curso";
	if (dataNascimento == null) return "dataNascimento";
	if (dataNascimento.equals("")) return "dataNascimento";
	if (sexo == null) return "sexo";
	if (sexo.equals("")) return "sexo";

	// Testa atributos exclusivos
	if (inclusao || !(obj.getEmail().equals(email))) {
		if (getElementByEmail(email) != null) return "email";
	}
	if (inclusao || !(obj.getLogin().equals(login))) {
		if (getElementByLogin(login) != null) return "login";
	}

	/* Coloque aqui o código restante do código referente ao teste de consist�ncia */

	return null;

}

protected synchronized static void inicializa() throws Exception {
	// Precisa do Turma_ger	

	if (listaObj == null) { // primeira utilização do gerente de objetos
		
		listaObj = new Vector();

		// Inicia a conexão com a base de dados
		Connection dbCon = BancoDados.abreConexao();
		Statement dbStmt = dbCon.createStatement();
		Statement dbStmt2 = dbCon.createStatement();
		ResultSet dbRs;

		Turma_ger turmager = new Turma_ger();

		// seleciona todos objetos
		String str = "SELECT * FROM Aluno";
		BancoDadosLog.log(str);
	dbRs  = dbStmt.executeQuery(str);

		while (dbRs.next()) {
			// Le dados da base
			String cod = dbRs.getString("cod");
			String nome = dbRs.getString("nome");
			String email = StringConverter.fromDataBaseNotation(dbRs.getString("email"));
			String login = StringConverter.fromDataBaseNotation(dbRs.getString("login"));
			String numeroMatricula = dbRs.getString("numeroMatricula");
			String sexo = dbRs.getString("sexo");
			String dataNascimento = dbRs.getString("dataNascimento");
			String curso = dbRs.getString("curso");
			String senha = StringConverter.fromDataBaseNotation(dbRs.getString("senha"));

			// Instancia o objeto
			Aluno obj = new Aluno (cod,nome,email,numeroMatricula, curso, dataNascimento, sexo,login,senha, null);

			// Le turmas
			str = "SELECT * FROM AlunoTurma WHERE CodAluno="+cod;
			ResultSet dbRs2 = dbStmt2.executeQuery(str);
			Vector turmas = new Vector();
			while (dbRs2.next()) {
				String codTurma = dbRs2.getString("codTurma");
				Turma turma = turmager.getElementByCod(codTurma);
				turmas.addElement(turma);
			}
			turmas.trimToSize();
			obj.setTurmas(turmas);


			// Coloca-o na lista de objetos
			listaObj.addElement(obj);
		}

		listaObj.trimToSize();

		// Finaliza conexao
		dbStmt.close();
		dbCon.close();

	}

}
}
