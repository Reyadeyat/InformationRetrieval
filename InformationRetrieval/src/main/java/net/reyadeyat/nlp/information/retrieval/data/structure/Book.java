package net.reyadeyat.nlp.information.retrieval.data.structure;

/**
 *
 * @author Mohammad Nabil Mostafa
 * <a href="mailto:code@reyadeyat.net">code@reyadeyat.net</a>
 * 
 * @since 2022.12.31
 */
public class Book {
    public Integer book_id;
    public String book_name;
    public String book_metadata;
    public String book_lang;
    public Integer book_release_year;
    
    public Book(Integer book_id, String book_name, String book_metadata, String book_lang, Integer book_release_year) {
        this.book_id = book_id;
        this.book_name = book_name;
        this.book_metadata = book_metadata;
        this.book_lang = book_lang;
        this.book_release_year = book_release_year;
    }
}
