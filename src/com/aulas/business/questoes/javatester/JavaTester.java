package com.aulas.business.questoes.javatester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import com.aulas.modelos.Servlet;
import com.aulas.util.StringConverter;

public class JavaTester {
    
    private String path;
    
    public JavaTester() {
        this.path = Servlet.instalationPath + "WEB-INF/sandbox/";
    }
    
    public void setPath(String path) {
        this.path = path;
    }

    public static void main(String[] args) {
        // USADO PARA DEPURAÇÃO
        String codigo = "class Dado{\nint sorteia (){\nreturn (int) (Math.random() * 6 + 1 );\n}\n}";
        String criterio = "Dado dado = new Dado();\nfor (int i = 0; i < 100; i++) {\n   int numeroSorteado = dado.sorteia();\n  assertEquals(numeroSorteado>=1,true);\n    assertEquals(numeroSorteado<=6,true);\n}\n\n// Testa se está viciado\nint[] valores = new int[7];\nfor (int i = 0; i < 6000; i++) {\n  valores[dado.sorteia()]++;\n}\n\nfor (int i = 1; i <= 6; i++) {\n   if ((valores[i] < 750) || (valores[i] > 1250)) {\n      String erro = \"Dado viciado. A distribuição de valores sorteados não está uniforme (tolerância de 25%). Valor 1 está saindo \"+Math.round(valores[1]/6000.00*100)+\"% das vezes.\" + \n            \"Valor 2 está saindo \"+Math.round(valores[2]/6000.00*100)+\"% das vezes.\" +\n            \"Valor 3 está saindo \"+Math.round(valores[3]/6000.00*100)+\"% das vezes.\" +\n            \"Valor 4 está saindo \"+Math.round(valores[4]/6000.00*100)+\"% das vezes.\" +\n            \"Valor 5 está saindo \"+Math.round(valores[5]/6000.00*100)+\"% das vezes.\" +\n            \"Valor 6 está saindo \"+Math.round(valores[6]/6000.00*100)+\"% das vezes.\";\n\n        throw new TestException(erro);     \n  }\n}\n";
        try {
            JavaTester javaTester = new JavaTester();
            javaTester.setPath("H:\\Workspace-Eclipse\\Aulas\\WebContent\\WEB-INF\\sandbox\\");
            System.out.println(javaTester.executeTest(codigo,criterio));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
	public synchronized String executeTest(String codigo, String criterio) throws Exception {
		// Path
		if (!(new File(path).exists())) throw new Exception("Diretório de execução não encontrado: "+path);
		
		// ----- Gera o arquivo do aluno ------
		// descobre o nome da classe
		int pos = codigo.indexOf("class ");
		if (pos == -1) return "não há classe definida.";
		pos = pos + "class ".length();
		while (pos < codigo.length() && codigo.charAt(pos) == ' ') pos++;
		int posFim = pos;
		while (posFim < codigo.length() && 
				(Character.isLetterOrDigit(codigo.charAt(posFim)) || codigo.charAt(posFim) == '_')) posFim++;
		if (posFim == pos) return "não há classe definida.";
		String nomeClasse = codigo.substring(pos, posFim);
		// Compila
		File toDelete;
		toDelete = new File (path+nomeClasse + ".java");
		toDelete.delete();
		toDelete = new File (path+nomeClasse + ".class");
		toDelete.delete();
		String resultCompilacao = Compilar.compilar(path, nomeClasse, codigo);
		if (!resultCompilacao.equals("")) {
			return "Falha ao compilar o código.\n"+resultCompilacao;
		}

		// Trata o codigo de teste
		criterio = StringConverter.replace(criterio, "\r", "");
		
		// Acrescenta parametro String no AssertEquals que não tiver
		pos = criterio.indexOf("assert");
		while (pos != -1) {
			// Encontra o primeiro parametro
			while (criterio.charAt(pos) != '(') pos++;
			pos++;
			posFim = pos;
			int qtdeParentesesAberto = 0;
			while (qtdeParentesesAberto != 0 || criterio.charAt(posFim) != ',') {
				if (criterio.charAt(posFim) == '(') qtdeParentesesAberto++;
				if (criterio.charAt(posFim) == ')') qtdeParentesesAberto--;
				if (qtdeParentesesAberto > 1000) return "Problema no código de teste. Notifique o professor. não foi possível identificar o primeiro parâmetro. Excesso de parênteses abertos.";
				if (posFim == criterio.length()-1) return "Problema no código de teste. Notifique o professor. não foi possível identificar o primeiro parâmetro. Fim do arquivo encontrado.";
				posFim++;
			}
			String parametro = criterio.substring(pos, posFim);
			// Encontrar o segundo parametro 
			qtdeParentesesAberto = 0;
			posFim++;
			while (qtdeParentesesAberto != 0 || (criterio.charAt(posFim) != ',' && criterio.charAt(posFim) != ')')) {
				if (criterio.charAt(posFim) == '(') qtdeParentesesAberto++;
				if (criterio.charAt(posFim) == ')') qtdeParentesesAberto--;
				if (qtdeParentesesAberto > 1000) return "Problema no código de teste. Notifique o professor. não foi possível identificar o último parâmetro. Excesso de parênteses abertos.";
				if (posFim == criterio.length()-1) return "Problema no código de teste. Notifique o professor. não foi possível identificar o último parâmetro. Fim do arquivo encontrado.";
				posFim++;
			}
			if (criterio.charAt(posFim) == ')') {
				// não tem o ultimo parametro
				criterio = criterio.substring(0, posFim) + ",\"" + StringConverter.replace(parametro, "\"", "\\\"") + "\"" + criterio.substring(posFim);
			}
			pos = criterio.indexOf("assert", pos); 
		}
		
		
		// Gera o arquivo de teste
		String testClass = "RealizaTesteNaClasseSubmetida";
		toDelete = new File(path+testClass + ".java");
		toDelete.delete();
		toDelete = new File (path+testClass + ".class");
		toDelete.delete();
		generateTestFile(path, testClass+".java", criterio);
		resultCompilacao = Compilar.compilar(path, testClass+".java");
		if (!resultCompilacao.equals("")) {
			if (resultCompilacao.indexOf("cannot find symbol") != -1) {
				if (resultCompilacao.indexOf("symbol  : class ") != -1) {
					String nome = extraiPalavraSeguinte(resultCompilacao, "symbol  : class");
					return "não foi encontrada a classe "+nome+".";
				} else if (resultCompilacao.indexOf("symbol  : method") != -1) {
					String nomeMetodo = extraiPalavraSeguinte(resultCompilacao, "symbol  : method");
					String nomeLocal = extraiPalavraSeguinte(resultCompilacao, "location: class ");
					return "não foi encontrado o método "+nomeMetodo+" na classe "+nomeLocal+".";
				}
			} else if (resultCompilacao.indexOf("cannot be applied to ") != -1) {
				String mensagem = extraiPalavraSeguinte(resultCompilacao,": ");
				return "Problema com parâmetros: the method "+mensagem;
			} else if (resultCompilacao.indexOf("incompatible types") != -1) {
				String found = extraiPalavraSeguinte(resultCompilacao, "found :");
				String required = extraiPalavraSeguinte(resultCompilacao, "required: ");
				return "Tipos de dados incompatíveis. Foi encontrado "+found+" e era requerido "+required+".";
			}
			return "Falha ao compilar o código de teste.\n"+resultCompilacao;
		}
		
		// Executa o teste
		RuntimeExecutor r = new RuntimeExecutor(3000);
		String resp = "";
		if (path.startsWith("/")) { // Linux
			resp = r.execute("java -cp "+path+" -Djava.security.manager "+testClass, null);
		} else { // Windows 
			resp = r.execute("java -cp \""+path+";\" -Djava.security.manager "+testClass, null); // Para teste stand alone o ; foi necessário
		}
		//System.out.println("java -cp \""+(new File(".")).getCanonicalPath()+"\" -Djava.security.manager "+testClass);
			
		//toDelete = new File(path+testClass + ".java");
		//toDelete.delete();
		//toDelete = new File (path+testClass + ".class");
		//toDelete.delete();
		toDelete = new File (path+nomeClasse + ".java");
		toDelete.delete();
		toDelete = new File (path+nomeClasse + ".class");
		toDelete.delete();
		toDelete = new File (path+"logCompilacao.txt");
		toDelete.delete();
		return resp;
	}

	private String extraiPalavraSeguinte(String texto, String inicio) {
		int tam = inicio.length();
		if (texto.indexOf(inicio) == -1) return null;
		int posIni = texto.indexOf(inicio) + tam;
		int posFim = texto.indexOf('\n', posIni);
		return texto.substring(posIni, posFim);
	}

	private void generateTestFile(String path, String className, String criterio) throws Exception {
		PrintWriter saida = new PrintWriter(new FileWriter(path+className));
		BufferedReader template;
		try {
			template = new BufferedReader(new FileReader(path+"TestTemplate.java"));
		} catch (Exception ex) {
			throw new Exception ("Arquivo TestTemplate.java não encontrado no diretório " + path);
		}
		String linha;
		while ((linha = template.readLine()) != null) {
			if (linha.indexOf("/* Insert code here */") != -1) {
				saida.append(criterio + "\n");
			} else {
				saida.append(linha + "\n");
			}
		}
		saida.close();
	}

}
