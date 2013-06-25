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
 
import java.util.*;

public class Vetor {
	

public static Vector embaralha(Vector vetor) throws Exception {

	Vector entrada = (Vector) vetor.clone();
	Vector saida = new Vector();
	
	while (entrada.size() > 0) {
		Random r = new Random();
		int pos = Math.abs(r.nextInt() % entrada.size()); // sorteia uma posicao
		saida.addElement(entrada.elementAt(pos));
		entrada.removeElementAt(pos);
	}
		
	return saida;

	
}
}
