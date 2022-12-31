package net.reyadeyat.nlp.information.retrieval.algorithm;

import net.reyadeyat.nlp.information.retrieval.data.structure.Citation;
import net.reyadeyat.nlp.information.retrieval.data.structure.Sentence;
import net.reyadeyat.nlp.information.retrieval.data.structure.Word;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2022.12.31
 */
public interface InformationRetreivalAlgorithm {
    public Boolean isMatch();
    public ArrayList<ArrayList<Word>> getResultWordList();
    public ArrayList<Sentence> getResultSentenceList();
    public ArrayList<Citation> getCitationList(Connection connection) throws Exception;
}
