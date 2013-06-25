package com.aulas.gerinterface;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

import com.aulas.business.Aluno;
import com.aulas.business.Aluno_ger;
import com.aulas.business.Disciplina;
import com.aulas.business.Lista;
import com.aulas.business.ListaGerada;
import com.aulas.business.ListaGerada_ger;
import com.aulas.business.Lista_ger;
import com.aulas.business.Questao;
import com.aulas.business.Resposta;
import com.aulas.business.questoes.QuestaoJava;
import com.aulas.objinterface.Menu;
import com.aulas.objinterface.PaginaHTML;
import com.aulas.objinterface.TextoPadrao;
import com.aulas.util.StringConverter;
import com.aulas.util.sessionmanager.SessionManager;

public class DetectaPlagio {
    
    public static final int MAXRESULT = 35;
    public static final double threshold = 0.9;
    
    public static void detectaPlagio(SessionManager sessionManager, Disciplina disc, PrintWriter out) throws Exception {

        PaginaHTML pagina = new PaginaHTML (sessionManager,out,"Detecta plágio - "+StringConverter.concatenateWithoutRepetion("Disciplina",disc.getNome()));
        pagina.init();

        pagina.titulo ("Detecta plágio - "+StringConverter.concatenateWithoutRepetion("Disciplina",disc.getNome()));

        out.println("<script>function toggle(elemento) { var e = document.getElementById(elemento); if (e.style.display == 'table-row') {e.style.display = 'none';} else {e.style.display = 'table-row';}} </script>");
        
        Aluno_ger alunoger = new Aluno_ger();

        Lista_ger listager = new Lista_ger();
        ListaGerada_ger listageradager = new ListaGerada_ger();
        
        int countGeral = 0;
        
        Vector<Lista> listas = listager.getElements(disc);
        for (Lista lista : listas) {
            out.println("<H1>"+lista.getNome()+"<H1>");
            out.println("<table border=1>");
            Vector<ListaGerada> listasgeradas = listageradager.getElements(lista);
            List<ResultadoComparacao> resultados = new ArrayList<ResultadoComparacao>();
            
            for (int i = 0; i < listasgeradas.size()-1; i++) {
                // compara a lista com todas as outras
                for (int j = i+1; j < listasgeradas.size(); j++) {
                    ListaGerada lista1 = listasgeradas.elementAt(i);
                    ListaGerada lista2 = listasgeradas.elementAt(j);
                    resultados.add(compara(lista1, lista2));
                }
            }
            
            Collections.sort(resultados);
            
            int count = 0;
            for(ResultadoComparacao t : resultados) {
                if (t.similaridade != 0) {
                    out.println("<tr>");
                    out.println("<td><span class=Descricao>");
                    for (Aluno a : t.lista1.getAlunos()) {
                        out.println("<a href=\"OpcoesLista?toDo=opcoesLista&codAluno="+a.getCod()+"&codLista="+t.lista1.getLista().getCod()+"&\">"+a.getNome()+" ("+t.lista1.getNota()+")</a>");
                    }
                    out.println("</span></td>");
                    out.println("<td><span class=Descricao>");
                    for (Aluno a : t.lista2.getAlunos()) {
                        out.println("<a href=\"OpcoesLista?toDo=opcoesLista&codAluno="+a.getCod()+"&codLista="+t.lista2.getLista().getCod()+"&\">"+a.getNome()+" ("+t.lista2.getNota()+")</a>");
                    }
                    out.println("</span></td>");
                    out.println("<td><span class=Descricao>"+Math.round(t.similaridade*100)/100.0+"</span></td>");
                    out.println("<td><span class=Descricao><B><a href=\"javascript://\" onclick=\"toggle('linha"+countGeral+"')\">"+t.qtdQuestoesComuns+"/"+t.lista1.getQuestoes().size()+"</a></span></td>");
                    out.println("</tr>");
                    
                    out.println("<tr id=\"linha"+countGeral+"\" style=\"display: none;\"><td colspan=\"4\">");
                    imprimeRespostasIguais(t, out);
                    out.println("</td>");

                    count++;
                    countGeral++;
                    if (count >= MAXRESULT || t.similaridade < threshold) break;
                }
            }
            
            out.println("</table>");
            out.println("<BR><BR>");
            
            
        }

        pagina.saltaLinha();
        Menu menu2 = new Menu (pagina,"Outras Opções");
            menu2.addItem("Voltar para as opções da disciplina","GerenciaDisciplinas?toDo=opcoesDisciplina&"+sessionManager.generateEncodedParameter());
            menu2.addItem("Logout","Logout?"+sessionManager.generateEncodedParameter());
        menu2.fim();

        pagina.rodape(TextoPadrao.copyright());

        pagina.fim();

    }
    
