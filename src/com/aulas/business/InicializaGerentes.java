package com.aulas.business;

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


import com.aulas.util.*;

import java.sql.*;
import java.util.*;

public class InicializaGerentes {

	private static boolean sucesso = false;

protected synchronized static void inicializaGerentes() throws Exception {

	if (sucesso == true) return; // nao faz nada

	// ordem � importante pois na inicializacao de um pode ser necess�rio ter outros gerentes já inicializados
	BancoDados.init();
	
	Disciplina_ger.inicializa();
	Lista_ger.inicializa();			// Precisa do Disciplina_Ger
	Turma_ger.inicializa(); 		// Precisa do Disciplina_ger e do Lista_ger
	Aluno_ger.inicializa(); 		// Precisa do Turma_ger	
	Questao_ger.inicializa();		// Precisa do Disciplina_ger
	Agrupamento_ger.inicializa(); 	// Precisa do Questao_ger e Lista_ger
	Resposta_ger.inicializa(); 		// Precisa do Questao_ger
	ListaGerada_ger.inicializa(); 	// Precisa do Lista_Ger, Questao_ger,agrup_ger,Resposta_ger e Aluno_ger

	sucesso = true;
	
}
}
