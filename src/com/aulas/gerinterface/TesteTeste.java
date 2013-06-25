package com.aulas.gerinterface;

import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.EuclideanDistance;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;

public class TesteTeste {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //creates the single metric to use - in this case the simple
        // Levenshtein is used, this is far from recomended as much better
        // metrics can be employed in most cases, please see the sourceforge
        // SimMetric forums for advice on the best metric to employ in
        // differing situations.
//        AbstractStringMetric metric = new Levenshtein();
//        AbstractStringMetric metric = new CosineSimilarity();
//        AbstractStringMetric metric = new EuclideanDistance();
       // AbstractStringMetric metric = new MongeElkan();
        
        String str1 = "Essa era uma casa muito engra�ada, não tinha porta não tinha nada.";
        String str2 = "Essa era uma   casa muito fgfsf , não tinha porta não tinha nada.";
        //String str2 = "Essa era uma casa engra�ada muito, não tinha porta não tinha nada.";

        //this single line performs the similarity test
        System.out.println((new Levenshtein()).getSimilarity(str1, str2));
        System.out.println((new CosineSimilarity()).getSimilarity(str1, str2));
        System.out.println((new EuclideanDistance()).getSimilarity(str1, str2));
        System.out.println((new MongeElkan()).getSimilarity(str1, str2));
    }

}
