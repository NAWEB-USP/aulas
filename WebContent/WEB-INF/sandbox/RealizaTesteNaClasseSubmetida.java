
public class RealizaTesteNaClasseSubmetida {

	public static void assertEquals(int valor, int expected, String txt) throws Exception {
		if (expected != valor) {
			throw new TestException("Falha na execução.\n Para "+txt+" era esperado "+expected +", por�m retornou "+valor+".");
		}
	}

	public static void assertEquals(long valor, long expected, String txt) throws Exception {
		if (expected != valor) {
			throw new TestException("Falha na execução.\n Para "+txt+" era esperado "+expected +", por�m retornou "+valor+".");
		}
	}
	
	public static void assertEquals(float valor, float expected, String txt) throws Exception {
		if (arredonda(expected,5) != arredonda(valor,5)) {
			throw new TestException("Falha na execução.\n Para "+txt+" era esperado "+expected +", por�m retornou "+valor+".");
		}
	}
	
	public static void assertEquals(double valor, double expected, String txt) throws Exception {
		if (arredonda(expected,5) != arredonda(valor,5)) {
			throw new TestException("Falha na execução.\n Para "+txt+" era esperado "+expected +", por�m retornou "+valor+".");
		}
	}
	
	public static double arredonda(double valor, int precisao) {
		return Math.round(valor * Math.pow(10,precisao) / (1.0 * Math.pow(10,precisao)));
	}
	
	public static void assertEquals(boolean valor, boolean expected, String txt) throws Exception {
		if (expected != valor) {
			throw new TestException("Falha na execução.\n Para "+txt+" era esperado "+expected +", por�m retornou "+valor+".");
		}
	}
	
	public static void assertEquals(Object valor, Object expected, String txt) throws Exception {
		if (expected == null) {
			if (valor != null) {
				throw new TestException("Falha na execução.\n Para "+txt+" era esperado null, por�m retornou \""+valor.toString()+"\".");
			}
		} else {
			if (!expected.equals(valor)) {
				throw new TestException("Falha na execução.\n Para "+txt+" era esperado \""+expected.toString() +"\", por�m retornou \""+valor.toString()+"\".");
			}
		}
	}
	
	public static void assertBetween(int valor, int liminf, int limsup, String txt) throws Exception {
		if (valor < liminf || valor > limsup) {
			throw new TestException("Falha na execução.\n Para "+txt+" era esperado um valor entre "+liminf
               +" e "+limsup +", por�m retornou "+valor+".");
		}
	}
	
	public static void assertTrue (boolean valor, String txt) throws Exception {
		if (!valor) {
			throw new TestException("Falha na execução.\n Para "+txt+" era esperado true, por�m retornou false.");
		}
	}

	
	public static void main(String[] args) {
		try {
			
			// Realiza os testes
			assertEquals(0, 0, "0");
			
Dado dado = new Dado();
for (int i = 0; i < 100; i++) {
   int numeroSorteado = dado.sorteia();
  assertEquals(numeroSorteado>=1,true,"numeroSorteado>=1");
    assertEquals(numeroSorteado<=6,true,"numeroSorteado<=6");
}

// Testa se está viciado
int[] valores = new int[7];
for (int i = 0; i < 6000; i++) {
  valores[dado.sorteia()]++;
}

for (int i = 1; i <= 6; i++) {
   if ((valores[i] < 750) || (valores[i] > 1250)) {
      String erro = "Dado viciado. A distribuição de valores sorteados não está uniforme (toler�ncia de 25%). Valor 1 está saindo "+Math.round(valores[1]/6000.00*100)+"% das vezes." + 
            "Valor 2 está saindo "+Math.round(valores[2]/6000.00*100)+"% das vezes." +
            "Valor 3 está saindo "+Math.round(valores[3]/6000.00*100)+"% das vezes." +
            "Valor 4 está saindo "+Math.round(valores[4]/6000.00*100)+"% das vezes." +
            "Valor 5 está saindo "+Math.round(valores[5]/6000.00*100)+"% das vezes." +
            "Valor 6 está saindo "+Math.round(valores[6]/6000.00*100)+"% das vezes.";

        throw new TestException(erro);     
  }
}

			
			System.out.println("Executou corretamente. Parab&eacute;ns!"); // Este texto � usado para detectar respostas corretas.
			System.err.println("Executou corretamente. Parab&eacute;ns!"); // Este texto � usado para detectar respostas corretas.
			System.exit(0);

		} catch (TestException ex) {
			System.out.println(ex.getMessage());
			System.exit(1);
		} catch (Exception ex) {
			System.out.println("Ocorreu um erro!!! ");
			System.out.println("Erro: "+ ex.getMessage());
			System.out.println("Tipo: " + ex.getClass());
			System.out.println("Local: "+ ex.getStackTrace()[0]);
			System.exit(1);
			//ex.printStackTrace(System.out);
			//ex.printStackTrace();
		}

	}
}

class TestException extends Exception {

	public TestException(String string) {
		super(string);
	}

}