    private static void imprimeRespostasIguais (ResultadoComparacao r, PrintWriter out) throws Exception {
        Vector<Questao> questoes = r.lista1.getQuestoes();
        for (Questao questao : questoes) {
            if (r.lista2.getQuestoes().contains(questao)) {
                Resposta r1 = r.lista1.getResposta(questao);
                Resposta r2 = r.lista2.getResposta(questao);
                if (r1 != null && r2 != null && !r1.getResposta().equals("") && !r2.getResposta().equals("")) {
                	boolean pre = questao.getTipo().equals(QuestaoJava.getIdTipoQuestao());
                	out.println("<table bordercolor=black border=1><tr><td colspan=2><span class=Descricao><b>"+questao.htmlParaImpressao()+"</b></span></td></tr>");
                    out.println("<tr><td width=50% valign=top><span class=Descricao>"+(pre ? "<pre>":"")+r1.getResposta().replaceAll("<", "&lt;")+(pre ? "</pre>":"")+"</span></td>");
                    out.println(    "<td width=50% valign=top><span class=Descricao>"+(pre ? "<pre>":"")+r2.getResposta().replaceAll("<", "&lt;")+(pre ? "</pre>":"")+"</span></td></tr></table>");
                }
            }
        }
    }

    private static ResultadoComparacao compara(ListaGerada lista1, ListaGerada lista2) throws Exception {
        double soma = 0;
        int contTotal = 0;
        
        Vector<Questao> questoes = lista1.getQuestoes();
        for (Questao questao : questoes) {
            if (lista2.getQuestoes().contains(questao)) {
                Resposta r1 = lista1.getResposta(questao);
                Resposta r2 = lista2.getResposta(questao);
                
              AbstractStringMetric metric = new Levenshtein();
             // AbstractStringMetric metric = new CosineSimilarity();
             // AbstractStringMetric metric = new EuclideanDistance();
             // AbstractStringMetric metric = new MongeElkan();
              
              
                if (r1 != null && r2 != null && !r1.getResposta().equals("") && !r2.getResposta().equals("")) {
                    soma += metric.getSimilarity(r1.getResposta(), r2.getResposta());
                    contTotal++;
                }

            }
        }
        
        double similaridade;
        if (contTotal == 0) similaridade = 0;
        else similaridade = soma / contTotal;
        
        return new ResultadoComparacao(lista1, lista2, similaridade, contTotal);
        
    }
}


// Classe auxiliar para guardar os resultados
class ResultadoComparacao implements Comparable {
    ListaGerada lista1;
    ListaGerada lista2;
    double similaridade;
    int qtdQuestoesComuns;
    
    ResultadoComparacao (ListaGerada lista1, ListaGerada lista2, double similaridade, int qtdQuestoesComuns) {
        this.lista1 = lista1;
        this.lista2 = lista2;
        this.similaridade = similaridade;
        this.qtdQuestoesComuns = qtdQuestoesComuns;
    }
    
    @Override
    public int compareTo(Object obj) {
        ResultadoComparacao t = (ResultadoComparacao) obj;
        if (similaridade < t.similaridade) return 1;
        if (similaridade > t.similaridade) return -1;
        return 0;
    }
}

