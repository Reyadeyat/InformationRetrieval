package net.reyadeyat.nlp.information.retrieval.data.structure;

import java.util.ArrayList;

/**
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2022.12.31
 */
public class Sentence {
    private ArrayList<Word> word_list;
    private ArrayList<Integer> distance_list;
    private Integer distance_ratio;
    
    public Sentence(ArrayList<Word> word_list) {
        this.word_list = word_list;
        distance_list = new ArrayList<>();
        distance_list.add(0);
        distance_ratio = 1;
        for (int i = 1; i < word_list.size(); i++) {
            Word word_1 = word_list.get(i-1);
            Word word_2 = word_list.get(i);
            distance_list.add(word_2.word_index - word_1.word_index);
            distance_ratio += word_2.word_index - word_1.word_index;
        }
    }
    
    public Boolean isSentence() throws Exception {
        throw new Exception("Method not implemented yet!");
    }
    
    public Boolean isConsecutiveSentence() {
        return distance_ratio == word_list.size();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word_list.size(); i++) {
            Word word = word_list.get(i);
            sb.append(word.raw_word).append("~").append(distance_list.get(i)).append(" ");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
