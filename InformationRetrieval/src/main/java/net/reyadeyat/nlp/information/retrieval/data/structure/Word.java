package net.reyadeyat.nlp.information.retrieval.data.structure;

/**
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2022.12.31
 */
public class Word {
    public Integer document_id;
    public String raw_word;
    public String word;
    public Integer word_index;
    public Integer word_start;
    public Integer word_length;
    public Integer search_index;
    
    public Word(Integer document_id, String raw_word, String word, Integer word_index, Integer word_start, Integer word_length, Integer search_index) {
        this.document_id = document_id;
        this.raw_word = raw_word;
        this.word = word;
        this.word_index = word_index;
        this.word_start = word_start;
        this.word_length = word_length;
        this.search_index = search_index;
    }
    
    public Word(String raw_word, String word, Integer word_index, Integer word_start, Integer word_length) {
        this(-1, raw_word, word, word_index, word_start, word_length, -1);
    }
    
    @Override
    public String toString() {
        return "["+document_id+"-"+word_index+"]@"+word_start+"."+word_length + " - " + word;
    }
}
