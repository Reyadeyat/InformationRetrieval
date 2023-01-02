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

package net.reyadeyat.nlp.information.retrieval.plagiarism;

import net.reyadeyat.nlp.information.retrieval.InformationRetrieval;
import net.reyadeyat.nlp.information.retrieval.algorithm.Match;
import net.reyadeyat.nlp.information.retrieval.data.structure.Word;
import java.sql.Connection;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

/**
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2023.01.01
 */
public class PlagiarismChecker {
    
    private String algorithm;
    private Integer words_before;
    private Integer words_after;
    private String phrase_original;
    private String phrase;
    
    private Duration duration;
    
    ArrayList<Match> match_list;
    
    public PlagiarismChecker(Connection connection, String algorithm, Integer words_before, Integer words_after, String phrase) throws Exception {
        this.algorithm = algorithm;
        this.words_before = words_before;
        this.words_after = words_after;
        this.phrase_original = phrase.toString();
        this.phrase = phrase;
        Instant start_instant = Instant.now();
        if (algorithm.equalsIgnoreCase("exact match")) {
            this.match(connection);
        } else {
            throw new Exception("Algorithm '"+algorithm+"' is not implemented!");
        }
        Instant end_instant = Instant.now();
        duration = Duration.between(start_instant, end_instant);
    }
    
    public Duration getDuration() {
        return duration;
    }
    
    private void match(Connection connection) throws Exception {
        phrase = InformationRetrieval.fixText(phrase);
        phrase = InformationRetrieval.cleanString(phrase);
        String[] word_list = phrase.split(" ");
        if (word_list.length < 2) {
            throw new Exception("Plagiarism Checker doesn't accept phrases less than 2 words!");
        }
        Integer[] phrase_index = new Integer[word_list.length];
        TreeMap<String, Integer> word_index_map = new TreeMap<>();
        for (int i = 0; i < word_list.length; i++) {
            String word_text = word_list[i];
            word_index_map.put(word_text, i);
        }
        for (int i = 0; i < word_list.length; i++) {
            String word_text = word_list[i];
            Integer search_index = word_index_map.get(word_text);
            phrase_index[i] = search_index;
        }
        ArrayList<Word> word_match_list = InformationRetrieval.selectWordMatchList(connection, word_list);
        word_match_list.sort(new Comparator<Word>() {
            @Override
            public int compare(Word word_1, Word word_2) {
                if (word_1 == word_2) {
                    return 0;
                }
                return word_1.document_id > word_2.document_id ? 1 : word_1.document_id < word_2.document_id ? -1 
                        : word_1.word_index > word_2.word_index ? 1 : word_1.word_index < word_2.word_index ? -1 : 0;
            }
            
        });
        Integer current_document_id = -1;
        match_list = new ArrayList<Match>();
        ArrayList<Word> retrieved_word_list = new ArrayList<Word>();
        for (int i = 0; i < word_match_list.size(); i++) {
            Word word = word_match_list.get(i);
            if (current_document_id.equals(word.document_id) == false) {
                if (retrieved_word_list.size() == 0) {
                    current_document_id = word.document_id;
                }
                if (current_document_id.equals(word.document_id) == false) {
                    Match match = new Match(current_document_id, phrase_index, phrase_original, retrieved_word_list, words_before, words_after);
                    if (match.isMatch() == true) {
                        match_list.add(match);
                    }
                    retrieved_word_list = new ArrayList<Word>();
                    current_document_id = word.document_id;
                }
            }
            retrieved_word_list.add(word);
        }
        if (retrieved_word_list.size() > 0) {
            Match match = new Match(current_document_id, phrase_index, phrase_original, retrieved_word_list, words_before, words_after);
            if (match.isMatch() == true) {
                match_list.add(match);
            }
        }
        /*match_list.sort(new Comparator<Match>() {
            @Override
            public int compare(Match match_1, Match match_2) {
                if (match_1 == match_2) {
                    return 0;
                }
                return match_1.score > match_2.score ? 1 : match_1.score < match_2.score ? -1 : 0;
            }
        });*/
    }
    
    public ArrayList<Match> getMatchList() throws Exception {
        return match_list;
    }
}
