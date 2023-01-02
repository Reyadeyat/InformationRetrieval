/*
 * Copyright (C) 2022 Reyadeyat
 *
 * Reyadeyat/NLP is licensed under the
 * BSD 3-Clause "New" or "Revised" License
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://reyadeyat.net/NLP.LICENSE  
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.reyadeyat.nlp.information.retrieval.algorithm;

import net.reyadeyat.nlp.information.retrieval.InformationRetrieval;
import net.reyadeyat.nlp.information.retrieval.data.structure.Citation;
import net.reyadeyat.nlp.information.retrieval.data.structure.SearchingDocument;
import net.reyadeyat.nlp.information.retrieval.data.structure.Sentence;
import net.reyadeyat.nlp.information.retrieval.data.structure.Word;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2023.01.01
 */
public class Match implements InformationRetreivalAlgorithm {
    private Integer document_id;
    private Integer[] phrase_index;
    private String phrase_original;
    private ArrayList<Word> retrieved_word_list;
    private Integer words_before;
    private Integer words_after;
    private ArrayList<Integer> final_result;
    private SlidingWindowMatch sliding_window_match;
    private Boolean is_match;
    private ArrayList<Sentence> result_sentence_list;
    
    public Match(Integer document_id, Integer[] phrase_index, String phrase_original, ArrayList<Word> retrieved_word_list, Integer words_before, Integer words_after) {
        this.document_id = document_id;
        this.phrase_index = phrase_index;
        this.phrase_original = phrase_original;
        this.retrieved_word_list = retrieved_word_list;
        this.words_before = words_before;
        this.words_after = words_after;
        Integer[] text = new Integer[retrieved_word_list.size()];
        for (int i = 0; i < retrieved_word_list.size(); i++) {
            Word word = retrieved_word_list.get(i);
            text[i] = word.search_index;
        }
        result_sentence_list = new ArrayList<Sentence>();
        sliding_window_match = new SlidingWindowMatch(phrase_index, text);
        if (sliding_window_match.isMatch() == true) {
            final_result = sliding_window_match.getMatchResult();
            ArrayList<Word> result_word_list = new ArrayList<>();
            for (int i = final_result.size()-1; i >=0 ; i--) {
                Integer word_index = final_result.get(i);
                for (int x = word_index; x < word_index + phrase_index.length; x++) {
                    Word word = retrieved_word_list.get(x);
                    result_word_list.add(word);
                }
                Sentence sentence = new Sentence(result_word_list);
                if (sentence.isConsecutiveSentence() == false) {
                    final_result.remove(i);
                } else {
                    result_sentence_list.add(sentence);
                }
                result_word_list.clear();
            }
        }
        is_match = final_result == null ? false : (final_result.size() > 0);
    }
    
    public Boolean isMatch() {
        return is_match;
    }
    
    @Override
    public ArrayList<ArrayList<Word>> getResultWordList() {
        ArrayList<ArrayList<Word>> word_list_result = new ArrayList<ArrayList<Word>>();
        if (is_match == false) {
            return word_list_result;
        }
        for (int i = 0; i < final_result.size(); i++) {
            ArrayList<Word> word_list = new ArrayList<Word>();
            word_list_result.add(word_list);
            Integer index = final_result.get(i);
            for (int x = index; x < index + phrase_index.length; x++) {
                Word word = retrieved_word_list.get(x);
                word_list.add(word);
            }
        }
        return word_list_result;
    }
    
    @Override
    public ArrayList<Sentence> getResultSentenceList() {
        ArrayList<Sentence> result_sentence_list_copy = new ArrayList<Sentence>();
        if (is_match == false) {
            return result_sentence_list_copy;
        }
        result_sentence_list_copy.addAll(result_sentence_list);
        
        return result_sentence_list_copy;
    }
    
    @Override
    public ArrayList<Citation> getCitationList(Connection connection) throws Exception {
        ArrayList<Citation> citation_list = new ArrayList<Citation>();
        if (is_match == false) {
            return citation_list;
        }
        ArrayList<ArrayList<Word>> word_list_result = getResultWordList();
        for (int i = 0; i < word_list_result.size(); i++) {
            ArrayList<Word> word_list = word_list_result.get(i);
            Word start_word = word_list.get(0);
            Word end_word = word_list.get(word_list.size()-1);
            SearchingDocument document = InformationRetrieval.selectSearchingDocument(connection, start_word.document_id);
            Citation citation = new Citation(connection, phrase_original, document, start_word, end_word, words_before, words_after);
            citation_list.add(citation);
        }
        return citation_list;
    }
    
    @Override
    public String toString() {
        if (isMatch() == false) {
            return "No Match found";
        }
        return "Match found";
    }
}
